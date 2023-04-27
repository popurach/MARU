package com.shoebill.maru.viewmodel

import androidx.lifecycle.ViewModel
import com.shoebill.maru.model.repository.MemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
) : ViewModel() {
    fun logout() {
        memberRepository.logout()
    }
}