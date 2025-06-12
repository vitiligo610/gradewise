package com.vitiligo.gradewise.model

import androidx.room.Embedded
import androidx.room.Relation

data class SemesterWithCourses(
    @Embedded
    val semester: Semester,
    @Relation(
        parentColumn = "id",
        entityColumn = "semester_id"
    )
    val courses: List<Course>
)