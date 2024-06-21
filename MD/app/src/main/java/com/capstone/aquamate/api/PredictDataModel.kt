package com.capstone.aquamate.api

import com.google.gson.annotations.SerializedName

data class PredictDataModel (
    @SerializedName("id") val id: String,
    @SerializedName("result") val result: String,
    @SerializedName("explanation") val explanation: String,
    @SerializedName("timestamp") val timestamp: String
)