package com.andrewreitz.circlestacker.ui

import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.andrewreitz.circlestacker.R
import com.andrewreitz.circlestacker.data.CircleColorManager
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import groovy.transform.CompileStatic
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.subscriptions.Subscriptions

import static android.view.View.GONE
import static android.view.View.VISIBLE
import static java.util.concurrent.TimeUnit.MILLISECONDS
import static timber.log.Timber.d as debugLog

@CompileStatic
class MainActivity extends Activity {

  /** Container view to place all circles in. */
  @InjectView(R.id.main_container) ViewGroup container

  @InjectView(R.id.main_start_text) TextView startText

  @InjectView(R.id.main_score) TextView scoreText

  /** Colors for the circles. */
  private CircleColorManager circleColors

  /** Index into colors */
  private int colorIndex = 0

  private CircleView startCircle

  /** A circle to just handle clicks. */
  private CircleView clearTopCircle

  private Point containerCenter

  private float overallMaxRadius

  /** The maximum radius the next circle to be drawn can be. */
  private float maxRadius

  private int stacksMade = 0

  private CircleView currentCircle

  private Map startCircleParams

  private Subscription newCircleSubscription = Subscriptions.empty()

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState)
    debugLog "onCreate ->"

    contentView = R.layout.activity_main
    SwissKnife.inject(this)

    circleColors = new CircleColorManager(resources: resources)

    container.viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
      @Override boolean onPreDraw() {
        debugLog "onPreDraw ->"
        container.viewTreeObserver.removeOnPreDrawListener(
            this as ViewTreeObserver.OnPreDrawListener)

        containerCenter = new Point(container.width / 2, container.height / 2)
        overallMaxRadius = maxRadius = container.width / 2 as float

        startCircleParams = [x      : containerCenter.x,
                             y      : containerCenter.y,
                             radius : maxRadius,
                             color  : nextColor,
                             context: MainActivity.this]

        startCircle = new CircleView(startCircleParams)

        // Transparent circle to receive all clicks on
        startCircleParams.color = Color.TRANSPARENT
        clearTopCircle = new CircleView(startCircleParams)

        clearTopCircle.onClickListener = startClickListener as View.OnClickListener

        container.addView(startCircle, 0)
        container.addView(clearTopCircle, container.childCount)

        debugLog "onPreDraw <-"
        return true
      }
    })
  }

  int getNextColor() {
    return circleColors[colorIndex++ % circleColors.length()]
  }

  Closure getStartClickListener() {
    return {
      debugLog "startClickListener ->"
      startText.visibility = GONE

      getOnCircleTouchListener()()
      clearTopCircle.onClickListener = onCircleTouchListener as View.OnClickListener
      debugLog "<- startClickListener"
    }
  }

  Closure getOnCircleTouchListener() {
    return {
      debugLog "onCircleTouchListener ->"
      if (currentCircle) {
        maxRadius = currentCircle.radius
      }
      newCircleSubscription.unsubscribe()
      scoreText.text = String.format(getString(R.string.score), ++stacksMade)

      currentCircle = new CircleView(x: containerCenter.x,
          y: containerCenter.y,
          radius: 0,
          color: nextColor,
          context: this)

      container.addView(currentCircle, container.childCount)
      final float incrementRadiusBy = maxRadius / 50 as float


      newCircleSubscription = rx.Observable.interval(16, MILLISECONDS)
          .filter(new Func1<Long, Boolean>() {
        @Override Boolean call(Long aLong) {
          return aLong != 0
        }
      }).map(new Func1<Long, Float>() {
        @Override Float call(Long aLong) {
          return aLong * incrementRadiusBy as Float
        }
      })
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new rx.Observer<Float>() {
        @Override void onCompleted() {}

        @Override void onError(Throwable e) {
          debugLog "Game Over"

          clearTopCircle.onClickListener = onRestartGameListener as View.OnClickListener
          startText.text = getString(R.string.game_over)
          startText.visibility = VISIBLE
          container.removeView(startText)
          container.addView(startText, container.childCount)
          maxRadius = overallMaxRadius
          stacksMade = 0
        }

        @Override void onNext(Float radius) {
          debugLog "Radius %s", radius

          if (radius > maxRadius) {
            // Throw exception to go to onError
            throw new Exception("Circle out of bounds")
          }
          currentCircle.radius = radius
          currentCircle.invalidate()
        }
      })

      debugLog "onCircleTouchListener <-"
    }
  }

  Closure getOnRestartGameListener() {
    return {
      debugLog "onRestartGameListener ->"
      startCircle = new CircleView(startCircleParams)
      container.addView(startCircle, container.childCount)
      clearTopCircle.onClickListener = onCircleTouchListener as View.OnClickListener
      getOnCircleTouchListener()()
      debugLog "onRestartGameListener <-"
    }
  }
}
