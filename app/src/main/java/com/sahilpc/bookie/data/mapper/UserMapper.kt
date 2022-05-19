package com.sahilpc.bookie.data.mapper

import at.favre.lib.crypto.bcrypt.BCrypt
import com.sahilpc.bookie.data.local.entity.UserEntity
import com.sahilpc.bookie.domain.model.User

fun UserEntity.toUser(): User {
    return User(
        name = name,
        phone = phone,
        password = password,
        email = email
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        name = name,
        phone = phone,
        password = BCrypt.withDefaults().hashToString(12, password.toCharArray()),
        email = email
    )
}