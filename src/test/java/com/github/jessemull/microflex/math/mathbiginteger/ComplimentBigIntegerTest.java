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

package com.github.jessemull.microflex.math.mathbiginteger;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigintegerflex.math.ComplimentBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the compliment big integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComplimentBigIntegerTest {

    /* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigInteger minValue = new BigInteger(-100000 + "");    // Minimum big integer value for wells
	private static BigInteger maxValue = new BigInteger(1000000 + "");    // Maximum big integer value for wells
	private static int minPlate = 10;                                     // Plate minimum
	private static int maxPlate = 25;                                     // Plate maximum
	private static Random random = new Random();                          // Generates random integers
	
	/* The addition operation */
	
	private static ComplimentBigInteger compliment = new ComplimentBigInteger();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<PlateBigInteger[]> arrays = new ArrayList<PlateBigInteger[]>();
	private static List<StackBigInteger> stacks = new ArrayList<StackBigInteger>();
	
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
		
		rows = PlateBigInteger.ROWS_48WELL + random.nextInt(PlateBigInteger.ROWS_1536WELL - 
	           PlateBigInteger.ROWS_48WELL + 1);

		columns =  PlateBigInteger.COLUMNS_48WELL + random.nextInt(PlateBigInteger.COLUMNS_1536WELL - 
		           PlateBigInteger.COLUMNS_48WELL + 1);

    	length = rows * columns / 5;
    	
    	for(int i = 0; i < stackNumber; i++) {
    		
    		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    		
    		StackBigInteger stack1 = RandomUtil.randomStackBigInteger(rows, columns, minValue, maxValue, length, "Plate" + i, plateNumber);
    		
    		stacks.add(stack1);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateBigInteger[] array1 = new PlateBigInteger[plateNumber];

    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateBigInteger.ROWS_48WELL + random.nextInt(PlateBigInteger.ROWS_1536WELL - 
    			       PlateBigInteger.ROWS_48WELL + 1);

    			columns =  PlateBigInteger.COLUMNS_48WELL + random.nextInt(PlateBigInteger.COLUMNS_1536WELL - 
    			           PlateBigInteger.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
    					rows, columns, minValue, maxValue, length, "Plate" + j);
    			
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
		ComplimentBigInteger test = new ComplimentBigInteger();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the compliment well operation.
     */
	@Test
    public void testWells() {

	    PlateBigInteger[] plates = arrays.get(0);
		
		for(PlateBigInteger plate : plates) {

			for(WellBigInteger well : plate) {
				
				List<BigInteger> result = new ArrayList<BigInteger>();
				List<BigInteger> returned = compliment.wells(well);
				
				for(BigInteger bd : well) {
					result.add(bd.not());
				}
				
				assertEquals(result, returned);
			}
		}					
		
    }
    
    /**
     * Tests the compliment well operation using indices.
     */
	@Test
    public void testWellIndices() {

        for(PlateBigInteger[] plates : arrays) {
			
			for(PlateBigInteger plate : plates) {

				for(WellBigInteger well : plate) {

					int begin = 1 + random.nextInt(well.size() - 1);
	    			int end = begin + random.nextInt(well.size() - begin);
	    			
	    			List<BigInteger> result = new ArrayList<BigInteger>();
					List<BigInteger> returned = compliment.wells(well, begin, end - begin);				
					
					for(int i = begin; i < end; i++) {
						result.add(well.data().get(i).not());
					}
					
					assertEquals(result, returned);
				}
			}			
		}

    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Tests the compliment plate operation.
     */
    @Test
    public void testPlates() {

        for(PlateBigInteger[] plates : arrays) {
			
			for(PlateBigInteger plate : plates) {

				WellSetBigInteger result = new WellSetBigInteger();

				for(WellBigInteger well : plate) {
					
					List<BigInteger> resultList = new ArrayList<BigInteger>();
					
					for(BigInteger bd : well) {
						resultList.add(bd.not());
					}
					
					result.add(new WellBigInteger(well.row(), well.column(), resultList));
				}
				
				PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
				PlateBigInteger returnedPlate = compliment.plates(plate);
				
				Iterator<WellBigInteger> iter1 = resultPlate.iterator();
				Iterator<WellBigInteger> iter2 = returnedPlate.iterator();
				
				while(iter1.hasNext()) {
					
					WellBigInteger well1 = iter1.next();
					WellBigInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(resultPlate, returnedPlate);
			}			
		}
    }
    
    /**
     * Tests the compliment plate operation using indices.
     */
    @Test
    public void testPlatesIndices() {
    	
    	for(PlateBigInteger[] plates : arrays) {
			
			for(PlateBigInteger plate : plates) {

				int begin = 1 + random.nextInt(plate.first().size() - 1);
    			int end = begin + random.nextInt(plate.first().size() - begin);
    			
				WellSetBigInteger result = new WellSetBigInteger();

				for(WellBigInteger well : plate) {
					
					List<BigInteger> resultList = new ArrayList<BigInteger>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i).add(BigInteger.ONE));
					}
					
					result.add(new WellBigInteger(well.row(), well.column(), resultList));
				}
				
				PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
				PlateBigInteger returnedPlate = compliment.plates(plate, begin, end - begin);
				
				Iterator<WellBigInteger> iter1 = resultPlate.iterator();
				Iterator<WellBigInteger> iter2 = returnedPlate.iterator();
				
				while(iter1.hasNext()) {
					
					WellBigInteger well1 = iter1.next();
					WellBigInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(resultPlate, returnedPlate);
			}			
		}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the compliment set operation.
     */
    @Test
    public void testSets() {

        for(PlateBigInteger[] plates : arrays) {
			
			for(PlateBigInteger plate : plates) {

				WellSetBigInteger result = new WellSetBigInteger();
				WellSetBigInteger returned = compliment.sets(plate.dataSet());
				
				for(WellBigInteger well : plate) {
					
					List<BigInteger> resultList = new ArrayList<BigInteger>();
					
					for(BigInteger bd : well) {
						resultList.add(bd.not());
					}
					
					result.add(new WellBigInteger(well.row(), well.column(), resultList));
				}
				
				Iterator<WellBigInteger> iter1 = result.iterator();
				Iterator<WellBigInteger> iter2 = returned.iterator();
				
				while(iter1.hasNext()) {
					
					WellBigInteger well1 = iter1.next();
					WellBigInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(result, returned);
			}			
		}
    	
    }
    
    /**
     * Tests the compliment set operation using indices.
     */
    @Test
    public void testSetsIndices() {
    	
        for(PlateBigInteger[] plates : arrays) {
			
			for(PlateBigInteger plate : plates) {

				int begin = 1 + random.nextInt(plate.first().size() - 1);
    			int end = begin + random.nextInt(plate.first().size() - begin);
    			
				WellSetBigInteger result = new WellSetBigInteger();
				WellSetBigInteger returned = compliment.sets(plate.dataSet(), begin, end - begin);
				
				for(WellBigInteger well : plate) {
					
					List<BigInteger> resultList = new ArrayList<BigInteger>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i).add(BigInteger.ONE));
					}
					
					result.add(new WellBigInteger(well.row(), well.column(), resultList));
				}
				
				Iterator<WellBigInteger> iter1 = result.iterator();
				Iterator<WellBigInteger> iter2 = returned.iterator();
				
				while(iter1.hasNext()) {
					
					WellBigInteger well1 = iter1.next();
					WellBigInteger well2 = iter2.next();
					
					assertEquals(well1, well2);
					
				}
				
				assertEquals(result, returned);
			}			
		}
    	
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the compliment stack operation.
     */
    @Test
    public void testStacks() {

        for(StackBigInteger stack : stacks) {
			
        	StackBigInteger resultStack = new StackBigInteger(stack.rows(), stack.columns());
        	
			for(PlateBigInteger plate : stack) {

				WellSetBigInteger result = new WellSetBigInteger();

				for(WellBigInteger well : plate) {
					
					List<BigInteger> resultList = new ArrayList<BigInteger>();
					
					for(BigInteger bd : well) {
						resultList.add(bd.not());
					}
					
					result.add(new WellBigInteger(well.row(), well.column(), resultList));
				}
				
				PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
				resultStack.add(resultPlate);
				
			}
		
			StackBigInteger returnedStack = compliment.stacks(stack);
			
			assertEquals(resultStack, returnedStack);
			
		    Iterator<PlateBigInteger> plateIter1 = resultStack.iterator();
		    Iterator<PlateBigInteger> plateIter2 = returnedStack.iterator();
		    
		    while(plateIter1.hasNext()) {
		    	
		    	PlateBigInteger plate1 = plateIter1.next();
		    	PlateBigInteger plate2 = plateIter2.next();
		    	
		    	assertEquals(plate1, plate2);
		    	
		    	Iterator<WellBigInteger> wellIter1 = plate1.iterator();
		    	Iterator<WellBigInteger> wellIter2 = plate2.iterator();
		    	
		    	while(wellIter1.hasNext()) {
		    		
		    		WellBigInteger well1 = wellIter1.next();
		    		WellBigInteger well2 = wellIter2.next();
		    		
		    		assertEquals(well1.data(), well2.data());
		    		
		    	}
		    }
        }			
    }
    
     /**
      * Tests the compliment stack operation using indices.
      */
    @Test
    public void testStacksIndices() {

    	for(StackBigInteger stack : stacks) {
			
			int begin = 1 + random.nextInt(stack.first().first().size() - 1);
			int end = begin + random.nextInt(stack.first().first().size() - begin);
			
        	StackBigInteger resultStack = new StackBigInteger(stack.rows(), stack.columns());
        	
			for(PlateBigInteger plate : stack) {

				WellSetBigInteger result = new WellSetBigInteger();

				for(WellBigInteger well : plate) {
					
					List<BigInteger> resultList = new ArrayList<BigInteger>();
					
					for(int i = begin; i < end; i++) {
						resultList.add(well.data().get(i).not());
					}
					
					result.add(new WellBigInteger(well.row(), well.column(), resultList));
				}
				
				PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
				resultStack.add(resultPlate);
				
			}
		
			StackBigInteger returnedStack = compliment.stacks(stack, begin, end - begin);
			
			assertEquals(resultStack, returnedStack);
			
		    Iterator<PlateBigInteger> plateIter1 = resultStack.iterator();
		    Iterator<PlateBigInteger> plateIter2 = returnedStack.iterator();
		    
		    while(plateIter1.hasNext()) {
		    	
		    	PlateBigInteger plate1 = plateIter1.next();
		    	PlateBigInteger plate2 = plateIter2.next();
		    	
		    	assertEquals(plate1, plate2);
		    	
		    	Iterator<WellBigInteger> wellIter1 = plate1.iterator();
		    	Iterator<WellBigInteger> wellIter2 = plate2.iterator();
		    	
		    	while(wellIter1.hasNext()) {
		    		
		    		WellBigInteger well1 = wellIter1.next();
		    		WellBigInteger well2 = wellIter2.next();
		    		
		    		assertEquals(well1.data(), well2.data());
		    		
		    	}
		    }
        }
    } 
}
