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

import com.github.jessemull.microflex.doubleflex.io.PlateReaderDouble;
import com.github.jessemull.microflex.doubleflex.io.PlateWriterDouble;
import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.StackDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.doubleflex.stat.MeanDouble;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * Tests the plate reader double test.
 * 
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateReaderDoubleResultsTest {
	
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
	private static Random random = new Random();
	
	private static PlateDouble[] array = new PlateDouble[plateNumber];
	private static List<Map<WellDouble, Double>> maps = new ArrayList<Map<WellDouble, Double>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackDouble> stacks = new ArrayList<StackDouble>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterDouble plateWriter;
	
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
    
    /*--------------------- Methods for Plate Map Input ----------------------*/
    
    /**
     * Tests has next map the method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testHasNextMap() throws IOException, JAXBException {

    	plateWriter.resultToPlateMap(maps, type);
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		while(reader.hasNextMap()) {
			
			PlateDouble input = new PlateDouble(type);
			PlateDouble output = reader.nextMap();
			
			for(Map.Entry<WellDouble, Double> entry : iter.next().entrySet()) {
				
				WellDouble next = entry.getKey();
				WellDouble well = new WellDouble(next.row(), next.column());
				
				well.add(entry.getValue());
				input.addWells(well);
				
			}
			
			Iterator<WellDouble> inputIter = input.iterator();
			Iterator<WellDouble> outputIter = output.iterator();
			
			for(int i = 0; i < maps.size(); i++) {
				
				WellDouble inputWell = inputIter.next();
				WellDouble outputWell = outputIter.next();
				
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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		while(reader.hasNextTable()) {
			
			WellSetDouble input = new WellSetDouble();
			WellSetDouble output = reader.nextTable();
			
			for(Map.Entry<WellDouble, Double> entry : iter.next().entrySet()) {
				
				WellDouble next = entry.getKey();
				WellDouble well = new WellDouble(next.row(), next.column());
				
				well.add(entry.getValue());
				input.add(well);
				
			}
			
			Iterator<WellDouble> inputIter = input.iterator();
			Iterator<WellDouble> outputIter = output.iterator();
			
			for(int i = 0; i < maps.size(); i++) {
				
				WellDouble inputWell = inputIter.next();
				WellDouble outputWell = outputIter.next();
				
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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		while(reader.hasNextJSONResult()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = reader.nextJSONResult();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		ListIterator<Map<WellDouble, Double>> iter = maps.listIterator(maps.size());
		
		while(reader.hasNextJSONResult()) {
			reader.nextJSONResult();
		}
		
		while(reader.hasPreviousJSONResult()) {
			
			Map<WellDouble, Double> input = iter.previous();
			WellSetDouble output = reader.previousJSONResult();

			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size());

		for(int i = 0; i < index; i++) {
			iter.next();
			reader.nextJSONResult();
		}
		
		List<WellSetDouble> remaining = reader.remainingJSONResults();
		Iterator<WellSetDouble> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = remainingIter.next();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size()) + 1;

		for(int i = 0; i < index; i++) {
			reader.nextJSONResult();
		}
		
		List<WellSetDouble> spent = reader.spentJSONResults();
		Collections.reverse(spent);
		Iterator<WellSetDouble> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = spentIter.next();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		List<WellSetDouble> all = reader.allJSONResults();
		
		Iterator<WellSetDouble> allIter = all.iterator();
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		while(allIter.hasNext()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = allIter.next();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		while(reader.hasNextXMLResult()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = reader.nextXMLResult();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		ListIterator<Map<WellDouble, Double>> iter = maps.listIterator(maps.size());
		
		while(reader.hasNextXMLResult()) {
			reader.nextXMLResult();
		}
		
		while(reader.hasPreviousXMLResult()) {
			
			Map<WellDouble, Double> input = iter.previous();
			WellSetDouble output = reader.previousXMLResult();

			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size());

		for(int i = 0; i < index; i++) {
			iter.next();
			reader.nextXMLResult();
		}
		
		List<WellSetDouble> remaining = reader.remainingXMLResults();
		Iterator<WellSetDouble> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = remainingIter.next();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size()) + 1;

		for(int i = 0; i < index; i++) {
			reader.nextXMLResult();
		}
		
		List<WellSetDouble> spent = reader.spentXMLResults();
		Collections.reverse(spent);
		Iterator<WellSetDouble> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = spentIter.next();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

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
		
		PlateReaderDouble reader = new PlateReaderDouble(file);
		List<WellSetDouble> all = reader.allXMLResults();
		
		Iterator<WellSetDouble> allIter = all.iterator();
		Iterator<Map<WellDouble, Double>> iter = maps.iterator();
		
		while(allIter.hasNext()) {
			
			Map<WellDouble, Double> input = iter.next();
			WellSetDouble output = allIter.next();
			
			Iterator<Map.Entry<WellDouble, Double>> iterInput = input.entrySet().iterator();
			Iterator<WellDouble> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellDouble, Double> entry = iterInput.next();
				WellDouble outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertTrue(entry.getValue() == outputWell.get(0));
			}
		}

		reader.close();
		
    }

}
