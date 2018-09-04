package org.xgame.commons.generator;

import java.util.concurrent.atomic.AtomicLong;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: flame-gxnjy 
 * @File: com.flame.game.core.aacommon.generate.TempLongIDGen.java
 * @Description: 临时的longID 生成器,从1开始自增，每次服务器重启之后从新开始，
 * 不能用于需要在服务器重启之后还需要维持的Id， 需要的话请使用另外一个 LongIDGen
 * @Create: DerekWu  2017年8月8日 上午10:36:26 
 * @version: V1.0 
 */
public class TempLongIDGen {

	/** ID值 */
	private static final AtomicLong value = new AtomicLong(0);
	
	/**
     * 生成临时的 LongID
     * @return
     */
    public static long gen() { 
        return value.incrementAndGet();
    }
    
}
