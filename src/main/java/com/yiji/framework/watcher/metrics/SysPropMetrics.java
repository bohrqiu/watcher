/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-04-25 15:52 创建
 *
 */
package com.yiji.framework.watcher.metrics;

import com.yiji.framework.watcher.MonitorMetrics;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author qzhanbo@yiji.com
 */
public class SysPropMetrics implements MonitorMetrics {

    public Object monitor(Map<String, Object> params) {
        String key = (String) params.get("key");
        if (key == null) {
            return toMap(System.getProperties());
        } else {
            return System.getProperties().get(key);
        }
    }

    public Map<String, String> toMap(Properties properties) {
        Map<String, String> map = new HashMap<>();
        if (properties == null) {
            return map;
        }

        for (Object key : properties.keySet()) {
            map.put((String) key, properties.getProperty((String) key));
        }

        return map;
    }


    public String name() {
        return "sysProp";
    }


    public String desc() {
        return "系统参数,不带任何参数，返回所有配置，传入参数key=xx，返回指定的参数.";
    }
}
