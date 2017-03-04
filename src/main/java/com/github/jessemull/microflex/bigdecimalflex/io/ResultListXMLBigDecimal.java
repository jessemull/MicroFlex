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

package com.github.jessemull.microflex.bigdecimalflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;

/**
 * This is a wrapper class used to marshal/unmarshal a list of XML encoded results.
 * 
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
@XmlRootElement(name = "results")
public class ResultListXMLBigDecimal implements Iterable<ResultXMLBigDecimal>{
    
	/*---------------------------- Private Fields ----------------------------*/
    
    List<ResultXMLBigDecimal> results = new ArrayList<ResultXMLBigDecimal>();    // The results in the list
    
    /*------------------------------ Constructors ----------------------------*/
    
    public ResultListXMLBigDecimal(){}
    
    /**
     * Creates an XML result list from a map.
     * @param    Map<WellBigDecimal, BigDecimal>    the result map
     */
    public ResultListXMLBigDecimal(Map<WellBigDecimal, BigDecimal> map) {
        this.results.add(new ResultXMLBigDecimal(map, "Result"));
    }
    
    /**
     * Creates an XML result list from a map and label.
     * @param    Map<WellBigDecimal, BigDecimal>    the result map
     * @param    String                             the result label
     */
    public ResultListXMLBigDecimal(Map<WellBigDecimal, BigDecimal> map, String label) {
        this.results.add(new ResultXMLBigDecimal(map, label));
    }
    
    /**
     * Creates an XML result list from a collection of maps.
     * @param    Collection<Map<WellBigDecimal, BigDecimal>>    collection of well objects
     */
    public ResultListXMLBigDecimal(Collection<Map<WellBigDecimal, BigDecimal>> collection) {
    	
    	int index = 1;
    	
        for(Map<WellBigDecimal, BigDecimal> map : collection) {
            this.results.add(new ResultXMLBigDecimal(map, "Result" + index++));
        }
    }
    
    /**
     * Creates an XML result list from a collection of maps and a list of labels.
     * @param    Collection<Map<WellBigDecimal, BigDecimal>>    collection of well objects
     * @param    List<String>                                   the result labels
     */
    public ResultListXMLBigDecimal(Collection<Map<WellBigDecimal, BigDecimal>> collection, List<String> labels) {
    	
    	int index = 0;
    	
        for(Map<WellBigDecimal, BigDecimal> map : collection) {
            this.results.add(new ResultXMLBigDecimal(map, labels.get(index++)));
        }
    }
    
    /*-------------------------- Getters and setters -------------------------*/
    
    /**
     * Sets the list of XML results.
     * @param    List<ResultXMLBigDecimal>    the new list of results
     */
    @XmlElement(name="result")
    public void setResults(List<ResultXMLBigDecimal> results) {
        this.results = results;
    }
    
    /**
     * Gets the list of XML results.
     * @return    the list of results
     */
    public List<ResultXMLBigDecimal> getResults() {
        return this.results;
    }

    /**
     * Returns the result at the index as a well set.
     * @param    int    the index
     * @return          the converted result
     */
    public ResultXMLBigDecimal get(int index) {
    	return this.results.get(index);
    }
    
    /**
     * Returns the size of the result list.
     * @return    the result list size
     */
    public int size() {
    	return this.results.size();
    }

    /**
     * Returns an iterator for result list.
     * @return    Iterator<ResultXMLBigDecimal>
     */
	public Iterator<ResultXMLBigDecimal> iterator() {
		return results.iterator();
	}

}
