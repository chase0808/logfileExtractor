package com.dci.logfileExtractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LogfileExtractor {
	
	public static void skip(FileReader in, int number) throws IOException {
		for( int i = 0; i < number; i++ ) {
			int c = in.read();
		}
	}
	
	public static ArrayList<Transaction> infoExtractor(String filename) throws IOException {
		File inputFile = new File(filename);
		FileReader in = new FileReader(inputFile);
		
		int c;
		StringBuilder str = new StringBuilder();
		int status = 1;
		Transaction tr = new Transaction();
		ArrayList<Transaction> arr = new ArrayList<Transaction>();
		while( (c = in.read()) != -1 ) {
			char temp = (char)c;
			switch(status) {
				case 1:	if( temp == ' ') {
							tr.setIpAddress(str.toString());
							status = 2;
							str = new StringBuilder();
							break;
						}
						str.append(temp);
						break;
				case 2: if( temp == ' ') {
							tr.setNumberI(str.toString());
							status = 3;
							str = new StringBuilder();
							skip(in, 4);
							break;
						}
						str.append(temp);
						break;
				case 3: if( temp == ']') {
							str.append(temp);
							tr.setTimeStamp(str.toString());
							status = 4;
							str = new StringBuilder();
							skip(in,2);
							break;
						}
						str.append(temp);
						break;
				case 4: if( temp == '"') {
							tr.setMethodURL(str.toString());
							status = 5;
							str = new StringBuilder();
							skip(in,1);
							break;
						}
						str.append(temp);
						break;
				case 5: if( temp == ' ') {
							tr.setNumberII(str.toString());
							status = 6;
							str = new StringBuilder();
							break;
						}
						str.append(temp);
						break;
				case 6: if( temp == ' ') {
							tr.setNumberIII(str.toString());
							status = 7;
							str = new StringBuilder();
							skip(in,1);
							break;
						}
						str.append(temp);
						break;
				case 7: if( temp == '"') {
							tr.setURL(str.toString());
							status = 8;
							str = new StringBuilder();
							skip(in, 2);
							break;
						}
						str.append(temp);
						break;
					

				case 8: if( temp == '"') {
							tr.setConfigureInfo(str.toString());
							status = 1;
							str = new StringBuilder();
							arr.add(tr);
							tr = new Transaction(); 
							break;
						}
						str.append(temp);
						break;
			}
		}
		
		in.close();
		System.out.println(filename + " Reading Successfully!");
		return arr;		
	}
	public static void main(String[] args) throws IOException {
		
		ArrayList<Transaction> result = infoExtractor("access_log.Q111041100");
		System.out.println("DONE!");
	}
}
