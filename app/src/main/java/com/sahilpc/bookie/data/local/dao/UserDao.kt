package com.sahilpc.bookie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sahilpc.bookie.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM userentity Where email = :user OR phone = :user")
    suspend fun loginUser(user: String): UserEntity?

}