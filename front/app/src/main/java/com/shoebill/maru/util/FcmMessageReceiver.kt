package com.shoebill.maru.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.shoebill.maru.viewmodel.NoticeViewModel

class FcmMessageReceiver constructor(private val noticeViewModel: NoticeViewModel) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            noticeViewModel.newNoticeArrived()
        }
    }

}