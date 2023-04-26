package com.example.electiveselector.fragments

import android.app.Activity
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.data.ElectiveData
import com.example.electiveselector.data.PdfIncludedData
import com.example.electiveselector.databinding.FragmentAddAnElectiveBinding
import com.example.electiveselector.viewModels.AddAnElectiveViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
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
     private var filepath: Uri? = null
    private val REQUEST_CODE_PICK_PDF=123
    var el1sub1Pdf:String = "NA"
    var el1sub2Pdf:String = "NA"
    var el1sub3Pdf:String = "NA"
    var el2sub1Pdf:String = "NA"
    var el2sub2Pdf:String = "NA"
    var el2sub3Pdf:String = "NA"
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
                        PdfIncludedData(sub1, faculty1,el1sub1Pdf),
                        PdfIncludedData(sub2, faculty2,el1sub2Pdf),
                        PdfIncludedData(sub3, faculty3,el1sub3Pdf),
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
                        PdfIncludedData(sub1, faculty1,el2sub1Pdf),
                        PdfIncludedData(sub2, faculty2,el2sub2Pdf),
                        PdfIncludedData(sub3, faculty3,el2sub3Pdf),
                        selBtnList,
                        mAuth.currentUser!!.email.toString(),
                        currentDate + "\n" + currentTime,
                    )
                )
            }
        }
        val resources = mutableListOf(binding.el1sub1AddResource,binding.el1sub2AddResource,binding.el1sub3AddResource,binding.el2sub1AddResource,binding.el2sub2AddResource,binding.el2sub3AddResource)
        for( i in resources){
            i.setOnClickListener {
                when(i){
                     binding.el1sub1AddResource ->{
                         pickPdf(sem,"1",binding.el1sub1Title.text.toString(),binding.el1sub1AddResource)
                     }
                    binding.el1sub2AddResource ->{
                        pickPdf(sem,"1",binding.el1sub2Title.text.toString(),binding.el1sub2AddResource)
                    }
                    binding.el1sub3AddResource ->{
                        pickPdf(sem,"1",binding.el1sub3Title.text.toString(),binding.el1sub3AddResource)
                    }
                    binding.el2sub1AddResource ->{
                        pickPdf(sem,"2",binding.el2sub1Title.text.toString(),binding.el2sub1AddResource)
                    }
                    binding.el2sub2AddResource ->{
                        pickPdf(sem,"2",binding.el2sub2Title.text.toString(),binding.el2sub2AddResource)
                    }
                    binding.el2sub3AddResource ->{
                        pickPdf(sem,"2",binding.el2sub3Title.text.toString(),binding.el2sub3AddResource)
                    }

                }
            }
        }


        return binding.root
    }

    private fun pickPdf(sem: String, el: String, subTitle: String,pdf:EditText) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        startActivityForResult(intent, REQUEST_CODE_PICK_PDF)
        uploadPdf(sem,el,subTitle,pdf)
    }

    private fun uploadPdf(sem: String, el: String, subTitle: String,pdf:EditText) {
        if(filepath!=null) {
            val pd = ProgressDialog(requireContext())
            pd.setTitle("Uploading")
            pd.show()
            val pdfRef = FirebaseStorage.getInstance().reference.child("resources/Semester_${sem}_Elective_${el}_${subTitle}.pdf")
            pdfRef.putFile(filepath!!)
                .addOnSuccessListener {
                    pd.dismiss()
                    Toast.makeText(requireContext(), "Uploaded", Toast.LENGTH_LONG).show()
                    pdf.setText(pdfRef.name)
                    pdfRef.downloadUrl.addOnSuccessListener { url ->
                        when(pdf){
                            binding.el1sub1AddResource ->{
                                el1sub1Pdf= url.toString()
                            }
                            binding.el1sub2AddResource ->{
                                el1sub2Pdf= url.toString()
                            }
                            binding.el1sub3AddResource ->{
                                el1sub3Pdf= url.toString()
                            }
                            binding.el2sub1AddResource ->{
                                el2sub1Pdf= url.toString()
                            }
                            binding.el2sub2AddResource ->{
                                el2sub2Pdf= url.toString()
                            }
                            binding.el2sub3AddResource ->{
                                el2sub3Pdf= url.toString()
                            }

                        }
                    }
                    filepath=null
                }
                .addOnFailureListener {
                    pd.dismiss()
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }
                .addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}%")
                }
        }
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
        if(binding.el1sub1AddResource.text.toString().trim()=="NA"){
            Toast.makeText(requireContext(),"Subject 1 Pdf cannot be empty",Toast.LENGTH_LONG).show()
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
        if(binding.el2sub1AddResource.text.toString().trim()=="NA"){
            Toast.makeText(requireContext(),"Subject 1 Pdf cannot be empty",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_PDF && resultCode == Activity.RESULT_OK) {
             filepath = data?.data!!

//            Toast.makeText(requireContext(),uri.toString(),Toast.LENGTH_LONG).show()
            // Do something with the URI
        }
    }


}