package Library

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class Multimedia(private val _activity: Activity) {
    private var mCurrentPhotoPath: String? = null
    private var photoURI: Uri? = null

    companion object{
        private const val REQUEST_CODE_CROP_IMAGE = 1
        const val REQUEST_CODE_TAKE_PHOTO = 0
        private const val TEMP_PHOYO_FILE = "temporary_img.png"
    }

    //dispatchTakePictureIntent = ejecutar la cámara del móvil
    fun dispatchTakePictureIntent(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val values = ContentValues()
        photoURI = _activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        _activity.startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO)

    }

    fun cropCapturedImage(action: Int) {
        var cropIntent: Intent? = null
        when(action){
            0 -> {
                cropIntent = Intent("com.android.camera.action.CROP")
                cropIntent.setDataAndType(photoURI, "image/*")
                //indicamos los límites de nuestra imagen al cortar
                cropIntent.putExtra("outputX", 400)
                cropIntent.putExtra("outputY", 250)


            }
            1 -> {
                cropIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                cropIntent.type = "image/*"
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempoFile())
                //limites de la imagen
                cropIntent.putExtra("outputX", 400)
                cropIntent.putExtra("outputY", 400)
            }
        }
        val list = _activity.packageManager.queryIntentActivities(cropIntent!!, 0)
        if(0 == list.size){
            Toast.makeText(_activity, "No se puede editar imágenes en este dispositivo", Toast.LENGTH_SHORT).show()
        }else{
            //Habilitamos el crop en este intent
            cropIntent!!.putExtra("crop", "true")
            cropIntent.putExtra("aspectX", 1)
            cropIntent.putExtra("aspectY", 1)
            cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG)
            cropIntent.putExtra("scale", true)
            //true retornará la imagen como un bitmap, false retornará la url de la imagen
            cropIntent.putExtra("return-data", true)
            //iniciamos la activity y pasamos un código de respuesta
            _activity.startActivityForResult(cropIntent, REQUEST_CODE_CROP_IMAGE)
        }
    }

    fun getTempoFile(): Uri? {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            val file = File(_activity!!.getExternalFilesDir(null)!!.absolutePath, TEMP_PHOYO_FILE)
            try{
                file.createNewFile()
            }catch (e: IOException){

            }
            Uri.fromFile(file)
        }else{
            null
        }
    }

    fun imageByte(imageView: ImageView): ByteArray?{
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return baos.toByteArray()
    }
}