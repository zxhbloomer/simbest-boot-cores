/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.model;

import com.simbest.boot.base.annotations.EntityCnName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * <strong>Description : 满足《用户主数据管理规范V1.0》和微信互联网应用的组织信息</strong><br>
 * <strong>Create on : 2017年08月23日</strong><br>
 * <strong>Modify on : 2017年11月08日</strong><br>
 * <strong>Copyright Beijing Simbest Technology Ltd.</strong><br>
 *
 * @author lishuyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityCnName(name = "系统组织信息")
public class SysOrgInfoFull extends SysOrgInfo {

    private String description; //定义组织的描述。例如：“网络管理中心……”

    private String function; //组织的业务职责码

    private String supervisor; //组织的上级主管领导的UID

    private String orgmanager; //组织的主负责人的UID

    private String viceManager; //组织的辅负责人的UID

    private String admin; //组织的管理员的UID

    private String manageOrgID; //负责本组织管理职能且和本组织平级的组织编码

    private String postalAddress; //组织的地址

    private String postalCode; //组织的邮政编码

    private String telephoneNumber; //组织的电话号码

    private String fax; //传真号码

    private String startTime; //开始生效时间

    private String endTime; //结束生效时间

    private String erpID; //此部门在ERP-HR中对应的组织编码

    private String l; //组织所属地市名称




}
