package com.hackhalo2.util;

import java.nio.charset.Charset;

/**
 * A helper class for Input/Output related things
 * @author hackhalo2
 *
 */
public class IOHandler {
	public final static Charset UTF8 = Charset.forName("UTF-8");

	private IOHandler() { }
	
	/**
	 * Decodes an int888 packed int into a float array
	 * @param rgb the int888 packed int
	 * @return the float array of the packed int
	 */
	public static float[] decodeRGB(int rgb) {
		float[] temp = new float[3];
		temp[0] = (float)(rgb >> 16 & 255) / 255.0F; //R
		temp[1] = (float)(rgb >> 8 & 255) / 255.0F;  //G
		temp[2] = (float)(rgb & 255) / 255.0F;       //B
		
		return temp;
	}

}
