package org.denevell.droidnatch.addthread.entities;

import org.denevell.droidnatch.listthreads.entities.ThreadResource;



public class AddPostResourceReturnData extends SuccessOrError {
	private ThreadResource thread;

	public ThreadResource getThread() {
		return thread;
	}

	public void setThread(ThreadResource thread) {
		this.thread = thread;
	}

}