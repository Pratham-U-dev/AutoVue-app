package com.example.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HealthPredictionRequest(
    @Json(name = "rpm") val rpm: Double,
    @Json(name = "throttle_pos") val throttlePos: Double,
    @Json(name = "map_kpa") val mapKpa: Double,
    @Json(name = "maf") val maf: Double,
    @Json(name = "coolant_temp") val coolantTemp: Double,
    @Json(name = "intake_air_temp") val intakeAirTemp: Double,
    @Json(name = "ambient_temp") val ambientTemp: Double,
    @Json(name = "pedal_d") val pedalD: Double
)

@JsonClass(generateAdapter = true)
data class HealthPredictionResponse(
    @Json(name = "status") val status: String,
    @Json(name = "confidence") val confidence: Double,
    @Json(name = "probabilities") val probabilities: Map<String, Double>
)
