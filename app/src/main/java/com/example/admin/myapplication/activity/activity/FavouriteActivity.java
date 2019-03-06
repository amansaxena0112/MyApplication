package com.example.admin.myapplication.activity.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.activity.adapter.MovieAdapter;
import com.example.admin.myapplication.activity.adapter.NavigationMenuAdapter;
import com.example.admin.myapplication.activity.model.MovieResultModel;
import com.example.admin.myapplication.activity.utils.Constants;
import com.example.admin.myapplication.activity.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener {

    private static final String TAG = "FavouriteActivity";
    private RecyclerView movieRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MovieAdapter movieAdapter;
    private SessionManager sessionManager;
    private List<MovieResultModel> favmovieList = new ArrayList<>();
    private ActionBarDrawerToggle navActionBarDrawerToggle;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        initActionBar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sessionManager = new SessionManager(this);
        favmovieList = sessionManager.getSavedFavourites();
        movieRecyclerView = findViewById(R.id.recycler_movie);
        mLayoutManager = new LinearLayoutManager(this);
        movieRecyclerView.setLayoutManager(mLayoutManager);
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this, false);
        movieAdapter.setLinearLayoutManager(mLayoutManager);
        movieAdapter.setRecyclerView(movieRecyclerView);
        movieAdapter.setOnItemClickListener(this);
        if (favmovieList != null && !favmovieList.isEmpty()) {
            movieAdapter.addAll(favmovieList);
        }
        movieRecyclerView.setAdapter(movieAdapter);
        movieRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public void onItemClick(View view, int position, boolean liked) {

    }

    @Override
    public void onButtonClick(View view, int position) {
        Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_MODEL, favmovieList.get(position).getId());
        startActivity(intent);

    }
}

