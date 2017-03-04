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

package com.github.jessemull.microflex.doubleflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.github.jessemull.microflex.doubleflex.plate.StackDouble;

/**
 * This is a wrapper class for importing or exporting a list of JSON encoded stack objects.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class StackListPOJODouble implements Iterable<StackPOJODouble> {
    
	/*---------------------------- Private fields ----------------------------*/
    
    private List<StackPOJODouble> stacks = new ArrayList<StackPOJODouble>();    // The stacks in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public StackListPOJODouble(){}
    
    /**
     * Creates a stack list POJO from a stack object.
     * @param    StackDouble    the stack object
     */
    public StackListPOJODouble(StackDouble stack) {
        this.stacks.add(new StackPOJODouble(stack));
    }
    
    /**
     * Creates a stack list POJO from a collection of stack objects.
     * @param    Collection<StackDouble>    collection of stack objects
     */
    public StackListPOJODouble(Collection<StackDouble> collection) {
        for(StackDouble stack : collection) {
            this.stacks.add(new StackPOJODouble(stack));
        }
    }
    
    /**
     * Creates a stack loist POJO from an array of stack objects.
     * @param    StackDouble[]    array of stack objects
     */
    public StackListPOJODouble(StackDouble[] array) {
        for(StackDouble stack : array) {
            this.stacks.add(new StackPOJODouble(stack));
        }
    }
    
    /*------------------------- Getters and setters --------------------------*/
    
    /**
     * Sets the list of stack POJOs.
     * @param    List<StackPOJODouble>    the new list of stacks
     */
    public void setStacks(List<StackPOJODouble> stacks) {
        this.stacks = stacks;
    }
    
    /**
     * Gets the list of stack POJOs.
     * @return    the list of stacks
     */
    public List<StackPOJODouble> getStacks() {
        return this.stacks;
    }
    
    /**
     * Returns the stack at the indicated index.
     * @param    int    the index into the stack list
     * @return          the stack POJO
     */
    public StackPOJODouble get(int index) {
        return this.stacks.get(index);
    }
    
    /*----------------- Methods for iterating over the list ------------------*/
    
    /**
     * Returns an iterator for the list of stacks.
     * @return    the iterator
     */
    public Iterator<StackPOJODouble> iterator() {
        return this.stacks.iterator();
    }
    
    /**
     * Returns the size of the stack list.
     * @return    the size of the list
     */
    public int size() {
        return this.stacks.size();
    }
}
