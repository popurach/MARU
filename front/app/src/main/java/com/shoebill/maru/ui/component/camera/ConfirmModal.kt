package com.shoebill.maru.ui.component.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shoebill.maru.ui.component.common.CustomAlertDialog
import com.shoebill.maru.ui.component.common.GradientButton
import com.shoebill.maru.ui.component.common.GradientColoredText
import com.shoebill.maru.ui.theme.MaruBrush

@Composable
fun ConfirmModal(
    bitmap: ImageBitmap,
    onDismissRequest: () -> Unit
) {

    CustomAlertDialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(bitmap, null, Modifier.height(180.dp), contentScale = ContentScale.Crop)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 25.dp)
                    .height(250.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 8.dp)
                        .padding(horizontal = 24.dp),
                    text = "멋진 사진이네요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    text = "해당 사진으로 랜드마크를 차지하기 위한 경매에 참여하는건 어떠세요?",
                    fontSize = 14.sp,
                    color = Color.LightGray
                )
                GradientButton(
                    text = "경매 참여하기",
                    gradient = MaruBrush,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {}
                )
                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Transparent)
                        .clickable { onDismissRequest() },
                ) {
                    GradientColoredText(
                        text = "괜찮아요",
                        modifier = Modifier
                            .align(Alignment.Center),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
