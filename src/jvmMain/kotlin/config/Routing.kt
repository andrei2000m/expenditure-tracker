package config

import io.ktor.server.application.Application
import io.ktor.server.http.content.angular
import io.ktor.server.http.content.singlePageApplication
import io.ktor.server.routing.routing
import routes.categoryRouting
import routes.subcategoryRouting
import routes.transactionRouting

fun Application.configureRouting() {
    routing {
        categoryRouting()
        subcategoryRouting()
        transactionRouting()
        singlePageApplication {
            angular("./src/jsMain/kotlin/dist/angular-app")
        }
    }
}