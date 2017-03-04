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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the well set big decimal
 * class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WellSetBigDecimalTest {

    /* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private BigDecimal minValue = new BigDecimal(-1000000000);   // Minimum big decimal value for random sets
	private BigDecimal maxValue = new BigDecimal(1000000000);    // Maximum big decimal value for random sets
	private int minLength = 100;                                 // Minimum well set length
	private int maxLength = 1000;                                // Maximum well set length
	private int minRow = 0;                                      // Minimum well row
	private int maxRow = 1000;                                   // Maximum well row
	private int minColumn = 1;                                   // Minimum well column
	private int maxColumn = 1000;                                // Maximum well column
	private int wellNumber = 200;                                // Number of wells used in select tests
	private Random random = new Random();                        // Generates random integers
	
	/* Objects used to setup each test */
	
	private WellSetBigDecimal testSet;         // A random set of wells
	private WellSetBigDecimal clone1;          // Clone of the test set
	private WellSetBigDecimal clone2;          // Clone of the test set
	private int begin;                         // Beginning index for subsets
	private int end;                           // Ending index for subsets
	private int size;                          // Size of the test set
	private Set<WellBigDecimal> collection;    // Random collection of wells
	int current;                               // Starting index into test set 
	
    /* Value of false redirects System.err */
	
	private static boolean error = false;
	private static PrintStream originalOut = System.out;
	
	/*--------------------- Methods run before each test ---------------------*/
	
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
	 * Creates objects used by nearly all test methods.
	 */
	@Before
	public void setup() {
		
		testSet = RandomUtil.randomWellSetBigDecimal(minValue, 
    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
		clone1 = new WellSetBigDecimal(testSet);
		clone2 = new WellSetBigDecimal(testSet);
		
		begin = random.nextInt(testSet.size() - 2 - 0 + 1) + 0;
    	end = random.nextInt(testSet.size() - 1 - begin) + begin + 1;
		current = 0;
    	
    	size = testSet.size();
    	
		collection = this.randomWellCollection(testSet);
	}
	
	/**
	 * Toggles system error.
	 */
	@AfterClass
	public static void restoreErrorOut() {
		System.setErr(originalOut);
	}
	
    /*--------------------------- Constructor Tests --------------------------*/
    
    /**
     * Tests a well set constructor with no arguments.
     */
    @Test
    public void testConstructor() {
        WellSetBigDecimal set = new WellSetBigDecimal();
        assertNotNull(set);
        assertTrue(set.label().equals("WellSetBigDecimal"));
        assertTrue(set.wellList().size() == 0);
    }
    
    /**
     * Tests a well set constructor using a well collection as an argument.
     */
    @Test
    public void testConstructorCollection() {
    	
    	WellSetBigDecimal set = new WellSetBigDecimal(collection);    

    	assertNotNull(set);
        assertEquals(set.label(), "WellSetBigDecimal");
        assertEquals(set.size(), collection.size());
    }
    
    /**
     * Tests a well set constructor using a well collection and a label as arguments.
     */
    @Test
    public void testconstructorCollectionLabel() {

    	String label = "TestLabel";
    	
    	WellSetBigDecimal set = new WellSetBigDecimal(collection, label);    
        
    	assertNotNull(set);
        assertEquals(set.label(), label);
        assertEquals(set.size(), collection.size());
    }
    
    /**
     * Tests a well set constructor using a well set as an argument.
     */
    @Test
    public void testConstructorWellSet() {
    	
    	WellSetBigDecimal testSet2 = new WellSetBigDecimal(testSet);
    	
    	assertEquals(testSet, testSet2);
    }
    
    /**
     * Tests a well set constructor using a well set and label as arguments.
     */
    @Test
    public void testConstructorWellSetLabel() {
    	
    	String label = "TestLabel";

    	testSet.setLabel(label);
    	
    	WellSetBigDecimal testSet2 = new WellSetBigDecimal(testSet, label);
    	
    	assertEquals(testSet, testSet2);
    }
    
    /**
     * Tests a well set constructor using an array of wells as an argument.
     */
    @Test
    public void testConstructorWellArray() {

    	WellBigDecimal[] array = collection.toArray(new WellBigDecimal[collection.size()]);
    	
    	WellSetBigDecimal set = new WellSetBigDecimal(array);    

    	assertNotNull(set);
        assertEquals(set.label(), "WellSetBigDecimal");
        assertEquals(set.size(), array.length);
    }
    
    /**
     * Tests a well set constructor using an array of wells and a label as arguments.
     */
    @Test
    public void testConstructorWellArrayLabel() {

    	WellBigDecimal[] array = collection.toArray(new WellBigDecimal[collection.size()]);
    	String label = "TestLabel";
    	WellSetBigDecimal set = new WellSetBigDecimal(array, label);    

    	assertNotNull(set);
        assertEquals(set.label(), label);
        assertEquals(set.size(), array.length);
    	
    }
    
    /*------------------------ Tests add well methods ------------------------*/
    
    /**
     * Tests the addition of a well.
     */
    @Test
    public void testAddWell() { 

    	WellBigDecimal well = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
    	
    	testSet.add(well);
    	
    	assertEquals(testSet.size(), size + 1);
    	assertTrue(testSet.contains(well));
    }
    
    /**
     * Tests the addition of a well collection.
     */
    @Test
    public void testAddCollection() {

    	testSet.add(collection);
    	
    	assertEquals(testSet.size(), size + collection.size());
    	assertTrue(testSet.contains(collection));
    }
    
    /**
     * Tests the addition of a well array.
     */
    @Test
    public void testAddWellArray() {

    	WellBigDecimal[] array = collection.toArray(new WellBigDecimal[collection.size()]);
    	
    	testSet.add(array);
    	
    	assertEquals(testSet.size(), size + array.length);
    	assertTrue(testSet.contains(array));
    }
    
    /**
     * Tests the addition of a well set.
     */
    @Test
    public void testAddWellSet() {

    	WellSetBigDecimal testSet2 = new WellSetBigDecimal(collection);
    	
    	testSet.add(testSet2);
    	
    	assertEquals(testSet.size(), size + testSet2.size());
    	assertTrue(testSet.contains(testSet2));
    }
    
    /*----------------------- Tests remove well methods ----------------------*/

    /**
     * Tests removal of a well.
     */
    @Test
    public void testRemoveWell() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());

    	testSet.remove(random);
    	
    	assertEquals(testSet.size(), size - 1);
    	assertFalse(testSet.contains(random));
    	
    	WellBigDecimal notCommon = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);
    	
    	size = testSet.size();
    	WellSetBigDecimal testSet2 = new WellSetBigDecimal(testSet);
    	testSet.remove(notCommon);
    	
    	assertEquals(testSet.size(), size);
    	assertEquals(testSet, testSet2);
    }
    

    /**
     * Tests removal of a well set.
     */
    @Test
    public void testRemoveSet() {

    	WellSetBigDecimal randomSet = new WellSetBigDecimal(testSet.subSet(begin, end));

    	clone1.remove(randomSet);

    	assertEquals(clone1.size(), testSet.size() - randomSet.size());
    	assertFalse(clone1.contains(randomSet));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.remove(noCommonWells);
    	
    	assertEquals(testSet, clone2);
    	assertFalse(clone2.contains(noCommonWells));
    	assertEquals(testSet.size(), clone2.size());
    	
    }
    
    /**
     * Tests removal of a collection of wells.
     */
    @Test
    public void testRemoveCollection() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);

    	clone1.remove(randomSet);

    	assertEquals(clone1.size(), testSet.size() - randomSet.size());
    	assertFalse(clone1.contains(randomSet));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.remove(noCommonWells);
    	
    	assertEquals(testSet, clone2);
    	assertFalse(clone2.contains(noCommonWells));
    	assertEquals(testSet.size(), clone2.size());

    }

    /**
     * Tests removal of an array of wells.
     */
    @Test
    public void testRemoveArray() {
    	
    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellBigDecimal[] randomArray = randomSet.toArray(new WellBigDecimal[randomSet.size()]);
    	
    	clone1.remove(randomArray);

    	assertEquals(clone1.size(), testSet.size() - randomArray.length);
    	assertFalse(clone1.contains(randomArray));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.remove(noCommonWells);
    	
    	assertEquals(testSet, clone2);
    	assertFalse(clone2.contains(noCommonWells));
    	assertEquals(testSet.size(), clone2.size());
    }

    /**
     * Tests removal of a delimiter separated list of wells.
     */
    @Test
    public void testRemoveWellListStringDelimiter() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigDecimal> iter = randomSet.iterator();

    	while(iter.hasNext()) {

    		WellBigDecimal next = iter.next();

    	    if(iter.hasNext()) {
    	    	list += next.index() + "@ ";
    	    } else {
    	    	list += next.index();
    	    	break;
    	    }
    	}

    	testSet.remove(list, "@");

    	assertFalse(testSet.contains(randomSet));
    	assertEquals(testSet.size(), size - randomSet.size());
    }
    
    
    /**
     * Tests removal of a well string.
     */
    @Test
    public void testRemoveString() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigDecimal> iter = randomSet.iterator();

    	while(iter.hasNext()) {
    	    WellBigDecimal next = iter.next();
    	    if(iter.hasNext()) {
    	    	list += next.index() + ", ";
    	    } else {
    	    	list += next.index();
    	    	break;
    	    }
    	}

    	testSet.remove(list);

    	assertFalse(testSet.contains(randomSet));
    	assertEquals(testSet.size(), size - randomSet.size());
    }

    /**
     * Tests removal of a well index.
     */
    @Test
    public void testRemoveIndex() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	testSet.remove(index);
    	
    	assertEquals(testSet.size(), size - 1);
    	assertFalse(testSet.contains(index));
    	
    	WellBigDecimal notCommon = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);
    	WellIndex indexNotCommon = new WellIndex(notCommon.row(), notCommon.column());
    	
    	size = testSet.size();
    	
    	WellSetBigDecimal testSet2 = new WellSetBigDecimal(testSet);
    	
    	testSet.remove(indexNotCommon);
    	
    	assertEquals(testSet.size(), size);
    	assertEquals(testSet, testSet2);
    }

    /**
     * Tests removal of a well list.
     */
    @Test
    public void testRemoveWellList() {
    	
    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellSetBigDecimal listSet = new WellSetBigDecimal(randomSet);
    	WellList list = listSet.wellList();
    	
    	clone1.remove(list);

    	assertEquals(clone1.size(), testSet.size() - list.size());
    	assertFalse(clone1.contains(list));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);
    	WellSetBigDecimal notCommonSet = new WellSetBigDecimal(noCommonWells);
    	WellList listNotCommon = notCommonSet.wellList();
    	
    	clone2.remove(listNotCommon);
    	
    	assertEquals(testSet, clone2);
    	assertFalse(clone2.contains(listNotCommon));
    	assertEquals(testSet.size(), clone2.size());
    }
    
    /*----------------------- Tests replace well methods ---------------------*/
    
    /**
     * Tests well replacement.
     */
    @Test
    public void testReplace() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	WellBigDecimal replace = new WellBigDecimal(random.row(), random.column(), RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));

    	WellBigDecimal toReplace = new WellBigDecimal(testSet.getWells(random.index()));
    	testSet.replace(replace);

    	assertEquals(testSet.size(), size);
    	assertTrue(testSet.contains(random));
    	assertFalse(testSet.getWells(random.index()).data().equals(random.data()));
    	assertTrue(testSet.getWells(random.index()).equals(toReplace));
    	
        size = testSet.size();
    	
    	WellBigDecimal well = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
    	
    	testSet.add(well);
    	
    	assertEquals(testSet.size(), size + 1);
    	assertTrue(testSet.contains(well));
    }   
    
    /**
     * Tests set replacement.
     */
    @Test
    public void testReplaceWellSet() {
    	
    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	WellSetBigDecimal randomSet = new WellSetBigDecimal();
    	
    	for(WellBigDecimal well : subset) {
    		randomSet.add(new WellBigDecimal(well.row(), well.column(), 
    				RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength)));
    	}
    	
    	clone1.replace(randomSet);

    	assertTrue(clone1.contains(randomSet));
    	assertEquals(clone1.size(), size);
    	
    	for(WellBigDecimal well : randomSet) {
    		assertEquals(clone1.getWells(well.index()).data(), well.data());
            assertFalse(testSet.getWells(well).data().equals(clone1.getWells(well).data()));
    	}
    	
    	WellSetBigDecimal noCommonWells = new WellSetBigDecimal(this.randomWellCollection(testSet));  	
    	clone2.replace(noCommonWells);
    	
    	assertTrue(clone2.contains(noCommonWells));
    	
    	for(WellBigDecimal well : noCommonWells) {
    		assertEquals(clone2.getWells(well).data(), well.data());
    		assertFalse(testSet.contains(well));
    	}
    }
    
    /**
     * Tests collection replacement.
     */
    @Test
    public void testReplaceCollection() {
    	
    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	Set<WellBigDecimal> randomSet = new HashSet<WellBigDecimal>();
    	
    	for(WellBigDecimal well : subset) {
    		randomSet.add(new WellBigDecimal(well.row(), well.column(), 
    				RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength)));
    	}
    	
    	clone1.replace(randomSet);

    	assertTrue(clone1.contains(randomSet));
    	assertEquals(clone1.size(), size);
    	
    	for(WellBigDecimal well : randomSet) {
    		assertEquals(clone1.getWells(well.index()).data(), well.data());
            assertFalse(testSet.getWells(well).data().equals(clone1.getWells(well).data()));
    	}
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.replace(noCommonWells);
    	
    	assertTrue(clone2.contains(noCommonWells));
    	
    	for(WellBigDecimal well : noCommonWells) {
    		assertEquals(clone2.getWells(well).data(), well.data());
    		assertFalse(testSet.contains(well));
    	}
    	
    }
    
    /**
     * Tests well array replacement.
     */
    @Test
    public void testReplaceArray() {

    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	WellBigDecimal[] arraySubset = new WellBigDecimal[subset.size()];
    	
    	int sizeClone2 = clone2.size();
    	
    	int index = 0;
    	
    	for(WellBigDecimal well : subset) {
    		arraySubset[index++] = new WellBigDecimal(well.row(), well.column(), 
    				RandomUtil.randomBigDecimalList(minValue, maxValue, minLength, maxLength));
    	}
    	
    	clone1.replace(arraySubset);

    	assertTrue(clone1.contains(arraySubset));
    	assertEquals(clone1.size(), sizeClone2);
    	
    	for(WellBigDecimal well : arraySubset) {
    		assertEquals(clone1.getWells(well.index()).data(), well.data());
            assertFalse(testSet.getWells(well).data().equals(clone1.getWells(well).data()));
    	}
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);
    	WellBigDecimal[] arrayNoneShared = noCommonWells.toArray(new WellBigDecimal[noCommonWells.size()]);
    	clone2.replace(arrayNoneShared);
    	
    	assertTrue(clone2.contains(arrayNoneShared));
    	assertEquals(clone2.size(), sizeClone2 + arrayNoneShared.length);
    	
    	for(WellBigDecimal well : arrayNoneShared) {
    		assertEquals(clone2.getWells(well).data(), well.data());
    		assertFalse(testSet.contains(well));
    	}
    }
    
    /*----------------------- Tests retain well methods ----------------------*/
    
    /**
     * Tests retention of a well.
     */
    @Test
    public void testRetain() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());

    	testSet.retain(random);
    	assertEquals(testSet.size(), 1);
    	assertTrue(testSet.contains(random));
    	
    	WellBigDecimal notCommon = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);

    	testSet.retain(notCommon);
    	
    	assertEquals(testSet.size(), 0);
    }
    
    /**
     * Tests retention of a well set.
     */
    @Test
    public void testRetainSet() {

    	WellSetBigDecimal randomSet = new WellSetBigDecimal(testSet.subSet(begin, end));

    	clone1.retain(randomSet);

    	assertEquals(clone1.size(), randomSet.size());
    	assertTrue(clone1.contains(randomSet));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.retain(noCommonWells);

    	assertEquals(clone2.size(), 0);    	
    }
    
    /**
     * Tests retention of a collection.
     */
    @Test
    public void testRetainCollection() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);

    	clone1.retain(randomSet);

    	assertEquals(clone1.size(), randomSet.size());
    	assertTrue(clone1.contains(randomSet));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.retain(noCommonWells);
    	
    	assertEquals(clone2.size(), 0);
    }
    
    /**
     * Tests retention of an array of wells.
     */
    @Test
    public void testRetainArray() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellBigDecimal[] randomArray = randomSet.toArray(new WellBigDecimal[randomSet.size()]);
    	
    	clone1.retain(randomArray);

    	assertEquals(clone1.size(), randomArray.length);
    	assertTrue(clone1.contains(randomArray));
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.retain(noCommonWells);

    	assertEquals(clone2.size(), 0);
    }
    
    /**
     * Tests retention of a delimiter separated list of wells.
     */
    @Test
    public void testRetainWellListStringDelimiter() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigDecimal> iter = randomSet.iterator();

    	while(iter.hasNext()) {

    		WellBigDecimal next = iter.next();

    	    if(iter.hasNext()) {
    	    	list += next.index() + ",";
    	    } else {
    	    	list += next.index();
    	    	break;
    	    }
    	}

    	Set<WellBigDecimal> notCommon = this.randomWellCollection(testSet);  
    	String listNotCommon = "";
    	
    	Iterator<WellBigDecimal> iterNotCommon = notCommon.iterator();

    	while(iterNotCommon.hasNext()) {

    		WellBigDecimal next = iterNotCommon.next();

    	    if(iterNotCommon.hasNext()) {
    	    	listNotCommon += next.index() + ",";
    	    } else {
    	    	listNotCommon += next.index();
    	    	break;
    	    }
    	}
    	
    	clone1.retain(list, ",");

    	assertEquals(clone1.size(), list.split(",").length);
    	assertTrue(clone1.contains(randomSet));
	
    	clone2.retain(listNotCommon, ",");

    	assertEquals(clone2.size(), 0);
    }
    
    /**
     * Tests retention of a well string.
     */
    @Test
    public void testRetainString() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	String randomString = random.index();
    	
    	testSet.retain(randomString);
    	
    	assertEquals(testSet.size(), 1);
    	assertTrue(testSet.contains(randomString));
    	
    	WellBigDecimal notCommon = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);
    	String notCommonString = notCommon.index();
    	
    	testSet.retain(notCommonString);
    	
    	assertEquals(testSet.size(), 0);
    }
    
    /**
     * Tests retention of a well index.
     */
    @Test
    public void testRetainIndex() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	testSet.retain(index);
    	
    	assertEquals(testSet.size(), 1);
    	assertTrue(testSet.contains(index));
    	
    	WellBigDecimal notCommon = RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);
    	WellIndex notCommonIndex = new WellIndex(notCommon.row(), notCommon.column());
    	
    	testSet.retain(notCommonIndex);
    	
    	assertEquals(testSet.size(), 0);
    }
    
    /**
     * Tests retention of a well list.
     */
    @Test
    public void testRetainWellList() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellSetBigDecimal randomWellSet = new WellSetBigDecimal(randomSet);
    	WellList list = randomWellSet.wellList();
    	
    	clone1.retain(list);

    	assertEquals(clone1.size(), list.size());
    	assertTrue(clone1.contains(list));
    	
    	Set<WellBigDecimal> notCommonWells = this.randomWellCollection(testSet);  
    	WellSetBigDecimal notCommonSet = new WellSetBigDecimal(notCommonWells);
    	WellList notCommonList = notCommonSet.wellList();
    	
    	clone2.retain(notCommonList);
    	
    	assertEquals(clone2.size(), 0);
    }
    
    /*---------------------- Tests tree set methods --------------------------*/
    
    /**
     * Tests the ceiling method.
     */
    @Test
    public void testCeiling() {

        int index = random.nextInt((testSet.size() - 2) + 1);    
        
        Iterator<WellBigDecimal> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toCeiling = iter.next();
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal input = new WellBigDecimal(toReturn.row(), toReturn.column() - 1);
        WellBigDecimal outside = new WellBigDecimal(maxRow + 1, maxColumn);
        
        toReturn = toCeiling.compareTo(input) < 1 ? toCeiling : toReturn;
        
        assertEquals(testSet.ceiling(toCeiling), toReturn);
        assertNull(testSet.ceiling(outside));
    }
    
    /**
     * Tests the descending iterator.
     */
    @Test
    public void testDescendingIterator() {

    	WellBigDecimal[] array = testSet.toWellArray();
    	Iterator<WellBigDecimal> iter = testSet.descendingIterator();
    	
    	for(int i = array.length - 1; i >= 0; i--) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the descending set method.
     */
    @Test
    public void testDescendingSet() {

    	Set<WellBigDecimal> reversed = testSet.descendingSet();

    	Iterator<WellBigDecimal> iter = testSet.descendingIterator();
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

    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	assertEquals(iter.next(), testSet.first());
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {

    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	WellBigDecimal last = null;
    	
    	while(iter.hasNext()) {
    		last = iter.next();
    	}
    	
    	assertEquals(last, testSet.last());
    }
    
    /**
     * Tests the floor method.
     */
    @Test
    public void testFloor() {

        int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellBigDecimal> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal toFloor = iter.next();
        WellBigDecimal input = new WellBigDecimal(toFloor.row(), toFloor.column() - 1);
        WellBigDecimal outside = new WellBigDecimal(minColumn - 1, minColumn);
        
        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;

        assertEquals(testSet.floor(toReturn), toReturn);
        assertNull(testSet.floor(outside));
    }
    
    /**
     * Tests the head set method using a well as a parameter.
     */
    @Test
    public void testHeadSetWell() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> headSet = testSet.headSet(random);
    	Set<WellBigDecimal> subSet = testSet.subSet(testSet.first(), random);
    	
    	assertEquals(headSet, subSet);
    	
    }
    
    /**
     * Tests the head set inclusive method using a well as a parameter.
     */
    @Test
    public void testHeadSetInclusiveWell() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> headSetTrue = testSet.headSet(random, true);
    	Set<WellBigDecimal> subSetTrue = testSet.subSet(testSet.first(), true, random, true);
    	Set<WellBigDecimal> headSetFalse = testSet.headSet(random, false);
    	Set<WellBigDecimal> subSetFalse = testSet.subSet(testSet.first(), true, random, false);
    	
    	assertEquals(headSetTrue, subSetTrue);
    	assertEquals(headSetFalse, subSetFalse);
    }
    
    /**
     * Tests the head set method using an index as a parameter.
     */
    @Test
    public void testHeadSetIndex() {

        int index = random.nextInt((testSet.size()) + 1);
    	
    	Set<WellBigDecimal> headSet = testSet.headSet(index);
    	Set<WellBigDecimal> subSet = testSet.subSet(0, index);
    	
    	assertEquals(headSet, subSet);
    	
    }
    
    /**
     * Tests the head set inclusive method using an index as a parameter.
     */
    @Test
    public void testHeadSetInclusiveIndex() {

        int index = random.nextInt((testSet.size()) + 1);

    	Set<WellBigDecimal> headSetTrue = testSet.headSet(index, true);
    	Set<WellBigDecimal> subSetTrue = testSet.subSet(0, true, index, true);
    	Set<WellBigDecimal> headSetFalse = testSet.headSet(index, false);
    	Set<WellBigDecimal> subSetFalse = testSet.subSet(0, true, index, false);
    	
    	assertEquals(headSetTrue, subSetTrue);
    	assertEquals(headSetFalse, subSetFalse);
    }
    
    /**
     * Tests the higher method.
     */
    @Test
    public void testHigher() {

        int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellBigDecimal> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toHigher = iter.next();
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal input = new WellBigDecimal(toReturn.row(), toReturn.column() - 1);
        WellBigDecimal outside = new WellBigDecimal(maxRow + 1, maxColumn);
        
        toReturn = toHigher.compareTo(input) < 1 ? toHigher : toReturn;
        
        assertEquals(testSet.ceiling(toHigher), toReturn);
        assertNull(testSet.ceiling(outside));
    }
    
    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {

    	WellSetBigDecimal empty = new WellSetBigDecimal();
    	assertTrue(empty.isEmpty());
    	
    	empty.add(RandomUtil.randomWellBigDecimal(minValue, 
    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength));
    	assertFalse(empty.isEmpty());
    }
    
    /**
     * Tests the iterator.
     */
    @Test
    public void testIterator() {

    	WellBigDecimal[] array = testSet.toWellArray();
    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	for(int i = 0; i < array.length; i++) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the lower method.
     */
    @Test
    public void testLower() {

        int index = random.nextInt((testSet.size() - 2) + 1);
       
        Iterator<WellBigDecimal> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toReturn = iter.next();
        WellBigDecimal toFloor = iter.next();
        WellBigDecimal input = new WellBigDecimal(toFloor.row(), toFloor.column() - 1);
        WellBigDecimal outside = new WellBigDecimal(minColumn - 1, minColumn);
        
        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;

        assertEquals(testSet.floor(toReturn), toReturn);
        assertNull(testSet.floor(outside));
    }
    
    /**
     * Tests the poll first method.
     */
    @Test
    public void testPollFirst() {

    	WellBigDecimal first = testSet.first();
    	WellBigDecimal polled = testSet.pollFirst();
    	
    	assertFalse(testSet.contains(polled));
    	assertEquals(first, polled);
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	WellBigDecimal last = testSet.last();
    	WellBigDecimal polled = testSet.pollLast();
    	
    	assertFalse(testSet.contains(polled));
    	assertEquals(last, polled);
    }
    
    
    /**
     * Tests subset method inclusive using two wells as parameters.
     */
    @Test
    public void testSubSetInclusiveWell() {
    	
    	Set<WellBigDecimal> subSetTrueTrue = new HashSet<WellBigDecimal>();
    	Set<WellBigDecimal> subSetTrueFalse = new HashSet<WellBigDecimal>();
    	Set<WellBigDecimal> subSetFalseTrue = new HashSet<WellBigDecimal>();;
    	Set<WellBigDecimal> subSetFalseFalse = new HashSet<WellBigDecimal>();;
    	
    	WellBigDecimal startingWell = null;
    	WellBigDecimal endingWell = null;
    	
    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
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
    	
    	Set<WellBigDecimal> trueTrue = testSet.subSet(startingWell, true, endingWell, true);
    	Set<WellBigDecimal> trueFalse = testSet.subSet(startingWell, true, endingWell, false);
    	Set<WellBigDecimal> falseTrue = testSet.subSet(startingWell, false, endingWell, true);
    	Set<WellBigDecimal> falseFalse = testSet.subSet(startingWell, false, endingWell, false);

    	assertEquals(trueTrue, subSetTrueTrue);
    	assertEquals(trueFalse, subSetTrueFalse);
    	assertEquals(falseTrue, subSetFalseTrue);
    	assertEquals(falseFalse, subSetFalseFalse);
    }
    
    /**
     * Tests subset method using two wells as parameters
     */
    @Test
    public void testSubSetWells() {

    	Set<WellBigDecimal> subSet = new HashSet<WellBigDecimal>();
    	
    	WellBigDecimal startingWell = null;
    	WellBigDecimal endingWell = null;
    	
    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
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
    	
    	Set<WellBigDecimal> toCompare = testSet.subSet(startingWell, endingWell);

    	assertEquals(toCompare, subSet);

    }
    
    /**
     * Tests subset inclusive method using two indices as parameters.
     */
    public void testSubSetInclusive(int index1, boolean inclusive1, int index2, boolean inclusive2) {

    	Set<WellBigDecimal> subSetTrueTrue = new HashSet<WellBigDecimal>();
    	Set<WellBigDecimal> subSetTrueFalse = new HashSet<WellBigDecimal>();
    	Set<WellBigDecimal> subSetFalseTrue = new HashSet<WellBigDecimal>();;
    	Set<WellBigDecimal> subSetFalseFalse = new HashSet<WellBigDecimal>();;

    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSetTrueTrue.add(new WellBigDecimal(inclusive));
    		    subSetTrueFalse.add(new WellBigDecimal(inclusive));
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current + 1 == end) {
    			
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
    	
    	Set<WellBigDecimal> trueTrue = testSet.subSet(begin, true, end, true);
    	Set<WellBigDecimal> trueFalse = testSet.subSet(begin, true, end, false);
    	Set<WellBigDecimal> falseTrue = testSet.subSet(begin, false, end, true);
    	Set<WellBigDecimal> falseFalse = testSet.subSet(begin, false, end, false);

    	assertEquals(trueTrue, subSetTrueTrue);
    	assertEquals(trueFalse, subSetTrueFalse);
    	assertEquals(falseTrue, subSetFalseTrue);
    	assertEquals(falseFalse, subSetFalseFalse);
    }
    
    /**
     * Tests the subset method using two indices as parameters.
     */
    public void testSubSet(int index1, int index2) {

    	Set<WellBigDecimal> subSet = new HashSet<WellBigDecimal>();
    	
    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSet.add(new WellBigDecimal(inclusive));
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigDecimal inclusive = iter.next();
    		
    		if(current + 1 == end) {

    		    break;
    		    
    		} else {
    			
    			subSet.add(new WellBigDecimal(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigDecimal> toCompare = testSet.subSet(begin, end);

    	assertEquals(toCompare, subSet);

    }
    
    /**
     * Tests the tail set method using a well as a parameter.
     */
    @Test
    public void testTailSetWell() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> tailSet = testSet.tailSet(random);
    	Set<WellBigDecimal> subSet = testSet.subSet(random, true, testSet.last(), true);

    	assertEquals(tailSet, subSet);
    	
    }
    
    /**
     * Tests the tail set inclusive method using a well as a parameter.
     */
    @Test
    public void testTailSetInclusiveWell() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigDecimal> tailSetTrue = testSet.tailSet(random, true);
    	Set<WellBigDecimal> subSetTrue = testSet.subSet(random, true, testSet.last(), true);
    	Set<WellBigDecimal> tailSetFalse = testSet.tailSet(random, false);
    	Set<WellBigDecimal> subSetFalse = testSet.subSet(random, false, testSet.last(), true);
    	
    	assertEquals(tailSetTrue, subSetTrue);
    	assertEquals(tailSetFalse, subSetFalse);
    }
    
    /**
     * Tests the tail set method using an index as a parameter.
     */
    @Test
    public void testTailSetIndex() {

        int index = random.nextInt((testSet.size()) + 1);
    	
    	Set<WellBigDecimal> tailSet = testSet.tailSet(index);
    	Set<WellBigDecimal> subSet = testSet.subSet(index, testSet.size() - 1);
    	
    	assertEquals(tailSet, subSet);
    	
    }
    
    /**
     * Tests the tail set inclusive method using an index as a parameter.
     */
    @Test
    public void testTailSetInclusiveIndex() {

        int index = random.nextInt((testSet.size()) + 1);

    	Set<WellBigDecimal> tailSetTrue = testSet.tailSet(index, true);
    	Set<WellBigDecimal> subSetTrue = testSet.subSet(index, true, testSet.size() - 1, true);
    	Set<WellBigDecimal> tailSetFalse = testSet.tailSet(index, false);
    	Set<WellBigDecimal> subSetFalse = testSet.subSet(index, false, testSet.size() - 1, true);
    	
    	assertEquals(tailSetTrue, subSetTrue);
    	assertEquals(tailSetFalse, subSetFalse);
    }
    
    /*------------------------- Tests well lookup ----------------------*/
    
    /**
     * Tests contains well method.
     */
    @Test
    public void testContainsWell() {

        int index = random.nextInt((testSet.size() - 2) + 1);

        Iterator<WellBigDecimal> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigDecimal toFind = iter.next();   	
    	WellBigDecimal outside = new WellBigDecimal(maxRow + 1, maxColumn);
    	
    	assertTrue(testSet.contains(toFind));
    	assertFalse(testSet.contains(outside));
    }   

    /**
     * Tests contains well set method.
     */
    @Test
    public void testContainsSet() {

    	WellSetBigDecimal subset = new WellSetBigDecimal(testSet.subSet(begin, end));
        WellSetBigDecimal outside = new WellSetBigDecimal(this.randomWellCollectionLength(testSet));
    	
    	assertTrue(testSet.contains(subset));
    	assertFalse(testSet.contains(outside));
    }
    
    /**
     * Tests contains collection method.
     */
    @Test
    public void testContainsCollection() {

    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	Set<WellBigDecimal> outside = this.randomWellCollectionLength(testSet);
    	
    	assertTrue(testSet.contains(subset));
    	assertFalse(testSet.contains(outside));
    }
    
    /**
     * Tests contains well array method.
     */
    @Test
    public void testContainsArray() {

    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	Set<WellBigDecimal> outside = this.randomWellCollectionLength(testSet);
    	
    	WellBigDecimal[] subsetArray = subset.toArray(new WellBigDecimal[subset.size()]);
    	WellBigDecimal[] outsideArray = outside.toArray(new WellBigDecimal[subset.size()]);
    	
    	assertTrue(testSet.contains(subsetArray));
    	assertFalse(testSet.contains(outsideArray));
    }
    
    /**
     * Tests contains method using a delimiter separated list of wells.
     */
    @Test
    public void testCountainsWellListStringDelimiter() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigDecimal> iter = randomSet.iterator();

    	while(iter.hasNext()) {

    		WellBigDecimal next = iter.next();

    	    if(iter.hasNext()) {
    	    	list += next.index() + " @ ";
    	    } else {
    	    	list += next.index();
    	    	break;
    	    }
    	}

    	Set<WellBigDecimal> outsideSet = this.randomWellCollectionLength(testSet);
        String outsideList = "";
    	
    	Iterator<WellBigDecimal> outsideIter = outsideSet.iterator();

    	while(outsideIter.hasNext()) {

    		WellBigDecimal next = outsideIter.next();

    	    if(outsideIter.hasNext()) {
    	    	outsideList += next.index() + " @ ";
    	    } else {
    	    	outsideList += next.index();
    	    	break;
    	    }
    	}
    	
    	assertTrue(testSet.contains(list, "@"));
    	assertFalse(testSet.contains(outsideList, "@"));
    }
    
    
    /**
     * Tests the contains method using a string.
     */
    @Test
    public void testContainsString() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	String well = ((WellBigDecimal) RandomUtil.randomObject(randomSet)).index();
    	
    	Set<WellBigDecimal> outsideSet = this.randomWellCollectionLength(testSet);
    	String outsideWell = ((WellBigDecimal) RandomUtil.randomObject(outsideSet)).index();
    	
    	assertTrue(testSet.contains(well));
    	assertFalse(testSet.contains(outsideWell));
    }

    /**
     * Tests look up of a well index.
     */
    @Test
    public void testContainsIndex() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	Set<WellBigDecimal> outsideSet = this.randomWellCollectionLength(testSet);
    	WellBigDecimal outsideWell = (WellBigDecimal) RandomUtil.randomObject(outsideSet);
    	WellIndex outsideIndex = new WellIndex(outsideWell.row(), outsideWell.column());
    	
    	assertTrue(testSet.contains(index));
    	assertFalse(testSet.contains(outsideIndex));
    }

    /**
     * Tests look up of a well list.
     */
    @Test
    public void testContainsWellList() {
    	
    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellSetBigDecimal listSet = new WellSetBigDecimal(randomSet);
    	WellList list = listSet.wellList();
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);
    	WellSetBigDecimal notCommonSet = new WellSetBigDecimal(noCommonWells);
    	WellList listNotCommon = notCommonSet.wellList();

    	assertTrue(testSet.contains(list));
    	assertFalse(testSet.contains(listNotCommon));
    }
    
    /*------------------------- Tests well retrieval -------------------------*/
    
    /**
     * Tests getter for all wells in the set. 
     */
    @Test
    public void testGetWells() {
    	Set<WellBigDecimal> set = this.randomWellCollectionLength(new WellSetBigDecimal());
    	WellSetBigDecimal wellSet = new WellSetBigDecimal(set);
    	assertEquals(set, wellSet.allWells());
    }

    /**
     * Tests get wells well method.
     */
    @Test
    public void testGetWellsWell() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	
    	assertEquals(random, testSet.getWells(random));
    }
    
    /**
     * Tests get wells well set method.
     */
    @Test
    public void testGetWellsSet() {

    	WellSetBigDecimal subset = new WellSetBigDecimal(testSet.subSet(begin, end));
        WellSetBigDecimal outside = new WellSetBigDecimal(this.randomWellCollectionLength(testSet));
    	
    	assertEquals(subset, testSet.getWells(subset));
    	assertEquals(null, testSet.getWells(outside));
    }
    
    /**
     * Tests get wells collection method.
     */
    @Test
    public void testGetWellsCollection() {

    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	Set<WellBigDecimal> outside = this.randomWellCollectionLength(testSet);
    	
    	assertEquals(new WellSetBigDecimal(subset), testSet.getWells(subset));
    	assertEquals(null, testSet.getWells(outside));
    }
    
    /**
     * Tests contains well array method.
     */
    @Test
    public void testGetWellArray() {

    	Set<WellBigDecimal> subset = testSet.subSet(begin, end);
    	Set<WellBigDecimal> outside = this.randomWellCollectionLength(testSet);
    	
    	WellBigDecimal[] subsetArray = subset.toArray(new WellBigDecimal[subset.size()]);
    	WellBigDecimal[] outsideArray = outside.toArray(new WellBigDecimal[subset.size()]);
    	
    	assertEquals(new WellSetBigDecimal(subset), testSet.getWells(subsetArray));
    	assertNull(testSet.getWells(outsideArray));
    }
    
    /**
     * Tests the retrieval of a list of delimiter separated wells.
     */
    @Test
    public void testGetWellsDelimiter() {

        WellBigDecimal[] array = testSet.toWellArray();
    	WellSetBigDecimal list = new WellSetBigDecimal();

    	WellBigDecimal startingWell = array[begin];
    	WellBigDecimal endingWell = array[end];
    	
    	boolean addMode = false;
    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	while(iter.hasNext()) {
    		
    		WellBigDecimal well = iter.next();
    		
    		if(well.equals(startingWell)) {
    			addMode = true;
    		}
    		
    		if(well.equals(endingWell)) {
    			break;
    		}
    		
    		if(addMode) {
    			list.add(new WellBigDecimal(well));
    		}
    		
    	}

        String listString = "";
    	
    	Iterator<WellBigDecimal> listIter = list.iterator();

    	while(listIter.hasNext()) {
    	    WellBigDecimal next = listIter.next();
    	    if(listIter.hasNext()) {
    	    	listString += next.index() + ", ";
    	    } else {
    	    	listString += next.index();
    	    	break;
    	    }
    	}

    	assertEquals(list, testSet.getWells(listString, ","));
    }

    /**
     * Tests the get wells method using a string.
     */
    @Test
    public void testGetWellsString() {

    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellBigDecimal well = ((WellBigDecimal) RandomUtil.randomObject(randomSet));
    	String wellString = well.index();
    	
    	Set<WellBigDecimal> outsideSet = this.randomWellCollectionLength(testSet);
    	WellBigDecimal outsideWell = ((WellBigDecimal) RandomUtil.randomObject(outsideSet));
    	String outsideString = outsideWell.index();
    	
    	assertEquals(well, testSet.getWells(wellString));
    	assertEquals(null, testSet.getWells(outsideString));
    }

    /**
     * Tests the get wells method using an index.
     */
    @Test
    public void testGetWellsIndex() {

    	WellBigDecimal random = (WellBigDecimal) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	Set<WellBigDecimal> outsideSet = this.randomWellCollectionLength(testSet);
    	WellBigDecimal outsideWell = (WellBigDecimal) RandomUtil.randomObject(outsideSet);
    	WellIndex outsideIndex = new WellIndex(outsideWell.row(), outsideWell.column());
    	
    	assertEquals(random, testSet.getWells(index));
    	assertEquals(null, testSet.getWells(outsideIndex));
    }

    /**
     * Tests the get wells method using a well list.
     */
    @Test
    public void testGetWellsWellList() {
    	
    	Set<WellBigDecimal> randomSet = testSet.subSet(begin, end);
    	WellSetBigDecimal listSet = new WellSetBigDecimal(randomSet);
    	WellList list = listSet.wellList();
    	
    	Set<WellBigDecimal> noCommonWells = this.randomWellCollection(testSet);
    	WellSetBigDecimal notCommonSet = new WellSetBigDecimal(noCommonWells);
    	WellList listNotCommon = notCommonSet.wellList();

    	assertEquals(listSet, testSet.getWells(list));
    	assertEquals(null, testSet.getWells(listNotCommon));
    }
    
    /**
     * Tests the retrieval of all wells in the given row.
     */
    @Test
    public void testGetRow() {

    	WellSetBigDecimal fixedRow = new WellSetBigDecimal();
    	
    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	
    	for(int i = 0; i < wellNum; i++) {
    		WellBigDecimal well = RandomUtil.randomWellBigDecimal(minValue, maxValue, 
    	    		maxRow + 1, maxRow + 1, minColumn, maxColumn, minLength, maxLength);
    		fixedRow.add(well);
    		testSet.add(well);
    	}

    	assertEquals(fixedRow, testSet.getRow(maxRow + 1));
    }
    
    /**
     * Tests the retrieval of all wells in a given column.
     */
    @Test
    public void testGetColumn() {

    	WellSetBigDecimal fixedColumn = new WellSetBigDecimal();

    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	
    	for(int i = 0; i < wellNum; i++) {
    		WellBigDecimal well = RandomUtil.randomWellBigDecimal(minValue, maxValue, 
    	    		maxRow + 1, maxRow + 100, maxColumn + 1, maxColumn + 1, 5, 10);
    		fixedColumn.add(well);
    		testSet.add(well);
    	}
    	
    	assertEquals(fixedColumn, testSet.getColumn(maxColumn + 1));
    }
    
    /*----------------------- Tests remaining methods ------------------------*/
    
    /**
     * Tests set clearance.
     */
    @Test
    public void testClear() {

    	assertFalse(testSet.isEmpty());
    	assertFalse(testSet.size() == 0);
    	
    	testSet.clear();
    	
    	assertTrue(testSet.isEmpty());
    	assertTrue(testSet.size() == 0);
    }
    
    /**
     * Tests the to well array method.
     */
    @Test
	public void testToWellArray() {

		WellBigDecimal[] array = testSet.toWellArray();
		
		assertTrue(testSet.contains(array));
		assertEquals(testSet.size(), array.length);
		assertTrue(array instanceof WellBigDecimal[]);
    }
    
    /**
     * Tests the label setter.
     */
    @Test
    public void testSetLabel() {

    	String label = "TestLabel";
      	
    	assertFalse(testSet.label().equals(label));
    	testSet.setLabel(label);
    	assertTrue(testSet.label().equals(label));
    }
    
    /**
     * Tests the size getter.
     */
    @Test
    public void testSize() {
    	
    	WellSetBigDecimal set = new WellSetBigDecimal();

    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	wellNum = wellNum % 2 == 0 ? wellNum : wellNum + 1;
    	
    	for(int i = 0; i < wellNum; i++) {
    		set.add(new WellBigDecimal(i, i + 1));
    	}
    	
    	assertEquals(wellNum, set.size());
    	
    	for(int i = 0; i < wellNum / 2; i++) {
    		set.remove(new WellBigDecimal(i, i + 1));
    	}
    	
    	assertEquals(wellNum / 2, set.size());
    }
    
    /**
     * Tests the well list getter.
     */
    @Test
    public void testWellList() {

    	WellList list = new WellList();
    	
    	for(WellBigDecimal well : testSet) {
    		list.add(new WellIndex(well.row(), well.column()));
    	}
    	
    	assertEquals(list, testSet.wellList());
    	
    }
    
    /*---------------------------- String Methods ----------------------------*/
    
    /**
     * Tests the to string array method.
     */
    @Test
    public void testToStringArray() {
        String[] compareTo = new String[testSet.size()];
        
        int index = 0;
        
        for(WellBigDecimal well : testSet) {
            compareTo[index++] = well.index() + " " + well.data().toString();        	
        }
    	
    	String[] indices = testSet.toStringArray();
    	
    	for(int i = 0; i < indices.length; i++) {
    		assertEquals(indices[i], compareTo[i]);
    	}
    }
    
    /**
     * Tests the label getter.
     */
    @Test
    public void testLabel() {
    	
    	String label1 = "Label1";
    	String label2 = "Label2";
    	
    	WellSetBigDecimal set = new WellSetBigDecimal();
    	
    	set.setLabel(label1);
    	
    	assertEquals(label1, set.label());
    	
    	set.setLabel(label2);
    	
    	assertEquals(label2, set.label());
    	
    }
    
    /**
     * Tests the to string method.
     */
    @Test
    public void testToString() {

    	String compareTo = testSet.label() + "\n";
    	
    	Iterator<WellBigDecimal> iter = testSet.iterator();
    	
    	while(iter.hasNext()) {
    		
    		WellBigDecimal well = iter.next();
    		
    		if(iter.hasNext()) {
    			compareTo += well.toString() + "\n";
    		} else {
    			compareTo += well.toString();
    			break;
    		}
    		
    	}

    	assertEquals(compareTo, testSet.toString());
    	
    }

    /*------------------------ Testing helper methods ------------------------*/

    /**
     * Returns a random list of big decimal wells.
     * @return    set of wells
     */
    public Set<WellBigDecimal> randomWellCollection(WellSetBigDecimal set) {
    
    	Set<WellBigDecimal> randomCollection = new HashSet<WellBigDecimal>();

    	while(randomCollection.size() != wellNumber) {
    		
    		WellBigDecimal well = RandomUtil.randomWellBigDecimal(minValue, maxValue, 
    	    		minRow, maxRow, minColumn, maxColumn, minLength, maxLength);

    		if(!randomCollection.contains(well) && !set.contains(well)) {
    	        randomCollection.add(well);
    		}
    	}

    	return randomCollection;
    }
    
    /**
     * Returns a random list of big decimal wells.
     * @return    set of wells
     */
    public Set<WellBigDecimal> randomWellCollectionLength(WellSetBigDecimal set) {
    
    	Set<WellBigDecimal> randomCollection = new HashSet<WellBigDecimal>();

    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	
    	while(randomCollection.size() != wellNum) {
    		
    		WellBigDecimal well = RandomUtil.randomWellBigDecimal(minValue, maxValue, 
    	    		minRow, maxRow, minColumn, maxColumn, minLength, maxLength);

    		if(!randomCollection.contains(well) && !set.contains(well)) {
    			randomCollection.add(well);
    		}
    	}

    	return randomCollection;
    }
    
}
