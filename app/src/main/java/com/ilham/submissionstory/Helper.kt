package com.ilham.submissionstory

import android.view.View

class Helper {
    fun showLoading(isLoading: Boolean, progressBar: View) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}