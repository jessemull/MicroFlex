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

package com.github.jessemull.microflex.integerflex.io;

/*------------------------------- Dependencies -------------------------------*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;

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
public class PlateWriterInteger extends PrintWriter {
	
	/*---------------------------- Private Fields ----------------------------*/
	
	/* Number of character types available for the row ID */

	private int ALPHA_BASE = 26;
    
    /* The delimiter for delimiter separated values */
    
    private String delimiter = "\t";
    
    /*------------------------------ Constructors ----------------------------*/
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using 
     * the specified file.
     * @param    File    the output file
     * @throws   FileNotFoundException 
     */
    public PlateWriterInteger(File file) throws FileNotFoundException {
    	super(file);
    }
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using 
     * the specified file and character set.
     * @param    File      the output file
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterInteger(File file, String csn) throws FileNotFoundException, 
                                                            UnsupportedEncodingException {
    	super(file, csn);
    }

    /**
     * Creates a new PlateWriterInteger, without automatic line flushing using
     * the OutputStream.
     * @param    OutputStream    the output stream
     */
    public PlateWriterInteger(OutputStream out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterInteger with automatic line flushing, using the 
     * OutputStream.
     * @param    OutputStream    the output stream
     * @param    boolean         sets automatic flush when true
     */
    public PlateWriterInteger(OutputStream out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using the
     * specified file name.
     * @param    String    the file name
     * @throws   FileNotFoundException 
     */
    public PlateWriterInteger(String fileName) throws FileNotFoundException {
    	super(fileName);
    }
    
    /**
     * Creates a new PlateWriterInteger without automatic line flushing using the
     * specified file name and character set.
     * @param    String    the file name
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterInteger(String fileName, String csn) throws FileNotFoundException, 
                                                                  UnsupportedEncodingException {
    	super(fileName, csn);
    }

    /**
     * Creates a new PlateWriterInteger without automatic line flushing using the
     * writer.
     * @param    Writer    the writer
     */
    public PlateWriterInteger(Writer out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterInteger with automatic line flushing using the
     * writer.
     * @param    Writer     the writer
     * @param    boolean    sets auto flush when true
     */
    public PlateWriterInteger(Writer out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellInteger, Integer> data, int type) 
			throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
		
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
     */
	public String resultToPlateMapAsString(Map<WellInteger, Integer> data, int type) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellInteger, Integer>> data, 
    		int type) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellInteger, Integer> map : data) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
    
	/**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellInteger, Integer>> data, int type) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellInteger, Integer> map : data) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
	
    
    
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellInteger, Integer> data, int rows, int columns) 
			throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
	    
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
	 * @return                                   the plate map
     */
	public String resultToPlateMapAsString(Map<WellInteger, Integer> data, 
			int rows, int columns) {
	    
	    TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
	    
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellInteger, Integer>> data, int rows, int columns) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellInteger, Integer> map : data) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
	
    /**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellInteger, Integer>> data, 
    		int rows, int columns) {
        
    	String result = "";
    	
        for(Map<WellInteger, Integer> map : data) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
     * @param    String                       the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellInteger, Integer> data, int type, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
		
		this.printMapResult(sorted, rows, columns, label);
		
		this.flush();
	}
	
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          the plate type
     * @param    String                       the data set label
	 * @return                                   the plate map
     */
	public String resultToPlateMapAsString(Map<WellInteger, Integer> data, 
			int type, String label) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, label);
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
     * @param    List<String>                       list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellInteger, Integer>> data, int type, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                the plate type
     * @param    List<String>                       list of data set labels
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellInteger, Integer>> data, 
    		int type, List<String> labels) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }

        return result;
    }
	
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellInteger, Integer> data, int rows, int columns, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
	    
		this.printMapResult(sorted, rows, columns, label);	
		
		this.flush();
	}

	/**
     * Returns a string containing the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
	 * @return                                   the plate map
     */
	public String resultToPlateMapAsString(Map<WellInteger, Integer> data, int rows, int columns, 
			String label) {
	    
	    TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
	    
	    return this.printMapResultAsString(sorted, rows, columns, label);	
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    List<String>                       list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellInteger, Integer>> data, int rows, int columns, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
	
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    List<String>                       list of data set labels
     * @return                                      the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellInteger, Integer>> data, 
    		int rows, int columns, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
     * @throws   FileNotFoundException 
     * @throws   UnsupportedEncodingException 
     */
	public void printMapResult(Map<WellInteger, Integer> data, int rows, 
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

				WellInteger well = new WellInteger(i, j);
				
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
     * @param    Map<WellInteger, Integer>    the data set
     * @param    int                          number of rows
     * @param    int                          number of columns
     * @param    String                       the data set label
     * @return                                the plate map
     */
	public String printMapResultAsString(Map<WellInteger, Integer> data, 
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

				WellInteger well = new WellInteger(i, j);
				
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
     * @param    Map<WellInteger, Integer>    the data set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellInteger, Integer> data) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
    	this.printTableResult(sorted, "Result");
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellInteger, Integer>    the data set
     * @return                                the table
     */
    public String resultToTableAsString(Map<WellInteger, Integer> data) {
        TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
    	return this.printTableResultAsString(sorted, "Result");
    }
    
    /**
     * Prints each set of well value pairs as a delimiter separated table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellInteger, Integer>> data) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellInteger, Integer> map : data) {
            
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(map);
            this.printTableResult(sorted, "Result");
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing each set of well value pairs as a delimiter 
     * separated table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @return                                      the tables
     */
    public String resultToTableAsString(List<Map<WellInteger, Integer>> data) {
        
    	String result = "";
    	
        for(Map<WellInteger, Integer> map : data) {
            
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(map);
            result += this.printTableResultAsString(sorted, "Result");
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellInteger, Integer> data, String label) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
        
    	this.printTableResult(sorted, label);
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @return                                the table 
     */
    public String resultToTableAsString(Map<WellInteger, Integer> data, String label) {
        TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data);
    	return this.printTableResultAsString(sorted, label);
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    List<String>                       list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellInteger, Integer>> data, List<String> labels) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            this.printTableResult(sorted, label);
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    List<Map<WellInteger, Integer>>    list of data sets
     * @param    List<String>                       list of data set labels
     * @return                                      the tables
     */
    public String resultToTableAsString(List<Map<WellInteger, Integer>> data, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellInteger, Integer> sorted = new TreeMap<WellInteger, Integer>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            
            result += this.printTableResultAsString(sorted, label);
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void printTableResult(Map<WellInteger, Integer> data, String label) throws FileNotFoundException, UnsupportedEncodingException {
    	    	
    	this.println(label);
    	
    	this.println("Index" + this.delimiter + "Value");
    	
    	for (Map.Entry<WellInteger, Integer> entry : data.entrySet()) {
    	    this.println(entry.getKey().index() + this.delimiter + entry.getValue());
    	}
    	
    	this.println();
    }
    
    /**
     * Resturns a string holding the well value pairs as a delimiter separated table.
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @return                                the table string
     */
    public String printTableResultAsString(Map<WellInteger, Integer> data, String label) {
    	    	
    	String result = label + "\n";
    	
    	result += "Index" + this.delimiter + "Value\n";
    	
    	for (Map.Entry<WellInteger, Integer> entry : data.entrySet()) {
    	    result += entry.getKey().index() + this.delimiter + entry.getValue();
    	    result += "\n";
    	}
    	
    	result += "\n";
    	
    	return result;
    }
    
    /*------------------- Methods for Data Set JSON Output -------------------*/
                                                            
    /**
     * Prints the well value pairs in a JSON format.
     * @param    Map<WellInteger, Integer>    the data set
     * @throws   IOException
     */
    public void resultToJSON(Map<WellInteger, Integer> data) throws IOException {  
          ObjectMapper mapper = new ObjectMapper();
          mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOInteger(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format.
     * @param    Map<WellInteger, Integer>    the data set
     * @return                                the JSON formatted result
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(Map<WellInteger, Integer> data) throws JsonProcessingException {  
          ObjectMapper mapper = new ObjectMapper();
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOInteger(data));
    }
     
    /**
     * Prints the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellInteger, Integer>> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOInteger(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @return                                      the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellInteger, Integer>> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOInteger(data));
    }
    
    /**
     * Prints the well value pairs in a JSON format using the label. 
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @throws   IOException
     */
    public void resultToJSON(Map<WellInteger, Integer> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOInteger(data, label));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format using the label. 
     * @param    Map<WellInteger, Integer>    the data set
     * @param    String                       the data set label
     * @return                                the JSON formatted result
     */
    public String resultToJSONAsString(Map<WellInteger, Integer> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOInteger(data, label));
    }
    
    /**
     * Prints the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @param    List<String>                       the list of data set labels
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellInteger, Integer>> data, List<String> labels) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOInteger(data, labels));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellInteger, Integer>>    the list of data sets
     * @param    List<String>                       the list of data set labels
     * @return                                      the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellInteger, Integer>> data, List<String> labels) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOInteger(data, labels));
    }
    
    /*--------------------- Methods for Well JSON Output ---------------------*/
    
    /**
     * Prints the well values in a JSON format.
     * @param    WellInteger    the well
     * @throws   IOException 
     */
    public void wellToJSON(WellInteger well) throws IOException {   	
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOInteger(well));
    }
    
    /**
     * Returns a string containing the well values in a JSON format.
     * @param    WellInteger    the well
     * @return                  the JSON formatted well
     * @throws  JsonProcessingException 
     */
    public String wellToJSONAsString(WellInteger well) throws JsonProcessingException {   	
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOInteger(well));
    }
    
    /**
     * Prints the collection of wells in a JSON format.
     * @param    Collection<WellInteger>    the collection of wells
     * @throws   IOException 
     */
    public void wellToJSON(Collection<WellInteger> collection) throws IOException {     
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of wells in a JSON format.
     * @param    Collection<WellInteger>    the collection of wells
     * @return                              the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(Collection<WellInteger> collection) throws JsonProcessingException {     
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOInteger(collection));
    }
    
    /**
     * Prints the wells values in the array in a JSON format.
     * @param    WellInteger[]    the array of wells
     * @throws   IOException 
     */
    public void wellToJSON(WellInteger[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOInteger(array));
    }
    
    /**
     * Returns a string containing the well values in the array in a JSON format.
     * @param    WellInteger[]    the array of wells
     * @return                    the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(WellInteger[] array) throws   JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOInteger(array));
    }
    
    /*------------------- Methods for Well Set JSON Output -------------------*/
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetInteger    the well set
     * @throws   IOException 
     */
    public void setToJSON(WellSetInteger set) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOInteger(set));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetInteger    the well set
     * @return                     the JSON formatted set
     * @throws   JsonProcessingException 
     */
    public String setToJSONAsString(WellSetInteger set) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOInteger(set));
    }
    
    /**
     * Prints the collection of well sets in a JSON format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @throws   IOException 
     */
    public void setToJSON(Collection<WellSetInteger> collection) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of well sets in a JSON format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @return                                 the JSON formatted set
     * @throws   JsonProcessingException 
     */ 
    public String setToJSONAsString(Collection<WellSetInteger> collection) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOInteger(collection));
    }
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetInteger[]    the array of well sets
     * @throws   IOException 
     */
    public void setToJSON(WellSetInteger[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOInteger(array));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetInteger[]    the array of well sets
     * @return                       the JSON formatted set
     * @throws   JsonProcessingException  
     */
    public String setToJSONAsString(WellSetInteger[] array) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOInteger(array));
    }
    
    /*-------------------- Methods for Plate JSON Output ---------------------*/
    
    /**
     * Prints the plate values in a JSON format.
     * @param    PlateInteger    the plate
     * @throws   IOException 
     */
    public void plateToJSON(PlateInteger plate) throws IOException {    
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOInteger(plate));
    }
    
    /**
     * Returns a string containing the plate values in a JSON format.
     * @param    PlateInteger    the plate
     * @return                   the JSON formatted plate
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateInteger plate) throws JsonProcessingException {    
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOInteger(plate));
    } 
    
    /**
     * Prints the collection of plates in a JSON format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @throws   IOException 
     */
    public void plateToJSON(Collection<PlateInteger> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of plates in a JSON format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @return                               the JSON formatted plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Collection<PlateInteger> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOInteger(collection));
    }
     
    /**
     * Prints the array of plates in a JSON format.
     * @param    PlateInteger[]    the array of plates
     * @throws IOException 
     */
    public void plateToJSON(PlateInteger[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOInteger(array));
    }
    
    /**
     * Returns a string containing the array of plates in a JSON format.
     * @param    PlateInteger[]    the array of plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateInteger[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOInteger(array));
    }
    
    /*-------------------- Methods for Stack JSON Output ---------------------*/
    
    /**
     * Prints the plate stack in a JSON format.
     * @param    StackInteger    the plate stack
     * @throws   IOException 
     */
    public void stackToJSON(StackInteger stack) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOInteger(stack));
    }
    
    /**
     * Returns a string containing the plate stack in a JSON format.
     * @param    StackInteger    the plate stack
     * @return                   the JSON formatted stack
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackInteger stack) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOInteger(stack));
    }
    
    /**
     * Prints the collection of plate stacks in a JSON format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @throws   IOException 
     */
    public void stackToJSON(Collection<StackInteger> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOInteger(collection));
    }
    
    /**
     * Returns a string containing the collection of plate stacks in a JSON format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @return                               the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Collection<StackInteger> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOInteger(collection));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackInteger[]    the array of plate stacks
     * @throws IOException 
     */
    public void stackToJSON(StackInteger[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOInteger(array));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackInteger[]    the array of plate stacks
     * @return                     the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackInteger[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOInteger(array));
    } 
    
    /*-------------------- Methods for Result XML Output --------------------*/
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellInteger, Integer> map) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(map);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @return                                the XML formatted result
     */
    public String resultToXMLAsString(Map<WellInteger, Integer> map) {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(map);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @param    String                       the label
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellInteger, Integer> map, String label) 
    		throws IOException, ParserConfigurationException, TransformerException {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(map, label);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellInteger, Integer>    the result map
     * @param    String                       the label
     * @return                                the XML formatted result values
     */
    public String resultToXMLAsString(Map<WellInteger, Integer> map, String label) {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(map, label);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellInteger, Integer>> collection) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(collection);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @return                                 the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellInteger, Integer>> collection) {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(collection);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @param    List<String>                  result labels
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellInteger, Integer>> collection, List<String> labels) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(collection, labels);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @param    List<String>                  result labels
     * @return                                 the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellInteger, Integer>> collection, List<String> labels) {
    	ResultListXMLInteger resultList = new ResultListXMLInteger(collection, labels);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    ResultListXMLInteger    the XML result list
     * @throws   IOException 
     */
    private void printXMLResult(ResultListXMLInteger resultList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(ResultListXMLInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Returns a string containing the result as an XML object.
     * @param    ResultListXMLInteger    the XML result list
     * @return                           the XML formatted result list
     */
    private String printXMLResultAsString(ResultListXMLInteger resultList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(ResultListXMLInteger.class);
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
     * @param    WellInteger    the set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellInteger well) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLInteger wellList = new WellListXMLInteger(well);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the well values in an XML format.
     * @param    WellInteger    the set
     * @return                  the XML formatted well values
     */
    public String wellToXMLAsString(WellInteger well) {
    	WellListXMLInteger wellList = new WellListXMLInteger(well);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the collection of well values in an XML format.
     * @param    Collection<WellInteger>    the collection of wells
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void wellToXML(Collection<WellInteger> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLInteger wellList = new WellListXMLInteger(collection);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the collection of well values in an XML format.
     * @param    Collection<WellInteger>    the collection of wells
     * @return                              the XML formatted wells
     */
    public String wellToXMLAsString(Collection<WellInteger> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLInteger wellList = new WellListXMLInteger(collection);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellInteger[]    the array of wells
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLInteger wellList = new WellListXMLInteger(array);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellInteger[]    the array of wells
     * @return                    the XML formatted wells
     */
    public String wellToXMLAsString(WellInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLInteger wellList = new WellListXMLInteger(array);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well list as an XML object.
     * @param    WellListXMLInteger    the well list
     * @throws   IOException 
     */
    private void printXMLWell(WellListXMLInteger wellList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellListXMLInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well list as an XML object.
     * @param    WellListXMLInteger    the well list
     * @return                         the XML formatted well list
     */
    private String printXMLWellAsString(WellListXMLInteger wellList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellListXMLInteger.class);
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
     * @param    WellSetInteger    the well set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetInteger set) throws IOException, 
                                                    ParserConfigurationException, 
                                                    TransformerException {
    	WellSetListXMLInteger setList = new WellSetListXMLInteger(set);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetInteger    the well set
     * @return                     the XML formatted well sets
     */
    public String setToXMLAsString(WellSetInteger set) {
    	WellSetListXMLInteger setList = new WellSetListXMLInteger(set);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the collection of well sets in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void setToXML(Collection<WellSetInteger> collection) throws IOException, 
                                                                       TransformerException, 
                                                                       ParserConfigurationException {
    	WellSetListXMLInteger setList = new WellSetListXMLInteger(collection);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the collection of well sets in an XML format.
     * @param    Collection<WellSetInteger>    the collection of well sets
     * @return                                 the XML formatted well sets
     */
    public String setToXMLAsString(Collection<WellSetInteger> collection) {
    	WellSetListXMLInteger setList = new WellSetListXMLInteger(collection);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSetInteger[]    the array of well sets
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetInteger[] array) throws IOException, 
                                                        ParserConfigurationException, 
                                                        TransformerException {
    	WellSetListXMLInteger setList = new WellSetListXMLInteger(array);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetInteger[]    the array of well sets
     * @return                       the XML formatted well sets
     */
    public String setToXMLAsString(WellSetInteger[] array) {
    	WellSetListXMLInteger setList = new WellSetListXMLInteger(array);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    WellSetListXMLInteger    the well set list
     * @throws   IOException 
     */
    private void printXMLSet(WellSetListXMLInteger setList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well set as an XML object.
     * @param    WellSetListXMLInteger    the well set list
     * @return                            the XML formatted set list
     */
    private String printXMLSetAsString(WellSetListXMLInteger setList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLInteger.class);
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
     * @param    PlateInteger    the plate
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(PlateInteger plate) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLInteger plateList = new PlateListXMLInteger(plate);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the plate values in an XML format.
     * @param    PlateInteger    the plate
     * @return                   the XML formatted plates
     */
    public String plateToXMLAsString(PlateInteger plate) {
    	PlateListXMLInteger plateList = new PlateListXMLInteger(plate);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the collection of plates in an XML format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(Collection<PlateInteger> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLInteger plateList = new PlateListXMLInteger(collection);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the collection of plates in an XML format.
     * @param    Collection<PlateInteger>    the collection of plates
     * @return                               the XML formatted plates 
     */
    public String plateToXMLAsString(Collection<PlateInteger> collection) {
    	PlateListXMLInteger plateList = new PlateListXMLInteger(collection);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the array of plates in an XML format.
     * @param    PlateInteger[]    the array of plates
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void plateToXML(PlateInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	PlateListXMLInteger plateList = new PlateListXMLInteger(array);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the array of plates in an XML format.
     * @param    PlateInteger[]    the array of plates
     * @return                     the XML formatted plates
     */
    public String plateToXMLAsString(PlateInteger[] array) {
    	PlateListXMLInteger plateList = new PlateListXMLInteger(array);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLInteger    the plate list
     * @throws   IOException 
     */
    private void printXMLPlate(PlateListXMLInteger plateList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(PlateListXMLInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLInteger    the plate list
     * @return                          the XML formatted plate list
     */
    private String printXMLPlateAsString(PlateListXMLInteger plateList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(PlateListXMLInteger.class);
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
     * @param    StackInteger    the plate stack
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void stackToXML(StackInteger stack) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	StackListXMLInteger stackList = new StackListXMLInteger(stack);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the plate stack in an XML format.
     * @param    StackInteger    the plate stack
     * @return                   the XML formatted stacks 
     */
    public String stackToXMLAsString(StackInteger stack) {
    	StackListXMLInteger stackList = new StackListXMLInteger(stack);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the collection of plate stacks in an XML format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(Collection<StackInteger> collection) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLInteger stackList = new StackListXMLInteger(collection);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the collection of plate stacks in an XML format.
     * @param    Collection<StackInteger>    the collection of plate stacks
     * @return                               the XML formatted stacks
     */
    public String stackToXMLAsString(Collection<StackInteger> collection) {
    	StackListXMLInteger stackList = new StackListXMLInteger(collection);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the array of plate stacks in an XML format.
     * @param    StackInteger[]    the array of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(StackInteger[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLInteger stackList = new StackListXMLInteger(array);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the array of plate stacks in an XML format.
     * @param    StackInteger[]    the array of plate stacks
     * @return                     the XML formatted stacks
     */
    public String stackToXMLAsString(StackInteger[] array) {
    	StackListXMLInteger stackList = new StackListXMLInteger(array);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the stack as an XML object.
     * @param    StackListXMLInteger    the stack list
     * @throws   IOException 
     * @throws   TransformerException 
     */
    public void printXMLStack(StackListXMLInteger stackList) throws IOException, 
    		TransformerException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(StackListXMLInteger.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the stack list as an XML object.
     * @param    StackListXMLInteger    the stack list
     * @return                          the XML formatted stack list
     */
    public String printXMLStackAsString(StackListXMLInteger stackList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(StackListXMLInteger.class);
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
     * @param    String    the delimiter
     */
    public void setDelimiter(String delimiter) {
    	this.delimiter = delimiter;
    }
    
    /**
     * Returns the delimiter.
     * @return    the delimiter
     */
    public String getDelimiter() {
    	return this.delimiter;
    }
    
    /*----------------------- Helper Methods for Output ----------------------*/
    
    /**
     * Returns the row ID.
     * @return    row ID
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
     * @param    int    the plate type
     * @return          the number of plate rows
     */
	private int parseRows(int type) {
		
		switch(type) {
		
		    case PlateInteger.PLATE_6WELL: return PlateInteger.ROWS_6WELL;
		
		    case PlateInteger.PLATE_12WELL: return PlateInteger.ROWS_12WELL;
		    
		    case PlateInteger.PLATE_24WELL: return PlateInteger.ROWS_24WELL;
		    
		    case PlateInteger.PLATE_48WELL: return PlateInteger.ROWS_48WELL;
		    
		    case PlateInteger.PLATE_96WELL: return PlateInteger.ROWS_96WELL;
		    
		    case PlateInteger.PLATE_384WELL: return PlateInteger.ROWS_384WELL;
		    
		    case PlateInteger.PLATE_1536WELL: return PlateInteger.ROWS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}
	
	/**
     * Returns the column number for the plate type.
     * @param    int    the plate type
     * @return          the number of plate columns
     */
    private int parseColumns(int type) {
		
		switch(type) {
		
		    case PlateInteger.PLATE_6WELL: return PlateInteger.COLUMNS_6WELL;
		
		    case PlateInteger.PLATE_12WELL: return PlateInteger.COLUMNS_12WELL;
		    
		    case PlateInteger.PLATE_24WELL: return PlateInteger.COLUMNS_24WELL;
		    
		    case PlateInteger.PLATE_48WELL: return PlateInteger.COLUMNS_48WELL;
		    
		    case PlateInteger.PLATE_96WELL: return PlateInteger.COLUMNS_96WELL;
		    
		    case PlateInteger.PLATE_384WELL: return PlateInteger.COLUMNS_384WELL;
		    
		    case PlateInteger.PLATE_1536WELL: return PlateInteger.COLUMNS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}

}
