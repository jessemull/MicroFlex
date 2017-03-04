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

/* -------------------------------- Package -------------------------------- */

package com.github.jessemull.microflex.bigdecimalflex.stat;

/* ----------------------------- Dependencies ------------------------------ */

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collections;
import java.util.List;

/**
 * This class calculates quantiles for big decimal plate stacks, plates, 
 * wells and well sets using the following steps:
 * 
 * <br><br>
 * 
 * <ol type="1"> 
 *    <li style="margin-bottom: 10px;">Calculate R as Q x (N + 1) where N is the number of values and Q is the desired quantile</li>
 *    <li style="margin-bottom: 10px;">Define IR as the integer portion of R</li>
 *    <li style="margin-bottom: 10px;">Define FR as the fractional portion of R</li>
 *    <li style="margin-bottom: 10px;">Find the scores with Rank IR and with Rank IR + 1</li>
 *    <li style="margin-bottom: 10px;">Interpolate using the formula (Score IR+1 - Score IR) * FR + Score IR and return the result</li>
 * </ol>
 * 
 * <br>
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
public class QuantileBigDecimal extends QuantileStatisticBigDecimalsContext {

    /* --------- Methods for calculating the quantile of a data set --------  */
    
    /**
     * Calculates the quantile.
     * @param    List<BigDecimal>    the list
     * @param    double              the quantile
     * @param    MathContext         the math context
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, double p, MathContext mc) {
        
        int n = list.size();
    	
    	if(n == 1) {
    		return list.get(0);
    	}
    	
        Collections.sort(list);        
        double pos = (p * (n + 1));
        
        if(pos < 1) {
        	return list.get(0);
        }
        
        if(pos >= n) {
        	return list.get(list.size() - 1);
        }
        
        if(pos == Math.floor(pos) && !Double.isInfinite(pos)) {       
        	return list.get((int) pos - 1);
        }
        	
        int lowerIndex = (int) Math.floor(pos) - 1;
        int upperIndex = lowerIndex + 1;
        
        BigDecimal lower = list.get(lowerIndex);
        BigDecimal upper = list.get(upperIndex);      
        BigDecimal d = new BigDecimal((pos - 1 - lowerIndex) + "");
        
        return upper.subtract(lower).multiply(d).add(lower);
    };
    
    /**
     * Calculates the quantile of the values between the beginning and ending 
     * indices.
     * @param    List<Double>    the list
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @param    double          the quantile
     * @param    MathContext     the math context
     * @return                   the result
     */
    public BigDecimal calculate(List<BigDecimal> list, int begin, int length, double p, MathContext mc) {
        return this.calculate(list.subList(begin, begin + length), p, mc);
    };
 
}
