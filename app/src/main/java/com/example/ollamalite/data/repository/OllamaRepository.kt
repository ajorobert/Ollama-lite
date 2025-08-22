package com.example.ollamalite.data.repository

import com.example.ollamalite.data.model.GenerateRequest
import com.example.ollamalite.data.model.GenerateResponse
import retrofit2.Response

interface OllamaRepository {
    suspend fun generate(request: GenerateRequest): Response<GenerateResponse>
}
