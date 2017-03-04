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
public class PlateReaderDoubleSetsTest {
	
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
	private static int stackNumber = 5;
	private static Random random = new Random();
	
	private static PlateDouble[] array = new PlateDouble[plateNumber];
	private static List<Map<WellDouble, Double>> maps = new ArrayList<Map<WellDouble, Double>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackDouble> stacks = new ArrayList<StackDouble>();
	
	private static String path = "test.txt";
	private static File file;
	
	
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
	
    /*-------------------- Methods for JSON Well Set Input -------------------*/
    
    /**
     * Tests the has next JSON set method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasNextJSONSet() throws IOException, JAXBException {    

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
	
			int index = 0;
			while(reader.hasNextJSONSet()) {
				reader.nextJSONSet();
				index++;
			}
	
			assertTrue(sets.size() == index);
			assertFalse(reader.hasNextJSONSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the has previous JSON set method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testHasPreviousJSONSet() throws IOException, JAXBException {    

    	for(StackDouble stack : stacks) {
    		
    		File writerFile = new File("setToJSON.txt");
    		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
    		
    		List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
    		
    		for(PlateDouble plate : stack) {
    			sets.add(plate.dataSet());
    		}
    		
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);

			assertFalse(reader.hasPreviousJSONSet());
			
			while(reader.hasNextJSONSet()) {
				reader.nextJSONSet();
			}
	
			int index = 0;
			while(reader.hasPreviousJSONSet()) {
				reader.previousJSONSet();
				index++;
			}
			
			assertTrue(sets.size() == index);
			assertFalse(reader.hasPreviousJSONSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next JSON set method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testNextJSONSet() throws IOException, JAXBException {
    	
    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.iterator();
			
			while(reader.hasNextJSONSet()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = reader.nextJSONSet();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}
    }
    
    /**
     * Tests the previous JSON set method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testPreviousJSONSet() throws IOException, JAXBException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.descendingIterator();
			
			while(reader.hasPreviousJSONSet()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = reader.previousJSONSet();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}
			
    }
    
    /**
     * Tests the remaingin JSON sets method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testRemainingJSONSets() throws IOException, JAXBException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.iterator();
			
			int index = random.nextInt(sets.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextJSONSet();
			}
			
			List<WellSetDouble> remaining = reader.remainingJSONSets();
			Iterator<WellSetDouble> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = remainingIter.next();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the spent JSON sets method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSpentJSONSets() throws IOException, JAXBException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.iterator();
			
			int index = random.nextInt(sets.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextJSONSet();
			}
				
			List<WellSetDouble> spent = reader.spentJSONSets();
			Collections.reverse(spent);
			Iterator<WellSetDouble> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = spentIter.next();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the all JSON sets method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testAllJSONSets() throws IOException, JAXBException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToJSON.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToJSON(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			List<WellSetDouble> all = reader.allJSONSets();
			
			Iterator<WellSetDouble> allIter = all.iterator();
			Iterator<PlateDouble> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = allIter.next();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /*-------------------- Methods for XML Well Set Input --------------------*/
    
    /**
     * Tests the has next XML set method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testHasNextXMLSet() throws IOException, JAXBException, ParserConfigurationException, TransformerException {    
    	
        for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
	
			int index = 0;
			while(reader.hasNextXMLSet()) {
				reader.nextXMLSet();
				index++;
			}
	
			assertTrue(sets.size() == index);
			assertFalse(reader.hasNextJSONSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}
		
    }
    
    /**
     * Tests the has previous XML set method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasPreviousXMLSet() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

    	for(StackDouble stack : stacks) {
    		
    		File writerFile = new File("setToXML.txt");
    		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
    		
    		List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
    		
    		for(PlateDouble plate : stack) {
    			sets.add(plate.dataSet());
    		}
    		
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);

			assertFalse(reader.hasPreviousXMLSet());
			
			while(reader.hasNextXMLSet()) {
				reader.nextXMLSet();
			}
	
			int index = 0;
			while(reader.hasPreviousXMLSet()) {
				reader.previousXMLSet();
				index++;
			}
			
			assertTrue(sets.size() == index);
			assertFalse(reader.hasPreviousXMLSet());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next XML set method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testNextXMLSet() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.iterator();
			
			while(reader.hasNextXMLSet()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = reader.nextXMLSet();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the previous XML set method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testPreviousXMLSet() throws IOException, TransformerException, ParserConfigurationException, JAXBException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.descendingIterator();
			
			while(reader.hasPreviousXMLSet()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = reader.previousXMLSet();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the remaingin XML sets method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testRemainingXMLSets() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.iterator();
			
			int index = random.nextInt(sets.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextXMLSet();
			}
			
			List<WellSetDouble> remaining = reader.remainingXMLSets();
			Iterator<WellSetDouble> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = remainingIter.next();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the spent XML sets method.
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testSpentXMLSets() throws IOException, TransformerException, ParserConfigurationException, JAXBException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			Iterator<PlateDouble> iter = stack.iterator();
			
			int index = random.nextInt(sets.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextXMLSet();
			}
				
			List<WellSetDouble> spent = reader.spentXMLSets();
			Collections.reverse(spent);
			Iterator<WellSetDouble> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = spentIter.next();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /**
     * Tests the all XML sets method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testAllXMLSets() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(StackDouble stack : stacks) {
    		
			File writerFile = new File("setToXML.txt");
			PlateWriterDouble writer = new PlateWriterDouble(writerFile);
	
			List<WellSetDouble> sets = new ArrayList<WellSetDouble>();
			
			for(PlateDouble plate : stack) {
				sets.add(plate.dataSet());
			}
			
	    	writer.setToXML(sets);
			
			PlateReaderDouble reader = new PlateReaderDouble(writerFile);
			List<WellSetDouble> all = reader.allXMLSets();
			
			Iterator<WellSetDouble> allIter = all.iterator();
			Iterator<PlateDouble> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				WellSetDouble inputSet = iter.next().dataSet();
				WellSetDouble outputSet = allIter.next();
				
				Iterator<WellDouble> inputSetIter = inputSet.iterator();
				Iterator<WellDouble> outputSetIter = outputSet.iterator();
				
				while(outputSetIter.hasNext()) {
					
					WellDouble inputWell = inputSetIter.next();
					WellDouble outputWell = outputSetIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }

}
