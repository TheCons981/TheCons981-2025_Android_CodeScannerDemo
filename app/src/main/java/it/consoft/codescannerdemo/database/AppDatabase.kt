package it.consoft.codescannerdemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.consoft.codescannerdemo.database.dao.CodeDao
import it.consoft.codescannerdemo.database.entities.CodeEntity

@Database(entities = [CodeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun codeDao(): CodeDao?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //private const val NUMBER_OF_THREADS = 1
        //val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        /*private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE code_entity ADD COLUMN imageUrl TEXT")
            }
        }*/

        fun getInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class.java) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "dbo_code_repository"
                    )
                        //.addMigrations(MIGRATION_1_2)
                        .allowMainThreadQueries()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }

        }
    }
}