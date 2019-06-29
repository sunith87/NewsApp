package com.newsapp.ui.articlelist.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.newsapp.R;
import org.jetbrains.annotations.NotNull;

class ArticleWeekViewHolder extends RecyclerView.ViewHolder {
    ArticleWeekViewHolder(View view) {
        super(view);
    }

    void bind(int week) {
        TextView weekTextView = itemView.findViewById(R.id.week_text);
        weekTextView.setText(getTextForWeek(week));
    }

    @NotNull
    private String getTextForWeek(int week) {
        Resources resources = itemView.getResources();
        if (week == 1) {
            return resources.getString(R.string.current_week_text);
        } else if (week == 2) {
            return resources.getString(R.string.last_week_text);
        } else {
            return week + " " + resources.getString(R.string.weeks_ago_post_fix);
        }
    }
}
