package com.sahilpc.bookie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sahilpc.bookie.data.local.convertors.BitmapConvertor
import com.sahilpc.bookie.data.local.dao.NoteDao
import com.sahilpc.bookie.data.local.dao.UserDao
import com.sahilpc.bookie.data.local.entity.NoteEntity
import com.sahilpc.bookie.data.local.entity.UserEntity

@Database(entities = [NoteEntity::class,UserEntity::class], version = 1)
@TypeConverters(BitmapConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNoteDao():NoteDao
    abstract fun getUserDao():UserDao
}