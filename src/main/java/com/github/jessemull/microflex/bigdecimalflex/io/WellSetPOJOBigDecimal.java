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

package com.github.jessemull.microflex.bigdecimalflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.List;

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

/**
 * This is a wrapper class for importing or exporting a JSON encoded well set object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellSetPOJOBigDecimal {
    
	/*---------------------------- Private Fields ----------------------------*/

    private String type;                        // Data set type
    private String label;                       // The set label
    private int size;                           // Size of the data set
    private List<SimpleWellPOJOBigDecimal> wells = 
    		new ArrayList<SimpleWellPOJOBigDecimal>();    // List of set wells
    
    /*------------------------------ Constructors ----------------------------*/
    
    public WellSetPOJOBigDecimal(){}
    
    /**
     * Creates a well set POJO from a well set object.
     * @param    WellSetBigDecimal    the well set object
     */
    public WellSetPOJOBigDecimal(WellSetBigDecimal set) {
        
        this.label = set.label();
        this.size = set.size();
        this.type = "BigDecimal";
        
        for(WellBigDecimal well : set) {
            wells.add(new SimpleWellPOJOBigDecimal(well));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the size.
     * @param   String    the numerical type
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the list of set wells.
     * @param    List<SimpleWellPOJOBigDecimal>    the list of set wells
     */
    public void setWells(List<SimpleWellPOJOBigDecimal> wells) {
    	this.wells = wells;
    }
    
    /**
     * Sets the label.
     * @param    String    the set label
     */
    public void setLabel(String label) {
    	this.label = label;
    }
    
    
    /**
     * Sets the data type.
     * @param    String    the data type
     */
    public void setType(String type) {
    	this.type = type;
    }
    /**
     * Returns the size of the values array.
     * @return    the size of the values array
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the list of set wells.
     * @return    the list of set wells
     */
    public List<SimpleWellPOJOBigDecimal> getWells() {
    	return this.wells;
    }
    
    /**
     * Returns the set label.
     * @return    the set label
     */
    public String getLabel() {
    	return this.label;
    }
    
    /**
     * Returns the data set type.
     * @return    the data set type
     */
    public String getType() {
    	return this.type;
    }
    
    /**
     * Returns a WellSetBigDecimal object.
     * @return    the well set
     */
    public WellSetBigDecimal toWellSetObject() {
        
    	WellSetBigDecimal set = new WellSetBigDecimal();
        set.setLabel(this.label);
        
        for(SimpleWellPOJOBigDecimal well : wells) {
        	set.add(well.toWellObject());
        }
        
        return set;
    }
}
