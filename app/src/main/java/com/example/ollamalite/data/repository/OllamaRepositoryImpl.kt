package com.example.ollamalite.data.repository

import com.example.ollamalite.data.api.OllamaApi
import com.example.ollamalite.data.model.GenerateRequest
import com.example.ollamalite.data.model.GenerateResponse
import retrofit2.Response
import javax.inject.Inject

import com.example.ollamalite.data.model.TagsResponse

class OllamaRepositoryImpl @Inject constructor(
    private val ollamaApi: OllamaApi
) : OllamaRepository {
    override suspend fun generate(request: GenerateRequest): Response<GenerateResponse> {
        return ollamaApi.generate(request)
    }

    override suspend fun getModels(): Response<TagsResponse> {
        return ollamaApi.getModels()
    }
}
