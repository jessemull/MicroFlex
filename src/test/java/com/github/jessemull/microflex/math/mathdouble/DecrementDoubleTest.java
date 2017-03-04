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

package com.github.jessemull.microflex.math.mathdouble;

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

import com.github.jessemull.microflex.doubleflex.math.DecrementDouble;
import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.StackDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the decrement double class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DecrementDoubleTest {

    /* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static double minValue = -100000;       // Minimum double value for wells
	private static double maxValue = 1000000;       // Maximum double value for wells
	private static int minPlate = 10;               // Plate minimum
	private static int maxPlate = 25;               // Plate maximum
	private static Random random = new Random();    // Generates random integers
	
	/* The addition operation */
	
	private static DecrementDouble decrement = new DecrementDouble();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<PlateDouble[]> arrays = new ArrayList<PlateDouble[]>();
	private static List<StackDouble> stacks = new ArrayList<StackDouble>();
	
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
		
		rows = PlateDouble.ROWS_48WELL + random.nextInt(PlateDouble.ROWS_1536WELL - 
	           PlateDouble.ROWS_48WELL + 1);

		columns =  PlateDouble.COLUMNS_48WELL + random.nextInt(PlateDouble.COLUMNS_1536WELL - 
		           PlateDouble.COLUMNS_48WELL + 1);

    	length = rows * columns / 5;
    	
    	for(int i = 0; i < stackNumber; i++) {
    		
    		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    		
    		StackDouble stack1 = RandomUtil.randomStackDouble(rows, columns, minValue, maxValue, length, "Plate" + i, plateNumber);
    		
    		stacks.add(stack1);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateDouble[] array1 = new PlateDouble[plateNumber];

    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateDouble.ROWS_48WELL + random.nextInt(PlateDouble.ROWS_1536WELL - 
    			       PlateDouble.ROWS_48WELL + 1);

    			columns =  PlateDouble.COLUMNS_48WELL + random.nextInt(PlateDouble.COLUMNS_1536WELL - 
    			           PlateDouble.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateDouble plate = RandomUtil.randomPlateDouble(
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
		DecrementDouble test = new DecrementDouble();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the decrement well operation.
     */
	@Test
    public void testWells() {

		for(PlateDouble[] plates : arrays) {
			
			for(PlateDouble plate : plates) {

				for(WellDouble well : plate) {
					
					List<Double> result = new ArrayList<Double>();
					List<Double> returned = decrement.wells(well);
					
					for(double db : well) {
						result.add(db--);
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

        for(PlateDouble[] plates : arrays) {
			
			for(PlateDouble plate : plates) {

				for(WellDouble well : plate) {

					int begin = 1 + random.nextInt(well.size() - 1);
	    			int end = begin + random.nextInt(well.size() - begin);
	    			
	    			List<Double> result = new ArrayList<Double>();
					List<Double> returned = decrement.wells(well, begin, end - begin);				
					
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

        for(PlateDouble[] plates : arrays) {
			
			for(PlateDouble plate : plates) {

				WellSetDouble result = new WellSetDouble();

				for(WellDouble well : plate) {
					
					List<Double> resultList = new ArrayList<Double>();
					
					for(double db : well) {
						resultList.add(db--);
					}
					
					result.add(new WellDouble(well.row(), well.column(), resultList));
				}
				
				PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
				PlateDouble returnedPlate = decrement.plates(plate);
				
				Iterator<WellDouble> iter1 = resultPlate.iterator();
				Iterator<WellDouble> iter2 = returnedPlate.iterator();
				
				while(iter1.hasNext()) {
					
					WellDouble well1 = iter1.next();
					WellDouble well2 = iter2.next();
					
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
    	
    	for(PlateDouble[] plates : arrays) {
			
			for(PlateDouble plate : plates) {

				int begin = 1 + random.nextInt(plate.first().size() - 1);
    			int end = begin + random.nextInt(plate.first().size() - begin);
    			
				WellSetDouble result = new WellSetDouble();

				for(WellDouble well : plate) {
					
					List<Double> resultList = new ArrayList<Double>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i) - 1);
					}
					
					result.add(new WellDouble(well.row(), well.column(), resultList));
				}
				
				PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
				PlateDouble returnedPlate = decrement.plates(plate, begin, end - begin);
				
				Iterator<WellDouble> iter1 = resultPlate.iterator();
				Iterator<WellDouble> iter2 = returnedPlate.iterator();
				
				while(iter1.hasNext()) {
					
					WellDouble well1 = iter1.next();
					WellDouble well2 = iter2.next();
					
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

        for(PlateDouble[] plates : arrays) {
			
			for(PlateDouble plate : plates) {

				WellSetDouble result = new WellSetDouble();
				WellSetDouble returned = decrement.sets(plate.dataSet());
				
				for(WellDouble well : plate) {
					
					List<Double> resultList = new ArrayList<Double>();
					
					for(double db : well) {
						resultList.add(db--);
					}
					
					result.add(new WellDouble(well.row(), well.column(), resultList));
				}
				
				Iterator<WellDouble> iter1 = result.iterator();
				Iterator<WellDouble> iter2 = returned.iterator();
				
				while(iter1.hasNext()) {
					
					WellDouble well1 = iter1.next();
					WellDouble well2 = iter2.next();
					
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
    	
        for(PlateDouble[] plates : arrays) {
			
			for(PlateDouble plate : plates) {

				int begin = 1 + random.nextInt(plate.first().size() - 1);
    			int end = begin + random.nextInt(plate.first().size() - begin);
    			
				WellSetDouble result = new WellSetDouble();
				WellSetDouble returned = decrement.sets(plate.dataSet(), begin, end - begin);
				
				for(WellDouble well : plate) {
					
					List<Double> resultList = new ArrayList<Double>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i) - 1);
					}
					
					result.add(new WellDouble(well.row(), well.column(), resultList));
				}

				Iterator<WellDouble> iter1 = result.iterator();
				Iterator<WellDouble> iter2 = returned.iterator();
				
				while(iter1.hasNext()) {
					
					WellDouble well1 = iter1.next();
					WellDouble well2 = iter2.next();
					
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

        for(StackDouble stack : stacks) {
			
        	StackDouble resultStack = new StackDouble(stack.rows(), stack.columns());
        	
			for(PlateDouble plate : stack) {

				WellSetDouble result = new WellSetDouble();

				for(WellDouble well : plate) {
					
					List<Double> resultList = new ArrayList<Double>();
					
					for(double db : well) {
						resultList.add(db--);
					}
					
					result.add(new WellDouble(well.row(), well.column(), resultList));
				}
				
				PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
				resultStack.add(resultPlate);
				
			}
		
			StackDouble returnedStack = decrement.stacks(stack);
			
			assertEquals(resultStack, returnedStack);
			
		    Iterator<PlateDouble> plateIter1 = resultStack.iterator();
		    Iterator<PlateDouble> plateIter2 = returnedStack.iterator();
		    
		    while(plateIter1.hasNext()) {
		    	
		    	PlateDouble plate1 = plateIter1.next();
		    	PlateDouble plate2 = plateIter2.next();
		    	
		    	assertEquals(plate1, plate2);
		    	
		    	Iterator<WellDouble> wellIter1 = plate1.iterator();
		    	Iterator<WellDouble> wellIter2 = plate2.iterator();
		    	
		    	while(wellIter1.hasNext()) {
		    		
		    		WellDouble well1 = wellIter1.next();
		    		WellDouble well2 = wellIter2.next();
		    		
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

    	for(StackDouble stack : stacks) {
			
			int begin = 1 + random.nextInt(stack.first().first().size() - 1);
			int end = begin + random.nextInt(stack.first().first().size() - begin);
			
        	StackDouble resultStack = new StackDouble(stack.rows(), stack.columns());
        	
			for(PlateDouble plate : stack) {

				WellSetDouble result = new WellSetDouble();

				for(WellDouble well : plate) {
					
					List<Double> resultList = new ArrayList<Double>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i) - 1);
					}
					
					result.add(new WellDouble(well.row(), well.column(), resultList));
				}
				
				PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
				resultStack.add(resultPlate);
				
			}
		
			StackDouble returnedStack = decrement.stacks(stack, begin, end - begin);
			
			assertEquals(resultStack, returnedStack);
			
		    Iterator<PlateDouble> plateIter1 = resultStack.iterator();
		    Iterator<PlateDouble> plateIter2 = returnedStack.iterator();
		    
		    while(plateIter1.hasNext()) {
		    	
		    	PlateDouble plate1 = plateIter1.next();
		    	PlateDouble plate2 = plateIter2.next();
		    	
		    	assertEquals(plate1, plate2);
		    	
		    	Iterator<WellDouble> wellIter1 = plate1.iterator();
		    	Iterator<WellDouble> wellIter2 = plate2.iterator();
		    	
		    	while(wellIter1.hasNext()) {
		    		
		    		WellDouble well1 = wellIter1.next();
		    		WellDouble well2 = wellIter2.next();
		    		
		    		assertEquals(well1.data(), well2.data());
		    		
		    	}
		    }
        }
    } 
}
