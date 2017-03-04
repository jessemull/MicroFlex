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

package com.github.jessemull.microflex.integerflex.stat;

/* ----------------------------- Dependencies ------------------------------ */

import java.util.List;

/**
 * This class calculates the skewness of integer plate stacks, plates, 
 * wells and well sets using the following unbiased formula:
 * 
 * <br><br>
 * 
 * [n / (n - 1) * (n - 2)] * SUMOF(Third Moment / (Standard Deviation)^3) where n is the number of values
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
public class SkewnessInteger extends DescriptiveStatisticInteger {
	
	/**
     * Calculates the skewness.
     * @param    List<Double>    the list   
     * @return                   the result
     */
	public double calculate(List<Double> list) {
		
		int n = list.size();
		double coefficient = n / ((n - 1.0) * (n - 2.0));
        double mean = this.mean(list);
        double stdev = this.stdev(list, mean);
        double thirdMoment = this.thirdMoment(list, mean);

        return coefficient * thirdMoment / Math.pow(stdev, 3);
    }

    /**
     * Calculates the skewness of the values between the beginning and ending 
     * indices.
     * @param    List<Double>    the list
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @return                   the result
     */
    public double  calculate(List<Double> list, int begin, int length) {
        return calculate(list.subList(begin, begin + length));
    }
    
    /**
     * Calculates the mean.
     * @param    List<Double>    the list
     * @return                   the result
     */
    private double mean(List<Double> list) {
        
        double sum = 0;
        
        for(double db : list) {
            sum += db;
        }
        
        return sum / list.size();
    }

    /**
     * Returns the standard deviation.
     * @param    List<Double>    the list
     * @return                   the result
     */
    private double stdev(List<Double> list, double mean) {
    	
    	double sum = 0;
    	
    	for(double db : list) {
    		sum += Math.pow(db - mean, 2);
    	}
    	
    	sum /= list.size() - 1;

    	return Math.sqrt(sum);
    }
    
    /**
     * Returns the third moment.
     * @param    List<Double>    the list
     * @return                   the result
     */
    private double thirdMoment(List<Double> list, double mean) {

    	double sum = 0;
    	
    	for(double db : list) {
    		sum += Math.pow(db - mean, 3);
    	}

    	return sum;
    }
}
