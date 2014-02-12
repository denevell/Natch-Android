package org.denevell.droidnatch.posts.list.entities;

import java.util.List;

public class EditPostResource {

	private String content;
	private String subject;
	private List<String> tags;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
