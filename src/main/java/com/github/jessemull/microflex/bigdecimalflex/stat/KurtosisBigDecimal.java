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

/* --------------------------------- Package -------------------------------- */

package com.github.jessemull.microflex.bigdecimalflex.stat;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

/**
 * This class calculates the kurtosis of BigDecimal plate stacks, plates, 
 * wells and well sets using the following unbiased formula:
 * 
 * <br><br>
 * 
 * [n * (n + 1) / (n - 1) * (n - 2) * (n - 3)] * SUMOF(Fourth Moment / (Standard Deviation)^2) where n is the number of values
 * 
 * <br><br>
 * 
 * Statistical operations can be performed on stacks, plates, sets and wells using
 * standard or aggregated functions. Standard functions calculate the desired
 * statistic for each well in the stack, plate or set. Aggregated functions aggregate
 * the values from all the wells in the stack, plate or set and perform the statistical
 * operation on the aggregated values. Both standard and aggregated functions can
 * be performed on a subset of data within the stack, plate, set or well.
 * 
 * <br><br>
 * 
 * The methods within the MicroFlex library are meant to be flexible and the
 * descriptive statistic object supports operations using a single stack, plate,
 * set or well as well as collections and arrays of stacks, plates, sets or wells. 
 *  
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Operation<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Beginning<br>Index<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Length of<br>Subset<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Input/Output</div></th>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Standard</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Accepts a single well, set, plate or stack as input</td>
 *             </tr>
 *             <tr>
 *                <td>Calculates the statistic for each well in a well, set, plate or stack</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Aggregated</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>+/-</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Accepts a single well/set/plate/stack or a collection/array of wells/sets/plates/stacks as input</td>
 *             </tr>
 *              <tr>
 *                <td>Aggregates the data from all the wells in a well/set/plate/stack and calculates the statistic using the aggregated data</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class KurtosisBigDecimal extends DescriptiveStatisticBigDecimalContext {
	
	/**
     * Calculates the kurtosis.
     * @param    List<BigDecimal>    the list
     * @return                       the result
     */
	public BigDecimal calculate(List<BigDecimal> list, MathContext mc) {
	
		if(list.size() <= 3) {
			throw new ArithmeticException("The kurtosis input list must contain " +
					"greater than three arguments.");
		}
		
		BigDecimal length = new BigDecimal(list.size() + "", mc);
	    BigDecimal mean = this.mean(list, mc);
        BigDecimal secondMoment = new BigDecimal("0", mc);
        BigDecimal fourthMoment = new BigDecimal("0", mc);

    	for(BigDecimal bd : list) {
    		
    		BigDecimal second = bd.subtract(mean, mc);
    		second = second.pow(2, mc);
    		secondMoment = secondMoment.add(second, mc);
    		
    		BigDecimal fourth = bd.subtract(mean, mc);
    		fourth = fourth.pow(4, mc);
    		fourthMoment = fourthMoment.add(fourth, mc);

    	}
		 
    	secondMoment = secondMoment.divide(length.subtract(BigDecimal.ONE, mc), mc);
    	secondMoment = secondMoment.pow(2, mc);
    	fourthMoment = fourthMoment.divide(secondMoment, mc);
    	
    	double n = length.doubleValue();
    	double coefficientDouble =  (n * (n + 1)) / ((n - 1) * (n - 2) * (n - 3));
    	double subtrahendDouble = (3 * Math.pow(n - 1, 2)) / ((n - 2) * (n - 3));
    	
    	BigDecimal coefficient = new BigDecimal(coefficientDouble + "", mc);  	
    	BigDecimal subtrahend = new BigDecimal(subtrahendDouble + "", mc);
    	
    	return fourthMoment.multiply(coefficient).subtract(subtrahend);
    }
    
    /**
     * Calculates the kurtosis of the values between the beginning and ending 
     * indices.
     * @param    List<BigDecimal>    the list
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @return                       the result
     */
    public BigDecimal  calculate(List<BigDecimal> list, int begin, int length, MathContext mc) {
        return calculate(list.subList(begin, begin + length), mc);
    }

	/**
     * Calculates the mean.
     * @param    List<BigDecimal>    the list
     * @return                       the result
     */
    private BigDecimal mean(List<BigDecimal> list, MathContext mc) {
        
        BigDecimal sum = BigDecimal.ZERO;
        
        for(BigDecimal db : list) {
            sum = sum.add(db);
        }
        
        return sum.divide(BigDecimal.valueOf(list.size()), mc);
    }
    
}
