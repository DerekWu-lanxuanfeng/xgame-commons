package org.xgame.commons.serialize.simple;

import java.util.HashMap;
import java.util.Map;

/**
 * @Name: BaseObjectManager.class
 * @Description: //
 * @Create: DerekWu on 2018/3/15 1:55
 * @Version: V1.0
 */
public class BaseObjectManager {

    private static HashMap<Class, IBaseObjectInstance> map = new HashMap<>();

    public static <T extends IBaseObject> void register(Class<T> clazz, IBaseObjectInstance baseObjectInstance) {
        map.putIfAbsent(clazz, baseObjectInstance);
    }

    public static <T extends IBaseObject> T instance(Class<T> clazz) {
        return (T) map.get(clazz).instance();
    }

}
