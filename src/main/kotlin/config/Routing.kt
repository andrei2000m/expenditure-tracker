package config

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import sql.calculateTotalValuesPerCategory
import sql.getAllCategories
import sql.insertCategory
import sql.insertSubcategory

fun Application.configureRouting() {
    routing {
        get("/categories") {
            val result = Json.encodeToJsonElement(getAllCategories())
            call.respond(result)
        }

        get("/categoryValues") {
            call.respond(calculateTotalValuesPerCategory())
        }

        post("/category") {
            val categoryName = call.receive<String>()
            insertCategory(categoryName)
        }

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