package routes

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import sql.insertSubcategory

fun Route.subcategoryRouting() {
    route("subcategory") {
        post {
            val (categoryName, subcategoryName) = call.receive<Pair<String, String>>()
            insertSubcategory(categoryName, subcategoryName)
        }
    }
}