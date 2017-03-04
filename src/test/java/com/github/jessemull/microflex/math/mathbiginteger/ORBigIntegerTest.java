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

import com.github.jessemull.microflex.bigintegerflex.math.ORBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the OR big integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Dec 9, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ORBigIntegerTest {
	
/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigInteger minValue = new BigInteger(-100000 + "");   // Minimum big integer value for wells
	private static BigInteger maxValue = new BigInteger(100000 + "");    // Maximum big integer value for wells
	private static int minPlate = 10;                                    // Plate minimum
	private static int maxPlate = 25;                                    // Plate maximum
	private static Random random = new Random();                         // Generates random integers
	
	/* The OR operation */
	
	private static ORBigInteger or = new ORBigInteger();
	
	/* Random objects and numbers for testing */

	private static int rows;
	private static int columns;
	private static int length;
	private static int plateNumber = 10;
	private static int arrayNumber = 5;
	private static int stackNumber = 5;
	private static List<PlateBigInteger[]> arrays1 = new ArrayList<PlateBigInteger[]>();
	private static List<PlateBigInteger[]> arrays2 = new ArrayList<PlateBigInteger[]>();
	private static List<PlateBigInteger[]> uneven = new ArrayList<PlateBigInteger[]>();
	private static List<StackBigInteger> stacks1 = new ArrayList<StackBigInteger>();
	private static List<StackBigInteger> stacks2 = new ArrayList<StackBigInteger>();
	private static List<StackBigInteger> stacksUneven = new ArrayList<StackBigInteger>();
	
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
    		
    		StackBigInteger stack1 = RandomUtil.randomStackBigInteger(rows, columns, minValue, maxValue, length, "Plate1-" + i, plateNumber);
    		StackBigInteger stack2 = RandomUtil.randomStackBigInteger(rows, columns, minValue, maxValue, length, "Plate2-" + i, plateNumber);
    		StackBigInteger stackUneven = RandomUtil.randomStackBigInteger(rows, columns, minValue, maxValue, length + length / 2, "Plate2-" + i, plateNumber);
    		
    		stacks1.add(stack1);
    		stacks2.add(stack2);
    		stacksUneven.add(stackUneven);
    	}
    	
    	for(int i = 0; i < arrayNumber; i++) {

    		PlateBigInteger[] array1 = new PlateBigInteger[plateNumber];
    		PlateBigInteger[] array2 = new PlateBigInteger[plateNumber];
    		PlateBigInteger[] unevenArray = new PlateBigInteger[plateNumber];
    		
    		for(int j = 0; j < array1.length; j++) {
    			
    			rows = PlateBigInteger.ROWS_48WELL + random.nextInt(PlateBigInteger.ROWS_1536WELL - 
    			       PlateBigInteger.ROWS_48WELL + 1);

    			columns =  PlateBigInteger.COLUMNS_48WELL + random.nextInt(PlateBigInteger.COLUMNS_1536WELL - 
    			           PlateBigInteger.COLUMNS_48WELL + 1);
    				
    			plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
    				
   		    	length = rows * columns / 5;
    			
    			PlateBigInteger plate1 = RandomUtil.randomPlateBigInteger(
    					rows, columns, minValue, maxValue, length, "Plate1-" + j);
    			PlateBigInteger plate2 = RandomUtil.randomPlateBigInteger(
    					rows, columns, minValue, maxValue, length, "Plate2-" + j);
    			PlateBigInteger unevenPlate = RandomUtil.randomPlateBigInteger(
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
		ORBigInteger test = new ORBigInteger();
		assertNotNull(test);
	}

	/* ----------------------------- Well Methods --------------------------- */
	
    /**
     * Tests the OR operation using two wells.
     */
	@Test
    public void testWells() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], well2[j]);
    			List<BigInteger> returnedUneven = or.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
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
     * Tests the OR operation using two wells and indices.
     */
	@Test
    public void testWellsIndices() {
		
		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], well2[j], begin, end - begin);
    			List<BigInteger> returnedUneven = or.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
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
     * Tests the strict OR operation using two wells.
     */
	@Test
    public void testWellsStrict() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wellsStrict(well1[j], well2[j]);
    			List<BigInteger> returnedUneven = or.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
    			}
    			
    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict OR operation using two wells and indices.
     */
	@Test
    public void testWellsStrictIndices() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<BigInteger> returnedUneven = or.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    	
    }
    
    /**
     * Tests the OR operation using a constant and a well.
     */
	@Test
    public void testWellConstant() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();

    		BigInteger constantBigInteger = new BigInteger(random.nextInt() + "");
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], constantBigInteger);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(constantBigInteger));
    			}

    			assertEquals(result, returned);
    		}
    		
    	}
    }
    
    /**
     * Tests the OR operation using an array and a well.
     */ 
	@Test
    public void testWellArray() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			BigInteger[] array = list2.toArray(new BigInteger[list2.size()]);
    			List<BigInteger> unevenList = wellUneven[j].data();
    			BigInteger[] arrayUneven = unevenList.toArray(new BigInteger[unevenList.size()]);
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], array);
    			List<BigInteger> returnedUneven = or.wells(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(array[k]));
    				resultUneven.add(list1.get(k).or(arrayUneven[k]));
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
     * Tests the OR operation using an array, a well and indices.
     */
	@Test
    public void testWellArrayIndices() {

    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			BigInteger[] array = list2.toArray(new BigInteger[list2.size()]);
    			List<BigInteger> unevenList = wellUneven[j].data();
    			BigInteger[] arrayUneven = unevenList.toArray(new BigInteger[unevenList.size()]);
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], array, begin, end - begin);
    			List<BigInteger> returnedUneven = or.wells(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).or(array[k]));
    				resultUneven.add(list1.get(k).or(arrayUneven[k]));
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
     * Tests the OR operation using a collection and a well.
     */
	@Test
    public void testWellCollection() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], well2[j]);
    			List<BigInteger> returnedUneven = or.wells(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
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
     * Tests the OR operation using a collection, a well and indices.
     */
    @Test
    public void testWellCollectionIndices() {


		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wells(well1[j], well2[j], begin, end - begin);
    			List<BigInteger> returnedUneven = or.wells(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
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
     * Tests the strict OR operation using an array and a well.
     */
    @Test
    public void testWellStrictArray() {


		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			BigInteger[] array = list2.toArray(new BigInteger[list2.size()]);
    			List<BigInteger> unevenList = wellUneven[j].data();
    			BigInteger[] arrayUneven = unevenList.toArray(new BigInteger[unevenList.size()]);
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wellsStrict(well1[j], array);
    			List<BigInteger> returnedUneven = or.wellsStrict(well1[j], arrayUneven);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(array[k]));
    				resultUneven.add(list1.get(k).or(arrayUneven[k]));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict OR operation using an array, a well and indices.
     */
    @Test
    public void testWellStrictArrayIndices() {

    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			BigInteger[] array = list2.toArray(new BigInteger[list2.size()]);
    			List<BigInteger> unevenList = wellUneven[j].data();
    			BigInteger[] arrayUneven = unevenList.toArray(new BigInteger[unevenList.size()]);
    			
    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wellsStrict(well1[j], array, begin, end - begin);
    			List<BigInteger> returnedUneven = or.wellsStrict(well1[j], arrayUneven, begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).or(array[k]));
    				resultUneven.add(list1.get(k).or(arrayUneven[k]));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}	
    	}
    }
    
    /**
     * Tests the strict OR operation using a collection and a well.
     */
    @Test
    public void testWellStrictCollection() {

    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();
    			
    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wellsStrict(well1[j], well2[j]);
    			List<BigInteger> returnedUneven = or.wellsStrict(well1[j], wellUneven[j]);
    			
    			for(int k = 0; k < list1.size(); k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
    			}

    			assertEquals(result, returned);
    			assertEquals(resultUneven, returnedUneven);
    		}
    		
    	}
    }
    
    /**
     * Tests the strict OR operation using a collection and a well.
     */
    public void testWellStrictCollectionIndices() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellBigInteger[] well1 = plates1[i].dataSet().toWellArray();
    		WellBigInteger[] well2 = plates2[i].dataSet().toWellArray();
    		WellBigInteger[] wellUneven = unevenPlates[i].dataSet().toWellArray();
    		
    		for(int j = 0; j < well1.length; j++) {
    			
    			List<BigInteger> list1 = well1[j].data();
    			List<BigInteger> list2 = well2[j].data();
    			List<BigInteger> unevenList = wellUneven[j].data();

    			int begin = 1 + random.nextInt(list1.size() - 1);
    			int end = begin + random.nextInt(list1.size() - begin);

    			List<BigInteger> result = new ArrayList<BigInteger>();
    			List<BigInteger> resultUneven = new ArrayList<BigInteger>();
    			List<BigInteger> returned = or.wellsStrict(well1[j], well2[j], begin, end - begin);
    			List<BigInteger> returnedUneven = or.wellsStrict(well1[j], wellUneven[j], begin, end - begin);
    			
    			for(int k = begin; k < end; k++) {
    				result.add(list1.get(k).or(list2.get(k)));
    				resultUneven.add(list1.get(k).or(unevenList.get(k)));
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
     * Tests the OR operation using two plates.
     */
    @Test
    public void testPlates() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		PlateBigInteger plate2 = plates2[i];
    		PlateBigInteger plateUneven = unevenPlates[i];
    		
    		WellSetBigInteger set1 = plate1.dataSet();
    		WellSetBigInteger set2 = plate2.dataSet();
    		WellSetBigInteger setUneven = plateUneven.dataSet();
    		
    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, false);
    		PlateBigInteger returnedPlate = or.plates(plate1, plate2);
    		PlateBigInteger returnedPlateUneven = or.plates(plate1, plateUneven);
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the OR operation using two plates and indices.
     */
    @Test
    public void testPlatesIndices() {
    	
		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		PlateBigInteger plate2 = plates2[i];
    		PlateBigInteger plateUneven = unevenPlates[i];
    		
    		WellSetBigInteger set1 = plate1.dataSet();
    		WellSetBigInteger set2 = plate2.dataSet();
    		WellSetBigInteger setUneven = plateUneven.dataSet();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, begin, end, false);
    		PlateBigInteger returnedPlate = or.plates(plate1, plate2, begin, end - begin);
    		PlateBigInteger returnedPlateUneven = or.plates(plate1, plateUneven, begin, end - begin);
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using two plates.
     */
    public void testPlatesStrict() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		PlateBigInteger plate2 = plates2[i];
    		PlateBigInteger plateUneven = unevenPlates[i];
    		
    		WellSetBigInteger set1 = plate1.dataSet();
    		WellSetBigInteger set2 = plate2.dataSet();
    		WellSetBigInteger setUneven = plateUneven.dataSet();
    		
    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, true);
    		PlateBigInteger returnedPlate = or.platesStrict(plate1, plate2);
    		PlateBigInteger returnedPlateUneven = or.platesStrict(plate1, plateUneven);
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the strict OR operation using two plates and indices.
     */
    @Test
    public void testPlatesStrictIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		PlateBigInteger plate2 = plates2[i];
    		PlateBigInteger plateUneven = unevenPlates[i];
    		
    		WellSetBigInteger set1 = plate1.dataSet();
    		WellSetBigInteger set2 = plate2.dataSet();
    		WellSetBigInteger setUneven = plateUneven.dataSet();

    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, begin, end, true);
    		PlateBigInteger returnedPlate = or.platesStrict(plate1, plate2, begin, end - begin);
    		PlateBigInteger returnedPlateUneven = or.platesStrict(plate1, plateUneven, begin, end - begin);
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
    		
    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returnedPlate.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUnevenPlate.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedPlateUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();
    
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUnevenPlate, returnedPlateUneven);
    	}
    }
    
    /**
     * Tests the OR operation using a constant and a plate.
     */
    @Test
    public void testPlateConstant() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);

    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];

    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		BigInteger min = new BigInteger("1000");
    		BigInteger max = new BigInteger("10000");
    		
    		BigInteger randomBigInteger = min.add(new BigInteger(random.nextInt() + "").multiply(max.subtract(min)));

    	    WellSetBigInteger result = new WellSetBigInteger();
    	    
    		for(WellBigInteger well : set1) {
    			
    			List<BigInteger> list = new ArrayList<BigInteger>();
    			
    			for(BigInteger bd : well.data()) {
    				list.add(bd.or(randomBigInteger));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), list));
    		}

    		PlateBigInteger returnedPlate = or.plates(plate1, randomBigInteger);
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		
    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returnedPlate.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returnedPlate);
    	}
    }
    
    /**
     * Tests the OR operation using an array and a plate.
     */
    @Test
    public void testPlateArray() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigInteger returned = or.plates(plate1, array);
    		PlateBigInteger returnedUneven = or.plates(plate1, arrayUneven);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the OR operation using an array, a plate and indices.
     */
    @Test
    public void testPlateArrayIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
           for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigInteger returned = or.plates(plate1, array, begin, end - begin);
    		PlateBigInteger returnedUneven = or.plates(plate1, arrayUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the OR operation using a collection and a plate.
     */
    @Test
    public void testPlateCollection() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigInteger returned = or.plates(plate1, inputList);
    		PlateBigInteger returnedUneven = or.plates(plate1, inputListUneven);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the OR operation using a collection, a plate and indices.
     */
    @Test
    public void testPlateCollectionIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateBigInteger returned = or.plates(plate1, inputList, begin, end - begin);
    		PlateBigInteger returnedUneven = or.plates(plate1, inputListUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict OR operation using an array and a plate.
     */
    @Test
    public void testPlateStrictArray() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigInteger returned = or.platesStrict(plate1, array);
    		PlateBigInteger returnedUneven = or.platesStrict(plate1, arrayUneven);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using an array, a plate and indices.
     */
    @Test
    public void testPlateStrictArrayIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    	    PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
            for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigInteger returned = or.platesStrict(plate1, array, begin, end - begin);
    		PlateBigInteger returnedUneven = or.platesStrict(plate1, arrayUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}    	
    }
    
    /**
     * Tests the strict OR operation using a collection and a plate.
     */
    @Test
    public void testPlateStrictCollection() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    		PlateBigInteger returned = or.platesStrict(plate1, inputList);
    		PlateBigInteger returnedUneven = or.platesStrict(plate1, inputListUneven);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using a collection, a plate and indices.
     */
    @Test
    public void testPlateStrictCollectionIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		PlateBigInteger plate1 = plates1[i];
    		WellSetBigInteger set1 = plate1.dataSet();
    		
    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int maxSize = inputList.size() < set1.first().size() ? inputList.size() : set1.first().size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
           for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate1.columns(), result);
    		PlateBigInteger resultPlateUneven = new PlateBigInteger(plate1.rows(), plate1.columns(), resultUneven);
    	
    		PlateBigInteger returned = or.platesStrict(plate1, inputList, begin, end - begin);
    		PlateBigInteger returnedUneven = or.platesStrict(plate1, inputListUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = resultPlate.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultPlateUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlate, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultPlateUneven, returnedUneven);
    	}
    }
    
    /* ----------------------------- Set Methods ---------------------------- */
    
    /**
     * Tests the OR operation using two sets.
     */
    @Test
    public void testSets() {
    	
		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();
    		WellSetBigInteger set2 = plates2[i].dataSet();
    		WellSetBigInteger setUneven = unevenPlates[i].dataSet();
    		
    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, false);
    		WellSetBigInteger returned = or.sets(set1, set2);
    		WellSetBigInteger returnedUneven = or.sets(set1, setUneven);

    		Iterator<WellBigInteger> iter1 = result[0].iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the OR operation using two sets and indices.
     */
    @Test
    public void testSetsIndices() {

    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();
    		WellSetBigInteger set2 = plates2[i].dataSet();
    		WellSetBigInteger setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, begin, end, false);
    		WellSetBigInteger returned = or.sets(set1, set2, begin, end - begin);
    		WellSetBigInteger returnedUneven = or.sets(set1, setUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = result[0].iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict OR operation using two sets.
     */
    @Test
    public void testSetsStrict() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();
    		WellSetBigInteger set2 = plates2[i].dataSet();
    		WellSetBigInteger setUneven = unevenPlates[i].dataSet();
    		
    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, true);
    		WellSetBigInteger returned = or.setsStrict(set1, set2);
    		WellSetBigInteger returnedUneven = or.setsStrict(set1, setUneven);

    		Iterator<WellBigInteger> iter1 = result[0].iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the strict OR operation using two sets and indices.
     */
    @Test
    public void testSetsStrictIndices() {

    	PlateBigInteger[] plates1 = arrays1.get(0);
		PlateBigInteger[] plates2 = arrays2.get(0);
		PlateBigInteger[] unevenPlates = uneven.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();
    		WellSetBigInteger set2 = plates2[i].dataSet();
    		WellSetBigInteger setUneven = unevenPlates[i].dataSet();
    
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;

    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, begin, end, true);
    		WellSetBigInteger returned = or.setsStrict(set1, set2, begin, end - begin);
    		WellSetBigInteger returnedUneven = or.setsStrict(set1, setUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = result[0].iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = result[1].iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();
    		
    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[0], returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result[1], returnedUneven);
    	}
    }
    
    /**
     * Tests the OR operation using a constant and a set.
     */
    @Test
    public void testSetConstant() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set = plates1[i].dataSet();
    		WellSetBigInteger result = new WellSetBigInteger();

    		BigInteger min = new BigInteger("1000");
    		BigInteger max = new BigInteger("10000");
    		
    		BigInteger randomBigInteger = min.add(new BigInteger(random.nextInt() + "").multiply(max.subtract(min)));

    		for(WellBigInteger well : set) {
    			
    			List<BigInteger> list = new ArrayList<BigInteger>();
    			
    			for(BigInteger bd : well.data()) {
    				list.add(bd.or(randomBigInteger));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), list));
    		}

    		WellSetBigInteger returned = or.sets(set, randomBigInteger);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();

    		while(iter1.hasNext()) {
    			
    			List<BigInteger> well1 = iter1.next().data();
    			List<BigInteger> well2 = iter2.next().data();
    			
    			assertEquals(well1, well2);
    			
    		}
    		
    		assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the OR operation using an array and a set.
     */
    @Test
    public void testSetArray() {   	

		PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigInteger returned = or.sets(set1, array);
    		WellSetBigInteger returnedUneven = or.sets(set1, arrayUneven);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the OR operation using an array, a set and indices.
     */
    @Test
    public void testSetArrayIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < array.length && j < end; j++) {
    				wellResult.add(array[j]);
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < arrayUneven.length && j < end; j++) {
    				wellResult.add(arrayUneven[j]);
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigInteger returned = or.sets(set1, array, begin, end - begin);
    		WellSetBigInteger returnedUneven = or.sets(set1, arrayUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the OR operation using a collection and a set.
     */
    @Test
    public void testSetCollection() {   

		PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size(); j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			for(int j = index; j < list.size(); j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size(); j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigInteger returned = or.sets(set1, inputList);
    		WellSetBigInteger returnedUneven = or.sets(set1, inputListUneven);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the OR operation using a collection, a set and indices.
     */
    @Test
    public void testSetCollectionIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellBigInteger well : set1) {
    		
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputList.size() && j < end; j++) {
    				wellResult.add(inputList.get(j));
    			}

    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			for(int j = index; j < list.size() && j < end; j++) {
    				wellResult.add(list.get(j));
    			}
    			
    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
    				wellResult.add(inputListUneven.get(j));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
			
    		WellSetBigInteger returned = or.sets(set1, inputList, begin, end - begin);
    		WellSetBigInteger returnedUneven = or.sets(set1, inputListUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict OR operation using an array and a set.
     */
    @Test
    public void testSetStrictArray() {

		PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < array.length; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}

    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigInteger returned = or.setsStrict(set1, array);
    		WellSetBigInteger returnedUneven = or.setsStrict(set1, arrayUneven);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /**
     * Tests the strict OR operation using an array, a set and indices.
     */
    public void testSetStrictArrayIndices() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		BigInteger[] array = wellEven.toBigIntegerArray();
		BigInteger[] arrayUneven = wellUneven.toBigIntegerArray();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int begin = 1 + random.nextInt(set1.first().size() - 1);
			int end = begin + random.nextInt(set1.first().size() - begin) + 1;
			
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < array.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(array[index]));
    			}
    			
    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < arrayUneven.length && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(arrayUneven[index]));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigInteger returned = or.setsStrict(set1, array, begin, end - begin);
    		WellSetBigInteger returnedUneven = or.setsStrict(set1, arrayUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using a collection and a set.
     */
    @Test
    public void testSetStrictCollection() {
    	
    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}

    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		

    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
    			
    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
    		
    		WellSetBigInteger returned = or.setsStrict(set1, inputList);
    		WellSetBigInteger returnedUneven = or.setsStrict(set1, inputListUneven);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using a collection, a set and indices.
     */
    @Test
    public void testSetStrictCollectionIndices() {

    	PlateBigInteger[] plates1 = arrays1.get(0);
		WellBigInteger wellEven = arrays2.get(0)[0].dataSet().first();
		WellBigInteger wellUneven = uneven.get(0)[0].dataSet().first();
		List<BigInteger> inputList = wellEven.data();
		List<BigInteger> inputListUneven = wellUneven.data();
		
    	for(int i = 0; i < plates1.length; i++) {
    		
    		WellSetBigInteger set1 = plates1[i].dataSet();

    		WellSetBigInteger result = new WellSetBigInteger();
    		WellSetBigInteger resultUneven = new WellSetBigInteger();
    		
    		int setSize = set1.first().size();
    		int maxSize = setSize < inputList.size() ? setSize : inputList.size();
    		
    		int begin = 1 + random.nextInt(maxSize - 1);
			int end = begin + random.nextInt(maxSize - begin) + 1;
			
    		for(WellBigInteger well : set1) {
    		
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputList.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputList.get(index)));
    			}

    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
 		
    		for(WellBigInteger well : set1) {
    			
    			int index;
    			List<BigInteger> list = well.data();
    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
    					index < end; index++) {
    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
    			}
    			
    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
    		}
			
    		WellSetBigInteger returned = or.setsStrict(set1, inputList, begin, end - begin);
    		WellSetBigInteger returnedUneven = or.setsStrict(set1, inputListUneven, begin, end - begin);

    		Iterator<WellBigInteger> iter1 = result.iterator();
    		Iterator<WellBigInteger> iter2 = returned.iterator();
    		Iterator<WellBigInteger> iterUneven1 = resultUneven.iterator();
    		Iterator<WellBigInteger> iterUneven2 = returnedUneven.iterator();

    		while(iter1.hasNext()) {
    			
    			WellBigInteger well1 = iter1.next();
    			WellBigInteger well2 = iter2.next();

    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(result, returned);

    		while(iterUneven1.hasNext()) {
    			
    			WellBigInteger well1 = iterUneven1.next();
    			WellBigInteger well2 = iterUneven2.next();
  
    			assertEquals(well1.data(), well2.data());
    		}
    		
    		assertEquals(resultUneven, returnedUneven);
    	}
    }
    
    /* ---------------------------- Stack Methods --------------------------- */
    
    /**
     * Tests the OR operation using two stacks.
     */
    @Test
    public void testStacks() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			StackBigInteger stack2 = stacks2.get(k);
			StackBigInteger stackUneven = stacksUneven.get(k);

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			Iterator<PlateBigInteger> stackIter2 = stack2.iterator();
			Iterator<PlateBigInteger> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigInteger plate1 = stackIter1.next();
	    		PlateBigInteger plate2 = stackIter2.next();
	    		PlateBigInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		WellSetBigInteger setUneven = plateUneven.dataSet();
	    		
	    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, false);
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
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
	    	
	    	StackBigInteger returned = or.stacks(stack1, stack2);
	    	StackBigInteger returnedUneven = or.stacks(stack1, stackUneven);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the OR operation using two stack and indices.
     */
    @Test
    public void testStacksIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			StackBigInteger stack2 = stacks2.get(k);
			StackBigInteger stackUneven = stacksUneven.get(k);

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			Iterator<PlateBigInteger> stackIter2 = stack2.iterator();
			Iterator<PlateBigInteger> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigInteger plate1 = stackIter1.next();
	    		PlateBigInteger plate2 = stackIter2.next();
	    		PlateBigInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		WellSetBigInteger setUneven = plateUneven.dataSet();

	    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, begin, end, false);
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		for(WellBigInteger well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackIter2.hasNext()) {

	    		PlateBigInteger plate = stackIter2.next();
	    		
	    		for(WellBigInteger well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStack.add(plate);
	    	}
	    	
	    	while(stackUnevenIter.hasNext()) {
	    		
	    		PlateBigInteger plate = stackUnevenIter.next();
	    		
	    		for(WellBigInteger well : plate) {
	    			well = well.subList(begin, end - begin);
	    		}
	    		
	    		resultStackUneven.add(plate);
	    	}
	    	
	    	StackBigInteger returned = or.stacks(stack1, stack2, begin, end - begin);
	    	StackBigInteger returnedUneven = or.stacks(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using two stacks.
     */
    @Test
    public void testStacksStrict() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			StackBigInteger stack2 = stacks2.get(k);
			StackBigInteger stackUneven = stacksUneven.get(k);

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			Iterator<PlateBigInteger> stackIter2 = stack2.iterator();
			Iterator<PlateBigInteger> stackUnevenIter = stackUneven.iterator();
			
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigInteger plate1 = stackIter1.next();
	    		PlateBigInteger plate2 = stackIter2.next();
	    		PlateBigInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		WellSetBigInteger setUneven = plateUneven.dataSet();
	    		
	    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, true);
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	
	    	StackBigInteger returned = or.stacksStrict(stack1, stack2);
	    	StackBigInteger returnedUneven = or.stacksStrict(stack1, stackUneven);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict OR operation using two stacks and indices.
     */
    @Test
    public void testStacksStrictIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			StackBigInteger stack2 = stacks2.get(k);
			StackBigInteger stackUneven = stacksUneven.get(k);

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			Iterator<PlateBigInteger> stackIter2 = stack2.iterator();
			Iterator<PlateBigInteger> stackUnevenIter = stackUneven.iterator();
			
    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
    		
	    	while(stackIter1.hasNext() && stackIter2.hasNext() && stackUnevenIter.hasNext()) {
	    		
	    		PlateBigInteger plate1 = stackIter1.next();
	    		PlateBigInteger plate2 = stackIter2.next();
	    		PlateBigInteger plateUneven = stackUnevenIter.next();
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		WellSetBigInteger setUneven = plateUneven.dataSet();

	    		WellSetBigInteger[] result = this.set(set1, set2, setUneven, begin, end, true);
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate1.rows(), plate2.columns(), result[0]);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plateUneven.rows(), plateUneven.columns(), result[1]);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacksStrict(stack1, stack2, begin, end - begin);
	    	StackBigInteger returnedUneven = or.stacksStrict(stack1, stackUneven, begin, end - begin);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the OR operation using a constant and a stack.
     */
    @Test
    public void testStackConstant() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());

			BigInteger min = new BigInteger("1000");
    		BigInteger max = new BigInteger("10000");
    		
    		BigInteger randomBigInteger = min.add(new BigInteger(random.nextInt() + "").multiply(max.subtract(min)));
    	    
			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();  
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns());

	    		for(WellBigInteger well : plate) {
	    			
	    			List<BigInteger> resultList = new ArrayList<BigInteger>();
	    			
	    			for(BigInteger bd : well) {
	    				resultList.add(bd.or(randomBigInteger));
	    			}
	    			
	    			resultPlate.addWells(new WellBigInteger(well.row(), well.column(), resultList));
	    		}
	
	    		resultStack.add(resultPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacks(stack1, randomBigInteger);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
    	}  	
    }
    
    /**
     * Tests the OR operation using an array and a stack.
     */
    @Test
    public void testStackArray() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			BigInteger[] array = stacks2.get(k).first().first().toBigIntegerArray();
			BigInteger[] arrayUneven = stacksUneven.get(k).first().first().toBigIntegerArray();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index).or(array[index]));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index).or(arrayUneven[index]));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacks(stack1, array);
	    	StackBigInteger returnedUneven = or.stacks(stack1, arrayUneven);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the OR operation using an array, a stack and indices.
     */
    @Test
    public void testStackArrayIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			BigInteger[] array = stacks2.get(k).first().first().toBigIntegerArray();
			BigInteger[] arrayUneven = stacksUneven.get(k).first().first().toBigIntegerArray();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(array[index]));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < array.length && j < end; j++) {
	    				wellResult.add(array[j]);
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(arrayUneven[index]));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < arrayUneven.length && j < end; j++) {
	    				wellResult.add(arrayUneven[j]);
	    			}
	    			
	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacks(stack1, array, begin, end - begin);
	    	StackBigInteger returnedUneven = or.stacks(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the OR operation using a collection and a stack.
     */
    @Test
    public void testStackCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			List<BigInteger> inputList = stacks2.get(k).first().first().data();
			List<BigInteger> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index).or(inputList.get(index)));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size(); j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
	    			}
	    			
	    			for(int j = index; j < list.size(); j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size(); j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacks(stack1, inputList);
	    	StackBigInteger returnedUneven = or.stacks(stack1, inputListUneven);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the OR operation using a collection, a stack and indices.
     */
    @Test
    public void testStackCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			List<BigInteger> inputList = stacks2.get(k).first().first().data();
			List<BigInteger> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(inputList.get(index)));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputList.size() && j < end; j++) {
	    				wellResult.add(inputList.get(j));
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
	    			}
	    			
	    			for(int j = index; j < list.size() && j < end; j++) {
	    				wellResult.add(list.get(j));
	    			}
	    			
	    			for(int j = index; j < inputListUneven.size() && j < end; j++) {
	    				wellResult.add(inputListUneven.get(j));
	    			}
	    			
	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacks(stack1, inputList, begin, end - begin);
	    	StackBigInteger returnedUneven = or.stacks(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    	
    }
    
    /**
     * Tests the strict OR operation using an array and a stack.
     */
    @Test
    public void testStackStrictArray() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			BigInteger[] array = stacks2.get(k).first().first().toBigIntegerArray();
			BigInteger[] arrayUneven = stacksUneven.get(k).first().first().toBigIntegerArray();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < array.length; index++) {
	    				wellResult.add(list.get(index).or(array[index]));
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < arrayUneven.length; index++) {
	    				wellResult.add(list.get(index).or(arrayUneven[index]));
	    			}

	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacksStrict(stack1, array);
	    	StackBigInteger returnedUneven = or.stacksStrict(stack1, arrayUneven);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict OR operation using an array, a stack and indices.
     */
    @Test
    public void testStackStrictArrayIndices() {

    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			BigInteger[] array = stacks2.get(k).first().first().toBigIntegerArray();
			BigInteger[] arrayUneven = stacksUneven.get(k).first().first().toBigIntegerArray();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < array.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(array[index]));
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < arrayUneven.length && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(arrayUneven[index]));
	    			}
	    			
	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacksStrict(stack1, array, begin, end - begin);
	    	StackBigInteger returnedUneven = or.stacksStrict(stack1, arrayUneven, begin, end - begin);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict OR operation using a collection and a stack.
     */
    public void testStackStrictCollection() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			List<BigInteger> inputList = stacks2.get(k).first().first().data();
			List<BigInteger> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < inputList.size(); index++) {
	    				wellResult.add(list.get(index).or(inputList.get(index)));
	    			}
	    			
	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}	    		

	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();
	    			
	    			for(index = 0; index < list.size() && index < inputListUneven.size(); index++) {
	    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
	    			}
	    			
	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacksStrict(stack1, inputList);
	    	StackBigInteger returnedUneven = or.stacksStrict(stack1, inputListUneven);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }
    
    /**
     * Tests the strict OR operation using a collection, a stack and indices.
     */
    public void testStackStrictCollectionIndices() {
    	
    	for(int k = 0; k < stacks1.size(); k++) {

			StackBigInteger stack1 = stacks1.get(k);
			List<BigInteger> inputList = stacks2.get(k).first().first().data();
			List<BigInteger> inputListUneven = stacksUneven.get(k).first().first().data();

			StackBigInteger resultStack = new StackBigInteger(stack1.rows(), stack1.columns());
			StackBigInteger resultStackUneven = new StackBigInteger(stack1.rows(), stack1.columns());

    		int begin = 1 + random.nextInt(stack1.first().first().size() - 1);
			int end = begin + random.nextInt(stack1.first().first().size() - begin) + 1;
			
			Iterator<PlateBigInteger> stackIter1 = stack1.iterator();
			
	    	while(stackIter1.hasNext()) {
	    		
	    		PlateBigInteger plate = stackIter1.next();
	    		
	    		WellSetBigInteger set = plate.dataSet();
	    		WellSetBigInteger result = new WellSetBigInteger();
	    		WellSetBigInteger resultUneven = new WellSetBigInteger();
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < inputList.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(inputList.get(index)));
	    			}

	    			result.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		for(WellBigInteger well : set) {
	    			
	    			int index;
	    			List<BigInteger> list = well.data();
	    			List<BigInteger> wellResult = new ArrayList<BigInteger>();

	    			for(index = begin; index < list.size() && index < inputListUneven.size() && 
	    					index < end; index++) {
	    				wellResult.add(list.get(index).or(inputListUneven.get(index)));
	    			}

	    			resultUneven.add(new WellBigInteger(well.row(), well.column(), wellResult));
	    		}
	    		
	    		PlateBigInteger resultPlate = new PlateBigInteger(plate.rows(), plate.columns(), result);
	    		PlateBigInteger resultUnevenPlate = new PlateBigInteger(plate.rows(), plate.columns(), resultUneven);
	
	    		resultStack.add(resultPlate);
	    		resultStackUneven.add(resultUnevenPlate);
	    	}
	    	
	    	StackBigInteger returned = or.stacksStrict(stack1, inputList, begin, end - begin);
	    	StackBigInteger returnedUneven = or.stacksStrict(stack1, inputListUneven, begin, end - begin);
	    	
	    	Iterator<PlateBigInteger> iter1 = resultStack.iterator();
	    	Iterator<PlateBigInteger> iter2 = returned.iterator();
	    	Iterator<PlateBigInteger> iterUneven1 = resultStackUneven.iterator();
	    	Iterator<PlateBigInteger> iterUneven2 = returnedUneven.iterator();

	    	while(iter1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iter1.next();
	    		PlateBigInteger plate2 = iter2.next();
	    		
	    		assertEquals(plate1, plate2);
	    		
	    		WellSetBigInteger set1 = plate1.dataSet();
	    		WellSetBigInteger set2 = plate2.dataSet();
	    		
	    		assertEquals(set1, set2);
	    		
	    		Iterator<WellBigInteger> set1Iter = set1.iterator();
	    		Iterator<WellBigInteger> set2Iter = set2.iterator();
	    		
	    		while(set1Iter.hasNext()) {
	    			
	    			WellBigInteger well1 = set1Iter.next();
	    			WellBigInteger well2 = set2Iter.next();
	    			
	    			assertEquals(well1.data(), well2.data());
	    		}
	    	}
	    	
	    	while(iterUneven1.hasNext()) {
	    		
	    		PlateBigInteger plate1 = iterUneven1.next();
	    		PlateBigInteger plate2 = iterUneven2.next();
	    		
	    		assertEquals(plate1, plate2);
	    	}
    	}
    }

    /*---------------------------- Helper methods ----------------------------*/
    
    /**
     * Performs a mathematical operation on two sets of equal length and two sets
     * of unequal length and returns the result.
     * @param    WellSetBigInteger    the first set
     * @param    WellSetBigInteger    set of equal length
     * @param    WellSetBigInteger    set of unequal length
     * @param    boolean              strict operation when true
     * @return                        result of two equal sets at index 0
     *                                result of two unequal sets at index 1
     */
    private WellSetBigInteger[] set(WellSetBigInteger set1, WellSetBigInteger set2, WellSetBigInteger uneven, boolean strict) {

    	WellSetBigInteger finalResult = new WellSetBigInteger();
    	WellSetBigInteger finalResultUneven = new WellSetBigInteger();
    	WellSetBigInteger[] finalResultReturn = new WellSetBigInteger[2];
    			
    	WellSetBigInteger clone1 = new WellSetBigInteger(set1);
    	WellSetBigInteger clone2 = new WellSetBigInteger(set2);
    	WellSetBigInteger cloneUneven1 = new WellSetBigInteger(set1);
    	WellSetBigInteger cloneUneven2 = new WellSetBigInteger(uneven);
    	
    	WellSetBigInteger excluded1 = new WellSetBigInteger(set1);
    	WellSetBigInteger excluded2 = new WellSetBigInteger(set2);
    	WellSetBigInteger excludedUneven1 = new WellSetBigInteger(set1);
    	WellSetBigInteger excludedUneven2 = new WellSetBigInteger(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellBigInteger> iter1 = clone1.iterator();
    	Iterator<WellBigInteger> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellBigInteger well1 = iter1.next();
			WellBigInteger well2 = iter2.next();
			
			List<BigInteger> list1 = well1.data();
			List<BigInteger> list2 = well2.data();

			List<BigInteger> result = new ArrayList<BigInteger>();
				
			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k).or(list2.get(k)));
			}
	
			finalResult.add(new WellBigInteger(well1.row(), well1.column(), result));
		}
		
		Iterator<WellBigInteger> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellBigInteger> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellBigInteger well1 = iterUneven1.next();
			WellBigInteger well2 = iterUneven2.next();
			
			List<BigInteger> list1 = well1.data();
			List<BigInteger> list2 = well2.data();
	
			List<BigInteger> result = new ArrayList<BigInteger>();

			for(int k = 0; k < list1.size(); k++) {
				result.add(list1.get(k).or(list2.get(k)));
			}

			if(!strict) {
			    for(int j = list1.size(); j < list2.size(); j++) {
		            result.add(list2.get(j));
			    }
			}
			
			finalResultUneven.add(new WellBigInteger(well1.row(), well1.column(), result));
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
     * @param    WellSetBigInteger    the first set
     * @param    WellSetBigInteger    set of equal length
     * @param    WellSetBigInteger    set of unequal length
     * @param    int                  beginning index
     * @param    int                  ending index
     * @param    boolean              strict operation when true
     * @return                        result of two equal sets at index 0
     *                                result of two unequal sets at index 1
     */
    private WellSetBigInteger[] set(WellSetBigInteger set1, WellSetBigInteger set2, WellSetBigInteger uneven, int begin, int end, boolean strict) {

    	WellSetBigInteger finalResult = new WellSetBigInteger();
    	WellSetBigInteger finalResultUneven = new WellSetBigInteger();
    	WellSetBigInteger[] finalResultReturn = new WellSetBigInteger[2];
    			
    	WellSetBigInteger clone1 = new WellSetBigInteger(set1);
    	WellSetBigInteger clone2 = new WellSetBigInteger(set2);
    	WellSetBigInteger cloneUneven1 = new WellSetBigInteger(set1);
    	WellSetBigInteger cloneUneven2 = new WellSetBigInteger(uneven);
    	
    	WellSetBigInteger excluded1 = new WellSetBigInteger(set1);
    	WellSetBigInteger excluded2 = new WellSetBigInteger(set2);
    	WellSetBigInteger excludedUneven1 = new WellSetBigInteger(set1);
    	WellSetBigInteger excludedUneven2 = new WellSetBigInteger(uneven);

    	excluded1.remove(set2);
    	excluded2.remove(set1);
    	excludedUneven1.remove(uneven);
    	excludedUneven2.remove(set1);
    	
    	clone1.retain(set2);
    	clone2.retain(set1);
    	cloneUneven1.retain(uneven);
    	cloneUneven2.retain(set1);

    	Iterator<WellBigInteger> iter1 = clone1.iterator();
    	Iterator<WellBigInteger> iter2 = clone2.iterator();
    	
		while(iter1.hasNext()) {
			
			WellBigInteger well1 = iter1.next();
			WellBigInteger well2 = iter2.next();
			
			List<BigInteger> list1 = well1.data();
			List<BigInteger> list2 = well2.data();

			List<BigInteger> result = new ArrayList<BigInteger>();
				
			for(int k = begin; k < end; k++) {
				result.add(list1.get(k).or(list2.get(k)));
			}
	
			finalResult.add(new WellBigInteger(well1.row(), well1.column(), result));
		}
		
		Iterator<WellBigInteger> iterUneven1 = cloneUneven1.iterator();
    	Iterator<WellBigInteger> iterUneven2 = cloneUneven2.iterator();

		while(iterUneven1.hasNext()) {
			
			WellBigInteger well1 = iterUneven1.next();
			WellBigInteger well2 = iterUneven2.next();
			
			List<BigInteger> list1 = well1.data();
			List<BigInteger> list2 = well2.data();
	
			List<BigInteger> result = new ArrayList<BigInteger>();

			for(int k = begin; k < end; k++) {
				result.add(list1.get(k).or(list2.get(k)));
			}
			
			finalResultUneven.add(new WellBigInteger(well1.row(), well1.column(), result));
		}
		
		if(!strict) {
			
			for(WellBigInteger well : excludedUneven1) {  
	    		well = well.subList(begin, end - begin);
	    	}
	    	
	    	for(WellBigInteger well : excludedUneven2) {
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
