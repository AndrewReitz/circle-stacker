package com.andrewreitz.circlestacker;

import android.app.Application;

final class CircleStackerInitializer {
  private final Application application;

  CircleStackerInitializer(Application application) {
    this.application = application;
  }

  /** Init all things release here */
  void init() {
    /* Ex. Timber.plant(new CrashReportingTree()); */
  }
}

