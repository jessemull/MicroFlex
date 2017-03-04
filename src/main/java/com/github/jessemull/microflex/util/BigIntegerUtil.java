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
 * This class safely converts (1) BigInteger values to lists or arrays of another 
 * numeric type (2) a value from another numeric type to a BigInteger. The 
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
public class BigIntegerUtil {

    /**
     * Safely converts a number to a BigInteger. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Number    object to parse
     * @return             parsed object
     * @throws   ArithmeticException    on overflow
     */
    public static BigInteger toBigInteger(Number number) {

        /* Switch on class and convert to BigInteger */
        
        String type = number.getClass().getSimpleName();
        BigInteger parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) number;
                                 parsed = new BigInteger(by.toString());
                                 break; 
                                 
            case "Short":        Short sh = (Short) number;
                                 parsed = new BigInteger(sh.toString());
                                 break;
                                 
            case "Integer":      Integer in = (Integer) number;
                                 parsed = new BigInteger(in.toString());
                                 break;
                                 
            case "Long":         Long lo = (Long) number;
                                 parsed = new BigInteger(lo.toString());
                                 break;
                                 
            case "Float":        Float fl = (Float) number;
                                 parsed = new BigInteger(fl.toString());
                                 break;
                                 
            case "BigInteger":    parsed = (BigInteger) number;
                                  break;
                                  
            case "BigDecimal":   parsed = ((BigDecimal) number).toBigInteger();
                                 break;
            
            case "Double":       Double db = (Double) number;
                                 parsed = new BigInteger(db.toString());
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Safely converts a number to a BigInteger. Loss of precision may occur. Throws
     * an arithmetic exception upon overflow.
     * @param    Object    object to parse
     * @return             parsed object
     * @throws   ArithmeticException    on overflow
     */
    public static BigInteger toBigInteger(Object obj) {

        /* Switch on class and convert to BigInteger */
        
        String type = obj.getClass().getSimpleName();
        BigInteger parsed;
        
        switch(type) {
            
            case "Byte":         Byte by = (Byte) obj;
                                 parsed = new BigInteger(by.toString());
                                 break; 
                                 
            case "Short":        Short sh = (Short) obj;
                                 parsed = new BigInteger(sh.toString());
                                 break;
                                 
            case "Integer":      Integer in = (Integer) obj;
                                 parsed = new BigInteger(in.toString());
                                 break;
                                 
            case "Long":         Long lo = (Long) obj;
                                 parsed = new BigInteger(lo.toString());
                                 break;
                                 
            case "Float":        Float fl = (Float) obj;
                                 parsed = new BigInteger(fl.toString());
                                 break;
                                 
            case "BigInteger":    parsed = (BigInteger) obj;
                                  break;
                                  
            case "BigDecimal":   parsed = ((BigDecimal) obj).toBigInteger();
                                 break;
            
            case "Double":       Double db = (Double) obj;
                                 parsed = new BigInteger(db.toString());
                                 break;
                
            default: throw new IllegalArgumentException("Invalid type: " + type + "\nData values " +
                                                        "must extend the abstract Number class.");
            
        }
        
        return parsed;
    }
    
    /**
     * Converts a list of BigIntegers to a list of bytes.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       list of bytes
     */
    public static List<Byte> toByteList(List<BigInteger> list) {
        
        List<Byte> byteList = new ArrayList<Byte>();
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            byteList.add(val.byteValue());
        }
        
