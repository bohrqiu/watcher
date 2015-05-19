/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-04 20:00 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import com.google.common.collect.Lists;
import org.hyperic.sigar.cmd.Df;

import java.util.List;

/**
 * @author qiubo@yiji.com
 */
public class WatcherDf extends Df {
    private List<String> result = Lists.newArrayList();

    @Override
    public void println(String line) {
        result.add(line);
    }

    public List<String> getResult() {
        return result;
    }
}
