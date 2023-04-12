package com.example.electiveselector.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.data.ElectiveData
import com.example.electiveselector.data.ElectiveDetails
import com.example.electiveselector.databinding.FragmentAddAnElectiveBinding
import com.example.electiveselector.viewModels.AddAnElectiveViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AddAnElective : Fragment(){
    private var _binding: FragmentAddAnElectiveBinding? = null
    private val binding get() = _binding!!
    lateinit var vm: AddAnElectiveViewModel
    lateinit var mAuth: FirebaseAuth
    lateinit var msg:String
    lateinit var errorMsg:String
    lateinit var sem:String
    @RequiresApi(Build.VERSION_CODES.N)
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
        val el1btnList:MutableList<AppCompatButton> = mutableListOf(binding.el1CsBtn,binding.el1ItBtn,binding.el1CsaiBtn,binding.el1CsbBtn)
        for( btn in el1btnList){
            btn.setOnClickListener {
                btn.isSelected = ! btn.isSelected
            }
        }
        val el2btnList:MutableList<AppCompatButton> = mutableListOf(binding.el2CsBtn,binding.el2ItBtn,binding.el2CsaiBtn,binding.el2CsbBtn)
        for( btn in el2btnList){
            btn.setOnClickListener {
                btn.isSelected = ! btn.isSelected
            }
        }
        binding.el1CsBtn.setOnClickListener {
            binding.el1CsBtn.isSelected= !binding.el1CsBtn.isSelected
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
                        Toast.makeText(requireContext(),errorMsg,Toast.LENGTH_LONG).show()
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
            if(checks1()) {
                val selBtnList:MutableList<String> = mutableListOf()
                for( btn in el1btnList){
                    if(btn.isSelected){
                        selBtnList.add(btn.text.toString())
                    }
                }

                vm.addElective(
                    ElectiveData(
                        sem,
                        "1",
                        ElectiveDetails(sub1, faculty1),
                        ElectiveDetails(sub2, faculty2),
                        ElectiveDetails(sub3, faculty3),
                        selBtnList,
                        mAuth.currentUser!!.email.toString(),
                        currentDate + "\n" + currentTime,
                    )
                )
            }


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

            if(checks2()) {
                val selBtnList:MutableList<String> = mutableListOf()
                for( btn in el2btnList){
                    if(btn.isSelected){
                        selBtnList.add(btn.text.toString())
                    }
                }
                vm.addElective(
                    ElectiveData(
                        sem,
                        "2",
                        ElectiveDetails(sub1, faculty1),
                        ElectiveDetails(sub2, faculty2),
                        ElectiveDetails(sub3, faculty3),
                        selBtnList,
                        mAuth.currentUser!!.email.toString(),
                        currentDate + "\n" + currentTime,
                    )
                )
            }
        }

        return binding.root
    }

    private fun checks1(): Boolean {
        if(binding.el1sub1Title.text.toString().trim().isEmpty()||binding.el1sub1Title.text.toString().trim().isEmpty()){
            Toast.makeText(requireContext(),"Subject 1 cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        if(!binding.el1CsBtn.isSelected&&!binding.el1ItBtn.isSelected&&!binding.el1CsaiBtn.isSelected&&!binding.el1CsbBtn.isSelected){
            Toast.makeText(requireContext(),"Select atleast 1 branch",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun checks2(): Boolean {
        if(binding.el2sub1Title.text.toString().trim().isEmpty()||binding.el2sub1Title.text.toString().trim().isEmpty()){
            Toast.makeText(requireContext(),"Subject 1 cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        if(!binding.el2CsBtn.isSelected&&!binding.el2ItBtn.isSelected&&!binding.el2CsaiBtn.isSelected&&!binding.el2CsbBtn.isSelected){
            Toast.makeText(requireContext(),"Select atleast 1 branch",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


}