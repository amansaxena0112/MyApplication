package com.example.admin.myapplication.activity.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.activity.model.MovieResultModel;
import com.example.admin.myapplication.activity.utils.Constants;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "DriverFilterAdapter";
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int visibleThreshold = 1;
    private final Context context;
    private int firstVisibleItem, visibleItemCount, totalItemCount;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isMoreLoading = false;
    private OnItemClickListener mItemClickListener;
    private List<MovieResultModel> movies;
    private List<MovieResultModel> favmovies;
    private boolean isForMovies;


    public MovieAdapter(Activity context, boolean isforMovies) {
        movies = new ArrayList<>();
        favmovies = new ArrayList<>();
        this.context = context;
        this.isForMovies = isforMovies;

    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void setRecyclerView(RecyclerView mView) {
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
//                    if (onLoadMoreListener != null) {
//                        onLoadMoreListener.onLoadMore();
//                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return movies.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.driver_list_row, parent, false);

            vh = new FilterDriverViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    public void addAll(List<MovieResultModel> lst) {
        movies.clear();
        movies.addAll(lst);
        notifyDataSetChanged();
    }

    public void addItemMore(List<MovieResultModel> lst) {
        movies.addAll(lst);
        notifyItemRangeChanged(0, movies.size());

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FilterDriverViewHolder) {
            MovieResultModel movie = movies.get(position);
            if (favmovies != null && !favmovies.isEmpty()){
                MovieResultModel favMovies = favmovies.get(position);
                if (favmovies.get(position).getId() == movies.get(position).getId()) {
                    ((FilterDriverViewHolder) holder).heart_button.setLiked(true);
                }
            }
            if (!isForMovies){
                ((FilterDriverViewHolder) holder).heart_button.setVisibility(View.GONE);
            }
            ((FilterDriverViewHolder) holder).name_textview.setText(movie.getOriginal_title());
            ((FilterDriverViewHolder) holder).date_textview.setText(movie.getRelease_date());
            ((FilterDriverViewHolder) holder).synopsis_textview.setText(movie.getOverview());
            Glide.with(context).load(Constants.IMAGE_URL+movie.getPoster_path()).into(((FilterDriverViewHolder)holder).movie_image);

        } else {
            ((ProgressViewHolder) holder).loadMoreProgressBar.setIndeterminate(true);
        }
    }

    public void setMoreLoading(boolean isMoreLoading) {
        this.isMoreLoading = isMoreLoading;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void setProgressMore(final boolean isProgress) {
        if (isProgress) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    movies.add(null);
                    notifyItemInserted(movies.size() - 1);
                }
            });
        } else {
            movies.remove(movies.size() - 1);
            notifyItemRemoved(movies.size());
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position,boolean liked);

        void onButtonClick(View view, int position);
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar loadMoreProgressBar;

        public ProgressViewHolder(View v) {
            super(v);
            loadMoreProgressBar = v.findViewById(R.id.progressBar1);
        }
    }

    private class FilterDriverViewHolder extends RecyclerView.ViewHolder{
        final public TextView name_textview,date_textview,synopsis_textview;
        final public Button moreInfo_button;
        final public LikeButton heart_button;
        final public ImageView movie_image;

        public FilterDriverViewHolder(View view) {
            super(view);
            this.setIsRecyclable(false);
            name_textview = view.findViewById(R.id.name_textview);
            movie_image = view.findViewById(R.id.movie_image);
            date_textview = view.findViewById(R.id.date_textview);
            synopsis_textview = view.findViewById(R.id.synopsis_textview);
            moreInfo_button = view.findViewById(R.id.moreInfo_button);
            heart_button = view.findViewById(R.id.heart_button);

            moreInfo_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onButtonClick(v, getAdapterPosition());
                        Log.d(TAG, "onClick: " + getAdapterPosition());
                        Log.d(TAG, "onClick: " + v);
                    }
                }
            });

            heart_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (heart_button.isLiked()){
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(v, getAdapterPosition(),false);
                        }
                        heart_button.setLiked(false);
                    }else {
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(v, getAdapterPosition(),true);
                        }
                        heart_button.setLiked(true);
                    }
                }
            });
//            heart_button.setOnLikeListener(new OnLikeListener() {
//                @Override
//                public void liked(LikeButton likeButton) {
//                    heart_button.setLiked(true);
//
//                }
//
//                @Override
//                public void unLiked(LikeButton likeButton) {
//                    heart_button.setLiked(false);
//                }
//            });

        }

    }
}
