package com.capstone.aquamate.api

import com.google.gson.annotations.SerializedName


data class DetailDictionaryResponseApi(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("id")
    val id: String,

    @SerializedName("fishName")
    val fishName: String,

    @SerializedName("fishDesc")
    val fishDesc: String,

    @SerializedName("fishImage")
    val fishImage: String?,

    @SerializedName("fishLatinName")
    val fishLatinName: String,

    @SerializedName("createdAt")
    val createdAt: CreatedAtDetailDictionaryResponse
)

data class CreatedAtDetailDictionaryResponse(
    @SerializedName("_seconds")
    val seconds: Int,

    @SerializedName("_nanoseconds")
    val nanoseconds: Int
)
