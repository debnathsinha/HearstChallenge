package com.cleveralgorithms.hearst;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is responsible for loading/manipulating the raw data
 *
 */
public class FileIO
{
	
	
	/**
	 * Write a string to disk
	 * 
	 * @param data
	 * @param file
	 * @throws IOException
	 */
	public static void writeStringToFile(String data, File file) 
			throws IOException
	{
		
			// write to disk
			FileWriter writer = new FileWriter(file);
			try {
				writer.write(data);
				writer.flush();
			} finally {
				writer.close();
			}
	}

	/**
	 * Load a file as a string, fast.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String fastLoadFileAsString(File file)
		throws IOException
	{
		StringBuffer buf = new StringBuffer();
		char [] dataBuffer = new char[1024 * 10]; // 10KB
				
		FileReader reader = new FileReader(file);
		
		try {
			int count = 0;
			while((count=reader.read(dataBuffer)) > 0) {
				buf.append(dataBuffer, 0, count);
			}			
		} finally {
			reader.close();
		}
		
		
		return buf.toString();
	}
}
