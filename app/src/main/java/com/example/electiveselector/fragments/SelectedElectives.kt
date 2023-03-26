package com.example.electiveselector.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.R
import com.example.electiveselector.databinding.FragmentSelectAnElectiveBinding
import com.example.electiveselector.databinding.FragmentSelectedElectivesBinding
import com.example.electiveselector.viewModels.SelectAnElectiveViewModel
import com.example.electiveselector.viewModels.SelectedElectivesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject


class SelectedElectives : Fragment() {

    private var _binding: FragmentSelectedElectivesBinding? = null
    private val binding get() = _binding!!
    lateinit var mAuth:FirebaseAuth
    lateinit var vm:SelectedElectivesViewModel
    lateinit var errorMsg:String
    lateinit var msg:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedElectivesBinding.inflate(
            inflater, container, false
        )
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(SelectedElectivesViewModel::class.java)
        mAuth= FirebaseAuth.getInstance()
        binding.greeting.text = "Welcome, ${mAuth.currentUser!!.displayName}"
        vm.getSelectedElectives(mAuth.currentUser!!.email!!)
        vm.selectedElectivesResponse.observe(viewLifecycleOwner){
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    val messageJsonObject = jsonObject.getJSONObject("message")
                    val el1=messageJsonObject.getJSONObject("sub1")
                    val el2=messageJsonObject.getJSONObject("sub2")
                    val sub1=el1.getString("subTitle")
                    val fac1=el1.getString("facultyName")
                    val sub2=el2.getString("subTitle")
                    val fac2=el2.getString("facultyName")
                    binding.el1Title.text=sub1
                    binding.el1Faculty.text=fac1
                    binding.el2Title.text=sub2
                    binding.el2Faculty.text=fac2
                    if(sub1=="NA"&&sub2=="NA"){
                        binding.noSelected.visibility=View.VISIBLE
                    }
                    else if(sub1=="NA"){
                        binding.sub1card.visibility=View.GONE
                    }
                    else if(sub2=="NA"){
                        binding.sub2card.visibility=View.GONE
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                try {
                    val jObjError = JSONObject(it.errorBody()!!.string())
                    errorMsg = jObjError.getString("message")
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }


        return binding.root
    }


}