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

package com.github.jessemull.microflex.plate;

import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A wrapper for a row and column index pair.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellIndex implements Comparable<WellIndex> {
    	
	/*---------------------------- Private Fields ----------------------------*/
	
	private int row;                // Row index
    private int column;             // Column index
    private int ALPHA_BASE = 26;    // Number of char types available for the row ID
    
    /*----------------------------- Constructors -----------------------------*/
    
    /**
     * Creates a new well index object.
     * @param    int    the row index
     * @param    int    the column index
     */
	public WellIndex(int row, int column) {
		this.row = row;
		this.column = column;
	}
		
	/*------------------------------- Methods --------------------------------*/
	
	/**
	 * Returns the row index.
	 * @return    the row index
	 */
	public int row() {
		return this.row;
	}
		
	/**
	 * Returns the column index.
	 * @return    the column index
	 */
	public int column() {
		return this.column;
	}

	/**
	 * Compares two well indices.
	 * @return    1 -> input > this
	 *            -1 -> input < this
	 *            0 -> input == this 
	 */
	public int compareTo(WellIndex index) {
		
		if(this.equals(index)) {
			return 0;
		}
		
		if(this.row != index.row()) {
			return this.row > index.row() ? 1 : -1;
		}
		
		return this.column > index.column() ? 1 : -1;
		
	}
	
	/**
	 * Returns a string containing the well.
	 */
	public String toString() {
		return this.rowString() + this.column;
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
     * Indicies with the same row and column are equivalent.
     * @param    Object    the object
     * @return             true if equal, false otherwise
     */
    public boolean equals(Object object) {
        
        if (object instanceof WellIndex == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }
        
        WellIndex index = (WellIndex) object;
        
        if(this.row != index.row() || this.column != index.column()) {
        	return false;
        }
        
        return true;
    }
    
    /**
     * Hash code uses the row and column.
     * @return    the hash code
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                   append(this.row).
                   append(this.column).
                   toHashCode();    
    }
}
