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

/*------------------------------- Dependencies -------------------------------*/

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

import com.github.jessemull.microflex.bigdecimalflex.io.PlateReaderBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.io.PlateWriterBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.StackBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.stat.MeanBigDecimal;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * Tests the plate reader big decimal test.
 * 
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateReaderBigDecimalStacksTest {
	
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
	private static int stackNumber = 5;
	private static Random random = new Random();
	
	private static PlateBigDecimal[] array = new PlateBigDecimal[plateNumber];
	private static List<Map<WellBigDecimal, BigDecimal>> maps = new ArrayList<Map<WellBigDecimal, BigDecimal>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigDecimal> stacks = new ArrayList<StackBigDecimal>();
	
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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		while(reader.hasNextJSONStack()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = reader.nextJSONStack();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		ListIterator<StackBigDecimal> inputStackIter = stacks.listIterator(stacks.size());
		
		while(reader.hasNextJSONStack()) {
			reader.nextJSONStack();
		}
		
		while(reader.hasPreviousJSONStack()) {
			
			StackBigDecimal inputStack = inputStackIter.previous();
			StackBigDecimal outputStack = reader.previousJSONStack();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size());
		
		for(int i = 0; i < index; i++) {
			inputStackIter.next();
			reader.nextJSONStack();
		}
		
		List<StackBigDecimal> remaining = reader.remainingJSONStacks();
		Iterator<StackBigDecimal> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = remainingIter.next();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size()) + 1;
		
		for(int i = 0; i < index; i++) {
			reader.nextJSONStack();
		}
		
		List<StackBigDecimal> spent = reader.spentJSONStacks();
		Collections.reverse(spent);
		Iterator<StackBigDecimal> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = spentIter.next();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToJSON(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		List<StackBigDecimal> all = reader.allJSONStacks();
		
		Iterator<StackBigDecimal> allIter = all.iterator();
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		while(allIter.hasNext()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = allIter.next();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		while(reader.hasNextXMLStack()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = reader.nextXMLStack();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		ListIterator<StackBigDecimal> inputStackIter = stacks.listIterator(stacks.size());
		
		while(reader.hasNextXMLStack()) {
			reader.nextXMLStack();
		}
		
		while(reader.hasPreviousXMLStack()) {
			
			StackBigDecimal inputStack = inputStackIter.previous();
			StackBigDecimal outputStack = reader.previousXMLStack();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size());
		
		for(int i = 0; i < index; i++) {
			inputStackIter.next();
			reader.nextXMLStack();
		}
		
		List<StackBigDecimal> remaining = reader.remainingXMLStacks();
		Iterator<StackBigDecimal> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = remainingIter.next();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		int index = random.nextInt(stacks.size()) + 1;
		
		for(int i = 0; i < index; i++) {
			reader.nextXMLStack();
		}
		
		List<StackBigDecimal> spent = reader.spentXMLStacks();
		Collections.reverse(spent);
		Iterator<StackBigDecimal> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = spentIter.next();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
		
    	writer.stackToXML(stacks);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
		List<StackBigDecimal> all = reader.allXMLStacks();
		
		Iterator<StackBigDecimal> allIter = all.iterator();
		Iterator<StackBigDecimal> inputStackIter = stacks.iterator();
		
		while(allIter.hasNext()) {
			
			StackBigDecimal inputStack = inputStackIter.next();
			StackBigDecimal outputStack = allIter.next();
			
			Iterator<PlateBigDecimal> inputPlateIter = inputStack.iterator();
			Iterator<PlateBigDecimal> outputPlateIter = outputStack.iterator();
			
			while(outputPlateIter.hasNext()) {
				
				PlateBigDecimal inputPlate = inputPlateIter.next();
				PlateBigDecimal outputPlate = outputPlateIter.next();
				
				Iterator<WellBigDecimal> inputWellIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputWellIter = outputPlate.iterator();
				
				while(outputWellIter.hasNext()) {
					
					WellBigDecimal inputWell = inputWellIter.next();
					WellBigDecimal outputWell = outputWellIter.next();

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
