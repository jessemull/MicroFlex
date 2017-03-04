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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded plate objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlateListPOJOInteger implements Iterable<PlatePOJOInteger> {

	/*---------------------------- Private fields ----------------------------*/
    
    private List<PlatePOJOInteger> plates = new ArrayList<PlatePOJOInteger>();    // The wells in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public PlateListPOJOInteger(){}
    
    /**
     * Creates a plate list POJO from a plate object.
     * @param    PlateInteger    the plate object
     */
    public PlateListPOJOInteger(PlateInteger plate) {
        this.plates.add(new PlatePOJOInteger(plate));
    }
    
    /**
     * Creates a plate list POJO from a collection of plate objects.
     * @param    Collection<plateInteger>    collection of plate objects
     */
    public PlateListPOJOInteger(Collection<PlateInteger> collection) {
        for(PlateInteger plate : collection) {
            this.plates.add(new PlatePOJOInteger(plate));
        }
    }
    
    /**
     * Creates a plate list POJO from an array of plate objects.
     * @param    PlateInteger[]    array of plate objects
     */
    public PlateListPOJOInteger(PlateInteger[] array) {
        for(PlateInteger plate : array) {
            this.plates.add(new PlatePOJOInteger(plate));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of plate POJOs.
     * @param    List<WellPOJO>    the new list of plate
     */
    public void setPlates(List<PlatePOJOInteger> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the list of plate POJOs.
     * @return    the list of plates
     */
    public List<PlatePOJOInteger> getPlates() {
        return this.plates;
    }

    /**
     * Returns the plate at the indicated index.
     * @param    int    the index into the well list
     * @return          the plate POJO
     */
    public PlatePOJOInteger get(int index) {
        return this.plates.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of plates.
     * @return    the iterator
     */
    public Iterator<PlatePOJOInteger> iterator() {
        return this.plates.iterator();
    }
    
    /**
     * Returns the size of the plate list.
     * @return    the size of the list
     */
    public int size() {
        return this.plates.size();
    }
}
