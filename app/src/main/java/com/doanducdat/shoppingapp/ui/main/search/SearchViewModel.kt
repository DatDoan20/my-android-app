package com.doanducdat.shoppingapp.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.doanducdat.shoppingapp.repository.ProductRepository
import com.doanducdat.shoppingapp.room.entity.KeyWordCacheEntity
import com.doanducdat.shoppingapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : BaseViewModel() {
    fun getAllKeyWord(): LiveData<List<KeyWordCacheEntity>> {
        return productRepository.getAllKeyWord().asLiveData()
    }

    fun insertKeyWord(keyWord: KeyWordCacheEntity) =
        viewModelScope.launch(scopeIO + NonCancellable) {
            productRepository.insertKeyWord(keyWord)
        }
}