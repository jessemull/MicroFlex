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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

/**
 * This class performs statistical operations that return a list of BigDecimals as
 * a result for BigDecimal plate stacks, plates, wells and well sets. To create 
 * a custom statistical operation extend this class and override the calculate 
 * methods using the appropriate statistical operation. 
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
 * MicroFlex currently supports the following statistical operations:
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
 *      <td>Quadratic Mean</td>
 *    </tr>
 *    <tr>
 *       <td>Sum</td>
 *    </tr>
 *    <tr>
 *       <td>Sum Squared</td>
 *    </tr>
 *    <tr>
 *       <td>Skewness</td>
 *    </tr>
 *    <tr>
 *       <td>Kurtosis</td>
 *    </tr>
 *    <tr>
 *       <td>Maximum</td>
 *    </tr>
 *    <tr>
 *       <td>Minimum</td>
 *    </tr>
 *    <tr>
 *       <td>N</td>
 *    </tr>
 *    <tr>
 *       <td>Percentile</td>
 *    </tr>
 *    <tr>
 *       <td>Population Variance</td>
 *    </tr>
 *    <tr>
 *       <td>Sample Variance</td>
 *    </tr>
 *    <tr>
 *       <td>Standard Deviation</td>
 *    </tr>
 *    <tr>
 *       <td></td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class DescriptiveStatisticListBigDecimalContext {
    
    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Returns the statistic for each plate well.
     * @param    PlateBigDecimal    the plate
     * @param    MathContext        the math context
     * @return                      map of wells and results
     */
    public Map<WellBigDecimal, List<BigDecimal>> plate(PlateBigDecimal plate, MathContext mc) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellBigDecimal, List<BigDecimal>> result = new TreeMap<WellBigDecimal, List<BigDecimal>>();
        
        for (WellBigDecimal well : plate) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well, mc));
        }
      
        return result;
        
    }
    
    /**
     * Returns the statistic for each plate well using the values between the beginning 
     * and ending indices.
     * @param    PlateBigDecimal    the plate
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    MathContext        the math context
     * @return                      map of wells and results   
     */
    public Map<WellBigDecimal, List<BigDecimal>> plate(PlateBigDecimal plate, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellBigDecimal, List<BigDecimal>> result = new TreeMap<WellBigDecimal, List<BigDecimal>>();
        
        for (WellBigDecimal well : plate) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well, begin, length, mc));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated plate statistics -------------------  */
    
    /**
     * Returns the aggregated statistic for the plate.
     * @param    PlateBigDecimal    the plate
     * @param    MathContext        the math context
     * @return                      the aggregated result
     */
    public List<BigDecimal> platesAggregated(PlateBigDecimal plate, MathContext mc) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigDecimal well : plate) {
            aggregated.addAll(well.data());
        }
      
        return calculate(aggregated, mc);
        
    }
    
    /**
     * Returns the aggregated statistic for each plate.
     * @param    Collection<PlateBigDecimal>    collection of plates
     * @param    MathContext                    the math context
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigDecimal, List<BigDecimal>> platesAggregated(Collection<PlateBigDecimal> collection, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");

        Map<PlateBigDecimal, List<BigDecimal>> results = new TreeMap<PlateBigDecimal, List<BigDecimal>>();
        
        for(PlateBigDecimal plate : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated statistic for each plate.
     * @param    PlateBigDecimal[]    array of plates
     * @param    MathContext          the math context
     * @return                        map of plates and aggregated result
     */
    public Map<PlateBigDecimal, List<BigDecimal>> platesAggregated(PlateBigDecimal[] array, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");

        Map<PlateBigDecimal, List<BigDecimal>> results = new TreeMap<PlateBigDecimal, List<BigDecimal>>();
        
        for(PlateBigDecimal plate : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated statistic for each plate using the values between the 
     * indices.
     * @param    PlateBigDecimal    the plate
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    MathContext        the math context
     * @return                      the aggregated result
     */
    public List<BigDecimal> platesAggregated(
            PlateBigDecimal plate, int begin, int length, MathContext mc) {

        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigDecimal well : plate) {
            aggregated.addAll(well.data().subList(begin, begin + length));
        }
      
        return calculate(aggregated, mc);
        
    }
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    Collection<PlateBigDecimal>    collection of plates
     * @param    int                            beginning index of subset
     * @param    int                            length of subset
     * @param    MathContext                    the math context
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigDecimal, List<BigDecimal>> platesAggregated(
            Collection<PlateBigDecimal> collection, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");

        Map<PlateBigDecimal, List<BigDecimal>> results = new TreeMap<PlateBigDecimal, List<BigDecimal>>();
        
        for(PlateBigDecimal plate : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
         
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    PlateBigDecimal[]    array of plates
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    MathContext          the math context
     * @return                        map of plates and aggregated results	
     */
    public Map<PlateBigDecimal, List<BigDecimal>> platesAggregated(
            PlateBigDecimal[] array, int begin, int length, MathContext mc) {
       
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<PlateBigDecimal, List<BigDecimal>> results = new TreeMap<PlateBigDecimal, List<BigDecimal>>();
        
        for(PlateBigDecimal plate : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }      
            
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Returns the statistic of each well in the well set.
     * @param    WellSetBigDecimal    the well set
     * @param    MathContext          the math context
     * @return                        map of wells and results
     */
    public Map<WellBigDecimal, List<BigDecimal>> set(WellSetBigDecimal set, MathContext mc) {
    	
        Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<WellBigDecimal, List<BigDecimal>> result = new TreeMap<WellBigDecimal, List<BigDecimal>>();
        
        for (WellBigDecimal well : set) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well, mc));
        }
      
        return result;
        
    }
    
    /**
     * Returns the statistic of each well in the well set using the values between 
     * the beginning and ending indices.
     * @param    WellSetBigDecimal    the well set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    MathContext          the math context
     * @return                        map of wells and results
     */
    public Map<WellBigDecimal, List<BigDecimal>> set(WellSetBigDecimal set, int begin, int length, MathContext mc) {
        
    	Preconditions.checkNotNull(set, "The well set cannot be null.");
    	
    	Map<WellBigDecimal, List<BigDecimal>> result = new TreeMap<WellBigDecimal, List<BigDecimal>>();
        
        for (WellBigDecimal well : set) {
        	WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone,  well(well, begin, length, mc));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated set statistics ---------------------  */
    
    /**
     * Returns the aggregated statistic for the well set.
     * @param    WellSetBigDecimal    the well set
     * @param    MathContext          the math context
     * @return                        the aggregated result
     */
    public List<BigDecimal> setsAggregated(WellSetBigDecimal set, MathContext mc) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
        	
        for (WellBigDecimal well : set) {
            aggregated.addAll(well.data());
        }
      
        return calculate(aggregated, mc);
        
    }
    
    /**
     * Returns the aggregated statistic for each well set.
     * @param    Collection<WellSetBigDecimal>    collection of well sets
     * @param    MathContext                      the math context
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, List<BigDecimal>> setsAggregated(Collection<WellSetBigDecimal> collection, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");

        Map<WellSetBigDecimal, List<BigDecimal>> results = new TreeMap<WellSetBigDecimal, List<BigDecimal>>();
        
        for(WellSetBigDecimal set : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated statistic for each well set.
     * @param    WellSetBigDecimal[]    array of well sets
     * @param    MathContext            the math context
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, List<BigDecimal>> setsAggregated(WellSetBigDecimal[] array, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");

        Map<WellSetBigDecimal, List<BigDecimal>> results = new TreeMap<WellSetBigDecimal, List<BigDecimal>>();
        
        for(WellSetBigDecimal set : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated statistic for each well set using the values between the 
     * indices.
     * @param    WellSetBigDecimal    the well set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    MathContext          the math context
     * @return                        the aggregated result
     */
    public List<BigDecimal> setsAggregated(
            WellSetBigDecimal set, int begin, int length, MathContext mc) {

        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigDecimal well : set) {
            aggregated.addAll(well.data().subList(begin, begin + length));
        }
      
        return calculate(aggregated, mc);
        
    }
    
    /**
     * Returns the aggregated statistic for each well set using the values between 
     * the indices.
     * @param    Collection<WellSetBigDecimal>    collection of well sets
     * @param    int                              beginning index of subset
     * @param    int                              length of subset
     * @param    MathContext                      the math context
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, List<BigDecimal>> setsAggregated(
            Collection<WellSetBigDecimal> collection, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSetBigDecimal, List<BigDecimal>> results = new TreeMap<WellSetBigDecimal, List<BigDecimal>>();
        
        for(WellSetBigDecimal set : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated statistic for each well set using the values between 
     * the indices.
     * @param    WellSetBigDecimal[]    array of well sets
     * @param    int                    beginning index of subset
     * @param    int                    length of subset
     * @param    MathContext            the math context
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, List<BigDecimal>> setsAggregated(
            WellSetBigDecimal[] array, int begin, int length, MathContext mc) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSetBigDecimal, List<BigDecimal>> results = new TreeMap<WellSetBigDecimal, List<BigDecimal>>();
        
        for(WellSetBigDecimal set : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, mc));
        
        }
        
        return results;
        
    }
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Returns the well statistic.
     * @param    WellBigDecimal    the well
     * @param    MathContext       the math context
     * @return                     the result
     */
    public List<BigDecimal> well(WellBigDecimal well, MathContext mc) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        return calculate(well.data(), mc);
        
    }
    
    /**
     * Returns the well statistic for the values between the beginning and ending 
     * indices.
     * @param    WellBigDecimal    the well
     * @param    int               beginning index of subset
     * @param    int               length of the subset
     * @param    MathContext       the math context
     * @return                     the result
     */
    public List<BigDecimal> well(WellBigDecimal well, int begin, int length, MathContext mc) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkArgument(begin <= well.data().size() &&
                                    begin >= 0 &&
                                    begin + length <= well.data().size());  
       
        return calculate(well.data(), begin, length, mc);
        
    }

    /* -------- Methods for calculating the statistic of a data set --------  */
    
    /**
     * Calculates the statistic.
     * @param    List<BigDecimal>    the list
     * @param    MathContext         the math context
     * @return                       the result
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, MathContext mc);
    
    /**
     * Calculates the statistic of the values between the beginning and ending indices.
     * @param    List<BigDecimal>    the list
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @param    MathContext         the math context
     * @return                       the result
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, int begin, int length, MathContext mc);
}
