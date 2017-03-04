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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class PlateWriterDoubleStringTest {

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
	
	private static String pathPlateWriter = "test.txt";
	private static String pathPrintWriter = "print.txt";
	private static File filePlateWriter;
	private static File filePrintWriter;
	private static PlateWriterDouble plateWriter;
	private static PrintWriter printWriter;
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
		filePlateWriter = new File(pathPlateWriter);
		filePrintWriter = new File(pathPrintWriter);
		plateWriter = new PlateWriterDouble(filePlateWriter);
		printWriter = new PrintWriter(filePrintWriter);
	}
	
	/**
	 * Deletes the file used in the test.
	 */
	@After
	public void afterTests() {
		filePlateWriter.delete();
		filePrintWriter.delete();
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass 
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Tests the result to plate map method.
	 * @throws JAXBException 
	 * @throws IOException 
     */
	@Test
	public void testResultToPlateMapType() throws IOException, JAXBException {

		List<String> results = new ArrayList<String>();
		
		for(Map<WellDouble, Double> map : maps) {
			results.add(plateWriter.resultToPlateMapAsString(map, type));
		}

		for(String string : results) {
			printWriter.println(string);
		}
		
		printWriter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

		String result = plateWriter.resultToPlateMapAsString(maps, type);
		printWriter.println(result);
		
		printWriter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

		List<String> results = new ArrayList<String>();
		
		for(Map<WellDouble, Double> map : maps) {
			results.add(plateWriter.resultToPlateMapAsString(map, rows, columns));
		}

		for(String string : results) {
			printWriter.println(string);
		}
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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
    	
		String result = plateWriter.resultToPlateMapAsString(maps, rows, columns);
		printWriter.println(result);
		
		printWriter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

		List<String> results = new ArrayList<String>();
		
		for(Map<WellDouble, Double> map : maps) {
			results.add(plateWriter.resultToPlateMapAsString(map, type, testLabel));
		}

		for(String string : results) {
			printWriter.println(string);
		}
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

		String result = plateWriter.resultToPlateMapAsString(maps, type, labelList);
		printWriter.println(result);
		
		printWriter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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
		
		List<String> results = new ArrayList<String>();
		
		for(Map<WellDouble, Double> map : maps) {
			results.add(plateWriter.resultToPlateMapAsString(map, rows, columns, testLabel));
		}

		for(String string : results) {
			printWriter.println(string);
		}
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

		String result = plateWriter.resultToPlateMapAsString(maps, rows, columns, labelList);
		printWriter.println(result);
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

    	List<String> results = new ArrayList<String>();
		
		for(Map<WellDouble, Double> map : maps) {
			results.add(plateWriter.resultToTableAsString(map));
		}
		
		for(String string : results) {
			printWriter.println(string);
		}
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

    	String result = plateWriter.resultToTableAsString(maps);
		printWriter.println(result);
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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
    	
    	List<String> results = new ArrayList<String>();
		
		for(Map<WellDouble, Double> map : maps) {
			results.add(plateWriter.resultToTableAsString(map, testLabel));
		}

		for(String string : results) {
			printWriter.println(string);
		}
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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

    	String result = plateWriter.resultToTableAsString(maps, labelList);
		printWriter.println(result);
		
		printWriter.flush();

		PlateReaderDouble reader = new PlateReaderDouble(filePrintWriter);

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
			String result = wellWriter.resultToJSONAsString(map);
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);
			
			wellPrinter.flush();
			
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
			wellPrinter.close();
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
		String result = wellWriter.resultToJSONAsString(maps);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);
		
		wellPrinter.flush();
	    
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
		wellPrinter.close();
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
			String result = wellWriter.resultToJSONAsString(map, testLabel);
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);
			
			wellPrinter.flush();
		    
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
			wellPrinter.close();
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
		String result = wellWriter.resultToJSONAsString(maps, labelList);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);
		
		wellPrinter.flush();
	    
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
		wellPrinter.close();
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
    			String result = wellWriter.wellToJSONAsString(well);
    		    
    			PrintWriter wellPrinter = new PrintWriter(wellFile);
    			wellPrinter.println(result);
    	
    			wellPrinter.flush();
			    
    			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
    			WellDouble output = reader.nextJSONWell();

    			assertEquals(well, output);

    			reader.close(); 
    			wellWriter.close();
    			wellPrinter.close();
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
			String result = wellWriter.wellToJSONAsString(plate.dataSet().allWells());
		   
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);
			
			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			WellDouble output = reader.nextJSONWell();
    			assertEquals(well, output);
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
			String result = wellWriter.wellToJSONAsString(plate.toArray());
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			WellDouble output = reader.nextJSONWell();
    			assertEquals(well, output);
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
			String result = wellWriter.setToJSONAsString(plate.dataSet());

			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextJSONSet();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
		String result = wellWriter.setToJSONAsString(collection);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : collection) {
    		
    		WellSetDouble output = reader.nextJSONSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close();
    	wellPrinter.close();
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
		String result = wellWriter.setToJSONAsString(sets);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : sets) {
    		
    		WellSetDouble output = reader.nextJSONSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
    	wellPrinter.close();
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
    		String result = wellWriter.plateToJSONAsString(plate);
    	  
    		PrintWriter wellPrinter = new PrintWriter(wellFile);
    		wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			PlateDouble output = reader.nextJSONPlate();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
		String result = wellWriter.plateToJSONAsString(collection);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : collection) {
    		
    		PlateDouble output = reader.nextJSONPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.plateToJSONAsString(array);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : array) {
    		
    		PlateDouble output = reader.nextJSONPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.stackToJSONAsString(stack);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		StackDouble output = reader.nextJSONStack();
		Iterator<PlateDouble> iterStack = output.iterator();
		
    	for(PlateDouble plate : stack) {
    		
    		PlateDouble outputPlate = iterStack.next();
    		Iterator<WellDouble> iterPlate = outputPlate.iterator();
    		
    		for(WellDouble well : plate) {
    			assertEquals(well, iterPlate.next());
    		}
     		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.stackToJSONAsString(stacks);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextJSONStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {
        			assertEquals(well, iterPlate.next());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.stackToJSONAsString(stacks.toArray(new StackDouble[stacks.size()]));
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextJSONStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {
        			assertEquals(well, iterPlate.next());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
			String result = wellWriter.resultToXMLAsString(map);
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
		    
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
			wellPrinter.close();
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
		String result = wellWriter.resultToXMLAsString(maps);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
	    
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
		wellPrinter.close();
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
			String result = wellWriter.resultToXMLAsString(map, testLabel);
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
		    
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
			wellPrinter.close();
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
		String result = wellWriter.resultToXMLAsString(maps, labelList);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
	    
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
		wellPrinter.close();
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
    			String result = wellWriter.wellToXMLAsString(well);
    		    
    			PrintWriter wellPrinter = new PrintWriter(wellFile);
    			wellPrinter.println(result);

    			wellPrinter.flush();
			    
    			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
    			WellDouble output = reader.nextXMLWell();

    			assertEquals(well, output);

    			reader.close(); 
    			wellWriter.close();
    			wellPrinter.close();
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
			String result = wellWriter.wellToXMLAsString(plate.dataSet().allWells());
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			WellDouble output = reader.nextXMLWell();
    			assertEquals(well, output);
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
			String result = wellWriter.wellToXMLAsString(plate.toArray());
		    
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			
    		for(WellDouble well : plate) {
    			WellDouble output = reader.nextXMLWell();
    			assertEquals(well, output);
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
			String result = wellWriter.setToXMLAsString(plate.dataSet());
	
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			WellSetDouble output = reader.nextXMLSet();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
		String result = wellWriter.setToXMLAsString(collection);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : collection) {
    		
    		WellSetDouble output = reader.nextXMLSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.setToXMLAsString(sets);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(WellSetDouble set : sets) {
    		
    		WellSetDouble output = reader.nextXMLSet();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : set) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
			String result = wellWriter.plateToXMLAsString(plate);
		   
			PrintWriter wellPrinter = new PrintWriter(wellFile);
			wellPrinter.println(result);

			wellPrinter.flush();
			
			PlateReaderDouble reader = new PlateReaderDouble(wellFile);
			PlateDouble output = reader.nextXMLPlate();
			Iterator<WellDouble> iter = output.iterator();
			
    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
    		reader.close(); 
			wellWriter.close();
			wellPrinter.close();
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
		String result = wellWriter.plateToXMLAsString(collection);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : collection) {
    		
    		PlateDouble output = reader.nextXMLPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.plateToXMLAsString(array);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);

    	for(PlateDouble plate : array) {
    		
    		PlateDouble output = reader.nextXMLPlate();
    		Iterator<WellDouble> iter = output.iterator();

    		for(WellDouble well : plate) {
    			assertEquals(well, iter.next());
    		}
    		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.stackToXMLAsString(stack);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		StackDouble output = reader.nextXMLStack();
		Iterator<PlateDouble> iterStack = output.iterator();
		
    	for(PlateDouble plate : stack) {
    		
    		PlateDouble outputPlate = iterStack.next();
    		Iterator<WellDouble> iterPlate = outputPlate.iterator();
    		
    		for(WellDouble well : plate) {
    			assertEquals(well, iterPlate.next());
    		}
     		
		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.stackToXMLAsString(stacks);
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextXMLStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {
        			assertEquals(well, iterPlate.next());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
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
		String result = wellWriter.stackToXMLAsString(stacks.toArray(new StackDouble[stacks.size()]));
	    
		PrintWriter wellPrinter = new PrintWriter(wellFile);
		wellPrinter.println(result);

		wellPrinter.flush();
		
		PlateReaderDouble reader = new PlateReaderDouble(wellFile);
		
    	for(StackDouble stack : stacks) {
    		
    		StackDouble output = reader.nextXMLStack();
    		Iterator<PlateDouble> iterStack = output.iterator();
    		
    		for(PlateDouble plate : stack) {
    			
    			PlateDouble outputPlate = iterStack.next();
        		Iterator<WellDouble> iterPlate = outputPlate.iterator();
        		
        		for(WellDouble well : plate) {
        			assertEquals(well, iterPlate.next());
        		}
    		}

		}
    	
    	reader.close(); 
		wellWriter.close();
		wellPrinter.close();
		wellFile.delete();
    }

}
