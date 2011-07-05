package org.hyperic.hq.plugin.gfee.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.MapMaker;

public class MetricCache {

    /** Last time when cache was updated. */
    private long metricCacheLastUpdate;

    /** Cache for all metric values. */
    private ConcurrentMap<String, Double> metricCache;

    /** Cache for tracked metrics. */
    private ConcurrentMap<String, Double> trackCache;

    /** Flag telling if member is visible. */
    private boolean memberOnline = false;
    
    /** Track cache lock*/
    private Object trackLock = new Object();

    public MetricCache() {
        metricCacheLastUpdate = 0;

        metricCache = new MapMaker()
        .makeMap();

        // Let unneeded metrics to stay under collection for 1 hour
        trackCache = new MapMaker()
        .expireAfterAccess(3600, TimeUnit.SECONDS)
        .makeMap();		

    }

    public long getMetricCacheLastUpdate() {
        return metricCacheLastUpdate;
    }

    public void setMetricCacheLastUpdate(long metricCacheLastUpdate) {
        this.metricCacheLastUpdate = metricCacheLastUpdate;
    }

    public boolean isMemberOnline() {
        return memberOnline;
    }

    public void setMemberOnline(boolean memberOnline) {
        this.memberOnline = memberOnline;
    }

    public ConcurrentMap<String, Double> getMetricCache() {
        return metricCache;
    }

//    public ConcurrentMap<String, Double> getTrackCache() {
//        return trackCache;
//    }
    
    public String[] getTrackKeySet() {
        synchronized (trackLock) {
            return trackCache.keySet().toArray(new String[0]);            
        }
    }
    
    public void putToTrackCache(String alias, Double value) {
        synchronized (trackLock) {
            trackCache.put(alias, value);            
        }
    }

}
