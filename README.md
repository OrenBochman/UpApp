# UpApp

Android Capstone Project.

Some Features:

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

Some patterns in use :

* Model View ViewMode (MVVM)
* viewHolder patten
* master detail pattern
* singleton patten
* [repository pattern](https://developer.android.com/jetpack/docs/guide) Repository is a facade for accessing the db off the main thread.

# References

Modern development relies on databases like stackoverflow.
The following information & code snippets proved useful:

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

Others were less useful

* [any layout manager for ConstraintLayout?](https://stackoverflow.com/questions/37803180/any-layout-manager-for-constraintlayout)
