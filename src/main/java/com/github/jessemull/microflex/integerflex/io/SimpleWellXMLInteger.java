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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.integerflex.plate.WellInteger;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded well in a plate or stack object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="well")
@XmlType (propOrder={"index", "values"})
public class SimpleWellXMLInteger {
    
	/*---------------------------- Private fields ----------------------------*/

    private String index;        // The well index
    private Integer[] values;    // Array holding the well values
   
    /*------------------------------ Constructors ----------------------------*/
    
    public SimpleWellXMLInteger(){}
    
    /**
     * Creates a simple XML well from a well object.
     * @param    WellInteger    the well object
     */
    public SimpleWellXMLInteger(WellInteger well) {
        this.index = well.index();
        this.values = well.data().toArray(new Integer[well.size()]);
    }
    
    /*------------------------- Getters and setters --------------------------*/
   
    /**
     * Sets the index.
     * @param    String    the new index
     */
    @XmlElement
    public void setIndex(String newIndex) {
        this.index = newIndex;
    }
    
    /**
     * Sets the values array.
     * @param    int[]    the values array
     */
    @XmlElement(name="value")
    @XmlElementWrapper(name="values")
    public void setValues(Integer[] newValues) {
        this.values = newValues;
    }

    /**
     * Returns the index.
     * @return    the index
     */
    public String getIndex() {
        return this.index;
    }
    
    /**
     * Returns the values array.
     * @return    the array of values
     */
    public Integer[] getValues() {
        return this.values;
    }
    
    /**
     * Returns a WellInteger object.
     * @return    the well
     */
    public WellInteger toWellObject() {
        return new WellInteger(index, values);
    }
}
