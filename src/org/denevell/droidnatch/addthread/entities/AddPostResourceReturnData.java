package org.denevell.droidnatch.addthread.entities;



public class AddPostResourceReturnData extends SuccessOrError {
	private ThreadResource thread;

	public ThreadResource getThread() {
		return thread;
	}

	public void setThread(ThreadResource thread) {
		this.thread = thread;
	}

}