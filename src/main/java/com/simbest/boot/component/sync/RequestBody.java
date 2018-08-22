/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.component.sync;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 用途：用于对外提供接口的请求体，适用于对外提供/anonymous/**路径的接口鉴权
 * 作者: lishuyi
 * 时间: 2018/8/22  11:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestBody<T> {

    public static final String REQUEST_TOKEN = "OaApi";

    //请求方IP
    @NonNull
    private String clientIp;

    //请求方应用代码
    @NonNull
    private String clientCode;

    //请求方应用名称
    @NonNull
    private String clientName;

    //请求方联系人
    @NonNull
    private String clientLinkman;

    //请求方联系人电话
    @NonNull
    private String clientLinkTel;

    //请求方联系人邮箱
    @NonNull
    private String clientLinkEmail;

    //请求方局方联系人
    @NonNull
    private String customLinkman;

    //请求方局方联系人电话
    @NonNull
    private String customLinkTel;

    //请求方局方联系人邮箱
    @NonNull
    private String customLinkEmail;

    //请求时间
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requestTime;

    //请求密码，采用Sha1-加密
    @NonNull
    private String requestToken;

    //请求数据
    @NonNull
    private T data;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
