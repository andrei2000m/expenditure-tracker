package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
                call.respond(HttpStatusCode.Created)
            } catch (e: ExposedSQLException) {
                call.respondText("Category $categoryName already exists", status = HttpStatusCode.BadRequest)
            }
        }
    }
}