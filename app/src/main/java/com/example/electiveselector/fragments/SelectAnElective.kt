package com.example.electiveselector.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentSelectAnElectiveBinding
import com.example.electiveselector.databinding.FragmentStudentHomeBinding


class SelectAnElective : Fragment() {
    private var _binding: FragmentSelectAnElectiveBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectAnElectiveBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

}