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

package com.github.jessemull.microflex.bigintegerflex.stat;

/* ----------------------------- Dependencies ------------------------------ */	

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

/**
 * This class calculates the sample variance of BigInteger plate stacks, plates, 
 * wells and well sets as the sum of the second moment divided by the number of 
 * values less one.
 * 
 * <br><br>
 * 
 * From wikipedia: a weight function is a mathematical device used when performing 
 * a sum, integral, or average to give some elements more "weight" or influence on 
 * the result than other elements in the same set. In statistics a weighted function 
 * is often used to correct bias. The weighted statistic class implements a weighted 
 * function by accepting an array of values as weights. The values in each well of 
 * the stack, plate, set or well are multiplied by the values within the double 
 * array prior to the statistical calculation.
 * 
 * <br><br>
 * 
 * Weighted statistical operations can be performed on stacks, plates, sets and 
 * wells using standard or aggregated functions. Standard functions calculate the 
 * desired statistic for each well in the stack, plate or set. Aggregated functions 
 * aggregate the values from all the wells in the stack, plate or set and perform 
 * the weighted statistical operation on the aggregated values. Both standard and 
 * aggregated functions can be performed on a subset of data within the stack, 
 * plate, set or well.
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
 *                <td style="padding-bottom: 7px;">Accepts a single well, set, plate or stack and an array of weights as input</td>
 *             </tr>
 *             <tr>
 *                <td>Multiplies the values in each well of a well, set, plate or stack by the values
 *                <br> in the weights array then calculates the statistic using the weighted values</td>
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
 *                <td style="padding-bottom: 7px;">Accepts a single well/set/plate/stack or a collection/array of wells/sets/plates/stacks 
 *                <br>and an array of weights as input</td>
 *             </tr>
 *              <tr>
 *                <td>Multiplies the values in each well of a well, set, plate or stack by the values in the 
 *                <br>weights array, aggregates the data from all the wells in the well/set/plate/stack then 
 *                <br>calculates the statistic using the aggregated weighted values</td>
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
public class SampleVarianceBigInteger extends DescriptiveStatisticBigIntegerWeightsContext {
	
	/**
     * Calculates the sample variance.
     * @param    List<BigDecimal>    the list
     * @param    MathContext         the math context
     * @return                       the result
     */
	public BigDecimal calculate(List<BigDecimal> list, MathContext mc) {
		
	    BigDecimal mean = this.mean(list, mc);
        BigDecimal sum = BigDecimal.ZERO;
        
        for(BigDecimal db : list) {
           sum =  sum.add(db.subtract(mean, mc).pow(2, mc), mc);
        }
        
        return sum.divide(BigDecimal.valueOf(list.size() - 1), mc); 
	    
    }
    
    /**
     * Calculates the weighted sample variance.
     * @param    List<BigDecimal>    the list
     * @param    double[]            weights for the data set
     * @param    MathContext         the math context
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, double[] weights, MathContext mc) {
        
    	List<BigDecimal> weighted = new ArrayList<BigDecimal>();
    	
    	for(int i = 0; i < list.size(); i++) {
    		weighted.add(list.get(i).multiply(new BigDecimal(weights[i] + ""), mc));
    	}
    	
    	BigDecimal mean = this.mean(weighted, mc);
        BigDecimal sum = BigDecimal.ZERO;
        
        for(BigDecimal db : weighted) {
           sum =  sum.add(db.subtract(mean, mc).pow(2, mc), mc);
        }
        
        return sum.divide(BigDecimal.valueOf(list.size() - 1), mc); 
        
    }
    
    /**
     * Calculates the sample variance of the values between the beginning 
     * and ending indices.
     * @param    List<BigDecimal>    the list
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @param    MathContext         the math context
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, int begin, int length, MathContext mc) {
        return calculate(list.subList(begin, begin + length), mc);    
    }
    
    /**
     * Calculates the weighted sample variance of the values between the 
     * beginning and ending indices.
     * @param    List<BigDecimal>    the list
     * @param    double[]            weights of the data set
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @param    MathContext         the math context
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, double[] weights, int begin, int length, MathContext mc) {
    	return calculate(list.subList(begin, begin + length), weights, mc);        
    }
    
    /**
     * Calculates the mean.
     * @param    List<BigDecimal>    the list
     * @param    MathContext         the math context
     * @return                       the result
     */
    public BigDecimal mean(List<BigDecimal> list, MathContext mc) {
        
        BigDecimal sum = BigDecimal.ZERO;
        
        for(BigDecimal db : list) {
            sum = sum.add(db, mc);
        }
        
        return sum.divide(BigDecimal.valueOf(list.size()), mc);
        
    }
    
}
