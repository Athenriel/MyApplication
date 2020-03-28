# MyApplication
Example using Kotlin, MVVM, Navigation, View Binding, Koin, Room, WorkManager, Retrofit


Git Cheatsheet

Create new branch
1. git checkout -b branch_name
2. git push -u origin branch_name

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
4. git push -from

Squash commits into 1 commit, N is number of commits
1. git rebase -i HEAD~N
2. {squash commits}
3. git push -f