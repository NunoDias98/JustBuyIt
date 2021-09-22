package com.example.cme

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ItemNormalAdapter(
    private val lista: List<Item>
): RecyclerView.Adapter<ItemNormalAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ItemViewHolder {
        val view = LayoutInflater
            .from(parent .context)
            .inflate(R.layout.item_layout, parent, false)
        return ItemViewHolder(view)

    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        val uid = auth.uid.toString()
        val dbFirestore : CollectionReference = FirebaseFirestore.getInstance().collection(uid)

        val currentItem = lista[position]
        var firstItem = true
        var qtd = 1

        FirebaseFirestore.getInstance().collection(uid) //Se tiver no carrinho
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    for(document in it.result!!){
                        val nome = document.data?.get("Nome").toString()
                        if(nome == currentItem.nome){
                            firstItem = false
                            qtd = document.data?.get("Quantidade").toString().toInt()
                        }
                    }
                }
            }
        holder.adicionar.setOnClickListener{
            var count = 0
            if(!firstItem){
                qtd += 1
            }
            firstItem = false
            count +=1
            if(count > 1){
                qtd += 1
            }
            val data = hashMapOf(
                "Id" to currentItem.id,
                "Imagem" to currentItem.imagem,
                "Nome" to currentItem.nome,
                "Preco" to currentItem.preco,
                "PrecoPromocao" to currentItem.precoPromocao,
                "PrecoUnidade" to currentItem.precoUnidade,
                "PrecoUnidadePromocao" to currentItem.precoPromocaoUnidade,
                "Promocao" to currentItem.promocao,
                "Quantidade" to qtd,
                "Favorito" to currentItem.favorito
            )
            dbFirestore.document(currentItem.nome).set(data).addOnCompleteListener {
                when {
                    it.isSuccessful -> {
                        Log.d("Added", "Adicionado ao carrinho")
                    }

                }
            }
        }

        Picasso.get()
            .load(currentItem.imagem)
            .into(holder.imagem)
        holder.produto.text = currentItem.nome
        if(currentItem.promocao){
            holder.precoNovo.text = currentItem.precoPromocao.toString() + "€"
        }else{
            holder.precoNovo.text = currentItem.preco.toString() + "€"
        }
        if(currentItem.favorito)
            holder.favorito.setImageResource(R.drawable.ic_fav)
        else{
            holder.favorito.setImageResource(R.drawable.ic_nofav)
        }

    }


    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagem : ImageView = itemView.findViewById(R.id.ivProduto)
        val produto : TextView = itemView.findViewById(R.id.tvProduto)
        val precoNovo : TextView = itemView.findViewById(R.id.tvPrecoNovo)
        val adicionar : Button = itemView.findViewById(R.id.btAdicionar)
        val favorito : ImageView = itemView.findViewById(R.id.ivFav)

    }


}


