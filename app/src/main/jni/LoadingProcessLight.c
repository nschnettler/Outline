#include <jni.h>

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-parameter"
/*
 * Piracy Check
 *
 * Change this value to JNI_TRUE if you would like to enable anti piracy
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_outline_SubstratumLauncher_getAPStatus(JNIEnv *env) {
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
Java_com_schnettler_outline_SubstratumLauncher_getBase64Key(JNIEnv *env) {
    // TODO: This must be done!
    return (*env)->NewStringUTF(env, "***REMOVED***");
}

/*
 * APK Signature Production
 *
 * Insert your release APK's signature in the quotations below!
 * ATTENTION: On line 34, do you see the "28"? If you're disabling antipiracy, change that to 0!
 * All release production signature prefixes have the length of 28!
 */
JNIEXPORT jstring JNICALL
Java_com_schnettler_outline_SubstratumLauncher_getAPKSignatureProduction(JNIEnv *env) {
    return (*env)->NewStringUTF(env, "***REMOVED***");
}

/*
 * Enforce Internet Check
 *
 * Change this value to JNI_TRUE if you would like to enable internet check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_outline_SubstratumLauncher_getInternetCheck(JNIEnv *env) {
    return JNI_TRUE;
}

/*
 * Enforce Google Play Install
 *
 * Change this value to JNI_TRUE if you would like to enable this check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_outline_SubstratumLauncher_getGooglePlayRequirement(JNIEnv *env) {
    return JNI_TRUE;
}

/*
 * Enforce Amazon App Store Install
 *
 * Change this value to JNI_TRUE if you would like to enable this check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_outline_SubstratumLauncher_getAmazonAppStoreRequirement(JNIEnv *env) {
    return JNI_FALSE;
}

/*
 * Enable check for Blacklisted APKs
 *
 * Change this value to JNI_TRUE if you would like to enable blacklist check
 */
JNIEXPORT jboolean JNICALL
Java_com_schnettler_outline_SubstratumLauncher_getBlacklistedApplications(JNIEnv *env) {
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
Java_com_schnettler_outline_SubstratumLauncher_allowThirdPartySubstratumBuilds(JNIEnv *env) {
    return JNI_FALSE;
}

#pragma clang diagnostic pop