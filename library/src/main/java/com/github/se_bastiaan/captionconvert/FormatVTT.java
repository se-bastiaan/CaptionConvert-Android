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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class represents the .VTT subtitle format
 */
public class FormatVTT extends TimedTextFileFormat {

    @Override
    public TimedTextObject parseFile(String fileName, String[] inputString) throws IOException, ParsingException {
        TimedTextObject tto = new TimedTextObject();
        Caption caption = new Caption();
        int captionNumber = 1;
        boolean allGood;

        //the file name is saved
        tto.fileName = fileName;

        int lineCounter = 0;
        int stringIndex = 0;
        String line;
        try {
            line = getLine(inputString, stringIndex++);
            while (line != null && stringIndex < inputString.length) {
                line = line.trim();
                lineCounter++;
                //if its a blank line, ignore it, otherwise...
                if (!line.isEmpty()) {
                    allGood = false;
                    //the first thing should be an increasing number
                    try {
                        int num = Integer.parseInt(line);
                        if (num != captionNumber)
                            throw new Exception();
                        else {
                            captionNumber++;
                            allGood = true;
                        }
                    } catch (Exception e) {
                        tto.warnings += captionNumber + " expected at line " + lineCounter;
                        tto.warnings += "\n skipping to next line\n\n";
                    }
                    if (allGood) {
                        //we go to next line, here the begin and end time should be found
                        try {
                            lineCounter++;
                            line = getLine(inputString, stringIndex++).trim();
                            String start = line.substring(0, 12);
                            String end = line.substring(line.length() - 12, line.length());
                            Time time = new Time("hh:mm:ss.ms", start);
                            caption.start = time;
                            time = new Time("hh:mm:ss.ms", end);
                            caption.end = time;
                        } catch (Exception e) {
                            tto.warnings += "incorrect time format at line " + lineCounter;
                            allGood = false;
                        }
                    }
                    if (allGood) {
                        //we go to next line where the caption text starts
                        lineCounter++;
                        line = getLine(inputString, stringIndex++).trim();
                        String text = "";
                        while (!line.isEmpty() && stringIndex < inputString.length) {
                            text += line + "<br />";
                            line = getLine(inputString, stringIndex++).trim();
                            lineCounter++;
                        }
                        caption.content = text;
                        int key = caption.start.getMilliseconds();
                        //in case the key is already there, we increase it by a millisecond, since no duplicates are allowed
                        while (tto.captions.containsKey(key)) key++;
                        if (key != caption.start.getMilliseconds())
                            tto.warnings += "caption with same start time found...\n\n";
                        //we add the caption.
                        tto.captions.put(key, caption);
                    }
                    //we go to next blank
                    while (!line.isEmpty() && stringIndex < inputString.length) {
                        line = getLine(inputString, stringIndex++).trim();
                        lineCounter++;
                    }
                    caption = new Caption();
                }
                if (stringIndex < inputString.length) {
                    line = getLine(inputString, stringIndex++);
                }
            }

        } catch (NullPointerException e) {
            tto.warnings += "unexpected end of file, maybe last caption is not complete.\n\n";
        }

        tto.built = true;
        return tto;
    }

    public String[] toFile(TimedTextObject tto) {

        //first we check if the TimedTextObject had been built, otherwise...
        if (!tto.built)
            return null;

        //we will write the lines in an ArrayList,
        int index = 0;
        //the minimum size of the file is 4*number of captions, so we'll take some extra space.
        ArrayList<String> file = new ArrayList<>(5 * tto.captions.size());
        //we iterate over our captions collection, they are ordered since they come from a TreeMap
        Collection<Caption> c = tto.captions.values();
        Iterator<Caption> itr = c.iterator();
        int captionNumber = 1;

        file.add("WEBVTT");
        file.add("");
        index += 2;

        while (itr.hasNext()) {
            //new caption
            Caption current = itr.next();
            //number is written
            file.add(index++, "" + captionNumber++);
            //we check for offset value:
            if (tto.offset != 0) {
                current.start.setMilliseconds(current.start.getMilliseconds() + tto.offset);
                current.end.setMilliseconds(current.end.getMilliseconds() + tto.offset);
            }
            //time is written
            file.add(index++, current.start.getTime("hh:mm:ss.ms") + " --> " + current.end.getTime("hh:mm:ss.ms"));
            //offset is undone
            if (tto.offset != 0) {
                current.start.setMilliseconds(current.start.getMilliseconds() - tto.offset);
                current.end.setMilliseconds(current.end.getMilliseconds() - tto.offset);
            }
            //text is added
            String[] lines = cleanTextForVTT(current);
            int i = 0;
            while (i < lines.length)
                file.add(index++, "" + lines[i++]);
            //we add the next blank line
            file.add(index++, "");
        }

        return file.toArray(new String[file.size()]);
    }

    /* PRIVATE METHODS */

    /**
     * This method cleans caption.content of XML and parses line breaks.
     */
    private String[] cleanTextForVTT(Caption current) {
        String[] lines;
        String text = current.content;
        //add line breaks
        lines = text.split("<br />");
        //clean XML
        for (int i = 0; i < lines.length; i++) {
            //this will destroy all remaining XML tags
            lines[i] = lines[i].replaceAll("<.*?>", "");
        }
        return lines;
    }

}