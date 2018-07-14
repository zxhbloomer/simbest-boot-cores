package com.simbest.boot.ws.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <strong>Title : XMLGregorianCalendar日期时间参数传递为空适配器</strong><br>
 * <strong>Description : </strong><br>
 * <strong>Create on : 2018/7/11</strong><br>
 * <strong>Modify on : 2018/7/11</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
public class RelaxedXMLGregorianCalendarAdapter extends XmlAdapter<String, XMLGregorianCalendar> {

    @Override
    public String marshal(XMLGregorianCalendar v) {
        return v.toXMLFormat();
    }

    @Override
    public XMLGregorianCalendar unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        if (v.trim().isEmpty()) {
            return null;
        }
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(v.replace(" ", "T"));
    }
}