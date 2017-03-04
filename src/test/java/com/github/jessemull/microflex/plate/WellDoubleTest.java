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

import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.plate.Well;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This test case tests all double well methods and constructors.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 26, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class WellDoubleTest {

	/* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private double minValue = 0;
	private double maxValue = 10000;
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

	        WellDouble doubleWell = new WellDouble(row, column);

	        assertNotNull(doubleWell);
	        assertEquals(doubleWell.alphaBase(), 26);
	        assertEquals(doubleWell.row(), row);
	        assertEquals(doubleWell.column(), column);
	        assertEquals(doubleWell.rowString(), rowString);
	        assertEquals(doubleWell.type(), Well.DOUBLE);
	        assertEquals(doubleWell.index(), index);
	        assertEquals(doubleWell.typeString(), "Double");    
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

	        WellDouble doubleWell = new WellDouble(rowString, column);
	        
	        assertEquals(doubleWell.row(), row);
	        assertEquals(doubleWell.column(), column);
	        assertEquals(doubleWell.rowString(), rowString);
	        assertEquals(doubleWell.type(), Well.DOUBLE);
	        assertEquals(doubleWell.index(), index);
	        assertEquals(doubleWell.typeString(), "Double");
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
			
	        WellDouble doubleWell = new WellDouble(row, columnString);
	        
	        assertEquals(doubleWell.row(), row);
	        assertEquals(doubleWell.column(), column);
	        assertEquals(doubleWell.rowString(), rowString);
	        assertEquals(doubleWell.type(), Well.DOUBLE);
	        assertEquals(doubleWell.index(), index);
	        assertEquals(doubleWell.typeString(), "Double");
	        
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

	        WellDouble doubleWell = new WellDouble(rowString, columnString);

	        assertNotNull(doubleWell);
	        assertEquals(doubleWell.alphaBase(), 26);
	        assertEquals(doubleWell.row(), row);
	        assertEquals(doubleWell.column(), column);
	        assertEquals(doubleWell.rowString(), rowString);
	        assertEquals(doubleWell.type(), Well.DOUBLE);
	        assertEquals(doubleWell.index(), index);
	        assertEquals(doubleWell.typeString(), "Double");
	        
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
			
	        WellDouble doubleWell = new WellDouble(index);

	        assertNotNull(doubleWell);
	        assertEquals(doubleWell.alphaBase(), 26);	        
	        assertEquals(doubleWell.row(), row);
	        assertEquals(doubleWell.column(), column);
	        assertEquals(doubleWell.rowString(), rowString);
	        assertEquals(doubleWell.type(), Well.DOUBLE);
	        assertEquals(doubleWell.index(), index);
	        assertEquals(doubleWell.typeString(), "Double");
	        
		}
        
    }
    
    /**
     * Tests exception thrown when the column parameter < 1.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testColumnException() {
        new WellDouble(0, 0);
    }
    
    /**
     * Tests exception thrown when the row parameter < 0.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRowException() {
        new WellDouble(-1, 1);
    }
    
    /**
     * Tests the compare to method for big integer wells.
     */
    @Test
    public void testCompareTo() {  
    	
        WellDouble well1 = new WellDouble(0, 1); 
        WellDouble well2 = new WellDouble(0, 2);
        WellDouble well3 = new WellDouble(2, 1);
        
        WellDouble clone = new WellDouble(well1);
        
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
    	
    	List<Double> doubleList = RandomUtil.
    			randomDoubleList(minValue, maxValue, minLength, maxLength);
        WellDouble doubleWell = this.randomWell(doubleList);

        for(int i = 0; i < 100; i++) {

	    	List<Double> clone = new ArrayList<Double>(doubleWell.data());
	    	doubleWell.add(doubleList.get(0));
	    	assertTrue(doubleWell.data().size() == clone.size() + 1);
	    	
	    	clone.add(doubleList.get(0));
	    	assertEquals(doubleWell.data(), clone);
        }
    }
    
    /**
     * Tests the addition of a big integer collection.
     */
    @Test
    public void testAdditionCollection() {
    	
    	List<Double> doubleList = RandomUtil.
    			randomDoubleList(minValue, maxValue, minLength, maxLength);
    	
        WellDouble doubleWell = this.randomWell(doubleList);

        for(int i = 0; i < 100; i++) {
 
	    	List<Double> clone = new ArrayList<Double>(doubleWell.data());

	    	doubleWell.add(doubleList);
	    	assertTrue(doubleWell.data().size() == clone.size() + doubleList.size());
	    	
	    	clone.addAll(doubleList);
	    	assertEquals(doubleWell.data(), clone);
	    
    	}
    }
    
    /**
     * Tests addition of a big integer array.
     */
    @Test
    public void testAdditionArray() {
    	
    	List<Double> doubleList = RandomUtil.
    			randomDoubleList(minValue, maxValue, minLength, maxLength);
        WellDouble doubleWell = this.randomWell(doubleList);

        for(int i = 0; i < 100; i++) {
    		
	    	List<Double> addDouble = RandomUtil.
	    			randomDoubleList(new Double(0), new Double(10000), 500, 1000);

	    	List<Double> clone = new ArrayList<Double>(doubleWell.data());

	    	doubleWell.add(addDouble.toArray(new Double[addDouble.size()]));
	    	assertTrue(doubleWell.data().size() == clone.size() + addDouble.size());
	    	
	    	clone.addAll(addDouble);
	    	assertEquals(doubleWell.data(), clone);
    	}
    }
    
    /**
     * Tests addition of a big integer well.
     */
    @Test
    public void testAdditionWell() {
    	
    	List<Double> doubleList = RandomUtil.
    			randomDoubleList(minValue, maxValue, minLength, maxLength);
    	
        WellDouble doubleWell = this.randomWell(doubleList);

        for(int i = 0; i < 100; i++) {

	    	List<Double> clone = new ArrayList<Double>(doubleWell.data());

	    	WellDouble addWell = RandomUtil.randomWellDouble(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
	    	
	    	doubleWell.add(addWell);

	    	assertEquals(doubleWell.size(), clone.size() + addWell.size());
	    	
	    	clone.addAll(addWell.data());
	    	assertEquals(doubleWell.data(), clone);

    	}
    }
    
    /**
     * Tests addition of a big integer well set.
     */
    @Test
    public void testAdditionSet() {
    	
    	List<Double> doubleList = RandomUtil.
    			randomDoubleList(minValue, maxValue, minLength, maxLength);
    	
        WellDouble doubleWell = this.randomWell(doubleList);

    	PrintStream current = System.err;
    	
    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
    	System.setErr(dummy);
    	
        for(int i = 0; i < 10000; i++) {
        	
	    	List<Double> clone = new ArrayList<Double>(doubleWell.data());
	    	
	    	WellSetDouble addSet = RandomUtil.randomWellSetDouble(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
	    	doubleWell.add(addSet);
	    	
	    	ArrayList<Double> setList = new ArrayList<Double>();
	    	for(WellDouble well : addSet) {
	    		setList.addAll(well.data());
	    	}
	    	
	    	assertEquals(doubleWell.size(), clone.size() + setList.size());
	    	
	    	clone.addAll(setList);
	    	assertEquals(doubleWell.data(), clone);
    	}
        
        System.setErr(current);
    }
    
    /**
     * Tests for replacement of a single big integer.
     */
    @Test
    public void testReplacement() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	Double toReplace = new Double(Math.random());
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	
	        doubleWell.replaceData(toReplace);
	        doubleList.add(toReplace);
	        
	        assertTrue(doubleWell.size() == 1);
	        assertEquals(doubleWell.data().get(0), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of an array of big integers.
     */
    @Test
    public void testReplacementArray() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	List<Double> toReplace = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	
	        doubleWell.replaceData(toReplace.toArray(new Double[toReplace.size()]));
	        
	        assertEquals(doubleWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	List<Double> toReplace = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	    	
	        doubleWell.replaceData(toReplace);
	        
	        assertEquals(doubleWell.data(), toReplace);
    	}
    }
    
    /**
     * Tests for replacement of a collection of big integers.
     */
    @Test
    public void testReplacementWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	List<Double> replaceList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        WellDouble toReplace = this.randomWell(replaceList);
	
	        doubleWell.replaceData(toReplace);
	        
	        assertEquals(doubleWell.data(), toReplace.data());
    	}
    }
    
    /**
     * Tests for replacement of a big integer well set.
     */
    @Test
    public void testReplacementSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	    	  	
	    	PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetDouble toReplace = RandomUtil.randomWellSetDouble(minValue, 
	    			maxValue, minRow, maxRow, minColumn, maxColumn, 1, 5);
	    	
		    doubleWell.replaceData(toReplace);
		    	
		    ArrayList<Double> setList = new ArrayList<Double>();
		    for(WellDouble well : toReplace) {
		    	setList.addAll(well.data());
		    }
	
		   assertEquals(doubleWell.data(), setList);
	        
	       System.setErr(current);
    	}
 
    }
    
    /**
     * Tests removal of a lone big integer.
     */
    @Test
    public void testRemoveArray() {

    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        Set<Double> toRemove = new HashSet<Double>();
	        
	        for(int j = 0; j < doubleWell.size() / 2; j++) {
	        	int index = random.nextInt(doubleList.size());
	        	toRemove.add(doubleWell.data().get(index));
	        }
	
	        doubleWell.remove(toRemove.toArray(new Double[toRemove.size()]));
	        
	        for(Double bd : toRemove) {
	        	assertFalse(doubleWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests the contains method.
     */
    @Test
    public void testContains() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        for(Double bd : doubleList) {
	        	assertTrue(doubleWell.contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a big integer array.
     */
    @Test
    public void testRemove() {
    	
    	for(int i = 0; i < 100; i++) {
    		
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);

	        int index = random.nextInt(doubleList.size());
	        
	        Double toRemove = doubleWell.data().get(index);
	        doubleWell.remove(toRemove);
	         
	        assertFalse(doubleWell.data().contains(toRemove));
    	}
    }
    
    /**
     * Tests removal of a big integer collection.
     */
    @Test
    public void testRemoveCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        List<Double> toRemove = new ArrayList<Double>();
	        
	        for(int j =0; j < 100; j++) {

		        int index = random.nextInt(doubleList.size());

	        	toRemove.add(doubleWell.data().get(index));
	        }
	        
	        doubleWell.remove(toRemove);
	        
	        for(Double bd : toRemove) {
	        	assertFalse(doubleWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well.
     */
    @Test
    public void testRemoveWell() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        List<Double> toRemove = new ArrayList<Double>(); 

        	for(int j = 0; j < 100; j++) {
		        int index = random.nextInt(doubleList.size());
	        	toRemove.add(doubleWell.data().get(index));
	        }
	        WellDouble toRemoveWell = 
	        		new WellDouble(doubleWell.row(), doubleWell.column(), toRemove);
	        doubleWell.removeWell(toRemoveWell);
	        
	        for(Double bd : toRemove) {
	        	assertFalse(doubleWell.data().contains(bd));
	        }
    	}
    }
    
    /**
     * Tests removal of a well set.
     */
    @Test
    public void testRemoveSet() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        PrintStream current = System.err;
	    	
	    	PrintStream dummy = new PrintStream(ByteStreams.nullOutputStream());
	    	System.setErr(dummy);
	    	
	    	WellSetDouble set = new WellSetDouble();

	    	for(int j = 0; j < 10; j++) {

		    	int row = (int)(Math.random() * (maxRow) + 1);
		    	int column = 1 + (int)(Math.random() * (maxColumn - 1) + 1);

	    		WellDouble well = new WellDouble(row, column);
	    		
	            for(int k = 0; k < 100; k++) {
	            	int index = random.nextInt(doubleList.size());
	        	    well.add(doubleWell.data().get(index));
	            }
	            
	            set.add(well);
	    	}
	    
	    	doubleWell.removeSet(set);
	        
	        for(WellDouble well : set) {
	        	for(Double bd : well) {
	        	    assertFalse(doubleWell.data().contains(bd));
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
	    	
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        List<Double> checkEq = new ArrayList<Double>(doubleWell.data());
	        
	        int begin = (int)(Math.random() * ((doubleList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin) + 1));

	        doubleWell.removeRange(begin, end);
	
	        checkEq.subList(begin, end).clear();
	        assertEquals(checkEq, doubleWell.data());
    	}
    }
    
    /**
     * Tests retention of a lone big integer.
     */
    @Test
    public void testRetain() {

    	for(int i = 0; i < 100; i++) {
	    	
    		List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
    		Set<Double> set = new HashSet<Double>(doubleList);
    		doubleList = new ArrayList<Double>(set);
    		
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        int index = random.nextInt(doubleList.size());
	    	
	        Double toRetain = doubleWell.get(index);
	        doubleWell.retain(toRetain);
	        
	        Double returned = doubleWell.get(0);
	        
	        assertEquals(returned, toRetain);
	        assertTrue(doubleWell.size() == 1);
	        
    	}
    }
    
    /**
     * Tests retention of a big integer collection.
     */
    @Test
    public void testRetainCollection() {
    	
    	for(int i = 0; i < 100; i++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        int begin = (int)(Math.random() * ((doubleList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin) + 1));
	        
	        List<Double> toRetain = new ArrayList<Double>(doubleList.subList(begin, end));
	        doubleWell.retain(toRetain);

	        for(Double bd : doubleWell) {
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
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        int begin = (int)(Math.random() * ((doubleList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin) + 1));
	        
	        List<Double> toRetainList = new ArrayList<Double>(doubleList.subList(begin, end));
	        Double[] toRetainArray = toRetainList.toArray(new Double[toRetainList.size()]);
	        doubleWell.retain(toRetainArray);

	        for(Double bd : doubleWell) {
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
    		
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        int begin = 1 + (int)(Math.random() * ((doubleList.size()) - 1));
	    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin)));
	        
	        List<Double> toRetainList = new ArrayList<Double>(doubleList.subList(begin, end));
	        WellDouble toRetainWell = doubleWell.subList(begin, end - begin); 
	        doubleList.retainAll(toRetainList);
	        
	        doubleWell.retainWell(toRetainWell);
	        
	        assertEquals(doubleWell.data(), doubleList);
    	}
    }    
    
    /**
     * Tests retention of a big integer well set.
     */
    @Test
    public void testRetainSet() {
    	    	
    	for(int j = 0; j < 100; j++) {
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	        
	        WellSetDouble toRetainSet = new WellSetDouble();
	        List<Double> allValues = new ArrayList<Double>();
	        
	        for(int i = 0; i < 5; i++) {
	        
	        	int begin = (int)(Math.random() * ((doubleList.size()) + 1));
		    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin) + 1));
	        
	            List<Double> toRetainList = new ArrayList<Double>(doubleList.subList(begin, end));
	            allValues.addAll(toRetainList);
	            WellDouble toRetainWell = new WellDouble(doubleWell.row(), doubleWell.column(), toRetainList); 
	            
	            toRetainSet.add(toRetainWell);
	        }

	        doubleWell.retainSet(toRetainSet);
	
	        for(Double bd : doubleWell.data()) {
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
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	
	        int begin = (int)(Math.random() * ((doubleList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin) + 1));
	
	        List<Double> range = new ArrayList<Double>(doubleWell.data().subList(begin, end));
	        doubleWell.retainRange(begin, end);
	
	        assertEquals(doubleWell.data(), range);
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
    		
    		List<Double> list = RandomUtil.randomDoubleList(0, 1000, 0, 1000);
    		WellDouble well = new WellDouble(row, column, list);
 
    		assertTrue(list.size() == well.size());
    	}
    }
    
    /**
     * Tests is empty method.
     */
    @Test
    public void testEmpty() {
    	
    	for(int i = 0; i < 100; i++) {
    	    
    		WellDouble well = new WellDouble(0,1);
    	    assertTrue(well.isEmpty());
    	
    	    well.add(new Double(1));
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
    		
    		List<Double> doubleList = RandomUtil.
        			randomDoubleList(minValue, maxValue, minLength, maxLength);
        	
            WellDouble doubleWell = this.randomWell(doubleList);
            
            int begin = (int)(Math.random() * ((doubleList.size()) + 1));
	    	int end = begin + (int)(Math.random() * ((doubleList.size() - begin) + 1));
	    	
    		begin = begin < end ? begin : end;
    		end = begin > end ? begin : end;

    		WellDouble sub = doubleWell.subList(begin, end - begin);

    		assertEquals(sub.data(), doubleWell.data().subList(begin, end));
    	}
    }
    
    /**
     * Tests last index of method.
     */
	@Test
    public void testLastIndexOf() {
    	
    	for(int i = 0; i < 100; i++) {
	    	Double[] array = {new Double(0), new Double(1), new Double(2)};
	    	WellDouble well = new WellDouble(0, 1);
	    	well.add(array);
	    	
	    	assertTrue(0 == well.lastIndexOf(new Double(0)));
	    	assertTrue(1 == well.lastIndexOf(new Double(1)));
	    	assertTrue(2 == well.lastIndexOf(new Double(2)));
	    	
	    	well.add(array);
	    	
	    	assertTrue(3 == well.lastIndexOf(new Double(0)));
	    	assertTrue(4 == well.lastIndexOf(new Double(1)));
	    	assertTrue(5 == well.lastIndexOf(new Double(2)));
	
	    	well.removeRange(3, 6);
	
	    	assertTrue(0 == well.lastIndexOf(new Double(0)));
	    	assertTrue(1 == well.lastIndexOf(new Double(1)));
	    	assertTrue(2 == well.lastIndexOf(new Double(2)));
    	}
    }
    
    /**
     * Tests the index of method.
     */
    @Test
    public void testIndexOf() {

    	for(int i = 0; i < 100; i++) {
    		
	    	List<Double> doubleList = RandomUtil.
	    			randomDoubleList(minValue, maxValue, minLength, maxLength);
	    	
	    	Set<Double> set = new HashSet<Double>(doubleList);
    		doubleList = new ArrayList<Double>(set);
    		
	    	int index = random.nextInt(doubleList.size());
	    	Double value = doubleList.get(index);
	    	
	        WellDouble doubleWell = this.randomWell(doubleList);
	    	assertTrue(index == doubleWell.indexOf(value));
    	}
    }
    
    /**
     * Returns a random big integer well.
     */
    public WellDouble randomWell(List<Double> doubleList) {
    	
    	Double[] doubleArray = doubleList.toArray(new Double[doubleList.size()]);
    	
    	int row = minRow + (int)(Math.random() * (maxRow + 1 - minRow) + 1);
    	int column = minColumn + (int)(Math.random() * (maxColumn + 1 + minColumn) + 1);
    	
    	return new WellDouble(row, column, doubleArray);
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
