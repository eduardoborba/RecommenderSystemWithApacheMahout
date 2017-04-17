package com.recommender.convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDataConverter {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("data/ua.test"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/a_test.csv"));
		
		String line;
		System.out.println("Reading ...");
		while((line = br.readLine()) != null) {
			String[] values = line.split("\\t", -1);
			bw.write(values[0] + "," + values[1] + "," + values[2] + "\n");
		}
		br.close();
		bw.close();
		System.out.println("Finished!");
	}
}
