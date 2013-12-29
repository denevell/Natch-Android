package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListThreadsArrayAdapter extends ArrayAdapter<ThreadResource> {
    public ListThreadsArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setContentDescription(v.getContentDescription()+String.valueOf(position));
        ThreadResource o = getItem(position);
        v.setText(o.getSubject());
        return v;
    }
}