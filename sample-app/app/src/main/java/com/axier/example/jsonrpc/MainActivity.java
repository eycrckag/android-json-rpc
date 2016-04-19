package com.axier.example.jsonrpc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.axier.jsonrpclibrary.JSONARRAYRPCClient;
import com.axier.jsonrpclibrary.JSONRPCClient;
import com.axier.jsonrpclibrary.JSONRPCException;
import com.axier.jsonrpclibrary.JSONRPCParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    //TODO add batch request




    public String URL = "";
    public String EXAMPLE_SUCCESS_CALL = "https://raw.githubusercontent.com/axierjhtjz/android-json-rpc/master/success.json";
    public String EXAMPLE_ERROR_CALL = "https://raw.githubusercontent.com/axierjhtjz/android-json-rpc/master/error.json";
    public String EXAMPLE_BATCH_SUCCESS_CALL = "https://raw.githubusercontent.com/eycrckag/android-json-rpc/master/batchsuccess.json";
    public String EXAMPLE_BATCH_ERROR_CALL = "https://raw.githubusercontent.com/eycrckag/android-json-rpc/master/batcherror.json";

    /**
     * Just a example of how to use it against a WS. In this case we are just fetching data from the
     * url above.
     */
    public String EXAMPLE_METHOD_NAME = "login";
    public String EXAMPLE_PARAM_1 = "user";
    public String EXAMPLE_PARAM_2 = "password";

    public TextView mResponseArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResponseArea = (TextView) findViewById(R.id.response_area);

        Button successBtn = (Button) findViewById(R.id.success_btn);
        Button errorBtn = (Button) findViewById(R.id.error_btn);
        Button BatchSuccessBtn = (Button) findViewById(R.id.batch_success_btn);
        Button BatchErrorBtn = (Button) findViewById(R.id.batch_error_btn);

        successBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL = EXAMPLE_SUCCESS_CALL;
                new MakeJSONRpcCallTask().execute();
            }
        });

        errorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL = EXAMPLE_ERROR_CALL;
                new MakeJSONRpcCallTask().execute();
            }
        });


        BatchSuccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL = EXAMPLE_BATCH_SUCCESS_CALL;
                new MakeJSONArrayRpcCallTask().execute();
            }
        });

        BatchErrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                URL = EXAMPLE_BATCH_ERROR_CALL;
                new MakeJSONArrayRpcCallTask().execute();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    public class MakeJSONRpcCallTask extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONRPCClient client = JSONRPCClient.create(URL, JSONRPCParams.Versions.VERSION_2);
            client.setConnectionTimeout(2000);
            client.setSoTimeout(2000);
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(EXAMPLE_PARAM_1, "myuser");
                jsonObj.put(EXAMPLE_PARAM_2, "mypassword");
                return client.callJSONObject(EXAMPLE_METHOD_NAME, jsonObj);
            } catch (JSONRPCException rpcEx) {
                rpcEx.printStackTrace();
            } catch (JSONException jsEx) {
                jsEx.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result != null && mResponseArea != null) {
                mResponseArea.setText(result.toString());
            }
        }
    }




    public class MakeJSONArrayRpcCallTask extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
            JSONARRAYRPCClient client = JSONARRAYRPCClient.create(URL, JSONRPCParams.Versions.VERSION_2);
            client.setConnectionTimeout(2000);
            client.setSoTimeout(2000);
            try {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put(EXAMPLE_PARAM_1, "myuser");
                jsonObj.put(EXAMPLE_PARAM_2, "mypassword");
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObj);
                System.out.println("jsonArray.toString ="+jsonArray.toString());

                return client.doJSONRequest(jsonArray);
            } catch (JSONRPCException rpcEx) {
                rpcEx.printStackTrace();
            } catch (Exception jsEx) {
                jsEx.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);
            if (result != null && mResponseArea != null) {
                mResponseArea.setText(result.toString());
            }
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}