package com.simbest.boot.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * <strong>Title : PaginationHelp</strong><br>
 * <strong>Description : 分页组件工具</strong><br>
 * <strong>Create on : 2018/12/21</strong><br>
 * <strong>Modify on : 2018/12/21</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LJW lijianwu@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 * <strong>修改历史:</strong><br>
 * 修改人 修改日期 修改描述<br>
 * -------------------------------------------<br>
 */
@Component
public class PaginationHelp {

    /**
     * 获取分页参数
     * @param page              页码
     * @param size              当前页数量
     * @param direction         排序字段
     * @param properties        排序规则 desc、asc
     * @return
     */
    public Pageable getPageable( int page, int size, String direction, String properties) {
        int pagePage = page < 1 ? 0 : (page - 1);
        int pageSize = size < 1 ? 1 : (size > 100 ? 100 : size);
        Pageable pageable;
        if ( StringUtils.isNotEmpty(direction) && StringUtils.isNotEmpty(properties)) {
            // 生成指定排序规则-顺序
            Sort.Direction sortDirection;
            String[] sortProperties;
            try {
                // 先转换为大写
                direction = direction.toUpperCase();
                // 再获取枚举
                sortDirection = Sort.Direction.valueOf(direction);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                sortDirection = Sort.Direction.ASC;
            }
            // 生成指定排序规则-关键字
            sortProperties = properties.split(",");
            // 生成排序规则
            // 生成排序规则
            Sort sort = new Sort(sortDirection, sortProperties);
            pageable = PageRequest.of(pagePage, pageSize, sort);
        } else {
            pageable = PageRequest.of(pagePage, pageSize);
        }
        return pageable;
    }
}
