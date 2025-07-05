package it.consoft.codescannerdemo.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "code_entity")
data class CodeEntity(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val code: String,
    val format: String,
    val imageUrl: String?

)