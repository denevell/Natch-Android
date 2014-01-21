package org.denevell.droidnatch.posts.list.di.resultsdisplayable;


import org.denevell.droidnatch.posts.list.entities.PostResource;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListPostsArrayAdapter extends
        ArrayAdapter<PostResource> {
    public ListPostsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        String pos = String.valueOf(position);
        v.setContentDescription("list_posts_row"+pos);
        PostResource o = getItem(position);
        v.setText(o.getContent());
        return v;
    }
}