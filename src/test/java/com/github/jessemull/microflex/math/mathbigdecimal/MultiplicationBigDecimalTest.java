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

package com.github.jessemull.microflex.math.mathbigdecimal;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigdecimalflex.math.MultiplicationBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.StackBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the multiplication big decimal class.
 * 
 * @author Jesse L. Mull
 * @update Updated Dec 9, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultiplicationBigDecimalTest {
	
/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigDecimal minValue = new BigDecimal(-100000);   // Minimum big decimal value for wells
	private static BigDecimal maxValue = new BigDecimal(100000);    // Maximum big decimal value for wells
	private static int minPlate = 10;                               // Plate minimum
	private static int maxPlate = 25;                               // Plate maximum
	private static Random random = new Random();                    // Generates random integers
	
	/* The multiplication operation */
	
	private static MultiplicationBigDecimal multiplication = new MultiplicationBigDecimal();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);
	private static List<PlateBigDecimal[]> arrays1 = new ArrayList<PlateBigDecimal[]>();
	private static List<PlateBigDecimal[]> arrays2 = new ArrayList<PlateBigDecimal[]>();
	private static List<PlateBigDecimal[]> uneven = new ArrayList<PlateBigDecimal[]>();
	private static List<StackBigDecimal> stacks1 = new ArrayList<StackBigDecimal>();
	private static List<StackBigDecimal> stacks2 = new ArrayList<StackBigDecimal>();
	private static List<StackBigDecimal> stacksUneven = new ArrayList<StackBigDecimal>();
	
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
		
		rows = PlateBigDecimal.ROWS_48WELL + random.nextInt(PlateBigDecimal.ROWS_1536WELL - 
	           PlateBigDecimal.ROWS_48WELL + 1);

		columns =  PlateBigDecimal.COLUMNS_48WELL + random.nextInt(PlateBigDecimal.COLUMNS_1536WELL - 
		           PlateBigDecimal.COLUMNS_48WELL + 1);

    	length = rows * columns / 5;
    	
    	for(int i = 0; i < stackNumber; i++) {
    		
    		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    		
    		StackBigDecimal stack1 = RandomUtil.randomStackBigDecimal(rows, columns, minValue, maxValue, length, "Plate1-" + i, plateNumber);
    		StackBigDecimal stack2 = RandomUtil.randomStackBigDecimal(rows, columns, minValue, maxValue, length, "Plate2-" + i, plateNumber);
    		StackBigDecimal stackUneven = RandomUtil.randomStackBigDecimal(rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + i, plateNumber);
    		
    		stacks1.add(stack1);
    		stacks2.add(stack2);
    		stacksUneven.add(stackUneven);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateBigDecimal[] array1 = new PlateBigDecimal[plateNumber];
    		PlateBigDecimal[] array2 = new PlateBigDecimal[plateNumber];
    		PlateBigDecimal[] unevenArray = new PlateBigDecimal[plateNumber];
    		
    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateBigDecimal.ROWS_48WELL + random.nextInt(PlateBigDecimal.ROWS_1536WELL - 
    			       PlateBigDecimal.ROWS_48WELL + 1);

    			columns =  PlateBigDecimal.COLUMNS_48WELL + random.nextInt(PlateBigDecimal.COLUMNS_1536WELL - 
    			           PlateBigDecimal.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateBigDecimal plate1 = RandomUtil.randomPlateBigDecimal(
    					rows, columns, minValue, maxValue, length, "Plate1-" + j);
    			PlateBigDecimal plate2 = RandomUtil.randomPlateBigDecimal(
    					rows, columns, minValue, maxValue, length, "Plate2-" + j);
    			PlateBigDecimal unevenPlate = RandomUtil.randomPlateBigDecimal(
    					rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + j);
    			
    			array1[j] = plate1;
    			array2[j] = plate2;
    			unevenArray[j] = unevenPlate;
    		}
    		
    		arrays1.add(array1);
    		arrays2.add(array2);
    		uneven.add(unevenArray);
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
		MultiplicationBigDecimal test = new MultiplicationBigDecimal();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the multiplication of two wells.
     */
	@Test
    public void testWells() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], well2[j], mc);
    			List<BigDecimal> returnedUneven = multiplication.wells(well1[j], wellUneven[j], mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			for(int l = list1.size(); l < unevenList.size(); l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
	
    /**
     * Tests the multiplication of two wells using indices.
     */
	@Test
    public void testWellsIndices() {
		
		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], well2[j], begin, end - begin, mc);
    			List<BigDecimal> returnedUneven = multiplication.wells(well1[j], wellUneven[j], begin, end - begin, mc);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict multiplication of two wells.
     */
	@Test
    public void testWellsStrict() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wellsStrict(well1[j], well2[j], mc);
    			List<BigDecimal> returnedUneven = multiplication.wellsStrict(well1[j], wellUneven[j], mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict multiplication of two wells using indices.
     */
	@Test
    public void testWellsStrictIndices() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wellsStrict(well1[j], well2[j], begin, end - begin, mc);
    			List<BigDecimal> returnedUneven = multiplication.wellsStrict(well1[j], wellUneven[j], begin, end - begin, mc);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the multiplication of a constant to a well.
     */
	@Test
    public void testWellConstant() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		
    		double constant = random.nextDouble();
    		BigDecimal constantBigDecimal = new BigDecimal(constant);
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], constantBigDecimal, mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(constantBigDecimal, mc));
    			}

    			assertEquals(result, returned);
    		}
    		
    	}
    }
    
    /**
     * Tests the multiplication of an array to a well.
     */ 
	@Test
    public void testWellArray() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			BigDecimal[] array = list2.toArray(new BigDecimal[list2.size()]);
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			BigDecimal[] arrayUneven = unevenList.toArray(new BigDecimal[unevenList.size()]);
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], array, mc);
    			List<BigDecimal> returnedUneven = multiplication.wells(well1[j], arrayUneven, mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(array[k], mc));
    				resultUneven.add(list1.get(k).multiply(arrayUneven[k], mc));
    			}

    			for(int l = list1.size(); l < unevenList.size(); l++) {
    				resultUneven.add(arrayUneven[l]);
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the multiplication of an array to a well using indices.
     */
	@Test
    public void testWellArrayIndices() {

    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			BigDecimal[] array = list2.toArray(new BigDecimal[list2.size()]);
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			BigDecimal[] arrayUneven = unevenList.toArray(new BigDecimal[unevenList.size()]);
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], array, begin, end - begin, mc);
    			List<BigDecimal> returnedUneven = multiplication.wells(well1[j], arrayUneven, begin, end - begin, mc);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).multiply(array[k], mc));
    				resultUneven.add(list1.get(k).multiply(arrayUneven[k], mc));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(arrayUneven[l]);
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the multiplication of a collection to a well.
     */
	@Test
    public void testWellCollection() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], well2[j], mc);
    			List<BigDecimal> returnedUneven = multiplication.wells(well1[j], wellUneven[j], mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			for(int l = list1.size(); l < unevenList.size(); l++) {
    				resultUneven.add(unevenList.get(l));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the multiplication of a collection to a well using indices.
     */
    @Test
    public void testWellCollectionIndices() {


		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wells(well1[j], well2[j], begin, end - begin, mc);
    			List<BigDecimal> returnedUneven = multiplication.wells(well1[j], wellUneven[j], begin, end - begin, mc);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict multiplication of an array to a well.
     */
    @Test
    public void testWellStrictArray() {


		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			BigDecimal[] array = list2.toArray(new BigDecimal[list2.size()]);
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			BigDecimal[] arrayUneven = unevenList.toArray(new BigDecimal[unevenList.size()]);
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wellsStrict(well1[j], array, mc);
    			List<BigDecimal> returnedUneven = multiplication.wellsStrict(well1[j], arrayUneven, mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(array[k], mc));
    				resultUneven.add(list1.get(k).multiply(arrayUneven[k], mc));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict multiplication of an array to a well using indices.
     */
    @Test
    public void testWellStrictArrayIndices() {

    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			BigDecimal[] array = list2.toArray(new BigDecimal[list2.size()]);
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			BigDecimal[] arrayUneven = unevenList.toArray(new BigDecimal[unevenList.size()]);
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wellsStrict(well1[j], array, begin, end - begin, mc);
    			List<BigDecimal> returnedUneven = multiplication.wellsStrict(well1[j], arrayUneven, begin, end - begin, mc);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).multiply(array[k], mc));
    				resultUneven.add(list1.get(k).multiply(arrayUneven[k], mc));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}	
    	}
    }
    
    /**
     * Tests the strict multiplication of a collection to a well.
     */
    @Test
    public void testWellStrictCollection() {

    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();
    			
    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wellsStrict(well1[j], well2[j], mc);
    			List<BigDecimal> returnedUneven = multiplication.wellsStrict(well1[j], wellUneven[j], mc);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict multiplication of a collection to a well.
     */
    public void testWellStrictCollectionIndices() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigDecimal[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigDecimal[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigDecimal[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigDecimal> list1 = well1[j].data();
    			List<BigDecimal> list2 = well2[j].data();
    			List<BigDecimal> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigDecimal> result = new ArrayList<BigDecimal>();
    			List<BigDecimal> resultUneven = new ArrayList<BigDecimal>();
    			List<BigDecimal> returned = multiplication.wellsStrict(well1[j], well2[j], begin, end - begin, mc);
    			List<BigDecimal> returnedUneven = multiplication.wellsStrict(well1[j], wellUneven[j], begin, end - begin, mc);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).multiply(list2.get(k), mc));
    				resultUneven.add(list1.get(k).multiply(unevenList.get(k), mc));
    			}

    			for(int l = list1.size(); l < end; l++) {
    				resultUneven.add(unevenList.get(l));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /* ---------------------------- Plate Methods --------------------------- */
    
    /**
     * Tests the multiplication of two plates.
     */
    @Test
    public void testPlates() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		PlateBigDecimal plate2 = plates2[i];
    		PlateBigDecimal plateUneven = unevenPlates[i];
    		
    		WellSetBigDecimal set1 = plate1.dataSet();
    		WellSetBigDecimal set2 = plate2.dataSet();
    		WellSetBigDecimal setUneven = plateUneven.dataSet();
    		
    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, false);
    		PlateBigDecimal returnedPlate = multiplication.plates(plate1, plate2, mc);
    		PlateBigDecimal returnedPlateUneven = multiplication.plates(plate1, plateUneven, mc);
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returnedPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the multiplication of two plates using indices.
     */
    @Test
    public void testPlatesIndices() {
    	
		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		PlateBigDecimal plate2 = plates2[i];
    		PlateBigDecimal plateUneven = unevenPlates[i];
    		
    		WellSetBigDecimal set1 = plate1.dataSet();
    		WellSetBigDecimal set2 = plate2.dataSet();
    		WellSetBigDecimal setUneven = plateUneven.dataSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, begin, end, false);
    		PlateBigDecimal returnedPlate = multiplication.plates(plate1, plate2, begin, end - begin, mc);
    		PlateBigDecimal returnedPlateUneven = multiplication.plates(plate1, plateUneven, begin, end - begin, mc);
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returnedPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of two plates.
     */
    public void testPlatesStrict() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		PlateBigDecimal plate2 = plates2[i];
    		PlateBigDecimal plateUneven = unevenPlates[i];
    		
    		WellSetBigDecimal set1 = plate1.dataSet();
    		WellSetBigDecimal set2 = plate2.dataSet();
    		WellSetBigDecimal setUneven = plateUneven.dataSet();
    		
    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, true);
    		PlateBigDecimal returnedPlate = multiplication.platesStrict(plate1, plate2, mc);
    		PlateBigDecimal returnedPlateUneven = multiplication.platesStrict(plate1, plateUneven, mc);
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returnedPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the strict multiplication of two plates using indices.
     */
    @Test
    public void testPlatesStrictIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		PlateBigDecimal plate2 = plates2[i];
    		PlateBigDecimal plateUneven = unevenPlates[i];
    		
    		WellSetBigDecimal set1 = plate1.dataSet();
    		WellSetBigDecimal set2 = plate2.dataSet();
    		WellSetBigDecimal setUneven = plateUneven.dataSet();

    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, begin, end, true);
    		PlateBigDecimal returnedPlate = multiplication.platesStrict(plate1, plate2, begin, end - begin, mc);
    		PlateBigDecimal returnedPlateUneven = multiplication.platesStrict(plate1, plateUneven, begin, end - begin, mc);
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returnedPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the multiplication of a constant to a plate.
     */
    @Test
    public void testPlateConstant() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);

    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];

    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		BigDecimal min = new BigDecimal("1000", mc);
    		BigDecimal max = new BigDecimal("10000", mc);
    		
    		BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
    	    randomBigDecimal = randomBigDecimal.setScale(mc.getPrecision(), mc.getRoundingMode());

    	    WellSetBigDecimal result = new WellSetBigDecimal();
    	    
    		for(WellBigDecimal well : set1) {
    			
    			List<BigDecimal> list = new ArrayList<BigDecimal>();
    			
    			for(BigDecimal bd : well.data()) {
    				list.add(bd.multiply(randomBigDecimal, mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), list));
    		}

    		PlateBigDecimal returnedPlate = multiplication.plates(plate1, randomBigDecimal, mc);
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		
    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returnedPlate.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);
    	}
    }
    
    /**
     * Tests the multiplication of an array to a plate.
     */
    @Test
    public void testPlateArray() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigDecimal returned = multiplication.plates(plate1, array, mc);
    		PlateBigDecimal returnedUneven = multiplication.plates(plate1, arrayUneven, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the multiplication of an array to a plate using indices.
     */
    @Test
    public void testPlateArrayIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
           for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigDecimal returned = multiplication.plates(plate1, array, begin, end - begin, mc);
    		PlateBigDecimal returnedUneven = multiplication.plates(plate1, arrayUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the multiplication of a collection to a plate.
     */
    @Test
    public void testPlateCollection() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigDecimal returned = multiplication.plates(plate1, inputList, mc);
    		PlateBigDecimal returnedUneven = multiplication.plates(plate1, inputListUneven, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the multiplication of a collection to a plate using indices.
     */
    @Test
    public void testPlateCollectionIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateBigDecimal returned = multiplication.plates(plate1, inputList, begin, end - begin, mc);
    		PlateBigDecimal returnedUneven = multiplication.plates(plate1, inputListUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict multiplication of an array to a plate.
     */
    @Test
    public void testPlateStrictArray() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigDecimal returned = multiplication.platesStrict(plate1, array, mc);
    		PlateBigDecimal returnedUneven = multiplication.platesStrict(plate1, arrayUneven, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of an array to a plate using indices.
     */
    @Test
    public void testPlateStrictArrayIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    	    PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
            for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigDecimal returned = multiplication.platesStrict(plate1, array, begin, end - begin, mc);
    		PlateBigDecimal returnedUneven = multiplication.platesStrict(plate1, arrayUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}    	
    }
    
    /**
     * Tests the strict multiplication of a collection to a plate.
     */
    @Test
    public void testPlateStrictCollection() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigDecimal returned = multiplication.platesStrict(plate1, inputList, mc);
    		PlateBigDecimal returnedUneven = multiplication.platesStrict(plate1, inputListUneven, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of a collection to a plate using indices.
     */
    @Test
    public void testPlateStrictCollectionIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigDecimal plate1 = plates1[i];
    		WellSetBigDecimal set1 = plate1.dataSet();
    		
    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate1.columns(), result);
    		PlateBigDecimal resultPlateUneven = new PlateBigDecimal(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateBigDecimal returned = multiplication.platesStrict(plate1, inputList, begin, end - begin, mc);
    		PlateBigDecimal returnedUneven = multiplication.platesStrict(plate1, inputListUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = resultPlate.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the multiplication of a two sets.
     */
    @Test
    public void testSets() {
    	
		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();
    		WellSetBigDecimal set2 = plates2[i].dataSet();
    		WellSetBigDecimal setUneven = unevenPlates[i].dataSet();
    		
    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, false);
    		WellSetBigDecimal returned = multiplication.sets(set1, set2, mc);
    		WellSetBigDecimal returnedUneven = multiplication.sets(set1, setUneven, mc);

    		Iterator<WellBigDecimal> iter1 = result[0].iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = result[1].iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the multiplication of two sets using indices.
     */
    @Test
    public void testSetsIndices() {

    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();
    		WellSetBigDecimal set2 = plates2[i].dataSet();
    		WellSetBigDecimal setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, begin, end, false);
    		WellSetBigDecimal returned = multiplication.sets(set1, set2, begin, end - begin, mc);
    		WellSetBigDecimal returnedUneven = multiplication.sets(set1, setUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = result[0].iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = result[1].iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict multiplication of two sets.
     */
    @Test
    public void testSetsStrict() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();
    		WellSetBigDecimal set2 = plates2[i].dataSet();
    		WellSetBigDecimal setUneven = unevenPlates[i].dataSet();
    		
    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, true);
    		WellSetBigDecimal returned = multiplication.setsStrict(set1, set2, mc);
    		WellSetBigDecimal returnedUneven = multiplication.setsStrict(set1, setUneven, mc);

    		Iterator<WellBigDecimal> iter1 = result[0].iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = result[1].iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict multiplication of two sets using indices.
     */
    @Test
    public void testSetsStrictIndices() {

    	PlateBigDecimal[] plates1 = arrays1.get(0);
		PlateBigDecimal[] plates2 = arrays2.get(0);
		PlateBigDecimal[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();
    		WellSetBigDecimal set2 = plates2[i].dataSet();
    		WellSetBigDecimal setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, begin, end, true);
    		WellSetBigDecimal returned = multiplication.setsStrict(set1, set2, begin, end - begin, mc);
    		WellSetBigDecimal returnedUneven = multiplication.setsStrict(set1, setUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = result[0].iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = result[1].iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the multiplication of a constant to a set.
     */
    @Test
    public void testSetConstant() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set = plates1[i].dataSet();
    		WellSetBigDecimal result = new WellSetBigDecimal();

    		BigDecimal min = new BigDecimal("1000");
    		BigDecimal max = new BigDecimal("10000");
    		
    		BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
    	    randomBigDecimal = randomBigDecimal.setScale(2, RoundingMode.DOWN);

    		for(WellBigDecimal well : set) {
    			
    			List<BigDecimal> list = new ArrayList<BigDecimal>();
    			
    			for(BigDecimal bd : well.data()) {
    				list.add(bd.multiply(randomBigDecimal, mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), list));
    		}

    		WellSetBigDecimal returned = multiplication.sets(set, randomBigDecimal, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();

    		while(iter1.hasNext()) {
    			
    			List<BigDecimal> well1 = iter1.next().data();
    			List<BigDecimal> well2 = iter2.next().data();
    			
    			assertEquals(well1, well2);
    			
    		}
    		
    		assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the multiplication of an array to a set.
     */
    @Test
    public void testSetArray() {   	

		PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigDecimal returned = multiplication.sets(set1, array, mc);
    		WellSetBigDecimal returnedUneven = multiplication.sets(set1, arrayUneven, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the multiplication of an array to a set using indices.
     */
    @Test
    public void testSetArrayIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigDecimal returned = multiplication.sets(set1, array, begin, end - begin, mc);
    		WellSetBigDecimal returnedUneven = multiplication.sets(set1, arrayUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the multiplication of a collection to a set.
     */
    @Test
    public void testSetCollection() {   

		PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigDecimal returned = multiplication.sets(set1, inputList, mc);
    		WellSetBigDecimal returnedUneven = multiplication.sets(set1, inputListUneven, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the multiplication of a collection to a set using indices.
     */
    @Test
    public void testSetCollectionIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellBigDecimal well : set1) {
    		
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
			
    		WellSetBigDecimal returned = multiplication.sets(set1, inputList, begin, end - begin, mc);
    		WellSetBigDecimal returnedUneven = multiplication.sets(set1, inputListUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict multiplication of an array to a set.
     */
    @Test
    public void testSetStrictArray() {

		PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}

    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigDecimal returned = multiplication.setsStrict(set1, array, mc);
    		WellSetBigDecimal returnedUneven = multiplication.setsStrict(set1, arrayUneven, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict multiplication of an array to a set using indices.
     */
    public void testSetStrictArrayIndices() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		BigDecimal[] array = wellEven.toBigDecimalArray();
		BigDecimal[] arrayUneven = wellUneven.toBigDecimalArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(array[index], mc));
    			}
    			
    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigDecimal returned = multiplication.setsStrict(set1, array, begin, end - begin, mc);
    		WellSetBigDecimal returnedUneven = multiplication.setsStrict(set1, arrayUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of a collection to a set.
     */
    @Test
    public void testSetStrictCollection() {
    	
    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}

    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigDecimal returned = multiplication.setsStrict(set1, inputList, mc);
    		WellSetBigDecimal returnedUneven = multiplication.setsStrict(set1, inputListUneven, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of a collection to a set using indices.
     */
    @Test
    public void testSetStrictCollectionIndices() {

    	PlateBigDecimal[] plates1 = arrays1.get(0);
		WellBigDecimal wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigDecimal wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigDecimal> inputList = wellEven.data();
		List<BigDecimal> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigDecimal set1 = plates1[i].dataSet();

    		WellSetBigDecimal result = new WellSetBigDecimal();
    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellBigDecimal well : set1) {
    		
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
    			}

    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellBigDecimal well : set1) {
    			
    			int index;
    			List<BigDecimal> list = well.data();
    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
    			}
    			
    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
    		}
			
    		WellSetBigDecimal returned = multiplication.setsStrict(set1, inputList, begin, end - begin, mc);
    		WellSetBigDecimal returnedUneven = multiplication.setsStrict(set1, inputListUneven, begin, end - begin, mc);

    		Iterator<WellBigDecimal> iter1 = result.iterator();
    		Iterator<WellBigDecimal> iter2 = returned.iterator();
    		Iterator<WellBigDecimal> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigDecimal> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigDecimal well1 = iter1.next();
    			WellBigDecimal well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigDecimal well1 = iterUneven1.next();
    			WellBigDecimal well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the multiplication of two stacks.
     */
    @Test
    public void testStacks() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			StackBigDecimal stack2 = stacks2.get(k);
			StackBigDecimal stackUneven = stacksUneven.get(k);

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			Iterator<PlateBigDecimal> stackIter2 = stack2.iterator();
			Iterator<PlateBigDecimal> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = stackIter1.next();
	    		PlateBigDecimal plate2 = stackIter2.next();
	    		PlateBigDecimal plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		WellSetBigDecimal setUneven = plateUneven.dataSet();
	    		
	    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, false);
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		resultStack.add(stackIter1.next());
	    	}
	    	
	    	while(stackIter2.hasNext()) {
	    		resultStack.add(stackIter2.next());
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		resultStackUneven.add(stackUnevenIter.next());
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, stack2, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacks(stack1, stackUneven, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the multiplication of two stack using indices.
     */
    @Test
    public void testStacksIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			StackBigDecimal stack2 = stacks2.get(k);
			StackBigDecimal stackUneven = stacksUneven.get(k);

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			Iterator<PlateBigDecimal> stackIter2 = stack2.iterator();
			Iterator<PlateBigDecimal> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = stackIter1.next();
	    		PlateBigDecimal plate2 = stackIter2.next();
	    		PlateBigDecimal plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		WellSetBigDecimal setUneven = plateUneven.dataSet();

	    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, begin, end, false);
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		for(WellBigDecimal well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackIter2.hasNext()) {

	    		PlateBigDecimal plate = stackIter2.next();
	    		
	    		for(WellBigDecimal well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackUnevenIter.next();
	    		
	    		for(WellBigDecimal well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStackUneven.add(plate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, stack2, begin, end - begin, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacks(stack1, stackUneven, begin, end - begin, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of two stacks.
     */
    @Test
    public void testStacksStrict() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			StackBigDecimal stack2 = stacks2.get(k);
			StackBigDecimal stackUneven = stacksUneven.get(k);

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			Iterator<PlateBigDecimal> stackIter2 = stack2.iterator();
			Iterator<PlateBigDecimal> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = stackIter1.next();
	    		PlateBigDecimal plate2 = stackIter2.next();
	    		PlateBigDecimal plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		WellSetBigDecimal setUneven = plateUneven.dataSet();
	    		
	    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, true);
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	
	    	StackBigDecimal returned = multiplication.stacksStrict(stack1, stack2, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacksStrict(stack1, stackUneven, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict multiplication of two stacks using indices.
     */
    @Test
    public void testStacksStrictIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			StackBigDecimal stack2 = stacks2.get(k);
			StackBigDecimal stackUneven = stacksUneven.get(k);

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			Iterator<PlateBigDecimal> stackIter2 = stack2.iterator();
			Iterator<PlateBigDecimal> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = stackIter1.next();
	    		PlateBigDecimal plate2 = stackIter2.next();
	    		PlateBigDecimal plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		WellSetBigDecimal setUneven = plateUneven.dataSet();

	    		WellSetBigDecimal[] result = this.set(set1, set2, setUneven, begin, end, true);
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacksStrict(stack1, stack2, begin, end - begin, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacksStrict(stack1, stackUneven, begin, end - begin, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the multiplication of a constant to a stack.
     */
    @Test
    public void testStackConstant() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());

			BigDecimal min = new BigDecimal("1000");
    		BigDecimal max = new BigDecimal("10000");
    		
    		BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
    	    randomBigDecimal = randomBigDecimal.setScale(2, RoundingMode.DOWN);
    	    
			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();  
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns());

	    		for(WellBigDecimal well : plate) {
	    			
	    			List<BigDecimal> resultList = new ArrayList<BigDecimal>();
	    			
	    			for(BigDecimal bd : well) {
	    				resultList.add(bd.multiply(randomBigDecimal, mc));
	    			}
	    			
	    			resultPlate.addWells(new WellBigDecimal(well.row(), well.column(), resultList));
	    		}
	
	    		resultStack.add(resultPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, randomBigDecimal, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
    	}  	
    }
    
    /**
     * Tests the multiplication of an array to a stack.
     */
    @Test
    public void testStackArray() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			BigDecimal[] array = stacks2.get(k).first().first().toBigDecimalArray();
			BigDecimal[] arrayUneven = stacksUneven.get(k).first().first().toBigDecimalArray();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index).multiply(array[index], mc));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, array, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacks(stack1, arrayUneven, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the multiplication of an array to a stack using indices.
     */
    @Test
    public void testStackArrayIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			BigDecimal[] array = stacks2.get(k).first().first().toBigDecimalArray();
			BigDecimal[] arrayUneven = stacksUneven.get(k).first().first().toBigDecimalArray();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(array[index], mc));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length && j < end; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length && j < end; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, array, begin, end - begin, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacks(stack1, arrayUneven, begin, end - begin, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the multiplication of a collection to a stack.
     */
    @Test
    public void testStackCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			List<BigDecimal> inputList = stacks2.get(k).first().first().data();
			List<BigDecimal> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size(); j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size(); j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, inputList, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacks(stack1, inputListUneven, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the multiplication of a collection to a stack using indices.
     */
    @Test
    public void testStackCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			List<BigDecimal> inputList = stacks2.get(k).first().first().data();
			List<BigDecimal> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size() && j < end; j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacks(stack1, inputList, begin, end - begin, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacks(stack1, inputListUneven, begin, end - begin, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict multiplication of an array to a stack.
     */
    @Test
    public void testStackStrictArray() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			BigDecimal[] array = stacks2.get(k).first().first().toBigDecimalArray();
			BigDecimal[] arrayUneven = stacksUneven.get(k).first().first().toBigDecimalArray();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index).multiply(array[index], mc));
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
	    			}

	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacksStrict(stack1, array, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacksStrict(stack1, arrayUneven, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict multiplication of an array to a stack using indices.
     */
    @Test
    public void testStackStrictArrayIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			BigDecimal[] array = stacks2.get(k).first().first().toBigDecimalArray();
			BigDecimal[] arrayUneven = stacksUneven.get(k).first().first().toBigDecimalArray();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(array[index], mc));
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(arrayUneven[index], mc));
	    			}
	    			
	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacksStrict(stack1, array, begin, end - begin, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacksStrict(stack1, arrayUneven, begin, end - begin, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict multiplication of a collection to a stack.
     */
    public void testStackStrictCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			List<BigDecimal> inputList = stacks2.get(k).first().first().data();
			List<BigDecimal> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
	    			}
	    			
	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
	    			}
	    			
	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacksStrict(stack1, inputList, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacksStrict(stack1, inputListUneven, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict multiplication of a collection to a stack using indices.
     */
    public void testStackStrictCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigDecimal stack1 = stacks1.get(k);
			List<BigDecimal> inputList = stacks2.get(k).first().first().data();
			List<BigDecimal> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigDecimal resultStack = new StackBigDecimal(stack1.rows(), stack1.columns());
			StackBigDecimal resultStackUneven = new StackBigDecimal(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigDecimal> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigDecimal plate = stackIter1.next();
	    		
	    		WellSetBigDecimal set = plate.dataSet();
	    		WellSetBigDecimal result = new WellSetBigDecimal();
	    		WellSetBigDecimal resultUneven = new WellSetBigDecimal();
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(inputList.get(index), mc));
	    			}

	    			result.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigDecimal well : set) {
	    			
	    			int index;
	    			List<BigDecimal> list = well.data();
	    			List<BigDecimal> wellResult = new ArrayList<BigDecimal>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).multiply(inputListUneven.get(index), mc));
	    			}

	    			resultUneven.add(new WellBigDecimal(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigDecimal resultPlate = new PlateBigDecimal(plate.rows(), plate.columns(), result);
	    		PlateBigDecimal resultUnevenPlate = new PlateBigDecimal(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigDecimal returned = multiplication.stacksStrict(stack1, inputList, begin, end - begin, mc);
	    	StackBigDecimal returnedUneven = multiplication.stacksStrict(stack1, inputListUneven, begin, end - begin, mc);
	    	
	    	Iterator<PlateBigDecimal> iter1 = resultStack.iterator();
	    	Iterator<PlateBigDecimal> iter2 = returned.iterator();
	    	Iterator<PlateBigDecimal> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigDecimal> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iter1.next();
	    		PlateBigDecimal plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigDecimal set1 = plate1.dataSet();
	    		WellSetBigDecimal set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigDecimal> set1Iter = set1.iterator();
	    		Iterator<WellBigDecimal> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigDecimal well1 = set1Iter.next();
	    			WellBigDecimal well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigDecimal plate1 = iterUneven1.next();
	    		PlateBigDecimal plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }

    /*---------------------------- Helper methods ----------------------------*/
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length and returns the result.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    set of equal length
     * @param    WellSetBigDecimal    set of unequal length
     * @param    boolean              strict operation when true
     * @return                        result of two equal sets at index 0
     *                                result of two unequal sets at index 1
     */
    private WellSetBigDecimal[] set(WellSetBigDecimal set1, WellSetBigDecimal set2, WellSetBigDecimal uneven, boolean strict) {

    	WellSetBigDecimal finalResult = new WellSetBigDecimal();
    	WellSetBigDecimal finalResultUneven = new WellSetBigDecimal();
    	WellSetBigDecimal[] finalResultReturn = new WellSetBigDecimal[2];
    			
    	WellSetBigDecimal clone1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal clone2 = new WellSetBigDecimal(set2);
    	WellSetBigDecimal cloneUneven1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal cloneUneven2 = new WellSetBigDecimal(uneven);
    	
    	WellSetBigDecimal excluded1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal excluded2 = new WellSetBigDecimal(set2);
    	WellSetBigDecimal excludedUneven1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal excludedUneven2 = new WellSetBigDecimal(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellBigDecimal> iter1 = clone1.iterator();
    	Iterator<WellBigDecimal> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellBigDecimal well1 = iter1.next();
			WellBigDecimal well2 = iter2.next();
			
			List<BigDecimal> list1 = well1.data();
			List<BigDecimal> list2 = well2.data();

			List<BigDecimal> result = new ArrayList<BigDecimal>();
				
			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k).multiply(list2.get(k), mc));
			}
	
			finalResult.add(new WellBigDecimal(well1.row(), well1.column(), result));
		}
		
		Iterator<WellBigDecimal> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellBigDecimal> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellBigDecimal well1 = iterUneven1.next();
			WellBigDecimal well2 = iterUneven2.next();
			
			List<BigDecimal> list1 = well1.data();
			List<BigDecimal> list2 = well2.data();
	
			List<BigDecimal> result = new ArrayList<BigDecimal>();

			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k).multiply(list2.get(k), mc));
			}

			if(!strict) {
			    for(int j = list1.size(); j < list2.size(); j++) {
		            result.add(list2.get(j));
			    }
			}
			
			finalResultUneven.add(new WellBigDecimal(well1.row(), well1.column(), result));
		}
		
        if(!strict) {
			
			finalResult.add(excluded1);
			finalResult.add(excluded2);
			
		    finalResultUneven.add(excludedUneven1);
		    finalResultUneven.add(excludedUneven2);
		}
		
		finalResultReturn[0] = finalResult;
		finalResultReturn[1] = finalResultUneven;

		return finalResultReturn;
    }
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length using the values between the indices and returns the result.
     * @param    WellSetBigDecimal    the first set
     * @param    WellSetBigDecimal    set of equal length
     * @param    WellSetBigDecimal    set of unequal length
     * @param    int                  beginning index
     * @param    int                  ending index
     * @param    boolean              strict operation when true
     * @return                        result of two equal sets at index 0
     *                                result of two unequal sets at index 1
     */
    private WellSetBigDecimal[] set(WellSetBigDecimal set1, WellSetBigDecimal set2, WellSetBigDecimal uneven, int begin, int end, boolean strict) {

    	WellSetBigDecimal finalResult = new WellSetBigDecimal();
    	WellSetBigDecimal finalResultUneven = new WellSetBigDecimal();
    	WellSetBigDecimal[] finalResultReturn = new WellSetBigDecimal[2];
    			
    	WellSetBigDecimal clone1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal clone2 = new WellSetBigDecimal(set2);
    	WellSetBigDecimal cloneUneven1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal cloneUneven2 = new WellSetBigDecimal(uneven);
    	
    	WellSetBigDecimal excluded1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal excluded2 = new WellSetBigDecimal(set2);
    	WellSetBigDecimal excludedUneven1 = new WellSetBigDecimal(set1);
    	WellSetBigDecimal excludedUneven2 = new WellSetBigDecimal(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellBigDecimal> iter1 = clone1.iterator();
    	Iterator<WellBigDecimal> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellBigDecimal well1 = iter1.next();
			WellBigDecimal well2 = iter2.next();
			
			List<BigDecimal> list1 = well1.data();
			List<BigDecimal> list2 = well2.data();

			List<BigDecimal> result = new ArrayList<BigDecimal>();
				
			for(int k = begin; k < end; k++) {
				result.add(list1.get(k).multiply(list2.get(k), mc));
			}
	
			finalResult.add(new WellBigDecimal(well1.row(), well1.column(), result));
		}
		
		Iterator<WellBigDecimal> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellBigDecimal> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellBigDecimal well1 = iterUneven1.next();
			WellBigDecimal well2 = iterUneven2.next();
			
			List<BigDecimal> list1 = well1.data();
			List<BigDecimal> list2 = well2.data();
	
			List<BigDecimal> result = new ArrayList<BigDecimal>();

			for(int k = begin; k < end; k++) {
				result.add(list1.get(k).multiply(list2.get(k), mc));
			}
			
			finalResultUneven.add(new WellBigDecimal(well1.row(), well1.column(), result));
		}
		
		if(!strict) {
			
			for(WellBigDecimal well : excludedUneven1) {  
	    		well = well.subList(begin, end - begin);
	    	}
	    	
	    	for(WellBigDecimal well : excludedUneven2) {
	    		well = well.subList(begin, end - begin);
	    	}
	    	
			finalResult.add(excluded1);
			finalResult.add(excluded2);
			
		    finalResultUneven.add(excludedUneven1);
		    finalResultUneven.add(excludedUneven2);
		}
		
		finalResultReturn[0] = finalResult;
		finalResultReturn[1] = finalResultUneven;

		return finalResultReturn;
    }
}
