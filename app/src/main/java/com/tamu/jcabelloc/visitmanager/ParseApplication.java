package com.tamu.jcabelloc.visitmanager;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jcabelloc on 2/8/2018.
 */

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("9222ef3c7b63fc9099de6d1370cfe60419cd91c1")
                .clientKey("c196ad25b32002395dc4efe3fe2a458fcbe08c93") //masterkey  // Console Parse Server // Password: nBEZMR4HiGLX // user: user // url: http://18.217.113.104:80//app
                .server("http://18.217.113.104:80/parse")
                .build()
        );
    }
}
