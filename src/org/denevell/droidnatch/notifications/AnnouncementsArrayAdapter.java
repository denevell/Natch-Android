package org.denevell.droidnatch.notifications;

import org.denevell.droidnatch.threads.list.ListThreadsArrayAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newfivefour.android.manchester.R;

public class AnnouncementsArrayAdapter extends ListThreadsArrayAdapter {

	public AnnouncementsArrayAdapter(Context context, int textViewResourceId) {
    	super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	View v = super.getView(position, convertView, parent);

        TextView threadTitle = (TextView) v.findViewById(R.id.list_thread_row_title_textview);
        TextView threadAuthor = (TextView) v.findViewById(R.id.list_thread_row_author_textview);
        TextView dateText = (TextView) v.findViewById(R.id.list_thread_row_date_textview);
        threadTitle.setContentDescription("announcement_title_row"+String.valueOf(position));
        threadAuthor.setContentDescription("announcement_row_author"+String.valueOf(position));
        dateText.setContentDescription("annnouncement_row_date"+String.valueOf(position));
    	return v;
    }
    
}