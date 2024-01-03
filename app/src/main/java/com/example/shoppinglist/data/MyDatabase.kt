package com.example.shoppinglist.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.shoppinglist.data.entities.*

private const val DB_NAME = "shoppinglist.db"
private const val DB_PATH = "databases/shoppinglist.db"

@Database(
    entities = [
        WaitingList::class, CateList::class ], version = 4, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun waitingList(): WaitingListDao
    abstract fun cateList(): CateListDao

    companion object {
        // Singleton prevents multiple instances of database opening at the same time
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // upgrade database version from 3 to 4
            val MIGRATION_3_4 = object : Migration(3, 4) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE CateList ADD type TEXT NOT NULL DEFAULT ''")
                }
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DB_NAME
                ).createFromAsset(DB_PATH).allowMainThreadQueries().addMigrations(MIGRATION_3_4)
                    .build()
                //.createFromAsset(DB_PATH)
                //.addMigrations(MIGRATION_3_4)

                INSTANCE = instance
                return instance
            }
        }
    }
}
