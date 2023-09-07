package com.dcsim.youkon

/// Holds the user's name and multiple projects
class YkUser {
    var name = "New User"
    var projects = mutableListOf<YkProject>()
}

/// For testing, create a generic user
fun testUser(): YkUser {
    val data = YkUser()
    data.name = "Eliott"
    data.projects.add(wembyProject())
    data.projects.add(spaceProject())
    return data
}