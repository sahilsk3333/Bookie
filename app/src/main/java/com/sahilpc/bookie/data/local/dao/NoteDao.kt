package com.sahilpc.bookie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sahilpc.bookie.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT  * FROM noteentity WHERE email = (:email) ORDER BY id ASC")
    fun getAllNotesByMail(email: String): Flow<List<NoteEntity>>

    @Query("DELETE FROM noteentity WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

}