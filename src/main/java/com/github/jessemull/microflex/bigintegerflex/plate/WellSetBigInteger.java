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

/*--------------------------------- Package ----------------------------------*/

package com.github.jessemull.microflex.bigintegerflex.plate;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;

/**
 * This class represents a set of wells. The well set implements all the methods
 * available in the Java set class. The well set cannot contain duplicate well 
 * values. Unlike the plate object, the well set object does not enforce plate
 * dimensions or wells within a specified range.
 * 
 * Like all members of the MicroFlex library, the well set is meant to be flexible
 * and accepts collections and arrays of wells for constructors and methods.
 * 
 * The well set object implements the Iterable interface. The iterator iterates 
 * over the wells in the set.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellSetBigInteger implements Iterable<WellBigInteger>, Comparable<WellSetBigInteger> {
    
    /* ------------------------------- Fields ------------------------------- */

    private TreeSet<WellBigInteger> wells = new TreeSet<WellBigInteger>();    // List of the wells in the set
    private String label = "WellSetBigInteger";                               // Set label
    
    /* ---------------------------- Constructors ---------------------------- */
    
    /**
     * Creates a new BigInteger well set.
     */
    public WellSetBigInteger() {}   
    
    /**
     * Creates a new well set from a collection of wells.
     * @param    Collection<WellBigInteger>    the collection of wells
     */
    public WellSetBigInteger(Collection<WellBigInteger> collection) {
        
        /* Creates the  set */
        
        this();
        
        /* Adds the collection, add method checks for type */
        
        this.add(collection);
    }
    
    /**
     * Creates a new well set from a collection of wells and a label.
     * @param    Collection<WellBigInteger>    the collection of wells
     * @param    String                        the label
     */
    public WellSetBigInteger(Collection<WellBigInteger> collection, String label) {
        
        /* Adds collection to the set */
        
        this(collection);
        
        /* Adds the collection, add method checks for type */
        
        this.label = label;
    }
    
    /**
     * Creates a new well set from another well set.
     * @param    WellSetBigInteger    the well set
     */
    public WellSetBigInteger(WellSetBigInteger set) {
        
        /* Creates the  set */
        
        this();
        
        /* Adds the wells to the new set */
        
        this.wells.addAll(Arrays.asList(set.toWellArray()));
    }
    
    /**
     * Creates a new well set from another well set and a label.
     * @param    WellSetBigInteger    the well set
     * @param    String               the label
     */
    public WellSetBigInteger(WellSetBigInteger set, String label) {

        /* Adds the wells to the new set */
        
        this(set);
        
        /* Stores the label */
        
        this.label = label;
    }
    
    /**
     * Creates a new well set from an array of wells.
     * @param    Well[]    the array of wells
     */
    public WellSetBigInteger(WellBigInteger[] wells) {
        
        /* Creates the  set */
        
        this();
        
        /* Adds the wells to the new set */
        
        this.wells.addAll(Arrays.asList(wells));
    }
    
    /**
     * Creates a new well set from an array of wells and a label.
     * @param    WellSetBigInteger    well set
     * @param    String               label
     */
    public WellSetBigInteger(WellBigInteger[] wells, String label) {

        /* Adds the wells to the new set */
        
        this(wells);
        
        /* Stores the label */
        
        this.label = label;
    }
    
    /* ---------------------- Methods for Adding Wells ---------------------- */
    
    /**
     * Adds a well to the well set.
     * @param    int    the well
     * @return          true on successful well addition
     */
    public boolean add(WellBigInteger well) { 
        
        try {
            
            boolean add = this.wells.add(well);
            if(!add) {
                throw new IllegalArgumentException("Failed to add well " + 
                                                   well.toString() + 
                                                   ". This well already exists in the set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Adds the wells in the input well set.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful addition of all wells
     */
    public boolean add(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : set) {
            try {
                success = this.add(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /**
     * Adds a collection of wells to the well set.
     * @param    Collection<WellBigInteger>    the well collection
     * @return                                 true on successful addition of all wells
     */
    public boolean add(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : collection) {
            try {
                success = this.add(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /**
     * Adds an array of wells to the well set.
     * @param    Well[]    the well array
     * @return             true on successful addition of all wells
     */
    public boolean add(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : array) {
            try {
                success = this.add(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /* --------------------- Methods for Removing Wells --------------------- */
    
    /**
     * Removes a well from the well set.
     * @param    int    the well
     * @return          true on successful well removal
     */
    public boolean remove(WellBigInteger well) { 
        
        try {
            
            boolean remove = this.wells.remove(well);
            if(!remove) {
                throw new IllegalArgumentException("Failed to remove well. " +
                		"This well does not exists in the set.");
            }
            
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    /**
     * Removes the wells in the input well set.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful removal of all wells
     */
    public boolean remove(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : set) {
            try {
                success = this.remove(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /**
     * Removes a collection of wells from the well set.
     * @param    Collection<WellBigInteger>    the well collection
     * @return                                 true on successful removal of all wells
     */
    public boolean remove(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : collection) {
            try {
                success = this.remove(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /**
     * Removes an array of wells from the well set.
     * @param    Well[]    the well array
     * @return             true on successful removal of all wells
     */
    public boolean remove(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : array) {
            try {
                success = this.remove(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /**
     * Removes a well or a list of delimiter separated wells.
     * @param    String    the well or list of wells
     * @param    String    the list delimiter
     * @return             true on successful removal of all wells
     */
    public boolean remove(String wellList, String delimiter) {
        
        boolean success = true;

        String[] split = wellList.split(delimiter);

        for(String wellID : split) {
            try {
            	this.remove(new WellBigInteger(wellID));
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes the well with the well index.
     * @param    String    the well index
     * @return             true on successful well removal
     */
    public boolean remove(String well) {
        return this.remove(well, ",");
    }
    
    /**
     * Removes the well with the well index.
     */
    public boolean remove(WellIndex index) {

        boolean success = true;
        
        try {
            Preconditions.checkNotNull(index, "The index cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
      
        try {
            success = this.remove(new WellBigInteger(index.row(), index.column())) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
        
        return success;
    	
    }
    
    /**
     * Removes the wells in the list.
     * @param    WellList    the well list
     * @return               true on successful removal
     */
    public boolean remove(WellList list) {

    	boolean success = true;
        
        try {
            Preconditions.checkNotNull(list, "The list cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellIndex index : list) {
            try {
                success = this.remove(new WellBigInteger(index.row(), index.column())) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /*----------------------- Methods for Replacing Wells --------------------*/
    
    /**
     * Replaces the set well with the input well. Adds the well if the well
     * does not exist.
     * @param    Well    the well for replacement
     * @return           true on successful well replacement
     */
    public boolean replace(WellBigInteger well) {
        
        try {
            if(this.contains(well)) {
            	this.remove(well);
            }
            this.add(well);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Replaces the set wells with the wells from the input set. Adds the 
     * well if the well does not exist.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful replacement of all wells
     */
    public boolean replace(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : set) {
            try {
            	if(this.contains(well)) {
                	this.remove(well);
                }
                this.add(well);
            } catch(Exception e) {
            	System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }
    
    /**
     * Replaces the set wells with the wells from the collection. Adds the 
     * well if the well does not exist.
     * @param    Collection<WellBigInteger>    the data collection
     * @return                                 true on successful replacement of all wells
     */
    public boolean replace(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : collection) {
            try {
                if(this.contains(well)) {
                	this.remove(well);
                }
                this.add(well);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }
    
    /**
     * Replaces the set wells with the wells from the array. Adds the well data 
     * if the well does not exist.
     * @param    Well[]    the array of wells
     * @return             true on successful replacement of all wells
     */
    public boolean replace(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : array) {
            try {
                this.remove(well);
                this.add(well);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }
    
    /*---------------------- Methods for Retaining Wells ---------------------*/
    
    /**
     * Retains the input well if it exists.
     * @param    Well    the well
     * @return           true on successful well retention
     */
    public boolean retain(WellBigInteger well) {
        
        boolean success = true;
        
        List<WellBigInteger> list = new ArrayList<WellBigInteger>();
        list.add(well);
        
        try {
            success = this.wells.retainAll(list);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return success;
    }
    
    /**
     * Retains the wells in the well set if they exist.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful retention of all wells
     */
    public boolean retain(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.wells.retainAll(set.allWells()) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
               
        return success;
    }
    
    /**
     * Retains the wells in the collection if they exist.
     * @param    Collection<WellBigInteger>    the data collection
     * @return                                 true on successful retention of all wells
     */
    public boolean retain(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.wells.retainAll(collection) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
               
        return success;
    }
    
    /**
     * Retains the wells in the array if they exist.
     * @param    WellBigInteger[]    the array of wells
     * @return                       true on successful retention of all wells
     */
    public boolean retain(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.wells.retainAll(Arrays.asList(array)) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
               
        return success;
    }
    
    /**
     * Retain the wells in the delimiter separated list.
     * @param    String    delimiter separated list of wells
     * @param    String    delimiter for the list of wells
     * @return             all wells with matching well indices or null
     */
    public boolean retain(String wellList, String delimiter) {
        
        if(wellList == null || delimiter == null) {
            return false;
        }

        String[] split = wellList.split(delimiter);

        WellSetBigInteger retain = new WellSetBigInteger();
        
        for(String well : split) {
            retain.add(new WellBigInteger(well));
        }
        
        return retain(retain);
    }
    
    /**
     * Retains the well.
     * @param    String    the well index
     * @return             true on successful retention
     */
    public boolean retain(String well) {
    	return retain(well, ",");
    }
    
    /**
     * Retains the well with the well index.
     * @param    WellIndex    the well index
     * @return                true on successful retention
     */
    public boolean retain(WellIndex index) {
    	return retain(new WellBigInteger(index.row(), index.column()));
    }
    
    /**
     * Retains the wells in the list.
     * @param    WellList    the well list
     * @return               true on successful retention  
     */
    public boolean retain(WellList list) {
    	
    	WellSetBigInteger set = new WellSetBigInteger();
    	
    	for(WellIndex index : list) {
    		set.add(new WellBigInteger(index.row(), index.column()));
    	}
    	
    	return retain(set);
    	
    }

    /*------------------------- Methods for Well Lookup ----------------------*/
    
    /**
     * Returns true if the well is part of the well set.
     * @param    WellBigInteger    the well
     * @return                     true if the well exists in the set
     */
    public boolean contains(WellBigInteger well) {
        return this.wells.contains(well);
    }   

    /**
     * Returns true if all the input wells are found within the well set.
     * @param    WellSetBigInteger    the input wells
     * @return                        true if all input wells are found within the set
     */
    public boolean contains(WellSetBigInteger set) {

        if(set == null) {
            return false;
        }

        for(WellBigInteger well : set) {
            if(!this.contains(well)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if all the input wells are found within the well set.
     * @param    Collection<WellBigInteger>    the input wells
     * @return                                 true if all input wells 
     *                                         are found within the set
     */
    public boolean contains(Collection<WellBigInteger> collection) {

        if(collection == null) {
            return false;
        }

        for(WellBigInteger well : collection) {
            if(!this.contains(well)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if all the input wells are found within the well array.
     * @param    WellBigInteger[]    the input wells
     * @return                       true if all input wells are found within 
     *                               the set
     */
    public boolean contains(WellBigInteger[] array) {

        if(array == null) {
            return false;
        }

        for(WellBigInteger well : array) {
            if(!this.contains(well)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if the wells are part of the set.
     * @param    String    delimiter separated list of wells
     * @param    String    delimiter for the list of wells
     * @return             true if the wells exist in the sest
     */
    public boolean contains(String wellList, String delimiter) {
        
        if(wellList == null || delimiter == null) {
            return false;
        }
        
        String[] split = wellList.split(delimiter);
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(String well : split) {
            set.add(new WellBigInteger(well));
        }
        
        return contains(set);
    }
    
    /**
     * Returns true if the well is part of the well set.
     * @param    String    the well
     * @return             true if the well exists in the set
     */
    public boolean contains(String well) {
        return this.wells.contains(new WellBigInteger(well));
    }
    
    /**
     * Returns true if the well is part of the set.
     * @param    WellIndex    the well index
     * @return                true if the well exists
     */
    public boolean contains(WellIndex index) {
        return contains(new WellBigInteger(index.row(), index.column()));
    }
    
    /**
     * Returns true if the wells exist in the set.
     * @param    WellList    the well list
     * @return               true if all the well exist
     */
    public boolean contains(WellList list) {
    	
    	WellSetBigInteger set = new WellSetBigInteger();
    	
    	for(WellIndex index : list) {
    		set.add(new WellBigInteger(index.row(), index.column()));
    	}
    	
    	return contains(set);
    	
    }
    
    /**
     * Returns an iterator over the wells in this set in descending order.
     * @return    iterator for traversing set in descending order
     */
    public Iterator<WellBigInteger> descendingIterator() {
        return this.wells.descendingIterator();
    }
    
    /**
     * Returns a reverse order view of the wells contained in this set.
     * @return    the set in descending order
     */
    public Set<WellBigInteger> descendingSet() {
        return this.wells.descendingSet();
    }
    
    /**
     * Returns the first well in the set.
     * @return    the first well in the set
     */
    public WellBigInteger first() {
        return this.wells.first();
    }
    
    /**
     * Returns the last well in the set.
     * @return    the last well in the set
     */
    public WellBigInteger last() {
        return this.wells.last();
    }
    
    /**
     * Returns the least well in this set greater than or equal to the given 
     * well, or null if there is no such well.
     * @param    Well    the well
     * @return           least well greater than or equal to the input well
     */
    public WellBigInteger ceiling(WellBigInteger well) {
        return this.wells.ceiling(well);
    }
    
    /**
     * Returns the greatest well in this set less than or equal to the given 
     * well, or null if there is no such well.
     * @return    greatest well less than or equal to the input well
     */
    public WellBigInteger floor(WellBigInteger well) {
        return this.wells.floor(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are strictly 
     * less than the input well.
     * @param    Well    input well
     * @return           well set with wells less than the input well
     */
    public Set<WellBigInteger> headSet(WellBigInteger well) {
        return this.wells.headSet(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * (or equal to, if inclusive is true) to the input well.
     * @param    Well       input well
     * @param    boolean    a true value retains the input well in the sub set
     * @return              well set with wells less than the input well
     */
    public Set<WellBigInteger> headSet(WellBigInteger well, boolean inclusive) {
        return this.wells.headSet(well, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * or equal to the index.
     * @param     Well    the input well
     * @return            set with wells less than or equal to the input well
     */
    public Set<WellBigInteger> headSet(int index) {
        return this.subSet(0, index);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * (or equal to, if inclusive is true) the index.
     * @param     Well    the input well
     * @return            set with wells less than or equal to the input well
     */
    public Set<WellBigInteger> headSet(int index, boolean inclusive) {
        return this.subSet(0, true, index, inclusive);
    }
    
    /**
     * Returns the least well in this set strictly greater than the given 
     * well, or null if there is no such well.
     * @param    Well    input well
     * @return           greatest well less than or equal to the input well
     */
    public WellBigInteger higher(WellBigInteger well) {
        return this.wells.higher(well);
    }
    
    /**
     * Returns true if this set contains no wells.
     * @return    greatest well less than or equal to the input well
     */
    public boolean isEmpty() {
        return this.wells.isEmpty();
    }
    
    /**
     * Returns an iterator over the wells in this set in ascending order.
     * @return    iterator traversing set in ascending order
     */
    public Iterator<WellBigInteger> iterator() {
        return this.wells.iterator();
    }
    
    /**
     * Returns the greatest well in this set strictly less than the given well, 
     * or null if there is no such well.
     * @param     Well    input well
     * @return            least well greater than or equal to the input well
     */
    public WellBigInteger lower(WellBigInteger well) {
        return this.wells.lower(well);
    }
    
    /**
     * Retrieves and removes the first (lowest) well, or returns null if this 
     * set is empty.
     * @return    first/lowest well
     */
    public WellBigInteger pollFirst() {
        return this.wells.pollFirst();
    }
    
    /**
     * Retrieves and removes the last (highest) well, or returns null if this 
     * set is empty.
     * @return    last/highest well
     */
    public WellBigInteger pollLast() {
        return this.wells.pollLast();
    }
    
    /**
     * Returns a view of the portion of this set whose wells range from 
     * the first input well to the second input well.
     * @param    Well       the first input well
     * @param    Well       the second input well
     * @param    boolean    includes the first input well in the returned set
     * @param    boolean    includes the second input well in the returned set
     * @return              last well in the set
     */
    public Set<WellBigInteger> subSet(WellBigInteger well1, boolean inclusive1, WellBigInteger well2, boolean inclusive2) {
        return this.wells.subSet(well1, inclusive1, well2, inclusive2);
    }
    
    /**
     * Returns a view of the portion of this set whose wells range from 
     * the first input well inclusive to the second input well inclusive.
     * @param    Well    the first input well
     * @param    Well    the second input well
     * @return           last well in the set
     */
    public Set<WellBigInteger> subSet(WellBigInteger well1, WellBigInteger well2) {
        return this.wells.subSet(well1, well2);
    }
    
    /**
     * Returns a view of the portion of this set between the indices.
     * @param    int        the first index
     * @param    int        the second index
     * @param    boolean    includes the first input well in the returned set
     * @param    boolean    includes the second input well in the returned set
     * @return              last well in the set
     */
    public Set<WellBigInteger> subSet(int index1, boolean inclusive1, int index2, boolean inclusive2) {
        
        if(index1 > this.wells.size() - 1 || 
    	    index2 > this.wells.size() - 1 ||
    	    index1 < 0 ||
    	    index2 < 0 ||
    	    index1 > index2) {
    	    throw new IndexOutOfBoundsException("Index is outside of valid range: " + index1 + " " + index2);
    	}
        
        int current = 0;

        Set<WellBigInteger> subset = new TreeSet<WellBigInteger>();
        Iterator<WellBigInteger> iter = this.wells.iterator();
        
        while(current < index1) {
        	
        	if(current == index1 && inclusive1) {
        		subset.add(iter.next());
        	}
        	
        	iter.next();
        	current++;
        }
        
        while(current < index2) {
        	if(current++ == index2 && inclusive2) {
        		subset.add(iter.next());
        		break;
        	}
        	
        	subset.add(iter.next());
        }
        
        return subset;
    }
    
    /**
     * Returns a view of the portion of this set between the indices.
     * @param    int    the first index
     * @param    int    the second index
     * @return          the subset
     */
    public Set<WellBigInteger> subSet(int index1, int index2) {
        
        if(index1 > this.wells.size() - 1  || 
    	    index2 > this.wells.size() - 1 ||
    	    index1 < 0                     ||
    	    index2 < 0                     ||
    	    index1 > index2) {
    	    throw new IndexOutOfBoundsException("Index is outside of valid range: " + index1 + " " + index2 + " " + this.wells.size());
    	}
        
        int current = 0;

        Set<WellBigInteger> subset = new TreeSet<WellBigInteger>();
        Iterator<WellBigInteger> iter = this.wells.iterator();
        
        while(current < index1) {
        	iter.next();
        	current++;
        }

        while(current < index2) {
        	subset.add(iter.next());
        	current++;
        }
        
        return subset;
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * or equal to the input well.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellBigInteger> tailSet(WellBigInteger well) {
        return this.wells.tailSet(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * (or equal to, if inclusive is true) the input well.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellBigInteger> tailSet(WellBigInteger well, boolean inclusive) {
        return this.wells.tailSet(well, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * or equal to the index.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellBigInteger> tailSet(int index) {
        return this.subSet(index, this.wells.size() - 1);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * (or equal to, if inclusive is true) the index.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellBigInteger> tailSet(int index, boolean inclusive) {
        return this.subSet(index, inclusive, this.wells.size() - 1, true);
    }
    
    /*---------------------- Methods for well retrieval ----------------------*/
    
    /**
     * Returns the well set.
     * @return    the well set
     */
    public Set<WellBigInteger> allWells() {
        return this.wells;
    }
    
    /**
     * Returns the well or null if no such well exists.
     * @param    WellBigInteger    the well
     * @return                     the plate well or null if the well does not exist
     */
    public WellBigInteger getWells(WellBigInteger input) {
    	
    	for(WellBigInteger well : this.wells) {
            if(input.row() == well.row() && input.column() == well.column()) {
                return well;
            }
        }
        
        return null;
    }
    
    /**
     * Returns a well set holding all plate wells that match the indices or null 
     * if there are no matches.
     * @param    WellSetBigInteger        the input well set
     * @return                            the output well set
     */
    public WellSetBigInteger getWells(WellSetBigInteger input) {
        
        if(input == null) {
            return null;
        }
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : input) {
            if(this.contains(well)) {
                set.add(this.getWells(well));
            }
        }
        
        return set.size() == 0 ? null : set;
    }
    
    /**
     * Returns a well set holding all plate wells that match the indices or null 
     * if there are no matches.
     * @param    Collection<WellBigInteger>    collection of wells
     * @return                                 all wells with matching well indices or null
     */
    public WellSetBigInteger getWells(Collection<WellBigInteger> collection) {
        
        if(collection == null) {
            return null;
        }
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : collection) {
            if(this.contains(well)) {
                set.add(this.getWells(well));
            }
        }
        
        return set.size() == 0 ? null : set;
    }
    
    /**
     * Returns a well set holding all wells that match the indices or null 
     * if there are no matches.
     * @param    WellBigInteger[]    array of well indices
     * @return                       all wells with matching well indices or null
     */
    public WellSetBigInteger getWells(WellBigInteger[] array) {
        
        if(array == null) {
            return null;
        }
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : array) {
            if(this.contains(well)) {
                set.add(this.getWells(well));
            }
        }
        
        return set.size() == 0 ? null : set;
    }
    
    /**
     * Returns a well set holding all plate wells that match the indices or 
     * null if there are no matches.
     * @param    String    delimiter separated list of wells
     * @param    String    delimiter for the list of wells
     * @return             all wells with matching well indices or null
     */
    public WellSetBigInteger getWells(String wellList, String delimiter) {
        
        if(wellList == null || delimiter == null) {
            return null;
        }
        
        String[] split = wellList.split(delimiter);
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(String well : split) {
            if(this.contains(well.trim())) {
                set.add(this.getWells(well.trim()));
            }
        }
        
        return set.size() == 0 ? null : set;
    }
    
    /**
     * Returns the well with the input indices.
     * @param    String    the well index
     * @return             well with matching well ID
     */
    public WellBigInteger getWells(String well) {
        
        if(well == null) {
            return null;
        }
        
        WellBigInteger input = new WellBigInteger(well);
        
        for(WellBigInteger bigInteger : this.wells) {
            if(input.row() == bigInteger.row() && input.column() == bigInteger.column()) {
                return bigInteger;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the well or null if the well does not exist.
     * @param    WellIndex    the well index
     * @return                the well
     */
     public WellBigInteger getWells(WellIndex index) {
        
        if(index == null) {
            return null;
        }
        
        WellBigInteger input = new WellBigInteger(index.row(), index.column());
        
        for(WellBigInteger bigInteger : this.wells) {
            if(input.row() == bigInteger.row() && input.column() == bigInteger.column()) {
                return bigInteger;
            }
        }
        
        return null;
    }

     /**
      * Returns a well set containing the wells in the list or null if none of
      * the wells exist.
      * @param    WellList    the well list
      * @return               the wells
      */
     public WellSetBigInteger getWells(WellList list) {
    	 
    	 WellSetBigInteger set = new WellSetBigInteger();
    	 
    	 for(WellIndex index : list) {
    		 set.add(new WellBigInteger(index.row(), index.column()));
    	 }
    	 
    	 return getWells(set);
     }
     
    /**
     * Returns all wells with the matching row index.
     * @param    int    the row index
     * @return          wells with matching row index
     */
    public WellSetBigInteger getRow(int row) {

        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : this.wells) {
            if(row == well.row()) {
                set.add(well);
            }
        }
        
        return set.isEmpty() ? null : set;
    }
    
    /**
     * Returns all wells with the matching column index.
     * @param    int    the row index
     * @return          wells with the matching column index
     */
    public WellSetBigInteger getColumn(int column) {
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : this.wells) {
            if(column == well.column()) {
                set.add(well);
            }
        }
        
        return set.isEmpty() ? null : set;
    }
    
    /*---------------------------- Other Methods -----------------------------*/
    
    /**
     * Clears the set.
     */
    public void clear() {
        this.wells.clear();
    }
    
    /**
     * Returns the well set as an array of wells.
     * @return    array of wells
     */
	public WellBigInteger[] toWellArray() {
        return this.wells.toArray(new WellBigInteger[this.wells.size()]);
    }
    
    /**
     * Sets the set label.
     * @param    String    the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Returns the size of the well set.
     * @return    size of the well set
     */
    public int size() {
        return wells.size();
    }
    
    /**
     * Returns all indices in the set in a well list.
     * @return    the list of well indices
     */
    public WellList wellList() {
    	
    	WellList list = new WellList();
    	list.setLabel(this.label);
    	
    	for(WellBigInteger well : this.wells) {
    		list.add(new WellIndex(well.row(), well.column()));
    	}
    	
    	return list;
    }
    
    /*---------------------------- String Methods ----------------------------*/
    
    /**
     * Returns the well set as an array of well IDs.
     * @return    the array of well IDs
     */
    public String[] toStringArray() {
        
        String[] wellStrings = new String[this.wells.size()];
        
        int i = 0;
        for(WellBigInteger well : this.wells) {
            wellStrings[i++] = well.toString();
        }

        return wellStrings;
        
    }
    
    /**
     * Returns the label.
     * @return    label
     */
    public String label() {
    	
    	if(this.label != null) {
    		return this.label;
    	}

    	Iterator<WellBigInteger> iter = this.wells.iterator();
    	
    	String str = "WellSetBigInteger " + iter.next().toString();
    	
    	while(iter.hasNext()) {
    		str += ", " + iter.next().toString();
    	}
    	
    	return str;
    }
    
    /**
     * Returns a string containing the label and wells.
     * @return    label and well IDs
     */
    public String toString() {
        
        String toString = "";
        
        if(this.label != null) {
            toString += this.label + "\n";
        }

        Iterator<WellBigInteger> iter = this.wells.iterator();
        
        while(iter.hasNext()) {
            WellBigInteger well = iter.next();
            
            if(iter.hasNext()) {
                toString += well.toString() + "\n";
            } else {
            	toString += well.toString();
            }
        }

        return toString;
    }

    /*---------------------- Equals and HashCode Methods ---------------------*/

    /**
     * Well sets are equivalent if they contain the same wells
     * and label.
     * @param    Object    the object
     * @return             true if equal, false otherwise
     */
    public boolean equals(Object object) {
        
        if (object instanceof WellSetBigInteger == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }
        
		WellSetBigInteger set = (WellSetBigInteger) object;
        
        return this.wells.equals(set.wells) && 
               this.label.equals(set.label);      
    }
    
    /**
     * Hash code uses the wells and label.
     * @return    the hash code
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                   append(this.wells).
                   append(this.label).
                   toHashCode();    
    }

    /**
     * Compares two well set objects.
     */
	public int compareTo(WellSetBigInteger set) {
		
		if(this.equals(set)) {
            return 0;
        }
        
		if(this.label().compareTo(set.label()) == 1) {
            return 1;
        } else if(this.label().compareTo(set.label()) != 0) {
            return -1;
        }
		
        if(this.size() > set.size()) {
            return 1;
        } else if(this.size() != set.size()) {
            return -1;
        }
        
        Iterator<WellBigInteger> iter1 = this.iterator();
        Iterator<WellBigInteger> iter2 = set.iterator();
        
        while(iter1.hasNext() && iter2.hasNext()) {
        	
        	WellBigInteger well1 = iter1.next();
        	WellBigInteger well2 = iter2.next();
        	
        	int comparison = well1.compareTo(well2);
        	
        	if(comparison != 0) {
        		return comparison;
        	}
        	
        }
        
        return 0;
	}
    
}
