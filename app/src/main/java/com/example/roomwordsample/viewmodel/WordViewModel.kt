package com.example.roomwordsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.roomwordsample.data.Word
import com.example.roomwordsample.data.WordRepository
import kotlinx.coroutines.launch

/**
 * O ViewModel atua como um centro de comunicação entre o Repositório (dados) e a UI.
 * Ele também é responsável por manter os dados "vivos" durante rotações de tela.
 */
class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Usar LiveData e armazenar em cache o que allWords retorna tem vários benefícios:
    // - Podemos colocar um observador nos dados (em vez de pesquisar por alterações)
    //   e atualizar a UI apenas quando os dados realmente mudarem.
    // - O Repositório é completamente separado da UI através do ViewModel.
    // .asLiveData() converte o Flow do repositório para LiveData, que é compatível com o ciclo de vida.
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    /**
     * Lança uma nova corrotina para inserir os dados de forma não bloqueante.
     * Usamos viewModelScope para garantir que a corrotina seja cancelada
     * se o ViewModel for destruído.
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

/**
 * Como nosso WordViewModel tem um argumento no construtor (o repository),
 * precisamos criar uma Factory (fábrica) para dizer ao Android como instanciá-lo.
 */
class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class") // Erro se a classe ViewModel for desconhecida
    }
}