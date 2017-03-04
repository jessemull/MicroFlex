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

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded well objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellListPOJOBigDecimal implements Iterable<WellPOJOBigDecimal> {
    
	/*---------------------------- Private Fields ----------------------------*/
    
    private List<WellPOJOBigDecimal> wells = new ArrayList<WellPOJOBigDecimal>();    // The wells in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public WellListPOJOBigDecimal(){}
    
    /**
     * Creates a well list POJO from a well object.
     * @param    WellBigDecimal    the well object
     */
    public WellListPOJOBigDecimal(WellBigDecimal well) {
        this.wells.add(new WellPOJOBigDecimal(well));
    }
    
    /**
     * Creates a well list POJO from a collection of well objects.
     * @param    Collection<WellBigDecimal>    collection of well objects
     */
    public WellListPOJOBigDecimal(Collection<WellBigDecimal> collection) {
        for(WellBigDecimal well : collection) {
            this.wells.add(new WellPOJOBigDecimal(well));
        }
    }
    
    /**
     * Creates a well list POJO from an array of well objects.
     * @param    WellBigDecimal[]    array of well objects
     */
    public WellListPOJOBigDecimal(WellBigDecimal[] array) {
        for(WellBigDecimal well : array) {
            this.wells.add(new WellPOJOBigDecimal(well));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of well POJOs.
     * @param    List<WellPOJOBigDecimal>    the new list of wells
     */
    public void setWells(List<WellPOJOBigDecimal> wells) {
        this.wells = wells;
    }
    
    /**
     * Gets the list of well POJOs.
     * @return    the list of wells
     */
    public List<WellPOJOBigDecimal> getWells() {
        return this.wells;
    }

    /**
     * Returns the well at the indicated index.
     * @param    int    the index into the well list
     * @return          the well POJO
     */
    public WellPOJOBigDecimal get(int index) {
        return this.wells.get(index);
    }
    
    /* Methods for iterating over the list */
    
    /**
     * Returns an iterator for the list of wells.
     * @return    the iterator
     */
    public Iterator<WellPOJOBigDecimal> iterator() {
        return this.wells.iterator();
    }
    
    /**
     * Returns the size of the well list.
     * @return    the size of the list
     */
    public int size() {
        return this.wells.size();
    }

}
