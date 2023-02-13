package routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.jetbrains.exposed.exceptions.ExposedSQLException
import sql.calculateTotalValuesPerCategory
import sql.getAllCategories
import sql.insertCategory

fun Route.categoryRouting() {
    route("category") {
        get {
            call.respond(getAllCategories())
        }
        get("values") {
            call.respond(calculateTotalValuesPerCategory())
        }
        post {
            val categoryName = call.receive<String>()
            try {
                insertCategory(categoryName)
                call.respondText("OK", status = HttpStatusCode.OK)
            } catch (e: ExposedSQLException) {
                call.respondText("Category $categoryName already exists", status = HttpStatusCode.BadRequest)
            }
        }
    }
}