package com.simbest.boot.sys.service;

import com.simbest.boot.base.service.IGenericService;
import com.simbest.boot.sys.model.SysLogLogin;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用途：登录日志管理逻辑层
 * 作者: lishuyi
 * 时间: 2018/2/23  10:14
 */
public interface ISysLogLoginService extends IGenericService<SysLogLogin, String> {

    List<Map<String, Object>> countLogin(Map<String, ?> paramMap);


}
