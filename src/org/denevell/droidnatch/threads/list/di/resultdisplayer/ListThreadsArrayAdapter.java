package org.denevell.droidnatch.threads.list.di.resultdisplayer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.denevell.droidnatch.threads.list.entities.ThreadResource;

public class ListThreadsArrayAdapter extends ArrayAdapter<ThreadResource> {
    public ListThreadsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setContentDescription("list_threads_row"+String.valueOf(position));
        ThreadResource o = getItem(position);
        v.setText(o.getSubject());
        return v;
    }
}