package com.example.worldcinematest.common

data class SocialEvents(
    val events: List<Events>
)

data class Events(
    val name: String,
    val eventDate: String,
    val description: String,
    val urlMedia: String
)
