# MyApplication
Example using Kotlin, Model-View-ViewModel, Coroutines, LiveData, Navigation, View Binding, Koin, Room, WorkManager, Retrofit and Unit Tests with Mockk

Learn the basics with this simple App:
- Create and store User with Room
- Pattern with Regex to match alphabetic user names
- Create Notification after creating User
- View users in a Recycler View with a Click Listener to delete the user from Room
- Download an image file from a valid url with Retrofit, monitor the download % and then save the file in a predefined folder to share with Intent Action Send
- Get the Firebase Instance Id device token and send it to your server using a retrying worker
- Coroutine Worker as well as Coroutines implemented in the Local and Remote Data Sources
- View Binding and Navigation from Android Jetpack
- Unit Tests for the View Models and the Worker using KoinTest and Mockk
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
