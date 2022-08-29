package com.seanshubin.kotlin.tryme.domain.json.store

import com.seanshubin.kotlin.tryme.domain.json.editor.JsonStringKeyValueStore
import com.seanshubin.kotlin.tryme.domain.json.editor.KeyValueStore
import com.seanshubin.kotlin.tryme.domain.json.util.JsonUtil

class JsonStringKeyValueStoreTest : KeyValueStoreTestBase() {
    override fun createStore(): KeyValueStore {
        return JsonStringKeyValueStore("")
    }

    override fun createStore(initialValue: Map<*, *>): KeyValueStore {
        val initialText = JsonUtil.pretty.writeValueAsString(initialValue)
        return JsonStringKeyValueStore(initialText)
    }
}
