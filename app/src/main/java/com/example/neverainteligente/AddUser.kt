package com.example.neverainteligente

import ViewModels.UserViewModel
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.neverainteligente.R
import com.example.neverainteligente.databinding.AddUserBinding
import com.example.neverainteligente.databinding.EmailVerifyBinding

class AddUser : AppCompatActivity() {

    private var user: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
       // setContentView(R.layout.add_user)
        var _binding =
            DataBindingUtil.setContentView<AddUserBinding>(this, R.layout.add_user)
        user = UserViewModel(this, _binding, null)
        _binding.userModel = user
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.colorNegro, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        user!!.onActivityResult(requestCode, resultCode, data)

    }
}