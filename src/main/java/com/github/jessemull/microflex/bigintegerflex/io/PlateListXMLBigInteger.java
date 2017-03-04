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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded plate objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="plates")
public class PlateListXMLBigInteger implements Iterable<PlateXMLBigInteger> {

	/*---------------------------- Private fields ----------------------------*/
    
    private List<PlateXMLBigInteger> plates = new ArrayList<PlateXMLBigInteger>();    // The plates in the list
    
    /*----------------------------- Constructors -----------------------------*/ 
    
    public PlateListXMLBigInteger(){}
    
    /**
     * Creates an xml plate list from a plate object.
     * @param    PlateBigInteger    the plate object
     */
    public PlateListXMLBigInteger(PlateBigInteger plate) {
        this.plates.add(new PlateXMLBigInteger(plate));
    }
    
    /**
     * Creates an xml plate list from a collection of plate objects.
     * @param    Collection<PlateBigInteger> collection    collection of plate objects
     */
    public PlateListXMLBigInteger(Collection<PlateBigInteger> collection) {
        for(PlateBigInteger plate : collection) {
            this.plates.add(new PlateXMLBigInteger(plate));
        }
    }
    
    /**
     * Creates an xml plate list from an array of plate objects.
     * @param    PlateBigInteger[] array    array of plate objects
     */
    public PlateListXMLBigInteger(PlateBigInteger[] array) {
        for(PlateBigInteger plate : array) {
            this.plates.add(new PlateXMLBigInteger(plate));
        }
    }
    
    /*----------------------------- Constructors -----------------------------*/
    
    /**
     * Sets the list of xml plates.
     * @param    List<PlateXMLBigInteger> plates    the new list of plate
     */
    @XmlElement(name="plate")
    public void setPlates(List<PlateXMLBigInteger> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the xml plate list.
     * @return    List<PlateXMLBigInteger>    the list of plates
     */
    public List<PlateXMLBigInteger> getPlates() {
        return this.plates;
    }

    /**
     * Returns the plate at the indicated index.
     * @param    int index    the index into the plate list
     * @return   PlateXMLBigInteger    the xml plate
     */
    public PlateXMLBigInteger get(int index) {
        return this.plates.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of plates.
     * @return    Iterator<PlateXMLBigInteger>    the iterator
     */
    public Iterator<PlateXMLBigInteger> iterator() {
        return this.plates.iterator();
    }
    
    /**
     * Returns the size of the plate list.
     * @return    int size    the size of the list
     */
    public int size() {
        return this.plates.size();
    }
}
