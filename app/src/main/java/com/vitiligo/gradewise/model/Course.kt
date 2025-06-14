package com.vitiligo.gradewise.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "courses", foreignKeys = [
    ForeignKey(
        entity = Semester::class,
        parentColumns = ["id"],
        childColumns = ["semester_id"],
        onDelete = ForeignKey.CASCADE
    )
], indices = [Index(value = ["semester_id"])])
data class Course(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "semester_id") val semesterId: String,
    val name: String,
    @ColumnInfo(name = "credit_hours") val creditHours: Int,
    val grade: Grade
)