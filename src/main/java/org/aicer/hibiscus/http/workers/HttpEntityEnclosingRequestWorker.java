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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.aicer.hibiscus.exception.HibiscusException;
import org.aicer.hibiscus.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;


/**
 * Represents an Entity-Enclosing HTTP Request method such as POST and PUT
 *
 */
abstract class HttpEntityEnclosingRequestWorker extends HttpWorkerAbstract {

    public HttpEntityEnclosingRequestWorker(HttpClient client, HttpEntityEnclosingRequestBase httpRequest) {
        super(client, httpRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() throws HibiscusException {

        HttpEntityEnclosingRequestBase request = (HttpEntityEnclosingRequestBase) this.httpRequest;

        try {
            request.setURI(getURI());

            for (BasicNameValuePair header : httpClient.getRequestHeaders()) {
                request.addHeader(header.getName(), header.getValue());
            }

        } catch (URISyntaxException e1) {
            throw new HibiscusException(e1);
        }

        final String requestBody = httpClient.getRequestBody();
        final int contentLength  = (null != requestBody) ? requestBody.length() : 0;

         if (contentLength > 0) {
             try {
                request.setEntity(new StringEntity(requestBody, httpClient.getEncoding()));
            } catch (UnsupportedEncodingException e2) {
                throw new HibiscusException("The encoding " + httpClient.getEncoding() + " is not supported", e2);
            }
         }
    }
}
