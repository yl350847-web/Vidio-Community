package com.tototo.video_community.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tototo.video_community.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

// 全局状态管理中心
// 大部分情况放在这里，多模块设计有更优解
class SharedViewModel : ViewModel() {
    // 内部可变数据源
    // MutableStateFlow: 这是一个“热流”。它像一个水库，里面永远存着最新的水（状态）
    private val appStateFlow = MutableStateFlow(AppState())
    // StateFlow: 这是对外暴露的接口。UI 只能“观察”它，不能直接修改它。这保证了数据安全性
    val appState: StateFlow<AppState> = appStateFlow.stateIn(
        viewModelScope,
        // 当有 UI（如 Activity）在观察这个流时，它才开始工作；当所有 UI 都销毁了（比如 App 切到后台或关闭），它会再等 5000 毫秒，如果还没人观察，就停止上游数据流以节省资源
        SharingStarted.WhileSubscribed(5000),
        appStateFlow.value
    )
    // update 原子操作,即使有多个线程同时尝试更新状态，它也能保证数据安全，不会发生覆盖冲突
    fun updateAppState(newState: AppState) {
        appStateFlow.update { newState }
    }
}