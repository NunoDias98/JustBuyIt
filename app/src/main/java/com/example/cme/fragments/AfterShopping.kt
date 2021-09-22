package com.example.cme.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.cme.R


class AfterShopping : Fragment() {

    lateinit var back : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_after_shopping, container, false)

        back = view.findViewById(R.id.btVoltar)

        back.setOnClickListener{
            val fragment = CarrinhoFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragment, "Dados Alterados")
                .addToBackStack(null)
                .commit()
        }

        return view
    }


}