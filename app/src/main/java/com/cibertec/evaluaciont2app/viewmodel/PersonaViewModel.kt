package com.cibertec.evaluaciont2app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.evaluaciont2app.data.model.Persona
import com.cibertec.evaluaciont2app.repository.PersonaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PersonaViewModel (application: Application) : AndroidViewModel(application){

    private val personaRepository = PersonaRepository(application)

    private val _personas = MutableStateFlow<List<Persona>>(emptyList())
    val personas: StateFlow<List<Persona>> = _personas.asStateFlow()

    private val _currentQuery = MutableStateFlow("")
    val currentQuery: StateFlow<String> = _currentQuery.asStateFlow()

    private val _personaCount = MutableStateFlow(0)
    val personaCount: StateFlow<Int> = _personaCount.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        viewModelScope.launch {
            _currentQuery.collectLatest { query ->
                personaRepository.findPersonas(query).collectLatest { personaList ->
                    _personas.value = personaList
                }
            }
        }

        viewModelScope.launch {
            personaRepository.getAllPersonas()
                .map { it.size }
                .collectLatest { count ->
                    _personaCount.value = count
                    if (_currentQuery.value.isEmpty()) {
                        _snackbarMessage.value = "Total de $count registros."
                    }
                }
        }
    }

    fun insertPersona(persona: Persona) {
        viewModelScope.launch {
            personaRepository.insertPersona(persona)
            _snackbarMessage.value = "Persona registrada con éxito."
        }
    }

    fun updatePersona(persona: Persona) {
        viewModelScope.launch {
            personaRepository.updatePersona(persona)
            _snackbarMessage.value = "Persona actualizada con éxito."
        }
    }

    fun deletePersona(persona: Persona) {
        viewModelScope.launch {
            personaRepository.deletePersona(persona)
            _snackbarMessage.value = "Persona eliminada correctamente."
        }
    }

    suspend fun getPersonaById(id: Int): Persona? {
        return personaRepository.getPersonaById(id)
    }

    fun updateSearchQuery(newQuery: String) {
        _currentQuery.value = newQuery
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

}