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

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the integer stack plate 
 * class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StackIntegerTest {

	/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private Integer minValue = new Integer(-1000000000);   // Minimum integer value for wells
	private Integer maxValue = new Integer(1000000000);    // Maximum integer value for wells
	private int minLength = 100;                           // Minimum well set length
	private int maxLength = 1000;                          // Maximum well set length
	private int groupNumber = 0;                           // Group number
	private int minRow = 0;								   // Minimum row number
	private int minColumn = 1;							   // Minimum column number
	private int minPlate = 10;                             // Plate minimum
	private int maxPlate = 25;                             // Plate maximum
	private Random random = new Random();                  // Generates random integers
	
	/* Random objects and numbers for testing */

	private int rows;
	private int columns;
	private int length;
	private int plateNumber;
	private StackInteger testStack;
	private List<PlateInteger[]> arrays = new ArrayList<PlateInteger[]>();
	private List<TreeSet<PlateInteger>> collections = new ArrayList<TreeSet<PlateInteger>>();
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
		
		rows = PlateInteger.ROWS_48WELL + random.nextInt(PlateInteger.ROWS_1536WELL - 
	           PlateInteger.ROWS_48WELL + 1);

		columns =  PlateInteger.COLUMNS_48WELL + random.nextInt(PlateInteger.COLUMNS_1536WELL - 
		           PlateInteger.COLUMNS_48WELL + 1);
		
		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
		
    	length = rows * columns / 5;
    	
    	testStack = RandomUtil.randomStackInteger(rows, columns, minValue, 
    			maxValue, minLength, maxLength, 0, length, "TestStack", plateNumber);
    	
    	for(int i = 0; i < 25; i++) {
    		
    		TreeSet<PlateInteger> list = new TreeSet<PlateInteger>();
    		PlateInteger[] array = new PlateInteger[20];
    		
    		for(int j = 0; j < 20; j++) {
    			PlateInteger plate = RandomUtil.randomPlateInteger(
            			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "PlateInteger" + j);
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

    	StackInteger stack = new StackInteger(PlateInteger.PLATE_96WELL);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), PlateInteger.ROWS_96WELL);
        assertEquals(stack.columns(), PlateInteger.COLUMNS_96WELL);      
        assertEquals(stack.type(), PlateInteger.PLATE_96WELL);
        assertEquals(stack.dataType(), WellInteger.INTEGER);
        assertEquals(stack.descriptor(), "96-Well");
        assertEquals(stack.label(), "StackInteger");
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
    	
    	StackInteger stack = new StackInteger(rows, columns);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a type constant and label.
     */
	@Test
    public void testConstructorTypeLabel() {
    	
    	String label = "TestLabel";
    	
    	StackInteger stack = new StackInteger(StackInteger.PLATE_384WELL, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), StackInteger.ROWS_384WELL);
        assertEquals(stack.columns(), StackInteger.COLUMNS_384WELL);      
        assertEquals(stack.type(), StackInteger.PLATE_384WELL);
        assertEquals(stack.dataType(), WellInteger.INTEGER);
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
    	
    	StackInteger stack = new StackInteger(rows, columns, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
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
    	
    	PlateInteger plate = RandomUtil.randomPlateInteger(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "StackInteger");
    	
    	StackInteger stack = new StackInteger(plate);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackInteger");
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
    	
    	PlateInteger plate = RandomUtil.randomPlateInteger(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, label);
    	
    	StackInteger stack = new StackInteger(plate, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
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
    	
    	StackInteger stack = new StackInteger(collections.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), collections.get(0).size());
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate collection and label.
     */
    @Test
    public void testConstructorCollectionLabel() {

    	String label = "TestLabel";
    	
    	StackInteger stack = new StackInteger(collections.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
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
    
    	StackInteger stack = new StackInteger(arrays.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackInteger");
        assertEquals(stack.size(), arrays.get(0).length);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate array and a label.
     */
    @Test
    public void testConstructorArrayLabel() {

    	String label = "TestLabel";
    	
    	StackInteger stack = new StackInteger(arrays.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellInteger.INTEGER);
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
    	
    	StackInteger sixWell = new StackInteger(StackInteger.PLATE_6WELL);
    	StackInteger twelveWell = new StackInteger(StackInteger.PLATE_12WELL);
    	StackInteger twentyFourWell = new StackInteger(StackInteger.PLATE_24WELL);
    	StackInteger fortyEightWell = new StackInteger(StackInteger.PLATE_48WELL);
    	StackInteger ninetySixWell = new StackInteger(StackInteger.PLATE_96WELL);
    	StackInteger threeEightyFourWell = new StackInteger(StackInteger.PLATE_384WELL);
    	StackInteger fifteenThirtySixWell = new StackInteger(StackInteger.PLATE_1536WELL);
    	
    	assertEquals(sixWell.rows(), StackInteger.ROWS_6WELL);
    	assertEquals(sixWell.columns(), StackInteger.COLUMNS_6WELL);
    	assertEquals(sixWell.type(), StackInteger.PLATE_6WELL);
    	
    	assertEquals(twelveWell.rows(), StackInteger.ROWS_12WELL);
    	assertEquals(twelveWell.columns(), StackInteger.COLUMNS_12WELL);
    	assertEquals(twelveWell.type(), StackInteger.PLATE_12WELL);
    	
    	assertEquals(twentyFourWell.rows(), StackInteger.ROWS_24WELL);
    	assertEquals(twentyFourWell.columns(), StackInteger.COLUMNS_24WELL);
    	assertEquals(twentyFourWell.type(), StackInteger.PLATE_24WELL);
    	
    	assertEquals(fortyEightWell.rows(), StackInteger.ROWS_48WELL);
    	assertEquals(fortyEightWell.columns(), StackInteger.COLUMNS_48WELL);
    	assertEquals(fortyEightWell.type(), StackInteger.PLATE_48WELL);
    	
    	assertEquals(ninetySixWell.rows(), StackInteger.ROWS_96WELL);
    	assertEquals(ninetySixWell.columns(), StackInteger.COLUMNS_96WELL);
    	assertEquals(ninetySixWell.type(), StackInteger.PLATE_96WELL);
    	
    	assertEquals(threeEightyFourWell.rows(), StackInteger.ROWS_384WELL);
    	assertEquals(threeEightyFourWell.columns(), StackInteger.COLUMNS_384WELL);
    	assertEquals(threeEightyFourWell.type(), StackInteger.PLATE_384WELL);
    	
    	assertEquals(fifteenThirtySixWell.rows(), StackInteger.ROWS_1536WELL);
    	assertEquals(fifteenThirtySixWell.columns(), StackInteger.COLUMNS_1536WELL);
    	assertEquals(fifteenThirtySixWell.type(), StackInteger.PLATE_1536WELL);
    }
    
    /* ------------------- Tests methods for adding plates ------------------ */
    
    /**
     * Tests the add method using a plate.
     */
    @Test
    public void testAddPlate() {

    	for(PlateInteger[] plates : this.arrays) {
    	
	        testStack.clear();
	    	
	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateInteger plate = plates[i];
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

    	for(Collection<PlateInteger> collection : this.collections) {
        	
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
		
        for(PlateInteger[] array : this.arrays) {
        	
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
    	
    	for(PlateInteger[] plates : this.arrays) {
        	
	        StackInteger stack = new StackInteger(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateInteger plate = plates[i];	
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

    	for(Collection<PlateInteger> collection : collections) {
        	
	        StackInteger stack = new StackInteger(testStack);
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

        for(PlateInteger[] array : arrays) {
        	
	        StackInteger stack = new StackInteger(testStack);
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
    	
    	for(PlateInteger[] plates : this.arrays) {
        	
	        StackInteger stack = new StackInteger(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateInteger plate = plates[i];	
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

        for(PlateInteger[] array : arrays) {

        	List<String> labels = new ArrayList<String>();
        	
        	for(int i = 0; i < array.length; i++) {
        		String label = "TestPlate" + i;
        		array[i].setLabel(label);
        		labels.add(label);
        	}
 
	        StackInteger stack = new StackInteger(testStack);
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
    	
    	StackInteger clone = new StackInteger(testStack);
    	clone.clear();
    	
    	assertTrue(clone.isEmpty());
    	assertTrue(clone.getAll().size() == 0);
    	
    	for(PlateInteger plate : testStack) {
    		assertFalse(clone.contains(plate));
    	}
    	
    }
    
    /*------------------ Test methods for replacing plates -------------------*/
    
    /**
     * Tests the replace method using a plate.
     */
    @Test
    public void testReplacePlate() {
    	
    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);   		
    		PlateInteger plate = new PlateInteger(array[0]);
    		
    		plate.clearWells();
    		plate.clearGroups();
    		
    		WellSetInteger set = RandomUtil.randomWellSetInteger(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellSetInteger groupSet = RandomUtil.randomWellSetInteger(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellList groups = groupSet.wellList();
    		
    		plate.addWells(set);
    		plate.addGroups(groups);

    		stack.replace(plate);

    		PlateInteger returned = stack.get(plate);
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

    	for(Collection<PlateInteger> collection : collections) {

    		StackInteger stack = new StackInteger(collection);   		
    		TreeSet<PlateInteger> inputSet = new TreeSet<PlateInteger>();
    		
    		for(PlateInteger toReplace : collection) {
	    		
    			PlateInteger plate = new PlateInteger(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSetInteger set = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSetInteger groupSet = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<PlateInteger> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<PlateInteger> iterInput = inputSet.iterator();
    		Iterator<PlateInteger> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			PlateInteger plateInput = iterInput.next();
    			PlateInteger plateReturned = iterReturned.next();

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

    	for(PlateInteger[] array : arrays) {

    		StackInteger stack = new StackInteger(array);   		
    		TreeSet<PlateInteger> inputSet = new TreeSet<PlateInteger>();
    		
    		for(PlateInteger toReplace : array) {
	    		
    			PlateInteger plate = new PlateInteger(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSetInteger set = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSetInteger groupSet = RandomUtil.randomWellSetInteger(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<PlateInteger> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<PlateInteger> iterInput = inputSet.iterator();
    		Iterator<PlateInteger> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			PlateInteger plateInput = iterInput.next();
    			PlateInteger plateReturned = iterReturned.next();

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
    
        for(PlateInteger[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackInteger stack = new StackInteger(array);   
    		PlateInteger plate = new PlateInteger(array[index]);
    		
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

    	for(Collection<PlateInteger> collection : collections) {
    		
    		StackInteger stack = new StackInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		TreeSet<PlateInteger> set = new TreeSet<PlateInteger>();
    		
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
    		
    		for(PlateInteger plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a plate array.
     */
    @Test
    public void testRetainArray() {

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		TreeSet<PlateInteger> set = new TreeSet<PlateInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		PlateInteger[] retainArray = set.toArray(new PlateInteger[set.size()]);
    		stack.retain(retainArray);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateInteger plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a label.
     */
    @Test
    public void testRetainLabel() {

    	for(PlateInteger[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackInteger stack = new StackInteger(array);   
    		PlateInteger plate = new PlateInteger(array[index]);
    		
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
    	
    	for(Collection<PlateInteger> collection : collections) {
    		
    		StackInteger stack = new StackInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		TreeSet<PlateInteger> set = new TreeSet<PlateInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateInteger plate : set) {
    			labelList.add(plate.label());
    		}
    		
    		stack.retain(labelList);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateInteger plate : set) {
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

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
    		for(PlateInteger plate : array) {
    			assertEquals(stack.get(plate.label()), plate);
    		}
    	}
    }
    
    /**
     * Tests the get method using a list of labels.
     */
    @Test
    public void testGetLabelList() {

    	for(Collection<PlateInteger> collection : collections) {
    		
    		StackInteger stack = new StackInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		Set<PlateInteger> set = new TreeSet<PlateInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateInteger plate : set) {
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

        for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
    		for(PlateInteger plate : array) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}

    }
    
    /**
     * Tests the get method using a plate collection.
     */
    public void testGetCollection() {
    	
    	for(Collection<PlateInteger> collection : collections) {
    		
    		StackInteger stack = new StackInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		Set<PlateInteger> set = new TreeSet<PlateInteger>();
    		
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

        for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		int begin = 1 + random.nextInt(array.length / 2 - 1);
    		int end = begin + random.nextInt(array.length - begin) + 1;
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		Set<PlateInteger> set = new TreeSet<PlateInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}

    		PlateInteger[] getArray = set.toArray(new PlateInteger[set.size()]);
    		
    		assertEquals(stack.get(getArray), set);
    		
    	}

    }
    
    /**
     * Tests the get all method.
     */
    @Test
    public void testGetAll() {

    	for(Collection<PlateInteger> collection : collections) {

    		StackInteger stack = new StackInteger(collection);   
    		assertEquals(stack.getAll(), collection);
    	}
    	
    }
    
    /*------------------- Tests methods for plate lookup ---------------------*/

    /**
     * Tests the contains method using a plate.
     */
    @Test
    public void testContainsPlate() {
    	
    	for(PlateInteger[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackInteger stack = new StackInteger(array);   
    		PlateInteger plate = new PlateInteger(array[index]);

    		assertTrue(stack.contains(plate));
    	}
    	
    }
    
    /**
     * Tests the contains method using a plate collection.
     */
    @Test
    public void testContainsCollection() {

        for(Collection<PlateInteger> collection : collections) {
    		
    		StackInteger stack = new StackInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		Set<PlateInteger> set = new TreeSet<PlateInteger>();
    		
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

        for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		Set<PlateInteger> set = new TreeSet<PlateInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		PlateInteger[] getArray = set.toArray(new PlateInteger[set.size()]);
    		
    		assertTrue(stack.contains(getArray));
    		
    	}

    }
    
    /**
     * Tests the contains method using a label.
     */
    @Test
    public void testContainsLabel() {

        for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
    		for(PlateInteger plate : array) {
    			assertTrue(stack.contains(plate));
    		}
    	}

    }
    
    /**
     * Tests the contains method using a list of labels.
     */
    @Test
    public void testContainsLabelList() {

        for(Collection<PlateInteger> collection : collections) {
    		
    		StackInteger stack = new StackInteger(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin) + 1;
    		int index = 0;

    		Iterator<PlateInteger> iter = stack.iterator();
    		Set<PlateInteger> set = new TreeSet<PlateInteger>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateInteger plate : set) {
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
    		
    		int rows = PlateInteger.ROWS_48WELL + 
	                 random.nextInt(PlateInteger.ROWS_1536WELL - 
		             PlateInteger.ROWS_48WELL + 1);

    		int columns = 1;
    		
    		StackInteger plate = new StackInteger(rows, columns);
    		
    		assertEquals(plate.rows(), rows);
    		
    	}
    	
    }
    
    /**
     * Tests the columns getter.
     */
    @Test
    public void testColumns() {
    	
        for(int i = 0; i < 100; i++) {
    		
    		int columns = PlateInteger.COLUMNS_48WELL + 
	                 random.nextInt(PlateInteger.COLUMNS_1536WELL - 
		             PlateInteger.COLUMNS_48WELL + 1);

    		int rows = 0;
    		
    		StackInteger stack = new StackInteger(rows, columns);
    		
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
    	    StackInteger stack = new StackInteger(type);
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
    		StackInteger stack = new StackInteger(type);
    		
    		switch(type) {
    		
    		    case StackInteger.PLATE_6WELL:    assertEquals(stack.descriptor(), "6-Well");
    		                                         break;
    		    case StackInteger.PLATE_12WELL:   assertEquals(stack.descriptor(), "12-Well");
                                                     break;
    		    case StackInteger.PLATE_24WELL:   assertEquals(stack.descriptor(), "24-Well");
                                                     break;
    		    case StackInteger.PLATE_48WELL:   assertEquals(stack.descriptor(), "48-Well");
                                                     break;
    		    case StackInteger.PLATE_96WELL:   assertEquals(stack.descriptor(), "96-Well");
                                                     break;
    		    case StackInteger.PLATE_384WELL:  assertEquals(stack.descriptor(), "384-Well");
                                                     break;
    		    case StackInteger.PLATE_1536WELL: assertEquals(stack.descriptor(), "1536-Well");
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		int rows = StackInteger.ROWS_48WELL + 
	                   random.nextInt(StackInteger.ROWS_1536WELL - 
   		               StackInteger.ROWS_48WELL + 1);

            int columns =  StackInteger.COLUMNS_48WELL + 
	                       random.nextInt(StackInteger.COLUMNS_1536WELL - 
			               StackInteger.COLUMNS_48WELL + 1);
            
            PlateInteger randomPlate = new PlateInteger(rows, columns);
            
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
    		StackInteger stack = new StackInteger(0);
    		assertEquals(stack.dataType(), PlateInteger.PLATE_INTEGER);
    	}
    }
    
    /**
     * Tests the data type string getter.
     */
    @Test
    public void testDataTypeString() {
    	for(int i = 0; i < 100; i++) {
    		StackInteger stack = new StackInteger(0);
    		assertEquals(stack.dataTypeString(), "Integer");
    	}
    }
    
    /**
     * Tests the label setter.
     */
    @Test
    public void testSetLabel() {
    	for(int i = 0; i < 100; i++) {
    		String label = "TestLabel";
    		StackInteger stack = new StackInteger(0);
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
    		StackInteger stack = new StackInteger(0);
    		assertEquals(stack.label(), "StackInteger");
    	}
    }

    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		StackInteger stack = new StackInteger(type);
    		
    		String toString = "Type: ";
    		
    		switch(type) {
    		
    		    case StackInteger.PLATE_6WELL:    toString += "6-Well Label: ";
    		                                         break;
    		    case StackInteger.PLATE_12WELL:   toString += "12-Well Label: ";
                                                     break;
    		    case StackInteger.PLATE_24WELL:   toString += "24-Well Label: ";
                                                     break;
    		    case StackInteger.PLATE_48WELL:   toString += "48-Well Label: ";
                                                     break;
    		    case StackInteger.PLATE_96WELL:   toString += "96-Well Label: ";
                                                     break;
    		    case StackInteger.PLATE_384WELL:  toString += "384-Well Label: ";
                                                     break;
    		    case StackInteger.PLATE_1536WELL: toString += "1536-Well Label: ";
                                                     break;
                default: throw new IllegalArgumentException("Invalid stack type constant.");
    		
    		}
    		
    		toString += stack.label();
    		
    		assertEquals(toString, stack.toString());
    		
    		int rows = StackInteger.ROWS_48WELL + 
	                   random.nextInt(StackInteger.ROWS_1536WELL - 
   		               StackInteger.ROWS_48WELL + 1);

            int columns =  StackInteger.COLUMNS_48WELL + 
	                       random.nextInt(StackInteger.COLUMNS_1536WELL - 
			               StackInteger.COLUMNS_48WELL + 1);
            
            StackInteger randomStack = new StackInteger(rows, columns);
            
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
    	for(PlateInteger[] array : arrays) {
    		StackInteger stack = new StackInteger(array);
    		assertEquals(stack.size(), array.length);
    	}
    }
    
    /**
     * Tests the to array method.
     */
    @Test
    public void testToArray() {
    	
    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		PlateInteger[] stackArray = stack.toArray();
    		
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

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	int current = 0;
	    	Set<PlateInteger> testSet = stack.getAll();
	
	    	int index = 1 + random.nextInt(testSet.size() - 2);

	        Iterator<PlateInteger> iter = stack.iterator();
	        
	        while(current < index) {
	        	current++;
	        	iter.next();
	        }
	        
	        PlateInteger toHigher = iter.next();
	        PlateInteger toReturn = iter.next();
	        PlateInteger input = new PlateInteger(toReturn.rows(), toReturn.columns() - 1);
	        PlateInteger outside = new PlateInteger(stack.rows() + 1, stack.columns());
	         
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
    	
    	for(PlateInteger[] array : arrays) {
    	
    		StackInteger stack = new StackInteger(array);
    		
	    	int current = 0;
	    	int index = 1 + random.nextInt(stack.size() - 2);
	        
	        Iterator<PlateInteger> iter = stack.iterator();
	        
	        while(current < index && iter.hasNext()) {
	        	current++;
	        	iter.next();
	        }

	        PlateInteger toReturn = iter.next();
	        PlateInteger toFloor = iter.next();
	        PlateInteger input = new PlateInteger(toFloor.rows(), toFloor.columns() - 1);
	        PlateInteger outside = new PlateInteger(stack.rows() - 1, stack.columns());
	        
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
    	
    	for(PlateInteger[] array : arrays) {
    
    		StackInteger stack = new StackInteger(array);
    	
	    	PlateInteger first = stack.first();
	    	PlateInteger polled = stack.pollFirst();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(first, polled);
    	}
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	for(PlateInteger[] array : arrays) {
    	    
    		StackInteger stack = new StackInteger(array);
    	
	    	PlateInteger last = stack.last();
	    	PlateInteger polled = stack.pollLast();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(last, polled);
    	}
    	
    }  
    
    /**
     * Tests the iterator getter.
     */
    @Test
    public void testIterator() {

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
	    	Iterator<PlateInteger> iter = stack.iterator();
	    	
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
    	
    	for(PlateInteger[] array : arrays) {

    		StackInteger stack = new StackInteger(array);
	    	Iterator<PlateInteger> iter = stack.descendingIterator();
	    	
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
    	
    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	Set<PlateInteger> reversed = stack.descendingSet();
	
	    	Iterator<PlateInteger> iter = stack.descendingIterator();
	    	Iterator<PlateInteger> iterReversed = reversed.iterator();
	    	
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
 	
    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
    		Iterator<PlateInteger> iter = stack.iterator();
    		assertEquals(iter.next(), stack.first());
    
    	}  	
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {
    	
    	for(PlateInteger[] array : arrays) {
        
    		StackInteger stack = new StackInteger(array);
    		
    		Iterator<PlateInteger> iter = stack.iterator();
    		PlateInteger last = null;
    	
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

    	for(PlateInteger[] array : arrays) {
    	
	    	StackInteger stack = new StackInteger(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<PlateInteger> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        PlateInteger toCeiling = iter.next();
	        PlateInteger outside = new PlateInteger(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toCeiling), toCeiling);
	        assertNull(stack.ceiling(outside));
    	}
        
    }
    
    /**
     * Tests the floor method.
     */
    @Test
    public void testFloor() {

    	for(PlateInteger[] array : arrays) {
        	
	    	StackInteger stack = new StackInteger(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<PlateInteger> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        PlateInteger toFloor = iter.next();
	        PlateInteger outside = new PlateInteger(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toFloor), toFloor);
	        assertNull(stack.ceiling(outside));
    	}
    	
    }
    
    /**
     * Tests the head set method.
     */
    @Test
    public void testHeadSet() {
    	
    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	Set<PlateInteger> testSet = stack.getAll();
	    	
	        PlateInteger random = (PlateInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateInteger> headSet = stack.headSet(random);
	    	Set<PlateInteger> subSet = stack.subSet(stack.first(), random);
	    	
	    	assertEquals(headSet, subSet);
    	}
    }
    
    /**
     * Tests the head set inclusive method.
     */
    @Test
    public void testHeadSetInclusive() {
    	
    	for(PlateInteger[] array : arrays) {
	    	
	    	StackInteger stack = new StackInteger(array);
	    	
	    	Set<PlateInteger> testSet = stack.getAll();
	    	
	    	PlateInteger random = (PlateInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateInteger> headSetTrue = stack.headSet(random, true);
	    	Set<PlateInteger> subSetTrue = stack.subSet(stack.first(), true, random, true);
	    	Set<PlateInteger> headSetFalse = stack.headSet(random, false);
	    	Set<PlateInteger> subSetFalse = stack.subSet(stack.first(), true, random, false);
	    	
	    	assertEquals(headSetTrue, subSetTrue);
	    	assertEquals(headSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the tail set method.
     */
    @Test
    public void testTailSet() {

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	Set<PlateInteger> testSet = stack.getAll();
	    	
	    	PlateInteger random = (PlateInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateInteger> tailSet = stack.tailSet(random);
	    	Set<PlateInteger> subSet = stack.subSet(random, true, stack.last(), true);
	
	    	assertEquals(tailSet, subSet);
    	}
    }
    
    /**
     * Tests the tail set inclusive method.
     */
    @Test
    public void testTailSetInclusive() {

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	Set<PlateInteger> testSet = stack.getAll();
	    	
	    	PlateInteger random = (PlateInteger) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateInteger> tailSetTrue = stack.tailSet(random, true);
	    	Set<PlateInteger> subSetTrue = stack.subSet(random, true, stack.last(), true);
	    	Set<PlateInteger> tailSetFalse = stack.tailSet(random, false);
	    	Set<PlateInteger> subSetFalse = stack.subSet(random, false, stack.last(), true);
	    	
	    	assertEquals(tailSetTrue, subSetTrue);
	    	assertEquals(tailSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the inclusive subset method.
     */
    @Test
    public void testSubSetInclusive() {

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;
	    	
	    	Set<PlateInteger> subSetTrueTrue = new HashSet<PlateInteger>();
	    	Set<PlateInteger> subSetTrueFalse = new HashSet<PlateInteger>();
	    	Set<PlateInteger> subSetFalseTrue = new HashSet<PlateInteger>();;
	    	Set<PlateInteger> subSetFalseFalse = new HashSet<PlateInteger>();;
	    	
	    	PlateInteger startingPlate = null;
	    	PlateInteger endingPlate = null;
	    	
	    	Iterator<PlateInteger> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		PlateInteger inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSetTrueTrue.add(new PlateInteger(inclusive));
	    		    subSetTrueFalse.add(new PlateInteger(inclusive));
	    		    startingPlate = new PlateInteger(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		PlateInteger inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new PlateInteger(inclusive);
	    		    subSetTrueTrue.add(new PlateInteger(inclusive));
	    		    subSetFalseTrue.add(new PlateInteger(inclusive));
	    		    break;
	    		    
	    		} else {
	    			
	    			subSetTrueTrue.add(new PlateInteger(inclusive));
	    			subSetTrueFalse.add(new PlateInteger(inclusive));
	    			subSetFalseTrue.add(new PlateInteger(inclusive));
	    			subSetFalseFalse.add(new PlateInteger(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}

	    	Set<PlateInteger> trueTrue = stack.subSet(startingPlate, true, endingPlate, true);
	    	Set<PlateInteger> trueFalse = stack.subSet(startingPlate, true, endingPlate, false);
	    	Set<PlateInteger> falseTrue = stack.subSet(startingPlate, false, endingPlate, true);
	    	Set<PlateInteger> falseFalse = stack.subSet(startingPlate, false, endingPlate, false);
	
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

    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;     	
	    	
	    	Set<PlateInteger> subSet = new TreeSet<PlateInteger>();
	    	
	    	PlateInteger startingPlate = null;
	    	PlateInteger endingPlate = null;
	    	
	    	Iterator<PlateInteger> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		PlateInteger inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSet.add(new PlateInteger(inclusive));
	    		    startingPlate = new PlateInteger(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		PlateInteger inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new PlateInteger(inclusive);
	    		    break;
	    		    
	    		} else {
	    			
	    			subSet.add(new PlateInteger(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}
	    	
	    	Set<PlateInteger> toCompare = stack.subSet(startingPlate, endingPlate);
	
	    	assertEquals(toCompare, subSet);
    	}
    }

    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {
    	
    	for(PlateInteger[] array : arrays) {
    		
    		StackInteger stack = new StackInteger(array);
    		
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
    	
        if((rows == StackInteger.ROWS_6WELL && columns == StackInteger.COLUMNS_6WELL) ||
          (rows == StackInteger.ROWS_12WELL && columns == StackInteger.COLUMNS_12WELL) ||
          (rows == StackInteger.ROWS_24WELL && columns == StackInteger.COLUMNS_24WELL) ||
          (rows == StackInteger.ROWS_48WELL && columns == StackInteger.COLUMNS_48WELL) ||
          (rows == StackInteger.ROWS_96WELL && columns == StackInteger.COLUMNS_96WELL) ||
          (rows == StackInteger.ROWS_384WELL && columns == StackInteger.COLUMNS_384WELL) ||
          (rows == StackInteger.ROWS_1536WELL && columns == StackInteger.COLUMNS_1536WELL)) {
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
    	
        if(rows == StackInteger.ROWS_6WELL && 
           columns == StackInteger.COLUMNS_6WELL) {

            descriptor = "6-Well";

        } else if(rows == StackInteger.ROWS_12WELL && 
                  columns == StackInteger.COLUMNS_12WELL) {

            descriptor = "12-Well";
        
        } else if(rows == StackInteger.ROWS_24WELL && 
                  columns == StackInteger.COLUMNS_24WELL) {

            descriptor = "24-Well";
        
        } else if(rows == StackInteger.ROWS_48WELL && 
                  columns == StackInteger.COLUMNS_48WELL) {

            descriptor = "48-Well";
        
        } else if(rows == StackInteger.ROWS_96WELL && 
                  columns == StackInteger.COLUMNS_96WELL) {

            descriptor = "96-Well";
        
        } else if(rows == StackInteger.ROWS_384WELL && 
                  columns == StackInteger.COLUMNS_384WELL) {

            descriptor = "384-Well";
        
        } else if(rows == StackInteger.ROWS_1536WELL && 
                  columns == StackInteger.COLUMNS_1536WELL) {

            descriptor = "1536-Well";
        
        } else {

            descriptor = "Custom Stack: " + rows + "x" + columns;
            
        }

        this.descriptors.add(descriptor);
    }
}
