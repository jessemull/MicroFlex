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

package com.github.jessemull.microflex.plate;

/*------------------------------- Dependencies -------------------------------*/

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;

/**
 * Internal data structure to hold lists of well indices for well sets or plates.
 * The list of well indices prevents redundant data storage for the wells
 * within a well set or plate object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellList implements Iterable<WellIndex>, Comparable<WellList> {
    	
	/*---------------------------- Private Fields ----------------------------*/
	
	/* Well list name */
	
    private String label;                                        
    
    /* Well list indices */
    
    private TreeSet<WellIndex> indices = new TreeSet<WellIndex>();
    
    /*----------------------------- Constructors -----------------------------*/
    
    /**
     * Creates a new well list object;
     */
    public WellList() {}
    
    
    /**
     * Creates a new well list object with the label.
     * @param label
     */
    public WellList(String label) {
    	this.label = label;
    }
    
    /**
     * Creates a new well list from a collection of indices.
     * @param    Collection<WellIndex>    collection of well indices
     */
    public WellList(Collection<WellIndex> collection) {
    	for(WellIndex index : collection) {
    		this.add(index);
    	}
    }
    
    /**
     * Creates a new well list from a collection of indices and a label.
     * @param    Collection<WellIndex>    collection of well indices
     * @param    String                   the list label
     */
    public WellList(Collection<WellIndex> collection, String label) {
    	this(collection);
    	this.label = label;
    }
    
    /**
     * Creates a new well list from an array.
     * @param    WellIndex[]    array of well indices
     */
    public WellList(WellIndex[] array) {
    	for(WellIndex index : array) {
    		this.add(index);
    	}
    }
    
    /**
     * Creates a new well list from an array of indices and a label.
     * @param    WellIndex[]    array of well indices
     * @param    String         the list label
     */
    public WellList(WellIndex[] array, String label) {
    	this(array);
    	this.label = label;
    }
    
    /**
     * Creates a new well list from an existing well list.
     * @param    WellList    the well list
     */
    public WellList(WellList list) {
    	for(WellIndex index : list) {
    		this.add(new WellIndex(index.row(), index.column()));
    	}
    	this.label = list.label();
    }
    
    /*------------------------------- Methods --------------------------------*/
    
    /**
     * Adds a well index.
     * @param    WellIndex    the well index
     */
    public void add(WellIndex index) {
    	this.indices.add(index);
    }
    	
    /**
     * Removes a well index.
     * @param    WellIndex    the well index
     */
    public void remove(WellIndex index) {
    	this.indices.remove(index);
    }
    	
    /**
     * Sets the list name.
     * @param    String    list name
     */
    public void setLabel(String label) {
    	this.label = label;
    }
    	
    /**
     * Returns the list of indices.
     * @return    the list of indices
     */
    public Set<WellIndex> indices() {
    	return this.indices;
    }
    	
    /**
     * Returns the list name.
     * @return    the list name
     */
    public String label() {
    	return this.label;
    }
    	
    /**
     * Returns the size of the list.
     * @return    list size
     */
    public int size() {
    	return this.indices.size();
    }
    
    /**
     * Returns a well index iterator.
     * @return    Iterator<WellIndex> iterator()
     */
    public Iterator<WellIndex> iterator() {
    	return this.indices.iterator();
    }

    /**
     * Returns true if the well set contains the same wells.
     * @param    WellSetInteger    the well set
     * @return                     true if the set and list have equivalent well indices
     */
    public boolean equalsSet(WellSetInteger set) {
    	
    	if(set.size() != this.size()) {
    		return false;
    	}
    	
    	if(!set.label().equals(this.label())) {
    		return false;
    	}
    	
    	for(WellInteger well : set) {
    		WellIndex index = new WellIndex(well.row(), well.column());
    		if(!this.indices.contains(index)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Returns true if the well set contains the same wells.
     * @param    WellSetDouble    the well set
     * @return                    true if the set and list have equivalent well indices
     */
    public boolean equalsSet(WellSetDouble set) {
    	
    	if(set.size() != this.size()) {
    		return false;
    	}
    	
    	for(WellDouble well : set) {
    		WellIndex index = new WellIndex(well.row(), well.column());
    		if(!this.indices.contains(index)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Returns true if the well set contains the same wells.
     * @param    WellSetBigInteger    the well set
     * @return                        true if the set and list have equivalent well indices
     */
    public boolean equalsSet(WellSetBigInteger set) {
    	
    	if(set.size() != this.size()) {
    		return false;
    	}
    	
    	for(WellBigInteger well : set) {
    		WellIndex index = new WellIndex(well.row(), well.column());
    		if(!this.indices.contains(index)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Returns true if the well set contains the same wells.
     * @param    WellSetBigDecimal    the well set
     * @return                        true if the set and list have equivalent well indices
     */
    public boolean equalsSet(WellSetBigDecimal set) {
    	
    	if(set.size() != this.size()) {
    		return false;
    	}
    	
    	for(WellBigDecimal well : set) {
    		WellIndex index = new WellIndex(well.row(), well.column());
    		if(!this.indices.contains(index)) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Return a string containing the label and indices.
     * @return    string holding label and indices
     */
    public String toString() {
    	return this.label() + " " + Arrays.toString(this.indices.toArray()).toString();
    }
    
    /**
     * Lists are equivalent if they contain the same wells.
     * @param    Object    the object
     * @return             true if equal, false otherwise
     */
    public boolean equals(Object object) {
        
        if (object instanceof WellList == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }
        
        WellList list = (WellList) object;
        
        if(this.size() != list.size()) {
        	return false;
        }
        
        for(WellIndex index : list) {
        	if(!this.indices.contains(index)) {
        		return false;
        	}
        }
        
        return true;
    }
    
    /**
     * Hash code uses the label and the indices in the list.
     * @return    the hash code
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                   append(this.indices).
                   append(this.label).
                   toHashCode();    
    }
    
    /**
     * Plates are ordered using the size, data type, label and number of sets.  
     * @param    PlateInteger    the plate for comparison
     * @return                   this == plate --> 0
     *                           this > plate --> 1
     *                           this < plate --> -1
     */
    public int compareTo(WellList list) throws ClassCastException {
    	
        if(this.equals(list)) {
            return 0;
        }
        
        if(this.indices.size() != list.size()) {
        	return this.indices.size() > list.size() ? 1 : -1;
        }
        
        Iterator<WellIndex> iter1 = this.indices.descendingIterator();
        Iterator<WellIndex> iter2 = new TreeSet<WellIndex>(list.indices()).descendingIterator();
        
        while(iter1.hasNext()) {
        	
        	WellIndex index1 = iter1.next();
        	WellIndex index2 = iter2.next();
        	
        	if(index1.row() != index2.row()) {
        		return index1.row() > index2.row() ? 1 : -1;
        	}
        	
        	if(index1.column() != index2.column()) {
        		return index1.column() > index2.column() ? 1 : -1;
        	}
        	
        }
        
        return 0;
    }

}
    	
    

