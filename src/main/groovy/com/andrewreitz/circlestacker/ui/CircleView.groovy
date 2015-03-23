package com.andrewreitz.circlestacker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import groovy.transform.CompileStatic

/** View for drawing circles onto the screen. */
@CompileStatic
final class CircleView extends View {
  private final float circleX
  private final float circleY
  private final Paint paint

  /** The radius of the circle. */
  float radius

  /**
   * Constructor.
   *
   * @param params Map containing, x; the x value of the center of the circle, y; the y value of
   * the center of the circle, radius; the radius of the circle, color the color of the circle.
   * @throws NullPointerException if any of these values are null.
   */
  CircleView(Map params) {
    super(params.context as Context)

    this.circleX = params.x as Float
    this.circleY = params.y as Float
    this.radius = params.radius as Float
    this.paint = new Paint()
    paint.color = params.color as Integer
  }

  /** Draws this circle onto the view. */
  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas)
    canvas.drawCircle(circleX, circleY, radius, paint)
  }

  /** Updated the radius of this circle. Causes the view to be redrawn. */
  void setRadius(float radius) {
    this.radius = radius
    invalidate()
  }
}
