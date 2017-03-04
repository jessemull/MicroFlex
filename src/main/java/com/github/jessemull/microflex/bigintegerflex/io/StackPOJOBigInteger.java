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

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;

/**
 * This is a wrapper class for importing or exporting a JSON encoded stack object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class StackPOJOBigInteger {
    
	/*---------------------------- Private fields ----------------------------*/

    private String type;                           // Data set numerical type
    private String label;                          // Stack label
    private int rows;                              // Stack row number
    private int columns;                           // Stack column number
    private int size;                              // Number of plates in the stack
    private List<PlatePOJOBigInteger> plates = 
    		new ArrayList<PlatePOJOBigInteger>();    // List of plates in the stack
    
    /*----------------------------- Constructors -----------------------------*/
    
    public StackPOJOBigInteger(){}
    
    /**
     * Creates a stack POJO from a stack object.
     * @param    StackBigInteger    the stack object
     */
    public StackPOJOBigInteger(StackBigInteger stack) {
        
        this.type = "BigInteger";
        this.label = stack.label();
        this.rows = stack.rows();
        this.columns = stack.columns();
        this.size = stack.size();
        
        for(PlateBigInteger plate : stack) {
            plates.add(new PlatePOJOBigInteger(plate));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the data type.
     * @param    String type    the data set numerical type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the stack label.
     * @param    String label    the stack label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the row number.
     * @param   int rows       number of rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * Sets the column number.
     * @param   int columns    number of columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the size.
     * @param   int size    number of plates in the stack
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the list of plates in the stack.
     * @param    List<PlatePOJOBigInteger> plates    the plates
     */
    public void setPlates(List<PlatePOJOBigInteger> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the data type.
     * @return    String    the data set numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the stack label.
     * @return    String    the stack label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the row number.
     * @return   int    number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Returns the column number.
     * @return   int    number of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Returns the size.
     * @return   int    number of plates in the stack
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the list of plates in the stack.
     * @return    List<PlatePOJOBigInteger>    the list of plates
     */
    public List<PlatePOJOBigInteger> getPlates() {
        return this.plates;
    }
    
    /**
     * Returns a StackBigInteger object.
     * @return    StackBigInteger    the stack
     */
    public StackBigInteger toStackObject() {

    	StackBigInteger stack = new StackBigInteger(this.rows, this.columns, this.label);
    	
    	for(PlatePOJOBigInteger plate : plates) {
    		stack.add(plate.toPlateObject());
    	}
    	
    	return stack;
    	
    }
}
