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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.aicer.hibiscus.exception.HibiscusException;
import org.aicer.hibiscus.http.workers.HttpWorkerAbstract;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;


public class HttpClient {

    private static final Logger log = Logger.getLogger(HttpClient.class);

    public static final String HEAD = HttpHead.METHOD_NAME;
    public static final String GET = HttpGet.METHOD_NAME;
    public static final String POST = HttpPost.METHOD_NAME;
    public static final String PUT = HttpPut.METHOD_NAME;
    public static final String DELETE = HttpDelete.METHOD_NAME;

    public static final String DEFAULT_HTTP_ENCODING = "UTF-8";

    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";

    public static final int DEFAULT_PORT_HTTP = 80;
    public static final int DEFAULT_PORT_HTTPS = 443;

    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_PATH = "/";

    private String encoding = DEFAULT_HTTP_ENCODING;

    private int port = DEFAULT_PORT_HTTP;

    private String scheme = SCHEME_HTTP;

    private String host = DEFAULT_HOST;

    private String path = DEFAULT_PATH;

    private String fragment = null;

    private String requestMethod = GET;

    private String requestBody = null;

    private String lastRequest = null;

    private Response lastResponse = null;

    private final List <String> validHttpRequestMethods = new ArrayList<String>();
    private final List <BasicNameValuePair> nameValuePairs = new ArrayList <BasicNameValuePair>();
    private final List <BasicNameValuePair> queryParameters = new ArrayList <BasicNameValuePair>();
    private final List <BasicNameValuePair> requestHeaders = new ArrayList <BasicNameValuePair>();

    private String rawUrl;

    public HttpClient() {

        validHttpRequestMethods.add(HEAD);
        validHttpRequestMethods.add(GET);
        validHttpRequestMethods.add(POST);
        validHttpRequestMethods.add(PUT);
        validHttpRequestMethods.add(DELETE);
    }

    public final HttpClient reset() {


        scheme = SCHEME_HTTP;
        host = DEFAULT_HOST;
        port = DEFAULT_PORT_HTTP;
        path = DEFAULT_PATH;
        fragment = null;

        requestMethod = GET;
        lastRequest = null;
        lastResponse = null;

        requestBody = null;

        encoding = DEFAULT_HTTP_ENCODING;

        resetNameValuePairs();
        resetQueryParameters();

        return this;
    }

    /**
     * Expects the Absolute URL for the Request
     *
     * @param uri
     * @return
     * @throws HibiscusException
     */
    public HttpClient setURI(final String uri) throws HibiscusException {

        try {
            setURI(new URI(uri));
        } catch (URISyntaxException e) {
            throw new HibiscusException(e);
        }

        return this;
    }

    /**
     * A URI representing the Absolute URL for the Request
     *
     * @param uri
     * @return
     */
    public HttpClient setURI(final URI uri) {

        final URIBuilder builder = new URIBuilder(uri);

        this.scheme = builder.getScheme();
        this.host   = builder.getHost();
        this.port   = builder.getPort();
        this.path   = builder.getPath();

        this.fragment = builder.getFragment();

        this.resetQueryParameters();

        for (NameValuePair nvp : builder.getQueryParams()) {
            this.queryParameters.add(new BasicNameValuePair(nvp.getName(), nvp.getValue()));
        }

        return this;
    }

    public final String getRequestBody() {

        if (nameValuePairs.size() > 0) {
            return this.getNameValuePairsAsString();
        }

        return requestBody;
    }

