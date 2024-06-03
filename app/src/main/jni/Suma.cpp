//
// Created by user on 03.06.2024..
//
#include "jelena_gajic_onlineshop_JNIsuma.h"
JNIEXPORT jint JNICALL Java_jelena_gajic_onlineshop_JNIsuma_suma
        (JNIEnv *env, jobject, jint a, jint b){
            return a+b;
}