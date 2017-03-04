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

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the big decimal plate class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateBigDecimalTest {
	
	/* ---------------------------- Local fields ---------------------------- */
	
    /* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private BigDecimal minValue = new BigDecimal(-1000000000);   // Minimum big decimal value for wells
	private BigDecimal maxValue = new BigDecimal(1000000000);    // Maximum big decimal value for wells
	private int minLength = 100;                                 // Minimum well set length
	private int maxLength = 1000;                                // Maximum well set length
	private int minRow = 0;                                      // Minimum well row
	private int minColumn = 1;                                   // Minimum well column
	private Random random = new Random();                        // Generates random integers
	
	/* Random plate, well, well list, index and set objects for testing */
	
    private PlateBigDecimal testPlate;
	private List<WellSetBigDecimal> sets = new ArrayList<WellSetBigDecimal>();
	private List<TreeSet<WellBigDecimal>> collections = new ArrayList<TreeSet<WellBigDecimal>>();
	private List<WellBigDecimal[]> arrays = new ArrayList<WellBigDecimal[]>();
	private List<WellList> lists = new ArrayList<WellList>();
	private List<WellBigDecimal> wells = new ArrayList<WellBigDecimal>();
	private List<WellIndex> indices = new ArrayList<WellIndex>();

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
	 * Generates random objects and numbers for testing.
	 */
	@Before
	public void setUp() {

		int rows = PlateBigDecimal.ROWS_48WELL + 
				                 random.nextInt(PlateBigDecimal.ROWS_1536WELL - 
					             PlateBigDecimal.ROWS_48WELL + 1);
		
		int columns =  PlateBigDecimal.COLUMNS_48WELL + 
				                 random.nextInt(PlateBigDecimal.COLUMNS_1536WELL - 
						         PlateBigDecimal.COLUMNS_48WELL + 1);
		
		int length = rows * columns / 5;

		testPlate = RandomUtil.randomPlateBigDecimal(
				rows, columns, minValue, maxValue, minLength, maxLength, 0, length, "TestPlate");
		
	    for(int i = 0; i < 100; i++) { 

			WellSetBigDecimal testSet = RandomUtil.randomWellSetBigDecimal(
					minValue, maxValue, minRow, testPlate.rows(), minColumn, testPlate.columns(), 1, length);
			
			testSet.setLabel("TestSet" + i);
			
			TreeSet<WellBigDecimal> testCollection = (TreeSet<WellBigDecimal>) testSet.allWells();		
			WellBigDecimal[] testArray = testSet.toWellArray();
			WellList testList = testSet.wellList();
			WellBigDecimal testWell = (WellBigDecimal) RandomUtil.randomObject(testCollection);
			WellIndex testIndex = new WellIndex(testWell.row(), testWell.column());

			sets.add(testSet);
			collections.add(testCollection);
			arrays.add(testArray);
			lists.add(testList);
			wells.add(testWell);
			indices.add(testIndex);
			
	    }
	    
	    testPlate.addGroups(lists);
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
	/* -------------------- Tests the plate constructors -------------------- */
	
    /**
     * Tests the plate constructor using a type constant.
     */
	@Test
    public void testPlateBigDecimalType() {
    	
    	PlateBigDecimal plate = new PlateBigDecimal(PlateBigDecimal.PLATE_96WELL);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateBigDecimal.ROWS_96WELL);
        assertEquals(plate.columns(), PlateBigDecimal.COLUMNS_96WELL);      
        assertEquals(plate.type(), PlateBigDecimal.PLATE_96WELL);
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), "96-Well");
        assertEquals(plate.label(), "PlateBigDecimal");
        assertEquals(plate.size(), 0);
        
        assertTrue(plate.allGroups().isEmpty());
        assertTrue(plate.dataSet().isEmpty());
       
    } 
    
    /**
     * Tests the plate constructor using a row and column.
     */
	@Test
    public void testPlateBigDecimalRowColumn() {
    
    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateBigDecimal plate = new PlateBigDecimal(rows, columns);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), descriptor);
        assertEquals(plate.label(), "PlateBigDecimal");
        assertEquals(plate.size(), 0);
        
        assertTrue(plate.allGroups().isEmpty());
        assertTrue(plate.dataSet().isEmpty());
        
    } 

    /**
     * Tests the plate constructor using a plate type constant and label.
     */
	@Test
    public void testPlateBigDecimalTypeLabel() {
    
    	String label = "TestLabel";
    	
    	PlateBigDecimal plate = new PlateBigDecimal(PlateBigDecimal.PLATE_384WELL, label);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateBigDecimal.ROWS_384WELL);
        assertEquals(plate.columns(), PlateBigDecimal.COLUMNS_384WELL);      
        assertEquals(plate.type(), PlateBigDecimal.PLATE_384WELL);
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), "384-Well");
        assertEquals(plate.label(), label);
        assertEquals(plate.size(), 0);
        
        assertTrue(plate.allGroups().isEmpty());
        assertTrue(plate.dataSet().isEmpty());
        
    }

    /**
     * Tests the plate constructor using a column, row and label.
     */
    @Test
    public void testPlateBigDecimalRowColumnLabel() {

    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	String label = "TestLabel";
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateBigDecimal plate = new PlateBigDecimal(rows, columns, label);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), descriptor);
        assertEquals(plate.label(), label);
        assertEquals(plate.size(), 0);
        
        assertTrue(plate.allGroups().isEmpty());
        assertTrue(plate.dataSet().isEmpty());
    }

    /**
     * Tests the plate constructor using a type constant and well set.
     */
    @Test
    public void testPlateBigDecimalTypeSet() {  	

    	int length = PlateBigDecimal.ROWS_48WELL * PlateBigDecimal.COLUMNS_384WELL / 2;
    	
    	WellSetBigDecimal random = RandomUtil.randomWellSetBigDecimal(
    			minValue, maxValue, 0, PlateBigDecimal.ROWS_384WELL, 1, PlateBigDecimal.COLUMNS_384WELL, 1, length);
    	
    	PlateBigDecimal plate = new PlateBigDecimal(PlateBigDecimal.PLATE_384WELL, random);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateBigDecimal.ROWS_384WELL);
        assertEquals(plate.columns(), PlateBigDecimal.COLUMNS_384WELL);      
        assertEquals(plate.type(), PlateBigDecimal.PLATE_384WELL);
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), "384-Well");
        assertEquals(plate.label(), "PlateBigDecimal");
        assertEquals(plate.size(), random.size());
        assertEquals(plate.dataSet(), random);
 
        assertTrue(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
    }

    /**
     * Tests the plate constructor using a type constant, label and well set.
     */
    @Test
    public void testPlateBigDecimalRowColumnSet() {
    	
    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	
        int length = rows * columns / 10;
    	
    	WellSetBigDecimal random = RandomUtil.randomWellSetBigDecimal(
    			minValue, maxValue, 0, rows, 1, columns, 1, length);
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateBigDecimal plate = new PlateBigDecimal(rows, columns, random);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), descriptor);
        assertEquals(plate.label(), "PlateBigDecimal");
        assertEquals(plate.size(), random.size());
        assertEquals(plate.dataSet(), random);
        
        assertTrue(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
        
    }

    /**
     * Tests the plate constructor using a type constant, label and well set.
     */
    @Test
    public void testPlateBigDecimalTypeLabelSet() {

    	String label = "TestLabel";
    	
        int length = PlateBigDecimal.ROWS_48WELL * PlateBigDecimal.COLUMNS_48WELL / 2;
    	
    	WellSetBigDecimal random = RandomUtil.randomWellSetBigDecimal(
    			minValue, maxValue, 0, PlateBigDecimal.ROWS_48WELL, 1, PlateBigDecimal.COLUMNS_48WELL, 1, length); 
    	
    	PlateBigDecimal plate = new PlateBigDecimal(PlateBigDecimal.PLATE_48WELL, label, random);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateBigDecimal.ROWS_48WELL);
        assertEquals(plate.columns(), PlateBigDecimal.COLUMNS_48WELL);      
        assertEquals(plate.type(), PlateBigDecimal.PLATE_48WELL);
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), "48-Well");
        assertEquals(plate.label(), label);
        assertEquals(plate.size(), random.size());
        assertEquals(plate.dataSet(), random);
        
        assertTrue(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
    }

    /**
     * Tests the plate constructor using a row, column, label and well set.
     */
    @Test
    public void testPlateBigDecimalRowColumnLabelSet() {

    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	
    	String label = "TestLabel";
    	
        int length = rows * columns / 10;
    	
    	WellSetBigDecimal random = RandomUtil.randomWellSetBigDecimal(
    			minValue, maxValue, 1, rows, 1, columns, 1, length);
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateBigDecimal plate = new PlateBigDecimal(rows, columns, label, random);
        assertNotNull(plate);

        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), descriptor);
        assertEquals(plate.label(), label);
        assertEquals(plate.size(), random.size());
        assertEquals(plate.dataSet(), random);
        
        assertTrue(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
        
    }
    
    /**
     * Tests the constructor using a plate.
     */
    @Test
    public void testPlateBigDecimalPlate() {

        PlateBigDecimal plate = new PlateBigDecimal(testPlate);
        assertNotNull(plate);

        assertEquals(plate.rows(), testPlate.rows());
        assertEquals(plate.columns(), testPlate.columns());      
        assertEquals(plate.type(), testPlate.type());
        assertEquals(plate.dataType(), WellBigDecimal.BIGDECIMAL);
        assertEquals(plate.descriptor(), testPlate.descriptor());
        assertEquals(plate.label(), testPlate.label());
        assertEquals(plate.size(), testPlate.size());
        assertEquals(plate.dataSet(), testPlate.dataSet());
        
        for(WellSetBigDecimal set : testPlate.allGroups()) {
        	assertTrue(plate.containsGroup(set.wellList()));
        }

        assertFalse(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
    }
    
    /* ---------------------- Constructor Helper Methods ---------------------*/

    /**
     * Tests the private initialize plate type methods by invoking the plate
     * constructor using a plate type.
     */
    @Test
    public void testInitializePlateType() {

    	PlateBigDecimal sixWell = new PlateBigDecimal(PlateBigDecimal.PLATE_6WELL);
    	PlateBigDecimal twelveWell = new PlateBigDecimal(PlateBigDecimal.PLATE_12WELL);
    	PlateBigDecimal twentyFourWell = new PlateBigDecimal(PlateBigDecimal.PLATE_24WELL);
    	PlateBigDecimal fortyEightWell = new PlateBigDecimal(PlateBigDecimal.PLATE_48WELL);
    	PlateBigDecimal ninetySixWell = new PlateBigDecimal(PlateBigDecimal.PLATE_96WELL);
    	PlateBigDecimal threeEightyFourWell = new PlateBigDecimal(PlateBigDecimal.PLATE_384WELL);
    	PlateBigDecimal fifteenThirtySixWell = new PlateBigDecimal(PlateBigDecimal.PLATE_1536WELL);
    	
    	assertEquals(sixWell.rows(), PlateBigDecimal.ROWS_6WELL);
    	assertEquals(sixWell.columns(), PlateBigDecimal.COLUMNS_6WELL);
    	assertEquals(sixWell.type(), PlateBigDecimal.PLATE_6WELL);
    	
    	assertEquals(twelveWell.rows(), PlateBigDecimal.ROWS_12WELL);
    	assertEquals(twelveWell.columns(), PlateBigDecimal.COLUMNS_12WELL);
    	assertEquals(twelveWell.type(), PlateBigDecimal.PLATE_12WELL);
    	
    	assertEquals(twentyFourWell.rows(), PlateBigDecimal.ROWS_24WELL);
    	assertEquals(twentyFourWell.columns(), PlateBigDecimal.COLUMNS_24WELL);
    	assertEquals(twentyFourWell.type(), PlateBigDecimal.PLATE_24WELL);
    	
    	assertEquals(fortyEightWell.rows(), PlateBigDecimal.ROWS_48WELL);
    	assertEquals(fortyEightWell.columns(), PlateBigDecimal.COLUMNS_48WELL);
    	assertEquals(fortyEightWell.type(), PlateBigDecimal.PLATE_48WELL);
    	
    	assertEquals(ninetySixWell.rows(), PlateBigDecimal.ROWS_96WELL);
    	assertEquals(ninetySixWell.columns(), PlateBigDecimal.COLUMNS_96WELL);
    	assertEquals(ninetySixWell.type(), PlateBigDecimal.PLATE_96WELL);
    	
    	assertEquals(threeEightyFourWell.rows(), PlateBigDecimal.ROWS_384WELL);
    	assertEquals(threeEightyFourWell.columns(), PlateBigDecimal.COLUMNS_384WELL);
    	assertEquals(threeEightyFourWell.type(), PlateBigDecimal.PLATE_384WELL);
    	
    	assertEquals(fifteenThirtySixWell.rows(), PlateBigDecimal.ROWS_1536WELL);
    	assertEquals(fifteenThirtySixWell.columns(), PlateBigDecimal.COLUMNS_1536WELL);
    	assertEquals(fifteenThirtySixWell.type(), PlateBigDecimal.PLATE_1536WELL);
    }
    
    /* ----------------------------- Group Tests ---------------------------- */
    
    /**
     * Tests the group addition method using a list of wells.
     */
    @Test
    public void testAddGroupsList() {

    	testPlate.clearGroups();
    	assertTrue(testPlate.allGroups().isEmpty());

    	for(WellList list : lists) {
    		testPlate.addGroups(list);   		
    	}

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list));
    	}

    }
    
    /**
     * Tests the group addition method using a collection.
     */
    @Test
    public void testAddGroupsCollection() {

    	testPlate.clearGroups();
    	assertTrue(testPlate.allGroups().isEmpty());
    	
        testPlate.addGroups(lists);

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    }
    
    /**
     * Tests the group addition method using an array. 
     */
    @Test
    public void testAddGroupsArray() {

    	testPlate.clearGroups();
    	assertTrue(testPlate.allGroups().isEmpty());

        testPlate.addGroups(lists.toArray(new WellList[lists.size()]));

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    }
    
    /* ------------------------- Remove Group Methods ----------------------- */

    /**
     * Tests the remove groups method using a well list.
     */
    @Test
    public void testRemoveGroupsList() {

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	int index = lists.size();
    	
    	for(WellList list : lists) {
    		testPlate.removeGroups(list);
    		assertFalse(testPlate.contains(list));
    		assertEquals(testPlate.allGroups().size(), --index);
    	}
    	
    	assertTrue(testPlate.allGroups().isEmpty());
    }
    
    /**
     * Tests the remove groups method using a collection.
     */
    @Test
    public void testRemoveGroupsCollection() {

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	List<WellList> subList1 = lists.subList(0, lists.size() / 2);
    	List<WellList> subList2 = lists.subList(lists.size() / 2, lists.size());
    	
    	testPlate.removeGroups(subList1);
    	
    	for(WellList list : subList1) {
    		assertFalse(testPlate.containsGroup(list));
    	}
    	
    	for(WellList list : subList2) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	testPlate.removeGroups(subList2);
    	
    	for(WellList list : subList2) {
    		assertFalse(testPlate.containsGroup(list));
    	}
    	
    	assertTrue(testPlate.allGroups().isEmpty());
    	
    }
    
    /**
     * Tests the remove groups method using an array.
     */
    @Test
    public void testRemoveGroupsArray() {

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	List<WellList> subList1 = lists.subList(0, lists.size() / 2);
    	List<WellList> subList2 = lists.subList(lists.size() / 2, lists.size());
    	WellList[] listArray1 = subList1.toArray(new WellList[subList1.size()]);
    	WellList[] listArray2 = subList2.toArray(new WellList[subList2.size()]);
    	
    	testPlate.removeGroups(listArray1);
    	
    	for(WellList list : listArray1) {
    		assertFalse(testPlate.containsGroup(list));
    	}
    	
    	for(WellList list : listArray2) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	testPlate.removeGroups(listArray2);
    	
    	for(WellList list : listArray2) {
    		assertFalse(testPlate.containsGroup(list));
    	}

    	assertTrue(testPlate.allGroups().isEmpty());
    	
    }
    
    /**
     * Tests the remove groups method using a label.
     */
    @Test
    public void testRemoveGroupsLabel() {
    	
    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	int index = lists.size();
    	
    	for(WellList list : lists) {
    		testPlate.removeGroups(list.label());
    		assertFalse(testPlate.contains(list));
    		assertEquals(testPlate.allGroups().size(), --index);
    	}
    	
    	assertTrue(testPlate.allGroups().isEmpty());
    }
    
    
    /**
     * Tests the remove groups method using a collection of labels.
     */
    @Test
    public void testRemoveGroupsLabelList() {
    	
    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	List<String> listString = new ArrayList<String>();
    	
    	for(WellList list : lists) {
    		listString.add(list.label());
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	List<String> subList1 = listString.subList(0, lists.size() / 2);
    	List<String> subList2 = listString.subList(lists.size() / 2, lists.size());
    	
    	testPlate.removeGroups(subList1);
    	
    	for(String list : subList1) {
    		assertFalse(testPlate.containsGroup(list));
    	}
    	
    	for(String list : subList2) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	testPlate.removeGroups(subList2);
    	
    	for(String list : subList2) {
    		assertFalse(testPlate.containsGroup(list));
    	}
    	
    	assertTrue(testPlate.allGroups().isEmpty());
    }
    
    /**
     * Tests the clear groups array.
     */
    @Test
    public void testClearGroups() {

    	assertFalse(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), lists.size());
    	
    	testPlate.clearGroups();
    	
    	assertTrue(testPlate.allGroups().isEmpty());
    	assertEquals(testPlate.allGroups().size(), 0);
    }
    
    /* ----------------------- Group Retrieval Methods ---------------------- */

    /**
     * Tests the all groups method.
     */
    @Test
    public void testAllGroups() {

    	Set<WellSetBigDecimal> groups = testPlate.allGroups();
    	
    	for(WellSetBigDecimal set : groups) {
    		WellList list = set.wellList();
    		assertTrue(lists.contains(list));
    		assertTrue(testPlate.containsGroup(list));
    	}
    }
    
    /**
     * Tests the get groups method using a label string.
     */
    @Test
    public void testGetGroupsLabel() {
    	
    	for(int i = 0; i < lists.size(); i++) {
    		
    		WellList list = lists.get(i);
    		
    		assertNotNull(list);
    		assertNotNull(testPlate.getGroups(list.label()));
    		
    		assertEquals(testPlate.getGroups(list.label()), sets.get(i));
    	}
    	
        SecureRandom secureRandom = new SecureRandom();
        
        for(int i = 0; i < 10; i++) {
    	    String str = new BigInteger(130, secureRandom).toString(32);
    	    assertNull(testPlate.getGroups(str));
    	}
    }      
    
    /**
     * Tests the get groups method using a well list.
     */
    @Test
    public void testGetGroupsList() {

        for(int i = 0; i < lists.size(); i++) {
    		
    		WellList list = lists.get(i);
    		
    		assertNotNull(list);
    		assertNotNull(testPlate.getGroups(list));
    		
    		assertEquals(testPlate.getGroups(list), sets.get(i));
    	}

        for(int i = 0; i < 10; i++) {
        	WellSetBigDecimal badSet = RandomUtil.randomWellSetBigDecimal(
        			minValue, maxValue, testPlate.rows(), testPlate.rows() + 
        			100, testPlate.columns(), testPlate.columns() + 100, minLength, maxLength);
        	assertNull(testPlate.getGroups(badSet.wellList()));
    	}
        
    }
    
    /**
     * Tests the get groups method using a collection of well lists.
     */
    @Test
    public void testGetGroupsCollection() {
    	
    	Set<WellSetBigDecimal> allGroups = testPlate.getGroups(lists);
    	List<WellList> combined = new ArrayList<WellList>();
    	
    	for(WellSetBigDecimal set : allGroups) {
    		WellList list = set.wellList();
    		combined.add(list);
    		assertTrue(lists.contains(list));
    	}
    	
    	Collections.sort(lists);
    	Collections.sort(combined);
    	
    	assertEquals(allGroups.size(), lists.size());
    	assertEquals(combined, lists);
    }
    
    /**
     * Tests the get groups method using an array of well lists.
     */
    @Test
    public void testGetGroupsArray() {

    	WellList[] listToArray = lists.toArray(new WellList[lists.size()]);
    	
    	Set<WellSetBigDecimal> allGroups = testPlate.getGroups(listToArray);
    	List<WellList> combined = new ArrayList<WellList>();
    	
    	for(WellSetBigDecimal set : allGroups) {
    		WellList list = set.wellList();
    		combined.add(list);
    		assertTrue(lists.contains(list));
    	}
    	
    	Collections.sort(lists);
    	Collections.sort(combined);
    	    	
    	assertEquals(allGroups.size(), lists.size());
    	assertEquals(combined, lists);
    	
    }

    /**
     * Tests the get groups method using a collection of labels.
     */
    @Test
    public void testGetGroupsLabelList() {
    	
    	List<String> listString = new ArrayList<String>();
    	
    	for(WellList list : lists) {
    		listString.add(list.label());
    	}
    	
    	Set<WellSetBigDecimal> allGroups = testPlate.getGroups(listString);
    	List<WellList> combined = new ArrayList<WellList>();
    	
    	for(WellSetBigDecimal set : allGroups) {
    		WellList list = set.wellList();
    		combined.add(list);
    		assertTrue(lists.contains(list));
    	}
    	
    	Collections.sort(lists);
    	Collections.sort(combined);
    	
    	assertEquals(allGroups.size(), lists.size());
    	assertEquals(combined, lists);
    }
    
    /* ------------------------ Set Lookup Method Tests --------------------- */
 
 	/**
 	 * Tests the contains group method using a label.
 	 */
    @Test
    public void testContainsGroupLabel() {
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list.label()));
    	}
    	
    	List<WellList> subList1 = lists.subList(0, lists.size() / 2);
    	List<WellList> subList2 = lists.subList(lists.size() / 2, lists.size());
    	
    	testPlate.removeGroups(subList1);
    	
    	for(WellList list : subList1) {
    		assertFalse(testPlate.containsGroup(list.label()));
    	}
    	
    	for(WellList list : subList2) {
    		assertTrue(testPlate.containsGroup(list.label()));
    	}
    }
    
    /**
     * Tests the contains group method using a well list.
     */
    @Test
    public void testContainsGroupList() {
    	
    	for(WellList list : lists) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    	
    	List<WellList> subList1 = lists.subList(0, lists.size() / 2);
    	List<WellList> subList2 = lists.subList(lists.size() / 2, lists.size());
    	
    	testPlate.removeGroups(subList1);
    	
    	for(WellList list : subList1) {
    		assertFalse(testPlate.containsGroup(list));
    	}
    	
    	for(WellList list : subList2) {
    		assertTrue(testPlate.containsGroup(list));
    	}
    }
    
    /**
     * Tests the contains group method using a collection.
     */
    @Test
    public void testContainsGroupCollection() {
    	
    	assertTrue(testPlate.containsGroup(lists));    	
    	
    	testPlate.removeGroups(lists.get(0));    	
    	assertFalse(testPlate.containsGroup(lists)); 
    	
    	testPlate.addGroups(lists.get(0));
    	assertTrue(testPlate.containsGroup(lists));
    }
    
    /**
     * Tests the contains group method using an array.
     */
    @Test
    public void testContainsGroupArray() {
    	
    	WellList[] listToArray = lists.toArray(new WellList[lists.size()]);
    	assertTrue(testPlate.containsGroup(listToArray));
    	
    	testPlate.removeGroups(lists.get(0));
    	assertFalse(testPlate.containsGroup(listToArray));
    	
    	testPlate.addGroups(lists.get(0));
    	assertTrue(testPlate.containsGroup(listToArray));
    }

    /**
     * Tests the contains group method using a collection of labels.
     */
    @Test
    public void testContainsGroupLabelList() {
    	
    	List<String> listString = new ArrayList<String>();
    	
    	for(WellList list : lists) {
    		listString.add(list.label());
    	}

        assertTrue(testPlate.containsGroup(listString));
    	
    }
    
    /*---------------------- Tests the tree set methods ----------------------*/
    
    /** 
     * Tests the higher method.
     */
    @Test
    public void testHigher() {

    	int current = 0;
    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
        int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellBigDecimal> iter = testPlate.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toHigher = iter.next();
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal input = new WellBigDecimal(toReturn.row(), toReturn.column() - 1);
        WellBigDecimal outside = new WellBigDecimal(testPlate.rows() + 1, testPlate.columns());
        
        toReturn = toHigher.compareTo(input) < 1 ? toHigher : toReturn;
        
        assertEquals(testPlate.ceiling(toHigher), toReturn);
        assertNull(testPlate.ceiling(outside));
        
    }
    
    /**
     * Tests the lower method.
     */
    @Test
    public void testLower() {

    	int current = 0;
    	WellSetBigDecimal testSet = testPlate.dataSet();
    	testSet.remove("A1");
    	
    	int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellBigDecimal> iter = testPlate.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal toFloor = iter.next();
        WellBigDecimal input = new WellBigDecimal(toFloor.row(), toFloor.column() - 1);
        WellBigDecimal outside = new WellBigDecimal(minColumn - 1, minColumn);
        
        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;

        assertEquals(testPlate.floor(toReturn), toReturn);
        assertNull(testPlate.floor(outside));
        
    }
    
    /**
     * Tests the poll first method.
     */
    @Test	
    public void testPollFirst() {
    	
    	WellBigDecimal first = testPlate.first();
    	WellBigDecimal polled = testPlate.pollFirst();
    	
    	assertFalse(testPlate.contains(polled));
    	assertEquals(first, polled);
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	WellBigDecimal last = testPlate.last();
    	WellBigDecimal polled = testPlate.pollLast();
    	
    	assertFalse(testPlate.contains(polled));
    	assertEquals(last, polled);
    }  
    
    /**
     * Tests the plate iterator.
     */
    @Test
    public void testIterator() {
    	
    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
    	WellBigDecimal[] array = testSet.toWellArray();
    	Iterator<WellBigDecimal> iter = testPlate.iterator();
    	
    	for(int i = 0; i < array.length; i++) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the descending plate iterator.
     */
    @Test
    public void testDescendingIterator() {
    	
    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
    	WellBigDecimal[] array = testSet.toWellArray();
    	Iterator<WellBigDecimal> iter = testPlate.descendingIterator();
    	
    	for(int i = array.length - 1; i >= 0; i--) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the descending set method.
     */
    @Test
    public void testDescendingSet() {

    	Set<WellBigDecimal> reversed = testPlate.descendingSet();

    	Iterator<WellBigDecimal> iter = testPlate.descendingIterator();
    	Iterator<WellBigDecimal> iterReversed = reversed.iterator();
    	
    	while(iter.hasNext() && iterReversed.hasNext()) {
    		assertEquals(iter.next(), iterReversed.next());
    	}
    }
    
    /**
     * Tests the first method.
     */
    @Test
    public void testFirst() {
    
    	Iterator<WellBigDecimal> iter = testPlate.iterator();
    	
    	assertEquals(iter.next(), testPlate.first());
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {
    	
        Iterator<WellBigDecimal> iter = testPlate.iterator();
    	
    	WellBigDecimal last = null;
    	
    	while(iter.hasNext()) {
    		last = iter.next();
    	}
    	
    	assertEquals(last, testPlate.last());
    }
    
    /**
     * Tests the ceiling method.
     */
    @Test
    public void testCeiling() {
    	
        WellSetBigDecimal testSet = testPlate.dataSet();
    	
        int current = 0;
    	int index = random.nextInt((testSet.size() - 2) + 1);    
         
        Iterator<WellBigDecimal> iter = testPlate.iterator();
         
        while(current < index) {
            iter.next();
            current++;
        }
         
        WellBigDecimal toCeiling = iter.next();
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal input = new WellBigDecimal(toReturn.row(), toReturn.column());
        WellBigDecimal outside = new WellBigDecimal(testPlate.rows() + 1, testPlate.columns());
         
        toReturn = toCeiling.compareTo(input) < 1 ? toCeiling : toReturn;
         
        assertEquals(testSet.ceiling(toCeiling), toReturn);
        assertNull(testSet.ceiling(outside));
    }
    
    /**
     * Tests the floor method.
     */
    @Test
    public void testFloor() {

        int index = random.nextInt((testPlate.size() - 2) + 1);
        
        int current = 0;

        Iterator<WellBigDecimal> iter = testPlate.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal toFloor = iter.next();
        WellBigDecimal input = new WellBigDecimal(toFloor.row(), toFloor.column() - 1);

        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;
        assertEquals(testPlate.floor(toReturn), toReturn);
        
        testPlate.pollFirst();

        WellBigDecimal well = testPlate.floor(new WellBigDecimal(0, 1));
        assertNull(well);
    }
    
    /**
     * Tests the head set method using a well.
     */
    @Test
    public void testHeadSet() {

    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
        WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> headSet = testPlate.headSet(random);
    	Set<WellBigDecimal> subSet = testPlate.subSet(testPlate.first(), random);
    	
    	assertEquals(headSet, subSet);
    	
    }
    
    /**
     * Tests the head set method using a well and boolean.
     */
    @Test
    public void testHeadSetInclusive() {

    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> headSetTrue = testPlate.headSet(random, true);
    	Set<WellBigDecimal> subSetTrue = testPlate.subSet(testSet.first(), true, random, true);
    	Set<WellBigDecimal> headSetFalse = testPlate.headSet(random, false);
    	Set<WellBigDecimal> subSetFalse = testPlate.subSet(testSet.first(), true, random, false);
    	
    	assertEquals(headSetTrue, subSetTrue);
    	assertEquals(headSetFalse, subSetFalse);
    }
    
    /**
     * Tests the tail set method using a well.
     */
    @Test
    public void testTailSet() {

    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> tailSet = testPlate.tailSet(random);
    	Set<WellBigDecimal> subSet = testPlate.subSet(random, true, testPlate.last(), true);

    	assertEquals(tailSet, subSet);
    }
    
    /**
     * Tests the tail set method using a well and boolean.
     */
    @Test
    public void testTailSetInclusive() {

    	WellSetBigDecimal testSet = testPlate.dataSet();
    	
    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> tailSetTrue = testPlate.tailSet(random, true);
    	Set<WellBigDecimal> subSetTrue = testPlate.subSet(random, true, testPlate.last(), true);
    	Set<WellBigDecimal> tailSetFalse = testPlate.tailSet(random, false);
    	Set<WellBigDecimal> subSetFalse = testPlate.subSet(random, false, testPlate.last(), true);
    	
    	assertEquals(tailSetTrue, subSetTrue);
    	assertEquals(tailSetFalse, subSetFalse);
    }
    
    /**
     * Tests the subset method using two wells and two booleans.
     */
    public void testSubSetInclusive() {
    	
    	int current = 0;
    	int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
    	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
    	
    	Set<WellBigDecimal> subSetTrueTrue = new HashSet<WellBigDecimal>();
    	Set<WellBigDecimal> subSetTrueFalse = new HashSet<WellBigDecimal>();
    	Set<WellBigDecimal> subSetFalseTrue = new HashSet<WellBigDecimal>();;
    	Set<WellBigDecimal> subSetFalseFalse = new HashSet<WellBigDecimal>();;
    	
    	WellBigDecimal startingWell = null;
    	WellBigDecimal endingWell = null;
    	
    	Iterator<WellBigDecimal> iter = testPlate.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSetTrueTrue.add(new WellBigDecimal(inclusive));
    		    subSetTrueFalse.add(new WellBigDecimal(inclusive));
    		    startingWell = new WellBigDecimal(inclusive);
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current + 1 == end) {

    			endingWell = new WellBigDecimal(inclusive);
    		    subSetTrueTrue.add(new WellBigDecimal(inclusive));
    		    subSetFalseTrue.add(new WellBigDecimal(inclusive));
    		    break;
    		    
    		} else {
    			
    			subSetTrueTrue.add(new WellBigDecimal(inclusive));
    			subSetTrueFalse.add(new WellBigDecimal(inclusive));
    			subSetFalseTrue.add(new WellBigDecimal(inclusive));
    			subSetFalseFalse.add(new WellBigDecimal(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigDecimal> trueTrue = testPlate.subSet(startingWell, true, endingWell, true);
    	Set<WellBigDecimal> trueFalse = testPlate.subSet(startingWell, true, endingWell, false);
    	Set<WellBigDecimal> falseTrue = testPlate.subSet(startingWell, false, endingWell, true);
    	Set<WellBigDecimal> falseFalse = testPlate.subSet(startingWell, false, endingWell, false);

    	assertEquals(trueTrue, subSetTrueTrue);
    	assertEquals(trueFalse, subSetTrueFalse);
    	assertEquals(falseTrue, subSetFalseTrue);
    	assertEquals(falseFalse, subSetFalseFalse);

    }
    
    /**
     * Tests the subset method using two wells.
     */
    @Test
    public void testSubSet() {

    	int current = 0;
    	int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
    	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1; 
    	
    	
    	Set<WellBigDecimal> subSet = new HashSet<WellBigDecimal>();
    	
    	WellBigDecimal startingWell = null;
    	WellBigDecimal endingWell = null;
    	
    	Iterator<WellBigDecimal> iter = testPlate.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSet.add(new WellBigDecimal(inclusive));
    		    startingWell = new WellBigDecimal(inclusive);
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current + 1 == end) {

    			endingWell = new WellBigDecimal(inclusive);
    		    break;
    		    
    		} else {
    			
    			subSet.add(new WellBigDecimal(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigDecimal> toCompare = testPlate.subSet(startingWell, endingWell);

    	assertEquals(toCompare, subSet);
    }

    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {
    	
    	assertFalse(testPlate.isEmpty());
    	testPlate.clearWells();
    	assertTrue(testPlate.isEmpty());

    }
    
    /* ---------------------- Plate Descriptor Methods ---------------------- */
    
    /**
     * Tests the label getter.
     */
    @Test
    public void testLabel() {

    	SecureRandom secureRandom = new SecureRandom();
    	
    	for(int i = 0; i < 100; i++) {
        
    		String label1 = new BigInteger(130, secureRandom).toString(32);
    		String label2 = new BigInteger(130, secureRandom).toString(32);

	    	PlateBigDecimal plate = new PlateBigDecimal(PlateBigDecimal.COLUMNS_12WELL);
	    	
	    	plate.setLabel(label1);
	    	
	    	assertEquals(label1, plate.label());
	    	
	    	plate.setLabel(label2);
	    	
	    	assertEquals(label2, plate.label());
    	}
    	
    }
    
    /**
     * Tests the columns getter.
     */
    @Test
    public void testColumns() {

    	for(int i = 0; i < 100; i++) {
    		
    		int columns =  PlateBigDecimal.COLUMNS_48WELL + 
	                 random.nextInt(PlateBigDecimal.COLUMNS_1536WELL - 
			         PlateBigDecimal.COLUMNS_48WELL + 1);
    		
    		int rows = 1;
    		
    		PlateBigDecimal plate = new PlateBigDecimal(rows, columns);

    		assertEquals(plate.columns(), columns);
    		
    	}
    	
    }
    
    /**
     * Tests the rows getter.
     */
    @Test
    public void testRows() {

    	for(int i = 0; i < 100; i++) {
    		
    		int rows = PlateBigDecimal.ROWS_48WELL + 
	                 random.nextInt(PlateBigDecimal.ROWS_1536WELL - 
		             PlateBigDecimal.ROWS_48WELL + 1);

    		int columns = 1;
    		
    		PlateBigDecimal plate = new PlateBigDecimal(rows, columns);
    		
    		assertEquals(plate.rows(), rows);
    		
    	}

    }
    
    /**
     * Tests the size getter.
     */
    @Test
    public void testSize() {

    	for(int i = 0; i < 100; i++) {
    		
	    	testPlate.clearWells();
	
	    	int wellNum = 2 + random.nextInt(testPlate.rows() * testPlate.columns() + 1);
	    	wellNum = wellNum / 2;
	    	int current = 0;
	    	int row = 0;
	    	int column = 1;
	
	    	while(current < wellNum) {
	    		
	    		testPlate.addWells(new WellBigDecimal(row, column));
	    		
	    		if(++column >= testPlate.columns()) {
	    			column = 1;
	    			row++;
	    		}
	    		
	    		if(row >= testPlate.rows()) {
	    			break;
	    		}
	    		
	    		current++;
	    	}
	    	
	    	assertEquals(wellNum, testPlate.size());
	    	
	    	int fullSize = testPlate.size();

	    	Set<WellBigDecimal> toRemove = testPlate.dataSet().subSet(0, testPlate.dataSet().size() / 4);
	    	
	    	for(WellBigDecimal well : toRemove) {
	    		testPlate.removeWells(well);
	    	}

	    	assertEquals(fullSize - toRemove.size(), testPlate.size());
    	}
    }
    
    /**
     * Test the type getter.
     */
    @Test
    public void testType() {
    	
    	for(int i = 0; i < 100; i++) {
    	    int type = random.nextInt(6 + 1);
    	    PlateBigDecimal plate = new PlateBigDecimal(type);
    	    assertEquals(type, plate.type());
    	}
    	
    	
    }
    
    /**
     * Tests the descriptor getter.
     */
    @Test
    public void testDescriptor() {

    	for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		PlateBigDecimal plate = new PlateBigDecimal(type);
    		
    		switch(type) {
    		
    		    case PlateBigDecimal.PLATE_6WELL:    assertEquals(plate.descriptor(), "6-Well");
    		                                         break;
    		    case PlateBigDecimal.PLATE_12WELL:   assertEquals(plate.descriptor(), "12-Well");
                                                     break;
    		    case PlateBigDecimal.PLATE_24WELL:   assertEquals(plate.descriptor(), "24-Well");
                                                     break;
    		    case PlateBigDecimal.PLATE_48WELL:   assertEquals(plate.descriptor(), "48-Well");
                                                     break;
    		    case PlateBigDecimal.PLATE_96WELL:   assertEquals(plate.descriptor(), "96-Well");
                                                     break;
    		    case PlateBigDecimal.PLATE_384WELL:  assertEquals(plate.descriptor(), "384-Well");
                                                     break;
    		    case PlateBigDecimal.PLATE_1536WELL: assertEquals(plate.descriptor(), "1536-Well");
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		int rows = PlateBigDecimal.ROWS_48WELL + 
	                   random.nextInt(PlateBigDecimal.ROWS_1536WELL - 
   		               PlateBigDecimal.ROWS_48WELL + 1);

            int columns =  PlateBigDecimal.COLUMNS_48WELL + 
	                       random.nextInt(PlateBigDecimal.COLUMNS_1536WELL - 
			               PlateBigDecimal.COLUMNS_48WELL + 1);
            
            PlateBigDecimal randomPlate = new PlateBigDecimal(rows, columns);
            
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
    		PlateBigDecimal plate = new PlateBigDecimal(0);
    		assertEquals(plate.dataType(), PlateBigDecimal.PLATE_BIGDECIMAL);
    	}
    }
    
    /**
     * Tests the data type string getter.
     */
    @Test
    public void testDataTypeString() {
    	for(int i = 0; i < 100; i++) {
    		PlateBigDecimal plate = new PlateBigDecimal(0);
    		assertEquals(plate.dataTypeString(), "BigDecimal");
    	}
    }
    
    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

        for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		PlateBigDecimal plate = new PlateBigDecimal(type);
    		
    		String toString = "Type: ";
    		
    		switch(type) {
    		
    		    case PlateBigDecimal.PLATE_6WELL:    toString += "6-Well Label: ";
    		                                         break;
    		    case PlateBigDecimal.PLATE_12WELL:   toString += "12-Well Label: ";
                                                     break;
    		    case PlateBigDecimal.PLATE_24WELL:   toString += "24-Well Label: ";
                                                     break;
    		    case PlateBigDecimal.PLATE_48WELL:   toString += "48-Well Label: ";
                                                     break;
    		    case PlateBigDecimal.PLATE_96WELL:   toString += "96-Well Label: ";
                                                     break;
    		    case PlateBigDecimal.PLATE_384WELL:  toString += "384-Well Label: ";
                                                     break;
    		    case PlateBigDecimal.PLATE_1536WELL: toString += "1536-Well Label: ";
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		toString += plate.label();
    		
    		assertEquals(toString, plate.toString());
    		
    		int rows = PlateBigDecimal.ROWS_48WELL + 
	                   random.nextInt(PlateBigDecimal.ROWS_1536WELL - 
   		               PlateBigDecimal.ROWS_48WELL + 1);

            int columns =  PlateBigDecimal.COLUMNS_48WELL + 
	                       random.nextInt(PlateBigDecimal.COLUMNS_1536WELL - 
			               PlateBigDecimal.COLUMNS_48WELL + 1);
            
            PlateBigDecimal randomPlate = new PlateBigDecimal(rows, columns);
            
            if(randomPlate.type() == -1) {
            	String toStringRandom = "Type: Custom Plate: " + rows + "x" + columns + " Label: " + randomPlate.label();
            	assertEquals(toStringRandom, toStringRandom);
            }
            
    	}

    }
    
    /*------------------- Tests methods for data set output ------------------*/
    
    /**
     * Tests the data set getter.
     */
    @Test
    public void testDataSet() {
    	
    	for(WellSetBigDecimal set : sets) {
    		
    		testPlate.clearWells();
    		testPlate.addWells(set);
    		
    		Iterator<WellBigDecimal> iterSet = set.iterator();
    		Iterator<WellBigDecimal> iterPlate = testPlate.iterator();
    		
    		while(iterSet.hasNext() && iterPlate.hasNext()) {
    			assertEquals(iterSet.next(), iterPlate.next());
    		}
    	}
    	
    }
    
    /**
     * Tests the to array method.
     */
    @Test
    public void testToArray() {

    	for(WellBigDecimal[] array : arrays) {
    		
    		testPlate.clearWells();
    		testPlate.addWells(array);
    		
    		WellBigDecimal[] plateArray = testPlate.toArray();
    		
    		for(int i = 0; i < array.length; i++) {
    			assertEquals(array[i], plateArray[i]);
    		}
    	}
    	
    }

    /*--------------------- Tests methods for adding wells -------------------*/
    
    /**
     * Tests the add wells method using a list of wells..
     */
    @Test
    public void testAddWellsWell() {

    	testPlate.clearWells();
    	
    	for(int i = 0; i < wells.size(); i++) {
    		
    		WellBigDecimal well = wells.get(i);
    		testPlate.addWells(well);
    		
    		assertTrue(testPlate.contains(well));
    	}
    	
    }
    
    /**
     * Tests the add wells method using a list of well sets.
     */
    @Test
    public void testAddWellsSet() {

        testPlate.clearWells();
    	
    	for(int i = 0; i < sets.size(); i++) {
    		
    		WellSetBigDecimal set = sets.get(i);
    		testPlate.addWells(set);
    		
    		assertTrue(testPlate.contains(set));
    	}
    	
    }
    
    /**
     * Tests the add wells method using a list of collections.
     */
    @Test
    public void testAddWellsCollection() {

        testPlate.clearWells();
    	
    	for(int i = 0; i < collections.size(); i++) {
    		
    		TreeSet<WellBigDecimal> collection = collections.get(i);
    		testPlate.addWells(collection);
    		
    		assertTrue(testPlate.contains(collection));
    	}
    	
    }
    
    /**
     * Tests the add wells method using a list of arrays.
     */
    @Test
    public void testAddWellsArray() {
    	
    	testPlate.clearWells();
    	
    	for(int i = 0; i < arrays.size(); i++) {
    		
    		WellBigDecimal[] array = arrays.get(i);
    		testPlate.addWells(array);
    		
    		assertTrue(testPlate.contains(array));
    	}
    }    

    /* -------------------- Methods for removing wells ---------------------- */
    
   /**
    * Tests the remove wells method using a list of wells. 
    */
    @Test
    public void testRemoveWellsWell() {

    	for(int i = 0; i < wells.size(); i++) {
    		
    		WellBigDecimal well = wells.get(i);
    		testPlate.removeWells(well);
    		
    		assertFalse(testPlate.contains(well));
    	}
    	
    }
    
    /**
     * Tests the remove wells method using a list of well sets.
     */
    @Test
    public void testRemoveWellsSet() {

    	for(int i = 0; i < sets.size(); i++) {
    		
    		WellSetBigDecimal set = sets.get(i);
    		testPlate.removeWells(set);
    		
    		assertFalse(testPlate.contains(set));
    	}
    	
    }
    
    /**
     * Tests the remove wells method using a list of collections.
     */
    @Test
    public void testRemoveWellsCollection() {

    	for(int i = 0; i < collections.size(); i++) {
    		
    		TreeSet<WellBigDecimal> collection = collections.get(i);
    		testPlate.removeWells(collection);
    		
    		assertFalse(testPlate.contains(collection));
    	}

    }
    
    /**
     * Tests the remove wells method using a list of well arrays.
     */
    @Test
    public void testRemoveWellsArray() {

        for(int i = 0; i < arrays.size(); i++) {
    		
    		WellBigDecimal[] array = arrays.get(i);
    		testPlate.removeWells(array);
    		
    		assertFalse(testPlate.contains(array));
    	}
    }
    
    /**
     * Tests the remove wells method using a list of delimiter separated lists.
     */
    @Test
    public void testRemoveWellsDelimiter() {

        for(int i = 0; i < collections.size(); i++) {
    		
    		TreeSet<WellBigDecimal> collection = collections.get(i);
    		String list = this.getDelimiterList(collection);
    		testPlate.removeWells(list, ",");
    		
    		assertFalse(testPlate.contains(collection));
    	}
    }
    
    /**
     * Tests the remove wells method using a list of index strings.
     */
    @Test
    public void testRemoveWellsWellString() {
        
    	for(int i = 0; i < wells.size(); i++) {
    		
    		WellBigDecimal well = wells.get(i);
    		
    		String wellString = well.index();
    		testPlate.removeWells(wellString);
    		
    		assertFalse(testPlate.contains(well));
    	}
    }
    
    /**
     * Tests the remove wells method using a list of indices.
     */
    @Test
    public void testRemoveWellsIndex() {
    	
    	for(int i = 0; i < wells.size(); i++) {
    		
    		WellBigDecimal well = wells.get(i);
    		
    		WellIndex index = new WellIndex(well.row(), well.column());
    		testPlate.removeWells(index);
    		
    		assertFalse(testPlate.contains(well));
    	}
    	
    }
    
    /**
     * Tests the remove wells method using a list of well lists.
     */
    @Test
    public void testRemoveWellsWellList() {

    	for(int i = 0; i < sets.size(); i++) {
    		
    		WellSetBigDecimal set = sets.get(i);
    		WellList list = set.wellList();
    		testPlate.removeWells(list);
    		
    		assertFalse(testPlate.contains(set));
    	}

    }
    
    /**
     * Tests the clear wells method.
     */
    @Test
    public void testClearWells() {

    	testPlate.clearWells();
    	
    	for(int i = 0; i < sets.size(); i++) {

    		testPlate.addWells(sets.get(i));
    		assertFalse(testPlate.isEmpty());
    		
    		testPlate.clearWells();
    		assertTrue(testPlate.isEmpty());
    	}
    	
    }
    
    /*------------------- Tests methods for replacing wells ------------------*/
    
    /**
     * Tests the replace wells method using a single well.
     */
    @Test
    public void testReplaceWellsWell() {

        for(int i = 0; i < wells.size(); i++) {
    		
        	WellBigDecimal input = wells.get(i);
        	input.clear();
        	input.add(RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
        	
        	WellBigDecimal current = testPlate.getWells(input);
        	testPlate.replaceWells(input);

        	if(current != null) {

        		current = new WellBigDecimal(current);
        		
        		assertNotEquals(input.data(), current.data());
        	    assertNotEquals(testPlate.getWells(input).data(), current.data());
        	    assertEquals(testPlate.getWells(input).data(), input.data());
        	    assertEquals(testPlate.getWells(input), input);
        	    
        	} else {
        		
        		assertTrue(testPlate.contains(input));
        		assertEquals(testPlate.getWells(input), input);
        	}
        	
        }
    }
    
    /**
     * Tests the replace wells method using a well set.
     */
    @Test
    public void testReplaceWellsSet() {

        for(int i = 0; i < 1; i++) {
    		
        	int begin = 1 + random.nextInt(testPlate.size() - 1 + 1);
        	int end = begin + random.nextInt(testPlate.size() - begin + 1);
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	WellSetBigDecimal previous = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    
        		WellBigDecimal toAdd = new WellBigDecimal(iter.next());
        		previous.add(new WellBigDecimal(toAdd));
     
        	    toAdd.clear();
        	    toAdd.add(RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
        	    set.add(toAdd);
        	    current++;
        	}
        	
        	testPlate.replaceWells(set);

        	Iterator<WellBigDecimal> previousIter = previous.iterator();

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertNotEquals(testPlate.getWells(well).data(), previousIter.next().data());
        	}
        	
        }
    }
    
    /**
     * Tests the replace wells method using a collection.
     */
    @Test
    public void testReplaceWellsCollection() {

    	for(int i = 0; i < 1; i++) {
    		
        	int begin = 1 + random.nextInt(testPlate.size() - 1 + 1);
        	int end = begin + random.nextInt(testPlate.size() - begin + 1);
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	WellSetBigDecimal previous = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    
        		WellBigDecimal toAdd = new WellBigDecimal(iter.next());
        		previous.add(new WellBigDecimal(toAdd));
     
        	    toAdd.clear();
        	    toAdd.add(RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
        	    set.add(toAdd);
        	    current++;
        	}
        	
        	testPlate.replaceWells(set.allWells());

        	Iterator<WellBigDecimal> previousIter = previous.iterator();

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertNotEquals(testPlate.getWells(well).data(), previousIter.next().data());
        	}
        	
        }

    }
    
    /**
     * Tests the replace wells method using an array.
     */
    @Test
    public void testReplaceWellsArray() {

    	for(int i = 0; i < 1; i++) {
    		
        	int begin = 1 + random.nextInt(testPlate.size() - 1 + 1);
        	int end = begin + random.nextInt(testPlate.size() - begin + 1);
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	WellSetBigDecimal previous = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    
        		WellBigDecimal toAdd = new WellBigDecimal(iter.next());
        		previous.add(new WellBigDecimal(toAdd));
     
        	    toAdd.clear();
        	    toAdd.add(RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
        	    set.add(toAdd);
        	    current++;
        	}
        	
        	testPlate.replaceWells(set.toWellArray());

        	Iterator<WellBigDecimal> previousIter = previous.iterator();

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertNotEquals(testPlate.getWells(well).data(), previousIter.next().data());
        	}
        	
        }

    }

    /*------------------- Tests methods for retaining wells ------------------*/
    
    /**
     * Tests the retain wells method using a single well.
     */
    @Test
    public void testRetainWellsWell() {
    	
    	for(int i = 0; i < 100; i++) {
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		clone.retainWells(well);

    		assertEquals(clone.size(), 1);
    		assertTrue(clone.contains(well));
    		
    	}
    	
    }
    
    /**
     * Tests the retain wells method using a well set.
     */
    @Test
    public void testRetainWellsSet() {

    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set);

        	for(WellBigDecimal well : set) {
        		assertTrue(clone.contains(well));
        		assertEquals(clone.getWells(well), well);
        		assertEquals(clone.getWells(well).data(), well.data());
        	}
        	
        	assertTrue(clone.contains(set));
        	assertEquals(clone.size(), set.size());
        	
        }
    }
    
    /**
     * Tests the retain wells method using a collection.
     */
    @Test
    public void testRetainWellsCollection() {
        
    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set.allWells());

        	for(WellBigDecimal well : set) {
        		assertTrue(clone.contains(well));
        		assertEquals(clone.getWells(well), well);
        		assertEquals(clone.getWells(well).data(), well.data());
        	}
        	
        	assertTrue(clone.contains(set));
        	assertEquals(clone.size(), set.size());
        	
        }
    }
    
    /**
     * Tests the retain wells method using an array.
     */
    @Test
    public void testRetainWellsArray() {
    	
    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set.toWellArray());

        	for(WellBigDecimal well : set) {
        		assertTrue(clone.contains(well));
        		assertEquals(clone.getWells(well), well);
        		assertEquals(clone.getWells(well).data(), well.data());
        	}
        	
        	assertTrue(clone.contains(set));
        	assertEquals(clone.size(), set.size());
        	
        }
    }

    /**
     * Tests the retain wells method using a delimiter separated list.
     */
    @Test
    public void testRetainWellsDelimiter() {

    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	String list = this.getDelimiterList(set.allWells());
        	clone.retainWells(list, ",");

        	for(WellBigDecimal well : set) {
        		assertTrue(clone.contains(well));
        		assertEquals(clone.getWells(well), well);
        		assertEquals(clone.getWells(well).data(), well.data());
        	}
        	
        	assertTrue(clone.contains(set));
        	assertEquals(clone.size(), set.size());
        	
        }
    }
    
    /**
     * Tests the retain wells method using a well index string.
     */
    @Test
    public void testRetainWellsString() {

    	for(int i = 0; i < 100; i++) {
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		String wellString = well.index();
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		clone.retainWells(wellString);

    		assertEquals(clone.size(), 1);
    		assertTrue(clone.contains(well));
    		
    	}
    }
    
    /**
     * Tests the retain wells method using a well index.
     */
    @Test
    public void testRetainWellsIndex() {
    	
        for(int i = 0; i < 100; i++) {
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		WellIndex index = new WellIndex(well.row(), well.column());
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		clone.retainWells(index);

    		assertEquals(clone.size(), 1);
    		assertTrue(clone.contains(well));
    		
    	}

    }
    
    /**
     * Tests the retain wells method using a well list.
     */
    @Test
    public void testRetainWellsList() {

    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set.wellList());

        	for(WellBigDecimal well : set) {
        		assertTrue(clone.contains(well));
        		assertEquals(clone.getWells(well), well);
        		assertEquals(clone.getWells(well).data(), well.data());
        	}
        	
        	assertTrue(clone.contains(set));
        	assertEquals(clone.size(), set.size());
        	
        }
    }
    
    /*------------------ Tests methods for retrieving wells ------------------*/
    
    /**
     * Tests the get wells method using a single well.
     */
    @Test
    public void testGetWellsWell() {
    	
    	for(int i = 0; i < 100; i++) {
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());

    		assertEquals(testPlate.getWells(well), well);
    		assertEquals(testPlate.getWells(well).data(), well.data());
    		
    	}
    	
    }
    
    /**
     * Tests the get wells method using a well set.
     */
    @Test
    public void testGetWellsSet() {

    	for(int i = 0; i < 100; i++) {

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	WellSetBigDecimal returned = testPlate.getWells(set);

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        	}
        	
        	assertEquals(returned, set);
        	
        }
    }
    
    /**
     * Tests the get wells method using a collection.
     */
    @Test
    public void testGetWellsCollection() {
        
    	for(int i = 0; i < 100; i++) {

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	WellSetBigDecimal returned = testPlate.getWells(set.allWells());

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        	}
        	
        	assertEquals(returned, set);
        	
        }
    }
    
    /**
     * Tests the get wells method using an array.
     */
    @Test
    public void testGetWellsArray() {
    	
    	for(int i = 0; i < 100; i++) {

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	WellSetBigDecimal returned = testPlate.getWells(set.toWellArray());

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        	}
        	
        	assertEquals(returned, set);
        	
        }
        	
    }

    /**
     * Tests the get wells method using a delimiter separated list.
     */
    @Test
    public void testGetWellsDelimiter() {

    	for(int i = 0; i < 100; i++) {

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	String list = this.getDelimiterList(set.allWells());
        	WellSetBigDecimal returned = testPlate.getWells(list, ",");

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        	}
        	
        	assertEquals(returned, set);
        	
        }
    }
    
    /**
     * Tests the get wells method using a well index string.
     */
    @Test
    public void testGetWellsString() {

    	for(int i = 0; i < 100; i++) {
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		String wellString = well.index();
    		
    		WellBigDecimal returned = testPlate.getWells(wellString);

    		assertEquals(returned, well);
    		
    	}
    }
    
    /**
     * Tests the get wells method using a well index.
     */
    @Test
    public void testGetWellsIndex() {

    	for(int i = 0; i < 100; i++) {
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		WellIndex index = new WellIndex(well.row(), well.column());
    		
    		WellBigDecimal returned = testPlate.getWells(index);

    		assertEquals(returned, well);
    		
    	}
    	
    }
    
    /**
     * Tests the get wells method using a well list.
     */
    @Test
    public void testGetWellsList() {

    	for(int i = 0; i < 100; i++) {

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	WellSetBigDecimal returned = testPlate.getWells(set.wellList());

        	for(WellBigDecimal well : set) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        	}
        	
        	assertEquals(returned, set);
        	
        }
    }

    /**
     * Tests the get row method.
     */
    @Test
    public void testGetRow() {
    	
    	for(int i = 0; i < 100; i++) {

        	int row = random.nextInt(testPlate.rows() + 1);

        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(iter.hasNext()) {
        		
        		WellBigDecimal well = iter.next();
        		
        		if(well.row() != row) {
        			set.add(well);
        		}

        	}

        	testPlate.clearWells();
        	testPlate.addWells(set);
        	
        	WellSetBigDecimal rowWells = new WellSetBigDecimal();
        	
        	for(int j = 1; j < testPlate.columns() / 2; j++) {
        		WellBigDecimal toAdd = new WellBigDecimal(row, j);
        		toAdd.add(RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
        		testPlate.addWells(toAdd);
        		rowWells.add(toAdd);
        	}
        	
        	WellSetBigDecimal returned = testPlate.getRow(row);

        	for(WellBigDecimal well : rowWells) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        		assertEquals(well.row(), row);
        	}
        	
        	assertEquals(returned, rowWells);
        	
        }
    	
    }
    
    /**
     * Tests the get column method.
     */
    @Test
    public void testGetColumn() {
    	
    	for(int i = 0; i < 100; i++) {

        	int column = 1 + random.nextInt(testPlate.columns());

        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(iter.hasNext()) {
        		
        		WellBigDecimal well = iter.next();
        		
        		if(well.column() != column) {
        			set.add(well);
        		}

        	}

        	testPlate.clearWells();
        	testPlate.addWells(set);
        	
        	WellSetBigDecimal columnWells = new WellSetBigDecimal();
        	
        	for(int j = 0; j < testPlate.rows() / 2; j++) {
        		WellBigDecimal toAdd = new WellBigDecimal(j, column);
        		toAdd.add(RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
        		testPlate.addWells(toAdd);
        		columnWells.add(toAdd);
        	}
        	
        	WellSetBigDecimal returned = testPlate.getColumn(column);

        	for(WellBigDecimal well :columnWells) {
        		assertTrue(testPlate.contains(well));
        		assertEquals(testPlate.getWells(well), well);
        		assertEquals(testPlate.getWells(well).data(), well.data());
        		assertEquals(well.column(), column);
        	}
        	
        	assertEquals(returned, columnWells);
        	
        }
    	
    }
    
    /*---------------------- Tests methods for well Lookup -------------------*/
    
    /**
     * Tests the contains method using a single well.
     */
    @Test
    public void testContainsWell() {
    	
    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());

    		assertTrue(clone.contains(well));
    		clone.removeWells(well);
    		assertFalse(clone.contains(well));
    		
    	}
    	
    }
    
    /**
     * Tests the contains method using a well set.
     */
    @Test
    public void testContainsSet() {

    	for(int i = 0; i < 100; i++) {

    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	assertTrue(clone.contains(set));
        	clone.removeWells(set);
        	assertFalse(clone.contains(set));
        	
        	for(WellBigDecimal well : set) {
        		assertFalse(clone.contains(well));
        	}
        }
    }
    
    /**
     * Tests the contains method using a collection.
     */
    @Test
    public void testContainsCollection() {
        
    	for(int i = 0; i < 100; i++) {

    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}
        	
        	Set<WellBigDecimal> collection = set.allWells();
        	
        	assertTrue(clone.contains(collection));
        	clone.removeWells(collection);
        	assertFalse(clone.contains(collection));
        	
        	for(WellBigDecimal well : collection) {
        		assertFalse(clone.contains(well));
        	}
        }
    }
    
    /**
     * Tests the contains method using an array.
     */
    @Test
    public void testContainsArray() {
    	
    	for(int i = 0; i < 100; i++) {

    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	WellBigDecimal[] array = set.toWellArray();
        	
        	assertTrue(clone.contains(array));
        	clone.removeWells(array);
        	assertFalse(clone.contains(array));
        	
        	for(WellBigDecimal well : array) {
        		assertFalse(clone.contains(well));
        	}
        	
        }
        	
    }

    /**
     * Tests the contains method using a delimiter separated list.
     */
    @Test
    public void testContainsDelimiter() {

    	for(int i = 0; i < 100; i++) {

    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	String list = this.getDelimiterList(set.allWells());

        	assertTrue(clone.contains(list, ","));
        	clone.removeWells(list, ",");
        	assertFalse(clone.contains(list, ","));
        	
        	for(WellBigDecimal well : set) {
        		assertFalse(clone.contains(well));
        	}
        	
        }
    }
    
    /**
     * Tests the contains method using a well index string.
     */
    @Test
    public void testContainsString() {

    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		String wellString = well.index();

        	assertTrue(clone.contains(wellString));
        	clone.removeWells(wellString);
        	assertFalse(clone.contains(wellString));
        	
    	}
    }
    
    /**
     * Tests the contains method using a well index.
     */
    @Test
    public void testContainsIndex() {

    	for(int i = 0; i < 100; i++) {
    		
    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		WellBigDecimal well = (WellBigDecimal) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		WellIndex index = new WellIndex(well.row(), well.column());

        	assertTrue(clone.contains(index));
        	clone.removeWells(index);
        	assertFalse(clone.contains(index));
    		
    	}
    	
    }
    
    /**
     * Tests the contains method using a well list.
     */
    @Test
    public void testContainsList() {

    	for(int i = 0; i < 100; i++) {

    		PlateBigDecimal clone = new PlateBigDecimal(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetBigDecimal set = new WellSetBigDecimal();
        	
        	Iterator<WellBigDecimal> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellBigDecimal(iter.next()));
        	    current++;
        	}

        	assertTrue(clone.contains(set.wellList()));
        	clone.removeWells(set.wellList());
        	assertFalse(clone.contains(set.wellList()));
        	
        	for(WellBigDecimal well : set) {
        		assertFalse(clone.contains(well));
        	}
        }
    }

    /*---------------------- Tests print/string methods ----------------------*/

    /**
     * Tests the print all data method.
     */
    @Test
    public void testPrintAllData() {
    	
    	for(WellSetBigDecimal set : sets) {
    		
    		testPlate.clearWells();
    		
    		String plateString = testPlate.label() + " " + testPlate.descriptor() + "\n";
    		
    		for(WellBigDecimal well : set) {
    			plateString += well.toString() + "\n";
    			testPlate.addWells(well);
    		}

        	assertEquals(plateString, testPlate.printAllData());
    	}

    }
    
    /**
     * Tests the print data method.
     */
    @Test
    public void testPrintData() {

    	for(WellBigDecimal well : wells) {
    		
    		String wellString = well.toString();
    		
    		testPlate.replaceWells(well);

    		assertEquals(wellString, testPlate.printData(well));
    	}
    	
    }
    
    /*------------------------ Testing helper methods ------------------------*/
    
    /**
     * Creates a delimiter separated list from a collection.
     * @param    Collection<WellBigDecimal>    the input collection
     * @return                                 the delimiter separated list
     */
    public String getDelimiterList(Collection<WellBigDecimal> collection) {
    	
    	String str = "";
    	Iterator<WellBigDecimal> iter = collection.iterator();
    	
    	while(iter.hasNext()) {
    		
    		WellBigDecimal well = iter.next();
    		
    		if(iter.hasNext()) {
    			str += well.index() + ", ";
    		} else {
    			str += well.index();
    			break;
    		}
    	}
    	
    	return str;
    }
}
