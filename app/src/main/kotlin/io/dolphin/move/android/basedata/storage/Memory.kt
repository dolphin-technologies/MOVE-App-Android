package io.dolphin.move.android.basedata.storage

import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

abstract class HashMapMemory : MutableMap<String, Any?> by ConcurrentHashMap()

class SessionMemory @Inject constructor() : HashMapMemory()

class AppMemory @Inject constructor() : HashMapMemory()