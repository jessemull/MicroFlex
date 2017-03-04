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

/*---------------------------- Package Declaration ---------------------------*/

package com.github.jessemull.microflex.util;

/*------------------------------- Dependencies -------------------------------*/

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Implementation of big integer and big decimal methods missing from the java
 * math library but required for the MicroFlex math and statistics packages.
 * 
 * The immutable math utility currently supports the following additional 
 * operations for the BigDecimal and BigInteger classes:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Operation<div></th>
 *    <tr>
 *       <td>Square Root</td>
 *    </tr>
 *    <tr>
 *       <td>Fractional Exponentiation</td>
 *    </tr>
 *    <tr>
 *       <td>Natural Exponentiation</td>
 *    </tr>
 *    <tr>
 *       <td>Natural Log</td>
 *    </tr>
 * </table>
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 20, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class ImmutableMathUtil {

	/*---------------- Methods for calculating the square root ---------------*/
	
	/**
	 * Returns the square root of a big integer
	 * @param    BigInteger randicand    the input big integer randicand
	 * @return                the result
	 */
	public static BigInteger sqrt(BigInteger randicand) {

        BigInteger current = BigInteger.ZERO.setBit(randicand.bitLength()/2);
		BigInteger next = current;

	    while(true){
		    	
		    BigInteger result = current.add(randicand.divide(current)).shiftRight(1);
		       
		    if(result.equals(current) || result.equals(next)) {
		        return result;
		    }
		        
		    next = current;
		    current = result;
	    }
	}
	
	/**
	 * Returns the square root of a BigDecimal.
	 * @param    BigDecimal randicand    the input BigDecimal randicand
	 * @return                 the result
	 */
	static public BigDecimal sqrt(BigDecimal randicand) {
		
		MathContext context = new MathContext(randicand.precision());
		BigDecimal two = new BigDecimal(2);
		
		BigDecimal result = randicand.divide(two, context);
		
		boolean finished = false;
		
		int iterations = context.getPrecision() + 1;	
		int count = 0;
		
		while(!finished && count < iterations) {

			BigDecimal next = randicand.divide(result, context);
			
			next = next.add(result);
			next = next.divide(two, context);
			
			finished = next.equals(result);
			result = next;
			
			iterations++;
		}
		
		return result;
	}
	
	/**
	 * Returns the square root of a BigDecimal.
	 * @param    BigDecimal randicand    the input BigDecimal randicand
	 * @param    MathContextcontext      the math context
	 * @return                 the result
	 */
	static public BigDecimal sqrt(BigDecimal randicand, MathContext context) {
	
		BigDecimal two = new BigDecimal(2, context);
		
		BigDecimal result = randicand.divide(two, context);
		
		boolean finished = false;
		
		int iterations = context.getPrecision() + 1;	
		int count = 0;
		
		while(!finished && count < iterations) {

			BigDecimal next = randicand.divide(result, context);
			
			next = next.add(result);
			next = next.divide(two, context);
			
			finished = next.equals(result);
			result = next;
			
			iterations++;
		}
		
		return result;
	}
	
	/*-------------------- Methods for calculating powers --------------------*/
	
    /** 
	 * Raises the base to the power and returns the result. Calculates the power
	 * by finding the natural log using a Taylor sequence and exp using Newton's
	 * method of approximating the nth root.
	 * @param    BigDecimal base    the base
	 * @param    BigDecimal power	the power
	 * @return            the result
     */
     static public BigDecimal pow(BigDecimal base, BigDecimal power) {
    	 
    	 int precisionBase = base.precision();
    	 int precisionPower = power.precision();
    	 int precision = precisionBase < precisionPower ? precisionBase : precisionPower;
    	 
    	 MathContext context = new MathContext(precision, RoundingMode.HALF_DOWN);

    	 return pow(base, power, context);
     }

     /** 
 	  * Raises the base to the power and returns the result. Calculates the power
	  * by finding the natural log using a Taylor sequence and exp using Newton's
	  * method of approximating the nth root.
 	  * @param    BigDecimal     the base
 	  * @param    BigDecimal     the power
 	  * @param    MathContext    the math context
 	  * @return                  the result
      */
      static public BigDecimal pow(BigDecimal base, BigDecimal power, MathContext context) {

     	 if(base.compareTo(BigDecimal.ZERO) < 0) {
         	 throw new ArithmeticException("Base cannot be negative.");    
          }

          if(base.compareTo(BigDecimal.ZERO) == 0) {     
         	 return BigDecimal.ZERO;
          } 

          BigDecimal result = ln(base);
          result = power.multiply(result, context);
          result = exp(result, context);
 
          return result;
      }
    
      /*-------------- Methods for calculating the natural log ---------------*/
      
      /**
       * Calculates the natural logarithm using a Taylor sequqnce.
       * @param    BigDecimal    the input big decimal > 0
       * @return                 the natural logarithm
       */
      public static BigDecimal ln(BigDecimal input) {
   
   	   int precision = input.precision();
   	   MathContext context = new MathContext(precision, RoundingMode.HALF_DOWN);    
   	 
   	   return ln(input, context);
      }
      
     /**
      * Calculates the natural logarithm using a Taylor sequqnce.
      * @param    BigDecimal    the input big decimal > 0
      * @return                 the natural logarithm
      */
     public static BigDecimal ln(BigDecimal input, MathContext context) {
    
  	   BigDecimal two = new BigDecimal("2", context);
  	   
  	   BigDecimal inputMinus = input.subtract(BigDecimal.ONE, context);
  	   BigDecimal inputPlus = input.add(BigDecimal.ONE, context);
  	   BigDecimal y = inputMinus.divide(inputPlus, context);
  	   
  	   BigDecimal result = new BigDecimal("0", context);
  	   BigDecimal last = new BigDecimal(result.toString(), context);
  	   
  	   int k = 0;
  	   while(true) {
  		   
  		   BigDecimal argumentOne = BigDecimal.ONE.divide(BigDecimal.ONE.add(two.multiply(new BigDecimal(k), context), context), context);
  		   BigDecimal argumentTwo = y.pow(k * 2, context);
  		   result = result.add(argumentOne.multiply(argumentTwo, context), context);
  		   
  		   if(last.equals(result)) {
  			   break;
  		   }
  		   
  		   last = new BigDecimal(result.toString(), context);
  		   k++;
  	   }
  	 
  	   return y.multiply(two, context).multiply(result, context);
     }
     
     /*------------------ Methods for raising e to a power -------------------*/
     
     /**
      * Raises e to the power of the input big decimal.
      * @param    BigDecimal    the input power
      * @return                 the result
      */
     public static BigDecimal exp(BigDecimal power) {
    
    	 int precision = power.precision();
    	 
    	 MathContext context = new MathContext(precision, RoundingMode.HALF_DOWN);
    	 
    	 return exp(power, context);
     }
     
     /**
      * Returns e raised to the input power using a Taylor expansion with
      * @param    BigDecimal     the power
      * @param    MathContext    the math context
      * @return                  e raised to the input power
      */
     public static BigDecimal exp(BigDecimal input, MathContext context) {
     
    	 int taylorTerms = 8;
    	 
    	 if(input.compareTo(BigDecimal.ZERO) < 0)  {
              
    		 BigDecimal inverse = exp(input.negate(), context);
    		 return BigDecimal.ONE.divide(inverse, context);
    		 
          } else if(input.compareTo(BigDecimal.ZERO) == 0) {
        	  
        	  return BigDecimal.ONE.setScale(context.getPrecision(), RoundingMode.HALF_DOWN);
        	  
          } else {

        	  double inputDouble = input.doubleValue();
              double inputUlpDouble = input.ulp().doubleValue();
              double taylorNumberCheck1 = Math.pow(inputDouble, taylorTerms);
              double taylorNumberCheck2 = taylorTerms * (taylorTerms - 1.0) * (taylorTerms - 2.0) * inputUlpDouble;
             
              if(taylorNumberCheck1 < taylorNumberCheck2) {
    
            	  BigDecimal result = BigDecimal.ONE;
                  BigDecimal inputPowerNum = BigDecimal.ONE;
                  BigInteger factorialNum = BigInteger.ONE;
                  
                  int taylorPrecision = 1 + (int) (Math.log10(Math.abs(0.5 / (inputUlpDouble / taylorTerms))));
                  MathContext taylorContext = new MathContext(taylorPrecision);

                  for(int i = 1; i<= taylorTerms; i++) {
  
                      factorialNum = factorialNum.multiply(new BigInteger(i + ""));
                      inputPowerNum = inputPowerNum.multiply(input);
                                  
                      BigDecimal c = inputPowerNum.divide(new BigDecimal(factorialNum), taylorContext);
                      result = result.add(c);
                      
                      double taylorNumberCheck3 = Math.abs(inputPowerNum.doubleValue());
                      double taylorNumberCheck4 = Math.abs(c.doubleValue());                    
                      double taylorNumberCheck5 = inputUlpDouble / 2;

                      if(taylorNumberCheck3 < i && taylorNumberCheck4 < taylorNumberCheck5) {
                          break;
                      }
                  }
                  
                  int finalPrecision = 1 + (int) (Math.log10(Math.abs(0.5 / (inputUlpDouble / 2.0))));;
                  MathContext finalContext = new MathContext(finalPrecision);
           
                  return result.round(finalContext); 
                  
              } else {
 
            	  double taylorProduct = taylorTerms * (taylorTerms - 1.0) * (taylorTerms - 2.0) * inputUlpDouble;
            	  double taylorPower = Math.pow(inputDouble, taylorTerms);
            	  
            	  Double scaleBy10Double = 1.0 - Math.log10(taylorProduct / taylorPower) / (taylorTerms - 1.0); 
                  int scaleBy10 = scaleBy10Double.intValue();
                  
            	  BigDecimal inputRaised10 = input.scaleByPowerOfTen(-scaleBy10);                   
            	  BigDecimal expxby10 = exp(inputRaised10, context);

            	  MathContext contextRaised10 = new MathContext(expxby10.precision() - scaleBy10);

            	  while(scaleBy10 > 0) {

            		  int minimum = Math.min(8, scaleBy10);
            		  scaleBy10 -= minimum;
            		             
            		  MathContext minContext = new MathContext(expxby10.precision() - minimum + 2);
                      int precisionMin = 1;
                      
                      while (minimum-- > 0) {
                    	  precisionMin *= 10;
                      }
                          
                      expxby10 = expxby10.pow(precisionMin, minContext);
                      
            	  }
                      
            	  return expxby10.round(contextRaised10);   
              }
          }
     }

}
