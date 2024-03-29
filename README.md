<p align="center">
<img src="app/src/main/res/mipmap-xxhdpi/ic_launcher_round.png" align="center" />
</p>

<h1 align="center">Recipes</h1>

<p align="center">  
Recipes is a simple demo app based on modern Android application tech-stacks and CLEAN architecture.
</p>
<br>
<p align="center">
<img src="previews/fav_day.png" width="24%"/> <img src="previews/detail_day.png" width="24%"/> <img src="previews/fav_night.png" width="24%"/>  <img src="previews/detail_night.png" width="24%"/>
</p>

## Tech stack & Open-source libraries
- ViewBinding - Allows to more easily write code that interacts with views.
- ViewModel - State Holder. Responsible for the production of UI state, contains the necessary logic for that task.
- Lifecycle - Lifecycle-aware components.
- Navigation - In-app navigation.
- SafeArgs - Passing data while navigating between fragments.
- Room - Database.
- Kotlin Coroutines - A concurrency design pattern to simplify code that executes asynchronously.
- Kotlin Flows - In coroutines, a flow is a type that can emit multiple values sequentially, as opposed to suspend functions that return only a single value. For example, you can use a flow to receive live updates from a database.
- [Dagger-Hilt](https://dagger.dev/hilt/gradle-setup) - Dependency injection.
- [Glide](https://github.com/bumptech/glide) - Image loading.
- [Retrofit2 & Gson](https://github.com/square/retrofit) - REST API consumption.
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - A scriptable web server for testing HTTP clients.
- [OkHttp Idling Resource](https://github.com/JakeWharton/okhttp-idling-resource) - An Espresso IdlingResource for OkHttp.
- [Google Play Services Location](https://developers.google.com/android/guides/setup) - Fused location provider.
- [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top of Android's normal Log class.
- [SpinKit](https://github.com/ybq/Android-SpinKit) - Loading animations.
- [Shimer](https://github.com/facebook/shimmer-android) - To add a shimmering effect to any view.
- [Material-Components](https://github.com/material-components/material-components-android) Modular and customizable Material Design UI components for Android.
- [Mockito](https://github.com/mockito/mockito-kotlin) - Mocking framework for unit tests.
- [Leakcanary](https://square.github.io/leakcanary/getting_started/) - A memory leak detection library.

## Architecture
<h3 align="center">CLEAN Architecture</h3>
<br>
<p align="center">
  <img src="previews/clean.png" width="35%" />
</p>
<br><br>
<h3 align="center">MVVM Architecture & Repository Pattern</h3>
<br>
<p align="center">
 <img src="previews/architecture_1.png" width="75%"/>
</p>
<br><br>
<h3 align="center">Single Source Of Truth & Offline Mode</h3>
<br>
<p align="center">
 <img src="previews/architecture_2.png" width="90%"/>
</p>
<br><br>

## Features
- ViewPager2 + TabLayout
  - Fragment 1 - Bookmarked recipes.
  - Fragment 2 - Search recipes by name.
  - Fragment 3 - Search recipes by country if ACCESS_COARSE_LOCATION permission granted.
- [Single source of truth (Room)](https://developer.android.com/jetpack/guide/data-layer#source-of-truth)
- [Unidirectional Data Flow](https://developer.android.com/jetpack/guide/ui-layer#udf)
- [Error handling (Result<T>)](https://developer.android.com/jetpack/guide/data-layer#expose-errors)
- Offline mode
- [User events in ItemUIState](https://developer.android.com/jetpack/guide/ui-layer/events?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-architecture%23article-https%3A%2F%2Fdeveloper.android.com%2Fjetpack%2Fguide%2Fui-layer%2Fevents#recyclerview-events)
- [Fragment constructor injection with FragmentFactory + Hilt](https://medium.com/supercharges-mobile-product-guide/fragmentfactory-with-dagger-and-hilt-31ee17babf73#14ad)
- CollapsingToolbarLayout
- Day/Night theme
- Orientation changes support
- Multi-module project. One module per CLEAN Architecture layer.
- Unit Tests with Flow
- UI Tests
  - Navigation
  - Drawable changes
  - Fragments with FragmentFactory + Hilt

## Preview
<H4 align="center">Normal Internet Connection</H4>
<p align="center">
<img src="previews/preview_1.gif" />
</p>

<H4 align="center">Slow Internet Connection</H4>
<p align="center">
<img src="previews/preview_2.gif" />
</p>

<H4 align="center">No Internet Connection</H4>
<p align="center">
<img src="previews/preview_3.gif" />
</p>

## MAD Scorecards
<img src="previews/summary.png"/>
<img src="previews/kotlin.png"/>
<img src="previews/jetpack.png"/>

## API
TheMealDB is an open, crowd-sourced database of Recipes from around the world.
They also offer a free JSON API for anyone wanting to use it, with additional features for subscribers.
Recipes uses the TheMealDB API. Obtain your API_KEY [TheMealDB](https://www.themealdb.com/api.php) and paste it to the gradle.properties file to try the app.
