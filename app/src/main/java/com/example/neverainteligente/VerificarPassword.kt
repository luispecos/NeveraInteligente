package com.example.neverainteligente

import ViewModels.LoginViewModels
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.neverainteligente.databinding.VerificarPasswordBinding

class VerificarPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.verificar_password)
        var binding = DataBindingUtil.setContentView<VerificarPasswordBinding>(this, R.layout.verificar_password)
        binding.passwordModel = LoginViewModels(this, null, binding)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorAzul, null)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, EmailVerify::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}