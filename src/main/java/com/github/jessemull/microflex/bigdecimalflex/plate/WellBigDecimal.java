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

package com.github.jessemull.microflex.bigdecimalflex.plate;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.jessemull.microflex.plate.Well;
import com.github.jessemull.microflex.util.BigDecimalUtil;

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
 * set, the numerical data type cannot be changed. This well type supports storage 
 * of BigDecimal values only. The MicroFlex library supports wells containing all 
 * primitive numerical types for input and output as well as two immutable types: 
 * BigDecimal and BigInteger.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellBigDecimal extends Well<BigDecimal> implements Iterable<BigDecimal> {
    
	/*---------------------------- Private Fields ----------------------------*/
	
    private List<BigDecimal> data = new ArrayList<BigDecimal>();
    
    /* ---------------------------- Constructors ---------------------------- */
    
    /**
     * Creates a new Well object from row and column integers.
     * @param    int    the well row
     * @param    int    the well column
     */
    public WellBigDecimal(int row, int column) {
        super(Well.BIGDECIMAL, row, column);
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row number, 
     * column number and data set.
     * @param    Collection<BigDecimal>    the data set
     * @param    int                       the well row
     * @param    int                       the well column
     */
    public WellBigDecimal(int row, int column, Collection<BigDecimal> data) {
    	super(Well.BIGDECIMAL, row, column);
    	for(BigDecimal number : data) {
    	    this.data.add(BigDecimalUtil.toBigDecimal(number));
    	}
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row number, 
     * column number and data set.
     * @param    BigDecimal[]     the data set
     * @param    int             the well row
     * @param    int             the well column
     */
    public WellBigDecimal(int row, int column, BigDecimal[] data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row string and 
     * column number.
     * @param    String    the well row
     * @param    int       the well column
     */
    public WellBigDecimal(String row, int column) {
        super(Well.BIGDECIMAL, row, column);  
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row string, 
     * column number and data set.
     * @param    String                    the well row
     * @param    int                       the well column
     * @param    Collection<BigDecimal>    the data set
     */
    public WellBigDecimal(String row, int column, Collection<BigDecimal> data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }

    /**
     * Creates a new Well object using the numerical type flag, row string, 
     * column number and data set.
     * @param    String          the well row
     * @param    int             the well column
     * @param    BigDecimal[]    the data set
     */
    public WellBigDecimal(String row, int column, BigDecimal[] data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row number and 
     * column string.
     * @param    int       the well row
     * @param    String    the well column
     * @throws   IllegalArgumentException    invalid column value
     */
    public WellBigDecimal(int row, String column) {
        super(Well.BIGDECIMAL, row, column);
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row number and 
     * column string.
     * @param    int                       the well row
     * @param    String                    the well column
     * @param    Collection<BigDecimal>    the data set
     * @throws   IllegalArgumentException       invalid column value
     */
    public WellBigDecimal(int row, String column, Collection<BigDecimal> data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row number and 
     * column string.
     * @param    int             the well row
     * @param    String          the well column
     * @param    BigDecimal[]    the data set
     * @throws   IllegalArgumentException    invalid column value
     */
    public WellBigDecimal(int row, String column, BigDecimal[] data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row and column 
     * strings.
     * @param    String    the well row
     * @param    String    the well column
     * @throws   IllegalArgumentException    invalid column value
     */
    public WellBigDecimal(String row, String column) {
        super(Well.BIGDECIMAL, row, column);
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row string, 
     * column string and data set.
     * @param    String                    the well row
     * @param    String                    the well column
     * @param    Collection<BigDecimal>    the data set
     * @throws   IllegalArgumentException       invalid column value
     */
    public WellBigDecimal(String row, String column, Collection<BigDecimal> data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object using the numerical type flag, row and column 
     * strings.
     * @param    String          the well row
     * @param    String          the well column
     * @param    BigDecimal[]    the data set
     * @throws   IllegalArgumentException    invalid column value
     */
    public WellBigDecimal(String row, String column, BigDecimal[] data) {
        super(Well.BIGDECIMAL, row, column);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object from a string holding the column and row values.
     * The string must be in the format [a-ZA-Z]+[0-9]+
     * @param    String    the well index
     */
    public WellBigDecimal(String wellID) {
        super(Well.BIGDECIMAL, wellID);  
    }
    
    /**
     * Creates a new Well object using the numerical type flag and a string 
     * holding the column and row values. The string must be in the format 
     * [a-ZA-Z]+[0-9]+
     * @param    String                    the well index
     * @param    Collection<BigDecimal>    the data set
     */
    public WellBigDecimal(String wellID, Collection<BigDecimal> data) {
        super(Well.BIGDECIMAL, wellID);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Creates a new Well object using the numerical type flag and a string 
     * holding the column and row values. The string must be in the format 
     * [a-ZA-Z]+[0-9]+
     * @param    String          the well index
     * @param    BigDecimal[]    the data set
     */
    public WellBigDecimal(String wellID, BigDecimal[] data) {
        super(Well.BIGDECIMAL, wellID);
        for(BigDecimal number : data) {
            this.data.add(BigDecimalUtil.toBigDecimal(number));
        }
    }
    
    /**
     * Clones a double well without invoking clone.
     * @param    Well    the well to clone
     */
    public WellBigDecimal(WellBigDecimal well) {
        super(well);
        this.data = new ArrayList<BigDecimal>(well.data());
    }
    
    /* -------------------- Methods for data set output --------------------- */

    /**
     * Returns the data set.
     * @return    the data set
     */
    public List<BigDecimal> data() {
        return this.data;
    }
    
    /**
     * Returns the well data set as a list of doubles. Overflow results in an
     * arithmetic exception.
     * @return    the data set
     */
    public List<Double> toDouble() {
    	return BigDecimalUtil.toDoubleList(this.data);
    }
    
    /**
     * Returns the well data set as an array of doubles. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public double[] toDoubleArray() {
        return BigDecimalUtil.toDoubleArray(this.data);
    }
    
    /**
     * Returns the well data set as a list of bytes. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public List<Byte> toByte() {  
        return BigDecimalUtil.toByteList(this.data);
    }
    
    /**
     * Returns the well data set as an array of bytes. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public byte[] toByteArray() {
    	return BigDecimalUtil.toByteArray(this.data);
    }
    
    /**
     * Returns the well data set as a list of shorts. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public List<Short> toShort() {
    	return BigDecimalUtil.toShortList(this.data);
    }
    
    /**
     * Returns the well data set as an array of shorts. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public short[] toShortArray() {
    	return BigDecimalUtil.toShortArray(this.data);
    }
    
    /**
     * Returns the well data set as an array of shorts. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public List<Integer> toInt() {
    	return BigDecimalUtil.toIntList(this.data);
    }
    
    /**
     * Returns the well data set as an array of integers. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public int[] toIntArray() {
    	return BigDecimalUtil.toIntArray(this.data);
    }
    
    /**
     * Returns the well data set as a list of longs. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public List<Long> toLong() {
    	return BigDecimalUtil.toLongList(this.data);
    }
    
    /**
     * Returns the well data set as an array of longs. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public long[] toLongArray() {
    	return BigDecimalUtil.toLongArray(this.data);
    }
    
    /**
     * Returns the well data set as a list of floats. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public List<Float> toFloat() {
    	return BigDecimalUtil.toFloatList(this.data);
    }
    
    /**
     * Returns the well data set as an array of floats. Overflow results in an 
     * arithmetic exception.
     * @return    the data set
     */
    public float[] toFloatArray() {
    	return BigDecimalUtil.toFloatArray(this.data);
    }
   
    /**
     * Returns the well data set as a list of big decimals.
     * @return    the data set
     */
    public List<BigDecimal> toBigDecimal() {
    	return this.data;
    }
    
    /**
     * Returns the well data set as a list of big decimals.
     * @return    the data set
     */
    public BigDecimal[] toBigDecimalArray() {
    	return BigDecimalUtil.toBigDecimalArray(this.data);
    }
    
    /**
     * Returns the well data set as a list of big integers.
     * @return    the data set
     */
    public List<BigInteger> toBigInteger() {
    	return BigDecimalUtil.toBigIntList(this.data);
    }
    
    /**
     * Returns the well data set as an array of big integers.
     * @return    the data set
     */
    public BigInteger[] toBigIntegerArray() {
    	return BigDecimalUtil.toBigIntArray(this.data);
    }
    
    /* ---------------------- Methods for adding data ----------------------- */
    
    /**
     * Adds a single data point to a well.
     * @param    BigDecimal    the value to add
     */
    public void add(BigDecimal datum) {
        this.data.add(BigDecimalUtil.toBigDecimal(datum));
    }
    
    /**
     * Adds a collection of numbers to the data set.
     * @param    Collection<BigDecimal>    the data to add
     */
    public void add(Collection<BigDecimal> collection) {
    	for(BigDecimal number : collection) {
    		this.data.add(BigDecimalUtil.toBigDecimal(number));
    	}
    }
    
    /**
     * Adds an array of numbers to the data set.
     * @param    BigDecimal[]    the data to add
     */
    public void add(BigDecimal[] array) {
    	for(BigDecimal number : array) {
    		this.data.add(BigDecimalUtil.toBigDecimal(number));
    	}
    }
    
    /**
     * Adds the data from another well.
     * @param    Well    the well with data set to add
     */
    public void add(WellBigDecimal well) {
    	for(BigDecimal db : well) {
    		this.data.add(db);
    	}
    }
    
    /**
     * Adds the data from each well in the well set.
     * @param    WellSet    the well set to add
     */
    public void add(WellSetBigDecimal set) {
    	for(Well<BigDecimal> well : set) {
    	    for(Object obj : well.data()) {
    		    this.add(BigDecimalUtil.toBigDecimal(obj));
    	    }
    	}
    }
    
    /* --------------------- Methods for replacing data --------------------- */
    
    /**
     * Clears the data set and adds the number.
     * @param    BigDecimal    replacement datum
     */
    public void replaceData(BigDecimal datum) {
    	this.data.clear();
        this.data.add(BigDecimalUtil.toBigDecimal(datum));
    }
    
    /**
     * Clears the data set and adds the collection.
     * @param    Collection<BigDecimal>    replacement data
     */
    public void replaceData(Collection<BigDecimal> collection) {
    	this.data.clear();
    	this.add(collection);
    }
    
    /**
     * Clears the data set and adds the array.
     * @param    BigDecimal[]    replacement data
     */
    public void replaceData(BigDecimal[] array) {
    	this.data.clear();
    	this.add(array);
    }
    
    /**
     * Clears the data set and adds the well data set.
     * @param    Well    well with replacement data
     */
    public void replaceData(WellBigDecimal well) {
    	this.data.clear();
    	this.add(well);
    }
    
    /**
     * Clears the data set and adds the data from each well in the set.
     * @param    WellSet    set of wells with replacement data
     */
    public void replaceData(WellSetBigDecimal set) {
    	this.data.clear();
    	this.add(set);
    }
    
    /* --------------------- Methods for removing data ---------------------- */
   
    /**
     * Removes the value from the data set.
     * @param    BigDecimal    datum for removal
     */
    public void remove(BigDecimal number) {
    	List<BigDecimal> list = new ArrayList<BigDecimal>();
    	list.add(number);
    	this.data.removeAll(list);
    }
    
    /**
     * Removes all the values in the collection from the data set.
     * @param    Collection<BigDecimal>    data for removal
     */
    public void remove(Collection<BigDecimal> collection) {
    	List<BigDecimal> list = new ArrayList<BigDecimal>(collection);
    	this.data.removeAll(list);
    }
    
    /**
     * Removes all the values in the array from the data set.
     * @param    BigDecimal[]    data for removal
     */
    public void remove(BigDecimal[] array) {
    	this.data.removeAll(Arrays.asList(array));
    }
    
    /**
     * Removes all the well values from the data set.
     * @param    Well    well with data for removal
     */
    public void removeWell(WellBigDecimal well) {
    	this.data.removeAll(well.data());
    }
    
    /**
     * Removes the well values for each well from the data set.
     * @param    WellSet    wells with replacement data
     */
    public void removeSet(WellSetBigDecimal set) {
    	for(Well<BigDecimal> well : set) {
    		this.data.removeAll(well.data());
    	}
    }
    
    /**
     * Removes all values within the indices.
     * @param    int    beginning index
     * @param    int    number of values to remove
     */
    public void removeRange(int begin, int end) {
    	
    	if(begin > end) {
    		throw new IndexOutOfBoundsException("The starting index must be less than the ending index.");
    	}
    	
    	if(begin < 0) {
    		throw new IllegalArgumentException("Indices must be positive values.");
    	}
    	
    	if(end > this.data().size()) {
    		throw new IndexOutOfBoundsException("Ending index does not exist.");
    	}
    	
    	List<BigDecimal> removed = new ArrayList<BigDecimal>(this.data().subList(0, begin));
    	removed.addAll(this.data().subList(end, this.size()));
    	this.data = removed;
    }
    
    /* -------------------- Methods for retaining data ---------------------- */
    
    /**
     * Retains the values in the data set.
     * @param    BigDecimal    datum for retention
     */
    public void retain(BigDecimal number) {
    	if(this.data.contains(number)) {
    		this.data.clear();
    		this.data.add(BigDecimalUtil.toBigDecimal(number));
    	} else {
    		throw new IllegalArgumentException(number + " does not exist in the well data set.");
    	}
    }
    
    /**
     * Retains all the values in the collection.
     * @param    Collection<BigDecimal>    data for retention
     */
    public void retain(Collection<BigDecimal> collection) {
    	this.data.retainAll(collection);
    }
    
    /**
     * Retains all the values in the array.
     * @param    BigDecimal[]    data for retention
     */
    public void retain(BigDecimal[] array) {
    	this.data.retainAll(Arrays.asList(array));
    }
    
    /**
     * Retains all the values in the well.
     * @param    Well    well with data for retention
     */
    public void retainWell(WellBigDecimal well) {
    	this.data.retainAll(well.data());
    }
    
    /**
     * Retains all the values in each well.
     * @param    WellSet    wells with retention data
     */
    public void retainSet(WellSetBigDecimal set) {
    	for(Well<BigDecimal> well : set) {
    		this.data.retainAll(well.data());
    	}
    }
    
    /**
     * Retains all values within the indices.
     * @param    int    beginning index
     * @param    int    number of values to remove
     */
    public void retainRange(int begin, int end) {
    	
    	if(begin > end) {
    		throw new IndexOutOfBoundsException("The starting index must be less than the ending index.");
    	}
    	
    	if(begin < 0) {
    		throw new IllegalArgumentException("Indices must be positive values.");
    	}
    	
    	if(end > this.data().size()) {
    		throw new IndexOutOfBoundsException("Ending index does not exist.");
    	}

    	this.data = this.data.subList(begin, end);
    }
    
    /* ------------------- Methods for plate parameters --------------------- */
    
    /**
     * Returns the size of the well data set.
     * @return    the number of values in the data set
     */
    public int size() {
    	return this.data.size();
    }
    
    /**
     * Clears the well data.
     */
    public void clear() {
    	this.data.clear();
    }
    
    /**
     * Returns true if the well data set is empty and false otherwise.
     * @return    true if the data set is empty
     */
    public boolean isEmpty() {
    	return this.data.isEmpty();
    }
    
    /**
     * Returns a new well containing the values within the indices.
     * @param    int    beginning index
     * @param    int    number of values to remove
     * @return          the new well
     */
    public WellBigDecimal subList(int begin, int length) {
    	WellBigDecimal well = new WellBigDecimal(this.row(), this.column());
    	well.add(this.data().subList(begin, begin + length));
    	return well;
    }
    
    /**
     * Returns true if the well data set contains the value.
     * @param    BigDecimal    the input value
     * @return                 true if the data set contains the value
     */
    public boolean contains(BigDecimal number) {
    	return this.data.contains(BigDecimalUtil.toBigDecimal(number));
    }
    
    /**
     * Returns the index of the first occurrence of the specified element in 
     * this list, or -1 if this list does not contain the element.
     * @param    BigDecimal    the input value
     * @return                 the index
     */
    public int indexOf(BigDecimal number) {
    	return this.data.indexOf(number);
    }
    
    /**
     * Returns the value at the specified index.
     * @param    int    the index
     * @return          the value at the index
     */
    public BigDecimal get(int index) {
    	return this.data.get(index);
    }
    
    /**
     * Returns the index of the last occurrence of the specified element in this 
     * list, or -1 if this list does not contain the element.
     * @param    int    the index
     * @return          the index of the last occurrence
     */
    public double lastIndexOf(BigDecimal number) {
    	return this.data.lastIndexOf(number);
    } 

	/**
	 * Returns an iterator for the well data set.
	 * @return    the iterator
	 */
	public Iterator<BigDecimal> iterator() {
		return this.data.iterator();
	}

	/**
     * Wells are equivalent if the row, column and ALPHA_BASE
     * fields are equivalent.
     * @param    Object    the object
     * @return             true if equal, false otherwise
     */
    public boolean equals(Object object) {
        
        if (object instanceof WellBigDecimal == false) {
            return false;
        }
          
        if (this == object) {
            return true;
        }
        
        WellBigDecimal well = (WellBigDecimal) object;
        
        return this.row() == well.row() && 
               this.column() == well.column() &&
               this.alphaBase() == well.alphaBase();    
    }
}
