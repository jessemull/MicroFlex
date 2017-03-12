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

package com.github.jessemull.microflex.doubleflex.stat;

/* ----------------------------- Dependencies ------------------------------ */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;

/**
 * This class calculates descriptive statistics with list and double inputs to
 * the calculate functions.
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
public abstract class QuantileStatisticRational {
    
    /* --------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Returns the statistic for each plate well.
     * @param    PlateDouble    the plate
     * @param    double         the double value
     * @return                  map of wells and results
     */
    public Map<WellDouble, Double> plate(PlateDouble plate, double p) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellDouble, Double> result = new TreeMap<WellDouble, Double>();
        
        for (WellDouble well : plate) {
            WellDouble clone = new WellDouble(well);
            result.put(clone, well(well, p));
        }
      
        return result;
        
    }
    
    /**
     * Returns the statistic for each plate well using the values between the 
     * beginning and ending indices.
     * @param    PlateDouble    the plate
     * @param    int            beginning index of subset
     * @param    int            length of subset
     * @param    double         the double value
     * @return                  map of wells and results   
     */
    public Map<WellDouble, Double> plate(PlateDouble plate, int begin, int length, double p) {
        
        Preconditions.checkNotNull(plate, "The plate value cannot be null.");
        
        Map<WellDouble, Double> result = new TreeMap<WellDouble, Double>();
        
        for (WellDouble well : plate) {
            WellDouble clone = new WellDouble(well);
            result.put(clone, well(well, begin, length, p));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated plate statistics -------------------  */
    
    /**
     * Returns the aggregated statistic for the plate.
     * @param    PlateDouble    the plate
     * @param    double         the double value
     * @return                  the aggregated result
     */
    public double platesAggregated(PlateDouble plate, double p) {
        
        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (WellDouble well : plate) {
            aggregated.addAll(well.data());
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each plate.
     * @param    Collection<PlateDouble>    collection of plates
     * @param    double                     the double value
     * @return                              map of plates and aggregated results
     */
    public Map<PlateDouble, Double> platesAggregated(Collection<PlateDouble> collection, double p) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");

        Map<PlateDouble, Double> results = new TreeMap<PlateDouble, Double>();
        
        for(PlateDouble plate : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            PlateDouble clone = new PlateDouble(plate);
            
            for (WellDouble well : plate) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated statistic for each plate.
     * @param    PlateDouble[]    array of plates
     * @param    double           the double value
     * @return                    map of plates and aggregated result
     */
    public Map<PlateDouble, Double> platesAggregated(PlateDouble[] array, double p) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");

        Map<PlateDouble, Double> results = new TreeMap<PlateDouble, Double>();
        
        for(PlateDouble plate : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            PlateDouble clone = new PlateDouble(plate);
            
            for (WellDouble well : plate) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    PlateDouble    the plate
     * @param    int            beginning index of subset
     * @param    int            length of subset
     * @param    double         the double value
     * @return                  the aggregated result
     */
    public double platesAggregated(
            PlateDouble plate, int begin, int length, double p) {

        Preconditions.checkNotNull(plate, "The plate cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (WellDouble well : plate) {
            aggregated.addAll(well.data().subList(begin, begin + length));
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    Collection<PlateDouble>    collection of plates
     * @param    int                        beginning index of subset
     * @param    int                        length of subset
     * @param    double                     the double value
     * @return                              map of plates and aggregated results
     */
    public Map<PlateDouble, Double> platesAggregated(
            Collection<PlateDouble> collection, int begin, int length, double p) {
        
        Preconditions.checkNotNull(collection, "The plate collection cannot be null.");
        
        Map<PlateDouble, Double> results = new TreeMap<PlateDouble, Double>();
        
        for(PlateDouble plate : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            PlateDouble clone = new PlateDouble(plate);
            
            for (WellDouble well : plate) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated statistic for each plate using the values between 
     * the indices.
     * @param    PlateDouble[]    array of plates
     * @param    int              beginning index of subset
     * @param    int              length of subset
     * @param    double           the double value
     * @return                    map of plates and aggregated results
     */
    public Map<PlateDouble, Double> platesAggregated(
            PlateDouble[] array, int begin, int length, double p) {
        
        Preconditions.checkNotNull(array, "The plate array cannot be null.");
        
        Map<PlateDouble, Double> results = new TreeMap<PlateDouble, Double>();
        
        for(PlateDouble plate : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            PlateDouble clone = new PlateDouble(plate);
            
            for (WellDouble well : plate) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Returns the statistic of each well in the well set.
     * @param    WellSetDouble    the well set
     * @param    double           the double value
     * @return                    map of wells and results
     */
    public Map<WellDouble, Double> set(WellSetDouble set, double p) {
    	
        Preconditions.checkNotNull(set, "The set cannot be null.");
    	
    	Map<WellDouble, Double> result = new TreeMap<WellDouble, Double>();
        
        for (WellDouble well : set) {
            WellDouble clone = new WellDouble(well);
            result.put(clone, well(well, p));
        }
      
        return result;
        
    }
    
    /**
     * Returns the statistic of each well in the well set using the values between 
     * the beginning and ending indices.
     * @param    WellSetDouble    the well set
     * @param    int              beginning index of subset
     * @param    int              length of subset
     * @param    double           the double value
     * @return                    map of wells and results
     */
    public Map<WellDouble, Double> set(WellSetDouble set, int begin, int length, double p) {
        
    	Preconditions.checkNotNull(set, "The well set cannot be null.");
    	
    	Map<WellDouble, Double> result = new TreeMap<WellDouble, Double>();
        
        for (WellDouble well : set) {
        	WellDouble clone = new WellDouble(well);
            result.put(clone,  well(well, begin, length, p));
        }
        
        return result;
        
    }

    /* --------------------- Aggregated set statistics ---------------------  */
    
    /**
     * Returns the aggregated statistic for the well set.
     * @param    WellSetDouble    the well set
     * @param    double           the double value
     * @return                    the aggregated result
     */
    public double setsAggregated(WellSetDouble set, double p) {
        
        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
        	
        for (WellDouble well : set) {
            aggregated.addAll(well.data());
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each well set.
     * @param    Collection<WellSetDouble>    collection of well sets
     * @param    double                       the double value
     * @return                                map of well sets and aggregated results
     */
    public Map<WellSetDouble, Double> setsAggregated(Collection<WellSetDouble> collection, double p) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");

        Map<WellSetDouble, Double> results = new TreeMap<WellSetDouble, Double>();
        
        for(WellSetDouble set : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSetDouble clone = new WellSetDouble(set);
            
            for (WellDouble well : set) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }
    
    /**
     * Returns the aggregated statistic for each well set.
     * @param    WellSetDouble[]    array of well sets
     * @param    double             the double value
     * @return                      map of well sets and aggregated results
     */
    public Map<WellSetDouble, Double> setsAggregated(WellSetDouble[] array, double p) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");

        Map<WellSetDouble, Double> results = new TreeMap<WellSetDouble, Double>();
        
        for(WellSetDouble set : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSetDouble clone = new WellSetDouble(set);
            
            for (WellDouble well : set) {
                aggregated.addAll(well.data());
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
    }    
    
    /**
     * Returns the aggregated statistic for each well set using the values between the 
     * indices.
     * @param    WellSetDouble    the well set
     * @param    int              beginning index of subset
     * @param    int              length of subset
     * @param    double           the double value
     * @return                    the aggregated result
     */
    public double setsAggregated(
            WellSetDouble set, int begin, int length, double p) {

        Preconditions.checkNotNull(set, "The well set cannot be null.");

        List<Double> aggregated = new ArrayList<Double>();
            
        for (WellDouble well : set) {
            aggregated.addAll(well.data().subList(begin, begin + length));
        }
      
        return calculate(aggregated, p);
        
    }
    
    /**
     * Returns the aggregated statistic for each well set using the values between 
     * the indices.
     * @param    Collection<WellSetDouble>    collection of well sets
     * @param    int                          beginning index of subset
     * @param    int                          length of subset
     * @param    double                       the double value
     * @return                                map of well sets and aggregated results
     */
    public Map<WellSetDouble, Double> setsAggregated(
            Collection<WellSetDouble> collection, int begin, int length, double p) {
        
        Preconditions.checkNotNull(collection, "The well set collection cannot be null.");
        
        Map<WellSetDouble, Double> results = new TreeMap<WellSetDouble, Double>();
        
        for(WellSetDouble set : collection) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSetDouble clone = new WellSetDouble(set);
            
            for (WellDouble well : set) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /**
     * Returns the aggregated statistic for each well set using the values between 
     * the indices.
     * @param    WellSetDouble[]    array of well sets
     * @param    int                beginning index of subset
     * @param    int                length of subset
     * @param    double             the double value
     * @return                      map of well sets and aggregated results
     */
    public Map<WellSetDouble, Double> setsAggregated(
            WellSetDouble[] array, int begin, int length, double p) {
        
        Preconditions.checkNotNull(array, "The well set array cannot be null.");
        
        Map<WellSetDouble, Double> results = new TreeMap<WellSetDouble, Double>();
        
        for(WellSetDouble set : array) {
            
            List<Double> aggregated = new ArrayList<Double>();
            WellSetDouble clone = new WellSetDouble(set);
            
            for (WellDouble well : set) {
                aggregated.addAll(well.data().subList(begin, begin + length));
            }
      
            results.put(clone, calculate(aggregated, p));
        
        }
        
        return results;
        
    }
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Returns the well statistic.
     * @param    WellDouble     the well
     * @param    double         the double value
     * @return                  the result
     */
    public double well(WellDouble well, double p) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        return calculate(well.data(), p);     
    }
    
    /**
     * Returns the well statistic for the values between the beginning and ending 
     * indices.
     * @param    WellDouble     the well
     * @param    int            beginning index of subset
     * @param    int            length of the subset
     * @param    double         the double value
     * @return                  the result
     */
    public double well(WellDouble well, int begin, int length, double p) {
        Preconditions.checkNotNull(well, "The well cannot be null.");
        Preconditions.checkArgument(begin <= well.data().size() &&
                                    begin >= 0 &&
                                    begin + length <= well.data().size());   
        return calculate(well.data(), begin, length, p);
        
    }

    /* ----------- Methods for calculating the data set statistic ----------  */
    
    /**
     * Calculates the statistic.
     * @param    List<Double>    the list
     * @param    double          the double value
     * @return                   the result
     */
    public abstract double calculate(List<Double> list, double p);
    
    /**
     * Calculates the statistic of the values between the beginning and ending 
     * indices.
     * @param    List<Double>    the list
     * @param    int             beginning index of subset
     * @param    int             length of subset
     * @param    double          the double value
     * @return                   the result
     */
    public abstract double calculate(List<Double> list, int begin, int length, double p);
}
