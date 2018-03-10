package com.logistics.module.util;

import java.util.Random;
import java.util.UUID;

public class RandomNumber {
    private static char ch[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', '0', '1' };//最后又重复两个0和1，因为需要凑足数组长度为64

    private static Random random = new Random();

    //生成指定长度的随机字符串
    public static String createRandomString(int len) {
        if (len > 0) {
            int index = 0;
            char[] temp = new char[len];
            int num = random.nextInt();
            for (int i = 0; i < len % 5; i++) {
                temp[index++] = ch[num & 63];//取后面六位，记得对应的二进制是以补码形式存在的。
                num >>= 6;//63的二进制为:111111
                // 为什么要右移6位？因为数组里面一共有64个有效字符。为什么要除5取余？因为一个int型要用4个字节表示，也就是32位。
            }
            for (int i = 0; i < len / 5; i++) {
                num = random.nextInt();
                for (int j = 0; j < 5; j++) {
                    temp[index++] = ch[num & 63];
                    num >>= 6;
                }
            }
            return new String(temp, 0, len);
        } else if (len == 0) {
            return "";
        } else {
            throw new IllegalArgumentException();
        }
    }

    //生成指定长度的随机字符串
    public static Integer createRandomInteger(int bound) {
    	Integer num = 0;
    	try {
            num = random.nextInt(bound);
            if(num > bound || num < 0) {
            	return 0;
            }
    	} catch (Exception e) {
    		e.printStackTrace();
    		return 0;
    	}
    	return num;
    }

    private static char _32TABLE[] = { 
    	'e', '2', 's', '3', 'k', '6', 'm', '7', 'n', '9', 'a', 'w',
    	'd', '1', 'f', '4', 'g', 'h', 'y', 'i', 'j', '5', 'l', '8', 'z', 'p',
    	'q', 'r', 't', 'u', 'v', 'c'};
    
    private static String decTo32(Long t) {
    	String ret = "";
    	boolean flag = true;
    	while(flag) {
    		if(t < 32) {
    			ret = ret + _32TABLE[(int)(long)t];
    			flag = false;
    		} else {
    			ret = ret + _32TABLE[(int) (t % 32)];
    			t = t / 32;
    		}
    	}
    	return ret;
    }
    
    private static int getDecValue(char c) {
    	for(int i = 0; i < _32TABLE.length; i++) {
    		if(c == _32TABLE[i]) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    private static Long _32ToDec(String t) {
    	Long ret = 0L;
    	long base = 1;
    	for(int i=0; i < t.length(); i++) {
    		base = (long) Math.pow(32, i);
    		ret = ret + getDecValue(t.charAt(i)) * base;
    	}
    	return ret;
    }
    
    // Order Token 
    private static final int TOKEN_MAGIC = 37;
    public static String generateTokenFromIdAndCount(long userId, int count) {
    	int len = createRandomInteger(6) + 6;
    	count = count + TOKEN_MAGIC;
    	String token = createRandomString(len) + "o" + decTo32((long)count) + "b" + decTo32(userId);
    	return token;
    }
    
    public static long getOrderIdFromOrderToken(String token) {
    	try {
	    	int index = token.lastIndexOf("b");
	    	String strId = token.substring(index + 1);
	    	return Long.valueOf(_32ToDec(strId));
    	}catch(Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }

    public static int getCountFromOrderToken(String token) {
    	try {
	    	int index0 = token.lastIndexOf("o");
	    	int index1 = token.lastIndexOf("b");
	    	String str = token.substring(index0 + 1, index1);
	    	int count =  (int)(long)Long.valueOf(_32ToDec(str));
	    	count = count - TOKEN_MAGIC;
	    	return count;
    	}catch(Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }

    // user token
    public static String generateUserToken(long userId) {
    	int len = createRandomInteger(6) + 6;
    	String token = createRandomString(len) + "b" + decTo32(userId);
    	return token;
    }

    public static long getUserIdFromToken(String token) {
    	try {
	    	int index = token.lastIndexOf("b");
	    	String strId = token.substring(index + 1);
	    	return Long.valueOf(_32ToDec(strId));
    	}catch(Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }
    
    public static String generatePresentSequence() {
    	Long t = System.currentTimeMillis();
    	String time = t.toString() + createRandomInteger(1000);
    	String str = decTo32(Long.valueOf(time)); 
    	return str;
    }


    public static void main(String[] args) {
    	System.out.println(createRandomInt(2));
    }

    //生成指定长度的随机字符串
    public static int createRandomInt() {
    	try {
    		return random.nextInt(65535);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return 1;
    	}
    }

    //生成指定长度的随机字符串
    public static Long createRandomLong() {
    	try {
    		return random.nextLong();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return 1L;
    	}
    }
    
    //生成指定长度的随机字符串
    public static int createRandomInt(int max) {
    	if (max <= 0) {
			return 0;
		}
    	try {
    		return random.nextInt(max);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return 1;
    	}
    }
    
    public static String generateUUID() {  
        String str = UUID.randomUUID().toString();  
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);  
    }

	/**
	 * 获得随机数
	 * @param start 范围开始
	 * @param end 范围结束
	 * @param base 基础数
	 * @return 随机数
	 */
	public static long getRandom(int start, int end,int base){
		if (start == end){
			return start + base;
		}
		Random random = new Random(System.nanoTime());
		Integer i =random.nextInt();
		int randomNumber = ( start < end ? start : end ) + Math.abs(i % Math.abs(end-start+1));
		return randomNumber + base;
	}

}