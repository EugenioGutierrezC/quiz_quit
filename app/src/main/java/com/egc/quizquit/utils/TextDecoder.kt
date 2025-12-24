package com.egc.quizquit.utils

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun String.urlDecodeUtf8(): String =
    URLDecoder.decode(this, StandardCharsets.UTF_8.name())