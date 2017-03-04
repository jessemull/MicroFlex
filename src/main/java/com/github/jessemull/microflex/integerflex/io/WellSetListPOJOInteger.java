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

import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded well set objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellSetListPOJOInteger implements Iterable<WellSetPOJOInteger> {
	
	/*---------------------------- Private fields ----------------------------*/
	
	private List<WellSetPOJOInteger> wellsets = new ArrayList<WellSetPOJOInteger>();
	
	/*------------------------------ Constructors ----------------------------*/
	
	public WellSetListPOJOInteger(){}
	
    /**
     * Creates a well set list POJO from a well set object.
     * @param    WellSetInteger    the well set object
     */
    public WellSetListPOJOInteger(WellSetInteger single) {
      this.wellsets.add(new WellSetPOJOInteger(single));
    }
    
    /**
     * Creates a well set list POJO from a collection of well set objects.
     * @param    Collection<WellSetInteger>    the collection of sets
     */
    public WellSetListPOJOInteger(Collection<WellSetInteger> collection) {
        for(WellSetInteger set : collection) {
            this.wellsets.add(new WellSetPOJOInteger(set));
        }
    }
    
    /**
     * Creates a well set list POJO from an array of well set objects.
     * @param    WellSetInteger    the well set object
     */
    public WellSetListPOJOInteger(WellSetInteger[] array) {
        for(WellSetInteger set : array) {
            this.wellsets.add(new WellSetPOJOInteger(set));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of well sets.
     * @param   List<WellSetPOJOInteger>    the new list of well sets
     */
    public void setWellsets(List<WellSetPOJOInteger> wellsets) {
        this.wellsets = wellsets;
    }
      
    /**
     * Returns the list of sets.
     * @return    the list of well sets
     */
    public List<WellSetPOJOInteger> getWellsets() {
    	return this.wellsets;
    }
    
    /**
     * Returns the well set at the indicated index.
     * @param    int    the index into the well list
     * @return          the well set POJO
     */
    public WellSetPOJOInteger get(int index) {
        return this.wellsets.get(index);
    }
    
    /*------------------ Methods for iterating over the list -----------------*/
    
    /**
     * Returns an iterator for the list of well sets.
     * @return    the iterator
     */
    public Iterator<WellSetPOJOInteger> iterator() {
        return this.wellsets.iterator();
    }
    
    /**
     * Returns the size of the well set list.
     * @return    the size of the list
     */
    public int size() {
        return this.wellsets.size();
    }
}
