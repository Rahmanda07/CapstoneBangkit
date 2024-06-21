package com.capstone.aquamate.api

import com.google.gson.annotations.SerializedName

data class DictionaryFishResponse(
	@SerializedName("status") val status: Boolean,
	@SerializedName("data") val data: List<DataItemFishDictionary>
)

data class DataItemFishDictionary(
	@SerializedName("id") val id: String,
	@SerializedName("fishName") val fishName: String,
	@SerializedName("fishDesc") val fishDesc: String,
	@SerializedName("fishImage") val fishImage: String,
	@SerializedName("fishLatinName") val fishLatinName: String,
	@SerializedName("createdAt") val createdAt: CreatedAt
)

data class CreatedAt(
	@SerializedName("_seconds") val seconds: Long,
	@SerializedName("_nanoseconds") val nanoseconds: Long
)