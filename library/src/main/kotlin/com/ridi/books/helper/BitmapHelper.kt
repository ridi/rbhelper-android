package com.ridi.books.helper

import android.content.Context
import android.graphics.*
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter
import java.net.URL

/**
 * Created by kering on 2013. 12. 20..
 */
object BitmapHelper {
    // 적합한 sample size를 계산해낸다
    @JvmStatic
    fun computeSampleSize(srcWidth: Int, srcHeight: Int, reqWidth: Int, reqHeight: Int): Int {
        var sampleSize = 1

        if (srcWidth > reqWidth || srcHeight > reqHeight) {
            val halfWidth = srcWidth / 2
            val halfHeight = srcHeight / 2

            while ((halfWidth / sampleSize) > reqWidth && (halfHeight / sampleSize) > reqHeight) {
                sampleSize *= 2
            }
        }

        return sampleSize
    }

    @JvmStatic
    fun loadWebImage(urlString: String): Bitmap? {
        try {
            return BitmapFactory.decodeStream(URL(urlString).openConnection().inputStream)
        } catch (e: Exception) {
            RBLog.e(com.ridi.books.helper.BitmapHelper::class.java, "failed to load web image", e)
            return null
        }
    }
}

fun Bitmap.roundedCornerBitmap(radius: Int, antiAlias: Boolean): Bitmap {
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint()
    val rect = Rect(0, 0, width, height)
    val rectF = RectF(rect)

    paint.isAntiAlias = antiAlias
    canvas.drawARGB(0, 0, 0, 0)
    paint.color = Color.rgb(0x42, 0x42, 0x42)
    canvas.drawRoundRect(rectF, radius.toFloat(), radius.toFloat(), paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)

    return output
}

/**
 * @param gamma value ranges from 0.0 to 3.0, with 1.0 as the normal level
 * @return adjusted bitmap
 */
fun Bitmap.gammaAdjustedBitmap(context: Context, gamma: Float): Bitmap? = try {
    val image = GPUImage(context)
    image.setFilter(GPUImageGammaFilter(gamma))
    image.getBitmapWithFilterApplied(this)
} catch (e: Exception) {
    RBLog.e(javaClass, e)
    null
}
