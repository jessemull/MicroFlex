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

package com.github.jessemull.microflex.bigintegerflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded results.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ResultListPOJOBigInteger implements Iterable<ResultPOJOBigInteger> {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private List<ResultPOJOBigInteger> results = new ArrayList<ResultPOJOBigInteger>();    // The results in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultListPOJOBigInteger(){}
    
    /**
     * Creates a result list POJO from a well object.
     * @param    Map<WellBigInteger, BigInteger> results    the input results
     */
    public ResultListPOJOBigInteger(Map<WellBigInteger, BigInteger> results) {
        this.results.add(new ResultPOJOBigInteger(results, "Result"));
    }
    
    /**
     * Creates a result list POJO from a well object.
     * @param    Map<WellBigInteger, BigInteger> results    the input results
     */
    public ResultListPOJOBigInteger(Map<WellBigInteger, BigInteger> results, String label) {
        this.results.add(new ResultPOJOBigInteger(results, label));
    }
    
    /**
     * Creates a result list POJO from a collection of well objects.
     * @param    Collection<Map<WellBigInteger, BigInteger>> collection    collection of results
     */
    public ResultListPOJOBigInteger(Collection<Map<WellBigInteger, BigInteger>> collection) {
        for(Map<WellBigInteger, BigInteger> result : collection) {
        	this.results.add(new ResultPOJOBigInteger(result, "Result"));
        }
    }
    
    /**
     * Creates a result list POJO from a collection of well objects.
     * @param    Collection<Map<WellBigInteger, BigInteger>> collection    collection of results
     * @param    List<String> labels                                       list of result labels
     */
    public ResultListPOJOBigInteger(Collection<Map<WellBigInteger, BigInteger>> collection, List<String> labels) {
    	int index = 0;
        for(Map<WellBigInteger, BigInteger> result : collection) {
        	this.results.add(new ResultPOJOBigInteger(result, labels.get(index++)));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of result POJOs.
     * @param    List<ResultPOJOBigInteger> results    the new list of results
     */
    public void setResults(List<ResultPOJOBigInteger> results) {
        this.results = results;
    }
    
    /**
     * Gets the list of result POJOs.
     * @return    List<ResultPOJOBigInteger>    the list of results
     */
    public List<ResultPOJOBigInteger> getResults() {
        return this.results;
    }

    /**
     * Returns the result at the indicated index.
     * @param    int index               the index into the well list
     * @return   ResultPOJOBigInteger    the result at the index
     */
    public ResultPOJOBigInteger get(int index) {
        return this.results.get(index);
    }
    
    /* Methods for iterating over the list */
    
    /**
     * Returns an iterator for the list of results.
     * @return    Iterator<ResultPOJOBigInteger>    the iterator
     */
    public Iterator<ResultPOJOBigInteger> iterator() {
        return this.results.iterator();
    }
    
    /**
     * Returns the size of the result list.
     * @return    int size    the size of the list
     */
    public int size() {
        return this.results.size();
    }

}
