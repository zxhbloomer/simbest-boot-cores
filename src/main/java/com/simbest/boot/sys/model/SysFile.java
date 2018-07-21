/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @SequenceGenerator (name = "sys_file_seq", sequenceName = "sys_file_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "sys_file_seq")
    private Long id;

    //文件名称
    @Column(nullable = false, length = 200)
    @NonNull
    private String fileName;

    //文件类型
    @Column(nullable = false, length = 20)
    @NonNull
    private String fileType;

    //文件实际存储路径
    @Column(nullable = false, length = 500)
    @NonNull
    @JsonIgnore //隐藏不对外暴露内部路径
    private String filePath;

    //文件大小
    @Column(nullable = false, length = 50)
    @NonNull
    private Long fileSize;

    //归属流程
    private String pmInsType;

    //归属流程ID
    private String pmInsId;

    //归属流程区块
    private String pmInsTypePart;

    //文件下载URL
    @Column(nullable = false, length = 500)
    @NonNull
    private String downLoadUrl;
}
