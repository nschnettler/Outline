package com.schnettler.common

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.schnettler.common.databinding.MainActivityBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val thanksCard = binding.rateCard
        thanksCard.cardTitle.text = "Thank you for buying ${resources.getString(R.string.ThemeName)}"
        thanksCard.cardContent.text = "Please consider leaving a review"
        thanksCard.cardIcon.setImageResource(R.drawable.ic_round_star_border_24)
        thanksCard.cardLayout.setOnClickListener {
            println("Package $packageName")
            openPlayStore(packageName)
        }

        val subsCard = binding.subsCard
        subsCard.cardTitle.text = "This is a Substratum Theme"
        subsCard.cardContent.text = "Substratum is needed to install it"
        subsCard.cardIcon.setImageResource(R.drawable.ic_round_substratum_border_24)

        val openCard = binding.openCard
        openCard.cardTitle.text = "Open Substratum"
        openCard.cardContent.text = "Click here to open/install Substratum"
        openCard.cardIcon.setImageResource(R.drawable.ic_round_open_border_24)
        openCard.cardLayout.setOnClickListener {
            try {
                openSubstratumLite()
            } catch (e: Exception) {
                try {
                    openSubstratum()
                } catch (e: Exception) {
                    openPlayStore("projekt.substratum.lite")
                }
            }
        }
    }

    private fun openSubstratumLite() =
            startActivity(packageManager.getLaunchIntentForPackage("projekt.substratum.lite"))

    private fun openSubstratum() =
            startActivity(packageManager.getLaunchIntentForPackage("projekt.substratum"))

    private fun openPlayStore(packageName: String) =
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=$packageName"
            )))
}