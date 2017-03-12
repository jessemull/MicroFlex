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

package com.github.jessemull.microflex.doubleflex.plate;

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
public class WellSetDouble implements Iterable<WellDouble>, Comparable<WellSetDouble> {
    
    /* ------------------------------- Fields ------------------------------- */

    private TreeSet<WellDouble> wells = new TreeSet<WellDouble>();    // List of the wells in the set
    private String label = "WellSetDouble";                           // Set label
    
    /* ---------------------------- Constructors ---------------------------- */
    
    /**
     * Creates a new Double well set.
     */
    public WellSetDouble() {}   
    
    /**
     * Creates a new well set from a collection of wells.
     * @param    Collection<WellDouble>    the collection of wells
     */
    public WellSetDouble(Collection<WellDouble> collection) {
        
        /* Creates the  set */
        
        this();
        
        /* Adds the collection, add method checks for type */
        
        this.add(collection);
    }
    
    /**
     * Creates a new well set from a collection of wells and a label.
     * @param    Collection<WellDouble>    the collection of wells
     * @param    String                    the label
     */
    public WellSetDouble(Collection<WellDouble> collection, String label) {
        
        /* Adds collection to the set */
        
        this(collection);
        
        /* Adds the collection, add method checks for type */
        
        this.label = label;
    }
    
    /**
     * Creates a new well set from another well set.
     * @param    WellSetDouble    the well set
     */
    public WellSetDouble(WellSetDouble set) {
        
        /* Creates the  set */
        
        this();
        
        /* Adds the wells to the new set */
        
        this.wells.addAll(Arrays.asList(set.toWellArray()));
    }
    
    /**
     * Creates a new well set from another well set and a label.
     * @param    WellSetDouble    the well set
     * @param    String           the label
     */
    public WellSetDouble(WellSetDouble set, String label) {

        /* Adds the wells to the new set */
        
        this(set);
        
        /* Stores the label */
        
        this.label = label;
    }
    
    /**
     * Creates a new well set from an array of wells.
     * @param    Well[]    the array of wells
     */
    public WellSetDouble(WellDouble[] wells) {
        
        /* Creates the  set */
        
        this();
        
        /* Adds the wells to the new set */
        
        this.wells.addAll(Arrays.asList(wells));
    }
    
