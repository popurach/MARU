package com.shoebill.maru.ui.component.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoebill.maru.R
import com.shoebill.maru.model.data.Member
import com.shoebill.maru.viewmodel.MemberViewModel

@Composable
fun MyPageMemberInfo(
    memberViewModel: MemberViewModel = hiltViewModel(),
) {
    val memberInfo = memberViewModel.memberInfo.observeAsState(initial = Member())

    AsyncImage(
        model = memberInfo.value.imageUrl,
        contentDescription = "Translated description of what the image contains",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 10.dp)
            .size(70.dp)
            .clip(CircleShape)
    )
    // nickname
    Text(text = memberInfo.value.nickname, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.point_icon),
            contentDescription = "point",
            tint = Color.Unspecified
        )
        Text(text = memberViewModel.getPoint(), modifier = Modifier
            .graphicsLayer(alpha = 0.99f)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF6039DF),
                                Color(0xFFA14AB7)
                            )
                        ),
                        blendMode = BlendMode.SrcAtop
                    )
                }
            },
        )
    }
}