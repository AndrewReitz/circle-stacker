package com.andrewreitz.circlestacker

import android.app.Application
import groovy.transform.CompileStatic
import timber.log.Timber

@CompileStatic
final class CircleStackerInitializer {
  /** Init all things debug here */
  static void init(Application app) {
    Timber.plant(new Timber.DebugTree());
  }

  private CircleStackerInitializer() {
    throw new AssertionError("No Instances" as Object)
  }
}
