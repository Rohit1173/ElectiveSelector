package com.example.electiveselector

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.electiveselector.databinding.ActivityStudentBinding
import com.example.electiveselector.fragments.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class Student : AppCompatActivity() {
    lateinit var binding: ActivityStudentBinding
    lateinit var mAuth:FirebaseAuth
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth=FirebaseAuth.getInstance()
        toggle= ActionBarDrawerToggle(this,binding.studentDrawer,R.string.open,R.string.close)
        binding.studentDrawer.addDrawerListener(toggle)
        toggle.syncState()
        replaceFragment(StudentHome())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.studentSideNav.setCheckedItem(R.id.home)
        binding.studentSideNav.itemIconTintList=null
        val header: View = binding.studentSideNav.getHeaderView(0)
        val sEmail:TextView=header.findViewById(R.id.email)
        val type: TextView =header.findViewById(R.id.type)
        sEmail.text=mAuth.currentUser!!.email
        type.text="STUDENT"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.studentSideNav.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.home -> replaceFragment(StudentHome())
                R.id.selectAnElective -> replaceFragment(SelectAnElective())
            }
            true
        }
        binding.studentLogout.setOnClickListener {
            mAuth.signOut()
            googleSignInClient.signOut()
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.studentFrame,fragment)
            commit()
            binding.studentDrawer.closeDrawers()
        }
    }
}