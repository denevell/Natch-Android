The Android client to the Natch REST project.

## Backlog  

* Tech: Use a lower resource hungry emulator in the docker container.
* Bug: Thread title on edit and rotation
* Bug: More button on threads page
* I should see whether I am logged in or not, showing who I am logged in as.

## Ice-box

### Edit thread 

* I should see an error on edit thread if there
* I shouldn't be able to edit thread if not mine

### Edit post 

* I shouldn't be able to edit post if not mine
* I should see an error on edit post if there

### Delete thread 

* I should see an error if the service provides one
* I should see delete thread specific loading screen

### Add threads

* I should see an error if add failed
* I should be able to add tags when adding a thread

### List threads

* I should see a error screen if they fail to load
* I should be able to refresh the threads
* I should see a retry option
* I shouldn't add the thread on empty editext
* I should see empty screen on no threads
* I should be able to specify / change the default number of threads / posts to see on pagination.
* I should keep the found threads on rotate / onResume / app close
* I should be able to keep the position of the refreshed list

--

## Dev complete

* Should close the keyboard on clicking a list item
* Shouldn't be able to click the edit text during the loading screens

## Tasks complete

* Task: Think about controllers as views
* Task: Get session id from static class instead of strings file
* Task: Change session id for ui tests in ui test class
* Task: Change url for ui tests in ui test class
* Task: Refactor / redesign ui event object code.
* Task: Integrate Espresso tests with CI environment.
* Task: Build step to clear the database via sql call
* Task: Start using Espresso testing framework.
* Task: UI build step to login and register
* Task: UI build step to change session id
* Task: Build step to add threads via rest calls
* Task: Method to change the base url from Application or a static class
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

0. I should be able to login and change the auth key for the calls.
1. I should see some threads along with content of first post in the app
3. I should be able to add a new thread and first post in thread
4. I should refresh the (plus loading) threads after an add
5. I should be able to delete a thread I have created 
6. I should load the posts in a thread after it's clicked on, and add a post
7. I should delete a thread from within the thread page
8. I should delete post from a thread, and then see the existing thread, one post less.
9. I should be able to edit my post
10: I should be able to edit my thread title and content
11: I should be able to press more to see more threads
12: I should be able to press more to see more posts 
13. I should see error logging on in cases of bad username / password
