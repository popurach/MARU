package com.shoebill.maru.ui.component.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun DrawerHeader(
    memberViewModel: MemberViewModel = hiltViewModel()
) {
    val iconSize = 18.dp
    val fontSize = 12.sp
    val memberInfo = memberViewModel.memberInfo.observeAsState(initial = Member())

    Box(
        Modifier
            .fillMaxWidth()
            .padding(end = 20.dp), contentAlignment = Alignment.TopEnd
    ) {
        Icon(
            painterResource(id = R.drawable.alert),
            "",
            Modifier.size(iconSize)
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .padding(top = 6.dp, bottom = 40.dp)

    ) {
        AsyncImage(
            model = memberInfo.value.imageUrl,
            contentDescription = "Translated description of what the image contains",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        // nickname
        Text(text = memberInfo.value.nickname, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.point_icon),
                contentDescription = "point",
                tint = Color.Unspecified
            )
            Text(text = "${memberInfo.value.point}", modifier = Modifier
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
                }
            )
        }
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.photo_album_icon),
                "photo_album",
                Modifier.size(iconSize)
            )
            Text(text = "사진첩", fontSize = fontSize)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.stamp_icon),
                "stamp",
                Modifier.size(iconSize)
            )
            Text(text = "스탬프", fontSize = fontSize)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.unscrap_icon),
                "scrap",
                Modifier.size(iconSize)
            )
            Text("스크랩", fontSize = fontSize)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painterResource(R.drawable.auction_icon),
                "auction",
                Modifier.size(iconSize)
            )
            Text("경매", fontSize = fontSize)
        }
    }
}