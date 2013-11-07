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
package org.aicer.hibiscus.http.workers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.aicer.hibiscus.exception.HibiscusException;
import org.aicer.hibiscus.http.client.HttpClient;
import org.aicer.hibiscus.http.client.Response;
import org.aicer.hibiscus.util.HashGenerator;
import org.aicer.hibiscus.util.StreamUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.log4j.Logger;


/**
 * Http Worker used to transmit HTTP Requests to the server
 *
 */
public abstract class HttpWorkerAbstract {

    public static final String HEAD = HttpHead.METHOD_NAME;
    public static final String GET = HttpGet.METHOD_NAME;
    public static final String POST = HttpPost.METHOD_NAME;
    public static final String PUT = HttpPut.METHOD_NAME;
    public static final String DELETE = HttpDelete.METHOD_NAME;

    private static Logger log = Logger.getLogger(HttpWorkerAbstract.class);

    protected final HttpRequestBase httpRequest;

    protected Response response;

    protected final HttpClient httpClient;

    private long responseTime = 0;

    protected HttpWorkerAbstract(HttpClient client, HttpRequestBase httpRequest) {
        this.httpClient = client;
        this.httpRequest = httpRequest;
    }

    /**
     * Returns the response from server
     *
     * @return
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Generates the URI Used for the Request
     *
     * Builds a URI String based on the following values
     *
     * - Scheme
     * - Hostname
     * - Port Number
     * - Path
     * - URI Fragment (if any)
     * - Query parameters (if any)
     *
     * @return
     * @throws URISyntaxException
     */
    protected URI getURI() throws URISyntaxException {
        return new URI(httpClient.getRawUrl());
    }

    /**
     * Prepares the worker for the HTTP request
     *
     * @throws HibiscusException
     */
    public abstract void prepare() throws HibiscusException;

    /**
     * Transmits the HTTP request from the client to the server
     *
     * @throws HibiscusException
     */
    public void execute() throws HibiscusException {

        prepare();

        httpRequest.addHeader("User-Agent", getClass().getName().toUpperCase());
        httpRequest.addHeader("X-Conversation-Id", HashGenerator.getMD5Hash(System.currentTimeMillis() + "." + new Thread().getId()));

        // Creates a registry for HTTP and HTTPS setting default ports to 80 and 443 respectively for these schemes
        // HTTP uses org.apache.http.conn.scheme.PlainSocketFactory
        // HTTPS uses org.apache.http.conn.ssl.SSLSocketFactory
        SchemeRegistry registry = SchemeRegistryFactory.createDefault();;

        PoolingClientConnectionManager connManager = new PoolingClientConnectionManager(registry);

        // This is necessary because of https://issues.apache.org/jira/browse/HTTPCLIENT-1193
        // We cannot use the Default constructor without overriding the connection manager from the default
        DefaultHttpClient requestClient = new DefaultHttpClient(connManager);

        try {

            final long startTime = System.currentTimeMillis();
            final HttpResponse response = requestClient.execute(httpRequest);
            final long elapsedTime = System.currentTimeMillis() - startTime;
            final HttpEntity responseEntity = response.getEntity();

            /* Capture the elapsed time for this request */
            this.responseTime = elapsedTime;

            /* This will reset the response object each time the request is made */
            this.response = new Response();

            /* Sets the status line and response headers */
            this.response.setStatusLine(response.getStatusLine().toString());
            this.response.setResponseHeaders(response.getAllHeaders());

            if (null != responseEntity) {
                this.response.setResponseBody(StreamUtil.inputStreamToString(responseEntity.getContent()));
            }

            if (log.isDebugEnabled()) {
                debugRequest(response);
            }

        } catch (IOException e) {
            throw new HibiscusException(e);
        }
    }

    private void debugRequest(final HttpResponse resp) {

        /* Logs the request line */
        log.debug(httpRequest.getRequestLine().toString());

        /* Logs all headers sent */
        for (Header requestHeader: httpRequest.getAllHeaders()) {
            log.debug(requestHeader.toString());
        }

        /* Logs the contents of any enclosing entity sent with the request */
        if (httpRequest instanceof HttpEntityEnclosingRequestBase && null != httpClient.getRequestBody()) {
            log.debug(httpClient.getRequestBody());
        }

        /* Logs the status line of the response */
        log.debug(resp.getStatusLine().toString());

        /* Logs all headers sent back in the response */
        for (Header responseHeader: resp.getAllHeaders()) {
            log.debug(responseHeader.toString());
        }

        /* Logs the response body if present */
        log.debug(this.response.getResponseBody());

        log.debug("////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////");
    }

    /**
     * Returns the HTTP method this worker uses, such as GET, PUT, POST, or other
     *
     * @return
     */
    public final String getRequestMethod() {
        return httpRequest.getMethod();
    }

    /**
     * Returns an instance of a HTTP Worker based on the request method
     *
     * @param requestMethod One of GET, PUT, POST, DELETE, HEAD
     * @param client
     * @return
     */
    public static HttpWorkerAbstract getWorkerStrategy(final String requestMethod, final HttpClient client) {

        if (requestMethod.equals(HEAD)) {
            return new HttpWorkerHead(client);
        } else if (requestMethod.equals(GET)) {
            return new HttpWorkerGet(client);
        } else if (requestMethod.equals(POST)) {
            return new HttpWorkerPost(client);
        } else if (requestMethod.equals(PUT)) {
            return new HttpWorkerPut(client);
        } else if (requestMethod.equals(DELETE)) {
            return new HttpWorkerDelete(client);
        }

        return new HttpWorkerGet(client);
    }

    /**
     * Returns how long the HTTP request took to complete
     *
     * @return
     */
    public long getResponseTime() {
        return responseTime;
    }
}
