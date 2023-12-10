package com.marcossan.despensa.data.network

//import com.google.gson.JsonObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

//import kotlinx.serialization.json.JsonObject

@Serializable
data class ProductJson (
    val code: String,
    val product: JsonObject,
    val status: Int,
    @SerialName(value = "status_verbose")
    val statusVerbose: String
)