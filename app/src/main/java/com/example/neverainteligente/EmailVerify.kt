package com.example.neverainteligente

import ViewModels.LoginViewModels
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.neverainteligente.databinding.EmailVerifyBinding

class EmailVerify : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.email_verify)
        var binding = DataBindingUtil.setContentView<EmailVerifyBinding>(this, R.layout.email_verify)
        binding.emailModel = LoginViewModels(this, binding, null)
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorAzul, null)
    }
}