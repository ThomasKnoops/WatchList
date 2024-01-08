package org.thoteman.watchlist.authentication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.thoteman.watchlist.MainActivity
import org.thoteman.watchlist.R


class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.editTextEmailR)
        val passwordEditText: EditText = findViewById(R.id.editTextPasswordR)
        val confirmPasswordEditText: EditText = findViewById(R.id.editTextPasswordConfirmationR)
        val signUpButton: Button = findViewById(R.id.buttonSignUpR)
        val backButton: Button = findViewById(R.id.buttonBack)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                // If any of the fields are empty, display a message to the user
                showAlert(getString(R.string.empty_fields),
                    getString(R.string.please_fill_in_all_the_fields))
            } else if (!isValidEmail(email)) {
                // If the email is not in a valid format, display a message to the user
                showAlert(getString(R.string.invalid_email),
                    getString(R.string.please_enter_a_valid_email_address))
            } else if (password.length < 6) {
                // If the password is less than 6 characters, display a message to the user
                showAlert(getString(R.string.invalid_password),
                    getString(R.string.please_enter_a_password_with_at_least_6_characters))
            }else if (password != confirmPassword) {
                // If the passwords don't match, display a message to the user
                showAlert(getString(R.string.password_mismatch),
                    getString(R.string.the_entered_passwords_do_not_match_please_try_again))
            } else {
                // Call the function to sign up the user
                signUpUser(email, password)
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up successful, navigate to the main activity or any desired screen
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign up fails, display a message to the user.
                    // You can handle specific sign-up failure cases here.
                    showAlert(getString(R.string.login_failed), getString(R.string.try_again))
                }
            }
    }

    private fun showAlert(title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(R.string.ok) { dialogInterface: DialogInterface, _: Int ->
            // Dismiss the alert dialog if the user clicks OK
            dialogInterface.dismiss()
        }
        alertDialogBuilder.show()
    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
