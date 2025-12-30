package com.anyi.sparrow.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 
 * @author shenbh
 * 
 *整形实体
 */
public class IntUtil {
	
	/**
	 * 转为 整形
	 * @param obj
	 * @return
	 */
	public static int convertToInt(Object obj){
		if(obj == null || "".equals(obj.toString())){
			return 0;
		}
		return Integer.parseInt(obj.toString());
	}
	/**
	 * 转为 长整形
	 * @param obj
	 * @return
	 */
	public static long convertToLong(Object obj){
		if(obj == null || "".equals(obj.toString())){
			return 0;
		}
		return Long.parseLong(obj.toString());
	}

	/**
	 * Object 转为 Byte
	 * @param obj 
	 * @return
	 */
	public static Byte convertToByte(Object obj){
		if(obj == null || "".equals(obj)){
			return 0;
		}
		return Byte.valueOf(obj.toString());
	}
	/**
	 * Double转为字符串,保留digits位小数点
	 * @param obj Double数字
	 * @param digits 小数位
	 * @return
	 */
	public static String doubleToStr(Double obj,int digits) {
		if(obj == null || "".equals(obj)){
			return "";
		}
		NumberFormat  formater  =  DecimalFormat.getInstance();  
		formater.setMaximumFractionDigits(digits);  
		formater.setMinimumFractionDigits(digits);  
		String str = formater.format(obj);
		if("".equals(str)){
			return "";
		}
		//将,号去掉
		return str.replaceAll(",", "");
	}
	/**
	 * 四舍五入，并保留位数
	 * @param val 值
	 * @param precision 精度 保留位数
	 * @return
	 */
	public static Double roundDouble(double val, int precision) {   
        Double ret = null;   
        try {   
            double factor = Math.pow(10, precision);   
            ret = Math.floor(val * factor + 0.5) / factor;   
        } catch (Exception e) {   
            e.printStackTrace();   
        }   
  
        return ret;   
    } 
	/**
	 * 转成 double
	 * @param obj
	 * @return
	 */
	public static double convertDouble(Object obj){
		if(obj == null || "".equals(obj)){
			return 0.00;
		}
		return Double.parseDouble(obj.toString());
	}
}
