package com.vitiligo.gradewise.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.vitiligo.gradewise.data.GradeWiseRepository
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.Grade
import com.vitiligo.gradewise.model.Semester
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SemesterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gradeWiseRepository: GradeWiseRepository
): ViewModel() {
    private val route = savedStateHandle.toRoute<com.vitiligo.gradewise.ui.navigation.Semester>()
    private val semesterId: Int = checkNotNull(route.id)

    private val _uiState = MutableStateFlow(SemesterUiState(semesterName = checkNotNull(route.name)))
    val uiState: StateFlow<SemesterUiState> = _uiState.asStateFlow()

    init {
        loadSemester(semesterId)
    }

    private fun loadSemester(semesterId: Int) {
        viewModelScope.launch {
            gradeWiseRepository.getSemesterDetails(semesterId).collect { semesterWithCourses ->
                _uiState.update { it.copy(sgpa = semesterWithCourses.semester.sgpa, courses = semesterWithCourses.courses) }
            }
        }
    }

    fun updateSemesterName(updatedName: String) {
        _uiState.update { it.copy(semesterName = updatedName) }
        viewModelScope.launch {
            gradeWiseRepository.updateSemester(Semester(semesterId, updatedName))
        }
    }

    fun addCourseForSemester() {
        val courseToAdd = Course(
            semesterId = semesterId,
            name = "Course ${_uiState.value.courses.size + 1}",
            creditHours = 3,
            grade = Grade.fromOrder(Grade.NUM_GRADES)
        )
        viewModelScope.launch {
            gradeWiseRepository.addCourseForSemester(semesterId, courseToAdd)
        }
    }

    fun updateCourseForSemester(updatedCourse: Course) {
        _uiState.update { state ->
            val updatedCourses = state.courses.map {
                if (it.id == updatedCourse.id) updatedCourse else it
            }
            state.copy(courses = updatedCourses)
        }
        viewModelScope.launch {
            gradeWiseRepository.updateCourseForSemester(semesterId, updatedCourse)
        }
    }

    fun deleteCourseFromSemester(courseToDelete: Course) {
        _uiState.update { state ->
            val updatedCourses = state.courses.filter { it.id != courseToDelete.id }
            state.copy(courses = updatedCourses)
        }
        viewModelScope.launch {
            gradeWiseRepository.deleteCourseFromSemester(semesterId, courseToDelete)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class SemesterUiState(
    val semesterName: String,
    val sgpa: Double = 0.0,
    var courses: List<Course> = listOf()
)