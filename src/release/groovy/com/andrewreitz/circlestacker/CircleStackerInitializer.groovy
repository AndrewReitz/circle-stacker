package com.andrewreitz.circlestacker

import android.app.Application
import com.crashlytics.android.Crashlytics
import groovy.transform.CompileStatic
import io.fabric.sdk.android.Fabric

@CompileStatic
final class CircleStackerInitializer {
  /** Init all things release here */
  static void init(Application app) {
    Fabric.with(app, new Crashlytics());
  }

  private CircleStackerInitializer() {
    throw new AssertionError("No Instances" as Object)
  }
}

