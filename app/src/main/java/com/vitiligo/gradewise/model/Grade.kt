package com.vitiligo.gradewise.model

import androidx.room.TypeConverter

sealed class Grade(val value: String, val points: Double, val order: Int) {

    data object F : Grade("F", 0.0, 1)
    data object D : Grade("D", 1.0, 2)
    data object D_PLUS : Grade("D+", 1.5, 3)
    data object C : Grade("C", 2.0, 4)
    data object C_PLUS : Grade("C+", 2.5, 5)
    data object B : Grade("B", 3.0, 6)
    data object B_PLUS : Grade("B+", 3.5, 7)
    data object A : Grade("A", 4.0, 8)

    companion object {
        val NUM_GRADES = 8

        fun fromString(gradeString: String): Grade {
            return when (gradeString) {
                A.value -> A
                B_PLUS.value -> B_PLUS
                B.value -> B
                C_PLUS.value -> C_PLUS
                C.value -> C
                D_PLUS.value -> D_PLUS
                D.value -> D
                F.value -> F
                else -> F
            }
        }

        fun fromOrder(orderValue: Int): Grade {
            return when (orderValue) {
                A.order -> A
                B_PLUS.order -> B_PLUS
                B.order -> B
                C_PLUS.order -> C_PLUS
                C.order -> C
                D_PLUS.order -> D_PLUS
                D.order -> D
                F.order -> F
                else -> F
            }
        }
    }
}


class GradeConverter {
    @TypeConverter
    fun fromGrade(grade: Grade?): String? {
        return grade?.value
    }

    @TypeConverter
    fun toGrade(gradeString: String?): Grade? {
        return gradeString?.let { Grade.fromString(it) }
    }
}

fun Grade.inc(): Grade {
    return if (this.order == Grade.NUM_GRADES) this else Grade.fromOrder(this.order + 1)
}

fun Grade.dec(): Grade {
    return if (this.order == 1) this else Grade.fromOrder(this.order - 1)
}