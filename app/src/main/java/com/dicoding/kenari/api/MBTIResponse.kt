package com.dicoding.kenari.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MbtiResultResponse(
    @Json(name = "message") val message: String,
    @Json(name = "data") val data: MbtiData
)

@JsonClass(generateAdapter = true)
data class MbtiData(
    @Json(name = "typeResult") val typeResult: MbtiTypeResult,
    @Json(name = "probResult") val probResult: Double,
    @Json(name = "resultDate") val resultDate: String
)

@JsonClass(generateAdapter = true)
data class MbtiTypeResult(
    @Json(name = "code") val code: String,
    @Json(name = "type") val type: String,
    @Json(name = "information") val information: String
)