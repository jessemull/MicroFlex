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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded plate objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlateListPOJOBigDecimal implements Iterable<PlatePOJOBigDecimal> {

    /*---------------------------- Private fields ----------------------------*/
    
    private List<PlatePOJOBigDecimal> plates = new ArrayList<PlatePOJOBigDecimal>();    // The wells in the list
    
    /*----------------------------- Constructors -----------------------------*/ 
    
    /**
     * Creates a new plate list POJO wrapper object.
     */
    public PlateListPOJOBigDecimal(){}
    
    /**
     * Creates a new plate list wrapper POJO object from a plate object.
     * @param    PlateBigDecimal    the plate object
     */
    public PlateListPOJOBigDecimal(PlateBigDecimal plate) {
        this.plates.add(new PlatePOJOBigDecimal(plate));
    }
    
    /**
     * Creates a new plate list POJO wrapper object from a collection of plate objects.
     * @param    Collection<plateBigDecimal>    collection of plate objects
     */
    public PlateListPOJOBigDecimal(Collection<PlateBigDecimal> collection) {
        for(PlateBigDecimal plate : collection) {
            this.plates.add(new PlatePOJOBigDecimal(plate));
        }
    }
    
    /**
     * Creates a plate list POJO wrapper object from an array of plate objects.
     * @param    PlateBigDecimal[]    array of plate objects
     */
    public PlateListPOJOBigDecimal(PlateBigDecimal[] array) {
        for(PlateBigDecimal plate : array) {
            this.plates.add(new PlatePOJOBigDecimal(plate));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of plate POJOs.
     * @param    List<WellPOJO>    the new list of plate POJOs
     */
    public void setPlates(List<PlatePOJOBigDecimal> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the list of plate POJOs.
     * @return    the list of plate POJOs
     */
    public List<PlatePOJOBigDecimal> getPlates() {
        return this.plates;
    }

    /**
     * Returns the plate at the index.
     * @param    int    the index into the plate POJO list
     * @return          the plate POJO
     */
    public PlatePOJOBigDecimal get(int index) {
        return this.plates.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of plate POJOs.
     * @return    the iterator
     */
    public Iterator<PlatePOJOBigDecimal> iterator() {
        return this.plates.iterator();
    }
    
    /**
     * Returns the size of the plate POJO list.
     * @return    the size of the list
     */
    public int size() {
        return this.plates.size();
    }
}
