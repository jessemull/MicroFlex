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
public class PlateReaderBigIntegerWellsTest {
	
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
	
    /*---------------------- Methods for JSON Well Input ---------------------*/
    
    /**
     * Tests the has next JSON well method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testHasNextJSONWell() throws IOException, JAXBException {    

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
	
			int index = 0;
			while(reader.hasNextJSONWell()) {
				reader.nextJSONWell();
				index++;
			}
	
			assertTrue(plate.size() == index);
			assertFalse(reader.hasNextJSONWell());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}
		
    }
    
    /**
     * Tests the has previous JSON well method.
     * @throws IOException 
     * @throws JAXBException 
     */
    @Test
    public void testHasPreviousJSONWell() throws IOException, JAXBException {    

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);

			assertFalse(reader.hasPreviousJSONWell());
			
			while(reader.hasNextJSONWell()) {
				reader.nextJSONWell();
			}
	
			int index = 0;
			while(reader.hasPreviousJSONWell()) {
				reader.previousJSONWell();
				index++;
			}
			
			assertTrue(plate.size() == index);
			assertFalse(reader.hasPreviousJSONWell());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next JSON well method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testNextJSONWell() throws IOException, JAXBException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.iterator();
			
			while(reader.hasNextJSONWell()) {
				
				WellBigInteger input = iter.next();
				WellBigInteger output = reader.nextJSONWell();
				
				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the previous JSON well method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testPreviousJSONWell() throws IOException, JAXBException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.descendingIterator();
			
			while(reader.hasNextJSONWell()) {
				reader.nextJSONWell();
			}
			
			while(reader.hasPreviousJSONWell()) {
				
				WellBigInteger input = iter.next();
				WellBigInteger output = reader.previousJSONWell();

				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the remaining JSON wells method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testRemainingJSONWells() throws IOException, JAXBException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.iterator();
			
			int index = random.nextInt(plate.size());

			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextJSONWell();
			}

			List<WellBigInteger> remaining = reader.remainingJSONWells();
			Iterator<WellBigInteger> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
				
				WellBigInteger input = remainingIter.next();
				WellBigInteger output = iter.next();

				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the spent JSON wells method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testSpentJSONWells() throws IOException, JAXBException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.iterator();
			
			int index = random.nextInt(plate.size()) + 1;

			for(int i = 0; i < index; i++) {
				reader.nextJSONWell();
			}

			List<WellBigInteger> spent = reader.spentJSONWells();
			Collections.reverse(spent);
			Iterator<WellBigInteger> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
				
				WellBigInteger input = spentIter.next();
				WellBigInteger output = iter.next();

				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the all JSON wells method.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testAllJSONWells() throws IOException, JAXBException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToJSON.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToJSON(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			List<WellBigInteger> all = reader.allJSONWells();
			
			Iterator<WellBigInteger> iter = plate.iterator();
			Iterator<WellBigInteger> iterAll = all.iterator();
			
			while(iterAll.hasNext()) {
				
				WellBigInteger input = iter.next();
				WellBigInteger output = iterAll.next();
				
				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /*---------------------- Methods for XML Well Input ----------------------*/
    
    /**
     * Tests the has next XML well method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasNextXMLWell() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
	
			int index = 0;
			while(reader.hasNextXMLWell()) {
				reader.nextXMLWell();
				index++;
			}
	
			assertTrue(plate.size() == index);
			assertFalse(reader.hasNextXMLWell());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}
    }
    
    /**
     * Tests the has previous XML well method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testHasPreviousXMLWell() throws IOException, JAXBException, TransformerException, ParserConfigurationException {    

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);

			assertFalse(reader.hasPreviousXMLWell());
			
			while(reader.hasNextXMLWell()) {
				reader.nextXMLWell();
			}
	
			int index = 0;
			while(reader.hasPreviousXMLWell()) {
				reader.previousXMLWell();
				index++;
			}
			
			assertTrue(plate.size() == index);
			assertFalse(reader.hasPreviousXMLWell());
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the next XML well method.
     * @throws IOException 
     * @throws JAXBException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testNextXMLWell() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.iterator();
			
			while(reader.hasNextXMLWell()) {
				
				WellBigInteger input = iter.next();
				WellBigInteger output = reader.nextXMLWell();
				
				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the previous XML well method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testPreviousXMLWell() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.descendingIterator();
			
			while(reader.hasNextXMLWell()) {
				reader.nextXMLWell();
			}
			
			while(reader.hasPreviousXMLWell()) {
				
				WellBigInteger input = iter.next();
				WellBigInteger output = reader.previousXMLWell();

				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the remaining XML wells method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testRemainingXMLWells() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.iterator();
			
			int index = random.nextInt(plate.size());

			for(int i = 0; i < index; i++) {
				iter.next();
				reader.nextXMLWell();
			}

			List<WellBigInteger> remaining = reader.remainingXMLWells();
			Iterator<WellBigInteger> remainingIter = remaining.iterator();
			
			while(remainingIter.hasNext()) {
				
				WellBigInteger input = remainingIter.next();
				WellBigInteger output = iter.next();

				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the spent XML wells method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testSpentXMLWells() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			Iterator<WellBigInteger> iter = plate.iterator();
			
			int index = random.nextInt(plate.size()) + 1;

			for(int i = 0; i < index; i++) {
				reader.nextXMLWell();
			}

			List<WellBigInteger> spent = reader.spentXMLWells();
			Collections.reverse(spent);
			Iterator<WellBigInteger> spentIter = spent.iterator();
			
			while(spentIter.hasNext()) {
				
				WellBigInteger input = spentIter.next();
				WellBigInteger output = iter.next();

				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }
    
    /**
     * Tests the all XML wells method.
     * @throws JAXBException 
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws TransformerException 
     */
    @Test
    public void testAllXMLWells() throws IOException, JAXBException, TransformerException, ParserConfigurationException {

    	for(PlateBigInteger plate : array) {
    		
    		File writerFile = new File("wellToXML.txt");
    		PlateWriterBigInteger writer = new PlateWriterBigInteger(writerFile);
    		
	    	writer.wellToXML(plate.dataSet().allWells());
			
			PlateReaderBigInteger reader = new PlateReaderBigInteger(writerFile);
			List<WellBigInteger> all = reader.allXMLWells();
			
			Iterator<WellBigInteger> iter = plate.iterator();
			Iterator<WellBigInteger> iterAll = all.iterator();
			
			while(iterAll.hasNext()) {
				
				WellBigInteger input = iter.next();
				WellBigInteger output = iterAll.next();
				
				assertEquals(input, output);
				assertEquals(input.data(), output.data());
			}
			
			reader.close();
			writer.close();
			writerFile.delete();
    	}

    }

}
