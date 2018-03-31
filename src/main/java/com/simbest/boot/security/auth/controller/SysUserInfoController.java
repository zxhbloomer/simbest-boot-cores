/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.constants.ErrorCodeConstants;
import com.simbest.boot.repository.Condition;
import com.simbest.boot.security.auth.model.SysPermission;
import com.simbest.boot.security.auth.model.SysUserInfo;
import com.simbest.boot.security.auth.repository.SysDutyRepository;
import com.simbest.boot.security.auth.repository.SysOrgInfoFullRepository;
import com.simbest.boot.security.auth.repository.SysOrgRepository;
import com.simbest.boot.security.auth.repository.SysPermissionRepository;
import com.simbest.boot.security.auth.repository.SysUserInfoFullRepository;
import com.simbest.boot.security.auth.repository.SysUserInfoRepository;
import com.simbest.boot.security.auth.service.SysPermissionService;
import com.simbest.boot.security.auth.service.SysRoleService;
import com.simbest.boot.security.auth.service.SysUserInfoService;
import com.simbest.boot.util.exception.GlobalExceptionRegister;
import com.simbest.boot.util.security.SecurityUtils;
import com.simbest.boot.web.response.JsonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <strong>Title</strong> : 用户信息控制器<br>
 * <strong>Description</strong> : <br>
 * <strong>Create on</strong> : 2018/02/27<br>
 * <strong>Modify on</strong> : 2018/03/02<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@Api(description = "系统用户操作相关接口")
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserInfoController {

	@Autowired
	private SysUserInfoFullRepository sysUserInfoRepository;

    @Autowired
    private SysOrgInfoFullRepository sysOrgRepository;

    @Autowired
    private SysDutyRepository dutyRepository;

    @Autowired
    private SysPermissionRepository sysPermissionRepository;

	@Autowired
	private SysPermissionService permissionService;

	@Autowired
	private SessionRegistry sessionRegistry;

	@Autowired
	private PasswordEncoder passwordEncoder;


    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
	@PostMapping("listOnlineUsers")
	public JsonResponse listOnlineUsers() {
		List<Object> principals = sessionRegistry.getAllPrincipals();
		List<String> usersNamesList = Lists.newArrayList();
		usersNamesList.addAll(principals.stream().filter(principal -> principal instanceof SysUserInfo).map(principal
				-> ((SysUserInfo) principal).getUsername()).collect(Collectors.toList()));
		return JsonResponse.success(usersNamesList);
	}

    @PreAuthorize("hasPermission(#sysUserInfo, 'ADMIN')")
    @PostMapping(value = "getMyOrg")
    public JsonResponse getMyOrg(@RequestBody final SysUserInfo sysUserInfo) {
        return JsonResponse.success(SecurityUtils.getCurrentUserName());
    }

	@ApiOperation(value = "获取用户列表", notes = "通过此接口来获取用户列表")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", //
					required = true, example = "1"), //
			@ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
					required = true, example = "10"), //
			@ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", //
					paramType = "query"), //
			@ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", //
					paramType = "query") //
	})
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN','sys:security:user')")
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ModelAndView getList(@RequestParam(required = false, defaultValue = "1") int page, //
                                @RequestParam(required = false, defaultValue = "10") int size, //
                                @RequestParam(required = false) String direction, //
                                @RequestParam(required = false) String properties, //
                                @RequestParam(required = false, defaultValue = "") String username //
	) {

		// 获取分页规则
		Pageable pageable = sysUserInfoRepository.getPageable(page, size, direction, properties);

		// 获取查询条件
		Condition condition = new Condition();
		condition.like("trueName", "%" + username + "%");
		Specification<SysUserInfo> s = sysUserInfoRepository.getSpecification(condition);

		// 获取查询结果
		Page<SysUserInfo> pages = sysUserInfoRepository.findAll(s, pageable);

		// 构成返回信息
		Map<String, Object> searchD = new HashMap<>();
		searchD.put("username", username);

		Map<String, Object> map = new HashMap<>();
		map.put("totalSize", pages.getTotalElements());
		map.put("totalPage", pages.getTotalPages());
		map.put("list", pages.getContent());
		map.put("size", pages.getSize());
		map.put("page", page);
		map.put("title", "用户列表");
		map.put("searchD", searchD);

		return new ModelAndView("sys/user/list", "userModel", map);
	}

	@ApiOperation(value = "新建用户信息", notes = "通过此接口来新建用户信息")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN','sys:security:user')")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public JsonResponse create(@RequestBody final SysUserInfo sysUserInfo) {
		Map<String, Object> map = new HashMap<>();
		if (sysUserInfo == null) {
			return JsonResponse.builder() //
					.errcode(JsonResponse.UNKNOWN_ERROR_CODE) //
					.errmsg("新建失败！") //
					.build();
		}
		sysUserInfo.setCreator(SecurityUtils.getCurrentUserName());
		sysUserInfo.setCreatedTime(new Date());
		sysUserInfo.setModifier(SecurityUtils.getCurrentUserName());
		sysUserInfo.setModifiedTime(new Date());
		sysUserInfo.setPassword(passwordEncoder.encode(sysUserInfo.getPassword()));
		sysUserInfo.setAccountNonExpired(true);
		sysUserInfo.setAccountNonLocked(true);
		sysUserInfo.setCredentialsNonExpired(true);
		sysUserInfo.setEnabled(true);
		sysUserInfo.setRemoved(false);
	//	sysUserInfo.setSysOrg(SysOrg.builder().id(1).build());

        if(sysUserInfo.getOrgId() != null) {
            SysOrg sysOrg = sysOrgRepository.findById(sysUserInfo.getOrgId()).orElse(null);
            sysUserInfo.setSysOrg(sysOrg);
        }

		sysUserInfoRepository.save(sysUserInfo);
		Map<String, Long> data = Maps.newHashMap();
		data.put("id", sysUserInfo.getId());

		return JsonResponse.builder() //
				.errcode(JsonResponse.SUCCESS_CODE) //
				.errmsg("新建成功！") //
				.data(data).build();
	}

	@ApiOperation(value = "删除用户信息", notes = "通过此接口来删除用户信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@Transactional
	public JsonResponse delete(@PathVariable final long id) {
		JsonResponse res = JsonResponse.defaultSuccessResponse();
		try {
			sysUserInfoRepository.deleteById(id);
		} catch (Exception e) {
			res.setErrcode(JsonResponse.UNKNOWN_ERROR_CODE);
			if (e.getMessage().endsWith("exists!")) {
				// 删除失败，用户不存在！
				res.setErrmsg("删除失败，用户不存在！");
			} else {
				// 删除失败，删除时出现异常！
				res.setErrmsg("删除失败，删除时出现异常！");

				// 同时使用标记和下面的语句来触发事务回滚
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			log.error("用户删除失败，" + e.getMessage(), e);
		}

		res.setErrmsg("删除成功！");
		return res;
	}


    /**
     * @param ids    ids
     * @param model model
     * @return ModelAndView
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @RequestMapping(value = "/deleteAll",method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse deleteAll(@RequestParam String ids, Model model) {
        String[] s  = ids.split(",");
        for(String t : s){
            Long id =  Long.parseLong(t);
            sysUserInfoRepository.deleteById(id);
        }
        return JsonResponse.builder().errcode(JsonResponse.SUCCESS_CODE).errmsg(JsonResponse.SUCCESS_MSG).build();
    }






	@ApiOperation(value = "修改用户信息", notes = "通过此接口来修改用户信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定用户权限才能操作方法
	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public JsonResponse update(@PathVariable final long id, @RequestBody final SysUserInfo sysUserInfo) {
		SysUserInfo sysUserInfoOld = sysUserInfoRepository.findById(id).orElse(null);

		if (sysUserInfo == null || sysUserInfoOld == null) {
			return null;
		}

		// BeanUtils.copyProperties(sysUserInfo, sysUserInfoOld, "id","accountNonExpired","accountNonLocked",
		// "authorities","creator","createdTime","password","credentialsNonExpired");
		sysUserInfoOld.setModifier(SecurityUtils.getCurrentUserName());
		sysUserInfoOld.setModifiedTime(new Date());
		sysUserInfoOld.setUsername(sysUserInfo.getUsername());
		sysUserInfoOld.setTrueName(sysUserInfo.getTrueName());
		sysUserInfoOld.setNickName(sysUserInfo.getNickName());
		sysUserInfoOld.setUserCode(sysUserInfo.getUserCode());
		sysUserInfoOld.setUserLevel(sysUserInfo.getUserLevel());
		sysUserInfoOld.setMajorPosition(sysUserInfo.getMajorPosition());
		sysUserInfoOld.setMajorPhone(sysUserInfo.getMajorPhone());
		sysUserInfoOld.setMajorTel(sysUserInfo.getMajorTel());
		sysUserInfoOld.setMajorEmail(sysUserInfo.getMajorEmail());
		sysUserInfoOld.setOrderBy(sysUserInfo.getOrderBy());
		sysUserInfoOld.setHeadUrl(sysUserInfo.getHeadUrl());

		if(sysUserInfo.getOrgId() != null) {
			SysOrg sysOrg = sysOrgRepository.findById(sysUserInfo.getOrgId()).orElse(null);
			sysUserInfoOld.setSysOrg(sysOrg);
		}

		sysUserInfoRepository.save(sysUserInfoOld);
		Map<String, Long> data = Maps.newHashMap();
		data.put("id", sysUserInfo.getId());

		return JsonResponse.builder() //
				.errcode(JsonResponse.SUCCESS_CODE) //
				.errmsg("更新成功！") //
				.data(data).build();
	}

	@ApiOperation(value = "查询用户信息", notes = "通过此接口来查询用户信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/query/{id}", method = RequestMethod.GET)
	public ModelAndView query(@PathVariable final long id) {
		SysUserInfo sysUserInfo = sysUserInfoRepository.findById(id).orElse(new SysUserInfo());

		Map<String, String> inputSelect1 = new HashMap<>();
		inputSelect1.put("0", "是");
		inputSelect1.put("1", "否");

		Map<String, String> inputSelect2 = new HashMap<>();
		inputSelect2.put("id", "1");
		inputSelect2.put("name", "123");

		List<Map<String, String>> inputSelect = new LinkedList<>();
		inputSelect.add(inputSelect1);
		inputSelect.add(inputSelect2);

		Map<String, String> inqList = new HashMap<>();
		inqList.put("0", "是");
		inqList.put("1", "否");

		// 获取查询结果
		List<SysOrg> sysOrgPage = sysOrgRepository.findAll();

		Map<String, Object> map = new HashMap<>();
		map.put("user", sysUserInfo);
		map.put("orgList", sysOrgPage);
		map.put("orgSize", sysOrgPage.size());
		map.put("inqList", inqList);
		map.put("inputSelect", inputSelect);
		return new ModelAndView("sys/user/form", "userModel", map);
	}

	@ApiOperation(value = "获取组织列表", notes = "通过此接口来获取组织列表")
	@ApiImplicitParams({ //
			@ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", //
					required = true, example = "1"), //
			@ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
					required = true, example = "10"), //
	})
	@RequestMapping(value = "/list/orgId", method = RequestMethod.GET, produces = "application/json")
	public JsonResponse getListByOrgId(@RequestParam(required = false, defaultValue = "1") int page, //
                                       @RequestParam(required = false, defaultValue = "10") int size, //
                                       @RequestParam(required = false, defaultValue = "-1") int orgId, //
                                       @RequestParam(required = false, defaultValue = "") String username //
	) {
		// 获取分页规则
		Pageable pageable = sysUserInfoRepository.getPageable(page, size, null, null);

		// 获取查询条件
		Condition condition = new Condition();
		condition.eq("sysOrg", orgId);
		condition.like("trueName", "%" + username + "%");
		Specification<SysUserInfo> s = sysUserInfoRepository.getSpecification(condition);

		// 获取查询结果
		Page<SysUserInfo> pages = sysUserInfoRepository.findAll(s, pageable);

		// 构成返回信息
		Map<String, Object> searchD = new HashMap<>();
		searchD.put("orgId", orgId);
		searchD.put("username", username);

		Map<String, Object> map = new HashMap<>();
		map.put("totalSize", pages.getTotalElements());
		map.put("totalPage", pages.getTotalPages());
		map.put("list", pages.getContent());
		map.put("size", pages.getSize());
		map.put("page", page);
		map.put("searchD", searchD);

		return JsonResponse.builder() //
				.errcode(JsonResponse.SUCCESS_CODE) //
				.errmsg("查询成功！") //
				.data(map).build();
	}

	/**
	 * 用户管理-用户授权 跳转连接
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "用户管理-用户授权 跳转连接", notes = "用户管理-用户授权 跳转连接")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/authorize", method = RequestMethod.GET)
	public ModelAndView authorize(@RequestParam final long id) {
		SysUserInfo sysUserInfo = sysUserInfoRepository.findById(id).orElse(new SysUserInfo());
		Set<SysPermission> sets=sysUserInfo.getPermissions();

		// 获取查询结果
		List<SysPermission> sysPermissionPage = permissionService.findAll();
		List<Map<String,Object>> pList=new ArrayList<Map<String,Object>>();

		for (SysPermission p:sysPermissionPage){
			Map<String,Object> pMap=new HashMap<String,Object>();
			pMap.put("id",p.getId());
			if(p.getParent()==null){
				pMap.put("parentId",null);
			}else{
				pMap.put("parentId",p.getParent().getId());
			}
			pMap.put("description",p.getDescription());
			if(sets.contains(p)){
				pMap.put("checked",true);
			}else {
				pMap.put("checked",false);
			}
			pList.add(pMap);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("user", sysUserInfo);
		map.put("pList", pList);
		map.put("pSize", pList.size());
		return new ModelAndView("sys/user/authorization", "dataModel", map);
	}

	@ApiOperation(value = "新建用户特殊权限", notes = "通过此接口来新建用户特殊权限")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN','sys:security:user')")
	@RequestMapping(value = "/createPermission", method = RequestMethod.POST, produces = "application/json")
	public JsonResponse createPermission(@RequestBody Map<String,Object> params) {
		long id=new Long(params.get("id").toString());
		String ids="";
		if(params.get("ids")!=null){
			ids=params.get("ids").toString();
		}

		SysUserInfo sysUserInfo = sysUserInfoRepository.findById(id).orElse(new SysUserInfo());
		Map<String, Object> map = new HashMap<>();
		if (sysUserInfo == null) {
			return JsonResponse.builder() //
					.errcode(JsonResponse.UNKNOWN_ERROR_CODE) //
					.errmsg("保存失败！") //
					.build();
		}

		Set<SysPermission> set= sysUserInfo.getPermissions();
		set.clear();

		if(!StringUtils.isEmpty(ids)){
			for (String l:ids.split(",")){
				SysPermission p=new SysPermission();
				p.setId(Integer.parseInt(l));
				set.add(p);
			}
		}
		sysUserInfo.setPermissions(set);
		sysUserInfoRepository.save(sysUserInfo);
		Map<String, Long> data = Maps.newHashMap();
		data.put("id", sysUserInfo.getId());

		return JsonResponse.builder() //
				.errcode(JsonResponse.SUCCESS_CODE) //
				.errmsg("保存成功！") //
				.data(data).build();
	}


	/**
	 * 用户管理-用户授权 跳转连接
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "用户管理-角色授权 跳转连接", notes = "用户管理-角色授权 跳转连接")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long", paramType = "path")
	@RequestMapping(value = "/roleAuthorization", method = RequestMethod.GET)
	public ModelAndView roleAuthorization(@RequestParam final long id) {
		SysUserInfo sysUserInfo = sysUserInfoRepository.findById(id).orElse(new SysUserInfo());
		Set<SysRole> sets=sysUserInfo.getRoles();

		// 获取查询结果
		List<SysRole> page = roleService.findAll();
		page.removeAll(sets);

		Map<String, Object> map = new HashMap<>();
		map.put("user", sysUserInfo);
		map.put("all", page);
		map.put("userRole", sets);
		return new ModelAndView("sys/user/roleAuthorization", "userModel", map);
	}

	@ApiOperation(value = "新建用户角色", notes = "通过此接口来新建用户角色")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN','sys:security:user')")
	@RequestMapping(value = "/createRole", method = RequestMethod.POST, produces = "application/json")
	public JsonResponse createRole(@RequestBody Map<String,Object> params) {
		long id=new Long(params.get("id").toString());
		String ids=params.get("ids").toString();

		SysUserInfo sysUserInfo = sysUserInfoRepository.findById(id).orElse(new SysUserInfo());
		Map<String, Object> map = new HashMap<>();
		if (sysUserInfo == null) {
			return JsonResponse.builder() //
					.errcode(JsonResponse.UNKNOWN_ERROR_CODE) //
					.errmsg("保存失败！") //
					.build();
		}

		Set<SysRole> set= sysUserInfo.getRoles();
		set.clear();

		for (String l:ids.split(",")){
			SysRole p=new SysRole();
			p.setId(Integer.parseInt(l));
			set.add(p);
		}
		sysUserInfo.setRoles(set);
		sysUserInfoRepository.save(sysUserInfo);
		Map<String, Long> data = Maps.newHashMap();
		data.put("id", sysUserInfo.getId());

		return JsonResponse.builder() //
				.errcode(JsonResponse.SUCCESS_CODE) //
				.errmsg("保存成功！") //
				.data(data).build();
	}

	/**
	 * 用户角色授权 跳转连接
	 * @return
	 */
	@ApiOperation(value = "用户角色授权 跳转连接", notes = "用户角色授权 跳转连接")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
	@RequestMapping(value = "/userRole", method = RequestMethod.GET)
	public ModelAndView userRole() {
		List<SysRole> page = roleService.findAll();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "用户角色授权");
		map.put("tree", page);
		return new ModelAndView("sys/user/usersAuthorization", "userModel", map);
	}

	@ApiOperation(value = "保存用户角色授权", notes = "通过此接口来保存用户角色授权")
	@PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN','sys:security:user')")
	@RequestMapping(value = "/createUsersRole", method = RequestMethod.POST, produces = "application/json")
	public JsonResponse createUsersRole(@RequestBody Map<String,Object> params) {
		String userIds="";
		if(params.get("userIds")==null){
			return JsonResponse.builder() //
					.errcode(JsonResponse.UNKNOWN_ERROR_CODE) //
					.errmsg("保存失败！") //
					.build();
		}
		userIds=params.get("userIds").toString();

		String roleIds="";
		if(params.get("roleIds")!=null){
			roleIds=params.get("roleIds").toString();
		}
		String[] roleId=roleIds.split(",");

		for(String userId:userIds.split(",")){
			SysUserInfo sysUserInfo = sysUserInfoRepository.findById(new Long(userId)).orElse(new SysUserInfo());
			Map<String, Object> map = new HashMap<>();
			if (sysUserInfo == null) {
				continue;
			}

			Set<SysRole> set= sysUserInfo.getRoles();
			set.clear();

			for (String l:roleId){
				SysRole p=new SysRole();
				p.setId(Integer.parseInt(l));
				set.add(p);
			}
			sysUserInfo.setRoles(set);
			sysUserInfoRepository.save(sysUserInfo);

		}

		return JsonResponse.builder() //
				.errcode(JsonResponse.SUCCESS_CODE) //
				.errmsg("保存成功！") //
				.build();
	}
}
