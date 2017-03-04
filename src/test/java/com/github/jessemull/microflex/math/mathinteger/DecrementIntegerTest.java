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

package com.github.jessemull.microflex.math.mathinteger;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.integerflex.math.DecrementInteger;
import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the decrement integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DecrementIntegerTest {

    /* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = -100000;          // Minimum integer value for wells
	private static int maxValue = 1000000;          // Maximum integer value for wells
	private static int minPlate = 10;               // Plate minimum
	private static int maxPlate = 25;               // Plate maximum
	private static Random random = new Random();    // Generates random integers
	
	/* The addition operation */
	
	private static DecrementInteger decrement = new DecrementInteger();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<PlateInteger[]> arrays = new ArrayList<PlateInteger[]>();
	private static List<StackInteger> stacks = new ArrayList<StackInteger>();
	
    /* Value of false redirects System.err */
	
	private static boolean error = true;
	private static PrintStream originalOut = System.out;

	/**
	 * Generates random objects and numbers for testing.
	 */
	@BeforeClass
	public static void setUp() {
		
		if(error) {

			System.setErr(new PrintStream(new OutputStream() {
			    public void write(int x) {}
			}));

		}
		
		rows = PlateInteger.ROWS_48WELL + random.nextInt(PlateInteger.ROWS_1536WELL - 
	           PlateInteger.ROWS_48WELL + 1);

		columns =  PlateInteger.COLUMNS_48WELL + random.nextInt(PlateInteger.COLUMNS_1536WELL - 
		           PlateInteger.COLUMNS_48WELL + 1);

    	length = rows * columns / 5;
    	
    	for(int i = 0; i < stackNumber; i++) {
    		
    		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    		
    		StackInteger stack1 = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length, "Plate" + i, plateNumber);
    		
    		stacks.add(stack1);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateInteger[] array1 = new PlateInteger[plateNumber];

    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateInteger.ROWS_48WELL + random.nextInt(PlateInteger.ROWS_1536WELL - 
    			       PlateInteger.ROWS_48WELL + 1);

    			columns =  PlateInteger.COLUMNS_48WELL + random.nextInt(PlateInteger.COLUMNS_1536WELL - 
    			           PlateInteger.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateInteger plate = RandomUtil.randomPlateInteger(
    					rows, columns, minValue, maxValue, length, "Plate1-" + j);
    			
    			array1[j] = plate;

    		}
    		
    		arrays.add(array1);
    	}
    	
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
	/* ---------------------------- Constructors ---------------------------- */
	
	/**
     * Tests the constructor.
     */
	@Test
	public void testConstructor() {
		DecrementInteger test = new DecrementInteger();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the decrement well operation.
     */
	@Test
    public void testWells() {

		for(PlateInteger[] plates : arrays) {
			
			for(PlateInteger plate : plates) {

				for(WellInteger well : plate) {
					
					List<Integer> result = new ArrayList<Integer>();
					List<Integer> returned = decrement.wells(well);
					
					for(int in : well) {
						result.add(in--);
					}
					
					assertEquals(result, returned);
				}
			}			
		}
		
    }
    
    /**
     * Tests the decrement well operation using indices.
     */
	@Test
    public void testWellIndices() {

        for(PlateInteger[] plates : arrays) {
			
			for(PlateInteger plate : plates) {

				for(WellInteger well : plate) {

					int begin = 1 + random.nextInt(well.size() - 1);
	    			int end = begin + random.nextInt(well.size() - begin);
	    			
	    			List<Integer> result = new ArrayList<Integer>();
					List<Integer> returned = decrement.wells(well, begin, end - begin);				
					
					for(int i = begin; i < end; i++) {
						result.add(well.data().get(i) - 1);
					}
					
					assertEquals(result, returned);
				}
			}			
		}

    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Tests the decrement plate operation.
     */
    @Test
    public void testPlates() {

        for(PlateInteger[] plates : arrays) {
			
			for(PlateInteger plate : plates) {

				WellSetInteger result = new WellSetInteger();

				for(WellInteger well : plate) {
					
					List<Integer> resultList = new ArrayList<Integer>();
					
					for(int in : well) {
						resultList.add(in--);
					}
					
					result.add(new WellInteger(well.row(), well.column(), resultList));
				}
				
				PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
				PlateInteger returnedPlate = decrement.plates(plate);
				
				Iterator<WellInteger> iter1 = resultPlate.iterator();
				Iterator<WellInteger> iter2 = returnedPlate.iterator();
				
				while(iter1.hasNext()) {
					
					WellInteger well1 = iter1.next();
					WellInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(resultPlate, returnedPlate);
			}			
		}
    }
    
    /**
     * Tests the decrement plate operation using indices.
     */
    @Test
    public void testPlatesIndices() {
    	
    	for(PlateInteger[] plates : arrays) {
			
			for(PlateInteger plate : plates) {

				int begin = 1 + random.nextInt(plate.first().size() - 1);
    			int end = begin + random.nextInt(plate.first().size() - begin);
    			
				WellSetInteger result = new WellSetInteger();

				for(WellInteger well : plate) {
					
					List<Integer> resultList = new ArrayList<Integer>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i) - 1);
					}
					
					result.add(new WellInteger(well.row(), well.column(), resultList));
				}
				
				PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
				PlateInteger returnedPlate = decrement.plates(plate, begin, end - begin);
				
				Iterator<WellInteger> iter1 = resultPlate.iterator();
				Iterator<WellInteger> iter2 = returnedPlate.iterator();
				
				while(iter1.hasNext()) {
					
					WellInteger well1 = iter1.next();
					WellInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(resultPlate, returnedPlate);
			}			
		}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the decrement set operation.
     */
    @Test
    public void testSets() {

        for(PlateInteger[] plates : arrays) {
			
			for(PlateInteger plate : plates) {

				WellSetInteger result = new WellSetInteger();
				WellSetInteger returned = decrement.sets(plate.dataSet());
				
				for(WellInteger well : plate) {
					
					List<Integer> resultList = new ArrayList<Integer>();
					
					for(int in : well) {
						resultList.add(in--);
					}
					
					result.add(new WellInteger(well.row(), well.column(), resultList));
				}
				
				Iterator<WellInteger> iter1 = result.iterator();
				Iterator<WellInteger> iter2 = returned.iterator();
				
				while(iter1.hasNext()) {
					
					WellInteger well1 = iter1.next();
					WellInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(result, returned);
			}			
		}
    	
    }
    
    /**
     * Tests the decrement set operation using indices.
     */
    @Test
    public void testSetsIndices() {
    	
        for(PlateInteger[] plates : arrays) {
			
			for(PlateInteger plate : plates) {

				int begin = 1 + random.nextInt(plate.first().size() - 1);
    			int end = begin + random.nextInt(plate.first().size() - begin);
    			
				WellSetInteger result = new WellSetInteger();
				WellSetInteger returned = decrement.sets(plate.dataSet(), begin, end - begin);
				
				for(WellInteger well : plate) {
					
					List<Integer> resultList = new ArrayList<Integer>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i) - 1);
					}
					
					result.add(new WellInteger(well.row(), well.column(), resultList));
				}

				Iterator<WellInteger> iter1 = result.iterator();
				Iterator<WellInteger> iter2 = returned.iterator();
				
				while(iter1.hasNext()) {
					
					WellInteger well1 = iter1.next();
					WellInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(result, returned);

			}			
		}
    	
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the decrement stack operation.
     */
    @Test
    public void testStacks() {

        for(StackInteger stack : stacks) {
			
        	StackInteger resultStack = new StackInteger(stack.rows(), stack.columns());
        	
			for(PlateInteger plate : stack) {

				WellSetInteger result = new WellSetInteger();

				for(WellInteger well : plate) {
					
					List<Integer> resultList = new ArrayList<Integer>();
					
					for(int in : well) {
						resultList.add(in--);
					}
					
					result.add(new WellInteger(well.row(), well.column(), resultList));
				}
				
				PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
				resultStack.add(resultPlate);
				
			}
		
			StackInteger returnedStack = decrement.stacks(stack);
			
			assertEquals(resultStack, returnedStack);
			
		    Iterator<PlateInteger> plateIter1 = resultStack.iterator();
		    Iterator<PlateInteger> plateIter2 = returnedStack.iterator();
		    
		    while(plateIter1.hasNext()) {
		    	
		    	PlateInteger plate1 = plateIter1.next();
		    	PlateInteger plate2 = plateIter2.next();
		    	
		    	assertEquals(plate1, plate2);
		    	
		    	Iterator<WellInteger> wellIter1 = plate1.iterator();
		    	Iterator<WellInteger> wellIter2 = plate2.iterator();
		    	
		    	while(wellIter1.hasNext()) {
		    		
		    		WellInteger well1 = wellIter1.next();
		    		WellInteger well2 = wellIter2.next();
		    		
		    		assertEquals(well1.data(), well2.data());
		    		
		    	}
		    }
        }			
    }
    
     /**
      * Tests the decrement stack operation using indices.
      */
    @Test
    public void testStacksIndices() {

    	for(StackInteger stack : stacks) {
			
			int begin = 1 + random.nextInt(stack.first().first().size() - 1);
			int end = begin + random.nextInt(stack.first().first().size() - begin);
			
        	StackInteger resultStack = new StackInteger(stack.rows(), stack.columns());
        	
			for(PlateInteger plate : stack) {

				WellSetInteger result = new WellSetInteger();

				for(WellInteger well : plate) {
					
					List<Integer> resultList = new ArrayList<Integer>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i) - 1);
					}
					
					result.add(new WellInteger(well.row(), well.column(), resultList));
				}
				
				PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
				resultStack.add(resultPlate);
				
			}
		
			StackInteger returnedStack = decrement.stacks(stack, begin, end - begin);
			
			assertEquals(resultStack, returnedStack);
			
		    Iterator<PlateInteger> plateIter1 = resultStack.iterator();
		    Iterator<PlateInteger> plateIter2 = returnedStack.iterator();
		    
		    while(plateIter1.hasNext()) {
		    	
		    	PlateInteger plate1 = plateIter1.next();
		    	PlateInteger plate2 = plateIter2.next();
		    	
		    	assertEquals(plate1, plate2);
		    	
		    	Iterator<WellInteger> wellIter1 = plate1.iterator();
		    	Iterator<WellInteger> wellIter2 = plate2.iterator();
		    	
		    	while(wellIter1.hasNext()) {
		    		
		    		WellInteger well1 = wellIter1.next();
		    		WellInteger well2 = wellIter2.next();
		    		
		    		assertEquals(well1.data(), well2.data());
		    		
		    	}
		    }
        }
    } 
}
