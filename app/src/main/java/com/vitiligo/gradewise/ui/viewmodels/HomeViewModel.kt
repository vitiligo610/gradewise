package com.vitiligo.gradewise.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitiligo.gradewise.data.GradeWiseRepository
import com.vitiligo.gradewise.model.Semester
import com.vitiligo.gradewise.model.SemesterInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gradeWiseRepository: GradeWiseRepository
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = gradeWiseRepository.getAllSemesters()
        .map { semesters ->
            HomeUiState(
                semesters = semesters,
                cgpa = if (semesters.isNotEmpty()) semesters.map { it.sgpa }.average() else 0.0,
                loading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState(loading = true)
        )

    init {
        Log.d(TAG, "Launching init")
        viewModelScope.launch {
            preFetchSemestersData()
        }
    }

    private suspend fun preFetchSemestersData() {
        val semesters = uiState.map { it.semesters }.first { it.isNotEmpty() }

        coroutineScope {
            semesters.forEach {
                launch {
                    gradeWiseRepository.getSemesterDetails(it.id).first()
                }
            }
        }
    }

    fun addSemester() {
        val semesterToAdd = Semester(
            id = UUID.randomUUID().toString(),
            name = "Semester # ${uiState.value.semesters.size + 1}"
        )
        viewModelScope.launch {
            gradeWiseRepository.addSemester(semesterToAdd)
        }
    }

    fun deleteSemester(semester: Semester) {
        viewModelScope.launch {
            gradeWiseRepository.deleteSemester(semester)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        private const val TAG = "HomeViewModel"
    }
}

data class HomeUiState(
    val semesters: List<SemesterInfo> = listOf(),
    val cgpa: Double = 0.0,
    val loading: Boolean = false,
)