/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-04-21 11:42 创建
 *
 */
package com.yiji.framework.watcher.metrics.serilization;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

/**
 * @author daidai@yiji.com
 */
public class TimerSerializer implements ObjectSerializer {
	public static final TimerSerializer INSTANCE = new TimerSerializer(TimeUnit.SECONDS, TimeUnit.MILLISECONDS);
	private final String rateUnit;
	private final double rateFactor;
	private final String durationUnit;
	private final double durationFactor;
	
	public TimerSerializer(TimeUnit rateUnit, TimeUnit durationUnit) {
		this.rateUnit = calculateRateUnit(rateUnit, "calls");
		this.rateFactor = rateUnit.toSeconds(1);
		this.durationUnit = durationUnit.toString().toLowerCase(Locale.US);
		this.durationFactor = 1.0 / durationUnit.toNanos(1);
	}
	
	private static String calculateRateUnit(TimeUnit unit, String name) {
		final String s = unit.toString().toLowerCase(Locale.US);
		return name + '/' + s.substring(0, s.length() - 1);
	}
	
	@Override
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		Timer timer = (Timer) object;
		final Snapshot snapshot = timer.getSnapshot();
		SerializeWriter writer = serializer.getWriter();
		writer.writeFieldValue('{', "count", timer.getCount());
		writer.writeFieldValue(',', "max", snapshot.getMax() * durationFactor);
		writer.writeFieldValue(',', "mean", snapshot.getMean() * durationFactor);
		writer.writeFieldValue(',', "min", snapshot.getMin() * durationFactor);
		
		writer.writeFieldValue(',', "p50", snapshot.getMedian() * durationFactor);
		writer.writeFieldValue(',', "p75", snapshot.get75thPercentile() * durationFactor);
		writer.writeFieldValue(',', "p95", snapshot.get95thPercentile() * durationFactor);
		writer.writeFieldValue(',', "p98", snapshot.get98thPercentile() * durationFactor);
		writer.writeFieldValue(',', "p99", snapshot.get99thPercentile() * durationFactor);
		writer.writeFieldValue(',', "p999", snapshot.get999thPercentile() * durationFactor);
		
		writer.writeFieldValue(',', "stddev", snapshot.getStdDev() * durationFactor);
		writer.writeFieldValue(',', "m15_rate", timer.getFifteenMinuteRate() * rateFactor);
		writer.writeFieldValue(',', "m1_rate", timer.getOneMinuteRate() * rateFactor);
		writer.writeFieldValue(',', "m5_rate", timer.getFiveMinuteRate() * rateFactor);
		writer.writeFieldValue(',', "mean_rate", timer.getMeanRate() * rateFactor);
		writer.writeFieldValue(',', "duration_units", durationUnit);
		writer.writeFieldValue(',', "rate_units", rateUnit);
		writer.write('}');
	}
}
