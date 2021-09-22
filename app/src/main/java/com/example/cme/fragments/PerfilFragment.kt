package com.example.cme.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cme.LoginActivity
import com.example.cme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class PerfilFragment : Fragment() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val dbFirestore : CollectionReference = FirebaseFirestore.getInstance().collection("User")


    lateinit var email : TextView

    lateinit var logout : Button
    lateinit var editar : Button
    lateinit var submeter : Button

    lateinit var nome : EditText
    lateinit var passNova : EditText
    lateinit var passCNova : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        email = view.findViewById(R.id.tvEmail)
        nome = view.findViewById(R.id.etNome)
        nome.isEnabled = false
        logout = view.findViewById(R.id.btLogout)
        editar = view.findViewById(R.id.btEditar)
        submeter = view.findViewById(R.id.btSubmeter)
        passNova = view.findViewById(R.id.etPasswordNova)
        passCNova = view.findViewById(R.id.etCPasswordNova)

        val currentUser = auth.currentUser?.email

        email.text = currentUser

        val docRef = dbFirestore.document(auth.uid.toString())
        docRef.get().addOnSuccessListener {document ->
            if(document != null){
                nome.setText(document.data?.get("nome").toString())
            }else{
                Toast.makeText(activity,"Falha a carregar o documento", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener{ execption ->
            Toast.makeText(activity,execption.toString(), Toast.LENGTH_LONG).show()
        }


        editar.setOnClickListener{
            nome.isEnabled = true
            editar.visibility = View.GONE
            submeter.visibility = View.VISIBLE

            passNova.visibility = View.VISIBLE
            passCNova.visibility = View.VISIBLE
        }

        submeter.setOnClickListener{
            if (nome.text.isNotEmpty()) {


                val valoresEditados = hashMapOf(
                    "nome" to nome.text.toString()
                )

                val uid = auth.uid.toString()


                val novaPW = passNova.text.toString()
                val cNovaPW = passCNova.text.toString()
                if(novaPW.isNotEmpty()){
                    if(novaPW != cNovaPW){
                        Toast.makeText(activity,"Passwords não são iguais",Toast.LENGTH_LONG).show()
                    }else{
                        auth.currentUser?.updatePassword(novaPW)?.addOnCompleteListener{
                            if(it.isSuccessful){
                                //Toast.makeText(activity,"Password alterada",Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(activity,"Erro a alterar password",Toast.LENGTH_LONG).show()
                            }
                        }
                        dbFirestore.document(uid).set(valoresEditados).addOnCompleteListener {
                            when {
                                it.isSuccessful -> {
                                    Toast.makeText(activity,"Alterações efetuadas com sucesso",Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                }else{
                    dbFirestore.document(uid).set(valoresEditados).addOnCompleteListener {
                        when {
                            it.isSuccessful -> {
                                Toast.makeText(activity,"Alterações efetuadas com sucesso",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            //manda para o perfil
            val fragment = PerfilFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragment, "Dados Alterados")
                .addToBackStack(null)
                .commit()
        }


        logout.setOnClickListener{
            auth.signOut()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {

        fun newInstance() : PerfilFragment = PerfilFragment()


    }
}