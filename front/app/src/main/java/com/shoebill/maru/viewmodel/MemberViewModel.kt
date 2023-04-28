package com.shoebill.maru.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.data.Member
import com.shoebill.maru.model.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    private val _memberInfo = MutableLiveData<Member>()
    val memberInfo: LiveData<Member> get() = _memberInfo

    fun updateMemberInfo(value: Member) {
        _memberInfo.value = value
    }

    fun getMemberInfo() {
        runBlocking {
            launch {
                val response = memberRepository.getMemberInfo()
                if (response.isSuccessful) {
                    updateMemberInfo(response.body()!!)
                } else {
                    Log.d("MEMBER", "회원 정보 조회 실패")
                }
            }
        }

    }

    fun logout() {
        memberRepository.logout()
    }
}