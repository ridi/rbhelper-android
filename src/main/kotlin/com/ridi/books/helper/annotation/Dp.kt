package com.ridi.books.helper.annotation

import android.support.annotation.Dimension

/**
 * Created by kering on 2017. 3. 2..
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE)
@Dimension(unit = Dimension.DP)
annotation class Dp
