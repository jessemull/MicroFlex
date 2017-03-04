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

/*--------------------------- Package Declaration ----------------------------*/

package com.github.jessemull.microflex.bigintegerflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded stack objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name="stacks")
public class StackListXMLBigInteger implements Iterable<StackXMLBigInteger> {
    
	/*---------------------------- Private fields ----------------------------*/
    
    private List<StackXMLBigInteger> stacks = new ArrayList<StackXMLBigInteger>();    // The stacks in the list
    
    /*----------------------------- Constructors -----------------------------*/
    
    public StackListXMLBigInteger(){}
    
    /**
     * Creates an XML stack list from a stack object.
     * @param    StackBigInteger    the stack object
     */
    public StackListXMLBigInteger(StackBigInteger stack) {
        this.stacks.add(new StackXMLBigInteger(stack));
    }
    
    /**
     * Creates an XML stack list from a collection of stack objects.
     * @param    Collection<StackBigInteger> collection    collection of stack objects
     */
    public StackListXMLBigInteger(Collection<StackBigInteger> collection) {
        for(StackBigInteger stack : collection) {
            this.stacks.add(new StackXMLBigInteger(stack));
        }
    }
    
    /**
     * Creates an XML stack list from an array of stack objects.
     * @param    StackBigInteger[] array    array of stack objects
     */
    public StackListXMLBigInteger(StackBigInteger[] array) {
        for(StackBigInteger stack : array) {
            this.stacks.add(new StackXMLBigInteger(stack));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of XML stacks.
     * @param    List<StackXMLBigInteger> stacks    the new list of stacks
     */
    @XmlElement(name="stack")
    public void setStacks(List<StackXMLBigInteger> stacks) {
        this.stacks = stacks;
    }
    
    /**
     * Gets the list of XML stacks.
     * @return    List<StackXMLBigInteger>    the list of stacks
     */
    public List<StackXMLBigInteger> getStacks() {
        return this.stacks;
    }
    
    /**
     * Returns the stack at the indicated index.
     * @param    int index         the index into the stack list
     * @return   StackXMLBigInteger stack    the XML stack
     */
    public StackXMLBigInteger get(int index) {
        return this.stacks.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of stacks.
     * @return    Iterator<StackXMLBigInteger>    the iterator
     */
    public Iterator<StackXMLBigInteger> iterator() {
        return this.stacks.iterator();
    }
    
    /**
     * Returns the size of the stack list.
     * @return    int size    the size of the list
     */
    public int size() {
        return this.stacks.size();
    }
}
