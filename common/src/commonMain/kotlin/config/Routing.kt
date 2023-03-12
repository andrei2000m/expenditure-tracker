package config

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
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