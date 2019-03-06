package com.example.admin.myapplication.activity.utils;

public class Constants {

    /*Voller re -try count*/
    public static final int VOLLEY_RETRY_GETDATA = 5;
    public static final int VOLLEY_RETRY_POSTDATA = 0;//DefaultRetryPolicy.DEFAULT_MAX_RETRIES;
    // Volley
    public static final int SERVICE_VOLLEY_200 = 200; // success
    public static final int SERVICE_VOLLEY_000 = 000; // unable to receive no message from server(not reachable)
    public static final int SERVICE_VOLLEY_403 = 403; //session expires in server(invalid token)
    public static final int SERVICE_VOLLEY_402 = 402; // invalid otp
    public static final int SERVICE_VOLLEY_401 = 401; // invalid coupon
    public static final int SERVICE_VOLLEY_400 = 400; // invalid coupon
    public static final int SERVICE_VOLLEY_404 = 404; // forgot password email not register
    public static final int SERVICE_VOLLEY_500 = 500; //500 error
    public static final String SERVICE_VOLLEY_CANT_REACH = "error";

    public static final String KEY = "api_key";
    public static final String LANGUAGE = "language";
    public static final String INCLUDE_ADULT = "include_adult";
    public static final String KEY_QUERY = "query";
    public static final String API_KEY = "c7cbcc56d961a049c8da49c800d24e32";
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    public static final String KEY_MOVIE_MODEL = "movie_model";
}
