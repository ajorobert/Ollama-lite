package com.example.ollamalite.domain.use_case

import com.example.ollamalite.data.model.TagsResponse
import com.example.ollamalite.data.repository.OllamaRepository
import com.example.ollamalite.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetModelsUseCase @Inject constructor(
    private val repository: OllamaRepository
) {
    operator fun invoke(): Flow<Result<TagsResponse>> = flow {
        try {
            emit(Result.Loading())
            val response = repository.getModels()
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
