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
import static org.junit.Assert.assertNull;
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
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
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
public class PlateReaderBigDecimalResultsTest {
	
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
	private static Random random = new Random();
	
	private static PlateBigDecimal[] array = new PlateBigDecimal[plateNumber];
	private static List<Map<WellBigDecimal, BigDecimal>> maps = new ArrayList<Map<WellBigDecimal, BigDecimal>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigDecimal> stacks = new ArrayList<StackBigDecimal>();
	
	private static String path = "test.txt";
	private static File file;
	private static PlateWriterBigDecimal plateWriter;
	
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
    
    /*--------------------- Methods for Plate Map Input ----------------------*/
    
    /**
     * Tests has next map the method.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testHasNextMap() throws IOException, JAXBException {

    	plateWriter.resultToPlateMap(maps, type);
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		while(reader.hasNextMap()) {
			
			PlateBigDecimal input = new PlateBigDecimal(type);
			PlateBigDecimal output = reader.nextMap();
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : iter.next().entrySet()) {
				
				WellBigDecimal next = entry.getKey();
				WellBigDecimal well = new WellBigDecimal(next.row(), next.column());
				
				well.add(entry.getValue());
				input.addWells(well);
				
			}
			
			Iterator<WellBigDecimal> inputIter = input.iterator();
			Iterator<WellBigDecimal> outputIter = output.iterator();
			
			for(int i = 0; i < maps.size(); i++) {
				
				WellBigDecimal inputWell = inputIter.next();
				WellBigDecimal outputWell = outputIter.next();
				
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		while(reader.hasNextTable()) {
			
			WellSetBigDecimal input = new WellSetBigDecimal();
			WellSetBigDecimal output = reader.nextTable();
			
			for(Map.Entry<WellBigDecimal, BigDecimal> entry : iter.next().entrySet()) {
				
				WellBigDecimal next = entry.getKey();
				WellBigDecimal well = new WellBigDecimal(next.row(), next.column());
				
				well.add(entry.getValue());
				input.add(well);
				
			}
			
			Iterator<WellBigDecimal> inputIter = input.iterator();
			Iterator<WellBigDecimal> outputIter = output.iterator();
			
			for(int i = 0; i < maps.size(); i++) {
				
				WellBigDecimal inputWell = inputIter.next();
				WellBigDecimal outputWell = outputIter.next();
				
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		while(reader.hasNextJSONResult()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = reader.nextJSONResult();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		ListIterator<Map<WellBigDecimal, BigDecimal>> iter = maps.listIterator(maps.size());
		
		while(reader.hasNextJSONResult()) {
			reader.nextJSONResult();
		}
		
		while(reader.hasPreviousJSONResult()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.previous();
			WellSetBigDecimal output = reader.previousJSONResult();

			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size());

		for(int i = 0; i < index; i++) {
			iter.next();
			reader.nextJSONResult();
		}
		
		List<WellSetBigDecimal> remaining = reader.remainingJSONResults();
		Iterator<WellSetBigDecimal> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = remainingIter.next();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size()) + 1;

		for(int i = 0; i < index; i++) {
			reader.nextJSONResult();
		}
		
		List<WellSetBigDecimal> spent = reader.spentJSONResults();
		Collections.reverse(spent);
		Iterator<WellSetBigDecimal> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = spentIter.next();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		List<WellSetBigDecimal> all = reader.allJSONResults();
		
		Iterator<WellSetBigDecimal> allIter = all.iterator();
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		while(allIter.hasNext()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = allIter.next();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);

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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		while(reader.hasNextXMLResult()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = reader.nextXMLResult();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		ListIterator<Map<WellBigDecimal, BigDecimal>> iter = maps.listIterator(maps.size());
		
		while(reader.hasNextXMLResult()) {
			reader.nextXMLResult();
		}
		
		while(reader.hasPreviousXMLResult()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.previous();
			WellSetBigDecimal output = reader.previousXMLResult();

			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size());

		for(int i = 0; i < index; i++) {
			iter.next();
			reader.nextXMLResult();
		}
		
		List<WellSetBigDecimal> remaining = reader.remainingXMLResults();
		Iterator<WellSetBigDecimal> remainingIter = remaining.iterator();
		
		while(remainingIter.hasNext()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = remainingIter.next();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		int index = random.nextInt(maps.size()) + 1;

		for(int i = 0; i < index; i++) {
			reader.nextXMLResult();
		}
		
		List<WellSetBigDecimal> spent = reader.spentXMLResults();
		Collections.reverse(spent);
		Iterator<WellSetBigDecimal> spentIter = spent.iterator();
		
		while(spentIter.hasNext()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = spentIter.next();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
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
		
		PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
		List<WellSetBigDecimal> all = reader.allXMLResults();
		
		Iterator<WellSetBigDecimal> allIter = all.iterator();
		Iterator<Map<WellBigDecimal, BigDecimal>> iter = maps.iterator();
		
		while(allIter.hasNext()) {
			
			Map<WellBigDecimal, BigDecimal> input = iter.next();
			WellSetBigDecimal output = allIter.next();
			
			Iterator<Map.Entry<WellBigDecimal, BigDecimal>> iterInput = input.entrySet().iterator();
			Iterator<WellBigDecimal> iterOutput = output.iterator();
			
			while(iterInput.hasNext()) {
				
				Map.Entry<WellBigDecimal, BigDecimal> entry = iterInput.next();
				WellBigDecimal outputWell = iterOutput.next();

				assertEquals(entry.getKey(), outputWell);
				assertEquals(entry.getValue(), outputWell.get(0));
			}
		}

		reader.close();
		
    }

}
