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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded stack object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="stack")
@XmlType (propOrder={"type", "label", "rows", "columns", "size", "plates"})
public class StackXMLInteger {
    
	/*---------------------------- Private fields ----------------------------*/

    private String type;                           // Data set numerical type
    private String label;                          // Stack label
    private int rows;                              // Stack row number
    private int columns;                           // Stack column number
    private int size;                              // Number of plates in the stack
    private List<PlateXMLInteger> plates = 
    		new ArrayList<PlateXMLInteger>();      // List of plates in the stack
    
    /*------------------------------ Constructors ----------------------------*/
    
    public StackXMLInteger(){}
    
    /**
     * Creates an XML stack from a stack object.
     * @param    StackInteger    the stack object
     */
    public StackXMLInteger(StackInteger stack) {
        
        this.type = "Integer";
        this.label = stack.label();
        this.rows = stack.rows();
        this.columns = stack.columns();
        this.size = stack.size();
        
        for(PlateInteger plate : stack) {
            plates.add(new PlateXMLInteger(plate));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the data type.
     * @param    String    the data set numerical type
     */
    @XmlElement
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the stack label.
     * @param    String    the stack label
     */
    @XmlElement
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the row number.
     * @param   int    number of rows
     */
    @XmlElement
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * Sets the column number.
     * @param   int    number of columns
     */
    @XmlElement
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the size.
     * @param   int    number of plates in the stack
     */
    @XmlElement
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the list of plates in the stack.
     * @param    List<PlateXMLInteger>    the plates
     */
    @XmlElement(name="plate")
    @XmlElementWrapper(name="plates")
    public void setPlates(List<PlateXMLInteger> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the data type.
     * @return    the data set numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the stack label.
     * @return    the stack label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the row number.
     * @return    number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Returns the column number.
     * @return    number of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Returns the size.
     * @return    number of plates in the stack
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the list of plates in the stack.
     * @return    the list of plates
     */
    public List<PlateXMLInteger> getPlates() {
        return this.plates;
    }
    
    /**
     * Returns a StackInteger object.
     * @return    the stack
     */
    public StackInteger toStackObject() {

    	StackInteger stack = new StackInteger(this.rows, this.columns, this.label);
    	
    	for(PlateXMLInteger plate : plates) {
    		stack.add(plate.toPlateObject());
    	}
    	
    	return stack;
    	
    }
}
