# Google Maps setup

The Home location tab opens the interactive library map. Select a pin or library chip to see its rooms; select a room for details. Back from a room returns to the map, and Back from the map returns home.

1. In Google Cloud, enable **Maps SDK for Android** in a project with billing enabled.
2. Create an API key restricted to Android application `com.example.booked` and your signing certificate SHA-1. Obtain the debug SHA-1 with `./gradlew signingReport`; use your release certificate for release builds. Restrict API access to Maps SDK for Android.
3. Add `MAPS_API_KEY=your_key_here` to the root `local.properties` (already ignored by Git). Do not commit the key. CI can supply the `MAPS_API_KEY` environment variable instead.
4. Sync Gradle and rebuild/run the app. Use a device/emulator with Google Play services and internet access.

Without a key, the screen displays “Map unavailable” and still lets you browse libraries and open rooms. Invalid/restricted keys or network problems may leave tiles blank; check Maps SDK authorization errors in Logcat and verify the package/SHA-1 restrictions.

The sample repository currently contains Olin and Uris rooms. Coordinates are approximate library-level sample locations, not indoor room positions or entrances. Add `RoomLocation(latitude, longitude)` to future repository rooms to include them. Availability is also sample data. No device-location permission is requested; this feature shows library pins, not the user's live position.

Official setup: https://developers.google.com/maps/documentation/android-sdk/get-api-key
