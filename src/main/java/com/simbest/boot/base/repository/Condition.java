package com.simbest.boot.base.repository;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>Title</strong> : Condition.java<br>
 * <strong>Description</strong> : 单表查询条件<br>
 * <strong>Create on</strong> : 2018/01/02<br>
 * <strong>Modify on</strong> : 2018/03/02<br>
 * <strong>Copyright (C) ___ Ltd.</strong><br>
 *
 * @author baimengqi baimengqi@simbest.com.cn
 * @version v0.0.1
 */
public class Condition {

	// 等于
	@Getter
	private Map<String, Object> eq = new HashMap<>();

	// 不等于
	@Getter
	private Map<String, Object> neq = new HashMap<>();

	// 大于
	@Getter
	private Map<String, Number> gt = new HashMap<>();

	// 小于
	@Getter
	private Map<String, Number> lt = new HashMap<>();

	// 相似
	@Getter
	private Map<String, String> like = new HashMap<>();

	// 在其中
	@Getter
	private Map<String, List<Object>> in = new HashMap<>();

	// 不在其中
	@Getter
	private Map<String, List<Object>> notIn = new HashMap<>();

	public void eq(String key, Object value) {
		if (value != null) {
			eq.put(key, value);
		}
	}

	public void neq(String key, Object value) {
		if (value != null) {
			neq.put(key, value);
		}
	}

	public void gt(String key, Number value) {
		if (value != null) {
			gt.put(key, value);
		}
	}

	public void lt(String key, Number value) {
		if (value != null) {
			lt.put(key, value);
		}
	}

	public void like(String key, String value) {
		if (value != null) {
			like.put(key, value);
		}
	}

	public void in(String key, Object value) {
		if (key != null && value != null) {
			List<Object> list = in.computeIfAbsent(key, k -> new ArrayList<>());
			list.add(value);
		}
	}

	public void notIn(String key, Object value) {
		if (key != null && value != null) {
			List<Object> list = notIn.computeIfAbsent(key, k -> new ArrayList<>());
			list.add(value);
		}
	}

}
