package com.thebaft.app.cartpulse

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform