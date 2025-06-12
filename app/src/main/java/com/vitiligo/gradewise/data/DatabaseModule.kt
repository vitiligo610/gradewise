package com.vitiligo.gradewise.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): GradeWiseDatabase {
        return Room.databaseBuilder(context, GradeWiseDatabase::class.java, "gradewise_db")
            .fallbackToDestructiveMigration(false)
//            .createFromAsset("database/gradewise.db")
            .build()
    }

    @Provides
    fun provideSemesterDao(database: GradeWiseDatabase): SemesterDao = database.semesterDao()

    @Provides
    fun provideCourseDao(database: GradeWiseDatabase): CourseDao = database.courseDao()

    @Provides
    @Singleton
    fun provideGradeWiseRepository(
        semesterDao: SemesterDao,
        courseDao: CourseDao
    ): GradeWiseRepository = GradeWiseRepositoryImpl(semesterDao, courseDao)
}