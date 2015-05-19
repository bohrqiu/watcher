/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-18 20:21 创建
 *
 */
package com.yiji.framework.watcher;

/**
 * @author qiubo@yiji.com
 */
public class Constants {
	public static final Object NULL = new Object() {
		@Override
		public String toString() {
			return "null";
		}
	};
	
	/**
	 * 返回类型key
	 */
	public static final String RES_TYPE_KEY = "resType";
	/**
	 * 请求类型key
	 */
	public static final String ACTION_KEY = "action";
    /**
     * 参数名字
     */
    public static final String KEY = "key";

    public enum ResponseType {
        TEXT,
        JSON;
    }
}
