package com.example.electiveselector.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.data.loginData
import com.example.electiveselector.databinding.FragmentLoginBinding
import com.example.electiveselector.viewModels.loginViewModel
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: loginViewModel
    private lateinit var logMsg:String
    private lateinit var logErrorMsg:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(
            inflater, container, false
        )
        val sharedPreference =  requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(loginViewModel::class.java)
        binding.logUser.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                binding.layoutLogUser.error = null
            }
        }
        binding.logPassword.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                binding.layoutLogPassword.error = null
            }
        }

        binding.changeToSignup.setOnClickListener {
           // findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.logInBtn.setOnClickListener {
            if (binding.logUser.text.toString().trim().isEmpty()) {
                binding.layoutLogUser.error = "This Field Cannot Be Empty"
            } else {
                binding.layoutLogUser.error = null
            }
            if (binding.logPassword.text.toString().trim().isEmpty()) {
                binding.layoutLogPassword.error = "Password Cannot Be Empty"
            } else {
                binding.layoutLogPassword.error = null
            }
            if (binding.logUser.text.toString().trim()
                    .isNotEmpty() && binding.logPassword.text.toString().trim().isNotEmpty()
            ) {
                vm.checkUser(
                    loginData(
                        binding.logUser.text.toString().trim(),
                        binding.logPassword.text.toString().trim()
                    )
                )
            }
        }

        vm.logResponse.observe(
            viewLifecycleOwner
        ) {
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    logMsg = jsonObject.getString("message")

                    val editor = sharedPreference.edit()
                    editor.putString("key",logMsg)
                    editor.apply()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                Toast.makeText(context, logMsg, Toast.LENGTH_LONG).show()
               // findNavController().navigate(R.id.action_loginFragment_to_professorFragment)
            } else {
                try {
                    val jObjError = JSONObject(it.errorBody()!!.string())
                    logErrorMsg = jObjError.getString("message")
                    if (logErrorMsg=="User not found") {
                        binding.layoutLogUser.error = logErrorMsg
                    }
                    else if(logErrorMsg=="Incorrect Password"){
                        binding.layoutLogPassword.error = logErrorMsg
                    }

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        return binding.root
    }

}