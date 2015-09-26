/*
 * This file is part of CaptionConvert-Android.
 *
 * CaptionConvert-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CaptionConvert-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CaptionConvert-Android. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.sv244.captionconvert;

/**
 * This class represents problems that may arise during the parsing of a subttile file.
 *
 * @author J. David
 */
public class FatalParsingException extends Exception {

    private static final long serialVersionUID = 6798827566637277804L;

    private String parsingErrror;

    public FatalParsingException(String parsingError) {
        super(parsingError);
        this.parsingErrror = parsingError;
    }

    @Override
    public String getLocalizedMessage() {
        return parsingErrror;
    }

}
