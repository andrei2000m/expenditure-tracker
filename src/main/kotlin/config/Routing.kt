package config

import config.routes.categoryRouting
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import sql.insertSubcategory

fun Application.configureRouting() {
    routing {
        categoryRouting()

        post("/subcategory") {
            val (categoryName, subcategoryName) = call.receive<Pair<String, String>>()
            insertSubcategory(categoryName, subcategoryName)
        }

        //TODO: Create object to represent parameters for creating transaction
//            post("/transaction") {
//                val subcategoryName = call.receive<String>()
//                insertSubcategory(subcategoryName)
//            }
    }
}