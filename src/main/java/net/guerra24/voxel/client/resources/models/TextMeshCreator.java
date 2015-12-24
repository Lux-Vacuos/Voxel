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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextMeshCreator {

	protected static final double LINE_HEIGHT = 0.03f;
	protected static final int SPACE_ASCII = 32;

	private MetaFile metaData;

	protected TextMeshCreator(File metaFile) {
		metaData = new MetaFile(metaFile);
	}

	protected TextMeshData createTextMesh(GUIText text) {
		List<Line> lines = createStructure(text);
		TextMeshData data = createQuadVertices(text, lines);
		return data;
	}

	private List<Line> createStructure(GUIText text) {
		char[] chars = text.getTextString().toCharArray();
		List<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
		Word currentWord = new Word(text.getFontSize());
		for (char c : chars) {
			int ascii = (int) c;
			if (ascii == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if (!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
					currentLine.attemptToAddWord(currentWord);
				}
				currentWord = new Word(text.getFontSize());
				continue;
			}
			Character character = metaData.getCharacter(ascii);
			currentWord.addCharacter(character);
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}

	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GUIText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if (!added) {
			lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
			currentLine.attemptToAddWord(currentWord);
		}
		lines.add(currentLine);
	}

	private TextMeshData createQuadVertices(GUIText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double curserX = 0f;
		double curserY = 0f;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoords = new ArrayList<Float>();
		for (Line line : lines) {
			if (text.isCentered()) {
				curserX = (line.getMaxLength() - line.getLineLength()) / 2;
			}
			for (Word word : line.getWords()) {
				for (Character letter : word.getCharacters()) {
					addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
					addTexCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(),
							letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
					curserX += letter.getxAdvance() * text.getFontSize();
				}
				curserX += metaData.getSpaceWidth() * text.getFontSize();
			}
			curserX = 0;
			curserY += LINE_HEIGHT * text.getFontSize();
		}
		return new TextMeshData(listToArray(vertices), listToArray(textureCoords));
	}

	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize,
			List<Float> vertices) {
		double x = curserX + (character.getxOffset() * fontSize);
		double y = curserY + (character.getyOffset() * fontSize);
		double maxX = x + (character.getSizeX() * fontSize);
		double maxY = y + (character.getSizeY() * fontSize);
		double properX = (2 * x) - 1;
		double properY = (-2 * y) + 1;
		double properMaxX = (2 * maxX) - 1;
		double properMaxY = (-2 * maxY) + 1;
		addVertices(vertices, properX, properY, properMaxX, properMaxY);
	}

	private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
		vertices.add((float) x);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) maxY);
		vertices.add((float) maxX);
		vertices.add((float) y);
		vertices.add((float) x);
		vertices.add((float) y);
	}

	private static void addTexCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
		texCoords.add((float) x);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) maxY);
		texCoords.add((float) maxX);
		texCoords.add((float) y);
		texCoords.add((float) x);
		texCoords.add((float) y);
	}

	private static float[] listToArray(List<Float> listOfFloats) {
		float[] array = new float[listOfFloats.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = listOfFloats.get(i);
		}
		return array;
	}

}
