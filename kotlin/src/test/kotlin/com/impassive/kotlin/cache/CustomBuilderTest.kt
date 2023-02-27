package com.impassive.kotlin.cache

import com.impassive.kotlin.cache.basic.CustomCache
import com.impassive.kotlin.cache.decoration.FallbackDecoration
import com.impassive.kotlin.cache.redis.CustomLettuceConnConfig
import com.impassive.kotlin.cache.redis.JsonRedisCodec
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import lombok.Data
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDate

/**
 * @author impassive
 */
internal class CustomBuilderTest {

    private var redisCache: CustomCache<Long, Item>? = null

    @BeforeEach
    fun setUp() {
        val connConfig = CustomLettuceConnConfig(
            client = RedisClient.create(),
            masterUri = RedisURI.Builder.redis("10.200.68.3", 6379).build(),
            codec = JsonRedisCodec(
                "prefix", 6, Long::class.java, Item::class.java
            )
        )

        redisCache = CustomBuilder<Long, Item>(Duration.ofMinutes(15))
            .cfg(connConfig)
            .build()

    }

    @Test
    internal fun test() {
        val item = redisCache?.get(1L)
        println(item)
    }

    @Test
    internal fun testSave() {
        val value = Item()
        redisCache?.put(1L, value)
    }

    @Test
    internal fun testDecoration() {
        var success = true

        var v2 = FallbackDecoration(null)
        val fallbackDecoration = FallbackDecoration(v2)
        fallbackDecoration.failed()
        println(v2.cnt)
        println(fallbackDecoration.value?.cnt)


    }

    @Data
    class Item {

        var key: String = ""

    }
}