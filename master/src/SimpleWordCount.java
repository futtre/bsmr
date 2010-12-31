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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class SimpleWordCount
{
	private Map<String, MutableInteger> counts;
	private BufferedReader br;
	private String regex;
	
	public SimpleWordCount(InputStream is, String regex)
	{
		this.regex = regex;
		this.counts = new HashMap<String, MutableInteger>();
		this.br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	}
	
	public void count() throws IOException
	{
		String s;
		
		while ( (s = br.readLine()) != null) {
			String [] tmp = s.split(regex);
		
			for (String word : tmp) {
				MutableInteger mi = counts.get(word);
				if (mi == null) {
					mi = new MutableInteger();
					counts.put(word, mi);
				}
				mi.n++;
			}
			
		}
	}
	
	public class MutableInteger
	{
		public int n;
		
		public MutableInteger()
		{
			n = 0;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		String file = "data/data.txt.gz";
		FileInputStream fis = new FileInputStream(file);
		GZIPInputStream gzis = new GZIPInputStream(fis);
		SimpleWordCount swc = new SimpleWordCount(gzis, "\\W+");
		
		System.out.println("Solving incredibly difficult differential equations..");
		swc.count();
		
		System.out.println("Done! Write the name of the key to check word count");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		String key;
		System.out.print("> "); System.out.flush();
		while ( (key = br.readLine()) != null) {
			
			MutableInteger mi = swc.counts.get(key);
			System.out.println("'"+key+"': "+(mi != null ? mi.n : "N/A"));
			
			System.out.print("> "); System.out.flush();
		}

	}

}
