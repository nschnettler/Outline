@file:Suppress("ConstantConditionIf")

package com.schnettler.common

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.github.javiersantos.piracychecker.*
import com.github.javiersantos.piracychecker.enums.InstallerID
import com.github.javiersantos.piracychecker.utils.apkSignature
import com.schnettler.common.AdvancedConstants.ORGANIZATION_THEME_SYSTEMS
import com.schnettler.common.AdvancedConstants.OTHER_THEME_SYSTEMS
import com.schnettler.common.ThemeFunctions.checkApprovedSignature
import com.schnettler.common.ThemeFunctions.getSelfSignature
import com.schnettler.common.ThemeFunctions.isCallingPackageAllowed

object CommonLauncher {

    private const val debug = false
    private const val tag = "SubstratumThemeReport"
    private const val substratumIntentData = "projekt.substratum.THEME"
    private const val getKeysIntent = "projekt.substratum.GET_KEYS"
    private const val receiveKeysIntent = "projekt.substratum.RECEIVE_KEYS"
    private const val themePiracyCheck = false

    fun start(ctx : Activity) {
        /* STEP 1: Block hijackers */
        val caller = ctx.callingActivity!!.packageName
        val organizationsSystem = ORGANIZATION_THEME_SYSTEMS.contains(caller)
        val supportedSystem = organizationsSystem || OTHER_THEME_SYSTEMS.contains(caller)
        if (!BuildConfig.SUPPORTS_THIRD_PARTY_SYSTEMS && !supportedSystem) {
            Log.e(tag, "This theme does not support the launching theme system. [HIJACK] ($caller)")
            Toast.makeText(ctx,
                    String.format(ctx.getString(R.string.unauthorized_theme_client_hijack), caller),
                    Toast.LENGTH_LONG).show()
            ctx.finish()
        }
        if (debug) {
            Log.d(tag, "'$caller' has been authorized to launch this theme. (Phase 1)")
        }

        /* STEP 2: Ensure that our support is added where it belongs */
        val action = ctx.intent.action
        var verified = false
        if ((action == substratumIntentData) or (action == getKeysIntent)) {
            // Assume this called from organization's app
            if (organizationsSystem) {
                verified = when {
                    BuildConfig.ALLOW_THIRD_PARTY_SUBSTRATUM_BUILDS -> true
                    else -> checkApprovedSignature(ctx, caller)
                }
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
        if (debug) {
            Log.d(tag, "'$action' has been authorized to launch this theme. (Phase 2)")
        }

        /* STEP 3: Do da thang */
        val sharedPref = ctx.getSharedPreferences(null, 0)
        if( !sharedPref.getBoolean("dialog_showed", false)) {
            showDialog(ctx)
            sharedPref.edit().putBoolean("dialog_showed", true).apply()
        } else {
            startAntiPiracyCheck(ctx)
        }
    }

    private fun startAntiPiracyCheck(ctx : Activity) {
        if (BuildConfig.BASE_64_LICENSE_KEY.isEmpty() && debug && !BuildConfig.DEBUG) {
            Log.e(tag, ctx.apkSignature)
        }

        if (!themePiracyCheck) {
            ctx.piracyChecker {
                if (BuildConfig.ENFORCE_GOOGLE_PLAY_INSTALL) {
                    enableInstallerId(InstallerID.GOOGLE_PLAY)
                }
                if (BuildConfig.BASE_64_LICENSE_KEY.isNotEmpty()) {
                    enableGooglePlayLicensing(BuildConfig.BASE_64_LICENSE_KEY)
                }
                if (BuildConfig.APK_SIGNATURE_PRODUCTION.isNotEmpty()) {
                    enableSigningCertificate(BuildConfig.APK_SIGNATURE_PRODUCTION)
                }
                callback {
                    allow {
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
                        destroy()
                        ctx.finish()
                    }
                    doNotAllow { _, _ ->
                        val parse = String.format(
                                ctx.getString(R.string.toast_unlicensed),
                                ctx.getString(R.string.ThemeName))
                        Toast.makeText(ctx, parse, Toast.LENGTH_SHORT).show()
                        destroy()
                        ctx.finish()
                    }
                    onError { error ->
                        Toast.makeText(ctx, error.toString(), Toast.LENGTH_LONG)
                                .show()
                        ctx.finish()
                    }
                }
            }.start()
        } else {
            Toast.makeText(ctx, R.string.unauthorized, Toast.LENGTH_LONG).show()
            ctx.finish()
        }
    }

    private fun showDialog(ctx: Activity) {
        val dialog = AlertDialog.Builder(ctx, R.style.DialogStyle)
                .setCancelable(false)
                .setTitle(R.string.launch_dialog_title)
                .setMessage(R.string.launch_dialog_content)
                .setPositiveButton(android.R.string.ok) { _, _ -> startAntiPiracyCheck(ctx) }
        dialog.show()
    }
}