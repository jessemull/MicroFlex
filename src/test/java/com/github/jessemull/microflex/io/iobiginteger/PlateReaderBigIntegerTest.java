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
import java.math.BigInteger;
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
public class PlateReaderBigIntegerTest {
	
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
	
	private static PlateBigInteger[] array = new PlateBigInteger[plateNumber];
	private static List<Map<WellBigInteger, BigInteger>> maps = new ArrayList<Map<WellBigInteger, BigInteger>>();
	private static List<String> labelList = new ArrayList<String>();
	private static List<StackBigInteger> stacks = new ArrayList<StackBigInteger>();
	
	private static String path = "test.txt";
	private static File file;
	
	@SuppressWarnings("unused")
	private static PlateWriterBigInteger plateWriter;
	
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
		plateWriter = new PlateWriterBigInteger(file);
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(reader);
    	
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(reader, 1000);
    	
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(stream);
    	
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(stream, 1000);
    	
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(stream, Charset.defaultCharset());
    	
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(stream, "UTF-8", 1000);
    	
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
    	
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(stream, decoder);
    	
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
    	
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(stream, decoder, 1000);
    	
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
    	
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(file);  	
    	
    	assertTrue(plateReader != null);
    	
    	plateReader.close();
    }
    
    /**
     * Tests the reader constructor.
     * @throws IOException 
     */
	@Test
    public void testConstructorFileInt() throws IOException {
    	
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(file, 1000);  	
    	
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
    	PlateReaderBigInteger plateReader = new PlateReaderBigInteger(fileName);
    
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

    	PlateReaderBigInteger reader = new PlateReaderBigInteger(file);
    	
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
