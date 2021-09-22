package com.example.cme

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cme.fragments.CarrinhoFragment
import com.example.cme.fragments.PerfilFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class CarrinhoAdapter(
    private val lista: List<Item>
): RecyclerView.Adapter<CarrinhoAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ItemViewHolder {
        val view = LayoutInflater
            .from(parent .context)
            .inflate(R.layout.carrinho_layout, parent, false)
        return ItemViewHolder(view)

    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        val uid = auth.uid.toString()
        val dbFirestore : CollectionReference = FirebaseFirestore.getInstance().collection(uid)


        val currentItem = lista[position]
        Picasso.get()
            .load(currentItem.imagem)
            .into(holder.imagem)
        holder.produto.text = currentItem.nome
        holder.precoNovo.text = currentItem.precoPromocao.toString() + "€"
        holder.qtd.text = currentItem.quantidade.toString() + "x"



        holder.remover.setOnClickListener{
            currentItem.quantidade -= 1
            if(currentItem.quantidade > 0){
                holder.qtd.text = currentItem.quantidade.toString() + "x"

                val data = hashMapOf(
                    "Id" to currentItem.id,
                    "Imagem" to currentItem.imagem,
                    "Nome" to currentItem.nome,
                    "Preco" to currentItem.preco,
                    "PrecoPromocao" to currentItem.precoPromocao,
                    "PrecoUnidade" to currentItem.precoUnidade,
                    "PrecoUnidadePromocao" to currentItem.precoPromocaoUnidade,
                    "Promocao" to currentItem.promocao,
                    "Quantidade" to currentItem.quantidade,
                    "Favorito" to currentItem.favorito
                )

                dbFirestore.document(currentItem.nome).set(data).addOnCompleteListener {
                    when {
                        it.isSuccessful -> {
                            Log.d("Added", "Alterado com sucesso")


                        }

                    }
                }


            }else{
                dbFirestore.document(currentItem.nome).delete()
            }
        }
    }


    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val imagem : ImageView = itemView.findViewById(R.id.ivProduto)
        val produto : TextView = itemView.findViewById(R.id.tvProduto)
        val precoNovo : TextView = itemView.findViewById(R.id.tvPrecoNovo)
        val qtd : TextView = itemView.findViewById(R.id.tvQtd)
        val remover : Button = itemView.findViewById(R.id.btRemover)
    }
}


