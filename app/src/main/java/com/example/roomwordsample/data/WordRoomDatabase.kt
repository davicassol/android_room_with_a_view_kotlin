package com.example.roomwordsample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * A classe de banco de dados principal do Room para este aplicativo.
 */
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    // Callback para popular o banco de dados quando ele for criado
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Exclui todo o conteúdo
            wordDao.deleteAll()

            // Adiciona palavras de exemplo.
            // Usamos 'word =' para garantir que estamos preenchendo o campo certo,
            // já que o ID é autogerado.
            var word = Word(word = "Hello")
            wordDao.insert(word)

            word = Word(word = "World!")
            wordDao.insert(word)

            word = Word(word = "Room!")
            wordDao.insert(word)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope // Adicionamos o Scope como parâmetro
        ): WordRoomDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope)) // Adiciona o callback aqui
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}