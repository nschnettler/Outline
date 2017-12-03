#include <jni.h>

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"
/*
 * Piracy Check
 *
 * Change this value to JNI_TRUE if you would like to enable anti piracy
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getAPStatus(JNIEnv *env) {
    return JNI_TRUE;
}

/*
 * Base 64 License Key
 *
 * Insert your base 64 license key obtained from Play Store in the quotations below!
 * ATTENTION: On line 23, do you see the "000"? You MUST count the amount of characters of your key
 * and input the proper number! This is so that you get your own specialized set of variables!
 */
JNIEXPORT jstring JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getBase64Key(JNIEnv *env) {
    // TODO: This must be done!
    return (*env)->NewStringUTF(env, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArTRaZnE2hkleiuajs6R41Fpf8f+em2T4cogtzPQ6O7r4DqBnUtc48KiZE408o0gGQoB3hr9vwoNKblxxSWxowcF9crDagEcWE6sfzHdEx/tM3nrVvq8jJPYPbUOfRPsxrPV0DqLGMU2DlxCeOhMWCRGK0IEMjQSBXXo33fie0g8sQrx7PGiaR9E0rbjO2eK5l7TsU7o1AhpONl4ODMcxLEupUEk3P8NxohafPSdLv/hFAJHDxzkJZCpvaxETcCAz8VatODZIKylHiYQsd0aEeYvfOmfpvG7PMz4o7H5riv1XE30qFFH0NVKXlYtgbewmhbmw5sRIcxuHmVQXTkIzWwIDAQAB");
}

/*
 * APK Signature Production
 *
 * Insert your release APK's signature in the quotations below!
 * ATTENTION: On line 34, do you see the "28"? If you're disabling antipiracy, change that to 0!
 * All release production signature prefixes have the length of 28!
 */
JNIEXPORT jstring JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getAPKSignatureProduction(JNIEnv *env) {
    return (*env)->NewStringUTF(env, "tGQYWSO6wsgTs90QJwLNH6vSwbA=");
}

/*
 * Enforce Internet Check
 *
 * Change this value to JNI_TRUE if you would like to enable internet check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getInternetCheck(JNIEnv *env) {
    return JNI_FALSE;
}

/*
 * Enforce Google Play Install
 *
 * Change this value to JNI_TRUE if you would like to enable this check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getGooglePlayRequirement(JNIEnv *env) {
    return JNI_TRUE;
}

/*
 * Enforce Amazon App Store Install
 *
 * Change this value to JNI_TRUE if you would like to enable this check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getAmazonAppStoreRequirement(JNIEnv *env) {
    return JNI_FALSE;
}

/*
 * Enable check for Blacklisted APKs
 *
 * Change this value to JNI_TRUE if you would like to enable blacklist check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_getBlacklistedApplications(JNIEnv *env) {
    return JNI_FALSE;
}

/*
 * Allow Third Party Substratum Builds
 *
 * Change this value to JNI_FALSE if you would like to ban your theme to work on external, non-team
 * built compilations of substratum
 *
 * WARNING: Having this enabled may or may not cause a bunch of issues due to the system not built
 *          and distributed by an official team member. You will take charge of handling bug reports
 *          if there are specific bugs unreproducible on the main stream of APKs.
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_ethereal_SubstratumLauncher_allowThirdPartySubstratumBuilds(JNIEnv *env) {
    return JNI_FALSE;
}

/*
 * Enable Samsung theming
 *
 * Change Line 94 to JNI_FALSE for official Samsung support!
 */
JNIEXPORT jboolean JNICALL
Java_substratum_theme_template_SubstratumLauncher_getSamsungSupport(JNIEnv *env) {
    return Java_com_schnettler_ethereal_SubstratumLauncher_allowThirdPartySubstratumBuilds(env);
}

#pragma clang diagnostic pop