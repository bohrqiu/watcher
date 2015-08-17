/*
 * www.yiji.com Inc.
 * Copyright (c) 2015 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-08-17 12:15 创建
 *
 */
package com.yiji.framework.watcher.http.adaptor.web.util;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qiubo@yiji.com
 */
public class VelocityLogChute implements LogChute {
    private static final Logger logger = LoggerFactory.getLogger("velocity");

    @Override
    public void init(RuntimeServices rs) throws Exception {
    }

    @Override
    public void log(int level, String message) {
        switch (level) {
            case LogChute.WARN_ID:
                logger.warn(message);
                break;
            case LogChute.INFO_ID:
                logger.info(message);
                break;
            case LogChute.TRACE_ID:
                logger.trace(message);
                break;
            case LogChute.ERROR_ID:
                logger.error(message);
                break;
            case LogChute.DEBUG_ID:
            default:
                logger.debug(message);
                break;
        }
    }

    @Override
    public void log(int level, String message, Throwable t) {
        switch (level) {
            case LogChute.WARN_ID:
                logger.warn(message, t);
                break;
            case LogChute.INFO_ID:
                logger.info(message, t);
                break;
            case LogChute.TRACE_ID:
                logger.trace(message, t);
                break;
            case LogChute.ERROR_ID:
                logger.error(message, t);
                break;
            case LogChute.DEBUG_ID:
            default:
                logger.debug(message, t);
                break;
        }
    }

    @Override
    public boolean isLevelEnabled(int level) {
        switch (level) {
            case LogChute.DEBUG_ID:
                return logger.isDebugEnabled();
            case LogChute.INFO_ID:
                return logger.isInfoEnabled();
            case LogChute.TRACE_ID:
                return logger.isTraceEnabled();
            case LogChute.WARN_ID:
                return logger.isWarnEnabled();
            case LogChute.ERROR_ID:
                return logger.isErrorEnabled();
            default:
                return true;
        }
    }
}
