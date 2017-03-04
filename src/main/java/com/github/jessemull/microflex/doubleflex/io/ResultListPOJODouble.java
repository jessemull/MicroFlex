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

/*--------------------------- Package Declaration ----------------------------*/

package com.github.jessemull.microflex.doubleflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.jessemull.microflex.doubleflex.plate.WellDouble;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded results.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ResultListPOJODouble implements Iterable<ResultPOJODouble> {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private List<ResultPOJODouble> results = new ArrayList<ResultPOJODouble>();    // The results in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultListPOJODouble(){}
    
    /**
     * Creates a result list POJO from a well object.
     * @param    Map<WellDouble, Double>    the input results
     */
    public ResultListPOJODouble(Map<WellDouble, Double> results) {
        this.results.add(new ResultPOJODouble(results, "Result"));
    }
    
    /**
     * Creates a result list POJO from a well object.
     * @param    Map<WellDouble, Double>    the input results
     */
    public ResultListPOJODouble(Map<WellDouble, Double> results, String label) {
        this.results.add(new ResultPOJODouble(results, label));
    }
    
    /**
     * Creates a result list POJO from a collection of well objects.
     * @param    Collection<Map<WellDouble, Double>>    collection of results
     */
    public ResultListPOJODouble(Collection<Map<WellDouble, Double>> collection) {
        for(Map<WellDouble, Double> result : collection) {
        	this.results.add(new ResultPOJODouble(result, "Result"));
        }
    }
    
    /**
     * Creates a result list POJO from a collection of well objects.
     * @param    Collection<Map<WellDouble, Double>>    collection of results
     * @param    List<String>                           list of result labels
     */
    public ResultListPOJODouble(Collection<Map<WellDouble, Double>> collection, List<String> labels) {
    	int index = 0;
        for(Map<WellDouble, Double> result : collection) {
        	this.results.add(new ResultPOJODouble(result, labels.get(index++)));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of result POJOs.
     * @param    List<ResultPOJODouble>    the new list of results
     */
    public void setResults(List<ResultPOJODouble> results) {
        this.results = results;
    }
    
    /**
     * Gets the list of result POJOs.
     * @return    the list of results
     */
    public List<ResultPOJODouble> getResults() {
        return this.results;
    }

    /**
     * Returns the result at the indicated index.
     * @param    int    the index into the well list
     * @return          the result at the index
     */
    public ResultPOJODouble get(int index) {
        return this.results.get(index);
    }
    
    /* Methods for iterating over the list */
    
    /**
     * Returns an iterator for the list of results.
     * @return    the iterator
     */
    public Iterator<ResultPOJODouble> iterator() {
        return this.results.iterator();
    }
    
    /**
     * Returns the size of the result list.
     * @return    the size of the list
     */
    public int size() {
        return this.results.size();
    }

}
