package com.capstone.aquamate.api

import com.google.gson.annotations.SerializedName

data class PredictFishResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("predicUrl")
	val predicUrl: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("explanation")
	val explanation: String? = null,
)