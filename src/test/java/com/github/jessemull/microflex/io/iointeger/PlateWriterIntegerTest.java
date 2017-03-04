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

package com.github.jessemull.microflex.io.iointeger;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

import com.github.jessemull.microflex.integerflex.io.PlateReaderInteger;
import com.github.jessemull.microflex.integerflex.io.PlateWriterInteger;
import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.integerflex.stat.MeanInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/*------------------------------- Dependencies -------------------------------*/

/**
 * Tests methods in the plate writer int class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateWriterIntegerTest {

    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = 0;      // Minimum int value for wells
	private static int maxValue = 100;    // Maximum int value for wells
	
	/* The addition operation */
	
	private static MeanInteger mean = new MeanInteger();

	/* Random objects and numbers for testing */

	private static int rows = PlateInteger.ROWS_96WELL;
	private static int columns = PlateInteger.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int type = PlateInteger.PLATE_96WELL;
	private static int stackNumber = 5;
	
	private static PlateInteger[] array = new PlateInteger[plateNumber];
	private static List<Map<WellInteger, Integer>> maps = new ArrayList<Map<WellInteger, Integer>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackInteger> stacks = new ArrayList<StackInteger>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterInteger plateWriter;
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
			
			PlateInteger plate = RandomUtil.randomPlateInteger(
					rows, columns, minValue, maxValue, length, label);
			
			labelList.add(label);
			array[j] = plate;
			
			Map<WellInteger, Double> toAddDouble = mean.plate(plate);
			TreeMap<WellInteger, Integer> toAddBigInteger = new TreeMap<WellInteger, Integer>();
			
			for(Map.Entry<WellInteger, Double> entry : toAddDouble.entrySet()) {
				toAddBigInteger.put(entry.getKey(), entry.getValue().intValue());
			}
			
			maps.add(toAddBigInteger);
		}
		
		for(int k = 0; k < stackNumber; k++) {	
			StackInteger stack = RandomUtil.randomStackInteger(
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
		plateWriter = new PlateWriterInteger(file);
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
		
    	PlateWriterInteger writer = new PlateWriterInteger(file);
    	
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
		
    	PlateWriterInteger writer = new PlateWriterInteger(file);
    	
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
    	PlateWriterInteger writer = new PlateWriterInteger(stream);
    	
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
    	PlateWriterInteger writer1 = new PlateWriterInteger(stream, true);
    	PlateWriterInteger writer2 = new PlateWriterInteger(stream, false);
    	
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

    	PlateWriterInteger writer = new PlateWriterInteger(path);
    	
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

    	PlateWriterInteger writer = new PlateWriterInteger(path, "UTF-8");
    	
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
    	PlateWriterInteger writer = new PlateWriterInteger(inputWriter);
    	
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
    	PlateWriterInteger writer1 = new PlateWriterInteger(inputWriter, true);
    	PlateWriterInteger writer2 = new PlateWriterInteger(inputWriter, false);
    	
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

		for(Map<WellInteger, Integer> map : maps) {
			plateWriter.resultToPlateMap(map, type);
		}

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index++];
			PlateInteger output = reader.nextMap();

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index++];
			PlateInteger output = reader.nextMap();

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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

		for(Map<WellInteger, Integer> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns);
		}

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index++];
			PlateInteger output = reader.nextMap();

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index++];
			PlateInteger output = reader.nextMap();

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);

				assertTrue(inputResult == outputResult);
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

		for(Map<WellInteger, Integer> map : maps) {
			plateWriter.resultToPlateMap(map, type, testLabel);
		}

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index++];
			PlateInteger output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index];
			PlateInteger output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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
		
		for(Map<WellInteger, Integer> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns, testLabel);
		}

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index++];
			PlateInteger output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateInteger input = array[index];
			PlateInteger output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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

    	for(Map<WellInteger, Integer> map : maps) {
			plateWriter.resultToTable(map);
		}

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateInteger input = array[index++];
			WellSetInteger output = reader.nextTable();

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateInteger input = array[index++];
			WellSetInteger output = reader.nextTable();

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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
    	
    	for(Map<WellInteger, Integer> map : maps) {
			plateWriter.resultToTable(map, testLabel);
		}

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateInteger input = array[index++];
			WellSetInteger output = reader.nextTable();

			assertEquals(testLabel, output.label());

			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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

		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateInteger input = array[index];
			WellSetInteger output = reader.nextTable();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellInteger> iter1 = input.iterator();
			Iterator<WellInteger> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				int inputResult = new Double(mean.well(iter1.next())).intValue();
				int outputResult = iter2.next().data().get(0);
				
				assertTrue(inputResult == outputResult);
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

    	for(Map<WellInteger, Integer> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.resultToJSON(map);
		    
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			WellSetInteger output = reader.nextJSONResult();
			Iterator<WellInteger> iter = output.iterator();
			
			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {	
				
				WellInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));				
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.resultToJSON(maps);
	    
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

		for(Map<WellInteger, Integer> map : maps) {	
			
			WellSetInteger set = reader.nextJSONResult();
			Iterator<WellInteger> iter = set.iterator();
			
			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {
				
				WellInteger next = iter.next();
				
				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));		
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
    	
    	for(Map<WellInteger, Integer> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.resultToJSON(map, testLabel);
		    
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			WellSetInteger output = reader.nextJSONResult();
			Iterator<WellInteger> iter = output.iterator();
			
			assertEquals(testLabel, output.label());
			
			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {	
				
				WellInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));				
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.resultToJSON(maps, labelList);
	    
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

		for(Map<WellInteger, Integer> map : maps) {	
			
			WellSetInteger set = reader.nextJSONResult();
			Iterator<WellInteger> iter = set.iterator();
			String inputLabel = labelIter.next();
			
			assertEquals(inputLabel, set.label());

			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {
				
				WellInteger next = iter.next();
				
				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));		
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

    	for(PlateInteger plate : array) {
    		
    		for(WellInteger well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
    			wellWriter.wellToJSON(well);
			    
    			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
    			WellInteger output = reader.nextJSONWell();

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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			
    		for(WellInteger well : plate) {
    			
    			WellInteger output = reader.nextJSONWell();
    			
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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.wellToJSON(plate.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			
    		for(WellInteger well : plate) {
    			
    			WellInteger output = reader.nextJSONWell();
    			
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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.setToJSON(plate.dataSet());
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			WellSetInteger output = reader.nextJSONSet();
			Iterator<WellInteger> iter = output.iterator();
			
    		for(WellInteger well : plate) {
    			
    			WellInteger outputWell = iter.next();
    			
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
    	
    	List<WellSetInteger> collection = new ArrayList<WellSetInteger>();
    	
    	for(PlateInteger plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.setToJSON(collection);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(WellSetInteger set : collection) {
    		
    		WellSetInteger output = reader.nextJSONSet();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : set) {

    			WellInteger outputWell = iter.next();
    			
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
    	
    	WellSetInteger[] sets = new WellSetInteger[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.setToJSON(sets);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(WellSetInteger set : sets) {
    		
    		WellSetInteger output = reader.nextJSONSet();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : set) {

    			WellInteger outputWell = iter.next();
    			
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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.plateToJSON(plate);
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			PlateInteger output = reader.nextJSONPlate();
			Iterator<WellInteger> iter = output.iterator();
			
    		for(WellInteger well : plate) {

    			WellInteger outputWell = iter.next();
    			
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
    	
    	List<PlateInteger> collection = Arrays.asList(array);
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.plateToJSON(collection);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(PlateInteger plate : collection) {
    		
    		PlateInteger output = reader.nextJSONPlate();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : plate) {

    			WellInteger outputWell = iter.next();
    			
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
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.plateToJSON(array);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(PlateInteger plate : array) {
    		
    		PlateInteger output = reader.nextJSONPlate();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : plate) {

    			WellInteger outputWell = iter.next();
    			
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
    	StackInteger stack = new StackInteger(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.stackToJSON(stack);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		StackInteger output = reader.nextJSONStack();
		Iterator<PlateInteger> iterStack = output.iterator();
		
    	for(PlateInteger plate : stack) {
    		
    		PlateInteger outputPlate = iterStack.next();
    		Iterator<WellInteger> iterPlate = outputPlate.iterator();
    		
    		for(WellInteger well : plate) {

    			WellInteger outputWell = iterPlate.next();
    			
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.stackToJSON(stacks);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		
    	for(StackInteger stack : stacks) {
    		
    		StackInteger output = reader.nextJSONStack();
    		Iterator<PlateInteger> iterStack = output.iterator();
    		
    		for(PlateInteger plate : stack) {
    			
    			PlateInteger outputPlate = iterStack.next();
        		Iterator<WellInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellInteger well : plate) {

        			WellInteger outputWell = iterPlate.next();
        			
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.stackToJSON(stacks.toArray(new StackInteger[stacks.size()]));
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		
    	for(StackInteger stack : stacks) {
    		
    		StackInteger output = reader.nextJSONStack();
    		Iterator<PlateInteger> iterStack = output.iterator();
    		
    		for(PlateInteger plate : stack) {
    			
    			PlateInteger outputPlate = iterStack.next();
        		Iterator<WellInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellInteger well : plate) {

        			WellInteger outputWell = iterPlate.next();
        			
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

    	for(Map<WellInteger, Integer> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.resultToXML(map);
		    
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			WellSetInteger output = reader.nextXMLResult();
			Iterator<WellInteger> iter = output.iterator();
			
			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {	
				
				WellInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));				
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.resultToXML(maps);
	    
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		
		for(Map<WellInteger, Integer> map : maps) {
		
			WellSetInteger output = reader.nextXMLResult();
			Iterator<WellInteger> iter = output.iterator();

			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {
			
				WellInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));
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

    	for(Map<WellInteger, Integer> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.resultToXML(map, testLabel);
		    
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			WellSetInteger output = reader.nextXMLResult();
			
			assertEquals(testLabel, output.label());
			
			Iterator<WellInteger> iter = output.iterator();
			
			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {	
				
				WellInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));				
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.resultToXML(maps, labelList);
	    
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		Iterator<String> labelIter = labelList.iterator();
		
		for(Map<WellInteger, Integer> map : maps) {
		
			WellSetInteger output = reader.nextXMLResult();
			Iterator<WellInteger> iter = output.iterator();

			assertEquals(labelIter.next(), output.label());
			
			for(Map.Entry<WellInteger, Integer> entry : map.entrySet()) {
			
				WellInteger next = iter.next();

				assertEquals(entry.getKey().index(), next.index());
				assertTrue(entry.getValue() == next.get(0));
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

    	for(PlateInteger plate : array) {
    		
    		for(WellInteger well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
    			wellWriter.wellToXML(well);
			    
    			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
    			WellInteger output = reader.nextXMLWell();

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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.wellToXML(plate.dataSet().allWells());
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			
    		for(WellInteger well : plate) {
    		
    			WellInteger output = reader.nextXMLWell();
    			
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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.wellToXML(plate.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			
    		for(WellInteger well : plate) {
    			
    			WellInteger output = reader.nextXMLWell();
    			
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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.setToXML(plate.dataSet());
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			WellSetInteger output = reader.nextXMLSet();
			Iterator<WellInteger> iter = output.iterator();
			
    		for(WellInteger well : plate) {
    			
    			WellInteger outputWell = iter.next();
    			
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
    	
    	List<WellSetInteger> collection = new ArrayList<WellSetInteger>();
    	
    	for(PlateInteger plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.setToXML(collection);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(WellSetInteger set : collection) {
    		
    		WellSetInteger output = reader.nextXMLSet();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : set) {
    			
    			WellInteger outputWell = iter.next();
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
    	
    	WellSetInteger[] sets = new WellSetInteger[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.setToXML(sets);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(WellSetInteger set : sets) {
    		
    		WellSetInteger output = reader.nextXMLSet();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : set) {
    			
    			WellInteger outputWell = iter.next();
    			
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

    	for(PlateInteger plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
			wellWriter.plateToXML(plate);
			
			PlateReaderInteger reader = new PlateReaderInteger(wellFile);
			PlateInteger output = reader.nextXMLPlate();
			Iterator<WellInteger> iter = output.iterator();
			
    		for(WellInteger well : plate) {
    			
    			WellInteger outputWell = iter.next();
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
    	
    	List<PlateInteger> collection = Arrays.asList(array);
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.plateToXML(collection);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(PlateInteger plate : collection) {
    		
    		PlateInteger output = reader.nextXMLPlate();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : plate) {
    			
    			WellInteger outputWell = iter.next();
    			
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
    	
		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.plateToXML(array);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);

    	for(PlateInteger plate : array) {
    		
    		PlateInteger output = reader.nextXMLPlate();
    		Iterator<WellInteger> iter = output.iterator();

    		for(WellInteger well : plate) {
    			
    			WellInteger outputWell = iter.next();
    			
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
    	StackInteger stack = new StackInteger(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.stackToXML(stack);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		StackInteger output = reader.nextXMLStack();
		Iterator<PlateInteger> iterStack = output.iterator();
		
    	for(PlateInteger plate : stack) {
    		
    		PlateInteger outputPlate = iterStack.next();
    		Iterator<WellInteger> iterPlate = outputPlate.iterator();
    		
    		for(WellInteger well : plate) {

    			WellInteger outputWell = iterPlate.next();
    			
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.stackToXML(stacks);
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		
    	for(StackInteger stack : stacks) {
    		
    		StackInteger output = reader.nextXMLStack();
    		Iterator<PlateInteger> iterStack = output.iterator();
    		
    		for(PlateInteger plate : stack) {
    			
    			PlateInteger outputPlate = iterStack.next();
        		Iterator<WellInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellInteger well : plate) {

        			WellInteger outputWell = iterPlate.next();
        			
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

		PlateWriterInteger wellWriter = new PlateWriterInteger(wellFile);
		wellWriter.stackToXML(stacks.toArray(new StackInteger[stacks.size()]));
		
		PlateReaderInteger reader = new PlateReaderInteger(wellFile);
		
    	for(StackInteger stack : stacks) {
    		
    		StackInteger output = reader.nextXMLStack();
    		Iterator<PlateInteger> iterStack = output.iterator();
    		
    		for(PlateInteger plate : stack) {
    			
    			PlateInteger outputPlate = iterStack.next();
        		Iterator<WellInteger> iterPlate = outputPlate.iterator();
        		
        		for(WellInteger well : plate) {

        			WellInteger outputWell = iterPlate.next();
        			
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
