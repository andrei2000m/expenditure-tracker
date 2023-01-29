package config

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import routes.categoryRouting
import routes.subcategoryRouting
import routes.transactionRouting

fun Application.configureRouting() {
    routing {
        categoryRouting()
        subcategoryRouting()
        transactionRouting()
    }
}