
@file:Suppress("ConstantConditionIf")

package com.schnettler.@theme@

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Toast
import com.github.javiersantos.piracychecker.PiracyChecker
import com.github.javiersantos.piracychecker.PiracyCheckerUtils
import com.github.javiersantos.piracychecker.enums.InstallerID
import com.github.javiersantos.piracychecker.enums.PiracyCheckerCallback
import com.github.javiersantos.piracychecker.enums.PiracyCheckerError
import com.github.javiersantos.piracychecker.enums.PirateApp
import com.schnettler.@theme@.AdvancedConstants.ENFORCE_MINIMUM_SUBSTRATUM_VERSION
import com.schnettler.@theme@.AdvancedConstants.ORGANIZATION_THEME_SYSTEMS
import com.schnettler.@theme@.AdvancedConstants.MINIMUM_SUBSTRATUM_VERSION
import com.schnettler.@theme@.AdvancedConstants.OTHER_THEME_SYSTEMS
import com.schnettler.@theme@.AdvancedConstants.SHOW_DIALOG_REPEATEDLY
import com.schnettler.@theme@.AdvancedConstants.SHOW_LAUNCH_DIALOG
import com.schnettler.@theme@.AdvancedConstants.SUBSTRATUM_FILTER_CHECK
import com.schnettler.@theme@.ThemeFunctions.SUBSTRATUM_PACKAGE_NAME
import com.schnettler.@theme@.ThemeFunctions.checkSubstratumIntegrity
import com.schnettler.@theme@.ThemeFunctions.getSelfSignature
import com.schnettler.@theme@.ThemeFunctions.getSelfVerifiedIntentResponse
import com.schnettler.@theme@.ThemeFunctions.getSelfVerifiedPirateTools
import com.schnettler.@theme@.ThemeFunctions.getSelfVerifiedThemeEngines
import com.schnettler.@theme@.ThemeFunctions.getSubstratumFromPlayStore
import com.schnettler.@theme@.ThemeFunctions.getSubstratumUpdatedResponse
import com.schnettler.@theme@.ThemeFunctions.hasOtherThemeSystem
import com.schnettler.@theme@.ThemeFunctions.isCallingPackageAllowed
import com.schnettler.@theme@.ThemeFunctions.isPackageInstalled

class SubstratumLauncher : Activity() {

    private var substratumIntentData = "projekt.substratum.THEME"
    private var getKeysIntent = "projekt.substratum.GET_KEYS"
    private var receiveKeysIntent = "projekt.substratum.RECEIVE_KEYS"
    private var tag = "SubstratumThemeReport"
    private var piracyChecker: PiracyChecker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Reject all other apps trying to hijack the theme first
        val caller = callingActivity.packageName
        var callerVerified = false

        val themeSystems: MutableList<String> = mutableListOf()
        themeSystems.addAll(ORGANIZATION_THEME_SYSTEMS)
        themeSystems.addAll(OTHER_THEME_SYSTEMS)
        themeSystems
                .filter { caller.startsWith(prefix = it, ignoreCase = true) }
                .forEach { callerVerified = true }
        if (!callerVerified) {
            Log.e(tag, "This theme does not support the launching theme system. [HIJACK] ($caller)")
            val hijackString =
                    String.format(getString(R.string.unauthorized_theme_client_hijack), caller)
            Toast.makeText(this, hijackString, Toast.LENGTH_LONG).show()
            finish()
            return
        } else {
            Log.d(tag, "'$caller' has been authorized to launch this theme. (Phase 1)")
        }

        // We will ensure that our support is added where it belongs
        val intent = intent
        val action = intent.action
        var verified = false
        val certified = intent.getBooleanExtra("certified", false)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if ((action == substratumIntentData) or (action == getKeysIntent)) {
            verified = when {
                BuildConfig.ALLOW_THIRD_PARTY_SUBSTRATUM_BUILDS -> true
                else -> checkSubstratumIntegrity(this)
            }
        } else {
            OTHER_THEME_SYSTEMS
                    .filter { action.startsWith(prefix = it, ignoreCase = true) }
                    .forEach { verified = true }
        }
        if (!verified) {
            Log.e(tag, "This theme does not support the launching theme system. ($action)")
            Toast.makeText(this, R.string.unauthorized_theme_client, Toast.LENGTH_LONG).show()
            finish()
            return
        } else {
            Log.d(tag, "'$action' has been authorized to launch this theme. (Phase 2)")
        }

