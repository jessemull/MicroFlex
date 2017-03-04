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

import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.StackDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the double stack plate 
 * class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StackDoubleTest {

	/* ---------------------------- Local fields ---------------------------- */
	
    /* Minimum and maximum values for random well and lists */
	
	private Double minValue = new Double(-1000000000);   // Minimum double value for wells
	private Double maxValue = new Double(1000000000);    // Maximum double value for wells
	private int minLength = 100;                         // Minimum well set length
	private int maxLength = 1000;                        // Maximum well set length
	private int groupNumber = 0;                         // Group number
	private int minRow = 0;								 // Minimum row number
	private int minColumn = 1;							 // Minimum column number
	private int minPlate = 10;                           // Plate minimum
	private int maxPlate = 25;                           // Plate maximum
	private Random random = new Random();                // Generates random integers
	
	/* Random objects and numbers for testing */

	private int rows;
	private int columns;
	private int length;
	private int plateNumber;
	private StackDouble testStack;
	private List<PlateDouble[]> arrays = new ArrayList<PlateDouble[]>();
	private List<TreeSet<PlateDouble>> collections = new ArrayList<TreeSet<PlateDouble>>();
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
		
		rows = PlateDouble.ROWS_48WELL + random.nextInt(PlateDouble.ROWS_1536WELL - 
	           PlateDouble.ROWS_48WELL + 1);

		columns =  PlateDouble.COLUMNS_48WELL + random.nextInt(PlateDouble.COLUMNS_1536WELL - 
		           PlateDouble.COLUMNS_48WELL + 1);
		
		plateNumber = minPlate + random.nextInt(maxPlate - minPlate + 1);
		
    	length = rows * columns / 5;
    	
    	testStack = RandomUtil.randomStackDouble(rows, columns, minValue, 
    			maxValue, minLength, maxLength, 0, length, "TestStack", plateNumber);
    	
    	for(int i = 0; i < 25; i++) {
    		
    		TreeSet<PlateDouble> list = new TreeSet<PlateDouble>();
    		PlateDouble[] array = new PlateDouble[20];
    		
    		for(int j = 0; j < 20; j++) {
    			PlateDouble plate = RandomUtil.randomPlateDouble(
            			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "PlateDouble" + j);
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

    	StackDouble stack = new StackDouble(PlateDouble.PLATE_96WELL);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), PlateDouble.ROWS_96WELL);
        assertEquals(stack.columns(), PlateDouble.COLUMNS_96WELL);      
        assertEquals(stack.type(), PlateDouble.PLATE_96WELL);
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
        assertEquals(stack.descriptor(), "96-Well");
        assertEquals(stack.label(), "StackDouble");
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
    	
    	StackDouble stack = new StackDouble(rows, columns);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackDouble");
        assertEquals(stack.size(), 0);
        assertTrue(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a type constant and label.
     */
	@Test
    public void testConstructorTypeLabel() {
    	
    	String label = "TestLabel";
    	
    	StackDouble stack = new StackDouble(StackDouble.PLATE_384WELL, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), StackDouble.ROWS_384WELL);
        assertEquals(stack.columns(), StackDouble.COLUMNS_384WELL);      
        assertEquals(stack.type(), StackDouble.PLATE_384WELL);
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
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
    	
    	StackDouble stack = new StackDouble(rows, columns, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
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
    	
    	PlateDouble plate = RandomUtil.randomPlateDouble(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, "StackDouble");
    	
    	StackDouble stack = new StackDouble(plate);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
        assertEquals(stack.descriptor(), descriptor);
        assertEquals(stack.label(), "StackDouble");
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
    	
    	PlateDouble plate = RandomUtil.randomPlateDouble(
    			rows, columns, minValue, maxValue, minLength, maxLength, groupNumber, length, label);
    	
    	StackDouble stack = new StackDouble(plate, label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
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
    	
    	StackDouble stack = new StackDouble(collections.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackDouble");
        assertEquals(stack.size(), collections.get(0).size());
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate collection and label.
     */
    @Test
    public void testConstructorCollectionLabel() {

    	String label = "TestLabel";
    	
    	StackDouble stack = new StackDouble(collections.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
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
    
    	StackDouble stack = new StackDouble(arrays.get(0));
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
        assertEquals(stack.descriptor(), descriptors.get(0));
        assertEquals(stack.label(), "StackDouble");
        assertEquals(stack.size(), arrays.get(0).length);
        assertFalse(stack.getAll().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate array and a label.
     */
    @Test
    public void testConstructorArrayLabel() {

    	String label = "TestLabel";
    	
    	StackDouble stack = new StackDouble(arrays.get(0), label);
        assertNotNull(stack);
        
        assertEquals(stack.rows(), rows);
        assertEquals(stack.columns(), columns);      
        assertEquals(stack.dataType(), WellDouble.DOUBLE);
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
    	
    	StackDouble sixWell = new StackDouble(StackDouble.PLATE_6WELL);
    	StackDouble twelveWell = new StackDouble(StackDouble.PLATE_12WELL);
    	StackDouble twentyFourWell = new StackDouble(StackDouble.PLATE_24WELL);
    	StackDouble fortyEightWell = new StackDouble(StackDouble.PLATE_48WELL);
    	StackDouble ninetySixWell = new StackDouble(StackDouble.PLATE_96WELL);
    	StackDouble threeEightyFourWell = new StackDouble(StackDouble.PLATE_384WELL);
    	StackDouble fifteenThirtySixWell = new StackDouble(StackDouble.PLATE_1536WELL);
    	
    	assertEquals(sixWell.rows(), StackDouble.ROWS_6WELL);
    	assertEquals(sixWell.columns(), StackDouble.COLUMNS_6WELL);
    	assertEquals(sixWell.type(), StackDouble.PLATE_6WELL);
    	
    	assertEquals(twelveWell.rows(), StackDouble.ROWS_12WELL);
    	assertEquals(twelveWell.columns(), StackDouble.COLUMNS_12WELL);
    	assertEquals(twelveWell.type(), StackDouble.PLATE_12WELL);
    	
    	assertEquals(twentyFourWell.rows(), StackDouble.ROWS_24WELL);
    	assertEquals(twentyFourWell.columns(), StackDouble.COLUMNS_24WELL);
    	assertEquals(twentyFourWell.type(), StackDouble.PLATE_24WELL);
    	
    	assertEquals(fortyEightWell.rows(), StackDouble.ROWS_48WELL);
    	assertEquals(fortyEightWell.columns(), StackDouble.COLUMNS_48WELL);
    	assertEquals(fortyEightWell.type(), StackDouble.PLATE_48WELL);
    	
    	assertEquals(ninetySixWell.rows(), StackDouble.ROWS_96WELL);
    	assertEquals(ninetySixWell.columns(), StackDouble.COLUMNS_96WELL);
    	assertEquals(ninetySixWell.type(), StackDouble.PLATE_96WELL);
    	
    	assertEquals(threeEightyFourWell.rows(), StackDouble.ROWS_384WELL);
    	assertEquals(threeEightyFourWell.columns(), StackDouble.COLUMNS_384WELL);
    	assertEquals(threeEightyFourWell.type(), StackDouble.PLATE_384WELL);
    	
    	assertEquals(fifteenThirtySixWell.rows(), StackDouble.ROWS_1536WELL);
    	assertEquals(fifteenThirtySixWell.columns(), StackDouble.COLUMNS_1536WELL);
    	assertEquals(fifteenThirtySixWell.type(), StackDouble.PLATE_1536WELL);
    }
    
    /* ------------------- Tests methods for adding plates ------------------ */
    
    /**
     * Tests the add method using a plate.
     */
    @Test
    public void testAddPlate() {

    	for(PlateDouble[] plates : this.arrays) {
    	
	        testStack.clear();
	    	
	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateDouble plate = plates[i];
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

    	for(Collection<PlateDouble> collection : this.collections) {
        	
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
		
        for(PlateDouble[] array : this.arrays) {
        	
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
    	
    	for(PlateDouble[] plates : this.arrays) {
        	
	        StackDouble stack = new StackDouble(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateDouble plate = plates[i];	
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

    	for(Collection<PlateDouble> collection : collections) {
        	
	        StackDouble stack = new StackDouble(testStack);
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

        for(PlateDouble[] array : arrays) {
        	
	        StackDouble stack = new StackDouble(testStack);
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
    	
    	for(PlateDouble[] plates : this.arrays) {
        	
	        StackDouble stack = new StackDouble(plates);

	    	for(int i = 0; i < plates.length; i++) {
	    		
	    		PlateDouble plate = plates[i];	
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

        for(PlateDouble[] array : arrays) {

        	List<String> labels = new ArrayList<String>();
        	
        	for(int i = 0; i < array.length; i++) {
        		String label = "TestPlate" + i;
        		array[i].setLabel(label);
        		labels.add(label);
        	}
 
	        StackDouble stack = new StackDouble(testStack);
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
    	
    	StackDouble clone = new StackDouble(testStack);
    	clone.clear();
    	
    	assertTrue(clone.isEmpty());
    	assertTrue(clone.getAll().size() == 0);
    	
    	for(PlateDouble plate : testStack) {
    		assertFalse(clone.contains(plate));
    	}
    	
    }
    
    /*------------------ Test methods for replacing plates -------------------*/
    
    /**
     * Tests the replace method using a plate.
     */
    @Test
    public void testReplacePlate() {
    	
    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);   		
    		PlateDouble plate = new PlateDouble(array[0]);
    		
    		plate.clearWells();
    		plate.clearGroups();
    		
    		WellSetDouble set = RandomUtil.randomWellSetDouble(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellSetDouble groupSet = RandomUtil.randomWellSetDouble(
    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), minLength, maxLength);
    		WellList groups = groupSet.wellList();
    		
    		plate.addWells(set);
    		plate.addGroups(groups);

    		stack.replace(plate);

    		PlateDouble returned = stack.get(plate);
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

    	for(Collection<PlateDouble> collection : collections) {

    		StackDouble stack = new StackDouble(collection);   		
    		TreeSet<PlateDouble> inputSet = new TreeSet<PlateDouble>();
    		
    		for(PlateDouble toReplace : collection) {
	    		
    			PlateDouble plate = new PlateDouble(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSetDouble set = RandomUtil.randomWellSetDouble(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSetDouble groupSet = RandomUtil.randomWellSetDouble(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<PlateDouble> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<PlateDouble> iterInput = inputSet.iterator();
    		Iterator<PlateDouble> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			PlateDouble plateInput = iterInput.next();
    			PlateDouble plateReturned = iterReturned.next();

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

    	for(PlateDouble[] array : arrays) {

    		StackDouble stack = new StackDouble(array);   		
    		TreeSet<PlateDouble> inputSet = new TreeSet<PlateDouble>();
    		
    		for(PlateDouble toReplace : array) {
	    		
    			PlateDouble plate = new PlateDouble(toReplace);

	    		plate.clearWells();
	    		plate.clearGroups();
	    		
	    		WellSetDouble set = RandomUtil.randomWellSetDouble(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellSetDouble groupSet = RandomUtil.randomWellSetDouble(
	    				minValue, maxValue, minRow, plate.rows(), minColumn, plate.columns(), 1, 5);
	    		WellList groups = groupSet.wellList();
	    		
	    		plate.addWells(set);
	    		plate.addGroups(groups);
	    		
	    		inputSet.add(plate);

    		}

    		stack.replace(inputSet);

    		Set<PlateDouble> returned = stack.get(inputSet);
    		assertEquals(returned, inputSet);
    	
    		Iterator<PlateDouble> iterInput = inputSet.iterator();
    		Iterator<PlateDouble> iterReturned = returned.iterator();
 
    		while(iterInput.hasNext()) {
    			
    			PlateDouble plateInput = iterInput.next();
    			PlateDouble plateReturned = iterReturned.next();

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
    
        for(PlateDouble[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackDouble stack = new StackDouble(array);   
    		PlateDouble plate = new PlateDouble(array[index]);
    		
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

    	for(Collection<PlateDouble> collection : collections) {
    		
    		StackDouble stack = new StackDouble(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		TreeSet<PlateDouble> set = new TreeSet<PlateDouble>();
    		
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
    		
    		for(PlateDouble plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a plate array.
     */
    @Test
    public void testRetainArray() {

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		TreeSet<PlateDouble> set = new TreeSet<PlateDouble>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		PlateDouble[] retainArray = set.toArray(new PlateDouble[set.size()]);
    		stack.retain(retainArray);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateDouble plate : set) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}
    	
    }
    
    /**
     * Tests the retain method using a label.
     */
    @Test
    public void testRetainLabel() {

    	for(PlateDouble[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackDouble stack = new StackDouble(array);   
    		PlateDouble plate = new PlateDouble(array[index]);
    		
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
    	
    	for(Collection<PlateDouble> collection : collections) {
    		
    		StackDouble stack = new StackDouble(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		TreeSet<PlateDouble> set = new TreeSet<PlateDouble>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateDouble plate : set) {
    			labelList.add(plate.label());
    		}
    		
    		stack.retain(labelList);
    		
    		assertEquals(stack.size(), set.size());
    		assertTrue(stack.contains(set));
    		
    		for(PlateDouble plate : set) {
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

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
    		for(PlateDouble plate : array) {
    			assertEquals(stack.get(plate.label()), plate);
    		}
    	}
    }
    
    /**
     * Tests the get method using a list of labels.
     */
    @Test
    public void testGetLabelList() {

    	for(Collection<PlateDouble> collection : collections) {
    		
    		StackDouble stack = new StackDouble(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		Set<PlateDouble> set = new TreeSet<PlateDouble>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateDouble plate : set) {
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

        for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
    		for(PlateDouble plate : array) {
    			assertEquals(stack.get(plate), plate);
    		}
    	}

    }
    
    /**
     * Tests the get method using a plate collection.
     */
    public void testGetCollection() {
    	
    	for(Collection<PlateDouble> collection : collections) {
    		
    		StackDouble stack = new StackDouble(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		Set<PlateDouble> set = new TreeSet<PlateDouble>();
    		
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

        for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		int begin = 1 + random.nextInt(array.length / 2 - 1);
    		int end = begin + random.nextInt(array.length - begin) + 1;
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		Set<PlateDouble> set = new TreeSet<PlateDouble>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}

    		PlateDouble[] getArray = set.toArray(new PlateDouble[set.size()]);
    		
    		assertEquals(stack.get(getArray), set);
    		
    	}

    }
    
    /**
     * Tests the get all method.
     */
    @Test
    public void testGetAll() {

    	for(Collection<PlateDouble> collection : collections) {

    		StackDouble stack = new StackDouble(collection);   
    		assertEquals(stack.getAll(), collection);
    	}
    	
    }
    
    /*------------------- Tests methods for plate lookup ---------------------*/

    /**
     * Tests the contains method using a plate.
     */
    @Test
    public void testContainsPlate() {
    	
    	for(PlateDouble[] array : arrays) {

        	int index = 1 + random.nextInt(array.length - 1);
        	
    		StackDouble stack = new StackDouble(array);   
    		PlateDouble plate = new PlateDouble(array[index]);

    		assertTrue(stack.contains(plate));
    	}
    	
    }
    
    /**
     * Tests the contains method using a plate collection.
     */
    @Test
    public void testContainsCollection() {

        for(Collection<PlateDouble> collection : collections) {
    		
    		StackDouble stack = new StackDouble(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		Set<PlateDouble> set = new TreeSet<PlateDouble>();
    		
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

        for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		int begin = 1 + random.nextInt(array.length - 1);
    		int end = begin + random.nextInt(array.length - begin);
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		Set<PlateDouble> set = new TreeSet<PlateDouble>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		PlateDouble[] getArray = set.toArray(new PlateDouble[set.size()]);
    		
    		assertTrue(stack.contains(getArray));
    		
    	}

    }
    
    /**
     * Tests the contains method using a label.
     */
    @Test
    public void testContainsLabel() {

        for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
    		for(PlateDouble plate : array) {
    			assertTrue(stack.contains(plate));
    		}
    	}

    }
    
    /**
     * Tests the contains method using a list of labels.
     */
    @Test
    public void testContainsLabelList() {

        for(Collection<PlateDouble> collection : collections) {
    		
    		StackDouble stack = new StackDouble(collection);
    		int begin = 1 + random.nextInt(collection.size() - 1);
    		int end = begin + random.nextInt(collection.size() - begin) + 1;
    		int index = 0;

    		Iterator<PlateDouble> iter = stack.iterator();
    		Set<PlateDouble> set = new TreeSet<PlateDouble>();
    		
    		while(index < begin) {
    			iter.next();
    			index++;
    		}
    		
    		while(index < end) {
    			set.add(iter.next());
    			index++;
    		}
    		
    		List<String> labelList = new ArrayList<String>();
    		
    		for(PlateDouble plate : set) {
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
    		
    		int rows = PlateDouble.ROWS_48WELL + 
	                 random.nextInt(PlateDouble.ROWS_1536WELL - 
		             PlateDouble.ROWS_48WELL + 1);

    		int columns = 1;
    		
    		StackDouble plate = new StackDouble(rows, columns);
    		
    		assertEquals(plate.rows(), rows);
    		
    	}
    	
    }
    
    /**
     * Tests the columns getter.
     */
    @Test
    public void testColumns() {
    	
        for(int i = 0; i < 100; i++) {
    		
    		int columns = PlateDouble.COLUMNS_48WELL + 
	                 random.nextInt(PlateDouble.COLUMNS_1536WELL - 
		             PlateDouble.COLUMNS_48WELL + 1);

    		int rows = 0;
    		
    		StackDouble stack = new StackDouble(rows, columns);
    		
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
    	    StackDouble stack = new StackDouble(type);
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
    		StackDouble stack = new StackDouble(type);
    		
    		switch(type) {
    		
    		    case StackDouble.PLATE_6WELL:    assertEquals(stack.descriptor(), "6-Well");
    		                                         break;
    		    case StackDouble.PLATE_12WELL:   assertEquals(stack.descriptor(), "12-Well");
                                                     break;
    		    case StackDouble.PLATE_24WELL:   assertEquals(stack.descriptor(), "24-Well");
                                                     break;
    		    case StackDouble.PLATE_48WELL:   assertEquals(stack.descriptor(), "48-Well");
                                                     break;
    		    case StackDouble.PLATE_96WELL:   assertEquals(stack.descriptor(), "96-Well");
                                                     break;
    		    case StackDouble.PLATE_384WELL:  assertEquals(stack.descriptor(), "384-Well");
                                                     break;
    		    case StackDouble.PLATE_1536WELL: assertEquals(stack.descriptor(), "1536-Well");
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		int rows = StackDouble.ROWS_48WELL + 
	                   random.nextInt(StackDouble.ROWS_1536WELL - 
   		               StackDouble.ROWS_48WELL + 1);

            int columns =  StackDouble.COLUMNS_48WELL + 
	                       random.nextInt(StackDouble.COLUMNS_1536WELL - 
			               StackDouble.COLUMNS_48WELL + 1);
            
            PlateDouble randomPlate = new PlateDouble(rows, columns);
            
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
    		StackDouble stack = new StackDouble(0);
    		assertEquals(stack.dataType(), PlateDouble.PLATE_DOUBLE);
    	}
    }
    
    /**
     * Tests the data type string getter.
     */
    @Test
    public void testDataTypeString() {
    	for(int i = 0; i < 100; i++) {
    		StackDouble stack = new StackDouble(0);
    		assertEquals(stack.dataTypeString(), "Double");
    	}
    }
    
    /**
     * Tests the label setter.
     */
    @Test
    public void testSetLabel() {
    	for(int i = 0; i < 100; i++) {
    		String label = "TestLabel";
    		StackDouble stack = new StackDouble(0);
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
    		StackDouble stack = new StackDouble(0);
    		assertEquals(stack.label(), "StackDouble");
    	}
    }

    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		StackDouble stack = new StackDouble(type);
    		
    		String toString = "Type: ";
    		
    		switch(type) {
    		
    		    case StackDouble.PLATE_6WELL:    toString += "6-Well Label: ";
    		                                         break;
    		    case StackDouble.PLATE_12WELL:   toString += "12-Well Label: ";
                                                     break;
    		    case StackDouble.PLATE_24WELL:   toString += "24-Well Label: ";
                                                     break;
    		    case StackDouble.PLATE_48WELL:   toString += "48-Well Label: ";
                                                     break;
    		    case StackDouble.PLATE_96WELL:   toString += "96-Well Label: ";
                                                     break;
    		    case StackDouble.PLATE_384WELL:  toString += "384-Well Label: ";
                                                     break;
    		    case StackDouble.PLATE_1536WELL: toString += "1536-Well Label: ";
                                                     break;
                default: throw new IllegalArgumentException("Invalid stack type constant.");
    		
    		}
    		
    		toString += stack.label();
    		
    		assertEquals(toString, stack.toString());
    		
    		int rows = StackDouble.ROWS_48WELL + 
	                   random.nextInt(StackDouble.ROWS_1536WELL - 
   		               StackDouble.ROWS_48WELL + 1);

            int columns =  StackDouble.COLUMNS_48WELL + 
	                       random.nextInt(StackDouble.COLUMNS_1536WELL - 
			               StackDouble.COLUMNS_48WELL + 1);
            
            StackDouble randomStack = new StackDouble(rows, columns);
            
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
    	for(PlateDouble[] array : arrays) {
    		StackDouble stack = new StackDouble(array);
    		assertEquals(stack.size(), array.length);
    	}
    }
    
    /**
     * Tests the to array method.
     */
    @Test
    public void testToArray() {
    	
    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		PlateDouble[] stackArray = stack.toArray();
    		
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

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	int current = 0;
	    	Set<PlateDouble> testSet = stack.getAll();
	
	    	int index = 1 + random.nextInt(testSet.size() - 2);

	        Iterator<PlateDouble> iter = stack.iterator();
	        
	        while(current < index) {
	        	current++;
	        	iter.next();
	        }
	        
	        PlateDouble toHigher = iter.next();
	        PlateDouble toReturn = iter.next();
	        PlateDouble input = new PlateDouble(toReturn.rows(), toReturn.columns() - 1);
	        PlateDouble outside = new PlateDouble(stack.rows() + 1, stack.columns());
	         
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
    	
    	for(PlateDouble[] array : arrays) {
    	
    		StackDouble stack = new StackDouble(array);
    		
	    	int current = 0;
	    	int index = 1 + random.nextInt(stack.size() - 2);
	        
	        Iterator<PlateDouble> iter = stack.iterator();
	        
	        while(current < index && iter.hasNext()) {
	        	current++;
	        	iter.next();
	        }

	        PlateDouble toReturn = iter.next();
	        PlateDouble toFloor = iter.next();
	        PlateDouble input = new PlateDouble(toFloor.rows(), toFloor.columns() - 1);
	        PlateDouble outside = new PlateDouble(stack.rows() - 1, stack.columns());
	        
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
    	
    	for(PlateDouble[] array : arrays) {
    
    		StackDouble stack = new StackDouble(array);
    	
	    	PlateDouble first = stack.first();
	    	PlateDouble polled = stack.pollFirst();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(first, polled);
    	}
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	for(PlateDouble[] array : arrays) {
    	    
    		StackDouble stack = new StackDouble(array);
    	
	    	PlateDouble last = stack.last();
	    	PlateDouble polled = stack.pollLast();
	    	
	    	assertFalse(stack.contains(polled));
	    	assertEquals(last, polled);
    	}
    	
    }  
    
    /**
     * Tests the iterator getter.
     */
    @Test
    public void testIterator() {

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
	    	Iterator<PlateDouble> iter = stack.iterator();
	    	
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
    	
    	for(PlateDouble[] array : arrays) {

    		StackDouble stack = new StackDouble(array);
	    	Iterator<PlateDouble> iter = stack.descendingIterator();
	    	
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
    	
    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	Set<PlateDouble> reversed = stack.descendingSet();
	
	    	Iterator<PlateDouble> iter = stack.descendingIterator();
	    	Iterator<PlateDouble> iterReversed = reversed.iterator();
	    	
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
 	
    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
    		Iterator<PlateDouble> iter = stack.iterator();
    		assertEquals(iter.next(), stack.first());
    
    	}  	
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {
    	
    	for(PlateDouble[] array : arrays) {
        
    		StackDouble stack = new StackDouble(array);
    		
    		Iterator<PlateDouble> iter = stack.iterator();
    		PlateDouble last = null;
    	
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

    	for(PlateDouble[] array : arrays) {
    	
	    	StackDouble stack = new StackDouble(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<PlateDouble> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        PlateDouble toCeiling = iter.next();
	        PlateDouble outside = new PlateDouble(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toCeiling), toCeiling);
	        assertNull(stack.ceiling(outside));
    	}
        
    }
    
    /**
     * Tests the floor method.
     */
    @Test
    public void testFloor() {

    	for(PlateDouble[] array : arrays) {
        	
	    	StackDouble stack = new StackDouble(array);
	    	
	        int current = 0;
	    	int index = random.nextInt((stack.size() - 2) + 1);    
	         
	        Iterator<PlateDouble> iter = stack.iterator();
	         
	        while(current < index) {
	            iter.next();
	            current++;
	        }
	         
	        PlateDouble toFloor = iter.next();
	        PlateDouble outside = new PlateDouble(stack.rows() + 1, stack.columns());

	        assertEquals(stack.ceiling(toFloor), toFloor);
	        assertNull(stack.ceiling(outside));
    	}
    	
    }
    
    /**
     * Tests the head set method.
     */
    @Test
    public void testHeadSet() {
    	
    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	Set<PlateDouble> testSet = stack.getAll();
	    	
	        PlateDouble random = (PlateDouble) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateDouble> headSet = stack.headSet(random);
	    	Set<PlateDouble> subSet = stack.subSet(stack.first(), random);
	    	
	    	assertEquals(headSet, subSet);
    	}
    }
    
    /**
     * Tests the head set inclusive method.
     */
    @Test
    public void testHeadSetInclusive() {
    	
    	for(PlateDouble[] array : arrays) {
	    	
	    	StackDouble stack = new StackDouble(array);
	    	
	    	Set<PlateDouble> testSet = stack.getAll();
	    	
	    	PlateDouble random = (PlateDouble) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateDouble> headSetTrue = stack.headSet(random, true);
	    	Set<PlateDouble> subSetTrue = stack.subSet(stack.first(), true, random, true);
	    	Set<PlateDouble> headSetFalse = stack.headSet(random, false);
	    	Set<PlateDouble> subSetFalse = stack.subSet(stack.first(), true, random, false);
	    	
	    	assertEquals(headSetTrue, subSetTrue);
	    	assertEquals(headSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the tail set method.
     */
    @Test
    public void testTailSet() {

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	Set<PlateDouble> testSet = stack.getAll();
	    	
	    	PlateDouble random = (PlateDouble) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateDouble> tailSet = stack.tailSet(random);
	    	Set<PlateDouble> subSet = stack.subSet(random, true, stack.last(), true);
	
	    	assertEquals(tailSet, subSet);
    	}
    }
    
    /**
     * Tests the tail set inclusive method.
     */
    @Test
    public void testTailSetInclusive() {

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	Set<PlateDouble> testSet = stack.getAll();
	    	
	    	PlateDouble random = (PlateDouble) RandomUtil.randomObject(testSet);
	    	
	    	Set<PlateDouble> tailSetTrue = stack.tailSet(random, true);
	    	Set<PlateDouble> subSetTrue = stack.subSet(random, true, stack.last(), true);
	    	Set<PlateDouble> tailSetFalse = stack.tailSet(random, false);
	    	Set<PlateDouble> subSetFalse = stack.subSet(random, false, stack.last(), true);
	    	
	    	assertEquals(tailSetTrue, subSetTrue);
	    	assertEquals(tailSetFalse, subSetFalse);
    	}
    }
    
    /**
     * Tests the inclusive subset method.
     */
    @Test
    public void testSubSetInclusive() {

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;
	    	
	    	Set<PlateDouble> subSetTrueTrue = new HashSet<PlateDouble>();
	    	Set<PlateDouble> subSetTrueFalse = new HashSet<PlateDouble>();
	    	Set<PlateDouble> subSetFalseTrue = new HashSet<PlateDouble>();;
	    	Set<PlateDouble> subSetFalseFalse = new HashSet<PlateDouble>();;
	    	
	    	PlateDouble startingPlate = null;
	    	PlateDouble endingPlate = null;
	    	
	    	Iterator<PlateDouble> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		PlateDouble inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSetTrueTrue.add(new PlateDouble(inclusive));
	    		    subSetTrueFalse.add(new PlateDouble(inclusive));
	    		    startingPlate = new PlateDouble(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		PlateDouble inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new PlateDouble(inclusive);
	    		    subSetTrueTrue.add(new PlateDouble(inclusive));
	    		    subSetFalseTrue.add(new PlateDouble(inclusive));
	    		    break;
	    		    
	    		} else {
	    			
	    			subSetTrueTrue.add(new PlateDouble(inclusive));
	    			subSetTrueFalse.add(new PlateDouble(inclusive));
	    			subSetFalseTrue.add(new PlateDouble(inclusive));
	    			subSetFalseFalse.add(new PlateDouble(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}

	    	Set<PlateDouble> trueTrue = stack.subSet(startingPlate, true, endingPlate, true);
	    	Set<PlateDouble> trueFalse = stack.subSet(startingPlate, true, endingPlate, false);
	    	Set<PlateDouble> falseTrue = stack.subSet(startingPlate, false, endingPlate, true);
	    	Set<PlateDouble> falseFalse = stack.subSet(startingPlate, false, endingPlate, false);
	
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

    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
	    	int current = 0;
	    	int begin = random.nextInt(stack.size() - 2 - 0 + 1) + 0;
	    	int end = random.nextInt(stack.size() - 1 - begin) + begin + 1;     	
	    	
	    	Set<PlateDouble> subSet = new TreeSet<PlateDouble>();
	    	
	    	PlateDouble startingPlate = null;
	    	PlateDouble endingPlate = null;
	    	
	    	Iterator<PlateDouble> iter = stack.iterator();
	    	
	    	while(current <= begin) {
	    		
	    		PlateDouble inclusive = iter.next();
	    		
	    		if(current == begin) {
	    		    
	    			subSet.add(new PlateDouble(inclusive));
	    		    startingPlate = new PlateDouble(inclusive);
	    		    break;
	    		    
	    		}   		
	    		
	    		current++;
	    	}
	    	
	    	while(current < end) {
	    		
	    		PlateDouble inclusive = iter.next();
	    		
	    		if(current + 1 == end) {
	
	    			endingPlate = new PlateDouble(inclusive);
	    		    break;
	    		    
	    		} else {
	    			
	    			subSet.add(new PlateDouble(inclusive));
	    			
	    		}
	    		
	    		current++;
	    	}
	    	
	    	Set<PlateDouble> toCompare = stack.subSet(startingPlate, endingPlate);
	
	    	assertEquals(toCompare, subSet);
    	}
    }

    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {
    	
    	for(PlateDouble[] array : arrays) {
    		
    		StackDouble stack = new StackDouble(array);
    		
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
    	
        if((rows == StackDouble.ROWS_6WELL && columns == StackDouble.COLUMNS_6WELL) ||
          (rows == StackDouble.ROWS_12WELL && columns == StackDouble.COLUMNS_12WELL) ||
          (rows == StackDouble.ROWS_24WELL && columns == StackDouble.COLUMNS_24WELL) ||
          (rows == StackDouble.ROWS_48WELL && columns == StackDouble.COLUMNS_48WELL) ||
          (rows == StackDouble.ROWS_96WELL && columns == StackDouble.COLUMNS_96WELL) ||
          (rows == StackDouble.ROWS_384WELL && columns == StackDouble.COLUMNS_384WELL) ||
          (rows == StackDouble.ROWS_1536WELL && columns == StackDouble.COLUMNS_1536WELL)) {
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
    	
        if(rows == StackDouble.ROWS_6WELL && 
           columns == StackDouble.COLUMNS_6WELL) {

            descriptor = "6-Well";

        } else if(rows == StackDouble.ROWS_12WELL && 
                  columns == StackDouble.COLUMNS_12WELL) {

            descriptor = "12-Well";
        
        } else if(rows == StackDouble.ROWS_24WELL && 
                  columns == StackDouble.COLUMNS_24WELL) {

            descriptor = "24-Well";
        
        } else if(rows == StackDouble.ROWS_48WELL && 
                  columns == StackDouble.COLUMNS_48WELL) {

            descriptor = "48-Well";
        
        } else if(rows == StackDouble.ROWS_96WELL && 
                  columns == StackDouble.COLUMNS_96WELL) {

            descriptor = "96-Well";
        
        } else if(rows == StackDouble.ROWS_384WELL && 
                  columns == StackDouble.COLUMNS_384WELL) {

            descriptor = "384-Well";
        
        } else if(rows == StackDouble.ROWS_1536WELL && 
                  columns == StackDouble.COLUMNS_1536WELL) {

            descriptor = "1536-Well";
        
        } else {

            descriptor = "Custom Stack: " + rows + "x" + columns;
            
        }

        this.descriptors.add(descriptor);
    }
}
