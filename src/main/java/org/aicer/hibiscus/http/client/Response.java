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
package org.aicer.hibiscus.http.client;

import org.apache.http.Header;


public class Response {

    private String body = null;

    private String statusLine = null;

    private int responseCode = 0;

    private long elapsedTime = 0;

    private Header[] responseHeaders;

    public Response() {

    }

    public String getStatusLine() {
        return statusLine;
    }

    public final Response setStatusLine(final String statusLine) {

        this.statusLine = statusLine;

        final String[] statusPieces = statusLine.split(" ");

        if (statusPieces.length > 1) {
            responseCode = Integer.parseInt(statusPieces[1]);
        }

        return this;
    }

    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    public Response setResponseHeaders(final Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
        return this;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Response setResponseCode(final int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public String getResponseBody() {
        return body;
    }
    public Response setResponseBody(final String body) {
        this.body = body;
        return this;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public Response setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
        return this;
    }
}
