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

/**
 * This class checks for overflow when casting a number to another type. The
 * class provides static methods to check overflow for all Java primitives as
 * well as two immutable data types:
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
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class OverFlowUtil {
    
    /* Maximum and minimum values for checking overflow when casting to BigDecimal */
    
    private static final BigDecimal BIGDECIMAL_BYTE_MAX = new BigDecimal(Byte.MAX_VALUE);
    private static final BigDecimal BIGDECIMAL_BYTE_MIN = new BigDecimal(Byte.MIN_VALUE);
    private static final BigDecimal BIGDECIMAL_SHORT_MAX = new BigDecimal(Short.MAX_VALUE);
    private static final BigDecimal BIGDECIMAL_SHORT_MIN = new BigDecimal(Short.MIN_VALUE);
    private static final BigDecimal BIGDECIMAL_INT_MAX = new BigDecimal(Integer.MAX_VALUE);
    private static final BigDecimal BIGDECIMAL_INT_MIN = new BigDecimal(Integer.MIN_VALUE);
    private static final BigDecimal BIGDECIMAL_LONG_MAX = new BigDecimal(Long.MAX_VALUE);
    private static final BigDecimal BIGDECIMAL_LONG_MIN = new BigDecimal(Long.MIN_VALUE);
    private static final BigDecimal BIGDECIMAL_FLOAT_MAX = new BigDecimal(Float.MAX_VALUE);
    private static final BigDecimal BIGDECIMAL_FLOAT_MIN = new BigDecimal(Float.MIN_VALUE);
    private static final BigDecimal BIGDECIMAL_DOUBLE_MAX = new BigDecimal(Double.MAX_VALUE);
    private static final BigDecimal BIGDECIMAL_DOUBLE_MIN = new BigDecimal(Double.MIN_VALUE);
    
    /* Maximum and minimum values for checking overflow when casting to BigInteger */
    
    private static final BigInteger BIGINT_BYTE_MAX = new BigInteger(BIGDECIMAL_BYTE_MAX.toString());
    private static final BigInteger BIGINT_BYTE_MIN = new BigInteger(BIGDECIMAL_BYTE_MIN.toString());
    private static final BigInteger BIGINT_SHORT_MAX = new BigInteger(BIGDECIMAL_SHORT_MAX.toString());
    private static final BigInteger BIGINT_SHORT_MIN = new BigInteger(BIGDECIMAL_SHORT_MIN.toString());
    private static final BigInteger BIGINT_INT_MAX = new BigInteger(BIGDECIMAL_INT_MAX.toString());
    private static final BigInteger BIGINT_INT_MIN = new BigInteger(BIGDECIMAL_INT_MIN.toString());
    private static final BigInteger BIGINT_LONG_MAX = new BigInteger(BIGDECIMAL_LONG_MAX.toString());
    private static final BigInteger BIGINT_LONG_MIN = new BigInteger(BIGDECIMAL_LONG_MIN.toString());
    private static final BigInteger BIGINT_FLOAT_MAX = new BigInteger(BIGDECIMAL_FLOAT_MAX.toString());
    private static final BigInteger BIGINT_FLOAT_MIN = new BigInteger(BIGDECIMAL_FLOAT_MIN.toString());
    private static final BigInteger BIGINT_DOUBLE_MAX = new BigInteger(BIGDECIMAL_DOUBLE_MAX.toString());
    private static final BigInteger BIGINT_DOUBLE_MIN = new BigInteger(BIGDECIMAL_DOUBLE_MIN.toString());

    /* --------- Methods for Checking Byte Overflow ----------*/
    
    /**
     * Checks for overflow when converting a double to a byte.
     * @param    Double    the input value
     * @return             true if no overflow occurs
     */
    public static boolean byteOverflow(Double value) {
        return value < Byte.MIN_VALUE || value > Byte.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a float to a byte.
     * @param    Float    the input value
     * @return            true if no overflow occurs
     */
    public static boolean byteOverflow(Float value) {
        return value < Byte.MIN_VALUE || value > Byte.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a long to a byte.
     * @param    Long    the input value
     * @return           true if no overflow occurs
     */
    public static boolean byteOverflow(Long value) {
        return value < Byte.MIN_VALUE || value > Byte.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting an integer to a byte.
     * @param    Integer    the input value
     * @return              true if no overflow occurs
     */
    public static boolean byteOverflow(Integer value) {
        return value < Byte.MIN_VALUE || value > Byte.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a short to a byte.
     * @param    Short    the input value
     * @return            true if no overflow occurs
     */
    public static boolean byteOverflow(Short value) {
        return value < Byte.MIN_VALUE || value > Byte.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigDecimal to a byte.
     * @param    BigDecimal    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean byteOverflow(BigDecimal value) {
        return value.compareTo(BIGDECIMAL_BYTE_MAX) > 0 || value.compareTo(BIGDECIMAL_BYTE_MIN) < 0 ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigInteger to a byte.
     * @param    BigInteger    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean byteOverflow(BigInteger value) {
        return value.compareTo(BIGINT_BYTE_MAX) > 0 || value.compareTo(BIGINT_BYTE_MIN) < 0 ? false : true;
    }
    
    /* -------- Methods for Checking Short Overflow ----------*/
    
    /**
     * Checks for overflow when converting a double to a short.
     * @param    Double    the input value
     * @return             true if no overflow occurs
     */
    public static boolean shortOverflow(Double value) {
        return value < Short.MIN_VALUE || value > Short.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a float to a short.
     * @param    Float    the input value
     * @return            true if no overflow occurs
     */
    public static boolean shortOverflow(Float value) {
        return value < Short.MIN_VALUE || value > Short.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a long to a short.
     * @param    Long    the input value
     * @return           true if no overflow occurs
     */
    public static boolean shortOverflow(Long value) {
        return value < Short.MIN_VALUE || value > Short.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting an integer to a short.
     * @param    Integer    the input value
     * @return              true if no overflow occurs
     */
    public static boolean shortOverflow(Integer value) {
        return value < Short.MIN_VALUE || value > Short.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a short to a short.
     * @param    Short    the input value
     * @return            true if no overflow occurs
     */
    public static boolean shortOverflow(Short value) {
        return value < Short.MIN_VALUE || value > Short.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigDecimal to a short.
     * @param    BigDecimal    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean shortOverflow(BigDecimal value) {
        return value.compareTo(BIGDECIMAL_SHORT_MAX) > 0 || value.compareTo(BIGDECIMAL_SHORT_MIN) < 0 ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigInteger to a short.
     * @param    BigInteger    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean shortOverflow(BigInteger value) {
        return value.compareTo(BIGINT_SHORT_MAX) > 0 || value.compareTo(BIGINT_SHORT_MIN) < 0 ? false : true;
    }
    
    /* -------- Methods for Checking Integer Overflow ---------*/
    
    /**
     * Checks for overflow when converting a double to an integer.
     * @param    Double    the input value
     * @return             true if no overflow occurs
     */
    public static boolean intOverflow(Double value) {
        return value < Integer.MIN_VALUE || value > Integer.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a float to an integer.
     * @param    Float    the input value
     * @return            true if no overflow occurs
     */
    public static boolean intOverflow(Float value) {
        return value < Integer.MIN_VALUE || value > Integer.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a long to an integer.
     * @param    Long    the input value
     * @return           true if no overflow occurs
     */
    public static boolean intOverflow(Long value) {
        return value < Integer.MIN_VALUE || value > Integer.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigDecimal to an integer.
     * @param    BigDecimal    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean intOverflow(BigDecimal value) {
        return value.compareTo(BIGDECIMAL_INT_MAX) > 0 || value.compareTo(BIGDECIMAL_INT_MIN) < 0 ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigInteger to an integer.
     * @param    BigInteger    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean intOverflow(BigInteger value) {
        return value.compareTo(BIGINT_INT_MAX) > 0 || value.compareTo(BIGINT_INT_MIN) < 0 ? false : true;
    }
    
    /* ---------- Methods for Checking Long Overflow ----------*/
    
    /**
     * Checks for overflow when converting a double to an long.
     * @param    Double    the input value
     * @return             true if no overflow occurs
     */
    public static boolean longOverflow(Double value) {
        return value < Long.MIN_VALUE || value > Long.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a float to an long.
     * @param    Float    the input value
     * @return            true if no overflow occurs
     */
    public static boolean longOverflow(Float value) {
        return value < Long.MIN_VALUE || value > Long.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigDecimal to an long.
     * @param    BigDecimal    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean longOverflow(BigDecimal value) {
        return value.compareTo(BIGDECIMAL_LONG_MAX) > 0 || value.compareTo(BIGDECIMAL_LONG_MIN) < 0 ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigInteger to an long.
     * @param    BigInteger    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean longOverflow(BigInteger value) {
        return value.compareTo(BIGINT_LONG_MAX) > 0 || value.compareTo(BIGINT_LONG_MIN) < 0 ? false : true;
    }
   
    /* ---------- Methods for Checking Float Overflow ----------*/
    
    /**
     * Checks for overflow when converting a double to an float.
     * @param    Double    the input value
     * @return             true if no overflow occurs
     */
    public static boolean floatOverflow(Double value) {
        return value < Float.MIN_VALUE || value > Float.MAX_VALUE ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigDecimal to an float.
     * @param    BigDecimal    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean floatOverflow(BigDecimal value) {
        return value.compareTo(BIGDECIMAL_FLOAT_MAX) > 0 || value.compareTo(BIGDECIMAL_FLOAT_MIN) < 0 ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigInteger to an float.
     * @param    BigInteger    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean floatOverflow(BigInteger value) {
        return value.compareTo(BIGINT_FLOAT_MAX) > 0 || value.compareTo(BIGINT_FLOAT_MIN) < 0 ? false : true;
    }
    
    /* ---------- Methods for Checking Double Overflow ---------*/
    
    /**
     * Checks for overflow when converting a BigDecimal to an double.
     * @param    BigDecimal    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean doubleOverflow(BigDecimal value) {
        return value.compareTo(BIGDECIMAL_DOUBLE_MAX) > 0 || value.compareTo(BIGDECIMAL_DOUBLE_MIN) < 0 ? false : true;
    }
    
    /**
     * Checks for overflow when converting a BigInteger to an double.
     * @param    BigInteger    the input value
     * @return                 true if no overflow occurs
     */
    public static boolean doubleOverflow(BigInteger value) {
        return value.compareTo(BIGINT_DOUBLE_MAX) > 0 || value.compareTo(BIGINT_DOUBLE_MIN) < 0 ? false : true;
    }
    
    /**
     * Throws an arithmetic exception when overflow occurs.
     * @param    Number    the number
     * @throws   ArithmeticException    on overflow
     */
    public static void overflowError(Number val) {
        throw new ArithmeticException("Overflow casting " + val + " to a " + 
                                      val.getClass().getSimpleName());
    }
}
