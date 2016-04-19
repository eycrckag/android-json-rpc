package com.axier.jsonrpclibrary;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;


/**
 * Created by lijianqun on 16/4/18.
 */
public class JSONARRAYRPCClient {
    //TODO
    protected JSONRPCParams.Versions version;
    protected String encoding = HTTP.UTF_8;

    private HttpClient httpClient;
    private String serviceUri;
    protected int soTimeout = 0, connectionTimeout = 0;
    private static final ProtocolVersion PROTOCOL_VERSION = new ProtocolVersion("HTTP", 1, 0);
    public static JSONARRAYRPCClient create(String uri, JSONRPCParams.Versions version) {
        JSONARRAYRPCClient client = new JSONARRAYRPCClient(uri);
        client.version = version;
        return client;
    }

    public JSONARRAYRPCClient(String uri) {
        this(new DefaultHttpClient(), uri);
    }

    public JSONARRAYRPCClient(HttpClient client, String uri) {
        httpClient = client;
        serviceUri = uri;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }


    public JSONArray doJSONRequest(JSONArray jsonRequest) throws JSONRPCException {
        // Create HTTP/POST request with a JSON entity containing the request
        HttpPost request = new HttpPost(serviceUri);
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, getConnectionTimeout());
        HttpConnectionParams.setSoTimeout(params, getSoTimeout());
        HttpProtocolParams.setVersion(params, PROTOCOL_VERSION);
        request.setParams(params);

        HttpEntity entity;
        try {
            entity = new JSONEntity(jsonRequest);
            request.setEntity(entity);

            long t = System.currentTimeMillis();
            HttpResponse response = httpClient.execute(request);
            t = System.currentTimeMillis() - t;
            String responseString = EntityUtils.toString(response.getEntity());

            responseString = responseString.trim();
            JSONArray jsonResponse = new JSONArray(responseString);

            return  jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return  new JSONArray();
        }
    }

}
