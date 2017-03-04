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

package com.github.jessemull.microflex.integerflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.util.List;

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;

/**
 * This class performs mathematical shift operations for integer plate stacks, 
 * plates, wells and well sets. To create a custom shift operation extend this 
 * class and override the calculate methods using the appropriate operation. 
 * Shift operations can also be performed on a subset of data using a beginning 
 * index and subset length. 
 * <br><br>
 * MicroFlex currently supports the following shift mathematical operations 
 * for integer values:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Operations<div></th>
 *    <tr>
 *       <td>Left Shift</td> 
 *    </tr>
 *    <tr>
 *       <td>Right Shift (Arithmetic/Signed)</td>
 *    </tr>
 *     <tr>
 *       <td>Right Shift (Logical/Unsigned)</td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public abstract class MathOperationIntegerShift {

	/* ---------------------------- Constructors ---------------------------- */
	
	/**
	 * Creates a new math operation.
	 */
	public MathOperationIntegerShift() {}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Returns the result of the mathematical operation.
     * @param    WellInteger    the well
     * @param    int            number of bits to shift
     * @return                  result of the operation
     */
    public List<Integer> wells(WellInteger well, int n) {
    	
    	if(well == null) {
    		throw new NullPointerException("Well is null.");
    	}
    	
    	return calculate(well.data(), n);
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices.
     * @param    WellInteger    the well
     * @param    int            number of bits to shift
     * @param    int            beginning index of the subset
     * @param    int            length of the subset
     * @return                  result of the operation
     */
    public List<Integer> wells(WellInteger well, int n, int begin, int length) {
    	
    	if(well == null) {
    		throw new NullPointerException("Well is null.");
    	}
    	
    	return calculate(well.data(), n, begin, length);
    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Returns the result of the mathematical operation.
     * @param    PlateInteger    the plate
     * @param    int             number of bits to shift
     * @return                   result of the operation
     */
    public PlateInteger plates(PlateInteger plate, int n) {
    	
    	if(plate == null) {
    		throw new NullPointerException("Plate is null.");
    	}
    	
    	PlateInteger result = new PlateInteger(plate.rows(), plate.columns());
    	
    	for(WellInteger well : plate) {
    		result.addWells(new WellInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n)));
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices.
     * @param    PlateInteger    the plate
     * @param    int             number of bits to shift
     * @param    int             beginning index of the subset
     * @param    int             length of the subset
     * @return                   result of the operation
     */
    public PlateInteger plates(PlateInteger plate, int n, int begin, int length) {
    	
    	if(plate == null) {
    		throw new NullPointerException("Plate is null.");
    	}
    	
    	PlateInteger result = new PlateInteger(plate.rows(), plate.columns());
    	
    	for(WellInteger well : plate) {
    		result.addWells(new WellInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n, begin, length)));
    	}
    	
    	return result;
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Returns the result of the mathematical operation.
     * @param    WellSetInteger well    the well set
     * @param    int                    number of bits to shift
     * @return                          result of the operation
     */
    public WellSetInteger sets(WellSetInteger set, int n) {
    	
    	if(set == null) {
    		throw new NullPointerException("Well set is null.");
    	}
    	
    	WellSetInteger result = new WellSetInteger();
    	
    	for(WellInteger well : set) {
    		result.add(new WellInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n)));
    	}
    	
    	return result;
    }
    
    /**
     * Returns the result of the mathematical operation using the values between
     * the indices.
     * @param    WellSetInteger    the first plate
     * @param    int               number of bits to shift
     * @param    int               beginning index of the subset
     * @param    int               length of the subset
     * @return                     result of the operation
     */
    public WellSetInteger sets(WellSetInteger set, int n, int begin, int length) {
    	
    	if(set == null) {
    		throw new NullPointerException("Well set is null.");
    	}
    	
    	WellSetInteger result = new WellSetInteger();
    	
    	for(WellInteger well : set) {
    		result.add(new WellInteger(well.row(), well.column(), 
    				this.calculate(well.data(), n, begin, length)));
    	}
    	
    	return result;
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Returns the result of the mathematical operation.
     * @param    StackInteger    the stack
     * @param    int             number of bits to shift
     * @return                   the result of the operation
     */
     public StackInteger stacks(StackInteger stack, int n) {
    	
    	if(stack == null) {
    		throw new NullPointerException("Stack is null.");
    	}
    	
    	StackInteger result = new StackInteger(stack.rows(), stack.columns());
    	
    	for(PlateInteger plate : stack) {
    		result.add(this.plates(plate, n));
    	}
    	
    	return result;
    }
    
     /**
      * Returns the result of the mathematical operation.
      * @param    StackInteger    the stack
      * @param    int             number of bits to shift
      * @param    int             beginning index of the sub set
      * @param    int             length of the subset
      * @return                   the result of the operation
      */
     public StackInteger stacks(StackInteger stack, int n, int begin, int length) {
     	
        if(stack == null) {
             throw new NullPointerException("Stack is null.");
     	}
     	
     	StackInteger result = new StackInteger(stack.rows(), stack.columns());
     	
     	for(PlateInteger plate : stack) {
     		result.add(this.plates(plate, n, begin, length));
     	}
     	
     	return result;
     } 
     
    /*------------------------ List Operation Methods ------------------------*/
    
    /**
     * Performs the mathematical operation for the two lists.
     * @param    List<Integer>    the first list
     * @param    int              number of bits to shift
     * @return                    result of the mathematical operation
     */
    public abstract List<Integer> calculate(List<Integer> list, int n);
    
    /**
     * Performs the mathematical operation for the two lists using the values
     * between the indices.
     * @param    List<Integer>    the first list
     * @param    int              number of bits to shift
     * @return                    result of the mathematical operation
     */
    public abstract List<Integer> calculate(List<Integer> list, int n, int begin, int length);
}
