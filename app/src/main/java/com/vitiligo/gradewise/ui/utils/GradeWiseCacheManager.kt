package com.vitiligo.gradewise.ui.utils

import android.util.Log
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.Semester
import com.vitiligo.gradewise.model.SemesterWithCourses
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.ConcurrentHashMap

class GradeWiseCacheManager {
    private val cachedData = ConcurrentHashMap<String, MutableStateFlow<SemesterWithCourses>>()

    fun getCacheForSemester(semesterId: String): MutableStateFlow<SemesterWithCourses>? {
        return cachedData[semesterId]
    }

    fun setCacheForSemester(semesterId: String, semesterWithCourses: SemesterWithCourses) {
        val stateFlow = cachedData.getOrPut(semesterId) {
            MutableStateFlow(semesterWithCourses)
        }
        stateFlow.value = semesterWithCourses
    }

    fun addCourseForSemester(semesterId: String, courseToAdd: Course): Semester {
        var updatedSemester = Semester(name = "")
        cachedData[semesterId]?.let { stateFlow ->
            val updatedList = stateFlow.value.courses.toMutableList().apply {
                add(courseToAdd)
            }
            val updatedSGPA = GPACalculator.calculateSGPA(updatedList)
            updatedSemester = stateFlow.value.semester.copy(sgpa = updatedSGPA) 
            stateFlow.value = stateFlow.value.copy(
                courses = updatedList,
                semester = updatedSemester
            )
        } ?: Log.d(TAG, "No cache found for semester id: $semesterId")
        return updatedSemester
    }

    fun updateCourseForSemester(semesterId: String, updatedCourse: Course): Semester {
        var updatedSemester = Semester(name = "")
        cachedData[semesterId]?.let { stateFlow ->
            val updatedList = stateFlow.value.courses.toMutableList().apply {
                val index = indexOfFirst { it.id == updatedCourse.id }
                if (index != -1) {
                    this[index] = updatedCourse
                }
            }
            val updatedSGPA = GPACalculator.calculateSGPA(updatedList)
            updatedSemester = stateFlow.value.semester.copy(sgpa = updatedSGPA)
            stateFlow.value = stateFlow.value.copy(
                courses = updatedList,
                semester = updatedSemester
            )
        } ?: Log.d(TAG, "No cache found for semester id: $semesterId")
        return updatedSemester
    }

    fun deleteCourseFromSemester(semesterId: String, courseToDelete: Course): Semester {
        var updatedSemester = Semester(name = "")
        cachedData[semesterId]?.let { stateFlow ->
            val updatedList = stateFlow.value.courses.filter { it.id != courseToDelete.id }
            val updatedSGPA = GPACalculator.calculateSGPA(updatedList)
            updatedSemester = stateFlow.value.semester.copy(sgpa = updatedSGPA)
            stateFlow.value = stateFlow.value.copy(
                courses = updatedList,
                semester = updatedSemester
            )
        } ?: Log.d(TAG, "No cache found for semester id: $semesterId")
        return updatedSemester
    }

    companion object {
        private const val TAG = "CourseCacheManager"
    }
}