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

package com.github.jessemull.microflex.stat.statdouble;

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

import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.doubleflex.stat.SumOfSquaresDouble;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the sum double class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SumOfSquaresDoubleTest {


	/* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static double minValue = 0;             // Minimum double value for wells
	private static double maxValue = 100;           // Maximum double value for wells
	private static Random random = new Random();    // Generates random integers
	private static int precision = 10;              // Precision for double results
	
	/* The addition operation */
	
	private static SumOfSquaresDouble sum = new SumOfSquaresDouble();

	/* Random objects and numbers for testing */

	private static int rows = 5;
	private static int columns = 4;
	private static int length = 5;
	private static int lengthIndices = 10;
	private static int plateNumber = 10;
	private static int plateNumberIndices = 5;
	private static PlateDouble[] array = new PlateDouble[plateNumber];
	private static PlateDouble[] arrayIndices =  new PlateDouble[plateNumberIndices];
	
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

			PlateDouble plate = RandomUtil.randomPlateDouble(
					rows, columns, minValue, maxValue, length, "Plate1-" + j);

			array[j] = plate;
		}
		
		for(int j = 0; j < arrayIndices.length; j++) {
			
			PlateDouble plateIndices = RandomUtil.randomPlateDouble(
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
		SumOfSquaresDouble test = new SumOfSquaresDouble();
		assertNotNull(test);
	}
	
    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Tests the plate statistics method.
     */
	@Test
    public void testPlate() {
		
		for(PlateDouble plate : array) {

    		Map<WellDouble, Double> resultMap = new TreeMap<WellDouble, Double>();
    		Map<WellDouble, Double> returnedMap = sum.plate(plate);
    		
			for(WellDouble well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double bd : well) {
					input[index++] = bd;;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getSumsq();

				resultMap.put(well, result);
			}

			for(WellDouble well : plate) {
				
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
    	
    	for(PlateDouble plate : arrayIndices) {

    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		Map<WellDouble, Double> resultMap = new TreeMap<WellDouble, Double>();
    		Map<WellDouble, Double> returnedMap = sum.plate(plate, begin, end - begin);
    		
			for(WellDouble well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double bd : well) {
					input[index++] = bd;;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double result = stat.getSumsq();

				resultMap.put(well, result);
			}

			for(WellDouble well : plate) {
				
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
        
    	for(PlateDouble plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(sum.platesAggregated(plate), precision);
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = Precision.round(statAggregated.getSumsq(), precision);

			assertTrue(resultAggregated == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedPlateCollection() {

    	List<PlateDouble> collection = Arrays.asList(array);
    	Map<PlateDouble, Double> aggregatedReturnedMap = sum.platesAggregated(collection);
    	Map<PlateDouble, Double> aggregatedResultMap = new TreeMap<PlateDouble, Double>();
    	
    	for(PlateDouble plate : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();
			
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateDouble plate : collection) {
    		
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

    	Map<PlateDouble, Double> aggregatedReturnedMap = sum.platesAggregated(array);
    	Map<PlateDouble, Double> aggregatedResultMap = new TreeMap<PlateDouble, Double>();
    	
    	for(PlateDouble plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateDouble plate : array) {
    		
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
    	
    	for(PlateDouble plate : arrayIndices) {

        	int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	    List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(sum.platesAggregated(plate, begin, end - begin), precision);
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = Precision.round(statAggregated.getSumsq(), precision);
	
			assertTrue(aggregatedResult == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices of
     * the collection.
     */
    @Test
    public void testAggregatedPlateCollectionIndices() {
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

		List<PlateDouble> collection = Arrays.asList(arrayIndices);
    	Map<PlateDouble, Double> aggregatedReturnedMap = sum.platesAggregated(collection, begin, end - begin);
  
    	Map<PlateDouble, Double> aggregatedResultMap = new TreeMap<PlateDouble, Double>();
    	
    	for(PlateDouble plate : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = statAggregated.getSumsq();

			aggregatedResultMap.put(plate, resultAggregated);
		}
    	
    	for(PlateDouble plate : collection) {
    		
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
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

    	Map<PlateDouble, Double> aggregatedReturnedMap = sum.platesAggregated(arrayIndices, begin, end - begin);
    	Map<PlateDouble, Double> aggregatedResultMap = new TreeMap<PlateDouble, Double>();
    	
    	for(PlateDouble plate : arrayIndices) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();

			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateDouble plate : arrayIndices) {
    		
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
    	
    	for(PlateDouble plate : array) {

    		Map<WellDouble, Double> resultMap = new TreeMap<WellDouble, Double>();
    		Map<WellDouble, Double> returnedMap = sum.set(plate.dataSet());
    		
			for(WellDouble well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double bd : well) {
					input[index++] = bd;;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = stat.getSumsq();

				resultMap.put(well, result);
			}

			for(WellDouble well : plate) {
				
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
        
    	for(PlateDouble plate : arrayIndices) {

    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		Map<WellDouble, Double> resultMap = new TreeMap<WellDouble, Double>();
    		Map<WellDouble, Double> returnedMap = sum.set(plate.dataSet(), begin, end - begin);
    		
			for(WellDouble well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double bd : well) {
					input[index++] = bd;;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double result = stat.getSumsq();

				resultMap.put(well, result);
			}

			for(WellDouble well : plate) {
				
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
        
    	for(PlateDouble plate : array) {

    		List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(sum.setsAggregated(plate.dataSet()), precision);
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = Precision.round(statAggregated.getSumsq(), precision);
	
			assertTrue(resultAggregated == aggregatedReturned);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedSetCollection() {

    	List<WellSetDouble> collection = new ArrayList<WellSetDouble>();
    	
    	for(PlateDouble plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
    	Map<WellSetDouble, Double> aggregatedReturnedMap = sum.setsAggregated(collection);
    	Map<WellSetDouble, Double> aggregatedResultMap = new TreeMap<WellSetDouble, Double>();
    	
    	for(WellSetDouble set : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : set) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetDouble set : collection) {
    		
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

    	WellSetDouble[] setArray = new WellSetDouble[array.length];
    	
    	for(int i = 0; i < setArray.length; i++) {
    		setArray[i] = array[i].dataSet();
    	}
    	
    	Map<WellSetDouble, Double> aggregatedReturnedMap = sum.setsAggregated(setArray);
    	Map<WellSetDouble, Double> aggregatedResultMap = new TreeMap<WellSetDouble, Double>();
    	
    	for(WellSetDouble set : setArray) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : set) {
				resultList.addAll(well.data());
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetDouble set : setArray) {
    		
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
    	
    	for(PlateDouble plate : arrayIndices) {

    		int size = arrayIndices[0].first().size();
    		int begin = random.nextInt(size - 5);
			int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
			
    		List<Double> resultList = new ArrayList<Double>();
    		double aggregatedReturned = Precision.round(sum.setsAggregated(plate.dataSet(), begin, end - begin), precision);
    		
    		for(WellDouble well : plate) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregated = Precision.round(statAggregated.getSumsq(), precision);
	
			assertTrue(resultAggregated == aggregatedReturned);
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

		List<WellSetDouble> collection = new ArrayList<WellSetDouble>();
		
		for(PlateDouble plate : arrayIndices) {
			collection.add(plate.dataSet());
		}
		
    	Map<WellSetDouble, Double> aggregatedReturnedMap = sum.setsAggregated(collection, begin, end - begin);
    	Map<WellSetDouble, Double> aggregatedResultMap = new TreeMap<WellSetDouble, Double>();
    	
    	for(WellSetDouble set : collection) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : set) {
				resultList.addAll(well.data().subList(begin, end));
			}

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetDouble set : collection) {
    		
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
    	
    	int size = arrayIndices[0].first().size();
		int begin = random.nextInt(size - 5);
		int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);
		
		WellSetDouble[] setArrayIndices = new WellSetDouble[arrayIndices.length];
		
		for(int i = 0; i < setArrayIndices.length; i++) {
			setArrayIndices[i] = arrayIndices[i].dataSet();
		}
		
    	Map<WellSetDouble, Double> aggregatedReturnedMap = sum.setsAggregated(setArrayIndices, begin, end - begin);
    	Map<WellSetDouble, Double> aggregatedResultMap = new TreeMap<WellSetDouble, Double>();
    	
    	for(WellSetDouble set : setArrayIndices) {

    		List<Double> resultList = new ArrayList<Double>();
    		
    		for(WellDouble well : set) {
				resultList.addAll(well.data().subList(begin, end));
			}
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i);
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double aggregatedResult = statAggregated.getSumsq();

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetDouble plate : setArrayIndices) {
    		
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

		for(PlateDouble plate : array) {

			for(WellDouble well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(double bd : well) {
					input[index++] = bd;;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double result = Precision.round(stat.getSumsq(), precision);
				double returned = Precision.round(sum.well(well), precision);					
			
				assertTrue(result == returned);
			}		
		}        
    }
    
    /**
     * Tests well calculation using indices.
     */
    @Test
    public void testWellIndices() {

    	for(PlateDouble plate : arrayIndices) {

			for(WellDouble well : plate) {

		    	double[] input = new double[well.size()];
				int index = 0;
				
				for(double bd : well) {
					input[index++] = bd;;
				}

				int size = arrayIndices[0].first().size();
	    		int begin = random.nextInt(size - 5);
				int end = (begin + 4) + random.nextInt(size - (begin + 4) + 1);

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double result = Precision.round(stat.getSumsq(), precision);
				double returned = Precision.round(sum.well(well, begin, end - begin), precision);					

				assertTrue(result == returned);
			}		
		}		
    }

}
