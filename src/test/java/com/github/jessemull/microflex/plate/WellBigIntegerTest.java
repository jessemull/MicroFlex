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
import java.math.BigInteger;
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

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.plate.Well;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This test case tests all big integer well methods and constructors.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 26, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellBigIntegerTest {

	/* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private BigInteger minValue = BigInteger.ZERO;
	private BigInteger maxValue = new BigInteger("" + 10000);
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

	        WellBigInteger bigIntegerWell = new WellBigInteger(row, column);

	        assertNotNull(bigIntegerWell);
	        assertEquals(bigIntegerWell.alphaBase(), 26);
	        assertEquals(bigIntegerWell.row(), row);
	        assertEquals(bigIntegerWell.column(), column);
	        assertEquals(bigIntegerWell.rowString(), rowString);
	        assertEquals(bigIntegerWell.type(), Well.BIGINTEGER);
	        assertEquals(bigIntegerWell.index(), index);
	        assertEquals(bigIntegerWell.typeString(), "BigInteger");    
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

	        WellBigInteger bigIntegerWell = new WellBigInteger(rowString, column);
	        
	        assertEquals(bigIntegerWell.row(), row);
	        assertEquals(bigIntegerWell.column(), column);
	        assertEquals(bigIntegerWell.rowString(), rowString);
	        assertEquals(bigIntegerWell.type(), Well.BIGINTEGER);
	        assertEquals(bigIntegerWell.index(), index);
	        assertEquals(bigIntegerWell.typeString(), "BigInteger");
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
			
	        WellBigInteger bigIntegerWell = new WellBigInteger(row, columnString);
	        
	        assertEquals(bigIntegerWell.row(), row);
	        assertEquals(bigIntegerWell.column(), column);
	        assertEquals(bigIntegerWell.rowString(), rowString);
	        assertEquals(bigIntegerWell.type(), Well.BIGINTEGER);
	        assertEquals(bigIntegerWell.index(), index);
	        assertEquals(bigIntegerWell.typeString(), "BigInteger");
	        
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

	        WellBigInteger bigIntegerWell = new WellBigInteger(rowString, columnString);

	        assertNotNull(bigIntegerWell);
	        assertEquals(bigIntegerWell.alphaBase(), 26);
	        assertEquals(bigIntegerWell.row(), row);
	        assertEquals(bigIntegerWell.column(), column);
	        assertEquals(bigIntegerWell.rowString(), rowString);
	        assertEquals(bigIntegerWell.type(), Well.BIGINTEGER);
	        assertEquals(bigIntegerWell.index(), index);
	        assertEquals(bigIntegerWell.typeString(), "BigInteger");
	        
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
			
	        WellBigInteger bigIntegerWell = new WellBigInteger(index);

	        assertNotNull(bigIntegerWell);
	        assertEquals(bigIntegerWell.alphaBase(), 26);	        
	        assertEquals(bigIntegerWell.row(), row);
	        assertEquals(bigIntegerWell.column(), column);
	        assertEquals(bigIntegerWell.rowString(), rowString);
	        assertEquals(bigIntegerWell.type(), Well.BIGINTEGER);
	        assertEquals(bigIntegerWell.index(), index);
	        assertEquals(bigIntegerWell.typeString(), "BigInteger");
	        
		}
        
    }
    
    /**
     * Tests exception thrown when the column parameter < 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumnException() {
        new WellBigInteger(0, 0);
    }
    
    /**
     * Tests exception thrown when the row parameter < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRowException() {
        new WellBigInteger(-1, 1);
    }
    
    /**
     * Tests the compare to method for big integer wells.
     */
    @Test
    public void testCompareTo() {  
    	
        WellBigInteger well1 = new WellBigInteger(0, 1); 
        WellBigInteger well2 = new WellBigInteger(0, 2);
        WellBigInteger well3 = new WellBigInteger(2, 1);
        
        WellBigInteger clone = new WellBigInteger(well1);
        
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
    	
    	List<BigInteger> bigIntegerList = RandomUtil.
    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);

        for(int i = 0; i < 100; i++) {

	    	List<BigInteger> clone = new ArrayList<BigInteger>(bigIntegerWell.data());
	    	bigIntegerWell.add(bigIntegerList.get(0));
	    	assertTrue(bigIntegerWell.data().size() == clone.size() + 1);
	    	
	    	clone.add(bigIntegerList.get(0));
	    	assertEquals(bigIntegerWell.data(), clone);
        }
    }
    
    /**
     * Tests the addition of a big integer collection.
     */
    @Test
    public void testAdditionCollection() {
    	
    	List<BigInteger> bigIntegerList = RandomUtil.
    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
    	
        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);

        for(int i = 0; i < 100; i++) {
 
	    	List<BigInteger> clone = new ArrayList<BigInteger>(bigIntegerWell.data());

	    	bigIntegerWell.add(bigIntegerList);
	    	assertTrue(bigIntegerWell.data().size() == clone.size() + bigIntegerList.size());
	    	
	    	clone.addAll(bigIntegerList);
	    	assertEquals(bigIntegerWell.data(), clone);
	    
    	}
    }
    
    /**
     * Tests addition of a big integer array.
     */
    @Test
    public void testAdditionArray() {
    	
    	List<BigInteger> bigIntegerList = RandomUtil.
    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);

        for(int i = 0; i < 100; i++) {
    		
	    	List<BigInteger> addBigInteger = RandomUtil.
	    			randomBigIntegerList(new BigInteger("" + 0), new BigInteger("" + 10000), 500, 1000);

	    	List<BigInteger> clone = new ArrayList<BigInteger>(bigIntegerWell.data());

	    	bigIntegerWell.add(addBigInteger.toArray(new BigInteger[addBigInteger.size()]));
	    	assertTrue(bigIntegerWell.data().size() == clone.size() + addBigInteger.size());
	    	
	    	clone.addAll(addBigInteger);
	    	assertEquals(bigIntegerWell.data(), clone);
    	}
    }
    
    /**
     * Tests addition of a big integer well.
     */
    @Test
    public void testAdditionWell() {
    	
    	List<BigInteger> bigIntegerList = RandomUtil.
    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
    	
        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);

        for(int i = 0; i < 100; i++) {

	    	List<BigInteger> clone = new ArrayList<BigInteger>(bigIntegerWell.data());

	    	WellBigInteger addWell = RandomUtil.randomWellBigInteger(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
	    	
	    	bigIntegerWell.add(addWell);

	    	assertEquals(bigIntegerWell.size(), clone.size() + addWell.size());
	    	
	    	clone.addAll(addWell.data());
	    	assertEquals(bigIntegerWell.data(), clone);

    	}
    }
    
    /**
     * Tests addition of a big integer well set.
     */
    @Test
    public void testAdditionSet() {
    	
    	List<BigInteger> bigIntegerList = RandomUtil.
    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
    	
        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);

    	PrintStream current = System.err;
    	
    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
    	System.setErr(dummy);
    	
        for(int i = 0; i < 10000; i++) {
        	
	    	List<BigInteger> clone = new ArrayList<BigInteger>(bigIntegerWell.data());
	    	
	    	WellSetBigInteger addSet = RandomUtil.randomWellSetBigInteger(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
	    	bigIntegerWell.add(addSet);
	    	
	    	ArrayList<BigInteger> setList = new ArrayList<BigInteger>();
	    	for(WellBigInteger well : addSet) {
	    		setList.addAll(well.data());
	    	}
	    	
	    	assertEquals(bigIntegerWell.size(), clone.size() + setList.size());
	    	
	    	clone.addAll(setList);
	    	assertEquals(bigIntegerWell.data(), clone);
    	}
        
        System.setErr(current);
    }
    
    /**
     * Tests for replacement of a single big integer.
     */
    @Test
    public void testReplacement() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	BigInteger toReplace = new BigInteger("" + random.nextInt());
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	
	        bigIntegerWell.replaceData(toReplace);
	        bigIntegerList.add(toReplace);
	        
	        assertTrue(bigIntegerWell.size() == 1);
	        assertEquals(bigIntegerWell.data().get(0), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of an array of big integers.
     */
    @Test
    public void testReplacementArray() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	List<BigInteger> toReplace = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	
	        bigIntegerWell.replaceData(toReplace.toArray(new BigInteger[toReplace.size()]));
	        
	        assertEquals(bigIntegerWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	List<BigInteger> toReplace = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	    	
	        bigIntegerWell.replaceData(toReplace);
	        
	        assertEquals(bigIntegerWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	List<BigInteger> replaceList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        WellBigInteger toReplace = this.randomWell(replaceList);
	
	        bigIntegerWell.replaceData(toReplace);
	        
	        assertEquals(bigIntegerWell.data(), toReplace.data());
    	}
    }
    
    /**
     * Tests for replacement of a big integer well set.
     */
    @Test
    public void testReplacementSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	    	  	
	    	PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetBigInteger toReplace = RandomUtil.randomWellSetBigInteger(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
		    bigIntegerWell.replaceData(toReplace);
		    	
		    ArrayList<BigInteger> setList = new ArrayList<BigInteger>();
		    for(WellBigInteger well : toReplace) {
		    	setList.addAll(well.data());
		    }
	
		   assertEquals(bigIntegerWell.data(), setList);
	        
	       System.setErr(current);
    	}
 
    }
    
    /**
     * Tests removal of a lone big integer.
     */
    @Test
    public void testRemoveArray() {

    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        Set<BigInteger> toRemove = new HashSet<BigInteger>();
	        
	        for(int j = 0; j < bigIntegerWell.size() / 2; j++) {
	        	int index = random.nextInt(bigIntegerList.size());
	        	toRemove.add(bigIntegerWell.data().get(index));
	        }
	
	        bigIntegerWell.remove(toRemove.toArray(new BigInteger[toRemove.size()]));
	        
	        for(BigInteger bd : toRemove) {
	        	assertFalse(bigIntegerWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests the contains method.
     */
    @Test
    public void testContains() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        for(BigInteger bd : bigIntegerList) {
	        	assertTrue(bigIntegerWell.contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a big integer array.
     */
    @Test
    public void testRemove() {
    	
    	for(int i = 0; i < 100; i++) {
    		
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);

	        int index = random.nextInt(bigIntegerList.size() + 1);
	        
	        BigInteger toRemove = bigIntegerWell.data().get(index);
	        bigIntegerWell.remove(toRemove);
	         
	        assertFalse(bigIntegerWell.data().contains(toRemove));
    	}
    }
    
    /**
     * Tests removal of a big integer collection.
     */
    @Test
    public void testRemoveCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        List<BigInteger> toRemove = new ArrayList<BigInteger>();
	        
	        for(int j =0; j < 100; j++) {

		        int index = random.nextInt(bigIntegerList.size());

	        	toRemove.add(bigIntegerWell.data().get(index));
	        }
	        
	        bigIntegerWell.remove(toRemove);
	        
	        for(BigInteger bd : toRemove) {
	        	assertFalse(bigIntegerWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well.
     */
    @Test
    public void testRemoveWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        List<BigInteger> toRemove = new ArrayList<BigInteger>(); 

        	for(int j = 0; j < 100; j++) {
		        int index = random.nextInt(bigIntegerList.size());
	        	toRemove.add(bigIntegerWell.data().get(index));
	        }
	        WellBigInteger toRemoveWell = 
	        		new WellBigInteger(bigIntegerWell.row(), bigIntegerWell.column(), toRemove);
	        bigIntegerWell.removeWell(toRemoveWell);
	        
	        for(BigInteger bd : toRemove) {
	        	assertFalse(bigIntegerWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well set.
     */
    @Test
    public void testRemoveSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetBigInteger set = new WellSetBigInteger();

	    	for(int j = 0; j < 10; j++) {

		    	int row = (int)(Math.random() * (maxRow) + 1);
		    	int column = 1 + (int)(Math.random() * (maxColumn - 1) + 1);

	    		WellBigInteger well = new WellBigInteger(row, column);
	    		
	            for(int k = 0; k < 100; k++) {
	            	int index = random.nextInt(bigIntegerList.size());
	        	    well.add(bigIntegerWell.data().get(index));
	            }
	            
	            set.add(well);
	    	}
	    
	    	bigIntegerWell.removeSet(set);
	        
	        for(WellBigInteger well : set) {
	        	for(BigInteger bd : well) {
	        	    assertFalse(bigIntegerWell.data().contains(bd));
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
	    	
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        List<BigInteger> checkEq = new ArrayList<BigInteger>(bigIntegerWell.data());
	        
	        int begin = (int)(Math.random() * ((bigIntegerList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin) + 1));

	        bigIntegerWell.removeRange(begin, end);
	
	        checkEq.subList(begin, end).clear();
	        assertEquals(checkEq, bigIntegerWell.data());
    	}
    }
    
    /**
     * Tests retention of a lone big integer.
     */
    @Test
    public void testRetain() {

    	for(int i = 0; i < 100; i++) {
	    	
    		List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        int index = random.nextInt(bigIntegerList.size() + 1);
	    	
	        BigInteger toRetain = bigIntegerWell.get(index);
	        bigIntegerWell.retain(toRetain);
	        
	        assertTrue(bigIntegerWell.size() == 1);
	        assertTrue(bigIntegerWell.get(0) == toRetain);
    	}
    }
    
    /**
     * Tests retention of a big integer collection.
     */
    @Test
    public void testRetainCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        int begin = (int)(Math.random() * ((bigIntegerList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin) + 1));
	        
	        List<BigInteger> toRetain = new ArrayList<BigInteger>(bigIntegerList.subList(begin, end));
	        bigIntegerWell.retain(toRetain);

	        for(BigInteger bd : bigIntegerWell) {
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
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        int begin = (int)(Math.random() * ((bigIntegerList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin) + 1));
	        
	        List<BigInteger> toRetainList = new ArrayList<BigInteger>(bigIntegerList.subList(begin, end));
	        BigInteger[] toRetainArray = toRetainList.toArray(new BigInteger[toRetainList.size()]);
	        bigIntegerWell.retain(toRetainArray);

	        for(BigInteger bd : bigIntegerWell) {
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
    		
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        int begin = 1 + (int)(Math.random() * ((bigIntegerList.size()) - 1));
	    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin)));
	        
	        List<BigInteger> toRetainList = new ArrayList<BigInteger>(bigIntegerList.subList(begin, end));
	        WellBigInteger toRetainWell = bigIntegerWell.subList(begin, end - begin); 
	        bigIntegerList.retainAll(toRetainList);
	        
	        bigIntegerWell.retainWell(toRetainWell);
	        
	        assertEquals(bigIntegerWell.data(), bigIntegerList);
    	}
    }    
    
    /**
     * Tests retention of a big integer well set.
     */
    @Test
    public void testRetainSet() {
    	    	
    	for(int j = 0; j < 100; j++) {
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	        
	        WellSetBigInteger toRetainSet = new WellSetBigInteger();
	        List<BigInteger> allValues = new ArrayList<BigInteger>();
	        
	        for(int i = 0; i < 5; i++) {
	        
	        	int begin = (int)(Math.random() * ((bigIntegerList.size()) + 1));
		    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin) + 1));
	        
	            List<BigInteger> toRetainList = new ArrayList<BigInteger>(bigIntegerList.subList(begin, end));
	            allValues.addAll(toRetainList);
	            WellBigInteger toRetainWell = new WellBigInteger(bigIntegerWell.row(), bigIntegerWell.column(), toRetainList); 
	            
	            toRetainSet.add(toRetainWell);
	        }

	        bigIntegerWell.retainSet(toRetainSet);
	
	        for(BigInteger bd : bigIntegerWell.data()) {
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
	    	List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	
	        int begin = (int)(Math.random() * ((bigIntegerList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin) + 1));
	
	        List<BigInteger> range = new ArrayList<BigInteger>(bigIntegerWell.data().subList(begin, end));
	        bigIntegerWell.retainRange(begin, end);
	
	        assertEquals(bigIntegerWell.data(), range);
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
    		
    		List<BigInteger> list = RandomUtil.randomBigIntegerList(BigInteger.ZERO, new BigInteger("" + 1000), 0, 1000);
    		WellBigInteger well = new WellBigInteger(row, column, list);
 
    		assertTrue(list.size() == well.size());
    	}
    }
    
    /**
     * Tests is empty method.
     */
    @Test
    public void testEmpty() {
    	
    	for(int i = 0; i < 100; i++) {
    	    
    		WellBigInteger well = new WellBigInteger(0,1);
    	    assertTrue(well.isEmpty());
    	
    	    well.add(BigInteger.ONE);
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
    		
    		List<BigInteger> bigIntegerList = RandomUtil.
        			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
        	
            WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
            
            int begin = (int)(Math.random() * ((bigIntegerList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((bigIntegerList.size() - begin) + 1));
	    	
    		begin = begin < end ? begin : end;
    		end = begin > end ? begin : end;

    		WellBigInteger sub = bigIntegerWell.subList(begin, end - begin);

    		assertEquals(sub.data(), bigIntegerWell.data().subList(begin, end));
    	}
    }
    
    /**
     * Tests last index of method.
     */
	@Test
    public void testLastIndexOf() {
    	
    	for(int i = 0; i < 100; i++) {
	    	BigInteger[] array = {BigInteger.ZERO, BigInteger.ONE, new BigInteger("" + 2)};
	    	WellBigInteger well = new WellBigInteger(0, 1);
	    	well.add(array);
	    	
	    	assertTrue(0 == well.lastIndexOf(BigInteger.ZERO));
	    	assertTrue(1 == well.lastIndexOf(BigInteger.ONE));
	    	assertTrue(2 == well.lastIndexOf(new BigInteger("" + 2)));
	    	
	    	well.add(array);
	    	
	    	assertTrue(3 == well.lastIndexOf(BigInteger.ZERO));
	    	assertTrue(4 == well.lastIndexOf(BigInteger.ONE));
	    	assertTrue(5 == well.lastIndexOf(new BigInteger("" + 2)));
	
	    	well.removeRange(3, 6);
	
	    	assertTrue(0 == well.lastIndexOf(BigInteger.ZERO));
	    	assertTrue(1 == well.lastIndexOf(BigInteger.ONE));
	    	assertTrue(2 == well.lastIndexOf(new BigInteger("" + 2)));
    	}
    }
    
    /**
     * Tests the index of method.
     */
    @Test
    public void testIndexOf() {

    	for(int i = 0; i < 100; i++) {
    		
    		List<BigInteger> bigIntegerList = RandomUtil.
	    			randomBigIntegerList(minValue, maxValue, minLength, maxLength);
    		
    		Set<BigInteger> set = new HashSet<BigInteger>(bigIntegerList);
    		bigIntegerList = new ArrayList<BigInteger>(set);
    		
	    	int index = random.nextInt(bigIntegerList.size());
	    	BigInteger value = bigIntegerList.get(index);
	    	
	        WellBigInteger bigIntegerWell = this.randomWell(bigIntegerList);
	    	assertTrue(index == bigIntegerWell.indexOf(value));
    	}
    }
    
    /**
     * Returns a random big integer well.
     */
    public WellBigInteger randomWell(List<BigInteger> bigIntegerList) {
    	
    	BigInteger[] bigIntegerArray = bigIntegerList.toArray(new BigInteger[bigIntegerList.size()]);
    	
    	int row = minRow + (int)(Math.random() * (maxRow + 1 - minRow) + 1);
    	int column = minColumn + (int)(Math.random() * (maxColumn + 1 + minColumn) + 1);
    	
    	return new WellBigInteger(row, column, bigIntegerArray);
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
