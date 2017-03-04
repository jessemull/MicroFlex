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
import java.util.HashMap;
import java.util.List;

/**
 * This class returns the mode in a data set for big decimal plate stacks, plates, 
 * wells and well sets. If two numbers have equal number of occurences it returns
 * the lesser number.
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
public class ModeBigDecimal extends DescriptiveStatisticBigDecimal {
	
	/**
     * Calculates the mode.
     * @param    List<BigDecimal>    the list
     * @return                       the result
     */
	public BigDecimal calculate(List<BigDecimal> list) {
    
		HashMap<BigDecimal, Integer> map = new HashMap<BigDecimal, Integer>();
		
		Integer max = 1;
		BigDecimal mode = list.get(0);
	
		for(int i = 0; i < list.size(); i++) {
			
			BigDecimal next = list.get(i);
			Integer value = map.get(next);
			
			if(value != null) {
				
				Integer count = ++value;
				map.put(next, count);

				if(count.doubleValue() == max.intValue() && next.intValue() < mode.intValue()) {
					mode = next;
				} else if(count.intValue() > max.intValue()) {
					max = count;
					mode = next;
				}
				
			} else {
				
				if(next.intValue() < mode.intValue() && max.intValue() == 1.0) {
				   mode = next;	
				}
				
				map.put(next, 1);	
			    
			}
		}

		return mode;
    }
    
    /**
     * Calculates the mode using the values between the beginning and ending indices.
     * @param    List<BigDecimal>    the list
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, int begin, int length) {
    	return calculate(list.subList(begin, begin + length));           
    }
  
}
