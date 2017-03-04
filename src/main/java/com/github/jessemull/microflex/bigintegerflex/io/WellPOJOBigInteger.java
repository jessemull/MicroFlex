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

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;

/**
 * This is a wrapper class for importing or exporting a JSON encoded well object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellPOJOBigInteger {
    
	/*---------------------------- Private fields ----------------------------*/
    
    private String type;            // Data set numerical type
    private String index;           // The well index
    private int size;               // Size of the data set
    private BigInteger[] values;    // Array holding the well values
    
    /*----------------------------- Constructors -----------------------------*/
    
    public WellPOJOBigInteger(){}
    
    /**
     * Creates a well POJO from the well object.
     * @param    WellBigInteger    the well object
     */
    public WellPOJOBigInteger(WellBigInteger well) {
        this.index = well.index();
        this.size = well.size();
        this.values = well.data().toArray(new BigInteger[well.size()]);
        this.type = well.typeString();
    }
    
    /**
     * Creates a well POJO from the well object.
     * @param    WellBigInteger    the well object
     */
    public WellPOJOBigInteger(String index, int size, BigInteger[] values, String type) {
        this.index = index;
        this.size = size;
        this.values = values;
        this.type = type;;
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the index.
     * @param    String newIndex    the new index
     */
    public void setIndex(String newIndex) {
        this.index = newIndex;
    }
    
    /**
     * Sets the values array.
     * @param    int[] newValues    the values array
     */
    public void setValues(BigInteger[] newValues) {
        this.values = newValues;
    }
    
    /**
     * Sets the type.
     * @param   String type    the numerical type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the size.
     * @param   String type    the numerical type
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Returns the index.
     * @return    String    the index
     */
    public String getIndex() {
        return this.index;
    }
    
    /**
     * Returns the values array.
     * @return    int[]    the array of values
     */
    public BigInteger[] getValues() {
        return this.values;
    }
    
    /**
     * Returns the numerical type as a string.
     * @return    String    the numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the size of the values array.
     * @return   int   the size of the values array
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns a WellBigInteger object.
     * @return    WellBigInteger    the well
     */
    public WellBigInteger toWellObject() {
        return new WellBigInteger(index, values);
    }
}
