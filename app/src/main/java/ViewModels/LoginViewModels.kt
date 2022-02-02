package ViewModels

import Interface.IonClick
import Library.MemoryData
import Library.Networks
import Library.Validate
import Models.BindableString
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.neverainteligente.MainActivity
import com.example.neverainteligente.R
import com.example.neverainteligente.VerificarPassword
import com.example.neverainteligente.databinding.EmailVerifyBinding
import com.example.neverainteligente.databinding.VerificarPasswordBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginViewModels(activity: Activity, bindingEmail: EmailVerifyBinding?, bindingPassword: VerificarPasswordBinding?) : ViewModel(), IonClick {
    private var _activity : Activity? = null
    //emailUI = variable para gestionar la información del campo de texto
    var emailUI: BindableString = BindableString()
    var passwordUI: BindableString = BindableString()
    var email : String? = null
    private var mAuth: FirebaseAuth? = null
    private var memoryData: MemoryData? = null

    //objetos estáticos
    companion object{
        private var _bindingEmail: EmailVerifyBinding? = null
        private var _bindingPassword: VerificarPasswordBinding? = null
        private var emailData: String? = null
    }
    //inicializamos
    init {
        _activity = activity
        _bindingEmail = bindingEmail
        _bindingPassword = bindingPassword
        if(emailData != null){
            emailUI.setValue(emailData!!)
            email = emailData!!
        }
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onClick(view: View) {
        //cuando view.id = R.id.email_sign_in_button se ejecuta la funcion verificarEmail()
        when(view.id){
            R.id.email_sign_in_button -> verificarEmail()
            R.id.password_sign_in_button -> login()
        }
        //Toast = mostrar mensaje que desaparece
        //Toast.makeText(_activity, emailUI.getValue(), Toast.LENGTH_SHORT).show()
    }

    private fun verificarEmail(){
        var cancel = true
        _bindingEmail!!.emailEditText.error = null
        //vemos si el campo de texto esta vacio
        if (TextUtils.isEmpty(emailUI.getValue())){
            _bindingEmail!!.emailEditText.error = _activity!!.getString(R.string.error_field_required)
            _bindingEmail!!.emailEditText.requestFocus()
            cancel = false
        }else if (!Validate.isEmail(emailUI.getValue())){
            _bindingEmail!!.emailEditText.error = _activity!!.getString(R.string.error_invalid_email)
            _bindingEmail!!.emailEditText.requestFocus()
            cancel = false
        }
        if(cancel){
            emailData = emailUI.getValue()
            _activity!!.startActivity(Intent(_activity, VerificarPassword::class.java))
        }
    }
    private fun login() {
        var cancel = true
        _bindingPassword!!.passwordEditText.error = null
        if (TextUtils.isEmpty(passwordUI.getValue())) {
            _bindingPassword!!.passwordEditText.error =
                _activity!!.getString(R.string.error_field_required)
            cancel = false
        } else if (!isPasswordValid(passwordUI.getValue())) {
            _bindingPassword!!.passwordEditText.error =
                _activity!!.getString(R.string.error_invalid_password)
            cancel = false
        }
        if (cancel) if (Networks(_activity!!).verificaNetworks()){
            mAuth!!.signInWithEmailAndPassword(emailData!!, passwordUI.getValue())
                .addOnCompleteListener(_activity!!){ task ->
                    if(task.isSuccessful) {
                        memoryData = MemoryData.getInstance(_activity!!)
                        memoryData!!.saveData("user", emailData.toString())
                        val intent = Intent(_activity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        _activity!!.startActivity(intent)
                    }else{
                        Snackbar.make(_bindingPassword!!.passwordEditText, R.string.invalid_credentials, Snackbar.LENGTH_LONG).show()
                    }

                }
        }else{
            Snackbar.make(_bindingPassword!!.passwordEditText, R.string.networks, Snackbar.LENGTH_LONG).show()
        }
    }
    private fun isPasswordValid(password: String) : Boolean{
        return password.length >= 6
    }
}