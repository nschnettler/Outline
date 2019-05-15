package com.schnettler.ethereal

import android.app.Activity
import android.os.Bundle
import com.schnettler.common.CommonLauncher

class SubstratumLauncher : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonLauncher.start(this)
    }
}