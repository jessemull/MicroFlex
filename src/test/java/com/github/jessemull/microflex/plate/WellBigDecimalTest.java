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

package com.github.jessemull.microflex.plate;

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.io.ByteStreams;

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.plate.Well;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This test case tests all big decimal well methods and constructors.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 26, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellBigDecimalTest {

	/* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private BigDecimal minValue = BigDecimal.ZERO;
	private BigDecimal maxValue = new BigDecimal(10000);
	private int minLength = 500;
	private int maxLength = 1000;
	private int minRow = 0;
	private int maxRow = 50;
	private int minColumn = 1;
	private int maxColumn = 50;
	private Random random = new Random();
	
    /* Value of false redirects System.err */
	
	private static boolean error = false;
	private static PrintStream originalOut = System.out;
	
	/**
	 * Toggles system error.
	 */
	@BeforeClass
	public static void redirectorErrorOut() {
		
		if(error == false) {

			System.setErr(new PrintStream(new OutputStream() {
			    public void write(int x) {}
			}));

		}
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
	
    /* ---------------------------- Constructors ---------------------------- */

    /**
     * Tests well constructor using row and column integers.
     */
	@Test
    public void testConstructorIntegers() {

		for(int i = 0; i < 10000; i++) { 

			int row = random.nextInt(51);
			int column = random.nextInt(51) + 1;
			String rowString = this.rowString(row);
			String index = rowString + column;

	        WellBigDecimal bigDecimalWell = new WellBigDecimal(row, column);

	        assertNotNull(bigDecimalWell);
	        assertEquals(bigDecimalWell.alphaBase(), 26);
	        assertEquals(bigDecimalWell.row(), row);
	        assertEquals(bigDecimalWell.column(), column);
	        assertEquals(bigDecimalWell.rowString(), rowString);
	        assertEquals(bigDecimalWell.type(), Well.BIGDECIMAL);
	        assertEquals(bigDecimalWell.index(), index);
	        assertEquals(bigDecimalWell.typeString(), "BigDecimal");    
		}
    }
       
    /**
     * Tests well constructor using a row string and a column integer.
     */
    @Test
    public void testConstructorStringInt() {
    	
        for(int i = 0; i < 10000; i++) { 			

			int row = random.nextInt(50);
			int column = random.nextInt(50) + 1;
			String rowString = this.rowString(row);
			String index = rowString + column;

	        WellBigDecimal bigDecimalWell = new WellBigDecimal(rowString, column);
	        
	        assertEquals(bigDecimalWell.row(), row);
	        assertEquals(bigDecimalWell.column(), column);
	        assertEquals(bigDecimalWell.rowString(), rowString);
	        assertEquals(bigDecimalWell.type(), Well.BIGDECIMAL);
	        assertEquals(bigDecimalWell.index(), index);
	        assertEquals(bigDecimalWell.typeString(), "BigDecimal");
		}
        
    }
        
    /**
     * Tests well constructor using a row integer and a column string.
     */
    @Test
    public void testConstructorIntString() {

        for(int i = 0; i < 10000; i++) {			

			int row = random.nextInt(50);
			int column = random.nextInt(50) + 1;
			String rowString = this.rowString(row);
			String columnString = "" + column;
			String index = rowString + column;
			
	        WellBigDecimal bigDecimalWell = new WellBigDecimal(row, columnString);
	        
	        assertEquals(bigDecimalWell.row(), row);
	        assertEquals(bigDecimalWell.column(), column);
	        assertEquals(bigDecimalWell.rowString(), rowString);
	        assertEquals(bigDecimalWell.type(), Well.BIGDECIMAL);
	        assertEquals(bigDecimalWell.index(), index);
	        assertEquals(bigDecimalWell.typeString(), "BigDecimal");
	        
		}
        
    }
    
    /**
     * Tests well constructor using row and column strings.
     */
    @Test
    public void testConstructorStringString() {

        for(int i = 0; i < 10000; i++) { 
        	
			int row = random.nextInt(50);
			int column = random.nextInt(50) + 1;
			String rowString = this.rowString(row);
			String columnString = "" + column;
			String index = rowString + column;

	        WellBigDecimal bigDecimalWell = new WellBigDecimal(rowString, columnString);

	        assertNotNull(bigDecimalWell);
	        assertEquals(bigDecimalWell.alphaBase(), 26);
	        assertEquals(bigDecimalWell.row(), row);
	        assertEquals(bigDecimalWell.column(), column);
	        assertEquals(bigDecimalWell.rowString(), rowString);
	        assertEquals(bigDecimalWell.type(), Well.BIGDECIMAL);
	        assertEquals(bigDecimalWell.index(), index);
	        assertEquals(bigDecimalWell.typeString(), "BigDecimal");
	        
		}
        
    }
    
    /**
     * Tests well constructor using a well ID string.
     */
    @Test
    public void testConstructorIntWellID() {

        for(int i = 0; i < 10000; i++) { 

			int row = random.nextInt(50);
			int column = random.nextInt(50) + 1;
			String rowString = this.rowString(row);
			String index = rowString + column;
			
	        WellBigDecimal bigDecimalWell = new WellBigDecimal(index);

	        assertNotNull(bigDecimalWell);
	        assertEquals(bigDecimalWell.alphaBase(), 26);	        
	        assertEquals(bigDecimalWell.row(), row);
	        assertEquals(bigDecimalWell.column(), column);
	        assertEquals(bigDecimalWell.rowString(), rowString);
	        assertEquals(bigDecimalWell.type(), Well.BIGDECIMAL);
	        assertEquals(bigDecimalWell.index(), index);
	        assertEquals(bigDecimalWell.typeString(), "BigDecimal");
	        
		}
        
    }
    
    /**
     * Tests exception thrown when the column parameter < 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumnException() {
        new WellBigDecimal(0, 0);
    }
    
    /**
     * Tests exception thrown when the row parameter < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRowException() {
        new WellBigDecimal(-1, 1);
    }
    
    /**
     * Tests the compare to method for big integer wells.
     */
    @Test
    public void testCompareTo() {  
    	
        WellBigDecimal well1 = new WellBigDecimal(0, 1); 
        WellBigDecimal well2 = new WellBigDecimal(0, 2);
        WellBigDecimal well3 = new WellBigDecimal(2, 1);
        
        WellBigDecimal clone = new WellBigDecimal(well1);
        
        assertEquals(well1.compareTo(well2), -1);
        assertEquals(well2.compareTo(well1), 1);
        assertEquals(well3.compareTo(well1), 1);
        assertEquals(well1.compareTo(clone), 0);
        
    }
    
    /**
     * Tests addition of a single big integer.
     */
    @Test
    public void testAddition() {
    	
    	List<BigDecimal> bigDecimalList = RandomUtil.
    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);

        for(int i = 0; i < 100; i++) {

	    	List<BigDecimal> clone = new ArrayList<BigDecimal>(bigDecimalWell.data());
	    	bigDecimalWell.add(bigDecimalList.get(0));
	    	assertTrue(bigDecimalWell.data().size() == clone.size() + 1);
	    	
	    	clone.add(bigDecimalList.get(0));
	    	assertEquals(bigDecimalWell.data(), clone);
        }
    }
    
    /**
     * Tests the addition of a big integer collection.
     */
    @Test
    public void testAdditionCollection() {
    	
    	List<BigDecimal> bigDecimalList = RandomUtil.
    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
    	
        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);

        for(int i = 0; i < 100; i++) {
 
	    	List<BigDecimal> clone = new ArrayList<BigDecimal>(bigDecimalWell.data());

	    	bigDecimalWell.add(bigDecimalList);
	    	assertTrue(bigDecimalWell.data().size() == clone.size() + bigDecimalList.size());
	    	
	    	clone.addAll(bigDecimalList);
	    	assertEquals(bigDecimalWell.data(), clone);
	    
    	}
    }
    
    /**
     * Tests addition of a big integer array.
     */
    @Test
    public void testAdditionArray() {
    	
    	List<BigDecimal> bigDecimalList = RandomUtil.
    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);

        for(int i = 0; i < 100; i++) {
    		
	    	List<BigDecimal> addBigDecimal = RandomUtil.
	    			randomBigDecimalList(new BigDecimal(0), new BigDecimal(10000), 500, 1000);

	    	List<BigDecimal> clone = new ArrayList<BigDecimal>(bigDecimalWell.data());

	    	bigDecimalWell.add(addBigDecimal.toArray(new BigDecimal[addBigDecimal.size()]));
	    	assertTrue(bigDecimalWell.data().size() == clone.size() + addBigDecimal.size());
	    	
	    	clone.addAll(addBigDecimal);
	    	assertEquals(bigDecimalWell.data(), clone);
    	}
    }
    
    /**
     * Tests addition of a big integer well.
     */
    @Test
    public void testAdditionWell() {
    	
    	List<BigDecimal> bigDecimalList = RandomUtil.
    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
    	
        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);

        for(int i = 0; i < 100; i++) {

	    	List<BigDecimal> clone = new ArrayList<BigDecimal>(bigDecimalWell.data());

	    	WellBigDecimal addWell = RandomUtil.randomWellBigDecimal(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
	    	
	    	bigDecimalWell.add(addWell);

	    	assertEquals(bigDecimalWell.size(), clone.size() + addWell.size());
	    	
	    	clone.addAll(addWell.data());
	    	assertEquals(bigDecimalWell.data(), clone);

    	}
    }
    
    /**
     * Tests addition of a big integer well set.
     */
    @Test
    public void testAdditionSet() {
    	
    	List<BigDecimal> bigDecimalList = RandomUtil.
    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
    	
        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);

    	PrintStream current = System.err;
    	
    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
    	System.setErr(dummy);
    	
        for(int i = 0; i < 10000; i++) {
        	
	    	List<BigDecimal> clone = new ArrayList<BigDecimal>(bigDecimalWell.data());
	    	
	    	WellSetBigDecimal addSet = RandomUtil.randomWellSetBigDecimal(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
	    	bigDecimalWell.add(addSet);
	    	
	    	ArrayList<BigDecimal> setList = new ArrayList<BigDecimal>();
	    	for(WellBigDecimal well : addSet) {
	    		setList.addAll(well.data());
	    	}
	    	
	    	assertEquals(bigDecimalWell.size(), clone.size() + setList.size());
	    	
	    	clone.addAll(setList);
	    	assertEquals(bigDecimalWell.data(), clone);
    	}
        
        System.setErr(current);
    }
    
    /**
     * Tests for replacement of a single big integer.
     */
    @Test
    public void testReplacement() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	BigDecimal toReplace = new BigDecimal(Math.random());
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	
	        bigDecimalWell.replaceData(toReplace);
	        bigDecimalList.add(toReplace);
	        
	        assertTrue(bigDecimalWell.size() == 1);
	        assertEquals(bigDecimalWell.data().get(0), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of an array of big integers.
     */
    @Test
    public void testReplacementArray() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	List<BigDecimal> toReplace = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	
	        bigDecimalWell.replaceData(toReplace.toArray(new BigDecimal[toReplace.size()]));
	        
	        assertEquals(bigDecimalWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	List<BigDecimal> toReplace = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	    	
	        bigDecimalWell.replaceData(toReplace);
	        
	        assertEquals(bigDecimalWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	List<BigDecimal> replaceList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        WellBigDecimal toReplace = this.randomWell(replaceList);
	
	        bigDecimalWell.replaceData(toReplace);
	        
	        assertEquals(bigDecimalWell.data(), toReplace.data());
    	}
    }
    
    /**
     * Tests for replacement of a big integer well set.
     */
    @Test
    public void testReplacementSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	    	  	
	    	PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetBigDecimal toReplace = RandomUtil.randomWellSetBigDecimal(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
		    bigDecimalWell.replaceData(toReplace);
		    	
		    ArrayList<BigDecimal> setList = new ArrayList<BigDecimal>();
		    for(WellBigDecimal well : toReplace) {
		    	setList.addAll(well.data());
		    }
	
		   assertEquals(bigDecimalWell.data(), setList);
	        
	       System.setErr(current);
    	}
 
    }
    
    /**
     * Tests removal of a lone big integer.
     */
    @Test
    public void testRemoveArray() {

    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        Set<BigDecimal> toRemove = new HashSet<BigDecimal>();
	        
	        for(int j = 0; j < bigDecimalWell.size() / 2; j++) {
	        	int index = random.nextInt(bigDecimalList.size());
	        	toRemove.add(bigDecimalWell.data().get(index));
	        }
	
	        bigDecimalWell.remove(toRemove.toArray(new BigDecimal[toRemove.size()]));
	        
	        for(BigDecimal bd : toRemove) {
	        	assertFalse(bigDecimalWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests the contains method.
     */
    @Test
    public void testContains() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        for(BigDecimal bd : bigDecimalList) {
	        	assertTrue(bigDecimalWell.contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a big integer array.
     */
    @Test
    public void testRemove() {
    	
    	for(int i = 0; i < 100; i++) {
    		
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);

	        int index = random.nextInt(bigDecimalList.size());
	        
	        BigDecimal toRemove = bigDecimalWell.data().get(index);
	        bigDecimalWell.remove(toRemove);
	         
	        assertFalse(bigDecimalWell.data().contains(toRemove));
    	}
    }
    
    /**
     * Tests removal of a big integer collection.
     */
    @Test
    public void testRemoveCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        List<BigDecimal> toRemove = new ArrayList<BigDecimal>();
	        
	        for(int j =0; j < 100; j++) {

		        int index = random.nextInt(bigDecimalList.size());

	        	toRemove.add(bigDecimalWell.data().get(index));
	        }
	        
	        bigDecimalWell.remove(toRemove);
	        
	        for(BigDecimal bd : toRemove) {
	        	assertFalse(bigDecimalWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well.
     */
    @Test
    public void testRemoveWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        List<BigDecimal> toRemove = new ArrayList<BigDecimal>(); 

        	for(int j = 0; j < 100; j++) {
		        int index = random.nextInt(bigDecimalList.size());
	        	toRemove.add(bigDecimalWell.data().get(index));
	        }
	        WellBigDecimal toRemoveWell = 
	        		new WellBigDecimal(bigDecimalWell.row(), bigDecimalWell.column(), toRemove);
	        bigDecimalWell.removeWell(toRemoveWell);
	        
	        for(BigDecimal bd : toRemove) {
	        	assertFalse(bigDecimalWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well set.
     */
    @Test
    public void testRemoveSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetBigDecimal set = new WellSetBigDecimal();

	    	for(int j = 0; j < 10; j++) {

		    	int row = (int)(Math.random() * (maxRow) + 1);
		    	int column = 1 + (int)(Math.random() * (maxColumn - 1) + 1);

	    		WellBigDecimal well = new WellBigDecimal(row, column);
	    		
	            for(int k = 0; k < 100; k++) {
	            	int index = random.nextInt(bigDecimalList.size());
	        	    well.add(bigDecimalWell.data().get(index));
	            }
	            
	            set.add(well);
	    	}
	    
	    	bigDecimalWell.removeSet(set);
	        
	        for(WellBigDecimal well : set) {
	        	for(BigDecimal bd : well) {
	        	    assertFalse(bigDecimalWell.data().contains(bd));
	        	}
	        }
	        
	        System.setErr(current);
    	}
   
    }
    
    /**
     * Tests removal using a range of values in the data set.
     */
    @Test
    public void testRemoveRange() {
    	
	    for(int i = 0; i < 100; i++) {
	    	
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        List<BigDecimal> checkEq = new ArrayList<BigDecimal>(bigDecimalWell.data());
	        
	        int begin = (int)(Math.random() * ((bigDecimalList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin) + 1));

	        bigDecimalWell.removeRange(begin, end);
	
	        checkEq.subList(begin, end).clear();
	        assertEquals(checkEq, bigDecimalWell.data());
    	}
    }
    
    /**
     * Tests retention of a lone big integer.
     */
    @Test
    public void testRetain() {

    	for(int i = 0; i < 100; i++) {
	    	
    		List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        int index = random.nextInt(bigDecimalList.size());
	    	
	        BigDecimal toRetain = bigDecimalWell.get(index);
	        bigDecimalWell.retain(toRetain);
	        
	        assertTrue(bigDecimalWell.size() == 1);
	        assertTrue(bigDecimalWell.get(0) == toRetain);
    	}
    }
    
    /**
     * Tests retention of a big integer collection.
     */
    @Test
    public void testRetainCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        int begin = (int)(Math.random() * ((bigDecimalList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin) + 1));
	        
	        List<BigDecimal> toRetain = new ArrayList<BigDecimal>(bigDecimalList.subList(begin, end));
	        bigDecimalWell.retain(toRetain);

	        for(BigDecimal bd : bigDecimalWell) {
	            assertTrue(toRetain.contains(bd));
	        }    	
	        
    	}
    }
    
    /**
     * Tests retention of a big integer array.
     */
    @Test
    public void testRetainArray() {
    	    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        int begin = (int)(Math.random() * ((bigDecimalList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin) + 1));
	        
	        List<BigDecimal> toRetainList = new ArrayList<BigDecimal>(bigDecimalList.subList(begin, end));
	        BigDecimal[] toRetainArray = toRetainList.toArray(new BigDecimal[toRetainList.size()]);
	        bigDecimalWell.retain(toRetainArray);

	        for(BigDecimal bd : bigDecimalWell) {
	            assertTrue(toRetainList.contains(bd));
	        }    	
    	}
    }
    
    /**
     * Tests retention of a big integer well.
     */
    @Test
    public void testRetainWell() {
    	    	
    	for(int i = 0; i < 100; i++) {
    		
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        int begin = 1 + (int)(Math.random() * ((bigDecimalList.size()) - 1));
	    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin)));
	        
	        List<BigDecimal> toRetainList = new ArrayList<BigDecimal>(bigDecimalList.subList(begin, end));
	        WellBigDecimal toRetainWell = bigDecimalWell.subList(begin, end - begin); 
	        bigDecimalList.retainAll(toRetainList);
	        
	        bigDecimalWell.retainWell(toRetainWell);
	        
	        assertEquals(bigDecimalWell.data(), bigDecimalList);
    	}
    }    
    
    /**
     * Tests retention of a big integer well set.
     */
    @Test
    public void testRetainSet() {
    	    	
    	for(int j = 0; j < 100; j++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	        
	        WellSetBigDecimal toRetainSet = new WellSetBigDecimal();
	        List<BigDecimal> allValues = new ArrayList<BigDecimal>();
	        
	        for(int i = 0; i < 5; i++) {
	        
	        	int begin = (int)(Math.random() * ((bigDecimalList.size()) + 1));
		    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin) + 1));
	        
	            List<BigDecimal> toRetainList = new ArrayList<BigDecimal>(bigDecimalList.subList(begin, end));
	            allValues.addAll(toRetainList);
	            WellBigDecimal toRetainWell = new WellBigDecimal(bigDecimalWell.row(), bigDecimalWell.column(), toRetainList); 
	            
	            toRetainSet.add(toRetainWell);
	        }

	        bigDecimalWell.retainSet(toRetainSet);
	
	        for(BigDecimal bd : bigDecimalWell.data()) {
	            assertTrue(allValues.contains(bd));
	        }
    	}
    }    
    
    /**
     * Tests removal using a range of values in the data set.
     */
    @Test
    public void testRetainRange() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	
	        int begin = (int)(Math.random() * ((bigDecimalList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin) + 1));
	
	        List<BigDecimal> range = new ArrayList<BigDecimal>(bigDecimalWell.data().subList(begin, end));
	        bigDecimalWell.retainRange(begin, end);
	
	        assertEquals(bigDecimalWell.data(), range);
    	}
    }
    
    /**
     * Tests the size method.
     */
    @Test
    public void testSize() {
    	
    	for(int i = 0; i < 10000; i++) {
    		
    		int row = (int)(Math.random() * (1000));
	    	int column = 1 + (int)(Math.random() * ((1000 - 1) + 1));
    		
    		List<BigDecimal> list = RandomUtil.randomBigDecimalList(BigDecimal.ZERO, new BigDecimal(1000), 0, 1000);
    		WellBigDecimal well = new WellBigDecimal(row, column, list);
 
    		assertTrue(list.size() == well.size());
    	}
    }
    
    /**
     * Tests is empty method.
     */
    @Test
    public void testEmpty() {
    	
    	for(int i = 0; i < 100; i++) {
    	    
    		WellBigDecimal well = new WellBigDecimal(0,1);
    	    assertTrue(well.isEmpty());
    	
    	    well.add(BigDecimal.ONE);
    	    assertFalse(well.isEmpty());
    	
    	    well.clear();
    	    assertTrue(well.isEmpty());
    	}
    	
    }
    
    /**
     * Tests sublist method.
     */
    @Test
    public void testSublist() {
    	
    	for(int i = 0; i < 10000; i++) {
    		
    		List<BigDecimal> bigDecimalList = RandomUtil.
        			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
        	
            WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
            
            int begin = (int)(Math.random() * ((bigDecimalList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigDecimalList.size() - begin) + 1));
	    	
    		begin = begin < end ? begin : end;
    		end = begin > end ? begin : end;

    		WellBigDecimal sub = bigDecimalWell.subList(begin, end - begin);

    		assertEquals(sub.data(), bigDecimalWell.data().subList(begin, end));
    	}
    }
    
    /**
     * Tests last index of method.
     */
	@Test
    public void testLastIndexOf() {
    	
    	for(int i = 0; i < 100; i++) {
	    	BigDecimal[] array = {BigDecimal.ZERO, BigDecimal.ONE, new BigDecimal(2)};
	    	WellBigDecimal well = new WellBigDecimal(0, 1);
	    	well.add(array);
	    	
	    	assertTrue(0 == well.lastIndexOf(BigDecimal.ZERO));
	    	assertTrue(1 == well.lastIndexOf(BigDecimal.ONE));
	    	assertTrue(2 == well.lastIndexOf(new BigDecimal(2)));
	    	
	    	well.add(array);
	    	
	    	assertTrue(3 == well.lastIndexOf(BigDecimal.ZERO));
	    	assertTrue(4 == well.lastIndexOf(BigDecimal.ONE));
	    	assertTrue(5 == well.lastIndexOf(new BigDecimal(2)));
	
	    	well.removeRange(3, 6);
	
	    	assertTrue(0 == well.lastIndexOf(BigDecimal.ZERO));
	    	assertTrue(1 == well.lastIndexOf(BigDecimal.ONE));
	    	assertTrue(2 == well.lastIndexOf(new BigDecimal(2)));
    	}
    }
    
    /**
     * Tests the index of method.
     */
    @Test
    public void testIndexOf() {

    	for(int i = 0; i < 100; i++) {
    		
	    	List<BigDecimal> bigDecimalList = RandomUtil.
	    			randomBigDecimalList(minValue, maxValue, minLength, maxLength);
	    	
	    	Set<BigDecimal> set = new HashSet<BigDecimal>(bigDecimalList);
    		bigDecimalList = new ArrayList<BigDecimal>(set);
	    	
	    	int index = random.nextInt(bigDecimalList.size());
	    	BigDecimal value = bigDecimalList.get(index);
	
	        WellBigDecimal bigDecimalWell = this.randomWell(bigDecimalList);
	    	assertTrue(index == bigDecimalWell.indexOf(value));
    	}
    }
    
    /**
     * Returns a random big integer well.
     */
    public WellBigDecimal randomWell(List<BigDecimal> bigDecimalList) {
    	
    	BigDecimal[] bigDecimalArray = bigDecimalList.toArray(new BigDecimal[bigDecimalList.size()]);
    	
    	int row = minRow + (int)(Math.random() * (maxRow + 1 - minRow) + 1);
    	int column = minColumn + (int)(Math.random() * (maxColumn + 1 + minColumn) + 1);
    	
    	return new WellBigDecimal(row, column, bigDecimalArray);
    }
    
    /**
     * Returns the row ID.
     * @return    row ID
     */
    public String rowString(int row) {
        
        String rowString = "";
        
        while (row >=  0) {
            rowString = (char) (row % 26 + 65) + rowString;
            row = (row / 26) - 1;
        }
        
        return rowString;
    }
	
}
