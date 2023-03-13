package com.sahilpc.bookie.data.repository

import com.sahilpc.bookie.data.local.AppDatabase
import com.sahilpc.bookie.data.local.datastore.AuthDatastore
import com.sahilpc.bookie.data.local.entity.NoteEntity
import com.sahilpc.bookie.data.mapper.toNote
import com.sahilpc.bookie.data.mapper.toNoteEntity
import com.sahilpc.bookie.domain.model.Note
import com.sahilpc.bookie.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Exception
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val db: AppDatabase,
    private val authDatastore: AuthDatastore
) : NoteRepository {
    override suspend fun getAllNotes(): Flow<List<Note>> {
        val email = authDatastore.getLoggedInUser()
        return db.getNoteDao().getAllNotesByMail(email!!).map { it.map { it.toNote() } }
    }

    override suspend fun insertNote(note: Note): Boolean {
        val email = authDatastore.getLoggedInUser()
        return try {
            db.getNoteDao().insertNote(note.toNoteEntity(email!!))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteNoteById(id: Int): Boolean {
        return try {
            db.getNoteDao().deleteNoteById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}