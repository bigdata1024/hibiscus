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

import java.net.URISyntaxException;

import org.aicer.hibiscus.exception.HibiscusException;
import org.aicer.hibiscus.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;


/**
 * Worker used for Non-Entity Enclosing Http Requests such as HEAD, GET and DELETE
 *
 */
abstract class HttpNonEntityEnclosingRequestWorker extends HttpWorkerAbstract {

    public HttpNonEntityEnclosingRequestWorker(HttpClient client, HttpRequestBase httpRequest) {
        super(client, httpRequest);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() throws HibiscusException {

        try {

            httpRequest.setURI(getURI());

            for (BasicNameValuePair header : httpClient.getRequestHeaders()) {
                httpRequest.addHeader(header.getName(), header.getValue());
            }

        } catch (URISyntaxException e1) {
            throw new HibiscusException(e1);
        }
    }

}
