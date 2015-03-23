package com.andrewreitz.circlestacker

import android.app.Application

class CircleStackerApp extends Application {
  @Override public void onCreate() {
    super.onCreate()

    // Call into build specific initialization.
    CircleStackerInitializer.init(this)
  }
}
