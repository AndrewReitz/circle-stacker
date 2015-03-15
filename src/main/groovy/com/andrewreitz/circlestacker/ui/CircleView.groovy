package com.andrewreitz.circlestacker.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import groovy.transform.CompileStatic

@CompileStatic
final class CircleView extends View {
  private final float circleX
  private final float circleY
  private final Paint paint

  float radius

  CircleView(Map params) {
    super(params.context as Context)

    this.circleX = params.x as float
    this.circleY = params.y as float
    this.radius = params.radius as float
    this.paint = new Paint()
    paint.color = params.color as int
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas)

    canvas.drawCircle(circleX, circleY, radius, paint)
  }
}
