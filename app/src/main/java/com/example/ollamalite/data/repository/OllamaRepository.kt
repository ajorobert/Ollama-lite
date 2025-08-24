package com.example.ollamalite.data.repository

import com.example.ollamalite.data.model.GenerateRequest
import com.example.ollamalite.data.model.GenerateResponse
import com.example.ollamalite.data.model.TagsResponse
import retrofit2.Response

interface OllamaRepository {
    suspend fun generate(request: GenerateRequest): Response<GenerateResponse>
    suspend fun getModels(): Response<TagsResponse>
}
