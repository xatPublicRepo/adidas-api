package com.demo.project.api.runner;

import com.demo.project.api.helper.Configvariable;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.CucumberFeatureWrapper;
import cucumber.api.testng.TestNGCucumberRunner;
import org.testng.annotations.*;

@CucumberOptions(
        features = "classpath:features",
        glue = {"com/demo/project/api/stepdef"},
        tags = {"@test"},
        plugin = {
                "pretty",
                "html:target/site/cucumber-pretty",
                "json:reports/cucumber/cucumber.json",
                "rerun:target/cucumber-reports/rerun.txt"
        }
)

public class CucumberRunner {

    private TestNGCucumberRunner testNGCucumberRunner;

    @BeforeSuite(alwaysRun = true)
    public void setUpEnvironmentToTest() {
        Configvariable conf = new Configvariable();
        conf.setupEnvironmentProperties("qa"); //pass through env-variables
    }

    @AfterSuite(alwaysRun = true)
    public void cleanUp() {

    }

    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
    public void feature(CucumberFeatureWrapper cucumberFeature) {
        testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
    }

    @DataProvider
    public Object[][] features() {
        return testNGCucumberRunner.provideFeatures();
    }

    @BeforeClass(alwaysRun = true)
    public void setUpCucumber() {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        testNGCucumberRunner.finish();
    }

}