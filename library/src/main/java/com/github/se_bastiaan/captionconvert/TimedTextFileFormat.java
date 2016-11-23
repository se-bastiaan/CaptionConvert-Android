/*
 * Copyright (C) 2015-2016 Sébastiaan (github.com/se-bastiaan)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.se_bastiaan.captionconvert;

import java.io.IOException;

/**
 * This class specifies the interface for any format supported by the converter, these formats must
 * create a {@link com.github.se_bastiaan.captionconvert.TimedTextObject} from an {@link java.io.InputStream} (so it can process files form standard In or uploads)
 * and return a String array for text formats, or byte array for binary formats.
 * <br><br>
 * Copyright (c) 2012 J. David Requejo <br>
 * j[dot]david[dot]requejo[at] Gmail
 * <br><br>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * <br><br>
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * <br><br>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * @author J. David Requejo
 */
public abstract class TimedTextFileFormat {

    /**
     * This methods receives the path to a file, parses it, and returns a TimedTextObject
     *
     * @param fileName String that contains the path to the file
     * @param inputString Input string containing subtitle text
     * @return TimedTextObject representing the parsed file
     * @throws java.io.IOException when having trouble reading the file from the given path
     * @throws ParsingException when parsing failed
     */
    public abstract TimedTextObject parseFile(String fileName, String[] inputString) throws IOException, ParsingException;

    public TimedTextObject parseFile(String fileName, String inputString) throws IOException, ParsingException {
        return parseFile(fileName, inputString.split("\n|\r\n"));
    }

    /**
     * This method transforms a given TimedTextObject into a formated subtitle file
     *
     * @param tto the object to transform into a file
     * @return NULL if the given TimedTextObject has not been built first,
     * or String[] where each String is at least a line, if size is 2, then the file has at least two lines.
     * or byte[] in case the file is a binary (as is the case of STL format)
     */
    public abstract Object toFile(TimedTextObject tto);

    protected String getLine(String[] strArray, int index) {
        if (index < strArray.length) {
            return strArray[index];
        }
        return null;
    }

    protected String join(String[] s, String glue) {
        if (s.length == 0)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append(s[0]);
        for (int x = 1; x < s.length; ++x) {
            sb.append(glue).append(s[x]);
        }
        return sb.toString();
    }

}
