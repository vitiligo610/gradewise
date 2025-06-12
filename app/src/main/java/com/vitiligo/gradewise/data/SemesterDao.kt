package com.vitiligo.gradewise.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.vitiligo.gradewise.model.Semester
import com.vitiligo.gradewise.model.SemesterInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface SemesterDao {
    @Query("SELECT s.id AS id, s.name AS name, SUM(c.credit_hours) AS total_credit_hours, COUNT(c.id) AS num_courses, s.sgpa AS sgpa FROM semesters s LEFT JOIN courses c ON s.id = c.semester_id GROUP BY s.id ORDER BY s.created_at DESC")
    fun getAllSemesters(): Flow<List<SemesterInfo>>

    @Query("SELECT AVG(sgpa) FROM semesters")
    fun getCGPA(): Flow<Double>

    @Insert
    suspend fun addSemester(semester: Semester)

    @Update
    suspend fun updateSemester(semester: Semester)
}