        //Piracy Check
        if (piracyChecker != null) {
            piracyChecker!!.start()
        } else {
            piracyChecker = PiracyChecker(this)
                    .enableGooglePlayLicensing(BuildConfig.BASE_64_LICENSE_KEY)
                    .enableSigningCertificate(BuildConfig.APK_SIGNATURE_PRODUCTION)
                    .enableInstallerId(InstallerID.GOOGLE_PLAY)
                    .enableDebugCheck(!BuildConfig.DEBUG)

            piracyChecker!!.callback(object : PiracyCheckerCallback() {
                //Not Pirated
                override fun allow() {
                    if (!hasOtherThemeSystem(this@SubstratumLauncher)) {
                        if (!isPackageInstalled(applicationContext, SUBSTRATUM_PACKAGE_NAME)) {
                            getSubstratumFromPlayStore(this@SubstratumLauncher)
                            finish()
                        }
                        if (ENFORCE_MINIMUM_SUBSTRATUM_VERSION
                                && !getSubstratumUpdatedResponse(applicationContext)) {
                            val parse = String.format(
                                    getString(R.string.outdated_substratum),
                                    getString(R.string.ThemeName),
                                    MINIMUM_SUBSTRATUM_VERSION.toString())
                            Toast.makeText(this@SubstratumLauncher, parse, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else if (!BuildConfig.SUPPORTS_THIRD_PARTY_SYSTEMS) {
                        Toast.makeText(this@SubstratumLauncher, R.string.unauthorized_theme_client, Toast.LENGTH_LONG).show()
                        finish()
                    }

                    var returnIntent = Intent()
                    if (intent.action == getKeysIntent) {
                        returnIntent = Intent(receiveKeysIntent)
                    }

                    val themeName = getString(R.string.ThemeName)
                    val themeAuthor = getString(R.string.ThemeAuthor)
                    val themePid = packageName
                    returnIntent.putExtra("theme_name", themeName)
                    returnIntent.putExtra("theme_author", themeAuthor)
                    returnIntent.putExtra("theme_pid", themePid)

                    val themeLaunchType = getSelfVerifiedThemeEngines(applicationContext)
                    val themeHash = getSelfSignature(applicationContext)
                    var themePiracyCheck = false
                    if (BuildConfig.ENABLE_APP_BLACKLIST_CHECK)
                        themePiracyCheck = getSelfVerifiedPirateTools(applicationContext)
                    if (themePiracyCheck or (SUBSTRATUM_FILTER_CHECK && !certified)) {
                        Toast.makeText(this@SubstratumLauncher, R.string.unauthorized, Toast.LENGTH_LONG).show()
                        finish()
                    }
                    returnIntent.putExtra("theme_hash", themeHash)
                    returnIntent.putExtra("theme_launch_type", themeLaunchType)
                    returnIntent.putExtra("theme_debug", BuildConfig.DEBUG)
                    returnIntent.putExtra("theme_piracy_check", themePiracyCheck)
                    returnIntent.putExtra("encryption_key", BuildConfig.DECRYPTION_KEY)
                    returnIntent.putExtra("iv_encrypt_key", BuildConfig.IV_KEY)

                    val callingPackage = intent.getStringExtra("calling_package_name")
                    if (callingPackage == null) {
                        val parse = String.format(
                                getString(R.string.outdated_substratum),
                                getString(R.string.ThemeName),
                                915)
                        Toast.makeText(this@SubstratumLauncher, parse, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    if (!isCallingPackageAllowed(callingPackage)) {
                      finish()
                    } else {
                        returnIntent.`package` = callingPackage
                    }

                    if (intent.action == substratumIntentData) {
                        setResult(getSelfVerifiedIntentResponse(applicationContext)!!, returnIntent)
                    } else if (intent.action == getKeysIntent) {
                        returnIntent.action = receiveKeysIntent
                        sendBroadcast(returnIntent)
                    }
                    finish()
                }

                override fun dontAllow(error: PiracyCheckerError, pirateApp: PirateApp?) {
                    val parse = String.format(
                            getString(R.string.toast_unlicensed),
                            getString(R.string.ThemeName))
                    Toast.makeText(this@SubstratumLauncher, parse, Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }
        piracyChecker!!.start()
    }
}