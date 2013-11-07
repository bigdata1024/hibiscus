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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.aicer.hibiscus.exception.HibiscusException;


/**
 * Hash Generator
 *
 * Used to generate MD5 and SHA1 hash from strings
 *
 * Uses UTF-8 Internally to convert strings to bytes before generating the hash values for those strings
 *
 */
public abstract class HashGenerator {

    private static final int MESSAGE_DIGEST_UPDATE_OFFSET = 0;

    private static final int BYTE_LENGTH_SHA1 = 40;

    private static final int BYTE_LENGTH_MD5 = 32;

    private static final String ENCODING_CHARSET_NAME = "utf-8";

    private static final String MESSAGE_DIGEST_ALGORITHM_SHA1 = "SHA-1";

    private static final String MESSAGE_DIGEST_ALGORITHM_MD5 = "MD5";

    /**
     * Converts an array of bytes to its hexadecimal equivalent
     *
     * @param data
     * @return String Hexadecimal equivalent of the byte array
     */
    private static String convertToHex(byte[] data) {

        final StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < data.length; i++) {

            int halfByte = (data[i] >>> 4) & 0x0F;
            int twoHalves = 0;

            do {

                if ((0 <= halfByte) && (halfByte <= 9)) {
                    buffer.append((char) ('0' + halfByte));
                } else {
                    buffer.append((char) ('a' + (halfByte - 10)));
                }

                halfByte = data[i] & 0x0F;

            } while(twoHalves++ < 1);
        }

        return buffer.toString();
    }

    /**
     * Returns the SHA1 hash of the input string
     *
     * @param input Input string
     * @return String The sha1 hash of the input string
     * @throws HibiscusException
     */
    public static String getSHA1Hash(final String input) throws HibiscusException  {

        String hashValue = null;

        try {

            final MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM_SHA1);
            byte[] sha1Hash = new byte[BYTE_LENGTH_SHA1];

            messageDigest.update(input.getBytes(ENCODING_CHARSET_NAME), MESSAGE_DIGEST_UPDATE_OFFSET, input.length());
            sha1Hash = messageDigest.digest();

            hashValue = convertToHex(sha1Hash);

        } catch (NoSuchAlgorithmException e) {
            throw new HibiscusException("Unsupported Message Digest Algorithm "  + MESSAGE_DIGEST_ALGORITHM_SHA1, e);
        } catch (UnsupportedEncodingException e) {
            throw new HibiscusException("Unsupported Encoding " + ENCODING_CHARSET_NAME , e);
        }

        return hashValue;
    }

    /**
     * Returns the MD5 hash of the input string
     *
     * @param input Input string
     * @return String The md5 hash of the input string
     * @throws HibiscusException
     */
    public static String getMD5Hash(final String input) throws HibiscusException  {

        String hashValue = null;

        try {

            final MessageDigest messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM_MD5);
            byte[] md5Hash = new byte[BYTE_LENGTH_MD5];

            messageDigest.update(input.getBytes(ENCODING_CHARSET_NAME), MESSAGE_DIGEST_UPDATE_OFFSET, input.length());
            md5Hash = messageDigest.digest();

            hashValue = convertToHex(md5Hash);

        } catch (NoSuchAlgorithmException e) {
            throw new HibiscusException("Unsupported Message Digest Algorithm "  + MESSAGE_DIGEST_ALGORITHM_MD5, e);
        } catch (UnsupportedEncodingException e) {
            throw new HibiscusException("Unsupported Encoding " + ENCODING_CHARSET_NAME , e);
        }

        return hashValue;
    }
}