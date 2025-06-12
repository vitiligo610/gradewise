package com.vitiligo.gradewise.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "semesters", indices = [Index(value = ["id"], unique = true)])
data class Semester(
    @PrimaryKey val id: String = "",
    val name: String,
    @ColumnInfo(defaultValue = "0.0")
    val sgpa: Double = 0.0,
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: String = ""
)

data class SemesterInfo(
    val id: String,
    val name: String,
    @ColumnInfo(name = "num_courses")
    val numCourses: Int,
    @ColumnInfo(name = "total_credit_hours")
    val totalCreditHours: Int,
    val sgpa: Double = 0.0,
)

fun SemesterInfo.toSemester(): Semester {
    return Semester(
        id = this.id,
        name = this.name,
        sgpa = this.sgpa,
    )
}