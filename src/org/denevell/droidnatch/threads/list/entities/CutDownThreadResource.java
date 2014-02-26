package org.denevell.droidnatch.threads.list.entities;

import java.util.List;

/**
 * Used for serialisation into json in the push request
 */
public class CutDownThreadResource {

	private List<String> tags;
	private String id;
	private String subject;
	private String author;
	private int numPosts;
	private long creation;
	private long modification;
	private long rootPostId;
	
	public CutDownThreadResource(ThreadResource tr) {
		subject = tr.getSubject();
		author = tr.getAuthor();
		numPosts = tr.getNumPosts();
		tags = tr.getTags();
		rootPostId = tr.getRootPostId();
		modification = tr.getModification();
		creation = tr.getCreation();
		id = tr.getId();
	}

	public CutDownThreadResource() {
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String title) {
		this.subject = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getNumPosts() {
		return numPosts;
	}

	public void setNumPosts(int maxPages) {
		this.numPosts = maxPages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getLatestPage(int postsPerPage) {
		double i = (double) this.numPosts / (double) postsPerPage;
		return (int) Math.ceil(i);
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public long getModification() {
		return modification;
	}

	public void setModification(long modification) {
		this.modification = modification;
	}

	public long getCreation() {
		return creation;
	}

	public void setCreation(long creation) {
		this.creation = creation;
	}
	
    public long getRootPostId() {
        return rootPostId;
    }



    public void setRootPostId(long rootPostId) {
        this.rootPostId = rootPostId;
    }	

}
