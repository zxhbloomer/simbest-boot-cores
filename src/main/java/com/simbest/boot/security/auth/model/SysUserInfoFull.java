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
 * <strong>Description : 满足《用户主数据管理规范V1.0》和微信互联网应用的用户信息</strong><br>
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
@EntityCnName(name = "用户基础信息")
public class SysUserInfoFull extends SysUserInfo {

    private String sn; //定义用户中文姓。

    private String employeeNumber; //定义员工号（与人力资源系统中的员工号一致），例如：“00120211”

    private String level; //【1-19】级岗位，职级。可选值参见附录员工职级的数据字典定义，例如：“12”表示12级。

    private String levelName; //岗位名称。如：IT规划，系统管理员等。

    private String category; //定义员工套入职级，见注1。例如：“12”表示12级。

    private String function; //定义用户的业务职责编码，可为多值，参照附录业务职责编码表。 例如：“财务”

    private String entryTime; //定义用户入职日期，时间格式参见附录。

    private String workOrg; //定义用户工作组织的编码号，编码定义参见附录中的组织编码规则。例如：“00010002000300040005”

    private String dputyDuty; //定义用户的职务编码，引用“职务”对象。例如：“GD00000001”。在一人多职的情况下，可作为副职务使用

    /**
     * 1-集团一级正、副职 领导
     * 2-集团二级正、副职领导和省公司一级正、副职领导
     * 3-集团三级正、副职领导，省公司二级正、副职领导，市公司一级正、副职领导
     * 4-集团普通员工，省公司三级正、副经理，市公司二级正、副经理，县公司一级正、副经理
     * 5-省公司普通员工，市公司三级正、副经理，县公司二级正、副经理
     * 6-市、县公司普通员工
     * 7-一般员工
     */
    private Integer positionLevel;

    private String employeeType; //定义用户类型，可选值参见附录员工类型的数据字典定义。

    private String l; //定义用户所属地市名称，如“青岛”。省（自治区/直辖市）公司用户填写“本部”。集团总部用户填写“总部”。

    private String description; //定义用户描述。

    private String email; //定义用户的邮件地址。

    private String nation; //民族，可选值参见附录中民族的数据字典定义。

    private String gender; //定义用户性别(男/女)

    private String birthday; //定义用户出生日期，时间格式参见附录。

    private String c; //定义用户国籍。

    private String religion; //定义用户政治面貌，可选值参见附录政治面貌的数据字典定义。

    private String telephoneNumber; //定义用户办公电话

    private String preferredMobile; //首选移动电话

    private String postalAddress; //定义用户通讯地址，同组织通信地址。

    private String postalCode; //定义用户的邮政编码，同组织邮政编码。

    private String fax; //定义用户的传真号码，同组织传真号码。

    private String startTime; //定义用户的开始生效时间，时间格式参见附录。

    private String endTime; //定义用户的结束生效时间，时间格式参见附录。

    private String passwordModifiedDate; //密码修改时间，上次密码修改时间，时间格式参见附录。

    private String idCardNumber; //身份证号，例如：“726631199901012201”

    private String memberOf; //定义用户所属的用户组。

    private String haduty; //职务信息

    private String msssystem; //综合信息网

    private String transferEndDate; //借调结束时间
}
