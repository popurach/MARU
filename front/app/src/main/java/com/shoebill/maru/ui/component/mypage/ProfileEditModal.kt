package com.shoebill.maru.ui.component.mypage

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.shoebill.maru.R
import com.shoebill.maru.ui.component.common.CustomAlertDialog
import com.shoebill.maru.ui.component.common.GradientButton
import com.shoebill.maru.ui.theme.MaruBackground
import com.shoebill.maru.ui.theme.MaruBrush
import com.shoebill.maru.viewmodel.MemberViewModel
import com.shoebill.maru.viewmodel.NavigateViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ProfileEditModal(
    onDismissRequest: () -> Unit,
    memberViewModel: MemberViewModel = hiltViewModel(),
    navigateViewModel: NavigateViewModel = viewModel(),
) {
    val memberInfo = memberViewModel.memberInfo
    val context = LocalContext.current

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val takePhotoFromAlbumLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
            onResult = { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    selectedImageUri.value = result.data?.data
                    memberViewModel.modifiedImageUri.value = result.data?.data
                } else if (result.resultCode != Activity.RESULT_CANCELED) {
                    Toast.makeText(context, "사진 불러오기 중 에러 발생", Toast.LENGTH_SHORT).show()
                }
            }
        )



    CustomAlertDialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth(fraction = .9f)
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column {
                Text(
                    text = "프로필 선택",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = "Maru 프로필은 랜드마크 점유, 경매 등의 기능을 사용할 때 다른 사용자들에게 표시됩니다.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 3.dp, bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            takePhotoFromAlbumLauncher.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
                                type = "image/*"
                            })
                        }
                ) {

                    if (selectedImageUri.value != null) {
                        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            ImageDecoder.decodeBitmap(
                                ImageDecoder.createSource(
                                    context.contentResolver,
                                    selectedImageUri.value!!
                                )
                            )
                        } else {
                            MediaStore.Images.Media.getBitmap(
                                context.contentResolver,
                                selectedImageUri.value
                            )
                        }
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription =
                            "Translated description of what the image contains",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clip(CircleShape)
                        )
                    } else {
                        AsyncImage(
                            model = memberInfo.value?.imageUrl,
                            contentDescription = "Translated description of what the image contains",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .clip(CircleShape)
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.btn_profile_edit),
                        contentDescription = "edit button",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .size(70.dp)
                            .alpha(0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                LocalTextField()

                Spacer(modifier = Modifier.height(16.dp))

                GradientButton(
                    text = "확인",
                    gradient = MaruBrush,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        GlobalScope.launch {
                            launch(Dispatchers.IO) {
                                memberViewModel.updateMemberProfileToServer(context)
                            }
                            launch(Dispatchers.Main) {
                                navigateViewModel.navigator?.navigate("main/-1")
                            }
                        }
                    })
            }
        }
    }
}

@Composable
fun LocalTextField(memberViewModel: MemberViewModel = hiltViewModel()) {
    val nickname = remember { mutableStateOf(memberViewModel.memberInfo.value!!.nickname) }

    TextField(
        value = nickname.value,
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaruBackground,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        onValueChange = {
            nickname.value = it
            memberViewModel.modifiedNickname.value = it
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = memberViewModel.memberInfo.value!!.nickname) }
    )
}