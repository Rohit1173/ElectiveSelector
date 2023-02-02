package com.example.electiveselector

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.electiveselector.databinding.ActivityProfessorBinding
import com.example.electiveselector.fragments.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class Professor : AppCompatActivity() {
    lateinit var binding: ActivityProfessorBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var mAuth:FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfessorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        mAuth=FirebaseAuth.getInstance()
        toggle= ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        replaceFragment(ProfHome())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.sideNav.setCheckedItem(R.id.home)
        binding.sideNav.itemIconTintList=null
        val header: View = binding.sideNav.getHeaderView(0)
        val pEmail:TextView=header.findViewById(R.id.email)
        pEmail.text=mAuth.currentUser!!.email
        val type:TextView=header.findViewById(R.id.type)
        type.text="PROFESSOR"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.sideNav.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.home -> replaceFragment(ProfHome())
                R.id.sem5 -> replaceFragment(ProfSem5())
                R.id.sem6 -> replaceFragment(ProfSem6())
                R.id.sem7 -> replaceFragment(ProfSem7())
            }
            true
        }
        binding.profLogout.setOnClickListener {
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
            replace(R.id.frame,fragment)
            commit()
            binding.drawerLayout.closeDrawers()
        }
    }
}