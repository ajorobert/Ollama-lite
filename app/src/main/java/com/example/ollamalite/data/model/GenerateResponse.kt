package com.example.ollamalite.data.model

import com.squareup.moshi.Json

data class GenerateResponse(
    @Json(name = "model") val model: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "response") val response: String,
    @Json(name = "done") val done: Boolean
)
