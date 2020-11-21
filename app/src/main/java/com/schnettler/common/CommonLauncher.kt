@file:Suppress("ConstantConditionIf")

package com.schnettler.common

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.schnettler.common.AdvancedConstants.ORGANIZATION_THEME_SYSTEMS
import com.schnettler.common.AdvancedConstants.OTHER_THEME_SYSTEMS
import com.schnettler.common.ThemeFunctions.getSelfSignature
import com.schnettler.common.ThemeFunctions.isCallingPackageAllowed

object CommonLauncher {

    private const val tag = "SubstratumThemeReport"
    private const val substratumIntentData = "projekt.substratum.THEME"
    private const val getKeysIntent = "projekt.substratum.GET_KEYS"
    private const val receiveKeysIntent = "projekt.substratum.RECEIVE_KEYS"
    private const val themePiracyCheck = false

    fun start(ctx : Activity) {
        val caller = ctx.callingActivity!!.packageName

        /* STEP 2: Ensure that our support is added where it belongs */
        val action = ctx.intent.action
        var verified = false
        if ((action == substratumIntentData) or (action == getKeysIntent)) {
            // Assume this called from organization's app
            if (ORGANIZATION_THEME_SYSTEMS.contains(caller)) {
                verified = true
            }
        } else {
            OTHER_THEME_SYSTEMS
                    .filter { action?.startsWith(prefix = it, ignoreCase = true) ?: false }
                    .forEach { _ -> verified = true }
        }
        if (!verified) {
            Log.e(tag, "This theme does not support the launching theme system. ($action)")
            Toast.makeText(ctx, R.string.unauthorized_theme_client, Toast.LENGTH_LONG).show()
            ctx.finish()
            return
        }

        /* STEP 3: Do da thang */
        val sharedPref = ctx.getSharedPreferences(null, 0)
        if( !sharedPref.getBoolean("dialog_showed", false)) {
            showDialog(ctx)
            sharedPref.edit().putBoolean("dialog_showed", true).apply()
        } else {
            openTheme(ctx)
        }
    }

    private fun openTheme(ctx : Activity) {
        val returnIntent = if (ctx.intent.action == getKeysIntent) {
            Intent(receiveKeysIntent)
        } else {
            Intent()
        }

        val themeName = ctx.getString(R.string.ThemeName)
        val themeAuthor = ctx.getString(R.string.ThemeAuthor)
        val themePid = ctx.packageName
        returnIntent.putExtra("theme_name", themeName)
        returnIntent.putExtra("theme_author", themeAuthor)
        returnIntent.putExtra("theme_pid", themePid)
        returnIntent.putExtra("theme_debug", BuildConfig.DEBUG)
        returnIntent.putExtra("theme_piracy_check", themePiracyCheck)
        returnIntent.putExtra("encryption_key", "")
        returnIntent.putExtra("iv_encrypt_key", "")

        val callingPackage = ctx.intent.getStringExtra("calling_package_name")
        if (!isCallingPackageAllowed(callingPackage)) {
            ctx.finish()
        } else {
            returnIntent.`package` = callingPackage
        }


        if (ctx.intent.action == substratumIntentData) {
            ctx.setResult(getSelfSignature(ctx.applicationContext), returnIntent)
        } else if (ctx.intent.action == getKeysIntent) {
            returnIntent.action = receiveKeysIntent
            ctx.sendBroadcast(returnIntent)
        }
        ctx.finish()
    }

    private fun showDialog(ctx: Activity) {
        val dialog = AlertDialog.Builder(ctx, R.style.DialogStyle)
                .setCancelable(false)
                .setTitle(R.string.launch_dialog_title)
                .setMessage(R.string.launch_dialog_content)
                .setPositiveButton(android.R.string.ok) { _, _ -> openTheme(ctx) }
        dialog.show()
    }
}