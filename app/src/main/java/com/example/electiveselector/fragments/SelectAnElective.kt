package com.example.electiveselector.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.electiveselector.Professor
import com.example.electiveselector.R
import com.example.electiveselector.Student
import com.example.electiveselector.data.chooseSub
import com.example.electiveselector.data.semData
import com.example.electiveselector.data.semwiseElectiveData
import com.example.electiveselector.databinding.FragmentSelectAnElectiveBinding
import com.example.electiveselector.viewModels.SelectAnElectiveViewModel
import com.example.electiveselector.viewModels.signupViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class SelectAnElective : Fragment() {
    private var _binding: FragmentSelectAnElectiveBinding? = null
    private val binding get() = _binding!!
    lateinit var mAuth:FirebaseAuth
    lateinit var email:String
    lateinit var vm:SelectAnElectiveViewModel
    lateinit var msg:String
    lateinit var errorMsg:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSelectAnElectiveBinding.inflate(
            inflater, container, false
        )
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(SelectAnElectiveViewModel::class.java)
        var sem="5"
        mAuth=FirebaseAuth.getInstance()
        email=mAuth.currentUser!!.email.toString()
        val year=email.subSequence(3,7)
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val x= currentYear-year.toString().toInt()
        binding.el1sub1Choose.setOnClickListener {
            vm.chooseASubject(chooseSub(email,binding.currentSem.text.toString().subSequence(4,5).toString(),"1","100"))
            vm.getSemWiseElectiveData(semData(sem,email))
            Toast.makeText(context, "e1s1", Toast.LENGTH_LONG).show()
        }
        binding.el1sub2Choose.setOnClickListener {
            vm.chooseASubject(chooseSub(email,binding.currentSem.text.toString().subSequence(4,5).toString(),"1","010"))
            vm.getSemWiseElectiveData(semData(sem,email))
            Toast.makeText(context, "e1s2", Toast.LENGTH_LONG).show()
        }
        binding.el1sub3Choose.setOnClickListener {
            vm.chooseASubject(chooseSub(email,binding.currentSem.text.toString().subSequence(4,5).toString(),"1","001"))
            vm.getSemWiseElectiveData(semData(sem,email))
            Toast.makeText(context, "e1s3", Toast.LENGTH_LONG).show()
        }
        binding.el2sub1Choose.setOnClickListener {
            vm.chooseASubject(chooseSub(email,binding.currentSem.text.toString().subSequence(4,5).toString(),"2","100"))
            vm.getSemWiseElectiveData(semData(sem,email))
            Toast.makeText(context, "e2s1", Toast.LENGTH_LONG).show()
        }
        binding.el2sub2Choose.setOnClickListener {
            vm.chooseASubject(chooseSub(email,binding.currentSem.text.toString().subSequence(4,5).toString(),"2","010"))
            vm.getSemWiseElectiveData(semData(sem,email))
            Toast.makeText(context, "e2s2", Toast.LENGTH_LONG).show()
        }
        binding.el2sub3Choose.setOnClickListener {
            vm.chooseASubject(chooseSub(email,binding.currentSem.text.toString().subSequence(4,5).toString(),"2","001"))
            vm.getSemWiseElectiveData(semData(sem,email))
            Toast.makeText(context, "e2s3", Toast.LENGTH_LONG).show()
        }
        vm.chooseSubResponse.observe(viewLifecycleOwner){
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    msg = jsonObject.getString("message")
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
        
        vm.selectAnElectiveResponse.observe(viewLifecycleOwner){
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))

                    val messageJsonObject = jsonObject.getJSONObject("message")

                    
                    val e1s1 = messageJsonObject.getJSONObject("e1s1")
                    val e1s2 = messageJsonObject.getJSONObject("e1s2")
                    val e1s3 = messageJsonObject.getJSONObject("e1s3")
                    val e2s1 = messageJsonObject.getJSONObject("e2s1")
                    val e2s2 = messageJsonObject.getJSONObject("e2s2")
                    val e2s3 = messageJsonObject.getJSONObject("e2s3")

                    val scheduledAt1=messageJsonObject.getString("scheduledAt1")
                    val scheduledAt2=messageJsonObject.getString("scheduledAt2")
                    val choiceString1=messageJsonObject.getString("choiceString1")
                    val choiceString2=messageJsonObject.getString("choiceString2")



                    binding.el1sub1SubTitle.text=e1s1.getString("subTitle")
                    binding.el1sub1Faculty.text=e1s1.getString("facultyName")
                    binding.el1sub2SubTitle.text=e1s2.getString("subTitle")
                    binding.el1sub2Faculty.text=e1s2.getString("facultyName")
                    binding.el1sub3SubTitle.text=e1s3.getString("subTitle")
                    binding.el1sub3Faculty.text=e1s3.getString("facultyName")
                    if(choiceString1[0]=='0'||binding.el1sub1SubTitle.text.toString()=="NA"){
                        binding.el1sub1Choose.isEnabled=false
                        binding.el1sub1Choose.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    else if(choiceString1[0]=='1'){
                        binding.el1sub1Choose.isEnabled=false
                        binding.el1sub1Choose.text="SELECTED"
                        binding.el1sub1Choose.setBackgroundColor(Color.parseColor("#12cc82"))
                    }
                    else{
                        binding.el1sub1Choose.isEnabled=true
                        binding.el1sub1Choose.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
                    }
                    if(choiceString1[1]=='0'||binding.el1sub2SubTitle.text.toString()=="NA"){
                        binding.el1sub2Choose.isEnabled=false
                        binding.el1sub2Choose.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    else if(choiceString1[1]=='1'){
                        binding.el1sub2Choose.isEnabled=false
                        binding.el1sub2Choose.text="SELECTED"
                        binding.el1sub2Choose.setBackgroundColor(Color.parseColor("#12cc82"))
                    }
                    else{
                        binding.el1sub2Choose.isEnabled=true
                        binding.el1sub2Choose.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
                    }
                    if(choiceString1[2]=='0'||binding.el1sub3SubTitle.text.toString()=="NA"){
                        binding.el1sub3Choose.isEnabled=false
                        binding.el1sub3Choose.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    else if(choiceString1[2]=='1'){
                        binding.el1sub3Choose.isEnabled=false
                        binding.el1sub3Choose.text="SELECTED"
                        binding.el1sub3Choose.setBackgroundColor(Color.parseColor("#12cc82"))
                    }
                    else{
                        binding.el1sub3Choose.isEnabled=true
                        binding.el1sub3Choose.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
                    }
                    binding.scheduledAt1.text=scheduledAt1

                    binding.el2sub1SubTitle.text=e2s1.getString("subTitle")
                    binding.el2sub1Faculty.text=e2s1.getString("facultyName")
                    binding.el2sub2SubTitle.text=e2s2.getString("subTitle")
                    binding.el2sub2Faculty.text=e2s2.getString("facultyName")
                    binding.el2sub3SubTitle.text=e2s3.getString("subTitle")
                    binding.el2sub3Faculty.text=e2s3.getString("facultyName")
                    binding.scheduledAt2.text=scheduledAt2
                    if(choiceString2[0]=='0'||binding.el2sub1SubTitle.text.toString()=="NA"){
                        binding.el2sub1Choose.isEnabled=false
                        binding.el2sub1Choose.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    else if(choiceString2[0]=='1'){
                        binding.el2sub1Choose.text="SELECTED"
                        binding.el2sub1Choose.isEnabled=false
                        binding.el2sub1Choose.setBackgroundColor(Color.parseColor("#12cc82"))
                    }
                    else{
                        binding.el2sub1Choose.isEnabled=true
                        binding.el2sub1Choose.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
                    }
                    if(choiceString2[1]=='0'||binding.el2sub2SubTitle.text.toString()=="NA"){
                        binding.el2sub2Choose.isEnabled=false
                        binding.el2sub2Choose.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    else if(choiceString2[1]=='1'){
                        binding.el2sub2Choose.isEnabled=false
                        binding.el2sub2Choose.text="SELECTED"
                        binding.el2sub2Choose.setBackgroundColor(Color.parseColor("#12cc82"))
                    }
                    else{
                        binding.el2sub2Choose.isEnabled=true
                        binding.el2sub2Choose.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
                    }
                    if(choiceString2[2]=='0'||binding.el2sub3SubTitle.text.toString()=="NA"){
                        binding.el2sub3Choose.isEnabled=false
                        binding.el2sub3Choose.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    else if(choiceString2[2]=='1'){
                        binding.el2sub3Choose.isEnabled=false
                        binding.el2sub3Choose.text="SELECTED"
                        binding.el2sub3Choose.setBackgroundColor(Color.parseColor("#12cc82"))

                    }
                    else{
                        binding.el2sub3Choose.isEnabled=true
                        binding.el2sub3Choose.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.purple_500))
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

        if(x==2&&(currentMonth in 6..11)){
            binding.currentSem.setText("SEM 5")
            vm.getSemWiseElectiveData(semData("5",email))
        }
        else if(x==3){
            if(currentMonth in 6..11){
                sem="7"
                binding.currentSem.setText("SEM 7")
                vm.getSemWiseElectiveData(semData("7",email))
            }
            else{
                sem="6"
                binding.currentSem.setText("SEM 6")
                vm.getSemWiseElectiveData(semData("6",email))
            }
        }
        return binding.root
    }

}