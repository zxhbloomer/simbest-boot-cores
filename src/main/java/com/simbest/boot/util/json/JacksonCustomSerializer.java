/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 用途：自定义对象转JSON的序列化
 * 作者: lishuyi
 * 时间: 2018/11/7  15:07
 */
public class JacksonCustomSerializer extends JsonSerializer<String> {
    public JacksonCustomSerializer(Class<String> string) {
        super();
    }

    public Class<String> handledType() {
        return String.class;
    }

    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        if (StringUtils.isNotEmpty(value)) {
            //如果注释以下代码，将直接将转义字符返回前端
            jsonGenerator.writeString(JacksonUtils.unescapeString(value));


            //避免Could not write JSON: Can not write a field name, expecting a value报错
            //jsonGenerator.writeString(value);
        }
    }
}
