/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/* -------------------------- Package Declaration --------------------------- */

package com.github.jessemull.microflex.util;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * This class safely converts (1) double values to lists or arrays of another 
 * numeric type (2) a value from another numeric type to a double. The 
 * utility supports conversion to and from all Java primitives as well as two 
 * immutable data types:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Primitives<div></th>
 *    <tr>
 *       <td>Byte</td>
 *    </tr>
 *    <tr>
 *       <td>Short</td>
 *    </tr>
 *    <tr>
 *       <td>Int</td>
 *    </tr>
 *    <tr>
 *       <td>Long</td>
 *    </tr>
 *    <tr>
 *       <td>Float</td>
 *    </tr>
 *    <tr>
 *       <td>Double</td>
 *    </tr>
 * </table>

 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Immutables<div></th>
 *    <tr>
 *       <td>BigInteger</td>
 *    </tr>
 *    <tr>
 *       <td>BigDecimal</td>
 *    </tr>
 * </table>   
 *     
 * This class throws an arithmetic exception on overflow.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class DoubleUtil {

    /**
     * Safely converts a number to a double. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Number    number to parse
     * @return             parsed number
     * @throws   ArithmeticException    on overflow
     */
    public static double toDouble(Number number) {

        /* Switch on class and convert to double */
        
        String type = number.getClass().getSimpleName();
        double parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) number;
                                 parsed = by.doubleValue();
                                 break; 
                                 
            case "Short":        Short sh = (Short) number;
                                 parsed = sh.doubleValue();
                                 break;
                                 
            case "Integer":      Integer in = (Integer) number;
                                 parsed = in.doubleValue();
                                 break;
                                 
            case "Long":         Long lo = (Long) number;
                                 parsed = lo.doubleValue();
                                 break;
                                 
            case "Float":        Float fl = (Float) number;
                                 parsed = fl.doubleValue();
                                 break;
                                 
            case "BigInteger":   BigInteger bi = (BigInteger) number;
                                 if (!OverFlowUtil.doubleOverflow(bi)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to a double.");
                                  }
                                  parsed = bi.doubleValue();
                                  break;
                                  
            case "BigDecimal":   BigDecimal bd = (BigDecimal) number;
                                 if (!OverFlowUtil.doubleOverflow(bd)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to a double.");
                                 }
                                 parsed = bd.doubleValue();
                                 break;
            
            case "Double":       Double db = (Double) number;
            	                 parsed = db.doubleValue();
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Safely converts an object to a double. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Object    object to parse
     * @return             parsed object
     * @throws   ArithmeticException    on overflow
     */
    public static double toDouble(Object obj) {

        /* Switch on class and convert to double */
        
        String type = obj.getClass().getSimpleName();
        double parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) obj;
                                 parsed = by.doubleValue();
                                 break; 
                                 
            case "Short":        Short sh = (Short) obj;
                                 parsed = sh.doubleValue();
                                 break;
                                 
            case "Integer":      Integer in = (Integer) obj;
                                 parsed = in.doubleValue();
                                 break;
                                 
            case "Long":         Long lo = (Long) obj;
                                 parsed = lo.doubleValue();
                                 break;
                                 
            case "Float":        Float fl = (Float) obj;
                                 parsed = fl.doubleValue();
                                 break;
                                 
            case "BigInteger":   BigInteger bi = (BigInteger) obj;
                                 if (!OverFlowUtil.doubleOverflow(bi)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to a double.");
                                  }
                                  parsed = bi.doubleValue();
                                  break;
                                  
            case "BigDecimal":   BigDecimal bd = (BigDecimal) obj;
                                 if (!OverFlowUtil.doubleOverflow(bd)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to a double.");
                                 }
                                 parsed = bd.doubleValue();
                                 break;
            
            case "Double":       Double db = (Double) obj;
                                 parsed = db.doubleValue();
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Converts a list of doubles to a list of bytes.
     * @param    List<Double>    list of doubles
     * @return                   list of bytes
     */
    public static List<Byte> toByteList(List<Double> list) {
        
        List<Byte> byteList = new ArrayList<Byte>();
        
        for(Double val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            byteList.add(val.byteValue());
        }
        
        return byteList;
        
    }
    
    /**
     * Converts a list of doubles to an array of bytes.
     * @param    List<Double>    list of doubles
     * @return                   array of bytes
     */
    public static byte[] toByteArray(List<Double> list) {
        
        for(Double val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Byte[list.size()]));
        
    }
    
    /**
     * Converts a list of doubles to a list of shorts.
     * @param    List<Double>    list of doubles
     * @return                   list of shorts
     */
    public static List<Short> toShortList(List<Double> list) {
        
        List<Short> shortList = new ArrayList<Short>();
        
        for(Double val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            shortList.add(val.shortValue());
        }
        
        return shortList;
        
    }
    
    /**
     * Converts a list of doubles to an array of shorts.
     * @param    List<Double>    list of doubles
     * @return                   array of shorts
     */
    public static short[] toShortArray(List<Double> list) {
        
        for(Double val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Short[list.size()]));
        
    }
    
    /**
     * Converts a list of doubles to a list of integers.
     * @param    List<Double>    list of doubles
     * @return                   list of shorts
     */
    public static List<Integer> toIntList(List<Double> list) {
        
        List<Integer> intList = new ArrayList<Integer>();
        
        for(Double val : list) {
            if(!OverFlowUtil.intOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            intList.add(val.intValue());
        }
        
        return intList;
        
    }
    
    /**
     * Converts a list of doubles to an array of integers.
     * @param    List<Double>    list of doubles
     * @return                   array of integers
     */
    public static int[] toIntArray(List<Double> list) {    
        
        for(Double val : list) {
            if(!OverFlowUtil.intOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Integer [list.size()]));
        
    }
    
    /**
     * Converts a list of doubles to a list of longs.
     * @param    List<Double>    list of doubles
     * @return                   list of longs
     */
    public static List<Long> toLongList(List<Double> list) {
        
        List<Long> longList = new ArrayList<Long>();
        
        for(Double val : list) {
            if(!OverFlowUtil.longOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            longList.add(val.longValue());
        }
        
        return longList;
        
    }
    
    /**
     * Converts a list of doubles to an array of longs.
     * @param    List<Double>    list of longs
     * @return                   array of longs
     */
    public static long[] toLongArray(List<Double> list) {    
        
        for(Double val : list) {
            if(!OverFlowUtil.longOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Long[list.size()]));
        
    }
    
    /**
     * Converts a list of doubles to a list of floats.
     * @param    List<Double>    list of doubles
     * @return                   list of floats
     */
    public static List<Float> toFloatList(List<Double> list) {
        
        List<Float> floatList = new ArrayList<Float>();
        
        for(Double val : list) {
            if(!OverFlowUtil.floatOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            floatList.add(val.floatValue());
        }
        
        return floatList;
        
    }
    
    /**
     * Converts a list of doubles to an array of floats.
     * @param    List<Double>    list of doubles
     * @return                   array of floats
     */
    public static float[] toFloatArray(List<Double> list) {
        
        for(Double val : list) {
            if(!OverFlowUtil.floatOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Float[list.size()]));
        
    }
    
    /**
     * Converts a collection of numbers to a list of doubles.
     * @param    Collection<Number>    the collection
     * @return                         the list of doubles
     */
    public static List<Double> toDoubleList(Collection<Number> collection) {
    	List<Double> list = new ArrayList<Double>();
    	for(Number num : collection) {
    		list.add(toDouble(num));
    	}
    	return list;
    }
    
    /**
     * Converts an array of numbers to a list of doubles.
     * @param    Number[]        the array
     * @return                   the list of doubles
     */
    public static List<Double> toDoubleList(Number[] array) {
    	List<Double> list = new ArrayList<Double>();
    	for(Number num : array) {
    		list.add(toDouble(num));
    	}
    	return list;
    }
    
    /**
     * Converts a list of doubles to an array of doubles.
     * @param    List<Double>    list of doubles
     * @return                   array of doubles
     */
    public static double[] toDoubleArray(List<Double> list) {
        return ArrayUtils.toPrimitive(list.toArray(new Double[list.size()]));
        
    }
    
    /**
     * Converts a list of doubles to a list of BigIntegers.
     * @param    List<Double>    list of doubles
     * @return                   list of BigIntegers
     */
    public static List<BigInteger> toBigIntList(List<Double> list) {
        
        List<BigInteger> bigIntList = new ArrayList<BigInteger>();
        
        for(Double val : list) {
            bigIntList.add(new BigDecimal(val).toBigInteger());
        }
        
        return bigIntList;
        
    }
       
    /**
     * Converts a list of doubles to an array of BigIntegers.
     * @param    List<Double>    list of doubles
     * @return                   array of BigIntegers
     */
    public static BigInteger[] toBigIntArray(List<Double> list) {
        
        BigInteger[] toBigInt = new BigInteger[list.size()];
        
        for(int i = 0; i < toBigInt.length; i++) {
            toBigInt[i] = new BigDecimal(list.get(i)).toBigInteger();
        }
        
        return toBigInt;
        
    }
    
    /**
     * Converts a list of doubles to a list of BigDecimals.
     * @param    List<Double>    list of doubles
     * @return                   list of BigDecimals
     */
    public static List<BigDecimal> toBigDecimalList(List<Double> list) {
        
        List<BigDecimal> bigDecimalList = new ArrayList<BigDecimal>();
        
        for(Double val : list) {
            bigDecimalList.add(new BigDecimal(val));
        }
        
        return bigDecimalList;
        
    }
    
    /**
     * Converts a list of doubles to an array of BigDecimals.
     * @param    List<Double>    list of doubles
     * @return                   array of BigDecimal
     */
    public static BigDecimal[] toBigDecimalArray(List<Double> list) {
        
        BigDecimal[] toBigDecimal = new BigDecimal[list.size()];
        
        for(int i = 0; i < toBigDecimal.length; i++) {
            toBigDecimal[i] = new BigDecimal(list.get(i));
        }
        
        return toBigDecimal;
        
    }
    
}


















