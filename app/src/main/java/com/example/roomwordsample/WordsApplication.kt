package com.example.roomwordsample

import android.app.Application
import com.example.roomwordsample.data.WordRepository
import com.example.roomwordsample.data.WordRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication : Application() {
    // Não precisamos cancelar este escopo, pois ele durará o tempo de vida do processo
    val applicationScope = CoroutineScope(SupervisorJob())

    // Usamos 'by lazy' para que o banco de dados e o repositório sejam criados apenas quando necessários
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}