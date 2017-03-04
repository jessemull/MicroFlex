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

package com.github.jessemull.microflex.doubleflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.util.ArrayList;
import java.util.List;

/**
 * This class performs the decrement operation for double plate stacks, plates, 
 * wells and well sets. Unary operations can also be performed on a subset of
 * data using a beginning index and subset length. 
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class DecrementDouble extends MathOperationDoubleUnary {

    /**
     * Decrements the values in the list.
     * @param    List<Double>    the list
     * @return                   the result
     */
    public List<Double> calculate(List<Double> list) {

        List<Double> result = new ArrayList<Double>();
        
        for(double db : list) {
            result.add(db--);
        }
        
        return result;
    }

    /**
     * Decrements the values in the list between the indices.
     * @param    List<Double>    the list
     * @return                   the result
     */
    public List<Double> calculate(List<Double> list, int begin, int length) {
        
        List<Double> result = new ArrayList<Double>();
        
        for(int i = begin; i < begin + length; i++) {
            result.add(list.get(i) - 1);
        }
        
        return result;
    }
    
}
