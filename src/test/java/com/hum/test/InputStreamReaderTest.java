package com.hum.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputStreamReaderTest {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		while (!(line = br.readLine()).equals("\n")) {
			System.out.println(line);
		}
	}
}
