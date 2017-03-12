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

/* -------------------------------- Package --------------------------------- */

package com.github.jessemull.microflex.integerflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.util.ArrayList;
import java.util.List;

/**
 * This class performs increment operations with a single argument for integer 
 * plate stacks, plates, wells and well sets. Unary operations can also be 
 * performed on a subset of data using a beginning index and subset length. 
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class IncrementInteger extends MathOperationIntegerUnary {

    /**
     * Increments the values in the list.
     * @param    List<Integer>    the list
     * @return                    the result
     */
    public List<Integer> calculate(List<Integer> list) {

        List<Integer> result = new ArrayList<Integer>();
        
        for(int in : list) {
            result.add(++in);
        }
        
        return result;
    }

    /**
     * Increments the values in the list between the indices.
     * @param    List<Integer>    the list
     * @return                    the result
     */
    public List<Integer> calculate(List<Integer> list, int begin, int length) {
        
        List<Integer> result = new ArrayList<Integer>();
        
        for(int i = begin; i < begin + length; i++) {
            result.add(list.get(i) + 1);
        }
        
        return result;
    }
    
}
