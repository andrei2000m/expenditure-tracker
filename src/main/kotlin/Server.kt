import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.html.respondHtml
import io.ktor.server.netty.Netty
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.title
import sql.calculateTotalValuesPerCategory
import sql.getAllCategories
import sql.insertCategory
import sql.insertSubcategory

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        routing {
            get("/") {
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }

            get("/categories") {
                call.respond(getAllCategories())
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
    }.start(wait = true)
}