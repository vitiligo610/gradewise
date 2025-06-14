package com.vitiligo.gradewise.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vitiligo.gradewise.model.Course
import com.vitiligo.gradewise.model.GradeConverter
import com.vitiligo.gradewise.model.Semester

@Database(entities = [Semester::class, Course::class], version = 4, exportSchema = false)
@TypeConverters(GradeConverter::class)
abstract class GradeWiseDatabase: RoomDatabase() {
    abstract fun semesterDao(): SemesterDao
    abstract fun courseDao(): CourseDao
}