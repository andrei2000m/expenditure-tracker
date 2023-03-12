package sql

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun insertCategory(categoryName: String) {
    transaction {
        Categories.insert {
            it[name] = categoryName
        }
    }
}

fun insertSubcategory(categoryName: String, subcategoryName: String) {
    transaction {
        val query = Categories.slice(Categories.categoryId).select { Categories.name eq categoryName }
        val id = query.iterator().next()[Categories.categoryId]

        Subcategories.insert {
            it[categoryId] = id
            it[name] = subcategoryName
        }
    }
}