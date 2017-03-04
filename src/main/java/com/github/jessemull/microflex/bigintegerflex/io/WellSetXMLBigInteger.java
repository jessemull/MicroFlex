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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded well set object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement
@XmlType (propOrder={"type", "label", "size", "wells"})
public class WellSetXMLBigInteger {
    
	/*---------------------------- Private fields ----------------------------*/

    private String type;                                 // Data set type
    private String label;                                // The set label
    private int size;                                    // Size of the data set
    private List<SimpleWellXMLBigInteger> wells = 
    		new ArrayList<SimpleWellXMLBigInteger>();    // List of set wells
    
    /*----------------------------- Constructors -----------------------------*/
    
    public WellSetXMLBigInteger(){}
    
    /**
     * Creates an XML well set from a well set object.
     * @param    WellSetBigInteger    the well set object
     */
    public WellSetXMLBigInteger(WellSetBigInteger set) {
        
        this.label = set.label();
        this.size = set.size();
        this.type = "BigInteger";
        
        for(WellBigInteger well : set) {
            wells.add(new SimpleWellXMLBigInteger(well));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the size.
     * @param   String type    the numerical type
     */
    @XmlElement
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the list of set wells.
     * @param    List<SimpleWellXMLBigInteger> wells    the list of set wells
     */
    @XmlElement(name="well")
    @XmlElementWrapper(name="wells")
    public void setWells(List<SimpleWellXMLBigInteger> wells) {
    	this.wells = wells;
    }
    
    /**
     * Sets the label.
     * @param    String label    the set label
     */
    @XmlElement
    public void setLabel(String label) {
    	this.label = label;
    }
    
    
    /**
     * Sets the data type.
     * @param    String type    the data type
     */
    @XmlElement
    public void setType(String type) {
    	this.type = type;
    }
    /**
     * Returns the size of the values array.
     * @return   int   the size of the values array
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the list of set wells.
     * @return    List<SimpleWellXMLBigInteger>    the list of set wells
     */
    public List<SimpleWellXMLBigInteger> getWells() {
    	return this.wells;
    }
    
    /**
     * Returns the set label.
     * @return    String label    the set label
     */
    public String getLabel() {
    	return this.label;
    }
    
    /**
     * Returns the data set type.
     * @return    String    the data set type
     */
    public String getType() {
    	return this.type;
    }
    
    /**
     * Returns a WellSetBigInteger object.
     * @return    WellSetBigInteger    the well set
     */
    public WellSetBigInteger toWellSetObject() {
        
    	WellSetBigInteger set = new WellSetBigInteger();
        set.setLabel(this.label);
        
        for(SimpleWellXMLBigInteger well : wells) {
        	set.add(well.toWellObject());
        }
        
        return set;
    }
}
