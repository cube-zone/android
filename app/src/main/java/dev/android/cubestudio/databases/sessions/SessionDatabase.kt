package dev.android.cubestudio.databases.sessions

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Session::class], version = 1)
abstract class SessionDatabase:RoomDatabase() {
    abstract val dao: SessionDao
}