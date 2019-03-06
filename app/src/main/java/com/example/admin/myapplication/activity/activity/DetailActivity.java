package com.example.admin.myapplication.activity.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.activity.adapter.MovieAdapter;
import com.example.admin.myapplication.activity.model.MovieDetailModel;
import com.example.admin.myapplication.activity.model.MovieModel;
import com.example.admin.myapplication.activity.model.MovieResultModel;
import com.example.admin.myapplication.activity.networkcall.Volley;
import com.example.admin.myapplication.activity.utils.Constants;
import com.example.admin.myapplication.activity.utils.SessionManager;
import com.example.admin.myapplication.activity.utils.StaticClass;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private TextView status_textview,name_textview,date_textview,vote_textview,description_textview,popularity_textview,language_textview;
    private int movieId;
    private Toolbar toolbar;
    private ImageView movie_image;

    private static ProgressDialog loadingDialog = null;

    private void showProgressPopup(Context context, String showText) {
        loadingDialog = new ProgressDialog(context);
        loadingDialog.setMessage(showText);
        loadingDialog.setCancelable(false);
        loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        initActionBar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        movieId = getIntent().getIntExtra(Constants.KEY_MOVIE_MODEL,550);
        name_textview = findViewById(R.id.name_textview);
        date_textview = findViewById(R.id.date_textview);
        vote_textview = findViewById(R.id.vote_textview);
        status_textview = findViewById(R.id.status_textview);
        popularity_textview = findViewById(R.id.popularity_textview);
        language_textview = findViewById(R.id.language_textview);
        movie_image = findViewById(R.id.movie_image);
        description_textview = findViewById(R.id.description_textview);
        callGetMovieDetail(DetailActivity.this, movieId);
    }

    public void setValues(MovieDetailModel movieDetailModel){
        name_textview.setText(movieDetailModel.getOriginal_title());
        date_textview.setText(movieDetailModel.getRelease_date());
        popularity_textview.setText(String.valueOf(movieDetailModel.getPopularity()));
        vote_textview.setText(String.valueOf(movieDetailModel.getVote_average()));
        description_textview.setText(movieDetailModel.getOverview());
        language_textview.setText(movieDetailModel.getOriginal_language());
        status_textview.setText(movieDetailModel.getStatus());
        Glide.with(DetailActivity.this).load(Constants.IMAGE_URL+movieDetailModel.getPoster_path()).into(movie_image);
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setElevation(0);
    }

    public void callGetMovieDetail(Context context, int movieId) {
        HashMap<String, String> headerParams = new HashMap<>();

        HashMap<String, String> params = new HashMap<>();

        new GetMovieDetail(context, params, movieId).execute();
    }

    private class GetMovieDetail extends Volley {
        private final Map<String, String> params;
        private final Context context;
        private final int moviedId;

        GetMovieDetail(Context context, HashMap<String, String> params, int movieID) {
            this.params = params;
            this.context = context;
            this.moviedId = movieID;
        }

        @Override
        protected void onPreExecute() {
            if (context != null) {
                showProgressPopup(context, context.getString(R.string.please_wait));
            }
        }

        @Override
        protected void setParamsAndUrl() {
            String url = "https://api.themoviedb.org/3/movie/"+ moviedId + "?" + Constants.KEY + "=" + Constants.API_KEY;
            setRequestMethod(Request.Method.GET);
            setParams(params);
            setUrl(url);
            setMaxReTry(Constants.VOLLEY_RETRY_POSTDATA);
            setPriority(Request.Priority.IMMEDIATE);
        }

        @Override
        protected void onPostExecute(int statusCode, String response) {
            if (context != null && loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }

            switch (statusCode) {
                case Constants.SERVICE_VOLLEY_200:
                    Gson gson = new Gson();
                    MovieDetailModel movieDetailModel = gson.fromJson(response,MovieDetailModel.class);
                    setValues(movieDetailModel);
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
