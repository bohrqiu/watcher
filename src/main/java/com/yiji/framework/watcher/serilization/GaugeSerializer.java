/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-04-21 14:24 创建
 *
 */
package com.yiji.framework.watcher.serilization;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.codahale.metrics.Gauge;

/**
 * @author daidai@yiji.com
 */
public class GaugeSerializer implements ObjectSerializer {
	public static final GaugeSerializer INSTANCE = new GaugeSerializer();
	
	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		Gauge gauge = (Gauge) object;
		Object value = gauge.getValue();
		SerializeWriter writer = serializer.getWriter();
		if (value == null)
			writer.write("0");
		else
			writer.write(value.toString());
	}
}
