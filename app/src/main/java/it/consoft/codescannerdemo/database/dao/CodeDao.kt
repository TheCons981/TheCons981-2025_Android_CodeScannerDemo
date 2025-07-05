package it.consoft.codescannerdemo.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import it.consoft.codescannerdemo.database.entities.CodeEntity


@Dao
interface CodeDao {

    @Insert
    suspend fun insert(code: CodeEntity)

    @Query("SELECT * FROM code_entity")
    suspend fun getAll(): List<CodeEntity>

    @Query("SELECT * FROM code_entity ORDER BY id DESC")
    fun getAllPaged(): PagingSource<Int, CodeEntity>

}