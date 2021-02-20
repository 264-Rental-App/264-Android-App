package edu.rentals.frontend;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    private ShoppingApiService shoppingApiService;
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setup() {
        //  Mocks are being created.
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getStoreInfo_isCorrect() {
        String string = "{ \"store\": {\"name\": \"Big Store\",\"lat\": 130,\"long\": 122,\"id\": 1,\"commonAddress\": \"CA 91711\",\"phoneNumber\": 9999999999 }";
        JSONObject storeObject = null;
        try {
            storeObject = new JSONObject(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}