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

import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;
import com.github.jessemull.microflex.plate.WellIndex;
import com.github.jessemull.microflex.plate.WellList;
import com.github.jessemull.microflex.util.RandomUtil;

/**
 * This class tests the methods and constructors in the well set big integer
 * class.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WellSetBigIntegerTest {

    /* Rule for testing exceptions */
	
	@Rule
    public ExpectedException thrown = ExpectedException.none();
	
	/* Minimum and maximum values for random well and lists */
	
	private BigInteger minValue = new BigInteger(-1000000000 + "");   // Minimum big integer value for random sets
	private BigInteger maxValue = new BigInteger(1000000000 + "");    // Maximum big integer value for random sets
	private int minLength = 100;                                      // Minimum well set length
	private int maxLength = 1000;                                     // Maximum well set length
	private int minRow = 0;                                           // Minimum well row
	private int maxRow = 1000;                                        // Maximum well row
	private int minColumn = 1;                                        // Minimum well column
	private int maxColumn = 1000;                                     // Maximum well column
	private int wellNumber = 200;                                     // Number of wells used in select tests
	private Random random = new Random();                             // Generates random integers
	
	/* Objects used to setup each test */
	
	private WellSetBigInteger testSet;         // A random set of wells
	private WellSetBigInteger clone1;          // Clone of the test set
	private WellSetBigInteger clone2;          // Clone of the test set
	private int begin;                         // Beginning index for subsets
	private int end;                           // Ending index for subsets
	private int size;                          // Size of the test set
	private Set<WellBigInteger> collection;    // Random collection of wells
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
		
		testSet = RandomUtil.randomWellSetBigInteger(minValue, 
    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength);
		clone1 = new WellSetBigInteger(testSet);
		clone2 = new WellSetBigInteger(testSet);
		
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
        WellSetBigInteger set = new WellSetBigInteger();
        assertNotNull(set);
        assertTrue(set.label().equals("WellSetBigInteger"));
        assertTrue(set.wellList().size() == 0);
    }
    
    /**
     * Tests a well set constructor using a well collection as an argument.
     */
    @Test
    public void testConstructorCollection() {
    	
    	WellSetBigInteger set = new WellSetBigInteger(collection);    

    	assertNotNull(set);
        assertEquals(set.label(), "WellSetBigInteger");
        assertEquals(set.size(), collection.size());
    }
    
    /**
     * Tests a well set constructor using a well collection and a label as arguments.
     */
    @Test
    public void testconstructorCollectionLabel() {

    	String label = "TestLabel";
    	
    	WellSetBigInteger set = new WellSetBigInteger(collection, label);    
        
    	assertNotNull(set);
        assertEquals(set.label(), label);
        assertEquals(set.size(), collection.size());
    }
    
    /**
     * Tests a well set constructor using a well set as an argument.
     */
    @Test
    public void testConstructorWellSet() {
    	
    	WellSetBigInteger testSet2 = new WellSetBigInteger(testSet);
    	
    	assertEquals(testSet, testSet2);
    }
    
    /**
     * Tests a well set constructor using a well set and label as arguments.
     */
    @Test
    public void testConstructorWellSetLabel() {
    	
    	String label = "TestLabel";

    	testSet.setLabel(label);
    	
    	WellSetBigInteger testSet2 = new WellSetBigInteger(testSet, label);
    	
    	assertEquals(testSet, testSet2);
    }
    
    /**
     * Tests a well set constructor using an array of wells as an argument.
     */
    @Test
    public void testConstructorWellArray() {

    	WellBigInteger[] array = collection.toArray(new WellBigInteger[collection.size()]);
    	
    	WellSetBigInteger set = new WellSetBigInteger(array);    

    	assertNotNull(set);
        assertEquals(set.label(), "WellSetBigInteger");
        assertEquals(set.size(), array.length);
    }
    
    /**
     * Tests a well set constructor using an array of wells and a label as arguments.
     */
    @Test
    public void testConstructorWellArrayLabel() {

    	WellBigInteger[] array = collection.toArray(new WellBigInteger[collection.size()]);
    	String label = "TestLabel";
    	WellSetBigInteger set = new WellSetBigInteger(array, label);    

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

    	WellBigInteger well = RandomUtil.randomWellBigInteger(minValue, 
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

    	WellBigInteger[] array = collection.toArray(new WellBigInteger[collection.size()]);
    	
    	testSet.add(array);
    	
    	assertEquals(testSet.size(), size + array.length);
    	assertTrue(testSet.contains(array));
    }
    
    /**
     * Tests the addition of a well set.
     */
    @Test
    public void testAddWellSet() {

    	WellSetBigInteger testSet2 = new WellSetBigInteger(collection);
    	
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

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());

    	testSet.remove(random);
    	
    	assertEquals(testSet.size(), size - 1);
    	assertFalse(testSet.contains(random));
    	
    	WellBigInteger notCommon = RandomUtil.randomWellBigInteger(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);
    	
    	size = testSet.size();
    	WellSetBigInteger testSet2 = new WellSetBigInteger(testSet);
    	testSet.remove(notCommon);
    	
    	assertEquals(testSet.size(), size);
    	assertEquals(testSet, testSet2);
    }
    

    /**
     * Tests removal of a well set.
     */
    @Test
    public void testRemoveSet() {

    	WellSetBigInteger randomSet = new WellSetBigInteger(testSet.subSet(begin, end));

    	clone1.remove(randomSet);

    	assertEquals(clone1.size(), testSet.size() - randomSet.size());
    	assertFalse(clone1.contains(randomSet));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
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

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);

    	clone1.remove(randomSet);

    	assertEquals(clone1.size(), testSet.size() - randomSet.size());
    	assertFalse(clone1.contains(randomSet));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
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
    	
    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellBigInteger[] randomArray = randomSet.toArray(new WellBigInteger[randomSet.size()]);
    	
    	clone1.remove(randomArray);

    	assertEquals(clone1.size(), testSet.size() - randomArray.length);
    	assertFalse(clone1.contains(randomArray));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
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

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigInteger> iter = randomSet.iterator();

    	while(iter.hasNext()) {

    		WellBigInteger next = iter.next();

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

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigInteger> iter = randomSet.iterator();

    	while(iter.hasNext()) {
    	    WellBigInteger next = iter.next();
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

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	testSet.remove(index);
    	
    	assertEquals(testSet.size(), size - 1);
    	assertFalse(testSet.contains(index));
    	
    	WellBigInteger notCommon = RandomUtil.randomWellBigInteger(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);
    	WellIndex indexNotCommon = new WellIndex(notCommon.row(), notCommon.column());
    	
    	size = testSet.size();
    	
    	WellSetBigInteger testSet2 = new WellSetBigInteger(testSet);
    	
    	testSet.remove(indexNotCommon);
    	
    	assertEquals(testSet.size(), size);
    	assertEquals(testSet, testSet2);
    }

    /**
     * Tests removal of a well list.
     */
    @Test
    public void testRemoveWellList() {
    	
    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellSetBigInteger listSet = new WellSetBigInteger(randomSet);
    	WellList list = listSet.wellList();
    	
    	clone1.remove(list);

    	assertEquals(clone1.size(), testSet.size() - list.size());
    	assertFalse(clone1.contains(list));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);
    	WellSetBigInteger notCommonSet = new WellSetBigInteger(noCommonWells);
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

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	WellBigInteger replace = new WellBigInteger(random.row(), random.column(), RandomUtil.randomBigIntegerList(minValue, maxValue, minLength, maxLength));

    	WellBigInteger toReplace = new WellBigInteger(testSet.getWells(random.index()));
    	testSet.replace(replace);

    	assertEquals(testSet.size(), size);
    	assertTrue(testSet.contains(random));
    	assertFalse(testSet.getWells(random.index()).data().equals(random.data()));
    	assertTrue(testSet.getWells(random.index()).equals(toReplace));
    	
        size = testSet.size();
    	
    	WellBigInteger well = RandomUtil.randomWellBigInteger(minValue, 
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
    	
    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	WellSetBigInteger randomSet = new WellSetBigInteger();
    	
    	for(WellBigInteger well : subset) {
    		randomSet.add(new WellBigInteger(well.row(), well.column(), 
    				RandomUtil.randomBigIntegerList(minValue, maxValue, minLength, maxLength)));
    	}
    	
    	clone1.replace(randomSet);

    	assertTrue(clone1.contains(randomSet));
    	assertEquals(clone1.size(), size);
    	
    	for(WellBigInteger well : randomSet) {
    		assertEquals(clone1.getWells(well.index()).data(), well.data());
            assertFalse(testSet.getWells(well).data().equals(clone1.getWells(well).data()));
    	}
    	
    	WellSetBigInteger noCommonWells = new WellSetBigInteger(this.randomWellCollection(testSet));  	
    	clone2.replace(noCommonWells);
    	
    	assertTrue(clone2.contains(noCommonWells));
    	
    	for(WellBigInteger well : noCommonWells) {
    		assertEquals(clone2.getWells(well).data(), well.data());
    		assertFalse(testSet.contains(well));
    	}
    }
    
    /**
     * Tests collection replacement.
     */
    @Test
    public void testReplaceCollection() {
    	
    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	Set<WellBigInteger> randomSet = new HashSet<WellBigInteger>();
    	
    	for(WellBigInteger well : subset) {
    		randomSet.add(new WellBigInteger(well.row(), well.column(), 
    				RandomUtil.randomBigIntegerList(minValue, maxValue, minLength, maxLength)));
    	}
    	
    	clone1.replace(randomSet);

    	assertTrue(clone1.contains(randomSet));
    	assertEquals(clone1.size(), size);
    	
    	for(WellBigInteger well : randomSet) {
    		assertEquals(clone1.getWells(well.index()).data(), well.data());
            assertFalse(testSet.getWells(well).data().equals(clone1.getWells(well).data()));
    	}
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.replace(noCommonWells);
    	
    	assertTrue(clone2.contains(noCommonWells));
    	
    	for(WellBigInteger well : noCommonWells) {
    		assertEquals(clone2.getWells(well).data(), well.data());
    		assertFalse(testSet.contains(well));
    	}
    	
    }
    
    /**
     * Tests well array replacement.
     */
    @Test
    public void testReplaceArray() {

    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	WellBigInteger[] arraySubset = new WellBigInteger[subset.size()];
    	
    	int sizeClone2 = clone2.size();
    	
    	int index = 0;
    	
    	for(WellBigInteger well : subset) {
    		arraySubset[index++] = new WellBigInteger(well.row(), well.column(), 
    				RandomUtil.randomBigIntegerList(minValue, maxValue, minLength, maxLength));
    	}
    	
    	clone1.replace(arraySubset);

    	assertTrue(clone1.contains(arraySubset));
    	assertEquals(clone1.size(), sizeClone2);
    	
    	for(WellBigInteger well : arraySubset) {
    		assertEquals(clone1.getWells(well.index()).data(), well.data());
            assertFalse(testSet.getWells(well).data().equals(clone1.getWells(well).data()));
    	}
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);
    	WellBigInteger[] arrayNoneShared = noCommonWells.toArray(new WellBigInteger[noCommonWells.size()]);
    	clone2.replace(arrayNoneShared);
    	
    	assertTrue(clone2.contains(arrayNoneShared));
    	assertEquals(clone2.size(), sizeClone2 + arrayNoneShared.length);
    	
    	for(WellBigInteger well : arrayNoneShared) {
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

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());

    	testSet.retain(random);
    	assertEquals(testSet.size(), 1);
    	assertTrue(testSet.contains(random));
    	
    	WellBigInteger notCommon = RandomUtil.randomWellBigInteger(minValue, 
    			maxValue, minRow, maxRow, maxColumn, maxColumn + 1, minLength, maxLength);

    	testSet.retain(notCommon);
    	
    	assertEquals(testSet.size(), 0);
    }
    
    /**
     * Tests retention of a well set.
     */
    @Test
    public void testRetainSet() {

    	WellSetBigInteger randomSet = new WellSetBigInteger(testSet.subSet(begin, end));

    	clone1.retain(randomSet);

    	assertEquals(clone1.size(), randomSet.size());
    	assertTrue(clone1.contains(randomSet));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.retain(noCommonWells);

    	assertEquals(clone2.size(), 0);    	
    }
    
    /**
     * Tests retention of a collection.
     */
    @Test
    public void testRetainCollection() {

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);

    	clone1.retain(randomSet);

    	assertEquals(clone1.size(), randomSet.size());
    	assertTrue(clone1.contains(randomSet));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.retain(noCommonWells);
    	
    	assertEquals(clone2.size(), 0);
    }
    
    /**
     * Tests retention of an array of wells.
     */
    @Test
    public void testRetainArray() {

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellBigInteger[] randomArray = randomSet.toArray(new WellBigInteger[randomSet.size()]);
    	
    	clone1.retain(randomArray);

    	assertEquals(clone1.size(), randomArray.length);
    	assertTrue(clone1.contains(randomArray));
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);  	
    	clone2.retain(noCommonWells);

    	assertEquals(clone2.size(), 0);
    }
    
    /**
     * Tests retention of a delimiter separated list of wells.
     */
    @Test
    public void testRetainWellListStringDelimiter() {

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigInteger> iter = randomSet.iterator();

    	while(iter.hasNext()) {

    		WellBigInteger next = iter.next();

    	    if(iter.hasNext()) {
    	    	list += next.index() + ",";
    	    } else {
    	    	list += next.index();
    	    	break;
    	    }
    	}

    	Set<WellBigInteger> notCommon = this.randomWellCollection(testSet);  
    	String listNotCommon = "";
    	
    	Iterator<WellBigInteger> iterNotCommon = notCommon.iterator();

    	while(iterNotCommon.hasNext()) {

    		WellBigInteger next = iterNotCommon.next();

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

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	String randomString = random.index();
    	
    	testSet.retain(randomString);
    	
    	assertEquals(testSet.size(), 1);
    	assertTrue(testSet.contains(randomString));
    	
    	WellBigInteger notCommon = RandomUtil.randomWellBigInteger(minValue, 
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

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	testSet.retain(index);
    	
    	assertEquals(testSet.size(), 1);
    	assertTrue(testSet.contains(index));
    	
    	WellBigInteger notCommon = RandomUtil.randomWellBigInteger(minValue, 
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

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellSetBigInteger randomWellSet = new WellSetBigInteger(randomSet);
    	WellList list = randomWellSet.wellList();
    	
    	clone1.retain(list);

    	assertEquals(clone1.size(), list.size());
    	assertTrue(clone1.contains(list));
    	
    	Set<WellBigInteger> notCommonWells = this.randomWellCollection(testSet);  
    	WellSetBigInteger notCommonSet = new WellSetBigInteger(notCommonWells);
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
        
        Iterator<WellBigInteger> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigInteger toCeiling = iter.next();
        WellBigInteger toReturn = iter.next();
        WellBigInteger input = new WellBigInteger(toReturn.row(), toReturn.column() - 1);
        WellBigInteger outside = new WellBigInteger(maxRow + 1, maxColumn);
        
        toReturn = toCeiling.compareTo(input) < 1 ? toCeiling : toReturn;
        
        assertEquals(testSet.ceiling(toCeiling), toReturn);
        assertNull(testSet.ceiling(outside));
    }
    
    /**
     * Tests the descending iterator.
     */
    @Test
    public void testDescendingIterator() {

    	WellBigInteger[] array = testSet.toWellArray();
    	Iterator<WellBigInteger> iter = testSet.descendingIterator();
    	
    	for(int i = array.length - 1; i >= 0; i--) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the descending set method.
     */
    @Test
    public void testDescendingSet() {

    	Set<WellBigInteger> reversed = testSet.descendingSet();

    	Iterator<WellBigInteger> iter = testSet.descendingIterator();
    	Iterator<WellBigInteger> iterReversed = reversed.iterator();
    	
    	while(iter.hasNext() && iterReversed.hasNext()) {
    		assertEquals(iter.next(), iterReversed.next());
    	}
    }
    
    /**
     * Tests the first method.
     */
    @Test
    public void testFirst() {

    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	assertEquals(iter.next(), testSet.first());
    }
    
    /**
     * Tests the last method.
     */
    @Test
    public void testLast() {

    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	WellBigInteger last = null;
    	
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
        
        Iterator<WellBigInteger> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigInteger toReturn = iter.next();
        WellBigInteger toFloor = iter.next();
        WellBigInteger input = new WellBigInteger(toFloor.row(), toFloor.column() - 1);
        WellBigInteger outside = new WellBigInteger(minColumn - 1, minColumn);
        
        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;

        assertEquals(testSet.floor(toReturn), toReturn);
        assertNull(testSet.floor(outside));
    }
    
    /**
     * Tests the head set method using a well as a parameter.
     */
    @Test
    public void testHeadSetWell() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigInteger> headSet = testSet.headSet(random);
    	Set<WellBigInteger> subSet = testSet.subSet(testSet.first(), random);
    	
    	assertEquals(headSet, subSet);
    	
    }
    
    /**
     * Tests the head set inclusive method using a well as a parameter.
     */
    @Test
    public void testHeadSetInclusiveWell() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigInteger> headSetTrue = testSet.headSet(random, true);
    	Set<WellBigInteger> subSetTrue = testSet.subSet(testSet.first(), true, random, true);
    	Set<WellBigInteger> headSetFalse = testSet.headSet(random, false);
    	Set<WellBigInteger> subSetFalse = testSet.subSet(testSet.first(), true, random, false);
    	
    	assertEquals(headSetTrue, subSetTrue);
    	assertEquals(headSetFalse, subSetFalse);
    }
    
    /**
     * Tests the head set method using an index as a parameter.
     */
    @Test
    public void testHeadSetIndex() {

        int index = random.nextInt((testSet.size()) + 1);
    	
    	Set<WellBigInteger> headSet = testSet.headSet(index);
    	Set<WellBigInteger> subSet = testSet.subSet(0, index);
    	
    	assertEquals(headSet, subSet);
    	
    }
    
    /**
     * Tests the head set inclusive method using an index as a parameter.
     */
    @Test
    public void testHeadSetInclusiveIndex() {

        int index = random.nextInt((testSet.size()) + 1);

    	Set<WellBigInteger> headSetTrue = testSet.headSet(index, true);
    	Set<WellBigInteger> subSetTrue = testSet.subSet(0, true, index, true);
    	Set<WellBigInteger> headSetFalse = testSet.headSet(index, false);
    	Set<WellBigInteger> subSetFalse = testSet.subSet(0, true, index, false);
    	
    	assertEquals(headSetTrue, subSetTrue);
    	assertEquals(headSetFalse, subSetFalse);
    }
    
    /**
     * Tests the higher method.
     */
    @Test
    public void testHigher() {

        int index = random.nextInt((testSet.size() - 2) + 1);
        
        Iterator<WellBigInteger> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigInteger toHigher = iter.next();
        WellBigInteger toReturn = iter.next();
        WellBigInteger input = new WellBigInteger(toReturn.row(), toReturn.column() - 1);
        WellBigInteger outside = new WellBigInteger(maxRow + 1, maxColumn);
        
        toReturn = toHigher.compareTo(input) < 1 ? toHigher : toReturn;
        
        assertEquals(testSet.ceiling(toHigher), toReturn);
        assertNull(testSet.ceiling(outside));
    }
    
    /**
     * Tests the is empty method.
     */
    @Test
    public void testIsEmpty() {

    	WellSetBigInteger empty = new WellSetBigInteger();
    	assertTrue(empty.isEmpty());
    	
    	empty.add(RandomUtil.randomWellBigInteger(minValue, 
    			maxValue, minRow, maxRow, minColumn, maxColumn, minLength, maxLength));
    	assertFalse(empty.isEmpty());
    }
    
    /**
     * Tests the iterator.
     */
    @Test
    public void iterator() {

    	WellBigInteger[] array = testSet.toWellArray();
    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	for(int i = 0; i < array.length; i++) {
    		assertEquals(array[i], iter.next());
    	}
    }
    
    /**
     * Tests the lower method.
     */
    @Test
    public void lower() {

        int index = random.nextInt((testSet.size() - 2) + 1);
       
        Iterator<WellBigInteger> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigInteger toReturn = iter.next();
        WellBigInteger toFloor = iter.next();
        WellBigInteger input = new WellBigInteger(toFloor.row(), toFloor.column() - 1);
        WellBigInteger outside = new WellBigInteger(minColumn - 1, minColumn);
        
        toReturn = toFloor.compareTo(input) > 1 ? input : toReturn;

        assertEquals(testSet.floor(toReturn), toReturn);
        assertNull(testSet.floor(outside));
    }
    
    /**
     * Tests the poll first method.
     */
    @Test
    public void testPollFirst() {

    	WellBigInteger first = testSet.first();
    	WellBigInteger polled = testSet.pollFirst();
    	
    	assertFalse(testSet.contains(polled));
    	assertEquals(first, polled);
    }
    
    /**
     * Tests the poll last method.
     */
    @Test
    public void testPollLast() {

    	WellBigInteger last = testSet.last();
    	WellBigInteger polled = testSet.pollLast();
    	
    	assertFalse(testSet.contains(polled));
    	assertEquals(last, polled);
    }
    
    
    /**
     * Tests subset method inclusive using two wells as parameters.
     */
    @Test
    public void testSubSetInclusiveWell() {
    	
    	Set<WellBigInteger> subSetTrueTrue = new HashSet<WellBigInteger>();
    	Set<WellBigInteger> subSetTrueFalse = new HashSet<WellBigInteger>();
    	Set<WellBigInteger> subSetFalseTrue = new HashSet<WellBigInteger>();;
    	Set<WellBigInteger> subSetFalseFalse = new HashSet<WellBigInteger>();;
    	
    	WellBigInteger startingWell = null;
    	WellBigInteger endingWell = null;
    	
    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSetTrueTrue.add(new WellBigInteger(inclusive));
    		    subSetTrueFalse.add(new WellBigInteger(inclusive));
    		    startingWell = new WellBigInteger(inclusive);
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current + 1 == end) {

    			endingWell = new WellBigInteger(inclusive);
    		    subSetTrueTrue.add(new WellBigInteger(inclusive));
    		    subSetFalseTrue.add(new WellBigInteger(inclusive));
    		    break;
    		    
    		} else {
    			
    			subSetTrueTrue.add(new WellBigInteger(inclusive));
    			subSetTrueFalse.add(new WellBigInteger(inclusive));
    			subSetFalseTrue.add(new WellBigInteger(inclusive));
    			subSetFalseFalse.add(new WellBigInteger(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigInteger> trueTrue = testSet.subSet(startingWell, true, endingWell, true);
    	Set<WellBigInteger> trueFalse = testSet.subSet(startingWell, true, endingWell, false);
    	Set<WellBigInteger> falseTrue = testSet.subSet(startingWell, false, endingWell, true);
    	Set<WellBigInteger> falseFalse = testSet.subSet(startingWell, false, endingWell, false);

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

    	Set<WellBigInteger> subSet = new HashSet<WellBigInteger>();
    	
    	WellBigInteger startingWell = null;
    	WellBigInteger endingWell = null;
    	
    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSet.add(new WellBigInteger(inclusive));
    		    startingWell = new WellBigInteger(inclusive);
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current + 1 == end) {

    			endingWell = new WellBigInteger(inclusive);
    		    break;
    		    
    		} else {
    			
    			subSet.add(new WellBigInteger(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigInteger> toCompare = testSet.subSet(startingWell, endingWell);

    	assertEquals(toCompare, subSet);

    }
    
    /**
     * Tests subset inclusive method using two indices as parameters.
     */
    public void testSubSetInclusive(int index1, boolean inclusive1, int index2, boolean inclusive2) {

    	Set<WellBigInteger> subSetTrueTrue = new HashSet<WellBigInteger>();
    	Set<WellBigInteger> subSetTrueFalse = new HashSet<WellBigInteger>();
    	Set<WellBigInteger> subSetFalseTrue = new HashSet<WellBigInteger>();;
    	Set<WellBigInteger> subSetFalseFalse = new HashSet<WellBigInteger>();;

    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSetTrueTrue.add(new WellBigInteger(inclusive));
    		    subSetTrueFalse.add(new WellBigInteger(inclusive));
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current + 1 == end) {
    			
    		    subSetTrueTrue.add(new WellBigInteger(inclusive));
    		    subSetFalseTrue.add(new WellBigInteger(inclusive));
    		    break;
    		    
    		} else {
    			
    			subSetTrueTrue.add(new WellBigInteger(inclusive));
    			subSetTrueFalse.add(new WellBigInteger(inclusive));
    			subSetFalseTrue.add(new WellBigInteger(inclusive));
    			subSetFalseFalse.add(new WellBigInteger(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigInteger> trueTrue = testSet.subSet(begin, true, end, true);
    	Set<WellBigInteger> trueFalse = testSet.subSet(begin, true, end, false);
    	Set<WellBigInteger> falseTrue = testSet.subSet(begin, false, end, true);
    	Set<WellBigInteger> falseFalse = testSet.subSet(begin, false, end, false);

    	assertEquals(trueTrue, subSetTrueTrue);
    	assertEquals(trueFalse, subSetTrueFalse);
    	assertEquals(falseTrue, subSetFalseTrue);
    	assertEquals(falseFalse, subSetFalseFalse);
    }
    
    /**
     * Tests the subset method using two indices as parameters.
     */
    public void testSubSet(int index1, int index2) {

    	Set<WellBigInteger> subSet = new HashSet<WellBigInteger>();
    	
    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	while(current <= begin) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current == begin) {
    		    
    			subSet.add(new WellBigInteger(inclusive));
    		    break;
    		    
    		}   		
    		
    		current++;
    	}
    	
    	while(current < end) {
    		
    		WellBigInteger inclusive = iter.next();
    		
    		if(current + 1 == end) {

    		    break;
    		    
    		} else {
    			
    			subSet.add(new WellBigInteger(inclusive));
    			
    		}
    		
    		current++;
    	}
    	
    	Set<WellBigInteger> toCompare = testSet.subSet(begin, end);

    	assertEquals(toCompare, subSet);

    }
    
    /**
     * Tests the tail set method using a well as a parameter.
     */
    @Test
    public void testTailSetWell() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigInteger> tailSet = testSet.tailSet(random);
    	Set<WellBigInteger> subSet = testSet.subSet(random, true, testSet.last(), true);

    	assertEquals(tailSet, subSet);
    	
    }
    
    /**
     * Tests the tail set inclusive method using a well as a parameter.
     */
    @Test
    public void testTailSetInclusiveWell() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	Set<WellBigInteger> tailSetTrue = testSet.tailSet(random, true);
    	Set<WellBigInteger> subSetTrue = testSet.subSet(random, true, testSet.last(), true);
    	Set<WellBigInteger> tailSetFalse = testSet.tailSet(random, false);
    	Set<WellBigInteger> subSetFalse = testSet.subSet(random, false, testSet.last(), true);
    	
    	assertEquals(tailSetTrue, subSetTrue);
    	assertEquals(tailSetFalse, subSetFalse);
    }
    
    /**
     * Tests the tail set method using an index as a parameter.
     */
    @Test
    public void testTailSetIndex() {

        int index = random.nextInt((testSet.size()) + 1);
    	
    	Set<WellBigInteger> tailSet = testSet.tailSet(index);
    	Set<WellBigInteger> subSet = testSet.subSet(index, testSet.size() - 1);
    	
    	assertEquals(tailSet, subSet);
    	
    }
    
    /**
     * Tests the tail set inclusive method using an index as a parameter.
     */
    @Test
    public void testTailSetInclusiveIndex() {

        int index = random.nextInt((testSet.size()) + 1);

    	Set<WellBigInteger> tailSetTrue = testSet.tailSet(index, true);
    	Set<WellBigInteger> subSetTrue = testSet.subSet(index, true, testSet.size() - 1, true);
    	Set<WellBigInteger> tailSetFalse = testSet.tailSet(index, false);
    	Set<WellBigInteger> subSetFalse = testSet.subSet(index, false, testSet.size() - 1, true);
    	
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

        Iterator<WellBigInteger> iter = testSet.iterator();
        
        while(current < index) {
        	iter.next();
        	current++;
        }
        
        WellBigInteger toFind = iter.next();   	
    	WellBigInteger outside = new WellBigInteger(maxRow + 1, maxColumn);
    	
    	assertTrue(testSet.contains(toFind));
    	assertFalse(testSet.contains(outside));
    }   

    /**
     * Tests contains well set method.
     */
    @Test
    public void testContainsSet() {

    	WellSetBigInteger subset = new WellSetBigInteger(testSet.subSet(begin, end));
        WellSetBigInteger outside = new WellSetBigInteger(this.randomWellCollectionLength(testSet));
    	
    	assertTrue(testSet.contains(subset));
    	assertFalse(testSet.contains(outside));
    }
    
    /**
     * Tests contains collection method.
     */
    @Test
    public void testContainsCollection() {

    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	Set<WellBigInteger> outside = this.randomWellCollectionLength(testSet);
    	
    	assertTrue(testSet.contains(subset));
    	assertFalse(testSet.contains(outside));
    }
    
    /**
     * Tests contains well array method.
     */
    @Test
    public void testContainsArray() {

    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	Set<WellBigInteger> outside = this.randomWellCollectionLength(testSet);
    	
    	WellBigInteger[] subsetArray = subset.toArray(new WellBigInteger[subset.size()]);
    	WellBigInteger[] outsideArray = outside.toArray(new WellBigInteger[subset.size()]);
    	
    	assertTrue(testSet.contains(subsetArray));
    	assertFalse(testSet.contains(outsideArray));
    }
    
    /**
     * Tests contains method using a delimiter separated list of wells.
     */
    @Test
    public void testCountainsWellListStringDelimiter() {

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	String list = "";
    	
    	Iterator<WellBigInteger> iter = randomSet.iterator();

    	while(iter.hasNext()) {

    		WellBigInteger next = iter.next();

    	    if(iter.hasNext()) {
    	    	list += next.index() + " @ ";
    	    } else {
    	    	list += next.index();
    	    	break;
    	    }
    	}

    	Set<WellBigInteger> outsideSet = this.randomWellCollectionLength(testSet);
        String outsideList = "";
    	
    	Iterator<WellBigInteger> outsideIter = outsideSet.iterator();

    	while(outsideIter.hasNext()) {

    		WellBigInteger next = outsideIter.next();

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

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	String well = ((WellBigInteger) RandomUtil.randomObject(randomSet)).index();
    	
    	Set<WellBigInteger> outsideSet = this.randomWellCollectionLength(testSet);
    	String outsideWell = ((WellBigInteger) RandomUtil.randomObject(outsideSet)).index();
    	
    	assertTrue(testSet.contains(well));
    	assertFalse(testSet.contains(outsideWell));
    }

    /**
     * Tests look up of a well index.
     */
    @Test
    public void testContainsIndex() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	Set<WellBigInteger> outsideSet = this.randomWellCollectionLength(testSet);
    	WellBigInteger outsideWell = (WellBigInteger) RandomUtil.randomObject(outsideSet);
    	WellIndex outsideIndex = new WellIndex(outsideWell.row(), outsideWell.column());
    	
    	assertTrue(testSet.contains(index));
    	assertFalse(testSet.contains(outsideIndex));
    }

    /**
     * Tests look up of a well list.
     */
    @Test
    public void testContainsWellList() {
    	
    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellSetBigInteger listSet = new WellSetBigInteger(randomSet);
    	WellList list = listSet.wellList();
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);
    	WellSetBigInteger notCommonSet = new WellSetBigInteger(noCommonWells);
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
    	Set<WellBigInteger> set = this.randomWellCollectionLength(new WellSetBigInteger());
    	WellSetBigInteger wellSet = new WellSetBigInteger(set);
    	assertEquals(set, wellSet.allWells());
    }

    /**
     * Tests get wells well method.
     */
    @Test
    public void testGetWellsWell() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	
    	assertEquals(random, testSet.getWells(random));
    }
    
    /**
     * Tests get wells well set method.
     */
    @Test
    public void testGetWellsSet() {

    	WellSetBigInteger subset = new WellSetBigInteger(testSet.subSet(begin, end));
        WellSetBigInteger outside = new WellSetBigInteger(this.randomWellCollectionLength(testSet));
    	
    	assertEquals(subset, testSet.getWells(subset));
    	assertEquals(null, testSet.getWells(outside));
    }
    
    /**
     * Tests get wells collection method.
     */
    @Test
    public void testGetWellsCollection() {

    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	Set<WellBigInteger> outside = this.randomWellCollectionLength(testSet);
    	
    	assertEquals(new WellSetBigInteger(subset), testSet.getWells(subset));
    	assertEquals(null, testSet.getWells(outside));
    }
    
    /**
     * Tests contains well array method.
     */
    @Test
    public void testGetWellArray() {

    	Set<WellBigInteger> subset = testSet.subSet(begin, end);
    	Set<WellBigInteger> outside = this.randomWellCollectionLength(testSet);
    	
    	WellBigInteger[] subsetArray = subset.toArray(new WellBigInteger[subset.size()]);
    	WellBigInteger[] outsideArray = outside.toArray(new WellBigInteger[subset.size()]);
    	
    	assertEquals(new WellSetBigInteger(subset), testSet.getWells(subsetArray));
    	assertNull(testSet.getWells(outsideArray));
    }
    
    /**
     * Tests the retrieval of a list of delimiter separated wells.
     */
    @Test
    public void testGetWellsDelimiter() {

        WellBigInteger[] array = testSet.toWellArray();
    	WellSetBigInteger list = new WellSetBigInteger();

    	WellBigInteger startingWell = array[begin];
    	WellBigInteger endingWell = array[end];
    	
    	boolean addMode = false;
    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	while(iter.hasNext()) {
    		
    		WellBigInteger well = iter.next();
    		
    		if(well.equals(startingWell)) {
    			addMode = true;
    		}
    		
    		if(well.equals(endingWell)) {
    			break;
    		}
    		
    		if(addMode) {
    			list.add(new WellBigInteger(well));
    		}
    		
    	}

        String listString = "";
    	
    	Iterator<WellBigInteger> listIter = list.iterator();

    	while(listIter.hasNext()) {
    	    WellBigInteger next = listIter.next();
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

    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellBigInteger well = ((WellBigInteger) RandomUtil.randomObject(randomSet));
    	String wellString = well.index();
    	
    	Set<WellBigInteger> outsideSet = this.randomWellCollectionLength(testSet);
    	WellBigInteger outsideWell = ((WellBigInteger) RandomUtil.randomObject(outsideSet));
    	String outsideString = outsideWell.index();
    	
    	assertEquals(well, testSet.getWells(wellString));
    	assertEquals(null, testSet.getWells(outsideString));
    }

    /**
     * Tests the get wells method using an index.
     */
    @Test
    public void testGetWellsIndex() {

    	WellBigInteger random = (WellBigInteger) RandomUtil.randomObject(testSet.allWells());
    	WellIndex index = new WellIndex(random.row(), random.column());
    	
    	Set<WellBigInteger> outsideSet = this.randomWellCollectionLength(testSet);
    	WellBigInteger outsideWell = (WellBigInteger) RandomUtil.randomObject(outsideSet);
    	WellIndex outsideIndex = new WellIndex(outsideWell.row(), outsideWell.column());
    	
    	assertEquals(random, testSet.getWells(index));
    	assertEquals(null, testSet.getWells(outsideIndex));
    }

    /**
     * Tests the get wells method using a well list.
     */
    @Test
    public void testGetWellsWellList() {
    	
    	Set<WellBigInteger> randomSet = testSet.subSet(begin, end);
    	WellSetBigInteger listSet = new WellSetBigInteger(randomSet);
    	WellList list = listSet.wellList();
    	
    	Set<WellBigInteger> noCommonWells = this.randomWellCollection(testSet);
    	WellSetBigInteger notCommonSet = new WellSetBigInteger(noCommonWells);
    	WellList listNotCommon = notCommonSet.wellList();

    	assertEquals(listSet, testSet.getWells(list));
    	assertEquals(null, testSet.getWells(listNotCommon));
    }
    
    /**
     * Tests the retrieval of all wells in the given row.
     */
    @Test
    public void testGetRow() {

    	WellSetBigInteger fixedRow = new WellSetBigInteger();
    	
    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	
    	for(int i = 0; i < wellNum; i++) {
    		WellBigInteger well = RandomUtil.randomWellBigInteger(minValue, maxValue, 
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

    	WellSetBigInteger fixedColumn = new WellSetBigInteger();

    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	
    	for(int i = 0; i < wellNum; i++) {
    		WellBigInteger well = RandomUtil.randomWellBigInteger(minValue, maxValue, 
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

		WellBigInteger[] array = testSet.toWellArray();
		
		assertTrue(testSet.contains(array));
		assertEquals(testSet.size(), array.length);
		assertTrue(array instanceof WellBigInteger[]);
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
    	
    	WellSetBigInteger set = new WellSetBigInteger();

    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	wellNum = wellNum % 2 == 0 ? wellNum : wellNum + 1;
    	
    	for(int i = 0; i < wellNum; i++) {
    		set.add(new WellBigInteger(i, i + 1));
    	}
    	
    	assertEquals(wellNum, set.size());
    	
    	for(int i = 0; i < wellNum / 2; i++) {
    		set.remove(new WellBigInteger(i, i + 1));
    	}
    	
    	assertEquals(wellNum / 2, set.size());
    }
    
    /**
     * Tests the well list getter.
     */
    @Test
    public void testWellList() {

    	WellList list = new WellList();
    	
    	for(WellBigInteger well : testSet) {
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
        
        for(WellBigInteger well : testSet) {
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
    	
    	WellSetBigInteger set = new WellSetBigInteger();
    	
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
    	
    	Iterator<WellBigInteger> iter = testSet.iterator();
    	
    	while(iter.hasNext()) {
    		
    		WellBigInteger well = iter.next();
    		
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
     * Returns a random list of big integer wells.
     * @return    set of wells
     */
    public Set<WellBigInteger> randomWellCollection(WellSetBigInteger set) {
    
    	Set<WellBigInteger> randomCollection = new HashSet<WellBigInteger>();

    	while(randomCollection.size() != wellNumber) {
    		
    		WellBigInteger well = RandomUtil.randomWellBigInteger(minValue, maxValue, 
    	    		minRow, maxRow, minColumn, maxColumn, minLength, maxLength);

    		if(!randomCollection.contains(well) && !set.contains(well)) {
    	        randomCollection.add(well);
    		}
    	}

    	return randomCollection;
    }
    
    /**
     * Returns a random list of big integer wells.
     * @return    set of wells
     */
    public Set<WellBigInteger> randomWellCollectionLength(WellSetBigInteger set) {
    
    	Set<WellBigInteger> randomCollection = new HashSet<WellBigInteger>();

    	int wellNum = minLength + random.nextInt((maxLength - minLength) + 1);
    	
    	while(randomCollection.size() != wellNum) {
    		
    		WellBigInteger well = RandomUtil.randomWellBigInteger(minValue, maxValue, 
    	    		minRow, maxRow, minColumn, maxColumn, minLength, maxLength);

    		if(!randomCollection.contains(well) && !set.contains(well)) {
    			randomCollection.add(well);
    		}
    	}

    	return randomCollection;
    }
    
}
