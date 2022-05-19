package com.sahilpc.bookie.data.local.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val title: String,
    val description: String,
    val image: Bitmap
)
