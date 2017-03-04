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

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the integer plate class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlateIntegerTest {
	
	/* ---------------------------- Local fields ---------------------------- */
	
    /* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private Integer minValue = new Integer(-1000000000);   // Minimum integer value for wells
	private Integer maxValue = new Integer(1000000000);    // Maximum integer value for wells
	private int minLength = 100;                           // Minimum well set length
	private int maxLength = 1000;                          // Maximum well set length
	private int minRow = 0;                                // Minimum well row
	private int minColumn = 1;                             // Minimum well column
	private Random random = new Random();                  // Generates random integers
	
	/* Random plate, well, well list, index and set objects for testing */
	
    private PlateInteger testPlate;
	private List<WellSetInteger> sets = new ArrayList<WellSetInteger>();
	private List<TreeSet<WellInteger>> collections = new ArrayList<TreeSet<WellInteger>>();
	private List<WellInteger[]> arrays = new ArrayList<WellInteger[]>();
	private List<WellList> lists = new ArrayList<WellList>();
	private List<WellInteger> wells = new ArrayList<WellInteger>();
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

		int rows = PlateInteger.ROWS_48WELL + 
				                 random.nextInt(PlateInteger.ROWS_1536WELL - 
					             PlateInteger.ROWS_48WELL + 1);
		
		int columns =  PlateInteger.COLUMNS_48WELL + 
				                 random.nextInt(PlateInteger.COLUMNS_1536WELL - 
						         PlateInteger.COLUMNS_48WELL + 1);
		
		int length = rows * columns / 5;

		testPlate = RandomUtil.randomPlateInteger(
				rows, columns, minValue, maxValue, minLength, maxLength, 0, length, "TestPlate");
		
	    for(int i = 0; i < 100; i++) { 

			WellSetInteger testSet = RandomUtil.randomWellSetInteger(
					minValue, maxValue, minRow, testPlate.rows(), minColumn, testPlate.columns(), 1, length);
			
			testSet.setLabel("TestSet" + i);
			
			TreeSet<WellInteger> testCollection = (TreeSet<WellInteger>) testSet.allWells();		
			WellInteger[] testArray = testSet.toWellArray();
			WellList testList = testSet.wellList();
			WellInteger testWell = (WellInteger) RandomUtil.randomObject(testCollection);
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
    public void testPlateIntegerType() {
    	
    	PlateInteger plate = new PlateInteger(PlateInteger.PLATE_96WELL);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateInteger.ROWS_96WELL);
        assertEquals(plate.columns(), PlateInteger.COLUMNS_96WELL);      
        assertEquals(plate.type(), PlateInteger.PLATE_96WELL);
        assertEquals(plate.dataType(), WellInteger.INTEGER);
        assertEquals(plate.descriptor(), "96-Well");
        assertEquals(plate.label(), "PlateInteger");
        assertEquals(plate.size(), 0);
        
        assertTrue(plate.allGroups().isEmpty());
        assertTrue(plate.dataSet().isEmpty());
       
    } 
    
    /**
     * Tests the plate constructor using a row and column.
     */
	@Test
    public void testPlateIntegerRowColumn() {
    
    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateInteger plate = new PlateInteger(rows, columns);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellInteger.INTEGER);
        assertEquals(plate.descriptor(), descriptor);
        assertEquals(plate.label(), "PlateInteger");
        assertEquals(plate.size(), 0);
        
        assertTrue(plate.allGroups().isEmpty());
        assertTrue(plate.dataSet().isEmpty());
        
    } 

    /**
     * Tests the plate constructor using a plate type constant and label.
     */
	@Test
    public void testPlateIntegerTypeLabel() {
    
    	String label = "TestLabel";
    	
    	PlateInteger plate = new PlateInteger(PlateInteger.PLATE_384WELL, label);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateInteger.ROWS_384WELL);
        assertEquals(plate.columns(), PlateInteger.COLUMNS_384WELL);      
        assertEquals(plate.type(), PlateInteger.PLATE_384WELL);
        assertEquals(plate.dataType(), WellInteger.INTEGER);
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
    public void testPlateIntegerRowColumnLabel() {

    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	String label = "TestLabel";
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateInteger plate = new PlateInteger(rows, columns, label);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellInteger.INTEGER);
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
    public void testPlateIntegerTypeSet() {  	

    	int length = PlateInteger.ROWS_48WELL * PlateInteger.COLUMNS_384WELL / 2;
    	
    	WellSetInteger random = RandomUtil.randomWellSetInteger(
    			minValue, maxValue, 0, PlateInteger.ROWS_384WELL, 1, PlateInteger.COLUMNS_384WELL, 1, length);
    	
    	PlateInteger plate = new PlateInteger(PlateInteger.PLATE_384WELL, random);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateInteger.ROWS_384WELL);
        assertEquals(plate.columns(), PlateInteger.COLUMNS_384WELL);      
        assertEquals(plate.type(), PlateInteger.PLATE_384WELL);
        assertEquals(plate.dataType(), WellInteger.INTEGER);
        assertEquals(plate.descriptor(), "384-Well");
        assertEquals(plate.label(), "PlateInteger");
        assertEquals(plate.size(), random.size());
        assertEquals(plate.dataSet(), random);
 
        assertTrue(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
    }

    /**
     * Tests the plate constructor using a type constant, label and well set.
     */
    @Test
    public void testPlateIntegerRowColumnSet() {
    	
    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	
        int length = rows * columns / 10;
    	
    	WellSetInteger random = RandomUtil.randomWellSetInteger(
    			minValue, maxValue, 0, rows, 1, columns, 1, length);
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateInteger plate = new PlateInteger(rows, columns, random);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellInteger.INTEGER);
        assertEquals(plate.descriptor(), descriptor);
        assertEquals(plate.label(), "PlateInteger");
        assertEquals(plate.size(), random.size());
        assertEquals(plate.dataSet(), random);
        
        assertTrue(plate.allGroups().isEmpty());
        assertFalse(plate.dataSet().isEmpty());
        
    }

    /**
     * Tests the plate constructor using a type constant, label and well set.
     */
    @Test
    public void testPlateIntegerTypeLabelSet() {

    	String label = "TestLabel";
    	
        int length = PlateInteger.ROWS_48WELL * PlateInteger.COLUMNS_48WELL / 2;
    	
    	WellSetInteger random = RandomUtil.randomWellSetInteger(
    			minValue, maxValue, 0, PlateInteger.ROWS_48WELL, 1, PlateInteger.COLUMNS_48WELL, 1, length); 
    	
    	PlateInteger plate = new PlateInteger(PlateInteger.PLATE_48WELL, label, random);
        assertNotNull(plate);
        
        assertEquals(plate.rows(), PlateInteger.ROWS_48WELL);
        assertEquals(plate.columns(), PlateInteger.COLUMNS_48WELL);      
        assertEquals(plate.type(), PlateInteger.PLATE_48WELL);
        assertEquals(plate.dataType(), WellInteger.INTEGER);
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
    public void testPlateIntegerRowColumnLabelSet() {

    	int rows = random.nextInt(100 + 1);
    	int columns = 1 + random.nextInt(100 - 1 + 1);
    	
    	String label = "TestLabel";
    	
        int length = rows * columns / 10;
    	
    	WellSetInteger random = RandomUtil.randomWellSetInteger(
    			minValue, maxValue, 1, rows, 1, columns, 1, length);
    	
    	String descriptor = "Custom Plate: " + rows + "x" + columns;
    	
    	PlateInteger plate = new PlateInteger(rows, columns, label, random);
        assertNotNull(plate);

        assertEquals(plate.rows(), rows);
        assertEquals(plate.columns(), columns);      
        assertEquals(plate.dataType(), WellInteger.INTEGER);
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
    public void testPlateIntegerPlate() {

        PlateInteger plate = new PlateInteger(testPlate);
        assertNotNull(plate);

        assertEquals(plate.rows(), testPlate.rows());
        assertEquals(plate.columns(), testPlate.columns());      
        assertEquals(plate.type(), testPlate.type());
        assertEquals(plate.dataType(), WellInteger.INTEGER);
        assertEquals(plate.descriptor(), testPlate.descriptor());
        assertEquals(plate.label(), testPlate.label());
        assertEquals(plate.size(), testPlate.size());
        assertEquals(plate.dataSet(), testPlate.dataSet());
        
        for(WellSetInteger set : testPlate.allGroups()) {
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

    	PlateInteger sixWell = new PlateInteger(PlateInteger.PLATE_6WELL);
    	PlateInteger twelveWell = new PlateInteger(PlateInteger.PLATE_12WELL);
    	PlateInteger twentyFourWell = new PlateInteger(PlateInteger.PLATE_24WELL);
    	PlateInteger fortyEightWell = new PlateInteger(PlateInteger.PLATE_48WELL);
    	PlateInteger ninetySixWell = new PlateInteger(PlateInteger.PLATE_96WELL);
    	PlateInteger threeEightyFourWell = new PlateInteger(PlateInteger.PLATE_384WELL);
    	PlateInteger fifteenThirtySixWell = new PlateInteger(PlateInteger.PLATE_1536WELL);
    	
    	assertEquals(sixWell.rows(), PlateInteger.ROWS_6WELL);
    	assertEquals(sixWell.columns(), PlateInteger.COLUMNS_6WELL);
    	assertEquals(sixWell.type(), PlateInteger.PLATE_6WELL);
    	
    	assertEquals(twelveWell.rows(), PlateInteger.ROWS_12WELL);
    	assertEquals(twelveWell.columns(), PlateInteger.COLUMNS_12WELL);
    	assertEquals(twelveWell.type(), PlateInteger.PLATE_12WELL);
    	
    	assertEquals(twentyFourWell.rows(), PlateInteger.ROWS_24WELL);
    	assertEquals(twentyFourWell.columns(), PlateInteger.COLUMNS_24WELL);
    	assertEquals(twentyFourWell.type(), PlateInteger.PLATE_24WELL);
    	
    	assertEquals(fortyEightWell.rows(), PlateInteger.ROWS_48WELL);
    	assertEquals(fortyEightWell.columns(), PlateInteger.COLUMNS_48WELL);
    	assertEquals(fortyEightWell.type(), PlateInteger.PLATE_48WELL);
    	
    	assertEquals(ninetySixWell.rows(), PlateInteger.ROWS_96WELL);
    	assertEquals(ninetySixWell.columns(), PlateInteger.COLUMNS_96WELL);
    	assertEquals(ninetySixWell.type(), PlateInteger.PLATE_96WELL);
    	
    	assertEquals(threeEightyFourWell.rows(), PlateInteger.ROWS_384WELL);
    	assertEquals(threeEightyFourWell.columns(), PlateInteger.COLUMNS_384WELL);
    	assertEquals(threeEightyFourWell.type(), PlateInteger.PLATE_384WELL);
    	
    	assertEquals(fifteenThirtySixWell.rows(), PlateInteger.ROWS_1536WELL);
    	assertEquals(fifteenThirtySixWell.columns(), PlateInteger.COLUMNS_1536WELL);
    	assertEquals(fifteenThirtySixWell.type(), PlateInteger.PLATE_1536WELL);
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

    	Set<WellSetInteger> groups = testPlate.allGroups();
    	
    	for(WellSetInteger set : groups) {
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
        	WellSetInteger badSet = RandomUtil.randomWellSetInteger(
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
    	
    	Set<WellSetInteger> allGroups = testPlate.getGroups(lists);
    	List<WellList> combined = new ArrayList<WellList>();
    	
    	for(WellSetInteger set : allGroups) {
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
    	
    	Set<WellSetInteger> allGroups = testPlate.getGroups(listToArray);
    	List<WellList> combined = new ArrayList<WellList>();
    	
    	for(WellSetInteger set : allGroups) {
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
    	
    	Set<WellSetInteger> allGroups = testPlate.getGroups(listString);
    	List<WellList> combined = new ArrayList<WellList>();
    	
    	for(WellSetInteger set : allGroups) {
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
    	WellSetInteger testSet = testPlate.dataSet();
    	
        int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellInteger> iter = testPlate.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellInteger toHigher = iter.next();
        WellInteger toReturn = iter.next();
        WellInteger input = new WellInteger(toReturn.row(), toReturn.column() - 1);
        WellInteger outside = new WellInteger(testPlate.rows() + 1, testPlate.columns());
        
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
    	WellSetInteger testSet = testPlate.dataSet();
    	
    	int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellInteger> iter = testPlate.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellInteger toReturn = iter.next();
        WellInteger toFloor = iter.next();
        WellInteger input = new WellInteger(toFloor.row(), toFloor.column() - 1);
        WellInteger outside = new WellInteger(minColumn - 1, minColumn);
        
        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;

        assertEquals(testPlate.floor(toReturn), toReturn);
        assertNull(testPlate.floor(outside));
        
    }
    
    /**
     * Tests the poll first method.
     */
    @Test	
    public void testPollFirst() {
    	
    	WellInteger first = testPlate.first();
    	WellInteger polled = testPlate.pollFirst();
    	
    	assertFalse(testPlate.contains(polled));
    	assertEquals(first, polled);
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	WellInteger last = testPlate.last();
    	WellInteger polled = testPlate.pollLast();
    	
    	assertFalse(testPlate.contains(polled));
    	assertEquals(last, polled);
    }  
    
    /**
     * Tests the plate iterator.
     */
    @Test
    public void testIterator() {
    	
    	WellSetInteger testSet = testPlate.dataSet();
    	
    	WellInteger[] array = testSet.toWellArray();
    	Iterator<WellInteger> iter = testPlate.iterator();
    	
    	for(int i = 0; i < array.length; i++) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the descending plate iterator.
     */
    @Test
    public void testDescendingIterator() {
    	
    	WellSetInteger testSet = testPlate.dataSet();
    	
    	WellInteger[] array = testSet.toWellArray();
    	Iterator<WellInteger> iter = testPlate.descendingIterator();
    	
    	for(int i = array.length - 1; i >= 0; i--) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the descending set method.
     */
    @Test
    public void testDescendingSet() {

    	Set<WellInteger> reversed = testPlate.descendingSet();

    	Iterator<WellInteger> iter = testPlate.descendingIterator();
    	Iterator<WellInteger> iterReversed = reversed.iterator();
    	
    	while(iter.hasNext() && iterReversed.hasNext()) {
    		assertEquals(iter.next(), iterReversed.next());
    	}
    }
    
    /**
     * Tests the first method.
     */
    @Test
    public void testFirst() {
    
    	Iterator<WellInteger> iter = testPlate.iterator();
    	
    	assertEquals(iter.next(), testPlate.first());
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {
    	
        Iterator<WellInteger> iter = testPlate.iterator();
    	
    	WellInteger last = null;
    	
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
    	
        WellSetInteger testSet = testPlate.dataSet();
    	
        int current = 0;
    	int index = random.nextInt((testSet.size() - 2) + 1);    
         
        Iterator<WellInteger> iter = testPlate.iterator();
         
        while(current < index) {
            iter.next();
            current++;
        }
         
        WellInteger toCeiling = iter.next();
        WellInteger toReturn = iter.next();
        WellInteger input = new WellInteger(toReturn.row(), toReturn.column());
        WellInteger outside = new WellInteger(testPlate.rows() + 1, testPlate.columns());
         
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

        Iterator<WellInteger> iter = testPlate.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellInteger toReturn = iter.next();
        WellInteger toFloor = iter.next();
        WellInteger input = new WellInteger(toFloor.row(), toFloor.column() - 1);

        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;
        assertEquals(testPlate.floor(toReturn), toReturn);
        
        testPlate.pollFirst();

        WellInteger well = testPlate.floor(new WellInteger(0, 1));
        assertNull(well);
    }
    
    /**
     * Tests the head set method using a well.
     */
    @Test
    public void testHeadSet() {

    	WellSetInteger testSet = testPlate.dataSet();
    	
        WellInteger random = (WellInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellInteger> headSet = testPlate.headSet(random);
    	Set<WellInteger> subSet = testPlate.subSet(testPlate.first(), random);
    	
    	assertEquals(headSet, subSet);
    	
    }
    
    /**
     * Tests the head set method using a well and boolean.
     */
    @Test
    public void testHeadSetInclusive() {

    	WellSetInteger testSet = testPlate.dataSet();
    	
    	WellInteger random = (WellInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellInteger> headSetTrue = testPlate.headSet(random, true);
    	Set<WellInteger> subSetTrue = testPlate.subSet(testSet.first(), true, random, true);
    	Set<WellInteger> headSetFalse = testPlate.headSet(random, false);
    	Set<WellInteger> subSetFalse = testPlate.subSet(testSet.first(), true, random, false);
    	
    	assertEquals(headSetTrue, subSetTrue);
    	assertEquals(headSetFalse, subSetFalse);
    }
    
    /**
     * Tests the tail set method using a well.
     */
    @Test
    public void testTailSet() {

    	WellSetInteger testSet = testPlate.dataSet();
    	
    	WellInteger random = (WellInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellInteger> tailSet = testPlate.tailSet(random);
    	Set<WellInteger> subSet = testPlate.subSet(random, true, testPlate.last(), true);

    	assertEquals(tailSet, subSet);
    }
    
    /**
     * Tests the tail set method using a well and boolean.
     */
    @Test
    public void testTailSetInclusive() {

    	WellSetInteger testSet = testPlate.dataSet();
    	
    	WellInteger random = (WellInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellInteger> tailSetTrue = testPlate.tailSet(random, true);
    	Set<WellInteger> subSetTrue = testPlate.subSet(random, true, testPlate.last(), true);
    	Set<WellInteger> tailSetFalse = testPlate.tailSet(random, false);
    	Set<WellInteger> subSetFalse = testPlate.subSet(random, false, testPlate.last(), true);
    	
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
    	
    	Set<WellInteger> subSetTrueTrue = new HashSet<WellInteger>();
    	Set<WellInteger> subSetTrueFalse = new HashSet<WellInteger>();
    	Set<WellInteger> subSetFalseTrue = new HashSet<WellInteger>();;
    	Set<WellInteger> subSetFalseFalse = new HashSet<WellInteger>();;
    	
    	WellInteger startingWell = null;
    	WellInteger endingWell = null;
    	
    	Iterator<WellInteger> iter = testPlate.iterator();
    	
    	while(current <= begin) {
    		
    		WellInteger inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSetTrueTrue.add(new WellInteger(inclusive));
    		    subSetTrueFalse.add(new WellInteger(inclusive));
    		    startingWell = new WellInteger(inclusive);
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellInteger inclusive = iter.next();
    		
    		if(current + 1 == end) {

    			endingWell = new WellInteger(inclusive);
    		    subSetTrueTrue.add(new WellInteger(inclusive));
    		    subSetFalseTrue.add(new WellInteger(inclusive));
    		    break;
    		    
    		} else {
    			
    			subSetTrueTrue.add(new WellInteger(inclusive));
    			subSetTrueFalse.add(new WellInteger(inclusive));
    			subSetFalseTrue.add(new WellInteger(inclusive));
    			subSetFalseFalse.add(new WellInteger(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellInteger> trueTrue = testPlate.subSet(startingWell, true, endingWell, true);
    	Set<WellInteger> trueFalse = testPlate.subSet(startingWell, true, endingWell, false);
    	Set<WellInteger> falseTrue = testPlate.subSet(startingWell, false, endingWell, true);
    	Set<WellInteger> falseFalse = testPlate.subSet(startingWell, false, endingWell, false);

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
    	
    	
    	Set<WellInteger> subSet = new HashSet<WellInteger>();
    	
    	WellInteger startingWell = null;
    	WellInteger endingWell = null;
    	
    	Iterator<WellInteger> iter = testPlate.iterator();
    	
    	while(current <= begin) {
    		
    		WellInteger inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSet.add(new WellInteger(inclusive));
    		    startingWell = new WellInteger(inclusive);
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellInteger inclusive = iter.next();
    		
    		if(current + 1 == end) {

    			endingWell = new WellInteger(inclusive);
    		    break;
    		    
    		} else {
    			
    			subSet.add(new WellInteger(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellInteger> toCompare = testPlate.subSet(startingWell, endingWell);

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

	    	PlateInteger plate = new PlateInteger(PlateInteger.COLUMNS_12WELL);
	    	
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
    		
    		int columns =  PlateInteger.COLUMNS_48WELL + 
	                 random.nextInt(PlateInteger.COLUMNS_1536WELL - 
			         PlateInteger.COLUMNS_48WELL + 1);
    		
    		int rows = 1;
    		
    		PlateInteger plate = new PlateInteger(rows, columns);

    		assertEquals(plate.columns(), columns);
    		
    	}
    	
    }
    
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
    		
    		PlateInteger plate = new PlateInteger(rows, columns);
    		
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
	    		
	    		testPlate.addWells(new WellInteger(row, column));
	    		
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

	    	Set<WellInteger> toRemove = testPlate.dataSet().subSet(0, testPlate.dataSet().size() / 4);
	    	
	    	for(WellInteger well : toRemove) {
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
    	    PlateInteger plate = new PlateInteger(type);
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
    		PlateInteger plate = new PlateInteger(type);
    		
    		switch(type) {
    		
    		    case PlateInteger.PLATE_6WELL:    assertEquals(plate.descriptor(), "6-Well");
    		                                         break;
    		    case PlateInteger.PLATE_12WELL:   assertEquals(plate.descriptor(), "12-Well");
                                                     break;
    		    case PlateInteger.PLATE_24WELL:   assertEquals(plate.descriptor(), "24-Well");
                                                     break;
    		    case PlateInteger.PLATE_48WELL:   assertEquals(plate.descriptor(), "48-Well");
                                                     break;
    		    case PlateInteger.PLATE_96WELL:   assertEquals(plate.descriptor(), "96-Well");
                                                     break;
    		    case PlateInteger.PLATE_384WELL:  assertEquals(plate.descriptor(), "384-Well");
                                                     break;
    		    case PlateInteger.PLATE_1536WELL: assertEquals(plate.descriptor(), "1536-Well");
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		int rows = PlateInteger.ROWS_48WELL + 
	                   random.nextInt(PlateInteger.ROWS_1536WELL - 
   		               PlateInteger.ROWS_48WELL + 1);

            int columns =  PlateInteger.COLUMNS_48WELL + 
	                       random.nextInt(PlateInteger.COLUMNS_1536WELL - 
			               PlateInteger.COLUMNS_48WELL + 1);
            
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
    		PlateInteger plate = new PlateInteger(0);
    		assertEquals(plate.dataType(), PlateInteger.PLATE_INTEGER);
    	}
    }
    
    /**
     * Tests the data type string getter.
     */
    @Test
    public void testDataTypeString() {
    	for(int i = 0; i < 100; i++) {
    		PlateInteger plate = new PlateInteger(0);
    		assertEquals(plate.dataTypeString(), "Integer");
    	}
    }
    
    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

        for(int i = 0; i < 100; i++) {
    		
    		int type = random.nextInt(6 + 1);
    		PlateInteger plate = new PlateInteger(type);
    		
    		String toString = "Type: ";
    		
    		switch(type) {
    		
    		    case PlateInteger.PLATE_6WELL:    toString += "6-Well Label: ";
    		                                         break;
    		    case PlateInteger.PLATE_12WELL:   toString += "12-Well Label: ";
                                                     break;
    		    case PlateInteger.PLATE_24WELL:   toString += "24-Well Label: ";
                                                     break;
    		    case PlateInteger.PLATE_48WELL:   toString += "48-Well Label: ";
                                                     break;
    		    case PlateInteger.PLATE_96WELL:   toString += "96-Well Label: ";
                                                     break;
    		    case PlateInteger.PLATE_384WELL:  toString += "384-Well Label: ";
                                                     break;
    		    case PlateInteger.PLATE_1536WELL: toString += "1536-Well Label: ";
                                                     break;
                default: throw new IllegalArgumentException("Invalid plate type constant.");
    		
    		}
    		
    		toString += plate.label();
    		
    		assertEquals(toString, plate.toString());
    		
    		int rows = PlateInteger.ROWS_48WELL + 
	                   random.nextInt(PlateInteger.ROWS_1536WELL - 
   		               PlateInteger.ROWS_48WELL + 1);

            int columns =  PlateInteger.COLUMNS_48WELL + 
	                       random.nextInt(PlateInteger.COLUMNS_1536WELL - 
			               PlateInteger.COLUMNS_48WELL + 1);
            
            PlateInteger randomPlate = new PlateInteger(rows, columns);
            
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
    	
    	for(WellSetInteger set : sets) {
    		
    		testPlate.clearWells();
    		testPlate.addWells(set);
    		
    		Iterator<WellInteger> iterSet = set.iterator();
    		Iterator<WellInteger> iterPlate = testPlate.iterator();
    		
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

    	for(WellInteger[] array : arrays) {
    		
    		testPlate.clearWells();
    		testPlate.addWells(array);
    		
    		WellInteger[] plateArray = testPlate.toArray();
    		
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
    		
    		WellInteger well = wells.get(i);
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
    		
    		WellSetInteger set = sets.get(i);
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
    		
    		TreeSet<WellInteger> collection = collections.get(i);
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
    		
    		WellInteger[] array = arrays.get(i);
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
    		
    		WellInteger well = wells.get(i);
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
    		
    		WellSetInteger set = sets.get(i);
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
    		
    		TreeSet<WellInteger> collection = collections.get(i);
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
    		
    		WellInteger[] array = arrays.get(i);
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
    		
    		TreeSet<WellInteger> collection = collections.get(i);
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
    		
    		WellInteger well = wells.get(i);
    		
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
    		
    		WellInteger well = wells.get(i);
    		
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
    		
    		WellSetInteger set = sets.get(i);
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
    		
        	WellInteger input = wells.get(i);
        	input.clear();
        	input.add(RandomUtil.randomIntegerList(minValue, maxValue, minLength, maxLength));
        	
        	WellInteger current = testPlate.getWells(input);
        	testPlate.replaceWells(input);

        	if(current != null) {

        		current = new WellInteger(current);
        		
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
        	WellSetInteger set = new WellSetInteger();
        	WellSetInteger previous = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    
        		WellInteger toAdd = new WellInteger(iter.next());
        		previous.add(new WellInteger(toAdd));
     
        	    toAdd.clear();
        	    toAdd.add(RandomUtil.randomIntegerList(minValue, maxValue, minLength, maxLength));
        	    set.add(toAdd);
        	    current++;
        	}
        	
        	testPlate.replaceWells(set);

        	Iterator<WellInteger> previousIter = previous.iterator();

        	for(WellInteger well : set) {
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
        	WellSetInteger set = new WellSetInteger();
        	WellSetInteger previous = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    
        		WellInteger toAdd = new WellInteger(iter.next());
        		previous.add(new WellInteger(toAdd));
     
        	    toAdd.clear();
        	    toAdd.add(RandomUtil.randomIntegerList(minValue, maxValue, minLength, maxLength));
        	    set.add(toAdd);
        	    current++;
        	}
        	
        	testPlate.replaceWells(set.allWells());

        	Iterator<WellInteger> previousIter = previous.iterator();

        	for(WellInteger well : set) {
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
        	WellSetInteger set = new WellSetInteger();
        	WellSetInteger previous = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    
        		WellInteger toAdd = new WellInteger(iter.next());
        		previous.add(new WellInteger(toAdd));
     
        	    toAdd.clear();
        	    toAdd.add(RandomUtil.randomIntegerList(minValue, maxValue, minLength, maxLength));
        	    set.add(toAdd);
        	    current++;
        	}
        	
        	testPlate.replaceWells(set.toWellArray());

        	Iterator<WellInteger> previousIter = previous.iterator();

        	for(WellInteger well : set) {
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
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		
    		PlateInteger clone = new PlateInteger(testPlate);
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set);

        	for(WellInteger well : set) {
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set.allWells());

        	for(WellInteger well : set) {
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set.toWellArray());

        	for(WellInteger well : set) {
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	String list = this.getDelimiterList(set.allWells());
        	clone.retainWells(list, ",");

        	for(WellInteger well : set) {
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
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		String wellString = well.index();
    		
    		PlateInteger clone = new PlateInteger(testPlate);
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
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		WellIndex index = new WellIndex(well.row(), well.column());
    		
    		PlateInteger clone = new PlateInteger(testPlate);
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(clone.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(clone.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	clone.retainWells(set.wellList());

        	for(WellInteger well : set) {
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
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
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
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	WellSetInteger returned = testPlate.getWells(set);

        	for(WellInteger well : set) {
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
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	WellSetInteger returned = testPlate.getWells(set.allWells());

        	for(WellInteger well : set) {
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
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	WellSetInteger returned = testPlate.getWells(set.toWellArray());

        	for(WellInteger well : set) {
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
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	String list = this.getDelimiterList(set.allWells());
        	WellSetInteger returned = testPlate.getWells(list, ",");

        	for(WellInteger well : set) {
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
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		String wellString = well.index();
    		
    		WellInteger returned = testPlate.getWells(wellString);

    		assertEquals(returned, well);
    		
    	}
    }
    
    /**
     * Tests the get wells method using a well index.
     */
    @Test
    public void testGetWellsIndex() {

    	for(int i = 0; i < 100; i++) {
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
    				testPlate.dataSet().allWells());
    		WellIndex index = new WellIndex(well.row(), well.column());
    		
    		WellInteger returned = testPlate.getWells(index);

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
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	WellSetInteger returned = testPlate.getWells(set.wellList());

        	for(WellInteger well : set) {
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

        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(iter.hasNext()) {
        		
        		WellInteger well = iter.next();
        		
        		if(well.row() != row) {
        			set.add(well);
        		}

        	}

        	testPlate.clearWells();
        	testPlate.addWells(set);
        	
        	WellSetInteger rowWells = new WellSetInteger();
        	
        	for(int j = 1; j < testPlate.columns() / 2; j++) {
        		WellInteger toAdd = new WellInteger(row, j);
        		toAdd.add(RandomUtil.randomIntegerList(minValue, maxValue, minLength, maxLength));
        		testPlate.addWells(toAdd);
        		rowWells.add(toAdd);
        	}
        	
        	WellSetInteger returned = testPlate.getRow(row);

        	for(WellInteger well : rowWells) {
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

        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(iter.hasNext()) {
        		
        		WellInteger well = iter.next();
        		
        		if(well.column() != column) {
        			set.add(well);
        		}

        	}

        	testPlate.clearWells();
        	testPlate.addWells(set);
        	
        	WellSetInteger columnWells = new WellSetInteger();
        	
        	for(int j = 0; j < testPlate.rows() / 2; j++) {
        		WellInteger toAdd = new WellInteger(j, column);
        		toAdd.add(RandomUtil.randomIntegerList(minValue, maxValue, minLength, maxLength));
        		testPlate.addWells(toAdd);
        		columnWells.add(toAdd);
        	}
        	
        	WellSetInteger returned = testPlate.getColumn(column);

        	for(WellInteger well :columnWells) {
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
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

    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	assertTrue(clone.contains(set));
        	clone.removeWells(set);
        	assertFalse(clone.contains(set));
        	
        	for(WellInteger well : set) {
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

    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}
        	
        	Set<WellInteger> collection = set.allWells();
        	
        	assertTrue(clone.contains(collection));
        	clone.removeWells(collection);
        	assertFalse(clone.contains(collection));
        	
        	for(WellInteger well : collection) {
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

    		PlateInteger clone = new PlateInteger(testPlate);

    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	WellInteger[] array = set.toWellArray();
        	
        	assertTrue(clone.contains(array));
        	clone.removeWells(array);
        	assertFalse(clone.contains(array));
        	
        	for(WellInteger well : array) {
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

    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	String list = this.getDelimiterList(set.allWells());

        	assertTrue(clone.contains(list, ","));
        	clone.removeWells(list, ",");
        	assertFalse(clone.contains(list, ","));
        	
        	for(WellInteger well : set) {
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
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
    		
    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		WellInteger well = (WellInteger) RandomUtil.randomObject(
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

    		PlateInteger clone = new PlateInteger(testPlate);
    		
    		int begin = random.nextInt(testPlate.size() - 2 - 0 + 1) + 0;
        	int end = random.nextInt(testPlate.size() - 1 - begin) + begin + 1;
        	int current = 0;
        	WellSetInteger set = new WellSetInteger();
        	
        	Iterator<WellInteger> iter = testPlate.iterator();
        	
        	while(current < begin) {
        		iter.next();
        		current++;
        	}
        	
        	while(current < end) {
        	    set.add(new WellInteger(iter.next()));
        	    current++;
        	}

        	assertTrue(clone.contains(set.wellList()));
        	clone.removeWells(set.wellList());
        	assertFalse(clone.contains(set.wellList()));
        	
        	for(WellInteger well : set) {
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
    	
    	for(WellSetInteger set : sets) {
    		
    		testPlate.clearWells();
    		
    		String plateString = testPlate.label() + " " + testPlate.descriptor() + "\n";
    		
    		for(WellInteger well : set) {
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

    	for(WellInteger well : wells) {
    		
    		String wellString = well.toString();
    		
    		testPlate.replaceWells(well);

    		assertEquals(wellString, testPlate.printData(well));
    	}
    	
    }
    
    /*------------------------ Testing helper methods ------------------------*/
    
    /**
     * Creates a delimiter separated list from a collection.
     * @param    Collection<WellInteger>    the input collection
     * @return                              the delimiter separated list
     */
    public String getDelimiterList(Collection<WellInteger> collection) {
    	
    	String str = "";
    	Iterator<WellInteger> iter = collection.iterator();
    	
    	while(iter.hasNext()) {
    		
    		WellInteger well = iter.next();
    		
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
