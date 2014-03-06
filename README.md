The Android client to the Natch REST project.

## Backlog  
* ~~Remove scroll to end on pagination~~
* ~~Overscrolling in listview if doesn't stick this time.~~
* ~~Change styling of pagination button, size and colour.~~
* ~~Fix ui tests out of memory thing~~
* ~~If styling not enough, scroll to new post on pagination~~
* ~~Change app to light theme~~
* ~~Add lighter styling to list items~~
* ~~Style action menu~~
* ~~Create icon~~

### Icebox

* See if I can tests gcm notifications
* I should see an error on delete thread 
* I should see an error on delete post 
* I should keep the found threads on rotate / onResume / app close
* Only see notification for summaries
* Save login details
* Login from any page
* 403/401 logs you out?
* Tech task: Unit tests from gradle
* I should be able to see new threads notification, a la g+ 
* Sliding add thread 
* Sliding add post

--

## Dev complete

* I should be able to keep the position of the refreshed list
* Should close the keyboard on clicking a list item
* Shouldn't be able to click the edit text during the loading screens
* Goto invidiual thread on notification
* Check if registered for gcm notifications

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
* Task: Use a lower resource hungry emulator in the docker container.

## Complete: To test

* Add thread button disabled if not logged in
* Listview saved on rotation.
* Unregister for push notifications
* Settings screen for gcm stuff
* I should only be able to add thread if I'm logged in
* Better list threads listview

### Stories (Numbers relate to classes names in the uiTests dir)

00.  I should be able to register, and be automatically logged in, and no longer see the register option
0.   I should be able to login and change the auth key for the calls.
0.   I should see an error message on login fail
1.   I should see some threads along with content of first post in the app
1a.  I should see an error on list threads 
1b.  I should see empty screen on no threads
1c.  I should be able to use a refresh button
3.   I should be able to add a new thread and first post in thread
3a.  I should see server error on adding a new thread with blank input
3b.  I should see error when I attempt to add a thread when not logged in
4.   I should refresh the (plus loading) threads after an add
5.   I should be able to delete a thread I have created 
5a.  I shouldn't be able to delete a thread I someone else has created 
6.   I should load the posts in a thread after it's clicked on, and add a post
6a.  I should see an error on add post with blank data
6b.  I should see an error on list posts
6c.  I should be able to refresh the posts 
6d.  I should see error when I attempt to add a post when not logged in
6e.  I should animate down to the list bottom on adding a post
7.   I should delete a thread from within the thread page
7a.  I shouldn't be able to delete a thread I someone else has created 
8.   I should delete post from a thread, and then see the existing thread, one post less.
9.   I should be able to edit my post
9a.  I shouldn't be able to edit a post that's not mine
9b.  I should see an error on edit post with blank data 
10:  I should be able to edit my thread title and content
10a: I shouldn't be able to edit a thread that's not mine
10b: I should see an error on edit thread with blank data
11:  I should be able to press more to see more threads
12:  I should be able to press more to see more posts 
13:  I should see whether I am logged in or not, showing who I am logged in as.
14:  I should see the author of the thread on the threads page
15:  I should see the author of a post in a thread page
16.  I should be able to logout
*:   I should be able to see dates on posts
*:   I should be able to see dates on threads