        return byteList;
        
    }
    
    /**
     * Converts a list of BigIntegers to an array of bytes.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of bytes
     */
    public static byte[] toByteArray(List<BigInteger> list) {
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.byteOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Byte[list.size()]));
        
    }
    
    /**
     * Converts a list of BigIntegers to a list of shorts.
     * @param    List<BigInteger>    list of BigIntegers
     * @return   List<Short          >     list of shorts
     */
    public static List<Short> toShortList(List<BigInteger> list) {
        
        List<Short> shortList = new ArrayList<Short>();
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            shortList.add(val.shortValue());
        }
        
        return shortList;
        
    }
    
    /**
     * Converts a list of BigIntegers to an array of shorts.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of shorts
     */
    public static short[] toShortArray(List<BigInteger> list) {
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.shortOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Short[list.size()]));
        
    }
    
    /**
     * Converts a list of BigIntegers to a list of integers.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       list of shorts
     */
    public static List<Integer> toIntList(List<BigInteger> list) {
        
        List<Integer> intList = new ArrayList<Integer>();
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.intOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            intList.add(val.intValue());
        }
        
        return intList;
        
    }
    
    /**
     * Converts a list of BigIntegers to an array of integers.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of integers
     */
    public static int[] toIntArray(List<BigInteger> list) {    
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.intOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Integer [list.size()]));
        
    }
    
    /**
     * Converts a list of BigIntegers to a list of longs.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       list of longs
     */
    public static List<Long> toLongList(List<BigInteger> list) {
        
        List<Long> longList = new ArrayList<Long>();
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.longOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            longList.add(val.longValue());
        }
        
        return longList;
        
    }
    
    /**
     * Converts a list of BigIntegers to an array of longs.
     * @param    List<BigInteger>    list of longs
     * @return                       array of longs
     */
    public static long[] toLongArray(List<BigInteger> list) {    
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.longOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Long[list.size()]));
        
    }
    
    /**
     * Converts a list of BigIntegers to a list of floats.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       list of floats
     */
    public static List<Float> toFloatList(List<BigInteger> list) {
        
        List<Float> floatList = new ArrayList<Float>();
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.floatOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            floatList.add(val.floatValue());
        }
        
        return floatList;
        
    }
    
    /**
     * Converts a list of BigIntegers to an array of floats.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of floats
     */
    public static float[] toFloatArray(List<BigInteger> list) {
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.floatOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Float[list.size()]));
        
    }
    
    /**
     * Converts a list of BigIntegers to a list of Doubles.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       list of doubles
     */
    public static List<Double> toDoubleList(List<BigInteger> list) {
        
        List<Double> doubleList = new ArrayList<Double>();
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.doubleOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
            doubleList.add(val.doubleValue());
        }
        
        return doubleList;
    }
    
    /**
     * Converts a list of BigIntegers to an array of doubles.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of doubles
     */
    public static double[] toDoubleArray(List<BigInteger> list) {
        
        for(BigInteger val : list) {
            if(!OverFlowUtil.doubleOverflow(val)) {
                OverFlowUtil.overflowError(val);
            }
        }
        
        return ArrayUtils.toPrimitive(list.toArray(new Double[list.size()]));
        
    }
       
    /**
     * Converts a list of BigIntegers to an array of BigIntegers.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of BigIntegers
     */
    public static BigInteger[] toBigIntArray(List<BigInteger> list) {
        
        BigInteger[] toBigInt = new BigInteger[list.size()];
        
        for(int i = 0; i < toBigInt.length; i++) {
            toBigInt[i] = list.get(i);
        }
        
        return toBigInt;
        
    }
    
    /**
     * Converts a list of BigIntegers to a list of BigDecimals.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       list of BigDecimals
     */
    public static List<BigDecimal> toBigDecimalList(List<BigInteger> list) {
        
        List<BigDecimal> bigDecimalList = new ArrayList<BigDecimal>();
        
        for(BigInteger val : list) {
            bigDecimalList.add(new BigDecimal(val));
        }
        
        return bigDecimalList;
        
    }
    
    
    /**
     * Converts a list of BigIntegers to an array of BigDecimals.
     * @param    List<BigInteger>    list of BigIntegers
     * @return                       array of BigDecimal
     */
    public static BigDecimal[] toBigDecimalArray(List<BigInteger> list) {
        
        BigDecimal[] toBigDecimal = new BigDecimal[list.size()];
        
        for(int i = 0; i < toBigDecimal.length; i++) {
            toBigDecimal[i] = new BigDecimal(list.get(i));
        }
        
        return toBigDecimal;
        
    }

}

















