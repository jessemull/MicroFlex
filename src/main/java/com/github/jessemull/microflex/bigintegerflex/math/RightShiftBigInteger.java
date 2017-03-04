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

package com.github.jessemull.microflex.bigintegerflex.math;

/* ------------------------------ Dependencies ------------------------------ */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * This class performs a right unsigned/arithmetic shift operations for BigInteger 
 * plate stacks, plates, wells and well sets. Right shift operations can also be 
 * performed on a subset of data using a beginning index and subset length. 
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 18, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class RightShiftBigInteger extends MathOperationBigIntegerShift {

    /**
     * Shifts the values in the list n bits to the right.
     * @param    List<BigInteger>    the list
     * @param    int                 number of bits to shift
     * @return                       the result
     * @override
     */
    public List<BigInteger> calculate(List<BigInteger> list, int n) {

        List<BigInteger> result = new ArrayList<BigInteger>();
        
        for(BigInteger in : list) {
            result.add(in.shiftRight(n));
        }
        
        return result;
        
    }

    /**
     * Shifts the values between the indices in the list n bits to the right.
     * @param    List<BigInteger>    the list
     * @param    int                 number of bits to shift
     * @return                       the result
     * @override
     */
    public List<BigInteger> calculate(List<BigInteger> list, int n, int begin, int length) {

        List<BigInteger> result = new ArrayList<BigInteger>();
        
        for(int i = begin; i < begin + length; i++) {
            result.add(list.get(i).shiftRight(n));
        }
        
        return result;
    }
    
}
