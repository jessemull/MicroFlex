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

/* ---------------------- Package ----------------------- */

package com.github.jessemull.microflex.plate;

/* -------------------- Dependencies -------------------- */

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.base.Preconditions;

/**
 * This class represents a well in a microplate. It contains the the logic to 
 * convert row letters to integers and vice-versa, enforces the correct format 
 * for well IDs and holds a list of data set values. The well object does not 
 * check for wells outside a specified range. This logic is housed within the 
 * plate object.
 * 
 * All the classes in the microplate library are designed to be flexible in
 * order to accommodate data in a variety of formats. The well object constructor
 * accepts well IDs in each of the following formats:
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Row<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Column</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Input</div></th>
 *    <tr>
 *       <td>Integer</td>
 *       <td>Integer</td>
 *       <td>Row = 1 Column = 2</td>
 *    </tr>
 *    <tr>
 *       <td>String</td>
 *       <td>Integer</td>
 *       <td>Row = "1" Column = 2</td>
 *    </tr>
 *    <tr>
 *       <td>String</td>
 *       <td>Integer</td>
 *       <td>Row = "A" Column = 2</td>
 *    </tr>
 *    <tr>
 *       <td>Integer</td>
 *       <td>String</td>
 *       <td>Row = 1 Column = "2"</td>
 *    </tr>
 *    <tr>
 *       <td>String</td>
 *       <td>String</td>
 *       <td>Row = "A" Column = "2"</td>
 *    </tr>
 *    <tr>
 *       <td>String</td>
 *       <td>String</td>
 *       <td>Row = "1" Column = "2"</td>
 *    </tr>
 *    <tr>
 *       <td>String</td>
 *       <td>String</td>
 *       <td>"A2" Input must be [A-Za-z]+[0-9]+</td>
 *    </tr>
 * </table>
 * 
 * The Well class also implements both hash code and equals functions in order to
 * prevent duplicate wells within a single plate object.
 * 
 * The well constructor is passed a flag holding the numerical data type. Once 
 * set, the numerical data type cannot be changed. The MicroFlex library supports 
 * wells containing all primitive numerical types for input and output as well 
 * as two immutable types: BigDecimal and BigInteger.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class Well<T extends Number> implements Comparable<Well<T>> {
    
    /* --------------------------- Public Fields ---------------------------- */
    
    public static final int DOUBLE = 0;
    public static final int INTEGER = 1;
    public static final int BIGDECIMAL = 2;
    public static final int BIGINTEGER = 3;
    
    /* --------------------------- Private Fields --------------------------- */
    
    private int row;                                                  // Well row
    private int column;                                               // Well column
    private int type;                                                 // Numerical data type
    
    private int ALPHA_BASE = 26;                                      // Number of char types available for the row ID
    private String digits = "\\d+$";                                  // Numerals only regex
    private String letters = "^[A-Z]+";                               // Letters only regex 
    private String alphaOnly = "^[A-Za-z]+[0-9]+$";                   // Alphanumeric characters only     
    private Pattern digitsPattern = Pattern.compile(digits);          // Numeral only pattern
    private Pattern lettersPattern = Pattern.compile(letters);        // Letters only pattern
    private Pattern alphaOnlyPattern = Pattern.compile(alphaOnly);    // Alphanumeric characters only pattern
    
    /* ---------------------------- Constructors ---------------------------- */
    
    /**
     * Creates a new Well object from row and column integers.
     * @param    int    the well row
     * @param    int    the well column
     */
    public Well(int type, int row, int column) {
        this.validateType(type);
        this.validateIndices(row, column);
        this.type = type;
        this.row = row;
        this.column = column;

    }
       
    /**
     * Creates a new Well object using the numerical type flag, row string and 
     * column number.
     * @param    int       the numerical data type
     * @param    String    the well row
     * @param    int       the well column
     */
    public Well(int type, String row, int column) {
        
        try {
            this.row = Integer.decode(row);
        } catch(NumberFormatException e) {
            this.row = parseRow(row);
        }
        
        this.column = column;
        this.type = type;
        
        validateIndices(this.row, this. column);
    }
        
    /**
     * Creates a new Well object using the numerical type flag, row number and 
     * column string.
     * @param    int       the numerical data type
     * @param    int       the well row
     * @param    String    the well column
     * @throws   IllegalArgumentException    invalid column value
     */
    public Well(int type, int row, String column) {
        
        this.row = row;
        this.type = type;
        
        try {
            this.column = Integer.decode(column);
        } catch(Exception e) {
            throw new IllegalArgumentException("Illegal column value: " + column);
        }

        validateIndices(this.row, this. column);
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row and column 
     * strings.
     * @param    int       the numerical data type
     * @param    String    the well row
     * @param    String    the well column
     * @throws   IllegalArgumentException    invalid column value
     */
    public Well(int type, String row, String column) {
       
        try {
            this.row = Integer.decode(row);
        } catch(NumberFormatException e) {
            this.row = parseRow(row);
        }
        
        try {
            this.column = Integer.decode(column);
        } catch(Exception e) {
            throw new IllegalArgumentException("Illegal column value: " + column);
        }
        
        this.type = type;
        validateIndices(this.row, this. column);
    }
    
    /**
     * Creates a new Well object from a string holding the column and row values.
     * The string must be in the format [a-ZA-Z]+[0-9]+
     * @param    int       the numerical data type
     * @param    String    the well index
     */
    public Well(int type, String wellID) {
        
    	this.type = type;
        String upper = wellID.toUpperCase().trim();
        
        Matcher digitsMatcher = digitsPattern.matcher(upper);
        Matcher lettersMatcher = lettersPattern.matcher(upper);
        Matcher alphasOnlyMatcher = alphaOnlyPattern.matcher(upper);
        
        /* Alphas matcher enforces the correct format for the well ID */
        
        if(alphasOnlyMatcher.find()) {
            
            lettersMatcher.find();
            digitsMatcher.find();
            
            this.row = parseRow(lettersMatcher.group(0).trim());
            
            try {
                this.column = Integer.decode(digitsMatcher.group(0));
            } catch(NumberFormatException e) {
                throw new IllegalArgumentException("Invalid column ID: " + digitsMatcher.group(0).trim());
            }
            
        } else {
            throw new IllegalArgumentException("Invalid well index: " + wellID);
        }

        validateIndices(this.row, this. column);
    }
    
    /**
     * Clones a well without invoking clone.
     * @param    Well    the well to clone
     */
    public Well(Well<T> well) {
        this.type = well.type();
    	this.row = well.row();
    	this.column = well.column();
    }

    /* --------------------------- Private Methods -------------------------- */
    
    /**
     * Converts a row ID to an integer value.
     * @param    String    the row as a string
     * @return             the row as an integer value
     */
    private int parseRow(String rowString) {
        
        int rowInt = 0;
        int baseIndex = 1;

        String upper = rowString.toUpperCase().trim();
        Matcher lettersMatcher = lettersPattern.matcher(upper);
        
        if(lettersMatcher.find()) {
            
            String letters = lettersMatcher.group(0);
            
            rowInt = letters.charAt(letters.length() - 1) - 65;

            for(int i = letters.length() - 2; i >= 0; i--) {
                rowInt += (letters.charAt(i) - 65 + 1) * Math.pow(ALPHA_BASE, baseIndex++);  
            }
            
            return rowInt;
            
        } else {           
            throw new IllegalArgumentException("Invalid row ID: " + row);
        }
        
    }

    /**
     * Returns the well index as a letter(s) number pair.
     * @return    the index
     */
    public String index() {
    	return this.rowString() + this.column;
    }
    
    /**
     * Returns the numerical data type as an integer value.
     * @return    numerical data type integer
     */
    public int type() {
        return this.type;
    }
    
    /**
     * Returns the numerical data type as a String.
     * @return    numerical data type String
     */
    public String typeString() {
        switch(this.type) {
            case 0: return "Double";
            case 1: return "Integer";
            case 2: return "BigDecimal";
            case 3: return "BigInteger";
            default: return "Undefined numerical data type.";
        }
    }
    
    /**
     * Returns the column number.
     * @return    the column number
     */
    public int column() {
        return this.column;
    }
    
    /**
     * Returns the row number.
     * @return    the row number
     */
    public int row() {
        return this.row;
    }
    
    /**
     * Returns the alpha base value.
     * @return    the alpha base value
     */
    public int alphaBase() {
        return this.ALPHA_BASE;
    }
    
    /**
     * Returns the row ID.
     * @return    row ID
     */
    public String rowString() {
        
        int rowInt = this.row;
        String rowString = "";
        
        while (rowInt >=  0) {
            rowString = (char) (rowInt % ALPHA_BASE + 65) + rowString;
            rowInt = (rowInt  / ALPHA_BASE) - 1;
        }
        
        return rowString;
    }    
    
    /**
     * Returns row ID plus column number.
     * @return    row ID plus column number
     */
    public String toString() {
    	
    	String array = "[";
    	
    	int index;
    	
    	for(index = 0; index < this.data().size() - 1; index++) {
    		array += this.data().get(index) + ", ";
    	}
    	
    	array += this.data().get(index) + "]";
    	
        return rowString() + this.column + " " + array;
    }
    
    /**
     * Checks that the row and column integer values have a positive sign.
     * @param    int    the row value
     * @param    int    the column value
     */
    private void validateIndices(int row, int column) {
        Preconditions.checkArgument(row >= 0, "Invalid row index: %s. Row value must be a positive value.", row);
        Preconditions.checkArgument(column > 0, "Invalid column index: %s. Column value must be greater than zero.", column);
    }
    
    /**
     * Checks type flag for invalid values.
     * @param    int    the numerical data type
     */
    private void validateType(int type) {  
        if(type < 0 || type > 3) {
            throw new IllegalArgumentException("Invalid numerical data type: " + type + ".");
        }
    }
    
    /**
     * Hash code uses the row, column and ALPHA_BASE fields.
     * @return    the hash code
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                   append(this.row).
                   append(this.column).
                   append(this.ALPHA_BASE).
                   toHashCode();    
    }
    
    /**
     * Wells are ordered based on row and column number.
     * @param    Well    the object for comparison
     * @return           this == well --> 0
     *                   this > well --> 1
     *                   this < well --> -1
     */
    public int compareTo(Well<T> well) throws ClassCastException {
        
        if(this.equals(well)) {
            return 0;
        }
        
        if(this.row() > well.row()) {
            return 1;
        } else if(this.row() != well.row()) {
            return -1;
        }
        
        if(this.column() > well.column()) {
            return 1;
        } else if(this.column() != well.column()) {
            return -1;
        }
        
        return 0;
    }
    
    public abstract List<T> data();
}
