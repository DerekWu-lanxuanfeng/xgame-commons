package org.xgame.commons.generator;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xgame.commons.exception.GameException;

/** 
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: flame-gxnjy 
 * @File: com.flame.game.core.aacommon.generate.LongIDGen.java
 * @Description: 唯一 long Id gen 
 * 针对游戏的需要唯一的ID生成（可插入数据库、也可用于其他唯一的long）， 原理是由3段不同的数字拼成一个唯一的long型
 * 临时的ID使用另外一个IntegerIDGenerator 
 * Long最大值9223372036854775806 
 * @Create: DerekWu  2017年8月8日 上午10:06:04 
 * @version: V1.0 
 */
public final class LongIDGen {

	private static final Logger LOG = LogManager.getLogger(LongIDGen.class);
	
	/** ID值 */
	private static AtomicLong value;
	
	/** 是否初始化  */
	private static boolean isInit = false;
	
	/** 重设阀值  */
	private static long resetValue;
	
	/** 最大值 */
	private static long maxValue;
	
	/** 是否在重设中 */
	private static boolean isInReset = false;
	
	/** 重设接口 */
	private static ILongIDReset longIDReset;
	
	/** 测试开关 */
//	private static final boolean isTest = true;
	private static final boolean isTest = false;
	
	/**
	 * 前5个十进制位表示服务器标识 随后5个十进制位表示服务器启动值 后面10个十进制位用于本次ID自增.
     * 格式：92233 7203685 4775807
     *     32767 0000000 0000000
	 * @param uniqueFlag  服务器唯一标识, 5个十进制位, 最大值65535 
	 * @param autoID  自动ID,每次启动服务器后 或者 接近这个值的时候自增一个
	 * @param oneLongIDReset 重设的时候需要的接口
	 * @return 是否成功
	 */
	private static synchronized boolean init(short uniqueFlag, int autoID, ILongIDReset oneLongIDReset) {
		long initialValue = (uniqueFlag * 100000000000000L) + (autoID * 10000000L);
        if (!isInit) {
        	if (oneLongIDReset != null) {
        		longIDReset = oneLongIDReset;
        	} else {
        		throw new GameException("LongIDGen init err, ILongIDReset is null");
        	}
        	value = new AtomicLong(initialValue);
        	//设置重设阀值 
        	if (isTest) {
        		resetValue = initialValue + 5L;
                maxValue = initialValue   + 10L;
        	} else {
        		resetValue = initialValue + 9900000L;
            	maxValue = initialValue   + 9999999L;
        	}
            isInit = true;
            printInfo(true, uniqueFlag, autoID);
            return true;
        }    
        return false;
	}

	/**
     * 前5个十进制位表示服务器标识 随后5个十进制位表示服务器启动值 后面10个十进制位用于本次ID自增.
     * 格式：92233 7203685 4775807
     *     32767 0000000 0000000
     * @param uniqueFlag 服务器唯一标识, 5个十进制位, 最大值65535 
     * @param autoID 自动ID,每次启动服务器后 或者 接近这个阀值的时候自增一个
     * @return 是否成功
     */
	private static synchronized boolean reset(short uniqueFlag, int autoID) {
    	if (isInReset) {
    		long initialValue = (uniqueFlag * 100000000000000L) + (autoID * 10000000L);
        	value.set(initialValue); 
        	//设置重设阀值 
        	if (isTest) {
        		resetValue = initialValue + 5L;
                maxValue = initialValue   + 10L;
        	} else {
        		resetValue = initialValue + 9900000L;
            	maxValue = initialValue   + 9999999L;
        	}
            //退出重设
            isInReset = false;
            printInfo(false, uniqueFlag, autoID);
            return true;
    	}
    	return false;
    }
    
    private static void printInfo(boolean isInit, short uniqueFlag, int autoID) {
    	StringBuilder sb = new StringBuilder();
    	if (isInit) {
    		sb.append("# LongIDGen init ");
    	} else {
    		sb.append("# LongIDGen reset ");
    	}
    	sb.append(" serverId=").append(uniqueFlag);
    	sb.append(" autoID=").append(autoID);
    	sb.append(" resetValue=").append(resetValue);
    	sb.append(" maxValue=").append(maxValue);
    	LOG.info(sb.toString()); 
    }

    /**
     * 生成LongID，初始化之后再使用
     * 
     * @return
     */
    public static long gen() {
        long newId = value.incrementAndGet();
        if (newId >= resetValue) {
        	LOG.debug("# newId="+newId); 
        	if (!isInReset) {
        		startReset();
        	}
        	if (newId >= maxValue) {
        		throw new GameException("# LongIDGen.gen newId:"+newId+" >= maxValue:"+maxValue);
        	}
        }
        return newId;
    }
    
    /**
     * 开始重设
     */
    private static synchronized void startReset() {
    	if (isInReset) {
    		return;
    	}
    	LOG.debug("# LongIDGen startReset "); 
    	//设置为重设中
    	isInReset = true;
    	longIDReset.startReset();
    }

    /**
     * 处于安全考虑提供一个 判断 
     * @return
     */
	private static boolean isInReset() {
		return isInReset;
	}

}
