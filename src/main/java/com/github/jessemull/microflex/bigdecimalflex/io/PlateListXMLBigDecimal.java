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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded plate objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="plates")
public class PlateListXMLBigDecimal implements Iterable<PlateXMLBigDecimal> {

	/*---------------------------- Private fields ----------------------------*/
    
    private List<PlateXMLBigDecimal> plates = new ArrayList<PlateXMLBigDecimal>();    // The plates in the list
    
    /*----------------------------- Constructors -----------------------------*/
    
    public PlateListXMLBigDecimal(){}
    
    /**
     * Creates an XML plate list from a plate object.
     * @param    PlateBigDecimal    the plate object
     */
    public PlateListXMLBigDecimal(PlateBigDecimal plate) {
        this.plates.add(new PlateXMLBigDecimal(plate));
    }
    
    /**
     * Creates an XML plate list from a collection of plate objects.
     * @param    Collection<PlateBigDecimal>    collection of plate objects
     */
    public PlateListXMLBigDecimal(Collection<PlateBigDecimal> collection) {
        for(PlateBigDecimal plate : collection) {
            this.plates.add(new PlateXMLBigDecimal(plate));
        }
    }
    
    /**
     * Creates an XML plate list from an array of plate objects.
     * @param    PlateBigDecimal[]    array of plate objects
     */
    public PlateListXMLBigDecimal(PlateBigDecimal[] array) {
        for(PlateBigDecimal plate : array) {
            this.plates.add(new PlateXMLBigDecimal(plate));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of XML plates.
     * @param    List<PlateXMLBigDecimal>    the new list of plate
     */
    @XmlElement(name="plate")
    public void setPlates(List<PlateXMLBigDecimal> plates) {
        this.plates = plates;
    }
    
    /**
     * Returns the XML plate list.
     * @return    the list of plates
     */
    public List<PlateXMLBigDecimal> getPlates() {
        return this.plates;
    }

    /**
     * Returns the plate at the indicated index.
     * @param    int    the index into the plate list
     * @return          the XML plate
     */
    public PlateXMLBigDecimal get(int index) {
        return this.plates.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of plates.
     * @return    the iterator
     */
    public Iterator<PlateXMLBigDecimal> iterator() {
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
