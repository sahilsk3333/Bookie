package com.sahilpc.bookie.domain.repository

import com.sahilpc.bookie.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun getAllNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note): Boolean
    suspend fun deleteNoteById(id: Int): Boolean
}