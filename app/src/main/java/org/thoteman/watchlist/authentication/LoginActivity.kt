package org.thoteman.watchlist.authentication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.thoteman.watchlist.MainActivity
import org.thoteman.watchlist.R

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.editTextEmailL)
        val passwordEditText: EditText = findViewById(R.id.editTextPasswordL)
        val loginButton: Button = findViewById(R.id.buttonLoginL)
        val signUpButton: Button = findViewById(R.id.buttonSignupL)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Call the function to log in the user
            loginUser(email, password)
        }

        signUpButton.setOnClickListener {
            // When the Sign Up button is clicked, navigate to SignUpActivity
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Log in successful, navigate to the main activity or any desired screen
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    // You can handle specific login failure cases here.
                    showAlert()
                }
            }
    }

    private fun showAlert() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.login_failed)
        alertDialogBuilder.setMessage(R.string.try_again)
        alertDialogBuilder.setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
            // Dismiss the alert dialog if the user clicks OK
            dialogInterface.dismiss()
        }
        alertDialogBuilder.show()
    }
}