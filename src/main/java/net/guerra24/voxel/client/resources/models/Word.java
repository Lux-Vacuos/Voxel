/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.guerra24.voxel.client.resources.models;

import java.util.ArrayList;
import java.util.List;

public class Word {

	private List<Character> characters = new ArrayList<Character>();
	private double width = 0;
	private double fontSize;

	/**
	 * Create a new empty word.
	 * 
	 * @param fontSize
	 *            - the font size of the text which this word is in.
	 */
	protected Word(double fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Adds a character to the end of the current word and increases the
	 * screen-space width of the word.
	 * 
	 * @param character
	 *            - the character to be added.
	 */
	protected void addCharacter(Character character) {
		characters.add(character);
		width += character.getxAdvance() * fontSize;
	}

	/**
	 * @return The list of characters in the word.
	 */
	protected List<Character> getCharacters() {
		return characters;
	}

	/**
	 * @return The width of the word in terms of screen size.
	 */
	protected double getWordWidth() {
		return width;
	}

}
