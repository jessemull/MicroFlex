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
 * Unless required by applicable law or agreed to in writing,S
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/*--------------------------- Package Declaration ----------------------------*/

package com.github.jessemull.microflex.doubleflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflex.doubleflex.plate.WellDouble;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded well objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name = "wells")
public class WellListXMLDouble implements Iterable<WellXMLDouble> {
    
	/*---------------------------- Private fields ----------------------------*/
    
    private List<WellXMLDouble> wells = new ArrayList<WellXMLDouble>();    // The wells in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public WellListXMLDouble(){}
    
    /**
     * Creates an XML well list from a well object.
     * @param    WellDouble    the well object
     */
    public WellListXMLDouble(WellDouble well) {
        this.wells.add(new WellXMLDouble(well));
    }
    
    /**
     * Creates an XML well list from a collection of well objects.
     * @param    Collection<WellDouble>    collection of well objects
     */
    public WellListXMLDouble(Collection<WellDouble> collection) {
        for(WellDouble well : collection) {
            this.wells.add(new WellXMLDouble(well));
        }
    }
    
    /**
     * Creates an XML well list from an array of well objects.
     * @param    WellDouble[]    array of well objects
     */
    public WellListXMLDouble(WellDouble[] array) {
        for(WellDouble well : array) {
            this.wells.add(new WellXMLDouble(well));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of XML wells.
     * @param    List<WellXMLDouble>    the new list of wells
     */
    @XmlElement(name = "well")
    public void setWells(List<WellXMLDouble> wells) {
        this.wells = wells;
    }
    
    /**
     * Gets the list of XML wells.
     * @return    the list of wells
     */
    public List<WellXMLDouble> getWells() {
        return this.wells;
    }

    /**
     * Returns the XML well at the indicated index.
     * @param    int    the index into the well list
     * @return          the XML well
     */
    public WellXMLDouble get(int index) {
        return this.wells.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of XML wells.
     * @return    the iterator
     */
    public Iterator<WellXMLDouble> iterator() {
        return this.wells.iterator();
    }
    
    /**
     * Returns the size of the XML well list.
     * @return    the size of the list
     */
    public int size() {
        return this.wells.size();
    }

}
