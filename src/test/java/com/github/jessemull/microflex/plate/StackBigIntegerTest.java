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

/* --------------------------- Package declaration -------------------------- */

package com.github.jessemull.microflex.plate;

/* ------------------------------ Dependencies ------------------------------ */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the big integer stack plate 
 * class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StackBigIntegerTest {

	/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private BigInteger minValue = new BigInteger(-1000000000 + "");   // Minimum big integer value for wells
	private BigInteger maxValue = new BigInteger(1000000000 + "");    // Maximum big integer value for wells
	private int minLength = 100;                                      // Minimum well set length
	private int maxLength = 1000;                                     // Maximum well set length
	private int groupNumber = 0;                                      // Group number
	private int minRow = 0;									          // Minimum row number
	private int minColumn = 1;									      // Minimum column number
	private int minPlate = 10;                                        // Plate minimum
	private int maxPlate = 25;                                        // Plate maximum
	private Random random = new Random();                             // Generates random integers
	
	/* Random objects and numbers for testing */

	private int rows;
	private int columns;
	private int length;
	private int plateNumber;
	private StackBigInteger testStack;
	private List<PlateBigInteger[]> arrays = new ArrayList<PlateBigInteger[]>();
	private List<TreeSet<PlateBigInteger>> collections = new ArrayList<TreeSet<PlateBigInteger>>();
	private List<String> descriptors = new ArrayList<String>();
	
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
	
	/**
	 * Generates random objects and numbers for testing.
	 */
	@Before
	public void setUp() {
		
		rows = PlateBigInteger.ROWS_48WELL + random.nextInt(PlateBigInteger.ROWS_1536WELL - 
	           PlateBigInteger.ROWS_48WELL + 1);

		columns =  PlateBigInteger.COLUMNS_48WELL + random.nextInt(PlateBigInteger.COLUMNS_1536WELL - 
		           PlateBigInteger.COLUMNS_48WELL + 1);
		
		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
		
    	length = rows * columns / 5;
    	
    	testStack = RandomUtil.randomStackBigInteger(rows, columns, minValue, 
    			maxValue, minLength, maxLength, 0, length, "TestStack", plateNumber);
    	
    	for(int i = 0; i < 25; i++) {
    		
    		TreeSet<PlateBigInteger> list = new TreeSet<PlateBigInteger>();
    		PlateBigInteger[] array = new PlateBigInteger[20];
    		
    		for(int j = 0; j < 20; j++) {
    			PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
            			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "PlateBigInteger" + j);
    			list.add(plate);
    			array[j] = plate;
    		}
    		
    		this.getDescriptor(rows, columns);
    		this.collections.add(list);
    		this.arrays.add(array);
    	}
	}
	
    /* -------------------------- Tests constructors ------------------------ */
    
    /**
     * Tests the constructor using a type constant.
     */
	@Test
    public void testConstructorType() {

    	StackBigInteger stack = new StackBigInteger(PlateBigInteger.PLATE_96WELL);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), PlateBigInteger.ROWS_96WELL);
        assertEquals(stack.columns(), PlateBigInteger.COLUMNS_96WELL);      
        assertEquals(stack.type(), PlateBigInteger.PLATE_96WELL);
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), "96-Well");
        assertEquals(stack.label(), "StackBigInteger");
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
    	
    }
    
    /**
     * Tests the constructor using a row and a column.
     */
	@Test
    public void testConstructorRowColumn() {

    	if(this.checkType(rows, columns)) {
    		rows++;
    		columns++;
    	}
    	
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	StackBigInteger stack = new StackBigInteger(rows, columns);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackBigInteger");
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a type constant and label.
     */
	@Test
    public void testConstructorTypeLabel() {
    	
    	String label = "TestLabel";
    	
    	StackBigInteger stack = new StackBigInteger(StackBigInteger.PLATE_384WELL, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), StackBigInteger.ROWS_384WELL);
        assertEquals(stack.columns(), StackBigInteger.COLUMNS_384WELL);      
        assertEquals(stack.type(), StackBigInteger.PLATE_384WELL);
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), "384-Well");
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }

    /**
     * Tests the constructor using a row, column and label.
     */
	@Test
    public void testConstructorRowColumnLabel() {

    	String label = "TestLabel";
    
    	if(this.checkType(rows, columns)) {
    		rows++;
    		columns++;
    	}
    	
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	StackBigInteger stack = new StackBigInteger(rows, columns, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate.
     */
	@Test
    public void testConstructorStackPlate() {
		
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "StackBigInteger");
    	
    	StackBigInteger stack = new StackBigInteger(plate);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackBigInteger");
        assertEquals(stack.size(), 1);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate and label.
     */
	@Test
    public void testConstructorPlateLabel() {

    	String label = "TestLabel";
    	String descriptor = "Custom Stack: " + rows + "x" + columns;
    	
    	PlateBigInteger plate = RandomUtil.randomPlateBigInteger(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, label);
    	
    	StackBigInteger stack = new StackBigInteger(plate, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), 1);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate collection.
     */
	@Test
    public void testConstructorCollection() {
    	
    	StackBigInteger stack = new StackBigInteger(collections.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackBigInteger");
        assertEquals(stack.size(), collections.get(0).size());
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate collection and label.
     */
    @Test
    public void testConstructorCollectionLabel() {

    	String label = "TestLabel";
    	
    	StackBigInteger stack = new StackBigInteger(collections.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), collections.get(0).size());
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate array.
     */
    @Test
    public void testConstructorArray() {
    
    	StackBigInteger stack = new StackBigInteger(arrays.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackBigInteger");
        assertEquals(stack.size(), arrays.get(0).length);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate array and a label.
     */
    @Test
    public void testConstructorArrayLabel() {

    	String label = "TestLabel";
    	
    	StackBigInteger stack = new StackBigInteger(arrays.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellBigInteger.BIGINTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), label);
        assertEquals(stack.size(), arrays.get(0).length);
        assertFalse(stack.getAll().isEmpty());
    }
    
    /**
     * Tests the parse dimensions constructor helper method.
     */ 
    @Test
    public void testParseDimensions() {
    	
    	StackBigInteger sixWell = new StackBigInteger(StackBigInteger.PLATE_6WELL);
    	StackBigInteger twelveWell = new StackBigInteger(StackBigInteger.PLATE_12WELL);
    	StackBigInteger twentyFourWell = new StackBigInteger(StackBigInteger.PLATE_24WELL);
    	StackBigInteger fortyEightWell = new StackBigInteger(StackBigInteger.PLATE_48WELL);
    	StackBigInteger ninetySixWell = new StackBigInteger(StackBigInteger.PLATE_96WELL);
    	StackBigInteger threeEightyFourWell = new StackBigInteger(StackBigInteger.PLATE_384WELL);
    	StackBigInteger fifteenThirtySixWell = new StackBigInteger(StackBigInteger.PLATE_1536WELL);
    	
    	assertEquals(sixWell.rows(), StackBigInteger.ROWS_6WELL);
    	assertEquals(sixWell.columns(), StackBigInteger.COLUMNS_6WELL);
    	assertEquals(sixWell.type(), StackBigInteger.PLATE_6WELL);
    	
    	assertEquals(twelveWell.rows(), StackBigInteger.ROWS_12WELL);
    	assertEquals(twelveWell.columns(), StackBigInteger.COLUMNS_12WELL);
    	assertEquals(twelveWell.type(), StackBigInteger.PLATE_12WELL);
    	
    	assertEquals(twentyFourWell.rows(), StackBigInteger.ROWS_24WELL);
    	assertEquals(twentyFourWell.columns(), StackBigInteger.COLUMNS_24WELL);
    	assertEquals(twentyFourWell.type(), StackBigInteger.PLATE_24WELL);
    	
    	assertEquals(fortyEightWell.rows(), StackBigInteger.ROWS_48WELL);
    	assertEquals(fortyEightWell.columns(), StackBigInteger.COLUMNS_48WELL);
    	assertEquals(fortyEightWell.type(), StackBigInteger.PLATE_48WELL);
    	
    	assertEquals(ninetySixWell.rows(), StackBigInteger.ROWS_96WELL);
    	assertEquals(ninetySixWell.columns(), StackBigInteger.COLUMNS_96WELL);
    	assertEquals(ninetySixWell.type(), StackBigInteger.PLATE_96WELL);
    	
    	assertEquals(threeEightyFourWell.rows(), StackBigInteger.ROWS_384WELL);
    	assertEquals(threeEightyFourWell.columns(), StackBigInteger.COLUMNS_384WELL);
    	assertEquals(threeEightyFourWell.type(), StackBigInteger.PLATE_384WELL);
    	
    	assertEquals(fifteenThirtySixWell.rows(), StackBigInteger.ROWS_1536WELL);
    	assertEquals(fifteenThirtySixWell.columns(), StackBigInteger.COLUMNS_1536WELL);
    	assertEquals(fifteenThirtySixWell.type(), StackBigInteger.PLATE_1536WELL);
    }
    
    /* ------------------- Tests methods for adding plates ------------------ */
    
    /**
     * Tests the add method using a plate.
     */
    @Test
    public void testAddPlate() {

    	for(PlateBigInteger[] plates : this.arrays) {
    	
	        testStack.clear();
	    	
	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateBigInteger plate = plates[i];
	    		testStack.add(plate);
	    		
	    		assertTrue(testStack.contains(plate));
	    	}
    	}
    }
    
    /**
     * Tests the add method using a plate collection.
     */
    @Test
    public void testAddCollection() {

    	for(Collection<PlateBigInteger> collection : this.collections) {
        	
    		testStack.clear();    		
    		testStack.add(collection);
    		
    		assertTrue(testStack.contains(collection));
    		
    	}
    	
    }
    
    /**
     * Tests the add method using a plate array.
     */
    @Test
    public void testAddArray() {
		
        for(PlateBigInteger[] array : this.arrays) {
        	
    		testStack.clear();    		
    		testStack.add(array);
    		
    		assertTrue(testStack.contains(array));
    		
    	}
    	
    }
    
    /* ------------------ Tests methods for removing plates ----------------- */
    
    /**
     * Tests the remove method using a plate.
     */
    @Test
    public void testRemovePlate() {
    	
    	for(PlateBigInteger[] plates : this.arrays) {
        	
	        StackBigInteger stack = new StackBigInteger(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateBigInteger plate = plates[i];	
	    		stack.remove(plate);

	    		assertFalse(stack.contains(plate));
	    		
	    	}
    	}

    }
    
    /**
     * Tests the remove method using a plate collection.
     */
    @Test
    public void testRemoveCollection() {

    	for(Collection<PlateBigInteger> collection : collections) {
        	
	        StackBigInteger stack = new StackBigInteger(testStack);
	        stack.add(collection);
	        stack.remove(collection);
	        
	        assertFalse(stack.contains(collection));
    	}

    }
    
    /**
     * Tests the remove method using a plate array.
     */
    @Test
    public void testRemoveArray() {

        for(PlateBigInteger[] array : arrays) {
        	
	        StackBigInteger stack = new StackBigInteger(testStack);
	        stack.add(array);
	        stack.remove(array);
	        
	        assertFalse(stack.contains(array));
    	}

    }
    
    /**
     * Tests the remove method using a label.
     */
    @Test
    public void testRemoveLabel() {
    	
    	for(PlateBigInteger[] plates : this.arrays) {
        	
	        StackBigInteger stack = new StackBigInteger(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateBigInteger plate = plates[i];	
	    		stack.remove(plate.label());

	    		assertFalse(stack.contains(plate));
	    		
	    	}
    	}

    }
    
    /**
     * Tests the remove method using a list of labels.
     */
    @Test
    public void testRemoveLabelList() {

        for(PlateBigInteger[] array : arrays) {

        	List<String> labels = new ArrayList<String>();
        	
        	for(int i = 0; i < array.length; i++) {
        		String label = "TestPlate" + i;
        		array[i].setLabel(label);
        		labels.add(label);
        	}
 
	        StackBigInteger stack = new StackBigInteger(testStack);
	        stack.add(array);
	        stack.remove(labels);

	        assertFalse(stack.contains(array));
    	}

    }
    
    /**
     * Tests the clear method.
     */
    @Test
    public void testClear() {
    	
    	StackBigInteger clone = new StackBigInteger(testStack);
    	clone.clear();
    	
    	assertTrue(clone.isEmpty());
    	assertTrue(clone.getAll().size() == 0);
    	
    	for(PlateBigInteger plate : testStack) {
    		assertFalse(clone.contains(plate));
    	}
    	
    }
    
    /*------------------ Test methods for replacing plates -------------------*/
    
    /**
     * Tests the replace method using a plate.
     */
    @Test
    public void testReplacePlate() {
    	
    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);   		
    		PlateBigInteger plate = new PlateBigInteger(array[0]);
    		
    		plate.clearWells();
    		plate.clearGroups();
    		
    		WellSetBigInteger set = RandomUtil.randomWellSetBigInteger(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellSetBigInteger groupSet = RandomUtil.randomWellSetBigInteger(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellList groups = groupSet.wellList();
    		
    		plate.addWells(set);
    		plate.addGroups(groups);

    		stack.replace(plate);

    		PlateBigInteger returned = stack.get(plate);
    		assertEquals(returned, plate);
    		assertEquals(returned.dataSet(), plate.dataSet());
    		assertEquals(returned.allGroups(), plate.allGroups());
    		assertFalse(returned.allGroups().equals(array[0].allGroups()));
    		assertFalse(returned.dataSet().equals(array[0].dataSet()));
    		
    	}
    	
    }
    
    /**
     * Tests the replace method using a plate collection.
     */
    @Test
    public void testReplaceCollection() {

    	for(Collection<PlateBigInteger> collection : collections) {

    		StackBigInteger stack = new StackBigInteger(collection);   		
    		TreeSet<PlateBigInteger> inputSet = new TreeSet<PlateBigInteger>();
    		
    		for(PlateBigInteger toReplace : collection) {
	    		
    			PlateBigInteger plate = new PlateBigInteger(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSetBigInteger set = RandomUtil.randomWellSetBigInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSetBigInteger groupSet = RandomUtil.randomWellSetBigInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<PlateBigInteger> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<PlateBigInteger> iterInput = inputSet.iterator();
    		Iterator<PlateBigInteger> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			PlateBigInteger plateInput = iterInput.next();
    			PlateBigInteger plateReturned = iterReturned.next();

    			assertEquals(plateInput, plateReturned);
    			assertEquals(plateInput.dataSet(), plateReturned.dataSet());
    			assertEquals(plateInput.allGroups(), plateReturned.allGroups());

    		}
    	}
    }
    
    /**
     * Tests the replace method using a plate array.
     */
    @Test
    public void testReplaceArray() {

    	for(PlateBigInteger[] array : arrays) {

    		StackBigInteger stack = new StackBigInteger(array);   		
    		TreeSet<PlateBigInteger> inputSet = new TreeSet<PlateBigInteger>();
    		
    		for(PlateBigInteger toReplace : array) {
	    		
    			PlateBigInteger plate = new PlateBigInteger(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSetBigInteger set = RandomUtil.randomWellSetBigInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSetBigInteger groupSet = RandomUtil.randomWellSetBigInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<PlateBigInteger> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<PlateBigInteger> iterInput = inputSet.iterator();
    		Iterator<PlateBigInteger> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			PlateBigInteger plateInput = iterInput.next();
    			PlateBigInteger plateReturned = iterReturned.next();

    			assertEquals(plateInput, plateReturned);
    			assertEquals(plateInput.dataSet(), plateReturned.dataSet());
    			assertEquals(plateInput.allGroups(), plateReturned.allGroups());

    		}
    	}
    }
    
    /*------------------ Tests methods for retaining plates ------------------*/
    
    /**
     * Tests the retain method using a plate.
     */
    @Test
    public void testRetainPlate() {
    
        for(PlateBigInteger[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackBigInteger stack = new StackBigInteger(array);   
    		PlateBigInteger plate = new PlateBigInteger(array[index]);
    		
    		stack.retain(plate);

    		assertEquals(stack.first(), plate);
    		assertEquals(stack.size(), 1);
    	}

    }
    
    /**
     * Tests the retain method using a plate collection.
     */
    @Test
    public void testRetainCollection() {

    	for(Collection<PlateBigInteger> collection : collections) {
    		
    		StackBigInteger stack = new StackBigInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		TreeSet<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		stack.retain(set);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateBigInteger plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a plate array.
     */
    @Test
    public void testRetainArray() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		TreeSet<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		PlateBigInteger[] retainArray = set.toArray(new PlateBigInteger[set.size()]);
    		stack.retain(retainArray);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateBigInteger plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a label.
     */
    @Test
    public void testRetainLabel() {

    	for(PlateBigInteger[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackBigInteger stack = new StackBigInteger(array);   
    		PlateBigInteger plate = new PlateBigInteger(array[index]);
    		
    		stack.retain(plate.label());

    		assertEquals(stack.first(), plate);
    		assertEquals(stack.size(), 1);
    	}
    	
    }
    
    /**
     * Tests the retain method using a list of labels.
     */
    @Test
    public void testRetainLabelList() {
    	
    	for(Collection<PlateBigInteger> collection : collections) {
    		
    		StackBigInteger stack = new StackBigInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		TreeSet<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateBigInteger plate : set) {
    			labelList.add(plate.label());
    		}
    		
    		stack.retain(labelList);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateBigInteger plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}

    }
    
    /*----------------- Tests methods for retrieving plates ------------------*/
    
    /**
     * Tests the get method using a label.
     */
    @Test
    public void testGetLabel() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
    		for(PlateBigInteger plate : array) {
    			assertEquals(stack.get(plate.label()), plate);
    		}
    	}
    }
    
    /**
     * Tests the get method using a list of labels.
     */
    @Test
    public void testGetLabelList() {

    	for(Collection<PlateBigInteger> collection : collections) {
    		
    		StackBigInteger stack = new StackBigInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		Set<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateBigInteger plate : set) {
    			labelList.add(plate.label());
    		}
    		
    		assertTrue(stack.get(labelList).equals(set));
    		
    	}
    }
   
    /**
     * Tests the get method using a plate.
     */
    @Test
    public void testGetPlate() {

        for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
    		for(PlateBigInteger plate : array) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}

    }
    
    /**
     * Tests the get method using a plate collection.
     */
    public void testGetCollection() {
    	
    	for(Collection<PlateBigInteger> collection : collections) {
    		
    		StackBigInteger stack = new StackBigInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		Set<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		assertEquals(stack.get(set), set);
    		
    	}

    }
    
    /**
     * Tests the get method using a plate array.
     */
    @Test
    public void testGetArray() {

        for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		int begin = 1 + random.nextInt(array.length / 2 - 1);
    		int end = begin + random.nextInt(array.length - begin) + 1;
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		Set<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}

    		PlateBigInteger[] getArray = set.toArray(new PlateBigInteger[set.size()]);
    		
    		assertEquals(stack.get(getArray), set);
    		
    	}

    }
    
    /**
     * Tests the get all method.
     */
    @Test
    public void testGetAll() {

    	for(Collection<PlateBigInteger> collection : collections) {

    		StackBigInteger stack = new StackBigInteger(collection);   
    		assertEquals(stack.getAll(), collection);
    	}
    	
    }
    
    /*------------------- Tests methods for plate lookup ---------------------*/

    /**
     * Tests the contains method using a plate.
     */
    @Test
    public void testContainsPlate() {
    	
    	for(PlateBigInteger[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackBigInteger stack = new StackBigInteger(array);   
    		PlateBigInteger plate = new PlateBigInteger(array[index]);

    		assertTrue(stack.contains(plate));
    	}
    	
    }
    
    /**
     * Tests the contains method using a plate collection.
     */
    @Test
    public void testContainsCollection() {

        for(Collection<PlateBigInteger> collection : collections) {
    		
    		StackBigInteger stack = new StackBigInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		Set<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		assertTrue(stack.contains(set));
    		
    	}

    }
    
    /**
     * Tests the contains method using a plate array.
     */
    @Test
    public void testContainsArray() {

        for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		Set<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		PlateBigInteger[] getArray = set.toArray(new PlateBigInteger[set.size()]);
    		
    		assertTrue(stack.contains(getArray));
    		
    	}

    }
    
    /**
     * Tests the contains method using a label.
     */
    @Test
    public void testContainsLabel() {

        for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
    		for(PlateBigInteger plate : array) {
    			assertTrue(stack.contains(plate));
    		}
    	}

    }
    
    /**
     * Tests the contains method using a list of labels.
     */
    @Test
    public void testContainsLabelList() {

        for(Collection<PlateBigInteger> collection : collections) {
    		
    		StackBigInteger stack = new StackBigInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin) + 1;
    		int index = 0;

    		Iterator<PlateBigInteger> iter = stack.iterator();
    		Set<PlateBigInteger> set = new TreeSet<PlateBigInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateBigInteger plate : set) {
    			labelList.add(plate.label());
    		}

    		assertTrue(stack.contains(labelList));
    		
    	}

    }
    
    /* ------------ Tests plate dimensions and data type methods ------------ */
    
    /**
     * Tests the rows getter.
     */
    @Test
    public void testRows() {
    	
    	for(int i = 0; i < 100; i++) {
    		
    		int rows = PlateBigInteger.ROWS_48WELL + 
	                 random.nextInt(PlateBigInteger.ROWS_1536WELL - 
		             PlateBigInteger.ROWS_48WELL + 1);

    		int columns = 1;
    		
    		StackBigInteger plate = new StackBigInteger(rows, columns);
    		
    		assertEquals(plate.rows(), rows);
    		
    	}
    	
    }
    
    /**
     * Tests the columns getter.
     */
    @Test
    public void testColumns() {
    	
        for(int i = 0; i < 100; i++) {
    		
    		int columns = PlateBigInteger.COLUMNS_48WELL + 
	                 random.nextInt(PlateBigInteger.COLUMNS_1536WELL - 
		             PlateBigInteger.COLUMNS_48WELL + 1);

    		int rows = 0;
    		
    		StackBigInteger stack = new StackBigInteger(rows, columns);
    		
    		assertEquals(stack.columns(), columns);
    		
    	}
    }

    /**
     * Tests the type getter.
     */
    @Test
    public void testType() {
     
    	for(int i = 0; i < 100; i++) {
    	    int type = random.nextInt(6 + 1);
    	    StackBigInteger stack = new StackBigInteger(type);
    	    assertEquals(type, stack.type());
    	}
    	
    }
    
    /**
     * Tests the descriptor getter.
     * @Test
     */
    @Test
    public void testDescriptor() {
 	
    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		StackBigInteger stack = new StackBigInteger(type);
    		
    		switch(type) {
    		
    		    case StackBigInteger.PLATE_6WELL:    assertEquals(stack.descriptor(), "6-Well");
    		                                         break;
    		    case StackBigInteger.PLATE_12WELL:   assertEquals(stack.descriptor(), "12-Well");
                                                     break;
    		    case StackBigInteger.PLATE_24WELL:   assertEquals(stack.descriptor(), "24-Well");
                                                     break;
    		    case StackBigInteger.PLATE_48WELL:   assertEquals(stack.descriptor(), "48-Well");
                                                     break;
    		    case StackBigInteger.PLATE_96WELL:   assertEquals(stack.descriptor(), "96-Well");
                                                     break;
    		    case StackBigInteger.PLATE_384WELL:  assertEquals(stack.descriptor(), "384-Well");
                                                     break;
    		    case StackBigInteger.PLATE_1536WELL: assertEquals(stack.descriptor(), "1536-Well");
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		int rows = StackBigInteger.ROWS_48WELL + 
	                   random.nextInt(StackBigInteger.ROWS_1536WELL - 
   		               StackBigInteger.ROWS_48WELL + 1);

            int columns =  StackBigInteger.COLUMNS_48WELL + 
	                       random.nextInt(StackBigInteger.COLUMNS_1536WELL - 
			               StackBigInteger.COLUMNS_48WELL + 1);
            
            PlateBigInteger randomPlate = new PlateBigInteger(rows, columns);
            
            if(randomPlate.type() == -1) {
            	String desc = "Custom Plate: " + rows + "x" + columns;
            	assertEquals(desc, randomPlate.descriptor());
            }
    	}
    }
    
    /**
     * Tests the data type getter.
     */
    @Test
    public void testDataType() {
    	for(int i = 0; i < 100; i++) {
    		StackBigInteger stack = new StackBigInteger(0);
    		assertEquals(stack.dataType(), PlateBigInteger.PLATE_BIGINTEGER);
    	}
    }
    
    /**
     * Tests the data type string getter.
     */
    @Test
    public void testDataTypeString() {
    	for(int i = 0; i < 100; i++) {
    		StackBigInteger stack = new StackBigInteger(0);
    		assertEquals(stack.dataTypeString(), "BigInteger");
    	}
    }
    
    /**
     * Tests the label setter.
     */
    @Test
    public void testSetLabel() {
    	for(int i = 0; i < 100; i++) {
    		String label = "TestLabel";
    		StackBigInteger stack = new StackBigInteger(0);
    		stack.setLabel(label);
    		assertEquals(stack.label(), label);
    	}
    }
    
    /**
     * Tests the label getter.
     */
    @Test
    public void testLabel() {
    	for(int i = 0; i < 100; i++) {
    		StackBigInteger stack = new StackBigInteger(0);
    		assertEquals(stack.label(), "StackBigInteger");
    	}
    }

    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		StackBigInteger stack = new StackBigInteger(type);
    		
    		String toString = "Type: ";
    		
    		switch(type) {
    		
    		    case StackBigInteger.PLATE_6WELL:    toString += "6-Well Label: ";
    		                                         break;
    		    case StackBigInteger.PLATE_12WELL:   toString += "12-Well Label: ";
                                                     break;
    		    case StackBigInteger.PLATE_24WELL:   toString += "24-Well Label: ";
                                                     break;
    		    case StackBigInteger.PLATE_48WELL:   toString += "48-Well Label: ";
                                                     break;
    		    case StackBigInteger.PLATE_96WELL:   toString += "96-Well Label: ";
                                                     break;
    		    case StackBigInteger.PLATE_384WELL:  toString += "384-Well Label: ";
                                                     break;
    		    case StackBigInteger.PLATE_1536WELL: toString += "1536-Well Label: ";
                                                     break;
                default: throw new IllegalArgumentException("Invalid stack type constant.");
    		
    		}
    		
    		toString += stack.label();
    		
    		assertEquals(toString, stack.toString());
    		
    		int rows = StackBigInteger.ROWS_48WELL + 
	                   random.nextInt(StackBigInteger.ROWS_1536WELL - 
   		               StackBigInteger.ROWS_48WELL + 1);

            int columns =  StackBigInteger.COLUMNS_48WELL + 
	                       random.nextInt(StackBigInteger.COLUMNS_1536WELL - 
			               StackBigInteger.COLUMNS_48WELL + 1);
            
            StackBigInteger randomStack = new StackBigInteger(rows, columns);
            
            if(randomStack.type() == -1) {
            	String toStringRandom = "Type: Custom Stack: " + rows + "x" + columns + " Label: " + randomStack.label();
            	assertEquals(toStringRandom, toStringRandom);
            }
            
    	}
    }

    /*------------------------- Tests TreeSet methods ------------------------*/
    
    /**
     * Tests the size getter.
     */
    @Test
    public void testSize() {
    	for(PlateBigInteger[] array : arrays) {
    		StackBigInteger stack = new StackBigInteger(array);
    		assertEquals(stack.size(), array.length);
    	}
    }
    
    /**
     * Tests the to array method.
     */
    @Test
    public void testToArray() {
    	
    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		PlateBigInteger[] stackArray = stack.toArray();
    		
    		Arrays.sort(array);
    		Arrays.sort(stackArray);
  
    		for(int i = 0; i < array.length; i++) {
    			assertEquals(array[i], stackArray[i]);
    		}
    	}
    }
    
    /**
     * Tests the higher method.
     */
    @Test
    public void testHigher() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	int current = 0;
	    	Set<PlateBigInteger> testSet = stack.getAll();
	
	    	int index = 1 + random.nextInt(testSet.size() - 2);

	        Iterator<PlateBigInteger> iter = stack.iterator();
	        
	        while(current < index) {
	        	current++;
	        	iter.next();
	        }
	        
	        PlateBigInteger toHigher = iter.next();
	        PlateBigInteger toReturn = iter.next();
	        PlateBigInteger input = new PlateBigInteger(toReturn.rows(), toReturn.columns() - 1);
	        PlateBigInteger outside = new PlateBigInteger(stack.rows() + 1, stack.columns());
	         
	        toReturn = toHigher.compareTo(input) < 1 ? toHigher : toReturn;

	        assertEquals(stack.higher(toHigher), toReturn);
	        assertNull(stack.higher(outside));
    	}
    }
    
    /**
     * Tests the lower method.
     */
    @Test
    public void testLower() {
    	
    	for(PlateBigInteger[] array : arrays) {
    	
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	int current = 0;
	    	int index = 1 + random.nextInt(stack.size() - 2);
	        
	        Iterator<PlateBigInteger> iter = stack.iterator();
	        
	        while(current < index && iter.hasNext()) {
	        	current++;
	        	iter.next();
	        }

	        PlateBigInteger toReturn = iter.next();
	        PlateBigInteger toFloor = iter.next();
	        PlateBigInteger input = new PlateBigInteger(toFloor.rows(), toFloor.columns() - 1);
	        PlateBigInteger outside = new PlateBigInteger(stack.rows() - 1, stack.columns());
	        
	        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;
	
	        assertEquals(stack.lower(toFloor), toReturn);
	        assertNull(stack.lower(outside));
    	}
    }
    
    /**
     * Tests the poll first method.
     */
    @Test
    public void testPollFirst() {
    	
    	for(PlateBigInteger[] array : arrays) {
    
    		StackBigInteger stack = new StackBigInteger(array);
    	
	    	PlateBigInteger first = stack.first();
	    	PlateBigInteger polled = stack.pollFirst();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(first, polled);
    	}
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	for(PlateBigInteger[] array : arrays) {
    	    
    		StackBigInteger stack = new StackBigInteger(array);
    	
	    	PlateBigInteger last = stack.last();
	    	PlateBigInteger polled = stack.pollLast();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(last, polled);
    	}
    	
    }  
    
    /**
     * Tests the iterator getter.
     */
    @Test
    public void testIterator() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
	    	Iterator<PlateBigInteger> iter = stack.iterator();
	    	
	    	Arrays.sort(array);
	    	
	    	for(int i = 0; i < array.length; i++) {
	    		assertEquals(array[i], iter.next());
	    	}
    	}
    }
    
    /**
     * Tests the descending iterator getter.
     */
    @Test
    public void testDescendingIterator() {
    	
    	for(PlateBigInteger[] array : arrays) {

    		StackBigInteger stack = new StackBigInteger(array);
	    	Iterator<PlateBigInteger> iter = stack.descendingIterator();
	    	
	    	Arrays.sort(array);
	    	
	    	for(int i = array.length - 1; i >= 0; i--) {
	    		assertEquals(array[i], iter.next());
	    	}
    	}
    }
    
    /**
     * Tests the descending set method.
     */
    @Test
    public void testDescendingSet() {
    	
    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	Set<PlateBigInteger> reversed = stack.descendingSet();
	
	    	Iterator<PlateBigInteger> iter = stack.descendingIterator();
	    	Iterator<PlateBigInteger> iterReversed = reversed.iterator();
	    	
	    	while(iter.hasNext() && iterReversed.hasNext()) {
	    		assertEquals(iter.next(), iterReversed.next());
	    	}
    	}
    }
    
    /**
     * Tests the first method.
     */
    @Test
    public void testFirst() {
 	
    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
    		Iterator<PlateBigInteger> iter = stack.iterator();
    		assertEquals(iter.next(), stack.first());
    
    	}  	
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {
    	
    	for(PlateBigInteger[] array : arrays) {
        
    		StackBigInteger stack = new StackBigInteger(array);
    		
    		Iterator<PlateBigInteger> iter = stack.iterator();
    		PlateBigInteger last = null;
    	
    		while(iter.hasNext()) {	
    			last = iter.next();
    		}
    	
    		assertEquals(last, stack.last());
    
    	}
    }
    
    /**
     * Tests the ceiling method.
     */
    @Test
    public void testCeiling() {

    	for(PlateBigInteger[] array : arrays) {
    	
	    	StackBigInteger stack = new StackBigInteger(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<PlateBigInteger> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        PlateBigInteger toCeiling = iter.next();
	        PlateBigInteger outside = new PlateBigInteger(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toCeiling), toCeiling);
	        assertNull(stack.ceiling(outside));
    	}
        
    }
    
    /**
     * Tests the floor method.
     */
    @Test
    public void testFloor() {

    	for(PlateBigInteger[] array : arrays) {
        	
	    	StackBigInteger stack = new StackBigInteger(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<PlateBigInteger> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        PlateBigInteger toFloor = iter.next();
	        PlateBigInteger outside = new PlateBigInteger(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toFloor), toFloor);
	        assertNull(stack.ceiling(outside));
    	}
    	
    }
    
    /**
     * Tests the head set method.
     */
    @Test
    public void testHeadSet() {
    	
    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	Set<PlateBigInteger> testSet = stack.getAll();
	    	
	        PlateBigInteger random = (PlateBigInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateBigInteger> headSet = stack.headSet(random);
	    	Set<PlateBigInteger> subSet = stack.subSet(stack.first(), random);
	    	
	    	assertEquals(headSet, subSet);
    	}
    }
    
    /**
     * Tests the head set inclusive method.
     */
    @Test
    public void testHeadSetInclusive() {
    	
    	for(PlateBigInteger[] array : arrays) {
	    	
	    	StackBigInteger stack = new StackBigInteger(array);
	    	
	    	Set<PlateBigInteger> testSet = stack.getAll();
	    	
	    	PlateBigInteger random = (PlateBigInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateBigInteger> headSetTrue = stack.headSet(random, true);
	    	Set<PlateBigInteger> subSetTrue = stack.subSet(stack.first(), true, random, true);
	    	Set<PlateBigInteger> headSetFalse = stack.headSet(random, false);
	    	Set<PlateBigInteger> subSetFalse = stack.subSet(stack.first(), true, random, false);
	    	
	    	assertEquals(headSetTrue, subSetTrue);
	    	assertEquals(headSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the tail set method.
     */
    @Test
    public void testTailSet() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	Set<PlateBigInteger> testSet = stack.getAll();
	    	
	    	PlateBigInteger random = (PlateBigInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateBigInteger> tailSet = stack.tailSet(random);
	    	Set<PlateBigInteger> subSet = stack.subSet(random, true, stack.last(), true);
	
	    	assertEquals(tailSet, subSet);
    	}
    }
    
    /**
     * Tests the tail set inclusive method.
     */
    @Test
    public void testTailSetInclusive() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	Set<PlateBigInteger> testSet = stack.getAll();
	    	
	    	PlateBigInteger random = (PlateBigInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateBigInteger> tailSetTrue = stack.tailSet(random, true);
	    	Set<PlateBigInteger> subSetTrue = stack.subSet(random, true, stack.last(), true);
	    	Set<PlateBigInteger> tailSetFalse = stack.tailSet(random, false);
	    	Set<PlateBigInteger> subSetFalse = stack.subSet(random, false, stack.last(), true);
	    	
	    	assertEquals(tailSetTrue, subSetTrue);
	    	assertEquals(tailSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the inclusive subset method.
     */
    @Test
    public void testSubSetInclusive() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;
	    	
	    	Set<PlateBigInteger> subSetTrueTrue = new HashSet<PlateBigInteger>();
	    	Set<PlateBigInteger> subSetTrueFalse = new HashSet<PlateBigInteger>();
	    	Set<PlateBigInteger> subSetFalseTrue = new HashSet<PlateBigInteger>();;
	    	Set<PlateBigInteger> subSetFalseFalse = new HashSet<PlateBigInteger>();;
	    	
	    	PlateBigInteger startingPlate = null;
	    	PlateBigInteger endingPlate = null;
	    	
	    	Iterator<PlateBigInteger> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		PlateBigInteger inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSetTrueTrue.add(new PlateBigInteger(inclusive));
	    		    subSetTrueFalse.add(new PlateBigInteger(inclusive));
	    		    startingPlate = new PlateBigInteger(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		PlateBigInteger inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new PlateBigInteger(inclusive);
	    		    subSetTrueTrue.add(new PlateBigInteger(inclusive));
	    		    subSetFalseTrue.add(new PlateBigInteger(inclusive));
	    		    break;
	    		    
	    		} else {
	    			
	    			subSetTrueTrue.add(new PlateBigInteger(inclusive));
	    			subSetTrueFalse.add(new PlateBigInteger(inclusive));
	    			subSetFalseTrue.add(new PlateBigInteger(inclusive));
	    			subSetFalseFalse.add(new PlateBigInteger(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}

	    	Set<PlateBigInteger> trueTrue = stack.subSet(startingPlate, true, endingPlate, true);
	    	Set<PlateBigInteger> trueFalse = stack.subSet(startingPlate, true, endingPlate, false);
	    	Set<PlateBigInteger> falseTrue = stack.subSet(startingPlate, false, endingPlate, true);
	    	Set<PlateBigInteger> falseFalse = stack.subSet(startingPlate, false, endingPlate, false);
	
	    	assertEquals(trueTrue, subSetTrueTrue);
	    	assertEquals(trueFalse, subSetTrueFalse);
	    	assertEquals(falseTrue, subSetFalseTrue);
	    	assertEquals(falseFalse, subSetFalseFalse);
    	}
    	
    }
    
    /**
     * Tests the subset method.
     */
    @Test
    public void testSubSet() {

    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;     	
	    	
	    	Set<PlateBigInteger> subSet = new TreeSet<PlateBigInteger>();
	    	
	    	PlateBigInteger startingPlate = null;
	    	PlateBigInteger endingPlate = null;
	    	
	    	Iterator<PlateBigInteger> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		PlateBigInteger inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSet.add(new PlateBigInteger(inclusive));
	    		    startingPlate = new PlateBigInteger(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		PlateBigInteger inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new PlateBigInteger(inclusive);
	    		    break;
	    		    
	    		} else {
	    			
	    			subSet.add(new PlateBigInteger(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}
	    	
	    	Set<PlateBigInteger> toCompare = stack.subSet(startingPlate, endingPlate);
	
	    	assertEquals(toCompare, subSet);
    	}
    }

    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {
    	
    	for(PlateBigInteger[] array : arrays) {
    		
    		StackBigInteger stack = new StackBigInteger(array);
    		
    		assertFalse(stack.isEmpty());
    		stack.clear();
    		assertTrue(stack.isEmpty());
    		
    	}
    	
    }

    /*----------------------- Testing helper methods -------------------------*/
    
    /**
     * Returns true if the row and column match plate type constants.
     * @param    int    plate row number
     * @param    int    plate column number
     * @return          true if row and column match a plate type
     */
    private boolean checkType(int rows, int columns) {
    	
        if((rows == StackBigInteger.ROWS_6WELL && columns == StackBigInteger.COLUMNS_6WELL) ||
          (rows == StackBigInteger.ROWS_12WELL && columns == StackBigInteger.COLUMNS_12WELL) ||
          (rows == StackBigInteger.ROWS_24WELL && columns == StackBigInteger.COLUMNS_24WELL) ||
          (rows == StackBigInteger.ROWS_48WELL && columns == StackBigInteger.COLUMNS_48WELL) ||
          (rows == StackBigInteger.ROWS_96WELL && columns == StackBigInteger.COLUMNS_96WELL) ||
          (rows == StackBigInteger.ROWS_384WELL && columns == StackBigInteger.COLUMNS_384WELL) ||
          (rows == StackBigInteger.ROWS_1536WELL && columns == StackBigInteger.COLUMNS_1536WELL)) {
        	return true;
        }
        
        return false;
    }
    
    /**
     * Adds the appropriate type and descriptor for the plate dimensions.
     * @param    int    plate row number
     * @param    int    plate column number
     */
    private void getDescriptor(int rows, int columns) {

    	String descriptor;
    	
        if(rows == StackBigInteger.ROWS_6WELL && 
           columns == StackBigInteger.COLUMNS_6WELL) {

            descriptor = "6-Well";

        } else if(rows == StackBigInteger.ROWS_12WELL && 
                  columns == StackBigInteger.COLUMNS_12WELL) {

            descriptor = "12-Well";
        
        } else if(rows == StackBigInteger.ROWS_24WELL && 
                  columns == StackBigInteger.COLUMNS_24WELL) {

            descriptor = "24-Well";
        
        } else if(rows == StackBigInteger.ROWS_48WELL && 
                  columns == StackBigInteger.COLUMNS_48WELL) {

            descriptor = "48-Well";
        
        } else if(rows == StackBigInteger.ROWS_96WELL && 
                  columns == StackBigInteger.COLUMNS_96WELL) {

            descriptor = "96-Well";
        
        } else if(rows == StackBigInteger.ROWS_384WELL && 
                  columns == StackBigInteger.COLUMNS_384WELL) {

            descriptor = "384-Well";
        
        } else if(rows == StackBigInteger.ROWS_1536WELL && 
                  columns == StackBigInteger.COLUMNS_1536WELL) {

            descriptor = "1536-Well";
        
        } else {

            descriptor = "Custom Stack: " + rows + "x" + columns;
            
        }

        this.descriptors.add(descriptor);
    }
}
