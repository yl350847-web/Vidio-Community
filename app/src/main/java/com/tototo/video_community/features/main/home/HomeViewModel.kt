package com.tototo.video_community.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tototo.video_community.data.repository.FakeHomeRepository
import com.tototo.video_community.data.repository.HomeItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class HomeUiState(
    val items: List<HomeItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repo: FakeHomeRepository
) : ViewModel() {
    private var initialLoaded = false
    private var _uiState = androidx.compose.runtime.mutableStateOf(HomeUiState())
    val uiState: androidx.compose.runtime.State<HomeUiState> get() = _uiState

    init {
        // 首次加载
        reload()
    }

    fun reload() {
        // 进入刷新状态，清空错误
        _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
        viewModelScope.launch {
            // 模拟网络耗时
            delay(1000)

            // 你可以把这里换成真实仓库调用
            val succeed = kotlin.random.Random.nextDouble() > 0.2 // 80% 成功
            if (succeed) {
                val newItems = repo.getHomeItems()
                _uiState.value = HomeUiState(items = newItems, isRefreshing = false, errorMessage = null)
                initialLoaded = true
            } else {
                _uiState.value = _uiState.value.copy(isRefreshing = false, errorMessage = "网络错误，请重试")
            }
        }
    }
}