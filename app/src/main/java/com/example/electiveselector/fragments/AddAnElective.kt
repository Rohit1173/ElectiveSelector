package com.example.electiveselector.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.electiveselector.Professor
import com.example.electiveselector.R
import com.example.electiveselector.Student
import com.example.electiveselector.databinding.FragmentAddAnElectiveBinding
import com.example.electiveselector.databinding.FragmentSignUpBinding
import com.example.electiveselector.viewModels.AddAnElectiveViewModel
import com.example.electiveselector.viewModels.signupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AddAnElective : Fragment() {
    private var _binding: FragmentAddAnElectiveBinding? = null
    private val binding get() = _binding!!
    lateinit var vm: AddAnElectiveViewModel
    lateinit var mAuth: FirebaseAuth
    lateinit var msg:String
    lateinit var errorMsg:String
    lateinit var sem:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddAnElectiveBinding.inflate(
            inflater, container, false
        )
        mAuth = FirebaseAuth.getInstance()
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(AddAnElectiveViewModel::class.java)
        val list = listOf("Sem 5", "Sem 6", "Sem 7")
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            list
        )
        binding.chooseSem.adapter = adapter
        binding.chooseSem.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val str=adapterView!!.getItemAtPosition(position)
                if(str=="Sem 5"){
                    sem="5"
                }
                else if(str=="Sem 6"){
                    sem="6"
                }
                else if(str=="Sem 7"){
                    sem="7"
                }
                Toast.makeText(
                    requireContext(),
                    "You have selected $str",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        vm.addElectiveResponse.observe(viewLifecycleOwner){
            if(it.isSuccessful){
                    try {
                        val jsonObject = JSONObject(Gson().toJson(it.body()))
                        msg = jsonObject.getString("message")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                   Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
                } else {
                    try {
                        val jObjError = JSONObject(it.errorBody()!!.string())
                        errorMsg = jObjError.getString("message")
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
        }

        binding.el1Post.setOnClickListener {
            var sub1 = "NA"
            var faculty1 = "NA"
            var sub2 = "NA"
            var faculty2 = "NA"
            var sub3 = "NA"
            var faculty3 = "NA"
            if (binding.el1sub1Title.text.toString().trim().isNotEmpty()) {
                sub1 = binding.el1sub1Title.text.toString().trim()
            }
            if (binding.el1sub1Faculty.text.toString().trim().isNotEmpty()) {
                faculty1 = binding.el1sub1Faculty.text.toString().trim()
            }
            if (binding.el1sub2Title.text.toString().trim().isNotEmpty()) {
                sub2 = binding.el1sub2Title.text.toString().trim()
            }
            if (binding.el1sub2Faculty.text.toString().trim().isNotEmpty()) {
                faculty2 = binding.el1sub2Faculty.text.toString().trim()
            }
            if (binding.el1sub3Title.text.toString().trim().isNotEmpty()) {
                sub3 = binding.el1sub3Title.text.toString().trim()
            }
            if (binding.el1sub3Faculty.text.toString().trim().isNotEmpty()) {
                faculty3 = binding.el1sub3Faculty.text.toString().trim()
            }
            val currentDate: String =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val currentTime: String =
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            vm.addElective(
                ElectiveData(
                    sem,
                    "1",
                    ElectiveDetails(sub1, faculty1),
                    ElectiveDetails(sub2, faculty2),
                    ElectiveDetails(sub3, faculty3),
                    mAuth.currentUser!!.email.toString(),
                    currentDate+"\n"+currentTime,
                    "Don't Know"
                )
            )


        }
        binding.el2Post.setOnClickListener {
            var sub1 = "NA"
            var faculty1 = "NA"
            var sub2 = "NA"
            var faculty2 = "NA"
            var sub3 = "NA"
            var faculty3 = "NA"

            if (binding.el2sub1Title.text.toString().trim().isNotEmpty()) {
                sub1 = binding.el2sub1Title.text.toString().trim()
            }
            if (binding.el2sub1Faculty.text.toString().trim().isNotEmpty()) {
                faculty1 = binding.el2sub1Faculty.text.toString().trim()
            }
            if (binding.el2sub2Title.text.toString().trim().isNotEmpty()) {
                sub2 = binding.el2sub2Title.text.toString().trim()
            }
            if (binding.el2sub2Faculty.text.toString().trim().isNotEmpty()) {
                faculty2 = binding.el2sub2Faculty.text.toString().trim()
            }
            if (binding.el2sub3Title.text.toString().trim().isNotEmpty()) {
                sub3 = binding.el2sub3Title.text.toString().trim()
            }
            if (binding.el2sub3Faculty.text.toString().trim().isNotEmpty()) {
                faculty3 = binding.el2sub3Faculty.text.toString().trim()
            }

            val currentDate: String =
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val currentTime: String =
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            vm.addElective(
                ElectiveData(
                    sem,
                    "2",
                    ElectiveDetails(sub1, faculty1),
                    ElectiveDetails(sub2, faculty2),
                    ElectiveDetails(sub3, faculty3),
                    mAuth.currentUser!!.email.toString(),
                    currentDate+"\n"+currentTime,
                    "DON'T KNOW"
                )
            )
        }

        return binding.root
    }


}