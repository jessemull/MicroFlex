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

package com.github.jessemull.microflex.stat.statinteger;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.integerflex.stat.PercentileInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the percentile integer class.
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PercentileIntegerTest {


/* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = 0;      // Minimum integer value for wells
	private static int maxValue = 100;    // Maximum integer value for wells
	private static Random random = new Random();    // Generates random integers
	private int precision = 10;                     // Precision for integer results
			
	/* The addition operation */
	
	private static PercentileInteger percentile = new PercentileInteger();

	/* Random objects and numbers for testing */

	private static int rows = 5;
	private static int columns = 4;
	private static int length = 5;
	private static int lengthIndices = 10;
	private static int plateNumber = 10;
	private static int plateNumberIndices = 5;
	private static PlateInteger[] array = new PlateInteger[plateNumber];
	private static PlateInteger[] arrayIndices =  new PlateInteger[plateNumberIndices];
	
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
			
			PlateInteger plate = RandomUtil.randomPlateInteger(
					rows, columns, minValue, maxValue, length, "Plate1-" + j);

			array[j] = plate;
		}
		
		for(int j = 0; j < arrayIndices.length; j++) {
			
			PlateInteger plateIndices = RandomUtil.randomPlateInteger(
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
		PercentileInteger test = new PercentileInteger();
		assertNotNull(test);
	}
	
    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Tests the plate statistics method.
     */
	@Test
    public void testPlate() {
		
		for(PlateInteger plate : array) {

			int inputPercentile = 1 + random.nextInt(100);
			
    		Map<WellInteger, Double> resultMap = new TreeMap<WellInteger, Double>();
    		Map<WellInteger, Double> returnedMap = percentile.plate(plate, inputPercentile);
    		
			for(WellInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double db : well) {
					input[index++] = db;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getPercentile(inputPercentile);

				resultMap.put(well, result);
			}

			for(WellInteger well : plate) {
				
				double result = Precision.round(resultMap.get(well), precision);
				double returned = Precision.round(returnedMap.get(well), precision);

				assertTrue(result == returned);
			}
		} 
    }
    
    /**
     * Tests the plate statistics method using the values between the indices.
     */
    @Test
    public void testPlateIndices() {
    	
    	for(PlateInteger plate : arrayIndices) {

    		int inputPercentile = 1 + random.nextInt(100);
    		
    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		Map<WellInteger, Double> resultMap = new TreeMap<WellInteger, Double>();
    		Map<WellInteger, Double> returnedMap = percentile.plate(plate, begin, end - begin, inputPercentile);
    		
			for(WellInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double db : well) {
					input[index++] = db;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double result = stat.getPercentile(inputPercentile);

				resultMap.put(well, result);
			}

			for(WellInteger well : plate) {
				
				double result = Precision.round(resultMap.get(well), precision);
				double returned = Precision.round(returnedMap.get(well), precision);
				
				assertTrue(result == returned);
			}
    	}
    }

    /* --------------------- Aggregated plate statistics -------------------  */
    
    /**
     * Tests the aggregated plate statistics method.
     */
    @Test
    public void testAggregatedPlate() {
        
    	for(PlateInteger plate : array) {

    		int inputPercentile = 1 + random.nextInt(100);
    		
    		List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(
    				percentile.platesAggregated(plate, inputPercentile), precision);
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = Precision.round(statAggregated.getPercentile(inputPercentile), precision);
	
			assertTrue(resultAggregated == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedPlateCollection() {

    	int inputPercentile = 1 + random.nextInt(100);
    	
    	List<PlateInteger> collection = Arrays.asList(array);
    	Map<PlateInteger, Double> aggregatedReturnedMap = percentile.platesAggregated(collection, inputPercentile);
    	Map<PlateInteger, Double> aggregatedResultMap = new TreeMap<PlateInteger, Double>();
    	
    	for(PlateInteger plate : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateInteger plate : collection) {
    		
    		double result = Precision.round(aggregatedResultMap.get(plate), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(plate), precision);

			assertTrue(result == returned);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using an array.
     */
    @Test
    public void testAggregatedPlateArray() {

    	int inputPercentile = 1 + random.nextInt(100);
    	
    	Map<PlateInteger, Double> aggregatedReturnedMap = percentile.platesAggregated(array, inputPercentile);
    	Map<PlateInteger, Double> aggregatedResultMap = new TreeMap<PlateInteger, Double>();
    	
    	for(PlateInteger plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateInteger plate : array) {
    		
    		double result = Precision.round(aggregatedResultMap.get(plate), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(plate), precision);

			assertTrue(result == returned);
    	}
    	
    }    
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices.
     */
    @Test
    public void testAggregatedPlateIndices() {
    	
    	for(PlateInteger plate : arrayIndices) {

    		int inputPercentile = 1 + random.nextInt(100);
    		
        	int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	    List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(
    				percentile.platesAggregated(plate, begin, end - begin, 
    				inputPercentile), precision);
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = Precision.round(statAggregated.getPercentile(inputPercentile), precision);

			assertTrue(aggregatedResult == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the collection.
     */
    @Test
    public void testAggregatedPlateCollectionIndices() {
    	
    	int inputPercentile = 1 + random.nextInt(100);
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

		List<PlateInteger> collection = Arrays.asList(arrayIndices);
    	Map<PlateInteger, Double> aggregatedReturnedMap = percentile.platesAggregated(collection, begin, end - begin, inputPercentile);
  
    	Map<PlateInteger, Double> aggregatedResultMap = new TreeMap<PlateInteger, Double>();
    	
    	for(PlateInteger plate : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);
			
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateInteger plate : collection) {
    		
    		double result = Precision.round(aggregatedResultMap.get(plate), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(plate), precision);

			assertTrue(result == returned);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the array.
     */
    @Test
    public void testAggregatedPlateArrayIndices() {
    	
    	int inputPercentile = 1 + random.nextInt(100);
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	Map<PlateInteger, Double> aggregatedReturnedMap = percentile.platesAggregated(arrayIndices, begin, end - begin, inputPercentile);
    	Map<PlateInteger, Double> aggregatedResultMap = new TreeMap<PlateInteger, Double>();
    	
    	for(PlateInteger plate : arrayIndices) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble().subList(begin, end));
			}
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateInteger plate : arrayIndices) {
    		
    		double result = Precision.round(aggregatedResultMap.get(plate), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(plate), precision);

			assertTrue(result == returned);
    	}
    }
    
    /* --------------- Well statistics for all wells in a set --------------  */
    
    /**
     * Tests set calculation.
     */
    @Test
    public void testSet() {
    	
    	int inputPercentile = 1 + random.nextInt(100);
    	
    	for(PlateInteger plate : array) {

    		Map<WellInteger, Double> resultMap = new TreeMap<WellInteger, Double>();
    		Map<WellInteger, Double> returnedMap = percentile.set(plate.dataSet(), inputPercentile);
    		
			for(WellInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double db : well) {
					input[index++] = db;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getPercentile(inputPercentile);

				resultMap.put(well, result);
			}

			for(WellInteger well : plate) {
				
				double result = Precision.round(resultMap.get(well), precision);
				double returned = Precision.round(returnedMap.get(well), precision);

				assertTrue(result == returned);
			}
		} 
    	
    }
    
    /**
     * Tests set calculation using indices.
     */
    @Test
    public void testSetIndices() {
        
    	for(PlateInteger plate : arrayIndices) {

    		int inputPercentile = 1 + random.nextInt(100);
    		
    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		Map<WellInteger, Double> resultMap = new TreeMap<WellInteger, Double>();
    		Map<WellInteger, Double> returnedMap = percentile.set(plate.dataSet(), begin, end - begin, inputPercentile);
    		
			for(WellInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double db : well) {
					input[index++] = db;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double result = stat.getPercentile(inputPercentile);

				resultMap.put(well, result);
			}

			for(WellInteger well : plate) {
				
				double result = Precision.round(resultMap.get(well), precision);
				double returned = Precision.round(returnedMap.get(well), precision);

				assertTrue(result == returned);
			}
    	}
    }

    /* ---------------------- Aggregated set statistics --------------------  */
    
    /**
     * Tests the aggregated plate statistics method.
     */
    @Test
    public void testAggregatedSet() {
        
    	for(PlateInteger plate : array) {

    		int inputPercentile = 1 + random.nextInt(100);
    		
    		List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(
    				percentile.setsAggregated(plate.dataSet(), inputPercentile), precision);
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = Precision.round(
					statAggregated.getPercentile(inputPercentile), precision);
		
			assertTrue(resultAggregated == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedSetCollection() {

    	int inputPercentile = 1 + random.nextInt(100);
    	
    	List<WellSetInteger> collection = new ArrayList<WellSetInteger>();
    	
    	for(PlateInteger plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
    	Map<WellSetInteger, Double> aggregatedReturnedMap = percentile.setsAggregated(collection, inputPercentile);
    	Map<WellSetInteger, Double> aggregatedResultMap = new TreeMap<WellSetInteger, Double>();
    	
    	for(WellSetInteger set : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : set) {
				resultList.addAll(well.toDouble());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetInteger set : collection) {
    		
    		double result = Precision.round(aggregatedResultMap.get(set), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(set), precision);

			assertTrue(result == returned);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using an array.
     */
    @Test
    public void testAggregatedSetArray() {

    	int inputPercentile = 1 + random.nextInt(100);
    	
    	WellSetInteger[] setArray = new WellSetInteger[array.length];
    	
    	for(int i = 0; i < setArray.length; i++) {
    		setArray[i] = array[i].dataSet();
    	}
    	
    	Map<WellSetInteger, Double> aggregatedReturnedMap = percentile.setsAggregated(setArray, inputPercentile);
    	Map<WellSetInteger, Double> aggregatedResultMap = new TreeMap<WellSetInteger, Double>();
    	
    	for(WellSetInteger set : setArray) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : set) {
				resultList.addAll(well.toDouble());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetInteger set : setArray) {
    		
    		double result = Precision.round(aggregatedResultMap.get(set), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(set), precision);

			assertTrue(result == returned);
    	}
    	
    }    
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices.
     */
    @Test
    public void testAggregatedSetIndices() {
    	
    	for(PlateInteger plate : arrayIndices) {

    		int inputPercentile = 1 + random.nextInt(100);
    		
    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(
    				percentile.setsAggregated(plate.dataSet(), begin, end - begin, 
    				inputPercentile), precision);
    		
    		for(WellInteger well : plate) {
				resultList.addAll(well.toDouble().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = Precision.round(statAggregated.getPercentile(inputPercentile), precision);

			assertTrue(aggregatedResult == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the collection.
     */
    @Test
    public void testAggregatedSetCollectionIndices() {
    	
    	int inputPercentile = 1 + random.nextInt(100);
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

		List<WellSetInteger> collection = new ArrayList<WellSetInteger>();
		
		for(PlateInteger plate : arrayIndices) {
			collection.add(plate.dataSet());
		}
		
    	Map<WellSetInteger, Double> aggregatedReturnedMap = percentile.setsAggregated(collection, begin, end - begin, inputPercentile);
    	Map<WellSetInteger, Double> aggregatedResultMap = new TreeMap<WellSetInteger, Double>();
    	
    	for(WellSetInteger set : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : set) {
				resultList.addAll(well.toDouble().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetInteger set : collection) {
    		
    		double result = Precision.round(aggregatedResultMap.get(set), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(set), precision);

			assertTrue(result == returned);
    	}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the array.
     */
    @Test
    public void testAggregatedSetArrayIndices() {
    	
    	int inputPercentile = 1 + random.nextInt(100);
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
		
		WellSetInteger[] setArrayIndices = new WellSetInteger[arrayIndices.length];
		
		for(int i = 0; i < setArrayIndices.length; i++) {
			setArrayIndices[i] = arrayIndices[i].dataSet();
		}
		
    	Map<WellSetInteger, Double> aggregatedReturnedMap = percentile.setsAggregated(setArrayIndices, begin, end - begin, inputPercentile);
    	Map<WellSetInteger, Double> aggregatedResultMap = new TreeMap<WellSetInteger, Double>();
    	
    	for(WellSetInteger set : setArrayIndices) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellInteger well : set) {
				resultList.addAll(well.toDouble().subList(begin, end));
			}
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getPercentile(inputPercentile);

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetInteger plate : setArrayIndices) {
    		
    		double result = Precision.round(aggregatedResultMap.get(plate), precision);
			double returned = Precision.round(aggregatedReturnedMap.get(plate), precision);

			assertTrue(result == returned);
    	}
    }
    
    /* -------------------------- Well statistics --------------------------  */
    
    /**
     * Tests well calculation.
     */
    @Test
    public void testWell() {

		for(PlateInteger plate : array) {

			for(WellInteger well : plate) {
				
				int inputPercentile = 1 + random.nextInt(100);
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double db : well) {
					input[index++] = db;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = Precision.round(stat.getPercentile(inputPercentile), precision);
				double returned = Precision.round(percentile.well(well, inputPercentile), precision);					
				
				assertTrue(result == returned);
			}		
		}        
    }
    
    /**
     * Tests well calculation using indices.
     */
    @Test
    public void testWellIndices() {

    	for(PlateInteger plate : arrayIndices) {

			for(WellInteger well : plate) {

				int inputPercentile = 1 + random.nextInt(100);
				
		    	double[] input = new double[well.size()];
				int index = 0;
				
				for(double db : well) {
					input[index++] = db;
				}

				int size = arrayIndices[0].first().size();
	    		int begin = random.nextInt(size - 5);
				int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double result = Precision.round(stat.getPercentile(inputPercentile), precision);
				double returned = Precision.round(percentile.well(well, begin, end - begin, inputPercentile), precision);					

				assertTrue(result == returned);
			}		
		}		
    }

}
