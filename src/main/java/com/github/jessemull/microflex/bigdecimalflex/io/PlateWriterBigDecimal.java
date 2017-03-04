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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
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

import com.github.jessemull.microflex.bigdecimalflex.plate.PlateBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.StackBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellBigDecimal;
import com.github.jessemull.microflex.bigdecimalflex.plate.WellSetBigDecimal;

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
public class PlateWriterBigDecimal extends PrintWriter {
	
	/*---------------------------- Private Fields ----------------------------*/
	
	/* Number of character types available for the row ID */

	private int ALPHA_BASE = 26;
    
    /* The delimiter for delimiter separated values */
    
    private String delimiter = "\t";
    
    /*------------------------------ Constructors ----------------------------*/
    
    /**
     * Creates a new PlateWriterBigDecimal without automatic line flushing using 
     * the specified file.
     * @param    File    the output file
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigDecimal(File file) throws FileNotFoundException {
    	super(file);
    }
    
    /**
     * Creates a new PlateWriterBigDecimal without automatic line flushing using 
     * the specified file and character set.
     * @param    File     the output file
     * @param    String   the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigDecimal(File file, String csn) throws FileNotFoundException, 
                                                            UnsupportedEncodingException {
    	super(file, csn);
    }

    /**
     * Creates a new PlateWriterBigDecimal, without automatic line flushing using
     * the OutputStream.
     * @param    OutputStream    the output stream
     */
    public PlateWriterBigDecimal(OutputStream out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterBigDecimal with automatic line flushing, using the 
     * OutputStream.
     * @param    OutputStream    the output stream
     * @param    boolean         sets automatic flush when true
     */
    public PlateWriterBigDecimal(OutputStream out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /**
     * Creates a new PlateWriterBigDecimal without automatic line flushing using the
     * specified file name.
     * @param    String    the file name
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigDecimal(String fileName) throws FileNotFoundException {
    	super(fileName);
    }
    
    /**
     * Creates a new PlateWriterBigDecimal without automatic line flushing using the
     * specified file name and character set.
     * @param    String    the file name
     * @param    String    the character set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public PlateWriterBigDecimal(String fileName, String csn) throws FileNotFoundException, 
                                                                  UnsupportedEncodingException {
    	super(fileName, csn);
    }

    /**
     * Creates a new PlateWriterBigDecimal without automatic line flushing using the
     * writer.
     * @param    Writer    the writer
     */
    public PlateWriterBigDecimal(Writer out) {
    	super(out);
    }
    
    /**
     * Creates a new PlateWriterBigDecimal with automatic line flushing using the
     * writer.
     * @param    Writer     the writer
     * @param    boolean    sets auto flush when true
     */
    public PlateWriterBigDecimal(Writer out, boolean autoFlush) {
    	super(out, autoFlush);
    }
    
    /*--------------------- Methods for Plate Map Output ---------------------*/
	
	/**
     * Prints the plate map.
     * @param    Map<WellBigDecimal,    the data set
     * @param    int                                the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigDecimal, BigDecimal> data, int type) 
			throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
		
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string containing the plate map.
     * @param    Map<WellBigDecimal,    the data set
     * @param    int                                the plate type
     */
	public String resultToPlateMapAsString(Map<WellBigDecimal, BigDecimal> data, int type) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal,    list of data sets
     * @param    int                                      the plate type
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigDecimal, BigDecimal>> data, 
    		int type) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellBigDecimal, BigDecimal> map : data) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
    
