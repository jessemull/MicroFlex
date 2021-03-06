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

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded well in a result.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="well")
@XmlType (propOrder={"index", "value"})
public class ResultWellXMLBigDecimal {
    
	/*---------------------------- Private Fields ----------------------------*/

    private String index;        // The well index
    private BigDecimal value;    // The value
   
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultWellXMLBigDecimal(){}
    
    /**
     * Creates a simple XML well from an index and value.
     * @param    WellBigDecimal    the well object
     */
    public ResultWellXMLBigDecimal(String index, BigDecimal value) {
        this.index = index;
        this.value = value;
    }
    
    /*-------------------------- Getters and setters -------------------------*/
   
    /**
     * Sets the index.
     * @param    String    the new index
     */
    @XmlElement
    public void setIndex(String newIndex) {
        this.index = newIndex;
    }
    
    /**
     * Sets the value.
     * @param    BigDecimal    the new value
     */
    @XmlElement
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Returns the index.
     * @return    the index
     */
    public String getIndex() {
        return this.index;
    }
    
    /**
     * Returns the value.
     * @return    the value
     */
    public BigDecimal getValue() {
        return this.value;
    }
    
    /**
     * Returns a WellBigDecimal object.
     * @return    the well
     */
    public WellBigDecimal toWellObject() {
        
    	WellBigDecimal well = new WellBigDecimal(this.index);
    	well.add(this.value);
    	
    	return well;
    	
    }
}
