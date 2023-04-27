package com.shoebill.maru.ui.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandmarkPictureList
import com.shoebill.maru.ui.component.bottomsheet.landmark.LandmarkSheet

@Composable
fun BottomSheetPage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .heightIn(min = 40.dp, max = 670.dp)
    ) {
//        BottomSheetIndicator()
//        SpotList()
        LandmarkSheet {
            LandmarkPictureList()
        }
    }
}