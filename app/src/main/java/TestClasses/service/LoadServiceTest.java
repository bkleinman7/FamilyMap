package TestClasses.service;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kbrent.FMSDesign.DataAccess.DataAccessException;
import kbrent.FMSDesign.Service.LoadService;

import static org.junit.Assert.*;

public class LoadServiceTest {

    String json = "";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        json = "{\n" +
                "  \"users\": [\n" +
                "    {\n" +
                "      \"userName\": \"sheila\",\n" +
                "      \"password\": \"parker\",\n" +
                "      \"email\": \"sheila@parker.com\",\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"persons\": [\n" +
                "    {\n" +
                "      \"firstName\": \"Sheila\",\n" +
                "      \"lastName\": \"Parker\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"father\": \"Patrick_Spencer\",\n" +
                "      \"mother\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"Patrick\",\n" +
                "      \"lastName\": \"Spencer\",\n" +
                "      \"gender\": \"m\",\n" +
                "      \"personID\":\"Patrick_Spencer\",\n" +
                "      \"spouse\": \"Im_really_good_at_names\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"firstName\": \"CS240\",\n" +
                "      \"lastName\": \"JavaRocks\",\n" +
                "      \"gender\": \"f\",\n" +
                "      \"personID\": \"Im_really_good_at_names\",\n" +
                "      \"spouse\": \"Patrick_Spencer\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"events\": [\n" +
                "    {\n" +
                "      \"eventType\": \"started family map\",\n" +
                "      \"personID\": \"Sheila_Parker\",\n" +
                "      \"city\": \"Salt Lake City\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.7500,\n" +
                "      \"longitude\": -110.1167,\n" +
                "      \"year\": 2016,\n" +
                "      \"eventID\": \"Sheila_Family_Map\",\n" +
                "      \"descendant\":\"sheila\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"eventType\": \"fixed this thing\",\n" +
                "      \"personID\": \"Patrick_Spencer\",\n" +
                "      \"city\": \"Provo\",\n" +
                "      \"country\": \"United States\",\n" +
                "      \"latitude\": 40.2338,\n" +
                "      \"longitude\": -111.6585,\n" +
                "      \"year\": 2017,\n" +
                "      \"eventID\": \"I_hate_formatting\",\n" +
                "      \"descendant\": \"sheila\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @After
    public void tearDown() throws Exception {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void loadJsonSuccess() {
        LoadService loadService = new LoadService();
        try {
            Integer[] intArr = loadService.loadJson(json);
            assertNotNull(intArr);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Test
    public void loadJsonFail() {
        LoadService loadService = new LoadService();
        try {
            boolean numIsZero = true;
            Integer[] intArr = loadService.loadJson("{\n" +
                    "  \"users\": [],\n" +
                    "  \"persons\": [],\n" +
                    "  \"events\": []\n" +
                    "}");
            if(intArr[0] != 0 || intArr[1] != 0 || intArr[2] != 0 ) {
                numIsZero = false;
            }
            assertTrue(numIsZero);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}