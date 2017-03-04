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

package com.github.jessemull.microflex.io.iodouble;

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

import com.github.jessemull.microflex.doubleflex.io.PlateReaderDouble;
import com.github.jessemull.microflex.doubleflex.io.PlateWriterDouble;
import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.StackDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.doubleflex.stat.MeanDouble;
import com.github.jessemull.microflex.util.RandomUtil;

/*------------------------------- Dependencies -------------------------------*/

/**
 * Tests methods in the plate writer double class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateWriterDoubleTest {

    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static double minValue = 0.0;      // Minimum double value for wells
	private static double maxValue = 100.0;    // Maximum double value for wells
	
	/* The addition operation */
	
	private static MeanDouble mean = new MeanDouble();

	/* Random objects and numbers for testing */

	private static int rows = PlateDouble.ROWS_96WELL;
	private static int columns = PlateDouble.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int type = PlateDouble.PLATE_96WELL;
	private static int stackNumber = 5;
	
	private static PlateDouble[] array = new PlateDouble[plateNumber];
	private static List<Map<WellDouble, Double>> maps = new ArrayList<Map<WellDouble, Double>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackDouble> stacks = new ArrayList<StackDouble>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterDouble plateWriter;
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
			
			PlateDouble plate = RandomUtil.randomPlateDouble(
					rows, columns, minValue, maxValue, length, label);
			
			labelList.add(label);
			array[j] = plate;
			maps.add((TreeMap<WellDouble, Double>) mean.plate(plate));
		}
		
		for(int k = 0; k < stackNumber; k++) {	
			StackDouble stack = RandomUtil.randomStackDouble(
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
		plateWriter = new PlateWriterDouble(file);
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
		
    	PlateWriterDouble writer = new PlateWriterDouble(file);
    	
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
		
    	PlateWriterDouble writer = new PlateWriterDouble(file);
    	
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
    	PlateWriterDouble writer = new PlateWriterDouble(stream);
    	
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
    	PlateWriterDouble writer1 = new PlateWriterDouble(stream, true);
    	PlateWriterDouble writer2 = new PlateWriterDouble(stream, false);
    	
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

    	PlateWriterDouble writer = new PlateWriterDouble(path);
    	
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

    	PlateWriterDouble writer = new PlateWriterDouble(path, "UTF-8");
    	
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
    	PlateWriterDouble writer = new PlateWriterDouble(inputWriter);
    	
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
    	PlateWriterDouble writer1 = new PlateWriterDouble(inputWriter, true);
    	PlateWriterDouble writer2 = new PlateWriterDouble(inputWriter, false);
    	
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

		for(Map<WellDouble, Double> map : maps) {
			plateWriter.resultToPlateMap(map, type);
		}

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index++];
			PlateDouble output = reader.nextMap();

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index++];
			PlateDouble output = reader.nextMap();

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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

		for(Map<WellDouble, Double> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns);
		}

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index++];
			PlateDouble output = reader.nextMap();

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index++];
			PlateDouble output = reader.nextMap();

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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

		for(Map<WellDouble, Double> map : maps) {
			plateWriter.resultToPlateMap(map, type, testLabel);
		}

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index++];
			PlateDouble output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index];
			PlateDouble output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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
		
		for(Map<WellDouble, Double> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns, testLabel);
		}

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index++];
			PlateDouble output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateDouble input = array[index];
			PlateDouble output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);

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

    	for(Map<WellDouble, Double> map : maps) {
			plateWriter.resultToTable(map);
		}

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateDouble input = array[index++];
			WellSetDouble output = reader.nextTable();

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);

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

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateDouble input = array[index++];
			WellSetDouble output = reader.nextTable();

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);

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
    	
    	for(Map<WellDouble, Double> map : maps) {
			plateWriter.resultToTable(map, testLabel);
		}

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateDouble input = array[index++];
			WellSetDouble output = reader.nextTable();

			assertEquals(testLabel, output.label());

			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);

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

		PlateReaderDouble reader = new PlateReaderDouble(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateDouble input = array[index];
			WellSetDouble output = reader.nextTable();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellDouble> iter1 = input.iterator();
			Iterator<WellDouble> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				Double inputResult = mean.well(iter1.next());
				Double outputResult = iter2.next().data().get(0);

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

    	for(Map<WellDouble, Double> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.resultToJSON(map);
		    
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextJSONResult();
			Iterator<WellDouble> iter = output.iterator();
			
			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {	
				
				WellDouble next = iter.next();

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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.resultToJSON(maps);
	    
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

		for(Map<WellDouble, Double> map : maps) {	
			
			WellSetDouble set = reader.nextJSONResult();
			Iterator<WellDouble> iter = set.iterator();
			
			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {
				
				WellDouble next = iter.next();
				
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
    	
    	for(Map<WellDouble, Double> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.resultToJSON(map, testLabel);
		    
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextJSONResult();
			Iterator<WellDouble> iter = output.iterator();
			
			assertEquals(testLabel, output.label());
			
			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {	
				
				WellDouble next = iter.next();

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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.resultToJSON(maps, labelList);
	    
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

		for(Map<WellDouble, Double> map : maps) {	
			
			WellSetDouble set = reader.nextJSONResult();
			Iterator<WellDouble> iter = set.iterator();
			String inputLabel = labelIter.next();
			
			assertEquals(inputLabel, set.label());

			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {
				
				WellDouble next = iter.next();
				
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

    	for(PlateDouble plate : array) {
    		
    		for(WellDouble well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
    			wellWriter.wellToJSON(well);
			    
    			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
    			WellDouble output = reader.nextJSONWell();

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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			
    			WellDouble output = reader.nextJSONWell();
    			
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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.wellToJSON(plate.toArray());
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			
    			WellDouble output = reader.nextJSONWell();
    			
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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.setToJSON(plate.dataSet());
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextJSONSet();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			
    			WellDouble outputWell = iter.next();
    			
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
    	
    	List<WellSetDouble> collection = new ArrayList<WellSetDouble>();
    	
    	for(PlateDouble plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.setToJSON(collection);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : collection) {
    		
    		WellSetDouble output = reader.nextJSONSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {

    			WellDouble outputWell = iter.next();
    			
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
    	
    	WellSetDouble[] sets = new WellSetDouble[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.setToJSON(sets);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : sets) {
    		
    		WellSetDouble output = reader.nextJSONSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {

    			WellDouble outputWell = iter.next();
    			
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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.plateToJSON(plate);
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			PlateDouble output = reader.nextJSONPlate();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {

    			WellDouble outputWell = iter.next();
    			
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
    	
    	List<PlateDouble> collection = Arrays.asList(array);
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.plateToJSON(collection);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : collection) {
    		
    		PlateDouble output = reader.nextJSONPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {

    			WellDouble outputWell = iter.next();
    			
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
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.plateToJSON(array);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : array) {
    		
    		PlateDouble output = reader.nextJSONPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {

    			WellDouble outputWell = iter.next();
    			
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
    	StackDouble stack = new StackDouble(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.stackToJSON(stack);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		StackDouble output = reader.nextJSONStack();
		Iterator<PlateDouble> iterStack = output.iterator();
		
    	for(PlateDouble plate : stack) {
    		
    		PlateDouble outputPlate = iterStack.next();
    		Iterator<WellDouble> iterPlate = outputPlate.iterator();
    		
    		for(WellDouble well : plate) {

    			WellDouble outputWell = iterPlate.next();
    			
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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextJSONStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {

        			WellDouble outputWell = iterPlate.next();
        			
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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.stackToJSON(stacks.toArray(new StackDouble[stacks.size()]));
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextJSONStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {

        			WellDouble outputWell = iterPlate.next();
        			
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

    	for(Map<WellDouble, Double> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.resultToXML(map);
		    
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextXMLResult();
			Iterator<WellDouble> iter = output.iterator();
			
			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {	
				
				WellDouble next = iter.next();

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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.resultToXML(maps);
	    
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
		for(Map<WellDouble, Double> map : maps) {
		
			WellSetDouble output = reader.nextXMLResult();
			Iterator<WellDouble> iter = output.iterator();

			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {
			
				WellDouble next = iter.next();

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

    	for(Map<WellDouble, Double> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.resultToXML(map, testLabel);
		    
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextXMLResult();
			
			assertEquals(testLabel, output.label());
			
			Iterator<WellDouble> iter = output.iterator();
			
			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {	
				
				WellDouble next = iter.next();

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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.resultToXML(maps, labelList);
	    
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		Iterator<String> labelIter = labelList.iterator();
		
		for(Map<WellDouble, Double> map : maps) {
		
			WellSetDouble output = reader.nextXMLResult();
			Iterator<WellDouble> iter = output.iterator();

			assertEquals(labelIter.next(), output.label());
			
			for(Map.Entry<WellDouble, Double> entry : map.entrySet()) {
			
				WellDouble next = iter.next();

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

    	for(PlateDouble plate : array) {
    		
    		for(WellDouble well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
    			wellWriter.wellToXML(well);
			    
    			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
    			WellDouble output = reader.nextXMLWell();

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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.wellToXML(plate.dataSet().allWells());
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    		
    			WellDouble output = reader.nextXMLWell();
    			
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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.wellToXML(plate.toArray());
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			
    			WellDouble output = reader.nextXMLWell();
    			
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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.setToXML(plate.dataSet());
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextXMLSet();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			
    			WellDouble outputWell = iter.next();
    			
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
    	
    	List<WellSetDouble> collection = new ArrayList<WellSetDouble>();
    	
    	for(PlateDouble plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.setToXML(collection);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : collection) {
    		
    		WellSetDouble output = reader.nextXMLSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {
    			
    			WellDouble outputWell = iter.next();
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
    	
    	WellSetDouble[] sets = new WellSetDouble[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.setToXML(sets);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : sets) {
    		
    		WellSetDouble output = reader.nextXMLSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {
    			
    			WellDouble outputWell = iter.next();
    			
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

    	for(PlateDouble plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
			wellWriter.plateToXML(plate);
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			PlateDouble output = reader.nextXMLPlate();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			
    			WellDouble outputWell = iter.next();
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
    	
    	List<PlateDouble> collection = Arrays.asList(array);
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.plateToXML(collection);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : collection) {
    		
    		PlateDouble output = reader.nextXMLPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {
    			
    			WellDouble outputWell = iter.next();
    			
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
    	
		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.plateToXML(array);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : array) {
    		
    		PlateDouble output = reader.nextXMLPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {
    			
    			WellDouble outputWell = iter.next();
    			
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
    	StackDouble stack = new StackDouble(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.stackToXML(stack);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		StackDouble output = reader.nextXMLStack();
		Iterator<PlateDouble> iterStack = output.iterator();
		
    	for(PlateDouble plate : stack) {
    		
    		PlateDouble outputPlate = iterStack.next();
    		Iterator<WellDouble> iterPlate = outputPlate.iterator();
    		
    		for(WellDouble well : plate) {

    			WellDouble outputWell = iterPlate.next();
    			
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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextXMLStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {

        			WellDouble outputWell = iterPlate.next();
        			
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

		PlateWriterDouble wellWriter = new PlateWriterDouble(wellFile);
		wellWriter.stackToXML(stacks.toArray(new StackDouble[stacks.size()]));
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextXMLStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {

        			WellDouble outputWell = iterPlate.next();
        			
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
