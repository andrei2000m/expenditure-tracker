package routes

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import sql.insertSubcategory

fun Route.subcategoryRouting() {
    route("subcategory") {
        post {
            val (categoryName, subcategoryName) = call.receive<Pair<String, String>>()
            insertSubcategory(categoryName, subcategoryName)
        }
    }
}