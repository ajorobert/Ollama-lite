package com.example.ollamalite.data.api

import com.example.ollamalite.data.model.GenerateRequest
import com.example.ollamalite.data.model.GenerateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaApi {
    @POST("/api/generate")
    suspend fun generate(@Body request: GenerateRequest): Response<GenerateResponse>
}
