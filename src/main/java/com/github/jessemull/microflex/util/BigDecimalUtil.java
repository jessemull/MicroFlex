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

/* -------------------------------- Package --------------------------------- */

package com.github.jessemull.microflex.util;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigDecimal; 
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

/**
 * This class safely converts (1) BigDecimal values to lists or arrays of another 
 * numeric type (2) a value from another numeric type to a BigDecimal. The 
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
 *
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
public class BigDecimalUtil {

    /**
     * Safely converts a number to a BigInteger. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Number    object to parse
     * @return             parsed object
     * @throws   ArithmeticException    on overflow
     */
    public static BigDecimal toBigDecimal(Number number) {

        /* Switch on class and convert to BigDecimal */
        
        String type = number.getClass().getSimpleName();
        BigDecimal parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) number;
                                 parsed = new BigDecimal(by.doubleValue());
                                 break; 
                                 
            case "Short":        Short sh = (Short) number;
                                 parsed = new BigDecimal(sh.doubleValue());
                                 break;
                                 
            case "Integer":      Integer in = (Integer) number;
                                 parsed = new BigDecimal(in.doubleValue());
                                 break;
                                 
            case "Long":         Long lo = (Long) number;
                                 parsed = new BigDecimal(lo.doubleValue());
                                 break;
                                 
            case "Float":        Float fl = (Float) number;
                                 parsed = new BigDecimal(fl.doubleValue());
                                 break;
                                 
            case "BigInteger":   parsed = new BigDecimal(((BigInteger) number));
                                 break;
                                  
            case "BigDecimal":   parsed = (BigDecimal) number;
                                 break;
            
            case "Double":       Double db = (Double) number;
                                 parsed = new BigDecimal(db);
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Safely converts an object to a BigInteger. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Object    object to parse
     * @return             parsed object
     * @throws   ArithmeticException    on overflow
     */
    public static BigDecimal toBigDecimal(Object obj) {

        /* Switch on class and convert to BigDecimal */
        
        String type = obj.getClass().getSimpleName();
        BigDecimal parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) obj;
                                 parsed = new BigDecimal(by.doubleValue());
                                 break; 
                                 
            case "Short":        Short sh = (Short) obj;
                                 parsed = new BigDecimal(sh.doubleValue());
                                 break;
                                 
            case "Integer":      Integer in = (Integer) obj;
                                 parsed = new BigDecimal(in.doubleValue());
                                 break;
                                 
            case "Long":         Long lo = (Long) obj;
                                 parsed = new BigDecimal(lo.doubleValue());
                                 break;
                                 
            case "Float":        Float fl = (Float) obj;
                                 parsed = new BigDecimal(fl.doubleValue());
                                 break;
                                 
            case "BigInteger":   parsed = new BigDecimal(((BigInteger) obj));
                                 break;
                                  
            case "BigDecimal":   parsed = (BigDecimal) obj;
                                 break;
            
            case "Double":       Double db = (Double) obj;
                                 parsed = new BigDecimal(db);
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Converts a list of BigDecimals to a list of bytes.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of bytes
     */
    public static List<Byte> toByteList(List<BigDecimal> list) {
        
        List<Byte> byteList = new ArrayList<Byte>();
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            byteList.add(val.byteValue());
        }
        
        return byteList;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of bytes.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of bytes
     */
    public static byte[] toByteArray(List<BigDecimal> list) {
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Byte[list.size()]));
        
    }
    
    /**
     * Converts a list of BigDecimals to a list of shorts.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of shorts
     */
    public static List<Short> toShortList(List<BigDecimal> list) {
        
        List<Short> shortList = new ArrayList<Short>();
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            shortList.add(val.shortValue());
        }
        
        return shortList;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of shorts.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of shorts
     */
    public static short[] toShortArray(List<BigDecimal> list) {
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Short[list.size()]));
        
    }
    
    /**
     * Converts a list of BigDecimals to a list of integers.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of shorts
     */
    public static List<Integer> toIntList(List<BigDecimal> list) {
        
        List<Integer> intList = new ArrayList<Integer>();
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.intOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            intList.add(val.intValue());
        }
        
        return intList;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of integers.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of integers
     */
    public static int[] toIntArray(List<BigDecimal> list) {    
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.intOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Integer [list.size()]));
        
    }
    
    /**
     * Converts a list of BigDecimals to a list of longs.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of longs
     */
    public static List<Long> toLongList(List<BigDecimal> list) {
        
        List<Long> longList = new ArrayList<Long>();
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.longOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            longList.add(val.longValue());
        }
        
        return longList;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of longs.
     * @param    List<BigDecimal>    list of longs
     * @return                       array of longs
     */
    public static long[] toLongArray(List<BigDecimal> list) {    
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.longOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Long[list.size()]));
        
    }
    
    /**
     * Converts a list of BigDecimals to a list of floats.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of floats
     */
    public static List<Float> toFloatList(List<BigDecimal> list) {
        
        List<Float> floatList = new ArrayList<Float>();
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.floatOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            floatList.add(val.floatValue());
        }
        
        return floatList;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of floats.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of floats
     */
    public static float[] toFloatArray(List<BigDecimal> list) {
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.floatOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Float[list.size()]));
        
    }
    
    /**
     * Converts a list of BigDecimals to a list of Doubles.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of doubles
     */
    public static List<Double> toDoubleList(List<BigDecimal> list) {
        
        List<Double> doubleList = new ArrayList<Double>();
        
        for(BigDecimal val : list) {
            if(!OverFlowUtil.doubleOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            doubleList.add(val.doubleValue());
        }
        
        return doubleList;
    }
    
    /**
     * Converts a list of BigDecimals to an array of doubles.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of doubles
     */
    public static double[] toDoubleArray(List<BigDecimal> list) {
        for(BigDecimal val : list) {
            if(!OverFlowUtil.doubleOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Double[list.size()]));
        
    }
     
    /**
     * Converts a list of BigDecimals to a a list of BigIntegers.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       list of BigIntegers
     */
    public static List<BigInteger> toBigIntList(List<BigDecimal> list) {
        
        List<BigInteger> toBigInt = new ArrayList<BigInteger>();
        
        for(BigDecimal bd : list) {
            toBigInt.add(bd.toBigInteger());
        }
        
        return toBigInt;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of BigIntegers.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of BigIntegers
     */
    public static BigInteger[] toBigIntArray(List<BigDecimal> list) {
        
        BigInteger[] toBigInt = new BigInteger[list.size()];
        
        for(int i = 0; i < toBigInt.length; i++) {
            toBigInt[i] = list.get(i).toBigInteger();
        }
        
        return toBigInt;
        
    }
    
    /**
     * Converts a list of BigDecimals to an array of BigDecimals.
     * @param    List<BigDecimal>    list of BigDecimals
     * @return                       array of BigDecimals
     */
    public static BigDecimal[] toBigDecimalArray(List<BigDecimal> list) {
        
        BigDecimal[] toBigDecimal = new BigDecimal[list.size()];
        
        for(int i = 0; i < toBigDecimal.length; i++) {
            toBigDecimal[i] = list.get(i);
        }
        
        return toBigDecimal;
        
    }

}

















