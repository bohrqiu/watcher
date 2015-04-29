/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-04-21 14:37 创建
 *
 */
package com.yiji.framework.watcher.serilization;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.codahale.metrics.Meter;

/**
 * Meter的fastjson序列化器。@see Meter
 * @author daidai@yiji.com
 */
public class MeterSerializer implements ObjectSerializer {
	public static final MeterSerializer INSTANCE = new MeterSerializer(TimeUnit.SECONDS);
	private final String rateUnit;
	private final double rateFactor;
	
	public MeterSerializer(TimeUnit rateUnit) {
		this.rateFactor = rateUnit.toSeconds(1);
		this.rateUnit = calculateRateUnit(rateUnit, "events");
	}
	
	private static String calculateRateUnit(TimeUnit unit, String name) {
		final String s = unit.toString().toLowerCase(Locale.US);
		return name + '/' + s.substring(0, s.length() - 1);
	}
	
	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		Meter meter = (Meter) object;
		SerializeWriter writer = serializer.getWriter();
		writer.writeFieldValue('{', "count", meter.getCount());
		writer.writeFieldValue(',', "m15_rate", meter.getFifteenMinuteRate() * rateFactor);
		writer.writeFieldValue(',', "m1_rate", meter.getOneMinuteRate() * rateFactor);
		writer.writeFieldValue(',', "m5_rate", meter.getFiveMinuteRate() * rateFactor);
		writer.writeFieldValue(',', "mean_rate", meter.getMeanRate() * rateFactor);
		writer.writeFieldValue(',', "units", rateUnit);
		writer.write('}');
	}
}
