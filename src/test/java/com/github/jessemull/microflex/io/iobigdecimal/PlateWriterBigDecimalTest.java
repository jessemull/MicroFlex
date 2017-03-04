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

package com.github.jessemull.microflex.io.iobigdecimal;

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

import com.github.jessemull.microflex.bigdecimalflex.io.PlateReaderBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.io.PlateWriterBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.StackBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.stat.MeanBigDecimal;
import com.github.jessemull.microflex.util.RandomUtil;

/*------------------------------- Dependencies -------------------------------*/

/**
 * Tests methods in the plate writer big decimal class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateWriterBigDecimalTest {

    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigDecimal minValue = new BigDecimal(0);                         // Minimum big decimal value for wells
	private static BigDecimal maxValue = new BigDecimal(10);                        // Maximum big decimal value for wells
	private static MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);    // Math context for input values
	
	/* The addition operation */
	
	private static MeanBigDecimal mean = new MeanBigDecimal();

	/* Random objects and numbers for testing */

	private static int rows = PlateBigDecimal.ROWS_96WELL;
	private static int columns = PlateBigDecimal.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int type = PlateBigDecimal.PLATE_96WELL;
	private static int stackNumber = 5;
	
	private static PlateBigDecimal[] array = new PlateBigDecimal[plateNumber];
	private static List<Map<WellBigDecimal, BigDecimal>> maps = new ArrayList<Map<WellBigDecimal, BigDecimal>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigDecimal> stacks = new ArrayList<StackBigDecimal>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterBigDecimal plateWriter;
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
			
			PlateBigDecimal plate = RandomUtil.randomPlateBigDecimal(
					rows, columns, minValue, maxValue, length, label);
			
			labelList.add(label);
			array[j] = plate;
			maps.add((TreeMap<WellBigDecimal, BigDecimal>) mean.plate(plate, mc));
		}
		
		for(int k = 0; k < stackNumber; k++) {	
			StackBigDecimal stack = RandomUtil.randomStackBigDecimal(
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
		plateWriter = new PlateWriterBigDecimal(file);
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
		
    	PlateWriterBigDecimal writer = new PlateWriterBigDecimal(file);
    	
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
		
    	PlateWriterBigDecimal writer = new PlateWriterBigDecimal(file);
    	
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
    	PlateWriterBigDecimal writer = new PlateWriterBigDecimal(stream);
    	
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
    	PlateWriterBigDecimal writer1 = new PlateWriterBigDecimal(stream, true);
    	PlateWriterBigDecimal writer2 = new PlateWriterBigDecimal(stream, false);
    	
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

    	PlateWriterBigDecimal writer = new PlateWriterBigDecimal(path);
    	
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

    	PlateWriterBigDecimal writer = new PlateWriterBigDecimal(path, "UTF-8");
    	
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
    	PlateWriterBigDecimal writer = new PlateWriterBigDecimal(inputWriter);
    	
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
    	PlateWriterBigDecimal writer1 = new PlateWriterBigDecimal(inputWriter, true);
    	PlateWriterBigDecimal writer2 = new PlateWriterBigDecimal(inputWriter, false);
    	
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

		for(Map<WellBigDecimal, BigDecimal> map : maps) {
			plateWriter.resultToPlateMap(map, type);
		}

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index++];
			PlateBigDecimal output = reader.nextMap();

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index++];
			PlateBigDecimal output = reader.nextMap();

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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

		for(Map<WellBigDecimal, BigDecimal> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns);
		}

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index++];
			PlateBigDecimal output = reader.nextMap();

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index++];
			PlateBigDecimal output = reader.nextMap();

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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

		for(Map<WellBigDecimal, BigDecimal> map : maps) {
			plateWriter.resultToPlateMap(map, type, testLabel);
		}

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index++];
			PlateBigDecimal output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index];
			PlateBigDecimal output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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
		
		for(Map<WellBigDecimal, BigDecimal> map : maps) {
			plateWriter.resultToPlateMap(map, rows, columns, testLabel);
		}

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index++];
			PlateBigDecimal output = reader.nextMap();

			assertEquals("TestLabel", output.label());
			
			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);
				
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = array[index];
			PlateBigDecimal output = reader.nextMap();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();
			
			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);

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

    	for(Map<WellBigDecimal, BigDecimal> map : maps) {
			plateWriter.resultToTable(map);
		}

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigDecimal input = array[index++];
			WellSetBigDecimal output = reader.nextTable();

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);

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

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigDecimal input = array[index++];
			WellSetBigDecimal output = reader.nextTable();

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);

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
    	
    	for(Map<WellBigDecimal, BigDecimal> map : maps) {
			plateWriter.resultToTable(map, testLabel);
		}

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigDecimal input = array[index++];
			WellSetBigDecimal output = reader.nextTable();

			assertEquals(testLabel, output.label());

			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);

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

		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

		int index = 0;
		while(reader.hasNextTable()) {
			
			PlateBigDecimal input = array[index];
			WellSetBigDecimal output = reader.nextTable();

			assertEquals(labelList.get(index++), output.label());
			
			Iterator<WellBigDecimal> iter1 = input.iterator();
			Iterator<WellBigDecimal> iter2 = output.iterator();

			while(iter1.hasNext() && iter2.hasNext()) {
				
				BigDecimal inputResult = mean.well(iter1.next(), mc);
				BigDecimal outputResult = iter2.next().data().get(0);

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

    	for(Map<WellBigDecimal, BigDecimal> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.resultToJSON(map);
		    
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			WellSetBigDecimal output = reader.nextJSONResult();
			Iterator<WellBigDecimal> iter = output.iterator();
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {	
				
				WellBigDecimal next = iter.next();

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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.resultToJSON(maps);
	    
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

		for(Map<WellBigDecimal, BigDecimal> map : maps) {	
			
			WellSetBigDecimal set = reader.nextJSONResult();
			Iterator<WellBigDecimal> iter = set.iterator();
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {
				
				WellBigDecimal next = iter.next();
				
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
    	
    	for(Map<WellBigDecimal, BigDecimal> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.resultToJSON(map, testLabel);
		    
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			WellSetBigDecimal output = reader.nextJSONResult();
			Iterator<WellBigDecimal> iter = output.iterator();
			
			assertEquals(testLabel, output.label());
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {	
				
				WellBigDecimal next = iter.next();

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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.resultToJSON(maps, labelList);
	    
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

		for(Map<WellBigDecimal, BigDecimal> map : maps) {	
			
			WellSetBigDecimal set = reader.nextJSONResult();
			Iterator<WellBigDecimal> iter = set.iterator();
			String inputLabel = labelIter.next();
			
			assertEquals(inputLabel, set.label());

			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {
				
				WellBigDecimal next = iter.next();
				
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

    	for(PlateBigDecimal plate : array) {
    		
    		for(WellBigDecimal well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
    			wellWriter.wellToJSON(well);
			    
    			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
    			WellBigDecimal output = reader.nextJSONWell();

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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			
    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal output = reader.nextJSONWell();
    			
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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.wellToJSON(plate.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			
    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal output = reader.nextJSONWell();
    			
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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.setToJSON(plate.dataSet());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			WellSetBigDecimal output = reader.nextJSONSet();
			Iterator<WellBigDecimal> iter = output.iterator();
			
    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal outputWell = iter.next();
    			
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
    	
    	List<WellSetBigDecimal> collection = new ArrayList<WellSetBigDecimal>();
    	
    	for(PlateBigDecimal plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.setToJSON(collection);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(WellSetBigDecimal set : collection) {
    		
    		WellSetBigDecimal output = reader.nextJSONSet();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : set) {

    			WellBigDecimal outputWell = iter.next();
    			
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
    	
    	WellSetBigDecimal[] sets = new WellSetBigDecimal[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.setToJSON(sets);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(WellSetBigDecimal set : sets) {
    		
    		WellSetBigDecimal output = reader.nextJSONSet();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : set) {

    			WellBigDecimal outputWell = iter.next();
    			
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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.plateToJSON(plate);
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			PlateBigDecimal output = reader.nextJSONPlate();
			Iterator<WellBigDecimal> iter = output.iterator();
			
    		for(WellBigDecimal well : plate) {

    			WellBigDecimal outputWell = iter.next();
    			
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
    	
    	List<PlateBigDecimal> collection = Arrays.asList(array);
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.plateToJSON(collection);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(PlateBigDecimal plate : collection) {
    		
    		PlateBigDecimal output = reader.nextJSONPlate();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : plate) {

    			WellBigDecimal outputWell = iter.next();
    			
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
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.plateToJSON(array);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(PlateBigDecimal plate : array) {
    		
    		PlateBigDecimal output = reader.nextJSONPlate();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : plate) {

    			WellBigDecimal outputWell = iter.next();
    			
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
    	StackBigDecimal stack = new StackBigDecimal(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.stackToJSON(stack);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		StackBigDecimal output = reader.nextJSONStack();
		Iterator<PlateBigDecimal> iterStack = output.iterator();
		
    	for(PlateBigDecimal plate : stack) {
    		
    		PlateBigDecimal outputPlate = iterStack.next();
    		Iterator<WellBigDecimal> iterPlate = outputPlate.iterator();
    		
    		for(WellBigDecimal well : plate) {

    			WellBigDecimal outputWell = iterPlate.next();
    			
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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		
    	for(StackBigDecimal stack : stacks) {
    		
    		StackBigDecimal output = reader.nextJSONStack();
    		Iterator<PlateBigDecimal> iterStack = output.iterator();
    		
    		for(PlateBigDecimal plate : stack) {
    			
    			PlateBigDecimal outputPlate = iterStack.next();
        		Iterator<WellBigDecimal> iterPlate = outputPlate.iterator();
        		
        		for(WellBigDecimal well : plate) {

        			WellBigDecimal outputWell = iterPlate.next();
        			
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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.stackToJSON(stacks.toArray(new StackBigDecimal[stacks.size()]));
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		
    	for(StackBigDecimal stack : stacks) {
    		
    		StackBigDecimal output = reader.nextJSONStack();
    		Iterator<PlateBigDecimal> iterStack = output.iterator();
    		
    		for(PlateBigDecimal plate : stack) {
    			
    			PlateBigDecimal outputPlate = iterStack.next();
        		Iterator<WellBigDecimal> iterPlate = outputPlate.iterator();
        		
        		for(WellBigDecimal well : plate) {

        			WellBigDecimal outputWell = iterPlate.next();
        			
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

    	for(Map<WellBigDecimal, BigDecimal> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.resultToXML(map);
		    
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			WellSetBigDecimal output = reader.nextXMLResult();
			Iterator<WellBigDecimal> iter = output.iterator();
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {	
				
				WellBigDecimal next = iter.next();

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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.resultToXML(maps);
	    
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		
		for(Map<WellBigDecimal, BigDecimal> map : maps) {
		
			WellSetBigDecimal output = reader.nextXMLResult();
			Iterator<WellBigDecimal> iter = output.iterator();

			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {
			
				WellBigDecimal next = iter.next();

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

    	for(Map<WellBigDecimal, BigDecimal> map : maps) {
    		   		   
			File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.resultToXML(map, testLabel);
		    
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			WellSetBigDecimal output = reader.nextXMLResult();
			
			assertEquals(testLabel, output.label());
			
			Iterator<WellBigDecimal> iter = output.iterator();
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {	
				
				WellBigDecimal next = iter.next();

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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.resultToXML(maps, labelList);
	    
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		Iterator<String> labelIter = labelList.iterator();
		
		for(Map<WellBigDecimal, BigDecimal> map : maps) {
		
			WellSetBigDecimal output = reader.nextXMLResult();
			Iterator<WellBigDecimal> iter = output.iterator();

			assertEquals(labelIter.next(), output.label());
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : map.entrySet()) {
			
				WellBigDecimal next = iter.next();

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

    	for(PlateBigDecimal plate : array) {
    		
    		for(WellBigDecimal well : plate) {
			    
    			File wellFile = new File(wellPath);

				PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
    			wellWriter.wellToXML(well);
			    
    			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
    			WellBigDecimal output = reader.nextXMLWell();

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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			
    		for(WellBigDecimal well : plate) {
    		
    			WellBigDecimal output = reader.nextXMLWell();
    			
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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.wellToXML(plate.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			
    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal output = reader.nextXMLWell();
    			
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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.setToXML(plate.dataSet());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			WellSetBigDecimal output = reader.nextXMLSet();
			Iterator<WellBigDecimal> iter = output.iterator();
			
    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal outputWell = iter.next();
    			
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
    	
    	List<WellSetBigDecimal> collection = new ArrayList<WellSetBigDecimal>();
    	
    	for(PlateBigDecimal plate : array) {
    		collection.add(plate.dataSet());
    	}
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.setToXML(collection);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(WellSetBigDecimal set : collection) {
    		
    		WellSetBigDecimal output = reader.nextXMLSet();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : set) {
    			
    			WellBigDecimal outputWell = iter.next();
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
    	
    	WellSetBigDecimal[] sets = new WellSetBigDecimal[array.length];
    	
    	for(int i = 0; i < sets.length; i++) {
    		sets[i] = array[i].dataSet();
    	}
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.setToXML(sets);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(WellSetBigDecimal set : sets) {
    		
    		WellSetBigDecimal output = reader.nextXMLSet();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : set) {
    			
    			WellBigDecimal outputWell = iter.next();
    			
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

    	for(PlateBigDecimal plate : array) {
    		
    		File wellFile = new File(wellPath);

			PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
			wellWriter.plateToXML(plate);
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
			PlateBigDecimal output = reader.nextXMLPlate();
			Iterator<WellBigDecimal> iter = output.iterator();
			
    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal outputWell = iter.next();
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
    	
    	List<PlateBigDecimal> collection = Arrays.asList(array);
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.plateToXML(collection);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(PlateBigDecimal plate : collection) {
    		
    		PlateBigDecimal output = reader.nextXMLPlate();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal outputWell = iter.next();
    			
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
    	
		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.plateToXML(array);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);

    	for(PlateBigDecimal plate : array) {
    		
    		PlateBigDecimal output = reader.nextXMLPlate();
    		Iterator<WellBigDecimal> iter = output.iterator();

    		for(WellBigDecimal well : plate) {
    			
    			WellBigDecimal outputWell = iter.next();
    			
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
    	StackBigDecimal stack = new StackBigDecimal(rows, columns);
    	stack.add(array);
    	
    	File wellFile = new File(wellPath);

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.stackToXML(stack);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		StackBigDecimal output = reader.nextXMLStack();
		Iterator<PlateBigDecimal> iterStack = output.iterator();
		
    	for(PlateBigDecimal plate : stack) {
    		
    		PlateBigDecimal outputPlate = iterStack.next();
    		Iterator<WellBigDecimal> iterPlate = outputPlate.iterator();
    		
    		for(WellBigDecimal well : plate) {

    			WellBigDecimal outputWell = iterPlate.next();
    			
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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		
    	for(StackBigDecimal stack : stacks) {
    		
    		StackBigDecimal output = reader.nextXMLStack();
    		Iterator<PlateBigDecimal> iterStack = output.iterator();
    		
    		for(PlateBigDecimal plate : stack) {
    			
    			PlateBigDecimal outputPlate = iterStack.next();
        		Iterator<WellBigDecimal> iterPlate = outputPlate.iterator();
        		
        		for(WellBigDecimal well : plate) {

        			WellBigDecimal outputWell = iterPlate.next();
        			
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

		PlateWriterBigDecimal wellWriter = new PlateWriterBigDecimal(wellFile);
		wellWriter.stackToXML(stacks.toArray(new StackBigDecimal[stacks.size()]));
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(wellFile);
		
    	for(StackBigDecimal stack : stacks) {
    		
    		StackBigDecimal output = reader.nextXMLStack();
    		Iterator<PlateBigDecimal> iterStack = output.iterator();
    		
    		for(PlateBigDecimal plate : stack) {
    			
    			PlateBigDecimal outputPlate = iterStack.next();
        		Iterator<WellBigDecimal> iterPlate = outputPlate.iterator();
        		
        		for(WellBigDecimal well : plate) {

        			WellBigDecimal outputWell = iterPlate.next();
        			
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
