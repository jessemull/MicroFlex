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

import com.github.jessemull.microflex.doubleflex.plate.PlateDouble;
import com.github.jessemull.microflex.doubleflex.plate.StackDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellDouble;
import com.github.jessemull.microflex.doubleflex.plate.WellSetDouble;

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
public class PlateWriterDouble extends PrintWriter {
	
	/*---------------------------- Private Fields ----------------------------*/
	
	/* Number of character types available for the row ID */

	private int ALPHA_BASE = 26;
    
    /* The delimiter for delimiter separated values */
    
    private String delimiter = "\t";
    
    /*------------------------------ Constructors ----------------------------*/
    
    /**
     * Creates a new PlateWriterDouble without automatic line flushing using 
     * the specified file.
     * @param    File    the output file
     * @throws   FileNotFoundException 
     */
    public PlateWriterDouble(File file) throws FileNotFoundException {
    	super(file);
    }
    
    /**
     * Creates a new PlateWriterDouble without automatic line flushing using 
     * the specified file and character set.
     * @param    File      the output file
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterDouble(File file, String csn) throws FileNotFoundException, 
                                                            UnsupportedEncodingException {
    	super(file, csn);
    }

    /**
     * Creates a new PlateWriterDouble, without automatic line flushing using
     * the OutputStream.
     * @param    OutputStream    the output stream
     */
    public PlateWriterDouble(OutputStream out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterDouble with automatic line flushing, using the 
     * OutputStream.
     * @param    OutputStream    the output stream
     * @param    boolean         sets automatic flush when true
     */
    public PlateWriterDouble(OutputStream out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /**
     * Creates a new PlateWriterDouble without automatic line flushing using the
     * specified file name.
     * @param    String    the file name
     * @throws   FileNotFoundException 
     */
    public PlateWriterDouble(String fileName) throws FileNotFoundException {
    	super(fileName);
    }
    
    /**
     * Creates a new PlateWriterDouble without automatic line flushing using the
     * specified file name and character set.
     * @param    String    the file name
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterDouble(String fileName, String csn) throws FileNotFoundException, 
                                                                  UnsupportedEncodingException {
    	super(fileName, csn);
    }

    /**
     * Creates a new PlateWriterDouble without automatic line flushing using the
     * writer.
     * @param    Writer    the writer
     */
    public PlateWriterDouble(Writer out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterDouble with automatic line flushing using the
     * writer.
     * @param    Writer     the writer
     * @param    boolean    sets auto flush when true
     */
    public PlateWriterDouble(Writer out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Prints the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellDouble, Double> data, int type) 
			throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
		
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        the plate type
     */
	public String resultToPlateMapAsString(Map<WellDouble, Double> data, int type) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellDouble, Double>> data, 
    		int type) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellDouble, Double> map : data) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
    
	/**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              the plate type
     * @return                                    the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellDouble, Double>> data, int type) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellDouble, Double> map : data) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
	
    
    
	/**
     * Prints the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        number of rows
     * @param    int                        number of columns
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellDouble, Double> data, int rows, int columns) 
			throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
	    
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        number of rows
     * @param    int                        number of columns
	 * @return                                 the plate map
     */
	public String resultToPlateMapAsString(Map<WellDouble, Double> data, 
			int rows, int columns) {
	    
	    TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
	    
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              number of rows
     * @param    int                              number of columns
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellDouble, Double>> data, int rows, int columns) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellDouble, Double> map : data) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
	
