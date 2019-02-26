# PopularMoviesProject

Project built for [Udacity's Android Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801).

Android app that fetches and displays movie information from [The Movie Database (TMDb)](https://www.themoviedb.org/). The user can check the details of a chosen movie and add the movie to a "favorite movies" list for easier access.

* Main View presents the user a grid of movie posters. The sort order can be changed in the Settings menu (sort criteria: most popular, highest rated, user's favorite).
* Selecting a movie poster will open a screen with additional information on the movie. 
* In the movie details screen, the star shaped button allows the user to mark a movie as favorite.
* Tapping a trailer video will display the video by opening either the youtube app or a web browser.


## Features

Android Architecture Components:
* The user's favorite movies information are stored in a Room database. 
* LiveData from ViewModel is used to not re-requery database unnecessarily after screen rotation.

<p align="center">
<img src="https://raw.githubusercontent.com/ootahiaoo/PopularMoviesProject/master/screenshot/Screenshot_1.png" width="200" title="Main View">   <img src="https://raw.githubusercontent.com/ootahiaoo/PopularMoviesProject/master/screenshot/Screenshot_2.png" width="200" title="Main View's Settings menu">   <img src="https://raw.githubusercontent.com/ootahiaoo/PopularMoviesProject/master/screenshot/Screenshot_3.png" width="200" title="Movie Details View">
</p>

## How to install

### Step 1
Clone the repository using git (or download it as a zip), then import the project in Android Studio.
```
git clone https://github.com/ootahiaoo/PopularMoviesProject.git
```

### Step 2

The app fetches information from [The Movie Database (TMDb) API](https://www.themoviedb.org/documentation/api). 
You need to register and get your own API KEY in order to use this app. Once you have a key, create a `gradle.properties` file:

1. From the Project panel, change the view from `Android` to `Project` directory.
2. Right click on the `PopularMoviesProject` file > New > File
3. Put the name as **gradle.properties**
4. If asked about "Open matching files in Android Studio", select `Properties` type.

Then paste the following line in the `gradle.properties` file, including your API key instead of `your-api-key`.

```
myApiTheGuardian="your-api-key"
```

If AndroidStudio still complains, please check that the BuildConfig is importing the right option in the MainActivity.java file.

![alt text](https://raw.githubusercontent.com/ootahiaoo/PopularMoviesProject/master/screenshot/BuildConfigScreenshot.jpg "BuildConfig options in MainActivity.java")


## License
_To be added._

Feel free to make pull requests/suggest improvements.
