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

package com.github.jessemull.microflex.bigintegerflex.stat;

/* ----------------------------- Dependencies ------------------------------- */

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * This class performs weighted statistical operations for BigInteger plate stacks, 
 * plates, wells and well sets with calculate methods that return a list of BigDecimal
 * values. To create a custom weighted statistical operation extend this class and 
 * override the calculate methods using the appropriate weighted statistical operation. 
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
 * MicroFlex currently supports the following weighted statistical operations:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Descriptive Statistics<div></th>
 *    <tr>
 *       <td>Mean</td>
 *    </tr>
 *    <tr>
 *       <td>Geometric Mean</td>
 *    </tr>
 *    <tr>
 *       <td>Quadratic Mean</td>
 *    </tr>
 *    <tr>
 *       <td>Sum</td>
 *    </tr>
 *    <tr>
 *       <td>Sum Squared</td>
 *    </tr>
 *    <tr>
 *       <td>Population Variance</td>
 *    </tr>
 *    <tr>
 *       <td>Sample Variance</td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class DescriptiveStatisticListBigIntegerWeightsContext extends DescriptiveStatisticListBigIntegerContext {

    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Returns the weighted statistic for each plate well.
     * @param    PlateBigInteger    the plate
     * @param    double[]           weights for the data set
     * @param    MathContext        the math context
     * @return                      map of wells and results
     */
    public Map<WellBigInteger, List<BigDecimal>> plate(PlateBigInteger plate, double[] weights, MathContext mc) {

        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
    	
    	Map<WellBigInteger, List<BigDecimal>> result = new TreeMap<WellBigInteger, List<BigDecimal>>();
        
        for (WellBigInteger well : plate) {
            WellBigInteger clone = new WellBigInteger(well);
            result.put(clone, well(well, weights, mc));
        }
        
        return result;
        
    }
    
    /**
     * Returns the weighted statistic for each plate well using the values between the 
     * beginning and ending indices.
     * @param    PlateBigInteger    the plate
     * @param    double[]           weights for the data set
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    MathContext        the math context
     * @return                      map of wells and results
     */
    public Map<WellBigInteger, List<BigDecimal>> plate(PlateBigInteger plate, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
    	
    	Map<WellBigInteger, List<BigDecimal>> result = new TreeMap<WellBigInteger, List<BigDecimal>>();
        
        for (WellBigInteger well : plate) {
            WellBigInteger clone = new WellBigInteger(well);
            result.put(clone, well(well, weights, begin, length, mc));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated plate statistics -------------------  */ 
        
    /**
     * Returns the aggregated weighted statistic for the plate.
     * @param    PlateBigInteger    the plate
     * @param    double[]           weights for the data set
     * @param    MathContext        the math context
     * @return                      the aggregated result
     */
    public List<BigDecimal> platesAggregated(PlateBigInteger plate, double[] weights, MathContext mc) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");
        Preconditions.checkNotNull(weights, "Weights array cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigInteger well : plate) {

        	List<BigDecimal> input = well.toBigDecimal();
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
        	}
        	
        }
      
        return calculate(aggregated, mc);
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each plate.
     * @param    Collection<PlateBigInteger>    collection of plates
     * @param    double[]                       weights for the data set
     * @param    MathContext                    the math context
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigInteger, List<BigDecimal>> platesAggregated(
            Collection<PlateBigInteger> collection, double[] weights, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<PlateBigInteger, List<BigDecimal>> results = new TreeMap<PlateBigInteger, List<BigDecimal>>();
        
        for(PlateBigInteger plate : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigInteger clone = new PlateBigInteger(plate);
            
            for (WellBigInteger well : plate) {

            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each plate.
     * @param    PlateBigInteger[]    array of plates
     * @param    double[]             weights for the data set
     * @param    MathContext          the math context
     * @return                        map of plates and aggregated results
     */
    public Map<PlateBigInteger, List<BigDecimal>> platesAggregated(
            PlateBigInteger[] array, double[] weights, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<PlateBigInteger, List<BigDecimal>> results = new TreeMap<PlateBigInteger, List<BigDecimal>>();
        
        for(PlateBigInteger plate : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigInteger clone = new PlateBigInteger(plate);
            
            for (WellBigInteger well : plate) {

            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for the plate using the values 
     * between the indices.
     * @param    PlateBigInteger    the plate
     * @param    double[]           weights for the data set
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    MathContext        the math context
     * @return                      the aggregated result
     */
    public List<BigDecimal> platesAggregated(PlateBigInteger plate, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");
        
        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();

        for (WellBigInteger well : plate) {

        	List<BigDecimal> input = well.toBigDecimal().subList(begin, begin + length);
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i]), mc));
        	}
        	
        }

        return calculate(aggregated, mc);
        
    }

    /**
     * Returns the aggregated weighted statistic for each plate using the values
     * between the indices.
     * @param    Collection<PlateBigInteger>    collection of plates
     * @param    double[]                       weights for the data set
     * @param    int                            beginning index of subset
     * @param    int                            length of subset
     * @param    MathContext                    the math context
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigInteger, List<BigDecimal>> platesAggregated(
            Collection<PlateBigInteger> collection, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<PlateBigInteger, List<BigDecimal>> results = new TreeMap<PlateBigInteger, List<BigDecimal>>();
        
        for(PlateBigInteger plate : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigInteger clone = new PlateBigInteger(plate);
            
            for (WellBigInteger well : plate) {

            	List<BigDecimal> input = well.toBigDecimal().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each plate using the values
     * between the indices.
     * @param    PlateBigInteger[]    array of plates
     * @param    double[]             weights for the data set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    MathContext          the math context
     * @return                        map of plates and aggregated results
     */
    public Map<PlateBigInteger, List<BigDecimal>> platesAggregated(
            PlateBigInteger[] array, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<PlateBigInteger, List<BigDecimal>> results = new TreeMap<PlateBigInteger, List<BigDecimal>>();
        
        for(PlateBigInteger plate : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigInteger clone = new PlateBigInteger(plate);
            
            for (WellBigInteger well : plate) {

            	List<BigDecimal> input = well.toBigDecimal().subList(begin, begin + length);
            
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Returns the weighted statistic of each well in the well set.
     * @param    WellSetBigInteger    the well set
     * @param    double[]             weights for the data set
     * @param    MathContext          the math context
     * @return                        map of wells and results
     */
    public Map<WellBigInteger, List<BigDecimal>> set(WellSetBigInteger set, double[] weights, MathContext mc) {

    	Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<WellBigInteger, List<BigDecimal>> result = new TreeMap<WellBigInteger, List<BigDecimal>>();
        
        for (WellBigInteger well : set) {
        	WellBigInteger clone = new WellBigInteger(well);
            result.put(clone,  well(well, weights, mc));
        }
        
        return result;
        
    }
    
    /**
     * Returns the weighted statistic of each well in the well set using the values 
     * between the beginning and ending indices.
     * @param    WellSetBigInteger    the well set
     * @param    double[]             weights for the data set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    MathContext          the math context
     * @return                        map of wells and results
     */
    public Map<WellBigInteger, List<BigDecimal>> set(WellSetBigInteger set, double[] weights, int begin, int length, MathContext mc) {

        Preconditions.checkNotNull(set, "The well set cannot be null.");
    	
    	Map<WellBigInteger, List<BigDecimal>> result = new TreeMap<WellBigInteger, List<BigDecimal>>();
        
        for (WellBigInteger well : set) {
        	WellBigInteger clone = new WellBigInteger(well);
            result.put(clone,  well(well, weights, begin, length, mc));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated set statistics ---------------------  */  
        
    /**
     * Returns the aggregated weighted statistic for the well set.
     * @param    WellSetBigInteger    the well set
     * @param    double[]             weights for the data set
     * @param    MathContext          the math context
     * @return                        the aggregated results
     */
    public List<BigDecimal> setsAggregated(WellSetBigInteger set, double[] weights, MathContext mc) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");
    	Preconditions.checkNotNull(weights, "Weights array cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigInteger well : set) {

        	List<BigDecimal> input = well.toBigDecimal();
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
        	}
        	
        }
      
        return calculate(aggregated, mc);
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each well set.
     * @param    Collection<WellSetBigInteger>    collection of well sets
     * @param    double[]                         weights for the data set
     * @param    MathContext                      the math context
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigInteger, List<BigDecimal>> setsAggregated(
            Collection<WellSetBigInteger> collection, double[] weights, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSetBigInteger, List<BigDecimal>> results = new TreeMap<WellSetBigInteger, List<BigDecimal>>();
        
        for(WellSetBigInteger set : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigInteger clone = new WellSetBigInteger(set);
            
            for (WellBigInteger well : set) {

            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each well set.
     * @param    WellSetBigInteger[]    array of well sets
     * @param    double[]               weights for the data set
     * @param    MathContext            the math context
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigInteger, List<BigDecimal>> setsAggregated(
            WellSetBigInteger[] array, double[] weights, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSetBigInteger, List<BigDecimal>> results = new TreeMap<WellSetBigInteger, List<BigDecimal>>();
        
        for(WellSetBigInteger set : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigInteger clone = new WellSetBigInteger(set);
            
            for (WellBigInteger well : set) {

            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for the well set using the values 
     * between the indices.
     * @param    WellSetBigInteger    the well set
     * @param    double[]             weights for the data set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    MathContext          the math context
     * @return                        the aggregated result
     */
    public List<BigDecimal> setsAggregated(WellSetBigInteger set, double[] weights, int begin, int length, MathContext mc) {
    	
        Preconditions.checkNotNull(set, "The well set cannot be null.");
        
        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigInteger well : set) {

        	List<BigDecimal> input = well.toBigDecimal().subList(begin, begin + length);
        	
        	for(int i = 0; i < input.size(); i++) {
        		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
        	}
        	
        }
      
        return calculate(aggregated, mc);
        
    }

    /**
     * Returns the aggregated weighted statistic for each well set using the values
     * between the indices.
     * @param    Collection<WellSetBigInteger>    collection of well sets
     * @param    double[]                         weights for the data set
     * @param    int                              beginning index of subset
     * @param    int                              length of subset
     * @param    MathContext                      the math context
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigInteger, List<BigDecimal>> setsAggregated(
            Collection<WellSetBigInteger> collection, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSetBigInteger, List<BigDecimal>> results = new TreeMap<WellSetBigInteger, List<BigDecimal>>();
        
        for(WellSetBigInteger set : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigInteger clone = new WellSetBigInteger(set);
            
            for (WellBigInteger well : set) {

            	List<BigDecimal> input = well.toBigDecimal().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated weighted statistic for each well set using the values
     * between the indices.
     * @param    WellSetBigInteger[]    array of well sets
     * @param    double[]               weights for the data set
     * @param    int                    beginning index of subset
     * @param    int                    length of subset
     * @param    MathContext            the math context
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigInteger, List<BigDecimal>> setsAggregated(
            WellSetBigInteger[] array, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSetBigInteger, List<BigDecimal>> results = new TreeMap<WellSetBigInteger, List<BigDecimal>>();
        
        for(WellSetBigInteger set : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigInteger clone = new WellSetBigInteger(set);
            
            for (WellBigInteger well : set) {

            	List<BigDecimal> input = well.toBigDecimal().subList(begin, begin + length);
            	
            	for(int i = 0; i < input.size(); i++) {
            		aggregated.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Returns the weighted well statistic.
     * @param    WellBigInteger    the well
     * @param    double[]          weights for the data set
     * @param    MathContext       the math context
     * @return                     the result
     */
    public List<BigDecimal> well(WellBigInteger well, double[] weights, MathContext mc) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkNotNull(weights, "The weights array cannot be null.");
        return calculate(well.toBigDecimal(), weights, mc);  
    }
    
    /**
     * Returns the well weighted statistic for the values between the beginning and 
     * ending indices.
     * @param    WellBigInteger    the well
     * @param    double[]          weights for the data set
     * @param    int               beginning index of subset
     * @param    int               length of the subset
     * @param    MathContext       the math context
     * @return                     the result
     */
    public List<BigDecimal> well(WellBigInteger well, double[] weights, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkNotNull(weights, "The weights array cannot be null.");
        Preconditions.checkArgument(begin <= well.data().size() &&
                                    begin >= 0 &&
                                    begin + length <= well.data().size());

        return calculate(well.toBigDecimal(), weights, begin, length, mc);
    }

    /* -------- Methods for calculating the statistic of a data set --------  */

    /**
     * Calculates the weighted statistic.
     * @param    List<BigDecimal>    the list
     * @param    double[]            weights for the data set
     * @param    MathContext         the math context
     * @return                       the result
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, double[] weights, MathContext mc);

    /**
     * Calculates the weighted statistic of the values between the beginning and 
     * ending indices.
     * @param    List<BigDecimal>    the list
     * @param    double[]            weights of the data set
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @param    MathContext         the math context
     * @return                       the result
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, double[] weights, int begin, int length, MathContext mc);
}
