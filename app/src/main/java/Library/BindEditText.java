package Library;

import android.util.Pair;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.example.neverainteligente.R;


import Models.BindableString;

//utilizamos este clase para crear un adaptador asociado a los campos de textos para generar eventos
public class BindEditText {
    //{"app:binding"} es el elemento que proporcionamos a los campos de texto que queremos vincular
    @BindingAdapter({"app:binding"})
    public static void bindEditText(EditText view, final BindableString bindableString){
        Pair<BindableString, TextWatcherAdapter> pair = (Pair) view.getTag(R.id.bound_observable);
        if(pair == null || pair.first != bindableString){
            if (pair != null){
                view.removeTextChangedListener(pair.second);
            }
            TextWatcherAdapter watcher = new TextWatcherAdapter() {
                public void onTextChanged(CharSequence s, int start, int before, int count){
                    bindableString.setValue(s.toString());
                }
            };
            view.setTag(R.id.bound_observable, new Pair<>(bindableString, watcher));
            view.addTextChangedListener(watcher);
        }
        String newValue = bindableString.getValue();
        if (!view.getText().toString().equals(newValue)){
            view.setText(newValue);
        }
    }
}
