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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.jessemull.microflex.bigintegerflex.plate.PlateBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.StackBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellBigInteger;
import com.github.jessemull.microflex.bigintegerflex.plate.WellSetBigInteger;

/**
 * Formats and writes stacks, plates, well sets and wells to an output stream or 
 * string. Supports the following formats:
 * 
 * <table cellspacing="5px" style="text-align:left; margin: 20px;">
 *    <th><div style="border-bottom: 1px solid black; padding-bottom: 5px;">Formats<div></th>
 *    <tr>
 *       <td>Plate Map</td>
 *    </tr>
 *    <tr>
 *       <td>Result Table</td>
 *    </tr>
 *    <tr>
 *       <td>JSON</td>
 *    </tr>
 *    <tr>
 *       <td>XML</td>
 *    </tr>
 * </table>
 *   
 * Result tables and maps are separated using a user defined delimiter or the
 * default tab delimiter.
 * 
 * <br><br>
 * 
 *  <div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result Plate Map:</p>
 *          <table style="text-align: left;" cellpadding="5px">
 *              <tr>
 *                  <td>Result</td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *                  <td></td>
 *              </tr>
 *              <tr>
 *                  <td></td>
 *                  <td>1</td>
 *                  <td>2</td>
 *                  <td>3</td>
 *                  <td>4</td>
 *                  <td>5</td>
 *                  <td>6</td>
 *              </tr>
 *              <tr>
 *                  <td>A</td>
 *                  <td>9.114</td>
 *                  <td>2.667</td>
 *                  <td>4.214</td>
 *                  <td>5.154</td>
 *                  <td>1.293</td>
 *                  <td>7.882</td>
 *              </tr>
 *              <tr>
 *                  <td>B</td>
 *                  <td>8.953</td>
 *                  <td>4.827</td>
 *                  <td>5.149</td>
 *                  <td>9.792</td>
 *                  <td>2.514</td>
 *                  <td>6.448</td>
 *              </tr>
 *              <tr>
 *                  <td>C</td>
 *                  <td>6.328</td>
 *                  <td>4.896</td>
 *                  <td>5.130</td>
 *                  <td>9.655</td>
 *                  <td>5.120</td>
 *                  <td>1.485</td>
 *              </tr>
 *              <tr>
 *                  <td>D</td>
 *                  <td>2.661</td>
 *                  <td>5.290</td>
 *                  <td>4.057</td>
 *                  <td>2.374</td>
 *                  <td>1.200</td>
 *                  <td>2.724</td>
 *              </tr>
 *          </table>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result Table:</p>
 *          <table style="text-align: left;" cellpadding="5px">
 *              <tr>
 *                  <td>Result</td>
 *                  <td></td>
 *              </tr>
 *              <tr>
 *                  <td>Index</td>
 *                  <td>Value</td>
 *              </tr>
 *              <tr>
 *                  <td>A1</td>
 *                  <td>48.77</td>
 *              </tr>
 *              <tr>
 *                  <td>A2</td>
 *                  <td>50.32</td>
 *              </tr>
 *              <tr>
 *                  <td>A3</td>
 *                  <td>67.23</td>
 *              </tr>
 *              <tr>
 *                  <td>B1</td>
 *                  <td>49.81</td>
 *              </tr>
 *          </table>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result JSON:</p>
 *          <pre>
 *  {
 *   "results" : [ {
 *     "type" : "Double",
 *     "label" : "Example Result",
 *     "size" : 10,
 *     "wells" : {
 *       "A11" : 56.455852093078924,
 *       "A2" : 61.49253731775972,
 *       "A5" : 58.462812164254956,
 *       "D10" : 58.957595115303654,
 *       "D2" : 54.5241886265687,
 *       "E4" : 56.821464250881434,
 *       "E8" : 61.16876436088828,
 *       "E9" : 49.45000301303015,
 *       "G6" : 58.74507294128508,
 *       "H1" : 49.115877056175314
 *     }
 *   } ]
 *  }
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Result XML:</p>
 *          <pre>
 *  &lt?xml version=&quot1.0&quot encoding=&quotUTF-8&quot standalone=&quotyes&quot?&gt
 *  &ltresults&gt
 *     &ltresult&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Result&lt/label&gt
 *         &ltsize&gt3&lt/size&gt
 *         &ltwells&gt
 *             &ltwell&gt
 *                 &ltindex&gtA2&lt/index&gt
 *                 &ltvalue&gt55.873951592640616&lt/value&gt
 *             &lt/well&gt
 *             &ltwell&gt
 *                 &ltindex&gtA3&lt/index&gt
 *                 &ltvalue&gt53.03191840271489&lt/value&gt
 *             &lt/well&gt
 *             &ltwell&gt
 *                 &ltindex&gtA4&lt/index&gt
 *                 &ltvalue&gt49.02962476222037&lt/value&gt
 *             &lt/well&gt
 *         &lt/wells&gt
 *     &lt/result&gt
 *  &lt/results&gt
 *       </pre>
 *      </div>
 *  </div>
 *  <div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Well JSON:</p>
 *          <pre>
 *  {
 *    "wells" : [ {
 *      "type" : "Double",
 *      "index" : "F8",
 *      "size" : 24,
 *      "values" : [ 15.11, 28.82, 52.54 ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Well Set JSON:</p>
 *          <pre>
 *  {
 *    "wellsets" : [ {
 *      "type" : "Double",
 *      "label" : "Example Well Set",
 *      "size" : 1,
 *      "wells" : [ {
 *        "index" : "A3",
 *        "values" : [ 44.70, 91.16, 52.15 ]
 *      } ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Plate JSON:</p>
 *          <pre>
 *  {
 *    "plates" : [ {
 *      "type" : "Double",
 *      "label" : "Example Plate",
 *      "descriptor" : "96-Well",
 *      "rows" : 8,
 *      "columns" : 12,
 *      "size" : 1,
 *      "wellsets" : [ {
 *        "label" : "Example Well Set",
 *        "size" : 3,
 *        "wells" : [ "C5", "D4", "F7" ]
 *      } ],
 *      "wells" : [ {
 *        "index" : "A7",
 *        "values" : [ 44.51, 34.53, 55.92 ]
 *      } ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *      <div style="display: inline-block; vertical-align: top; margin-right: 40px;">
 *          <p style="font-weight: bold;">Example Stack JSON:</p>
 *          <pre>
 *  {
 *    "stacks" : [ {
 *      "type" : "Double",
 *      "label" : "Example Stack",
 *      "rows" : 8,
 *      "columns" : 12,
 *      "size" : 1,
 *      "plates" : [ {
 *        "type" : "Double",
 *        "label" : "Example Plate",
 *        "descriptor" : "96-Well",
 *        "rows" : 8,
 *        "columns" : 12,
 *        "size" : 1,
 *        "wellsets" : [ {
 *          "label" : "Example Well Set",
 *          "size" : 3,
 *          "wells" : [ "B6", "G1", "G12" ]
 *        } ],
 *        "wells" : [ {
 *          "index" : "A2",
 *          "values" : [ 35.94, 28.17, 21.00 ]
 *        } ]
 *      } ]
 *    } ]
 *  }
 *        </pre>
 *      </div>
 *  </div>
 *  <div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Well XML:</p>
 *          <pre>
 *  &ltwells&gt
 *     &ltwell&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltindex&gtH8&lt/index&gt
 *         &ltsize&gt24&lt/size&gt
 *         &ltvalues&gt
 *             &ltvalue&gt78.70&lt/value&gt
 *             &ltvalue&gt72.96&lt/value&gt
 *             &ltvalue&gt38.98&lt/value&gt
 *         &lt/values&gt
 *     &lt/well&gt
 *  &lt/wells&gt
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Well Set XML:</p>
 *          <pre>
 *  &ltwellsets&gt
 *     &ltwellset&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Well Set&lt/label&gt
 *         &ltsize&gt1&lt/size&gt
 *         &ltwells&gt
 *             &ltwell&gt
 *                 &ltindex&gtA11&lt/index&gt
 *                 &ltvalues&gt
 *                     &ltvalue&gt17.39&lt/value&gt
 *                     &ltvalue&gt54.65&lt/value&gt
 *                     &ltvalue&gt54.12&lt/value&gt
 *                 &lt/values&gt
 *             &lt/well&gt
 *         &lt/wells&gt
 *     &lt/wellset&gt
 *  &lt/wellsets&gt
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Plate XML:</p>
 *          <pre>
 *  &ltplates&gt
 *     &ltplate&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Plate&lt/label&gt
 *         &ltdescriptor&gt96-Well&lt/descriptor&gt
 *         &ltrows&gt8&lt/rows&gt
 *         &ltcolumns&gt12&lt/columns&gt
 *         &ltsize&gt1&lt/size&gt
 *         &ltwellsets&gt
 *             &ltwellset&gt
 *                 &ltlabel&gtExample Well Set&lt/label&gt
 *                 &ltsize&gt3&lt/size&gt
 *                 &ltwells&gt
 *                     &ltwell&gtB12&lt/well&gt
 *                     &ltwell&gtE2&lt/well&gt
 *                     &ltwell&gtH8&lt/well&gt
 *                 &lt/wells&gt
 *             &lt/wellset&gt
 *         &lt/wellsets&gt
 *         &ltwells&gt
 *             &ltwell&gt
 *                 &ltindex&gtA5&lt/index&gt
 *                 &ltvalues&gt
 *                     &ltvalue&gt13.89&lt/value&gt
 *                     &ltvalue&gt92.46&lt/value&gt
 *                     &ltvalue&gt19.48&lt/value&gt
 *                 &lt/values&gt
 *             &lt/well&gt
 *         &lt/wells&gt
 *     &lt/plate&gt
 *  &lt/plates&gt
 *       </pre>
 *      </div>
 *      <div style="display: inline-block; margin-right: 40px; vertical-align: top;">
 *          <p style="font-weight: bold;">Example Stack XML:</p>
 *          <pre>
 *  &ltstacks&gt
 *     &ltstack&gt
 *         &lttype&gtDouble&lt/type&gt
 *         &ltlabel&gtExample Stack&lt/label&gt
 *         &ltrows&gt8&lt/rows&gt
 *         &ltcolumns&gt12&lt/columns&gt
 *         &ltsize&gt1&lt/size&gt
 *         &ltplates&gt
 *             &ltplate&gt
 *                 &lttype&gtDouble&lt/type&gt
 *                 &ltlabel&gtExample Plate&lt/label&gt
 *                 &ltdescriptor&gt96-Well&lt/descriptor&gt
 *                 &ltrows&gt8&lt/rows&gt
 *                 &ltcolumns&gt12&lt/columns&gt
 *                 &ltsize&gt1&lt/size&gt
 *                 &ltwellsets&gt
 *                     &ltwellset&gt
 *                         &ltlabel&gtExample Well Set&lt/label&gt
 *                         &ltsize&gt3&lt/size&gt
 *                         &ltwells&gt
 *                             &ltwell&gtB7&lt/well&gt
 *                             &ltwell&gtH2&lt/well&gt
 *                             &ltwell&gtH3&lt/well&gt
 *                         &lt/wells&gt
 *                     &lt/wellset&gt
 *                 &lt/wellsets&gt
 *                 &ltwells&gt
 *                     &ltwell&gt
 *                         &ltindex&gtA2&lt/index&gt
 *                         &ltvalues&gt
 *                             &ltvalue&gt13.03&lt/value&gt
 *                             &ltvalue&gt53.21&lt/value&gt
 *                             &ltvalue&gt28.08&lt/value&gt
 *                         &lt/values&gt
 *                     &lt/well&gt
 *                 &lt/wells&gt
 *             &lt/plate&gt
 *         &lt/plates&gt
 *     &lt/stack&gt
 *  &lt/stacks&gt
 *       </pre>
 *      </div>
 *  </div>
 *  
 * @author Jesse L. Mull
 * @update Updated Oct 17, 2016
 * @address http://www.jessemull.com
 * @email hello@jessemull.com
 */
