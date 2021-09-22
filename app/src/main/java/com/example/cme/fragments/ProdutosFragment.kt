package com.example.cme.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cme.Item
import com.example.cme.ItemAdapter
import com.example.cme.ItemNormalAdapter
import com.example.cme.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_promocoes.view.*


class ProdutosFragment : Fragment() {
    lateinit var noItem : TextView
    lateinit var favs : SwitchCompat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_produtos, container, false)

        favs = view.findViewById(R.id.swFav)

        favs.isChecked = false


        noItem = view.findViewById(R.id.tvNoItem)
        getData {
            if (it.size == 0) {
                view.recyclerView.adapter = ItemNormalAdapter(it)
                view.recyclerView.layoutManager = LinearLayoutManager(activity)
                noItem.visibility = View.VISIBLE
            } else {
                view.recyclerView.adapter = ItemNormalAdapter(it)
                view.recyclerView.layoutManager = LinearLayoutManager(activity)
                noItem.visibility = View.INVISIBLE
            }
        }

        favs.setOnClickListener(){
            if(favs.isChecked){
                getDataFav{
                    if(it.size == 0){
                        view.recyclerView.adapter = ItemNormalAdapter(it)
                        view.recyclerView.layoutManager = LinearLayoutManager(activity)
                        noItem.visibility = View.VISIBLE
                    }else{
                        view.recyclerView.adapter = ItemNormalAdapter(it)
                        view.recyclerView.layoutManager = LinearLayoutManager(activity)
                        noItem.visibility = View.INVISIBLE
                    }
                }
            }else{
                getData{
                    if(it.size == 0){
                        view.recyclerView.adapter = ItemNormalAdapter(it)
                        view.recyclerView.layoutManager = LinearLayoutManager(activity)
                        noItem.visibility = View.VISIBLE
                    }else{
                        view.recyclerView.adapter = ItemNormalAdapter(it)
                        view.recyclerView.layoutManager = LinearLayoutManager(activity)
                        noItem.visibility = View.INVISIBLE
                    }
                }
            }
        }
        return view
    }
        private fun getData(myCallback: (ArrayList<Item>)->Unit){
            val Products = ArrayList<Item>()

            FirebaseFirestore.getInstance().collection("Produtos")
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
                        myCallback(Products)
                    }
                }
        }

    private fun getDataFav(myCallback: (ArrayList<Item>)->Unit){
        val Products = ArrayList<Item>()

        FirebaseFirestore.getInstance().collection("Produtos")
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    for(document in it.result!!){
                        if(document.getBoolean("Favorito") == true){
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
                    }
                    myCallback(Products)
                }
            }
    }

}