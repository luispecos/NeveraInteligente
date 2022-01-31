package Models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.util.*


/* BaseOnservable se puede ampliar. Las clases de los datos son las responsables de notificar
cuándo cambian las propiedades. Se invoca en cada actualización y actualiza las visatas correspondientes
La anotación @Bindable se le da a las funciones que realizan cambios
 */
class BindableString : BaseObservable() {
    private var value : String? = null

    //obtener datos de los campos de texto
    @Bindable
    fun getValue() :String{
        return if (value != null) value!! else ""
    }
    fun setValue(value: String){
        if(!Objects.equals(this.value, value)){
            this.value = value
            notifyPropertyChanged(BR.value)
        }
    }
}