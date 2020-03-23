# Android-Laboratories

## Lab 5
### P1:
I used the Preference API from [here](https://developer.android.com/guide/topics/ui/settings.html) and made my SettingsActivity and 
SettingsFragment as it was described in that example. Then, I listened to preference changes and updated the dark mode accordingly.
### P2:
I used [Room](https://developer.android.com/topic/libraries/architecture/room) as ORM and created a new database named ProductsDatabase. The BaseEntity and BaseDao are from my Bachelor's degree project. 
With the help of those two classes I made a callback for the creation of a new database in which I populated the database and in the 
other activities I'm getting LiveDatas of products in order to display them.
#### Commits of this homework:
* lab5/p1/Added Settings Activity and darkMode
* Added darkmode
* Moved all products into a database using room

## Lab 6
### P1:
I used the [Sensor Manager](https://developer.android.com/guide/topics/ui/settings.html) together with the Sensor.TYPE_ALL flag and showed the list using a recyclerView list. The solution is placed inside: SensorListAdapter and SensorActivity.
### P2:
Inside the helpers.permissions package there are some classes from my Bachelor's degree project used to keep the permission checks separated from the activity's logic. This package was used in SensorActivity in order to get all the permissions necessary for getting the last known gps location. After the permissions check, I made a listener on the last known location using [FusedLocationProviderClient](https://developer.android.com/training/location/retrieve-current).
#### Commits of this homework:
* finished lab 6
