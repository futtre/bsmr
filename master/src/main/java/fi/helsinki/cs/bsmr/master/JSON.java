package fi.helsinki.cs.bsmr.master;

/**
 * The MIT License
 * 
 * Copyright (c) 2010   Department of Computer Science, University of Helsinki
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Author Sampo Savolainen
 *
 */

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;


public class JSON 
{
	private static Logger logger = Logger.getLogger(JSON.class.getCanonicalName());
	
	public static Map<?,?> getJSONMapForJob(String s)
	{
		ObjectMapper mapper = new ObjectMapper();
		
		Map<?, ?> ret;
		try {
			ret =  mapper.readValue(s, Map.class);
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Could not parse JSON string '"+s+"'", e);
			return null; 
		}
		
		return ret;
	}
	
	public static Object parse(String s)
	{
		return getJSONMapForJob(s);
	}

	
	public static String toString(Object o)
	{
		
		ObjectMapper mapper = new ObjectMapper();
		
		String ret;
		try {
			ret = mapper.writeValueAsString(o);
		} catch(Exception e) {
			logger.log(Level.SEVERE, "Could not parse object to JSON '"+o+"'", e);
			return null; 
		}

		return ret;
	}
	
	
}
