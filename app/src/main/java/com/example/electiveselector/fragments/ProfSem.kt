package com.example.electiveselector.fragments

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentProfSemBinding

class ProfSem : Fragment() {

    private var _binding: FragmentProfSemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfSemBinding.inflate(
            inflater, container, false
        )
        val myData = arguments?.getString("key")
        binding.semHeading.text= "Semester $myData"
        binding.el1List.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("sem", myData)
            bundle.putString("el", "1")
            replaceFragment(SemData(),bundle)
        }
        binding.el2List.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("sem", myData)
            bundle.putString("el", "2")
            replaceFragment(SemData(),bundle)
        }
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment,args: Bundle? = null){
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame,fragment)
            commit()
        }
    }


}