//JNI functions for the FullyConnectedLayer class
#include <jni.h>
#include <android/log.h>
#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include <fstream>
#include <algorithm>
#include <iterator>
#include <cmath>
#include <thread>


extern "C"
JNIEXPORT jobject JNICALL
Java_com_vibebuddy_vibebuddy_Buddy_MachineLearning_Layers_FullyConnectedLayer_ForwardNative(
        JNIEnv *env, jclass thiz, jdoubleArray x, jobjectArray w, jdoubleArray z, jdoubleArray b, jint inputs,
        jint outputs, jint threads, jclass Activation){
    std::vector<std::thread> threadPool;

    jdouble *xArray = env->GetDoubleArrayElements(x, nullptr);
    jdouble *bArray = env->GetDoubleArrayElements(b, nullptr);
    jdouble *zArray = env->GetDoubleArrayElements(z, nullptr);
    //get activation function from Activation class
    jmethodID activation = env->GetStaticMethodID(Activation, "activation", "(D)D");

    for (int i = 0; i < threads; ++i) {
        threadPool.emplace_back([i, threads, xArray, bArray, zArray, inputs, outputs, &env, &Activation, activation, &w]() {
            int startingIndex = i * (outputs / threads);
            int Zs_to_calculate = outputs / threads;
            if (i == threads - 1) {
                Zs_to_calculate += outputs % threads;
            }

            for (int j = startingIndex; j < startingIndex + Zs_to_calculate; j++) {
                double sum = 0;
                for (int k = 0; k < inputs; k++) {
                    //get x and w from ArrayLists
                    //w is a 2D array, so we need to get the 1D array at index j
                    auto wArray = env->GetObjectArrayElement(w, j);
                    jdouble *wArray2 = env->GetDoubleArrayElements((jdoubleArray) wArray, nullptr);
                    sum += xArray[k] * wArray2[k];
                }
                //get b from ArrayList
                sum += bArray[j];
                //get z from ArrayList
                //call activation function
                zArray[j] = env->CallStaticDoubleMethod(Activation, activation, sum);
            }
        });
    }

    for (int i = 0; i < threads; ++i) {
        threadPool[i].join();
    }

    return z;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_vibebuddy_vibebuddy_Buddy_MachineLearning_Layers_FullyConnectedLayer_BackwardNative(
        JNIEnv *env, jclass thiz, jdoubleArray x, jdoubleArray w, jdoubleArray z, jdoubleArray b, jdoubleArray fg,
        jdouble lr, jint inputs, jint outputs, jint threads,jclass Activation, jclass Loss) {
    std::vector<std::thread> threadPool;

    //get x and w from ArrayLists
    jdouble *xArray = env->GetDoubleArrayElements(x, nullptr);
    //w is a 2D array, so we need to get the 1D array at index j
    jdouble *wArray = env->GetDoubleArrayElements(w, nullptr);
    //get fg from ArrayList
    jdouble *fgArray = env->GetDoubleArrayElements(fg, nullptr);
    //get z from ArrayList
    jdouble *zArray = env->GetDoubleArrayElements(z, nullptr);
    //get b from ArrayList
    jdouble *bArray = env->GetDoubleArrayElements(b, nullptr);

    //get activation function from Activation class
    jmethodID activationprime = env->GetStaticMethodID(Activation, "activationPrime", "(D)D");

    //create da_dx array
    jdoubleArray da_dx = env->NewDoubleArray(inputs);
    jdouble *da_dxArray = env->GetDoubleArrayElements(da_dx, nullptr);

    for (int i = 0; i < threads; ++i) {
        threadPool.emplace_back([i, threads, xArray, wArray, zArray, bArray, fgArray, lr, inputs, outputs, &env, &Activation, activationprime, da_dxArray]() {
            int startingIndex = i * (outputs / threads);
            int Zs_to_calculate = outputs / threads;
            if (i == threads - 1) {
                Zs_to_calculate += outputs % threads;
            }
            for (int j = startingIndex; j < startingIndex + Zs_to_calculate; j++) {
                //update b
                bArray[j] += fgArray[j] * lr* env->CallStaticDoubleMethod(Activation, activationprime, zArray[j]);
                for (int k = 0; k < inputs; k++) {
                    //update w
                    wArray[k] += xArray[k] * fgArray[j] * lr* env->CallStaticDoubleMethod(Activation, activationprime, zArray[j]);
                    //update da_dx
                    da_dxArray[k] += wArray[k] * fgArray[j]* lr * env->CallStaticDoubleMethod(Activation, activationprime, zArray[j]);
                }
            }
        });
    }

    for (int i = 0; i < threads; ++i) {
        threadPool[i].join();
    }

    return da_dx;
}