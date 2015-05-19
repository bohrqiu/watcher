package com.yiji.framework.watcher.metrics;

import java.util.Map;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.codahale.metrics.*;
import com.google.common.collect.Maps;
import com.yiji.framework.watcher.Constants;
import com.yiji.framework.watcher.MetricsHolder;
import com.yiji.framework.watcher.marshaller.*;
import com.yiji.framework.watcher.metrics.base.AbstractWatcherMetrics;

/**
 * @author daidai@yiji.com
 */
public class MetricRegistryMetrics extends AbstractWatcherMetrics {
	private MetricRegistry metricRegistry;
	
	public MetricRegistryMetrics() {
		this.metricRegistry = MetricsHolder.metricRegistry();
		SerializeConfig.getGlobalInstance().put(Counter.class, CounterSerializer.INSTANCE);
		SerializeConfig.getGlobalInstance().put(Gauge.class, GaugeSerializer.INSTANCE);
		SerializeConfig.getGlobalInstance().put(Histogram.class, HistogramSerializer.INSTANCE);
		SerializeConfig.getGlobalInstance().put(Meter.class, MeterSerializer.INSTANCE);
		SerializeConfig.getGlobalInstance().put(Timer.class, TimerSerializer.INSTANCE);
	}
	
	@Override
	public Object watch(Map<String, Object> params) {
		Map<String, Metric> metrics = metricRegistry.getMetrics();
		Map<String, Metric> ret = Maps.newHashMap();
		for (Map.Entry<String, Metric> metricEntry : metrics.entrySet()) {
			if (matches(params, metricEntry.getKey(), metricEntry.getValue())) {
				ret.put(metricEntry.getKey(), metricEntry.getValue());
			}
		}
		return ret;
	}
	
	public boolean matches(Map<String, Object> param, String metricName, Metric metric) {
		if (param == null) {
			return true;
		}
		String name = (String) param.get(Constants.KEY);
		String type = (String) param.get("type");
		if (metricName != null && name != null && !metricName.startsWith(name)) {
			return false;
		}
		if (type != null) {
			MetricType chosen;
			try {
				chosen = MetricType.valueOf(type);
			} catch (Exception e) {
				chosen = MetricType.ALL;
			}
			return chosen.equals(MetricType.ALL) || type2MetricClass(chosen).isInstance(metric);
		} else {
			return true;
		}
	}
	
	private Class<? extends Metric> type2MetricClass(MetricType type) {
		switch (type) {
			case TIMER:
				return Timer.class;
			case GAUGE:
				return Gauge.class;
			case COUNTER:
				return Counter.class;
			case HISTOGRAM:
				return Histogram.class;
			case METER:
				return Meter.class;
			default:
				return Metric.class;
		}
	}
	
	@Override
	public String name() {
		return "metricRegistry";
	}
	
	@Override
	public String desc() {
		return "Metric indicators. Optional parameters: key=xx[&type=yy]";
	}
	
	public enum MetricType {
		TIMER,
		GAUGE,
		COUNTER,
		HISTOGRAM,
		METER,
		ALL
	}
}
