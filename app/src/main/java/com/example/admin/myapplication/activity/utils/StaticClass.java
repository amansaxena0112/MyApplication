package com.example.admin.myapplication.activity.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.activity.activity.MainActivity;
import com.example.admin.myapplication.activity.model.MovieModel;
import com.example.admin.myapplication.activity.networkcall.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class StaticClass {

    private static final String TAG = "StaticClass";

    private static ProgressDialog loadingDialog = null;

    private static void showProgressPopup(Context context, String showText) {
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setMessage(showText);
        loadingDialog.setCancelable(false);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.show();
    }

    public static void callGetMovies(Context context) {
        HashMap<String, String> headerParams = new HashMap<>();


        HashMap<String, String> params = new HashMap<>();

        new GetMovies(context, params, headerParams).execute();
    }

    private static class GetMovies extends Volley {
        private final Map<String, String> params, hParams;
        private final Context context;

        GetMovies(Context context, HashMap<String, String> params, HashMap<String, String> hParams) {
            this.params = params;
            this.context = context;
            this.hParams = hParams;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: ");
        }

        @Override
        protected void setParamsAndUrl() {
            String url = "https://api.themoviedb.org/3/search/movie" + "?" + Constants.KEY + "=" + Constants.API_KEY
                    + "&" + Constants.INCLUDE_ADULT + "=false" + "&" + Constants.KEY_QUERY + "=" + "action" + "&page=1";
            setRequestMethod(Request.Method.GET);
            setParams(params);
            setUrl(url);
            setMaxReTry(Constants.VOLLEY_RETRY_POSTDATA);
            setPriority(Request.Priority.IMMEDIATE);
        }

        @Override
        protected void onPostExecute(int statusCode, String response) {
            Log.d(TAG, "Response from UpdateMobile service: " + response);
            Log.d(TAG, "Response from UpdateMobile service: " + statusCode);

            switch (statusCode) {
                case Constants.SERVICE_VOLLEY_200:
                    Gson gson = new Gson();
                    MovieModel movieModel = gson.fromJson(response,MovieModel.class);
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.setSaveMovies(movieModel);
                    Log.d(TAG, "onPostExecute: "+movieModel.getResults().size());
                    Log.d(TAG, "onPostExecute: "+movieModel.getPage());
                    Log.d(TAG, "onPostExecute: "+movieModel.getTotal_results());
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                    break;
                case Constants.SERVICE_VOLLEY_000:
                    Toast.makeText(context, context.getString(R.string.cant_reach_server), Toast.LENGTH_SHORT).show();
                    break;
                case Constants.SERVICE_VOLLEY_403:
                    Toast.makeText(context, context.getString(R.string.invalid_session), Toast.LENGTH_SHORT).show();
                    //Utility.sessionLogout(context);
                    break;
                case Constants.SERVICE_VOLLEY_500:
                    Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    if (response != null && !response.equalsIgnoreCase("")) {
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
