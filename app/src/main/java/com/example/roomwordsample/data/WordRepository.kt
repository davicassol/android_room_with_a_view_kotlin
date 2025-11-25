package com.example.roomwordsample.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Declara o DAO como uma propriedade privada no construtor.
 * Passamos apenas o DAO em vez do banco de dados inteiro,
 * pois só precisamos acessar os métodos de leitura/escrita do DAO.
 */
class WordRepository(private val wordDao: WordDao) {

    // O Room executa todas as consultas em uma thread separada.
    // O Flow observado notificará o observador quando os dados mudarem.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // Por padrão, o Room executa consultas 'suspend' fora da thread principal,
    // portanto, não precisamos implementar mais nada para garantir que não
    // estamos fazendo trabalhos longos de banco de dados na thread principal.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}