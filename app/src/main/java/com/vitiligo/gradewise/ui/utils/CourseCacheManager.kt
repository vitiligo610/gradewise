package com.vitiligo.gradewise.ui.utils

import android.util.Log
import com.vitiligo.gradewise.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.ConcurrentHashMap

class CourseCacheManager {
    private var lastCourseId: Int = 0
    private val courseCache = ConcurrentHashMap<Int, MutableStateFlow<List<Course>>>()

    fun getCourseCacheForSemester(semesterId: Int): MutableStateFlow<List<Course>>? {
        return courseCache[semesterId]
    }

    fun setCourseCacheForSemester(semesterId: Int, courses: List<Course>) {
        val stateFlow = courseCache.getOrPut(semesterId) {
            MutableStateFlow(courses)
        }
        stateFlow.value = courses
    }

    fun getLastCourseId(): Int = lastCourseId

    fun setLastCourseId(id: Int) {
        lastCourseId = id
    }

    fun addCourseForSemester(semesterId: Int, course: Course) {
        val courseToAdd = course.copy(id = ++lastCourseId)
        courseCache[semesterId]?.let { stateFlow ->
            val updatedList = stateFlow.value.toMutableList().apply {
                add(courseToAdd)
            }
            stateFlow.value = updatedList
        } ?: Log.d(TAG, "No cache found for semester id: $semesterId")
    }

    fun updateCourseForSemester(semesterId: Int, updatedCourse: Course) {
        courseCache[semesterId]?.let { stateFlow ->
            val updatedList = stateFlow.value.toMutableList().apply {
                val index = indexOfFirst { it.id == updatedCourse.id }
                if (index != -1) {
                    this[index] = updatedCourse
                }
            }
            stateFlow.value = updatedList
        } ?: Log.d(TAG, "No cache found for semester id: $semesterId")
    }

    fun deleteCourseFromSemester(semesterId: Int, courseToDelete: Course) {
        courseCache[semesterId]?.let { stateFlow ->
            val updatedList = stateFlow.value.filter { it.id != courseToDelete.id }
            stateFlow.value = updatedList
        } ?: Log.d(TAG, "No cache found for semester id: $semesterId")
    }

    companion object {
        private const val TAG = "CourseCacheManager"
    }
}