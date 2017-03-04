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
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded result.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name = "result")
@XmlType (propOrder={"type", "label", "size", "wells"})
public class ResultXMLBigInteger {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private String type;                                 // Data set numerical type
    private String label;                                // The well index
    private int size;                                    // Size of the data set
    private List<ResultWellXMLBigInteger> wells = 
    		new ArrayList<ResultWellXMLBigInteger>();    // Array holding the well values
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultXMLBigInteger(){}
    
    /**
     * Creates a result XML object from a map.
     * @param    Map<WellBigInteger, BigInteger> map    the result map
     * @param    String label                           the label
     */
    public ResultXMLBigInteger(Map<WellBigInteger, BigInteger> map, String label) {
        
    	this.type = "BigInteger";
        this.label = label;
        this.size = map.size();

        for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {
        	this.wells.add(new ResultWellXMLBigInteger(entry.getKey().index(), entry.getValue()));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the type.
     * @param   String type    the numerical type
     */
    @XmlElement
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the label.
     * @param   String label    the label
     */
    @XmlElement
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets the size.
     * @param   String type    the numerical type
     */
    @XmlElement
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the wells list.
     * @param    List<ResultWellXMLBigInteger> list    the wells list
     */
    @XmlElement(name="well")
    @XmlElementWrapper(name="wells")
    public void setWells(List<ResultWellXMLBigInteger> list) {
        this.wells = list;
    }
    
    /**
     * Returns the numerical type as a string.
     * @return    String    the numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the label.
     * @return    String    the label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the size of the well list.
     * @return   int   the size of the well list
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the well list.
     * @return    List<ResultWellXMLBigInteger>    the result list
     */
    public List<ResultWellXMLBigInteger> getWells() {
    	return this.wells;
    }
    
    /**
     * Returns a WellSetBigInteger object.
     * @return    WellSetBigInteger    the well set
     */
    public WellSetBigInteger toWellSetObject() {
        
    	WellSetBigInteger result = new WellSetBigInteger();
    	result.setLabel(this.label);
    	
    	for(ResultWellXMLBigInteger well : this.wells) {
    		result.add(well.toWellObject());
    	}
    	
    	return result;
    }
}