    /**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              number of rows
     * @param    int                              number of columns
     * @return                                    the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellDouble, Double>> data, 
    		int rows, int columns) {
        
    	String result = "";
    	
        for(Map<WellDouble, Double> map : data) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        the plate type
     * @param    String                     the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellDouble, Double> data, int type, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
		
		this.printMapResult(sorted, rows, columns, label);
		
		this.flush();
	}
	
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        the plate type
     * @param    String                     the data set label
	 * @return                                 the plate map
     */
	public String resultToPlateMapAsString(Map<WellDouble, Double> data, 
			int type, String label) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, label);
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              the plate type
     * @param    List<String>                     list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellDouble, Double>> data, int type, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              the plate type
     * @param    List<String>                     list of data set labels
     * @return                                    the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellDouble, Double>> data, 
    		int type, List<String> labels) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }

        return result;
    }
	
	/**
     * Prints the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        number of rows
     * @param    int                        number of columns
     * @param    String                     the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellDouble, Double> data, int rows, int columns, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
	    
		this.printMapResult(sorted, rows, columns, label);	
		
		this.flush();
	}

	/**
     * Returns a string containing the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        number of rows
     * @param    int                        number of columns
     * @param    String                     the data set label
	 * @return                                 the plate map
     */
	public String resultToPlateMapAsString(Map<WellDouble, Double> data, int rows, int columns, 
			String label) {
	    
	    TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
	    
	    return this.printMapResultAsString(sorted, rows, columns, label);	
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              number of rows
     * @param    int                              number of columns
     * @param    List<String>                     list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellDouble, Double>> data, int rows, int columns, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
	
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    int                              number of rows
     * @param    int                              number of columns
     * @param    List<String>                     list of data set labels
     * @return                                    the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellDouble, Double>> data, 
    		int rows, int columns, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        number of rows
     * @param    int                        number of columns
     * @param    String                     the data set label
     * @throws   FileNotFoundException 
     * @throws   UnsupportedEncodingException 
     */
	public void printMapResult(Map<WellDouble, Double> data, int rows, 
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

				WellDouble well = new WellDouble(i, j);
				
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
     * @param    Map<WellDouble, Double>    the data set
     * @param    int                        number of rows
     * @param    int                        number of columns
     * @param    String                     the data set label
     * @return                              the plate map
     */
	public String printMapResultAsString(Map<WellDouble, Double> data, 
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

				WellDouble well = new WellDouble(i, j);
				
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
     * @param    Map<WellDouble, Double>    the data set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellDouble, Double> data) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
    	this.printTableResult(sorted, "Result");
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellDouble, Double>    the data set
     * @return                              the table
     */
    public String resultToTableAsString(Map<WellDouble, Double> data) {
        TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
    	return this.printTableResultAsString(sorted, "Result");
    }
    
    /**
     * Prints each set of well value pairs as a delimiter separated table.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellDouble, Double>> data) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellDouble, Double> map : data) {
            
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(map);
            this.printTableResult(sorted, "Result");
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing each set of well value pairs as a delimiter 
     * separated table.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @return                                    the tables
     */
    public String resultToTableAsString(List<Map<WellDouble, Double>> data) {
        
    	String result = "";
    	
        for(Map<WellDouble, Double> map : data) {
            
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(map);
            result += this.printTableResultAsString(sorted, "Result");
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellDouble, Double>    the data set
     * @param    String                     the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellDouble, Double> data, String label) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
        
    	this.printTableResult(sorted, label);
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellDouble, Double>    the data set
     * @param    String                     the data set label
     * @return                              the table 
     */
    public String resultToTableAsString(Map<WellDouble, Double> data, String label) {
        TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data);
    	return this.printTableResultAsString(sorted, label);
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    List<String>                     list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellDouble, Double>> data, List<String> labels) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            this.printTableResult(sorted, label);
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    List<Map<WellDouble, Double>>    list of data sets
     * @param    List<String>                     list of data set labels
     * @return                                    the tables
     */
    public String resultToTableAsString(List<Map<WellDouble, Double>> data, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellDouble, Double> sorted = new TreeMap<WellDouble, Double>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            
            result += this.printTableResultAsString(sorted, label);
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellDouble, Double>    the data set
     * @param    String                     the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void printTableResult(Map<WellDouble, Double> data, String label) throws FileNotFoundException, UnsupportedEncodingException {
    	    	
    	this.println(label);
    	
    	this.println("Index" + this.delimiter + "Value");
    	
    	for (Map.Entry<WellDouble, Double> entry : data.entrySet()) {
    	    this.println(entry.getKey().index() + this.delimiter + entry.getValue());
    	}
    	
    	this.println();
    }
    
    /**
     * Resturns a string holding the well value pairs as a delimiter separated table.
     * @param    Map<WellDouble, Double>    the data set
     * @param    String                     the data set label
     * @return                              the table string
     */
    public String printTableResultAsString(Map<WellDouble, Double> data, String label) {
    	    	
    	String result = label + "\n";
    	
    	result += "Index" + this.delimiter + "Value\n";
    	
    	for (Map.Entry<WellDouble, Double> entry : data.entrySet()) {
    	    result += entry.getKey().index() + this.delimiter + entry.getValue();
    	    result += "\n";
    	}
    	
    	result += "\n";
    	
    	return result;
    }
    
    /*------------------- Methods for Data Set JSON Output -------------------*/
                                                            
    /**
     * Prints the well value pairs in a JSON format.
     * @param    Map<WellDouble, Double>    the data set
     * @throws   IOException
     */
    public void resultToJSON(Map<WellDouble, Double> data) throws IOException {  
          ObjectMapper mapper = new ObjectMapper();
          mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJODouble(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format.
     * @param    Map<WellDouble, Double>    the data set
     * @return                              the JSON formatted result
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(Map<WellDouble, Double> data) throws JsonProcessingException {  
          ObjectMapper mapper = new ObjectMapper();
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJODouble(data));
    }
     
    /**
     * Prints the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellDouble, Double>>    the list of data sets
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellDouble, Double>> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJODouble(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellDouble, Double>>    the list of data sets
     * @return                                    the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellDouble, Double>> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJODouble(data));
    }
    
    /**
     * Prints the well value pairs in a JSON format using the label. 
     * @param    Map<WellDouble, Double>    the data set
     * @param    String                     the data set label
     * @throws   IOException
     */
    public void resultToJSON(Map<WellDouble, Double> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJODouble(data, label));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format using the label. 
     * @param    Map<WellDouble, Double>    the data set
     * @param    String                     the data set label
     * @return                              the JSON formatted result
     */
    public String resultToJSONAsString(Map<WellDouble, Double> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJODouble(data, label));
    }
    
    /**
     * Prints the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellDouble, Double>>    the list of data sets
     * @param    List<String>                     the list of data set labels
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellDouble, Double>> data, List<String> labels) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJODouble(data, labels));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellDouble, Double>>    the list of data sets
     * @param    List<String>                     the list of data set labels
     * @return                                    the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellDouble, Double>> data, List<String> labels) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJODouble(data, labels));
    }
    
    /*--------------------- Methods for Well JSON Output ---------------------*/
    
    /**
     * Prints the well values in a JSON format.
     * @param    WellDouble    the well
     * @throws   IOException 
     */
    public void wellToJSON(WellDouble well) throws IOException {   	
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJODouble(well));
    }
    
    /**
     * Returns a string containing the well values in a JSON format.
     * @param    WellDouble    the well
     * @return                 the JSON formatted well
     * @throws  JsonProcessingException 
     */
    public String wellToJSONAsString(WellDouble well) throws JsonProcessingException {   	
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJODouble(well));
    }
    
    /**
     * Prints the collection of wells in a JSON format.
     * @param    Collection<WellDouble>    the collection of wells
     * @throws   IOException 
     */
    public void wellToJSON(Collection<WellDouble> collection) throws IOException {     
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJODouble(collection));
    }
    
    /**
     * Returns a string containing the collection of wells in a JSON format.
     * @param    Collection<WellDouble>    the collection of wells
     * @return                             the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(Collection<WellDouble> collection) throws JsonProcessingException {     
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJODouble(collection));
    }
    
    /**
     * Prints the wells values in the array in a JSON format.
     * @param    WellDouble[]    the array of wells
     * @throws   IOException 
     */
    public void wellToJSON(WellDouble[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJODouble(array));
    }
    
    /**
     * Returns a string containing the well values in the array in a JSON format.
     * @param    WellDouble[]    the array of wells
     * @return                   the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(WellDouble[] array) throws   JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJODouble(array));
    }
    
    /*------------------- Methods for Well Set JSON Output -------------------*/
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetDouble    the well set
     * @throws   IOException 
     */
    public void setToJSON(WellSetDouble set) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJODouble(set));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetDouble    the well set
     * @return                    the JSON formatted set
     * @throws   JsonProcessingException 
     */
    public String setToJSONAsString(WellSetDouble set) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJODouble(set));
    }
    
    /**
     * Prints the collection of well sets in a JSON format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @throws   IOException 
     */
    public void setToJSON(Collection<WellSetDouble> collection) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJODouble(collection));
    }
    
    /**
     * Returns a string containing the collection of well sets in a JSON format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @return                                the JSON formatted set
     * @throws   JsonProcessingException 
     */ 
    public String setToJSONAsString(Collection<WellSetDouble> collection) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJODouble(collection));
    }
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetDouble[]    the array of well sets
     * @throws   IOException 
     */
    public void setToJSON(WellSetDouble[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJODouble(array));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetDouble[]    the array of well sets
     * @return                      the JSON formatted set
     * @throws   JsonProcessingException  
     */
    public String setToJSONAsString(WellSetDouble[] array) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJODouble(array));
    }
    
    /*-------------------- Methods for Plate JSON Output ---------------------*/
    
    /**
     * Prints the plate values in a JSON format.
     * @param    PlateDouble    the plate
     * @throws   IOException 
     */
    public void plateToJSON(PlateDouble plate) throws IOException {    
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJODouble(plate));
    }
    
    /**
     * Returns a string containing the plate values in a JSON format.
     * @param    PlateDouble    the plate
     * @return                  the JSON formatted plate
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateDouble plate) throws JsonProcessingException {    
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJODouble(plate));
    } 
    
    /**
     * Prints the collection of plates in a JSON format.
     * @param    Collection<PlateDouble>    the collection of plates
     * @throws   IOException 
     */
    public void plateToJSON(Collection<PlateDouble> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJODouble(collection));
    }
    
    /**
     * Returns a string containing the collection of plates in a JSON format.
     * @param    Collection<PlateDouble>    the collection of plates
     * @return                              the JSON formatted plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Collection<PlateDouble> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJODouble(collection));
    }
     
    /**
     * Prints the array of plates in a JSON format.
     * @param    PlateDouble[]    the array of plates
     * @throws IOException 
     */
    public void plateToJSON(PlateDouble[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJODouble(array));
    }
    
    /**
     * Returns a string containing the array of plates in a JSON format.
     * @param    PlateDouble[]    the array of plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateDouble[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJODouble(array));
    }
    
    /*-------------------- Methods for Stack JSON Output ---------------------*/
    
    /**
     * Prints the plate stack in a JSON format.
     * @param    StackDouble    the plate stack
     * @throws   IOException 
     */
    public void stackToJSON(StackDouble stack) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJODouble(stack));
    }
    
    /**
     * Returns a string containing the plate stack in a JSON format.
     * @param    StackDouble    the plate stack
     * @return                  the JSON formatted stack
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackDouble stack) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJODouble(stack));
    }
    
    /**
     * Prints the collection of plate stacks in a JSON format.
     * @param    Collection<StackDouble>    the collection of plate stacks
     * @throws   IOException 
     */
    public void stackToJSON(Collection<StackDouble> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJODouble(collection));
    }
    
    /**
     * Returns a string containing the collection of plate stacks in a JSON format.
     * @param    Collection<StackDouble>    the collection of plate stacks
     * @return                              the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Collection<StackDouble> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJODouble(collection));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackDouble[]    the array of plate stacks
     * @throws IOException 
     */
    public void stackToJSON(StackDouble[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJODouble(array));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackDouble[]    the array of plate stacks
     * @return                    the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackDouble[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJODouble(array));
    } 
    
    /*-------------------- Methods for Result XML Output --------------------*/
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellDouble, Double>    the result map
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellDouble, Double> map) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(map);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellDouble, Double>    the result map
     * @return                              the XML formatted result
     */
    public String resultToXMLAsString(Map<WellDouble, Double> map) {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(map);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellDouble, Double>    the result map
     * @param    String                     the label
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellDouble, Double> map, String label) 
    		throws IOException, ParserConfigurationException, TransformerException {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(map, label);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellDouble, Double>    the result map
     * @param    String                     the label
     * @return                              the XML formatted result values
     */
    public String resultToXMLAsString(Map<WellDouble, Double> map, String label) {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(map, label);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellDouble, Double>> collection) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(collection);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @return                                the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellDouble, Double>> collection) {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(collection);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @param    List<String>                 result labels
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellDouble, Double>> collection, List<String> labels) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(collection, labels);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @param    List<String>                 result labels
     * @return                                the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellDouble, Double>> collection, List<String> labels) {
    	ResultListXMLDouble resultList = new ResultListXMLDouble(collection, labels);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    ResultListXMLDouble    the XML result list
     * @throws   IOException 
     */
    private void printXMLResult(ResultListXMLDouble resultList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(ResultListXMLDouble.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Returns a string containing the result as an XML object.
     * @param    ResultListXMLDouble    the XML result list
     * @return                          the XML formatted result list
     */
    private String printXMLResultAsString(ResultListXMLDouble resultList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(ResultListXMLDouble.class);
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
     * @param    WellDouble    the set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellDouble well) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLDouble wellList = new WellListXMLDouble(well);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the well values in an XML format.
     * @param    WellDouble    the set
     * @return                 the XML formatted well values
     */
    public String wellToXMLAsString(WellDouble well) {
    	WellListXMLDouble wellList = new WellListXMLDouble(well);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the collection of well values in an XML format.
     * @param    Collection<WellDouble>    the collection of wells
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void wellToXML(Collection<WellDouble> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLDouble wellList = new WellListXMLDouble(collection);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the collection of well values in an XML format.
     * @param    Collection<WellDouble>    the collection of wells
     * @return                             the XML formatted wells
     */
    public String wellToXMLAsString(Collection<WellDouble> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLDouble wellList = new WellListXMLDouble(collection);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellDouble[]    the array of wells
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellDouble[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLDouble wellList = new WellListXMLDouble(array);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellDouble[]    the array of wells
     * @return                   the XML formatted wells
     */
    public String wellToXMLAsString(WellDouble[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLDouble wellList = new WellListXMLDouble(array);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well list as an XML object.
     * @param    WellListXMLDouble    the well list
     * @throws   IOException 
     */
    private void printXMLWell(WellListXMLDouble wellList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellListXMLDouble.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well list as an XML object.
     * @param    WellListXMLDouble    the well list
     * @return                        the XML formatted well list
     */
    private String printXMLWellAsString(WellListXMLDouble wellList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellListXMLDouble.class);
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
     * @param    WellSetDouble    the well set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetDouble set) throws IOException, 
                                                    ParserConfigurationException, 
                                                    TransformerException {
    	WellSetListXMLDouble setList = new WellSetListXMLDouble(set);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetDouble    the well set
     * @return                    the XML formatted well sets
     */
    public String setToXMLAsString(WellSetDouble set) {
    	WellSetListXMLDouble setList = new WellSetListXMLDouble(set);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the collection of well sets in an XML format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void setToXML(Collection<WellSetDouble> collection) throws IOException, 
                                                                       TransformerException, 
                                                                       ParserConfigurationException {
    	WellSetListXMLDouble setList = new WellSetListXMLDouble(collection);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the collection of well sets in an XML format.
     * @param    Collection<WellSetDouble>    the collection of well sets
     * @return                                the XML formatted well sets
     */
    public String setToXMLAsString(Collection<WellSetDouble> collection) {
    	WellSetListXMLDouble setList = new WellSetListXMLDouble(collection);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSetDouble[]    the array of well sets
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetDouble[] array) throws IOException, 
                                                        ParserConfigurationException, 
                                                        TransformerException {
    	WellSetListXMLDouble setList = new WellSetListXMLDouble(array);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetDouble[]    the array of well sets
     * @return                      the XML formatted well sets
     */
    public String setToXMLAsString(WellSetDouble[] array) {
    	WellSetListXMLDouble setList = new WellSetListXMLDouble(array);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    WellSetListXMLDouble    the well set list
     * @throws   IOException 
     */
    private void printXMLSet(WellSetListXMLDouble setList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLDouble.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well set as an XML object.
     * @param    WellSetListXMLDouble    the well set list
     * @return                           the XML formatted set list
     */
    private String printXMLSetAsString(WellSetListXMLDouble setList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLDouble.class);
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
     * @param    PlateDouble    the plate
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(PlateDouble plate) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLDouble plateList = new PlateListXMLDouble(plate);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the plate values in an XML format.
     * @param    PlateDouble    the plate
     * @return                  the XML formatted plates
     */
    public String plateToXMLAsString(PlateDouble plate) {
    	PlateListXMLDouble plateList = new PlateListXMLDouble(plate);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the collection of plates in an XML format.
     * @param    Collection<PlateDouble>    the collection of plates
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(Collection<PlateDouble> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLDouble plateList = new PlateListXMLDouble(collection);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the collection of plates in an XML format.
     * @param    Collection<PlateDouble>    the collection of plates
     * @return                              the XML formatted plates 
     */
    public String plateToXMLAsString(Collection<PlateDouble> collection) {
    	PlateListXMLDouble plateList = new PlateListXMLDouble(collection);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the array of plates in an XML format.
     * @param    PlateDouble[]    the array of plates
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void plateToXML(PlateDouble[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	PlateListXMLDouble plateList = new PlateListXMLDouble(array);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the array of plates in an XML format.
     * @param    PlateDouble[]    the array of plates
     * @return                    the XML formatted plates
     */
    public String plateToXMLAsString(PlateDouble[] array) {
    	PlateListXMLDouble plateList = new PlateListXMLDouble(array);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLDouble    the plate list
     * @throws   IOException 
     */
    private void printXMLPlate(PlateListXMLDouble plateList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(PlateListXMLDouble.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLDouble    the plate list
     * @return                         the XML formatted plate list
     */
    private String printXMLPlateAsString(PlateListXMLDouble plateList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(PlateListXMLDouble.class);
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
     * @param    StackDouble    the plate stack
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void stackToXML(StackDouble stack) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	StackListXMLDouble stackList = new StackListXMLDouble(stack);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the plate stack in an XML format.
     * @param    StackDouble    the plate stack
     * @return                  the XML formatted stacks 
     */
    public String stackToXMLAsString(StackDouble stack) {
    	StackListXMLDouble stackList = new StackListXMLDouble(stack);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the collection of plate stacks in an XML format.
     * @param    Collection<StackDouble>    the collection of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(Collection<StackDouble> collection) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLDouble stackList = new StackListXMLDouble(collection);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the collection of plate stacks in an XML format.
     * @param    Collection<StackDouble>    the collection of plate stacks
     * @return                              the XML formatted stacks
     */
    public String stackToXMLAsString(Collection<StackDouble> collection) {
    	StackListXMLDouble stackList = new StackListXMLDouble(collection);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the array of plate stacks in an XML format.
     * @param    StackDouble[]    the array of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(StackDouble[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLDouble stackList = new StackListXMLDouble(array);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the array of plate stacks in an XML format.
     * @param    StackDouble[]    the array of plate stacks
     * @return                    the XML formatted stacks
     */
    public String stackToXMLAsString(StackDouble[] array) {
    	StackListXMLDouble stackList = new StackListXMLDouble(array);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the stack as an XML object.
     * @param    StackListXMLDouble    the stack list
     * @throws   IOException 
     * @throws   TransformerException 
     */
    public void printXMLStack(StackListXMLDouble stackList) throws IOException, 
    		TransformerException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(StackListXMLDouble.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the stack list as an XML object.
     * @param    StackListXMLDouble    the stack list
     * @return                         the XML formatted stack list
     */
    public String printXMLStackAsString(StackListXMLDouble stackList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(StackListXMLDouble.class);
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
		
		    case PlateDouble.PLATE_6WELL: return PlateDouble.ROWS_6WELL;
		
		    case PlateDouble.PLATE_12WELL: return PlateDouble.ROWS_12WELL;
		    
		    case PlateDouble.PLATE_24WELL: return PlateDouble.ROWS_24WELL;
		    
		    case PlateDouble.PLATE_48WELL: return PlateDouble.ROWS_48WELL;
		    
		    case PlateDouble.PLATE_96WELL: return PlateDouble.ROWS_96WELL;
		    
		    case PlateDouble.PLATE_384WELL: return PlateDouble.ROWS_384WELL;
		    
		    case PlateDouble.PLATE_1536WELL: return PlateDouble.ROWS_1536WELL;
		    
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
		
		    case PlateDouble.PLATE_6WELL: return PlateDouble.COLUMNS_6WELL;
		
		    case PlateDouble.PLATE_12WELL: return PlateDouble.COLUMNS_12WELL;
		    
		    case PlateDouble.PLATE_24WELL: return PlateDouble.COLUMNS_24WELL;
		    
		    case PlateDouble.PLATE_48WELL: return PlateDouble.COLUMNS_48WELL;
		    
		    case PlateDouble.PLATE_96WELL: return PlateDouble.COLUMNS_96WELL;
		    
		    case PlateDouble.PLATE_384WELL: return PlateDouble.COLUMNS_384WELL;
		    
		    case PlateDouble.PLATE_1536WELL: return PlateDouble.COLUMNS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}

}
