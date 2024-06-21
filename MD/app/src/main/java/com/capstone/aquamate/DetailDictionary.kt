package com.capstone.aquamate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailDictionary(
    val id: String,
    val fishName: String,
    val fishDesc: String,
    val fishImage: String,
    val createdAt: String,
    val fishLatinName: String?,
): Parcelable