public class PlateWriterBigInteger extends PrintWriter {
	
	/*---------------------------- Private Fields ----------------------------*/
	
	/* Number of character types available for the row ID */

	private int ALPHA_BASE = 26;
    
    /* The delimiter for delimiter separated values */
    
    private String delimiter = "\t";
    
    /*------------------------------ Constructors ----------------------------*/
    
    /**
     * Creates a new PlateWriterBigInteger without automatic line flushing using 
     * the specified file.
     * @param    File file    the output file
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigInteger(File file) throws FileNotFoundException {
    	super(file);
    }
    
    /**
     * Creates a new PlateWriterBigInteger without automatic line flushing using 
     * the specified file and character set.
     * @param    File file    the output file
     * @param    String csn   the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigInteger(File file, String csn) throws FileNotFoundException, 
                                                            UnsupportedEncodingException {
    	super(file, csn);
    }

    /**
     * Creates a new PlateWriterBigInteger, without automatic line flushing using
     * the OutputStream.
     * @param    OutputStream out    the output stream
     */
    public PlateWriterBigInteger(OutputStream out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterBigInteger with automatic line flushing, using the 
     * OutputStream.
     * @param    OutputStream out    the output stream
     * @param    boolean autoFlush   sets automatic flush when true
     */
    public PlateWriterBigInteger(OutputStream out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /**
     * Creates a new PlateWriterBigInteger without automatic line flushing using the
     * specified file name.
     * @param    String fileName    the file name
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigInteger(String fileName) throws FileNotFoundException {
    	super(fileName);
    }
    
    /**
     * Creates a new PlateWriterBigInteger without automatic line flushing using the
     * specified file name and character set.
     * @param    String fileName    the file name
     * @param    String csn         the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigInteger(String fileName, String csn) throws FileNotFoundException, 
                                                                  UnsupportedEncodingException {
    	super(fileName, csn);
    }

    /**
     * Creates a new PlateWriterBigInteger without automatic line flushing using the
     * writer.
     * @param    Writer out    the writer
     */
    public PlateWriterBigInteger(Writer out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterBigInteger with automatic line flushing using the
     * writer.
     * @param    Writer out    the writer
     * @param    boolean autoFlush    sets auto flush when true
     */
    public PlateWriterBigInteger(Writer out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Prints the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int type                                the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigInteger, BigInteger> data, int type) 
			throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
		
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int type                                the plate type
     */
	public String resultToPlateMapAsString(Map<WellBigInteger, BigInteger> data, int type) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int type                                      the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigInteger, BigInteger>> data, 
    		int type) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellBigInteger, BigInteger> map : data) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
    
	/**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int type                                      the plate type
     * @return   String                                        the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigInteger, BigInteger>> data, int type) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellBigInteger, BigInteger> map : data) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
	
    
    
	/**
     * Prints the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int rows                                number of rows  
     * @param    int columns                             number of columns
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigInteger, BigInteger> data, int rows, int columns) 
			throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
	    
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int rows                                number of rows  
     * @param    int columns                             number of columns
	 * @return   String                                  the plate map
     */
	public String resultToPlateMapAsString(Map<WellBigInteger, BigInteger> data, 
			int rows, int columns) {
	    
	    TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
	    
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int rows                                      number of rows  
     * @param    int columns                                   number of columns
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigInteger, BigInteger>> data, int rows, int columns) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellBigInteger, BigInteger> map : data) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
	
