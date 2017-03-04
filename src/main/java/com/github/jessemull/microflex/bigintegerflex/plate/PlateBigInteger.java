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

/* -------------------------------- Package --------------------------------- */

package com.github.jessemull.microflex.bigintegerflex.plate;

/* ------------------------------ Dependencies ------------------------------ */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.ValUtil;

/**
 * This class represents a microplate used by chemists, biologists, physicists
 * and many other natural science researchers. A microplate consists of a 
 * rectangular grid of wells. Rows are assigned a letter starting with A and 
 * working towards Z. Rows outside this range are identified by adding an 
 * additional letter to the row index (A -> ... -> Z -> AA -> AB). Columns 
 * are assigned integer values starting at zero working towards infinity.
 * The row and column indices uniquely identify a single well (A1, A2 etc).
 * 
 * Example of a typical microplate layout:
 * 
 * <table cellpadding="10px" border="1px" style="margin: 50px; text-align: center">
 *    <tr>
 *       <td></td>
 *       <td>1</td>
 *       <td>2</td>
 *       <td>3</td>
 *       <td>4</td>
 *       <td>5</td>
 *       <td>6</td>
 *       <td>7</td>
 *       <td>8</td>
 *       <td>9</td>
 *       <td>10</td>
 *       <td>11</td>
 *       <td>12</td>
 *    </tr>
 *    <tr>
 *       <td>A</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>    
 *    </tr>
 *    <tr>
 *       <td>B</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *    </tr>
 *    <tr>
 *       <td>C</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>      
 *    </tr>
 *    <tr>
 *       <td>D</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>      
 *    </tr>
 *    <tr>
 *       <td>E</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>      
 *    </tr>
 *    <tr>
 *       <td>F</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td      
 *    </tr>
 *    <tr>
 *       <td>G</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td      
 *    </tr>
 *    <tr>
 *       <td>H</td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td> 
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>
 *       <td><div style="border-radius: 50%; border: 1px solid black; width: 20px; height: 20px;"></div></td>     
 *    </tr>
 * </table>
 * 
 * The plate constructor accepts a flag for plates with standard dimensions:
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Plate Type<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Flag</div></th>
 *    <tr>
 *       <td>6-Well</td>
 *       <td>PLATE_6</td>
 *    </tr>
 *    <tr>
 *       <td>12-Well</td>
 *       <td>PLATE_12</td>
 *    </tr>
 *    <tr>
 *       <td>24-Well</td>
 *       <td>PLATE_24</td>
 *    </tr>
 *    <tr>
 *       <td>48-Well</td>
 *       <td>PLATE_48</td>
 *    </tr>
 *    <tr>
 *       <td>96-Well</td>
 *       <td>PLATE_96</td>
 *    </tr>
 *    <tr>
 *       <td>384-Well</td>
 *       <td>PLATE_384</td>
 *    </tr>
 *    <tr>
 *       <td>1536-Well</td>
 *       <td>PLATE_1536</td>
 *    </tr>
 * </table>
 *    
 * Row and column numbers for plates with custom dimensions can be passed to the
 * plate constructor.
 * 
 * The plate data is implemented as a set of wells and a set of subgroups. 
 * Wells house the well ID and a list of values. Well subsets are used to 
 * aggregate data for different experimental conditions or replicates.
 * 
 * Like all the members of the MicroFlex library the plate object is meant
 * to be flexible, as such the constructors and methods accept all collection 
 * types, all numeric Java primitives and two immutable number objects:
 * 
 * <table cellspacing="5px" style="text-align:left; margin-top: 20px; margin-left: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Primitives<div></th>
 *    <tr>
 *       <td>Byte</td>
 *    </tr>
 *    <tr>
 *       <td>Short</td>
 *    </tr>
 *    <tr>
 *       <td>Int</td>
 *    </tr>
 *    <tr>
 *       <td>Long</td>
 *    </tr>
 *    <tr>
 *       <td>Float</td>
 *    </tr>
 *    <tr>
 *       <td>Double</td>
 *    </tr>
 * </table>
 * 
 * <table cellspacing="5px" style="text-align:left; margin-bottom: 20px; margin-left: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Immutables<div></th>
 *    <tr>
 *       <td>BigInteger</td>
 *    </tr>
 *    <tr>
 *       <td>BigDecimal</td>
 *    </tr>
 * </table>
 *         
 * Descriptive statistics and mathematical operations between plate stacks,
 * plates, well sets and wells are performed using static methods housed within 
 * the statistics and math packages respectively. Custom mathematical or statistical
 * calculations can be added by extending the MathOperation and DescriptiveStatistics
 * abstract classes and over riding the calculate methods.
 * 
 * The plate object implements comparable and iterable interfaces. The iterator
 * iterates over the plate wells. Wells are compared using the well number,
 * row number, column number, label then data type in that order.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlateBigInteger implements Iterable<WellBigInteger>, Comparable<PlateBigInteger> {            
    
    /* ---------------------------- Public Fields --------------------------- */
    
    /* Flags for predefined plate dimension types */
    
    public static final int PLATE_6WELL = 0;
    public static final int PLATE_12WELL = 1;
    public static final int PLATE_24WELL = 2;
    public static final int PLATE_48WELL = 3;
    public static final int PLATE_96WELL = 4;
    public static final int PLATE_384WELL = 5;
    public static final int PLATE_1536WELL = 6;
    
    /* Flags for available data types */
    
    public static final int PLATE_DOUBLE = 0;
    public static final int PLATE_INTEGER = 1;
    public static final int PLATE_BIGDECIMAL = 2;
    public static final int PLATE_BIGINTEGER = 3;
    
    /* Row number for each predefined plate dimension type */
    
    public static final int ROWS_6WELL = 2;
    public static final int ROWS_12WELL = 3;
    public static final int ROWS_24WELL = 4;
    public static final int ROWS_48WELL = 6;
    public static final int ROWS_96WELL = 8;
    public static final int ROWS_384WELL = 16;
    public static final int ROWS_1536WELL = 32;
    
    public static final int COLUMNS_6WELL = 3;
    public static final int COLUMNS_12WELL = 4;
    public static final int COLUMNS_24WELL = 6;
    public static final int COLUMNS_48WELL = 8;
    public static final int COLUMNS_96WELL = 12;
    public static final int COLUMNS_384WELL = 24;
    public static final int COLUMNS_1536WELL = 48;
    
    /* -------------------------- Private Fields ---------------------------- */
    
    private int rows;                            // Row number
    private int columns;                         // Column number
    private String label = "PlateBigInteger";    // Label
    private int type;                            // Plate type as integer
    private String descriptor;                   // Plate type as string
    private Set<WellList> groups;                // Well groups for analysis
    private int dataType = PLATE_BIGINTEGER;     // Data type flag
    private WellSetBigInteger data;              // Well data
    
    /*----------------------------- Constructors -----------------------------*/
    
    /**
     * Creates a BigInteger plate.
     * @param    int    the plate type
     */
    public PlateBigInteger(int type) {
        this.groups = new TreeSet<WellList>();
        this.data = new WellSetBigInteger();
        this.initializePlateType(type);
    } 
    
    /**
     * Creates a BigInteger plate.
     * @param    int    plate rows
     * @param    int    plate columns
     */
    public PlateBigInteger(int rows, int columns) {
        this.groups = new TreeSet<WellList>();
        this.data = new WellSetBigInteger();
        this.initializePlateType(rows, columns);
    } 

    /**
     * Creates a BigInteger plate.
     * @param    int       plate type flag
     * @param    String    plate label
     */
    public PlateBigInteger(int type, String label) {
        this(type);
        this.label = label;
    }

    /**
     * Creates a BigInteger plate.
     * @param    int       plate rows
     * @param    int       plate columns
     * @param    String    plate label
     */
    public PlateBigInteger(int rows, int columns, String label) {
        this(rows, columns);
        this.label = label;
    }
    
    /**
     * Creates a BigInteger plate.
     * @param    int                  plate type flag
     * @param    WellSetBigInteger    well set
     */
    public PlateBigInteger(int type, WellSetBigInteger set) {  	
        this(type);
        ValUtil.validateSet(this.rows, this.columns, set);
        this.addWells(set);
    }

    /**
     * Creates a BigInteger plate.
     * @param    int                  number of plate rows
     * @param    int                  number of plate columns
     * @param    WellSetBigInteger    plate well sets
     */
    public PlateBigInteger(int rows, int columns, WellSetBigInteger set) {
        this(rows, columns);
        ValUtil.validateSet(this.rows, this.columns, set);
        this.addWells(set);
    }

    /**
     * Creates a BigInteger plate.
     * @param    int                  plate type flag
     * @param    String               plate label
     * @param    WellSetBigInteger    plate well set
     */
    public PlateBigInteger(int type, String label, WellSetBigInteger data) {
        this(type, data);
        this.label = label;
    }

    /**
     * Creates a BigInteger plate.
     * @param    int       plate rows
     * @param    int       plate columns
     * @param    String    plate label
     * @param    E         plate well set
     */
    public PlateBigInteger(int rows, int columns, String label, WellSetBigInteger data) {
    	this(rows, columns, data);
        this.label = label;
    }
    
    /**
     * Clones a BigInteger plate.
     * @param    PlateBigInteger    plate to clone
     */
    public PlateBigInteger(PlateBigInteger plate) {

    	this(plate.rows(), plate.columns());
    	
        this.label = plate.label();             
        this.type = plate.type();                  
        this.descriptor = plate.descriptor();                           
        this.dataType = plate.dataType();      
        
        WellSetBigInteger toAdd = plate.dataSet();
        this.addWells(toAdd);
        
        for(WellSetBigInteger set : plate.allGroups()) {      	
        	WellList setList = set.wellList();
        	this.addGroups(new WellList(setList)); 
        }

    }

    
    /* ---------------------- Constructor Helper Methods ---------------------*/

    /**
     * Sets the number of rows, the number of columns, the size, the plate type 
     * and the descriptor.
     * @param    int    plate row number
     * @param    int    plate column number
     */
    private void initializePlateType(int rows, int columns) {
        
        this.rows = rows;
        this.columns = columns;
        
        if(rows == ROWS_6WELL && 
           columns == COLUMNS_6WELL) {
            
            this.type = PLATE_6WELL;
            this.descriptor = "6-Well";

        } else if(rows == ROWS_12WELL && 
                  columns == COLUMNS_12WELL) {
            
            this.type = PLATE_12WELL;
            this.descriptor = "12-Well";
        
        } else if(rows == ROWS_24WELL && 
                  columns == COLUMNS_24WELL) {
        
            this.type = PLATE_24WELL;
            this.descriptor = "24-Well";
        
        } else if(rows == ROWS_48WELL && 
                  columns == COLUMNS_48WELL) {
            
            this.type = PLATE_48WELL;
            this.descriptor = "48-Well";
        
        } else if(rows == ROWS_96WELL && 
                  columns == COLUMNS_96WELL) {
            
            this.type = PLATE_96WELL;
            this.descriptor = "96-Well";
        
        } else if(rows == ROWS_384WELL && 
                  columns == COLUMNS_384WELL) {
            
            this.type = PLATE_384WELL;
            this.descriptor = "384-Well";
        
        } else if(rows == ROWS_1536WELL && 
                  columns == COLUMNS_1536WELL) {
            
            this.type = PLATE_1536WELL;
            this.descriptor = "1536-Well";
        
        } else {
            
            this.type = -1;
            this.descriptor = "Custom Plate: " + rows + "x" + columns;
            
        }
    }
    
    /**
     * Sets the number of rows, the number of columns, the size, the plate type 
     * and the descriptor.
     * @param    int    the plate type
     */
    private void initializePlateType(int type) {
        
        switch(type) {
            case PLATE_6WELL:    this.initializePlateType(ROWS_6WELL, COLUMNS_6WELL);
                                 break;
            case PLATE_12WELL:   this.initializePlateType(ROWS_12WELL, COLUMNS_12WELL);
                                 break;
            case PLATE_24WELL:   this.initializePlateType(ROWS_24WELL, COLUMNS_24WELL);
                                 break;
            case PLATE_48WELL:   this.initializePlateType(ROWS_48WELL, COLUMNS_48WELL);
                                 break;
            case PLATE_96WELL:   this.initializePlateType(ROWS_96WELL, COLUMNS_96WELL);
                                 break;
            case PLATE_384WELL:  this.initializePlateType(ROWS_384WELL, COLUMNS_384WELL);
                                 break;
            case PLATE_1536WELL: this.initializePlateType(ROWS_1536WELL, COLUMNS_1536WELL);
                                 break;
            default:             throw new IllegalArgumentException("Invalid plate type: " + type + ".");
        }
        
    }
    
    /* ------------------------ Group Addition Methods ---------------------- */
    
    /**
     * Adds a well group for analysis.
     * @param    WellList    the list of wells
     * @return               true on successful group addition
     */
    public void addGroups(WellList list) {
    
    	Preconditions.checkNotNull(list, "The group list cannot be null.");
    	
    	ValUtil.validateGroup(this.rows(), this.columns(), list);
    	
    	if(this.groups.contains(list)) {
    		throw new IllegalArgumentException("The group " + list.toString() + " already exists in the group list.");
    	} else {
    		this.groups.add(list);
    	}
    }
    
    /**
     * Adds a collection of well groups for analysis.
     * @param    Collection<WellList>    collection of groups
     * @return                           true on successful addition
     */
    public void addGroups(Collection<WellList> collection) {
    	for(WellList list : collection) {
    		this.addGroups(list);
    	}
    }
    
    /**
     * Adds an array of well groups for analysis.
     * @param    WellList[]    array of groups
     * @return                 true on successful addition
     */
    public void addGroups(WellList[] array) {
    	for(WellList list : array) {
    		this.addGroups(list);
    	}
    }
    
    /* ------------------------- Remove Group Methods ----------------------- */
    
    /**
     * Removes a well group.
     * @param    WellList    the group
     */
    public void removeGroups(WellList list) {
        
    	Preconditions.checkNotNull(list, "The group list cannot be null.");
    	
    	ValUtil.validateGroup(this.rows(), this.columns(), list);
    	
    	if(!this.groups.contains(list)) {
    		throw new IllegalArgumentException("The group " + list.toString() + " does not exist.");
    	} else {
    		this.groups.remove(list);
    	}
    }
    
    /**
     * Removes a collection of well groups.
     * @param    Collection<WellList>    the collection of well groups
     */
    public void removeGroups(Collection<WellList> collection) {
    	for(WellList list : collection) {
    		this.removeGroups(list);
    	}
    }
    
    /**
     * Removes an array of well groups.
     * @param    WellList[]    the array of well groups
     */
    public void removeGroups(WellList[] array) {
    	for(WellList list : array) {
    		this.removeGroups(list);
    	}
    }
    
    /**
     * Removes an the group with the label.
     * @param    String    the group label
     */
    public void removeGroups(String label) {
    	
    	WellList toRemove = null;
    	
    	for(WellList list : this.groups) {
    		if(list.label().equals(label)) {
        		toRemove = list;
    		}
    	}
    	
    	if(toRemove != null) {
    		this.groups.remove(toRemove);
    	}
    }
    
    /**
     * Removes all wells with labels contained in the input list.
     * @param    List<String>    the group labels
     */
    public void removeGroups(List<String> labels) {
    	for(String label : labels) {
    		this.removeGroups(label);
    	}
    }
    
    /**
     * Clears all groups.
     */
    public void clearGroups() {
        this.groups.clear();
    }
    
    /* ----------------------- Group Retrieval Methods ---------------------- */
    
    /**
     * Returns a well set for all analysis groups.
     * @return    well sets for each group
     */
    public Set<WellSetBigInteger> allGroups() {

    	Set<WellSetBigInteger> groups = new TreeSet<WellSetBigInteger>();

    	for(WellList list : this.groups) {    		

    		WellSetBigInteger set = new WellSetBigInteger();
    		
    		for(WellIndex index : list) {

    			WellBigInteger well = this.getWells(new WellBigInteger(index.row(), index.column()));
    			
    			if(well == null) {
    				well = new WellBigInteger(index.row(), index.column());
    			}
    			
    			set.add(well);
    		}	
    		
    		set.setLabel(list.label());
    		groups.add(set);
    	}

    	return groups;
    }
    
    /**
     * Returns a well set containing the wells for the group with the given label.
     * @param    String    label
     * @return             well set containing the group wells
     */
    public WellSetBigInteger getGroups(String label) {
    	
    	WellSetBigInteger set = new WellSetBigInteger();
    	
    	for(WellList list : groups) {
    		
    		if(list.label().equals(label)) {

    			for(WellIndex index : list) {
    				
    				WellBigInteger well = this.getWells(new WellBigInteger(index.row(), index.column()));
    				
    				if(well == null) {
    					well = new WellBigInteger(index.row(), index.column());
    				}
    			
    				set.add(well);
    			}
    			
    			set.setLabel(list.label());
    			return set;
    		}
    	}
    	
    	return null;
    }      
    
    /**
     * Returns a well set containing the wells for the group.
     * @param    WellList    the group
     * @return               well set containing the group wells
     */
    public WellSetBigInteger getGroups(WellList list) {
    	
    	WellSetBigInteger set = new WellSetBigInteger();
    	
    	for(WellList group : this.groups) {
    		
    		if(group.equals(list)) {
    			
    			for(WellIndex index : group) {
    				
    				WellBigInteger well = this.getWells(new WellBigInteger(index.row(), index.column()));
    				
    				if(well == null) {
    					well = new WellBigInteger(index.row(), index.column());
    				}
    				
    				set.add(well);
    			}
    			
    			set.setLabel(group.label());
    			return set;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Returns a well set containing the wells for each group.
     * @param    Collection<WellList>    the collection of groups
     * @return                           wells for each group
     */
    public Set<WellSetBigInteger> getGroups(Collection<WellList> collection) {
    	
    	Set<WellSetBigInteger> set = new TreeSet<WellSetBigInteger>();
    	
    	for(WellList list : collection) {
    		
    		WellSetBigInteger group = this.getGroups(list);
    		
    		if(group != null) {
    			set.add(group);
    		}
    		
    	}
    	
    	return set;
    	
    }
    
    /**
     * Returns a well set containing the wells for each group.
     * @param    WellList[]                the array of groups
     * @return                             wells for each group
     */
    public Set<WellSetBigInteger> getGroups(WellList[] array) {
    	
        Set<WellSetBigInteger> set = new TreeSet<WellSetBigInteger>();
    	
    	for(WellList list : array) {
    		
    		WellSetBigInteger group = this.getGroups(list);
    		
    		if(group != null) {
    			set.add(group);
    		}
    		
    	}
    	
    	return set;
    }
    
    /**
     * Returns all wells with a label contained in the input list.
     * @param    List<String>    the group labels
     * @return                   set of group data 
     */
    public Set<WellSetBigInteger> getGroups(List<String> labels) {

    	Set<WellSetBigInteger> set = new TreeSet<WellSetBigInteger>();
    	
    	for(WellList list : this.groups) {
    		
    		if(labels.contains(list.label())) {
    			WellSetBigInteger group = this.getGroups(list);
    			if(group != null) {
        			set.add(group);
        		}
    		}	
    	}
    	
    	return set;
    }
    
    /* ------------------------ Set Lookup Method Tests --------------------- */
    
    /**
     * Returns true if the group exists.
     * @param    String    label
     * @return             true on successful lookup
     */
    public boolean containsGroup(String label) {
    	
    	for(WellList list : this.groups) {
    		
    		if(list.label().equals(label)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * Returns true if groups with all the labels exist.
     * @param    List<String>    label
     * @return                   true on successful lookup
     */
    public boolean containsGroup(List<String> labels) {
    	
    	for(WellList list : this.groups) {
    		
    		if(!labels.contains(list.label())) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Returns true if the group exists.
     * @param    WellList    the group
     * @return               true on successful lookup
     */
    public boolean containsGroup(WellList list) {
        return this.groups.contains(list);
    }
    
    /**
     * Returns true if the groups in the collection exist.
     * @param    Collection<WellList>    collection of groups
     * @return                           true on successful lookup 
     */
    public boolean containsGroup(Collection<WellList> collection) {
	    
    	for(WellList list : collection) {
    		
    		if(this.containsGroup(list) == false) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /**
     * Returns true if the groups in the array exist.
     * @param    WellList[]    array of groups
     * @return                 true on successful lookup 
     */
    public boolean containsGroup(WellList[] array) {
    	
        for(WellList list : array) {
    		
    		if(this.containsGroup(list) == false) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    /*---------------------------- TreeSet methods ---------------------------*/
    
    /**
     * Returns the least well in this set strictly greater than the given 
     * well, or null if there is no such well.
     * @param    WellBigInteger    input well
     * @return                     greatest well less than or equal to the input well
     */
    public WellBigInteger higher(WellBigInteger well) {
        return this.data.higher(well);
    }
    
    /**
     * Returns the greatest well in this set strictly less than the given well, 
     * or null if there is no such well.
     * @param     WellBigInteger    input well
     * @return                      least well greater than or equal to the input well
     */
    public WellBigInteger lower(WellBigInteger well) {
        return this.data.lower(well);
    }
    
    /**
     * Retrieves and removes the first (lowest) well, or returns null if this 
     * set is empty.
     * @return    first/lowest well
     */
    public WellBigInteger pollFirst() {
        return this.data.pollFirst();
    }
    
    /**
     * Retrieves and removes the last (highest) well, or returns null if this 
     * set is empty.
     * @return    last/highest well
     */
    public WellBigInteger pollLast() {
        return this.data.pollLast();
    }  
    
    /**
     * Returns an iterator that iterates over the well in the plate.
     * @return    iterator for plate wells
     */
    public Iterator<WellBigInteger> iterator() {
        return this.data.iterator();
    }
    
    /**
     * Returns an iterator over the wells in this set in descending order.
     * @return    iterator for traversing set in descending order
     */
    public Iterator<WellBigInteger> descendingIterator() {
        return this.data.descendingIterator();
    }
    
    /**
     * Returns a reverse order view of the wells contained in this set.
     * @return    the set in descending order
     */
    public Set<WellBigInteger> descendingSet() {
        return this.data.descendingSet();
    }
    
    /**
     * Returns the first well in the set.
     * @return    the first well in the set
     */
    public WellBigInteger first() {
        return this.data.first();
    }
    
    /**
     * Returns the last well in the set.
     * @return    the first well in the set
     */
    public WellBigInteger last() {
        return this.data.last();
    }
    
    /**
     * Returns the least well in this set greater than or equal to the given 
     * well, or null if there is no such well.
     * @param    WellBigInteger    the well
     * @return                     least well greater than or equal to the input well
     */
    public WellBigInteger ceiling(WellBigInteger well) {
        return this.data.ceiling(well);
    }
    
    /**
     * Returns the greatest well in this set less than or equal to the given 
     * well, or null if there is no such well.
     * @return    greatest well less than or equal to the input well
     */
    public WellBigInteger floor(WellBigInteger well) {
        return this.data.floor(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are strictly 
     * less than the input well.
     * @param    WellBigInteger    input well
     * @return                     well set with wells less than the input well
     */
    public Set<WellBigInteger> headSet(WellBigInteger well) {
        return this.data.headSet(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are less than 
     * (or equal to, if inclusive is true) to the input well.
     * @param    WellBigInteger well    input   well 
     * @param    boolean    a true value retains the input well in the sub set
     * @return              well set with wells less than the input well
     */
    public Set<WellBigInteger> headSet(WellBigInteger well, boolean inclusive) {
        return this.data.headSet(well, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * or equal to the input well.
     * @param     WellBigInteger    the input well
     * @return                      set with wells greater than or equal to the input well
     */
    public Set<WellBigInteger> tailSet(WellBigInteger well) {
        return this.data.tailSet(well);
    }
    
    /**
     * Returns a view of the portion of this set whose wells are greater than 
     * (or equal to, if inclusive is true) the input well.
     * @param     WellBigInteger    the input well
     * @return                      set with wells greater than or equal to the input well
     */
    public Set<WellBigInteger> tailSet(WellBigInteger well, boolean inclusive) {
        return this.data.tailSet(well, inclusive);
    }
    
    /**
     * Returns a view of the portion of this set whose wells range from 
     * the first input well to the second input well.
     * @param    WellBigInteger    the first input well
     * @param    WellBigInteger    the second input well
     * @param    boolean           includes the first input well in the returned set
     * @param    boolean           includes the second input well in the returned set
     * @return                     last well in the set
     */
    public Set<WellBigInteger> subSet(WellBigInteger well1, boolean inclusive1, WellBigInteger well2, boolean inclusive2) {
        return this.data.subSet(well1, inclusive1, well2, inclusive2);
    }
    
    /**
     * Returns a view of the portion of this set whose wells range from 
     * the first input well inclusive to the second input well inclusive.
     * @param    WellBigInteger    the first input well
     * @param    WellBigInteger    the second input well
     * @return                     last well in the set
     */
    public Set<WellBigInteger> subSet(WellBigInteger well1, WellBigInteger well2) {
        return this.data.subSet(well1, well2);
    }

    /**
     * Returns true if the plate is empty.
     * @return    true if plate has no wells
     */
    public boolean isEmpty() {
        return this.data.isEmpty();
    }
    
    /* ---------------------- Plate Descriptor Methods ---------------------- */
    
    /**
     * Returns the plate label or the hash code if the label is undefined.
     * @return    label or hash code if the label is undefined
     */
    public String label() {
        return this.label != null ? this.label : "" + this.hashCode();
    }
    
    /**
     * Sets the label.
     * @param    String    the new label
     */
    public void setLabel(String newLabel) {
    	this.label = newLabel;
    }
    
    /**
     * Returns the number of columns.
     * @return    number of columns
     */
    public int columns() {
        return this.columns;
    }
    
    /**
     * Returns the number of rows.
     * @return    number of rows
     */
    public int rows() {
        return this.rows;
    }
    
    /**
     * Returns the number of wells.
     * @return    number of wells
     */
    public int size() {
        return this.dataSet().size();
    }
    
    /**
     * Returns the plate type.
     * @return    the plate type
     */
    public int type() {
        return this.type;
    }
    
    /**
     * Returns the descriptor.
     * @return    descriptor
     */
    public String descriptor() {
        return this.descriptor;
    }
    
    /**
     * Returns the data type as an integer value.
     * @return    the data type
     */
    public int dataType() {
        return dataType;
    }
    
    /**
     * Returns the data type as a string.
     * @return    the data type
     */
    public String dataTypeString() {
        return "BigInteger";
    }
    
    /**
     * Returns the plate descriptor and label.
     * @return    plate descriptor and label
     */
    public String toString() {
        return "Type: " + this.descriptor + " Label: " + this.label;
    }
    
    /*---------------------- Methods for Data Set Output ---------------------*/
    
    /**
     * Returns the data set.
     * @return    the data set
     */
    public WellSetBigInteger dataSet() {
        return new WellSetBigInteger(this.data);
    }
    
    /**
     * Returns the plate wells in an array. Overflow results in an arithmetic 
     * exception.
     * @return    the data set as an array of wells
     */
    public WellBigInteger[] toArray() {
        return (WellBigInteger[]) this.data.toWellArray();
    }

    /*------------------------ Methods for Adding Wells ----------------------*/
    
    /**
     * Adds a well to the data set.
     * @param    WellBigInteger    the well
     * @return                     true on successful addition of the well
     */
    public boolean addWells(WellBigInteger well) {
        
        try {
            
            ValUtil.validateWell(this.rows, this.columns, well);
            
            boolean add = this.data.add(new WellBigInteger(well.row(), well.column(), well.data()));
            
            if(!add) {
                throw new IllegalArgumentException("Failed to add well " + 
                        well.toString() + ". This well already exists in the data set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Adds an array of wells to the data set omitting wells that are already
     * members of the set.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful addition of all the wells 
     *                                in the set
     */
    public boolean addWells(WellSetBigInteger set) {

        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : set) {
            try {
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.addWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Adds a collection of wells to the data set omitting wells that are already
     * members of the set.
     * @param    Collection<WellBigInteger>    the well collection
     * @return                                 true on successful addition of
     *                                         all the wells in the collection
     */
    public boolean addWells(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : collection) {
            try {
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.addWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
        
    }
    
    /**
     * Adds an array of wells to the data set omitting wells that are already
     * members of the set.
     * @param    WellBigInteger[]    the well array
     * @return                       true on successful addition of all the wells 
     *                               in the array
     */
    public boolean addWells(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : array) {
            try {
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.addWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }

        return success;
        
    }    

    /* -------------------- Methods for removing wells ---------------------- */
    
    /**
     * Removes the well from the data set.
     * @param    WellBigInteger    the well
     * @return                     true on successful well removal
     */
    public boolean removeWells(WellBigInteger well) {
        
        try {
            
            ValUtil.validateWell(this.rows, this.columns, well);
            
            boolean remove = this.data.remove(well);
            if(!remove) {
            	throw new IllegalArgumentException("Failed to remove well " + 
                        well.toString() + ". This well does not exist in the data set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Removes a set of wells.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful removal of all wells
     */
    public boolean removeWells(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : set) {
            try {
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.removeWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes a collection of wells.
     * @param    Collection<WellBigInteger>    collection of wells to
     *                                         remove
     * @return    true on successful removal 
     *            of all wells
     */
    public boolean removeWells(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : collection) {
            try {
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.removeWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes an array of wells.
     * @param    WellBigInteger[]    array of wells to remove
     * @return                       true on successful removal of all wells
     */
    public boolean removeWells(WellBigInteger[] wells) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(wells, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : wells) {
            try {
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.removeWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes the wells in the delimiter separated list.
     * @param    String    delimiter separated list of wells
     * @param    String    delimiter for the list of wells
     * @return             true on successful removal
     */
    public boolean removeWells(String wellList, String delimiter) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(wellList, "The well list cannot be null.");
            Preconditions.checkNotNull(wellList, "The delimiter cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        String[] split = wellList.split(delimiter);
        
        for(String well : split) {
            try {
            	WellBigInteger toRemove = new WellBigInteger(well);
                ValUtil.validateWell(this.rows,  this.columns, toRemove);
                success = this.removeWells(toRemove) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes the well from the data set.
     * @param    String    the well
     * @return             true on successful well removal
     */
    public boolean removeWells(String well) {
        
        try {
            
        	WellBigInteger toRemove = new WellBigInteger(well);
        	
            ValUtil.validateWell(this.rows, this.columns, toRemove);
            
            boolean remove = this.data.remove(toRemove);
            
            if(!remove) {
            	throw new IllegalArgumentException("Failed to remove well " + 
                        toRemove.toString() + ". This well does not exist in the data set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Removes the well from the data set.
     * @param    String    the index
     * @return             true on successful well removal
     */
    public boolean removeWells(WellIndex index) {
        
        try {
            
        	WellBigInteger toRemove = new WellBigInteger(index.row(), index.column());
        	
            ValUtil.validateWell(this.rows, this.columns, toRemove);
            
            boolean remove = this.data.remove(toRemove);
            
            if(!remove) {
            	throw new IllegalArgumentException("Failed to remove well " + 
                        toRemove.toString() + ". This well does not exist in the data set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Removes a well list.
     * @param    WellList    the well list
     * @return               true on successful removal of all wells
     */
    public boolean removeWells(WellList list) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(list, "The list cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellIndex index : list) {
            try {
            	WellBigInteger well = new WellBigInteger(index.row(), index.column());
                ValUtil.validateWell(this.rows,  this.columns, well);
                success = this.removeWells(well) ? success : false;
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Removes all plate wells.
     * @return    true on successful removal of all wells
     */
    public boolean clearWells() {
        
        try {
            this.data.clear();
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /*---------------------- Methods for Replacing Wells ---------------------*/
    
    /**
     * Replaces the plate well with the input well. Adds the well if the well
     * does not exist.
     * @param    WellBigInteger    the well for replacement
     * @return                     true on successful well replacement
     */
    public boolean replaceWells(WellBigInteger well) {
        
        try {
            this.data.remove(well);
            this.addWells(well);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Replaces the plate wells with the wells from the well set. Adds the 
     * well if the well does not exist.
     * @param    WellSetBigInteger    the well set
     * @return                        true on successful replacement of all wells
     */
    public boolean replaceWells(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : set) {
            try {
                this.removeWells(well);
                this.addWells(well);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }
    
    /**
     * Replaces the plate wells with the wells from the collection. Adds the 
     * well if the well does not exist.
     * @param    Collection<WellBigInteger>    the data collection
     * @return                                 true on successful replacement of all wells
     */
    public boolean replaceWells(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : collection) {
            try {
                this.removeWells(well);
                this.addWells(well);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
               
        return success;
    }

    /**
     * Replaces the plate wells with the wells from the array. Adds the well 
     * data if the well does not exist.
     * @param    WellBigInteger[]    the array of wells
     * @return                       true on successful replacement of all wells
     */
    public boolean replaceWells(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        for(WellBigInteger well : array) {
            try {
                this.removeWells(well);
                this.addWells(well);
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
     * @param    WellBigInteger    the well
     * @return                     true on successful well retention
     */
    public boolean retainWells(WellBigInteger well) {
        
        boolean success = true;
        
        List<WellBigInteger> list = new ArrayList<WellBigInteger>();
        list.add(well);
        
        try {
            ValUtil.validateWell(this.rows, this.columns, well);
            success = this.data.retain(list);
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
    public boolean retainWells(WellSetBigInteger set) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(set, "The set cannot be null.");
            for(WellBigInteger well : set) {
                ValUtil.validateWell(this.rows,  this. columns, well);
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.data.retain(set.allWells()) ? success : false;
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
    public boolean retainWells(Collection<WellBigInteger> collection) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(collection, "The collection cannot be null.");
            for(WellBigInteger well : collection) {
                ValUtil.validateWell(this.rows,  this. columns, well);
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.data.retain(collection) ? success : false;
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
    public boolean retainWells(WellBigInteger[] array) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(array, "The array cannot be null.");
            for(WellBigInteger well : array) {
                ValUtil.validateWell(this.rows,  this. columns, well);
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
            
        try { 
            success = this.data.retain(Arrays.asList(array)) ? success : false;
        } catch(Exception e) {
            System.err.println(e.getMessage());
            success = false;
        }
               
        return success;
    }

    /**
     * Retains the wells in the delimiter separated list.
     * @param    String    delimiter separated list of wells
     * @param    String    delimiter for the list of wells
     * @return             true on successful removal
     */
    public boolean retainWells(String wellList, String delimiter) {
        
        boolean success = true;
        
        try {
            Preconditions.checkNotNull(wellList, "The well list cannot be null.");
            Preconditions.checkNotNull(wellList, "The delimiter cannot be null.");
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        String[] split = wellList.split(delimiter);
        
        List<WellBigInteger> list = new ArrayList<WellBigInteger>();
        
        for(String well : split) {
            try {
            	WellBigInteger toRetain = new WellBigInteger(well);
                ValUtil.validateWell(this.rows,  this.columns, toRetain);
                list.add(toRetain);
            } catch(Exception e) {
                System.err.println(e.getMessage());
                success = false;
            }
        }
        
        success = this.data.retain(list) ? success : false;
        
        return success;
    }
    
    /**
     * Retains the well in the data set.
     * @param    String    the well
     * @return             true on successful well removal
     */
    public boolean retainWells(String well) {
        
        try {
            
        	WellBigInteger toRetain = new WellBigInteger(well);
        	List<WellBigInteger> list = new ArrayList<WellBigInteger>();
        	list.add(toRetain);
        	
            ValUtil.validateWell(this.rows, this.columns, toRetain);
            
            boolean retain = this.data.retain(list);
            
            if(!retain) {
                throw new IllegalArgumentException("Failed to remove well " + 
                        toRetain.toString() + ". This well does not exist in the data set.");
            }
            
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
        
        return true;
    }
    
    /**
     * Retains the well with the index.
     * @param    WellIndex    the well index
     * @return                true on successful well retention
     */
    public boolean retainWells(WellIndex index) {
    	return this.retainWells(new WellBigInteger(index.row(), index.column()));
    }
    
    /**
     * Retains the wells in the list.
     * @param    WellList    the well list
     * @param    boolean          true on successful well retention
     */
    public boolean retainWells(WellList list) {
    	
    	WellSetBigInteger set = new WellSetBigInteger();
    	
    	for(WellIndex index : list) {
    		set.add(new WellBigInteger(index.row(), index.column()));
    	}
    	
    	return this.retainWells(set);
    	
    }
    
    /*---------------------- Methods for Well Retrieval ----------------------*/
    
    /**
     * Returns the well or null if no such well exists.
     * @param    WellBigInteger    the well
     * @return                     the plate well or null if the well does not exist
     */
    public WellBigInteger getWells(WellBigInteger well) {
    	
    	Preconditions.checkNotNull(well, "Well cannot be null.");
    	
    	for(WellBigInteger result : this.data) {
            if(result.row() == well.row() && result.column() == well.column()) {
                return result;
            }
        }
    	
    	return null;
    }
    
    /**
     * Returns the well or null if no such well exists.
     * @param    WellSetBigInteger    the well
     * @return                        the well set
     */
    public WellSetBigInteger getWells(WellSetBigInteger set) {
    	
        Preconditions.checkNotNull(set, "Well set cannot be null.");
        WellSetBigInteger result = new WellSetBigInteger();
        
        for(WellBigInteger well : set) {
        
        	WellBigInteger toAdd = this.getWells(new WellBigInteger(well.row(), well.column()));
        	
        	if(toAdd != null) {
        		result.add(toAdd);
        	}
        }
        
        return result;
    }
    
    /**
     * Returns the well or null if no such well exists.
     * @param    WellBigInteger[]    the well
     * @return                       the well set
     */
    public WellSetBigInteger getWells(Collection<WellBigInteger> collection) {
        
    	Preconditions.checkNotNull(collection, "Well set cannot be null.");
        WellSetBigInteger result = new WellSetBigInteger();
        
        for(WellBigInteger well : collection) {
        
        	WellBigInteger toAdd = this.getWells(new WellBigInteger(well.row(), well.column()));
        	
        	if(toAdd != null) {
        		result.add(toAdd);
        	}	
        }
        
        return result;
    }
    
    /**
     * Returns the well or null if no such well exists.
     * @param    WellBigInteger[]    the well
     * @return                       the well set
     */
    public WellSetBigInteger getWells(WellBigInteger[] array) {
        
    	Preconditions.checkNotNull(array, "Well set cannot be null.");
        WellSetBigInteger result = new WellSetBigInteger();
        
        for(WellBigInteger well : array) {
        
        	WellBigInteger toAdd = this.getWells(new WellBigInteger(well.row(), well.column()));
        	
        	if(toAdd != null) {
        		result.add(toAdd);
        	}
        }
        
        return result;
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
     * @param    String    the well ID
     * @return             well with matching well ID
     */
    public WellBigInteger getWells(String well) {
        
        if(well == null) {
            return null;
        }
        
        WellBigInteger input = new WellBigInteger(well);
        
        for(WellBigInteger toReturn : this.data) {
            if(input.row() == toReturn.row() && input.column() == toReturn.column()) {
                return toReturn;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the well with the input index.
     * @param    WellIndex    the well index
     * @return                well with matching index
     */
    public WellBigInteger getWells(WellIndex index) {
        
        if(index == null) {
            return null;
        }

        for(WellBigInteger well : this.data) {
            if(index.row() == well.row() && index.column() == well.column()) {
                return well;
            }
        }
        
        return null;
    }
    
    /**
     * Returns the wells in the input list.
     * @param    WellList    the well list
     * @return               set of matching wells
     */
    public WellSetBigInteger getWells(WellList list) {
        
    	WellSetBigInteger set = new WellSetBigInteger();
    	
        if(list == null) {
            return null;
        }

        for(WellIndex index : list) {
            
        	WellBigInteger well = this.getWells(index);
        	
        	if(well != null) {
        		set.add(well);
        	}
        	
        }
        
        return set;
    }
    
    /**
     * Returns all wells with the matching row index.
     * @param    int    the row index
     * @return          wells with matching row index
     */
    public WellSetBigInteger getRow(int row) {
        
        if(row < 0 || row > this.rows) {
            return null;
        }
        
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : this.data) {
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
        
    	if(column < 0 || column > this.columns()) {
    		return null;
    	}
    	
        WellSetBigInteger set = new WellSetBigInteger();
        
        for(WellBigInteger well : this.data) {
            if(column == well.column()) {
                set.add(well);
            }
        }
        
        return set.isEmpty() ? null : set;
    }
    
    /*------------------------- Methods for Well Lookup ----------------------*/
    
    /**
     * Returns true if the well is part of the well set.
     * @param    WellBigInteger    the well
     * @return                     true if the well exists in the set
     */
    public boolean contains(WellBigInteger well) {
        return this.data.contains(well);
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
     * Returns true if all the input wells are found within the plate.
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
     * Returns true if all the wells in the delimiter separated list exist in the
     * plate.
     * @param    String    delimiter separated list of wells
     * @param    String    delimiter for the list of wells
     * @return             true if all the wells exist
     */
    public boolean contains(String wellList, String delimiter) {
        
        if(wellList == null || delimiter == null) {
            return false;
        }
        
        String[] split = wellList.split(delimiter);
        
        for(String well : split) {
        	
            if(!this.contains(new WellBigInteger(well))) {
                return false;
            }
        }
        
        return true;
    }
    
    
    /**
     * Returns true if the well is part of the well set.
     * @param    String    the well
     * @return             true if the well exists in the set
     */
    public boolean contains(String well) {
        return this.data.contains(new WellBigInteger(well));
    }
    
    /**
     * Returns true if a well with the index exists.
     */
    public boolean contains(WellIndex index) {
    	
    	if(index == null) {
            return false;
        }
    	
    	return this.data.contains(new WellBigInteger(index.row(), index.column()));
    }
    
    /**
     * Returns true if the wells in the list exist.
     */
    public boolean contains(WellList list) {
    	
    	if(list == null) {
            return false;
        }
    	
    	for(WellIndex index : list) {
    		if(!this.contains(index)) {
    			return false;
    		}
    	}
    	
    	return true;
    }

    /*---------------------------- String Methods ----------------------------*/

    /**
     * Returns a string holding the well ID and data for all the plate wells.
     * @return    well IDs and data
     */
    public String printAllData() {
        String result = this.label + " " + this.descriptor + "\n";
        for(WellBigInteger well : this.data) {
            result += this.printData(well) + "\n";
        }
        return result;
    }
    
    /**
     * Returns a string holding the well data.
     * @param    WellBigInteger    the well
     * @return                     the well data
     */
    public <T extends Number> String printData(WellBigInteger well) {
        
    	if(!this.contains(well)) {
            return null;
        }

        String data = well.toString();
        
        return data;
    }
    
    /*------------ Methods for equality, comparison and hash codes -----------*/ 
    
    /**
     * Plates are equivalent if they share the same dimensions, label, plate type,
     * data type, descriptor, size and well sets.
     * @param    Object    the object
     * @return             true if equal, false otherwise
     */
    public boolean equals(Object object) {
        
        if (object instanceof PlateBigInteger == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }
        
        PlateBigInteger plate = (PlateBigInteger) object;
        
        return this.rows == plate.rows() &&
               this.columns == plate.columns() &&
               this.label.equals(plate.label()) &&
               this.type == plate.type() &&
               this.descriptor.equals(plate.descriptor()) &&
               this.allGroups().equals(plate.allGroups()) &&
               this.size() == plate.size() &&
               this.dataType == plate.dataType();
    }
    
    /**
     * Hash code uses the plate dimensions, label, plate type, data type, 
     * descriptor, size and well sets.
     * @return    the hash code
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                   append(this.rows).
                   append(this.columns).
                   append(this.label).
                   append(this.type).
                   append(this.descriptor).
                   append(this.groups).
                   append(this.dataSet().size()).
                   append(this.dataType).
                   toHashCode();    
    }
    
    /**
     * Compares the object to another plate.
     * @param    PlateBigInteger plate the plate for comparison
     * @return       this == plate --> 0
     *               this > plate --> 1
     *               this < plate --> -1
     */
    public int compareTo(PlateBigInteger plate) {
    	return this.compare(this, plate);
    }
    
    /**
     * Plates are ordered using the size, data type, label and number of sets.  
     * @param    PlateBigInteger    the plate for comparison
     * @return                      plate1 == plate2 --> 0
     *                              plate1 > plate2 --> 1
     *                              plate1 < plate2 --> -1
     */
    public int compare(PlateBigInteger plate1, PlateBigInteger plate2) throws ClassCastException {
        
        if(plate1.equals(plate2)) {
            return 0;
        }
        
        int totalWellNumberThis = plate1.rows() * plate1.columns();
        int totalWellNumberPlate = plate2.rows() * plate2.columns();
        
        if(totalWellNumberThis > totalWellNumberPlate) {
            return 1;
        } else if(totalWellNumberThis < totalWellNumberPlate) {
            return -1;
        }
        
        if(plate1.rows() > plate2.rows()) {
            return 1;
        } else if(plate1.rows() < plate2.rows()) {
            return -1;
        }
        
        if(plate1.columns() > plate2.columns()) {
            return 1;
        } else if(plate1.columns() < plate2.columns()) {
            return -1;
        }

        if(plate1.label().compareTo(plate2.label()) > 0) {
            return 1;
        } else if(plate1.label().compareTo(plate2.label()) < 0) {
            return -1;
        }

        if(plate1.dataType > plate2.dataType) {
            return 1;
        } else if(plate1.dataType != plate2.dataType){
            return -1;
        }
        
        return 0;
    }
    
}
