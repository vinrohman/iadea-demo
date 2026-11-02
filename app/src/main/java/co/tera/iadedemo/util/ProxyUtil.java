package co.tera.iadedemo.util;

import java.lang.reflect.Proxy;

public class ProxyUtil {
    public static Object wrap(final Object obj, boolean force, Class<?>... interfaces) {
        for (final Class intf : interfaces) {
            if (!intf.isInstance(obj)) {
                if (!force) {
                    return null;
                }
                return Proxy.newProxyInstance(interfaces[0].getClassLoader(), interfaces, (proxy, method, args) -> {
                    return obj.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(obj, args);
                });
            }
        }
        return obj;
    }

    public static Object wrap(final Object obj, Class<?>... interfaces) {
        return wrap(obj, false, interfaces);
    }
};

