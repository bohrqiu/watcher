/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-25 13:51 创建
 *
 */
package com.yiji.framework.watcher.dubbo.health;

import com.codahale.metrics.health.HealthCheck;
import com.yiji.framework.watcher.Utils;

/**
 * @author qiubo@yiji.com
 */
public abstract class  AbstractDubboHealthCheck extends HealthCheck {
    public AbstractDubboHealthCheck() {
        Utils.checkClassExists("com.alibaba.dubbo.common.Version", "dubbo");
    }
}
