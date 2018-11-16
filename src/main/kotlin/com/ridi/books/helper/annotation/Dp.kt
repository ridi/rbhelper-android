package com.ridi.books.helper.annotation

import androidx.annotation.Dimension

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.LOCAL_VARIABLE
)
@Dimension(unit = Dimension.DP)
annotation class Dp
