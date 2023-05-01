package com.shoebill.maru.ui.component.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoebill.maru.ui.component.bottomsheet.BottomSheetIndicator
import com.shoebill.maru.ui.component.common.Chip
import com.shoebill.maru.ui.theme.MaruBackground
import com.shoebill.maru.viewmodel.ImageUploadViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImageUploadForm(
    imageUploadViewModel: ImageUploadViewModel = hiltViewModel()
) {
    val inputTag = imageUploadViewModel.inputTag.observeAsState("")
    val tagList = imageUploadViewModel.tagList.observeAsState(listOf())
    Column(
        modifier = Modifier
            .height(400.dp)
            .padding(horizontal = 30.dp)
    ) {
        BottomSheetIndicator()
        Box(modifier = Modifier.height(40.dp))
        Text(text = "나만의 스팟을 저장해 보세요!", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Box(modifier = Modifier.padding(vertical = 14.dp)) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputTag.value,
                onValueChange = { value: String ->
                    imageUploadViewModel.updateInputTag(value)
                },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { imageUploadViewModel.addTag() }
                ),
                shape = RoundedCornerShape(24.dp),
                placeholder = { Text(text = "사진과 함께 저장하고 싶은 태그를 입력하세요!", fontSize = 12.sp) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaruBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
        FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            tagList.value.forEach { tag ->
                Chip(text = tag)
            }
        }
    }
}
