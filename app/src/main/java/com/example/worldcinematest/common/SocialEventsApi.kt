package com.example.worldcinematest.common

import retrofit2.http.GET

interface  SocialEventsApi {
    @GET("social/upcoming/events/507f191e810c19729de860ea")
    suspend fun getEventById(): SocialEvents
}