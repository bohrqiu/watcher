/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-04-21 14:30 创建
 *
 */
package com.yiji.framework.watcher.metrics.serilization;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.codahale.metrics.Counter;

/**
 * @author daidai@yiji.com
 */
public class CounterSerializer implements ObjectSerializer {
	public static final CounterSerializer INSTANCE = new CounterSerializer();
	
	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		Counter counter = (Counter) object;
		serializer.write(counter.getCount());
	}
}
