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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.common.io.CharStreams;

import com.github.jessemull.microflex.integerflex.plate.PlateInteger;
import com.github.jessemull.microflex.integerflex.plate.StackInteger;
import com.github.jessemull.microflex.integerflex.plate.WellInteger;
import com.github.jessemull.microflex.integerflex.plate.WellSetInteger;

/**
 * Parses an input stream containing integer stacks, plates, well sets, 
 * and/or wells. Supports the following inputs:
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
 * Result tables and maps must be separated using a user defined delimiter or the
 * default tab delimiter. Input must be formatted as follows:
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
public class PlateReaderInteger extends BufferedReader {

    /*---------------------------- Private Fields ----------------------------*/
    
    private int ALPHA_BASE = 26;        // The number of possible characters for row indices
    private String delimiter = "\t";    // The delimiter
    
    private ResultListPOJOInteger resultsJSON;  // Result list POJO for JSON input
    private WellListPOJOInteger wellsJSON;      // Well list POJO for JSON input
    private WellSetListPOJOInteger setsJSON;    // Set list POJO for JSON input
    private PlateListPOJOInteger platesJSON;    // Plate list POJO for JSON input
    private StackListPOJOInteger stacksJSON;    // Stack list POJO for JSON input
    
    private ResultListXMLInteger resultsXML;   // Result list for XML input
    private WellListXMLInteger wellsXML;       // Well list for XML input
    private WellSetListXMLInteger setsXML;     // Set list for XML input
    private PlateListXMLInteger platesXML;     // Plate list for XML input
    private StackListXMLInteger stacksXML;     // Stack list for XML input
    
    private int indexResultsJSON = -1;     // Current index into the JSON result list
    private int indexWellsJSON = -1;       // Current index into the JSON well list
    private int indexSetsJSON = -1;        // Current index into the JSON set list
    private int indexPlatesJSON = -1;      // Current index into the JSON plate list
    private int indexStacksJSON = -1;      // Current index into the JSON stack list
    
    private int indexResultsXML = -1;     // Current index into the XML result list
    private int indexWellsXML = -1;       // Current index into the XML well list
    private int indexSetsXML = -1;        // Current index into the XML set list
    private int indexPlatesXML = -1;      // Current index into the XML plate list
    private int indexStacksXML = -1;      // Current index into the XML stack list
    
	private String labelRegex = "^\\s*[^ ]+\\s*$";              // Regex for matching plate map labels
	private String letters = "^[A-Z]+";                         // Regex for matching row index letters
	   
	/* Regex for matching JSON objects */

	private String columnHeadersRegex;    // Regex for matching plate map column headers
	private String tableHeaderRegex;      // Regex for matching table headers
	
    private Pattern labelPattern = Pattern.compile(labelRegex);            // Pattern for matching plate map labels
    private Pattern lettersPattern = Pattern.compile(letters);             // Pattern for matching row index letters
    private Pattern columnHeadersPattern;                                  // Pattern for matching plate map column headers
    private Pattern tableHeaderPattern;                                    // Pattern for matching table headers
    
    /* Paths to well, well set, plate and stack JSON/XML schemas */
    
    private final String JSON_RESULT_SCHEMA = "json_result_schema.json";
    private final String JSON_WELL_SCHEMA = "json_well_schema.json";
    private final String JSON_WELLSET_SCHEMA = "json_wellset_schema.json";
    private final String JSON_PLATE_SCHEMA = "json_plate_schema.json";
    private final String JSON_STACK_SCHEMA = "json_stack_schema.json";
    
    /* Default buffer size for a buffered reader is 8192 bytes/chars */
    
    int readAheadLimit = 8192 * 10000;
    
    /*----------------------------- Constructors -----------------------------*/

    /**
     * Constructs a new reader for a buffering character input stream using
     * the default buffer size.
     * @param    Reader    the source
     */
    public PlateReaderInteger(Reader source) {     
        
    	super(source);
        
		try {
			
			this.setDelimiter(this.delimiter);
	        
	        String input = CharStreams.toString(source);
	        
			this.validateJSON(input);
			this.validateXML(input);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Constructs a new reader for a buffering character input stream using
     * an input buffer of the specified size.
     * @param    Reader    reader source
     * @param    int       buffer size
     * @throws   IOException 
     * @throws   JAXBException 
     */
    public PlateReaderInteger(Reader source, int size) { 	
        
        super(source, size);
       
        try {
        	
            this.setDelimiter(this.delimiter);

            String input = CharStreams.toString(source);
     
			this.validateJSON(input);
		    this.validateXML(input);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
   
    }

    /**
     * Constructs a new reader for a buffering character input stream using
     * the default buffer size.
     * @param    InputStream    input stream source
     */
    public PlateReaderInteger(InputStream source) { 	
        
        super(new InputStreamReader(source));

	    try {
	    	
	        this.setDelimiter(this.delimiter);
	        
		    String input = CharStreams.toString(new InputStreamReader(source));
		    
			this.validateJSON(input);
	        this.validateXML(input);
		
	    } catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Constructs a new reader for a buffering character input stream using
     * an input buffer of the specified size.
     * @param    InputStream    input stream source
     * @param    int            buffer size
     */
    public PlateReaderInteger(InputStream source, int size) {
        
        super(new InputStreamReader(source), size);
        
        try {
        	
        	this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new InputStreamReader(source));

			this.validateJSON(input);        
	        this.validateXML(input);
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * Constructs a new reader for a buffering character input stream using
     * the default buffer size.
     * @param    InputStream    input stream source
     * @param    Charset        character set
     */
    public PlateReaderInteger(InputStream source, Charset set) {
        
        super(new BufferedReader(new InputStreamReader(source, set)));        
        
        try {
        	
        	this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new InputStreamReader(source, set));

			this.validateJSON(input);
	        this.validateXML(input);

        } catch(Exception e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Constructs a new reader for a buffering character input stream using
     * an input buffer of the specified size.
     * @param    InputStream    input stream source
     * @param    String         character set
     * @param    int            buffer size
     * @throws   UnsupportedEncodingException 
     */
    public PlateReaderInteger(InputStream source, String charsetName, int size) throws UnsupportedEncodingException {
        
        super(new BufferedReader(new InputStreamReader(source, charsetName), size));        
        
        try {
        	
        	this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new InputStreamReader(source, charsetName));

			this.validateJSON(input);
	        this.validateXML(input);
	        
		} catch(Exception e) {
			e.printStackTrace();
		}

    }

    /**
     * Constructs a new reader for a buffering character input stream using
     * the default buffer size.
     * @param    InputStream       input stream source
     * @param    CharsetDecoder    decoder
     */
    public PlateReaderInteger(InputStream source, CharsetDecoder decoder) {
        
        super(new InputStreamReader(source, decoder));        
        
        try {

        	this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new InputStreamReader(source, decoder));

			this.validateJSON(input);
			this.validateXML(input);
		
        } catch(Exception e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Constructs a new reader for a buffering character input stream using
     * an input buffer of the specified size.
     * @param    InputStream       input stream source
     * @param    CharsetDecoder    decoder
     * @param    int               buffer size
     */
    public PlateReaderInteger(InputStream source, CharsetDecoder decoder, int size) { 
        
        super(new InputStreamReader(source, decoder), size);        
        
        try {

        	this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new InputStreamReader(source, decoder));

			this.validateJSON(input);
			this.validateXML(input);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
        
    }
    
    /**
     * Constructs a new reader for a buffering character input stream using
     * the default buffer size.
     * @param    File    source file
     * @throws   IOException    
     * @throws   JAXBException 
     */
    public PlateReaderInteger(File source) throws IOException, JAXBException {

        super(new BufferedReader(new FileReader(source)));

        try {
        	
            this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new FileReader(source));
	        
            this.validateJSON(input);
	        this.validateXML(input);
	      
		} catch(ProcessingException e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Constructs a new reader for a buffering character input stream using
     * an input buffer of the specified size.
     * @param    File    source file
     * @param    int     buffer size
     * @throws   FileNotFoundException 
     */
    public PlateReaderInteger(File source, int size) throws FileNotFoundException {
        
        super(new FileReader(source), size);
        
        try {
	        this.setDelimiter(this.delimiter);
	
	        String input = CharStreams.toString(new FileReader(source));
	        
	        this.validateJSON(input);
	        this.validateXML(input);
	        
        } catch(Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * Constructs a new reader for a buffering character input stream using
     * the default buffer size.
     * @param    File    the file
     * @throws   FileNotFoundException 
     */
    public PlateReaderInteger(String fileName) throws FileNotFoundException {
        
        super(new FileReader(new File(fileName)));
        
        try {

			this.setDelimiter(this.delimiter);
	        
	        String input = CharStreams.toString(new FileReader(new File(fileName)));
	        
			this.validateJSON(input);
	        this.validateXML(input);
	        
		} catch(Exception e) {
			e.printStackTrace();
		}

    }
    
    /**
     * Constructs a new reader for a buffering character input stream using the
     * specified file source and charset name.
     * @param    File    the file
     * @param    int     buffer size
     * @throws   FileNotFoundException 
     * @throws   IOException 
     * @throws   JAXBException 
     */
    public PlateReaderInteger(String fileName, int size) throws FileNotFoundException {
        
        super(new FileReader(new File(fileName)), size);
        
        try {
        	
        	this.setDelimiter(this.delimiter);
            
            String input = CharStreams.toString(new FileReader(new File(fileName)));

			this.validateJSON(input);
	        this.validateXML(input);
	        
		} catch(Exception e) {
			e.printStackTrace();
		}

    }

    
    /*--------------------- Methods for Plate Map Input ----------------------*/
    
    /**
     * Returns true if the scanner has another plate map in its input. This 
     * method may block while waiting for input to scan. The scanner does not 
     * advance past any input.
     * @return    true if the scanner has another plate map in its input
     */
    public boolean hasNextMap() {
        
        try {
            
        	/* Check that more lines exist in the input buffer */
        	
        	if(!this.hasNextLine()) {
        		return false;
        	}
        	
            this.mark(readAheadLimit);
            
            /* Get the label */
        
    	    String label = this.nextLine();

    	    /* Skip blank lines */
    	
    	    while(label.trim().isEmpty()) {
    	    	
    	    	if(!this.hasNextLine()) {
    	    		return false;
    	    	}
    	    	
    	        label = this.nextLine();
    	    }

    	    /* Check for a valid label */
    	    
    	    Matcher labelMatcher = labelPattern.matcher(label);

    	    if(!labelMatcher.matches()) {	        
    		    return false;
         	}
      
    	    /* Check for valid column headers */

    	    String headers = this.nextLine();

    	    Matcher headersMatcher = columnHeadersPattern.matcher(headers);
    	    int columnNumber = 0;
    	
    	    if(!headersMatcher.matches()) {   	              		    
    	    	return false;       		    
          	} else {
    	    
    	    	String[] splitHeaders = headers.trim().split(delimiter);
    		
    		    for(int i = 0; i < splitHeaders.length; i++) {
    			    if(Integer.parseInt(splitHeaders[i]) != i + 1) {			        
    				    throw new IllegalArgumentException("Invalid column index.");
    			    }
    		    }
    		
    		    columnNumber = splitHeaders.length;	
    	    }

    	    /* Validate the rows and row indices */
    	
    	    int currentIndex = 0;
    	    String row = null;
        	
    	    while((row = this.nextLine().trim()) != null && !row.trim().isEmpty()) {

        	    String regex = "^\\s*[A-Za-z]+" + this.delimiter + "(\\s*(\\d+\\.?\\d+|Null)\\s*" + this.delimiter + "|\\s*(\\d+\\.?\\d*|Null))+\\s*\\n?$";
            	Pattern pattern = Pattern.compile(regex);
            	Matcher rowMatcher = pattern.matcher(row);

    		    if(!rowMatcher.lookingAt()) {
    		    	
    		    	if(currentIndex == 0) {
    		    		return false;
    		    	}
    		    	
    		    	break;
    		    	
    	        } else {
    	        
    	            String[] rowSplit = row.split(delimiter);
    	    	    int numValue = this.parseRow(rowSplit[0].trim());
    	    	
    	    	    if(numValue != currentIndex) {   
    	    	    	return false;
    	    	    } 

    	    	    if(rowSplit.length - 1 != columnNumber) {
    	    	    	return false;
    	    	    }
    	    	
    	            currentIndex++;  	    	
    	        }
    		    
    	    }

    	    /* Reset the buffer index and return true */

    	    this.reset();

    	    return true;
    	    
    	} catch(Exception e) {
    		
    	    /* Reset the buffer index and return true */
    		
    		try {
                this.reset();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }

            e.printStackTrace();
            
    		return false;
    	}
    }
    
    /**
     * Scans the next token of the input as a plate map. If the next token 
     * matches the plate map regular expression defined above then the token is 
     * converted into a plate object.
     * @return    plate containing the data from the map
     */
    public PlateInteger nextMap() {

    	try {
        
    		/* Check that more lines exist in the input buffer */
    		
    		if(!this.hasNextLine()) {
        		throw new IOException("No more lines in the input buffer.");
        	}
    		
    	    /* Mark the input stream */
    	    
    	    this.mark(readAheadLimit);
    	    
            /* Get the first line holding the label */
        
            String label = this.nextLine();

            /* Move past any blank lines */
        
            while(label.trim().isEmpty()) {
                label = this.nextLine().trim();
            }

            /* Check for a valid label */
        
            Matcher labelMatcher = labelPattern.matcher(label);
    
            if(!labelMatcher.matches()) {
                throw new InputMismatchException("Invalid plate map label.");
            }
      
            /* Check for valid column headers */
        
            String headers = this.nextLine();
            Matcher headersMatcher = columnHeadersPattern.matcher(headers);
            int columnNumber = 0;
        
            if(!headersMatcher.matches()) {
            
                throw new InputMismatchException("Invalid plate map column headers.");
            
            } else {
            
                String[] splitHeaders = headers.trim().split(delimiter);
            
                for(int i = 0; i < splitHeaders.length; i++) {
                    if(Integer.parseInt(splitHeaders[i]) != i + 1) {
                        throw new InputMismatchException("Invalid plate map column headers.");
                    }
                }
            
                columnNumber = splitHeaders.length; 
            }

            /* Validate the rows and row indices */
        
            int currentIndex = 0;
            List<WellInteger> wells = new ArrayList<WellInteger>();
            String row = null;
        
            while((row = this.nextLine()) != null && !row.trim().isEmpty()) {
                
            	String regex = "^\\s*[A-Za-z]+" + this.delimiter + "(\\s*(\\d+\\.?\\d+|Null)\\s*" + this.delimiter + "|\\s*(\\d+\\.?\\d*|Null))+\\s*\\n?$";
            	Pattern pattern = Pattern.compile(regex);
            	Matcher rowMatcher = pattern.matcher(row);
                
                if(!rowMatcher.matches()) {
                	
                    break;
                
                } else {
                
                    String[] rowSplit = row.split(delimiter);
                    int numValue = this.parseRow(rowSplit[0].trim());
                
                    if(numValue != currentIndex) {   
                        throw new InputMismatchException("Invalid plate map row index.");
                    } 

                    if(rowSplit.length - 1 != columnNumber) {
                        throw new InputMismatchException("Invalid plate map row length.");
                    }
                
                    for(int i = 1; i < rowSplit.length; i++) {
                    	
                    	if(!rowSplit[i].equals("Null")) {

                    		WellInteger well = new WellInteger(numValue, i);
                            int toAdd = Integer.parseInt(rowSplit[i].trim());
                            
                            well.add(toAdd);
                            wells.add(well);
                    	}
                    }
                
                    currentIndex++;             
                }
            }

            PlateInteger input = new PlateInteger(currentIndex, columnNumber, label);
        
            for(WellInteger well : wells) {
        	    input.addWells(well);
            }
        
            return input;      
     
    	} catch(Exception e) {
            
    		try {
                this.reset();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return null;
            }	

            e.printStackTrace();
            
            return null;
        }
    }
        
    /*----------------------- Methods for Table Input ------------------------*/
                    
    /**
     * Returns true if the scanner has another table of wells in its input. This 
     * method may block while waiting for input to scan. The scanner does not 
     * advance past any input.
     * @return    true if the scanner has another table of wells in 
     *            its input
     */
    public boolean hasNextTable() {
        
        try {

        	/* Check that more lines exist in the input buffer */
        	
        	if(!this.hasNextLine()) {
        		return false;
        	}
        	
            /* Mark the input stream */
            
            this.mark(readAheadLimit);
            
            /* Get the first line holding the label */

            String label = this.nextLine();
            
            while(label.trim().isEmpty()) {
            	
            	if(!this.hasNextLine()) {
            		return false;
            	}
            	
                label = this.nextLine();
            }
            
    	    /* Check for a valid label */
    	
    	    Matcher labelMatcher = labelPattern.matcher(label);
    	    
    	    if(!labelMatcher.matches()) {	        
    		    return false;
         	}
    	    
            /* Get the first line holding the label */
            
    	    String headers = this.nextLine();
    	    
    	    while(headers.trim().isEmpty()) {
    	        headers = this.nextLine();
    	    }

    	    /* Check for valid table headers */
    	    
    	    this.tableHeaderPattern = Pattern.compile(this.tableHeaderRegex);
    	    Matcher tableHeaderMatcher = tableHeaderPattern.matcher(headers);

    	    if(!tableHeaderMatcher.matches()) {
    	        return false;    	
    	    }

    	    String row = null;
    	    int index  = 0;
    	    while((row = this.nextLine()) != null && !row.trim().isEmpty()) {

    	    	String regex = "^\\s*[A-Za-z]+\\d+\\s*" + this.delimiter + "\\s*\\d+\\.?\\d+\\s*$";
    	    	Pattern pattern = Pattern.compile(regex);
    	    	
    	    	Matcher tableRowMatcher = pattern.matcher(row);

    	    	if(!tableRowMatcher.matches()) {
    	    		
    	    		if(index == 0) {
    	    			return false;
    	    		}
    	    		
    	    		break;
    	    	}
    	    
    	    	index++;
    	    }

            this.reset();
    	    
    	    return true;
    	    
    	} catch(Exception e) {
    		
    		/* Reset the index into the buffer on error */
    		
    		try {
                this.reset();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
    		
            e.printStackTrace();

    		return false;
    	}
    }
    
    /**
     * Scans the next token of the input as a table of wells. If the next token 
     * matches the table regular expression defined above then the token is 
     * converted into a well set.
     * @return    the converted table of wells  
     */
    public WellSetInteger nextTable() {

        try {

            /* Mark the input stream */
            
            this.mark(readAheadLimit);
            
            /* Get the label */
           
            String label = this.nextLine();
            
            while(label.trim().isEmpty()) {
                label = this.nextLine();
            }
            
    	    /* Check for a valid label */
    	
    	    Matcher labelMatcher = labelPattern.matcher(label);
    
    	    if(!labelMatcher.matches()) {	        
    		    throw new IllegalArgumentException("Invalid plate label.");
         	}
    	    
            /* Get the table headers */
            
    	    String headers = this.nextLine();
    	    
    	    while(headers.trim().isEmpty()) {
    	        headers = this.nextLine();    
    	    }
    	    
    	    /* Check for valid table headers */

    	    Matcher tableHeaderMatcher = tableHeaderPattern.matcher(headers);

    	    if(!tableHeaderMatcher.matches()) {
    	        throw new IllegalArgumentException("Invalid table headers.");    	
    	    }
    	    
    	    List<WellInteger> wells = new ArrayList<WellInteger>();
    	    String row = null;
    	    
    	    while((row = this.nextLine()) != null && !row.trim().isEmpty()) {
    	    	
    	    	String regex = "^\\s*[A-Za-z]+\\d+\\s*" + this.delimiter + "\\s*\\d+\\.?\\d+\\s*$";
    	    	Pattern pattern = Pattern.compile(regex);
    	    	
    	    	Matcher tableRowMatcher = pattern.matcher(row);
    	    	String[] split = row.split(this.delimiter);

    	    	if(!tableRowMatcher.matches()) {
    	    		break;
    	    	} else {
    	    		
    	    		WellInteger well = new WellInteger(split[0].trim());
    	            
    	    		int toAdd = Integer.parseInt(split[1].trim());
    	    		
    	            well.add(toAdd);    	    		
    	    		wells.add(well);
    	    	}
    	    	
    	    }
    	    
    	    return new WellSetInteger(wells, label);
    	    
    	} catch(Exception e) {
    		
    		/* Reset the index into the buffer on error */
    		
    		try {
                this.reset();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return null;
            }
    		
            e.printStackTrace();

    		return null;
    	}
    }
    
    /*---------------------- Methods for JSON Result Input ---------------------*/
    
    /**
     * Returns true if the the next result POJO in the list is a valid result.
     * @return    true if the next result POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextJSONResult() {    
        return this.indexResultsJSON >= 0 && this.indexResultsJSON < this.resultsJSON.size();
    }
    
    /**
     * Returns true if the the previous result POJO in the list is a valid result.
     * @return    true if the previous result POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousJSONResult() {    
        return this.indexResultsJSON > 0;
    }
    
    /**
     * Parses the next result in the result list to a well set object and returns the
     * result.
     * @return    the converted result object  
     */
    public WellSetInteger nextJSONResult() {

    	if(this.hasNextJSONResult()) {
    		
    		ResultPOJOInteger result = this.resultsJSON.get(indexResultsJSON++);
    	
        	WellSetInteger set = new WellSetInteger();
        	set.setLabel(result.getLabel());
        	
        	Map<String, Integer> map = result.getWells();
        	
        	for(Map.Entry<String, Integer> entry : map.entrySet()) {
        	
        		WellInteger well = new WellInteger(entry.getKey());
        		well.add(entry.getValue());
        		
        		set.add(well);
        	}
        	
        	return set;
        	
    	} else {
    		
    		return null;
    	
    	}             
    }
    
    /**
     * Parses the previous result in the result list to a well set object and returns the
     * result.
     * @return    the converted result object  
     */
    public WellSetInteger previousJSONResult() {

    	if(this.hasPreviousJSONResult()) {
    		
    		ResultPOJOInteger result = this.resultsJSON.get(--indexResultsJSON);
    	
        	WellSetInteger set = new WellSetInteger();
        	set.setLabel(result.getLabel());
        	
        	Map<String, Integer> map = result.getWells();
        	
        	for(Map.Entry<String, Integer> entry : map.entrySet()) {
        	
        		WellInteger well = new WellInteger(entry.getKey());
        		well.add(entry.getValue());
        		
        		set.add(well);
        	}
        	
        	return set;
        	
    	} else {
    		
    		return null;
    	
    	}
    }
    
    /**
     * Returns a list containing the result objects with an index greater than the
     * current index.
     * @return    list of converted result objects
     */
    public List<WellSetInteger> remainingJSONResults() {
        
        if(!this.hasNextJSONResult()) {
            return null;
        }

        List<WellSetInteger> remaining = new ArrayList<WellSetInteger>();
        
        while(this.hasNextJSONResult()) {
            remaining.add(this.nextJSONResult());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the result objects with an index less than the
     * current index.
     * @return    list of converted result objects
     */
    public List<WellSetInteger> spentJSONResults() {
        
        if(!this.hasPreviousJSONResult()) {
            return null;
        }

        List<WellSetInteger> spent = new ArrayList<WellSetInteger>();
        
        while(this.hasPreviousJSONResult()) {
            spent.add(this.previousJSONResult());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the JSON results in the list.
     * @return    list of all result objects
     */
    public List<WellSetInteger> allJSONResults() {

        List<WellSetInteger> allResults = new ArrayList<WellSetInteger>();
        
        for(ResultPOJOInteger result : this.resultsJSON) {

        	WellSetInteger toAddSet = new WellSetInteger();
        	toAddSet.setLabel(result.getLabel());
        	
        	Map<String, Integer> map = result.getWells();
        	
        	for(Map.Entry<String, Integer> entry : map.entrySet()) {
        	
        		WellInteger toAddWell = new WellInteger(entry.getKey());
        		toAddWell.add(entry.getValue());
        		
        		toAddSet.add(toAddWell);
        	}
        	
        	allResults.add(toAddSet);
        	
        }
        
        return allResults;
    }
    
    /*-------------------- Methods for XML Result Input --------------------*/
    
    /**
     * Returns true if the the next XML result in the list is a valid result.
     * @return    true if the next XML result is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextXMLResult() {    
        return this.indexResultsXML >= 0 && this.indexResultsXML < this.resultsXML.size();
    }
    
    /**
     * Returns true if the the previous XML result in the list is a valid result.
     * @return    true if the previous XML result is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousXMLResult() {    
        return this.indexResultsXML > 0;
    }
    
    /**
     * Parses the next result in the result list to a well set object and 
     * returns the result.
     * @return    the converted result object  
     */
    public WellSetInteger nextXMLResult() {
        return this.hasNextXMLResult() ? 
                this.resultsXML.get(indexResultsXML++).toWellSetObject() : null;
    }
    
    /**
     * Parses the previous result in the result list to a well set object 
     * and returns the result.
     * @return    the converted result object  
     */
    public WellSetInteger previousXMLResult() {
        return this.hasPreviousXMLResult() ? 
                this.resultsXML.get(--indexResultsXML).toWellSetObject() : null;
    }
    
    /**
     * Returns a list containing the result objects with an index greater than the
     * current index.
     * @return    list of converted result objects
     */
    public List<WellSetInteger> remainingXMLResults() {
        
        if(!this.hasNextXMLResult()) {
            return null;
        }

        List<WellSetInteger> remaining = new ArrayList<WellSetInteger>();
        
        while(this.hasNextXMLResult()) {
            remaining.add(this.nextXMLResult());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the result objects with an index less than the
     * current index.
     * @return    list of converted result objects
     */
    public List<WellSetInteger> spentXMLResults() {
        
        if(!this.hasPreviousXMLResult()) {
            return null;
        }

        List<WellSetInteger> spent = new ArrayList<WellSetInteger>();
        
        while(this.hasPreviousXMLResult()) {
            spent.add(this.previousXMLResult());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the XML results in the list.
     * @return    list of all converted result objects
     */
    public List<WellSetInteger> allXMLResults() {

        List<WellSetInteger> allWellResults = new ArrayList<WellSetInteger>();
        
        for(ResultXMLInteger result: this.resultsXML) {
            allWellResults.add(result.toWellSetObject());
        }
        
        return allWellResults;
    }
    
    /*---------------------- Methods for JSON Well Input ---------------------*/
    
    /**
     * Returns true if the the next well POJO in the list is a valid well.
     * @return    true if the next well POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextJSONWell() {    
        return this.indexWellsJSON >= 0 && this.indexWellsJSON < this.wellsJSON.size();
    }
    
    /**
     * Returns true if the the previous well POJO in the list is a valid well.
     * @return    true if the previous well POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousJSONWell() {    
        return this.indexWellsJSON > 0;
    }
    
    /**
     * Parses the next well in the well list to a well object and returns the
     * result.
     * @return    the converted well object  
     */
    public WellInteger nextJSONWell() {
        return this.hasNextJSONWell() ? 
                this.wellsJSON.get(indexWellsJSON++).toWellObject() : null;
    }
    
    /**
     * Parses the previous well in the well list to a well object and returns the
     * result.
     * @return    the converted well object  
     */
    public WellInteger previousJSONWell() {
        return this.hasPreviousJSONWell() ? 
                this.wellsJSON.get(--indexWellsJSON).toWellObject() : null;
    }
    
    /**
     * Returns a list containing the well objects with an index greater than the
     * current index.
     * @return    list of converted well objects
     */
    public List<WellInteger> remainingJSONWells() {
        
        if(!this.hasNextJSONWell()) {
            return null;
        }

        List<WellInteger> remaining = new ArrayList<WellInteger>();
        
        while(this.hasNextJSONWell()) {
            remaining.add(this.nextJSONWell());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the well objects with an index less than the
     * current index.
     * @return    list of converted well objects
     */
    public List<WellInteger> spentJSONWells() {
        
        if(!this.hasPreviousJSONWell()) {
            return null;
        }

        List<WellInteger> spent = new ArrayList<WellInteger>();
        
        while(this.hasPreviousJSONWell()) {
            spent.add(this.previousJSONWell());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the JSON wells in the list.
     * @return    list of all well objects
     */
    public List<WellInteger> allJSONWells() {

        List<WellInteger> allWells = new ArrayList<WellInteger>();
        
        for(WellPOJOInteger well : this.wellsJSON) {
            allWells.add(well.toWellObject());
        }
        
        return allWells;
    }
    
    /*---------------------- Methods for XML Well Input ----------------------*/
    
    /**
     * Returns true if the the next XML well in the list is a valid well.
     * @return    true if the next XML well is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextXMLWell() {    
        return this.indexWellsXML >= 0 && this.indexWellsXML < this.wellsXML.size();
    }
    
    /**
     * Returns true if the the previous XML well in the list is a valid well.
     * @return    true if the previous XML well is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousXMLWell() {    
        return this.indexWellsXML > 0;
    }
    
    /**
     * Parses the next well in the well list to a well object and returns the
     * result.
     * @return    the converted well object  
     */
    public WellInteger nextXMLWell() {
        return this.hasNextXMLWell() ? 
                this.wellsXML.get(indexWellsXML++).toWellObject() : null;
    }
    
    /**
     * Parses the previous well in the well list to a well object and returns the
     * result.
     * @return    the converted well object  
     */
    public WellInteger previousXMLWell() {
        return this.hasPreviousXMLWell() ? 
                this.wellsXML.get(--indexWellsXML).toWellObject() : null;
    }
    
    /**
     * Returns a list containing the well objects with an index greater than the
     * current index.
     * @return    list of converted well objects
     */
    public List<WellInteger> remainingXMLWells() {
        
        if(!this.hasNextXMLWell()) {
            return null;
        }

        List<WellInteger> remaining = new ArrayList<WellInteger>();
        
        while(this.hasNextXMLWell()) {
            remaining.add(this.nextXMLWell());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the well objects with an index less than the
     * current index.
     * @return    list of converted well objects
     */
    public List<WellInteger> spentXMLWells() {
        
        if(!this.hasPreviousXMLWell()) {
            return null;
        }

        List<WellInteger> spent = new ArrayList<WellInteger>();
        
        while(this.hasPreviousXMLWell()) {
            spent.add(this.previousXMLWell());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the XML wells in the list.
     * @return    list of all well objects
     */
    public List<WellInteger> allXMLWells() {

        List<WellInteger> allWells = new ArrayList<WellInteger>();
        
        for(WellXMLInteger well : this.wellsXML) {
            allWells.add(well.toWellObject());
        }
        
        return allWells;
    }
    
    /*-------------------- Methods for JSON Well Set Input -------------------*/
    
    /**
     * Returns true if the the next set POJO in the list is a valid set.
     * @return    true if the next set POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextJSONSet() {   
        return this.indexSetsJSON >= 0 && this.indexSetsJSON < this.setsJSON.size();
    }
    
    /**
     * Returns true if the the previous set POJO in the list is a valid set.
     * @return    true if the previous set POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousJSONSet() {    
        return this.indexSetsJSON > 0;
    }
    
    /**
     * Parses the next set in the set list to a well set object and returns the
     * result.
     * @return    the converted well set object  
     */
    public WellSetInteger nextJSONSet() {
        return this.hasNextJSONSet() ? 
                this.setsJSON.get(indexSetsJSON++).toWellSetObject() : null;
    }
    
    /**
     * Parses the previous set in the set list to a well set object and returns the
     * result.
     * @return    the converted well set object  
     */
    public WellSetInteger previousJSONSet() {
        return this.hasPreviousJSONSet() ? 
                this.setsJSON.get(--indexSetsJSON).toWellSetObject() : null;
    }
    
    /**
     * Returns a list containing the set objects with an index greater than the
     * current index.
     * @return    list of converted set objects
     */
    public List<WellSetInteger> remainingJSONSets() {
        
        if(!this.hasNextJSONSet()) {
            return null;
        }

        List<WellSetInteger> remaining = new ArrayList<WellSetInteger>();
        
        while(this.hasNextJSONSet()) {
            remaining.add(this.nextJSONSet());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the set objects with an index less than the
     * current index.
     * @return    list of converted set objects
     */
    public List<WellSetInteger> spentJSONSets() {
        
        if(!this.hasPreviousJSONSet()) {
            return null;
        }

        List<WellSetInteger> spent = new ArrayList<WellSetInteger>();
        
        while(this.hasPreviousJSONSet()) {
            spent.add(this.previousJSONSet());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the JSON sets in the list.
     * @return    list of all set objects
     */
    public List<WellSetInteger> allJSONSets() {

        List<WellSetInteger> allSets = new ArrayList<WellSetInteger>();
        
        for(WellSetPOJOInteger set : this.setsJSON) {
            allSets.add(set.toWellSetObject());
        }
        
        return allSets;
    }
    
    /*-------------------- Methods for XML Well Set Input --------------------*/
    
    /**
     * Returns true if the the next XML well set in the list is a valid well set.
     * @return    true if the next XML well set is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextXMLSet() {    
        return this.indexSetsXML >= 0 && this.indexSetsXML < this.setsXML.size();
    }
    
    /**
     * Returns true if the the previous XML well set in the list is a valid well set.
     * @return    true if the previous XML well set is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousXMLSet() {    
        return this.indexSetsXML > 0;
    }
    
    /**
     * Parses the next well set in the well set list to a well set object and 
     * returns the result.
     * @return    the converted well set object  
     */
    public WellSetInteger nextXMLSet() {
        return this.hasNextXMLSet() ? 
                this.setsXML.get(indexSetsXML++).toWellSetObject() : null;
    }
    
    /**
     * Parses the previous well set in the well set list to a well set object 
     * and returns the result.
     * @return    the converted well set object  
     */
    public WellSetInteger previousXMLSet() {
        return this.hasPreviousXMLSet() ? 
                this.setsXML.get(--indexSetsXML).toWellSetObject() : null;
    }
    
    /**
     * Returns a list containing the well set objects with an index greater than the
     * current index.
     * @return    list of converted well set objects
     */
    public List<WellSetInteger> remainingXMLSets() {
        
        if(!this.hasNextXMLSet()) {
            return null;
        }

        List<WellSetInteger> remaining = new ArrayList<WellSetInteger>();
        
        while(this.hasNextXMLSet()) {
            remaining.add(this.nextXMLSet());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the well set objects with an index less than the
     * current index.
     * @return    list of converted well set objects
     */
    public List<WellSetInteger> spentXMLSets() {
        
        if(!this.hasPreviousXMLSet()) {
            return null;
        }

        List<WellSetInteger> spent = new ArrayList<WellSetInteger>();
        
        while(this.hasPreviousXMLSet()) {
            spent.add(this.previousXMLSet());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the XML well sets in the list.
     * @return    list of all well set objects
     */
    public List<WellSetInteger> allXMLSets() {

        List<WellSetInteger> allWellSets = new ArrayList<WellSetInteger>();
        
        for(WellSetXMLInteger wellset: this.setsXML) {
            allWellSets.add(wellset.toWellSetObject());
        }
        
        return allWellSets;
    }
    
    /*---------------------- Methods for JSON Plate Input --------------------*/
    
    /**
     * Returns true if the the next plate POJO in the list is a valid plate.
     * @return    true if the next plate POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextJSONPlate() {    
        return this.indexPlatesJSON >= 0 && this.indexPlatesJSON < this.platesJSON.size();
    }
    
    /**
     * Returns true if the the previous plate POJO in the list is a valid plate.
     * @return    true if the previous plate POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousJSONPlate() {    
        return this.indexPlatesJSON > 0;
    }
    
    /**
     * Parses the next plate in the plate list to a plate object and returns the
     * result.
     * @return    the converted plate object  
     */
    public PlateInteger nextJSONPlate() {
        return this.hasNextJSONPlate() ? 
                this.platesJSON.get(indexPlatesJSON++).toPlateObject() : null;
    }
    
    /**
     * Parses the previous plate in the plate list to a plate object and returns the
     * result.
     * @return    the converted plate object  
     */
    public PlateInteger previousJSONPlate() {
        return this.hasPreviousJSONPlate() ? 
                this.platesJSON.get(--indexPlatesJSON).toPlateObject() : null;
    }
    
    /**
     * Returns a list containing the plate objects with an index greater than the
     * current index.
     * @return    list of converted plate objects
     */
    public List<PlateInteger> remainingJSONPlates() {
        
        if(!this.hasNextJSONPlate()) {
            return null;
        }

        List<PlateInteger> remaining = new ArrayList<PlateInteger>();
        
        while(this.hasNextJSONPlate()) {
            remaining.add(this.nextJSONPlate());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the plate objects with an index less than the
     * current index.
     * @return    list of converted plate objects
     */
    public List<PlateInteger> spentJSONPlates() {
        
        if(!this.hasPreviousJSONPlate()) {
            return null;
        }

        List<PlateInteger> spent = new ArrayList<PlateInteger>();
        
        while(this.hasPreviousJSONPlate()) {
            spent.add(this.previousJSONPlate());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the JSON plates in the list.
     * @return    list of all plate objects
     */
    public List<PlateInteger> allJSONPlates() {

        List<PlateInteger> allPlates = new ArrayList<PlateInteger>();
        
        for(PlatePOJOInteger plate : this.platesJSON) {
            allPlates.add(plate.toPlateObject());
        }
        
        return allPlates;
    }
    
    /*---------------------- Methods for XML Plate Input ---------------------*/
    
    /**
     * Returns true if the the next XML plate in the list is a valid plate.
     * @return    true if the next XML plate is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextXMLPlate() {    
        return this.indexPlatesXML >= 0 && this.indexPlatesXML < this.platesXML.size();
    }
    
    /**
     * Returns true if the the previous XML plate in the list is a valid plate.
     * @return    true if the previous XML plate is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousXMLPlate() {    
        return this.indexPlatesXML > 0;
    }
    
    /**
     * Parses the next plate in the plate list to a plate object and returns the
     * result.
     * @return    the converted plate object  
     */
    public PlateInteger nextXMLPlate() {
        return this.hasNextXMLPlate() ? 
                this.platesXML.get(indexPlatesXML++).toPlateObject() : null;
    }
    
    /**
     * Parses the previous plate in the plate list to a plate object and returns the
     * result.
     * @return    the converted plate object  
     */
    public PlateInteger previousXMLPlate() {
        return this.hasPreviousXMLPlate() ? 
                this.platesXML.get(--indexPlatesXML).toPlateObject() : null;
    }
    
    /**
     * Returns a list containing the plate objects with an index greater than the
     * current index.
     * @return    list of converted plate objects
     */
    public List<PlateInteger> remainingXMLPlates() {
        
        if(!this.hasNextXMLPlate()) {
            return null;
        }

        List<PlateInteger> remaining = new ArrayList<PlateInteger>();
        
        while(this.hasNextXMLPlate()) {
            remaining.add(this.nextXMLPlate());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the plate objects with an index less than the
     * current index.
     * @return    list of converted plate objects
     */
    public List<PlateInteger> spentXMLPlates() {
        
        if(!this.hasPreviousXMLPlate()) {
            return null;
        }

        List<PlateInteger> spent = new ArrayList<PlateInteger>();
        
        while(this.hasPreviousXMLPlate()) {
            spent.add(this.previousXMLPlate());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the XML plates in the list.
     * @return    list of all plate objects
     */
    public List<PlateInteger> allXMLPlates() {

        List<PlateInteger> allPlates = new ArrayList<PlateInteger>();
        
        for(PlateXMLInteger plate : this.platesXML) {
            allPlates.add(plate.toPlateObject());
        }
        
        return allPlates;
    }
    
    /*------------------ Methods for JSON Plate Stack Input ------------------*/
    
    /**
     * Returns true if the the next stack POJO in the list is a valid stack.
     * @return    true if the next stack POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextJSONStack() {    
        return this.indexStacksJSON >= 0 && this.indexStacksJSON < this.stacksJSON.size();
    }
    
    /**
     * Returns true if the the previous stack POJO in the list is a valid stack.
     * @return    true if the previous stack POJO is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousJSONStack() {    
        return this.indexStacksJSON > 0;
    }
    
    /**
     * Parses the next stack in the stack list to a stack object and returns the
     * result.
     * @return    the converted stack object  
     */
    public StackInteger nextJSONStack() {
        return this.hasNextJSONStack() ? 
                this.stacksJSON.get(indexStacksJSON++).toStackObject() : null;
    }
    
    /**
     * Parses the previous stack in the stack list to a stack object and returns the
     * result.
     * @return    the converted stack object  
     */
    public StackInteger previousJSONStack() {
        return this.hasPreviousJSONStack() ? 
                this.stacksJSON.get(--indexStacksJSON).toStackObject() : null;
    }
    
    /**
     * Returns a list containing the stack objects with an index greater than the
     * current index.
     * @return    list of converted stack objects
     */
    public List<StackInteger> remainingJSONStacks() {
        
        if(!this.hasNextJSONStack()) {
            return null;
        }

        List<StackInteger> remaining = new ArrayList<StackInteger>();
        
        while(this.hasNextJSONStack()) {
            remaining.add(this.nextJSONStack());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the stack objects with an index less than the
     * current index.
     * @return    list of converted stack objects
     */
    public List<StackInteger> spentJSONStacks() {
        
        if(!this.hasPreviousJSONStack()) {
            return null;
        }

        List<StackInteger> spent = new ArrayList<StackInteger>();
        
        while(this.hasPreviousJSONStack()) {
            spent.add(this.previousJSONStack());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the JSON stacks in the list.
     * @return    list of all stack objects
     */
    public List<StackInteger> allJSONStacks() {

        List<StackInteger> allStacks = new ArrayList<StackInteger>();
        
        for(StackPOJOInteger stack : this.stacksJSON) {
            allStacks.add(stack.toStackObject());
        }
        
        return allStacks;
    }
    
    /*------------------ Methods for XML Plate Stack Input -------------------*/
    
    /**
     * Returns true if the the next XML stack in the list is a valid stack.
     * @return    true if the next XML stack is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasNextXMLStack() {    
        return this.indexStacksXML >= 0 && this.indexStacksXML < this.stacksXML.size();
    }
    
    /**
     * Returns true if the the previous XML stack in the list is a valid stack.
     * @return    true if the previous XML stack is valid
     * @throws    IOException 
     * @throws    ProcessingException 
     */
    public boolean hasPreviousXMLStack() {    
        return this.indexStacksXML > 0;
    }
    
    /**
     * Parses the next stack in the stack list to a stack object and returns the
     * result.
     * @return    the converted stack object  
     */
    public StackInteger nextXMLStack() {
        return this.hasNextXMLStack() ? 
                this.stacksXML.get(indexStacksXML++).toStackObject() : null;
    }
    
    /**
     * Parses the previous stack in the stack list to a stack object and returns the
     * result.
     * @return    the converted stack object  
     */
    public StackInteger previousXMLStack() {
        return this.hasPreviousXMLStack() ? 
                this.stacksXML.get(--indexStacksXML).toStackObject() : null;
    }
    
    /**
     * Returns a list containing the stack objects with an index greater than the
     * current index.
     * @return    list of converted stack objects
     */
    public List<StackInteger> remainingXMLStacks() {
        
        if(!this.hasNextXMLStack()) {
            return null;
        }

        List<StackInteger> remaining = new ArrayList<StackInteger>();
        
        while(this.hasNextXMLStack()) {
            remaining.add(this.nextXMLStack());
        }
        
        return remaining;
    }
    
    /**
     * Returns a list containing the stack objects with an index less than the
     * current index.
     * @return    list of converted stack objects
     */
    public List<StackInteger> spentXMLStacks() {
        
        if(!this.hasPreviousXMLStack()) {
            return null;
        }

        List<StackInteger> spent = new ArrayList<StackInteger>();
        
        while(this.hasPreviousXMLStack()) {
            spent.add(this.previousXMLStack());
        }
        
        return spent;
    }
    
    /**
     * Returns a list containing all the XML stacks in the list.
     * @return    list of all stack objects
     */
    public List<StackInteger> allXMLStacks() {

        List<StackInteger> allStacks = new ArrayList<StackInteger>();
        
        for(StackXMLInteger stack : this.stacksXML) {
            allStacks.add(stack.toStackObject());
        }
        
        return allStacks;
    }
    
    /*--------------------- Methods for validating input ---------------------*/
    
    /**
     * Sets the delimiter.
     * @param   String    the delimiter
     */
    public void setDelimiter(String delim) {		

    	try {

	    	this.delimiter = delim;
	     
	        this.columnHeadersRegex = "^\\s*" + this.delimiter + "?\\s*(\\s*\\d+)\\s*(" + this.delimiter + "\\s*\\d+\\s*)*$";
	    	this.tableHeaderRegex = "^\\s*Index\\s*" + this.delimiter + "\\s*Value\\s*$";

	        this.columnHeadersPattern = Pattern.compile(columnHeadersRegex); 
	        this.tableHeaderPattern = Pattern.compile(tableHeaderRegex);
	        
    	} catch(Exception e) {
    		
    		this.delimiter = "\t";
    		throw new IllegalArgumentException("Invalid delimiter.");
    	
    	}
        
    }
    
    /**
     * Returns the delimiter.
     * @return    the delimiter
     */
    public String getDelimiter() {		
    	return this.delimiter;
    }
    
    /**
     * Converts a row ID to an integer value.
     * @param    String    the row as a string
     * @return             the row as an integer value
     */
    private int parseRow(String rowString) {
        
        int rowInt = 0;
        int baseIndex = 0;
        
        String upper = rowString.toUpperCase().trim();
        Matcher lettersMatcher = lettersPattern.matcher(upper);
        
        if(lettersMatcher.find()) {
            
            String letters = lettersMatcher.group(0);
            rowInt = letters.charAt(letters.length() - 1) - 65;
            
            for(int i = letters.length() - 1; i >= 0; i--) {
                rowInt += (baseIndex++ * (letters.charAt(i) - 65 + 1) * ALPHA_BASE);                
            }
            
            return rowInt;
            
        } else {           
            throw new IllegalArgumentException("Invalid row ID: " + rowString);
        }
        
    }
    
    /**
     * Validates a string against a JSON schema.
     * @param    String    file path to the JSON schema
     * @param    String    string to validate
     * @return             true when string is a valid JSON object
     */
    private boolean validateSchema(String schemaPath, String input) {
        
        try {
        	
        	ClassLoader classLoader = getClass().getClassLoader();
        	File schemaFile = new File(classLoader.getResource(schemaPath).getFile());

        	/* Convert the string to a JSON node */

            JsonNode rootNode = JsonLoader.fromString(input);

            /* Get the schema */

            JsonNode schemaNode = JsonLoader.fromFile(schemaFile);
            
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            return schema.validInstance(rootNode);
            
        } catch (Exception e) {
            
            return false;
            
        }
    }
    
    /**
     * Sets the boolean values for JSON schema validation.
     * @param    String    string to validate
     * @throws IOException 
     * @throws JsonProcessingException 
     */
    private void validateJSON(String input) throws ProcessingException, IOException {

    	if(this.validateSchema(this.JSON_RESULT_SCHEMA, input)) {
    		ObjectMapper mapper = new ObjectMapper();
    		this.resultsJSON = mapper.readValue(this, ResultListPOJOInteger.class);
    		this.indexResultsJSON = 0;
    		return;
    	}
    	
    	if(this.validateSchema(this.JSON_WELL_SCHEMA, input)) {
    		ObjectMapper mapper = new ObjectMapper();
    		this.wellsJSON = mapper.readValue(this, WellListPOJOInteger.class);
    		this.indexWellsJSON = 0;
    		return;
    	}

    	if(this.validateSchema(this.JSON_WELLSET_SCHEMA, input)) {
    		ObjectMapper mapper = new ObjectMapper();
    		this.setsJSON = mapper.readValue(this, WellSetListPOJOInteger.class);
    		this.indexSetsJSON = 0;
    		return;
    	}

    	if(this.validateSchema(this.JSON_PLATE_SCHEMA, input)) {
    		ObjectMapper mapper = new ObjectMapper();
    		this.platesJSON = mapper.readValue(this, PlateListPOJOInteger.class);
    		this.indexPlatesJSON = 0;
    		return;
    	}

    	if(this.validateSchema(this.JSON_STACK_SCHEMA, input)) {
    		ObjectMapper mapper = new ObjectMapper();
    		this.stacksJSON = mapper.readValue(this, StackListPOJOInteger.class);
    		this.indexStacksJSON = 0;
    		return;
    	}
    }
    
    /**
     * Sets the boolean values for XML schema validation.
     * @param    String    string to validate
     * @throws   JAXBException 
     */
    private void validateXML(String input) throws JAXBException {
        
    	try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ResultListXMLInteger.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(input);
            this.resultsXML = (ResultListXMLInteger) unmarshaller.unmarshal(reader);
            this.indexResultsXML = 0;
        } catch(Exception e) {}
    	
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WellListXMLInteger.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(input);
            this.wellsXML = (WellListXMLInteger) unmarshaller.unmarshal(reader);
            this.indexWellsXML = 0;
        } catch(Exception e) {}
        
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(WellSetListXMLInteger.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(input);
            this.setsXML = (WellSetListXMLInteger) unmarshaller.unmarshal(reader);
            this.indexSetsXML = 0;
        } catch(Exception e) {}
        
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PlateListXMLInteger.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(input);
            this.platesXML = (PlateListXMLInteger) unmarshaller.unmarshal(reader);
            this.indexPlatesXML = 0;
        } catch(Exception e) {}
        
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(StackListXMLInteger.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(input);
            this.stacksXML = (StackListXMLInteger) unmarshaller.unmarshal(reader);
            this.indexStacksXML = 0;
        } catch(Exception e) {}
    }
    
    /**
     * Returns the next line in the input buffer. If the next line is null this
     * method throws a NoSuchElementException.
     * @return   next line in the input buffer
     * @throws   NoSuchElementException
     */
    private String nextLine() {
        
        String nextLine = null;
        
        try {
            nextLine = this.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        if(nextLine == null) {
            throw new NoSuchElementException("There are no more lines in the input buffer.");
        } else {
            return nextLine;
        }
    }

    /**
     * Returns true if there is another line in the input buffer.
     * @return    false if no more lines exist in the buffer
     */
    private boolean hasNextLine() {
    	
    	String nextLine = null;
        
        try {
        	
        	this.mark(readAheadLimit);
            
        	nextLine = this.readLine();           

        	this.reset();

        	return nextLine != null;
        	
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}
