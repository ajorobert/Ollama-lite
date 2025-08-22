package com.example.ollamalite.data.model

import com.squareup.moshi.Json

data class GenerateRequest(
    @Json(name = "model") val model: String,
    @Json(name = "prompt") val prompt: String,
    @Json(name = "stream") val stream: Boolean = false
)
