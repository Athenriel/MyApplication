# MyApplication
Example using Kotlin, Model-View-ViewModel, AndroidX, Coroutines, Fused Location, LiveData, Navigation, View Binding, Bottom Navigation, Koin, Room, WorkManager, Retrofit and Unit Tests with Mockk

What is:
- **Kotlin** New supported language, very readable, very concise, interoperable, null safety, more info [here](https://developer.android.com/kotlin)
- **MVVM** Model-View-ViewModel, solid architectural pattern, using android's [View Models](https://developer.android.com/topic/libraries/architecture/viewmodel)
- **AndroidX** Compatibility and recommended code solutions, more info [here](https://developer.android.com/jetpack/androidx)
- **Coroutines** safe threading, managed by view models, more info [here](https://developer.android.com/kotlin/coroutines)
- **Fused Location** receive location updates from available sources, more info [here](https://developers.google.com/location-context/fused-location-provider)
- **LiveData** observable data source, respond to changes and events, lifecycle aware, more info [here](https://developer.android.com/topic/libraries/architecture/livedata)
- **Navigation** provides easy to use "endpoints" as fragments and safe arguments to start them, more info [here](https://developer.android.com/guide/navigation)
- **View Binding** easy and safe interaction with views, more info [here](https://developer.android.com/topic/libraries/view-binding)
- **Bottom Navigation** Bottom Navigation bar using Material Components, more info [here](https://material.io/develop/android/components/bottom-navigation-view/)
- **Koin** Dependency injection, define once, use anywhere, more info [here](https://insert-koin.io/)
- **Room** Data persistance over SQLite, more info [here](https://developer.android.com/topic/libraries/architecture/room)
- **WorkManager** safe async deferable tasks, more info [here](https://developer.android.com/topic/libraries/architecture/workmanager)
- **Retrofit** type safe HTTP calls, more info [here](https://square.github.io/retrofit/)
- **Unit Tests** ensure correct code logic and error handling, more info [here](https://developer.android.com/training/testing/unit-testing)
- **Mockk** allows the creation of "mock" instances of objects to ease unit testing, more info [here](https://mockk.io/)

Learn the basics with this simple App:
- Create and store User with Room
- Pattern with Regex to match alphabetic user names
- Create Notification after creating User
- View users in a Recycler View with a Click Listener to delete the user from Room
- Download an image file from a valid url with Retrofit, monitor the download % and then save the file in a predefined folder to share with Intent Action Send
- Pick one image and copy file to app designated folder and share it
- Use Fused Location Client to get the user location and update server using a retrying worker
- Share location with Intent Action Send
- Get the Firebase Instance Id device token and send it to your server using a retrying worker
- Coroutine Worker as well as Coroutines implemented in the Local and Remote Data Sources
- View Binding and Navigation from Android Jetpack
- Unit Tests for the View Models and the Workers using KoinTest and Mockk
- Some neat utils
- Clean Architecture

Git Cheatsheet

Pull from master
1. git fetch
2. git pull origin master

Create new branch
1. git checkout -b branch_name
2. git push -u origin branch_name

Commit and push
1. git add .
2. git commit -m "message"
3. git push origin branch_name

Uncommit
- git reset --soft "HEAD^"

Amend last commit and push
1. git add .
2. git commit --amend
3. git push -f

Rebase from master and push to branch, if there are no conflicts, skip steps 2 and 3
1. git rebase master
2. {solve conflicts} git add .
3. git rebase --continue
4. git push -f

Squash commits into 1 commit, N is number of commits
1. git rebase -i HEAD~N
2. {squash commits}
3. git push -f
