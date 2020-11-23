package com.demo.project.api.helper;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestAPIMethods {
    private static final Logger LOGGER = LogManager.getLogger(RestAPIMethods.class);
    private Response response;
    private RequestSpecification requestSpec;
    private JsonReader jsonReader = new JsonReader();
    private Configvariable var = new Configvariable();

    public synchronized Response sendRequest(String requestType, String baseURL, String endPoint, String jsonString) {
        String url = null;
        try {
            url = baseURL + endPoint;
            LOGGER.info("Sending " + requestType + " request to " + url);
            switch (requestType) {
                case "GET":
//                    response = this.sendGetRequest(url, jsonString);
                    break;
                case "POST":
                    response = this.sendPostRequest(url, jsonString);
                    break;
                case "DELETE":
                    response = this.sendDeleteRequest(url);
                    break;
                case "PUT":
                    response = this.sendPutRequest(url, jsonString);
                    break;
                case "PATCH":
//                    response = this.sendPatchRequest(url, jsonString);
                    break;
                default:
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            this.clearRequestSpecObject();

        }

        return response;

    }

    public void clearRequestSpecObject() {
        requestSpec = null;
    }


    public RequestSpecification getRequestSpec() {
        return requestSpec;
    }


    public Response getResponse() {
        return response;
    }


    //Setting Basic Authentication
    public RequestSpecification setBasicAuthentication(final String username, final String password) {
        LOGGER.warn("Basic authentication using " + username + " and " + password);
        if (getRequestSpec() == null) {
            requestSpec = given().auth().basic(username, password);
        } else {
            requestSpec = getRequestSpec().given().auth().basic(username, password);
        }
        return requestSpec;
    }

    public RequestSpecification setBasicAuthentication(final String token) {
        LOGGER.warn("Basic authentication using token " + token);
        if (getRequestSpec() == null) {
            requestSpec = given().auth().oauth2(token);
        } else {
            requestSpec = getRequestSpec().given().auth().oauth2(token);
        }
        return requestSpec;
    }


    //Setting Header params
    public RequestSpecification setHeaderParams(final Map<String, String> headerParams) {
        LOGGER.warn("Setting Api header params " + headerParams.toString());
        if (getRequestSpec() == null) {
            requestSpec = given().headers(headerParams);
        } else {
            requestSpec = getRequestSpec().given().headers(headerParams);
        }
        return requestSpec;
    }


    //Setting Body Params
    public RequestSpecification setBodyParam(final String bodyContent) {
        LOGGER.warn("Setting Api body param " + bodyContent);
        if (getRequestSpec() == null) {
            requestSpec = given().body(bodyContent);
        } else {
            requestSpec = getRequestSpec().given().body(bodyContent);
        }
        return requestSpec;
    }

    public RequestSpecification setBodyParam(final Map<String, String> bodyContent) {
        LOGGER.warn("Setting Api body param " + bodyContent);
        if (getRequestSpec() == null) {
            requestSpec = given().formParams(bodyContent);
        } else {
            requestSpec = getRequestSpec().given().formParams(bodyContent);
        }
        return requestSpec;
    }


    public RequestSpecification setBodyParam(String key, String value) {
        LOGGER.warn("Setting Api body param " + value);
        if (getRequestSpec() == null) {
            requestSpec = given().formParams(key, value);
        } else {
            requestSpec = getRequestSpec().given().formParams(key, value);
        }
        return requestSpec;

    }

    //Validation methods
    public Integer getStatusCode() {
        return getResponse().getStatusCode();
    }

    public String getResponseAsString() {
        return getResponse().asString();
    }

    public String getValueFromResponse(final String jsonKey) {
        return ((ArrayList) getObjectFromResponse(jsonKey)).get(0).toString();
    }

    public Object getObjectFromResponse(final String jsonKey) {
        return this.getObjectFromResponse(getResponse(), jsonKey);
    }

    public Object getObjectFromResponse(final Response response, final String jsonKey) {
        if (response == null) {
            return null;
        }
        Object value = response.jsonPath().get(jsonKey);
        LOGGER.warn("Response value for jsonKey " + jsonKey + " is " + value);
        return value;
    }

    public <T> T getResponseIntoObject(final Response response, final String jsonKey, final Class<T> clazz) {
        if (response == null) {
            return null;
        }
        return response.jsonPath().getObject(jsonKey, clazz);
    }

    //API interaction methods
    private Response sendPostRequest(String url, String jsonString) {
        if(jsonString == null)
            jsonString = "";
        String requestBody = jsonReader.getJsonString(jsonString);
        requestBody = var.expandValue(requestBody);

        response = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(url);

        return response;
    }

    private Response sendPutRequest(String url, String jsonString) {
        if(jsonString == null)
            jsonString = "";
        String requestBody = jsonReader.getJsonString(jsonString);
        requestBody = var.expandValue(requestBody);

        response = given().
                contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(url);

        return response;
    }

    private Response sendDeleteRequest(String url) {
        url = url + "/" + var.getStringVar("pet_id");
        return given().delete(url);

    }

}
