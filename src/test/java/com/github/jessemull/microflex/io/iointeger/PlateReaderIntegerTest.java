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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.xml.bind.JAXBException;
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
public class PlateReaderIntegerTest {
	
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
	private static int stackNumber = 5;
	
	private static PlateInteger[] array = new PlateInteger[plateNumber];
	private static List<Map<WellInteger, Integer>> maps = new ArrayList<Map<WellInteger, Integer>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackInteger> stacks = new ArrayList<StackInteger>();
	
	private static String path = "test.txt";
	private static File file;
	
	@SuppressWarnings("unused")
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
	
	/*----------------------------- Constructors -----------------------------*/

    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorReader() throws IOException {     
    	
    	Reader reader = new FileReader(file);
    	PlateReaderInteger plateReader = new PlateReaderInteger(reader);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorReaderSize() throws IOException { 	
    	
    	Reader reader = new FileReader(file);
    	PlateReaderInteger plateReader = new PlateReaderInteger(reader, 1000);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }

    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorInputStream() throws IOException {

    	InputStream stream = new FileInputStream(file);
    	PlateReaderInteger plateReader = new PlateReaderInteger(stream);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorInputStreamInt() throws IOException {
    	
    	InputStream stream = new FileInputStream(file);
    	PlateReaderInteger plateReader = new PlateReaderInteger(stream, 1000);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }

    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorInputStreamCharset() throws IOException {
    	
    	InputStream stream = new FileInputStream(file);
    	PlateReaderInteger plateReader = new PlateReaderInteger(stream, Charset.defaultCharset());
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    	
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorInputStreamCharsetInt() throws IOException {

    	InputStream stream = new FileInputStream(file);
    	PlateReaderInteger plateReader = new PlateReaderInteger(stream, "UTF-8", 1000);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    	
    }

    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorInputStreamCharsetDecoder() throws IOException {
    	
    	InputStream stream = new FileInputStream(file);
    	CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
    	
    	PlateReaderInteger plateReader = new PlateReaderInteger(stream, decoder);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorInputStreamCharsetDecoderInt() throws IOException { 

    	InputStream stream = new FileInputStream(file);
    	CharsetDecoder decoder = Charset.defaultCharset().newDecoder();
    	
    	PlateReaderInteger plateReader = new PlateReaderInteger(stream, decoder, 1000);
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws JAXBException 
     * @throws IOException 
     */
	@Test
    public void testConstructorFile() throws IOException, JAXBException {
    	
    	PlateReaderInteger plateReader = new PlateReaderInteger(file);  	
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorFileInt() throws IOException {
    	
    	PlateReaderInteger plateReader = new PlateReaderInteger(file, 1000);  	
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }

    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorString() throws IOException {
    	
    	String fileName = file.getAbsolutePath();
    	PlateReaderInteger plateReader = new PlateReaderInteger(fileName);
    
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     */
	@Test
    public void testConstructorStringSize() {

    }
    
    /*--------------------- Methods for validating input ---------------------*/
    
    /**
     * Tests the set and get delimiter methods.
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void testDelimiter() throws IOException, JAXBException {

    	PlateReaderInteger reader = new PlateReaderInteger(file);
    	
    	Random random = new Random();
        String last = "\t";
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
        		            "abcdefghijklmnopqrstuvwxyz" +
        		            "0123456789, %&#@!~	<>;:=";
    	
    	for(int i = 0; i < 1000; i++) {   	
    		
    		int index = random.nextInt(characters.length());
    		String next = characters.charAt(index) + "";
    		
    		assertEquals(reader.getDelimiter(), last);
    		reader.setDelimiter(next);
    		
    		assertEquals(reader.getDelimiter(), next);
    		last = next;
    	}
    	
    	reader.close();
    	
    }

}
