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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

/**
 * This class calculates percentiles for BigDecimal plate stacks, plates, 
 * wells and well sets using the following steps:
 * 
 * <br><br>
 * 
 * <ol type="1">
 *    <li style="margin-bottom: 10px;">Calculate R as P/100 x (N + 1) where N is the number of values and P is the desired percentile</li>
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
public class PercentileBigDecimal {
    
    /* --------------- Well percentiles for all plate wells ----------------- */
    
    /**
     * Returns the percentile for each plate well.
     * @param    PlateBigDecimal    the plate
     * @param    int                the percentile
     * @return                      map of wells and results
     */
    public Map<WellBigDecimal, BigDecimal> plate(PlateBigDecimal plate, int p) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellBigDecimal, BigDecimal> result = new TreeMap<WellBigDecimal, BigDecimal>();
        
        for (WellBigDecimal well : plate) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well, p));
        }
      
        return result;
        
    }
    
    /**
     * Returns the percentile for each plate well using the values between the 
     * beginning and ending indices.
     * @param    PlateBigDecimal    the plate
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    int                the percentile
     * @return                      map of wells and results   
     */
    public Map<WellBigDecimal, BigDecimal> plate(PlateBigDecimal plate, int begin, int length, int p) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellBigDecimal, BigDecimal> result = new TreeMap<WellBigDecimal, BigDecimal>();
        
        for (WellBigDecimal well : plate) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well, begin, length, p));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated plate percentiles ------------------  */
    
    /**
     * Returns the aggregated percentile for the plate.
     * @param    PlateBigDecimal    the plate
     * @param    int                the percentile
     * @return                      the aggregated result
     */
    public BigDecimal platesAggregated(PlateBigDecimal plate, int p) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigDecimal well : plate) {
            aggregated.addAll(well.data());
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated percentile for each plate.
     * @param    Collection<PlateBigDecimal>    collection of plates
     * @param    int                            the percentile
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigDecimal, BigDecimal> platesAggregated(Collection<PlateBigDecimal> collection, int p) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");

        Map<PlateBigDecimal, BigDecimal> results = new TreeMap<PlateBigDecimal, BigDecimal>();
        
        for(PlateBigDecimal plate : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated percentile for each plate.
     * @param    PlateBigDecimal[]    array of plates
     * @param    int                  the percentile
     * @return                        map of plates and aggregated result
     */
    public Map<PlateBigDecimal, BigDecimal> platesAggregated(PlateBigDecimal[] array, int p) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");

        Map<PlateBigDecimal, BigDecimal> results = new TreeMap<PlateBigDecimal, BigDecimal>();
        
        for(PlateBigDecimal plate : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated percentile for each plate using the values between 
     * the indices.
     * @param    PlateBigDecimal    the plate
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    int                the percentile
     * @return                      the aggregated result
     */
    public BigDecimal platesAggregated(
            PlateBigDecimal plate, int begin, int length, int p) {

        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigDecimal well : plate) {
            aggregated.addAll(well.data().subList(begin, begin + length));
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated percentile for each plate using the values between 
     * the indices.
     * @param    Collection<PlateBigDecimal>    collection of plates
     * @param    int                            beginning index of subset
     * @param    int                            length of subset
     * @param    int                            the percentile
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigDecimal, BigDecimal> platesAggregated(
            Collection<PlateBigDecimal> collection, int begin, int length, int p) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<PlateBigDecimal, BigDecimal> results = new TreeMap<PlateBigDecimal, BigDecimal>();
        
        for(PlateBigDecimal plate : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated percentile for each plate using the values between 
     * the indices.
     * @param    PlateBigDecimal[]    array of plates
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    int                  the percentile
     * @return                        map of plates and aggregated results
     */
    public Map<PlateBigDecimal, BigDecimal> platesAggregated(
            PlateBigDecimal[] array, int begin, int length, int p) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<PlateBigDecimal, BigDecimal> results = new TreeMap<PlateBigDecimal, BigDecimal>();
        
        for(PlateBigDecimal plate : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /* --------------- Well percentiles for all wells in a set -------------  */
    
    /**
     * Returns the percentile of each well in the well set.
     * @param    WellSetBigDecimal    the well set
     * @param    int                  the percentile
     * @return                        map of wells and results
     */
    public Map<WellBigDecimal, BigDecimal> set(WellSetBigDecimal set, int p) {
    	
        Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<WellBigDecimal, BigDecimal> result = new TreeMap<WellBigDecimal, BigDecimal>();
        
        for (WellBigDecimal well : set) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well, p));
        }
      
        return result;
        
    }
    
    /**
     * Returns the percentile of each well in the well set using the values between 
     * the beginning and ending indices.
     * @param    WellSetBigDecimal    the well set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    int                  the percentile
     * @return                        map of wells and results
     */
    public Map<WellBigDecimal, BigDecimal> set(WellSetBigDecimal set, int begin, int length, int p) {
        
    	Preconditions.checkNotNull(set, "The well set cannot be null.");
    	
    	Map<WellBigDecimal, BigDecimal> result = new TreeMap<WellBigDecimal, BigDecimal>();
        
        for (WellBigDecimal well : set) {
        	WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone,  well(well, begin, length, p));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated set percentiles --------------------  */
    
    /**
     * Returns the aggregated percentile for the well set.
     * @param    WellSetBigDecimal    the well set
     * @param    int                  the percentile
     * @return                        the aggregated result
     */
    public BigDecimal setsAggregated(WellSetBigDecimal set, int p) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
        	
        for (WellBigDecimal well : set) {
            aggregated.addAll(well.data());
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated percentile for each well set.
     * @param    Collection<WellSetBigDecimal>    collection of well sets
     * @param    int                              the percentile
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, BigDecimal> setsAggregated(Collection<WellSetBigDecimal> collection, int p) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");

        Map<WellSetBigDecimal, BigDecimal> results = new TreeMap<WellSetBigDecimal, BigDecimal>();
        
        for(WellSetBigDecimal set : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated percentile for each well set.
     * @param    WellSetBigDecimal[]    array of well sets
     * @param    int                    the percentile
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, BigDecimal> setsAggregated(WellSetBigDecimal[] array, int p) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");

        Map<WellSetBigDecimal, BigDecimal> results = new TreeMap<WellSetBigDecimal, BigDecimal>();
        
        for(WellSetBigDecimal set : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated percentile for each well set using the values between the 
     * indices.
     * @param    WellSetBigDecimal    the well set
     * @param    int                  beginning index of subset
     * @param    int                  length of subset
     * @param    int                  the percentile
     * @return                        the aggregated result
     */
    public BigDecimal setsAggregated(
            WellSetBigDecimal set, int begin, int length, int p) {

        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            
        for (WellBigDecimal well : set) {
            aggregated.addAll(well.data().subList(begin, begin + length));
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated percentile for each well set using the values between 
     * the indices.
     * @param    Collection<WellSetBigDecimal>    collection of well sets
     * @param    int                              beginning index of subset
     * @param    int                              length of subset
     * @param    int                              the percentile
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, BigDecimal> setsAggregated(
            Collection<WellSetBigDecimal> collection, int begin, int length, int p) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSetBigDecimal, BigDecimal> results = new TreeMap<WellSetBigDecimal, BigDecimal>();
        
        for(WellSetBigDecimal set : collection) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated percentile for each well set using the values between 
     * the indices.
     * @param    WellSetBigDecimal[]    array of well sets
     * @param    int                    beginning index of subset
     * @param    int                    length of subset
     * @param    int                    the percentile
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, BigDecimal> setsAggregated(
            WellSetBigDecimal[] array, int begin, int length, int p) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSetBigDecimal, BigDecimal> results = new TreeMap<WellSetBigDecimal, BigDecimal>();
        
        for(WellSetBigDecimal set : array) {
            
            List<BigDecimal> aggregated = new ArrayList<BigDecimal>();
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /* -------------------------- Well percentiles -------------------------  */
    
    /**
     * Returns the well percentile.
     * @param    WellBigDecimal    the well
     * @param    int               the percentile
     * @return                     the result
     */
    public BigDecimal well(WellBigDecimal well, int p) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkArgument(p <= 100 || p > 0, "Percentile is outside the valid range: %d", p);
        return calculate(well.data(), p);
        
    }
    
    /**
     * Returns the well percentile for the values between the beginning and ending 
     * indices.
     * @param    WellBigDecimal    the well
     * @param    int               beginning index of subset
     * @param    int               length of the subset
     * @param    int               the percentile
     * @return                     the result
     */
    public BigDecimal well(WellBigDecimal well, int begin, int length, int p) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkArgument(p <= 100 || p > 0, "Percentile is outside the valid range: %d", p);
        Preconditions.checkArgument(begin <= well.data().size() &&
                                    begin >= 0 &&
                                    begin + length <= well.data().size());   
        return calculate(well.data(), begin, length, p);
        
    }

    /* -------- Methods for calculating the percentile of a data set -------  */
    
    /**
     * Calculates the percentile.
     * @param    List<BigDecimal>    the list
     * @param    int                 the percentile
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, int p) {
        
    	int n = list.size();
    	
    	if(n == 1) {
    		return list.get(0);
    	}
    	
        Collections.sort(list);        
        double pos = (p * (n + 1)) / 100.0;
        
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
     * Calculates the percentile of the values between the beginning and ending 
     * indices.
     * @param    List<BigDecimal>    the list
     * @param    int                 beginning index of subset
     * @param    int                 length of subset
     * @param    int                 the percentile
     * @return                       the result
     */
    public BigDecimal calculate(List<BigDecimal> list, int begin, int length, int p) {
        return this.calculate(list.subList(begin, begin + length), p);
    };
}
