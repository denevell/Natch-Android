package org.denevell.droidnatch.posts.list.entities;

import java.util.ArrayList;
import java.util.List;


public class ListPostsResource {
	
	private List<PostResource> posts = new ArrayList<PostResource>();

	public List<PostResource> getPosts() {
		return posts;
	}

	public void setPosts(List<PostResource> posts) {
		this.posts = posts;
	}

}
