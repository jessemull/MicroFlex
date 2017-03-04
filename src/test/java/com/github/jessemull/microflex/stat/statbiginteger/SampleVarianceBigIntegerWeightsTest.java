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

package com.github.jessemull.microflex.stat.statbiginteger;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
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

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.bigintegerflex.stat.SampleVarianceBigInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods in the sample variance big integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SampleVarianceBigIntegerWeightsTest {


/* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigInteger minValue = new BigInteger(0 + "");     // Minimum big integer value for wells
	private static BigInteger maxValue = new BigInteger(100 + "");    // Maximum big integer value for wells
	private static Random random = new Random();                                            // Generates random integers
	private static MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);            // The math context for input values
	
	/* The addition operation */
	
	private static SampleVarianceBigInteger variance = new SampleVarianceBigInteger();

	/* Random objects and numbers for testing */

	private static int rows = 5;
	private static int columns = 4;
	private static int length = 5;
	private static int lengthIndices = 10;
	private static int plateNumber = 10;
	private static int plateNumberIndices = 5;
	private static PlateBigInteger[] array = new PlateBigInteger[plateNumber];
	private static PlateBigInteger[] arrayIndices =  new PlateBigInteger[plateNumberIndices];
	private static double[] weights = new double[length];
	private static double[] weightsIndices = new double[lengthIndices];
	
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

			PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
					rows, columns, minValue, maxValue, length, "Plate1-" + j);

			array[j] = plate;
		}
		
		for(int j = 0; j < arrayIndices.length; j++) {
			
			PlateBigInteger plateIndices = RandomUtil.randomPlateBigInteger(
					rows, columns, minValue, maxValue, lengthIndices, "Plate1-" + j);

			arrayIndices[j] = plateIndices;		
		}
		
		for(int i = 0; i < weights.length; i++) {
			weights[i] = random.nextDouble();
		}
		
		for(int i = 0; i < weightsIndices.length; i++) {
			weightsIndices[i] = random.nextDouble();
		}
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
    /* ---------------- Well statistics for all plate wells ----------------- */
    
    /**
     * Tests the plate statistics method.
     */
	@Test
    public void testPlate() {
		
		for(PlateBigInteger plate : array) {

    		Map<WellBigInteger, BigDecimal> resultMap = new TreeMap<WellBigInteger, BigDecimal>();
    		Map<WellBigInteger, BigDecimal> returnedMap = variance.plate(plate, weights, mc);
    		
			for(WellBigInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;				
		
				for(BigInteger bi : well) {
					input[index] = bi.doubleValue() * weights[index];
					index++;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double resultDouble = stat.getVariance();

				BigDecimal result = new BigDecimal(resultDouble, mc);
				
				resultMap.put(well, result);
			}

			for(WellBigInteger well : plate) {
				
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
    	
    	for(PlateBigInteger plate : arrayIndices) {

    		int begin = random.nextInt(plate.first().size() - 4);
			int end = begin + random.nextInt(3) + 3;
			
    		Map<WellBigInteger, BigDecimal> resultMap = new TreeMap<WellBigInteger, BigDecimal>();
    		Map<WellBigInteger, BigDecimal> returnedMap = variance.plate(plate, ArrayUtils.subarray(weightsIndices, begin, end), begin, end - begin, mc);
    		
			for(WellBigInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigInteger bi : well) {
					input[index] = bi.doubleValue() * weightsIndices[index];
					index++;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double resultDouble = stat.getVariance();

				BigDecimal result = new BigDecimal(resultDouble, mc);
				
				resultMap.put(well, result);
			}

			for(WellBigInteger well : plate) {
				
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
        
    	for(PlateBigInteger plate : array) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = variance.platesAggregated(plate, weights, mc);
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();
			
			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			
			BigDecimal[] corrected = correctRoundingErrors(aggregatedResult, aggregatedReturned);		
			assertEquals(corrected[0], corrected[1]);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedPlateCollection() {

    	List<PlateBigInteger> collection = Arrays.asList(array);
    	Map<PlateBigInteger, BigDecimal> aggregatedReturnedMap = variance.platesAggregated(collection, weights, mc);
    	Map<PlateBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigInteger, BigDecimal>();
    	
    	for(PlateBigInteger plate : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigInteger plate : collection) {
    		
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

    	Map<PlateBigInteger, BigDecimal> aggregatedReturnedMap = variance.platesAggregated(array, weights, mc);
    	Map<PlateBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigInteger, BigDecimal>();
    	
    	for(PlateBigInteger plate : array) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigInteger plate : array) {
    		
    		BigDecimal result = aggregatedResultMap.get(plate);
			BigDecimal returned = aggregatedReturnedMap.get(plate);
			BigDecimal[] corrected = correctRoundingErrors(result, returned);
			System.out.println(result + " " + returned);
			assertEquals(corrected[0], corrected[1]);
    	}
    	
    }    
    
    /**
     * Tests the aggregated plate statistics method using the values between the indices.
     */
    @Test
    public void testAggregatedPlateIndices() {
    	
    	for(PlateBigInteger plate : arrayIndices) {

    		int begin = random.nextInt(plate.first().size() - 4);
			int end = begin + random.nextInt(3) + 3;
			
    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = variance.platesAggregated(plate, weightsIndices, begin, end - begin, mc);
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal().subList(begin, end);
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weightsIndices[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			
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
    	
    	int begin = random.nextInt(arrayIndices[0].first().size() - 4);
		int end = begin + random.nextInt(3) + 3;

		List<PlateBigInteger> collection = Arrays.asList(arrayIndices);
    	Map<PlateBigInteger, BigDecimal> aggregatedReturnedMap = variance.platesAggregated(collection, weightsIndices, begin, end - begin, mc);
  
    	Map<PlateBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigInteger, BigDecimal>();
    	
    	for(PlateBigInteger plate : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal().subList(begin, end);
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weightsIndices[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(plate, aggregatedResult);
		}
    	
    	for(PlateBigInteger plate : collection) {
    		
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
    	
    	int begin = random.nextInt(arrayIndices[0].first().size() - 4);
		int end = begin + random.nextInt(3) + 3;

    	Map<PlateBigInteger, BigDecimal> aggregatedReturnedMap = variance.platesAggregated(arrayIndices, weightsIndices, begin, end - begin, mc);
    	Map<PlateBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<PlateBigInteger, BigDecimal>();
    	
    	for(PlateBigInteger plate : arrayIndices) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal().subList(begin, end);
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weightsIndices[i])));
            	}
            	
            }
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}

			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(plate, aggregatedResult);
			
		}
    	
    	for(PlateBigInteger plate : arrayIndices) {

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
    	
    	for(PlateBigInteger plate : array) {

    		Map<WellBigInteger, BigDecimal> resultMap = new TreeMap<WellBigInteger, BigDecimal>();
    		Map<WellBigInteger, BigDecimal> returnedMap = variance.set(plate.dataSet(), weights, mc);
    		
			for(WellBigInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigInteger bi : well) {
					input[index] = bi.doubleValue() * weights[index];
					index++;
				}
			
				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double resultDouble = stat.getVariance();

				BigDecimal result = new BigDecimal(resultDouble, mc);
				
				resultMap.put(well, result);
			}

			for(WellBigInteger well : plate) {
				
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
        
    	for(PlateBigInteger plate : arrayIndices) {

    		int begin = random.nextInt(plate.first().size() - 4);
			int end = begin + random.nextInt(3) + 3;
			
    		Map<WellBigInteger, BigDecimal> resultMap = new TreeMap<WellBigInteger, BigDecimal>();
    		Map<WellBigInteger, BigDecimal> returnedMap = variance.set(plate.dataSet(), ArrayUtils.subarray(weightsIndices, begin, end), begin, end - begin, mc);
    		
			for(WellBigInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigInteger bi : well) {
					input[index] = bi.doubleValue() * weightsIndices[index];
					index++;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double resultDouble = stat.getVariance();

				BigDecimal result = new BigDecimal(resultDouble, mc);
				
				resultMap.put(well, result);
			}

			for(WellBigInteger well : plate) {
				
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
        
    	for(PlateBigInteger plate : array) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = variance.setsAggregated(plate.dataSet(), weights, mc);
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			
			BigDecimal[] corrected = correctRoundingErrors(aggregatedResult, aggregatedReturned);		
			assertEquals(corrected[0], corrected[1]);
		}
    }
    
    /**
     * Tests the aggregated plate statistics method using a collection.
     */
    @Test
    public void testAggregatedSetCollection() {

    	List<WellSetBigInteger> collection = new ArrayList<WellSetBigInteger>();
    	
    	for(PlateBigInteger plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
    	Map<WellSetBigInteger, BigDecimal> aggregatedReturnedMap = variance.setsAggregated(collection, weights, mc);
    	Map<WellSetBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigInteger, BigDecimal>();
    	
    	for(WellSetBigInteger set : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : set) {
            	
            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigInteger set : collection) {
    		
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

    	WellSetBigInteger[] setArray = new WellSetBigInteger[array.length];
    	
    	for(int i = 0; i < setArray.length; i++) {
    		setArray[i] = array[i].dataSet();
    	}
    	
    	Map<WellSetBigInteger, BigDecimal> aggregatedReturnedMap = variance.setsAggregated(setArray, weights, mc);
    	Map<WellSetBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigInteger, BigDecimal>();
    	
    	for(WellSetBigInteger set : setArray) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : set) {
            	
            	List<BigDecimal> input = well.toBigDecimal();
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weights[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigInteger set : setArray) {
    		
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
    	
    	for(PlateBigInteger plate : arrayIndices) {

    		int begin = random.nextInt(plate.first().size() - 4);
			int end = begin + random.nextInt(3) + 3;
			
    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		BigDecimal aggregatedReturned = variance.setsAggregated(plate.dataSet(), weightsIndices, begin, end - begin, mc);
    		
    		for (WellBigInteger well : plate) {
            	
            	List<BigDecimal> input = well.toBigDecimal().subList(begin, end);
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weightsIndices[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			
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
    	
    	int begin = random.nextInt(arrayIndices[0].first().size() - 4);
		int end = begin + random.nextInt(3) + 3;

		List<WellSetBigInteger> collection = new ArrayList<WellSetBigInteger>();
		
		for(PlateBigInteger plate : arrayIndices) {
			collection.add(plate.dataSet());
		}
		
    	Map<WellSetBigInteger, BigDecimal> aggregatedReturnedMap = variance.setsAggregated(collection, weightsIndices, begin, end - begin, mc);
    	Map<WellSetBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigInteger, BigDecimal>();
    	
    	for(WellSetBigInteger set : collection) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : set) {
            	
            	List<BigDecimal> input = well.toBigDecimal().subList(begin, end);
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weightsIndices[i])));
            	}
            	
            }

			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);
			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigInteger set : collection) {
    		
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
    	
    	int begin = random.nextInt(arrayIndices[0].first().size() - 4);
		int end = begin + random.nextInt(3) + 3;
		
		WellSetBigInteger[] setArrayIndices = new WellSetBigInteger[arrayIndices.length];
		
		for(int i = 0; i < setArrayIndices.length; i++) {
			setArrayIndices[i] = arrayIndices[i].dataSet();
		}
		
    	Map<WellSetBigInteger, BigDecimal> aggregatedReturnedMap = variance.setsAggregated(setArrayIndices, weightsIndices, begin, end - begin, mc);
    	Map<WellSetBigInteger, BigDecimal> aggregatedResultMap = new TreeMap<WellSetBigInteger, BigDecimal>();
    	
    	for(WellSetBigInteger set : setArrayIndices) {

    		List<BigDecimal> resultList = new ArrayList<BigDecimal>();
    		
    		for (WellBigInteger well : set) {
            	
            	List<BigDecimal> input = well.toBigDecimal().subList(begin, end);
            	
            	for(int i = 0; i < input.size(); i++) {
            		resultList.add(input.get(i).multiply(new BigDecimal(weightsIndices[i])));
            	}
            	
            }
			
			double[] inputAggregated = new double[resultList.size()];
			
			for(int i = 0; i < resultList.size(); i++) {
				inputAggregated[i] = resultList.get(i).doubleValue();
			}
			
			DescriptiveStatistics statAggregated = new DescriptiveStatistics(inputAggregated);
			double resultAggregatedDouble = statAggregated.getVariance();

			BigDecimal aggregatedResult = new BigDecimal(resultAggregatedDouble, mc);

			aggregatedResultMap.put(set, aggregatedResult);
		}
    	
    	for(WellSetBigInteger plate : setArrayIndices) {
    		
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

		for(PlateBigInteger plate : array) {

			for(WellBigInteger well : plate) {
				
				double[] input = new double[well.size()];
				int index = 0;
				
				for(BigInteger bi : well) {
					input[index] = bi.doubleValue() * weights[index];
					index++;
				}

				DescriptiveStatistics stat = new DescriptiveStatistics(input);
				double resultDouble = stat.getVariance();

				BigDecimal returned = variance.well(well, weights, mc);					
				BigDecimal result = new BigDecimal(resultDouble, mc);
			
				BigDecimal[] corrected = correctRoundingErrors(result, returned);
				
				assertEquals(corrected[0], corrected[1]);
			}		
		}        
    }
    
    /**
     * Tests well calculation using indices.
     */
    @Test
    public void testWellIndices() {

    	for(PlateBigInteger plate : arrayIndices) {

			for(WellBigInteger well : plate) {

		    	double[] input = new double[well.size()];
				int index = 0;
				
				for(BigInteger bi : well) {
					input[index] = bi.doubleValue() * weightsIndices[index];
					index++;
				}

				int begin = random.nextInt(well.size() - 4);
				int end = begin + random.nextInt(3) + 3;

				DescriptiveStatistics stat = new DescriptiveStatistics(ArrayUtils.subarray(input, begin, end));
				double resultDouble = stat.getVariance();

				BigDecimal returned = variance.well(well, ArrayUtils.subarray(weightsIndices, begin, end), begin, end - begin, mc);					
				BigDecimal result = new BigDecimal(resultDouble, mc);
				
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
