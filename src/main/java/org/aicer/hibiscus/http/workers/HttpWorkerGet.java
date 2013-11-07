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

import org.aicer.hibiscus.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;


/**
 * Http Worker for GET Requests
 *
 * This class is only intended to be visible within this package only.
 * The impelementation details are kept away from the users.
 *
 */
final class HttpWorkerGet extends HttpNonEntityEnclosingRequestWorker {

    public HttpWorkerGet(HttpClient client) {
        super(client, new HttpGet());
    }
}
