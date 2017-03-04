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
 * This class safely converts (1) integer values to lists or arrays of another 
 * numeric type (2) a value from another numeric type to an integer. The 
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
public class IntegerUtil {

    /**
     * Safely converts a number to an integer. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Number    number to parse
     * @return             parsed number
     * @throws   ArithmeticException    on overflow
     */
    public static int toInteger(Number number) {

        /* Switch on class and convert to an int */
        
        String type = number.getClass().getSimpleName();
        int parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) number;
                                 parsed = by.intValue();
                                 break; 
                                 
            case "Short":        Short sh = (Short) number;
                                 parsed = sh.intValue();
                                 break;
                                 
            case "Integer":      Integer in = (Integer) number;
                                 parsed = in.intValue();
                                 break;
                                 
            case "Long":         Long lo = (Long) number;
                                 if (!OverFlowUtil.intOverflow(lo)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to an int.");
                                 }
                                 parsed = lo.intValue();
                                 break;
                                 
            case "Float":        Float fl = (Float) number;
                                 if (!OverFlowUtil.intOverflow(fl)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to an int.");
                                 }
                                 parsed = fl.intValue();
                                 break;
                                 
            case "BigInteger":   BigInteger bi = (BigInteger) number;
                                 if (!OverFlowUtil.intOverflow(bi)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to an int.");
                                 }
                                 parsed = bi.intValue();
                                 break;
                                  
            case "BigDecimal":   BigDecimal bd = (BigDecimal) number;
                                 if (!OverFlowUtil.intOverflow(bd)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to an int.");
                                 }
                                 parsed = bd.intValue();
                                 break;
            
            case "Double":       Double db = (Double) number;
                                 if (!OverFlowUtil.intOverflow(db)) {
                                     throw new ArithmeticException("Overflow casting " + number + " to an int.");
                                 }
                                 parsed = db.intValue();
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Safely converts a number to an integer. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Object    number to parse
     * @return             parsed number
     * @throws   ArithmeticException    on overflow
     */
    public static int toInteger(Object obj) {

        /* Switch on class and convert to an int */
        
        String type = obj.getClass().getSimpleName();
        int parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) obj;
                                 parsed = by.intValue();
                                 break; 
                                 
            case "Short":        Short sh = (Short) obj;
                                 parsed = sh.intValue();
                                 break;
                                 
            case "Integer":      Integer in = (Integer) obj;
                                 parsed = in.intValue();
                                 break;
                                 
            case "Long":         Long lo = (Long) obj;
                                 if (!OverFlowUtil.intOverflow(lo)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to an int.");
                                 }
                                 parsed = lo.intValue();
                                 break;
                                 
            case "Float":        Float fl = (Float) obj;
                                 if (!OverFlowUtil.intOverflow(fl)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to an int.");
                                 }
                                 parsed = fl.intValue();
                                 break;
                                 
            case "BigInteger":   BigInteger bi = (BigInteger) obj;
                                 if (!OverFlowUtil.intOverflow(bi)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to an int.");
                                 }
                                 parsed = bi.intValue();
                                 break;
                                  
            case "BigDecimal":   BigDecimal bd = (BigDecimal) obj;
                                 if (!OverFlowUtil.intOverflow(bd)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to an int.");
                                 }
                                 parsed = bd.intValue();
                                 break;
            
            case "Double":       Double db = (Double) obj;
                                 if (!OverFlowUtil.intOverflow(db)) {
                                     throw new ArithmeticException("Overflow casting " + obj + " to an int.");
                                 }
                                 parsed = db.intValue();
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Converts a list of integers to a list of bytes.
     * @param    List<Integer>    list of integers
     * @return                    list of bytes
     */
    public static List<Byte> toByteList(List<Integer> list) {
        
        List<Byte> byteList = new ArrayList<Byte>();
        
        for(Integer val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            byteList.add(val.byteValue());
        }
        
        return byteList;
        
    }
    
