package com.capstone.aquamate.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class recomendation(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable