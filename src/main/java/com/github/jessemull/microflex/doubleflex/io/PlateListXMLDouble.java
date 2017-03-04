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

package com.github.jessemull.microflex.doubleflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded plate objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="plates")
public class PlateListXMLDouble implements Iterable<PlateXMLDouble> {

	/*---------------------------- Private fields ----------------------------*/
    
    private List<PlateXMLDouble> plates = new ArrayList<PlateXMLDouble>();    // The plates in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public PlateListXMLDouble(){}
    
    /**
     * Creates an xml plate list from a plate object.
     * @param    PlateDouble    the plate object
     */
    public PlateListXMLDouble(PlateDouble plate) {
        this.plates.add(new PlateXMLDouble(plate));
    }
    
    /**
     * Creates an xml plate list from a collection of plate objects.
     * @param    Collection<PlateDouble>    collection of plate objects
     */
    public PlateListXMLDouble(Collection<PlateDouble> collection) {
        for(PlateDouble plate : collection) {
            this.plates.add(new PlateXMLDouble(plate));
        }
    }
    
    /**
     * Creates an xml plate list from an array of plate objects.
     * @param    PlateDouble[]    array of plate objects
     */
    public PlateListXMLDouble(PlateDouble[] array) {
        for(PlateDouble plate : array) {
            this.plates.add(new PlateXMLDouble(plate));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of xml plates.
     * @param    List<PlateXMLDouble>    the new list of plate
     */
    @XmlElement(name="plate")
    public void setPlates(List<PlateXMLDouble> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the xml plate list.
     * @return    the list of plates
     */
    public List<PlateXMLDouble> getPlates() {
        return this.plates;
    }

    /**
     * Returns the plate at the indicated index.
     * @param    int    the index into the plate list
     * @return          the xml plate
     */
    public PlateXMLDouble get(int index) {
        return this.plates.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of plates.
     * @return    the iterator
     */
    public Iterator<PlateXMLDouble> iterator() {
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
