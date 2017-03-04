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

package com.github.jessemull.microflex.bigdecimalflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class performs addition operations for BigDecimal plate stacks, plates, 
 * wells and well sets.
 * 
 * Operations can be performed on stacks, plates, sets or wells of uneven length
 * using standard or strict functions. Standard functions treat all values missing
 * from a data set as zeroes and combine all stacks, plates, sets and wells from 
 * both input objects. Strict functions omit all values, stacks, plates, wells 
 * and sets missing from one of the input objects:
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Operation<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Output</div></th>
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
 *                <td>Treats missing values as zeroes</td>
 *             </tr>
 *             <tr>
 *                <td>Combines stacks, plates, sets, wells and values from both input objects</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Strict</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Omits all missing values</td>
 *             </tr>
 *              <tr>
 *                <td>Combines stacks, plates, sets, wells and values present in both input objects only</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 * </table>
 * 
 * The functions within the MicroFlex library are designed to be flexible and classes 
 * extending the math operation binary object support operations using two stacks, 
 * plates, sets and well objects as input. In addition, they support operations using 
 * a single stack, plate, set or well object and a collection, array or constant, and
 * also allow the developer to limit the operation to a subset of data:
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Input 1<br><div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Input 2</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Beginning<br>Index</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Length of<br>Subset</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Operation</div></th>
 *    <tr>
 *       <td>Well</td>
 *       <td>Well</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the two wells</td>
 *    </tr>
 *    <tr>
 *       <td>Well</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in the well</td>
 *    </tr>
 *    <tr>
 *       <td>Well</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in the well</td>
 *    </tr>
 *    <tr>
 *       <td>Well</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant value and each value in the well</td>
 *    </tr>

 *    <tr></tr>
 *    <tr></tr>

 *    <tr>
 *       <td>Set</td>
 *       <td>Set</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation on the values in each matching pair of wells in the two sets</td>
 *    </tr>
 *    <tr>
 *       <td>Set</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in each well of the set</td>
 *    </tr>
 *    <tr>
 *       <td>Set</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in each well of the set</td>
 *    </tr>
 *    <tr>
 *       <td>Set</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant and each value in each well of the set</td> 
 *    </tr>

 *    <tr></tr>
 *    <tr></tr>

 *    <tr>
 *       <td>Plate</td>
 *       <td>Plate</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation on the values in each matching pair of wells in the two plates</td>
 *    </tr>
 *    <tr>
 *       <td>Plate</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in each well of the plate</td>
 *    </tr>
 *    <tr>
 *       <td>Plate</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in each well of the plate</td>
 *    </tr>
 *    <tr>
 *       <td>Plate</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant and each value in each well of the plate</td>
 *    </tr>

 *    <tr></tr>
 *    <tr></tr>
 *    
 *    <tr>
 *       <td>Stack</td>
 *       <td>Stack</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation on the values in each matching pair of wells in each matching plate in the stack</td>
 *    </tr>
 *    <tr>
 *       <td>Stack</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in each well of each plate in the stack</td>
 *    </tr>
 *    <tr>
 *       <td>Stack</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in each well of each plate in the stack</td>
 *    </tr>
 *    <tr>
 *       <td>Stack</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant and each value in each well of each plate in the stack</td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class AdditionBigDecimal extends MathOperationBigDecimalBinary {
	
	/**
	 * Adds the lists and returns the result. Missing data points due to uneven 
	 * list lengths are treated as zeroes.
	 * @param    List<BigDecimal> list1    the first list
	 * @param    List<BigDecimal> list2    the second list
	 * @return             the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list1, List<BigDecimal> list2) {
		
		List<BigDecimal> largest = null;
		List<BigDecimal> smallest = null;
		
		if(list1.size() != list2.size()) {
			largest = list1.size() > list2.size() ? list1 : list2;
			smallest = list1.size() < list2.size() ? list1 : list2;
		} else {
			smallest = list1;
			largest = list2;
		}
		
		List<BigDecimal> result = new ArrayList<BigDecimal>();

		for(int i = 0; i < smallest.size(); i++) {
			result.add(smallest.get(i).add(largest.get(i)));
		}

		for(int i = smallest.size(); i < largest.size(); i++) {
			result.add(largest.get(i));
		}

		return result;
	}

	/**
	 * Adds the lists and returns the result. Missing data points due to uneven 
	 * list lengths are omitted.
	 * @param    List<BigDecimal> list1    the first list
	 * @param    List<BigDecimal> list2    the second list
	 * @return             the result
	 * @override
	 */
	public List<BigDecimal> calculateStrict(List<BigDecimal> list1, List<BigDecimal> list2) {
		
		List<BigDecimal> smallest = list1.size() < list2.size() ? list1 : list2;
		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(int i = 0; i < smallest.size(); i++) {
			result.add(list1.get(i).add(list2.get(i)));
		}
		
		return result;
	}

	/**
	 * Adds the lists and returns the result using the values between the indices.
	 * Missing data points due to uneven list lengths are treated as zeroes.
	 * @param    List<BigDecimal> list1    the first list
	 * @param    List<BigDecimal> list2    the second list
	 * @param    int begin                 beginning index of the subset
	 * @param    int length                length of the subset
	 * @return             the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list1, List<BigDecimal> list2, int begin, int length) {

		
		List<BigDecimal> largest = list1.size() > list2.size() ? list1 : list2;
		List<BigDecimal> smallest = list1.size() < list2.size() ? list1 : list2;		
		List<BigDecimal> result = new ArrayList<BigDecimal>();

		int end1 = begin + length;
		int end2 = end1;
		
		if(end1 > smallest.size()) {
			end1 = smallest.size();
		}

		for(int i = begin; i < end1; i++) {
			result.add(list1.get(i).add(list2.get(i)));
		}
		
		for(int i = end1; i < end2; i++) {
			result.add(largest.get(i));
	    }
		
		return result;
	}

	/**
	 * Adds the lists and returns the result using the values between the indices.
	 * Missing data points due to uneven list lengths are omitted.
	 * @param    List<BigDecimal> list1    the first list
	 * @param    List<BigDecimal> list2    the second list
	 * @param    int begin                 beginning index of the subset
	 * @param    int length                length of the subset
	 * @return             the result
	 * @override
	 */
	public List<BigDecimal> calculateStrict(List<BigDecimal> list1,
			List<BigDecimal> list2, int begin, int length) {

		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(int i = begin; i < begin + length; i++) {
			result.add(list1.get(i).add(list2.get(i)));
		}
		
		return result;
	}

	/**
	 * Adds the constant to each value in the list.
	 * @param    List<BigDecimal> list    the list
	 * @param    BigDecimal constant      the constant value
	 * @return            the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list, BigDecimal constant) {
		
		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(BigDecimal bd : list) {
			result.add(bd.add(constant));
		}
		
		return result;
		
	}

	/**
	 * Adds the list values and the values within the array. Missing data
	 * points due to uneven list and array sizes are treated as zeroes.
	 * @param    List<BigDecimal> list    the list
	 * @param    BigDecimal[] array       the array
	 * @return            the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list, BigDecimal[] array) {
		
		int index;
	    List<BigDecimal> result = new ArrayList<BigDecimal>();
	
		for(index = 0; index < list.size() && index < array.length; index++) {
			result.add(list.get(index).add(array[index]));
		}
		
		for(int i = index; i < list.size(); i++) {
			result.add(list.get(i));
		}
		
		for(int i = index; i < array.length; i++) {
			result.add(array[i]);
		}
		
		return result;
	}

	/**
	 * Adds the list values and the values within the array using the values
	 * between the indices. Missing data points due to uneven list and array
	 * sizes are treated as zeroes.
	 * @param    List<BigDecimal> list    the list
	 * @param    BigDecimal[] array       the array
	 * @param    int begin                beginning index of the subset
	 * @param    int length               length of the subset
	 * @return            the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list, BigDecimal[] array,
			int begin, int length) {
		
		int index;
		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(index = begin; index < list.size() && index < array.length && 
				index < begin + length; index++) {
			result.add(list.get(index).add(array[index]));
		}
		
		for(int i = index; i < list.size() && i < begin + length; i++) {
			result.add(list.get(i));
		}
		
		for(int i = index; i < array.length && i < begin + length; i++) {
			result.add(array[i]);
		}
		
		return result;
		
	}

	/**
	 * Adds the list values and the values within the collection. Missing 
	 * data points due to uneven list and collection sizes are treated as zeroes.
	 * @param    List<BigDecimal> list                the list
	 * @param    Collection<BigDecimal> collection    the collection of values
	 * @return                        the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list, Collection<BigDecimal> collection) {
		
		int index;
		Iterator<BigDecimal> iter = collection.iterator();
	    List<BigDecimal> result = new ArrayList<BigDecimal>();
	
		for(index = 0; index < list.size() && index < collection.size(); index++) {
			result.add(list.get(index).add(iter.next()));
		}
		
		for(int i = index; i < list.size(); i++) {
			result.add(list.get(i));
		}
		
		while(iter.hasNext()) {
			result.add(iter.next());
		}
		
		return result;
	}

	/**
	 * Adds the list values and the values within the collection. Missing 
	 * data points due to uneven list and collection sizes are treated as zeroes.
	 * @param    List<BigDecimal> list                the list
	 * @param    Collection<BigDecimal> collection    the collection of values
	 * @param    int begin                            beginning index of the subset
	 * @param    int length                           length of the subset
	 * @return                        the result
	 * @override
	 */
	public List<BigDecimal> calculate(List<BigDecimal> list,
			Collection<BigDecimal> collection, int begin, int length) {

		int index;
		Iterator<BigDecimal> iter = collection.iterator();
	    List<BigDecimal> result = new ArrayList<BigDecimal>();

	    for(int i = 0; i < begin; i++) {
	    	iter.next();
	    }

	    for(index = begin; index < list.size() && index < collection.size() && index < begin + length; index++) {
			result.add(list.get(index).add(iter.next()));
		}
		
		for(int i = index; i < list.size() && index < begin + length; i++) {
			result.add(list.get(i));
		}

		while(iter.hasNext() && index < begin + length) {
			result.add(iter.next());
			index++;
		}

		return result;
	}

	/**
	 * Adds the list values and the values within the array. Missing data 
	 * points due to uneven list and array sizes are omitted.
	 * @param    List<BigDecimal> list    the list
	 * @param    BigDecimal[] array       the array
	 * @return            the result
	 * @override
	 */
	public List<BigDecimal> calculateStrict(List<BigDecimal> list, BigDecimal[] array) {

	    List<BigDecimal> result = new ArrayList<BigDecimal>();
	
		for(int i = 0; i < list.size() && i < array.length; i++) {
			result.add(list.get(i).add(array[i]));
		}
		
		return result;
	}

	/**
	 * Adds the list values and the values within the array using the values
	 * between the indices. Missing data points due to uneven list and array 
	 * sizes are omitted.
	 * @param    List<BigDecimal> list    the list
	 * @param    BigDecimal[] array       the array
	 * @param    int begin                beginning index of the subset
	 * @param    int length               length of the subset
	 * @return            the result
	 * @override
	 */
	public List<BigDecimal> calculateStrict(List<BigDecimal> list, BigDecimal[] array,
			int begin, int length) {
		
		List<BigDecimal> result = new ArrayList<BigDecimal>();
		
		for(int i = begin; i < list.size() && i < array.length && 
				i < begin + length; i++) {
			result.add(list.get(i).add(array[i]));
		}
		
		return result;
	}

	/**
	 * Adds the list values and the values within the collection. Missing data 
	 * points due to uneven list and array sizes are omitted.
	 * @param    List<BigDecimal> list                the list
	 * @param    Collection<BigDecimal> collection    the collection
	 * @return                        the result
	 * @override
	 */
	public List<BigDecimal> calculateStrict(List<BigDecimal> list,
			Collection<BigDecimal> collection) {
	
		Iterator<BigDecimal> iter = collection.iterator();
	    List<BigDecimal> result = new ArrayList<BigDecimal>();
	
		for(int i = 0; i < list.size() && i < collection.size(); i++) {
			result.add(list.get(i).add(iter.next()));
		}
		
		return result;
	}

	/**
	 * Adds the list the values and the values within the collection using the 
	 * values between the indices. Missing data points due to uneven list and 
	 * collection sizes are omitted.
	 * @param    List<BigDecimal> list                the list
	 * @param    Collection<BigDecimal> collection    the collection
	 * @param    int begin                            beginning index of the subset
	 * @param    int length                           length of the subset
	 * @return                        the result
	 * @override
	 */
	public List<BigDecimal> calculateStrict(List<BigDecimal> list,
			Collection<BigDecimal> collection, int begin, int length) {

        List<BigDecimal> result = new ArrayList<BigDecimal>();
		Iterator<BigDecimal> iter = collection.iterator();
		
		for(int i = 0; i < begin; i++) {
			iter.next();
		}
		
		for(int i = begin; i < list.size() && iter.hasNext() && 
				i < begin + length; i++) {
			result.add(list.get(i).add(iter.next()));
		} 

		return result;
	}
    
}
