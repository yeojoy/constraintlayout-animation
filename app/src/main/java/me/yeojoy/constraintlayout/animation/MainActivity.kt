package me.yeojoy.constraintlayout.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ANIMATION_TIME = 300L
    }

    private var handler: Handler? = null
    private var parentView: ConstraintLayout? = null
    private var buttonDoAnimation: Button? = null
    private var buttonShow: Button? = null
    private var cardView: CardView? = null
    private var isShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler(Looper.getMainLooper())

        parentView = findViewById(R.id.constraintLayout)
        buttonDoAnimation = findViewById(R.id.button_start)
        buttonShow = findViewById(R.id.button_show)
        cardView = findViewById(R.id.animatedBanner)

        buttonDoAnimation?.setOnClickListener { doAnimation() }
        buttonShow?.setOnClickListener { showAnimation() }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
        buttonDoAnimation = null
        buttonShow = null
        cardView = null
    }

    private fun doAnimation() {
        buttonDoAnimation?.isEnabled = false
        buttonShow?.isEnabled = false

        handler?.postDelayed({
            buttonShow?.isEnabled = true
            buttonDoAnimation?.isEnabled = true
        }, ANIMATION_TIME * 4)

        val animationBannerUpConstraintSet = ConstraintSet().apply {
            clone(parentView)
            connect(
                cardView?.id ?: 0,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            clear(cardView?.id ?: 0, ConstraintSet.BOTTOM)
        }

        val transition = AutoTransition().apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = ANIMATION_TIME
        }

        handler?.postDelayed({

            parentView?.let {
                TransitionManager.beginDelayedTransition(it, transition)
            }

            animationBannerUpConstraintSet.applyTo(parentView)
        }, ANIMATION_TIME)

        handler?.postDelayed({
            parentView?.let {
                TransitionManager.beginDelayedTransition(it, transition)
            }
            val restoreConstraintSet = ConstraintSet().apply {
                clone(parentView)
                connect(
                    cardView?.id ?: 0,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    0
                )
                clear(cardView?.id ?: 0, ConstraintSet.TOP)
            }
            restoreConstraintSet.applyTo(parentView)

            buttonShow?.text = "SHOW"
            isShown = false
        }, ANIMATION_TIME * 3)
    }

    private fun showAnimation() {
        isShown = isShown.not()
        buttonShow?.isEnabled = false

        handler?.postDelayed({
            buttonShow?.isEnabled = true
        }, ANIMATION_TIME)

        val transition = AutoTransition().apply {
            interpolator = AccelerateDecelerateInterpolator()
            duration = ANIMATION_TIME
        }

        val animationBannerUpConstraintSet = if (isShown) {
            buttonShow?.text = "HIDE"
            ConstraintSet().apply {
                clone(parentView)
                connect(
                    cardView?.id ?: 0,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    0
                )
                clear(cardView?.id ?: 0, ConstraintSet.BOTTOM)
            }
        } else {
            buttonShow?.text = "SHOW"
            ConstraintSet().apply {
                clone(parentView)
                connect(
                    cardView?.id ?: 0,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                    0
                )
                clear(cardView?.id ?: 0, ConstraintSet.TOP)
            }
        }

        parentView?.let {
            TransitionManager.beginDelayedTransition(it, transition)
            animationBannerUpConstraintSet.applyTo(it)
        }
    }
}