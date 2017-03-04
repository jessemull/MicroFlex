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
import java.util.List;

import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;

/**
 * This is a wrapper class for importing or exporting a JSON encoded plate object.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlatePOJODouble {
    
	/*---------------------------- Private fields ----------------------------*/

    private String type;                                   // Data set numerical type
    private String label;                                  // Plate label
    private String descriptor;                             // Plate descriptor
    private int rows;                                      // Number of plate rows
    private int columns;                                   // Number of plate columns 
    private int size;                                      // Number of plate wells   
    private List<SimpleWellSetPOJODouble> wellsets = 
    		new ArrayList<SimpleWellSetPOJODouble>();      // Plate well sets
    private List<SimpleWellPOJODouble> wells = 
    		new ArrayList<SimpleWellPOJODouble>();         // Plate wells
    
    /*------------------------------ Constructors ----------------------------*/
    
    public PlatePOJODouble(){}
    
    /**
     * Creates a plate POJO from a plate object.
     * @param    PlateDouble    the plate
     */
    public PlatePOJODouble(PlateDouble plate) {
        
        this.type = "Double";
        this.label = plate.label();
        this.descriptor = plate.descriptor();
        this.rows = plate.rows();
        this.columns = plate.columns();
        this.size = plate.size();
        
        for(WellSetDouble set : plate.allGroups()) {
            wellsets.add(new SimpleWellSetPOJODouble(set));
        }
        
        for(WellDouble well : plate) {
            wells.add(new SimpleWellPOJODouble(well));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the data type.
     * @param    String    the data set numerical type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Sets the stack label.
     * @param    String    the stack label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * Sets the row number.
     * @param   int    number of rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * Sets the column number.
     * @param   int    number of columns
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * Sets the size.
     * @param   int    number of plates in the stack
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    /**
     * Sets the plate descriptor.
     * @param   String    the plate descriptor
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    
    /**
     * Sets the list of well sets.
     * @param    List<SimpleWellSetPOJODouble>    list of plate well sets
     */
    public void setWellsets(List<SimpleWellSetPOJODouble> wellsets) {
    	this.wellsets = wellsets;
    }
    
    /**
     * Sets the list of wells.
     * @param    List<SimpleWellPOJODouble>    list of plate wells
     */
    public void setWells(List<SimpleWellPOJODouble> wells) {
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
    public List<SimpleWellSetPOJODouble> getWellsets() {
        return this.wellsets;
    }
    
    /**
     * Returns the list of wells.
     * @return    list of plate wells
     */
    public List<SimpleWellPOJODouble> getWells() {
        return this.wells;
    }
    
    /**
     * Returns a PlateDouble object.
     * @return    the plate
     */
    public PlateDouble toPlateObject() {

        PlateDouble plate = new PlateDouble(this.rows, this.columns, this.label);
        
        for(SimpleWellSetPOJODouble set : this.wellsets) {
        	plate.addGroups(set.toWellSetObject().wellList());
        }
        
        for(SimpleWellPOJODouble well : wells) {
        	plate.addWells(well.toWellObject());
        }
        
        return plate;
    }
}
