
package com.mytestframework.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.ExtentTest;

import org.json.JSONObject;

public class APITests {
	
   //private ExtentReports extent;
  // private ExtentTest test;


    @BeforeMethod
    public void setUp() {
    	
    }

    
    @Test
    public void testAddNewDevice() {
        RestAssured.baseURI = "https://api.restful-api.dev/objects";

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "Apple MaxNew Pro 2TB");
        JSONObject dataParams = new JSONObject();
        dataParams.put("year", 2024);
        dataParams.put("price", 9000.99);
        dataParams.put("CPU model", "Apple ARM A8");
        dataParams.put("Hard disk size", "2 TB");
        requestParams.put("data", dataParams);

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestParams.toString())
                .post();

        Assert.assertEquals(response.getStatusCode(), 200);

        JSONObject responseBody = new JSONObject(response.getBody().asString());
        Assert.assertNotNull(responseBody.getString("id"));
        Assert.assertEquals(responseBody.getString("name"), "Apple MaxNew Pro 2TB");
        Assert.assertNotNull(responseBody.getString("createdAt"));

        JSONObject responseData = responseBody.getJSONObject("data");
        Assert.assertEquals(responseData.getInt("year"), 2024);
        Assert.assertEquals(responseData.getDouble("price"), 9000.99);
        Assert.assertEquals(responseData.getString("CPU model"), "Apple ARM A8");
        Assert.assertEquals(responseData.getString("Hard disk size"), "2 TB");
    

        
        
    }
    
    
}

