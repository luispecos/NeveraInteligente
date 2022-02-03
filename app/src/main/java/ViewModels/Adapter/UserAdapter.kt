package ViewModels.Adapter

import Models.Pojo.User
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.neverainteligente.R
import com.example.neverainteligente.databinding.ItemUserBinding

class UserAdapter(
    userList: List<User>,
    listener: AdapterListener?
) : RecyclerView.Adapter<UserAdapter.MyViewHolder>(){
    private val _userList: List<User>
    private var _layoutInflater: LayoutInflater? = null
    private val _listener: AdapterListener?

    init {
        _userList = userList
        _listener = listener
    }
    inner class MyViewHolder(val _binding: ItemUserBinding) :
            RecyclerView.ViewHolder(_binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        if(_layoutInflater == null){
            _layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ItemUserBinding>(
            _layoutInflater!!,
            R.layout.item_user,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder, position: Int) {
        holder._binding.user = _userList[position]
        holder._binding.cardViewUser.setOnClickListener{
            v: View? -> _listener?.onUserClicked(_userList[position])
        }
    }

    override fun getItemCount(): Int {
        return _userList.size
    }
    interface AdapterListener{
        fun onUserClicked(user: User?)
    }

}