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

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

/**
 * This class returns the number of data points in BigDecimal plate stacks, plates, 
 * wells and well sets.
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
public class NBigDecimal {
    
    /* ----------- Number of well data points for all plate wells ----------- */
    
    /**
     * Returns the number of data points for each plate well.
     * @param    PlateBigDecimal    the plate
     * @return                      map of wells and results
     */
    public Map<WellBigDecimal, Integer> plate(PlateBigDecimal plate) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellBigDecimal, Integer> result = new TreeMap<WellBigDecimal, Integer>();
        
        for (WellBigDecimal well : plate) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well));
        }
      
        return result;
        
    }

    /* --------------------- Aggregated plate statistics -------------------  */
    
    /**
     * Returns the aggregated number of data points for the plate.
     * @param    PlateBigDecimal    the plate
     * @return                      the aggregated result
     */
    public int platesAggregated(PlateBigDecimal plate) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        int aggregated = 0;
            
        for (WellBigDecimal well : plate) {
            aggregated += well(well);
        }
 
        return aggregated;
        
    }
    
    /**
     * Returns the aggregated number of data points for each plate.
     * @param    Collection<PlateBigDecimal>    collection of plates
     * @return                                  map of plates and aggregated results
     */
    public Map<PlateBigDecimal, Integer> platesAggregated(Collection<PlateBigDecimal> collection) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");

        Map<PlateBigDecimal, Integer> results = new TreeMap<PlateBigDecimal, Integer>();
        
        for(PlateBigDecimal plate : collection) {
            
            int aggregated = 0;
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated += well(well);
            }
      
            results.put(clone, aggregated);
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated number of data points for each plate.
     * @param    PlateBigDecimal[]    array of plates
     * @return                        map of plates and aggregated results
     */
    public Map<PlateBigDecimal, Integer> platesAggregated(PlateBigDecimal[] array) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");

        Map<PlateBigDecimal, Integer> results = new TreeMap<PlateBigDecimal, Integer>();
        
        for(PlateBigDecimal plate : array) {
            
            int aggregated = 0;
            PlateBigDecimal clone = new PlateBigDecimal(plate);
            
            for (WellBigDecimal well : plate) {
                aggregated += well(well);
            }
      
            results.put(clone, aggregated);
        
        }
        
        return results;
    }    
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Returns the number of data points for each well in the well set.
     * @param    WellSetBigDecimal    the well set
     * @return                        map of wells and results
     */
    public Map<WellBigDecimal, Integer> set(WellSetBigDecimal set) {
    	
        Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<WellBigDecimal, Integer> result = new TreeMap<WellBigDecimal, Integer>();
        
        for (WellBigDecimal well : set) {
            WellBigDecimal clone = new WellBigDecimal(well);
            result.put(clone, well(well));
        }
      
        return result;
        
    }

    /* --------------------- Aggregated set statistics ---------------------  */
    
    /**
     * Returns the aggregated number of data points for the well set.
     * @param    WellSetBigDecimal    the well set
     * @return                        the aggregated result
     */
    public int setsAggregated(WellSetBigDecimal set) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");

        int aggregated = 0;
        	
        for (WellBigDecimal well : set) {
            aggregated += well(well);
        }
      
        return aggregated;
        
    }
    
    /**
     * Returns the aggregated number of data points for each well set.
     * @param    Collection<WellSetBigDecimal>    collection of well sets
     * @return                                    map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, Integer> setsAggregated(Collection<WellSetBigDecimal> collection) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");

        Map<WellSetBigDecimal, Integer> results = new TreeMap<WellSetBigDecimal, Integer>();
        
        for(WellSetBigDecimal set : collection) {
            
            int aggregated = 0;
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated += well(well);
            }
      
            results.put(clone, aggregated);
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated number of data points for each well set.
     * @param    WellSetBigDecimal[]    array of well sets
     * @return                          map of well sets and aggregated results
     */
    public Map<WellSetBigDecimal, Integer> setsAggregated(WellSetBigDecimal[] array) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");

        Map<WellSetBigDecimal, Integer> results = new TreeMap<WellSetBigDecimal, Integer>();
        
        for(WellSetBigDecimal set : array) {
            
            int aggregated = 0;
            WellSetBigDecimal clone = new WellSetBigDecimal(set);
            
            for (WellBigDecimal well : set) {
                aggregated += well(well);
            }
      
            results.put(clone, aggregated);
        
        }
        
        return results;
    }    
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Returns the number of well data points.
     * @param    WellBigDecimal    the well
     * @return                     the result
     */
    public int well(WellBigDecimal well) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        return well.data().size();  
    }
    
}
