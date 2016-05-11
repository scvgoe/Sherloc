package com.example.daesungkim.sherloc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by daeseongkim on 2015. 12. 2..
 */
public class GeneratingSpecialID {
    String keyPool[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};



    public String generateSpecialID () {
        List<String> keypool = new ArrayList<String>();

        for (int i = 0; i < keyPool.length; i++) {
            keypool.add(keyPool[i]);
        }

        Random randomizer = new Random();

        String result = "";

        for (int i = 0; i < 10; i++){
            String random = keypool.get(randomizer.nextInt(keypool.size()));
            result = result + random;
        }

        return result;
    }
}
