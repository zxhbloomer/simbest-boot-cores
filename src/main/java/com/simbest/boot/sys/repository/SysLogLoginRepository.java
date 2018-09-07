/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.repository;

import com.simbest.boot.base.repository.GenericRepository;
import com.simbest.boot.sys.model.SysLogLogin;
import org.springframework.stereotype.Repository;

/**
 * 用途：登录日志
 * 作者: lishuyi
 * 时间: 2018/6/10  13:42
 */
@Repository
public interface SysLogLoginRepository extends GenericRepository<SysLogLogin, String> {

}

