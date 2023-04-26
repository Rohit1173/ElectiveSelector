package com.example.electiveselector.fragments

import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.R
import com.example.electiveselector.data.SemAndBranchList
import com.example.electiveselector.data.chooseSub
import com.example.electiveselector.data.semData
import com.example.electiveselector.databinding.FragmentSelectAnElectiveBinding
import com.example.electiveselector.viewModels.SelectAnElectiveViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import org.json.JSONArray
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
    lateinit var userName:String
    lateinit var el1branchList:MutableList<String>
    lateinit var el2branchList:MutableList<String>
    lateinit var e1s1Pdf:String
    lateinit var e1s2Pdf:String
    lateinit var e1s3Pdf:String
    lateinit var e2s1Pdf:String
    lateinit var e2s2Pdf:String
    lateinit var e2s3Pdf:String
    var sem="5"
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

        mAuth=FirebaseAuth.getInstance()
        email=mAuth.currentUser!!.email.toString()
        userName=mAuth.currentUser!!.displayName.toString()
        el1branchList= mutableListOf()
        el2branchList= mutableListOf()
        val year=email.subSequence(3,7)
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val x= currentYear-year.toString().toInt()
        val resList = mutableListOf(binding.el1sub1Resource,binding.el1sub2Resource,binding.el1sub3Resource,binding.el2sub1Resource,binding.el2sub2Resource,binding.el2sub3Resource)
        for( i in resList){
            i.setOnClickListener {
                var myUrl=""
                var myTitle=""
                when(i){
                    binding.el1sub1Resource ->{
                        myUrl=e1s1Pdf
                        myTitle="Semester_${sem}_Elective_1_${binding.el1sub1SubTitle}"
                    }
                    binding.el1sub2Resource ->{
                        myUrl=e1s2Pdf
                        myTitle="Semester_${sem}_Elective_1_${binding.el1sub2SubTitle}"
                    }
                    binding.el1sub3Resource ->{
                        myUrl=e1s3Pdf
                        myTitle="Semester_${sem}_Elective_1_${binding.el1sub3SubTitle}"
                    }
                    binding.el2sub1Resource ->{
                        myUrl=e2s1Pdf
                        myTitle="Semester_${sem}_Elective_2_${binding.el2sub1SubTitle}"
                    }
                    binding.el2sub2Resource ->{
                        myUrl=e2s2Pdf
                        myTitle="Semester_${sem}_Elective_2_${binding.el2sub2SubTitle}"
                    }
                    binding.el2sub3Resource ->{
                        myUrl=e2s3Pdf
                        myTitle="Semester_${sem}_Elective_2_${binding.el2sub3SubTitle}"
                    }
                }
                val request = DownloadManager.Request(Uri.parse(myUrl))
                request.setTitle(myTitle)
                request.setDescription("Downloading file...")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${myTitle}.pdf")

                val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)
            }
        }
        binding.el1sub1Choose.setOnClickListener {
            showDialog(binding.el1sub1SubTitle.text.toString(),binding.el1sub1Faculty.text.toString(),"1","100",el1branchList)
        }
        binding.el1sub2Choose.setOnClickListener {
            showDialog(binding.el1sub2SubTitle.text.toString(),binding.el1sub2Faculty.text.toString(),"1","010",el1branchList)
        }
        binding.el1sub3Choose.setOnClickListener {
            showDialog(binding.el1sub3SubTitle.text.toString(),binding.el1sub3Faculty.text.toString(),"1","001",el1branchList)
        }
        binding.el2sub1Choose.setOnClickListener {
            showDialog(binding.el2sub1SubTitle.text.toString(),binding.el2sub1Faculty.text.toString(),"2","100",el2branchList)
        }
        binding.el2sub2Choose.setOnClickListener {
            showDialog(binding.el2sub2SubTitle.text.toString(),binding.el2sub2Faculty.text.toString(),"2","010",el2branchList)
        }
        binding.el2sub3Choose.setOnClickListener {
            showDialog(binding.el2sub3SubTitle.text.toString(),binding.el2sub3Faculty.text.toString(),"2","001",el2branchList)
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
                    val choiceString1=messageJsonObject.getString("choiceString1")
                    val choiceString2=messageJsonObject.getString("choiceString2")

                    el1branchList=jsonArrayToMutableList(messageJsonObject.getJSONArray("el1branchList"))
                    el2branchList=jsonArrayToMutableList(messageJsonObject.getJSONArray("el2branchList"))



                    binding.el1sub1SubTitle.text=e1s1.getString("subTitle")
                    binding.el1sub1Faculty.text=e1s1.getString("facultyName")
                    e1s1Pdf=e1s1.getString("pdfUrl")
                    if(e1s1Pdf=="NA"){
                        binding.el1sub1Resource.isEnabled=false
                        binding.el1sub1Resource.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    binding.el1sub2SubTitle.text=e1s2.getString("subTitle")
                    binding.el1sub2Faculty.text=e1s2.getString("facultyName")
                    e1s2Pdf=e1s2.getString("pdfUrl")
                    if(e1s2Pdf=="NA"){
                        binding.el1sub2Resource.isEnabled=false
                        binding.el1sub2Resource.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    binding.el1sub3SubTitle.text=e1s3.getString("subTitle")
                    binding.el1sub3Faculty.text=e1s3.getString("facultyName")
                    e1s3Pdf=e1s3.getString("pdfUrl")
                    if(e1s3Pdf=="NA"){
                        binding.el1sub3Resource.isEnabled=false
                        binding.el1sub3Resource.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
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
                    binding.el2sub1SubTitle.text=e2s1.getString("subTitle")
                    binding.el2sub1Faculty.text=e2s1.getString("facultyName")
                    e2s1Pdf=e2s1.getString("pdfUrl")
                    if(e2s1Pdf=="NA"){
                        binding.el2sub1Resource.isEnabled=false
                        binding.el2sub1Resource.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    binding.el2sub2SubTitle.text=e2s2.getString("subTitle")
                    binding.el2sub2Faculty.text=e2s2.getString("facultyName")
                    e2s2Pdf=e2s2.getString("pdfUrl")
                    if(e2s2Pdf=="NA"){
                        binding.el2sub2Resource.isEnabled=false
                        binding.el2sub2Resource.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
                    binding.el2sub3SubTitle.text=e2s3.getString("subTitle")
                    binding.el2sub3Faculty.text=e2s3.getString("facultyName")
                    e2s3Pdf=e2s3.getString("pdfUrl")
                    if(e2s3Pdf=="NA"){
                        binding.el2sub3Resource.isEnabled=false
                        binding.el2sub3Resource.setBackgroundColor(Color.parseColor("#3e4142"))
                    }
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

    private fun jsonArrayToMutableList(jsonArray: JSONArray): MutableList<String> {
        val mutableList = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            mutableList.add(jsonArray[i] as String)
        }
        return mutableList
    }

    private fun showDialog(subTitle:String, faculty:String,electiveNum:String,choiceString: String,branchList: MutableList<String>){
        val message = "You've selected $subTitle taught by $faculty. Are you sure you want to proceed with this? \n \n Note: Elective cannot be changed. Please choose carefully before proceeding."
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogText:TextView = dialog.findViewById(R.id.dialogText)
        val btnYes:TextView = dialog.findViewById(R.id.btnYes)
        val btnNo:TextView = dialog.findViewById(R.id.btnNo)
        dialogText.text=message
        btnYes.setOnClickListener {
            vm.chooseASubject(chooseSub(email,userName,binding.currentSem.text.toString().subSequence(4,5).toString(),electiveNum,choiceString,branchList))
            Toast.makeText(context,"You have selected $subTitle as your elective", Toast.LENGTH_LONG).show()
            vm.getSemWiseElectiveData(semData(sem,email))
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}