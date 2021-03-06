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

public abstract class StringUtils {

   /**
    * Converts and array of objects into a concatenated string
    *
    * Uses the "," character as the glue
    *
    * @param params
    * @return
    */
   public static final String implodeParams(Object[] params) {
       return implodeParams(params, ",");
   }

   /**
    * Converts and array of objects into a concatenated string
    *
    * @param params
    * @param glue
    * @return
    */
   public static final String implodeParams(final Object[] params, final String glue) {

       String returnValue = "";

       if (params.length == 1) {
           return params[0].toString();
       }

       returnValue += params[0];

       for (int i =1; i < params.length; i++) {
           returnValue += glue + params[i].toString();
       }

       return returnValue;
   }
}
