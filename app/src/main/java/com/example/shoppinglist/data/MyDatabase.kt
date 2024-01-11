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
    entities = [Category::class ], version = 5, exportSchema = false)

abstract class MyDatabase : RoomDatabase() {

    abstract fun category(): CateDao

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
                    db.execSQL("ALTER TABLE CateList ADD parentID INTEGER NOT NULL DEFAULT 0")
                    db.execSQL("ALTER TABLE CateList ADD complete BOOLEAN NOT NULL DEFAULT false")
                    db.execSQL("ALTER TABLE CateList ADD note TEXT NOT NULL DEFAULT ''")
                }
            }



            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().addMigrations(MIGRATION_3_4)
                    .build()
                //.createFromAsset(DB_PATH)
                //.addMigrations(MIGRATION_3_4)

                INSTANCE = instance
                return instance
            }
        }
    }
}
