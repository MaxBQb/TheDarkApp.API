package lab.maxb.dark_api.domain.gateway

interface PersistGateway<T> {
    fun save(model: T & Any): T & Any
}

interface QueryGateway<in K, out V> {
    fun findById(id: K & Any): V?
    fun existsById(id: K & Any): Boolean = findById(id) != null
}

interface DeleteGateway<in K> {
    fun deleteById(id: K & Any)
}

interface ReadWriteGateway<in K, V> : PersistGateway<V>, QueryGateway<K, V>

interface CRUDGateway<in K, V> : ReadWriteGateway<K, V>, DeleteGateway<K>