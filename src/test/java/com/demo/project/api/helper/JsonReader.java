package com.demo.project.api.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class JsonReader {
    private static final Logger logger = LogManager.getLogger(JsonReader.class);
    private JSONObject jsonObject;

    public String getJsonString(String jsonFileName) {
        String jsonString = "";
        try {
            InputStream initialStream = getClass().getResourceAsStream(jsonFileName);
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(initialStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            jsonObject = new JSONObject(responseStrBuilder.toString());
            jsonString = jsonObject.toString();

        } catch (Exception e) {
            logger.error("Not able to get Json string from json file [{}]" + jsonFileName);
        }
        return jsonString;
    }

    public Map<String, Object> convertJsonStringToMap(String jsonValue) {
        Map<String, Object> jsonMAP = null;
        try {
            jsonObject = new JSONObject(jsonValue);
            jsonMAP = jsonObject.toMap();
        } catch (Exception e) {
            logger.error("Not able to convert Json string to map [{}]" + jsonValue);
        }

        return jsonMAP;
    }

    public JSONObject convertJsonStringToJsonObject(String jsonValue) {
        try {
            jsonObject = new JSONObject(jsonValue);
        } catch (Exception e) {
            logger.error("Not able to convert Json string to jsonObject [{}]" + jsonValue);
        }

        return jsonObject;
    }

    public JSONObject convertJsonArrayToJsonObject(JSONArray jsonValue, int index) {
        JSONObject object = null;

        try {
            object = jsonValue.getJSONObject(index);
        } catch (Exception e) {
            logger.error("Not able to convert Json array to jsonObject [{}]" + jsonValue);
        }
        return object;
    }
}