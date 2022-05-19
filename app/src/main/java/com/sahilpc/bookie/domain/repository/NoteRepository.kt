package com.sahilpc.bookie.domain.repository

import com.sahilpc.bookie.domain.model.Note

interface NoteRepository {
    suspend fun getAllNotes(): List<Note>
    suspend fun insertNote(note: Note): Boolean
    suspend fun deleteNoteById(id: Int): Boolean
}