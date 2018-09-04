package org.xgame.commons.generator;

import java.util.UUID;

/**
 * @Name: UUIDGen.class
 * @Description: //
 * @Create: DerekWu on 2018/9/2 21:27
 * @Version: V1.0
 */
public class UUIDGen {
    public static String gen() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
