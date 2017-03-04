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

package com.github.jessemull.microflex.io.iobiginteger;

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
import java.math.BigInteger;
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

import com.github.jessemull.microflex.bigintegerflex.io.PlateReaderBigInteger;
import com.github.jessemull.microflex.bigintegerflex.io.PlateWriterBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.stat.MeanBigInteger;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * Tests the plate reader big integer test.
 * 
 * @author Jesse L. Mull
 * @update Updated Jan 17, 2017
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateReaderBigIntegerPlatesTest {
	
    /* ---------------------------- Local Fields -----------------------------*/
	
    /* Minimum and maximum values for random well and lists */
	
	private static BigInteger minValue = new BigInteger(0 + "");                    // Minimum big integer value for wells
	private static BigInteger maxValue = new BigInteger(100 + "");                  // Maximum big integer value for wells
	private static MathContext mc = new MathContext(10, RoundingMode.HALF_DOWN);    // Math context for input values
	
	/* The addition operation */
	
	private static MeanBigInteger mean = new MeanBigInteger();

	/* Random objects and numbers for testing */

	private static int rows = PlateBigInteger.ROWS_96WELL;
	private static int columns = PlateBigInteger.COLUMNS_96WELL;
	private static int length = 24;
	private static int plateNumber = 10;
	private static int stackNumber = 5;
	private static Random random = new Random();
	
	private static PlateBigInteger[] array = new PlateBigInteger[plateNumber];
	private static List<Map<WellBigInteger, BigInteger>> maps = new ArrayList<Map<WellBigInteger, BigInteger>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigInteger> stacks = new ArrayList<StackBigInteger>();
	
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
			
			PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
					rows, columns, minValue, maxValue, length, label);
			
			labelList.add(label);
			array[j] = plate;

			Map<WellBigInteger, BigDecimal> toAddBigDecimal = mean.plate(plate, mc);
			TreeMap<WellBigInteger, BigInteger> toAddBigInteger = new TreeMap<WellBigInteger, BigInteger>();
			
			for(Map.Entry<WellBigInteger, BigDecimal> entry : toAddBigDecimal.entrySet()) {
				toAddBigInteger.put(entry.getKey(), entry.getValue().toBigInteger());
			}
			
			maps.add(toAddBigInteger);
		}
		
		for(int k = 0; k < stackNumber; k++) {	
			StackBigInteger stack = RandomUtil.randomStackBigInteger(
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

    	for(StackBigInteger stack : stacks) {
    		
	    	File writerFile = new File("plateToJSON.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
			
	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
	
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

    	for(StackBigInteger stack : stacks) {
    		
    		File writerFile = new File("plateToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.iterator();
			
			while(reader.hasNextJSONPlate()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = reader.nextJSONPlate();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.descendingIterator();
			
			while(reader.hasNextJSONPlate()) {
				reader.nextJSONPlate();
			}
			
			while(reader.hasPreviousJSONPlate()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = reader.previousJSONPlate();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextJSONPlate();
			}
			
			List<PlateBigInteger> remaining = reader.remainingJSONPlates();
			Iterator<PlateBigInteger> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = remainingIter.next();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextJSONPlate();
			}
			
			List<PlateBigInteger> spent = reader.spentJSONPlates();
			Collections.reverse(spent);
			Iterator<PlateBigInteger> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = spentIter.next();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToJSON.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToJSON(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			List<PlateBigInteger> all = reader.allJSONPlates();
			
			Iterator<PlateBigInteger> allIter = all.iterator();
			Iterator<PlateBigInteger> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = allIter.next();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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
	
    	for(StackBigInteger stack : stacks) {
    		
	    	File writerFile = new File("plateToXML.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
			
	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
	
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

    	for(StackBigInteger stack : stacks) {
    		
    		File writerFile = new File("plateToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.iterator();
			
			while(reader.hasNextXMLPlate()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = reader.nextXMLPlate();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.descendingIterator();
			
			while(reader.hasNextXMLPlate()) {
				reader.nextXMLPlate();
			}
			
			while(reader.hasPreviousXMLPlate()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = reader.previousXMLPlate();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size());
			
			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextXMLPlate();
			}
			
			List<PlateBigInteger> remaining = reader.remainingXMLPlates();
			Iterator<PlateBigInteger> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = remainingIter.next();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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

    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<PlateBigInteger> iter = stack.iterator();
		
			int index = random.nextInt(stack.size()) + 1;
			
			for(int i = 0; i < index; i++) {
				reader.nextXMLPlate();
			}
			
			List<PlateBigInteger> spent = reader.spentXMLPlates();
			Collections.reverse(spent);
			Iterator<PlateBigInteger> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = spentIter.next();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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
   
    	for(StackBigInteger stack : stacks) {
    		
			File writerFile = new File("plateToXML.txt");
			PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);

	    	writer.plateToXML(stack.toArray());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			List<PlateBigInteger> all = reader.allXMLPlates();
			
			Iterator<PlateBigInteger> allIter = all.iterator();
			Iterator<PlateBigInteger> iter = stack.iterator();
			
			while(allIter.hasNext()) {
			    
				PlateBigInteger inputPlate = iter.next();
				PlateBigInteger outputPlate = allIter.next();
				
				Iterator<WellBigInteger> inputPlateIter = inputPlate.iterator();
				Iterator<WellBigInteger> outputPlateIter = outputPlate.iterator();
				
				while(outputPlateIter.hasNext()) {
					
					WellBigInteger inputWell = inputPlateIter.next();
					WellBigInteger outputWell = outputPlateIter.next();

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
