/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization */

#ifndef _Included_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
#define _Included_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    Login
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILcom/qpidnetwork/livemodule/httprequest/OnRequestLoginCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_Login
  (JNIEnv *, jclass, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    Logout
 * Signature: (Lcom/qpidnetwork/livemodule/httprequest/OnRequestCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_Logout
  (JNIEnv *, jclass, jobject);

/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    UploadPushTokenId
 * Signature: (Ljava/lang/String;Lcom/qpidnetwork/livemodule/httprequest/OnRequestCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_UploadPushTokenId
  (JNIEnv *, jclass, jstring, jobject);


/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    GetValidSiteId
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/livemodule/httprequest/OnGetValidSiteIdCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_GetValidSiteId
        (JNIEnv *, jclass, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    AddToken
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/livemodule/httprequest/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_AddToken
        (JNIEnv *, jclass, jstring, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    DestroyToken
 * Signature: (Lcom/qpidnetwork/livemodule/httprequest/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_DestroyToken
        (JNIEnv *, jclass, jobject);

/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    FindPassword
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/livemodule/httprequest/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_FindPassword
        (JNIEnv *, jclass, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization
 * Method:    ChangePassword
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/livemodule/httprequest/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_ChangePassword
        (JNIEnv *, jclass, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_request_RequestJniAuthorization
 * Method:    DoLogin
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/request/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_DoLogin
        (JNIEnv *, jclass, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_request_RequestJniAuthorization
 * Method:    GetToken
 * Signature: (ILcom/qpidnetwork/request/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_GetAuthToken
        (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     com_qpidnetwork_request_RequestJniAuthorization
 * Method:    PasswordLogin
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/request/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_PasswordLogin
        (JNIEnv *, jclass, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_request_RequestJniAuthorization
 * Method:    TokenLogin
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/qpidnetwork/request/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_TokenLogin
        (JNIEnv *, jclass, jstring, jstring, jobject);

/*
 * Class:     com_qpidnetwork_request_RequestJniAuthorization
 * Method:    GetValidateCode
 * Signature: (ILcom/qpidnetwork/request/OnRequestCommonCallback;)J
 */
JNIEXPORT jlong JNICALL Java_com_qpidnetwork_livemodule_httprequest_RequestJniAuthorization_GetValidateCode
        (JNIEnv *, jclass, jint , jobject);

#ifdef __cplusplus
}
#endif
#endif