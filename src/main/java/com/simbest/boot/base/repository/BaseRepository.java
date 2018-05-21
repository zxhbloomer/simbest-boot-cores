package com.simbest.boot.base.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title</strong> : BaseRepository.java<br>
 * <strong>Description</strong> : 基础类<br>
 * <strong>Create on</strong> : 2017/12/26<br>
 * <strong>Modify on</strong> : 2018/01/04<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @param <T>
 * @param <ID>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	/**
	 * 在这里封装一个分页对象
	 *
	 * @param page       条件-当前页码，必须大于0，从1开始
	 * @param size       条件-每页条数，必须大于0，每页最多不超过100
	 * @param direction  条件-排序规则，asc/desc
	 * @param properties 条件-排序属性，如：userId
	 *
	 * @return 分页对象
	 */
	default Pageable getPageable(int page, int size, String direction, String properties) {
		// 页码是从零开始的
		int pagePage = page < 1 ? 0 : (page - 1);
		int pageSize = size < 1 ? 1 : (size > 100 ? 100 : size);

		Pageable pageable;

		if (direction != null && !direction.equals("") && properties != null && !properties.equals("")) {
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
			Sort sort = new Sort(sortDirection, sortProperties);
			pageable = PageRequest.of(pagePage, pageSize, sort);
		} else {
			pageable = PageRequest.of(pagePage, pageSize);
		}

		return pageable;
	}

	/**
	 * 在这里封装一个单表查询的对象<br>
	 * 只判断属性值是否相等
	 *
	 * @param conditions 条件
	 *
	 * @return 结果集
	 */
	default Specification<T> getSpecification(T conditions) {
		if (conditions == null) {
			return null;
		}

		return (root, query, cb) -> {
			List<Predicate> list = new ArrayList<>();

			Class<?> clazz = conditions.getClass();

			// 获取所有的字段包括private/public，但不包括继承的字段
			Field[] declaredFields = clazz.getDeclaredFields();
			for (Field declaredField : declaredFields) {
				// 获取字段的注解
				Annotation[] annotations = declaredField.getAnnotations();
				for (Annotation annotation : annotations) {
					if (annotation instanceof Id || annotation instanceof Column) {
						declaredField = null;
						break;
					}
				}

				// 跳过数据库中没有的字段
				if (declaredField == null) {
					continue;
				}

				// 获得字段名称
				String filedName = declaredField.getName();
				// 获得字段第一个字母大写
				String firstLetter = filedName.substring(0, 1).toUpperCase();
				// 转换成字段的get方法
				String getMethodName = "get" + firstLetter + filedName.substring(1);

				try {
					// 获取方法
					Method getMethod = clazz.getMethod(getMethodName);
					// 这个对象字段get方法的值
					Object value = getMethod.invoke(conditions);
					// 跳过空值
					if (value == null) {
						continue;
					}

					// =
					list.add(cb.equal(root.get(filedName), value));
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			Predicate[] p = new Predicate[list.size()];
			return cb.and(list.toArray(p));
		};
	}

	/**
	 * 在这里封装一个单表查询的方法（Map）<br>
	 * 只判断属性值是否相等
	 *
	 * @param conditions 条件
	 *
	 * @return 结果集
	 */
	default Specification<T> getSpecification(Map<String, Object> conditions) {
		if (conditions == null) {
			return null;
		}

		return (root, query, cb) -> {
			List<Predicate> list = new ArrayList<>();

			// =
			for (Map.Entry<String, Object> entry : conditions.entrySet()) {
				list.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
			}

			Predicate[] p = new Predicate[list.size()];
			return cb.and(list.toArray(p));
		};
	}

	/**
	 * 在这里封装一个单表查询的方法（Condition）
	 *
	 * @param conditions 条件
	 *
	 * @return 结果集
	 */
	default Specification<T> getSpecification(Condition conditions) {
		if (conditions == null) {
			return null;
		}

		return (root, query, cb) -> {
			List<Predicate> list = new ArrayList<>();

			// =
			Map<String, Object> eq = conditions.getEq();
			if (eq != null) {
				for (Map.Entry<String, Object> entry : eq.entrySet()) {
					list.add(cb.equal(root.get(entry.getKey()), entry.getValue()));
				}
			}

			// =
			Map<String, Object> neq = conditions.getNeq();
			if (neq != null) {
				for (Map.Entry<String, Object> entry : neq.entrySet()) {
					list.add(cb.notEqual(root.get(entry.getKey()), entry.getValue()));
				}
			}

			// >
			Map<String, Number> gt = conditions.getGt();
			if (gt != null) {
				for (Map.Entry<String, Number> entry : gt.entrySet()) {
					list.add(cb.gt(root.get(entry.getKey()), entry.getValue()));
				}
			}

			// <
			Map<String, Number> lt = conditions.getLt();
			if (lt != null) {
				for (Map.Entry<String, Number> entry : lt.entrySet()) {
					list.add(cb.lt(root.get(entry.getKey()), entry.getValue()));
				}
			}

			// %
			Map<String, String> like = conditions.getLike();
			if (lt != null) {
				for (Map.Entry<String, String> entry : like.entrySet()) {
					list.add(cb.like(root.get(entry.getKey()), entry.getValue()));
				}
			}

			// in
			Map<String, List<Object>> in = conditions.getIn();
			if (lt != null) {
				for (Map.Entry<String, List<Object>> entry : in.entrySet()) {
					list.add(cb.and(root.get(entry.getKey()).in(entry.getValue())));
				}
			}

			// notIn
			Map<String, List<Object>> notIn = conditions.getNotIn();
			if (lt != null) {
				for (Map.Entry<String, List<Object>> entry : notIn.entrySet()) {
					list.add(cb.not(root.get(entry.getKey()).in(entry.getValue())));
				}
			}

			Predicate[] p = new Predicate[list.size()];
			return cb.and(list.toArray(p));
		};
	}

}
