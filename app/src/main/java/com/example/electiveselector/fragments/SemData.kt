package com.example.electiveselector.fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.R
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.electiveselector.adapters.SemDataAdapter
import com.example.electiveselector.data.*
import com.example.electiveselector.databinding.FragmentSemDataBinding
import com.example.electiveselector.viewModels.SemDataViewModel
import com.google.gson.Gson
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellUtil.createCell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class SemData : Fragment() {
    private var _binding: FragmentSemDataBinding? = null
    private val binding get() = _binding!!
    lateinit var vm: SemDataViewModel
    lateinit var errorMsg: String
    lateinit var sem:String
    lateinit var el:String
    lateinit var branchList: MutableList<String>
    lateinit var sub1Title: String
    lateinit var sub2Title: String
    lateinit var sub3Title: String
    lateinit var sub1Faculty: String
    lateinit var sub2Faculty: String
    lateinit var sub3Faculty: String
    lateinit var branchStr:String
    lateinit var studentList:MutableList<StudentsData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSemDataBinding.inflate(
            inflater, container, false
        )

        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(SemDataViewModel::class.java)
         studentList = mutableListOf()
        vm.semDataResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    val messageArray = jsonObject.getJSONArray("message")
                    binding.semDataRecycler.adapter = SemDataAdapter(messageArray)
                    studentList = convertToMutableList(messageArray)

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
         sem = arguments?.getString("sem").toString()
         el = arguments?.getString("el").toString()
        binding.semHeading.text = "Semester - ${sem} \n Elective - $el"
        vm.getBranchesData(SemAndElectiveData(sem, el))


        vm.branchesResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    val messageArray = jsonObject.getJSONArray("message")

                    val list: MutableList<String> = mutableListOf()
                    for (i in 0 until messageArray.length()) {
                        val subArray = messageArray.getJSONArray(i)
                        var group = ""
                        for (j in 0 until subArray.length()) {
                            val branch = subArray.getString(j)
                            group += branch
                            if (j != subArray.length() - 1) {
                                group += " | "
                            }

                        }
                        list.add(group)
                    }

                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        list
                    )
                    binding.branches.adapter = adapter


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


        vm.subjectsResponse.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
                try {
                    val jsonObject = JSONObject(Gson().toJson(it.body()))
                    val messageObject = jsonObject.getJSONObject("message")
                    val sub1 = messageObject.getJSONObject("sub1")
                    val sub2 = messageObject.getJSONObject("sub2")
                    val sub3 = messageObject.getJSONObject("sub3")
                    sub1Title = sub1.getString("subTitle")
                    sub2Title = sub2.getString("subTitle")
                    sub3Title = sub3.getString("subTitle")
                    sub1Faculty = sub1.getString("facultyName")
                    sub2Faculty = sub2.getString("facultyName")
                    sub3Faculty = sub3.getString("facultyName")
                    val list: MutableList<String> = mutableListOf(sub1Title, sub2Title, sub3Title)
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        list
                    )
                    binding.subjects.adapter = adapter


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
        binding.subjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                when (position) {
                    0 -> {
                        vm.getSemData(
                            filterSemData(
                                sem,
                                el,
                                ElectiveDetails(sub1Title, sub1Faculty),
                                branchList
                            )
                        )
                    }
                    1 -> {
                        vm.getSemData(
                            filterSemData(
                                sem,
                                el,
                                ElectiveDetails(sub2Title, sub2Faculty),
                                branchList
                            )
                        )
                    }
                    2 -> {
                        vm.getSemData(
                            filterSemData(
                                sem,
                                el,
                                ElectiveDetails(sub3Title, sub3Faculty),
                                branchList
                            )
                        )
                    }
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.branches.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 branchStr = adapterView!!.getItemAtPosition(position).toString()
                branchList = branchStr.split(" | ").toMutableList()
                vm.getSubjectsData(SemAndBranchList(sem, el, branchList))

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        binding.exportCsv.setOnClickListener {
            TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .check();



            val ourWB = createWorkbook()
            createExcelFile(ourWB)
        }

        return binding.root
    }

    private fun convertToMutableList(messageArray: JSONArray): MutableList<StudentsData> {
        val list: MutableList<StudentsData> = mutableListOf()
        for (i in 0 until messageArray.length()) {
            val item = messageArray.getJSONObject(i)
            val userName = item.getString("userName")
            val userEmail = item.getString("userEmail")
            val sub = item.getJSONObject("sub")
            val title = sub.getString("subTitle")
            val faculty = sub.getString("facultyName")
            val semNum = item.getString("semNum")
            val electiveNum = item.getString("electiveNum")
//            val branchList =item.getJSONObject()
            val b = mutableListOf<String>()
            list.add(
                StudentsData(
                    userName,
                    userEmail,
                    ElectiveDetails(title, faculty),
                    semNum,
                    electiveNum,
                    b
                )
            )
        }
        return list
    }

    private fun createWorkbook(): Workbook {
        val ourWorkbook = XSSFWorkbook()
        val sheet: Sheet = ourWorkbook.createSheet("Semester - $sem")
        val boldFont = ourWorkbook.createFont()
        boldFont.bold = true
        val boldStyle = ourWorkbook.createCellStyle()
        boldStyle.setFont(boldFont)
        addData(sheet,boldStyle)

        return ourWorkbook
    }
    private fun addData(sheet: Sheet, boldStyle: XSSFCellStyle) {


        val row0 = sheet.createRow(0)
        createCell(row0,0, "Semester - $sem",boldStyle)
        createCell(row0,1, "Elective - $el",boldStyle)
        createCell(row0,2, branchStr,boldStyle)
        val row=sheet.createRow(1)
        createCell(row,0,"UserName",boldStyle)
        createCell(row,1,"UserEmail",boldStyle)
        createCell(row,2,"Subject",boldStyle)
        createCell(row,3,"Faculty",boldStyle)


        for (i in studentList.indices){
            val item=studentList[i]
            val row1 = sheet.createRow(i+2)
            createCell(row1,0,item.userName)
            createCell(row1,1,item.userEmail)
            createCell(row1,2,item.sub.subTitle)
            createCell(row1,3,item.sub.facultyName)
        }
    }
    private fun createExcelFile(ourWorkbook: Workbook) {

        val ourAppFileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (ourAppFileDirectory != null && !ourAppFileDirectory.exists()) {
            ourAppFileDirectory.mkdirs()
        }
        val subTitle = binding.subjects.getItemAtPosition(binding.subjects.selectedItemPosition)
        val bStr= branchStr.replace(" | ","_")
        val title ="Semester_"+sem+"_Elective_"+el+"_"+subTitle+"_"+bStr+".xlsx"
        val excelFile = File(ourAppFileDirectory, title)
        try {
            val fileOut = FileOutputStream(excelFile)
            ourWorkbook.write(fileOut)
            fileOut.close()
            val uri = FileProvider.getUriForFile(requireContext(), context?.packageName + ".fileprovider", excelFile)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // set MIME type to XLSX
            intent.putExtra(Intent.EXTRA_STREAM, uri) // attach the file to the intent

            startActivity(Intent.createChooser(intent, "Send XLSX file"))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: List<String?>) {
            Toast.makeText(
                requireContext(),
                "Permission Denied\n$deniedPermissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}