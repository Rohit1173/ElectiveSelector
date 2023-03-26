package com.example.electiveselector.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.electiveselector.adapters.SemDataAdapter
import com.example.electiveselector.databinding.FragmentSemDataBinding


class SemData : Fragment() {
    private var _binding: FragmentSemDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSemDataBinding.inflate(
            inflater, container, false
        )
        val sem = arguments?.getString("sem")
        val el = arguments?.getString("el")
        binding.semHeading.text="Semester - $sem Elective - $el"
        val list = mutableListOf<Int>()

        for (i in 1..100) {
            list.add(i)
        }
        binding.semDataRecycler.adapter=SemDataAdapter(list)

        return binding.root
    }

}