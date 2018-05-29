/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.base.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用途：标记是否导入导出的字段
 * 作者: lishuyi
 * 时间: 2018/5/29  22:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { java.lang.annotation.ElementType.FIELD })
public @interface ExcelVOAttribute {

    /**
     * 导出到Excel中的名字. 
     */
    String name();

    /**
     * 配置列的名称,对应A,B,C,D.... 
     */
    String column();

    /**
     * 提示信息 
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容. 
     */
    String[] combo() default {};

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写. 
     */
    boolean isExport() default true;

    /**
     * 标记是否导出此列. 
     */
    String[] isTag() default {};
}
