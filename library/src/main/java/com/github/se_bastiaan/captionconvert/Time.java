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

public class Time {

    // in an integer we can store 24 days worth of milliseconds, no need for a long
    private int milliseconds;

    /**
     * Constructor to create a time object.
     *
     * @param format supported formats: "hh:mm:ss,ms", "h:mm:ss.cs" and "h:m:s:f/fps"
     * @param value  string in the correct format
     */
    public Time(String format, String value) {
        if (format.equalsIgnoreCase("hh:mm:ss,ms")) {
            // this type of format:  01:02:22,501 (used in .SRT)
            int h, m, s, ms;
            h = Integer.parseInt(value.substring(0, 2));
            m = Integer.parseInt(value.substring(3, 5));
            s = Integer.parseInt(value.substring(6, 8));
            ms = Integer.parseInt(value.substring(9, 12));

            milliseconds = ms + s * 1000 + m * 60000 + h * 3600000;

        } else if (format.equalsIgnoreCase("h:mm:ss.cs")) {
            // this type of format:  1:02:22.51 (used in .ASS/.SSA)
            int h, m, s, cs;
            h = Integer.parseInt(value.substring(0, 1));
            m = Integer.parseInt(value.substring(2, 4));
            s = Integer.parseInt(value.substring(5, 7));
            cs = Integer.parseInt(value.substring(8, 10));

            milliseconds = cs * 10 + s * 1000 + m * 60000 + h * 3600000;
        } else if (format.equalsIgnoreCase("hh:mm:ss.ms")) {
            // this type of format:  01:02:22.501 (used in .VTT)
            int h, m, s, cs;
            h = Integer.parseInt(value.substring(0, 1));
            m = Integer.parseInt(value.substring(2, 4));
            s = Integer.parseInt(value.substring(5, 7));
            cs = Integer.parseInt(value.substring(8, 10));

            milliseconds = cs * 10 + s * 1000 + m * 60000 + h * 3600000;
        } else if (format.equalsIgnoreCase("h:m:s:f/fps")) {
            int h, m, s, f;
            float fps;
            String[] args = value.split("/");
            fps = Float.parseFloat(args[1]);
            args = args[0].split(":");
            h = Integer.parseInt(args[0]);
            m = Integer.parseInt(args[1]);
            s = Integer.parseInt(args[2]);
            f = Integer.parseInt(args[3]);

            milliseconds = (int) (f * 1000 / fps) + s * 1000 + m * 60000 + h * 3600000;
        }
    }

    /**
     * Method to return a formatted value of the time stored
     *
     * @param format supported formats: "hh:mm:ss,ms", "h:mm:ss.cs" and "hhmmssff/fps"
     * @return formatted time in a string
     */
    protected String getTime(String format) {
        //we use string builder for efficiency
        StringBuilder time = new StringBuilder();
        String aux;
        if (format.equalsIgnoreCase("hh:mm:ss,ms")) {
            // this type of format:  01:02:22,501 (used in .SRT)
            int h, m, s, ms;
            h = milliseconds / 3600000;
            aux = String.valueOf(h);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            m = (milliseconds / 60000) % 60;
            aux = String.valueOf(m);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            s = (milliseconds / 1000) % 60;
            aux = String.valueOf(s);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(',');
            ms = milliseconds % 1000;
            aux = String.valueOf(ms);
            if (aux.length() == 1) time.append("00");
            else if (aux.length() == 2) time.append('0');
            time.append(aux);

        } else if (format.equalsIgnoreCase("hh:mm:ss.ms")) {
            // this type of format:  01:02:22.501 (used in .SRT)
            int h, m, s, ms;
            h = milliseconds / 3600000;
            aux = String.valueOf(h);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            m = (milliseconds / 60000) % 60;
            aux = String.valueOf(m);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            s = (milliseconds / 1000) % 60;
            aux = String.valueOf(s);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append('.');
            ms = milliseconds % 1000;
            aux = String.valueOf(ms);
            if (aux.length() == 1) time.append("00");
            else if (aux.length() == 2) time.append('0');
            time.append(aux);

        } else if (format.equalsIgnoreCase("h:mm:ss.cs")) {
            // this type of format:  1:02:22.51 (used in .ASS/.SSA)
            int h, m, s, cs;
            h = milliseconds / 3600000;
            aux = String.valueOf(h);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            m = (milliseconds / 60000) % 60;
            aux = String.valueOf(m);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            s = (milliseconds / 1000) % 60;
            aux = String.valueOf(s);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append('.');
            cs = (milliseconds / 10) % 100;
            aux = String.valueOf(cs);
            if (aux.length() == 1) time.append('0');
            time.append(aux);

        } else if (format.startsWith("hhmmssff/")) {
            //this format is used in EBU's STL
            int h, m, s, f;
            float fps;
            String[] args = format.split("/");
            fps = Float.parseFloat(args[1]);
            //now we concatenate time
            h = milliseconds / 3600000;
            aux = String.valueOf(h);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            m = (milliseconds / 60000) % 60;
            aux = String.valueOf(m);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            s = (milliseconds / 1000) % 60;
            aux = String.valueOf(s);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            f = (milliseconds % 1000) * (int) fps / 1000;
            aux = String.valueOf(f);
            if (aux.length() == 1) time.append('0');
            time.append(aux);

        } else if (format.startsWith("h:m:s:f/")) {
            //this format is used in EBU's STL
            int h, m, s, f;
            float fps;
            String[] args = format.split("/");
            fps = Float.parseFloat(args[1]);
            //now we concatenate time
            h = milliseconds / 3600000;
            aux = String.valueOf(h);
            //if (aux.length()==1) time.append('0');
            time.append(aux);
            time.append(':');
            m = (milliseconds / 60000) % 60;
            aux = String.valueOf(m);
            //if (aux.length()==1) time.append('0');
            time.append(aux);
            time.append(':');
            s = (milliseconds / 1000) % 60;
            aux = String.valueOf(s);
            //if (aux.length()==1) time.append('0');
            time.append(aux);
            time.append(':');
            f = (milliseconds % 1000) * (int) fps / 1000;
            aux = String.valueOf(f);
            //if (aux.length()==1) time.append('0');
            time.append(aux);
        } else if (format.startsWith("hh:mm:ss:ff/")) {
            //this format is used in SCC
            int h, m, s, f;
            float fps;
            String[] args = format.split("/");
            fps = Float.parseFloat(args[1]);
            //now we concatenate time
            h = milliseconds / 3600000;
            aux = String.valueOf(h);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            m = (milliseconds / 60000) % 60;
            aux = String.valueOf(m);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            s = (milliseconds / 1000) % 60;
            aux = String.valueOf(s);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
            time.append(':');
            f = (milliseconds % 1000) * (int) fps / 1000;
            aux = String.valueOf(f);
            if (aux.length() == 1) time.append('0');
            time.append(aux);
        }

        return time.toString();
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

}
