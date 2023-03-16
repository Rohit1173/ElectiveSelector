package com.example.electiveselector.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentProfHomeBinding
import com.example.electiveselector.databinding.FragmentStudentHomeBinding


class StudentHome : Fragment() {

    private var _binding: FragmentStudentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentHomeBinding.inflate(
            inflater, container, false
        )
        replaceFragment(SelectedElectives())
        binding.studentBottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.announcement -> replaceFragment(Announcement())
                R.id.selectedElectives -> replaceFragment(SelectedElectives())
            }
            true
        }
        return binding.root
    }

    private fun replaceFragment(fragment: Fragment){
        activity?.supportFragmentManager!!.beginTransaction().apply {
            replace(R.id.mStudentFrame,fragment)
            commit()
        }
    }

}