    /**
     * Converts a list of integers to an array of bytes.
     * @param    List<Integer>    list of integers
     * @return                    array of bytes
     */
    public static byte[] toByteArray(List<Integer> list) {
        
        for(Integer val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Byte[list.size()]));
        
    }
    
    /**
     * Converts a list of integers to a list of shorts.
     * @param    List<Integer>    list of integers
     * @return                    list of shorts
     */
    public static List<Short> toShortList(List<Integer> list) {
        
        List<Short> shortList = new ArrayList<Short>();
        
        for(Integer val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            shortList.add(val.shortValue());
        }
        
        return shortList;
        
    }
    
    /**
     * Converts a list of integers to an array of shorts.
     * @param    List<Integer>    list of integers
     * @return                    array of shorts
     */
    public static short[] toShortArray(List<Integer> list) {
        
        for(Integer val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Short[list.size()]));
        
    }
    
    /**
     * Converts a collection of numbers to a list of integers.
     * @param    Collection<Number>    the collection
     * @return                         the list of integers
     */
    public static List<Integer> toIntegerList(Collection<Number> collection) {
        List<Integer> list = new ArrayList<Integer>();
        for(Number num : collection) {
            list.add(toInteger(num));
        }
        return list;
    }
    
    /**
     * Converts a list of integers to an array of integers.
     * @param    List<Integer>    list of integers
     * @return                    array of integers
     */
    public static int[] toIntegerArray(List<Integer> list) {    
        return ArrayUtils.toPrimitive(list.toArray(new Integer [list.size()]));        
    }
    
    /**
     * Converts a list of integers to a list of longs.
     * @param    List<Integer>    list of integers
     * @return                    list of longs
     */
    public static List<Long> toLongList(List<Integer> list) {
        
        List<Long> longList = new ArrayList<Long>();
        
        for(Integer val : list) {
            longList.add(val.longValue());
        }
        
        return longList;
        
    }
    
    /**
     * Converts a list of integers to an array of longs.
     * @param    List<Integer>    list of longs
     * @return                    array of longs
     */
    public static long[] toLongArray(List<Integer> list) {    
        return ArrayUtils.toPrimitive(list.toArray(new Long[list.size()]));
        
    }
    
    /**
     * Converts a list of integers to a list of floats.
     * @param    List<Integer>    list of integers
     * @return                    list of floats
     */
    public static List<Float> toFloatList(List<Integer> list) {
        
        List<Float> floatList = new ArrayList<Float>();
        
        for(Integer val : list) {
            floatList.add(val.floatValue());
        }
        
        return floatList;
        
    }
    
    /**
     * Converts a list of integers to an array of floats.
     * @param    List<Integer>    list of integers
     * @return                    array of floats
     */
    public static float[] toFloatArray(List<Integer> list) {
        return ArrayUtils.toPrimitive(list.toArray(new Float[list.size()]));
        
    }
    
    /**
     * Converts a list of integers to a list of doubles.
     * @param    List<Integer>    the list of integers
     * @return                    the list of doubles
     */
    public static List<Double> toDoubleList(List<Integer> list) {
    	List<Double> doubleList = new ArrayList<Double>();
    	for(Integer val : list) {
    		doubleList.add(val.doubleValue());
    	}
    	return doubleList;
    }
    
    /**
     * Converts a list of integers to an array of doubles.
     * @param    List<Integer>    list of integers
     * @return                    array of doubles
     */
    public static double[] toDoubleArray(List<Integer> list) {
        return ArrayUtils.toPrimitive(list.toArray(new Double[list.size()]));
        
    }
    
    /**
     * Converts a list of integers to a list of BigIntegers.
     * @param    List<Integer>    list of integers
     * @return                    list of BigIntegers
     */
    public static List<BigInteger> toBigIntList(List<Integer> list) {
        
        List<BigInteger> bigIntList = new ArrayList<BigInteger>();
        
        for(Integer val : list) {
            bigIntList.add(new BigDecimal(val).toBigInteger());
        }
        
        return bigIntList;
        
    }
       
    /**
     * Converts a list of integers to an array of BigIntegers.
     * @param    List<Integer>    list of integers
     * @return                    array of BigIntegers
     */
    public static BigInteger[] toBigIntArray(List<Integer> list) {
        
        BigInteger[] toBigInt = new BigInteger[list.size()];
        
        for(int i = 0; i < toBigInt.length; i++) {
            toBigInt[i] = new BigDecimal(list.get(i)).toBigInteger();
        }
        
        return toBigInt;
        
    }
    
    /**
     * Converts a list of integers to a list of BigDecimals.
     * @param    List<Integer>    list of integers
     * @return                    list of BigDecimals
     */
    public static List<BigDecimal> toBigDecimalList(List<Integer> list) {
        
        List<BigDecimal> bigDecimalList = new ArrayList<BigDecimal>();
        
        for(Integer val : list) {
            bigDecimalList.add(new BigDecimal(val));
        }
        
        return bigDecimalList;
        
    }
    
    /**
     * Converts a list of integers to an array of BigDecimals.
     * @param    List<Integer>    list of integers
     * @return                    array of BigDecimal
     */
    public static BigDecimal[] toBigDecimalArray(List<Integer> list) {
        
        BigDecimal[] toBigDecimal = new BigDecimal[list.size()];
        
        for(int i = 0; i < toBigDecimal.length; i++) {
            toBigDecimal[i] = new BigDecimal(list.get(i));
        }
        
        return toBigDecimal;
        
    }
    
}


















