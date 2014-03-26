package com.beerchallenge.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {
    private static Map<String, Typeface> cache = new HashMap<>();

    public static Typeface getFonts(Context c, String name) {
        String assetPath = "fonts/" + name;
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
}