    public final HttpClient setRequestBody(final String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public final HttpClient setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public final int getPort() {
        return port;
    }

    public final HttpClient setPort(final int port) {
        this.port = port;
        return this;
    }

    public final String getScheme() {
        return scheme;
    }

    public final HttpClient setScheme(final String scheme) {
        this.scheme = scheme;
        return this;
    }

    public final String getHost() {
        return host;
    }

    public final HttpClient setHost(final String host) {
        this.host = host;
        return this;
    }

    public final String getPath() {
        return path;
    }

    public final HttpClient setPath(final String path) {
        this.path = path;
        return this;
    }

    public final String getFragment() {
        return fragment;
    }

    public final HttpClient setFragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public final HttpClient resetRequestBody() {
        this.resetNameValuePairs();
        this.requestBody = null;
        return this;
    }

    public final HttpClient resetHeaders() {
        requestHeaders.clear();
        return this;
    }

    public final HttpClient resetQueryParameters() {
        queryParameters.clear();
        return this;
    }

    public final HttpClient resetNameValuePairs() {
        nameValuePairs.clear();
        return this;
    }

    /**
     * Used by Entity-Enclosing HTTP Requests to send Name-Value pairs in the body of the request
     *
     * @param param Name of Parameter
     * @param value Value of Parameter
     * @return
     */
    public final HttpClient addNameValuePair(final String param, final String value) {
        nameValuePairs.add(new BasicNameValuePair(param, value));
        return this;
    }

    /**
     * Used by Entity-Enclosing HTTP Requests to send Name-Value pairs in the body of the request
     *
     * @param param Name of Parameter
     * @param value Value of Parameter
     * @return
     */
    public final HttpClient addNameValuePair(final String param, final Integer value) {
        return addNameValuePair(param, value.toString());
    }

    /**
     * Used by Entity-Enclosing HTTP Requests to send Name-Value pairs in the body of the request
     *
     * @param param Name of Parameter
     * @param value Value of Parameter
     * @return
     */
    public final HttpClient addNameValuePair(final String param, final Double value) {
        return addNameValuePair(param, value.toString());
    }

    public List <BasicNameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public HttpClient addQueryParameter(final String param, final String value) {
        queryParameters.add(new BasicNameValuePair(param, value));
        return this;
    }

    public List <BasicNameValuePair> getQueryParameters() {
        return queryParameters;
    }

    public HttpClient addHeader(final String param, final String value) {
        requestHeaders.add(new BasicNameValuePair(param, value));
        return this;
    }

    public List<BasicNameValuePair> getRequestHeaders() {
        return this.requestHeaders;
    }

    public HttpClient removeHeader(final String headerName) {

        final int numberOfHeaders = this.requestHeaders.size();

        for (int i=0; i < numberOfHeaders; i++) {

            BasicNameValuePair header = this.requestHeaders.get(i);

            if (header.getName().equals(headerName)) {
                this.requestHeaders.remove(i);
            }
        }

        return this;
    }

    private String getNameValuePairsAsString() {

        String query = "";

        if (nameValuePairs.size() == 0) {
            return query;
        }

        URIBuilder builder = new URIBuilder()
                .setScheme(SCHEME_HTTP)
                .setHost(DEFAULT_HOST)
                .setPath(DEFAULT_PATH);

        for (final BasicNameValuePair nvp : nameValuePairs) {
            builder.setParameter(nvp.getName(), nvp.getValue());
        }

        try {
            query = builder.build().getRawQuery();
        } catch (URISyntaxException e) {
            log.debug("Error while attempting to build NVP entity", e);
        }

        return query;
    }

    /**
     * Retrieves the HTTP worker and makes the request to the server
     *
     * Also populates the response object for this client
     *
     * @return
     * @throws HibiscusException
     */
    public HttpClient execute() throws HibiscusException {

        HttpWorkerAbstract httpWorker = HttpWorkerAbstract.getWorkerStrategy(requestMethod, this);

        httpWorker.execute();

        lastResponse = httpWorker.getResponse();

        lastResponse.setElapsedTime(httpWorker.getResponseTime());

        return this;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * Must be One of HEAD, GET, POST, PUT or DELETE
     *
     * @param rMethod
     * @throws HibiscusException If the request method is not allowed or valid
     */
    public HttpClient setRequestMethod(final String rMethod) throws HibiscusException {

        if (null == rMethod || false == validHttpRequestMethods.contains(rMethod)) {
            throw new HibiscusException(rMethod + " is not a valid HTTP Request Method");
        }

        requestMethod = rMethod;
        return this;
    }

    public String getLastRequest() {
        return lastRequest;
    }

    public Response getLastResponse() {
        return this.lastResponse;
    }

    public String getRawUrl() {
        return rawUrl;
    }

    public HttpClient setRawUrl(String rawUrl) {
        this.rawUrl = rawUrl;
        return this;
    }
}
