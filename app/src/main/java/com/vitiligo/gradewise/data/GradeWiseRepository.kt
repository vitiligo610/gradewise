package com.vitiligo.gradewise.data

import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.Semester
import com.vitiligo.gradewise.model.SemesterInfo
import com.vitiligo.gradewise.model.SemesterWithCourses
import com.vitiligo.gradewise.ui.utils.GradeWiseCacheManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

interface GradeWiseRepository {
    fun getAllSemesters(): Flow<List<SemesterInfo>>
    fun getSemesterDetails(semesterId: String): Flow<SemesterWithCourses>
    suspend fun addSemester(semester: Semester)
    suspend fun updateSemester(semester: Semester)
    suspend fun updateSemesterName(semesterId: String, name: String)
    suspend fun deleteSemester(semester: Semester)
    suspend fun addCourseForSemester(semesterId: String, course: Course)
    suspend fun updateCourseForSemester(semesterId: String, course: Course)
    suspend fun deleteCourseFromSemester(semesterId: String, course: Course)
}

@Singleton
class GradeWiseRepositoryImpl @Inject constructor(
    private val semesterDao: SemesterDao,
    private val courseDao: CourseDao
): GradeWiseRepository {
    private val cacheManager = GradeWiseCacheManager()

    override fun getAllSemesters(): Flow<List<SemesterInfo>> {
        return semesterDao.getAllSemesters()
    }

    override fun getSemesterDetails(semesterId: String): Flow<SemesterWithCourses> {
        val cachedFlow = cacheManager.getCacheForSemester(semesterId)
        return cachedFlow?.asStateFlow()
            ?: courseDao.getSemesterWithCourses(semesterId)
                .onEach {
                    cacheManager.setCacheForSemester(semesterId, it)
                }
    }

    override suspend fun addSemester(semester: Semester) {
        val withTimestamp = semester.copy(createdAt = System.currentTimeMillis())
        cacheManager.setCacheForSemester(semester.id, SemesterWithCourses(semester = withTimestamp, courses = emptyList()))
        semesterDao.addSemester(withTimestamp)
    }

    override suspend fun updateSemester(semester: Semester) {
        semesterDao.updateSemester(semester)
    }

    override suspend fun updateSemesterName(semesterId: String, name: String) {
        cacheManager.updateSemesterName(semesterId, name)
        semesterDao.updateSemesterName(semesterId, name)
    }

    override suspend fun deleteSemester(semester: Semester) {
        cacheManager.removeCacheForSemester(semester.id)
        semesterDao.deleteSemester(semester)
    }

    override suspend fun addCourseForSemester(semesterId: String, course: Course) {
        val semester = cacheManager.addCourseForSemester(semesterId, course)
        courseDao.addCourse(course)
        semesterDao.updateSemester(semester.copy())
    }

    override suspend fun updateCourseForSemester(semesterId: String, course: Course) {
        val semester = cacheManager.updateCourseForSemester(semesterId, course)
        courseDao.updateCourse(course)
        semesterDao.updateSemester(semester.copy())
    }

    override suspend fun deleteCourseFromSemester(semesterId: String, course: Course) {
        val semester = cacheManager.deleteCourseFromSemester(semesterId, course)
        courseDao.deleteCourse(course)
        semesterDao.updateSemester(semester.copy())
    }
}