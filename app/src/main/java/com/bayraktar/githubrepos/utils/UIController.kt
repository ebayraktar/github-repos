package com.bayraktar.githubrepos.utils

interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun showToastMessage(message: String)

    fun showSnacbkarMessage(message: String)
}