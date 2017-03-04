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
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
public class PlateReaderBigDecimalTest {
	
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
	
	private static PlateBigDecimal[] array = new PlateBigDecimal[plateNumber];
	private static List<Map<WellBigDecimal, BigDecimal>> maps = new ArrayList<Map<WellBigDecimal, BigDecimal>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigDecimal> stacks = new ArrayList<StackBigDecimal>();
	
	private static String path = "test.txt";
	private static File file;
	
	@SuppressWarnings("unused")
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
	
	/*----------------------------- Constructors -----------------------------*/

    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorReader() throws IOException {     
    	
    	Reader reader = new FileReader(file);
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(reader);
    	
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
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(reader, 1000);
    	
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
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(stream);
    	
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
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(stream, 1000);
    	
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
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(stream, Charset.defaultCharset());
    	
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
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(stream, "UTF-8", 1000);
    	
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
    	
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(stream, decoder);
    	
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
    	
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(stream, decoder, 1000);
    	
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
    	
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(file);  	
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorFileInt() throws IOException {
    	
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(file, 1000);  	
    	
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
    	PlateReaderBigDecimal plateReader = new PlateReaderBigDecimal(fileName);
    
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

    	PlateReaderBigDecimal reader = new PlateReaderBigDecimal(file);
    	
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
