package cz.kudlav.vutindex.database

import android.content.Context
import androidx.room.*
import cz.kudlav.vutindex.database.daos.IndexDao
import cz.kudlav.vutindex.database.entities.SemesterEntity
import cz.kudlav.vutindex.database.entities.SubjectEntity


@Database(
    entities = [SubjectEntity::class, SemesterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun indexDao(): IndexDao

    companion object {
        private const val DATABASE_NAME = "VutIndexDB"

        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context, AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}