package com.example.neverainteligente

import Library.MemoryData
import ViewModels.LoginViewModels
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.neverainteligente.databinding.EmailVerifyBinding

class VerifyEmail : AppCompatActivity() {
    private var memoryData: MemoryData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.email_verify)
        memoryData = MemoryData.getInstance(this)
        if (memoryData!!.getData("user") == "") {
            var binding =
                DataBindingUtil.setContentView<EmailVerifyBinding>(this, R.layout.email_verify)
            binding.emailModel = LoginViewModels(this, binding, null)
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorAzul, null)
        }else{
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}