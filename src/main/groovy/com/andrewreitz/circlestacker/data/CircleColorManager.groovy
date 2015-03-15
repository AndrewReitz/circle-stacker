package com.andrewreitz.circlestacker.data

import android.content.res.Resources
import android.content.res.TypedArray
import com.andrewreitz.circlestacker.R
import groovy.transform.CompileStatic

@CompileStatic
class CircleColorManager {
  private final Resources resources

  @Delegate
  private final TypedArray colors

  CircleColorManager(Map params) {
    resources = params.resources as Resources
    colors = resources.obtainTypedArray(R.array.circle_colors)
  }

  int getAt(int index) {
    if (index < 0 || index > colors.length() - 1) {
      throw new IndexOutOfBoundsException()
    }
    colors.getColor(index, 0)
  }
}
