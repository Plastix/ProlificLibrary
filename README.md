# ProlificLibrary

Code test app for the Prolific Interactive Android Internship application.

Tested on Nexus 5 API 23 (Emulator) and Samsung S3 API 19 (Physical Device).

## Libraries
* [Dagger 2](http://google.github.io/dagger/)
* [Retrofit 2](http://square.github.io/retrofit/)
* [RxJava](https://github.com/ReactiveX/RxJava) and [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [RxRelay](https://github.com/JakeWharton/RxRelay)
* [Google Support Libraries](http://developer.android.com/tools/support-library/index.html)
* [Parcler](https://github.com/johncarl81/parceler)
* [Retrolambda](https://github.com/evant/gradle-retrolambda)

## Testing Libraries
* [JUnit](http://junit.org/junit4/)
* [Mockito](http://mockito.org/)

## Requirements
To compile and run the project you'll need:

- [Android SDK](http://developer.android.com/sdk/index.html)
- [Android N (API 24)](http://developer.android.com/tools/revisions/platforms.html)
- Android SDK Tools
- Android SDK Build Tools `24.0.2`
- Android Support Repository
- Android Support Libraries
- JDK 1.8


Building & Installing
--------

To build, install and run a debug version, run this from the root of the project:

```
./gradlew installDebug
```

Testing
-------

To run **unit** tests on your machine:

```
./gradlew test
```

Design Choices / Notes
--------

#### MVVM Architecture with Databinding
I chose to implement Model-View-ViewModel architecture in my app. Like other architectures, it aims to separate business logic from Android UI logic. This makes your business logic easier to test and less coupled to UI changes.

Unlike the Presenter in Model-View-Presenter architcture, the View-Model in MVVM just exposes data and doesn't tell the view to update itself. The ViewModel doesn't know about the view at all. It merely exposes state which the View interacts with. MVVM coupled with the (Beta) Android Databinding library eliminates a lot of boilerplate since your data exposed by your ViewModel can be automatically bound to the view.

Because Databinding is still not compatible with the new Jack compiler/toolchain, I had to use Retrolambda for lambdas and method references.

MVVM References:
- https://labs.ribot.co.uk/approaching-android-with-mvvm-8ceec02d5442#.krdtmw5y1
- http://tech.vg.no/2015/07/17/android-databinding-goodbye-presenter-hello-viewmodel/
- https://medium.com/@hiBrianLee/writing-testable-android-mvvm-app-prelude-e49f7e6223#.a5ff0fabb
- http://upday.github.io/blog/model-view-presenter/
- http://upday.github.io/blog/model-view-viewmodel/

#### Configuration Changes
I chose to handle configuration changes by retaining ViewModels outside of the Activity lifecycle. Thus, on a configuration change, the data inside the ViewModel is retained and when a new activity is bound to the ViewModel it is simply "repopulated" by the data in the ViewModel automatically through databinding.

I am using synchronous loaders to achieve this using a similar pattern as described in this article: https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.ubn4nsbvd.

#### Why Dagger (2)?
Dependency Injection is a great pattern to implement inversion of control for dependencies. Objects shouldn't need to know how to create its depedenncies and we should pass them through the constructor. Dagger is a great framework that reduces the boilerplate of wiring up depednency injection. I choose Dagger 2 since that is what I'm used to using and it removes all the reflection still used in Dagger 1 (So technically it is more efficient). Compile time graph validation is really handy with Dagger since your code won't compile if you "wire" up your dependency graph wrong.

#### Why RxJava?
I can talk about RxJava all day. I chose RxJava for numerous reasons:
- It makes it easy to handle concurrency using the abstraction of Schedulers (which is a big deal on Android)
- It provides numerous abstractions instead of callbacks (`Single`, `Completable`, etc..)
- It makes it easy to combine streams of async data
- Retrofit supports RxJava out of the box!
- RxJava is a great way to expose changing data and events from the `ViewModel`.

#### Why Parcler?
Passing objects between Activities can be a pain since you need to pass parceled objects into Intents. Implementing the Parcelable interface for your objects can be very tedious and error prone. Parcler saves a lot of time by autogenerating code to implement these interfaces for you.

Challenges
--------

#### Databinding
Databinding is still technically in Beta. I had a few issues where methods in my ViewModels wouldn't autogenerate bindings for my XMLs to interact with. I'm not sure if this was an Android Studio bug. It seemed to resolve itself by changing the method names a few times. 

#### Loader Lifecycle
The Loader lifecycle is a little complicated. Since I'm using synchronous loaders to deliver ViewModels to my activities, I had to make sure to only call ViewModels after the appropriate Activity lifecycle methods.

### Add/Edit Screen
Near the end of the project, I needed to support editing a book. Since editing a book is very similar to adding a book, I decided to "hack" it into the Add screen. I had to add some logic into the ViewModel to change the activity title, and change the API endpoint from add book to updateBook.

IMO this screen is not as clean as a I wanted it to me. I'm not sure if there is a better solution.

Future Changes
--------
* Make data models immutable using [AutoValue](https://github.com/google/auto/tree/master/value)
* Switch from Parcler to [Auto-value-parcel](https://github.com/rharter/auto-value-parcel)




