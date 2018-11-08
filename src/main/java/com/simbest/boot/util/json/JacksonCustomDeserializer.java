/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Strings;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;

/**
 * 用途：自定义JSON转对象的反序列化
 *
 * 作者: lishuyi
 * 时间: 2018/3/5  1:41
 */
public class JacksonCustomDeserializer extends StdDeserializer<String> {

    /*public final static String regex = "'|%|--|and|or|not|insert|delete|update|select|count|group|union" +
            "|create|drop|truncate|alter|grant|execute|exec|xp_cmdshell|call|declare|source|sql";*/

    public JacksonCustomDeserializer() {
        this(null);
    }

    public JacksonCustomDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.getNodeType() == JsonNodeType.STRING) {
            String val = node.asText("");
            // 1、将空字符串转换为null
            if (Strings.isNullOrEmpty(val.trim())) {
                return null;
            }
            // 2、防止SQL注入, (?i)不区分大小写替换
            else {
                val = HtmlUtils.htmlEscape(val);
                //val = val.trim().replaceAll("(?i)"+regex, "");
                return val;
            }
        }
        return node.asText();
    }
}
