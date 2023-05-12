package com.shoebill.maru.ui.page

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.auction.BiddingConfirmModal
import com.shoebill.maru.ui.component.auction.DeleteConfirmModal
import com.shoebill.maru.ui.component.common.GradientButton
import com.shoebill.maru.ui.theme.Pretendard
import com.shoebill.maru.viewmodel.AuctionViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel
import java.text.DecimalFormat

@Composable
fun AuctionPage(
    id: Long,
    auctionViewModel: AuctionViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    val context = LocalContext.current

    val auctionInfo = auctionViewModel.auctionInfo.observeAsState()
    val auctionHistory = auctionViewModel.auctionHistory.observeAsState(arrayOf(1000))
    val currentHighestBid = auctionViewModel.currentHighestBid.observeAsState()

    val chartEntryModel = entryModelOf(*(auctionHistory.value))
    val gradient = Brush.horizontalGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
    val bid = auctionViewModel.bid.observeAsState()
    val dec = DecimalFormat("#,###")
    val isDeleteModalOpen = remember { mutableStateOf(false) }
    val isBiddingModalOpen = remember { mutableStateOf(false) }

    val currentHighestBidAnimation by animateIntAsState(
        targetValue = currentHighestBid.value ?: 0,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )
    val bidAnimation by animateIntAsState(
        targetValue = bid.value ?: 0,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutLinearInEasing
        )
    )

    DisposableEffect(Unit) {
        auctionViewModel.initLandmarkId(id, context)
        onDispose {
            auctionViewModel.viewModelOnCleared()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp)
                    .size(30.dp)
                    .clickable {
                        auctionViewModel.exit()
                        navigateViewModel.navigator?.navigateUp()
                    },
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "뒤로가기",
            )
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                text = "경매장"
            )
        }
        auctionInfo.value?.let {
            Text(
                modifier = Modifier
                    .padding(top = 30.dp)
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
                text = it.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            modifier = Modifier.padding(top = 25.dp),
            fontSize = 12.sp,
            color = Color(0xFFA1A1A1),
            text = "현재 최고 입찰가"
        )
        Text(
            text = "$ ${dec.format(currentHighestBidAnimation)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Chart(
            modifier = Modifier.padding(start = 50.dp, end = 50.dp, top = 10.dp),
            chart = lineChart(
                lines = listOf(
                    lineSpec(
                        lineColor = Color(0xFF6039DF),
                        lineThickness = 4.dp,
                        lineBackgroundShader = verticalGradient(
                            arrayOf(
                                Color(0xFF6039DF).copy(0.5f),
                                Color(0xFFA14AB7).copy(alpha = 0f)
                            ),
                        ),
                    ),
                ),
                axisValuesOverrider = AxisValuesOverrider.fixed(
                    maxX = auctionHistory.value.size.minus(
                        2
                    ).toFloat(),
                    minY = 7000f
                ),
                pointPosition = LineChart.PointPosition.Start,
            ),
            model = chartEntryModel,
            marker = rememberMarker(),
        )
        Text(
            modifier = Modifier.padding(top = 25.dp),
            fontSize = 12.sp,
            color = Color(0xFFA1A1A1),
            text = "내 입찰가"
        )
        Text(
            modifier = Modifier.padding(bottom = 15.dp),
            text = "$ ${dec.format(bidAnimation)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                onClick = { auctionViewModel.decreaseBid() },
                enabled = auctionViewModel.downPrice != currentHighestBid.value,
                modifier = Modifier
                    .shadow(
                        elevation = if (auctionViewModel.downPrice != currentHighestBid.value) 10.dp else 0.1.dp,
                        shape = RoundedCornerShape(28.dp),
                    )
                    .size(140.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF6F5FF)),
            ) {
                Column() {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 7.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (auctionViewModel.downPrice != currentHighestBid.value) Color(
                                    0xFFE8E6FE
                                ) else Color(
                                    0xFFE9E9E9
                                )
                            )
                            .size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.down_arrow),
                            contentDescription = "입찰 가격 내리기",
                        )
                    }
                    Text(
                        text = dec.format(auctionViewModel.unit.value),
                        color = if (auctionViewModel.downPrice != currentHighestBid.value) Color(
                            0xFF424242
                        ) else Color(
                            0xFF949494
                        ),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = Pretendard,
                        style = TextStyle(letterSpacing = (-0.2).sp),
                    )
                    Text(
                        modifier = if (auctionViewModel.downPrice != currentHighestBid.value) Modifier
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
                            } else Modifier,
                        text = "$ ${dec.format(auctionViewModel.downPrice)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        style = TextStyle(letterSpacing = (-0.2).sp),
                    )
                }
            }
            Button(
                onClick = { auctionViewModel.increaseBid() },
                modifier = Modifier
                    .shadow(
                        elevation = 10.dp,
                        shape = RoundedCornerShape(28.dp),
                    )
                    .size(140.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF6F5FF)),
            ) {
                Column() {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 7.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color(0xFFE8E6FE))
                            .size(38.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.up_arrow),
                            contentDescription = "입찰 가격 올리기",
                        )
                    }
                    Text(
                        text = dec.format(auctionViewModel.unit.value),
                        color = Color(0xFF424242),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = Pretendard,
                        style = TextStyle(letterSpacing = (-0.2).sp),
                    )
                    Text(
                        modifier = Modifier
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
                        text = "$ ${dec.format(auctionViewModel.upPrice)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Pretendard,
                        style = TextStyle(letterSpacing = (-0.2).sp),
                    )
                }
            }
        }
        Row(
            modifier = Modifier.padding(top = 25.dp, start = 25.dp, end = 25.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
                enabled = auctionInfo.value?.myBidding != null,
                onClick = { isDeleteModalOpen.value = true },
                colors = ButtonDefaults.buttonColors(Color(0xFFD3D3D3)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "입찰 포기하기",
                    color = Color.White,
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(letterSpacing = (-0.2).sp),
                    fontSize = 15.sp
                )
            }
            GradientButton(
                text = "입찰 하기",
                gradient = gradient,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                onClick = { isBiddingModalOpen.value = true }
            )
        }
        if (isDeleteModalOpen.value) {
            DeleteConfirmModal(auctionInfo.value!!.myBidding.id) {
                isDeleteModalOpen.value = false
            }
        }
        if (isBiddingModalOpen.value) {
            BiddingConfirmModal() {
                isBiddingModalOpen.value = false
            }
        }
    }
}