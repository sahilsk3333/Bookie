package com.sahilpc.bookie.presentation.home_screen

import android.app.Application
import android.app.Dialog
import android.net.Uri
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sahilpc.bookie.R
import com.sahilpc.bookie.domain.model.Note
import com.sahilpc.bookie.domain.repository.AuthRepository
import com.sahilpc.bookie.domain.repository.NoteRepository
import com.sahilpc.bookie.presentation.signin_screen.SignInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
    private val app: Application
) : ViewModel() {

    private val _notesList = MutableStateFlow<List<Note>>(emptyList())
    val notesList = _notesList.asStateFlow()

    var imageUri: Uri? = null
    var isLoggedIn = MutableLiveData(true)


    init {
        getAllNotes()
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            val isNoteInserted = noteRepository.insertNote(note)

            if (!isNoteInserted) {
                Toast.makeText(app, "Something went wrong", Toast.LENGTH_SHORT).show()
                return@launch
            }
//            getAllNotes()
            Toast.makeText(app, "Note is Inserted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            noteRepository.getAllNotes().collectLatest {
                _notesList.value = it
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logOutUser()
            isLoggedIn.postValue(false)
        }
    }


}