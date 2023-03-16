package com.example.electiveselector.fragments

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentProfHomeBinding


class ProfHome : Fragment() {

    private var _binding: FragmentProfHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfHomeBinding.inflate(
            inflater, container, false
        )
        replaceFragment(AddAnElective())
        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.announcement -> replaceFragment(Announcement())
                R.id.addElective -> replaceFragment(AddAnElective())
            }
            true
        }

        return binding.root
    }
    private fun replaceFragment(fragment: Fragment){
        activity?.supportFragmentManager!!.beginTransaction().apply {
            replace(R.id.mFrame,fragment)
            commit()
        }
    }
}