/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Strings;

import java.io.IOException;

/**
 * 用途：将空字符串转换为null
 * 作者: lishuyi
 * 时间: 2018/3/5  1:41
 */
public class JacksonEmptyStringDeserializer extends StdDeserializer<String> {

    JacksonEmptyStringDeserializer() {
        this(null);
    }

    protected JacksonEmptyStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.getNodeType() == JsonNodeType.STRING) {
            String val = node.asText("");
            if (Strings.isNullOrEmpty(val.trim())) {
                return null;
            }
        }
        return node.asText();
    }
}
