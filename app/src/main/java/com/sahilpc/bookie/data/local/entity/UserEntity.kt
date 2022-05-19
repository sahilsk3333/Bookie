package com.sahilpc.bookie.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    val name: String,
    val phone: String,
    @PrimaryKey
    val email: String,
    val password: String,
)
