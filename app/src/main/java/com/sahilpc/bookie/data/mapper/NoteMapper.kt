package com.sahilpc.bookie.data.mapper

import com.sahilpc.bookie.data.local.entity.NoteEntity
import com.sahilpc.bookie.domain.model.Note

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        description = description,
        image = image
    )
}

fun Note.toNoteEntity(email: String): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        description = description,
        image = image,
        email = email
    )
}