package com.vitiligo.gradewise.ui.utils

import com.vitiligo.gradewise.model.Course

object GPACalculator {
    fun calculateSGPA(courses: List<Course>): Double {
        var sum = 0.0
        var creditHours = 0

        courses.forEach {
            sum += (it.creditHours * it.grade.points)
            creditHours += it.creditHours
        }

        return sum / creditHours
    }

    fun calculateCGPA(semestersCourses: List<List<Course>>): Double {
        var sgpaSum = 0.0
        var semesters = 0
        
        semestersCourses.forEach { courses ->
            val creditHoursSum = courses.sumOf { it.creditHours }
            if (creditHoursSum > 0) {
                sgpaSum += calculateSGPA(courses)
                semesters++
            }
        }
        
        return sgpaSum / semesters
    }
}