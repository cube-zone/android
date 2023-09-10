package dev.android.cubestudio.databases.solves

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import dev.android.cubestudio.databases.solves.Solve
import kotlinx.coroutines.flow.Flow

@Dao
interface SolveDao {
    @Upsert
    suspend fun upsertSolve(solve: Solve)

    @Delete
    suspend fun deleteSolve(solve: Solve)

    @Update
    suspend fun updateSolve(solve: Solve)

    @Query("SELECT * FROM solve ORDER BY createdAt DESC")
    fun getAllSolves(): Flow<List<Solve>>

    @Query("SELECT * FROM solve WHERE sessionId = :sessionId ORDER BY createdAt DESC")
    fun getSolvesFromSession(sessionId: Int): Flow<List<Solve>>
}