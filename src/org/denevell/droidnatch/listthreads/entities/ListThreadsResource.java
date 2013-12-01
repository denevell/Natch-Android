package org.denevell.droidnatch.listthreads.entities;

import java.util.ArrayList;
import java.util.List;

public class ListThreadsResource {
	
	private long numOfThreads;
	private List<ThreadResource> threads = new ArrayList<ThreadResource>();

	public List<ThreadResource> getThreads() {
		return threads;
	}

	public void setThreads(List<ThreadResource> posts) {
		this.threads = posts;
	}

	public long getNumOfThreads() {
		return numOfThreads;
	}

	public void setNumOfThreads(long numOfThreads) {
		this.numOfThreads = numOfThreads;
	}

}
