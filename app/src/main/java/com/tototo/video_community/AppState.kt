package com.tototo.video_community

data class AppState(
    // 登录状态、用户信息、主题设置等全局性状态统一管理
    val isLogin: Boolean = false
)
// 涉及到大型的项目会更加丰富
//data class AppState(
//    // 1. 核心登录态
//    val isLogin: Boolean = false,
//
//    // 2. 用户详细信息 (登录后才有效)
//    val userId: Long? = null,
//    val userName: String? = null,
//    val userFaceUrl: String? = null,
//    val vipStatus: Int = 0, // 0:非会员，1:月度，2:年度...
//
//    // 3. 应用级配置
//    val isDarkMode: Boolean = false, // 是否强制深色
//    val hasNewNotification: Boolean = false, // 是否有新消息红点
//
//    // 4. 加载/错误状态
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null
//)