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

/*------------------------------- Dependencies -------------------------------*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

/**
 * Tests the plate reader int test.
 * 
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateReaderIntegerResultsTest {
	
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
	private static Random random = new Random();
	
	private static PlateInteger[] array = new PlateInteger[plateNumber];
	private static List<Map<WellInteger, Integer>> maps = new ArrayList<Map<WellInteger, Integer>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackInteger> stacks = new ArrayList<StackInteger>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterInteger plateWriter;
	
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
    
    /*--------------------- Methods for Plate Map Input ----------------------*/
    
    /**
     * Tests has next map the method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testHasNextMap() throws IOException, JAXBException {

    	plateWriter.resultToPlateMap(maps, type);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextMap()) {
			reader.nextMap();
			index++;
		}

		assertTrue(maps.size() == index);
		assertFalse(reader.hasNextMap());
		
		reader.close();
    }
    
    /**
     * Tests next map the method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testNextMap() throws IOException, JAXBException {

    	plateWriter.resultToPlateMap(maps, type);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		while(reader.hasNextMap()) {
			
			PlateInteger input = new PlateInteger(type);
			PlateInteger output = reader.nextMap();
			
			for(Map.Entry<WellInteger, Integer> entry : iter.next().entrySet()) {
				
				WellInteger next = entry.getKey();
				WellInteger well = new WellInteger(next.row(), next.column());
				
				well.add(entry.getValue());
				input.addWells(well);
				
			}
			
			Iterator<WellInteger> inputIter = input.iterator();
			Iterator<WellInteger> outputIter = output.iterator();
			
			for(int i = 0; i < maps.size(); i++) {
				
				WellInteger inputWell = inputIter.next();
				WellInteger outputWell = outputIter.next();
				
				assertEquals(inputWell, outputWell);
				assertEquals(inputWell.data(), outputWell.data());
			}

		}

		assertNull(reader.nextMap());
		reader.close();
    }

    /*----------------------- Methods for Table Input ------------------------*/

    /**
     * Tests has next table the method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testHasNextTable() throws IOException, JAXBException {

    	plateWriter.resultToTable(maps, labelList);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextTable()) {
			reader.nextTable();
			index++;
		}

		assertTrue(maps.size() == index);
		assertFalse(reader.hasNextTable());
		reader.close();
    }
    
    /**
     * Tests the next table method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testNextTable() throws IOException, JAXBException {
    	
    	plateWriter.resultToTable(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		while(reader.hasNextTable()) {
			
			WellSetInteger input = new WellSetInteger();
			WellSetInteger output = reader.nextTable();
			
			for(Map.Entry<WellInteger, Integer> entry : iter.next().entrySet()) {
				
				WellInteger next = entry.getKey();
				WellInteger well = new WellInteger(next.row(), next.column());
				
				well.add(entry.getValue());
				input.add(well);
				
			}
			
			Iterator<WellInteger> inputIter = input.iterator();
			Iterator<WellInteger> outputIter = output.iterator();
			
			for(int i = 0; i < maps.size(); i++) {
				
				WellInteger inputWell = inputIter.next();
				WellInteger outputWell = outputIter.next();
				
				assertEquals(inputWell, outputWell);
				assertEquals(inputWell.data(), outputWell.data());
			}

		}

		assertFalse(reader.hasNextTable());
		reader.close();
    }
    
    /*---------------------- Methods for JSON Result Input ---------------------*/
    
    /**
     * Tests the has next JSON result method.
     * @throws IOException 
     * @throws JAXBException 
     */
	@Test
    public void testHasNextJSONResult() throws IOException, JAXBException {    

    	plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextJSONResult()) {
			reader.nextJSONResult();
			index++;
		}

		assertTrue(maps.size() == index);
		assertFalse(reader.hasNextJSONResult());
		reader.close();
		
    }
    
    /**
     * Tests the has previous JSON result method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testHasPreviousJSONResult() throws IOException, JAXBException {    
    	
    	plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		assertFalse(reader.hasPreviousJSONResult());
		
		while(reader.hasNextJSONResult()) {
			reader.nextJSONResult();
		}

		int index = 0;		
		while(reader.hasPreviousJSONResult()) {
			reader.previousJSONResult();
			index++;
		}
		
		assertTrue(maps.size() == index);
		reader.close();
    }
    
    /**
     * Tests the next JSON result method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testNextJSONResult() throws IOException, JAXBException {
    	
    	plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		while(reader.hasNextJSONResult()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = reader.nextJSONResult();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
    }
    
    /**
     * Tests the previous JSON result method.
     * @throws IOException 
     * @throws JAXBException 
     */
	@Test
    public void testPreviousJSONResult() throws IOException, JAXBException {

    	plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		ListIterator<Map<WellInteger, Integer>> iter = maps.listIterator(maps.size());
		
		while(reader.hasNextJSONResult()) {
			reader.nextJSONResult();
		}
		
		while(reader.hasPreviousJSONResult()) {
			
			Map<WellInteger, Integer> input = iter.previous();
			WellSetInteger output = reader.previousJSONResult();

			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }
    
    /**
     * Tests the remaining JSON results method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testRemainingJSONResults() throws IOException, JAXBException {

    	plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size());

		for(int i = 0; i < index; i++) {
			iter.next();
			reader.nextJSONResult();
		}
		
		List<WellSetInteger> remaining = reader.remainingJSONResults();
		Iterator<WellSetInteger> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = remainingIter.next();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }
    
    /**
     * Tests the spent JSON results method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testSpentJSONResults() throws IOException, JAXBException {

        plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size()) + 1;

		for(int i = 0; i < index; i++) {
			reader.nextJSONResult();
		}
		
		List<WellSetInteger> spent = reader.spentJSONResults();
		Collections.reverse(spent);
		Iterator<WellSetInteger> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = spentIter.next();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }
    
    /**
     * Tests the all JSON results method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
	@Test
    public void testAllJSONResults() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	plateWriter.resultToJSON(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		List<WellSetInteger> all = reader.allJSONResults();
		
		Iterator<WellSetInteger> allIter = all.iterator();
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		while(allIter.hasNext()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = allIter.next();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }
    
    /*-------------------- Methods for XML Result Input --------------------*/
    
    /**
     * Tests the has next XML result method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testHasNextXMLResult() throws IOException, TransformerException, ParserConfigurationException, JAXBException {    

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		int index = 0;
		while(reader.hasNextXMLResult()) {
			reader.nextXMLResult();
			index++;
		}

		assertTrue(maps.size() == index);
		assertFalse(reader.hasNextXMLResult());
		reader.close();
		
    }
    
    /**
     * Tests the has previous XML result method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasPreviousXMLResult() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);

		assertFalse(reader.hasPreviousXMLResult());
		
		while(reader.hasNextXMLResult()) {
			reader.nextXMLResult();
		}

		int index = 0;		
		while(reader.hasPreviousXMLResult()) {
			reader.previousXMLResult();
			index++;
		}
		
		assertTrue(maps.size() == index);
		reader.close();
    }
    
    /**
     * Tests the next XML result method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testNextXMLResult() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		while(reader.hasNextXMLResult()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = reader.nextXMLResult();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
    }
    
    /**
     * Tests the previous XML result method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testPreviousXMLResult() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		ListIterator<Map<WellInteger, Integer>> iter = maps.listIterator(maps.size());
		
		while(reader.hasNextXMLResult()) {
			reader.nextXMLResult();
		}
		
		while(reader.hasPreviousXMLResult()) {
			
			Map<WellInteger, Integer> input = iter.previous();
			WellSetInteger output = reader.previousXMLResult();

			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }
    
    /**
     * Tests the remaining XML results method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testRemainingXMLResults() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size());

		for(int i = 0; i < index; i++) {
			iter.next();
			reader.nextXMLResult();
		}
		
		List<WellSetInteger> remaining = reader.remainingXMLResults();
		Iterator<WellSetInteger> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = remainingIter.next();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }
    
    /**
     * Tests the spent XML results method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testSpentXMLResults() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size()) + 1;

		for(int i = 0; i < index; i++) {
			reader.nextXMLResult();
		}
		
		List<WellSetInteger> spent = reader.spentXMLResults();
		Collections.reverse(spent);
		Iterator<WellSetInteger> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = spentIter.next();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
    }
    
    /**
     * Tests the all XML results method.
     * @throws IOException 
     * @throws JAXBException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testAllXMLResults() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	plateWriter.resultToXML(maps);
		
		PlateReaderInteger reader = new PlateReaderInteger(file);
		List<WellSetInteger> all = reader.allXMLResults();
		
		Iterator<WellSetInteger> allIter = all.iterator();
		Iterator<Map<WellInteger, Integer>> iter = maps.iterator();
		
		while(allIter.hasNext()) {
			
			Map<WellInteger, Integer> input = iter.next();
			WellSetInteger output = allIter.next();
			
			Iterator<Map.Entry<WellInteger, Integer>> iterInput = input.entrySet().iterator();
			Iterator<WellInteger> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellInteger, Integer> entry = iterInput.next();
				WellInteger outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }

}
