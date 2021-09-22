package com.example.cme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cme.CarrinhoAdapter
import com.example.cme.Item
import com.example.cme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_promocoes.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class CarrinhoFragment : Fragment() {

    lateinit var noItem : TextView
    lateinit var amount : TextView
    lateinit var pagar : Button
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val uid = auth.uid.toString()
    val T:MutableLiveData<Double> = MutableLiveData(0.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_carrinho, container, false)
        noItem = view.findViewById(R.id.tvNoItem)
        amount = view.findViewById(R.id.tvTotal)
        pagar = view.findViewById(R.id.btCheckout)
        val timer = Timer()

        T.observe(requireActivity(), Observer {
            amount.text = it.toString() + "€"
        })

        getData {
            if (it.size == 0) {
                view.recyclerView.adapter = CarrinhoAdapter(it)
                view.recyclerView.layoutManager = LinearLayoutManager(activity)
                noItem.visibility = View.VISIBLE
            } else {
                view.recyclerView.adapter = CarrinhoAdapter(it)
                view.recyclerView.layoutManager = LinearLayoutManager(activity)
                noItem.visibility = View.INVISIBLE

            }
        }
        pagar.setOnClickListener{
            timer.cancel()
            FirebaseFirestore.getInstance().collection(uid)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for (document in it.result!!) {
                            val nome = (document.data?.get("Nome").toString())
                            FirebaseFirestore.getInstance().collection(uid).document(nome)
                                .delete()
                        }
                    }
                }
            val fragment = AfterShopping()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragment, "Dados Alterados")
                .addToBackStack(null)
                .commit()
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                getData {
                    if (it.size == 0) {
                        view.recyclerView.adapter = CarrinhoAdapter(it)
                        view.recyclerView.layoutManager = LinearLayoutManager(activity)
                        noItem.visibility = View.VISIBLE
                    } else {
                        view.recyclerView.adapter = CarrinhoAdapter(it)
                        view.recyclerView.layoutManager = LinearLayoutManager(activity)
                        noItem.visibility = View.INVISIBLE

                    }
                }
            }
        }, 0, 2000) //wait 0 ms before doing the action and do it evry 1000ms (1second)


        return view
    }



    private fun getData(myCallback: (ArrayList<Item>)->Unit){
        var total = 0.0
        val Products = ArrayList<Item>()
        FirebaseFirestore.getInstance().collection(uid)
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    for(document in it.result!!){
                            val item = Item(document.data.getValue("Id").toString().toInt(),
                                document.data.getValue("Imagem").toString(),
                                document.data.getValue("Nome").toString(),
                                document.data.getValue("Preco").toString().toFloat(),
                                document.data.getValue("PrecoUnidade").toString().toFloat(),
                                document.data.getValue("PrecoPromocao").toString().toFloat(),
                                document.data.getValue("PrecoUnidadePromocao").toString().toFloat(),
                                document.data.getValue("Promocao").toString().toBoolean(),
                                document.data.getValue("Quantidade").toString().toInt(),
                                document.data.getValue("Favorito").toString().toBoolean()
                            )
                            Products.add(item)
                    }
                    for(i in Products){
                        if(i.promocao){
                            total += i.precoPromocao * i.quantidade

                        }else{
                            total += i.preco * i.quantidade

                        }
                    }
                    val decimal = (total * 100.00).roundToInt() / 100.00
                    T.postValue(decimal)

                    myCallback(Products)
                }
            }
    }
}