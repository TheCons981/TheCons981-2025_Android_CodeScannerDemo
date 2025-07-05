package it.consoft.codescannerdemo.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import it.consoft.codescannerdemo.database.dao.CodeDao

class CodeRepositoryViewModel(private val codeDao: CodeDao) : ViewModel() {

    private val codeRepositoryPager = Pager(
        PagingConfig(pageSize = 30)
    ) {
        codeDao.getAllPaged()
    }

    var codeRepositoryItems = codeRepositoryPager.flow.cachedIn(viewModelScope)

}