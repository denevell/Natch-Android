package org.denevell.droidnatch.threads.list.entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThreadResource {

	private List<String> tags;
	private String id;
	private String subject;
	private String author;
	private int numPosts;
	private long creation;
	private long modification;
	private long rootPostId;
	
	public ThreadResource(ThreadResource tr) {
		subject = tr.subject;
		author = tr.author;
		numPosts = tr.numPosts;
		tags = tr.tags;
	}

	
	
	public ThreadResource(String subject,
			String author, long creation, long modification, 
			List<String> tags) {
		this.tags = tags;
		this.subject = subject;
		this.author = author;
		this.creation = creation;
		this.modification = modification;
	}



	public ThreadResource() {
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
	
	public String getLastModifiedDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(modification);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MMM", Locale.UK).format(c.getTime());
        int year = c.get(Calendar.YEAR);
        String dateString = dom + " " + month + " " + year;		
        return dateString;
	}	
	
	public String getLastModifiedDateWithTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(modification);
        String dateString = new SimpleDateFormat("d MMM yyyy, K:mm:ss a", Locale.UK).format(c.getTime());
        return dateString;
	}



    public long getRootPostId() {
        return rootPostId;
    }



    public void setRootPostId(long rootPostId) {
        this.rootPostId = rootPostId;
    }	

}
