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

/* -------------------------- Package Declaration --------------------------- */

package com.github.jessemull.microflex.util;

/* ------------------------------ Dependencies ------------------------------ */

import java.util.Collection;

import com.google.common.base.Preconditions;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;

/**
 * This class validates well, well sets, plates and stacks by checking for null
 * values and wells outside a range of indices.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ValUtil {

	/*------- Methods for validating double wells, well sets and plates ------*/
	
    /**
     * Validates the set by checking for wells outside the valid range.
     * @param    int              the number of plate rows
     * @param    int              the number of plate columns
     * @param    WellSetDouble    the well set to validate
     */
    public static void validateSet(int rows, int columns, WellSetDouble set) {
        
        if(set == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(WellDouble well : set) {
            if(well.row() > rows || well.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + well.toString() 
                                                   + " in well set: " 
                                                   + set.toString());
            }
        }
    }
    
    /**
     * Validates the well by checking for invalid indices.
     * @param    int     the number of plate rows
     * @Param    int columns    the number of plate columns
     * @param    Well    the well to validate
     */
    public static void validateWell(int rows, int columns, WellDouble well) {
        
        if(well == null) {
            throw new NullPointerException("The well cannot be null.");
        }
        
        if(well.row() > rows || well.column() > columns) {
            throw new IllegalArgumentException("Invalid well indices for well: " 
                                               + well.toString()); 
        }
    }

    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    PlateDouble    the first plate
     * @param    PlateDouble    the second plate
     */
    public static void validatePlateDouble(PlateDouble plate1, PlateDouble plate2) {
        Preconditions.checkNotNull(plate1, "Plates cannot be null.");
        Preconditions.checkNotNull(plate2, "Plates cannot be null.");
        Preconditions.checkArgument(plate1.rows() == plate2.rows() || 
                                    plate1.columns() == plate2.columns(), 
                                    "Unequal plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    PlateDouble                the plate
     * @param    Collection<PlateDouble>    the plate collection
     */
    public static void validatePlateDouble(PlateDouble plate1, Collection<PlateDouble> collection) {
        for(PlateDouble plate2 : collection) {
            validatePlateDouble(plate1, plate2);
        }
    }
    
    /**
     * Validate each plate by checking for null values and invalid dimensions.
     * @param    PlateDouble[]    the plate array
     */
    public static void validatePlateDouble(PlateDouble plate1, PlateDouble[] array) {
        for(PlateDouble plate2 : array) {
            validatePlateDouble(plate1, plate2);
        }
    }
    
    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    int            the number of rows
     * @param    int            the number of columns
     * @param    PlateDouble    the second plate
     */
    public static void validatePlateDouble(int rows, int columns, PlateDouble plate) {
        Preconditions.checkNotNull(plate, "Plate cannot be null.");
        Preconditions.checkArgument(plate.rows() == rows || 
                                    plate.columns() == columns, 
                                    "Invalid plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    int                        the number of rows
     * @param    int                        the number of columns
     * @param    Collection<PlateDouble>    the plate collection
     */
    public static void validatePlateDouble(int row, int column, Collection<PlateDouble> collection) {
        for(PlateDouble plate : collection) {
            validatePlateDouble(row, column, plate);
        }
    }
    
    /**
     * Validates each plate by checking for null values and invalid dimensions.
     * @param    int              the number of rows
     * @param    int              the number of columns
     * @param    PlateDouble[]    the plate array
     */
    public static void validatePlateDouble(int row, int column, PlateDouble[] array) {
        for(PlateDouble plate : array) {
            validatePlateDouble(row, column, plate);
        }
    }
    
    /*------ Methods for validating integer wells, well sets and plates ------*/
    
    /**
     * Validates the set by checking for wells outside the valid range.
     * @param    int               the number of plate rows
     * @param    int               the number of plate columns
     * @param    WellSetInteger    the well set to validate
     */
    public static void validateSet(int rows, int columns, WellSetInteger set) {

        if(set == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(WellInteger well : set) {

            if(well.row() > rows || well.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + well.toString() 
                                                   + " in well set: " 
                                                   + set.toString());
            }
        }
    }
    
    /**
     * Validates the well by checking for invalid indices.
     * @param    int     the number of plate rows
     * @Param    int columns    the number of plate columns
     * @param    Well    the well to validate
     */
    public static void validateWell(int rows, int columns, WellInteger well) {
        
        if(well == null) {
            throw new NullPointerException("The well cannot be null.");
        }
        
        if(well.row() > rows || well.column() > columns) {
            throw new IllegalArgumentException("Invalid well indices for well: " 
                                               + well.toString()); 
        }
    }

    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    PlateInteger    the first plate
     * @param    PlateInteger    the second plate
     */
    public static void validatePlateInteger(PlateInteger plate1, PlateInteger plate2) {
        Preconditions.checkNotNull(plate1, "Plates cannot be null.");
        Preconditions.checkNotNull(plate2, "Plates cannot be null.");
        Preconditions.checkArgument(plate1.rows() == plate2.rows() || 
                                    plate1.columns() == plate2.columns(), 
                                    "Unequal plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    PlateInteger                the plate
     * @param    Collection<PlateInteger>    the plate collection
     */
    public static void validatePlateInteger(PlateInteger plate1, Collection<PlateInteger> collection) {
        for(PlateInteger plate2 : collection) {
            validatePlateInteger(plate1, plate2);
        }
    }
    
    /**
     * Validate each plate by checking for null values and invalid dimensions.
     * @param    PlateInteger[]    the plate array
     */
    public static void validatePlateInteger(PlateInteger plate1, PlateInteger[] array) {
        for(PlateInteger plate2 : array) {
            validatePlateInteger(plate1, plate2);
        }
    }
    
    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    int             the number of rows
     * @param    int             the number of columns
     * @param    PlateInteger    the second plate
     */
    public static void validatePlateInteger(int rows, int columns, PlateInteger plate) {
        Preconditions.checkNotNull(plate, "Plate cannot be null.");
        Preconditions.checkArgument(plate.rows() == rows && 
                                    plate.columns() == columns, 
                                    "Invalid plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    int                         the number of rows
     * @param    int                         the number of columns
     * @param    Collection<PlateInteger>    the plate collection
     */
    public static void validatePlateInteger(int row, int column, Collection<PlateInteger> collection) {
        for(PlateInteger plate : collection) {
            validatePlateInteger(row, column, plate);
        }
    }
    
    /**
     * Validates each plate by checking for null values and invalid dimensions.
     * @param    int               the number of rows
     * @param    int               the number of columns
     * @param    PlateInteger[]    the plate array
     */
    public static void validatePlateInteger(int row, int column, PlateInteger[] array) {
        for(PlateInteger plate : array) {
            validatePlateInteger(row, column, plate);
        }
    }
    
    /*----- Methods for validating BigDecimal wells, well sets and plates ----*/
    
    /**
     * Validates the set by checking for wells outside the valid range.
     * @param    int                  the number of plate rows
     * @param    int                  the number of plate columns
     * @param    WellSetBigDecimal    the well set to validate
     */
    public static void validateSet(int rows, int columns, WellSetBigDecimal set) {
        
        if(set == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(WellBigDecimal well : set) {
            if(well.row() > rows || well.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + well.toString() 
                                                   + " in well set: " 
                                                   + set.toString());
            }
        }
    }
    
    /**
     * Validates the well by checking for invalid indices.
     * @param    int     the number of plate rows
     * @Param    int columns    the number of plate columns
     * @param    Well    the well to validate
     */
    public static void validateWell(int rows, int columns, WellBigDecimal well) {
        
        if(well == null) {
            throw new NullPointerException("The well cannot be null.");
        }
        
        if(well.row() > rows || well.column() > columns) {
            throw new IllegalArgumentException("Invalid well indices for well: " 
                                               + well.toString()); 
        }
    }

    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    PlateBigDecimal    the first plate
     * @param    PlateBigDecimal    the second plate
     */
    public static void validatePlateBigDecimal(PlateBigDecimal plate1, PlateBigDecimal plate2) {
        Preconditions.checkNotNull(plate1, "Plates cannot be null.");
        Preconditions.checkNotNull(plate2, "Plates cannot be null.");
        Preconditions.checkArgument(plate1.rows() == plate2.rows() || 
                                    plate1.columns() == plate2.columns(), 
                                    "Unequal plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    PlateBigDecimal                the plate
     * @param    Collection<PlateBigDecimal>    the plate collection
     */
    public static void validatePlateBigDecimal(PlateBigDecimal plate1, Collection<PlateBigDecimal> collection) {
        for(PlateBigDecimal plate2 : collection) {
            validatePlateBigDecimal(plate1, plate2);
        }
    }
    
    /**
     * Validate each plate by checking for null values and invalid dimensions.
     * @param    PlateBigDecimal[]    the plate array
     */
    public static void validatePlateBigDecimal(PlateBigDecimal plate1, PlateBigDecimal[] array) {
        for(PlateBigDecimal plate2 : array) {
            validatePlateBigDecimal(plate1, plate2);
        }
    }
    
    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    int                the number of rows
     * @param    int                the number of columns
     * @param    PlateBigDecimal    the second plate
     */
    public static void validatePlateBigDecimal(int rows, int columns, PlateBigDecimal plate) {
        Preconditions.checkNotNull(plate, "Plate cannot be null.");
        Preconditions.checkArgument(plate.rows() == rows || 
                                    plate.columns() == columns, 
                                    "Invalid plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    int                            the number of rows
     * @param    int                            the number of columns
     * @param    Collection<PlateBigDecimal>    the plate collection
     */
    public static void validatePlateBigDecimal(int row, int column, Collection<PlateBigDecimal> collection) {
        for(PlateBigDecimal plate : collection) {
            validatePlateBigDecimal(row, column, plate);
        }
    }
    
    /**
     * Validates each plate by checking for null values and invalid dimensions.
     * @param    int                  the number of rows
     * @param    int                  the number of columns
     * @param    PlateBigDecimal[]    the plate array
     */
    public static void validatePlateBigDecimal(int row, int column, PlateBigDecimal[] array) {
        for(PlateBigDecimal plate : array) {
            validatePlateBigDecimal(row, column, plate);
        }
    }
    
    /*----- Methods for validating BigInteger wells, well sets and plates ----*/
    
    /**
     * Validates the set by checking for wells outside the valid range.
     * @param    int                  the number of plate rows
     * @param    int                  the number of plate columns
     * @param    WellSetBigInteger    the well set to validate
     */
    public static void validateSet(int rows, int columns, WellSetBigInteger set) {
        
        if(set == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(WellBigInteger well : set) {
            if(well.row() > rows || well.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + well.toString() 
                                                   + " in well set: " 
                                                   + set.toString());
            }
        }
    }
    
    /**
     * Validates the well by checking for invalid indices.
     * @param    int     the number of plate rows
     * @Param    int columns    the number of plate columns
     * @param    Well    the well to validate
     */
    public static void validateWell(int rows, int columns, WellBigInteger well) {
        
        if(well == null) {
            throw new NullPointerException("The well cannot be null.");
        }
        
        if(well.row() > rows || well.column() > columns) {
            throw new IllegalArgumentException("Invalid well indices for well: " 
                                               + well.toString()); 
        }
    }

    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    PlateBigInteger    the first plate
     * @param    PlateBigInteger    the second plate
     */
    public static void validatePlateBigInteger(PlateBigInteger plate1, PlateBigInteger plate2) {
        Preconditions.checkNotNull(plate1, "Plates cannot be null.");
        Preconditions.checkNotNull(plate2, "Plates cannot be null.");
        Preconditions.checkArgument(plate1.rows() == plate2.rows() || 
                                    plate1.columns() == plate2.columns(), 
                                    "Unequal plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    PlateBigInteger                the plate
     * @param    Collection<PlateBigInteger>    the plate collection
     */
    public static void validatePlateBigInteger(PlateBigInteger plate1, Collection<PlateBigInteger> collection) {
        for(PlateBigInteger plate2 : collection) {
            validatePlateBigInteger(plate1, plate2);
        }
    }
    
    /**
     * Validate each plate by checking for null values and invalid dimensions.
     * @param    PlateBigInteger[]    the plate array
     */
    public static void validatePlateBigInteger(PlateBigInteger plate1, PlateBigInteger[] array) {
        for(PlateBigInteger plate2 : array) {
            validatePlateBigInteger(plate1, plate2);
        }
    }
    
    /**
     * Validates the plate by checking for null values and invalid dimensions.
     * @param    int                the number of rows
     * @param    int                the number of columns
     * @param    PlateBigInteger    the second plate
     */
    public static void validatePlateBigInteger(int rows, int columns, PlateBigInteger plate) {
        Preconditions.checkNotNull(plate, "Plate cannot be null.");
        Preconditions.checkArgument(plate.rows() == rows || 
                                    plate.columns() == columns, 
                                    "Invalid plate dimensions.");
    }
    
    /**
     * Validates each plate in the collection by checking for null values and 
     * invalid dimensions.
     * @param    int                            the number of rows
     * @param    int                            the number of columns
     * @param    Collection<PlateBigInteger>    the plate collection
     */
    public static void validatePlateBigInteger(int row, int column, Collection<PlateBigInteger> collection) {
        for(PlateBigInteger plate : collection) {
            validatePlateBigInteger(row, column, plate);
        }
    }
    
    /**
     * Validates each plate by checking for null values and invalid dimensions.
     * @param    int                  the number of rows
     * @param    int                  the number of columns
     * @param    PlateBigInteger[]    the plate array
     */
    public static void validatePlateBigInteger(int row, int column, PlateBigInteger[] array) {
        for(PlateBigInteger plate : array) {
            validatePlateBigInteger(row, column, plate);
        }
    }
    
    /**
     * Validates the group by checking for wells outside the valid range.
     * @param    int         the number of plate rows
     * @param    int         the number of plate columns
     * @param    WellList    the group
     */
    public static void validateGroup(int rows, int columns, WellList list) {
        
        if(list == null) {
            throw new NullPointerException("The well set cannot be null.");
        }
        
        for(WellIndex index : list) {
            if(index.row() > rows || index.column() > columns) {
                throw new IllegalArgumentException("Invalid well indices for well: " 
                                                   + index.toString() 
                                                   + " in well group: " 
                                                   + list.toString());
            }
        }
    }
}
