package org.denevell.droidnatch.thread.delete.entities;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@SuppressLint("SimpleDateFormat")
public class PostResource {

	private long id;
	private String username;
	private String subject;
	private String content;
	private String threadId;
	private long creation;
	private long modification;
	private List<String> tags;
	private boolean adminEdited;
	
	public PostResource() {
	}
	
	public PostResource(String username, long created, long modified, String subject, String content, List<String> tags, boolean adminEdit) {
		this.username = username;
		this.creation = created;
		this.modification = modified;
		this.subject = subject;
		this.content = content;
		this.tags = tags;
		this.adminEdited = adminEdit;
	}
	
	public PostResource(PostResource post) {
		this(post.username, post.creation, post.modification, post.subject, post.content, post.tags, post.adminEdited);
		this.threadId = post.threadId;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		String content2 = content;
		content2 = content2.replace("&gt;", ">");
		return content2;
	}
	public String getQuotedContent() {
		String[] split = content.split("\n");
		for (int i = 0; i < split.length; i++) {
			split[i] = "> " + split[i];
		}
		String c = "";
		for (String string : split) {
			c += string;
		}
		return c+"\n\n";
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreation() {
		return creation;
	}
	public void setCreation(long creation) {
		this.creation = creation;
	}
	public long getModification() {
		return modification;
	}
	public void setModification(long modification) {
		this.modification = modification;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getLastModifiedDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(modification);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        String month = new SimpleDateFormat("MMM").format(c.getTime());
        int year = c.get(Calendar.YEAR);
        String dateString = dom + " " + month + " " + year;		
        return dateString;
	}
	
	public String getLastModifiedDateWithTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(modification);
        String dateString = new SimpleDateFormat("d MMM yyyy, K:mm:ss a").format(c.getTime());
        return dateString;
	}

    public boolean isAdminEdited() {
        return adminEdited;
    }

    public void setAdminEdited(boolean adminEdited) {
        this.adminEdited = adminEdited;
    }	
}
