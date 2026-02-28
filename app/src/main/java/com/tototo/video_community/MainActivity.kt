package com.tototo.video_community

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tototo.video_community.nav.AppNav
import com.tototo.video_community.ui.theme.VideoCommunityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 让 App 的内容绘制到状态栏（显示时间的地方）和导航栏（底部返回键的地方）后面
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                // 包裹在这里面的所有组件，都会自动继承上面定义的风格
                // Jetpack Compose 的标准范式,所有的 UI 必须包裹在主题组件内
                AppNav()
            }
        }
    }
}