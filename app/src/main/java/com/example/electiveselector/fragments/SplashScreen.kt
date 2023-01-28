package com.example.electiveselector.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentSplashScreenBinding
import com.example.electiveselector.viewModels.jwtViewModel


class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var vm: jwtViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(
            inflater, container, false
        )
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(jwtViewModel::class.java)

//        Handler().postDelayed({
//           findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
//        },3000)

        val sharedPreference =  requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)

        val myKey: String? =sharedPreference.getString("key","")
        vm.status.observe(viewLifecycleOwner){
            if(it!="SUCCESS"){
                Handler().postDelayed({
                    findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
                },3000)
            }
        }
        if(myKey!!.isNotEmpty())
        {

            vm.checkLogin(myKey)

            vm.myevent.observe(viewLifecycleOwner){

                Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                if(it.message=="SUCCESSFULLY VERIFIED"){
//                    Handler().postDelayed({
//                        findNavController().navigate(R.id.action_splashScreen_to_entranceFragment)
//                    },3000)
                }
                else{
                    Handler().postDelayed({
                        findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
                    },3000)
                }
            }
        }
        else{
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
            },3000)
        }




        return binding.root
    }

}