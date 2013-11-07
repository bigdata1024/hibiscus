/**
 * Copyright 2012-2013 American Institute for Computing Education and Research Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aicer.hibiscus.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.aicer.hibiscus.exception.HibiscusException;

public abstract class StreamUtil {

    public static final String NEWLINE = "\n";

    public static final String inputStreamToString(final InputStream stream) throws HibiscusException {

        final DataInputStream in = new DataInputStream(stream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        final StringBuilder builder = new StringBuilder();

        String strLine = null;

        if (null == stream) {
            return null;
        }

        try {
            while ((strLine = br.readLine()) != null)   {
                builder.append(strLine);
            }
        } catch (IOException e) {
            throw new HibiscusException(e);
        }

        return builder.toString();
    }

    /**
     * Converst the input stream to a String object
     *
     * @param stream
     * @param keepNewlines Whether to keep new lines
     * @return
     * @throws HibiscusException
     */
    public static final String inputStreamToString(final InputStream stream, boolean keepNewlines) throws HibiscusException {

        if (!keepNewlines) {
            return inputStreamToString(stream);
        }

        final DataInputStream in = new DataInputStream(stream);
        final BufferedReader br = new BufferedReader(new InputStreamReader(in));
        final StringBuilder builder = new StringBuilder();

        String strLine = null;

        if (null == stream) {
            return null;
        }

        try {
            while ((strLine = br.readLine()) != null)   {
                builder.append(strLine + NEWLINE);
            }
        } catch (IOException e) {
            throw new HibiscusException(e);
        }

        return builder.toString();
    }
}
