package org.denevell.droidnatch.thread.add.entities;

import org.denevell.droidnatch.threads.list.entities.ThreadResource;



public class AddPostResourceReturnData extends SuccessOrError {
	private ThreadResource thread;

	public ThreadResource getThread() {
		return thread;
	}

	public void setThread(ThreadResource thread) {
		this.thread = thread;
	}

}