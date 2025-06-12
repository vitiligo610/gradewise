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
    suspend fun setLastCourseIdInCache()
    fun getSemesterDetails(semesterId: Int): Flow<SemesterWithCourses>
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
    private val cacheManager = GradeWiseCacheManager()

    override suspend fun setLastCourseIdInCache() {
        val id = courseDao.getLastCourseId()
        cacheManager.setLastCourseId(id)
    }

    override fun getAllSemesters(): Flow<List<SemesterInfo>> {
        return semesterDao.getAllSemesters()
    }

    override fun getSemesterDetails(semesterId: Int): Flow<SemesterWithCourses> {
        val cachedFlow = cacheManager.getCacheForSemester(semesterId)
        return cachedFlow?.asStateFlow()
            ?: courseDao.getSemesterWithCourses(semesterId)
                .onEach {
                    cacheManager.setCacheForSemester(semesterId, it)
                }
    }

    override suspend fun updateSemester(semester: Semester) {
        semesterDao.updateSemester(semester)
    }

    override suspend fun addCourseForSemester(semesterId: Int, course: Course) {
        val semester = cacheManager.addCourseForSemester(semesterId, course)
        courseDao.addCourse(course)
        semesterDao.updateSemester(semester.copy())

    }

    override suspend fun updateCourseForSemester(semesterId: Int, course: Course) {
        val semester = cacheManager.updateCourseForSemester(semesterId, course)
        courseDao.updateCourse(course)
        semesterDao.updateSemester(semester.copy())
    }

    override suspend fun deleteCourseFromSemester(semesterId: Int, course: Course) {
        val semester = cacheManager.deleteCourseFromSemester(semesterId, course)
        courseDao.deleteCourse(course)
        semesterDao.updateSemester(semester.copy())
    }
}