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

/*--------------------------- Package Declaration ----------------------------*/

package com.github.jessemull.microflex.io.iobiginteger;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigintegerflex.io.PlateReaderBigInteger;
import com.github.jessemull.microflex.bigintegerflex.io.PlateWriterBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.bigintegerflex.stat.MeanBigInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/*------------------------------- Dependencies -------------------------------*/

/**
 * Tests methods in the plate writer big integer class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateWriterBigIntegerTest {

    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigInteger minValue = new BigInteger(0 + "");                    // Minimum big integer value for wells
	private static BigInteger maxValue = new BigInteger(100 + "");                  // Maximum big integer value for wells
	private static MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);    // Math context for input values
	
	/* The addition operation */
	
	private static MeanBigInteger mean = new MeanBigInteger();

	/* Random objects and numbers for testing */

	private static int rows = PlateBigInteger.ROWS_96WELL;
	private static int columns = PlateBigInteger.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int type = PlateBigInteger.PLATE_96WELL;
	private static int stackNumber = 5;
	
	private static PlateBigInteger[] array = new PlateBigInteger[plateNumber];
	private static List<Map<WellBigInteger, BigInteger>> maps = new ArrayList<Map<WellBigInteger, BigInteger>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigInteger> stacks = new ArrayList<StackBigInteger>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterBigInteger plateWriter;
	private static String testLabel = "TestLabel";
	
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

			String label = "Plate1-" + j;
			
			PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
					rows, columns, minValue, maxValue, length, label);
			
			labelList.add(label);
			array[j] = plate;
			
			Map<WellBigInteger, BigDecimal> toAddBigDecimal = mean.plate(plate, mc);
			TreeMap<WellBigInteger, BigInteger> toAddBigInteger = new TreeMap<WellBigInteger, BigInteger>();
			
			for(Map.Entry<WellBigInteger, BigDecimal> entry : toAddBigDecimal.entrySet()) {
				toAddBigInteger.put(entry.getKey(), entry.getValue().toBigInteger());
			}
			
			maps.add(toAddBigInteger);
		}
		
		for(int k = 0; k < stackNumber; k++) {	
			StackBigInteger stack = RandomUtil.randomStackBigInteger(
					rows, columns, minValue, maxValue, length, "Stack" + k, plateNumber);
			stacks.add(stack);
		}
	}
	
	/**
	 * Creates a file and plate writer.
	 * @throws FileNotFoundException 
	 */
	@Before
	public void beforeTests() throws FileNotFoundException {
		file = new File(path);
		plateWriter = new PlateWriterBigInteger(file);
	}
	
	/**
	 * Deletes the file used in the test.
	 */
	@After
	public void afterTests() {
		file.delete();
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass 
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
    /*------------------------------ Constructors ----------------------------*/
    
    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     */
	@Test
    public void testConstructorFile() throws FileNotFoundException {
		
    	PlateWriterBigInteger writer = new PlateWriterBigInteger(file);
    	
    	writer.close();

    	assertFalse(writer == null);
    	
    	file.delete();
    }
    
    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     */
	@Test
    public void testConstructorFileCSN() throws FileNotFoundException, UnsupportedEncodingException {
		
    	PlateWriterBigInteger writer = new PlateWriterBigInteger(file);
    	
    	writer.close();

    	assertFalse(writer == null);
    	
    	file.delete();
    }

    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     */
	@Test
    public void testConstructorOutputStream() throws FileNotFoundException {

    	FileOutputStream stream = new FileOutputStream(file);
    	PlateWriterBigInteger writer = new PlateWriterBigInteger(stream);
    	
    	writer.close();

    	assertFalse(writer == null);
    	
    	file.delete();
    }
    
    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     */
	@Test
    public void testConstructorOutputStreamAutoFlush() throws FileNotFoundException {

    	FileOutputStream stream = new FileOutputStream(file);
    	PlateWriterBigInteger writer1 = new PlateWriterBigInteger(stream, true);
    	PlateWriterBigInteger writer2 = new PlateWriterBigInteger(stream, false);
    	
    	writer1.close();
    	writer2.close();

    	assertFalse(writer1 == null);
    	assertFalse(writer2 == null);
    	
    	file.delete();
    }
    
    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     */
	@Test
    public void testConstructorFileName() throws FileNotFoundException {

    	PlateWriterBigInteger writer = new PlateWriterBigInteger(path);
    	
    	writer.close();

    	assertFalse(writer == null);
    	
    	file.delete();
    }
    
    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     */
	@Test
    public void testConstructorFileNameCSN() throws FileNotFoundException, UnsupportedEncodingException {

    	PlateWriterBigInteger writer = new PlateWriterBigInteger(path, "UTF-8");
    	
    	writer.close();

    	assertFalse(writer == null);
    	
    	file.delete();
    }

    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     */
	@Test
    public void testConstructorWriter() throws FileNotFoundException {

    	PrintWriter inputWriter = new PrintWriter(file);
    	PlateWriterBigInteger writer = new PlateWriterBigInteger(inputWriter);
    	
    	writer.close();
    	
    	assertFalse(writer == null);
    	
    	file.delete();
    }
    
    /**
     * Tests the plate writer constructor.
     * @throws FileNotFoundException 
     */
	@Test
    public void testConstructorWriterAutoFlush() throws FileNotFoundException {

    	PrintWriter inputWriter = new PrintWriter(file);
    	PlateWriterBigInteger writer1 = new PlateWriterBigInteger(inputWriter, true);
    	PlateWriterBigInteger writer2 = new PlateWriterBigInteger(inputWriter, false);
    	
    	writer1.close();
    	writer2.close();
    	
    	assertFalse(writer1 == null);
    	assertFalse(writer2 == null);
    	
    	file.delete();
    }
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
	public void testResultToPlateMapType() throws IOException, JAXBException {

		for(Map<WellBigInteger, BigInteger> map : maps) {
			plateWriter.resultToPlateMap(map, type);
		}

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index++];
			PlateBigInteger output = reader.nextMap();

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
	}
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
    public void testResultToPlateMapListType() throws IOException, JAXBException {

		plateWriter.resultToPlateMap(maps, type);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index++];
			PlateBigInteger output = reader.nextMap();

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
	public void testResultToPlateMapRowsColumns() throws IOException, JAXBException {

		for(Map<WellBigInteger, BigInteger> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns);
		}

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index++];
			PlateBigInteger output = reader.nextMap();

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
	}
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
    public void testResultToPlateMapListRowsColumns() throws IOException, JAXBException {
    	
    	plateWriter.resultToPlateMap(maps, rows, columns);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index++];
			PlateBigInteger output = reader.nextMap();

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
	public void testResultToPlateMapTypeLabel() throws IOException, JAXBException {

		for(Map<WellBigInteger, BigInteger> map : maps) {
			plateWriter.resultToPlateMap(map, type, testLabel);
		}

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index++];
			PlateBigInteger output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
	}
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
    public void testResultToPlateMapListTypeLabel() throws IOException, JAXBException {

    	plateWriter.resultToPlateMap(maps, type, labelList);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index];
			PlateBigInteger output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
	
	/**
	 * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
	public void testResultToPlateMapRowsColumnsLabel() throws IOException, JAXBException {
		
		for(Map<WellBigInteger, BigInteger> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns, testLabel);
		}

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index++];
			PlateBigInteger output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);
				
				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
	}

	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
    public void testResultToPlateMapListRowsColumnsLabel() throws IOException, JAXBException {

    	plateWriter.resultToPlateMap(maps, rows, columns, labelList);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigInteger input = array[index];
			PlateBigInteger output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);

				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
    
	/*----------------------- Methods for Table Output -----------------------*/
	
	/**
     * Tests the result to table method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
    @Test
    public void testResultToTable() throws IOException, JAXBException {

    	for(Map<WellBigInteger, BigInteger> map : maps) {
			plateWriter.resultToTable(map);
		}

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigInteger input = array[index++];
			WellSetBigInteger output = reader.nextTable();

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);

				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
    
    /**
     * Tests the result to table method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testResultToTableList() throws IOException, JAXBException {

		plateWriter.resultToTable(maps);

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigInteger input = array[index++];
			WellSetBigInteger output = reader.nextTable();

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);

				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
    
    /**
     * Tests the result to table method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testResultToTableListLabel() throws IOException, JAXBException {
    	
    	for(Map<WellBigInteger, BigInteger> map : maps) {
			plateWriter.resultToTable(map, testLabel);
		}

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigInteger input = array[index++];
			WellSetBigInteger output = reader.nextTable();

			assertEquals(testLabel, output.label());

			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);

				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
    
    /**
     * Tests the result to table method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testResultToTableListLabels() throws IOException, JAXBException {

		plateWriter.resultToTable(maps, labelList);

		PlateReaderBigInteger reader = new PlateReaderBigInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigInteger input = array[index];
			WellSetBigInteger output = reader.nextTable();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellBigInteger> iter1 = input.iterator();
			Iterator<WellBigInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigInteger inputResult = mean.well(iter1.next(), mc).toBigInteger();
				BigInteger outputResult = iter2.next().data().get(0);

				assertEquals(inputResult, outputResult);
			}
		}

		reader.close();
    }
    
    /*------------------- Methods for Result JSON Output -------------------*/
                                                            
    /**
     * Tests the result to JSON method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testResultToJSON() throws IOException, JAXBException { 
    	
    	String wellPath = "testJSONResult.txt";

    	for(Map<WellBigInteger, BigInteger> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.resultToJSON(map);
		    
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			WellSetBigInteger output = reader.nextJSONResult();
			Iterator<WellBigInteger> iter = output.iterator();
			
			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {	
				
				WellBigInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));				
			}
			
			reader.close(); 
			wellWriter.close();
			wellFile.delete();
			
		}
    }
    
    /**
     * Tests the result to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testResultToJSONList() throws IOException, JAXBException {

    	String wellPath = "testJSONResult.txt";

		File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.resultToJSON(maps);
	    
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

		for(Map<WellBigInteger, BigInteger> map : maps) {	
			
			WellSetBigInteger set = reader.nextJSONResult();
			Iterator<WellBigInteger> iter = set.iterator();
			
			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {
				
				WellBigInteger next = iter.next();
				
				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));		
			}
		}
		
		reader.close(); 
		wellWriter.close();
		wellFile.delete();
    	
    }
    
    /**
     * Tests the result to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testResultToJSONLabel() throws IOException, JAXBException {

    	String wellPath = "testJSONResult.txt";
    	
    	for(Map<WellBigInteger, BigInteger> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.resultToJSON(map, testLabel);
		    
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			WellSetBigInteger output = reader.nextJSONResult();
			Iterator<WellBigInteger> iter = output.iterator();
			
			assertEquals(testLabel, output.label());
			
			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {	
				
				WellBigInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));				
			}
			
			reader.close(); 
			wellWriter.close();
			wellFile.delete();
			
		}
    	
    }
    
    /**
     * Tests the result to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testResultToJSONListLabels() throws IOException, JAXBException {

    	String wellPath = "testJSONResult.txt";
    	List<String> labelList = new ArrayList<String>();
    	
    	for(int i = 0; i < maps.size(); i++) {
    		labelList.add("Result" + i);
    	}
    	
    	Iterator<String> labelIter = labelList.iterator();
		File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.resultToJSON(maps, labelList);
	    
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

		for(Map<WellBigInteger, BigInteger> map : maps) {	
			
			WellSetBigInteger set = reader.nextJSONResult();
			Iterator<WellBigInteger> iter = set.iterator();
			String inputLabel = labelIter.next();
			
			assertEquals(inputLabel, set.label());

			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {
				
				WellBigInteger next = iter.next();
				
				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));		
			}
		}
		
		reader.close(); 
		wellWriter.close();
		wellFile.delete();
    	
    }
    
    /*--------------------- Methods for Well JSON Output ---------------------*/
    
    /**
     * Tests the well to JSON method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testWellToJSON() throws IOException, JAXBException {

    	String wellPath = "testJSONWell.txt";

    	for(PlateBigInteger plate : array) {
    		
    		for(WellBigInteger well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
    			wellWriter.wellToJSON(well);
			    
    			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
    			WellBigInteger output = reader.nextJSONWell();

    			assertEquals(well, output);
    			assertEquals(well.data(), output.data());
    			
    			reader.close(); 
    			wellWriter.close();
    			wellFile.delete();
    		}
		}
    	
    }
    
    /**
     * Tests the well to JSON method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testWellToJSONCollection() throws IOException, JAXBException {     

    	String wellPath = "testJSONWell.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			
    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger output = reader.nextJSONWell();
    			
    			assertEquals(well, output);
    			assertEquals(well.data(), output.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /**
     * Tests the well to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testWellToJSONArray() throws IOException, JAXBException {

    	String wellPath = "testJSONWell.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.wellToJSON(plate.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			
    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger output = reader.nextJSONWell();
    			
    			assertEquals(well, output);
    			assertEquals(well.data(), output.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /*------------------- Methods for Well Set JSON Output -------------------*/
    
    /**
     * Tests the set to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSetToJSON() throws IOException, JAXBException {

    	String wellPath = "testJSONSet.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.setToJSON(plate.dataSet());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			WellSetBigInteger output = reader.nextJSONSet();
			Iterator<WellBigInteger> iter = output.iterator();
			
    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /**
     * Tests the set to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSetToJSONCollection() throws IOException, JAXBException {
    	
    	String wellPath = "testJSONSet.txt";
    	File wellFile = new File(wellPath);
    	
    	List<WellSetBigInteger> collection = new ArrayList<WellSetBigInteger>();
    	
    	for(PlateBigInteger plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.setToJSON(collection);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(WellSetBigInteger set : collection) {
    		
    		WellSetBigInteger output = reader.nextJSONSet();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : set) {

    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /**
     * Tests the set to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSetToJSONArray() throws IOException, JAXBException {
    	
    	String wellPath = "testJSONSet.txt";
    	File wellFile = new File(wellPath);
    	
    	WellSetBigInteger[] sets = new WellSetBigInteger[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.setToJSON(sets);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(WellSetBigInteger set : sets) {
    		
    		WellSetBigInteger output = reader.nextJSONSet();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : set) {

    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*-------------------- Methods for Plate JSON Output ---------------------*/
    
    /**
     * Tests the plate to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testPlateToJSONPlate() throws IOException, JAXBException {    

    	String wellPath = "testJSONPlate.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.plateToJSON(plate);
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			PlateBigInteger output = reader.nextJSONPlate();
			Iterator<WellBigInteger> iter = output.iterator();
			
    		for(WellBigInteger well : plate) {

    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /**
     * Tests the plate to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testPlateToJSONCollection() throws IOException, JAXBException {
    	
    	String wellPath = "testJSONSet.txt";
    	File wellFile = new File(wellPath);
    	
    	List<PlateBigInteger> collection = Arrays.asList(array);
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.plateToJSON(collection);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(PlateBigInteger plate : collection) {
    		
    		PlateBigInteger output = reader.nextJSONPlate();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : plate) {

    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /**
     * Tests the plate to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testPlateToJSONArray() throws IOException, JAXBException {
    	
    	String wellPath = "testJSONSet.txt";
    	File wellFile = new File(wellPath);
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.plateToJSON(array);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(PlateBigInteger plate : array) {
    		
    		PlateBigInteger output = reader.nextJSONPlate();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : plate) {

    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*-------------------- Methods for Stack JSON Output ---------------------*/
    
    /**
     * Tests the stack to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testStackToJSON() throws IOException, JAXBException {
    	
    	String wellPath = "testJSONPlate.txt";
    	StackBigInteger stack = new StackBigInteger(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.stackToJSON(stack);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		StackBigInteger output = reader.nextJSONStack();
		Iterator<PlateBigInteger> iterStack = output.iterator();
		
    	for(PlateBigInteger plate : stack) {
    		
    		PlateBigInteger outputPlate = iterStack.next();
    		Iterator<WellBigInteger> iterPlate = outputPlate.iterator();
    		
    		for(WellBigInteger well : plate) {

    			WellBigInteger outputWell = iterPlate.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
     		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /**
     * Tests the stack to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testStackToJSONCollection() throws IOException, JAXBException {

    	String wellPath = "testJSONPlate.txt";
    	File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.stackToJSON(stacks);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		
    	for(StackBigInteger stack : stacks) {
    		
    		StackBigInteger output = reader.nextJSONStack();
    		Iterator<PlateBigInteger> iterStack = output.iterator();
    		
    		for(PlateBigInteger plate : stack) {
    			
    			PlateBigInteger outputPlate = iterStack.next();
        		Iterator<WellBigInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellBigInteger well : plate) {

        			WellBigInteger outputWell = iterPlate.next();
        			
        			assertEquals(well, outputWell);
        			assertEquals(well.data(), outputWell.data());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
		
    }
    
    /**
     * Tests the stack to JSON method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testStackToJSONArray() throws IOException, JAXBException {
    	
    	String wellPath = "testJSONPlate.txt";
    	File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.stackToJSON(stacks.toArray(new StackBigInteger[stacks.size()]));
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		
    	for(StackBigInteger stack : stacks) {
    		
    		StackBigInteger output = reader.nextJSONStack();
    		Iterator<PlateBigInteger> iterStack = output.iterator();
    		
    		for(PlateBigInteger plate : stack) {
    			
    			PlateBigInteger outputPlate = iterStack.next();
        		Iterator<WellBigInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellBigInteger well : plate) {

        			WellBigInteger outputWell = iterPlate.next();
        			
        			assertEquals(well, outputWell);
        			assertEquals(well.data(), outputWell.data());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*------------------------ Methods for Result XML Output ------------------------*/
    
    /**
     * Tests the result to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testResultToXML() throws IOException, JAXBException, 
    	TransformerException, ParserConfigurationException {

    	String wellPath = "testXMLResults.txt";

    	for(Map<WellBigInteger, BigInteger> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.resultToXML(map);
		    
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			WellSetBigInteger output = reader.nextXMLResult();
			Iterator<WellBigInteger> iter = output.iterator();
			
			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {	
				
				WellBigInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));				
			}
			
			reader.close(); 
			wellWriter.close();
			wellFile.delete();
			
		}
    	
    }
    
    /**
     * Tests the result to XML method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testResultToXMLList() throws IOException, TransformerException, ParserConfigurationException, JAXBException {
    	
    	String wellPath = "testXMLResults.txt";
  		   
		File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.resultToXML(maps);
	    
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		
		for(Map<WellBigInteger, BigInteger> map : maps) {
		
			WellSetBigInteger output = reader.nextXMLResult();
			Iterator<WellBigInteger> iter = output.iterator();

			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {
			
				WellBigInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));
			}
		
		}
		
		reader.close(); 
		wellWriter.close();
		wellFile.delete();

    }
    
    /**
     * Tests the result to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testResultToXMLLabel() throws IOException, JAXBException, ParserConfigurationException, TransformerException {
    
    	String wellPath = "testXMLResults.txt";

    	for(Map<WellBigInteger, BigInteger> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.resultToXML(map, testLabel);
		    
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			WellSetBigInteger output = reader.nextXMLResult();
			
			assertEquals(testLabel, output.label());
			
			Iterator<WellBigInteger> iter = output.iterator();
			
			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {	
				
				WellBigInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));				
			}
			
			reader.close(); 
			wellWriter.close();
			wellFile.delete();
			
		}
    	
    }
    
    /**
     * Tests the result to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testResultToXMLListLabels() throws IOException, JAXBException, TransformerException, ParserConfigurationException {
    	
    	String wellPath = "testXMLResults.txt";
		   
		File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.resultToXML(maps, labelList);
	    
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		Iterator<String> labelIter = labelList.iterator();
		
		for(Map<WellBigInteger, BigInteger> map : maps) {
		
			WellSetBigInteger output = reader.nextXMLResult();
			Iterator<WellBigInteger> iter = output.iterator();

			assertEquals(labelIter.next(), output.label());
			
			for(Map.Entry<WellBigInteger, BigInteger> entry : map.entrySet()) {
			
				WellBigInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertEquals(entry.getValue(), next.get(0));
			}
		
		}
		
		reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*--------------------- Methods for Well XML Output ----------------------*/
    
    /**
     * Tests the well to XML method.
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testWellToXML() throws IOException, ParserConfigurationException, TransformerException, JAXBException {

    	String wellPath = "testXMLWell.txt";

    	for(PlateBigInteger plate : array) {
    		
    		for(WellBigInteger well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
    			wellWriter.wellToXML(well);
			    
    			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
    			WellBigInteger output = reader.nextXMLWell();

    			assertEquals(well, output);
    			assertEquals(well.data(), output.data());
    			
    			reader.close(); 
    			wellWriter.close();
    			wellFile.delete();
    		}
		}
    	
    }
    
    /**
     * Tests the well to XML method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testWellToXMLCollection() throws IOException, TransformerException, ParserConfigurationException, JAXBException {

    	String wellPath = "testXMLWell.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			
    		for(WellBigInteger well : plate) {
    		
    			WellBigInteger output = reader.nextXMLWell();
    			
    			assertEquals(well, output);
    			assertEquals(well.data(), output.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /**
     * Tests the well to XML method.
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testWellToXMLArray() throws IOException, ParserConfigurationException, TransformerException, JAXBException {

    	String wellPath = "testJSONWell.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.wellToXML(plate.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			
    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger output = reader.nextXMLWell();
    			
    			assertEquals(well, output);
    			assertEquals(well.data(), output.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /*-------------------- Methods for Well Set XML Output --------------------*/
    
    /**
     * Tests the set to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testSetToXML() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	String wellPath = "testXMLSet.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.setToXML(plate.dataSet());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			WellSetBigInteger output = reader.nextXMLSet();
			Iterator<WellBigInteger> iter = output.iterator();
			
    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /**
     * Tests the set to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testSetToXMLCollection() throws IOException, JAXBException, TransformerException, ParserConfigurationException {
    	
    	String wellPath = "testXMLSet.txt";
    	File wellFile = new File(wellPath);
    	
    	List<WellSetBigInteger> collection = new ArrayList<WellSetBigInteger>();
    	
    	for(PlateBigInteger plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.setToXML(collection);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(WellSetBigInteger set : collection) {
    		
    		WellSetBigInteger output = reader.nextXMLSet();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : set) {
    			
    			WellBigInteger outputWell = iter.next();
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /**
     * Tests the set to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testSetToXMLArray() throws IOException, JAXBException, ParserConfigurationException, TransformerException {
    	
    	String wellPath = "testXMLSet.txt";
    	File wellFile = new File(wellPath);
    	
    	WellSetBigInteger[] sets = new WellSetBigInteger[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.setToXML(sets);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(WellSetBigInteger set : sets) {
    		
    		WellSetBigInteger output = reader.nextXMLSet();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : set) {
    			
    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*-------------------- Methods for Plate XML Output ---------------------*/
    
    /**
     * Tests the plate to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testPlateToXMLPlate() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

    	String wellPath = "testXMLPlate.txt";

    	for(PlateBigInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
			wellWriter.plateToXML(plate);
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
			PlateBigInteger output = reader.nextXMLPlate();
			Iterator<WellBigInteger> iter = output.iterator();
			
    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger outputWell = iter.next();
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellFile.delete();
		}
    	
    }
    
    /**
     * Tests the plate to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testPlateToXMLCollection() throws IOException, JAXBException, TransformerException, ParserConfigurationException {
    	
    	String wellPath = "testXMLSet.txt";
    	File wellFile = new File(wellPath);
    	
    	List<PlateBigInteger> collection = Arrays.asList(array);
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.plateToXML(collection);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(PlateBigInteger plate : collection) {
    		
    		PlateBigInteger output = reader.nextXMLPlate();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /**
     * Tests the plate to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testPlateToXMLArray() throws IOException, JAXBException, ParserConfigurationException, TransformerException {
    	
    	String wellPath = "testXMLSet.txt";
    	File wellFile = new File(wellPath);
    	
		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.plateToXML(array);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);

    	for(PlateBigInteger plate : array) {
    		
    		PlateBigInteger output = reader.nextXMLPlate();
    		Iterator<WellBigInteger> iter = output.iterator();

    		for(WellBigInteger well : plate) {
    			
    			WellBigInteger outputWell = iter.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*-------------------- Methods for Stack XML Output ---------------------*/
    
    /**
     * Tests the stack to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testStackToXML() throws IOException, JAXBException, TransformerException, ParserConfigurationException {
    	
    	String wellPath = "testXMLPlate.txt";
    	StackBigInteger stack = new StackBigInteger(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.stackToXML(stack);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		StackBigInteger output = reader.nextXMLStack();
		Iterator<PlateBigInteger> iterStack = output.iterator();
		
    	for(PlateBigInteger plate : stack) {
    		
    		PlateBigInteger outputPlate = iterStack.next();
    		Iterator<WellBigInteger> iterPlate = outputPlate.iterator();
    		
    		for(WellBigInteger well : plate) {

    			WellBigInteger outputWell = iterPlate.next();
    			
    			assertEquals(well, outputWell);
    			assertEquals(well.data(), outputWell.data());
    		}
     		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /**
     * Tests the stack to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testStackToXMLCollection() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	String wellPath = "testXMLPlate.txt";
    	File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.stackToXML(stacks);
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		
    	for(StackBigInteger stack : stacks) {
    		
    		StackBigInteger output = reader.nextXMLStack();
    		Iterator<PlateBigInteger> iterStack = output.iterator();
    		
    		for(PlateBigInteger plate : stack) {
    			
    			PlateBigInteger outputPlate = iterStack.next();
        		Iterator<WellBigInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellBigInteger well : plate) {

        			WellBigInteger outputWell = iterPlate.next();
        			
        			assertEquals(well, outputWell);
        			assertEquals(well.data(), outputWell.data());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
		
    }
    
    /**
     * Tests the stack to XML method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testStackToXMLArray() throws IOException, JAXBException, ParserConfigurationException, TransformerException {
    	
    	String wellPath = "testXMLPlate.txt";
    	File wellFile = new File(wellPath);

		PlateWriterBigInteger wellWriter = new PlateWriterBigInteger(wellFile);
		wellWriter.stackToXML(stacks.toArray(new StackBigInteger[stacks.size()]));
		
		PlateReaderBigInteger reader = new PlateReaderBigInteger(wellFile);
		
    	for(StackBigInteger stack : stacks) {
    		
    		StackBigInteger output = reader.nextXMLStack();
    		Iterator<PlateBigInteger> iterStack = output.iterator();
    		
    		for(PlateBigInteger plate : stack) {
    			
    			PlateBigInteger outputPlate = iterStack.next();
        		Iterator<WellBigInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellBigInteger well : plate) {

        			WellBigInteger outputWell = iterPlate.next();
        			
        			assertEquals(well, outputWell);
        			assertEquals(well.data(), outputWell.data());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellFile.delete();
    }
    
    /*------------ Methods for Setting Print Stream and Delimiter ------------*/
    
    /**
     * Tests the set and get delimiter methods.
     */
    @Test
    public void testDelimiter() {
    	
    	Random random = new Random();
        String last = "\t";
    	
    	for(int i = 0; i < 100; i++) {   	
    		String next = "" + ((char) (random.nextInt(126 - 32 + 1) + 32));
    		assertEquals(plateWriter.getDelimiter(), last);
    		plateWriter.setDelimiter(next);
    		assertEquals(plateWriter.getDelimiter(), next);
    		last = next;
    	}
    	  	
    }

}
