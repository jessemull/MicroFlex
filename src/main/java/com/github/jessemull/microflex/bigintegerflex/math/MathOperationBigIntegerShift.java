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

package com.github.jessemull.microflex.bigintegerflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigInteger;
import java.util.List;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * This class performs mathematical shift operations for BigInteger plate stacks, 
 * plates, wells and well sets. To create a custom shift operation extend this 
 * class and override the calculate methods using the appropriate operation. 
 * Shift operations can also be performed on a subset of data using a beginning 
 * index and subset length. 
 * <br><br>
 * MicroFlex currently supports the following shift mathematical operations 
 * for BigInteger objects:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Operations<div></th>
 *    <tr>
 *       <td>Left Shift</td> 
 *    </tr>
 *    <tr>
 *       <td>Right Shift (Arithmetic/Signed)</td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class MathOperationBigIntegerShift {

	/* ---------------------------- Constructors ---------------------------- */
	
	/**
	 * Creates a new math operation.
	 */
	public MathOperationBigIntegerShift() {}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Returns the result of the mathematical operation.
     * @param    WellBigInteger    the well
     * @param    int               number of bits to shift
     * @return                     result of the operation
     */
    public List<BigInteger> wells(WellBigInteger well, int n) {
    	
    	if(well == null) {
    		throw new NullPointerException("Well is null.");
    	}
    	
    	return calculate(well.data(), n);
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices.
     * @param    WellBigInteger    the well
     * @param    int               number of bits to shift
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     * @return                     result of the operation
     */
    public List<BigInteger> wells(WellBigInteger well, int n, int begin, int length) {
    	
    	if(well == null) {
    		throw new NullPointerException("Well is null.");
    	}
    	
    	return calculate(well.data(), n, begin, length);
    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Returns the result of the mathematical operation.
     * @param    PlateBigInteger    the plate
     * @param    int                number of bits to shift
     * @return                      result of the operation
     */
    public PlateBigInteger plates(PlateBigInteger plate, int n) {
    	
    	if(plate == null) {
    		throw new NullPointerException("Plate is null.");
    	}
    	
    	PlateBigInteger result = new PlateBigInteger(plate.rows(), plate.columns());
    	
    	for(WellBigInteger well : plate) {
    		result.addWells(new WellBigInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n)));
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices.
     * @param    PlateBigInteger    the plate
     * @param    int                number of bits to shift
     * @param    int                beginning index of the subset
     * @param    int                length of the subset
     * @return                      result of the operation
     */
    public PlateBigInteger plates(PlateBigInteger plate, int n, int begin, int length) {
    	
    	if(plate == null) {
    		throw new NullPointerException("Plate is null.");
    	}
    	
    	PlateBigInteger result = new PlateBigInteger(plate.rows(), plate.columns());
    	
    	for(WellBigInteger well : plate) {
    		result.addWells(new WellBigInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n, begin, length)));
    	}
    	
    	return result;
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Returns the result of the mathematical operation.
     * @param    WellSetBigInteger well    the well set
     * @param    int                       number of bits to shift
     * @return                             result of the operation
     */
    public WellSetBigInteger sets(WellSetBigInteger set, int n) {
    	
    	if(set == null) {
    		throw new NullPointerException("Well set is null.");
    	}
    	
    	WellSetBigInteger result = new WellSetBigInteger();
    	
    	for(WellBigInteger well : set) {
    		result.add(new WellBigInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n)));
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices.
     * @param    WellSetBigInteger    the first plate
     * @param    int                  number of bits to shift
     * @param    int                  beginning index of the subset
     * @param    int                  length of the subset
     * @return                        result of the operation
     */
    public WellSetBigInteger sets(WellSetBigInteger set, int n, int begin, int length) {
    	
    	if(set == null) {
    		throw new NullPointerException("Well set is null.");
    	}
    	
    	WellSetBigInteger result = new WellSetBigInteger();
    	
    	for(WellBigInteger well : set) {
    		result.add(new WellBigInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n, begin, length)));
    	}
    	
    	return result;
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Returns the result of the mathematical operation.
     * @param    StackBigInteger    the stack
     * @param    int                number of bits to shift
     * @return                      the result of the operation
     */
     public StackBigInteger stacks(StackBigInteger stack, int n) {
    	
    	if(stack == null) {
    		throw new NullPointerException("Stack is null.");
    	}
    	
    	StackBigInteger result = new StackBigInteger(stack.rows(), stack.columns());
    	
    	for(PlateBigInteger plate : stack) {
    		result.add(this.plates(plate, n));
    	}
    	
    	return result;
    }
    
     /**
      * Returns the result of the mathematical operation.
      * @param    StackBigInteger    the stack
      * @param    int                number of bits to shift
      * @param    int                beginning index of the sub set
      * @param    int                length of the subset
      * @return                      the result of the operation
      */
     public StackBigInteger stacks(StackBigInteger stack, int n, int begin, int length) {
     	
        if(stack == null) {
             throw new NullPointerException("Stack is null.");
     	}
     	
     	StackBigInteger result = new StackBigInteger(stack.rows(), stack.columns());
     	
     	for(PlateBigInteger plate : stack) {
     		result.add(this.plates(plate, n, begin, length));
     	}
     	
     	return result;
     } 
     
    /*------------------------ List Operation Methods ------------------------*/
    
    /**
     * Performs the mathematical operation for the two lists.
     * @param    List<BigInteger>    the first list
     * @param    int                 number of bits to shift
     * @return                       result of the mathematical operation
     */
    public abstract List<BigInteger> calculate(List<BigInteger> list, int n);
    
    /**
     * Performs the mathematical operation for the two lists using the values
     * between the indices.
     * @param    List<BigInteger>    the first list
     * @param    int                 number of bits to shift
     * @return                       result of the mathematical operation
     */
    public abstract List<BigInteger> calculate(List<BigInteger> list, int n, int begin, int length);
}
