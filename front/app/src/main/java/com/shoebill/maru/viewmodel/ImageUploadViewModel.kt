package com.shoebill.maru.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageUploadViewModel() : ViewModel() {
    private val _inputTag = MutableLiveData("")
    val inputTag get() = _inputTag

    private val listOfTag: MutableList<String> = mutableListOf()
    private val _tagList = MutableLiveData<List<String>>(listOf())
    val tagList get() = _tagList

    fun updateInputTag(value: String) {
        _inputTag.value = value
    }

    fun addTag() {
        if (_inputTag.value.isNullOrEmpty()) return
        listOfTag.add(_inputTag.value!!)
        _tagList.value = listOfTag
        _inputTag.value = ""
    }
}