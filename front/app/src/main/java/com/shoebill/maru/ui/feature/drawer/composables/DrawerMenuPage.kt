package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shoebill.maru.ui.feature.drawer.DrawerBody
import com.shoebill.maru.ui.feature.drawer.DrawerHeader

@Composable
fun DrawerMenuPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 50.dp, end = 84.dp)
    ) {
        DrawerHeader()
        Divider(Modifier.padding(horizontal = 15.dp))
        DrawerBody()
    }
}