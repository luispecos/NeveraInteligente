package Library

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.jar.Manifest

class Permissions(activity: Activity) {
    private var _activity: Activity? = null

    init {
        _activity = activity
    }

    //STORAGE = función para pedir permiso para poder acceder a la información multimedia del dispositivo
    fun STORAGE(): Boolean{
        return if (ContextCompat.checkSelfPermission(_activity!!, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(_activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(_activity!!, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            false
        }else{
            true
        }
    }

    fun CAMERA(): Boolean{
        return if (ContextCompat.checkSelfPermission(_activity!!, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(_activity!!, arrayOf(android.Manifest.permission.CAMERA), 2)
            false
        }else{
            true
        }
    }
}