package com.capstone.aquamate.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NamaCustomView(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    private val MIN_NAME_LENGTH = 2
    private var isValid = false

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isValid = isNameValid(s.toString())
                if (!isValid) {
                    (parent.parent as? TextInputLayout)?.error = "Nama harus terdiri dari setidaknya $MIN_NAME_LENGTH karakter"
                } else {
                    (parent.parent as? TextInputLayout)?.error = null
                }
            }
        })
    }

    fun isValidName(): Boolean {
        return isValid
    }

    private fun isNameValid(name: String): Boolean {
        return name.length >= MIN_NAME_LENGTH
    }
}