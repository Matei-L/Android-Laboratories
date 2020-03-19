# Android-Laboratories

## Lab 5
### P1:
I user the Preference API from [here](https://developer.android.com/guide/topics/ui/settings.html) and made my SettingsActivity and 
SettingsFragment as it was described in that example. Then, I listened to Preference changes and updated the dark mode accordingly.
### P2:
I used Room as ORM and created a new database named ProductsDatabase. The BaseEntity and BaseDao are from my Bachelor's degree project. 
With the help of those two classes I made a callback for the creation of a new database in which I populated the database and in the 
other activities I'm getting LiveData of products in order to display them.
#### Commits of this homework:
* lab5/p1/Added Settings Activity and darkMode
* Added darkmode
* Moved all products into a database using room
