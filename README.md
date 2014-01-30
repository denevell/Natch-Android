# Android Natch

The Android client to the Natch REST project.

## Backlog  

* Tech task: Build step to clear the database via sql call

## Ice-box

### Edit thread 

* I should be able to edit my thread title
* I should see an error on edit thread if there
* I should see a specific editing loading screen

### Delete thread 

* I should see an error if the service provides one
* I should see delete thread specific loading screen

### Add threads

* I should see add thread loading screen after the thread add
* I should see an error if add failed

### List threads

* I should see a error screen if they fail to load
* I should be able to refresh the threads
* I should see a loading icon while the threads load
* I should see a retry option
* I shouldn't add the thread on empty editext
* I should see empty screen on no threads
* I should keep the found threads on rotate / onResume
* I should be able to press more to see more threads
* I should see how many more pages there are to view
* I should be able to set the numbers of threads shown

--

## Dev complete

* Tech task: Start using Espresso testing framework.
* Tech task: UI build step to login and register
* Tech task: UI build step to change session id
* Tech task: Build step to add threads via rest calls
* Tech task: Method to change the base url from Application or a static class
* Task: Move the auth key to strings.xml
* Task: Move setting the request headers to a method
* Task: Setup ci server that simply builds it
* Task: Setup ci server that runs ui tests
* Task: Setup ci server that updates the dev version on the play store
* Task: Start using gradle for normal builds
* Task: Add robolectic
* Task: Should start unit testing
* Task: Start ui testing
* Task: EditText which automatically hides the keyboard
* Task: Gradle for the junit tests

### Stories (Numbers relate to classes names in the uiTests dir)

1. I should see some threads in the app
#. I should see a loading screen while the threads are loaded
3. I should be able to add a new thread (subject only atm)
#. I should refresh the (plus loading) threads after an add
#. I should be able to delete a thread I have created 
#. I should load the posts in a thread after it's clicked on, and add a post
#. I should delete post from a thread, and then see the existing thread, one post less.
#. I should delete a thread from within the thread page
