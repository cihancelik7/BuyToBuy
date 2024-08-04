package com.example.buytobuy.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buytobuy.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : BaseActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("users")

        setupUserProfile()
        setupListeners()

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@EditProfileActivity, ProfileActivity::class.java))
        }
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            val name = binding.editNameEditText.text.toString().trim()
            val currentPassword = binding.editPasswordCurrentEditText.text.toString().trim()
            val newPassword = binding.editPasswordNewEditText.text.toString().trim()

            updateProfile(name, currentPassword, newPassword)
        }
    }

    private fun updateProfile(name: String, currentPassword: String, newPassword: String) {
        val user = auth.currentUser
        user?.let {
            if (currentPassword.isEmpty()) {
                Toast.makeText(this, "Current Password cannot be empty", Toast.LENGTH_SHORT).show()
                return
            }
            val credential = EmailAuthProvider.getCredential(it.email!!, currentPassword)
            it.reauthenticate(credential).addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    // Şifre güncellemesi
                    if (newPassword.isNotEmpty()) {
                        it.updatePassword(newPassword).addOnCompleteListener { updatePasswordTask ->
                            if (updatePasswordTask.isSuccessful) {
                                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    // İsim güncellemesi
                    dbRef.child(it.uid).child("name").setValue(name)
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupUserProfile() {
        val user = auth.currentUser
        user?.let {
            dbRef.child(it.uid).get().addOnSuccessListener { snapshot ->
                val name = snapshot.child("name").value.toString()
                binding.editNameEditText.setText(name)
            }
            binding.editEmailEditText.apply {
                setText(it.email)
                isEnabled = false
            }
        }
    }

}