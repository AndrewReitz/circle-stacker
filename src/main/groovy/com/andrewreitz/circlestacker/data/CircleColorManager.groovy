package com.andrewreitz.circlestacker.data

import android.content.res.Resources
import android.content.res.TypedArray
import com.andrewreitz.circlestacker.R
import groovy.transform.CompileStatic

/**
 * Class that makes it easier to index into the colors TypedArray.
 */
@CompileStatic
class CircleColorManager {
  private final Resources resources

  @Delegate
  private final TypedArray colors

  /**
   * Constructor.
   *
   * @param params Map containing key resources that is a {@link Resources}.
   * @throws NullPointerException if resources is null.
   */
  CircleColorManager(Map params) {
    if (params.resources == null) {
      throw new NullPointerException("resources may not be null.")
    }
    resources = params.resources as Resources
    colors = resources.obtainTypedArray(R.array.circle_colors)
  }

  /** Allows use of CircleColorManager[index] to access the colors array. */
  int getAt(int index) {
    if (index < 0 || index > colors.length() - 1) {
      throw new IndexOutOfBoundsException()
    }
    colors.getColor(index, 0)
  }
}
