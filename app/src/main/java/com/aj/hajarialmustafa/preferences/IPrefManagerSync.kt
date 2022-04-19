package com.aj.hajarialmustafa.preferences

import java.io.IOException

interface IPrefManagerSync {
    @Throws(IOException::class)
    fun getLocalJson(): String?

    fun setLocalJson(localJson: String?)
}