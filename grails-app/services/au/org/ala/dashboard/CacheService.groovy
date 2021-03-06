package au.org.ala.dashboard

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.converters.JSON

/**
 * Handles caching of service responses (after transforming).
 * Uses passed closures to handle service requests - so remains independent
 * of the source of information.
 * Implements the info source for 'static' counts read from a config file.
 */
class CacheService {

    static cache = [:]

    /**
     * Returns the cached results for the specified key if available and fresh
     * else calls the passed closure to get the results (and cache them).
     * @param key for cache storage
     * @param source closure to retrieve the results if required
     * @param maxAgeInDays the maximum age of the cached results
     * @return the results
     */
    def get(String key, Closure source, int maxAgeInDays = 1) {
        def cached = cache[key]
        if (cached && cached.resp && !(new Date().after(cached.time + maxAgeInDays))) {
            log.info "using cache for " + key
            return cached.resp
        }
        log.debug "retrieving " + key
        def results = source.call()
        cache.put key, [resp: results, time: new Date()]
        return results
    }

    def clear(key) {
        cache[key]?.resp = null
    }

    def clear() {
        cache = [:]
    }

    /**
     * Info provider based on an external metadata file.
     * Loading any key will load results for all keys in the file.
     * @param key the type of info requested
     * @return
     */
    def loadStaticCacheFromFile(key) {
        log.info 'loading static data from file'
        def json = new File(ConfigurationHolder.config.dashboard.data.file as String).text
        if (json) {
            JSON.parse(json).each { k,v ->
                cache.put k, [resp: v, time: new Date()]
            }
        }
        return cache[key]?.resp
    }
}
