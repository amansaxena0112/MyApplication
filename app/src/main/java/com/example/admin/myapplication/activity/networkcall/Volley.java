package com.example.admin.myapplication.activity.networkcall;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.admin.myapplication.activity.activity.MainActivity;
import com.example.admin.myapplication.activity.activity.SplashActivity;
import com.example.admin.myapplication.activity.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class Volley {
    private static final String TAG = "Volley";

    private Map<String, String> params;
    private Map<String, String> headerParams;
    private String url;
    private Request.Priority priority = Request.Priority.NORMAL;
    private int maxReTry = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
    private String bodyJson;
    private int requestMethod = Request.Method.GET;
    private int timeOut = 10000;

    public Volley() {
    }

    public void execute(String... args) {
        onPreExecute();
        setParamsAndUrl();

        if (params != null) {
            Log.d(TAG, "url: " + url);
            String tag_string_req = url;
            Log.d(TAG, "after_url: " + tag_string_req);
            StringRequest strReq = new StringRequest(requestMethod, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG + " " + url, response);
                    onPostExecute(Constants.SERVICE_VOLLEY_200, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error: " + error.getMessage());
                    callErrorMethod(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Log.d(TAG, "Params: " + params.toString());
                    return params;
                }

                @Override
                public Priority getPriority() {
                    return priority;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(timeOut,
                    maxReTry,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            SplashActivity.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public void executeHeader(String... args) {
        onPreExecute();
        setParamsAndUrl();
        if (headerParams != null) {
            Log.d(TAG, "url: " + url);
            final String tag_string_req = url;
            Log.d(TAG, "after url: " + tag_string_req);
            StringRequest strReq = new StringRequest(requestMethod, tag_string_req, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG + " " + tag_string_req, response);
                    onPostExecute(Constants.SERVICE_VOLLEY_200, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "error: " + error);
                    callErrorMethod(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Log.d(TAG, "Params: " + params.toString());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Log.d(TAG, "headerParams: " + headerParams.toString());
                    return headerParams;
                }

                @Override
                public Priority getPriority() {
                    return priority;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(timeOut,
                    maxReTry,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            //MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    public void executeHeaderBodyPost(String... args) {
        onPreExecute();
        setParamsAndUrl();
        if (params != null) {
            Log.d(TAG, "url: " + url);
            String tag_string_req = url;
            StringRequest strReq = new StringRequest(requestMethod, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG + " " + url, response);
                    onPostExecute(Constants.SERVICE_VOLLEY_200, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callErrorMethod(error);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Log.d(TAG, "Params: " + params.toString());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    Log.d(TAG, "headerParams: " + headerParams.toString());
                    return headerParams;
                }

                @Override
                public Priority getPriority() {
                    return priority;
                }

                public byte[] getBody() {

                    try {
                        Log.e(TAG, "getBody(): request: " + bodyJson);
                        if (bodyJson != null) {
                            Log.d(TAG, "getBody: " + bodyJson.getBytes(getParamsEncoding()));
                            return bodyJson.getBytes(getParamsEncoding());
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "getBody(): request has no json");
                        e.printStackTrace();
                    }
                    return new byte[0];
                }
            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(timeOut,
                    maxReTry,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Adding request to request queue
            //MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private void callErrorMethod(VolleyError error) {

        String json = null;
        NetworkResponse response = error.networkResponse;
        if (response != null) {
            if (response.data != null) {
                json = new String(response.data);
            }
            onPostExecute(response.statusCode, json);
        } else {
            onPostExecute(Constants.SERVICE_VOLLEY_000, Constants.SERVICE_VOLLEY_CANT_REACH);
        }
    }

    protected abstract void setParamsAndUrl();

    protected void onPreExecute() {
    }

    protected void onPostExecute(int statusCode, String response) {
    }

    protected void setParams(Map<String, String> params) {
        this.params = params;
    }

    protected void setHeaderParams(Map<String, String> params) {
        this.headerParams = params;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    protected void setPriority(Request.Priority priority) {
        this.priority = priority;
    }

    public void setMaxReTry(int maxReTry) {
        this.maxReTry = maxReTry;
    }


    protected String getBodyJson() {
        return bodyJson;
    }

    protected void setBodyJson(String bodyJson) {
        this.bodyJson = bodyJson;
    }

    protected void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }
}
