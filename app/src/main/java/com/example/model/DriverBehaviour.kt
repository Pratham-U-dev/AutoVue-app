package com.example.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DriverBehaviourRequest(
    @Json(name = "rpm_values") val rpmValues: List<Double>,
    @Json(name = "speed_values") val speedValues: List<Double>,
    @Json(name = "throttle_values") val throttleValues: List<Double>
)

@JsonClass(generateAdapter = true)
data class DriverBehaviourResponse(
    @Json(name = "cluster_id") val clusterId: Int,
    @Json(name = "behaviour_class") val behaviourClass: String,
    @Json(name = "features_debug") val featuresDebug: Map<String, Double>?
)
