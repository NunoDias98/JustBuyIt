package com.example.cme

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cme.fragments.CarrinhoFragment
import com.example.cme.fragments.PerfilFragment
import com.example.cme.fragments.ProdutosFragment
import com.example.cme.fragments.PromocoesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val promocoesFragment = PromocoesFragment()
    private val produtosFragment = ProdutosFragment()
    private val perfilFragment = PerfilFragment()
    private val carrinhoFragment = CarrinhoFragment()

    companion object{
        var once = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(promocoesFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.promocoes -> replaceFragment(promocoesFragment)
                R.id.produtos -> replaceFragment(produtosFragment)
                R.id.carrinho -> replaceFragment(carrinhoFragment)
                R.id.perfil -> replaceFragment(perfilFragment)
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}