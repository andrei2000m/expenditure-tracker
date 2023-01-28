package config

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import routes.categoryRouting
import routes.subcategoryRouting

fun Application.configureRouting() {
    routing {
        categoryRouting()
        subcategoryRouting()

        //TODO: Create object to represent parameters for creating transaction
//            post("/transaction") {
//                val subcategoryName = call.receive<String>()
//                insertSubcategory(subcategoryName)
//            }
    }
}