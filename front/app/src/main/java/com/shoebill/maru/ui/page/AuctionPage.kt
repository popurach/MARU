package com.shoebill.maru.ui.page

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.auction.BiddingConfirmModal
import com.shoebill.maru.ui.component.auction.DeleteConfirmModal
import com.shoebill.maru.ui.component.common.GradientButton
import com.shoebill.maru.ui.theme.Pretendard
import com.shoebill.maru.viewmodel.AuctionViewModel
import java.text.DecimalFormat

@Composable
fun AuctionPage(
    auctionViewModel: AuctionViewModel = viewModel(),
) {
    val auctionInfo = auctionViewModel.auctionInfo.observeAsState(arrayOf(1, 1, 1, 1, 1))
    val chartEntryModel = entryModelOf(*(auctionInfo.value))
    val gradient = Brush.horizontalGradient(listOf(Color(0xFF6039DF), Color(0xFFA14AB7)))
    val bid = auctionViewModel.bid.observeAsState()
    val dec = DecimalFormat("#,###")
    val isDeleteModalOpen = remember { mutableStateOf(false) }
    val isBiddingModalOpen = remember { mutableStateOf(false) }

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
                    .size(30.dp),
                painter = painterResource(id = R.drawable.ic_arrow_back_24),
                contentDescription = "뒤로가기"
            )
            Text(
                modifier = Modifier.align(Alignment.TopCenter),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                text = "경매장"
            )
        }
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
            text = "서울현대박물관",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(top = 25.dp),
            fontSize = 12.sp,
            color = Color(0xFFA1A1A1),
            text = "현재 최고 입찰가"
        )
        Text(
            text = "$ 22,000",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Chart(
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
                axisValuesOverrider = AxisValuesOverrider.fixed(),
            ),
            model = chartEntryModel,
        )
        Text(
            modifier = Modifier.padding(top = 25.dp),
            fontSize = 12.sp,
            color = Color(0xFFA1A1A1),
            text = "내 입찰가"
        )
        Text(
            modifier = Modifier.padding(bottom = 15.dp),
            text = "$ ${dec.format(bid.value)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                onClick = { auctionViewModel.decreaseBid() },
                enabled = auctionViewModel.downPrice != 22000,
                modifier = Modifier
                    .shadow(
                        elevation = if (auctionViewModel.downPrice != 22000) 10.dp else 0.1.dp,
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
                                if (auctionViewModel.downPrice != 22000) Color(0xFFE8E6FE) else Color(
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
                        text = dec.format(auctionViewModel.unit),
                        color = if (auctionViewModel.downPrice != 22000) Color(0xFF424242) else Color(
                            0xFF949494
                        ),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        fontFamily = Pretendard,
                        style = TextStyle(letterSpacing = (-0.2).sp),
                    )
                    Text(
                        modifier = if (auctionViewModel.downPrice != 22000) Modifier
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
                        text = dec.format(auctionViewModel.unit),
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
            DeleteConfirmModal() {
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