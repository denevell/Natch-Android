package org.denevell.droidnatch.threads.list;

import org.denevell.droidnatch.Application;
import org.denevell.droidnatch.app.visited_db.VisitedPostsTable;
import org.denevell.droidnatch.threads.list.entities.ThreadResource;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newfivefour.android.manchester.R;

public class ListThreadsArrayAdapter extends ArrayAdapter<ThreadResource> {
    @SuppressWarnings("unused")
	private int mLastPosition = -1;
	private VisitedPostsTable mVistedPostsDb;

	public ListThreadsArrayAdapter(Context context, int textViewResourceId) {
    	super(context, android.R.layout.simple_list_item_1);
    	mVistedPostsDb = Application.getVisitedPostsDatabase(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(convertView==null || !(convertView instanceof RelativeLayout)) {
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.threads_list_row, parent, false);
    	}
        RelativeLayout rl = (RelativeLayout) convertView;
        TextView threadTitle = (TextView) rl.findViewById(R.id.list_thread_row_title_textview);
        TextView threadAuthor = (TextView) rl.findViewById(R.id.list_thread_row_author_textview);
        TextView dateText = (TextView) rl.findViewById(R.id.list_thread_row_date_textview);
        threadTitle.setContentDescription("list_threads_row"+String.valueOf(position));
        threadAuthor.setContentDescription("list_threads_row_author"+String.valueOf(position));
        dateText.setContentDescription("list_threads_row_date"+String.valueOf(position));

        ThreadResource o = getItem(position);
        threadTitle.setText(o.getSubject());
        threadAuthor.setText(o.getAuthor());
        int numPosts = o.getNumPosts();
        dateText.setText(" @ " + o.getLastModifiedDate()+" "+o.getLastModifiedTime() + " | Posts: " + numPosts);
        long lastSeen = mVistedPostsDb.isPostIdInTable(o.getLatestPostId());
        if(lastSeen!=o.getModification()) {
        	threadTitle.setTypeface(null, Typeface.BOLD);
        } else {
        	threadTitle.setTypeface(null, Typeface.NORMAL);
        }
        
//        if(position>1 && position > mLastPosition) {
//        	Animation animation = AnimationUtils.loadAnimation(getContext(), (position > mLastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        	convertView.startAnimation(animation);
//        }
        mLastPosition = position;        
        return convertView;
    }
    
}