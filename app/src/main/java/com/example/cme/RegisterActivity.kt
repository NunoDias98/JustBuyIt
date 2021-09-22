package com.example.cme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbFirestore : CollectionReference = FirebaseFirestore.getInstance().collection("User")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btRegister.setOnClickListener{

            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            var aux = true

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etEmail.error = "Insira um email válido"
                etEmail.requestFocus()
                aux = false
            }


            if (nome.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && aux == true) {
                registerUser(nome,email, password)
            }else{
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                if(nome.isEmpty()){
                    etNome.error = "Preencha o nome"
                    etNome.requestFocus()

                }
                if(password.isEmpty()){
                    etPassword.error = "Preencha a password"
                    etPassword.requestFocus()
                }
            }
        }
    }

    private fun registerUser(nome: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            when{
                it.isSuccessful -> {
                    Toast.makeText(this,"Utilizador registado", Toast.LENGTH_LONG).show()

                    val user = HashMap<String, Any>()
                    user["nome"] = nome
                    user["email"] = email

                    val uid = auth.uid.toString()
                    //Toast.makeText(this,uid, Toast.LENGTH_LONG).show()
                    //Toast.makeText(this,nome, Toast.LENGTH_LONG).show()
                    //Toast.makeText(this,email, Toast.LENGTH_LONG).show()

                    dbFirestore.document(uid).set(user).addOnCompleteListener {
                        when {
                            it.isSuccessful -> {
                                auth.signOut()
                            }
                        }
                    }

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                }
                else ->{
                    Toast.makeText(this,"Falha a registar o utilizador", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}