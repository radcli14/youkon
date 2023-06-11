package com.dcsim.youkon

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform