package org.denevell.droidnatch.addthread.entities;

import java.util.ArrayList;
import java.util.List;

public class AddPostResourceInput {
	
	private String subject;
	private String content;
	private String threadId;
	private List<String> tags = new ArrayList<String>();
	
	public AddPostResourceInput() {
	}
	
	public AddPostResourceInput(String subject, String content) {
		this.subject = subject;
		this.content = content;
	}
	
	public AddPostResourceInput(String subject, String content, List<String> tags) {
		this.subject = subject;
		this.content = content;
		this.tags = tags;
	}
	
	public AddPostResourceInput(String subject, String content, String threadId) {
		this.subject = subject;
		this.content = content;
		this.threadId = threadId;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String thread) {
		this.threadId = thread;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
