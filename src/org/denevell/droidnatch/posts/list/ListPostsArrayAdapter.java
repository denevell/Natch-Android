package org.denevell.droidnatch.posts.list;


import org.denevell.droidnatch.app.utils.AndroidUtils;
import org.denevell.droidnatch.posts.list.entities.PostResource;
import org.denevell.natch.android.R;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListPostsArrayAdapter extends ArrayAdapter<PostResource> {
    @SuppressWarnings("unused")
	private int mLastPosition = -1;

	public ListPostsArrayAdapter(Context context, int textViewResourceId) {
    	super(context, android.R.layout.simple_list_item_1);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	if(convertView==null) {
    		convertView = LayoutInflater.from(getContext()).inflate(R.layout.posts_list_row, parent, false);
    	}
        RelativeLayout rl = (RelativeLayout) convertView;
        TextView threadTitle = (TextView) rl.findViewById(R.id.list_posts_row_title_textview);
        TextView threadAuthor = (TextView) rl.findViewById(R.id.list_posts_row_author_textview);
        threadTitle.setContentDescription("list_posts_row"+String.valueOf(position));
        threadAuthor.setContentDescription("list_posts_row_author"+String.valueOf(position));
        PostResource o = getItem(position);
        threadTitle.setText(o.getContent());
        threadAuthor.setText(o.getUsername());

        TextView dateText = (TextView) rl.findViewById(R.id.list_posts_row_date_textview);
        dateText.setContentDescription("list_posts_row_date"+String.valueOf(position));

        dateText.setText(o.getLastModifiedDate()+"\n"+o.getLastModifiedTime());

//        if(position>0 && position > mLastPosition) {
//        	Animation animation = AnimationUtils.loadAnimation(getContext(), (position > mLastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        	convertView.startAnimation(animation);
//        }
        mLastPosition = position;        

		// So it doesn't clash with transparent nav bar
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (position == getCount() - 1) {
				convertView.setPadding(0, 0, 0, AndroidUtils.getNavigationBarHeight(getContext()));
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
		}

        return convertView;
        
    }    
    
}