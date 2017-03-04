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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

/**
 * This is a wrapper class used to marshal/unmarshal an XML encoded plate object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="plate")
@XmlType (propOrder={"type", "label", "descriptor", "rows", "columns", "size", "wellsets", "wells"})
public class PlateXMLBigDecimal {
    
	/*---------------------------- Private Fields ----------------------------*/

    private String type;          // Data set numerical type
    private String label;         // Plate label
    private String descriptor;    // Plate descriptor
    private int rows;             // Number of plate rows
    private int columns;          // Number of plate columns 
    private int size;             // Number of plate wells   
    
    /* Plate well sets */
    
    List<SimpleWellSetXMLBigDecimal> wellsets = 
    		new ArrayList<SimpleWellSetXMLBigDecimal>();
    
    /* Plate wells */
    
    List<SimpleWellXMLBigDecimal> wells = 
    		new ArrayList<SimpleWellXMLBigDecimal>();
    
    /*------------------------------ Constructors ----------------------------*/
    
    public PlateXMLBigDecimal(){}
    
    /**
     * Creates an XML plate from a plate object.
     * @param    PlateBigDecimal    the plate
     */
    public PlateXMLBigDecimal(PlateBigDecimal plate) {
        
        this.type = "BigDecimal";
        this.label = plate.label();
        this.descriptor = plate.descriptor();
        this.rows = plate.rows();
        this.columns = plate.columns();
        this.size = plate.size();
        
        for(WellSetBigDecimal set : plate.allGroups()) {
            wellsets.add(new SimpleWellSetXMLBigDecimal(set));
        }
        
        for(WellBigDecimal well : plate) {
            wells.add(new SimpleWellXMLBigDecimal(well));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the data type.
     * @param    String    the data set numerical type
     */
    @XmlElement
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the plate label.
     * @param    String    the plate label
     */
    @XmlElement
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the row number.
     * @param   int    number of rows
     */
    @XmlElement
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * Sets the column number.
     * @param   int    number of columns
     */
    @XmlElement
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the size.
     * @param   int    number of well in the plate
     */
    @XmlElement
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the plate descriptor.
     * @param   String    the plate descriptor
     */
    @XmlElement
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    
    /**
     * Sets the list of well sets.
     * @param    List<SimpleWellSetXMLBigDecimal>    list of plate well sets
     */
    @XmlElement(name="wellset")
    @XmlElementWrapper(name="wellsets")
    public void setWellsets(List<SimpleWellSetXMLBigDecimal> wellsets) {
    	this.wellsets = wellsets;
    }
    
    /**
     * Sets the list of wells.
     * @param    List<SimpleWellXMLBigDecimal>    list of plate wells
     */
    @XmlElement(name="well")
    @XmlElementWrapper(name="wells")
    public void setWells(List<SimpleWellXMLBigDecimal> wells) {
    	this.wells = wells;
    }
    
    /**
     * Returns the data type.
     * @return    the data set numerical type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Returns the stack label.
     * @return    the stack label
     */
    public String getLabel() {
        return this.label;
    }
    
    /**
     * Returns the row number.
     * @return    number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Returns the column number.
     * @return    number of columns
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * Returns the size.
     * @return    number of plates in the stack
     */
    public int getSize() {
        return this.size;
    }
    
    /**
     * Returns the descriptor.
     * @return    plate descriptor
     */
    public String getDescriptor() {
        return this.descriptor;
    }
    
    /**
     * Returns the list of well sets.
     * @return    returns the plate well sets
     */
    public List<SimpleWellSetXMLBigDecimal> getWellsets() {
        return this.wellsets;
    }
    
    /**
     * Returns the list of wells.
     * @return    list of plate wells
     */
    public List<SimpleWellXMLBigDecimal> getWells() {
        return this.wells;
    }
    
    /**
     * Returns a PlateBigDecimal object.
     * @return    the plate
     */
    public PlateBigDecimal toPlateObject() {

        PlateBigDecimal plate = new PlateBigDecimal(this.rows, this.columns, this.label);
        
        for(SimpleWellSetXMLBigDecimal set : this.wellsets) {
        	plate.addGroups(set.toWellSetObject().wellList());
        }
        
        for(SimpleWellXMLBigDecimal well : wells) {
        	plate.addWells(well.toWellObject());
        }
        
        return plate;
    }
}
