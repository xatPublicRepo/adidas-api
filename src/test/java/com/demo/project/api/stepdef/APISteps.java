package com.demo.project.api.stepdef;

import com.demo.project.api.helper.Configvariable;
import com.demo.project.api.model.ApiFunctions;
import com.demo.project.api.runner.CucumberRunner;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;

import java.util.Map;
import java.util.Set;

public class APISteps extends CucumberRunner {

    private ApiFunctions api = new ApiFunctions();
    private Configvariable var = new Configvariable();

    @And("^I add new pet via API$")
    public void iAddNewPetViaAPI(DataTable table) {
        Map<String, String> hm = table.asMap(String.class, String.class);
        Set<String> keys = hm.keySet();
        for(String key : keys){
            var.setStringVariable(hm.get(key), key);
        }

        api.createNewPet();
    }

    @And("^I verify the new pet added$")
    public void iVerifyTheNewPetAdded() {
        api.verifyNewPet();
    }

    @And("^I update the pet status to sold$")
    public void iUpdateThePetStatusToSold(DataTable table) {
        Map<String, String> hm = table.asMap(String.class, String.class);
        Set<String> keys = hm.keySet();
        for(String key : keys){
            var.setStringVariable(hm.get(key), key);
        }

        api.updatePetStatus();
    }

    @And("^I verify the new pet status updated$")
    public void iVerifyTheNewPetUpdated() {
        api.verifyUpdatedPet();
    }

    @And("^I delete the pet and verify$")
    public void iDeleteThePetAndVerify() {
        api.deletePetandVerify();
    }
}


