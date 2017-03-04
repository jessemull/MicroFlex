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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded plate object.
 * 
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="plate")
@XmlType (propOrder={"type", "label", "descriptor", "rows", "columns", "size", "wellsets", "wells"})
public class PlateXMLBigInteger {
    
	/*---------------------------- Private fields ----------------------------*/

    private String type;                                  // Data set numerical type
    private String label;                                 // Plate label
    private String descriptor;                            // Plate descriptor
    private int rows;                                     // Number of plate rows
    private int columns;                                  // Number of plate columns 
    private int size;                                     // Number of plate wells   
    private List<SimpleWellSetXMLBigInteger> wellsets = 
    		new ArrayList<SimpleWellSetXMLBigInteger>();    // Plate well sets
    private List<SimpleWellXMLBigInteger> wells = 
    		new ArrayList<SimpleWellXMLBigInteger>();       // Plate wells
    
    /*----------------------------- Constructors -----------------------------*/
    
    public PlateXMLBigInteger(){}
    
    /**
     * Creates an XML plate from a plate object.
     * @param    PlateBigInteger plate    the plate
     */
    public PlateXMLBigInteger(PlateBigInteger plate) {
        
        this.type = "BigInteger";
        this.label = plate.label();
        this.descriptor = plate.descriptor();
        this.rows = plate.rows();
        this.columns = plate.columns();
        this.size = plate.size();
        
        for(WellSetBigInteger set : plate.allGroups()) {
            wellsets.add(new SimpleWellSetXMLBigInteger(set));
        }
        
        for(WellBigInteger well : plate) {
            wells.add(new SimpleWellXMLBigInteger(well));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the data type.
     * @param    String type    the data set numerical type
     */
    @XmlElement
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the plate label.
     * @param    String label    the plate label
     */
    @XmlElement
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the row number.
     * @param   int rows       number of rows
     */
    @XmlElement
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * Sets the column number.
     * @param   int columns    number of columns
     */
    @XmlElement
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the size.
     * @param   int size    number of well in the plate
     */
    @XmlElement
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the plate descriptor.
     * @param   String descriptor    the plate descriptor
     */
    @XmlElement
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    
    /**
     * Sets the list of well sets.
     * @param    List<SimpleWellSetXMLBigInteger> sets    list of plate well sets
     */
    @XmlElement(name="wellset")
    @XmlElementWrapper(name="wellsets")
    public void setWellsets(List<SimpleWellSetXMLBigInteger> wellsets) {
    	this.wellsets = wellsets;
    }
    
    /**
     * Sets the list of wells.
     * @param    List<SimpleWellXMLBigInteger> wells    list of plate wells
     */
    @XmlElement(name="well")
    @XmlElementWrapper(name="wells")
    public void setWells(List<SimpleWellXMLBigInteger> wells) {
    	this.wells = wells;
    }
    
    /**
     * Returns the data type.
     * @return    String    the data set numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the stack label.
     * @return    String    the stack label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the row number.
     * @return   int    number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Returns the column number.
     * @return   int    number of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Returns the size.
     * @return   int    number of plates in the stack
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the descriptor.
     * @return   String    plate descriptor
     */
    public String getDescriptor() {
        return this.descriptor;
    }
    
    /**
     * Returns the list of well sets.
     * @return   List<SimpleWellSetXMLBigInteger>    returns the plate well sets
     */
    public List<SimpleWellSetXMLBigInteger> getWellsets() {
        return this.wellsets;
    }
    
    /**
     * Returns the list of wells.
     * @return   List<SimpleWellXMLBigInteger>    list of plate wells
     */
    public List<SimpleWellXMLBigInteger> getWells() {
        return this.wells;
    }
    
    /**
     * Returns a PlateBigInteger object.
     * @return    PlateBigInteger    the plate
     */
    public PlateBigInteger toPlateObject() {

        PlateBigInteger plate = new PlateBigInteger(this.rows, this.columns, this.label);
        
        for(SimpleWellSetXMLBigInteger set : this.wellsets) {
        	plate.addGroups(set.toWellSetObject().wellList());
        }
        
        for(SimpleWellXMLBigInteger well : wells) {
        	plate.addWells(well.toWellObject());
        }
        
        return plate;
    }
}
