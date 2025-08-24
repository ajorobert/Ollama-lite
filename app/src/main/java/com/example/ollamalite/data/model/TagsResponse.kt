package com.example.ollamalite.data.model

import com.squareup.moshi.Json

data class TagsResponse(
    val models: List<Model>
)

data class Model(
    val name: String,
    @Json(name = "modified_at") val modified_at: String,
    val size: Long
)
