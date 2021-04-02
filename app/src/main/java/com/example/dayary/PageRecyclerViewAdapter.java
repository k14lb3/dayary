package com.example.dayary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PageRecyclerViewAdapter extends RecyclerView.Adapter<PageRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Page> pages = new ArrayList<>();
    private Context mContext;
    private int test = 0;
    private int mCurrentViewType = 0;

    public PageRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void toggleItemViewType(int n) {
        mCurrentViewType = n;
    }

    @Override
    public int getItemViewType(int position) {
        return mCurrentViewType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mCurrentViewType != 0) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_display_portrait, parent, false));
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_display_landscape, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if(getAppTheme()){
            holder.parent.setCardElevation(0);
        }

        int moods_one_displayed_size = mContext.getResources().getDimensionPixelSize(R.dimen.moods_one_displayed_size);
        int moods_two_displayed_size = mContext.getResources().getDimensionPixelSize(R.dimen.moods_two_displayed_size);
        int moods_three_displayed_size = mContext.getResources().getDimensionPixelSize(R.dimen.moods_three_displayed_size);

        Page pagePosition = pages.get(position);

        holder.imgPage.setImageBitmap(pages.get(position).getThumbnail());

        holder.txtDayNum.setText(pagePosition.getDateDayOfTheWeekNum());
        holder.txtDay.setText(pagePosition.getDateDayOfTheWeek());
        holder.imgFavorite.setVisibility(pagePosition.getMoodFavoriteVisibility());
        holder.imgHappy.setVisibility(pagePosition.getMoodHappyVisibility());
        holder.imgSad.setVisibility(pagePosition.getMoodSadVisibility());
        holder.imgBad.setVisibility(pagePosition.getMoodBadVisibility());

        // For both horizontal and vertical view//
        // Centers the date if no moods are displayed //
        if (holder.imgFavorite.getVisibility() == View.GONE &&
                holder.imgHappy.getVisibility() == View.GONE &&
                holder.imgSad.getVisibility() == View.GONE &&
                holder.imgBad.getVisibility() == View.GONE) {
            holder.clMoods.setVisibility(View.GONE);
        }


        // For horizontal view //
        if (!getAppLayout()) {
            //Change mood size depending on the number of displayed moods //

            // If only one mood is displayed //
            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgFavorite.getLayoutParams().height = moods_one_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_one_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 8 &&
                    pagePosition.getMoodHappyVisibility() == 0 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgHappy.getLayoutParams().height = moods_one_displayed_size;
                holder.imgHappy.getLayoutParams().width = moods_one_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 8 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgSad.getLayoutParams().height = moods_one_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_one_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 8 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgBad.getLayoutParams().height = moods_one_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_one_displayed_size;
            }

            //If two moods are displayed //
            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 0 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgFavorite.getLayoutParams().height = moods_two_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_two_displayed_size;
                holder.imgHappy.getLayoutParams().height = moods_two_displayed_size;
                holder.imgHappy.getLayoutParams().width = moods_two_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgFavorite.getLayoutParams().height = moods_two_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_two_displayed_size;
                holder.imgSad.getLayoutParams().height = moods_two_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_two_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgFavorite.getLayoutParams().height = moods_two_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_two_displayed_size;
                holder.imgBad.getLayoutParams().height = moods_two_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_two_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 8 &&
                    pagePosition.getMoodHappyVisibility() == 0 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgHappy.getLayoutParams().height = moods_two_displayed_size;
                holder.imgHappy.getLayoutParams().width = moods_two_displayed_size;
                holder.imgSad.getLayoutParams().height = moods_two_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_two_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 8 &&
                    pagePosition.getMoodHappyVisibility() == 0 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgHappy.getLayoutParams().height = moods_two_displayed_size;
                holder.imgHappy.getLayoutParams().width = moods_two_displayed_size;
                holder.imgBad.getLayoutParams().height = moods_two_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_two_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 8 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgSad.getLayoutParams().height = moods_two_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_two_displayed_size;
                holder.imgBad.getLayoutParams().height = moods_two_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_two_displayed_size;
            }

            // If three moods are displayed //
            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 0 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 8) {
                holder.imgFavorite.getLayoutParams().height = moods_three_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_three_displayed_size;
                holder.imgHappy.getLayoutParams().height = moods_three_displayed_size;
                holder.imgHappy.getLayoutParams().width = moods_three_displayed_size;
                holder.imgSad.getLayoutParams().height = moods_three_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_three_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 0 &&
                    pagePosition.getMoodSadVisibility() == 8 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgFavorite.getLayoutParams().height = moods_three_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_three_displayed_size;
                holder.imgHappy.getLayoutParams().height = moods_three_displayed_size;
                holder.imgHappy.getLayoutParams().width = moods_three_displayed_size;
                holder.imgBad.getLayoutParams().height = moods_three_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_three_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgFavorite.getLayoutParams().height = moods_three_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_three_displayed_size;
                holder.imgSad.getLayoutParams().height = moods_three_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_three_displayed_size;
                holder.imgBad.getLayoutParams().height = moods_three_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_three_displayed_size;
            }

            if (pagePosition.getMoodFavoriteVisibility() == 0 &&
                    pagePosition.getMoodHappyVisibility() == 8 &&
                    pagePosition.getMoodSadVisibility() == 0 &&
                    pagePosition.getMoodBadVisibility() == 0) {
                holder.imgFavorite.getLayoutParams().height = moods_three_displayed_size;
                holder.imgFavorite.getLayoutParams().width = moods_three_displayed_size;
                holder.imgSad.getLayoutParams().height = moods_three_displayed_size;
                holder.imgSad.getLayoutParams().width = moods_three_displayed_size;
                holder.imgBad.getLayoutParams().height = moods_three_displayed_size;
                holder.imgBad.getLayoutParams().width = moods_three_displayed_size;
            }
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PageActivity.class);
                intent.putExtra("date", pagePosition.getDate());
                intent.putExtra("dateDayOfTheWeek", pagePosition.getDateDayOfTheWeek());

                ByteArrayOutputStream byteArrayOutputStream;
                byte[] bytes;

                byteArrayOutputStream = new ByteArrayOutputStream();
                pagePosition.getThumbnail().compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
                intent.putExtra("thumbnail",bytes);

                byteArrayOutputStream = new ByteArrayOutputStream();
                pagePosition.getCover().compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
                intent.putExtra("cover",bytes);

                intent.putExtra("title", pagePosition.getTitle());
                intent.putExtra("note", pagePosition.getNote());
                intent.putExtra("favorite", pagePosition.getMoodFavoriteVisibility());
                intent.putExtra("happy", pagePosition.getMoodHappyVisibility());
                intent.putExtra("sad", pagePosition.getMoodSadVisibility());
                intent.putExtra("bad", pagePosition.getMoodBadVisibility());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public void setPages(ArrayList<Page> pages) {
        this.pages = pages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView parent;
        private ConstraintLayout clMoods, clDate;
        private ImageView imgPage, imgFavorite, imgHappy, imgSad, imgBad;
        private TextView txtDayNum, txtDay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cvParent);
            clDate = itemView.findViewById(R.id.clDate);
            clMoods = itemView.findViewById(R.id.clMoods);
            imgPage = itemView.findViewById(R.id.imgCover);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            imgHappy = itemView.findViewById(R.id.imgHappy);
            imgSad = itemView.findViewById(R.id.imgSad);
            imgBad = itemView.findViewById(R.id.imgBad);
            txtDayNum = itemView.findViewById(R.id.txtDayNum);
            txtDay = itemView.findViewById(R.id.txtDay);
        }
    }

    // SharedPreferences //

    // Layout method/s //
    private boolean getAppLayout() {
        return PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("vertical", false);
    }

    // Theme methods //
    private boolean getAppTheme() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getBoolean("dark_mode", false);
    }

}
