package com.dcsim.youkon

/// Holds the user's name and multiple projects
class UserData {
    var name = "New User"
    var projects = mutableListOf<Project>()
}

/// For testing, create a generic user
fun testUser(): UserData {
    val data = UserData()
    data.name = "Eliott"
    data.projects.add(wembyProject())
    data.projects.add(spaceProject())
    return data
}