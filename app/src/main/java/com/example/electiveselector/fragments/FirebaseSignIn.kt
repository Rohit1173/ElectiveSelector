package com.example.electiveselector.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.electiveselector.Professor
import com.example.electiveselector.R
import com.example.electiveselector.Student
import com.example.electiveselector.databinding.FragmentFirebaseSignInBinding
import com.example.electiveselector.viewModels.ProfVerifyViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONException
import org.json.JSONObject


class FirebaseSignIn : Fragment() {
    private var _binding: FragmentFirebaseSignInBinding? = null
    private val binding get() = _binding!!
    private val RC_SIGN_IN = 120
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var vm:ProfVerifyViewModel
    lateinit var msg:String
    lateinit var errorMsg:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirebaseSignInBinding.inflate(
            inflater, container, false
        )
        vm = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(ProfVerifyViewModel::class.java)
        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        binding.signIn.setOnClickListener {
            signIn()
        }
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
            }
        }
        return binding.root
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent;
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!, account.displayName.toString(),account.email.toString())
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, name: String,email:String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(email.endsWith("iiitl.ac.in")||email=="sairohitchappa01@gmail.com") {
                        vm.checkProf(email)
                    }
                    else{
                        googleSignInClient.signOut()
                        Toast.makeText(requireContext(), "Try Logging in with Institute ID", Toast.LENGTH_LONG).show()
                    }

                   // Toast.makeText(requireContext(), "Hi $name", Toast.LENGTH_LONG).show()

//                    findNavController().navigate(R.id.action_firebaseSignIn_to_studentFragment)
                    // Sign in success, update UI with the signed-in user's information
                } else {
                    Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_LONG).show()
                    // If sign in fails, display a message to the user.
                }
            }
    }
}