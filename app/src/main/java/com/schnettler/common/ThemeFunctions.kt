package com.schnettler.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import com.schnettler.common.AdvancedConstants.ORGANIZATION_THEME_SYSTEMS
import com.schnettler.common.AdvancedConstants.OTHER_THEME_SYSTEMS

@Suppress("ConstantConditionIf")
object ThemeFunctions {

    fun isCallingPackageAllowed(packageId: String?) =
            ORGANIZATION_THEME_SYSTEMS.contains(packageId) || OTHER_THEME_SYSTEMS.contains(packageId)

    @Suppress("DEPRECATION")
    @SuppressLint("PackageManagerGetSignatures")
    fun getSelfSignature(context: Context): Int {
        try {
            val sigs = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
            ).signatures
            return sigs[0].hashCode()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }
}