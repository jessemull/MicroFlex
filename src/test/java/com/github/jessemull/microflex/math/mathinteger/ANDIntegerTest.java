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

import org.apache.commons.lang3.ArrayUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.integerflex.math.ANDInteger;
import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the AND integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Dec 9, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ANDIntegerTest {
	
    /* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = -100000;          // Minimum integer value for wells
	private static int maxValue = 1000000;          // Maximum integer value for wells
	private static int minPlate = 10;               // Plate minimum
	private static int maxPlate = 25;               // Plate maximum
	private static Random random = new Random();    // Generates random integers
	
	/* The AND operation */
	
	private static ANDInteger and = new ANDInteger();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<PlateInteger[]> arrays1 = new ArrayList<PlateInteger[]>();
	private static List<PlateInteger[]> arrays2 = new ArrayList<PlateInteger[]>();
	private static List<PlateInteger[]> uneven = new ArrayList<PlateInteger[]>();
	private static List<StackInteger> stacks1 = new ArrayList<StackInteger>();
	private static List<StackInteger> stacks2 = new ArrayList<StackInteger>();
	private static List<StackInteger> stacksUneven = new ArrayList<StackInteger>();
	
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
    		
    		StackInteger stack1 = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length, "Plate1-" + i, plateNumber);
    		StackInteger stack2 = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length, "Plate2-" + i, plateNumber);
    		StackInteger stackUneven = RandomUtil.randomStackInteger(rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + i, plateNumber);
    		
    		stacks1.add(stack1);
    		stacks2.add(stack2);
    		stacksUneven.add(stackUneven);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateInteger[] array1 = new PlateInteger[plateNumber];
    		PlateInteger[] array2 = new PlateInteger[plateNumber];
    		PlateInteger[] unevenArray = new PlateInteger[plateNumber];
    		
    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateInteger.ROWS_48WELL + random.nextInt(PlateInteger.ROWS_1536WELL - 
    			       PlateInteger.ROWS_48WELL + 1);

    			columns =  PlateInteger.COLUMNS_48WELL + random.nextInt(PlateInteger.COLUMNS_1536WELL - 
    			           PlateInteger.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateInteger plate1 = RandomUtil.randomPlateInteger(
    					rows, columns, minValue, maxValue, length, "Plate1-" + j);
    			PlateInteger plate2 = RandomUtil.randomPlateInteger(
    					rows, columns, minValue, maxValue, length, "Plate2-" + j);
    			PlateInteger unevenPlate = RandomUtil.randomPlateInteger(
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
		ANDInteger test = new ANDInteger();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the AND operation using two wells.
     */
	@Test
    public void testWells() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], well2[j]);
    			List<Integer> returnedUneven = and.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
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
     * Tests the AND operation using two wells and indices.
     */
	@Test
    public void testWellsIndices() {
		
		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = and.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
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
     * Tests the strict AND operation using two wells.
     */
	@Test
    public void testWellsStrict() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wellsStrict(well1[j], well2[j]);
    			List<Integer> returnedUneven = and.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict AND operation using two wells and indices.
     */
	@Test
    public void testWellsStrictIndices() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = and.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the AND operation using a constant and a well.
     */
	@Test
    public void testWellConstant() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();

    		int constantInt = random.nextInt();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], constantInt);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & constantInt);
    			}

    			assertEquals(result, returned);
    		}
    		
    	}
    }
    
    /**
     * Tests the AND operation using an array and a well.
     */ 
	@Test
    public void testWellArray() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
    			int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], array);
    			List<Integer> returnedUneven = and.wells(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & array[k]);
    				resultUneven.add(list1.get(k) & arrayUneven[k]);
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
     * Tests the AND operation using an array, a well and indices.
     */
	@Test
    public void testWellArrayIndices() {

    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
    			int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], array, begin, end - begin);
    			List<Integer> returnedUneven = and.wells(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) & array[k]);
    				resultUneven.add(list1.get(k) & arrayUneven[k]);
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
     * Tests the AND operation using a collection and a well.
     */
	@Test
    public void testWellCollection() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], well2[j]);
    			List<Integer> returnedUneven = and.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
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
     * Tests the AND operation using a collection, a well and indices.
     */
    @Test
    public void testWellCollectionIndices() {


		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wells(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = and.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
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
     * Tests the strict AND operation using an array and a well.
     */
    @Test
    public void testWellStrictArray() {


		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
    			int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wellsStrict(well1[j], array);
    			List<Integer> returnedUneven = and.wellsStrict(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & array[k]);
    				resultUneven.add(list1.get(k) & arrayUneven[k]);
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict AND operation using an array, a well and indices.
     */
    @Test
    public void testWellStrictArrayIndices() {

    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			int[] array = ArrayUtils.toPrimitive(list2.toArray(new Integer[list2.size()]));
    			List<Integer> unevenList = wellUneven[j].data();
    			int[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Integer[unevenList.size()]));
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wellsStrict(well1[j], array, begin, end - begin);
    			List<Integer> returnedUneven = and.wellsStrict(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) & array[k]);
    				resultUneven.add(list1.get(k) & arrayUneven[k]);
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}	
    	}
    }
    
    /**
     * Tests the strict AND operation using a collection and a well.
     */
    @Test
    public void testWellStrictCollection() {

    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();
    			
    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wellsStrict(well1[j], well2[j]);
    			List<Integer> returnedUneven = and.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict AND operation using a collection and a well.
     */
    public void testWellStrictCollectionIndices() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Integer> list1 = well1[j].data();
    			List<Integer> list2 = well2[j].data();
    			List<Integer> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Integer> result = new ArrayList<Integer>();
    			List<Integer> resultUneven = new ArrayList<Integer>();
    			List<Integer> returned = and.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<Integer> returnedUneven = and.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) & list2.get(k));
    				resultUneven.add(list1.get(k) & unevenList.get(k));
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
     * Tests the AND operation using two plates.
     */
    @Test
    public void testPlates() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		PlateInteger plate2 = plates2[i];
    		PlateInteger plateUneven = unevenPlates[i];
    		
    		WellSetInteger set1 = plate1.dataSet();
    		WellSetInteger set2 = plate2.dataSet();
    		WellSetInteger setUneven = plateUneven.dataSet();
    		
    		WellSetInteger[] result = this.set(set1, set2, setUneven, false);
    		PlateInteger returnedPlate = and.plates(plate1, plate2);
    		PlateInteger returnedPlateUneven = and.plates(plate1, plateUneven);
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the AND operation using two plates and indices.
     */
    @Test
    public void testPlatesIndices() {
    	
		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		PlateInteger plate2 = plates2[i];
    		PlateInteger plateUneven = unevenPlates[i];
    		
    		WellSetInteger set1 = plate1.dataSet();
    		WellSetInteger set2 = plate2.dataSet();
    		WellSetInteger setUneven = plateUneven.dataSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetInteger[] result = this.set(set1, set2, setUneven, begin, end, false);
    		PlateInteger returnedPlate = and.plates(plate1, plate2, begin, end - begin);
    		PlateInteger returnedPlateUneven = and.plates(plate1, plateUneven, begin, end - begin);
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using two plates.
     */
    public void testPlatesStrict() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		PlateInteger plate2 = plates2[i];
    		PlateInteger plateUneven = unevenPlates[i];
    		
    		WellSetInteger set1 = plate1.dataSet();
    		WellSetInteger set2 = plate2.dataSet();
    		WellSetInteger setUneven = plateUneven.dataSet();
    		
    		WellSetInteger[] result = this.set(set1, set2, setUneven, true);
    		PlateInteger returnedPlate = and.platesStrict(plate1, plate2);
    		PlateInteger returnedPlateUneven = and.platesStrict(plate1, plateUneven);
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the strict AND operation using two plates and indices.
     */
    @Test
    public void testPlatesStrictIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		PlateInteger plate2 = plates2[i];
    		PlateInteger plateUneven = unevenPlates[i];
    		
    		WellSetInteger set1 = plate1.dataSet();
    		WellSetInteger set2 = plate2.dataSet();
    		WellSetInteger setUneven = plateUneven.dataSet();

    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetInteger[] result = this.set(set1, set2, setUneven, begin, end, true);
    		PlateInteger returnedPlate = and.platesStrict(plate1, plate2, begin, end - begin);
    		PlateInteger returnedPlateUneven = and.platesStrict(plate1, plateUneven, begin, end - begin);
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the AND operation using a constant and a plate.
     */
    @Test
    public void testPlateConstant() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);

    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];

    		WellSetInteger set1 = plate1.dataSet();
    		
    		int min = 1000;
    		int max = 10000;
    		
    		int randomInt =  min + (max - min) * random.nextInt();

    	    WellSetInteger result = new WellSetInteger();
    	    
    		for(WellInteger well : set1) {
    			
    			List<Integer> list = new ArrayList<Integer>();
    			
    			for(int in : well.data()) {
    				list.add(in & randomInt);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), list));
    		}

    		PlateInteger returnedPlate = and.plates(plate1, randomInt);
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		
    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returnedPlate.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);
    	}
    }
    
    /**
     * Tests the AND operation using an array and a plate.
     */
    @Test
    public void testPlateArray() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateInteger returned = and.plates(plate1, array);
    		PlateInteger returnedUneven = and.plates(plate1, arrayUneven);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the AND operation using an array, a plate and indices.
     */
    @Test
    public void testPlateArrayIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
           for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateInteger returned = and.plates(plate1, array, begin, end - begin);
    		PlateInteger returnedUneven = and.plates(plate1, arrayUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the AND operation using a collection and a plate.
     */
    @Test
    public void testPlateCollection() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateInteger returned = and.plates(plate1, inputList);
    		PlateInteger returnedUneven = and.plates(plate1, inputListUneven);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the AND operation using a collection, a plate and indices.
     */
    @Test
    public void testPlateCollectionIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateInteger returned = and.plates(plate1, inputList, begin, end - begin);
    		PlateInteger returnedUneven = and.plates(plate1, inputListUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict AND operation using an array and a plate.
     */
    @Test
    public void testPlateStrictArray() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateInteger returned = and.platesStrict(plate1, array);
    		PlateInteger returnedUneven = and.platesStrict(plate1, arrayUneven);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using an array, a plate and indices.
     */
    @Test
    public void testPlateStrictArrayIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    	    PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
            for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateInteger returned = and.platesStrict(plate1, array, begin, end - begin);
    		PlateInteger returnedUneven = and.platesStrict(plate1, arrayUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}    	
    }
    
    /**
     * Tests the strict AND operation using a collection and a plate.
     */
    @Test
    public void testPlateStrictCollection() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateInteger returned = and.platesStrict(plate1, inputList);
    		PlateInteger returnedUneven = and.platesStrict(plate1, inputListUneven);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using a collection, a plate and indices.
     */
    @Test
    public void testPlateStrictCollectionIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateInteger plate1 = plates1[i];
    		WellSetInteger set1 = plate1.dataSet();
    		
    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate1.columns(), result);
    		PlateInteger resultPlateUneven = new PlateInteger(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateInteger returned = and.platesStrict(plate1, inputList, begin, end - begin);
    		PlateInteger returnedUneven = and.platesStrict(plate1, inputListUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = resultPlate.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the AND operation using two sets.
     */
    @Test
    public void testSets() {
    	
		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();
    		WellSetInteger set2 = plates2[i].dataSet();
    		WellSetInteger setUneven = unevenPlates[i].dataSet();
    		
    		WellSetInteger[] result = this.set(set1, set2, setUneven, false);
    		WellSetInteger returned = and.sets(set1, set2);
    		WellSetInteger returnedUneven = and.sets(set1, setUneven);

    		Iterator<WellInteger> iter1 = result[0].iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the AND operation using two sets and indices.
     */
    @Test
    public void testSetsIndices() {

    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();
    		WellSetInteger set2 = plates2[i].dataSet();
    		WellSetInteger setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetInteger[] result = this.set(set1, set2, setUneven, begin, end, false);
    		WellSetInteger returned = and.sets(set1, set2, begin, end - begin);
    		WellSetInteger returnedUneven = and.sets(set1, setUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = result[0].iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict AND operation using two sets.
     */
    @Test
    public void testSetsStrict() {

		PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();
    		WellSetInteger set2 = plates2[i].dataSet();
    		WellSetInteger setUneven = unevenPlates[i].dataSet();
    		
    		WellSetInteger[] result = this.set(set1, set2, setUneven, true);
    		WellSetInteger returned = and.setsStrict(set1, set2);
    		WellSetInteger returnedUneven = and.setsStrict(set1, setUneven);

    		Iterator<WellInteger> iter1 = result[0].iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict AND operation using two sets and indices.
     */
    @Test
    public void testSetsStrictIndices() {

    	PlateInteger[] plates1 = arrays1.get(0);
		PlateInteger[] plates2 = arrays2.get(0);
		PlateInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();
    		WellSetInteger set2 = plates2[i].dataSet();
    		WellSetInteger setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetInteger[] result = this.set(set1, set2, setUneven, begin, end, true);
    		WellSetInteger returned = and.setsStrict(set1, set2, begin, end - begin);
    		WellSetInteger returnedUneven = and.setsStrict(set1, setUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = result[0].iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the AND operation using a constant and a set.
     */
    @Test
    public void testSetConstant() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set = plates1[i].dataSet();
    		WellSetInteger result = new WellSetInteger();

    		int min = 1000;
    		int max = 10000;
    		
    		int randomInt =  min + (max - min) * random.nextInt();

    		for(WellInteger well : set) {
    			
    			List<Integer> list = new ArrayList<Integer>();
    			
    			for(int in : well.data()) {
    				list.add(in & randomInt);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), list));
    		}

    		WellSetInteger returned = and.sets(set, randomInt);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();

    		while(iter1.hasNext()) {
    			
    			List<Integer> well1 = iter1.next().data();
    			List<Integer> well2 = iter2.next().data();
    			
    			assertEquals(well1, well2);
    			
    		}
    		
    		assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the AND operation using an array and a set.
     */
    @Test
    public void testSetArray() {   	

		PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetInteger returned = and.sets(set1, array);
    		WellSetInteger returnedUneven = and.sets(set1, arrayUneven);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the AND operation using an array, a set and indices.
     */
    @Test
    public void testSetArrayIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetInteger returned = and.sets(set1, array, begin, end - begin);
    		WellSetInteger returnedUneven = and.sets(set1, arrayUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the AND operation using a collection and a set.
     */
    @Test
    public void testSetCollection() {   

		PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetInteger returned = and.sets(set1, inputList);
    		WellSetInteger returnedUneven = and.sets(set1, inputListUneven);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the AND operation using a collection, a set and indices.
     */
    @Test
    public void testSetCollectionIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellInteger well : set1) {
    		
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
			
    		WellSetInteger returned = and.sets(set1, inputList, begin, end - begin);
    		WellSetInteger returnedUneven = and.sets(set1, inputListUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict AND operation using an array and a set.
     */
    @Test
    public void testSetStrictArray() {

		PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}

    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetInteger returned = and.setsStrict(set1, array);
    		WellSetInteger returnedUneven = and.setsStrict(set1, arrayUneven);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict AND operation using an array, a set and indices.
     */
    public void testSetStrictArrayIndices() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		int[] array = wellEven.toIntArray();
		int[] arrayUneven = wellUneven.toIntArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & array[index]);
    			}
    			
    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetInteger returned = and.setsStrict(set1, array, begin, end - begin);
    		WellSetInteger returnedUneven = and.setsStrict(set1, arrayUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using a collection and a set.
     */
    @Test
    public void testSetStrictCollection() {
    	
    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}

    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetInteger returned = and.setsStrict(set1, inputList);
    		WellSetInteger returnedUneven = and.setsStrict(set1, inputListUneven);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using a collection, a set and indices.
     */
    @Test
    public void testSetStrictCollectionIndices() {

    	PlateInteger[] plates1 = arrays1.get(0);
		WellInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<Integer> inputList = wellEven.data();
		List<Integer> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetInteger set1 = plates1[i].dataSet();

    		WellSetInteger result = new WellSetInteger();
    		WellSetInteger resultUneven = new WellSetInteger();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellInteger well : set1) {
    		
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputList.get(index));
    			}

    			result.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellInteger well : set1) {
    			
    			int index;
    			List<Integer> list = well.data();
    			List<Integer> wellResult = new ArrayList<Integer>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) & inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
    		}
			
    		WellSetInteger returned = and.setsStrict(set1, inputList, begin, end - begin);
    		WellSetInteger returnedUneven = and.setsStrict(set1, inputListUneven, begin, end - begin);

    		Iterator<WellInteger> iter1 = result.iterator();
    		Iterator<WellInteger> iter2 = returned.iterator();
    		Iterator<WellInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellInteger well1 = iter1.next();
    			WellInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellInteger well1 = iterUneven1.next();
    			WellInteger well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the AND operation using two stacks.
     */
    @Test
    public void testStacks() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			StackInteger stack2 = stacks2.get(k);
			StackInteger stackUneven = stacksUneven.get(k);

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			Iterator<PlateInteger> stackIter2 = stack2.iterator();
			Iterator<PlateInteger> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateInteger plate1 = stackIter1.next();
	    		PlateInteger plate2 = stackIter2.next();
	    		PlateInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		WellSetInteger setUneven = plateUneven.dataSet();
	    		
	    		WellSetInteger[] result = this.set(set1, set2, setUneven, false);
	    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
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
	    	
	    	StackInteger returned = and.stacks(stack1, stack2);
	    	StackInteger returnedUneven = and.stacks(stack1, stackUneven);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the AND operation using two stack and indices.
     */
    @Test
    public void testStacksIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			StackInteger stack2 = stacks2.get(k);
			StackInteger stackUneven = stacksUneven.get(k);

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			Iterator<PlateInteger> stackIter2 = stack2.iterator();
			Iterator<PlateInteger> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateInteger plate1 = stackIter1.next();
	    		PlateInteger plate2 = stackIter2.next();
	    		PlateInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		WellSetInteger setUneven = plateUneven.dataSet();

	    		WellSetInteger[] result = this.set(set1, set2, setUneven, begin, end, false);
	    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		for(WellInteger well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackIter2.hasNext()) {

	    		PlateInteger plate = stackIter2.next();
	    		
	    		for(WellInteger well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		
	    		PlateInteger plate = stackUnevenIter.next();
	    		
	    		for(WellInteger well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStackUneven.add(plate);
	    	}
	    	
	    	StackInteger returned = and.stacks(stack1, stack2, begin, end - begin);
	    	StackInteger returnedUneven = and.stacks(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using two stacks.
     */
    @Test
    public void testStacksStrict() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			StackInteger stack2 = stacks2.get(k);
			StackInteger stackUneven = stacksUneven.get(k);

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			Iterator<PlateInteger> stackIter2 = stack2.iterator();
			Iterator<PlateInteger> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateInteger plate1 = stackIter1.next();
	    		PlateInteger plate2 = stackIter2.next();
	    		PlateInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		WellSetInteger setUneven = plateUneven.dataSet();
	    		
	    		WellSetInteger[] result = this.set(set1, set2, setUneven, true);
	    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	
	    	StackInteger returned = and.stacksStrict(stack1, stack2);
	    	StackInteger returnedUneven = and.stacksStrict(stack1, stackUneven);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict AND operation using two stacks and indices.
     */
    @Test
    public void testStacksStrictIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			StackInteger stack2 = stacks2.get(k);
			StackInteger stackUneven = stacksUneven.get(k);

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			Iterator<PlateInteger> stackIter2 = stack2.iterator();
			Iterator<PlateInteger> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateInteger plate1 = stackIter1.next();
	    		PlateInteger plate2 = stackIter2.next();
	    		PlateInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		WellSetInteger setUneven = plateUneven.dataSet();

	    		WellSetInteger[] result = this.set(set1, set2, setUneven, begin, end, true);
	    		PlateInteger resultPlate = new PlateInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacksStrict(stack1, stack2, begin, end - begin);
	    	StackInteger returnedUneven = and.stacksStrict(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the AND operation using a constant and a stack.
     */
    @Test
    public void testStackConstant() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());

			int min = 1000;
    		int max = 10000;
    		
    		int randomInt =  min + (max - min) * random.nextInt();
    	    
			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();  
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns());

	    		for(WellInteger well : plate) {
	    			
	    			List<Integer> resultList = new ArrayList<Integer>();
	    			
	    			for(int in : well) {
	    				resultList.add(in & randomInt);
	    			}
	    			
	    			resultPlate.addWells(new WellInteger(well.row(), well.column(), resultList));
	    		}
	
	    		resultStack.add(resultPlate);
	    	}
	    	
	    	StackInteger returned = and.stacks(stack1, randomInt);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
    	}  	
    }
    
    /**
     * Tests the AND operation using an array and a stack.
     */
    @Test
    public void testStackArray() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index) & array[index]);
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index) & arrayUneven[index]);
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacks(stack1, array);
	    	StackInteger returnedUneven = and.stacks(stack1, arrayUneven);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the AND operation using an array, a stack and indices.
     */
    @Test
    public void testStackArrayIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & array[index]);
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length && j < end; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & arrayUneven[index]);
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length && j < end; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacks(stack1, array, begin, end - begin);
	    	StackInteger returnedUneven = and.stacks(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the AND operation using a collection and a stack.
     */
    @Test
    public void testStackCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index) & inputList.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size(); j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index) & inputListUneven.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size(); j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacks(stack1, inputList);
	    	StackInteger returnedUneven = and.stacks(stack1, inputListUneven);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the AND operation using a collection, a stack and indices.
     */
    @Test
    public void testStackCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & inputList.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size() && j < end; j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & inputListUneven.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacks(stack1, inputList, begin, end - begin);
	    	StackInteger returnedUneven = and.stacks(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict AND operation using an array and a stack.
     */
    @Test
    public void testStackStrictArray() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index) & array[index]);
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index) & arrayUneven[index]);
	    			}

	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacksStrict(stack1, array);
	    	StackInteger returnedUneven = and.stacksStrict(stack1, arrayUneven);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict AND operation using an array, a stack and indices.
     */
    @Test
    public void testStackStrictArrayIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			int[] array = stacks2.get(k).first().first().toIntArray();
			int[] arrayUneven = stacksUneven.get(k).first().first().toIntArray();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & array[index]);
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & arrayUneven[index]);
	    			}
	    			
	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacksStrict(stack1, array, begin, end - begin);
	    	StackInteger returnedUneven = and.stacksStrict(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict AND operation using a collection and a stack.
     */
    public void testStackStrictCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index) & inputList.get(index));
	    			}
	    			
	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index) & inputListUneven.get(index));
	    			}
	    			
	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacksStrict(stack1, inputList);
	    	StackInteger returnedUneven = and.stacksStrict(stack1, inputListUneven);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict AND operation using a collection, a stack and indices.
     */
    public void testStackStrictCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackInteger stack1 = stacks1.get(k);
			List<Integer> inputList = stacks2.get(k).first().first().data();
			List<Integer> inputListUneven = stacksUneven.get(k).first().first().data();

			StackInteger resultStack = new StackInteger(stack1.rows(), stack1.columns());
			StackInteger resultStackUneven = new StackInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateInteger plate = stackIter1.next();
	    		
	    		WellSetInteger set = plate.dataSet();
	    		WellSetInteger result = new WellSetInteger();
	    		WellSetInteger resultUneven = new WellSetInteger();
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & inputList.get(index));
	    			}

	    			result.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellInteger well : set) {
	    			
	    			int index;
	    			List<Integer> list = well.data();
	    			List<Integer> wellResult = new ArrayList<Integer>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) & inputListUneven.get(index));
	    			}

	    			resultUneven.add(new WellInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateInteger resultPlate = new PlateInteger(plate.rows(), plate.columns(), result);
	    		PlateInteger resultUnevenPlate = new PlateInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackInteger returned = and.stacksStrict(stack1, inputList, begin, end - begin);
	    	StackInteger returnedUneven = and.stacksStrict(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<PlateInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateInteger> iter2 = returned.iterator();
	    	Iterator<PlateInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateInteger plate1 = iter1.next();
	    		PlateInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetInteger set1 = plate1.dataSet();
	    		WellSetInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellInteger> set1Iter = set1.iterator();
	    		Iterator<WellInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellInteger well1 = set1Iter.next();
	    			WellInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateInteger plate1 = iterUneven1.next();
	    		PlateInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }

    /*---------------------------- Helper methods ----------------------------*/
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length and returns the result.
     * @param    WellSetInteger    the first set
     * @param    WellSetInteger    set of equal length
     * @param    WellSetInteger    set of unequal length
     * @param    boolean           strict operation when true
     * @return                     result of two equal sets at index 0
     *                             result of two unequal sets at index 1
     */
    private WellSetInteger[] set(WellSetInteger set1, WellSetInteger set2, WellSetInteger uneven, boolean strict) {

    	WellSetInteger finalResult = new WellSetInteger();
    	WellSetInteger finalResultUneven = new WellSetInteger();
    	WellSetInteger[] finalResultReturn = new WellSetInteger[2];
    			
    	WellSetInteger clone1 = new WellSetInteger(set1);
    	WellSetInteger clone2 = new WellSetInteger(set2);
    	WellSetInteger cloneUneven1 = new WellSetInteger(set1);
    	WellSetInteger cloneUneven2 = new WellSetInteger(uneven);
    	
    	WellSetInteger excluded1 = new WellSetInteger(set1);
    	WellSetInteger excluded2 = new WellSetInteger(set2);
    	WellSetInteger excludedUneven1 = new WellSetInteger(set1);
    	WellSetInteger excludedUneven2 = new WellSetInteger(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellInteger> iter1 = clone1.iterator();
    	Iterator<WellInteger> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellInteger well1 = iter1.next();
			WellInteger well2 = iter2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();

			List<Integer> result = new ArrayList<Integer>();
				
			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k) & list2.get(k));
			}
	
			finalResult.add(new WellInteger(well1.row(), well1.column(), result));
		}
		
		Iterator<WellInteger> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellInteger> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellInteger well1 = iterUneven1.next();
			WellInteger well2 = iterUneven2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();
	
			List<Integer> result = new ArrayList<Integer>();

			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k) & list2.get(k));
			}

			if(!strict) {
			    for(int j = list1.size(); j < list2.size(); j++) {
		            result.add(list2.get(j));
			    }
			}
			
			finalResultUneven.add(new WellInteger(well1.row(), well1.column(), result));
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
     * @param    WellSetInteger    the first set
     * @param    WellSetInteger    set of equal length
     * @param    WellSetInteger    set of unequal length
     * @param    int               beginning index
     * @param    int               ending index
     * @param    boolean           strict operation when true
     * @return                     result of two equal sets at index 0
     *                             result of two unequal sets at index 1
     */
    private WellSetInteger[] set(WellSetInteger set1, WellSetInteger set2, WellSetInteger uneven, int begin, int end, boolean strict) {

    	WellSetInteger finalResult = new WellSetInteger();
    	WellSetInteger finalResultUneven = new WellSetInteger();
    	WellSetInteger[] finalResultReturn = new WellSetInteger[2];
    			
    	WellSetInteger clone1 = new WellSetInteger(set1);
    	WellSetInteger clone2 = new WellSetInteger(set2);
    	WellSetInteger cloneUneven1 = new WellSetInteger(set1);
    	WellSetInteger cloneUneven2 = new WellSetInteger(uneven);
    	
    	WellSetInteger excluded1 = new WellSetInteger(set1);
    	WellSetInteger excluded2 = new WellSetInteger(set2);
    	WellSetInteger excludedUneven1 = new WellSetInteger(set1);
    	WellSetInteger excludedUneven2 = new WellSetInteger(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellInteger> iter1 = clone1.iterator();
    	Iterator<WellInteger> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellInteger well1 = iter1.next();
			WellInteger well2 = iter2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();

			List<Integer> result = new ArrayList<Integer>();
				
			for(int k = begin; k < end; k++) {
				result.add(list1.get(k) & list2.get(k));
			}
	
			finalResult.add(new WellInteger(well1.row(), well1.column(), result));
		}
		
		Iterator<WellInteger> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellInteger> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellInteger well1 = iterUneven1.next();
			WellInteger well2 = iterUneven2.next();
			
			List<Integer> list1 = well1.data();
			List<Integer> list2 = well2.data();
	
			List<Integer> result = new ArrayList<Integer>();

			for(int k = begin; k < end; k++) {
				result.add(list1.get(k) & list2.get(k));
			}
			
			finalResultUneven.add(new WellInteger(well1.row(), well1.column(), result));
		}
		
		if(!strict) {
			
			for(WellInteger well : excludedUneven1) {  
	    		well = well.subList(begin, end - begin);
	    	}
	    	
	    	for(WellInteger well : excludedUneven2) {
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
