package com.doiliomatsinhe.bakingapp.util;

import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.idling.CountingIdlingResource;

public class EspressoUtils {
    private static final String RESOURCE = "GLOBAL";

    private static CountingIdlingResource countingIdlingResource = new CountingIdlingResource(RESOURCE);

    public static void increment() {
        countingIdlingResource.increment();
    }

    public static void decrement() {
        countingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return countingIdlingResource;
    }
}
