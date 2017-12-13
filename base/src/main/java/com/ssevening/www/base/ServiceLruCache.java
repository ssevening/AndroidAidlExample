package com.ssevening.www.base;

import android.support.v4.util.LruCache;

/**
 * Created by Pan on 2017/12/13.
 */

public class ServiceLruCache extends LruCache {
    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public ServiceLruCache(int maxSize) {
        super(maxSize);
    }
}
