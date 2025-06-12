package com.vitiligo.gradewise.data

import android.util.Log
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.Semester
import com.vitiligo.gradewise.model.SemesterInfo
import com.vitiligo.gradewise.ui.utils.CourseCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

interface GradeWiseRepository {
    fun getAllSemesters(): Flow<List<SemesterInfo>>
    suspend fun setLastCourseIdInCache()
    fun getCoursesForSemester(semesterId: Int): Flow<List<Course>>
    suspend fun updateSemester(semester: Semester)
    suspend fun addCourseForSemester(semesterId: Int, course: Course)
    suspend fun updateCourseForSemester(semesterId: Int, course: Course)
    suspend fun deleteCourseFromSemester(semesterId: Int, course: Course)
}

@Singleton
class GradeWiseRepositoryImpl @Inject constructor(
    private val semesterDao: SemesterDao,
    private val courseDao: CourseDao
): GradeWiseRepository {
    private val courseCacheManager = CourseCacheManager()

    override suspend fun setLastCourseIdInCache() {
        val id = courseDao.getLastCourseId()
        courseCacheManager.setLastCourseId(id)
    }

    override fun getAllSemesters(): Flow<List<SemesterInfo>> {
        return semesterDao.getAllSemesters()
    }

    override fun getCoursesForSemester(semesterId: Int): Flow<List<Course>> {
        val cachedFlow = courseCacheManager.getCourseCacheForSemester(semesterId)
        return if (cachedFlow != null) {
            Log.d("HomeViewModel", "Returning live cache for semester $semesterId")
            cachedFlow.asStateFlow()
        } else {
            Log.d("HomeViewModel", "No cache, loading from DB")
            courseDao.getSemesterCourses(semesterId)
                .onEach { courses ->
                    courseCacheManager.setCourseCacheForSemester(semesterId, courses)
                }
        }
    }

    override suspend fun updateSemester(semester: Semester) {
        semesterDao.updateSemester(semester)
    }

    override suspend fun addCourseForSemester(semesterId: Int, course: Course) {
        courseCacheManager.addCourseForSemester(semesterId, course)
        courseDao.addCourse(course)
    }

    override suspend fun updateCourseForSemester(semesterId: Int, course: Course) {
        courseCacheManager.updateCourseForSemester(semesterId, course)
        courseDao.updateCourse(course)
    }

    override suspend fun deleteCourseFromSemester(semesterId: Int, course: Course) {
        courseCacheManager.deleteCourseFromSemester(semesterId, course)
        courseDao.deleteCourse(course)
    }
}