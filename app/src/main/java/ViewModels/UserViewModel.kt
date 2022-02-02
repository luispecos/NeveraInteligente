package ViewModels

import Interface.IonClick
import Library.Multimedia
import Library.Permissions
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.neverainteligente.R
import com.example.neverainteligente.databinding.AddUserBinding

class UserViewModel(activity: Activity, binding: AddUserBinding) : ViewModel() ,IonClick {
    private var _activity: Activity? = null
    private var _binding: AddUserBinding? = null
    private var _permissions: Permissions? = null
    private var _multimedia: Multimedia? = null
    private val REQUEST_CODE_CROP_IMAGE = 1
    private val REQUEST_CODE_TAKE_PHOTO = 0
    private val TEMP_PHOYO_FILE = "temporary_img.png"

    init {
        _activity = activity
        _binding = binding
        _permissions = Permissions(activity)
        _multimedia = Multimedia(activity)
    }

    override fun onClick(view: View) {
        when (view.id){
            R.id.buttonCamera -> if (_permissions!!.CAMERA() && _permissions!!.STORAGE()){
                _multimedia!!.dispatchTakePictureIntent()
            }
            R.id.buttonGallery -> if (_permissions!!.STORAGE()){
                _multimedia!!.cropCapturedImage(1)
            }
        }
    }
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(resultCode === RESULT_OK){
            when(requestCode){
                REQUEST_CODE_TAKE_PHOTO -> _multimedia!!.cropCapturedImage(0)
                REQUEST_CODE_CROP_IMAGE -> {
                    var imagenCortada: Bitmap? = data?.extras?.get("data") as Bitmap?
                    if(imagenCortada == null){
                        val filePath: String = _activity!!.getExternalFilesDir(null)!!.absolutePath + "/" + TEMP_PHOYO_FILE
                        imagenCortada = BitmapFactory.decodeFile(filePath)
                    }
                    _binding!!.imageViewUser.setImageBitmap(imagenCortada)
                    _binding!!.imageViewUser.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }
    }
}