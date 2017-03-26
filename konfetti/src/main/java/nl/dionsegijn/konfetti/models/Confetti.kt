package nl.dionsegijn.konfetti.models

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import java.util.*

/**
 * Created by dionsegijn on 3/26/17.
 */
class Confetti(var location: Vector,
               val color: Int,
               var velocity: Vector = Vector(),
               var acceleration: Vector = Vector(0f, 0f),
               var mass: Float = 4f,
               var lifespan: Float = 255f) {

    private var width = 40f
    private val paint: Paint = Paint()

    private var rotationSpeed = 1
    private var rotation = 0
    private var rotationWidth = width

    init {
        paint.color = color
        rotationSpeed = Random().nextInt(3) + 1
    }

    fun getSize(): Float {
        return width
    }

    fun isDead(): Boolean {
        return lifespan <= 0
    }

    fun applyForce(force: Vector) {
        val f = force.copy()
        f.div(mass)
        acceleration.add(f)
    }

    fun render(canvas: Canvas) {
        update()
        display(canvas)
    }

    fun update() {
        velocity.add(acceleration)
        location.add(velocity)

        if(location.y > 500) {
            if (lifespan < 0 || lifespan == 0f) lifespan = 0f
            else lifespan -= 5f
        }

        rotation += rotationSpeed
        if (rotation > 360) rotation = 0

        rotationWidth -= rotationSpeed
        if(rotationWidth < 0) rotationWidth = width
    }

    fun display(canvas: Canvas) {
        val rect: RectF = RectF(
                location.x + (width - rotationWidth), // center of rotation
                location.y,
                location.x + rotationWidth,
                location.y + getSize())

        paint.alpha = if (lifespan < 0) 0 else lifespan.toInt()

        canvas.save()
        canvas.rotate(rotation.toFloat(), rect.centerX(), rect.centerY())
        canvas.drawRect(rect, paint)
        canvas.restore()
    }

}