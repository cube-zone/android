package dev.android.cubestudio.databases.sessions

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import dev.android.cubestudio.databases.sessions.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Upsert
    suspend fun upsertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Update
    suspend fun updateSession(session: Session)

    @Query("SELECT * FROM session ORDER BY createdAt DESC")
    fun getAllSessions(): Flow<List<Session>>

    //@Query("SELECT * FROM session WHERE sessionId = :sessionId ORDER BY createdAt DESC")
    //suspend fun getSessionsFromSession(sessionId: String): Flow<List<Session>>
}