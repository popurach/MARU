package com.shoebill.maru.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.shoebill.maru.model.data.Member
import com.shoebill.maru.model.data.MemberUpdateRequest
import com.shoebill.maru.model.repository.MemberRepository
import com.shoebill.maru.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val preferenceUtil: PreferenceUtil,
) : ViewModel() {
    private val _memberInfo = MutableLiveData<Member>()
    val modifiedNickname = MutableLiveData<String?>()
    val modifiedImageUri = MutableLiveData<Uri?>()

    val memberInfo: LiveData<Member> get() = _memberInfo

    fun updateMemberInfo(value: Member) {
        _memberInfo.value = value
    }

    fun getPoint(): String {
        return NumberFormat.getNumberInstance(Locale.US).format(_memberInfo.value?.point)
    }

    private fun updateNoticeToken() {
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

            runBlocking {
                launch {
                    memberRepository.updateNoticeToken(
                        token.toRequestBody("text/plain".toMediaType())
                    )
                }
            }
        })
    }

    fun getMemberInfo(navigateViewModel: NavigateViewModel) {
        runBlocking {
            launch {
                val response = memberRepository.getMemberInfo()
                if (response.isSuccessful) {
                    updateMemberInfo(response.body()!!)
                    // notice token update 필요
                    updateNoticeToken()
                } else {
                    Log.d("MEMBER", "회원 정보 조회 실패")
                    preferenceUtil.clear()
                    navigateViewModel.navigator?.navigate("login")
                }
            }
        }

    }

    fun logout() {
        memberRepository.logout()
    }

    fun updateMemberProfileToServer(context: Context) {
        val imageUri = modifiedImageUri.value
        val nickname = modifiedNickname.value
        var file: File? = null
        if (imageUri != null) {
            file = uriToFile(context, imageUri)
        }
        viewModelScope.launch {
            val response = memberRepository.updateMemberInfo(
                MemberUpdateRequest(
                    image = file,
                    nickname = nickname
                )
            )

            if (response.isSuccessful) {
                response.body()?.let { updateMemberInfo(it) }
            }
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File {
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            val filePath = it.getString(columnIndex)
            
            cursor.close()
            return File(filePath)
        }

        throw IllegalArgumentException("Uri not found")
    }
}