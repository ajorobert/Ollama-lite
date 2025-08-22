package com.example.ollamalite.domain.use_case

import com.example.ollamalite.data.model.GenerateRequest
import com.example.ollamalite.data.model.GenerateResponse
import com.example.ollamalite.data.repository.OllamaRepository
import com.example.ollamalite.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GenerateUseCase @Inject constructor(
    private val repository: OllamaRepository
) {
    operator fun invoke(prompt: String): Flow<Result<GenerateResponse>> = flow {
        try {
            emit(Result.Loading())
            val request = GenerateRequest(model = "llama2", prompt = prompt) // a default model for now
            val response = repository.generate(request)
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("Empty response body"))
            } else {
                emit(Result.Error(response.message()))
            }
        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Result.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
