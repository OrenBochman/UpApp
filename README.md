# UpApp

Android Capstone Project.

## Requirements:

1. observing and responding to wifi & power conectivity changes.
2. search near-by & with & without keyword.
3. persisting last search for next use.
4. calculate distance of each point of interest.
5. display place name, address & the result in a recyclerview which
6. responds to click events by showing the place on the map and
9. responds to long click by showing a menu with:
10. sharing a place
11. show last search results if offline.
12. support adaptive layouts - master-detail for  tablet and handset.
13. a setting icon in the toolbar openning a preference with:  
15. clearing the favourites
16. setting the units

## Some Features:

* MVVM architecture
* live data + observable for updates
* using Master Detail flow
* adaptive and responsive layout (for handsets and tablets)
* RecyclerViews
* Google Maps + Google Places api
* Room Database.
* Room DB migrations.
* Preferences
* Persisting preferences using
* Photos using Picasso third party library.
* Requesting permissions
* Overriding the application object.
* IntentService to handle api requests off the main thread.
* BroadcastReceiver to check for connectivity.
* Testing google places api asynchronously as a JUnit integration tests 

## Some patterns in use :

* Model View ViewMode (MVVM)
* observable
* viewHolder patten
* master-detail pattern
* singleton patten
* [repository pattern](https://developer.android.com/jetpack/docs/guide) Repository is a facade for accessing the db off the main thread.

# Test Plan - Test By feature:

##Testing plan + Refactoring for testability.

 1. wifi - These were traditionally done using a **BroadcastReceiver**. When BR are defined in their
      own file they cannot request permissions or access the view model. So BR are refactored into 
      an abstract parent activity that also provides access to the ViewModel and will get methods 
      to access the state as well as SharedPreference methods all in a single object.
   a. Observing changes:
      - changes to power and connection are easly done manually via emulator. (don't know how to automate - ask Oran)
      - changes are reported via intents and android components - possible to mock but not easy to do so
      - setting the view model LiveData can be be set / observed / tested independently. 
   b. Responding to changes: 
       - currently the app just sends a toast this is hard to test and should be refactored so that:
       - the BR fires an intent to the activity which sends the toast or 
       - updating the view-model - which sends the toasts or better yet fires a sank-bar 
       - Note: getting internet connectivity broadcast has now been deprecaded so another method need to be used..   
   c. check un/subscription in lifecycle events + config changes.
 2. Search api.
   a. real world challenges include: asynchronicity, timeouts, bad api keys, permissions, networking.
      * use integration tests to check system resiliency.
      * use unit tests + mocks to check sub tests api calls are correct.
    b. 

# References

Modern development relies on databases like:
 
 * StackOverflow, 
 * code path, 
 * android developer docs.
 * google maps/places api documentation.
 * blogposts.

## The following information & code snippets proved useful:

* [How to use vector drawables in Android API lower 21?](https://stackoverflow.com/questions/34417843/how-to-use-vector-drawables-in-android-api-lower-21/34417988)
* [Please provide a Migration in the builder or call fallbackToDestructiveMigration in the builder in which case Room will re-create all of the tables](https://stackoverflow.com/questions/49629656/please-provide-a-migration-in-the-builder-or-call-fallbacktodestructivemigration)
* [Understanding migrations with Room](https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929) by Florina Muntenescu
* [How to convert lat lng to a Location variable?](https://stackoverflow.com/questions/31099140/how-to-convert-lat-lng-to-a-location-variable)
* [Places SDK for Android - Place Autocomplete](https://developers.google.com/places/android-sdk/autocomplete#get_place_predictions_programmatically)
* [Android Room with a View CodeLab](https://codelabs.developers.google.com/codelabs/android-room-with-a-view)
* [How to ask for location permission in Android Studio?](https://stackoverflow.com/questions/57098852/how-to-ask-for-location-permission-in-android-studio)
* [Requesting App permissions](https://developer.android.com/training/permissions/requesting)
* [How to customise permission request](https://www.journaldev.com/10409/android-runtime-permissions-example#requesting-android-runtime-permissions)
* [Android: Stop/Start service depending on WiFi state?](https://stackoverflow.com/questions/7094606/android-stop-start-service-depending-on-wifi-state)
* [Multiple tables with same type of objects in Room database](https://stackoverflow.com/questions/48279481/multiple-tables-with-same-type-of-objects-in-room-database)
* [data doesn't show up on recyclerview - got error No layout manager attached; skipping layout](https://stackoverflow.com/questions/51359950/data-doesnt-show-up-on-recyclerview-got-error-no-layout-manager-attached-ski)
* [How to share text to WhatsApp from my app?](https://stackoverflow.com/questions/12952865/how-to-share-text-to-whatsapp-from-my-app)
* [converting Java bitmap to byte array](https://stackoverflow.com/questions/4989182/converting-java-bitmap-to-byte-array)
* [How to use git branch with Android Studio](https://stackoverflow.com/questions/24657326/how-to-use-git-branch-with-android-studio)
* [Missing org/apache/http/client/methods/HttpUriRequest on Android app](https://stackoverflow.com/questions/46283831/missing-org-apache-http-client-methods-httpurirequest-on-android-app)

## Others were less useful 

* [any layout manager for ConstraintLayout?](https://stackoverflow.com/questions/37803180/any-layout-manager-for-constraintlayout)
* [Android IllegalStateException No instrumentation registered! Must run under a registering instrumentation](https://stackoverflow.com/questions/32957741/android-illegalstateexception-no-instrumentation-registered-must-run-under-a-re)
* [Unit Testing Asynchronous Tasks from Bolts-Android](https://medium.com/@trionkidnapper/unit-testing-asynchronous-tasks-from-bolts-android-e780f02bf1be)
