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

import com.github.jessemull.microflex.integerflex.io.PlateReaderInteger;
import com.github.jessemull.microflex.integerflex.io.PlateWriterInteger;
import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
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
public class PlateReaderIntegerPlatesTest {
	
    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static int minValue = 0;      // Minimum int value for wells
	private static int maxValue = 100;    // Maximum int value for well
	
	/* The addition operation */
	
	private static MeanInteger mean = new MeanInteger();

	/* Random objects and numbers for testing */

	private static int rows = PlateInteger.ROWS_96WELL;
	private static int columns = PlateInteger.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int stackNumber = 5;
	private static Random random = new Random();
	
	private static PlateInteger[] array = new PlateInteger[plateNumber];
	private static List<Map<WellInteger, Integer>> maps = new ArrayList<Map<WellInteger, Integer>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackInteger> stacks = new ArrayList<StackInteger>();
	
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
	
    /*---------------------- Methods for JSON Plate Input --------------------*/
    
    /**
     * Tests the has next JSON plate method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasNextJSONPlate() throws IOException, JAXBException {    

    	for(StackInteger stack : stacks) {
    		
	    	File writerFile = new File("plateToJSON.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);
			
	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
	
			int index = 0;
			while(reader.hasNextJSONPlate()) {
				reader.nextJSONPlate();
				index++;
			}
	
			assertTrue(stack.size() == index);
			assertFalse(reader.hasNextJSONPlate());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the has previous JSON plate method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasPreviousJSONPlate() throws IOException, JAXBException {    

    	for(StackInteger stack : stacks) {
    		
    		File writerFile = new File("plateToJSON.txt");
    		PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);

			assertFalse(reader.hasPreviousJSONPlate());
			
			while(reader.hasNextJSONPlate()) {
				reader.nextJSONPlate();
			}
	
			int index = 0;
			while(reader.hasPreviousJSONPlate()) {
				reader.previousJSONPlate();
				index++;
			}

			assertTrue(stack.size() == index);
			assertFalse(reader.hasPreviousJSONPlate());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next JSON plate method.
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testNextJSONPlate() throws IOException, ParserConfigurationException, TransformerException, JAXBException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.iterator();
			
			while(reader.hasNextJSONPlate()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = reader.nextJSONPlate();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the previous JSON plate method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testPreviousJSONPlate() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.descendingIterator();
			
			while(reader.hasNextJSONPlate()) {
				reader.nextJSONPlate();
			}
			
			while(reader.hasPreviousJSONPlate()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = reader.previousJSONPlate();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the remaining JSON plates method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testRemainingJSONPlates() throws IOException, JAXBException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextJSONPlate();
			}
			
			List<PlateInteger> remaining = reader.remainingJSONPlates();
			Iterator<PlateInteger> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = remainingIter.next();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the spent JSON plates method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSpentJSONPlates() throws IOException, JAXBException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextJSONPlate();
			}
			
			List<PlateInteger> spent = reader.spentJSONPlates();
			Collections.reverse(spent);
			Iterator<PlateInteger> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = spentIter.next();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the all JSON plates method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testAllJSONPlates() throws IOException, JAXBException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			List<PlateInteger> all = reader.allJSONPlates();
			
			Iterator<PlateInteger> allIter = all.iterator();
			Iterator<PlateInteger> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = allIter.next();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

					assertEquals(inputWell, outputWell);
					assertEquals(inputWell.data(), outputWell.data());
					
				}
				
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
			
    	}

    }
    
    /*---------------------- Methods for XML Plate Input ---------------------*/
    
    /**
     * Tests the has next XML plate method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasNextXMLPlate() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    
	
    	for(StackInteger stack : stacks) {
    		
	    	File writerFile = new File("plateToXML.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);
			
	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
	
			int index = 0;
			while(reader.hasNextXMLPlate()) {
				reader.nextXMLPlate();
				index++;
			}
	
			assertTrue(stack.size() == index);
			assertFalse(reader.hasNextXMLPlate());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the has previous XML plate method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testHasPreviousXMLPlate() throws IOException, JAXBException, ParserConfigurationException, TransformerException {    

    	for(StackInteger stack : stacks) {
    		
    		File writerFile = new File("plateToXML.txt");
    		PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);

			assertFalse(reader.hasPreviousXMLPlate());
			
			while(reader.hasNextXMLPlate()) {
				reader.nextXMLPlate();
			}
	
			int index = 0;
			while(reader.hasPreviousXMLPlate()) {
				reader.previousXMLPlate();
				index++;
			}

			assertTrue(stack.size() == index);
			assertFalse(reader.hasPreviousXMLPlate());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next XML plate method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testNextXMLPlate() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.iterator();
			
			while(reader.hasNextXMLPlate()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = reader.nextXMLPlate();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the previous XML plate method.
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testPreviousXMLPlate() throws IOException, ParserConfigurationException, TransformerException, JAXBException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.descendingIterator();
			
			while(reader.hasNextXMLPlate()) {
				reader.nextXMLPlate();
			}
			
			while(reader.hasPreviousXMLPlate()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = reader.previousXMLPlate();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the remaining XML plates method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testRemainingXMLPlates() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextXMLPlate();
			}
			
			List<PlateInteger> remaining = reader.remainingXMLPlates();
			Iterator<PlateInteger> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = remainingIter.next();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the spent XML plates method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testSpentXMLPlates() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			Iterator<PlateInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextXMLPlate();
			}
			
			List<PlateInteger> spent = reader.spentXMLPlates();
			Collections.reverse(spent);
			Iterator<PlateInteger> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = spentIter.next();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
     * Tests the all XML plates method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testAllXMLPlates() throws IOException, JAXBException, ParserConfigurationException, TransformerException {
   
    	for(StackInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterInteger writer = new PlateWriterInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderInteger reader = new PlateReaderInteger(writerFile);
			List<PlateInteger> all = reader.allXMLPlates();
			
			Iterator<PlateInteger> allIter = all.iterator();
			Iterator<PlateInteger> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				PlateInteger inputPlate = iter.next();
				PlateInteger outputPlate = allIter.next();
				
				Iterator<WellInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellInteger inputWell = inputPlateIter.next();
					WellInteger outputWell = outputPlateIter.next();

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
