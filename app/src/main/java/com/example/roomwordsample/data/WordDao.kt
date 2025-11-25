package com.example.roomwordsample.data // <-- Mude para o seu pacote correto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow // <-- Importação necessária

/**
 * O DAO (Data Access Object) para a entidade Word.
 */
@Dao
interface WordDao {

    /**
     * Retorna um *Flow* (fluxo) de dados. O Room cuidará de
     * rodar isso em background e atualizará automaticamente
     * a lista sempre que o banco de dados mudar.
     */
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): Flow<List<Word>> // <-- ESTA É A MUDANÇA

    /**
     * Insere uma nova palavra.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    /**
     * Deleta todas as palavras da tabela.
     */
    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}