package org.xgame.commons.utils.random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Company: 深圳市烈焰时代科技有限公司
 * @Product: flame-game-aacommon
 * @File: com.flame.game.core.aacommon.random。RandomUtil.java
 * @Description: 随机数工具类
 * @Create: DerekWu 2015年4月21日 上午9:32:00
 * @version: V1.0
 */
public class RandomUtil {

//	public static Random random = new Random();
	
	private static Random getRandom() {
		return ThreadLocalRandom.current();
	}

	/**
	 * 是否在范围内 �?5/100)则有5%几率返回值为true
	 * 
	 * @param minPercent
	 *            分子
	 * @param maxPercent
	 *            分母
	 * @return 是否在范围内
	 */
	public static boolean isInTheLimits(Integer minPercent, Integer maxPercent) {
		if (getRandom().nextInt(maxPercent) + 1 <= minPercent) {
			return true;
		} else {
			return false;
		}
	}

	public static int[] randomIndexFromList(List<Integer> indexList, int length) {
		int[] randomArray = new int[length];
		if (length == 0) {
			return randomArray;
		}
		Set<Integer> set = new TreeSet<Integer>();
		while (true) {
			int index = getRandom().nextInt(indexList.size());
			set.add(indexList.get(index));
			indexList.remove(index);
			if (set.size() >= length) {
				break;
			}
		}

		Iterator<Integer> iterator = set.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			randomArray[i] = iterator.next();
			i++;
		}

		return randomArray;
	}

	/**
	 * 生成随机数字数组(大于等于minNum且小于等于maxNum)
	 * 
	 * @param minNum
	 * @param maxNum
	 * @param length
	 * @return int数组(按照从小到大自动排序)
	 */
	public static int[] generateRandomNumberArray(int minNum, int maxNum,
			int length) {
		int[] randomArray = new int[length];
		Set<Integer> set = new TreeSet<Integer>();
		int offset = maxNum - minNum;
		while (true) {
			set.add(getRandom().nextInt(offset) + minNum + 1);
			if (set.size() >= length) {
				break;
			}
		}

		Iterator<Integer> iterator = set.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			randomArray[i] = iterator.next();
			i++;
		}
		return randomArray;
	}

	/**
	 * 生成随机数字数组(大于等于minNum且小于等于maxNum)
	 * 
	 * @param minNum
	 * @param maxNum
	 * @param length
	 * @return int数组(按照从小到大自动排序)
	 */
	public static Collection<Integer> generateNumberArray(int minNum,
			int maxNum, int length) {
		int min = minNum < maxNum ? minNum : maxNum;
		int offset = Math.abs(maxNum - minNum);
		if (length >= offset + 1) {
			Collection<Integer> randomArray = new ArrayList<>(offset + 1);
			for (int i = 0; i < offset + 1; i++) {
				randomArray.add(min + i);
			}
			return randomArray;
		}
		// int i = 0;
		Set<Integer> set = new TreeSet<Integer>();
		while (true) {
			int r = getRandom().nextInt(offset);
			while (!set.add(r + min)) {
				r = (r + 1) % offset;
			}
			if (set.size() >= length) {
				break;
			}
		}
		return set;
	}

	/**
	 * 在已存在数据中随机�?取数�?
	 * 
	 * @param existingArray
	 *            已存在数�?
	 * @param num
	 *            数量
	 * @return
	 */
	public static int[] generateRandomNumberArrayFromExistingArray(
			Integer[] existingArray, int num) {
		int[] randomArray = new int[num];
		Set<Integer> set = new TreeSet<Integer>();
		while (true) {
			set.add(existingArray[getRandom().nextInt(existingArray.length)]);
			if (set.size() >= num) {
				break;
			}
		}
		Iterator<Integer> iterator = set.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			randomArray[i] = iterator.next();
			i++;
		}
		return randomArray;
	}

	public static int getRandomNumber(int len) {
		return getRandom().nextInt(len);
	}

	public static boolean isInArray(int[] array, int e) {
		for (int i : array) {
			if (i == e) {
				return true;
			}
		}
		return false;
	}

	public static <T> T getOneRandomElement(Collection<T> coll) {
		int length = coll.size();
		int sel = getRandomNumber(length);
		int i = 0;
		for (T one : coll) {
			if (sel == i)
				return one;
			// sel++;
			i++;
		}
		return null;
	}

	/**
	 * 随机出value~value*N之间的整数，precision指定精度 例如：将value=1000，在100%~200%内随机，精度2%
	 * 则这样调用randomMultipleN(1000,2,2)
	 * 
	 * @param value
	 *            �?��随机出�?数的�?
	 * @param maxMultiple
	 *            倍数 >1
	 * @param precision
	 *            指定精度 >=1
	 * @return
	 */
	public static int randomMultipleN(int value, int maxMultiple, int precision) {
		// 精度�?
		int precisionValue = (int) Math.pow(10, precision);
		// 因子范围
		int factorRange = precisionValue * (maxMultiple - 1);
		int factor = RandomUtil.getRandomNumber(factorRange + 1);

		double temp = value + value * factor / precisionValue;
		int result = (int) Math.round(temp); // 四舍五入
		return result;
	}

	/**
	 * 产生min与max之间的一个随机整�?
	 * 
	 * @param min
	 * @param max
	 * @return
	 * @Added by 500 [min,max)
	 */
	public static int randomBetween(int min, int max) {
		if (min >= max)
			return min;
		return getRandom().nextInt(max - min) + min;
	}

	public static void main(String[] args) {
		List<Integer> indexList = new ArrayList<Integer>();
		for (int k = 0; k < 8; k++)
			indexList.add(k);
		int[] a = randomIndexFromList(indexList, 2);
		System.out.println(a[0] + "," + a[1]);
		a = randomIndexFromList(indexList, 2);
		System.out.println(a[0] + "," + a[1]);
		a = randomIndexFromList(indexList, 2);
		System.out.println(a[0] + "," + a[1]);
		System.out.println(indexList.size());

		for (Integer r : generateNumberArray(50, 70, 10)) {
			System.out.println(r);
		}
	}
}
