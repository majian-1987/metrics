package com.alibaba.metrics.jvm;

import com.alibaba.metrics.Metric;
import com.alibaba.metrics.MetricName;
import com.alibaba.metrics.MetricSet;
import com.alibaba.metrics.PersistentGauge;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A set of gauges for the JVM name, vendor, and uptime.
 */
public class JvmAttributeGaugeSet implements MetricSet {

    private final RuntimeMXBean runtime;

    /**
     * Creates a new set of gauges.
     */
    public JvmAttributeGaugeSet() {
        this(ManagementFactory.getRuntimeMXBean());
    }

    /**
     * Creates a new set of gauges with the given {@link RuntimeMXBean}.
     * @param runtime JVM management interface with access to system properties
     */
    public JvmAttributeGaugeSet(RuntimeMXBean runtime) {
        this.runtime = runtime;
    }

    @Override
    public Map<MetricName, Metric> getMetrics() {
        final Map<MetricName, Metric> gauges = new HashMap<MetricName, Metric>();

        gauges.put(MetricName.build("name"), new PersistentGauge<String>() {
            @Override
            public String getValue() {
                return runtime.getName();
            }
        });

        gauges.put(MetricName.build("vendor"), new PersistentGauge<String>() {
            @Override
            public String getValue() {
                return String.format(Locale.US,
                        "%s %s %s (%s)",
                        runtime.getVmVendor(),
                        runtime.getVmName(),
                        runtime.getVmVersion(),
                        runtime.getSpecVersion());
            }
        });

        gauges.put(MetricName.build("uptime"), new PersistentGauge<Long>() {
            @Override
            public Long getValue() {
                return runtime.getUptime();
            }
        });

        return Collections.unmodifiableMap(gauges);
    }

    @Override
    public long lastUpdateTime() {
        return System.currentTimeMillis();
    }
}
