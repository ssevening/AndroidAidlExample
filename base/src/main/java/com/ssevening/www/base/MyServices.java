package com.ssevening.www.base;

import java.util.HashMap;

/**
 * Created by Pan on 2017/12/13.
 * 第二种解决模块间依赖的方法，这种方法不支持跨进程，但更简单和轻便
 */

public class MyServices {

    private static ServiceLruCache serviceLruCache = new ServiceLruCache(20);
    private static HashMap<String, String> properties;

    public static <T> T get(Class<T> tClass) {
        final T instance;

        String serviceName = tClass.getSimpleName();
        if (serviceLruCache.get(serviceName) != null) {
            return (T) serviceLruCache.get(serviceName);
        } else {
            Class c;
            try {
                String classPath = properties.get(serviceName);
                c = Class.forName(classPath);
                instance = (T) c.newInstance();
                serviceLruCache.put(serviceName, instance);
                return instance;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void register(String interfaceName, String implPath) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(interfaceName, implPath);


    }
}
