package com.example.electiveselector.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentSelectAnElectiveBinding
import com.example.electiveselector.databinding.FragmentStudentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class SelectAnElective : Fragment() {
    private var _binding: FragmentSelectAnElectiveBinding? = null
    private val binding get() = _binding!!
    lateinit var mAuth:FirebaseAuth
    lateinit var email:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectAnElectiveBinding.inflate(
            inflater, container, false
        )
        mAuth=FirebaseAuth.getInstance()
        email=mAuth.currentUser!!.email.toString()
        val year=email.subSequence(3,7)
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val x= currentYear-year.toString().toInt()

        if(x==2&&(currentMonth in 6..11)){
            binding.currentSem.setText("SEM 5")
        }
        else if(x==3){
            if(currentMonth in 6..11){
                binding.currentSem.setText("SEM 7")
            }
            else{
                binding.currentSem.setText("SEM 6")
            }
        }
        return binding.root
    }

}