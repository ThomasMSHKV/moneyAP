package com.example.currency.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currency.R
import com.example.currency.data.CorrencyRepository
import kotlinx.android.synthetic.main.fragment_currency.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.coroutines.CoroutineContext

class CurrencyFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val repository = CorrencyRepository()

        when (arguments?.getInt("key") ?: "") {
            1 -> launch {
                val curency = repository.getCurrency().await()

                loader.visibility=View.GONE
                textAndImage.visibility=View.VISIBLE
                currencyRate.visibility=View.VISIBLE

                curency?.let {
                    currencyText.text = getString(R.string.currenyFragmentTitleUsd)
                    currencyImage.setImageResource(R.drawable.america)
                    currencyRate.text = getString(R.string.currency).format(it.rub.toString())
                }

            }
            2 ->
                launch {
                    val curency = repository.getCurrency().await()

                    loader.visibility=View.GONE
                    textAndImage.visibility=View.VISIBLE
                    currencyRate.visibility=View.VISIBLE

                    //форматирование евро в формат 1,11
                    val euro = ((1.0 - curency!!.eur) + 1) * curency.rub
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.CEILING

                    curency?.let {
                        currencyText.text = getString(R.string.currenyFragmentTitleEUR)
                        currencyImage.setImageResource(R.drawable.evrope)
                        currencyRate.text =
                            getString(R.string.currency).format(df.format(euro).toString())
                    }

                }
        }


    }
}