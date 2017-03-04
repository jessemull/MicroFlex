package com.github.jessemull.microflex.io.iobigdecimal;

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

/*------------------------------- Dependencies -------------------------------*/
import static org.junit.Assert.*;

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

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.stat.MeanBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.StackBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.util.RandomUtil;

import com.github.jessemull.microflex.bigdecimalflex.io.PlateReaderBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.io.PlateWriterBigDecimal;

/**
 * Tests the plate reader big decimal test.
 *  
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateReaderBigDecimalPlatesTest {
	
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
	
    /*---------------------- Methods for JSON Plate Input --------------------*/
    
    /**
     * Tests the has next JSON plate method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasNextJSONPlate() throws IOException, JAXBException {    

    	for(StackBigDecimal stack : stacks) {
    		
	    	File writerFile = new File("plateToJSON.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
			
	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
	
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

    	for(StackBigDecimal stack : stacks) {
    		
    		File writerFile = new File("plateToJSON.txt");
    		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.iterator();
			
			while(reader.hasNextJSONPlate()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = reader.nextJSONPlate();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.descendingIterator();
			
			while(reader.hasNextJSONPlate()) {
				reader.nextJSONPlate();
			}
			
			while(reader.hasPreviousJSONPlate()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = reader.previousJSONPlate();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.iterator();
		
			int index = random.nextInt(stack.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextJSONPlate();
			}
			
			List<PlateBigDecimal> remaining = reader.remainingJSONPlates();
			Iterator<PlateBigDecimal> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = remainingIter.next();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.iterator();
		
			int index = random.nextInt(stack.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextJSONPlate();
			}
			
			List<PlateBigDecimal> spent = reader.spentJSONPlates();
			Collections.reverse(spent);
			Iterator<PlateBigDecimal> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = spentIter.next();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			List<PlateBigDecimal> all = reader.allJSONPlates();
			
			Iterator<PlateBigDecimal> allIter = all.iterator();
			Iterator<PlateBigDecimal> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = allIter.next();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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
	
    	for(StackBigDecimal stack : stacks) {
    		
	    	File writerFile = new File("plateToXML.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);
			
	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
	
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

    	for(StackBigDecimal stack : stacks) {
    		
    		File writerFile = new File("plateToXML.txt");
    		PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.iterator();
			
			while(reader.hasNextXMLPlate()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = reader.nextXMLPlate();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.descendingIterator();
			
			while(reader.hasNextXMLPlate()) {
				reader.nextXMLPlate();
			}
			
			while(reader.hasPreviousXMLPlate()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = reader.previousXMLPlate();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.iterator();
		
			int index = random.nextInt(stack.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextXMLPlate();
			}
			
			List<PlateBigDecimal> remaining = reader.remainingXMLPlates();
			Iterator<PlateBigDecimal> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = remainingIter.next();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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

    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			Iterator<PlateBigDecimal> iter = stack.iterator();
		
			int index = random.nextInt(stack.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextXMLPlate();
			}
			
			List<PlateBigDecimal> spent = reader.spentXMLPlates();
			Collections.reverse(spent);
			Iterator<PlateBigDecimal> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = spentIter.next();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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
   
    	for(StackBigDecimal stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigDecimal writer = new PlateWriterBigDecimal(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigDecimal reader = new PlateReaderBigDecimal(writerFile);
			List<PlateBigDecimal> all = reader.allXMLPlates();
			
			Iterator<PlateBigDecimal> allIter = all.iterator();
			Iterator<PlateBigDecimal> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				PlateBigDecimal inputPlate = iter.next();
				PlateBigDecimal outputPlate = allIter.next();
				
				Iterator<WellBigDecimal> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigDecimal> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigDecimal inputWell = inputPlateIter.next();
					WellBigDecimal outputWell = outputPlateIter.next();

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
