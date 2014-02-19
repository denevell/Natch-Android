package org.denevell.droidnatch.threads.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.denevell.droidnatch.threads.list.entities.ThreadResource;
import org.denevell.natch.android.R;

public class ListThreadsArrayAdapter extends ArrayAdapter<ThreadResource> {
    public ListThreadsArrayAdapter(Context context, int textViewResourceId) {
    	super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(convertView==null) {
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_threads_row, parent, false);
    	}
        RelativeLayout rl = (RelativeLayout) convertView;
        TextView threadTitle = (TextView) rl.findViewById(R.id.list_thread_row_title_textview);
        TextView threadAuthor = (TextView) rl.findViewById(R.id.list_thread_row_author_textview);
        threadTitle.setContentDescription("list_threads_row"+String.valueOf(position));
        threadAuthor.setContentDescription("list_threads_row_author"+String.valueOf(position));
        ThreadResource o = getItem(position);
        threadTitle.setText(o.getSubject());
        threadAuthor.setText(o.getAuthor());
        return convertView;
    }
    
}