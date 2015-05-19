/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-04-21 14:33 创建
 *
 */
package com.yiji.framework.watcher.marshaller;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Snapshot;

/**
 * Histogram的fastjson序列化器。@see Histogram
 * @author daidai@yiji.com
 */
public class HistogramSerializer implements ObjectSerializer {
	public static final HistogramSerializer INSTANCE = new HistogramSerializer();
	
	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		Histogram histogram = (Histogram) object;
		SerializeWriter writer = serializer.getWriter();
		final Snapshot snapshot = histogram.getSnapshot();
		writer.writeFieldValue('{', "count", histogram.getCount());
		writer.writeFieldValue(',', "max", snapshot.getMax());
		writer.writeFieldValue(',', "mean", snapshot.getMean());
		writer.writeFieldValue(',', "min", snapshot.getMin());
		writer.writeFieldValue(',', "p50", snapshot.getMedian());
		writer.writeFieldValue(',', "p75", snapshot.get75thPercentile());
		writer.writeFieldValue(',', "p95", snapshot.get95thPercentile());
		writer.writeFieldValue(',', "p98", snapshot.get98thPercentile());
		writer.writeFieldValue(',', "p99", snapshot.get99thPercentile());
		writer.writeFieldValue(',', "p999", snapshot.get999thPercentile());
		writer.writeFieldValue(',', "stddev", snapshot.getStdDev());
		writer.write('}');
	}
}