    /**
     * Creates a new well set from an array of wells and a label.
     * @param    WellSetDouble    well set
     * @param    String           label
     */
    public WellSetDouble(WellDouble[] wells, String label) {

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
    public boolean add(WellDouble well) { 
        
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
     * @param    WellSetDouble    the well set
     * @return                    true on successful addition of all wells
     */
    public boolean add(WellSetDouble set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : set) {
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
     * @param    Collection<WellDouble>    the well collection
     * @return                             true on successful addition of all wells
     */
    public boolean add(Collection<WellDouble> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : collection) {
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
    public boolean add(WellDouble[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : array) {
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
    public boolean remove(WellDouble well) { 
        
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
     * @param    WellSetDouble    the well set
     * @return                    true on successful removal of all wells
     */
    public boolean remove(WellSetDouble set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : set) {
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
     * @param    Collection<WellDouble>    the well collection
     * @return                             true on successful removal of all wells
     */
    public boolean remove(Collection<WellDouble> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : collection) {
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
    public boolean remove(WellDouble[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : array) {
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
            	this.remove(new WellDouble(wellID));
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
            success = this.remove(new WellDouble(index.row(), index.column())) ? success : false;
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
                success = this.remove(new WellDouble(index.row(), index.column())) ? success : false;
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
    public boolean replace(WellDouble well) {
        
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
     * @param    WellSetDouble    the well set
     * @return                    true on successful replacement of all wells
     */
    public boolean replace(WellSetDouble set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : set) {
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
     * @param    Collection<WellDouble>    the data collection
     * @return                             true on successful replacement of all wells
     */
    public boolean replace(Collection<WellDouble> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : collection) {
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
    public boolean replace(WellDouble[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellDouble well : array) {
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
    public boolean retain(WellDouble well) {
        
        boolean success = true;
        
        List<WellDouble> list = new ArrayList<WellDouble>();
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
     * @param    WellSetDouble    the well set
     * @return                    true on successful retention of all wells
     */
    public boolean retain(WellSetDouble set) {
        
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
     * @param    Collection<WellDouble>    the data collection
     * @return                             true on successful retention of all wells
     */
    public boolean retain(Collection<WellDouble> collection) {
        
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
     * @param    WellDouble[]    the array of wells
     * @return                   true on successful retention of all wells
     */
    public boolean retain(WellDouble[] array) {
        
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

        WellSetDouble retain = new WellSetDouble();
        
        for(String well : split) {
            retain.add(new WellDouble(well));
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
    	return retain(new WellDouble(index.row(), index.column()));
    }
    
    /**
     * Retains the wells in the list.
     * @param    WellList    the well list
     * @return               true on successful retention  
     */
    public boolean retain(WellList list) {
    	
    	WellSetDouble set = new WellSetDouble();
    	
    	for(WellIndex index : list) {
    		set.add(new WellDouble(index.row(), index.column()));
    	}
    	
    	return retain(set);
    	
    }

    /*------------------------- Methods for Well Lookup ----------------------*/
    
    /**
     * Returns true if the well is part of the well set.
     * @param    WellDouble    the well
     * @return                 true if the well exists in the set
     */
    public boolean contains(WellDouble well) {
        return this.wells.contains(well);
    }   

    /**
     * Returns true if all the input wells are found within the well set.
     * @param    WellSetDouble    the input wells
     * @return                    true if all input wells are found within the set
     */
    public boolean contains(WellSetDouble set) {

        if(set == null) {
            return false;
        }

        for(WellDouble well : set) {
            if(!this.contains(well)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if all the input wells are found within the well set.
     * @param    Collection<WellDouble>    the input wells
     * @return                             true if all input wells 
     *                                     are found within the set
     */
    public boolean contains(Collection<WellDouble> collection) {

        if(collection == null) {
            return false;
        }

        for(WellDouble well : collection) {
            if(!this.contains(well)) {
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Returns true if all the input wells are found within the well array.
     * @param    WellDouble[]    the input wells
     * @return                   true if all input wells are found within 
     *                           the set
     */
    public boolean contains(WellDouble[] array) {

        if(array == null) {
            return false;
        }

        for(WellDouble well : array) {
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
        
        WellSetDouble set = new WellSetDouble();
        
        for(String well : split) {
            set.add(new WellDouble(well));
        }
        
        return contains(set);
    }
    
    /**
     * Returns true if the well is part of the well set.
     * @param    String    the well
     * @return             true if the well exists in the set
     */
    public boolean contains(String well) {
        return this.wells.contains(new WellDouble(well));
    }
    
    /**
     * Returns true if the well is part of the set.
     * @param    WellIndex    the well index
     * @return                true if the well exists
     */
    public boolean contains(WellIndex index) {
        return contains(new WellDouble(index.row(), index.column()));
    }
    
    /**
     * Returns true if the wells exist in the set.
     * @param    WellList    the well list
     * @return               true if all the well exist
     */
    public boolean contains(WellList list) {
    	
    	WellSetDouble set = new WellSetDouble();
    	
    	for(WellIndex index : list) {
    		set.add(new WellDouble(index.row(), index.column()));
    	}
    	
    	return contains(set);
    	
    }
    
    /**
     * Returns an iterator over the wells in this set in descending order.
     * @return    iterator for traversing set in descending order
     */
    public Iterator<WellDouble> descendingIterator() {
        return this.wells.descendingIterator();
    }
    
    /**
     * Returns a reverse order view of the wells contained in this set.
     * @return    the set in descending order
     */
    public Set<WellDouble> descendingSet() {
        return this.wells.descendingSet();
    }
    
    /**
     * Returns the first well in the set.
     * @return    the first well in the set
     */
    public WellDouble first() {
        return this.wells.first();
    }
    
    /**
     * Returns the last well in the set.
     * @return    the last well in the set
     */
    public WellDouble last() {
        return this.wells.last();
    }
    
    /**
     * Returns the least well in this set greater than or equal to the given 
     * well, or null if there is no such well.
     * @param    Well    the well
     * @return           least well greater than or equal to the input well
     */
    public WellDouble ceiling(WellDouble well) {
        return this.wells.ceiling(well);
    }
    
    /**
     * Returns the greatest well in this set less than or equal to the given 
     * well, or null if there is no such well.
     * @return    greatest well less than or equal to the input well
     */
    public WellDouble floor(WellDouble well) {
        return this.wells.floor(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are strictly 
     * less than the input well.
     * @param    Well    input well
     * @return           well set with wells less than the input well
     */
    public Set<WellDouble> headSet(WellDouble well) {
        return this.wells.headSet(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * (or equal to, if inclusive is true) to the input well.
     * @param    Well       input well
     * @param    boolean    a true value retains the input well in the sub set
     * @return              well set with wells less than the input well
     */
    public Set<WellDouble> headSet(WellDouble well, boolean inclusive) {
        return this.wells.headSet(well, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * or equal to the index.
     * @param     Well    the input well
     * @return            set with wells less than or equal to the input well
     */
    public Set<WellDouble> headSet(int index) {
        return this.subSet(0, index);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * (or equal to, if inclusive is true) the index.
     * @param     Well    the input well
     * @return            set with wells less than or equal to the input well
     */
    public Set<WellDouble> headSet(int index, boolean inclusive) {
        return this.subSet(0, true, index, inclusive);
    }
    
    /**
     * Returns the least well in this set strictly greater than the given 
     * well, or null if there is no such well.
     * @param    Well    input well
     * @return           greatest well less than or equal to the input well
     */
    public WellDouble higher(WellDouble well) {
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
    public Iterator<WellDouble> iterator() {
        return this.wells.iterator();
    }
    
    /**
     * Returns the greatest well in this set strictly less than the given well, 
     * or null if there is no such well.
     * @param     Well    input well
     * @return            least well greater than or equal to the input well
     */
    public WellDouble lower(WellDouble well) {
        return this.wells.lower(well);
    }
    
    /**
     * Retrieves and removes the first (lowest) well, or returns null if this 
     * set is empty.
     * @return    first/lowest well
     */
    public WellDouble pollFirst() {
        return this.wells.pollFirst();
    }
    
    /**
     * Retrieves and removes the last (highest) well, or returns null if this 
     * set is empty.
     * @return    last/highest well
     */
    public WellDouble pollLast() {
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
    public Set<WellDouble> subSet(WellDouble well1, boolean inclusive1, WellDouble well2, boolean inclusive2) {
        return this.wells.subSet(well1, inclusive1, well2, inclusive2);
    }
    
    /**
     * Returns a view of the portion of this set whose wells range from 
     * the first input well inclusive to the second input well inclusive.
     * @param    Well    the first input well
     * @param    Well    the second input well
     * @return           last well in the set
     */
    public Set<WellDouble> subSet(WellDouble well1, WellDouble well2) {
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
    public Set<WellDouble> subSet(int index1, boolean inclusive1, int index2, boolean inclusive2) {
        
        if(index1 > this.wells.size() - 1 || 
    	    index2 > this.wells.size() - 1 ||
    	    index1 < 0 ||
    	    index2 < 0 ||
    	    index1 > index2) {
    	    throw new IndexOutOfBoundsException("Index is outside of valid range: " + index1 + " " + index2);
    	}
        
        int current = 0;

        Set<WellDouble> subset = new TreeSet<WellDouble>();
        Iterator<WellDouble> iter = this.wells.iterator();
        
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
    public Set<WellDouble> subSet(int index1, int index2) {
        
        if(index1 > this.wells.size() - 1  || 
    	    index2 > this.wells.size() - 1 ||
    	    index1 < 0                     ||
    	    index2 < 0                     ||
    	    index1 > index2) {
    	    throw new IndexOutOfBoundsException("Index is outside of valid range: " + index1 + " " + index2 + " " + this.wells.size());
    	}
        
        int current = 0;

        Set<WellDouble> subset = new TreeSet<WellDouble>();
        Iterator<WellDouble> iter = this.wells.iterator();
        
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
    public Set<WellDouble> tailSet(WellDouble well) {
        return this.wells.tailSet(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * (or equal to, if inclusive is true) the input well.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellDouble> tailSet(WellDouble well, boolean inclusive) {
        return this.wells.tailSet(well, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * or equal to the index.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellDouble> tailSet(int index) {
        return this.subSet(index, this.wells.size() - 1);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * (or equal to, if inclusive is true) the index.
     * @param     Well    the input well
     * @return            set with wells greater than or equal to the input well
     */
    public Set<WellDouble> tailSet(int index, boolean inclusive) {
        return this.subSet(index, inclusive, this.wells.size() - 1, true);
    }
    
    /*---------------------- Methods for well retrieval ----------------------*/
    
    /**
     * Returns the well set.
     * @return    the well set
     */
    public Set<WellDouble> allWells() {
        return this.wells;
    }
    
    /**
     * Returns the well or null if no such well exists.
     * @param    WellDouble    the well
     * @return                 the plate well or null if the well does not exist
     */
    public WellDouble getWells(WellDouble input) {
    	
    	for(WellDouble well : this.wells) {
            if(input.row() == well.row() && input.column() == well.column()) {
                return well;
            }
        }
        
        return null;
    }
    
    /**
     * Returns a well set holding all plate wells that match the indices or null 
     * if there are no matches.
     * @param    WellSetDouble        the input well set
     * @return                        the output well set
     */
    public WellSetDouble getWells(WellSetDouble input) {
        
        if(input == null) {
            return null;
        }
        
        WellSetDouble set = new WellSetDouble();
        
        for(WellDouble well : input) {
            if(this.contains(well)) {
                set.add(this.getWells(well));
            }
        }
        
        return set.size() == 0 ? null : set;
    }
    
    /**
     * Returns a well set holding all plate wells that match the indices or null 
     * if there are no matches.
     * @param    Collection<WellDouble>    collection of wells
     * @return                             all wells with matching well indices or null
     */
    public WellSetDouble getWells(Collection<WellDouble> collection) {
        
        if(collection == null) {
            return null;
        }
        
        WellSetDouble set = new WellSetDouble();
        
        for(WellDouble well : collection) {
            if(this.contains(well)) {
                set.add(this.getWells(well));
            }
        }
        
        return set.size() == 0 ? null : set;
    }
    
    /**
     * Returns a well set holding all wells that match the indices or null 
     * if there are no matches.
     * @param    WellDouble[]    array of well indices
     * @return                   all wells with matching well indices or null
     */
    public WellSetDouble getWells(WellDouble[] array) {
        
        if(array == null) {
            return null;
        }
        
        WellSetDouble set = new WellSetDouble();
        
        for(WellDouble well : array) {
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
    public WellSetDouble getWells(String wellList, String delimiter) {
        
        if(wellList == null || delimiter == null) {
            return null;
        }
        
        String[] split = wellList.split(delimiter);
        
        WellSetDouble set = new WellSetDouble();
        
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
    public WellDouble getWells(String well) {
        
        if(well == null) {
            return null;
        }
        
        WellDouble input = new WellDouble(well);
        
        for(WellDouble wellDouble : this.wells) {
            if(input.row() == wellDouble.row() && input.column() == wellDouble.column()) {
                return wellDouble;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the well or null if the well does not exist.
     * @param    WellIndex    the well index
     * @return                the well
     */
     public WellDouble getWells(WellIndex index) {
        
        if(index == null) {
            return null;
        }
        
        WellDouble input = new WellDouble(index.row(), index.column());
        
        for(WellDouble wellDouble : this.wells) {
            if(input.row() == wellDouble.row() && input.column() == wellDouble.column()) {
                return wellDouble;
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
     public WellSetDouble getWells(WellList list) {
    	 
    	 WellSetDouble set = new WellSetDouble();
    	 
    	 for(WellIndex index : list) {
    		 set.add(new WellDouble(index.row(), index.column()));
    	 }
    	 
    	 return getWells(set);
     }
     
    /**
     * Returns all wells with the matching row index.
     * @param    int    the row index
     * @return          wells with matching row index
     */
    public WellSetDouble getRow(int row) {

        WellSetDouble set = new WellSetDouble();
        
        for(WellDouble well : this.wells) {
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
    public WellSetDouble getColumn(int column) {
        
        WellSetDouble set = new WellSetDouble();
        
        for(WellDouble well : this.wells) {
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
	public WellDouble[] toWellArray() {
        return this.wells.toArray(new WellDouble[this.wells.size()]);
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
    	
    	for(WellDouble well : this.wells) {
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
        for(WellDouble well : this.wells) {
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

    	Iterator<WellDouble> iter = this.wells.iterator();
    	
    	String str = "WellSetDouble " + iter.next().toString();
    	
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

        Iterator<WellDouble> iter = this.wells.iterator();
        
        while(iter.hasNext()) {
            WellDouble well = iter.next();
            
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
        
        if (object instanceof WellSetDouble == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }
        
		WellSetDouble set = (WellSetDouble) object;
        
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
	public int compareTo(WellSetDouble set) {
		
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
        
        Iterator<WellDouble> iter1 = this.iterator();
        Iterator<WellDouble> iter2 = set.iterator();
        
        while(iter1.hasNext() && iter2.hasNext()) {
        	
        	WellDouble well1 = iter1.next();
        	WellDouble well2 = iter2.next();
        	
        	int comparison = well1.compareTo(well2);
        	
        	if(comparison != 0) {
        		return comparison;
        	}
        	
        }
        
        return 0;
	}
    
}
