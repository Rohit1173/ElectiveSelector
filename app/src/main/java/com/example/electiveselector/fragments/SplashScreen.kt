package com.example.electiveselector.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.electiveselector.Professor
import com.example.electiveselector.R
import com.example.electiveselector.Student
import com.example.electiveselector.databinding.FragmentSplashScreenBinding
import com.example.electiveselector.viewModels.ProfVerifyViewModel
import com.example.electiveselector.viewModels.jwtViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class SplashScreen : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var vm: ProfVerifyViewModel
    private lateinit var mAuth:FirebaseAuth
    lateinit var msg:String
    lateinit var errorMsg:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashScreenBinding.inflate(
            inflater, container, false
        )
        mAuth = FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(ProfVerifyViewModel::class.java)

//        Handler().postDelayed({
//           findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
//        },3000)

//        val sharedPreference =  requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
//
//        val myKey: String? =sharedPreference.getString("key","")
//        vm.status.observe(viewLifecycleOwner){
//            if(it!="SUCCESS"){
//                Handler().postDelayed({
//                    findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
//                },3000)
//            }
//        }
//        if(myKey!!.isNotEmpty())
//        {
//
//            vm.checkLogin(myKey)
//
//            vm.myevent.observe(viewLifecycleOwner){
//
//                Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
//                if(it.message=="SUCCESSFULLY VERIFIED"){
//                    Handler().postDelayed({
//                        findNavController().navigate(R.id.action_splashScreen_to_professorFragment)
//                    },3000)
//                }
//                else{
//                    Handler().postDelayed({
//                        findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
//                    },3000)
//                }
//            }
//        }
//        else{
//            Handler().postDelayed({
//                findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
//            },3000)
//        }
        vm.profResponse.observe(
            viewLifecycleOwner
        ) {
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    msg = jsonObject.getString("message")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if(msg=="prof"){
                    val intent = Intent (activity, Professor::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent (activity, Student::class.java)
                    startActivity(intent)
                }


            } else {
                try {
                    val jObjError = JSONObject(it.errorBody()!!.string())
                    errorMsg = jObjError.getString("message")
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
                findNavController().navigate(R.id.action_splashScreen_to_firebaseSignIn)
            }
        }
        vm.status.observe(viewLifecycleOwner){
            if(it=="FAIL"){
                findNavController().navigate(R.id.action_splashScreen_to_firebaseSignIn)
            }
        }
        Handler().postDelayed({
            if(user!=null){
                vm.checkProf(user.email.toString())
            }
            else{
                findNavController().navigate(R.id.action_splashScreen_to_firebaseSignIn)
            }
            },3000)




        return binding.root
    }

}