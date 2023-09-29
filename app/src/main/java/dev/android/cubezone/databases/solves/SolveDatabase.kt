package dev.android.cubezone.databases.solves

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Solve::class], version = 1)
abstract class SolveDatabase:RoomDatabase() {
    abstract val dao: SolveDao
}