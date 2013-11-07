package org.aicer.hibiscus.examples;

import org.aicer.hibiscus.http.client.HttpClient;
import org.aicer.hibiscus.http.client.Response;
import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class ExampleGetRequest {

    public static void main(String[] args) {

        String elasticSearchStatusURL = "http://localhost:9200/";

        Gson gson = new GsonBuilder().create();

        HttpClient client = new HttpClient();

        client.setRawUrl(elasticSearchStatusURL);

        client.execute();

        Response response = client.getLastResponse();

        String jsonString = response.getResponseBody();

        JsonObject objectResponse = gson.fromJson(jsonString, JsonObject.class);

        System.out.println(objectResponse.get("ok"));

        for (Header responseHeader : response.getResponseHeaders()) {

            System.out.println(responseHeader.getName() + ": " + responseHeader.getValue());
        }

        System.out.println("ElasticSearch Raw Response: " + jsonString);
    }
}
