package com.demo.project.api.model;

import com.demo.project.api.helper.ApiEndPoints;
import com.demo.project.api.helper.ConfigDetails;
import com.demo.project.api.helper.Configvariable;
import com.demo.project.api.helper.RestAPIMethods;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.Assert;

public class ApiFunctions {

    private static final Logger logger = LogManager.getLogger(ApiFunctions.class);
    private ApiEndPoints ep = new ApiEndPoints();
    private RestAPIMethods rest = new RestAPIMethods();
    private Configvariable var = new Configvariable();

    public void createNewPet() {
        Response response = rest.sendRequest("POST", ConfigDetails.HOST, ep.ADD_PET, "/testdata/CreatePet.json");
        JsonPath jsonPathEvaluator = response.jsonPath();
        var.setStringVariable(jsonPathEvaluator.get("id").toString(), "pet_id");
    }

    public void verifyNewPet() {
        Response response = rest.getResponse();
        response.then().statusCode(200);
        response.then().body("status", Matchers.is("available"));
    }

    public void updatePetStatus() {
        rest.sendRequest("PUT", ConfigDetails.HOST, ep.ADD_PET, "/testdata/CreatePet.json");
    }

    public void verifyUpdatedPet() {
        Response response = rest.getResponse();
        response.then().statusCode(200);
        response.then().body("status", Matchers.is("sold"));
    }

    public void deletePetandVerify() {
        Response response = rest.sendRequest("DELETE", ConfigDetails.HOST, ep.ADD_PET, "");
        response.then().statusCode(200);
        JsonPath jsonPathEvaluator = response.jsonPath();
        Assert.assertEquals(jsonPathEvaluator.get("message").toString(), var.getStringVar("pet_id"));
    }
}
