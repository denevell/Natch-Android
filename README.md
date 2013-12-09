# Android Natch

The Android client to the Natch REST project.

## Backlog

* I should be able to delete a thread I own
* I shouldn't add the thread on empty editext
* Task: EditText which automatically hides the keyboard

## Ice-box


### Add threads

* I should see add thread loading screen after the thread add
* I should see an error if add failed

### List threads

* I should see a error screen if they fail to load
* I should be able to refresh the threads
* I should see a loading icon while the threads load
* I should see a retry option
* I should see empty screen on no threads
* I should keep the found threads on rotate / onResume
* I should be able to press more to see more threads
* I should see how many more pages there are to view
* I should be able to set the numbers of threads shown

--

* Task: Start using gradle for normal builds
* Task: Gradle for the junit tests
* Task: Setup ci server that simply builds it
* Task: Setup ci server that runs unit tests
* Task: Setup ci server that runs ui tests
* Task: Setup ci server that updates the dev version on the play store

## Dev complete

* Task: Add robolectic
* Task: Should start unit testing
* Task: Start ui testing

### Stories (Numbers relate to classes names in the uiTests dir)

1. I should see the x most recent thread titles
2. I should see a loading screen while the threads are loaded
3. I should be able to add a new thread (subject only atm)
4. I should refresh the (plus loading) threads after an add
