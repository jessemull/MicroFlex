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
public class PlateReaderDoubleStacksTest {
	
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
    
    /*------------------ Methods for JSON Plate Stack Input ------------------*/
    
    /**
     * Tests the has next JSON stack method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasNextJSONStack() throws IOException, JAXBException {    
    	
    	File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);

		int index = 0;
		while(reader.hasNextJSONStack()) {
			reader.nextJSONStack();
			index++;
		}

		assertTrue(stacks.size() == index);
		assertFalse(reader.hasNextJSONStack());
		
		reader.close();
		writer.close();
		writerFile.delete();

    }
    
    /**
     * Tests the has previous JSON stack method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasPreviousJSONStack() throws IOException, JAXBException {    
 		
		File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);

    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);

		assertFalse(reader.hasPreviousJSONStack());
		
		while(reader.hasNextJSONStack()) {
			reader.nextJSONStack();
		}

		int index = 0;
		while(reader.hasPreviousJSONStack()) {
			reader.previousJSONStack();
			index++;
		}

		assertTrue(stacks.size() == index);
		assertFalse(reader.hasPreviousJSONStack());
		
		reader.close();
		writer.close();
		writerFile.delete();
    }
    
    /**
     * Tests the next JSON stack method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testNextJSONStack() throws IOException, JAXBException {

    	File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		while(reader.hasNextJSONStack()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = reader.nextJSONStack();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the previous JSON stack method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testPreviousJSONStack() throws IOException, JAXBException {

    	File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		ListIterator<StackDouble> inputStackIter = stacks.listIterator(stacks.size());
		
		while(reader.hasNextJSONStack()) {
			reader.nextJSONStack();
		}
		
		while(reader.hasPreviousJSONStack()) {
			
			StackDouble inputStack = inputStackIter.previous();
			StackDouble outputStack = reader.previousJSONStack();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the remaining JSON stacks method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testRemainingJSONStacks() throws IOException, JAXBException {

    	File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size());
		
		for(int i = 0; i < index; i++) {
			inputStackIter.next();
			reader.nextJSONStack();
		}
		
		List<StackDouble> remaining = reader.remainingJSONStacks();
		Iterator<StackDouble> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = remainingIter.next();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the spent JSON stacks method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSpentJSONStacks() throws IOException, JAXBException {

    	File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size()) + 1;
		
		for(int i = 0; i < index; i++) {
			reader.nextJSONStack();
		}
		
		List<StackDouble> spent = reader.spentJSONStacks();
		Collections.reverse(spent);
		Iterator<StackDouble> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = spentIter.next();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the all JSON stacks method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testAllJSONStacks() throws IOException, JAXBException {

    	File writerFile = new File("stackToJSON.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		List<StackDouble> all = reader.allJSONStacks();
		
		Iterator<StackDouble> allIter = all.iterator();
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		while(allIter.hasNext()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = allIter.next();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /*------------------ Methods for XML Plate Stack Input -------------------*/
    
    /**
     * Tests the has next XML stack method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasNextXMLStack() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

		File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);

		int index = 0;
		while(reader.hasNextXMLStack()) {
			reader.nextXMLStack();
			index++;
		}

		assertTrue(stacks.size() == index);
		assertFalse(reader.hasNextXMLStack());
		
		reader.close();
		writer.close();
		writerFile.delete();

    }
    
    /**
     * Tests the has previous XML stack method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testHasPreviousXMLStack() throws IOException, JAXBException, ParserConfigurationException, TransformerException {    

    	File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);

    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);

		assertFalse(reader.hasPreviousXMLStack());
		
		while(reader.hasNextXMLStack()) {
			reader.nextXMLStack();
		}

		int index = 0;
		while(reader.hasPreviousXMLStack()) {
			reader.previousXMLStack();
			index++;
		}

		assertTrue(stacks.size() == index);
		assertFalse(reader.hasPreviousXMLStack());
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the next XML stack method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testNextXMLStack() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		while(reader.hasNextXMLStack()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = reader.nextXMLStack();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the previous XML stack method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testPreviousXMLStack() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		ListIterator<StackDouble> inputStackIter = stacks.listIterator(stacks.size());
		
		while(reader.hasNextXMLStack()) {
			reader.nextXMLStack();
		}
		
		while(reader.hasPreviousXMLStack()) {
			
			StackDouble inputStack = inputStackIter.previous();
			StackDouble outputStack = reader.previousXMLStack();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the remaining XML stacks method.
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testRemainingXMLStacks() throws IOException, ParserConfigurationException, TransformerException, JAXBException {

    	File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size());
		
		for(int i = 0; i < index; i++) {
			inputStackIter.next();
			reader.nextXMLStack();
		}
		
		List<StackDouble> remaining = reader.remainingXMLStacks();
		Iterator<StackDouble> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = remainingIter.next();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the spent XML stacks method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testSpentXMLStacks() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size()) + 1;
		
		for(int i = 0; i < index; i++) {
			reader.nextXMLStack();
		}
		
		List<StackDouble> spent = reader.spentXMLStacks();
		Collections.reverse(spent);
		Iterator<StackDouble> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = spentIter.next();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }
    
    /**
     * Tests the all XML stacks method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws TransformerException 
     * @throws ParserConfigurationException 
     */
    @Test
    public void testAllXMLStacks() throws IOException, JAXBException, ParserConfigurationException, TransformerException {

    	File writerFile = new File("stackToXML.txt");
		PlateWriterDouble writer = new PlateWriterDouble(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderDouble reader = new PlateReaderDouble(writerFile);
		List<StackDouble> all = reader.allXMLStacks();
		
		Iterator<StackDouble> allIter = all.iterator();
		Iterator<StackDouble> inputStackIter = stacks.iterator();
		
		while(allIter.hasNext()) {
			
			StackDouble inputStack = inputStackIter.next();
			StackDouble outputStack = allIter.next();
			
			Iterator<PlateDouble> inputPlateIter = inputStack.iterator();
			Iterator<PlateDouble> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateDouble inputPlate = inputPlateIter.next();
				PlateDouble outputPlate = outputPlateIter.next();
				
				Iterator<WellDouble> inputWellIter = inputPlate.iterator();
				Iterator<WellDouble> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellDouble inputWell = inputWellIter.next();
					WellDouble outputWell = outputWellIter.next();

				    assertEquals(inputWell, outputWell);
				    assertEquals(inputWell.data(), outputWell.data());
				}
				
			}
			
		}
		
		reader.close();
		writer.close();
		writerFile.delete();
		
    }

}
