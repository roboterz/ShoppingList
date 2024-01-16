package com.aerolite.shoppinglist.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aerolite.shoppinglist.data.entities.Category

private const val DB_NAME = "shoppinglist.db"
private const val DB_PATH = "databases/shoppinglist.db"

@Database(
    entities = [Category::class ], version = 6, exportSchema = false)

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
            val MIGRATION_5_6 = object : Migration(5, 6) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE Category ADD countSub INTEGER NOT NULL DEFAULT 0")
                }
            }



            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    DB_NAME
                ).createFromAsset(DB_PATH).allowMainThreadQueries().build()
                //.createFromAsset(DB_PATH)
                //.addMigrations(MIGRATION_5_6)

                INSTANCE = instance
                return instance
            }
        }
    }
}
