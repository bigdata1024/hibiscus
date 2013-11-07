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
package org.aicer.hibiscus.exception;

/**
 * Thrown if there is a problem
 */
public class HibiscusException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public HibiscusException() {
        super();
    }

    public HibiscusException(String message) {
        super(message);
    }

    public HibiscusException(Throwable cause) {
        super(cause);
    }

    public HibiscusException(String message, Throwable cause) {
        super(message, cause);
    }
}
