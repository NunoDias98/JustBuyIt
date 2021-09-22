package com.example.cme

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btLogin.setOnClickListener{
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etEmail.error = "Insira um email válido"
                etEmail.requestFocus()

            }else{
                signIn(email,password)
            }
        }
    }

    private fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            when{
                it.isSuccessful -> {
                    Toast.makeText(this,"Login efetuado com sucesso", Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else ->{
                    Toast.makeText(this,"Falha no login", Toast.LENGTH_LONG).show()
                }
            }
            clearInputs()
        }
    }

    private fun clearInputs(){
        etEmail.text.clear()
        etPassword.text.clear()
    }

    fun Registar(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    fun Esqueceu(view: View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Esqueceu password")
        val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
        val email = view.findViewById<EditText>(R.id.etEmail)
        builder.setView(view)
        builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
            forgotPassword(email)
        })
        builder.setNegativeButton("Close", DialogInterface.OnClickListener { _, _ ->  })
        builder.show()
    }

    private fun forgotPassword(email : EditText){
        if(email.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            return
        }

        auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener{
                task ->
            if(task.isSuccessful){
                Toast.makeText(this,"Email enviado", Toast.LENGTH_LONG).show()
            }
        }
    }
}