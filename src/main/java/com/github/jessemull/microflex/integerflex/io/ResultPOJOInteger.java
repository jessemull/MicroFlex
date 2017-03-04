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

package com.github.jessemull.microflex.integerflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.Map;
import java.util.TreeMap;

import com.github.jessemull.microflex.integerflex.plate.WellInteger;

/**
 * This is a wrapper class for importing or exporting a JSON encoded result.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ResultPOJOInteger {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private String type;                      // The result data type
    private String label;                     // The result label
    private int size;                         // Number of well results
    private Map<String, Integer> wells = 
    		new TreeMap<String, Integer>();    // The map of well results
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultPOJOInteger(){}
    
    /**
     * Creates a result POJO.
     * @param    Map<WellInteger, Integer>    the result object
     * @param    String                       the result label
     */
    public ResultPOJOInteger(Map<WellInteger, Integer> results, String label) {
        
        this.size = results.size();
        this.label = label;
        this.type = "Integer";
        
        for (Map.Entry<WellInteger, Integer> entry : results.entrySet()) {
            this.wells.put(entry.getKey().index(), entry.getValue());
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the type.
     * @param   String    the numerical type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the size.
     * @param   int    result size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the result label.
     * @param   String    the result label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the map of well results.
     * @param   Map<String, Integer>    the list of well results
     */
    public void setResults(Map<String, Integer> results) {
        this.wells = results;
    }
    
    /**
     * Returns the numerical type as a string.
     * @return    the numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the size of the well results array.
     * @return    the size of the well results array
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the result label.
     * @return    the result label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Returns the map of result results.
     * @return   the wells
     */
    public Map<String, Integer> getWells() {
        return this.wells;
    }
}
