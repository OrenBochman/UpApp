# UpApp

Android Capstone Project.

## Requirements :

1. observing and responding to wifi & power connectivity changes. - Set result in repo + return observer.
2. search near-by & with & without keyword.
3. persisting last search for next use.
4. calculate distance of each point of interest.
5. display place name, address & the result in a recyclerview which
6. responds to click events by showing the place on the map and
9. responds to long click by showing a menu with:
10. sharing a place
11. show last search results if offline.
12. support adaptive layouts - master-detail for  tablet and handset.
13. a setting icon in the toolbar opening a preference with:  
15. clearing the favourites
16. setting the units

## THe UseCases Features:

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

* [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) - This encapsulates the UI state.
* [Observer](https://en.wikipedia.org/wiki/Observer_pattern) - this allows the UI to respond to asynchronous data updates. 
* [Adapter](https://en.wikipedia.org/wiki/Adapter_pattern) - to allow Recycler Views to consume custom data types.
* [View Holder](https://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html) - to optimise resource use by Recycler Views 
* [Master Detail](https://medium.com/@lucasurbas/case-study-master-detail-pattern-revisited-86c0ed7fc3e) - to facilitate adaptive layout for handset and tablets
* [Singleton](https://en.wikipedia.org/wiki/Singleton_pattern)  - to ensure certain objects are created only once.
* [Repository pattern](https://developer.android.com/jetpack/docs/guide) Repository centralises
  data access and decouples it from web requests. Repo returns observers and dispatches data base and api requests as needed and off the UI thread.

# References

Modern development relies on accessing numerous information sources such as:
 * StackOverflow ❓
 * Code path 🛣
 * Google & Android developer docs 🍭
 * Blogposts & Articles Ⓜ
 * GoogleI/O & Android Dev Summit talks, other Videos 🎥
 * Codelabs 🧪
 * Wikipedia / Reference Books 📚

## The following information & code snippets proved useful:

* Bitmap
  * ❓[How to use vector drawables in Android API lower 21?](https://stackoverflow.com/questions/34417843/how-to-use-vector-drawables-in-android-api-lower-21/34417988)
  * ❓[converting Java bitmap to byte array](https://stackoverflow.com/questions/4989182/converting-java-bitmap-to-byte-array)
  * ❓[Programmatically save/convert image to webp format](https://stackoverflow.com/questions/53277442/programmatically-save-convert-image-to-webp-format)
* Room DB
  * ❓[Please provide a Migration in the builder or call fallbackToDestructiveMigration in the builder in which case Room will re-create all of the tables](https://stackoverflow.com/questions/49629656/please-provide-a-migration-in-the-builder-or-call-fallbacktodestructivemigration)
  * Ⓜ️[Understanding migrations with Room](https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929) by Florina Muntenescu
  * 🧪[Android Room with a View CodeLab](https://codelabs.developers.google.com/codelabs/android-room-with-a-view)
  * ❓[Multiple tables with same type of objects in Room database](https://stackoverflow.com/questions/48279481/multiple-tables-with-same-type-of-objects-in-room-database)
  * 🍭[Create views into a database](https://developer.android.com/training/data-storage/room/creating-views)
* Location & Places SDK
  * ❓[How to convert lat lng to a Location variable?](https://stackoverflow.com/questions/31099140/how-to-convert-lat-lng-to-a-location-variable)
  * 🍭[Places SDK for Android - Place Autocomplete](https://developers.google.com/places/android-sdk/autocomplete#get_place_predictions_programmatically)
* Permissions
  * 🎥["Mother, May I?" Asking for Permissions](https://youtu.be/5xVh-7ywKpE?t=25m25s) - Android Dev Summit 2015, how to work with external storage without  permissions
  * 🎥[Updating Your Apps for Location Permission Changes in Android Q](https://youtu.be/L7zwfTwrDEs?t=383) Google I/O'19 covering ACCESS_BACKGROUND_LOCATION
  * 🍭[Request App Permissions Guide](https://developer.android.com/training/permissions/requesting) - The official guide    
  * Ⓜ️[How to customise permission request](https://www.journaldev.com/10409/android-runtime-permissions-example#requesting-android-runtime-permissions)
  * ❓[How to ask for location permission in Android Studio?](https://stackoverflow.com/questions/57098852/how-to-ask-for-location-permission-in-android-studio)
* Wifi State
  * ❓[Android: Stop/Start service depending on WiFi state?](https://stackoverflow.com/questions/7094606/android-stop-start-service-depending-on-wifi-state)
* RecyclerView 
  * ❓[How ListView's recycling mechanism works](https://stackoverflow.com/questions/11945563/how-listviews-recycling-mechanism-works)
  * ❓[data doesn't show up on recyclerview - got error No layout manager attached; skipping layout](https://stackoverflow.com/questions/51359950/data-doesnt-show-up-on-recyclerview-got-error-no-layout-manager-attached-ski)
* Sharing
  * ❓[How to share text to WhatsApp from my app?](https://stackoverflow.com/questions/12952865/how-to-share-text-to-whatsapp-from-my-app)
  * 🛣️[Sharing Content with Intents](https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-remote-images)
* Networking
  * ❓[Missing org/apache/http/client/methods/HttpUriRequest on Android app](https://stackoverflow.com/questions/46283831/missing-org-apache-http-client-methods-httpurirequest-on-android-app)
* File Storage
  * 🛣️[Persisting Data to the Device](https://guides.codepath.com/android/Persisting-Data-to-the-Device)
  * ❓[MODE_WORLD_READABLE no longer supported](https://stackoverflow.com/questions/39121052/java-lang-securityexception-mode-world-readable-no-longer-supported)
  * 🍭[Save a file on external storage](https://developer.android.com/training/data-storage/files/external#ExternalStoragePermissions)
  * 🍭[View on-device files with Device File Explorer](https://developer.android.com/studio/debug/device-file-explorer)
  * ❓[Android: How to create an AVD with an external sd card?](https://stackoverflow.com/questions/26934394/android-how-to-create-an-avd-with-an-external-sd-card)
* Asynchronicity & Background processing
  * 🍭[Guide to background processing](https://developer.android.com/guide/background)
  * ❓[When to use RxJava in Android and when to use LiveData from Android Architectural Components?](https://stackoverflow.com/questions/46312937/when-to-use-rxjava-in-android-and-when-to-use-livedata-from-android-architectura)
  * Ⓜ [Unit Testing Asynchronous Tasks from Bolts-Android](https://medium.com/@trionkidnapper/unit-testing-asynchronous-tasks-from-bolts-android-e780f02bf1be)
* Git & VCS
  * ❓[How to use git branch with Android Studio](https://stackoverflow.com/questions/24657326/how-to-use-git-branch-with-android-studio)
  * 📚[Android version history](https://en.wikipedia.org/wiki/Android_version_history) from Wikipedia
* odds & ends
  * ❓[Any layout manager for ConstraintLayout?](https://stackoverflow.com/questions/37803180/any-layout-manager-for-constraintlayout)
  * ❓[Android IllegalStateException No instrumentation registered! Must run under a registering instrumentation](https://stackoverflow.com/questions/32957741/android-illegalstateexception-no-instrumentation-registered-must-run-under-a-re)
* Patterns & Architecture
  * 📚[Strategy Pattern](https://en.wikipedia.org/wiki/Strategy_pattern)
  * ❓[Android M - check runtime permission - how to determine if the user checked “Never ask again”?](https://stackoverflow.com/questions/30719047/android-m-check-runtime-permission-how-to-determine-if-the-user-checked-nev)
  * ❓[What is the benefit of ViewHolder pattern in android?](https://stackoverflow.com/questions/21501316/what-is-the-benefit-of-viewholder-pattern-in-android)
  * Ⓜ[Clean Architecture Tutorial for Android: Getting Started](https://www.raywenderlich.com/3595916-clean-architecture-tutorial-for-android-getting-started)
