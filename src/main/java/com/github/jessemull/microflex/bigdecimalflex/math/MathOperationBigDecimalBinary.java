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

package com.github.jessemull.microflex.bigdecimalflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.StackBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

/**
 * This class performs mathematical operations with two arguments for BigDecimal 
 * plate stacks, plates, wells and well sets. To create a custom mathematical
 * operation extend this class and override the calculate methods using the
 * appropriate operation. 
 * 
 * Operations can be performed on stacks, plates, sets or wells of uneven length
 * using standard or strict functions. Standard functions treat all values missing
 * from a data set as zeroes and combine all stacks, plates, sets and wells from 
 * both input objects. Strict functions omit all values, stacks, plates, wells 
 * and sets missing from one of the input objects:
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Operation<div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Output</div></th>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Standard</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Treats missing values as zeroes</td>
 *             </tr>
 *             <tr>
 *                <td>Combines stacks, plates, sets, wells and values from both input objects</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 *    <tr>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Strict</td>
 *             </tr>
 *          </table>  
 *       </td>
 *       <td valign="top">
 *          <table>
 *             <tr>
 *                <td>Omits all missing values</td>
 *             </tr>
 *              <tr>
 *                <td>Combines stacks, plates, sets, wells and values present in both input objects only</td>
 *             </tr>
 *          </table>  
 *       </td>
 *    </tr>
 * </table>
 * 
 * The functions within the MicroFlex library are designed to be flexible and the
 * math operation binary object supports operations using two stacks, plates, sets
 * and well objects as input. In addition, it supports operations using a single
 * stack, plate, set or well object and a collection, array or constant. It also
 * allows the developer to limit the operation to a subset of data:
 * 
 * <table cellspacing="10px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Input 1<br><div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Input 2</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Beginning<br>Index</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Length of<br>Subset</div></th>
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px; padding-top: 18px;">Operation</div></th>
 *    <tr>
 *       <td>Well</td>
 *       <td>Well</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the two wells</td>
 *    </tr>
 *    <tr>
 *       <td>Well</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in the well</td>
 *    </tr>
 *    <tr>
 *       <td>Well</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in the well</td>
 *    </tr>
 *    <tr>
 *       <td>Well</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant value and each value in the well</td>
 *    </tr>

 *    <tr></tr>
 *    <tr></tr>

 *    <tr>
 *       <td>Set</td>
 *       <td>Set</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation on the values in each matching pair of wells in the two sets</td>
 *    </tr>
 *    <tr>
 *       <td>Set</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in each well of the set</td>
 *    </tr>
 *    <tr>
 *       <td>Set</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in each well of the set</td>
 *    </tr>
 *    <tr>
 *       <td>Set</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant and each value in each well of the set</td> 
 *    </tr>

 *    <tr></tr>
 *    <tr></tr>

 *    <tr>
 *       <td>Plate</td>
 *       <td>Plate</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation on the values in each matching pair of wells in the two plates</td>
 *    </tr>
 *    <tr>
 *       <td>Plate</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in each well of the plate</td>
 *    </tr>
 *    <tr>
 *       <td>Plate</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in each well of the plate</td>
 *    </tr>
 *    <tr>
 *       <td>Plate</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant and each value in each well of the plate</td>
 *    </tr>

 *    <tr></tr>
 *    <tr></tr>
 *    
 *    <tr>
 *       <td>Stack</td>
 *       <td>Stack</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation on the values in each matching pair of wells in each matching plate in the stack</td>
 *    </tr>
 *    <tr>
 *       <td>Stack</td>
 *       <td>Array</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the array and the values in each well of each plate in the stack</td>
 *    </tr>
 *    <tr>
 *       <td>Stack</td>
 *       <td>Collection</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the values in the collection and the values in each well of each plate in the stack</td>
 *    </tr>
 *    <tr>
 *       <td>Stack</td>
 *       <td>Constant</td>
 *       <td>+/-</td>
 *       <td>+/-</td>
 *       <td>Performs the operation using the constant and each value in each well of each plate in the stack</td>
 *    </tr>
 * </table>
 * 
 * MicroFlex currently supports the following binary mathematical operations 
 * for BigDecimal objects:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Operations<div></th>
 *    <tr>
 *       <td>Addition</td> 
 *    </tr>
 *    <tr>
 *       <td>Subtraction</td>
 *    </tr>
 *    <tr>
 *       <td>Multiplication</td>
 *    </tr>
 *    <tr>
 *       <td>Division</td>
 *    </tr>
 *    <tr>
 *       <td>Modulus</td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class MathOperationBigDecimalBinary {

	/* ---------------------------- Constructors ---------------------------- */
	
	/**
	 * Creates a new math operation.
	 */
	public MathOperationBigDecimalBinary() {}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     * @return                     result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well1, WellBigDecimal well2) {
    	this.validateArgs(well1, well2);
    	return calculate(well1.data(), well2.data());
    }
    
    /**
     * Returns the result of the mathematical operation using the data between
     * the indices. Missing data points due to uneven data set lengths are treated as 
     * zeroes.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     * @return                     result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well1, WellBigDecimal well2, int begin, int length) {
    	this.validateArgs(well1, well2, begin, length);
    	return calculate(well1.data(), well2.data(), begin, length);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points 
     * due to uneven data set lengths are omitted.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     * @return                     result of the operation
     */
    public List<BigDecimal> wellsStrict(WellBigDecimal well1, WellBigDecimal well2) {
    	this.validateArgs(well1, well2);
    	return calculateStrict(well1.data(), well2.data());
    }
    
    /**
     * Returns the result of the mathematical operation using the data between
     * the indices. Missing data points due to uneven data set lengths are omitted.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     * @return                     result of the operation
     */
    public List<BigDecimal> wellsStrict(WellBigDecimal well1, WellBigDecimal well2, int begin, int length) {
    	this.validateArgs(well1, well2, begin, length);
    	return calculateStrict(well1.data(), well2.data(), begin, length);
    }
    
    /**
     * Returns the result of the mathematical operation.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal        constant for operation
     * @return                     result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well, BigDecimal constant) {
    	this.validateArgs(well);
    	return this.calculate(well.data(), constant);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array for the operation
     * @return                     result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well, BigDecimal[] array) {
    	this.validateArgs(well, array);
    	return this.calculate(well.data(), array);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array for the operation
     * @param    int               beginning index of subset
     * @param    int               length of the subset
     * @return                     result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well, BigDecimal[] array, int begin, int length) {
    	this.validateArgs(well, array, begin, length);
    	return this.calculate(well.data(), array, begin, length);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellBigDecimal            the well
     * @param    Collection<BigDecimal>    collection for the operation
     * @return                             result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well, Collection<BigDecimal> collection) {
    	this.validateArgs(well, collection);
    	return this.calculate(well.data(), collection);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellBigDecimal            the well
     * @param    Collection<BigDecimal>    collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public List<BigDecimal> wells(WellBigDecimal well, Collection<BigDecimal> collection, int begin, int length) {
    	this.validateArgs(well, collection, begin, length);
    	return this.calculate(well.data(), collection, begin, length);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array for the operation
     * @return                     result of the operation
     */
    public List<BigDecimal> wellsStrict(WellBigDecimal well, BigDecimal[] array) {
    	this.validateArgs(well, array);
    	return this.calculateStrict(well.data(), array);
    	
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array for the operation
     * @param    int               beginning index of subset
     * @param    int               length of the subset
     * @return                     result of the operation
     */
    public List<BigDecimal> wellsStrict(WellBigDecimal well, BigDecimal[] array, int begin, int length) {
    	this.validateArgs(well, array, begin, length);
    	return this.calculateStrict(well.data(), array, begin, length);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellBigDecimal            the well
     * @param    Collection<BigDecimal>    collection for the operation
     * @return                             result of the operation
     */
    public List<BigDecimal> wellsStrict(WellBigDecimal well, Collection<BigDecimal> collection) {
    	this.validateArgs(well, collection);
    	return this.calculateStrict(well.data(), collection);
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellBigDecimal            the well
     * @param    Collection<BigDecimal>    collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public List<BigDecimal> wellsStrict(WellBigDecimal well, Collection<BigDecimal> collection, int begin, int length) {
    	this.validateArgs(well, collection, begin, length);
    	return this.calculateStrict(well.data(), collection, begin, length);
    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to unequal data set lengths are treated as zero values.
     * @param    PlateBigDecimal    the first plate
     * @param    PlateBigDecimal    the second plate
     * @return                      result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate1, PlateBigDecimal plate2) {
    	
    	this.validateArgs(plate1, plate2);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate1.rows(), plate1.columns());
    	
    	for(WellSetBigDecimal set : plate1.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	for(WellSetBigDecimal set : plate2.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate1.dataSet(), plate2.dataSet());
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices. Missing data points due to data sets of unequal length are 
     * treated as zero values.
     * @param    PlateBigDecimal    the first plate
     * @param    PlateBigDecimal    the second plate
     * @param    int                beginning index of the subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate1, PlateBigDecimal plate2, int begin, int length) {
    	
        this.validateArgs(plate1, plate2);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate1.rows(), plate1.columns());
    	
    	for(WellSetBigDecimal set : plate1.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	for(WellSetBigDecimal set : plate2.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate1.dataSet(), plate2.dataSet(), begin, length);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to unequal data set lengths are omitted.
     * @param    PlateBigDecimal    the first plate
     * @param    PlateBigDecimal    the second plate
     * @return                      result of the operation
     */
    public PlateBigDecimal platesStrict(PlateBigDecimal plate1, PlateBigDecimal plate2) {

    	this.validateArgs(plate1, plate2);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate1.rows(), plate1.columns());
    	
    	for(WellSetBigDecimal set : plate1.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	for(WellSetBigDecimal set : plate2.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.setsStrict(plate1.dataSet(), plate2.dataSet());
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices. Missing data points due to unequal data set lengths are omitted.
     * @param    PlateBigDecimal    the first plate
     * @param    PlateBigDecimal    the second plate
     * @param    int                beginning index of the subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public PlateBigDecimal platesStrict(PlateBigDecimal plate1, PlateBigDecimal plate2, int begin, int length) {

    	this.validateArgs(plate1, plate2);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate1.rows(), plate1.columns());
    	
    	Set<WellSetBigDecimal> groups1 = plate1.allGroups();
    	Set<WellSetBigDecimal> groups2 = plate2.allGroups();
    	
    	groups1.retainAll(groups2);
    	    	
    	for(WellSetBigDecimal set : groups1) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.setsStrict(plate1.dataSet(), plate2.dataSet(), begin, length);

    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation.
     * @param    PlateBigDecimal    the plate
     * @param    BigDecimal         constant for operation
     * @return                      result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate, BigDecimal constant) {
    	
        this.validateArgs(plate);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
  	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate.dataSet(), constant);

    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    PlateBigDecimal    the plate
     * @param    BigDecimal[]       array for the operation
     * @return                      result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate, BigDecimal[] array) {

    	this.validateArgs(plate, array);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate.dataSet(), array);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    PlateBigDecimal    the plate
     * @param    BigDecimal[]       array for the operation
     * @param    int                beginning index of subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate, BigDecimal[] array, int begin, int length) {
    	
        this.validateArgs(plate, array);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate.dataSet(), array, begin, length);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    PlateBigDecimal           the plate
     * @param    Collection<BigDecimal>   collection for the operation
     * @return                            result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate, Collection<BigDecimal> collection) {

    	this.validateArgs(plate, collection);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate.dataSet(), collection);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    PlateBigDecimal           the plate
     * @param    Collection<BigDecimal>    collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public PlateBigDecimal plates(PlateBigDecimal plate, Collection<BigDecimal> collection, int begin, int length) {
    	
        this.validateArgs(plate, collection);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.sets(plate.dataSet(), collection, begin, length);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    PlateBigDecimal    the plate
     * @param    BigDecimal[]       array for the operation
     * @return                      result of the operation
     */
    public PlateBigDecimal platesStrict(PlateBigDecimal plate, BigDecimal[] array) {
    	
        this.validateArgs(plate, array);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.setsStrict(plate.dataSet(), array);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    PlateBigDecimal    the plate
     * @param    BigDecimal[]       array for the operation
     * @param    int                beginning index of subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public PlateBigDecimal platesStrict(PlateBigDecimal plate, BigDecimal[] array, int begin, int length) {
    	
        this.validateArgs(plate, array);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.setsStrict(plate.dataSet(), array, begin, length);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    PlateBigDecimal           the plate
     * @param    Collection<BigDecimal>    collection for the operation
     * @return                             result of the operation
     */
    public PlateBigDecimal platesStrict(PlateBigDecimal plate, Collection<BigDecimal> collection) {
    	
        this.validateArgs(plate, collection);
    	
    	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
    	
    	for(WellSetBigDecimal set : plate.allGroups()) {
    		result.addGroups(set.wellList());
    	}
    	
    	WellSetBigDecimal resultSet = this.setsStrict(plate.dataSet(), collection);
    	
    	result.addWells(resultSet);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    PlateBigDecimal           the plate
     * @param    Collection<BigDecimal>    collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public PlateBigDecimal platesStrict(PlateBigDecimal plate, Collection<BigDecimal> collection, int begin, int length) {
    	
    	this.validateArgs(plate, collection);
     	
     	PlateBigDecimal result = new PlateBigDecimal(plate.rows(), plate.columns());
     	
     	for(WellSetBigDecimal set : plate.allGroups()) {
     		result.addGroups(set.wellList());
     	}
     	
     	WellSetBigDecimal resultSet = this.setsStrict(plate.dataSet(), collection, begin, length);
     	
     	result.addWells(resultSet);
     	
     	return result;
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to unequal data set lengths are treated as zero values.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    the second set
     * @return                        result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set1, WellSetBigDecimal set2) {
    	
        this.validateArgs(set1, set2);
    	
        WellSetBigDecimal result = new WellSetBigDecimal();
    	
        WellSetBigDecimal clone1 = new WellSetBigDecimal(set1);
        WellSetBigDecimal clone2 = new WellSetBigDecimal(set2);
        
        WellSetBigDecimal set1Excluded = new WellSetBigDecimal(set1);
        WellSetBigDecimal set2Excluded = new WellSetBigDecimal(set2);
        
        set1Excluded.remove(set2);
        set2Excluded.remove(set1);
        clone1.retain(set2);
        clone2.retain(set1);
        
        Iterator<WellBigDecimal> iter1 = clone1.iterator();
        Iterator<WellBigDecimal> iter2 = clone2.iterator();
        
    	while(iter1.hasNext()) {
    		
    		WellBigDecimal well1 = iter1.next();
    		WellBigDecimal well2 = iter2.next();
    		
    		List<BigDecimal> list = this.calculate(well1.data(), well2.data());
    		result.add(new WellBigDecimal(well1.row(), well1.column(), list));

    	}
    	
    	result.add(set1Excluded);
    	result.add(set2Excluded);

    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices. Missing data points due to data sets of unequal length are 
     * treated as zero values.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    the second set
     * @param    int                  beginning index of the subset
     * @param    int                  length of the subset
     * @return                        result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set1, WellSetBigDecimal set2, int begin, int length) {

        this.validateArgs(set1, set2);
    	
        WellSetBigDecimal result = new WellSetBigDecimal();
    	
        WellSetBigDecimal clone1 = new WellSetBigDecimal(set1);
        WellSetBigDecimal clone2 = new WellSetBigDecimal(set2);
        
        WellSetBigDecimal set1Excluded = new WellSetBigDecimal(set1);
        WellSetBigDecimal set2Excluded = new WellSetBigDecimal(set2);
        
        set1Excluded.remove(set2);
        set2Excluded.remove(set1);
        clone1.retain(set2);
        clone2.retain(set1);
        
        Iterator<WellBigDecimal> iter1 = clone1.iterator();
        Iterator<WellBigDecimal> iter2 = clone2.iterator();
        
    	while(iter1.hasNext()) {
    		
    		WellBigDecimal well1 = iter1.next();
    		WellBigDecimal well2 = iter2.next();
    		
    		validateArgs(well1, begin, length);
    		validateArgs(well2, begin, length);
    			
    		List<BigDecimal> list = this.calculate(well1.data(), well2.data(), begin, length);
    		result.add(new WellBigDecimal(well1.row(), well1.column(), list));

    	}
    	
    	for(WellBigDecimal well : set1Excluded) {   	
    		well = well.subList(begin, length);
    	}
    	
    	for(WellBigDecimal well : set2Excluded) {
    		well = well.subList(begin, length);
    	}
    	
    	result.add(set1Excluded);
    	result.add(set2Excluded);
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to unequal data set lengths are omitted.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    the second set
     * @return                        result of the operation
     */
    public WellSetBigDecimal setsStrict(WellSetBigDecimal set1, WellSetBigDecimal set2) {
    	
    	this.validateArgs(set1, set2);
    	
        WellSetBigDecimal result = new WellSetBigDecimal();
    	
        WellSetBigDecimal clone1 = new WellSetBigDecimal(set1);
        WellSetBigDecimal clone2 = new WellSetBigDecimal(set2);
        
        WellSetBigDecimal set1Excluded = new WellSetBigDecimal(clone1);
        WellSetBigDecimal set2Excluded = new WellSetBigDecimal(clone2);
        
        set1Excluded.remove(clone2);
        set2Excluded.remove(clone1);
        clone1.retain(set2);
        clone2.retain(set1);
        
        Iterator<WellBigDecimal> iter1 = clone1.iterator();
        Iterator<WellBigDecimal> iter2 = clone2.iterator();
        
    	while(iter1.hasNext()) {
    		
    		WellBigDecimal well1 = iter1.next();
    		WellBigDecimal well2 = iter2.next();
    		
    		validateArgs(well1, well2);
    			
    		List<BigDecimal> list = this.calculateStrict(well1.data(), well2.data());
    		result.add(new WellBigDecimal(well1.row(), well1.column(), list));

    	}

    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices. Missing data points due to unequal data set lengths are omitted.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    the second set
     * @param    int                  beginning index of the subset
     * @param    int                  length of the subset
     * @return                        result of the operation
     */
    public WellSetBigDecimal setsStrict(WellSetBigDecimal set1, WellSetBigDecimal set2, int begin, int length) {
    	

        this.validateArgs(set1, set2);
    	
        WellSetBigDecimal result = new WellSetBigDecimal();
    	
        WellSetBigDecimal clone1 = new WellSetBigDecimal(set1);
        WellSetBigDecimal clone2 = new WellSetBigDecimal(set2);

        clone1.retain(set2);
        clone2.retain(set1);
        
        Iterator<WellBigDecimal> iter1 = clone1.iterator();
        Iterator<WellBigDecimal> iter2 = clone2.iterator();
        
    	while(iter1.hasNext()) {
    		
    		WellBigDecimal well1 = iter1.next();
    		WellBigDecimal well2 = iter2.next();
    		
    		validateArgs(well1, begin, length);
    		validateArgs(well2, begin, length);
    			
    		List<BigDecimal> list = this.calculateStrict(well1.data(), well2.data(), begin, length);
    		result.add(new WellBigDecimal(well1.row(), well1.column(), list));

    	}

    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation.
     * @param    WellSetBigDecimal    the well set
     * @param    BigDecimal           constant for operation
     * @return                        result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set, BigDecimal constant) {
    	
    	this.validateArgs(set);
    	
    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    
    	for(WellBigDecimal well : set) {
    		result.add(new WellBigDecimal(well.row(), well.column(), 
    				this.calculate(well.data(), constant)));
    	} 
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellSetBigDecimal    the well set
     * @param    BigDecimal[]         array for the operation
     * @return                        result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set, BigDecimal[] array) {
    	
        this.validateArgs(set, array);
    	
        WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
    	for(WellBigDecimal well : set) {
    		result.add(new WellBigDecimal(well.row(), well.column(), 
    				this.calculate(well.data(), array)));
    	} 
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellSetBigDecimal    the well set
     * @param    BigDecimal[]         array for the operation
     * @param    int                  beginning index of subset
     * @param    int                  length of the subset
     * @return                        result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set, BigDecimal[] array, int begin, int length) {
    	
    	this.validateArgs(set, array);
    	
    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
    	for(WellBigDecimal well : set) {
    		this.validateArgs(well, begin, length);
    		result.add(new WellBigDecimal(well.row(), well.column(), 
    				this.calculate(well.data(), array, begin, length)));
    	} 
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellSetBigDecimal         the well set
     * @param    Collection<BigDecimal>     collection for the operation
     * @return                              result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set, Collection<BigDecimal> collection) {
    	
    	this.validateArgs(set, collection);
    	
    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
    	for(WellBigDecimal well : set) {
    		result.add(new WellBigDecimal(well.row(), well.column(), this.calculate(well.data(), collection)));
         } 
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are treated as zeroes.
     * @param    WellSetBigDecimal         the well set
     * @param    Collection<BigDecimal>      collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public WellSetBigDecimal sets(WellSetBigDecimal set, Collection<BigDecimal> collection, int begin, int length) {
    	
    	this.validateArgs(set, collection);
    	
    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
    	for(WellBigDecimal well : set) {
    		this.validateArgs(well, begin, length);
    		result.add(new WellBigDecimal(well.row(), well.column(), 
    				this.calculate(well.data(), collection, begin, length)));
    	} 
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellSetBigDecimal    the well set
     * @param    BigDecimal[]         array for the operation
     * @return                        result of the operation
     */
    public WellSetBigDecimal setsStrict(WellSetBigDecimal set, BigDecimal[] array) {
    	
    	this.validateArgs(set, array);

    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
        for(WellBigDecimal well : set) {
        	result.add(new WellBigDecimal(well.row(), well.column(), this.calculateStrict(well.data(), array)));
        }
        
        return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellSetBigDecimal    the well set
     * @param    BigDecimal[]         array for the operation
     * @param    int                  beginning index of subset
     * @param    int                  length of the subset
     * @return                        result of the operation
     */
    public WellSetBigDecimal setsStrict(WellSetBigDecimal set, BigDecimal[] array, int begin, int length) {
    	
    	this.validateArgs(set, array);

    	WellSetBigDecimal result = new WellSetBigDecimal();
        result.setLabel(set.label());
    	
        for(WellBigDecimal well : set) {
        	this.validateArgs(well, begin, length);
        	result.add(new WellBigDecimal(well.row(), well.column(), this.calculateStrict(well.data(), array, begin, length)));
        }
        
        return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellSetBigDecimal         the well set
     * @param    Collection<BigDecimal>      collection for the operation
     * @return                               result of the operation
     */
    public WellSetBigDecimal setsStrict(WellSetBigDecimal set, Collection<BigDecimal> collection) {
    	
    	this.validateArgs(set, collection);

    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
        for(WellBigDecimal well : set) {
        	result.add(new WellBigDecimal(well.row(), well.column(), this.calculateStrict(well.data(), collection)));
        }
        
        return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data set lengths are omitted.
     * @param    WellSetBigDecimal         the well set
     * @param    Collection<BigDecimal>      collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public WellSetBigDecimal setsStrict(WellSetBigDecimal set, Collection<BigDecimal> collection, int begin, int length) {
    	
        this.validateArgs(set, collection);

    	WellSetBigDecimal result = new WellSetBigDecimal();
    	result.setLabel(set.label());
    	
        for(WellBigDecimal well : set) {
        	this.validateArgs(well, begin, length);
        	result.add(new WellBigDecimal(well.row(), well.column(), this.calculateStrict(well.data(), collection, begin, length)));
        }
        
        return result;
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to data sets of unequal length are treated as zero values.
     * @param    StackBigDecimal    the first stack
     * @param    StackBigDecimal    the second stack
     * @return                      the result of the operation
     */
    public StackBigDecimal stacks(StackBigDecimal stack1, StackBigDecimal stack2) {
    	
        this.validateArgs(stack1, stack2);
    	
    	StackBigDecimal result = new StackBigDecimal(stack1.rows(), stack2.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack1.iterator();
    	Iterator<PlateBigDecimal> iter2 = stack2.iterator();
    	
    	while(iter1.hasNext() && iter2.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), iter2.next());
    	    result.add(resultPlate);
    	}

    	while(iter1.hasNext()) {
    		result.add(iter1.next());
    	}
    	
    	while(iter2.hasNext()) {
    		result.add(iter2.next());
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices. Missing data points due to data sets of unequal length are
     * treated as zero values.
     * @param    StackBigDecimal    the first stack
     * @param    StackBigDecimal    the second stack
     * @param    int                beginning index of the subset
     * @param    int                length of the subset
     * @return                      the result of the addition
     */
    public StackBigDecimal stacks(StackBigDecimal stack1, StackBigDecimal stack2, int begin, int length) {

        this.validateArgs(stack1, stack2);
    	
    	StackBigDecimal result = new StackBigDecimal(stack1.rows(), stack2.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack1.iterator();
    	Iterator<PlateBigDecimal> iter2 = stack2.iterator();
    	
    	while(iter1.hasNext() && iter2.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), iter2.next(), begin, length);
    	    result.add(resultPlate);
    	}

    	while(iter1.hasNext()) {
    		
    		PlateBigDecimal plate = iter1.next();
    		
    		for(WellBigDecimal well : plate) {
    			well = well.subList(begin, length);
    		}
    		
    		result.add(iter1.next());
    	}
    	
    	while(iter2.hasNext()) {

            PlateBigDecimal plate = iter2.next();
    		
    		for(WellBigDecimal well : plate) {
    			well = well.subList(begin, length);
    		}
    		
    		result.add(iter2.next());
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to data sets of unequal length are omitted.
     * @param    StackBigDecimal    the first stack
     * @param    StackBigDecimal    the second stack
     * @return                      the result of the operation
     */
    public StackBigDecimal stacksStrict(StackBigDecimal stack1, StackBigDecimal stack2) {

        this.validateArgs(stack1, stack2);
    	
    	StackBigDecimal result = new StackBigDecimal(stack1.rows(), stack2.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack1.iterator();
    	Iterator<PlateBigDecimal> iter2 = stack2.iterator();
    	
    	while(iter1.hasNext() && iter2.hasNext()) {
    	    PlateBigDecimal resultPlate = this.platesStrict(iter1.next(), iter2.next());
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices. Missing data points due to data sets of unequal length are 
     * omitted.
     * @param    StackBigDecimal    the first stack
     * @param    StackBigDecimal    the second stack
     * @param    int                beginning index of the subset
     * @param    int                length of the subset
     * @return                      the result of the addition
     */
    public StackBigDecimal stacksStrict(StackBigDecimal stack1, StackBigDecimal stack2, int begin, int length) {
    	
        this.validateArgs(stack1, stack2);
    	
    	StackBigDecimal result = new StackBigDecimal(stack1.rows(), stack2.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack1.iterator();
    	Iterator<PlateBigDecimal> iter2 = stack2.iterator();
    	
    	while(iter1.hasNext() && iter2.hasNext()) {
    	    PlateBigDecimal resultPlate = this.platesStrict(iter1.next(), iter2.next(), begin, length);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation.
     * @param    StackBigDecimal    the stack
     * @param    BigDecimal         constant for operation
     * @return                      result of the operation
     */
    public StackBigDecimal stacks(StackBigDecimal stack, BigDecimal constant) {
        
        this.validateArgs(stack);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), constant);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are treated as zeroes.
     * @param    StackBigDecimal    the stack
     * @param    BigDecimal[]       array for the operation
     * @return                      result of the operation
     */
    public StackBigDecimal stacks(StackBigDecimal stack, BigDecimal[] array) {
    
    	this.validateArgs(stack, array);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), array);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are treated as zeroes.
     * @param    StackBigDecimal    the stack
     * @param    BigDecimal[]       array for the operation
     * @param    int                beginning index of subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public StackBigDecimal stacks(StackBigDecimal stack, BigDecimal[] array, int begin, int length) {
    	
    	this.validateArgs(stack, array);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), array, begin, length);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are treated as zeroes.
     * @param    StackBigDecimal           the stack
     * @param    Collection<BigDecimal>    collection for the operation
     * @return                             result of the operation
     */
    public StackBigDecimal stacks(StackBigDecimal stack, Collection<BigDecimal> collection) {
    	
    	this.validateArgs(stack, collection);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), collection);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are treated as zeroes.
     * @param    StackBigDecimal           the stack
     * @param    Collection<BigDecimal>    collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public StackBigDecimal stacks(StackBigDecimal stack, Collection<BigDecimal> collection, int begin, int length) {

    	this.validateArgs(stack, collection);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.plates(iter1.next(), collection, begin, length);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are omitted.
     * @param    StackBigDecimal    the stack
     * @param    BigDecimal[]       array for the operation
     * @return                      result of the operation
     */
    public StackBigDecimal stacksStrict(StackBigDecimal stack, BigDecimal[] array) {

    	this.validateArgs(stack);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.platesStrict(iter1.next(), array);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are omitted.
     * @param    StackBigDecimal    the stack
     * @param    BigDecimal[]       array for the operation
     * @param    int                beginning index of subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public StackBigDecimal stacksStrict(StackBigDecimal stack, BigDecimal[] array, int begin, int length) {

    	this.validateArgs(stack);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.platesStrict(iter1.next(), array, begin, length);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are omitted.
     * @param    StackBigDecimal           the stack
     * @param    Collection<BigDecimal>    collection for the operation
     * @return                             result of the operation
     */
    public StackBigDecimal stacksStrict(StackBigDecimal stack, Collection<BigDecimal> collection) {

    	this.validateArgs(stack);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.platesStrict(iter1.next(), collection);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation. Missing data points due 
     * to uneven data stack lengths are omitted.
     * @param    StackBigDecimal           the stack
     * @param    Collection<BigDecimal>    collection for the operation
     * @param    int                       beginning index of subset
     * @param    int                       length of the subset
     * @return                             result of the operation
     */
    public StackBigDecimal stacksStrict(StackBigDecimal stack, Collection<BigDecimal> collection, int begin, int length) {

    	this.validateArgs(stack);
    	
    	StackBigDecimal result = new StackBigDecimal(stack.rows(), stack.columns());
    	
    	Iterator<PlateBigDecimal> iter1 = stack.iterator();
    	
    	while(iter1.hasNext()) {
    	    PlateBigDecimal resultPlate = this.platesStrict(iter1.next(), collection, begin, length);
    	    result.add(resultPlate);
    	}
    	
    	return result;
    }
    
    /*---------------- Methods for Validating Well Arguments -----------------*/
    
    /**
     * Validates well arguments.
     * @param    WellBigDecimal    the well
     */
    private void validateArgs(WellBigDecimal well) {
    	if(well == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /**
     * Validates well arguments.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     */
    private void validateArgs(WellBigDecimal well1, WellBigDecimal well2) {
    	
    	if(well1 == null || well2 == null) {
    		throw new NullPointerException("Null argument.");
    	}
     
    }
    
    /**
     * Validates well and index arguments.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     */
    private void validateArgs(WellBigDecimal well1, int begin, int length) {

    	this.validateArgs(well1);
        
        if(well1.size() < begin + length) {
        	throw new IllegalArgumentException("Invalid indices.");
        }
    }
    
    /**
     * Validates well and index arguments.
     * @param    WellBigDecimal    the first well
     * @param    WellBigDecimal    the second well
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     */
    private void validateArgs(WellBigDecimal well1, WellBigDecimal well2, int begin, int length) {
        
    	this.validateArgs(well1, well2);
        
        if(well1.size() < begin + length && well2.size() < begin + length) {
        	throw new IllegalArgumentException("Invalid indices.");
        }
    }
    
    /**
     * Validates well arguments.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array of values
     */
    private void validateArgs(WellBigDecimal well, BigDecimal[] array) {
        
    	if(well == null || array == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /**
     * Validates well and index arguments.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array of values
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     */
    private void validateArgs(WellBigDecimal well, BigDecimal[] array, int begin, int length) {
    
    	this.validateArgs(well, array);
        
        if(well.size() < begin + length) {
        	throw new IllegalArgumentException("Invalid indices.");
        }
    }
    
    /**
     * Validates well arguments.
     * @param    WellBigDecimal            the well
     * @param    Collection<BigDecimal>    collection of values
     */
    private void validateArgs(WellBigDecimal well, Collection<BigDecimal> collection) {
    	
    	if(well == null || collection == null) {
    		throw new NullPointerException("Null argument.");
    	}
    	
    }
    
    /**
     * Validates well and index arguments.
     * @param    WellBigDecimal    the well
     * @param    BigDecimal[]      array of values
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     */
    private void validateArgs(WellBigDecimal well, Collection<BigDecimal> collection, int begin, int length) {
        
        this.validateArgs(well, collection);
        
        if(well.size() < begin + length) {
        	throw new IllegalArgumentException("Invalid indices.");
        }
        
    }
    
    /*-------------- Methods for Validating Well Set Arguments ---------------*/
    
    /**
     * Validates well set arguments.
     * @param    WellSetBigDecimal    the set
     */
    private void validateArgs(WellSetBigDecimal set) {
    	
    	if(set == null) {
    		throw new NullPointerException("Null argument.");
    	}
     
    }
    
    /**
     * Validates well set arguments.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    the second set
     */
    private void validateArgs(WellSetBigDecimal set1, WellSetBigDecimal set2) {
    	
    	if(set1 == null || set2 == null) {
    		throw new NullPointerException("Null argument.");
    	}
     
    }
    
    /**
     * Validates set arguments.
     * @param    WellSetBigDecimal    the set
     * @param    BigDecimal[]         array of values
     */
    private void validateArgs(WellSetBigDecimal set, BigDecimal[] array) {
        
    	if(set == null || array == null) {
    		throw new NullPointerException("Null argument.");
    	}
    	
    }
    
    /**
     * Validates set arguments.
     * @param    WellSetBigDecimal         the set
     * @param    Collection<BigDecimal>    collection of values
     */
    private void validateArgs(WellSetBigDecimal set, Collection<BigDecimal> collection) {
    	
    	if(set == null || collection == null) {
    		throw new NullPointerException("Null argument.");
    	}
    	
    }
    
    /*---------------- Methods for Validating Plate Arguments ----------------*/
    
    /**
     * Validates plate arguments.
     * @param    PlateBigDecimal    the plate
     */
    private void validateArgs(PlateBigDecimal  plate) {
    	if(plate == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /**
     * Validates plate arguments.
     * @param    PlateBigDecimal    the first plate
     * @param    PlateBigDecimal    the second plate
     */
    private void validateArgs(PlateBigDecimal  plate1, PlateBigDecimal  plate2) {
    	
    	if(plate1 == null || plate2 == null) {
    		throw new NullPointerException("Null argument.");
    	}
    	
    	if(plate1.rows() != plate2.rows() || plate1.columns() != plate2.columns()) {
    		throw new IllegalArgumentException("Unequal plate dimensios.");
    	}
     
    }
    
    /**
     * Validates plate arguments.
     * @param    PlateBigDecimal    the plate
     * @param    BigDecimal[]       array of values
     */
    private void validateArgs(PlateBigDecimal  plate, BigDecimal[] array) {
    	if(plate == null || array == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /**
     * Validates plate arguments.
     * @param    PlateBigDecimal           the plate
     * @param    Collection<BigDecimal>    collection of values
     */
    private void validateArgs(PlateBigDecimal  plate, Collection<BigDecimal> collection) {
    	if(plate == null || collection == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /*---------------- Methods for Validating Stack Arguments ----------------*/
    
    /**
     * Validates stack arguments.
     * @param    StackBigDecimal    the stack
     */
    private void validateArgs(StackBigDecimal stack) {
    	if(stack == null) {
    		throw new NullPointerException("Null argument.");
    	}   
    }
    
    /**
     * Validates stack arguments.
     * @param    StackBigDecimal    the first stack
     * @param    StackBigDecimal    the second stack
     */
    private void validateArgs(StackBigDecimal  stack1, StackBigDecimal  stack2) {
    	if(stack1 == null || stack2 == null) {
    		throw new NullPointerException("Null argument.");
    	}    
    }
    
    /**
     * Validates stack arguments.
     * @param    StackBigDecimal    the stack
     * @param    BigDecimal[]       array of values
     */
    private void validateArgs(StackBigDecimal  stack, BigDecimal[] array) { 
    	if(stack == null || array == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /**
     * Validates stack arguments.
     * @param    StackBigDecimal           the stack
     * @param    Collection<BigDecimal>    collection of values
     */
    private void validateArgs(StackBigDecimal  stack, Collection<BigDecimal> collection) {
    	if(stack == null || collection == null) {
    		throw new NullPointerException("Null argument.");
    	}
    }
    
    /*------------------------ List Operation Methods ------------------------*/
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are treated as zero values.
     * @param    List<BigDecimal>    the first list
     * @param    List<BigDecimal>    the second list
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list1, List<BigDecimal> list2);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are omitted.
     * @param    List<BigDecimal>    the first list
     * @param    List<BigDecimal>    the second list
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculateStrict(List<BigDecimal> list1, List<BigDecimal> list2);

    /**
     * Performs the mathematical operation for the two lists using the values
     * between the indices. Missing data points due to data sets of unequal 
     * length are treated as zero values.
     * @param    List<BigDecimal>    the first list
     * @param    List<BigDecimal>    the second list
     * @param    int                 the beginning index of the subset
     * @param    int                 the length of the subset
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list1, List<BigDecimal> list2, int begin, int length);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are omitted.
     * @param    List<BigDecimal>    the first list
     * @param    List<BigDecimal>    the second list
     * @param    int                 the beginning index of the subset
     * @param    int                 the length of the subset
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculateStrict(List<BigDecimal> list1, List<BigDecimal> list2, int begin, int length);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are treated as zero values.
     * @param    List<BigDecimal>    the first list
     * @param    BigDecimal          the constant value
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, BigDecimal constant);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are treated as zero values.
     * @param    List<BigDecimal>    the first list
     * @param    BigDecimal[]        the array values
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, BigDecimal[] array);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are treated as zero values.
     * @param    List<BigDecimal>    the first list
     * @param    BigDecimal[]        the array values
     * @param    int                 beginning index of the subset
     * @param    int                 length of the subset
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, BigDecimal[] array, int begin, int length);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are treated as zero values.
     * @param    List<BigDecimal>          the first list
     * @param    Collection<BigDecimal>    the array values
     * @return                             result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, Collection<BigDecimal> collection);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are treated as zero values.
     * @param    List<BigDecimal>          the first list
     * @param    Collection<BigDecimal>    the array values
     * @param    int                       beginning index of the subset
     * @param    int                       length of the subset
     * @return                             result of the mathematical operation
     */
    public abstract List<BigDecimal> calculate(List<BigDecimal> list, Collection<BigDecimal> collection, int begin, int length);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are omitted.
     * @param    List<BigDecimal>    the first list
     * @param    BigDecimal[]        the array values
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculateStrict(List<BigDecimal> list, BigDecimal[] array);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are omitted.
     * @param    List<BigDecimal>    the first list
     * @param    BigDecimal[]        the array values
     * @param    int                 beginning index of the subset
     * @param    int                 length of the subset
     * @return                       result of the mathematical operation
     */
    public abstract List<BigDecimal> calculateStrict(List<BigDecimal> list, BigDecimal[] array, int begin, int length);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are omitted.
     * @param    List<BigDecimal>          the first list
     * @param    Collection<BigDecimal>    the array values
     * @return                             result of the mathematical operation
     */
    public abstract List<BigDecimal> calculateStrict(List<BigDecimal> list, Collection<BigDecimal> collection);
    
    /**
     * Performs the mathematical operation for the two lists. Missing data points 
     * due to data sets of unequal length are omitted.
     * @param    List<BigDecimal>          the first list
     * @param    Collection<BigDecimal>    the array values
     * @param    int                       beginning index of the subset
     * @param    int                       length of the subset
     * @return                             result of the mathematical operation
     */
    public abstract List<BigDecimal> calculateStrict(List<BigDecimal> list, Collection<BigDecimal> collection, int begin, int length);
}
