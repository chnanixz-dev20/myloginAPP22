package com.example.myloginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group

class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
        private const val EXTRA_PASSWORD = "extra_password"
        private const val HARDCODED_USERNAME = "admin"
        private const val HARDCODED_PASSWORD = "admin"
    }

    private lateinit var loginFormGroup: Group
    private lateinit var resultTextView: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginFormGroup = findViewById(R.id.loginFormGroup)
        resultTextView = findViewById(R.id.resultTextView)
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send an intent to ourselves (singleTop) carrying the entered values
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_PASSWORD, password)
            }
            startActivity(intent)
        }

        // Handle the case where the Activity is first created with credentials already in the intent
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val username = intent?.getStringExtra(EXTRA_USERNAME)
        val password = intent?.getStringExtra(EXTRA_PASSWORD)

        if (username != null && password != null) {
            if (username == HARDCODED_USERNAME && password == HARDCODED_PASSWORD) {
                showWelcomeMessage(username)
            } else {
                showErrorAndReturnToForm()
            }
        }
    }

    private fun showWelcomeMessage(username: String) {
        loginFormGroup.visibility = android.view.View.GONE
        resultTextView.visibility = android.view.View.VISIBLE
        resultTextView.text = "Welcome, $username!"
    }

    private fun showErrorAndReturnToForm() {
        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        loginFormGroup.visibility = android.view.View.VISIBLE
        resultTextView.visibility = android.view.View.GONE
        passwordEditText.text.clear()
    }
}