    /**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int rows                                      number of rows  
     * @param    int columns                                   number of columns
     * @return   String                                        the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigInteger, BigInteger>> data, 
    		int rows, int columns) {
        
    	String result = "";
    	
        for(Map<WellBigInteger, BigInteger> map : data) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int type                                the plate type
     * @param    String label                            the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigInteger, BigInteger> data, int type, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
		
		this.printMapResult(sorted, rows, columns, label);
		
		this.flush();
	}
	
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int type                                the plate type
     * @param    String label                            the data set label
	 * @return   String                                  the plate map
     */
	public String resultToPlateMapAsString(Map<WellBigInteger, BigInteger> data, 
			int type, String label) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, label);
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int type                                      the plate type
     * @param    List<String> label                            list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigInteger, BigInteger>> data, int type, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int type                                      the plate type
     * @param    List<String> label                            list of data set labels
     * @return   String                                        the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigInteger, BigInteger>> data, 
    		int type, List<String> labels) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }

        return result;
    }
	
	/**
     * Prints the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int rows                          number of rows  
     * @param    int columns                       number of columns
     * @param    String label                      the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigInteger, BigInteger> data, int rows, int columns, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
	    
		this.printMapResult(sorted, rows, columns, label);	
		
		this.flush();
	}

	/**
     * Returns a string containing the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int rows                                number of rows  
     * @param    int columns                             number of columns
     * @param    String label                            the data set label
	 * @return   String                                  the plate map
     */
	public String resultToPlateMapAsString(Map<WellBigInteger, BigInteger> data, int rows, int columns, 
			String label) {
	    
	    TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
	    
	    return this.printMapResultAsString(sorted, rows, columns, label);	
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int rows                                      number of rows
     * @param    int columns                                   number of columns
     * @param    List<String> label                            list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigInteger, BigInteger>> data, int rows, int columns, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
	
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    int rows                                      number of rows
     * @param    int columns                                   number of columns
     * @param    List<String> label                            list of data set labels
     * @return   String                                        the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigInteger, BigInteger>> data, 
    		int rows, int columns, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int rows                                number of rows  
     * @param    int columns                             number of columns
     * @param    String label                            the data set label
     * @throws   FileNotFoundException 
     * @throws   UnsupportedEncodingException 
     */
	public void printMapResult(Map<WellBigInteger, BigInteger> data, int rows, 
			int columns, String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		this.println(label);
		this.print(delimiter);
		
		for(int k = 0; k < columns; k++) {
			this.print((k + 1) + delimiter);
		}
		
		this.println();
		
		for(int i = 0; i < rows; i++) {
			
			this.print(this.rowString(i) + delimiter);
			
			for(int j = 1; j < columns + 1; j++) {

				WellBigInteger well = new WellBigInteger(i, j);
				
				if(data.containsKey(well)) {
					this.print(data.get(well) + delimiter);
				} else {
					this.print("Null" + delimiter);
				}
			}
			
			this.println();
		}
		
		this.println();
	}
    
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    int rows                                number of rows  
     * @param    int columns                             number of columns
     * @param    String label                            the data set label
     * @return   String                                  the plate map
     */
	public String printMapResultAsString(Map<WellBigInteger, BigInteger> data, 
			int rows, int columns, String label) {
		
		String result = label + "\n";
		result += this.delimiter;

		for(int k = 0; k < columns; k++) {
			result += (k + 1) + delimiter;
		}
		
		result += "\n";
		
		for(int i = 0; i < rows; i++) {
			
			result += this.rowString(i) + delimiter;
			
			for(int j = 1; j < columns + 1; j++) {

				WellBigInteger well = new WellBigInteger(i, j);
				
				if(data.containsKey(well)) {
					result += data.get(well) + delimiter;
				} else {
					result += "Null" + delimiter;
				}
			}
			
			result += "\n";;
		}

		return result;
	}
	
	/*----------------------- Methods for Table Output -----------------------*/
	
	/**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellBigInteger, BigInteger> data) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
    	this.printTableResult(sorted, "Result");
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @return   String                                  the table
     */
    public String resultToTableAsString(Map<WellBigInteger, BigInteger> data) {
        TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
    	return this.printTableResultAsString(sorted, "Result");
    }
    
    /**
     * Prints each set of well value pairs as a delimiter separated table.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellBigInteger, BigInteger>> data) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellBigInteger, BigInteger> map : data) {
            
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(map);
            this.printTableResult(sorted, "Result");
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing each set of well value pairs as a delimiter 
     * separated table.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @return   String                                        the tables
     */
    public String resultToTableAsString(List<Map<WellBigInteger, BigInteger>> data) {
        
    	String result = "";
    	
        for(Map<WellBigInteger, BigInteger> map : data) {
            
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(map);
            result += this.printTableResultAsString(sorted, "Result");
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    String label                            the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellBigInteger, BigInteger> data, String label) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
        
    	this.printTableResult(sorted, label);
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    String label                            the data set label
     * @return   String                                  the table 
     */
    public String resultToTableAsString(Map<WellBigInteger, BigInteger> data, String label) {
        TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data);
    	return this.printTableResultAsString(sorted, label);
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    List<String> label                            list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellBigInteger, BigInteger>> data, List<String> labels) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            this.printTableResult(sorted, label);
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    List<Map<WellBigInteger, BigInteger>> data    list of data sets
     * @param    List<String> label                            list of data set labels
     * @return   String                                        the tables
     */
    public String resultToTableAsString(List<Map<WellBigInteger, BigInteger>> data, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellBigInteger, BigInteger> sorted = new TreeMap<WellBigInteger, BigInteger>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            
            result += this.printTableResultAsString(sorted, label);
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    String label                            the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void printTableResult(Map<WellBigInteger, BigInteger> data, String label) throws FileNotFoundException, UnsupportedEncodingException {
    	    	
    	this.println(label);
    	
    	this.println("Index" + this.delimiter + "Value");
    	
    	for (Map.Entry<WellBigInteger, BigInteger> entry : data.entrySet()) {
    	    this.println(entry.getKey().index() + this.delimiter + entry.getValue());
    	}
    	
    	this.println();
    }
    
    /**
     * Resturns a string holding the well value pairs as a delimiter separated table.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    String label                            the data set label
     * @return   String                                  the table string
     */
    public String printTableResultAsString(Map<WellBigInteger, BigInteger> data, String label) {
    	    	
    	String result = label + "\n";
    	
    	result += "Index" + this.delimiter + "Value\n";
    	
    	for (Map.Entry<WellBigInteger, BigInteger> entry : data.entrySet()) {
    	    result += entry.getKey().index() + this.delimiter + entry.getValue();
    	    result += "\n";
    	}
    	
    	result += "\n";
    	
    	return result;
    }
    
    /*------------------- Methods for Data Set JSON Output -------------------*/
                                                            
    /**
     * Prints the well value pairs in a JSON format.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @throws   IOException
     */
    public void resultToJSON(Map<WellBigInteger, BigInteger> data) throws IOException {  
          ObjectMapper mapper = new ObjectMapper();
          mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigInteger(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format.
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @return   String                                  the JSON formatted result
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(Map<WellBigInteger, BigInteger> data) throws JsonProcessingException {  
          ObjectMapper mapper = new ObjectMapper();
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigInteger(data));
    }
     
    /**
     * Prints the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellBigInteger, BigInteger>> data    the list of data sets
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellBigInteger, BigInteger>> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigInteger(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellBigInteger, BigInteger>> data    the list of data sets
     * @return   String                                        the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellBigInteger, BigInteger>> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigInteger(data));
    }
    
    /**
     * Prints the well value pairs in a JSON format using the label. 
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    String label                            the data set label
     * @throws   IOException
     */
    public void resultToJSON(Map<WellBigInteger, BigInteger> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigInteger(data, label));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format using the label. 
     * @param    Map<WellBigInteger, BigInteger> data    the data set
     * @param    String label                            the data set label
     * @return   String                                  the JSON formatted result
     */
    public String resultToJSONAsString(Map<WellBigInteger, BigInteger> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigInteger(data, label));
    }
    
    /**
     * Prints the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellBigInteger, BigInteger>> data    the list of data sets
     * @param    List<String> labels                           the list of data set labels
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellBigInteger, BigInteger>> data, List<String> labels) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigInteger(data, labels));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellBigInteger, BigInteger>> data    the list of data sets
     * @param    List<String> labels                           the list of data set labels
     * @return   String                                        the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellBigInteger, BigInteger>> data, List<String> labels) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigInteger(data, labels));
    }
    
    /*--------------------- Methods for Well JSON Output ---------------------*/
    
    /**
     * Prints the well values in a JSON format.
     * @param    WellBigInteger    the well
     * @throws   IOException 
     */
    public void wellToJSON(WellBigInteger well) throws IOException {   	
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOBigInteger(well));
    }
    
    /**
     * Returns a string containing the well values in a JSON format.
     * @param    WellBigInteger    the well
     * @return   String            the JSON formatted well
     * @throws  JsonProcessingException 
     */
    public String wellToJSONAsString(WellBigInteger well) throws JsonProcessingException {   	
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOBigInteger(well));
    }
    
    /**
     * Prints the collection of wells in a JSON format.
     * @param    Collection<WellBigInteger> collection    the collection of wells
     * @throws   IOException 
     */
    public void wellToJSON(Collection<WellBigInteger> collection) throws IOException {     
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOBigInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of wells in a JSON format.
     * @param    Collection<WellBigInteger> collection    the collection of wells
     * @return   String                                   the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(Collection<WellBigInteger> collection) throws JsonProcessingException {     
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOBigInteger(collection));
    }
    
    /**
     * Prints the wells values in the array in a JSON format.
     * @param    WellBigInteger[] array    the array of wells
     * @throws   IOException 
     */
    public void wellToJSON(WellBigInteger[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOBigInteger(array));
    }
    
    /**
     * Returns a string containing the well values in the array in a JSON format.
     * @param    WellBigInteger[] array    the array of wells
     * @return   String                    the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(WellBigInteger[] array) throws   JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOBigInteger(array));
    }
    
    /*------------------- Methods for Well Set JSON Output -------------------*/
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetBigInteger set    the well set
     * @throws   IOException 
     */
    public void setToJSON(WellSetBigInteger set) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOBigInteger(set));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetBigInteger set    the well set
     * @return   String                   the JSON formatted set
     * @throws   JsonProcessingException 
     */
    public String setToJSONAsString(WellSetBigInteger set) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOBigInteger(set));
    }
    
    /**
     * Prints the collection of well sets in a JSON format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @throws   IOException 
     */
    public void setToJSON(Collection<WellSetBigInteger> collection) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOBigInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of well sets in a JSON format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @return   String                                      the JSON formatted set
     * @throws   JsonProcessingException 
     */ 
    public String setToJSONAsString(Collection<WellSetBigInteger> collection) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOBigInteger(collection));
    }
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetBigInteger[] array    the array of well sets
     * @throws   IOException 
     */
    public void setToJSON(WellSetBigInteger[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOBigInteger(array));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetBigInteger[] array    the array of well sets
     * @return   String                   the JSON formatted set
     * @throws   JsonProcessingException  
     */
    public String setToJSONAsString(WellSetBigInteger[] array) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOBigInteger(array));
    }
    
    /*-------------------- Methods for Plate JSON Output ---------------------*/
    
    /**
     * Prints the plate values in a JSON format.
     * @param    PlateBigInteger plate    the plate
     * @throws   IOException 
     */
    public void plateToJSON(PlateBigInteger plate) throws IOException {    
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOBigInteger(plate));
    }
    
    /**
     * Returns a string containing the plate values in a JSON format.
     * @param    PlateBigInteger plate    the plate 
     * @return   String                   the JSON formatted plate
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateBigInteger plate) throws JsonProcessingException {    
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOBigInteger(plate));
    } 
    
    /**
     * Prints the collection of plates in a JSON format.
     * @param    Collection<PlateBigInteger> collection    the collection of plates
     * @throws   IOException 
     */
    public void plateToJSON(Collection<PlateBigInteger> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOBigInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of plates in a JSON format.
     * @param    Collection<PlateBigInteger> collection    the collection of plates
     * @return   String                                    the JSON formatted plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Collection<PlateBigInteger> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOBigInteger(collection));
    }
     
    /**
     * Prints the array of plates in a JSON format.
     * @param    PlateBigInteger[] array    the array of plates
     * @throws IOException 
     */
    public void plateToJSON(PlateBigInteger[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOBigInteger(array));
    }
    
    /**
     * Returns a string containing the array of plates in a JSON format.
     * @param    PlateBigInteger[] array    the array of plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateBigInteger[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOBigInteger(array));
    }
    
    /*-------------------- Methods for Stack JSON Output ---------------------*/
    
    /**
     * Prints the plate stack in a JSON format.
     * @param    StackBigInteger stack    the plate stack
     * @throws   IOException 
     */
    public void stackToJSON(StackBigInteger stack) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOBigInteger(stack));
    }
    
    /**
     * Returns a string containing the plate stack in a JSON format.
     * @param    StackBigInteger stack    the plate stack
     * @return   String                   the JSON formatted stack
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackBigInteger stack) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOBigInteger(stack));
    }
    
    /**
     * Prints the collection of plate stacks in a JSON format.
     * @param    Collection<StackBigInteger> collection    the collection of plate stacks
     * @throws   IOException 
     */
    public void stackToJSON(Collection<StackBigInteger> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOBigInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of plate stacks in a JSON format.
     * @param    Collection<StackBigInteger> collection    the collection of plate stacks
     * @return   String                                    the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Collection<StackBigInteger> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOBigInteger(collection));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackBigInteger[] array    the array of plate stacks
     * @throws IOException 
     */
    public void stackToJSON(StackBigInteger[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOBigInteger(array));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackBigInteger[] array    the array of plate stacks
     * @return   String                     the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackBigInteger[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOBigInteger(array));
    } 
    
    /*-------------------- Methods for Result XML Output --------------------*/
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellBigInteger, BigInteger> map    the result map
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellBigInteger, BigInteger> map) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(map);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellBigInteger, BigInteger> map    the result map
     * @return   String                                 the XML formatted result
     */
    public String resultToXMLAsString(Map<WellBigInteger, BigInteger> map) {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(map);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellBigInteger, BigInteger> map    the result map
     * @param    String label                           the label
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellBigInteger, BigInteger> map, String label) 
    		throws IOException, ParserConfigurationException, TransformerException {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(map, label);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellBigInteger, BigInteger> map    the result map
     * @param    String label                           the label
     * @return   String                                 the XML formatted result values
     */
    public String resultToXMLAsString(Map<WellBigInteger, BigInteger> map, String label) {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(map, label);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellBigInteger, BigInteger>> collection) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(collection);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @return   String                                      the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellBigInteger, BigInteger>> collection) {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(collection);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @param    List<String> labels                         result labels
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellBigInteger, BigInteger>> collection, List<String> labels) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(collection, labels);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @param    List<String> labels                         result labels
     * @return   String                                      the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellBigInteger, BigInteger>> collection, List<String> labels) {
    	ResultListXMLBigInteger resultList = new ResultListXMLBigInteger(collection, labels);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    ResultListXMLBigInteger resultList    the XML result list
     * @throws   IOException 
     */
    private void printXMLResult(ResultListXMLBigInteger resultList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(ResultListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Returns a string containing the result as an XML object.
     * @param    ResultListXMLBigInteger resultList    the XML result list
     * @return   String                                the XML formatted result list
     */
    private String printXMLResultAsString(ResultListXMLBigInteger resultList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(ResultListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}

    }
    
    /*--------------------- Methods for Well XML Output ----------------------*/
    
    /**
     * Prints the well values in an XML format.
     * @param    WellBigInteger set    the set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellBigInteger well) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLBigInteger wellList = new WellListXMLBigInteger(well);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the well values in an XML format.
     * @param    WellBigInteger set    the set
     * @return   String                the XML formatted well values
     */
    public String wellToXMLAsString(WellBigInteger well) {
    	WellListXMLBigInteger wellList = new WellListXMLBigInteger(well);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the collection of well values in an XML format.
     * @param    Collection<WellBigInteger> collection    the collection of wells
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void wellToXML(Collection<WellBigInteger> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLBigInteger wellList = new WellListXMLBigInteger(collection);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the collection of well values in an XML format.
     * @param    Collection<WellBigInteger> collection    the collection of wells
     * @return   String                                   the XML formatted wells
     */
    public String wellToXMLAsString(Collection<WellBigInteger> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLBigInteger wellList = new WellListXMLBigInteger(collection);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellBigInteger[] array    the array of wells
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellBigInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLBigInteger wellList = new WellListXMLBigInteger(array);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellBigInteger[] array    the array of wells
     * @return   String                    the XML formatted wells
     */
    public String wellToXMLAsString(WellBigInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLBigInteger wellList = new WellListXMLBigInteger(array);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well list as an XML object.
     * @param    WellListXMLBigInteger wellList    the well list
     * @throws   IOException 
     */
    private void printXMLWell(WellListXMLBigInteger wellList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well list as an XML object.
     * @param    WellListXMLBigInteger wellList    the well list
     * @return   String                            the XML formatted well list
     */
    private String printXMLWellAsString(WellListXMLBigInteger wellList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, writer);
			
			return writer.toString();
		
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    /*-------------------- Methods for Well Set XML Output --------------------*/
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSetBigInteger set    the well set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetBigInteger set) throws IOException, 
                                                    ParserConfigurationException, 
                                                    TransformerException {
    	WellSetListXMLBigInteger setList = new WellSetListXMLBigInteger(set);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetBigInteger set    the well set
     * @return   String                   the XML formatted well sets
     */
    public String setToXMLAsString(WellSetBigInteger set) {
    	WellSetListXMLBigInteger setList = new WellSetListXMLBigInteger(set);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the collection of well sets in an XML format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void setToXML(Collection<WellSetBigInteger> collection) throws IOException, 
                                                                       TransformerException, 
                                                                       ParserConfigurationException {
    	WellSetListXMLBigInteger setList = new WellSetListXMLBigInteger(collection);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the collection of well sets in an XML format.
     * @param    Collection<WellSetBigInteger> collection    the collection of well sets
     * @return   String                   the XML formatted well sets
     */
    public String setToXMLAsString(Collection<WellSetBigInteger> collection) {
    	WellSetListXMLBigInteger setList = new WellSetListXMLBigInteger(collection);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSetBigInteger[] array    the array of well sets
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetBigInteger[] array) throws IOException, 
                                                        ParserConfigurationException, 
                                                        TransformerException {
    	WellSetListXMLBigInteger setList = new WellSetListXMLBigInteger(array);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetBigInteger[] array    the array of well sets
     * @return   String                   the XML formatted well sets
     */
    public String setToXMLAsString(WellSetBigInteger[] array) {
    	WellSetListXMLBigInteger setList = new WellSetListXMLBigInteger(array);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    WellSetListXMLBigInteger setList    the well set list
     * @throws   IOException 
     */
    private void printXMLSet(WellSetListXMLBigInteger setList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well set as an XML object.
     * @param    WellSetListXMLBigInteger setList    the well set list
     * @return   String                              the XML formatted set list
     */
    private String printXMLSetAsString(WellSetListXMLBigInteger setList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    /*-------------------- Methods for Plate XML Output ---------------------*/
    
    /**
     * Prints the plate values in an XML format.
     * @param    PlateBigInteger plate    the plate
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(PlateBigInteger plate) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLBigInteger plateList = new PlateListXMLBigInteger(plate);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the plate values in an XML format.
     * @param    PlateBigInteger plate    the plate
     * @return   String                   the XML formatted plates
     */
    public String plateToXMLAsString(PlateBigInteger plate) {
    	PlateListXMLBigInteger plateList = new PlateListXMLBigInteger(plate);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the collection of plates in an XML format.
     * @param    Collection<PlateBigInteger> collection    the collection of plates
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(Collection<PlateBigInteger> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLBigInteger plateList = new PlateListXMLBigInteger(collection);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the collection of plates in an XML format.
     * @param    Collection<PlateBigInteger> collection    the collection of plates
     * @return   String                                    the XML formatted plates 
     */
    public String plateToXMLAsString(Collection<PlateBigInteger> collection) {
    	PlateListXMLBigInteger plateList = new PlateListXMLBigInteger(collection);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the array of plates in an XML format.
     * @param    PlateBigInteger[] array    the array of plates
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void plateToXML(PlateBigInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	PlateListXMLBigInteger plateList = new PlateListXMLBigInteger(array);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the array of plates in an XML format.
     * @param    PlateBigInteger[] array    the array of plates
     * @return   String                     the XML formatted plates
     */
    public String plateToXMLAsString(PlateBigInteger[] array) {
    	PlateListXMLBigInteger plateList = new PlateListXMLBigInteger(array);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLBigInteger plateList    the plate list
     * @throws   IOException 
     */
    private void printXMLPlate(PlateListXMLBigInteger plateList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(PlateListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLBigInteger plateList    the plate list
     * @return   String                              the XML formatted plate list
     */
    private String printXMLPlateAsString(PlateListXMLBigInteger plateList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(PlateListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
        
    }
    
    /*-------------------- Methods for Stack XML Output ---------------------*/
    
    /**
     * Prints the plate stack in an XML format.
     * @param    StackBigInteger stack    the plate stack
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void stackToXML(StackBigInteger stack) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	StackListXMLBigInteger stackList = new StackListXMLBigInteger(stack);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the plate stack in an XML format.
     * @param    StackBigInteger stack    the plate stack
     * @return   String                     the XML formatted stacks 
     */
    public String stackToXMLAsString(StackBigInteger stack) {
    	StackListXMLBigInteger stackList = new StackListXMLBigInteger(stack);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the collection of plate stacks in an XML format.
     * @param    Collection<StackBigInteger> collection    the collection of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(Collection<StackBigInteger> collection) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLBigInteger stackList = new StackListXMLBigInteger(collection);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the collection of plate stacks in an XML format.
     * @param    Collection<StackBigInteger> collection    the collection of plate stacks
     * @return   String                                    the XML formatted stacks
     */
    public String stackToXMLAsString(Collection<StackBigInteger> collection) {
    	StackListXMLBigInteger stackList = new StackListXMLBigInteger(collection);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the array of plate stacks in an XML format.
     * @param    StackBigInteger[] array    the array of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(StackBigInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLBigInteger stackList = new StackListXMLBigInteger(array);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the array of plate stacks in an XML format.
     * @param    StackBigInteger[] array    the array of plate stacks
     * @return   String                     the XML formatted stacks
     */
    public String stackToXMLAsString(StackBigInteger[] array) {
    	StackListXMLBigInteger stackList = new StackListXMLBigInteger(array);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the stack as an XML object.
     * @param    StackListXMLBigInteger stackList    the stack list
     * @throws   IOException 
     * @throws   TransformerException 
     */
    public void printXMLStack(StackListXMLBigInteger stackList) throws IOException, 
    		TransformerException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(StackListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the stack list as an XML object.
     * @param    StackListXMLBigInteger stackList    the stack list
     * @return   String                              the XML formatted stack list
     */
    public String printXMLStackAsString(StackListXMLBigInteger stackList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(StackListXMLBigInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, writer);
			
			return writer.toString();
			
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    /*-------------------- Methods for Setting Delimiter ---------------------*/
    
    /**
     * Sets the delimiter for the plate writer.
     * @param    String delimiter    the delimiter
     */
    public void setDelimiter(String delimiter) {
    	this.delimiter = delimiter;
    }
    
    /**
     * Returns the delimiter.
     * @return    String    the delimiter
     */
    public String getDelimiter() {
    	return this.delimiter;
    }
    
    /*----------------------- Helper Methods for Output ----------------------*/
    
    /**
     * Returns the row ID.
     * @return    String    row ID
     */
    private String rowString(int row) {

        String rowString = "";
        
        while (row >=  0) {
            rowString = (char) (row % ALPHA_BASE + 65) + rowString;
            row = (row  / ALPHA_BASE) - 1;
        }
        
        return rowString;
    }
	
    /**
     * Returns the row number for the plate type.
     * @param    int type    the plate type
     * @return   int         the number of plate rows
     */
	private int parseRows(int type) {
		
		switch(type) {
		
		    case PlateBigInteger.PLATE_6WELL: return PlateBigInteger.ROWS_6WELL;
		
		    case PlateBigInteger.PLATE_12WELL: return PlateBigInteger.ROWS_12WELL;
		    
		    case PlateBigInteger.PLATE_24WELL: return PlateBigInteger.ROWS_24WELL;
		    
		    case PlateBigInteger.PLATE_48WELL: return PlateBigInteger.ROWS_48WELL;
		    
		    case PlateBigInteger.PLATE_96WELL: return PlateBigInteger.ROWS_96WELL;
		    
		    case PlateBigInteger.PLATE_384WELL: return PlateBigInteger.ROWS_384WELL;
		    
		    case PlateBigInteger.PLATE_1536WELL: return PlateBigInteger.ROWS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}
	
	/**
     * Returns the column number for the plate type.
     * @param    int type    the plate type
     * @return   int         the number of plate columns
     */
    private int parseColumns(int type) {
		
		switch(type) {
		
		    case PlateBigInteger.PLATE_6WELL: return PlateBigInteger.COLUMNS_6WELL;
		
		    case PlateBigInteger.PLATE_12WELL: return PlateBigInteger.COLUMNS_12WELL;
		    
		    case PlateBigInteger.PLATE_24WELL: return PlateBigInteger.COLUMNS_24WELL;
		    
		    case PlateBigInteger.PLATE_48WELL: return PlateBigInteger.COLUMNS_48WELL;
		    
		    case PlateBigInteger.PLATE_96WELL: return PlateBigInteger.COLUMNS_96WELL;
		    
		    case PlateBigInteger.PLATE_384WELL: return PlateBigInteger.COLUMNS_384WELL;
		    
		    case PlateBigInteger.PLATE_1536WELL: return PlateBigInteger.COLUMNS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}

}
