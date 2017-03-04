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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.stat.MinBigDecimal;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the min big decimal class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MinBigDecimalTest {


	/* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigDecimal minValue = new BigDecimal(0);     // Minimum big decimal value for wells
	private static BigDecimal maxValue = new BigDecimal(10);    // Minimum big decimal value for wells
	private static Random random = new Random();                                    // Generates random integers
	private static MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);    // Math context for input values
	
	/* The addition operation */
	
	private static MinBigDecimal min = new MinBigDecimal();

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
		MinBigDecimal test = new MinBigDecimal();
		assertNotNull(test);
	}
	
    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Tests the plate statistics method.
     */
	@Test
    public void testPlate() {
		
		for(PlateBigDecimal plate : array) {

    		Map<WellBigDecimal, BigDecimal> resultMap = new TreeMap<WellBigDecimal, BigDecimal>();
    		Map<WellBigDecimal, BigDecimal> returnedMap = min.plate(plate);
    		
			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double resultDouble = stat.getMin();
				
				BigDecimal result = new BigDecimal(resultDouble);
				
				resultMap.put(well, result);
			}

			for(WellBigDecimal well : plate) {
				
				BigDecimal result = resultMap.get(well);
				BigDecimal returned = returnedMap.get(well);
				
				BigDecimal[] corrected = correctRoundingErrors(result, returned);
				
				assertEquals(corrected[0], corrected[1]);
			}
		} 
    }
    
    /**
     * Tests the plate statistics method using the values between the indices.
     */
    @Test
    public void testPlateIndices() {
    	
    	for(PlateBigDecimal plate : arrayIndices) {

    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		Map<WellBigDecimal, BigDecimal> resultMap = new TreeMap<WellBigDecimal, BigDecimal>();
    		Map<WellBigDecimal, BigDecimal> returnedMap = min.plate(plate, begin, end - begin);
    		
			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double resultDouble = stat.getMin();
				
				BigDecimal result = new BigDecimal(resultDouble);
				
				resultMap.put(well, result);
			}

			for(WellBigDecimal well : plate) {
				
				BigDecimal result = resultMap.get(well);
				BigDecimal returned = returnedMap.get(well);
				
				BigDecimal[] corrected = correctRoundingErrors(result, returned);
				
				assertEquals(corrected[0], corrected[1]);
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

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = min.platesAggregated(plate);
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			
			BigDecimal[] corrected = correctRoundingErrors(aggregatedResult, aggregatedReturned);		
			assertEquals(corrected[0], corrected[1]);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedPlateCollection() {

    	List<PlateBigDecimal> collection = Arrays.asList(array);
    	Map<PlateBigDecimal, BigDecimal> aggregatedReturnedMap = min.platesAggregated(collection);
    	Map<PlateBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigDecimal, BigDecimal>();
    	
    	for(PlateBigDecimal plate : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigDecimal plate : collection) {
    		
    		BigDecimal result = aggregatedResultMap.get(plate);
			BigDecimal returned = aggregatedReturnedMap.get(plate);
			
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using an array.
     */
    @Test
    public void testAggregatedPlateArray() {

    	Map<PlateBigDecimal, BigDecimal> aggregatedReturnedMap = min.platesAggregated(array);
    	Map<PlateBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigDecimal, BigDecimal>();
    	
    	for(PlateBigDecimal plate : array) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigDecimal plate : array) {
    		
    		BigDecimal result = aggregatedResultMap.get(plate);
			BigDecimal returned = aggregatedReturnedMap.get(plate);
			
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
    	}
    	
    }    
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices.
     */
    @Test
    public void testAggregatedPlateIndices() {
    	
    	for(PlateBigDecimal plate : arrayIndices) {

        	int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	    List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = min.platesAggregated(plate, begin, end - begin);
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			
			BigDecimal[] corrected = correctRoundingErrors(aggregatedResult, aggregatedReturned);	
			assertEquals(corrected[0], corrected[1]);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the collection.
     */
    @Test
    public void testAggregatedPlateCollectionIndices() {
    	
    	List<PlateBigDecimal> collection = Arrays.asList(arrayIndices);
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	Map<PlateBigDecimal, BigDecimal> aggregatedReturnedMap = min.platesAggregated(arrayIndices, begin, end - begin);
    	Map<PlateBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigDecimal, BigDecimal>();
    	
    	for(PlateBigDecimal plate : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigDecimal plate : arrayIndices) {

    		BigDecimal result = aggregatedResultMap.get(plate);
			BigDecimal returned = aggregatedReturnedMap.get(plate);
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the array.
     */
    @Test
    public void testAggregatedPlateArrayIndices() {
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	Map<PlateBigDecimal, BigDecimal> aggregatedReturnedMap = min.platesAggregated(arrayIndices, begin, end - begin);
    	Map<PlateBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigDecimal, BigDecimal>();
    	
    	for(PlateBigDecimal plate : arrayIndices) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigDecimal plate : arrayIndices) {
    		
    		BigDecimal result = aggregatedResultMap.get(plate);
			BigDecimal returned = aggregatedReturnedMap.get(plate);
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
    	}
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Tests set calculation.
     */
    @Test
    public void testSet() {
    	
    	for(PlateBigDecimal plate : array) {

    		Map<WellBigDecimal, BigDecimal> resultMap = new TreeMap<WellBigDecimal, BigDecimal>();
    		Map<WellBigDecimal, BigDecimal> returnedMap = min.set(plate.dataSet());
    		
			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double resultDouble = stat.getMin();
				
				BigDecimal result = new BigDecimal(resultDouble);
				
				resultMap.put(well, result);
			}

			for(WellBigDecimal well : plate) {
				
				BigDecimal result = resultMap.get(well);
				BigDecimal returned = returnedMap.get(well);
				
				BigDecimal[] corrected = correctRoundingErrors(result, returned);
				
				assertEquals(corrected[0], corrected[1]);
			}
		} 
    	
    }
    
    /**
     * Tests set calculation using indices.
     */
    @Test
    public void testSetIndices() {
        
    	for(PlateBigDecimal plate : arrayIndices) {

    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		Map<WellBigDecimal, BigDecimal> resultMap = new TreeMap<WellBigDecimal, BigDecimal>();
    		Map<WellBigDecimal, BigDecimal> returnedMap = min.set(plate.dataSet(), begin, end - begin);
    		
			for(WellBigDecimal well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigDecimal bd : well) {
					input[index++] = bd.doubleValue();
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double resultDouble = stat.getMin();
				
				BigDecimal result = new BigDecimal(resultDouble);
				
				resultMap.put(well, result);
			}

			for(WellBigDecimal well : plate) {
				
				BigDecimal result = resultMap.get(well);
				BigDecimal returned = returnedMap.get(well);
				
				BigDecimal[] corrected = correctRoundingErrors(result, returned);
				
				assertEquals(corrected[0], corrected[1]);
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

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = min.setsAggregated(plate.dataSet());
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			
			BigDecimal[] corrected = correctRoundingErrors(aggregatedResult, aggregatedReturned);		
			assertEquals(corrected[0], corrected[1]);
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
    	
    	Map<WellSetBigDecimal, BigDecimal> aggregatedReturnedMap = min.setsAggregated(collection);
    	Map<WellSetBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigDecimal, BigDecimal>();
    	
    	for(WellSetBigDecimal set : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : set) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigDecimal set : collection) {
    		
    		BigDecimal result = aggregatedResultMap.get(set);
			BigDecimal returned = aggregatedReturnedMap.get(set);
			
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
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
    	
    	Map<WellSetBigDecimal, BigDecimal> aggregatedReturnedMap = min.setsAggregated(setArray);
    	Map<WellSetBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigDecimal, BigDecimal>();
    	
    	for(WellSetBigDecimal set : setArray) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : set) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigDecimal set : setArray) {
    		
    		BigDecimal result = aggregatedResultMap.get(set);
			BigDecimal returned = aggregatedReturnedMap.get(set);
			
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
    	}
    	
    }    
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices.
     */
    @Test
    public void testAggregatedSetIndices() {
    	
    	for(PlateBigDecimal plate : arrayIndices) {

    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = min.setsAggregated(plate.dataSet(), begin, end - begin);
    		
    		for(WellBigDecimal well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			
			BigDecimal[] corrected = correctRoundingErrors(aggregatedResult, aggregatedReturned);	
			assertEquals(corrected[0], corrected[1]);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the collection.
     */
    @Test
    public void testAggregatedSetCollectionIndices() {
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

		List<WellSetBigDecimal> collection = new ArrayList<WellSetBigDecimal>();
		
		for(PlateBigDecimal plate : arrayIndices) {
			collection.add(plate.dataSet());
		}
		
    	Map<WellSetBigDecimal, BigDecimal> aggregatedReturnedMap = min.setsAggregated(collection, begin, end - begin);
    	Map<WellSetBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigDecimal, BigDecimal>();
    	
    	for(WellSetBigDecimal set : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : set) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);
			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigDecimal set : collection) {
    		
    		BigDecimal result = aggregatedResultMap.get(set);
			BigDecimal returned = aggregatedReturnedMap.get(set);
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the array.
     */
    @Test
    public void testAggregatedSetArrayIndices() {
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
		
		WellSetBigDecimal[] setArrayIndices = new WellSetBigDecimal[arrayIndices.length];
		
		for(int i = 0; i < setArrayIndices.length; i++) {
			setArrayIndices[i] = arrayIndices[i].dataSet();
		}
		
    	Map<WellSetBigDecimal, BigDecimal> aggregatedReturnedMap = min.setsAggregated(setArrayIndices, begin, end - begin);
    	Map<WellSetBigDecimal, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigDecimal, BigDecimal>();
    	
    	for(WellSetBigDecimal set : setArrayIndices) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for(WellBigDecimal well : set) {
				resultList.addAll(well.data().subList(begin, end));
			}
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getMin();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble);

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigDecimal plate : setArrayIndices) {
    		
    		BigDecimal result = aggregatedResultMap.get(plate);
			BigDecimal returned = aggregatedReturnedMap.get(plate);
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			
			assertEquals(corrected[0], corrected[1]);
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
				double resultDouble = stat.getMin();

				BigDecimal returned = min.well(well);					
				BigDecimal result = new BigDecimal(resultDouble);
				
				BigDecimal[] corrected = correctRoundingErrors(returned, result);
				
				assertEquals(corrected[0], corrected[1]);
			}		
		}        
    }
    
    /*---------------------------- Helper Methods ----------------------------*/
    
    /**
     * Corrects any rounding errors due to differences in the implementation of
     * the statistic between the Apache and MicroFlex libraries
     * @param    BigDecimal    the first result
     * @param    BigDecimal    the second result
     * @return                 corrected results
     */
    private static BigDecimal[] correctRoundingErrors(BigDecimal bd1, BigDecimal bd2) {

    	BigDecimal[] array = new BigDecimal[2];
    	int scale = mc.getPrecision();
    	
    	while(!bd1.equals(bd2) && scale > mc.getPrecision() / 4) {
			
			bd1 = bd1.setScale(scale, RoundingMode.HALF_DOWN);
			bd2 = bd2.setScale(scale, RoundingMode.HALF_DOWN);
		
			if(bd1.subtract(bd1.ulp()).equals(bd2)) {
				bd1 = bd1.subtract(bd1.ulp());
			}
			
			if(bd1.add(bd1.ulp()).equals(bd2)) {
				bd1 = bd1.add(bd1.ulp());
			}
			
			scale--;
		}
		
		array[0] = bd1;
		array[1] = bd2;
		
		return array;
    }
}
