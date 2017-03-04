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

package com.github.jessemull.microflex.stat.statbigdecimal;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.stat.NBigDecimal;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the max big decimal class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NBigDecimalTest {


	/* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigDecimal minValue = new BigDecimal(0);     // Minimum big decimal value for wells
	private static BigDecimal maxValue = new BigDecimal(10);    // Nimum big decimal value for wells
	
	/* The addition operation */
	
	private static NBigDecimal n = new NBigDecimal();

	/* Random objects and numbers for testing */

	private static int rows = 5;
	private static int columns = 4;
	private static int length = 5;
	private static int lengthIndices = 10;
	private static int plateNumber = 10;
	private static int plateNumberIndices = 5;
	private static PlateBigDecimal[] array = new PlateBigDecimal[plateNumber];
	private static PlateBigDecimal[] arrayIndices =  new PlateBigDecimal[plateNumberIndices];
	
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

		for(int j = 0; j < array.length; j++) {

			PlateBigDecimal plate = RandomUtil.randomPlateBigDecimal(
					rows, columns, minValue, maxValue, length, "Plate1-" + j);

			array[j] = plate;
		}
		
		for(int j = 0; j < arrayIndices.length; j++) {
			
			PlateBigDecimal plateIndices = RandomUtil.randomPlateBigDecimal(
					rows, columns, minValue, maxValue, lengthIndices, "Plate1-" + j);

			arrayIndices[j] = plateIndices;		
		}
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
	/* ---------------------------- Constructors -----------------------------*/
	
	/**
	 * Tests the default constructor.
	 */
	@Test
	public void testConstructor() {
		NBigDecimal test = new NBigDecimal();
		assertNotNull(test);
	}
	
    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Tests the plate statistics method.
     */
	@Test
    public void testPlate() {
		
		for(PlateBigDecimal plate : array) {

    		Map<WellBigDecimal, Integer> resultMap = new TreeMap<WellBigDecimal, Integer>();
    		Map<WellBigDecimal, Integer> returnedMap = n.plate(plate);
    		
			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getN();

				resultMap.put(well, (int) result);
			}

			for(WellBigDecimal well : plate) {
				
				int result = resultMap.get(well);
				int returned = returnedMap.get(well);

				assertEquals(result, returned);
			}
		} 
    }
    
    

    /* --------------------- Aggregated plate statistics -------------------  */
    
    /**
     * Tests the aggregated plate statistics method.
     */
    @Test
    public void testAggregatedPlate() {
        
    	for(PlateBigDecimal plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		int aggregatedReturned = n.platesAggregated(plate);
    		
			for(WellBigDecimal well : plate) {

				for(BigDecimal bd : well) {
					resultList.add(bd.doubleValue());
				}

			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getN();

			assertEquals((int) resultAggregated, aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedPlateCollection() {

    	List<PlateBigDecimal> collection = Arrays.asList(array);
    	Map<PlateBigDecimal, Integer> aggregatedReturnedMap = n.platesAggregated(collection);
    	Map<PlateBigDecimal, Integer> aggregatedResultMap = new TreeMap<PlateBigDecimal, Integer>();
    	
    	for(PlateBigDecimal plate : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
			for(WellBigDecimal well : plate) {
				
				for(BigDecimal bd : well) {
					resultList.add(bd.doubleValue());
				}
				
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getN();

			aggregatedResultMap.put(plate, (int) resultAggregated);
		}
    	
    	for(PlateBigDecimal plate : collection) {
    		
    		int result = aggregatedResultMap.get(plate);
			int returned = aggregatedReturnedMap.get(plate);

			assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using an array.
     */
    @Test
    public void testAggregatedPlateArray() {

    	Map<PlateBigDecimal, Integer> aggregatedReturnedMap = n.platesAggregated(array);
    	Map<PlateBigDecimal, Integer> aggregatedResultMap = new TreeMap<PlateBigDecimal, Integer>();
    	
    	for(PlateBigDecimal plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		
			for(WellBigDecimal well : plate) {
				
				for(BigDecimal bd : well) {
					resultList.add(bd.doubleValue());
				}
				
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getN();
			
			aggregatedResultMap.put(plate, (int) resultAggregated);
		}
    	
    	for(PlateBigDecimal plate : array) {
    		
    		int result = aggregatedResultMap.get(plate);
			int returned = aggregatedReturnedMap.get(plate);
			
			assertEquals(result, returned);
    	}
    	
    }    
    
    
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Tests set calculation.
     */
    @Test
    public void testSet() {
    	
    	for(PlateBigDecimal plate : array) {

    		Map<WellBigDecimal, Integer> resultMap = new TreeMap<WellBigDecimal, Integer>();
    		Map<WellBigDecimal, Integer> returnedMap = n.set(plate.dataSet());
    		
			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getN();
	
				resultMap.put(well, (int) result);
			}

			for(WellBigDecimal well : plate) {
				
				int result = resultMap.get(well);
				int returned = returnedMap.get(well);

				assertEquals(result, returned);
			}
		} 
    	
    }
    
    

    /* ---------------------- Aggregated set statistics --------------------  */
    
    /**
     * Tests the aggregated plate statistics method.
     */
    @Test
    public void testAggregatedSet() {
        
    	for(PlateBigDecimal plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		int aggregatedReturned = n.setsAggregated(plate.dataSet());
    		
			for(WellBigDecimal well : plate) {
				
				for(BigDecimal bd : well) {
					resultList.add(bd.doubleValue());
				}
				
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getN();
		
			assertEquals((int) resultAggregated, aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedSetCollection() {

    	List<WellSetBigDecimal> collection = new ArrayList<WellSetBigDecimal>();
    	
    	for(PlateBigDecimal plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
    	Map<WellSetBigDecimal, Integer> aggregatedReturnedMap = n.setsAggregated(collection);
    	Map<WellSetBigDecimal, Integer> aggregatedResultMap = new TreeMap<WellSetBigDecimal, Integer>();
    	
    	for(WellSetBigDecimal set : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
			for(WellBigDecimal well : set) {
				
				for(BigDecimal bd : well) {
					resultList.add(bd.doubleValue());
				}
				
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getN();

			aggregatedResultMap.put(set, (int) resultAggregated);
		}
    	
    	for(WellSetBigDecimal set : collection) {
    		
    		int result = aggregatedResultMap.get(set);
			int returned = aggregatedReturnedMap.get(set);

			assertEquals(result, returned);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using an array.
     */
    @Test
    public void testAggregatedSetArray() {

    	WellSetBigDecimal[] setArray = new WellSetBigDecimal[array.length];
    	
    	for(int i = 0; i < setArray.length; i++) {
    		setArray[i] = array[i].dataSet();
    	}
    	
    	Map<WellSetBigDecimal, Integer> aggregatedReturnedMap = n.setsAggregated(setArray);
    	Map<WellSetBigDecimal, Integer> aggregatedResultMap = new TreeMap<WellSetBigDecimal, Integer>();
    	
    	for(WellSetBigDecimal set : setArray) {

    		List<Double> resultList = new ArrayList<Double>();
    		
			for(WellBigDecimal well : set) {
				
				for(BigDecimal bd : well) {
					resultList.add(bd.doubleValue());
				}
				
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getN();

			aggregatedResultMap.put(set, (int) resultAggregated);
		}
    	
    	for(WellSetBigDecimal set : setArray) {
    		
    		int result = aggregatedResultMap.get(set);
			int returned = aggregatedReturnedMap.get(set);

			assertEquals(result, returned);
    	}
    	
    }    
    
    
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Tests well calculation.
     */
    @Test
    public void testWell() {

		for(PlateBigDecimal plate : array) {

			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getN();
				double returned = n.well(well);		
				
				assertTrue(result == returned);
			}		
		}        
    }

}
