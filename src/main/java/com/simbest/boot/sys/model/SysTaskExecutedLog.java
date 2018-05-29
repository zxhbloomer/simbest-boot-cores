/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.model;

import com.simbest.boot.base.model.SystemModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 用途：定时任务执行日志
 * 作者: lishuyi
 * 时间: 2018/3/7  23:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SysTaskExecutedLog extends SystemModel {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "sys_task_executed_log_seq", sequenceName = "sys_task_executed_log_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sys_task_executed_log_seq")
    private Long id;

    @Column(nullable = false, length = 200)
    @NonNull
    private String taskName;

    @Column(nullable = false, length = 50)
    @NonNull
    private String hostname;

    @Column(nullable = false)
    @NonNull
    private Integer port;

    //持续时间(毫秒)
    @Column(nullable = false)
    @NonNull
    private Long durationTime;

    //文件大小
    @Column(nullable = false, length = 2000)
    @NonNull
    private String content;
}