	/**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal,    list of data sets
     * @param    int                                      the plate type
     * @return                                            the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigDecimal, BigDecimal>> data, int type) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(Map<WellBigDecimal, BigDecimal> map : data) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
	
    
    
	/**
     * Prints the plate map.
     * @param    Map<WellBigDecimal,    the data set
     * @param    int                                number of rows  
     * @param    int                                number of columns
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigDecimal, BigDecimal> data, int rows, int columns) 
			throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
	    
		this.printMapResult(sorted, rows, columns, "Result");
		
		this.flush();
	}
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                number of rows
     * @param    int                                number of columns
	 * @return                                         the plate map
     */
	public String resultToPlateMapAsString(Map<WellBigDecimal, BigDecimal> data, 
			int rows, int columns) {
	    
	    TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
	    
		return this.printMapResultAsString(sorted, rows, columns, "Result");
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    int                                      number of rows
     * @param    int                                      number of columns
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigDecimal, BigDecimal>> data, int rows, int columns) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellBigDecimal, BigDecimal> map : data) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(map);
            this.printMapResult(sorted, rows, columns, "Result");
            this.println();
        }
        
        this.flush(); 
    }
	
    /**
     * Returns a string holding the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    int                                      number of rows
     * @param    int                                      number of columns
     * @return                                            the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigDecimal, BigDecimal>> data, 
    		int rows, int columns) {
        
    	String result = "";
    	
        for(Map<WellBigDecimal, BigDecimal> map : data) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(map);
            result += this.printMapResultAsString(sorted, rows, columns, "Result");
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                the plate type
     * @param    String                             the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigDecimal, BigDecimal> data, int type, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
		
		this.printMapResult(sorted, rows, columns, label);
		
		this.flush();
	}
	
	
	/**
     * Returns a string holding the plate map.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                the plate type
     * @param    String                             the data set label
	 * @return                                         the plate map
     */
	public String resultToPlateMapAsString(Map<WellBigDecimal, BigDecimal> data, 
			int type, String label) {
		
		int rows = parseRows(type);
		int columns = parseColumns(type);
		
		TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
		
		return this.printMapResultAsString(sorted, rows, columns, label);
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    int                                      the plate type
     * @param    List<String>                             list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigDecimal, BigDecimal>> data, int type, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    int                                      the plate type
     * @param    List<String>                             list of data set labels
     * @return                                            the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigDecimal, BigDecimal>> data, 
    		int type, List<String> labels) {
        
    	String result = "";
    	
        int rows = parseRows(type);
        int columns = parseColumns(type);
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }

        return result;
    }
	
	/**
     * Prints the plate map.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    String                             the data set label
	 * @throws   UnsupportedEncodingException 
	 * @throws   FileNotFoundException 
     */
	public void resultToPlateMap(Map<WellBigDecimal, BigDecimal> data, int rows, int columns, 
			String label) throws FileNotFoundException, UnsupportedEncodingException {
	    
	    TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
	    
		this.printMapResult(sorted, rows, columns, label);	
		
		this.flush();
	}

	/**
     * Returns a string containing the plate map.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    String                             the data set label
	 * @return                                         the plate map
     */
	public String resultToPlateMapAsString(Map<WellBigDecimal, BigDecimal> data, int rows, int columns, 
			String label) {
	    
	    TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
	    
	    return this.printMapResultAsString(sorted, rows, columns, label);	
	}
	
	/**
     * Prints the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    int                                      number of rows
     * @param    int                                      number of columns
     * @param    List<String>                             list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToPlateMap(List<Map<WellBigDecimal, BigDecimal>> data, int rows, int columns, 
            List<String> labels) throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            this.printMapResult(sorted, rows, columns, label);
            this.println();
        }
        
        this.flush();
    }
	
    /**
     * Returns a string containing the plate map for each data set in the list.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    int                                      number of rows
     * @param    int                                      number of columns
     * @param    List<String>                             list of data set labels
     * @return                                            the plate maps
     */
    public String resultToPlateMapAsString(List<Map<WellBigDecimal, BigDecimal>> data, 
    		int rows, int columns, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);           
            result += this.printMapResultAsString(sorted, rows, columns, label);
            result += "\n";
        }
        
        return result;
    }
    
	/**
     * Prints the plate map.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    String                             the data set label
     * @throws   FileNotFoundException 
     * @throws   UnsupportedEncodingException 
     */
	public void printMapResult(Map<WellBigDecimal, BigDecimal> data, int rows, 
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

				WellBigDecimal well = new WellBigDecimal(i, j);
				
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
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    int                                number of rows
     * @param    int                                number of columns
     * @param    String                             the data set label
     * @return                                      the plate map
     */
	public String printMapResultAsString(Map<WellBigDecimal, BigDecimal> data, 
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

				WellBigDecimal well = new WellBigDecimal(i, j);
				
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
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellBigDecimal, BigDecimal> data) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
    	this.printTableResult(sorted, "Result");
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @return                                      the table
     */
    public String resultToTableAsString(Map<WellBigDecimal, BigDecimal> data) {
        TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
    	return this.printTableResultAsString(sorted, "Result");
    }
    
    /**
     * Prints each set of well value pairs as a delimiter separated table.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellBigDecimal, BigDecimal>> data) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(Map<WellBigDecimal, BigDecimal> map : data) {
            
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(map);
            this.printTableResult(sorted, "Result");
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing each set of well value pairs as a delimiter 
     * separated table.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @return                                            the tables
     */
    public String resultToTableAsString(List<Map<WellBigDecimal, BigDecimal>> data) {
        
    	String result = "";
    	
        for(Map<WellBigDecimal, BigDecimal> map : data) {
            
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(map);
            result += this.printTableResultAsString(sorted, "Result");
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    String                             the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(Map<WellBigDecimal, BigDecimal> data, String label) 
    		throws FileNotFoundException, UnsupportedEncodingException {
        
        TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
        
    	this.printTableResult(sorted, label);
    	
    	this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    String                             the data set label
     * @return                                      the table 
     */
    public String resultToTableAsString(Map<WellBigDecimal, BigDecimal> data, String label) {
        TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data);
    	return this.printTableResultAsString(sorted, label);
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    List<String>                             list of data set labels
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void resultToTable(List<Map<WellBigDecimal, BigDecimal>> data, List<String> labels) 
            throws FileNotFoundException, UnsupportedEncodingException {
        
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            this.printTableResult(sorted, label);
            this.print("\n");
            
        }
        
        this.flush();
    }
    
    /**
     * Returns a string containing the well value pairs as a delimiter separated 
     * table.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    list of data sets
     * @param    List<String>                             list of data set labels
     * @return                                            the tables
     */
    public String resultToTableAsString(List<Map<WellBigDecimal, BigDecimal>> data, List<String> labels) {
        
    	String result = "";
    	
        for(int i = 0; i < data.size(); i++) {
            
            TreeMap<WellBigDecimal, BigDecimal> sorted = new TreeMap<WellBigDecimal, BigDecimal>(data.get(i));
            String label = i > labels.size() ? "Result" : labels.get(i);
            
            result += this.printTableResultAsString(sorted, label);
            result += "\n";
            
        }
        
        return result;
    }
    
    /**
     * Prints the well value pairs as a delimiter separated table.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    String                             the data set label
     * @throws   UnsupportedEncodingException 
     * @throws   FileNotFoundException 
     */
    public void printTableResult(Map<WellBigDecimal, BigDecimal> data, String label) throws FileNotFoundException, UnsupportedEncodingException {
    	    	
    	this.println(label);
    	
    	this.println("Index" + this.delimiter + "Value");
    	
    	for (Map.Entry<WellBigDecimal, BigDecimal> entry : data.entrySet()) {
    	    this.println(entry.getKey().index() + this.delimiter + entry.getValue());
    	}
    	
    	this.println();
    }
    
    /**
     * Resturns a string holding the well value pairs as a delimiter separated table.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    String                             the data set label
     * @return                                      the table string
     */
    public String printTableResultAsString(Map<WellBigDecimal, BigDecimal> data, String label) {
    	    	
    	String result = label + "\n";
    	
    	result += "Index" + this.delimiter + "Value\n";
    	
    	for (Map.Entry<WellBigDecimal, BigDecimal> entry : data.entrySet()) {
    	    result += entry.getKey().index() + this.delimiter + entry.getValue();
    	    result += "\n";
    	}
    	
    	result += "\n";
    	
    	return result;
    }
    
    /*------------------- Methods for Data Set JSON Output -------------------*/
                                                            
    /**
     * Prints the well value pairs in a JSON format.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @throws   IOException
     */
    public void resultToJSON(Map<WellBigDecimal, BigDecimal> data) throws IOException {  
          ObjectMapper mapper = new ObjectMapper();
          mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigDecimal(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format.
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @return                                      the JSON formatted result
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(Map<WellBigDecimal, BigDecimal> data) throws JsonProcessingException {  
          ObjectMapper mapper = new ObjectMapper();
          return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigDecimal(data));
    }
     
    /**
     * Prints the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    the list of data sets
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellBigDecimal, BigDecimal>> data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigDecimal(data));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    the list of data sets
     * @return                                            the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellBigDecimal, BigDecimal>> data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigDecimal(data));
    }
    
    /**
     * Prints the well value pairs in a JSON format using the label. 
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    String                             the data set label
     * @throws   IOException
     */
    public void resultToJSON(Map<WellBigDecimal, BigDecimal> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigDecimal(data, label));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format using the label. 
     * @param    Map<WellBigDecimal, BigDecimal>    the data set
     * @param    String                             the data set label
     * @return                                      the JSON formatted result
     */
    public String resultToJSONAsString(Map<WellBigDecimal, BigDecimal> data, String label) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigDecimal(data, label));
    }
    
    /**
     * Prints the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    the list of data sets
     * @param    List<String>                             the list of data set labels
     * @throws   IOException
     */
    public void resultToJSON(List<Map<WellBigDecimal, BigDecimal>> data, List<String> labels) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new ResultListPOJOBigDecimal(data, labels));
    }
    
    /**
     * Returns a string containing the well value pairs in a JSON format for each data set using the 
     * specified labels.
     * @param    List<Map<WellBigDecimal, BigDecimal>>    the list of data sets
     * @param    List<String>                             the list of data set labels
     * @return                                            the JSON formatted results
     * @throws   JsonProcessingException 
     */
    public String resultToJSONAsString(List<Map<WellBigDecimal, BigDecimal>> data, List<String> labels) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ResultListPOJOBigDecimal(data, labels));
    }
    
    /*--------------------- Methods for Well JSON Output ---------------------*/
    
    /**
     * Prints the well values in a JSON format.
     * @param    WellBigDecimal    the well
     * @throws   IOException 
     */
    public void wellToJSON(WellBigDecimal well) throws IOException {   	
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOBigDecimal(well));
    }
    
    /**
     * Returns a string containing the well values in a JSON format.
     * @param    WellBigDecimal    the well
     * @return                     the JSON formatted well
     * @throws  JsonProcessingException 
     */
    public String wellToJSONAsString(WellBigDecimal well) throws JsonProcessingException {   	
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOBigDecimal(well));
    }
    
    /**
     * Prints the collection of wells in a JSON format.
     * @param    Collection<WellBigDecimal>    the collection of wells
     * @throws   IOException 
     */
    public void wellToJSON(Collection<WellBigDecimal> collection) throws IOException {     
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOBigDecimal(collection));
    }
    
    /**
     * Returns a string containing the collection of wells in a JSON format.
     * @param    Collection<WellBigDecimal>    the collection of wells
     * @return                                 the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(Collection<WellBigDecimal> collection) throws JsonProcessingException {     
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOBigDecimal(collection));
    }
    
    /**
     * Prints the wells values in the array in a JSON format.
     * @param    WellBigDecimal[]    the array of wells
     * @throws   IOException 
     */
    public void wellToJSON(WellBigDecimal[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellListPOJOBigDecimal(array));
    }
    
    /**
     * Returns a string containing the well values in the array in a JSON format.
     * @param    WellBigDecimal[]    the array of wells
     * @return                       the JSON formatted wells
     * @throws   JsonProcessingException 
     */
    public String wellToJSONAsString(WellBigDecimal[] array) throws   JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellListPOJOBigDecimal(array));
    }
    
    /*------------------- Methods for Well Set JSON Output -------------------*/
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetBigDecimal    the well set
     * @throws   IOException 
     */
    public void setToJSON(WellSetBigDecimal set) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOBigDecimal(set));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetBigDecimal    the well set
     * @return                        the JSON formatted set
     * @throws   JsonProcessingException 
     */
    public String setToJSONAsString(WellSetBigDecimal set) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOBigDecimal(set));
    }
    
    /**
     * Prints the collection of well sets in a JSON format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @throws   IOException 
     */
    public void setToJSON(Collection<WellSetBigDecimal> collection) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOBigDecimal(collection));
    }
    
    /**
     * Returns a string containing the collection of well sets in a JSON format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @return                                    the JSON formatted set
     * @throws   JsonProcessingException 
     */ 
    public String setToJSONAsString(Collection<WellSetBigDecimal> collection) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOBigDecimal(collection));
    }
    
    /**
     * Prints the well set values in a JSON format.
     * @param    WellSetBigDecimal[]    the array of well sets
     * @throws   IOException 
     */
    public void setToJSON(WellSetBigDecimal[] array) throws   IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new WellSetListPOJOBigDecimal(array));
    }
    
    /**
     * Returns a string containing the well set values in a JSON format.
     * @param    WellSetBigDecimal[]    the array of well sets
     * @return                          the JSON formatted set
     * @throws   JsonProcessingException  
     */
    public String setToJSONAsString(WellSetBigDecimal[] array) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new WellSetListPOJOBigDecimal(array));
    }
    
    /*-------------------- Methods for Plate JSON Output ---------------------*/
    
    /**
     * Prints the plate values in a JSON format.
     * @param    PlateBigDecimal    the plate
     * @throws   IOException 
     */
    public void plateToJSON(PlateBigDecimal plate) throws IOException {    
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOBigDecimal(plate));
    }
    
    /**
     * Returns a string containing the plate values in a JSON format.
     * @param    PlateBigDecimal    the plate
     * @return                      the JSON formatted plate
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateBigDecimal plate) throws JsonProcessingException {    
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOBigDecimal(plate));
    } 
    
    /**
     * Prints the collection of plates in a JSON format.
     * @param    Collection<PlateBigDecimal>    the collection of plates
     * @throws   IOException 
     */
    public void plateToJSON(Collection<PlateBigDecimal> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOBigDecimal(collection));
    }
    
    /**
     * Returns a string containing the collection of plates in a JSON format.
     * @param    Collection<PlateBigDecimal>    the collection of plates
     * @return                                  the JSON formatted plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(Collection<PlateBigDecimal> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOBigDecimal(collection));
    }
     
    /**
     * Prints the array of plates in a JSON format.
     * @param    PlateBigDecimal[]    the array of plates
     * @throws IOException 
     */
    public void plateToJSON(PlateBigDecimal[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new PlateListPOJOBigDecimal(array));
    }
    
    /**
     * Returns a string containing the array of plates in a JSON format.
     * @param    PlateBigDecimal[]    the array of plates
     * @throws   JsonProcessingException 
     */
    public String plateToJSONAsString(PlateBigDecimal[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper(); 
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new PlateListPOJOBigDecimal(array));
    }
    
    /*-------------------- Methods for Stack JSON Output ---------------------*/
    
    /**
     * Prints the plate stack in a JSON format.
     * @param    StackBigDecimal    the plate stack
     * @throws   IOException 
     */
    public void stackToJSON(StackBigDecimal stack) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOBigDecimal(stack));
    }
    
    /**
     * Returns a string containing the plate stack in a JSON format.
     * @param    StackBigDecimal    the plate stack
     * @return                      the JSON formatted stack
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackBigDecimal stack) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOBigDecimal(stack));
    }
    
    /**
     * Prints the collection of plate stacks in a JSON format.
     * @param    Collection<StackBigDecimal>    the collection of plate stacks
     * @throws   IOException 
     */
    public void stackToJSON(Collection<StackBigDecimal> collection) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOBigDecimal(collection));
    }
    
    /**
     * Returns a string containing the collection of plate stacks in a JSON format.
     * @param    Collection<StackBigDecimal>    the collection of plate stacks
     * @return                                  the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(Collection<StackBigDecimal> collection) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOBigDecimal(collection));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackBigDecimal[]    the array of plate stacks
     * @throws IOException 
     */
    public void stackToJSON(StackBigDecimal[] array) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(this, new StackListPOJOBigDecimal(array));
    }
    
    /**
     * Prints the array of plate stacks in a JSON format.
     * @param    StackBigDecimal[]    the array of plate stacks
     * @return                        the JSON formatted stacks
     * @throws   JsonProcessingException 
     */
    public String stackToJSONAsString(StackBigDecimal[] array) throws JsonProcessingException {
    	ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new StackListPOJOBigDecimal(array));
    } 
    
    /*-------------------- Methods for Result XML Output --------------------*/
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellBigDecimal, BigDecimal>    the result map
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellBigDecimal, BigDecimal> map) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(map);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellBigDecimal, BigDecimal>    the result map
     * @return                                      the XML formatted result
     */
    public String resultToXMLAsString(Map<WellBigDecimal, BigDecimal> map) {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(map);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well result values in an XML format.
     * @param    Map<WellBigDecimal, BigDecimal>    the result map
     * @param    String                             the label
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void resultToXML(Map<WellBigDecimal, BigDecimal> map, String label) 
    		throws IOException, ParserConfigurationException, TransformerException {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(map, label);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the result values in an XML format.
     * @param    Map<WellBigDecimal, BigDecimal>    the result map
     * @param    String                             the label
     * @return                                      the XML formatted result values
     */
    public String resultToXMLAsString(Map<WellBigDecimal, BigDecimal> map, String label) {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(map, label);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellBigDecimal, BigDecimal>> collection) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(collection);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @return                                    the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellBigDecimal, BigDecimal>> collection) {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(collection);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the collection of result values in an XML format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @param    List<String>                     result labels
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void resultToXML(Collection<Map<WellBigDecimal, BigDecimal>> collection, List<String> labels) 
    		throws IOException, TransformerException, ParserConfigurationException {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(collection, labels);
    	this.printXMLResult(resultList);
    }
    
    /**
     * Returns a string containing the collection of result values in an XML format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @param    List<String>                     result labels
     * @return                                    the XML formatted result values
     */
    public String resultToXMLAsString(Collection<Map<WellBigDecimal, BigDecimal>> collection, List<String> labels) {
    	ResultListXMLBigDecimal resultList = new ResultListXMLBigDecimal(collection, labels);
    	return this.printXMLResultAsString(resultList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    ResultListXMLBigDecimal    the XML result list
     * @throws   IOException 
     */
    private void printXMLResult(ResultListXMLBigDecimal resultList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(ResultListXMLBigDecimal.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(resultList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Returns a string containing the result as an XML object.
     * @param    ResultListXMLBigDecimal    the XML result list
     * @return                              the XML formatted result list
     */
    private String printXMLResultAsString(ResultListXMLBigDecimal resultList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(ResultListXMLBigDecimal.class);
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
     * @param    WellBigDecimal    the set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellBigDecimal well) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLBigDecimal wellList = new WellListXMLBigDecimal(well);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the well values in an XML format.
     * @param    WellBigDecimal    the set
     * @return                     the XML formatted well values
     */
    public String wellToXMLAsString(WellBigDecimal well) {
    	WellListXMLBigDecimal wellList = new WellListXMLBigDecimal(well);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the collection of well values in an XML format.
     * @param    Collection<WellBigDecimal>    the collection of wells
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void wellToXML(Collection<WellBigDecimal> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLBigDecimal wellList = new WellListXMLBigDecimal(collection);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Returns a string containing the collection of well values in an XML format.
     * @param    Collection<WellBigDecimal>    the collection of wells
     * @return                                 the XML formatted wells
     */
    public String wellToXMLAsString(Collection<WellBigDecimal> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	WellListXMLBigDecimal wellList = new WellListXMLBigDecimal(collection);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellBigDecimal[]    the array of wells
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void wellToXML(WellBigDecimal[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLBigDecimal wellList = new WellListXMLBigDecimal(array);
    	this.printXMLWell(wellList);
    }
    
    /**
     * Prints the well values in the array in an XML format.
     * @param    WellBigDecimal[]    the array of wells
     * @return                       the XML formatted wells
     */
    public String wellToXMLAsString(WellBigDecimal[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	WellListXMLBigDecimal wellList = new WellListXMLBigDecimal(array);
    	return this.printXMLWellAsString(wellList);
    }
    
    /**
     * Prints the well list as an XML object.
     * @param    WellListXMLBigDecimal    the well list
     * @throws   IOException 
     */
    private void printXMLWell(WellListXMLBigDecimal wellList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellListXMLBigDecimal.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(wellList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well list as an XML object.
     * @param    WellListXMLBigDecimal    the well list
     * @return                            the XML formatted well list
     */
    private String printXMLWellAsString(WellListXMLBigDecimal wellList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellListXMLBigDecimal.class);
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
     * @param    WellSetBigDecimal    the well set
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetBigDecimal set) throws IOException, 
                                                    ParserConfigurationException, 
                                                    TransformerException {
    	WellSetListXMLBigDecimal setList = new WellSetListXMLBigDecimal(set);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetBigDecimal    the well set
     * @return                        the XML formatted well sets
     */
    public String setToXMLAsString(WellSetBigDecimal set) {
    	WellSetListXMLBigDecimal setList = new WellSetListXMLBigDecimal(set);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the collection of well sets in an XML format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void setToXML(Collection<WellSetBigDecimal> collection) throws IOException, 
                                                                       TransformerException, 
                                                                       ParserConfigurationException {
    	WellSetListXMLBigDecimal setList = new WellSetListXMLBigDecimal(collection);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the collection of well sets in an XML format.
     * @param    Collection<WellSetBigDecimal>    the collection of well sets
     * @return                                    the XML formatted well sets
     */
    public String setToXMLAsString(Collection<WellSetBigDecimal> collection) {
    	WellSetListXMLBigDecimal setList = new WellSetListXMLBigDecimal(collection);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set values in an XML format.
     * @param    WellSetBigDecimal[]    the array of well sets
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void setToXML(WellSetBigDecimal[] array) throws IOException, 
                                                        ParserConfigurationException, 
                                                        TransformerException {
    	WellSetListXMLBigDecimal setList = new WellSetListXMLBigDecimal(array);
    	this.printXMLSet(setList);
    }
    
    /**
     * Returns a string containing the well set values in an XML format.
     * @param    WellSetBigDecimal[]    the array of well sets
     * @return                          the XML formatted well sets
     */
    public String setToXMLAsString(WellSetBigDecimal[] array) {
    	WellSetListXMLBigDecimal setList = new WellSetListXMLBigDecimal(array);
    	return this.printXMLSetAsString(setList);
    }
    
    /**
     * Prints the well set as an XML object.
     * @param    WellSetListXMLBigDecimal    the well set list
     * @throws   IOException 
     */
    private void printXMLSet(WellSetListXMLBigDecimal setList) throws IOException {
        
    	try {

    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLBigDecimal.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(setList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the well set as an XML object.
     * @param    WellSetListXMLBigDecimal    the well set list
     * @return                               the XML formatted set list
     */
    private String printXMLSetAsString(WellSetListXMLBigDecimal setList) {
        
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(WellSetListXMLBigDecimal.class);
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
     * @param    PlateBigDecimal    the plate
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(PlateBigDecimal plate) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLBigDecimal plateList = new PlateListXMLBigDecimal(plate);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the plate values in an XML format.
     * @param    PlateBigDecimal    the plate
     * @return                      the XML formatted plates
     */
    public String plateToXMLAsString(PlateBigDecimal plate) {
    	PlateListXMLBigDecimal plateList = new PlateListXMLBigDecimal(plate);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the collection of plates in an XML format.
     * @param    Collection<PlateBigDecimal>    the collection of plates
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void plateToXML(Collection<PlateBigDecimal> collection) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	PlateListXMLBigDecimal plateList = new PlateListXMLBigDecimal(collection);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the collection of plates in an XML format.
     * @param    Collection<PlateBigDecimal>    the collection of plates
     * @return                                  the XML formatted plates 
     */
    public String plateToXMLAsString(Collection<PlateBigDecimal> collection) {
    	PlateListXMLBigDecimal plateList = new PlateListXMLBigDecimal(collection);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the array of plates in an XML format.
     * @param    PlateBigDecimal[]    the array of plates
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void plateToXML(PlateBigDecimal[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	PlateListXMLBigDecimal plateList = new PlateListXMLBigDecimal(array);
    	this.printXMLPlate(plateList);
    }
    
    /**
     * Returns a string containing the array of plates in an XML format.
     * @param    PlateBigDecimal[]    the array of plates
     * @return                        the XML formatted plates
     */
    public String plateToXMLAsString(PlateBigDecimal[] array) {
    	PlateListXMLBigDecimal plateList = new PlateListXMLBigDecimal(array);
    	return this.printXMLPlateAsString(plateList);
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLBigDecimal    the plate list
     * @throws   IOException 
     */
    private void printXMLPlate(PlateListXMLBigDecimal plateList) throws IOException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(PlateListXMLBigDecimal.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(plateList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Prints the plate as an XML object.
     * @param    PlateListXMLBigDecimal    the plate list
     * @return                             the XML formatted plate list
     */
    private String printXMLPlateAsString(PlateListXMLBigDecimal plateList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(PlateListXMLBigDecimal.class);
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
     * @param    StackBigDecimal    the plate stack
     * @throws   IOException 
     * @throws   TransformerException 
     * @throws   ParserConfigurationException 
     */
    public void stackToXML(StackBigDecimal stack) throws IOException, 
    		TransformerException, ParserConfigurationException {
    	StackListXMLBigDecimal stackList = new StackListXMLBigDecimal(stack);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the plate stack in an XML format.
     * @param    StackBigDecimal    the plate stack
     * @return                      the XML formatted stacks 
     */
    public String stackToXMLAsString(StackBigDecimal stack) {
    	StackListXMLBigDecimal stackList = new StackListXMLBigDecimal(stack);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the collection of plate stacks in an XML format.
     * @param    Collection<StackBigDecimal>    the collection of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(Collection<StackBigDecimal> collection) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLBigDecimal stackList = new StackListXMLBigDecimal(collection);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the collection of plate stacks in an XML format.
     * @param    Collection<StackBigDecimal>    the collection of plate stacks
     * @return                                  the XML formatted stacks
     */
    public String stackToXMLAsString(Collection<StackBigDecimal> collection) {
    	StackListXMLBigDecimal stackList = new StackListXMLBigDecimal(collection);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the array of plate stacks in an XML format.
     * @param    StackBigDecimal[]    the array of plate stacks
     * @throws   IOException 
     * @throws   ParserConfigurationException 
     * @throws   TransformerException 
     */
    public void stackToXML(StackBigDecimal[] array) throws IOException, 
    		ParserConfigurationException, TransformerException {
    	StackListXMLBigDecimal stackList = new StackListXMLBigDecimal(array);
    	this.printXMLStack(stackList);
    }
    
    /**
     * Returns a string containing the array of plate stacks in an XML format.
     * @param    StackBigDecimal[]    the array of plate stacks
     * @return                        the XML formatted stacks
     */
    public String stackToXMLAsString(StackBigDecimal[] array) {
    	StackListXMLBigDecimal stackList = new StackListXMLBigDecimal(array);
    	return this.printXMLStackAsString(stackList);
    }
    
    /**
     * Prints the stack as an XML object.
     * @param    StackListXMLBigDecimal    the stack list
     * @throws   IOException 
     * @throws   TransformerException 
     */
    public void printXMLStack(StackListXMLBigDecimal stackList) throws IOException, 
    		TransformerException {
    	
    	try {

    		JAXBContext context = JAXBContext.newInstance(StackListXMLBigDecimal.class);
    		Marshaller marshaller = context.createMarshaller();
    		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			marshaller.marshal(stackList, this);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
     * Returns a string containing the stack list as an XML object.
     * @param    StackListXMLBigDecimal    the stack list
     * @return                             the XML formatted stack list
     */
    public String printXMLStackAsString(StackListXMLBigDecimal stackList) {
    	
    	try {

    		StringWriter writer = new StringWriter();
    		JAXBContext context = JAXBContext.newInstance(StackListXMLBigDecimal.class);
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
		
		    case PlateBigDecimal.PLATE_6WELL: return PlateBigDecimal.ROWS_6WELL;
		
		    case PlateBigDecimal.PLATE_12WELL: return PlateBigDecimal.ROWS_12WELL;
		    
		    case PlateBigDecimal.PLATE_24WELL: return PlateBigDecimal.ROWS_24WELL;
		    
		    case PlateBigDecimal.PLATE_48WELL: return PlateBigDecimal.ROWS_48WELL;
		    
		    case PlateBigDecimal.PLATE_96WELL: return PlateBigDecimal.ROWS_96WELL;
		    
		    case PlateBigDecimal.PLATE_384WELL: return PlateBigDecimal.ROWS_384WELL;
		    
		    case PlateBigDecimal.PLATE_1536WELL: return PlateBigDecimal.ROWS_1536WELL;
		    
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
		
		    case PlateBigDecimal.PLATE_6WELL: return PlateBigDecimal.COLUMNS_6WELL;
		
		    case PlateBigDecimal.PLATE_12WELL: return PlateBigDecimal.COLUMNS_12WELL;
		    
		    case PlateBigDecimal.PLATE_24WELL: return PlateBigDecimal.COLUMNS_24WELL;
		    
		    case PlateBigDecimal.PLATE_48WELL: return PlateBigDecimal.COLUMNS_48WELL;
		    
		    case PlateBigDecimal.PLATE_96WELL: return PlateBigDecimal.COLUMNS_96WELL;
		    
		    case PlateBigDecimal.PLATE_384WELL: return PlateBigDecimal.COLUMNS_384WELL;
		    
		    case PlateBigDecimal.PLATE_1536WELL: return PlateBigDecimal.COLUMNS_1536WELL;
		    
		    default: throw new IllegalArgumentException("Invalid plate type.");
		}
	}

}
