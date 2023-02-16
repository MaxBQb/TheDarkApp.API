package lab.maxb.dark_api.infrastracture.configuration.db

import com.vladmihalcea.hibernate.type.array.StringArrayType
import org.hibernate.dialect.PostgreSQL94Dialect


@Suppress("unused")
class PostgreSQL94CustomDialect : PostgreSQL94Dialect() {
    init {
        this.registerHibernateType(2003, StringArrayType::class.java.name)
    }
}