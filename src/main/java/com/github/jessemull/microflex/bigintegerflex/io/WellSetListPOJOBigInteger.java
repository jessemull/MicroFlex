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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded well set objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellSetListPOJOBigInteger implements Iterable<WellSetPOJOBigInteger> {
	
	/*---------------------------- Private fields ----------------------------*/
	
	private List<WellSetPOJOBigInteger> wellsets = new ArrayList<WellSetPOJOBigInteger>();
	
    /* Constructors */
	
	public WellSetListPOJOBigInteger(){}
	
    /**
     * Creates a well set list POJO from a well set object.
     * @param    WellSetBigInteger    the well set object
     */
    public WellSetListPOJOBigInteger(WellSetBigInteger single) {
      this.wellsets.add(new WellSetPOJOBigInteger(single));
    }
    
    /**
     * Creates a well set list POJO from a collection of well set objects.
     * @param    Collection<WellSetBigInteger> wellsets    the collection of sets
     */
    public WellSetListPOJOBigInteger(Collection<WellSetBigInteger> collection) {
        for(WellSetBigInteger set : collection) {
            this.wellsets.add(new WellSetPOJOBigInteger(set));
        }
    }
    
    /**
     * Creates a well set list POJO from an array of well set objects.
     * @param    WellSetBigInteger    the well set object
     */
    public WellSetListPOJOBigInteger(WellSetBigInteger[] array) {
        for(WellSetBigInteger set : array) {
            this.wellsets.add(new WellSetPOJOBigInteger(set));
        }
    }
    
    /* Getters and setters */
    
    /**
     * Sets the list of well sets.
     * @param   List<WellSetPOJOBigInteger> wellsets   the new list of well sets
     */
    public void setWellsets(List<WellSetPOJOBigInteger> wellsets) {
        this.wellsets = wellsets;
    }
      
    /**
     * Returns the list of sets.
     * @return    List<WellSetPOJOBigInteger>    the list of well sets
     */
    public List<WellSetPOJOBigInteger> getWellsets() {
    	return this.wellsets;
    }
    
    /**
     * Returns the well set at the indicated index.
     * @param    int index           the index into the well list
     * @return   WellSetPOJOBigInteger well    the well set POJO
     */
    public WellSetPOJOBigInteger get(int index) {
        return this.wellsets.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of well sets.
     * @return    Iterator<PlatePOJO>    the iterator
     */
    public Iterator<WellSetPOJOBigInteger> iterator() {
        return this.wellsets.iterator();
    }
    
    /**
     * Returns the size of the well set list.
     * @return    int size    the size of the list
     */
    public int size() {
        return this.wellsets.size();
    }
}
