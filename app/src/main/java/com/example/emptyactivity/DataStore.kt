package com.example.emptyactivity

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


const val PROFILE_DATASTORE ="profile_datastore"
private val Context.dataStore: DataStore<Preferences> by
preferencesDataStore(name = PROFILE_DATASTORE)

class ProfileRepositoryDataStore (private val context: Context) :
    ProfileRepository {
    companion object {
        val NAME = stringPreferencesKey("NAME")
        val COUNTER = intPreferencesKey("COUNTER")
    }

    /** Update the values in the DataStore. */
    override suspend fun saveProfile(profileData: ProfileData) {
        context.dataStore.edit {
            it[NAME] = profileData.name
            it[COUNTER] = profileData.counter
        }
    }

    /** Get the data in the DataStore as a flow. Since the store may have never
     * been used yet, handle the null case with default values. */
    override fun getProfile(): Flow<ProfileData> = context.dataStore.data.map {
        ProfileData(
            name = it[NAME] ?: "",
            counter = it[COUNTER] ?: 0
        )
    }
    override suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }
}



/** Simple view model that keeps track of a single value (count in this case) */
class MyViewModelSimpleSaved(private val profileRepository: ProfileRepository) :
    ViewModel() {

    // private UI state (MutableStateFlow)
    private val _uiState = MutableStateFlow(ProfileData( "", 0))

    // public getter for the state (StateFlow)
    val uiState: StateFlow<ProfileData> = _uiState.asStateFlow()

    /* Method called when ViewModel is first created */
    init {
        // Start collecting the data from the data store when the ViewModel is created.
        viewModelScope.launch {
            profileRepository.getProfile().collect { profileData ->
                _uiState.value = profileData
            }
        }
    }

    fun setName(newName: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(name = newName) }
            profileRepository.saveProfile (_uiState.value)
        }
    }

    /* Increments the value of the counter stored in the state flow */
    fun increment() {
        viewModelScope.launch {
            val count = _uiState.value.counter;
            _uiState.update { currentState ->
                currentState.copy(counter = count + 1)
            }
            profileRepository.saveProfile (_uiState.value)
        }
    }
}