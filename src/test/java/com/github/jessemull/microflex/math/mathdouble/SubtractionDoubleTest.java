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

import org.apache.commons.lang3.ArrayUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.doubleflex.math.SubtractionDouble;
import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.StackDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the subtraction double class.
 * 
 * @author Jesse L. Mull
 * @update Updated Dec 9, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubtractionDoubleTest {
	
/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static double minValue = -100000;       // Minimum double value for wells
	private static double maxValue = 1000000;       // Maximum double value for wells
	private static int minPlate = 10;               // Plate minimum
	private static int maxPlate = 25;               // Plate maximum
	private static Random random = new Random();    // Generates random integers
	
	/* The subtraction operation */
	
	private static SubtractionDouble subtraction = new SubtractionDouble();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<PlateDouble[]> arrays1 = new ArrayList<PlateDouble[]>();
	private static List<PlateDouble[]> arrays2 = new ArrayList<PlateDouble[]>();
	private static List<PlateDouble[]> uneven = new ArrayList<PlateDouble[]>();
	private static List<StackDouble> stacks1 = new ArrayList<StackDouble>();
	private static List<StackDouble> stacks2 = new ArrayList<StackDouble>();
	private static List<StackDouble> stacksUneven = new ArrayList<StackDouble>();
	
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
    		
    		StackDouble stack1 = RandomUtil.randomStackDouble(rows, columns, minValue, maxValue, length, "Plate1-" + i, plateNumber);
    		StackDouble stack2 = RandomUtil.randomStackDouble(rows, columns, minValue, maxValue, length, "Plate2-" + i, plateNumber);
    		StackDouble stackUneven = RandomUtil.randomStackDouble(rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + i, plateNumber);
    		
    		stacks1.add(stack1);
    		stacks2.add(stack2);
    		stacksUneven.add(stackUneven);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateDouble[] array1 = new PlateDouble[plateNumber];
    		PlateDouble[] array2 = new PlateDouble[plateNumber];
    		PlateDouble[] unevenArray = new PlateDouble[plateNumber];
    		
    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateDouble.ROWS_48WELL + random.nextInt(PlateDouble.ROWS_1536WELL - 
    			       PlateDouble.ROWS_48WELL + 1);

    			columns =  PlateDouble.COLUMNS_48WELL + random.nextInt(PlateDouble.COLUMNS_1536WELL - 
    			           PlateDouble.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateDouble plate1 = RandomUtil.randomPlateDouble(
    					rows, columns, minValue, maxValue, length, "Plate1-" + j);
    			PlateDouble plate2 = RandomUtil.randomPlateDouble(
    					rows, columns, minValue, maxValue, length, "Plate2-" + j);
    			PlateDouble unevenPlate = RandomUtil.randomPlateDouble(
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
		SubtractionDouble test = new SubtractionDouble();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the subtraction of two wells.
     */
	@Test
    public void testWells() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], well2[j]);
    			List<Double> returnedUneven = subtraction.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
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
     * Tests the subtraction of two wells using indices.
     */
	@Test
    public void testWellsIndices() {
		
		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], well2[j], begin, end - begin);
    			List<Double> returnedUneven = subtraction.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
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
     * Tests the strict subtraction of two wells.
     */
	@Test
    public void testWellsStrict() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wellsStrict(well1[j], well2[j]);
    			List<Double> returnedUneven = subtraction.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict subtraction of two wells using indices.
     */
	@Test
    public void testWellsStrictIndices() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<Double> returnedUneven = subtraction.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the subtraction of a constant to a well.
     */
	@Test
    public void testWellConstant() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();

    		double constantDouble = random.nextDouble();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> result = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], constantDouble);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - constantDouble);
    			}

    			assertEquals(result, returned);
    		}
    		
    	}
    }
    
    /**
     * Tests the subtraction of an array to a well.
     */ 
	@Test
    public void testWellArray() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			double[] array = ArrayUtils.toPrimitive(list2.toArray(new Double[list2.size()]));
    			List<Double> unevenList = wellUneven[j].data();
    			double[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Double[unevenList.size()]));
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], array);
    			List<Double> returnedUneven = subtraction.wells(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - array[k]);
    				resultUneven.add(list1.get(k) - arrayUneven[k]);
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
     * Tests the subtraction of an array to a well using indices.
     */
	@Test
    public void testWellArrayIndices() {

    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			double[] array = ArrayUtils.toPrimitive(list2.toArray(new Double[list2.size()]));
    			List<Double> unevenList = wellUneven[j].data();
    			double[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Double[unevenList.size()]));
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], array, begin, end - begin);
    			List<Double> returnedUneven = subtraction.wells(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) - array[k]);
    				resultUneven.add(list1.get(k) - arrayUneven[k]);
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
     * Tests the subtraction of a collection to a well.
     */
	@Test
    public void testWellCollection() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], well2[j]);
    			List<Double> returnedUneven = subtraction.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
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
     * Tests the subtraction of a collection to a well using indices.
     */
    @Test
    public void testWellCollectionIndices() {


		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wells(well1[j], well2[j], begin, end - begin);
    			List<Double> returnedUneven = subtraction.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
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
     * Tests the strict subtraction of an array to a well.
     */
    @Test
    public void testWellStrictArray() {


		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			double[] array = ArrayUtils.toPrimitive(list2.toArray(new Double[list2.size()]));
    			List<Double> unevenList = wellUneven[j].data();
    			double[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Double[unevenList.size()]));
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wellsStrict(well1[j], array);
    			List<Double> returnedUneven = subtraction.wellsStrict(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - array[k]);
    				resultUneven.add(list1.get(k) - arrayUneven[k]);
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict subtraction of an array to a well using indices.
     */
    @Test
    public void testWellStrictArrayIndices() {

    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			double[] array = ArrayUtils.toPrimitive(list2.toArray(new Double[list2.size()]));
    			List<Double> unevenList = wellUneven[j].data();
    			double[] arrayUneven = ArrayUtils.toPrimitive(unevenList.toArray(new Double[unevenList.size()]));
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wellsStrict(well1[j], array, begin, end - begin);
    			List<Double> returnedUneven = subtraction.wellsStrict(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) - array[k]);
    				resultUneven.add(list1.get(k) - arrayUneven[k]);
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}	
    	}
    }
    
    /**
     * Tests the strict subtraction of a collection to a well.
     */
    @Test
    public void testWellStrictCollection() {

    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();
    			
    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wellsStrict(well1[j], well2[j]);
    			List<Double> returnedUneven = subtraction.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict subtraction of a collection to a well.
     */
    public void testWellStrictCollectionIndices() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellDouble[] well1 = plates1[i].dataSet().toWellArray();
    		WellDouble[] well2 = plates2[i].dataSet().toWellArray();
    		WellDouble[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<Double> list1 = well1[j].data();
    			List<Double> list2 = well2[j].data();
    			List<Double> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<Double> result = new ArrayList<Double>();
    			List<Double> resultUneven = new ArrayList<Double>();
    			List<Double> returned = subtraction.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<Double> returnedUneven = subtraction.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k) - list2.get(k));
    				resultUneven.add(list1.get(k) - unevenList.get(k));
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
     * Tests the subtraction of two plates.
     */
    @Test
    public void testPlates() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		PlateDouble plate2 = plates2[i];
    		PlateDouble plateUneven = unevenPlates[i];
    		
    		WellSetDouble set1 = plate1.dataSet();
    		WellSetDouble set2 = plate2.dataSet();
    		WellSetDouble setUneven = plateUneven.dataSet();
    		
    		WellSetDouble[] result = this.set(set1, set2, setUneven, false);
    		PlateDouble returnedPlate = subtraction.plates(plate1, plate2);
    		PlateDouble returnedPlateUneven = subtraction.plates(plate1, plateUneven);
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returnedPlate.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the subtraction of two plates using indices.
     */
    @Test
    public void testPlatesIndices() {
    	
		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		PlateDouble plate2 = plates2[i];
    		PlateDouble plateUneven = unevenPlates[i];
    		
    		WellSetDouble set1 = plate1.dataSet();
    		WellSetDouble set2 = plate2.dataSet();
    		WellSetDouble setUneven = plateUneven.dataSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetDouble[] result = this.set(set1, set2, setUneven, begin, end, false);
    		PlateDouble returnedPlate = subtraction.plates(plate1, plate2, begin, end - begin);
    		PlateDouble returnedPlateUneven = subtraction.plates(plate1, plateUneven, begin, end - begin);
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returnedPlate.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of two plates.
     */
    public void testPlatesStrict() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		PlateDouble plate2 = plates2[i];
    		PlateDouble plateUneven = unevenPlates[i];
    		
    		WellSetDouble set1 = plate1.dataSet();
    		WellSetDouble set2 = plate2.dataSet();
    		WellSetDouble setUneven = plateUneven.dataSet();
    		
    		WellSetDouble[] result = this.set(set1, set2, setUneven, true);
    		PlateDouble returnedPlate = subtraction.platesStrict(plate1, plate2);
    		PlateDouble returnedPlateUneven = subtraction.platesStrict(plate1, plateUneven);
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returnedPlate.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the strict subtraction of two plates using indices.
     */
    @Test
    public void testPlatesStrictIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		PlateDouble plate2 = plates2[i];
    		PlateDouble plateUneven = unevenPlates[i];
    		
    		WellSetDouble set1 = plate1.dataSet();
    		WellSetDouble set2 = plate2.dataSet();
    		WellSetDouble setUneven = plateUneven.dataSet();

    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetDouble[] result = this.set(set1, set2, setUneven, begin, end, true);
    		PlateDouble returnedPlate = subtraction.platesStrict(plate1, plate2, begin, end - begin);
    		PlateDouble returnedPlateUneven = subtraction.platesStrict(plate1, plateUneven, begin, end - begin);
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returnedPlate.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the subtraction of a constant to a plate.
     */
    @Test
    public void testPlateConstant() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);

    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];

    		WellSetDouble set1 = plate1.dataSet();
    		
    		double min = 1000;
    		double max = 10000; 

    		double randomDouble = min + (max - min) * random.nextDouble();

    	    WellSetDouble result = new WellSetDouble();
    	    
    		for(WellDouble well : set1) {
    			
    			List<Double> list = new ArrayList<Double>();
    			
    			for(double db : well.data()) {
    				list.add(db - randomDouble);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), list));
    		}

    		PlateDouble returnedPlate = subtraction.plates(plate1, randomDouble);
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		
    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returnedPlate.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);
    	}
    }
    
    /**
     * Tests the subtraction of an array to a plate.
     */
    @Test
    public void testPlateArray() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    		PlateDouble returned = subtraction.plates(plate1, array);
    		PlateDouble returnedUneven = subtraction.plates(plate1, arrayUneven);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the subtraction of an array to a plate using indices.
     */
    @Test
    public void testPlateArrayIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
           for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    		PlateDouble returned = subtraction.plates(plate1, array, begin, end - begin);
    		PlateDouble returnedUneven = subtraction.plates(plate1, arrayUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the subtraction of a collection to a plate.
     */
    @Test
    public void testPlateCollection() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    		PlateDouble returned = subtraction.plates(plate1, inputList);
    		PlateDouble returnedUneven = subtraction.plates(plate1, inputListUneven);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the subtraction of a collection to a plate using indices.
     */
    @Test
    public void testPlateCollectionIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateDouble returned = subtraction.plates(plate1, inputList, begin, end - begin);
    		PlateDouble returnedUneven = subtraction.plates(plate1, inputListUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict subtraction of an array to a plate.
     */
    @Test
    public void testPlateStrictArray() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    		PlateDouble returned = subtraction.platesStrict(plate1, array);
    		PlateDouble returnedUneven = subtraction.platesStrict(plate1, arrayUneven);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of an array to a plate using indices.
     */
    @Test
    public void testPlateStrictArrayIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    	    PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
            for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    		PlateDouble returned = subtraction.platesStrict(plate1, array, begin, end - begin);
    		PlateDouble returnedUneven = subtraction.platesStrict(plate1, arrayUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}    	
    }
    
    /**
     * Tests the strict subtraction of a collection to a plate.
     */
    @Test
    public void testPlateStrictCollection() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    		PlateDouble returned = subtraction.platesStrict(plate1, inputList);
    		PlateDouble returnedUneven = subtraction.platesStrict(plate1, inputListUneven);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of a collection to a plate using indices.
     */
    @Test
    public void testPlateStrictCollectionIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateDouble plate1 = plates1[i];
    		WellSetDouble set1 = plate1.dataSet();
    		
    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate1.columns(), result);
    		PlateDouble resultPlateUneven = new PlateDouble(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateDouble returned = subtraction.platesStrict(plate1, inputList, begin, end - begin);
    		PlateDouble returnedUneven = subtraction.platesStrict(plate1, inputListUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = resultPlate.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the subtraction of a two sets.
     */
    @Test
    public void testSets() {
    	
		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();
    		WellSetDouble set2 = plates2[i].dataSet();
    		WellSetDouble setUneven = unevenPlates[i].dataSet();
    		
    		WellSetDouble[] result = this.set(set1, set2, setUneven, false);
    		WellSetDouble returned = subtraction.sets(set1, set2);
    		WellSetDouble returnedUneven = subtraction.sets(set1, setUneven);

    		Iterator<WellDouble> iter1 = result[0].iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = result[1].iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the subtraction of two sets using indices.
     */
    @Test
    public void testSetsIndices() {

    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();
    		WellSetDouble set2 = plates2[i].dataSet();
    		WellSetDouble setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetDouble[] result = this.set(set1, set2, setUneven, begin, end, false);
    		WellSetDouble returned = subtraction.sets(set1, set2, begin, end - begin);
    		WellSetDouble returnedUneven = subtraction.sets(set1, setUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = result[0].iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = result[1].iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict subtraction of two sets.
     */
    @Test
    public void testSetsStrict() {

		PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();
    		WellSetDouble set2 = plates2[i].dataSet();
    		WellSetDouble setUneven = unevenPlates[i].dataSet();
    		
    		WellSetDouble[] result = this.set(set1, set2, setUneven, true);
    		WellSetDouble returned = subtraction.setsStrict(set1, set2);
    		WellSetDouble returnedUneven = subtraction.setsStrict(set1, setUneven);

    		Iterator<WellDouble> iter1 = result[0].iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = result[1].iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict subtraction of two sets using indices.
     */
    @Test
    public void testSetsStrictIndices() {

    	PlateDouble[] plates1 = arrays1.get(0);
		PlateDouble[] plates2 = arrays2.get(0);
		PlateDouble[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();
    		WellSetDouble set2 = plates2[i].dataSet();
    		WellSetDouble setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetDouble[] result = this.set(set1, set2, setUneven, begin, end, true);
    		WellSetDouble returned = subtraction.setsStrict(set1, set2, begin, end - begin);
    		WellSetDouble returnedUneven = subtraction.setsStrict(set1, setUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = result[0].iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = result[1].iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the subtraction of a constant to a set.
     */
    @Test
    public void testSetConstant() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set = plates1[i].dataSet();
    		WellSetDouble result = new WellSetDouble();

    		double min = 1000;
    		double max = 10000; 
    		
    		double randomDouble = min + (max - min) * random.nextDouble();

    		for(WellDouble well : set) {
    			
    			List<Double> list = new ArrayList<Double>();
    			
    			for(double db : well.data()) {
    				list.add(db - randomDouble);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), list));
    		}

    		WellSetDouble returned = subtraction.sets(set, randomDouble);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();

    		while(iter1.hasNext()) {
    			
    			List<Double> well1 = iter1.next().data();
    			List<Double> well2 = iter2.next().data();
    			
    			assertEquals(well1, well2);
    			
    		}
    		
    		assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the subtraction of an array to a set.
     */
    @Test
    public void testSetArray() {   	

		PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetDouble returned = subtraction.sets(set1, array);
    		WellSetDouble returnedUneven = subtraction.sets(set1, arrayUneven);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the subtraction of an array to a set using indices.
     */
    @Test
    public void testSetArrayIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetDouble returned = subtraction.sets(set1, array, begin, end - begin);
    		WellSetDouble returnedUneven = subtraction.sets(set1, arrayUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the subtraction of a collection to a set.
     */
    @Test
    public void testSetCollection() {   

		PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetDouble returned = subtraction.sets(set1, inputList);
    		WellSetDouble returnedUneven = subtraction.sets(set1, inputListUneven);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the subtraction of a collection to a set using indices.
     */
    @Test
    public void testSetCollectionIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellDouble well : set1) {
    		
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
			
    		WellSetDouble returned = subtraction.sets(set1, inputList, begin, end - begin);
    		WellSetDouble returnedUneven = subtraction.sets(set1, inputListUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict subtraction of an array to a set.
     */
    @Test
    public void testSetStrictArray() {

		PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}

    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetDouble returned = subtraction.setsStrict(set1, array);
    		WellSetDouble returnedUneven = subtraction.setsStrict(set1, arrayUneven);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict subtraction of an array to a set using indices.
     */
    public void testSetStrictArrayIndices() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		double[] array = wellEven.toDoubleArray();
		double[] arrayUneven = wellUneven.toDoubleArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - array[index]);
    			}
    			
    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - arrayUneven[index]);
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetDouble returned = subtraction.setsStrict(set1, array, begin, end - begin);
    		WellSetDouble returnedUneven = subtraction.setsStrict(set1, arrayUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of a collection to a set.
     */
    @Test
    public void testSetStrictCollection() {
    	
    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}

    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetDouble returned = subtraction.setsStrict(set1, inputList);
    		WellSetDouble returnedUneven = subtraction.setsStrict(set1, inputListUneven);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of a collection to a set using indices.
     */
    @Test
    public void testSetStrictCollectionIndices() {

    	PlateDouble[] plates1 = arrays1.get(0);
		WellDouble wellEven = arrays2.get(0)[0].dataSet().first();
		WellDouble wellUneven = uneven.get(0)[0].dataSet().first();
		List<Double> inputList = wellEven.data();
		List<Double> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetDouble set1 = plates1[i].dataSet();

    		WellSetDouble result = new WellSetDouble();
    		WellSetDouble resultUneven = new WellSetDouble();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellDouble well : set1) {
    		
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputList.get(index));
    			}

    			result.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellDouble well : set1) {
    			
    			int index;
    			List<Double> list = well.data();
    			List<Double> wellResult = new ArrayList<Double>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index) - inputListUneven.get(index));
    			}
    			
    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
    		}
			
    		WellSetDouble returned = subtraction.setsStrict(set1, inputList, begin, end - begin);
    		WellSetDouble returnedUneven = subtraction.setsStrict(set1, inputListUneven, begin, end - begin);

    		Iterator<WellDouble> iter1 = result.iterator();
    		Iterator<WellDouble> iter2 = returned.iterator();
    		Iterator<WellDouble> iterUneven1 = resultUneven.iterator();
    		Iterator<WellDouble> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellDouble well1 = iter1.next();
    			WellDouble well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellDouble well1 = iterUneven1.next();
    			WellDouble well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the subtraction of two stacks.
     */
    @Test
    public void testStacks() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			StackDouble stack2 = stacks2.get(k);
			StackDouble stackUneven = stacksUneven.get(k);

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			Iterator<PlateDouble> stackIter2 = stack2.iterator();
			Iterator<PlateDouble> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateDouble plate1 = stackIter1.next();
	    		PlateDouble plate2 = stackIter2.next();
	    		PlateDouble plateUneven = stackUnevenIter.next();
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		WellSetDouble setUneven = plateUneven.dataSet();
	    		
	    		WellSetDouble[] result = this.set(set1, set2, setUneven, false);
	    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
	
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
	    	
	    	StackDouble returned = subtraction.stacks(stack1, stack2);
	    	StackDouble returnedUneven = subtraction.stacks(stack1, stackUneven);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the subtraction of two stack using indices.
     */
    @Test
    public void testStacksIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			StackDouble stack2 = stacks2.get(k);
			StackDouble stackUneven = stacksUneven.get(k);

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			Iterator<PlateDouble> stackIter2 = stack2.iterator();
			Iterator<PlateDouble> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateDouble plate1 = stackIter1.next();
	    		PlateDouble plate2 = stackIter2.next();
	    		PlateDouble plateUneven = stackUnevenIter.next();
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		WellSetDouble setUneven = plateUneven.dataSet();

	    		WellSetDouble[] result = this.set(set1, set2, setUneven, begin, end, false);
	    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		for(WellDouble well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackIter2.hasNext()) {

	    		PlateDouble plate = stackIter2.next();
	    		
	    		for(WellDouble well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		
	    		PlateDouble plate = stackUnevenIter.next();
	    		
	    		for(WellDouble well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStackUneven.add(plate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacks(stack1, stack2, begin, end - begin);
	    	StackDouble returnedUneven = subtraction.stacks(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of two stacks.
     */
    @Test
    public void testStacksStrict() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			StackDouble stack2 = stacks2.get(k);
			StackDouble stackUneven = stacksUneven.get(k);

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			Iterator<PlateDouble> stackIter2 = stack2.iterator();
			Iterator<PlateDouble> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateDouble plate1 = stackIter1.next();
	    		PlateDouble plate2 = stackIter2.next();
	    		PlateDouble plateUneven = stackUnevenIter.next();
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		WellSetDouble setUneven = plateUneven.dataSet();
	    		
	    		WellSetDouble[] result = this.set(set1, set2, setUneven, true);
	    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	
	    	StackDouble returned = subtraction.stacksStrict(stack1, stack2);
	    	StackDouble returnedUneven = subtraction.stacksStrict(stack1, stackUneven);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict subtraction of two stacks using indices.
     */
    @Test
    public void testStacksStrictIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			StackDouble stack2 = stacks2.get(k);
			StackDouble stackUneven = stacksUneven.get(k);

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			Iterator<PlateDouble> stackIter2 = stack2.iterator();
			Iterator<PlateDouble> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateDouble plate1 = stackIter1.next();
	    		PlateDouble plate2 = stackIter2.next();
	    		PlateDouble plateUneven = stackUnevenIter.next();
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		WellSetDouble setUneven = plateUneven.dataSet();

	    		WellSetDouble[] result = this.set(set1, set2, setUneven, begin, end, true);
	    		PlateDouble resultPlate = new PlateDouble(plate1.rows(), plate2.columns(), result[0]);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacksStrict(stack1, stack2, begin, end - begin);
	    	StackDouble returnedUneven = subtraction.stacksStrict(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the subtraction of a constant to a stack.
     */
    @Test
    public void testStackConstant() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());

			double min = 1000;
    		double max = 10000; 
    		
    		double randomDouble = min + (max - min) * random.nextDouble();
    	    
			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();  
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns());

	    		for(WellDouble well : plate) {
	    			
	    			List<Double> resultList = new ArrayList<Double>();
	    			
	    			for(double db : well) {
	    				resultList.add(db - randomDouble);
	    			}
	    			
	    			resultPlate.addWells(new WellDouble(well.row(), well.column(), resultList));
	    		}
	
	    		resultStack.add(resultPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacks(stack1, randomDouble);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
    	}  	
    }
    
    /**
     * Tests the subtraction of an array to a stack.
     */
    @Test
    public void testStackArray() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			double[] array = stacks2.get(k).first().first().toDoubleArray();
			double[] arrayUneven = stacksUneven.get(k).first().first().toDoubleArray();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index) - array[index]);
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index) - arrayUneven[index]);
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacks(stack1, array);
	    	StackDouble returnedUneven = subtraction.stacks(stack1, arrayUneven);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the subtraction of an array to a stack using indices.
     */
    @Test
    public void testStackArrayIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			double[] array = stacks2.get(k).first().first().toDoubleArray();
			double[] arrayUneven = stacksUneven.get(k).first().first().toDoubleArray();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - array[index]);
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length && j < end; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - arrayUneven[index]);
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length && j < end; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacks(stack1, array, begin, end - begin);
	    	StackDouble returnedUneven = subtraction.stacks(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the subtraction of a collection to a stack.
     */
    @Test
    public void testStackCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			List<Double> inputList = stacks2.get(k).first().first().data();
			List<Double> inputListUneven = stacksUneven.get(k).first().first().data();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index) - inputList.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size(); j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index) - inputListUneven.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size(); j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacks(stack1, inputList);
	    	StackDouble returnedUneven = subtraction.stacks(stack1, inputListUneven);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the subtraction of a collection to a stack using indices.
     */
    @Test
    public void testStackCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			List<Double> inputList = stacks2.get(k).first().first().data();
			List<Double> inputListUneven = stacksUneven.get(k).first().first().data();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - inputList.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size() && j < end; j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - inputListUneven.get(index));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacks(stack1, inputList, begin, end - begin);
	    	StackDouble returnedUneven = subtraction.stacks(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict subtraction of an array to a stack.
     */
    @Test
    public void testStackStrictArray() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			double[] array = stacks2.get(k).first().first().toDoubleArray();
			double[] arrayUneven = stacksUneven.get(k).first().first().toDoubleArray();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index) - array[index]);
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index) - arrayUneven[index]);
	    			}

	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacksStrict(stack1, array);
	    	StackDouble returnedUneven = subtraction.stacksStrict(stack1, arrayUneven);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict subtraction of an array to a stack using indices.
     */
    @Test
    public void testStackStrictArrayIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			double[] array = stacks2.get(k).first().first().toDoubleArray();
			double[] arrayUneven = stacksUneven.get(k).first().first().toDoubleArray();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - array[index]);
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - arrayUneven[index]);
	    			}
	    			
	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacksStrict(stack1, array, begin, end - begin);
	    	StackDouble returnedUneven = subtraction.stacksStrict(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict subtraction of a collection to a stack.
     */
    public void testStackStrictCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			List<Double> inputList = stacks2.get(k).first().first().data();
			List<Double> inputListUneven = stacksUneven.get(k).first().first().data();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index) - inputList.get(index));
	    			}
	    			
	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index) - inputListUneven.get(index));
	    			}
	    			
	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacksStrict(stack1, inputList);
	    	StackDouble returnedUneven = subtraction.stacksStrict(stack1, inputListUneven);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict subtraction of a collection to a stack using indices.
     */
    public void testStackStrictCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackDouble stack1 = stacks1.get(k);
			List<Double> inputList = stacks2.get(k).first().first().data();
			List<Double> inputListUneven = stacksUneven.get(k).first().first().data();

			StackDouble resultStack = new StackDouble(stack1.rows(), stack1.columns());
			StackDouble resultStackUneven = new StackDouble(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateDouble> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateDouble plate = stackIter1.next();
	    		
	    		WellSetDouble set = plate.dataSet();
	    		WellSetDouble result = new WellSetDouble();
	    		WellSetDouble resultUneven = new WellSetDouble();
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - inputList.get(index));
	    			}

	    			result.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellDouble well : set) {
	    			
	    			int index;
	    			List<Double> list = well.data();
	    			List<Double> wellResult = new ArrayList<Double>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index) - inputListUneven.get(index));
	    			}

	    			resultUneven.add(new WellDouble(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateDouble resultPlate = new PlateDouble(plate.rows(), plate.columns(), result);
	    		PlateDouble resultUnevenPlate = new PlateDouble(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackDouble returned = subtraction.stacksStrict(stack1, inputList, begin, end - begin);
	    	StackDouble returnedUneven = subtraction.stacksStrict(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<PlateDouble> iter1 = resultStack.iterator();
	    	Iterator<PlateDouble> iter2 = returned.iterator();
	    	Iterator<PlateDouble> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateDouble> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateDouble plate1 = iter1.next();
	    		PlateDouble plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetDouble set1 = plate1.dataSet();
	    		WellSetDouble set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellDouble> set1Iter = set1.iterator();
	    		Iterator<WellDouble> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellDouble well1 = set1Iter.next();
	    			WellDouble well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateDouble plate1 = iterUneven1.next();
	    		PlateDouble plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }

    /*---------------------------- Helper methods ----------------------------*/
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length and returns the result.
     * @param    WellSetDouble    the first set
     * @param    WellSetDouble    set of equal length
     * @param    WellSetDouble    set of unequal length
     * @param    boolean          strict operation when true
     * @return                    result of two equal sets at index 0
     *                            result of two unequal sets at index 1
     */
    private WellSetDouble[] set(WellSetDouble set1, WellSetDouble set2, WellSetDouble uneven, boolean strict) {

    	WellSetDouble finalResult = new WellSetDouble();
    	WellSetDouble finalResultUneven = new WellSetDouble();
    	WellSetDouble[] finalResultReturn = new WellSetDouble[2];
    			
    	WellSetDouble clone1 = new WellSetDouble(set1);
    	WellSetDouble clone2 = new WellSetDouble(set2);
    	WellSetDouble cloneUneven1 = new WellSetDouble(set1);
    	WellSetDouble cloneUneven2 = new WellSetDouble(uneven);
    	
    	WellSetDouble excluded1 = new WellSetDouble(set1);
    	WellSetDouble excluded2 = new WellSetDouble(set2);
    	WellSetDouble excludedUneven1 = new WellSetDouble(set1);
    	WellSetDouble excludedUneven2 = new WellSetDouble(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellDouble> iter1 = clone1.iterator();
    	Iterator<WellDouble> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellDouble well1 = iter1.next();
			WellDouble well2 = iter2.next();
			
			List<Double> list1 = well1.data();
			List<Double> list2 = well2.data();

			List<Double> result = new ArrayList<Double>();
				
			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k) - list2.get(k));
			}
	
			finalResult.add(new WellDouble(well1.row(), well1.column(), result));
		}
		
		Iterator<WellDouble> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellDouble> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellDouble well1 = iterUneven1.next();
			WellDouble well2 = iterUneven2.next();
			
			List<Double> list1 = well1.data();
			List<Double> list2 = well2.data();
	
			List<Double> result = new ArrayList<Double>();

			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k) - list2.get(k));
			}

			if(!strict) {
			    for(int j = list1.size(); j < list2.size(); j++) {
		            result.add(list2.get(j));
			    }
			}
			
			finalResultUneven.add(new WellDouble(well1.row(), well1.column(), result));
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
     * @param    WellSetDouble    the first set
     * @param    WellSetDouble    set of equal length
     * @param    WellSetDouble    set of unequal length
     * @param    int              beginning index
     * @param    int              ending index
     * @param    boolean          strict operation when true
     * @return                    result of two equal sets at index 0
     *                            result of two unequal sets at index 1
     */
    private WellSetDouble[] set(WellSetDouble set1, WellSetDouble set2, WellSetDouble uneven, int begin, int end, boolean strict) {

    	WellSetDouble finalResult = new WellSetDouble();
    	WellSetDouble finalResultUneven = new WellSetDouble();
    	WellSetDouble[] finalResultReturn = new WellSetDouble[2];
    			
    	WellSetDouble clone1 = new WellSetDouble(set1);
    	WellSetDouble clone2 = new WellSetDouble(set2);
    	WellSetDouble cloneUneven1 = new WellSetDouble(set1);
    	WellSetDouble cloneUneven2 = new WellSetDouble(uneven);
    	
    	WellSetDouble excluded1 = new WellSetDouble(set1);
    	WellSetDouble excluded2 = new WellSetDouble(set2);
    	WellSetDouble excludedUneven1 = new WellSetDouble(set1);
    	WellSetDouble excludedUneven2 = new WellSetDouble(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellDouble> iter1 = clone1.iterator();
    	Iterator<WellDouble> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellDouble well1 = iter1.next();
			WellDouble well2 = iter2.next();
			
			List<Double> list1 = well1.data();
			List<Double> list2 = well2.data();

			List<Double> result = new ArrayList<Double>();
				
			for(int k = begin; k < end; k++) {
				result.add(list1.get(k) - list2.get(k));
			}
	
			finalResult.add(new WellDouble(well1.row(), well1.column(), result));
		}
		
		Iterator<WellDouble> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellDouble> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellDouble well1 = iterUneven1.next();
			WellDouble well2 = iterUneven2.next();
			
			List<Double> list1 = well1.data();
			List<Double> list2 = well2.data();
	
			List<Double> result = new ArrayList<Double>();

			for(int k = begin; k < end; k++) {
				result.add(list1.get(k) - list2.get(k));
			}
			
			finalResultUneven.add(new WellDouble(well1.row(), well1.column(), result));
		}
		
		if(!strict) {
			
			for(WellDouble well : excludedUneven1) {  
	    		well = well.subList(begin, end - begin);
	    	}
	    	
	    	for(WellDouble well : excludedUneven2) {
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
