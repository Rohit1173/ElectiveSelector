package com.example.electiveselector

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cloudinary.android.MediaManager
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
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        mAuth=FirebaseAuth.getInstance()
        toggle= ActionBarDrawerToggle(this,binding.drawerLayout,R.string.open,R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        replaceFragment(ProfHome())
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.sideNav.setCheckedItem(R.id.home)
        binding.sideNav.itemIconTintList=null
        val header: View = binding.sideNav.getHeaderView(0)
        val pName:TextView=header.findViewById(R.id.displayName)
        val pEmail:TextView=header.findViewById(R.id.email)
        pName.text=mAuth.currentUser!!.displayName
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
                R.id.sem5 -> {
                    val bundle = Bundle()
                    bundle.putString("key", "5")
                    replaceFragment(ProfSem(),bundle)
                }
                R.id.sem6 -> {
                    val bundle = Bundle()
                    bundle.putString("key", "6")
                    replaceFragment(ProfSem(),bundle)
                }
                R.id.sem7 -> {
                    val bundle = Bundle()
                    bundle.putString("key", "7")
                    replaceFragment(ProfSem(),bundle)
                }
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
    private fun replaceFragment(fragment: Fragment, args: Bundle? = null){
        fragment.arguments = args
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame,fragment)
            commit()
            binding.drawerLayout.closeDrawers()
        }
    }
    var time = 0L
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (time + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(
                this, "Press once again to exit",
                Toast.LENGTH_SHORT
            ).show()
            time = System.currentTimeMillis()
        }
    }
}