package com.vitiligo.gradewise.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.SemesterWithCourses
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Transaction
    @Query("SELECT * FROM semesters WHERE id = :semesterId")
    fun getSemesterWithCourses(semesterId: String): Flow<SemesterWithCourses>

    @Query("SELECT * FROM courses WHERE semester_id = :semesterId")
    fun getSemesterCourses(semesterId: String): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)
}