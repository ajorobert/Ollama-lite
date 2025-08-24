package com.example.ollamalite.data.api

import com.example.ollamalite.data.model.GenerateRequest
import com.example.ollamalite.data.model.GenerateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

import com.example.ollamalite.data.model.TagsResponse
import retrofit2.http.GET

interface OllamaApi {
    @POST("/api/generate")
    suspend fun generate(@Body request: GenerateRequest): Response<GenerateResponse>

    @GET("/api/tags")
    suspend fun getModels(): Response<TagsResponse>
}
