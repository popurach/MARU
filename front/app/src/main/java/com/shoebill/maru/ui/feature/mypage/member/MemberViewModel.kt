package com.shoebill.maru.ui.feature.mypage.member

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.shoebill.maru.model.data.Member
import com.shoebill.maru.model.data.MemberUpdateRequest
import com.shoebill.maru.model.repository.MemberRepository
import com.shoebill.maru.util.apiCallback
import com.shoebill.maru.ui.main.NavigateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _memberInfo = MutableLiveData<Member>()
    val modifiedNickname = MutableLiveData<String?>()
    val modifiedImageUri = MutableLiveData<Uri?>()

    val memberInfo: LiveData<Member> get() = _memberInfo

    private fun updateMemberInfo(value: Member) {
        _memberInfo.value = value
    }

    fun getPoint(): String =
        NumberFormat.getNumberInstance(Locale.US).format(_memberInfo.value?.point ?: 0)

    private fun updateNoticeToken(navHostController: NavHostController) {
        val TAG = "MyFirebaseMessagingService"

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(
                    TAG,
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            viewModelScope.launch {
                apiCallback(navHostController) {
                    memberRepository.updateNoticeToken(
                        token.toRequestBody("text/plain".toMediaType())
                    )
                }
            }
        })
    }

    fun getMemberInfo(navigateViewModel: NavigateViewModel) {
        viewModelScope.launch {
            val memberInfo = apiCallback(navigateViewModel.navigator!!) {
                memberRepository.getMemberInfo()
            }
            updateMemberInfo(memberInfo!!)
            updateNoticeToken(navigateViewModel.navigator!!)
        }
    }

    fun logout() {
        memberRepository.logout()
    }

    suspend fun updateMemberProfileToServer(
        context: Context,
        navHostController: NavHostController
    ) {
        viewModelScope.launch() {
            val imageUri = modifiedImageUri.value
            val nickname = modifiedNickname.value
            var file: File? = null
            if (imageUri != null) {
                file = uriToFile(context, imageUri)
            }

            apiCallback(navHostController) {
                memberRepository.updateMemberInfo(
                    MemberUpdateRequest(
                        image = file,
                        nickname = nickname
                    )
                )
            }?.let {
                updateMemberInfo(it)
            } ?: run {
                // response null 일때 실행되는 코드
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "image.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return file
    }
}