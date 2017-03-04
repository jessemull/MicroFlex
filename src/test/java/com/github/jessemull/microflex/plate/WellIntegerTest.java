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

import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.plate.Well;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This test case tests all integer well methods and constructors.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 26, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellIntegerTest {

	/* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private int minValue = 0;
	private int maxValue = 10000;
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

	        WellInteger intWell = new WellInteger(row, column);

	        assertNotNull(intWell);
	        assertEquals(intWell.alphaBase(), 26);
	        assertEquals(intWell.row(), row);
	        assertEquals(intWell.column(), column);
	        assertEquals(intWell.rowString(), rowString);
	        assertEquals(intWell.type(), Well.INTEGER);
	        assertEquals(intWell.index(), index);
	        assertEquals(intWell.typeString(), "Integer");    
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

	        WellInteger intWell = new WellInteger(rowString, column);
	        
	        assertEquals(intWell.row(), row);
	        assertEquals(intWell.column(), column);
	        assertEquals(intWell.rowString(), rowString);
	        assertEquals(intWell.type(), Well.INTEGER);
	        assertEquals(intWell.index(), index);
	        assertEquals(intWell.typeString(), "Integer");
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
			
	        WellInteger intWell = new WellInteger(row, columnString);
	        
	        assertEquals(intWell.row(), row);
	        assertEquals(intWell.column(), column);
	        assertEquals(intWell.rowString(), rowString);
	        assertEquals(intWell.type(), Well.INTEGER);
	        assertEquals(intWell.index(), index);
	        assertEquals(intWell.typeString(), "Integer");
	        
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

	        WellInteger intWell = new WellInteger(rowString, columnString);

	        assertNotNull(intWell);
	        assertEquals(intWell.alphaBase(), 26);
	        assertEquals(intWell.row(), row);
	        assertEquals(intWell.column(), column);
	        assertEquals(intWell.rowString(), rowString);
	        assertEquals(intWell.type(), Well.INTEGER);
	        assertEquals(intWell.index(), index);
	        assertEquals(intWell.typeString(), "Integer");
	        
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
			
	        WellInteger intWell = new WellInteger(index);

	        assertNotNull(intWell);
	        assertEquals(intWell.alphaBase(), 26);	        
	        assertEquals(intWell.row(), row);
	        assertEquals(intWell.column(), column);
	        assertEquals(intWell.rowString(), rowString);
	        assertEquals(intWell.type(), Well.INTEGER);
	        assertEquals(intWell.index(), index);
	        assertEquals(intWell.typeString(), "Integer");
	        
		}
        
    }
    
    /**
     * Tests exception thrown when the column parameter < 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumnException() {
        new WellInteger(0, 0);
    }
    
    /**
     * Tests exception thrown when the row parameter < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRowException() {
        new WellInteger(-1, 1);
    }
    
    /**
     * Tests the compare to method for big integer wells.
     */
    @Test
    public void testCompareTo() {  
    	
        WellInteger well1 = new WellInteger(0, 1); 
        WellInteger well2 = new WellInteger(0, 2);
        WellInteger well3 = new WellInteger(2, 1);
        
        WellInteger clone = new WellInteger(well1);
        
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
    	
    	List<Integer> intList = RandomUtil.
    			randomIntegerList(minValue, maxValue, minLength, maxLength);
        WellInteger intWell = this.randomWell(intList);

        for(int i = 0; i < 100; i++) {

	    	List<Integer> clone = new ArrayList<Integer>(intWell.data());
	    	intWell.add(intList.get(0));
	    	assertTrue(intWell.data().size() == clone.size() + 1);
	    	
	    	clone.add(intList.get(0));
	    	assertEquals(intWell.data(), clone);
        }
    }
    
    /**
     * Tests the addition of a big integer collection.
     */
    @Test
    public void testAdditionCollection() {
    	
    	List<Integer> intList = RandomUtil.
    			randomIntegerList(minValue, maxValue, minLength, maxLength);
    	
        WellInteger intWell = this.randomWell(intList);

        for(int i = 0; i < 100; i++) {
 
	    	List<Integer> clone = new ArrayList<Integer>(intWell.data());

	    	intWell.add(intList);
	    	assertTrue(intWell.data().size() == clone.size() + intList.size());
	    	
	    	clone.addAll(intList);
	    	assertEquals(intWell.data(), clone);
	    
    	}
    }
    
    /**
     * Tests addition of a big integer array.
     */
    @Test
    public void testAdditionArray() {
    	
    	List<Integer> intList = RandomUtil.
    			randomIntegerList(minValue, maxValue, minLength, maxLength);
        WellInteger intWell = this.randomWell(intList);

        for(int i = 0; i < 100; i++) {
    		
	    	List<Integer> addInteger = RandomUtil.
	    			randomIntegerList(new Integer(0), new Integer(10000), 500, 1000);

	    	List<Integer> clone = new ArrayList<Integer>(intWell.data());

	    	intWell.add(addInteger.toArray(new Integer[addInteger.size()]));
	    	assertTrue(intWell.data().size() == clone.size() + addInteger.size());
	    	
	    	clone.addAll(addInteger);
	    	assertEquals(intWell.data(), clone);
    	}
    }
    
    /**
     * Tests addition of a big integer well.
     */
    @Test
    public void testAdditionWell() {
    	
    	List<Integer> intList = RandomUtil.
    			randomIntegerList(minValue, maxValue, minLength, maxLength);
    	
        WellInteger intWell = this.randomWell(intList);

        for(int i = 0; i < 100; i++) {

	    	List<Integer> clone = new ArrayList<Integer>(intWell.data());

	    	WellInteger addWell = RandomUtil.randomWellInteger(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
	    	
	    	intWell.add(addWell);

	    	assertEquals(intWell.size(), clone.size() + addWell.size());
	    	
	    	clone.addAll(addWell.data());
	    	assertEquals(intWell.data(), clone);

    	}
    }
    
    /**
     * Tests addition of a big integer well set.
     */
    @Test
    public void testAdditionSet() {
    	
    	List<Integer> intList = RandomUtil.
    			randomIntegerList(minValue, maxValue, minLength, maxLength);
    	
        WellInteger intWell = this.randomWell(intList);

    	PrintStream current = System.err;
    	
    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
    	System.setErr(dummy);
    	
        for(int i = 0; i < 10000; i++) {
        	
	    	List<Integer> clone = new ArrayList<Integer>(intWell.data());
	    	
	    	WellSetInteger addSet = RandomUtil.randomWellSetInteger(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
	    	intWell.add(addSet);
	    	
	    	ArrayList<Integer> setList = new ArrayList<Integer>();
	    	for(WellInteger well : addSet) {
	    		setList.addAll(well.data());
	    	}
	    	
	    	assertEquals(intWell.size(), clone.size() + setList.size());
	    	
	    	clone.addAll(setList);
	    	assertEquals(intWell.data(), clone);
    	}
        
        System.setErr(current);
    }
    
    /**
     * Tests for replacement of a single big integer.
     */
    @Test
    public void testReplacement() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	Integer toReplace = new Integer(random.nextInt());
	    	
	        WellInteger intWell = this.randomWell(intList);
	
	        intWell.replaceData(toReplace);
	        intList.add(toReplace);
	        
	        assertTrue(intWell.size() == 1);
	        assertEquals(intWell.data().get(0), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of an array of big integers.
     */
    @Test
    public void testReplacementArray() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	List<Integer> toReplace = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	
	        intWell.replaceData(toReplace.toArray(new Integer[toReplace.size()]));
	        
	        assertEquals(intWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	List<Integer> toReplace = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	    	
	        intWell.replaceData(toReplace);
	        
	        assertEquals(intWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	List<Integer> replaceList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        WellInteger toReplace = this.randomWell(replaceList);
	
	        intWell.replaceData(toReplace);
	        
	        assertEquals(intWell.data(), toReplace.data());
    	}
    }
    
    /**
     * Tests for replacement of a big integer well set.
     */
    @Test
    public void testReplacementSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	    	  	
	    	PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetInteger toReplace = RandomUtil.randomWellSetInteger(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
		    intWell.replaceData(toReplace);
		    	
		    ArrayList<Integer> setList = new ArrayList<Integer>();
		    for(WellInteger well : toReplace) {
		    	setList.addAll(well.data());
		    }
	
		   assertEquals(intWell.data(), setList);
	        
	       System.setErr(current);
    	}
 
    }
    
    /**
     * Tests removal of a lone big integer.
     */
    @Test
    public void testRemoveArray() {

    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        Set<Integer> toRemove = new HashSet<Integer>();
	        
	        for(int j = 0; j < intWell.size() / 2; j++) {
	        	int index = random.nextInt(intList.size());
	        	toRemove.add(intWell.data().get(index));
	        }
	
	        intWell.remove(toRemove.toArray(new Integer[toRemove.size()]));
	        
	        for(Integer bd : toRemove) {
	        	assertFalse(intWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests the contains method.
     */
    @Test
    public void testContains() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        
	        for(Integer bd : intList) {
	        	assertTrue(intWell.contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a big integer array.
     */
    @Test
    public void testRemove() {
    	
    	for(int i = 0; i < 100; i++) {
    		
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);

	        int index = random.nextInt(intList.size());
	        
	        Integer toRemove = intWell.data().get(index);
	        intWell.remove(toRemove);
	         
	        assertFalse(intWell.data().contains(toRemove));
    	}
    }
    
    /**
     * Tests removal of a big integer collection.
     */
    @Test
    public void testRemoveCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        List<Integer> toRemove = new ArrayList<Integer>();
	        
	        for(int j =0; j < 100; j++) {

		        int index = random.nextInt(intList.size());

	        	toRemove.add(intWell.data().get(index));
	        }
	        
	        intWell.remove(toRemove);
	        
	        for(Integer bd : toRemove) {
	        	assertFalse(intWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well.
     */
    @Test
    public void testRemoveWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        List<Integer> toRemove = new ArrayList<Integer>(); 

        	for(int j = 0; j < 100; j++) {
		        int index = random.nextInt(intList.size());
	        	toRemove.add(intWell.data().get(index));
	        }
	        WellInteger toRemoveWell = 
	        		new WellInteger(intWell.row(), intWell.column(), toRemove);
	        intWell.removeWell(toRemoveWell);
	        
	        for(Integer bd : toRemove) {
	        	assertFalse(intWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well set.
     */
    @Test
    public void testRemoveSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        
	        PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetInteger set = new WellSetInteger();

	    	for(int j = 0; j < 10; j++) {

		    	int row = (int)(Math.random() * (maxRow) + 1);
		    	int column = 1 + (int)(Math.random() * (maxColumn - 1) + 1);

	    		WellInteger well = new WellInteger(row, column);
	    		
	            for(int k = 0; k < 100; k++) {
	            	int index = random.nextInt(intList.size());
	        	    well.add(intWell.data().get(index));
	            }
	            
	            set.add(well);
	    	}
	    
	    	intWell.removeSet(set);
	        
	        for(WellInteger well : set) {
	        	for(Integer bd : well) {
	        	    assertFalse(intWell.data().contains(bd));
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
	    	
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        List<Integer> checkEq = new ArrayList<Integer>(intWell.data());
	        
	        int begin = (int)(Math.random() * ((intList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((intList.size() - begin) + 1));

	        intWell.removeRange(begin, end);
	
	        checkEq.subList(begin, end).clear();
	        assertEquals(checkEq, intWell.data());
    	}
    }
    
    /**
     * Tests retention of a lone big integer.
     */
    @Test
    public void testRetain() {

    	for(int i = 0; i < 100; i++) {
	    	
    		List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, 10, 20);
    		Set<Integer> set = new HashSet<Integer>(intList);
    		intList = new ArrayList<Integer>(set);
    		
	        WellInteger intWell = this.randomWell(intList);
	        
	        int index = random.nextInt(intList.size());

	        Integer toRetain = intWell.get(index);
	        intWell.retain(toRetain);

	        assertTrue(intWell.size() == 1);
	        assertEquals(intWell.get(0), toRetain);
    	}
    }
    
    /**
     * Tests retention of a big integer collection.
     */
    @Test
    public void testRetainCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        
	        int begin = (int)(Math.random() * ((intList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((intList.size() - begin) + 1));
	        
	        List<Integer> toRetain = new ArrayList<Integer>(intList.subList(begin, end));
	        intWell.retain(toRetain);

	        for(Integer bd : intWell) {
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
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        
	        int begin = (int)(Math.random() * ((intList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((intList.size() - begin) + 1));
	        
	        List<Integer> toRetainList = new ArrayList<Integer>(intList.subList(begin, end));
	        Integer[] toRetainArray = toRetainList.toArray(new Integer[toRetainList.size()]);
	        intWell.retain(toRetainArray);

	        for(Integer bd : intWell) {
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
    		
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        
	        int begin = 1 + (int)(Math.random() * ((intList.size()) - 1));
	    	int end = begin + (int)(Math.random() * ((intList.size() - begin)));

	        List<Integer> toRetainList = new ArrayList<Integer>(intList.subList(begin, end));
	        WellInteger toRetainWell = intWell.subList(begin, end - begin); 
	        intList.retainAll(toRetainList);
	        
	        intWell.retainWell(toRetainWell);
	        
	        assertEquals(intWell.data(), intList);
    	}
    }    
    
    /**
     * Tests retention of a big integer well set.
     */
    @Test
    public void testRetainSet() {
    	    	
    	for(int j = 0; j < 100; j++) {
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	        
	        WellSetInteger toRetainSet = new WellSetInteger();
	        List<Integer> allValues = new ArrayList<Integer>();
	        
	        for(int i = 0; i < 5; i++) {
	        
	        	int begin = (int)(Math.random() * ((intList.size()) + 1));
		    	int end = begin + (int)(Math.random() * ((intList.size() - begin) + 1));
	        
	            List<Integer> toRetainList = new ArrayList<Integer>(intList.subList(begin, end));
	            allValues.addAll(toRetainList);
	            WellInteger toRetainWell = new WellInteger(intWell.row(), intWell.column(), toRetainList); 
	            
	            toRetainSet.add(toRetainWell);
	        }

	        intWell.retainSet(toRetainSet);
	
	        for(Integer bd : intWell.data()) {
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
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	        WellInteger intWell = this.randomWell(intList);
	
	        int begin = (int)(Math.random() * ((intList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((intList.size() - begin) + 1));
	
	        List<Integer> range = new ArrayList<Integer>(intWell.data().subList(begin, end));
	        intWell.retainRange(begin, end);
	
	        assertEquals(intWell.data(), range);
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
    		
    		List<Integer> list = RandomUtil.randomIntegerList(0, 1000, 0, 1000);
    		WellInteger well = new WellInteger(row, column, list);
 
    		assertTrue(list.size() == well.size());
    	}
    }
    
    /**
     * Tests is empty method.
     */
    @Test
    public void testEmpty() {
    	
    	for(int i = 0; i < 100; i++) {
    	    
    		WellInteger well = new WellInteger(0,1);
    	    assertTrue(well.isEmpty());
    	
    	    well.add(new Integer(1));
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
    		
    		List<Integer> intList = RandomUtil.
        			randomIntegerList(minValue, maxValue, minLength, maxLength);
        	
            WellInteger intWell = this.randomWell(intList);
            
            int begin = (int)(Math.random() * ((intList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((intList.size() - begin) + 1));
	    	
    		begin = begin < end ? begin : end;
    		end = begin > end ? begin : end;

    		WellInteger sub = intWell.subList(begin, end - begin);

    		assertEquals(sub.data(), intWell.data().subList(begin, end));
    	}
    }
    
    /**
     * Tests last index of method.
     */
	@Test
    public void testLastIndexOf() {
    	
    	for(int i = 0; i < 100; i++) {
	    	Integer[] array = {new Integer(0), new Integer(1), new Integer(2)};
	    	WellInteger well = new WellInteger(0, 1);
	    	well.add(array);
	    	
	    	assertTrue(0 == well.lastIndexOf(new Integer(0)));
	    	assertTrue(1 == well.lastIndexOf(new Integer(1)));
	    	assertTrue(2 == well.lastIndexOf(new Integer(2)));
	    	
	    	well.add(array);
	    	
	    	assertTrue(3 == well.lastIndexOf(new Integer(0)));
	    	assertTrue(4 == well.lastIndexOf(new Integer(1)));
	    	assertTrue(5 == well.lastIndexOf(new Integer(2)));
	
	    	well.removeRange(3, 6);
	
	    	assertTrue(0 == well.lastIndexOf(new Integer(0)));
	    	assertTrue(1 == well.lastIndexOf(new Integer(1)));
	    	assertTrue(2 == well.lastIndexOf(new Integer(2)));
    	}
    }
    
    /**
     * Tests the index of method.
     */
    @Test
    public void testIndexOf() {

    	for(int i = 0; i < 100; i++) {
    		
	    	List<Integer> intList = RandomUtil.
	    			randomIntegerList(minValue, maxValue, minLength, maxLength);
	    	
	    	Set<Integer> set = new HashSet<Integer>(intList);
    		intList = new ArrayList<Integer>(set);
    		
	    	int index = random.nextInt(intList.size());
	    	Integer value = intList.get(index);
	    	
	        WellInteger intWell = this.randomWell(intList);
	    	assertTrue(index == intWell.indexOf(value));
    	}
    }
    
    /**
     * Returns a random big integer well.
     */
    public WellInteger randomWell(List<Integer> intList) {
    	
    	Integer[] intArray = intList.toArray(new Integer[intList.size()]);
    	
    	int row = minRow + (int)(Math.random() * (maxRow + 1 - minRow) + 1);
    	int column = minColumn + (int)(Math.random() * (maxColumn + 1 + minColumn) + 1);
    	
    	return new WellInteger(row, column, intArray);
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
