package com.seenubommisetti.app.cartpulse

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
