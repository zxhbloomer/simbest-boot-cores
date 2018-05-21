/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import com.simbest.boot.base.model.LogicModel;
import lombok.*;

import javax.persistence.*;

/**
 * 用途：统一系统文件管理
 * 作者: lishuyi
 * 时间: 2018/3/7  23:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SysFile extends LogicModel {
    @Id
    @Column(name = "id")
    @SequenceGenerator (name = "sys_dict_value_seq", sequenceName = "sys_dict_value_seq")
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "sys_dict_value_seq")
    private Long id;

    //文件名称
    @Column(nullable = false, length = 200)
    @NonNull
    private String fileName;

    //文件类型
    @Column(nullable = false, length = 20)
    @NonNull
    private String fileType;

    //存储路径
    @Column(nullable = false, length = 500)
    @NonNull
    private String filePath;

    //文件大小
    @Column(nullable = false, length = 50)
    @NonNull
    private Long fileSize;

    //归属应用
    @Column(nullable = false, length = 20)
    @NonNull
    private String application;

    //归属模块
    @Column(nullable = false, length = 20)
    @NonNull
    private String moudule;
}
