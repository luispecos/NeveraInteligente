package com.example.neverainteligente.ui.close

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.neverainteligente.VerifyEmail
import com.google.firebase.auth.FirebaseAuth



class CloseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseAuth.getInstance().signOut()
        startActivity(
            Intent(requireContext(), VerifyEmail::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        return null
    }


}