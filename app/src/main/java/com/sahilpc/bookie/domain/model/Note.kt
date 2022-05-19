package com.sahilpc.bookie.domain.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Int = 0,
    val title: String,
    val description: String,
    val image: Bitmap
): Parcelable
