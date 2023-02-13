package com.example.electiveselector.fragments

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.adapters.AnnouncementAdapter
import com.example.electiveselector.data.ElectiveData
import com.example.electiveselector.data.ElectiveDetails
import com.example.electiveselector.databinding.FragmentAnnouncementBinding
import com.example.electiveselector.viewModels.AnnouncementViewModel


class Announcement : Fragment() {
    private var _binding: FragmentAnnouncementBinding? = null
    private val binding get() = _binding!!
    lateinit var vm:AnnouncementViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAnnouncementBinding.inflate(
            inflater, container, false
        )
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(AnnouncementViewModel::class.java)

        vm.announcements.observe(viewLifecycleOwner) {
            val list:MutableList<ElectiveData> = it.message
            binding.recycler.adapter = AnnouncementAdapter(list)
            binding.recycler.setHasFixedSize(true)
        }


        return binding.root
    }


}