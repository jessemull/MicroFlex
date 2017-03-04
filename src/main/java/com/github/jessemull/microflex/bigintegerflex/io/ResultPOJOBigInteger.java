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
import java.util.Map;
import java.util.TreeMap;

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;

/**
 * This is a wrapper class for importing or exporting a JSON encoded result.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ResultPOJOBigInteger {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private String type;                          // The result data type
    private String label;                         // The result label
    private int size;                             // Number of well results
    private Map<String, BigInteger> wells = 
    		new TreeMap<String, BigInteger>();    // The map of well results
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultPOJOBigInteger(){}
    
    /**
     * Creates a result POJO.
     * @param    Map<WellBigInteger, BigInteger> results    the result object
     * @param    String label                               the result label 
     */
    public ResultPOJOBigInteger(Map<WellBigInteger, BigInteger> results, String label) {
        
        this.size = results.size();
        this.label = label;
        this.type = "BigInteger";
        
        for (Map.Entry<WellBigInteger, BigInteger> entry : results.entrySet()) {
            this.wells.put(entry.getKey().index(), entry.getValue());
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the type.
     * @param   String type    the numerical type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the size.
     * @param   int size    result size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the result label.
     * @param   String label    the result label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the map of well results.
     * @param   Map<String, BigInteger> results    the list of well results
     */
    public void setResults(Map<String, BigInteger> results) {
        this.wells = results;
    }
    
    /**
     * Returns the numerical type as a string.
     * @return    String    the numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the size of the well results array.
     * @return   int   the size of the well results array
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the result label.
     * @return   String label    the result label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Returns the map of result results.
     * @return   the wells
     */
    public Map<String, BigInteger> getWells() {
        return this.wells;
    }
}
