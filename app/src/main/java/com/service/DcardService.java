package com.service;

import com.Utility.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


public class DcardService {

    private String baseUri;
    private String controller;
    protected HttpGet httpReq;
    public String responseMsg;
    public boolean status;
    // public AndroidHttpClient client;
    protected HttpResponse response;
    protected Map<String, String> getPostParams;
    private static HttpClient client = new DefaultHttpClient();

    public DcardService() {
        this.baseUri = Utility.BASE_URL;
        this.controller = "";
        this.getPostParams = new HashMap<String, String>();
        this.responseMsg = "";
        this.status = false;
    }

    protected String getData(String method) {
        // client = new DefaultHttpClient();

        String dataAsString = "";
        try {
            HttpResponse response = null;
            System.out.println(this.getUri(method));

            HttpGet getRequest;
            HttpPost postRequest;
            HttpPut putRequest;
            // request.setHeader("User-Agent", "set your desired User-Agent");
            if (method.endsWith("GET")) {
                System.out.println("GET");
                getRequest = new HttpGet(this.getUri(method));
                response = client.execute(getRequest);
            } else if (method.equalsIgnoreCase("POST")) {
                postRequest = new HttpPost(this.getUri(method));
                postRequest.setEntity(new UrlEncodedFormEntity(this
                        .getPostParamsEntity()));
                response = client.execute(postRequest);
            } else if (method.equalsIgnoreCase("PUT")) {
                System.out.println("PUT METHOD");
                putRequest = new HttpPut(this.getUri(method));
                putRequest.setEntity(new UrlEncodedFormEntity(this
                        .getPostParamsEntity()));
                response = client.execute(putRequest);
            }

            if (response != null) {

                StatusLine status = response.getStatusLine();
                if (status.getStatusCode() != 200) {
                    throw new IOException("Internal Server Error "
                            + status.toString());
                }
                System.out.println("ok");
                // Pull content stream from response
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();

                ByteArrayOutputStream content = new ByteArrayOutputStream();

                // Read response into a buffered stream
                int readBytes = 0;
                byte[] sBuffer = new byte[512];
                while ((readBytes = inputStream.read(sBuffer)) != -1) {
                    content.write(sBuffer, 0, readBytes);
                }

                // Return result from buffered stream
                dataAsString = new String(content.toByteArray());
                System.out.println(dataAsString);
                return dataAsString;
            } else {
                return "";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataAsString;
    }

    protected void setParams(Map<String, String> getPostParams) {
        this.getPostParams = getPostParams;
    }

    protected void setParams(String key, String value) {
        this.getPostParams.put(key, value);
    }



    protected void setController(String controller) {
        this.controller = controller;
    }

    protected String getUri(String method) {
        if (method.equalsIgnoreCase("POST")) {

            return this.baseUri+ this.controller;
        }
        String params = this.getPostURLEncodedParams();
        if (params != null && params != "") {
            params = "?" + params;
        }
        String thePath = this.baseUri;

        return baseUri + this.controller;
    }

    protected String getPostURLEncodedParams() {
        StringBuilder sb = new StringBuilder();
        String value = "";
        for (String key : this.getPostParams.keySet()) {
            try {
                value = URLEncoder.encode(this.getPostParams.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    protected List<NameValuePair> getPostParamsEntity() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : this.getPostParams.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        return parameters;
    }

}
