package ViewModels

import Interface.IonClick
import Library.*
import Models.BindableString
import Models.Collections
import Models.Item
import Models.Pojo.User
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import com.example.neverainteligente.AddUser
import com.example.neverainteligente.R
import com.example.neverainteligente.databinding.AddUserBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*
import kotlin.collections.HashMap

class UserViewModel(activity: Activity, binding: AddUserBinding) : ViewModel() ,IonClick {
    private var _activity: Activity? = null
    private var _binding: AddUserBinding? = null
    private var _permissions: Permissions? = null
    private var _multimedia: Multimedia? = null
    private val REQUEST_CODE_CROP_IMAGE = 1
    private val REQUEST_CODE_TAKE_PHOTO = 0
    private val TEMP_PHOYO_FILE = "temporary_img.png"
    private var memoryData: MemoryData? = null
    private var mAuth: FirebaseAuth? = null
    private var _db: FirebaseFirestore? = null
    private var _documentRef: DocumentReference? = null
    private var _storage: FirebaseStorage? = null
    private var _storageRef: StorageReference? = null

    var nameUI = BindableString()
    var lastnameUI = BindableString()
    var emailUI = BindableString()
    var passwordUI = BindableString()
    var item: Item = Item()
    private var userList: ArrayList<User> = ArrayList()

    init {
        _activity = activity
        _binding = binding
        _permissions = Permissions(activity)
        _multimedia = Multimedia(activity)
        memoryData = MemoryData.getInstance(activity)
        mAuth = FirebaseAuth.getInstance()
        _storage = FirebaseStorage.getInstance()
        _storageRef = _storage!!.reference
        _binding!!.progressBar.visibility = ProgressBar.INVISIBLE
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonCamera -> if (_permissions!!.CAMERA() && _permissions!!.STORAGE()) {
                _multimedia!!.dispatchTakePictureIntent()
            }
            R.id.buttonGallery -> if (_permissions!!.STORAGE()) {
                _multimedia!!.cropCapturedImage(1)
            }
            R.id.buttonAddUser -> addUser()
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_TAKE_PHOTO -> _multimedia!!.cropCapturedImage(0)
                REQUEST_CODE_CROP_IMAGE -> {
                    var imagenCortada: Bitmap? = data?.extras?.get("data") as Bitmap?
                    if (imagenCortada == null) {
                        val filePath: String =
                            _activity!!.getExternalFilesDir(null)!!.absolutePath + "/" + TEMP_PHOYO_FILE
                        imagenCortada = BitmapFactory.decodeFile(filePath)
                    }
                    _binding!!.imageViewUser.setImageBitmap(imagenCortada)
                    _binding!!.imageViewUser.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }
    }

    private fun addUser() {
        if (TextUtils.isEmpty(nameUI.getValue())) {
            _binding!!.nameEditText.error = _activity!!.getString(R.string.error_field_required)
            _binding!!.nameEditText.requestFocus()
        } else {
            if (TextUtils.isEmpty(lastnameUI.getValue())) {
                _binding!!.lastnameEditText.error =
                    _activity!!.getString(R.string.error_field_required)
                _binding!!.lastnameEditText.requestFocus()
            } else {
                if (TextUtils.isEmpty(emailUI.getValue())) {
                    _binding!!.emailEditText.error =
                        _activity!!.getString(R.string.error_field_required)
                    _binding!!.emailEditText.requestFocus()
                } else {
                    if(!Validate.isEmail(emailUI.getValue())){
                        _binding!!.emailEditText.error =
                            _activity!!.getString(R.string.error_invalid_email)
                        _binding!!.emailEditText.requestFocus()
                    }else{
                        if (TextUtils.isEmpty(passwordUI.getValue())) {
                            _binding!!.passwordEditText.error =
                                _activity!!.getString(R.string.error_field_required)
                            _binding!!.passwordEditText.requestFocus()
                        }else{
                            if (!isPasswordValid(passwordUI.getValue())) {
                                _binding!!.passwordEditText.error =
                                    _activity!!.getString(R.string.error_invalid_password)
                                _binding!!.passwordEditText.requestFocus()
                            }else{
                                if (Networks(_activity!!).verificaNetworks()){
                                    insertUser()
                                }else{
                                    Snackbar.make(_binding!!.passwordEditText, R.string.networks, Snackbar.LENGTH_LONG).show()
                                }
                            }
                        }
                    }

                }
            }
        }
    }
    private fun isPasswordValid(password: String) : Boolean{
        return password.length >= 6
    }
    private fun insertUser(){
        _binding!!.progressBar.visibility = ProgressBar.VISIBLE
        mAuth!!.createUserWithEmailAndPassword(emailUI.getValue(), passwordUI.getValue())
            .addOnCompleteListener(
                _activity!!
            ){task: Task<AuthResult?> ->
                if(task.isSuccessful){
                    val imagesRef = _storageRef!!.child(Collections.User.USERS + "/" + emailUI.getValue())
                    val data: ByteArray? = _multimedia?.imageByte(_binding!!.imageViewUser)
                    val uploadTask = imagesRef.putBytes(data!!)
                    uploadTask.addOnFailureListener{exception: Exception? ->
                    }
                        .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                            //url de la imagen
                            val image = taskSnapshot.metadata!!.path
                            val role = _activity!!.resources.getStringArray(R.array.item_roles)[item.getSelectedItemPosition()]
                            _db = FirebaseFirestore.getInstance()
                            _documentRef = _db!!.collection(Collections.User.USERS).document(emailUI.getValue())
                            val user: MutableMap<String, Any> = HashMap()
                            user[Collections.User.LASTNAME] = lastnameUI.getValue()
                            user[Collections.User.EMAIL] = emailUI.getValue()
                            user[Collections.User.NAME] = nameUI.getValue()
                            user[Collections.User.ROLE] = role
                            user[Collections.User.IMAGE] = image
                            _documentRef!!.set(user)
                                .addOnCompleteListener{task2: Task<Void?> ->
                                    if(task2.isSuccessful){
                                        _activity!!.finish()
                                    }
                                }
                        }
                }else{
                    _binding!!.progressBar.visibility = ProgressBar.INVISIBLE
                    Snackbar.make(_binding!!.passwordEditText, R.string.fail_register, Snackbar.LENGTH_LONG).show()
                }

            }
    }
    private fun CloudFirestore(){
        _db = FirebaseFirestore.getInstance()
        _db!!.collection(Collections.User.USERS).addSnapshotListener{
                snapshots,e ->
            userList = ArrayList()
            for (document in snapshots!!){
                val lastname = document.data[Collections.User.LASTNAME].toString()
                val email = document.data[Collections.User.EMAIL].toString()
                val name = document.data[Collections.User.NAME].toString()
                val role = document.data[Collections.User.ROLE].toString()
                val image = document.data[Collections.User.IMAGE].toString()
                userList.add(User(lastname, name, email, role, image))
                initRecyclerView(userList)
            }
        }
    }

    private fun initRecyclerView(list: MutableList<User>) {

    }
}