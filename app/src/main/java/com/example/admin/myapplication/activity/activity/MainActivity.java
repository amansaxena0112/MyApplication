package com.example.admin.myapplication.activity.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private RecyclerView movieRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MovieAdapter movieAdapter;
    private SessionManager sessionManager;
    private List<MovieResultModel> movieList = new ArrayList<>();
    private List<MovieResultModel> favmovieList = new ArrayList<>();
    private DrawerLayout navDrawerLayout;
    private ActionBarDrawerToggle navActionBarDrawerToggle;
    private NavigationView navigationView;
    private ListView lst_menu_items;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        initActionBar();
        sessionManager = new SessionManager(this);
        movieList = sessionManager.getSavedMovies().getResults();
        Log.d(TAG, "onCreate:main "+movieList.size());
        Log.d(TAG, "onCreate:main "+movieList.toString());
        movieRecyclerView = findViewById(R.id.recycler_movie);
        mLayoutManager = new LinearLayoutManager(this);
        movieRecyclerView.setLayoutManager(mLayoutManager);
        movieRecyclerView.setHasFixedSize(true);
        movieAdapter = new MovieAdapter(this, true);
        movieAdapter.setLinearLayoutManager(mLayoutManager);
        movieAdapter.setRecyclerView(movieRecyclerView);
        movieAdapter.setOnItemClickListener(this);
        movieAdapter.addAll(movieList);
        movieRecyclerView.setAdapter(movieAdapter);
        movieRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        navDrawerLayout = findViewById(R.id.drawer_layout);
        navActionBarDrawerToggle = getActionBarDrawerToggle();
        navDrawerLayout.addDrawerListener(navActionBarDrawerToggle);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lst_menu_items = findViewById(R.id.lst_menu_items);
        List<String> menuList = new ArrayList<>();
        menuList.add("Movies");
        menuList.add("Favourites");
        //menuList.add("Reorder");
        lst_menu_items.setAdapter(new NavigationMenuAdapter(this, menuList));
        lst_menu_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "pos: " + i);
                displaySelectedActivity(i);
            }
        });
    }

    private void displaySelectedActivity(int position){
        navDrawerLayout.closeDrawer(GravityCompat.START);
        switch (position) {
            case 0:
                Intent mIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case 1:
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle() {
        return new ActionBarDrawerToggle(MainActivity.this, navDrawerLayout,toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                /** calling onPrepareOptionsMenu() to show action bar icons **/
                //invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                /** calling onPrepareOptionsMenu() to hide action bar icons **/
                //invalidateOptionsMenu();
            }
        };
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setElevation(0);
    }

    @Override
    public void onItemClick(View view, int position, boolean liked) {
        Log.d(TAG, "onItemClick: "+favmovieList);
        if (liked) {
            if (!favmovieList.contains(movieList.get(position))) {
                favmovieList.add(movieList.get(position));
            }
        }else {
            favmovieList.remove(movieList.get(position));
        }
        sessionManager.setSavedFavourites(favmovieList);
    }

    @Override
    public void onButtonClick(View view, int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_MODEL, movieList.get(position).getId());
        startActivity(intent);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
