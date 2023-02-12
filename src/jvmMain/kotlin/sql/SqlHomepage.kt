package sql

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.sum
import org.jetbrains.exposed.sql.transactions.transaction

fun calculateTotalValuesPerCategory(): Map<String, Double> {
    val sumAlias = Transactions.value.sum().alias("sum")

    val categoryQuery = Transactions
        .join(Subcategories, JoinType.INNER, additionalConstraint = { Transactions.subcategoryId eq Subcategories.subcategoryId } )
        .join(Categories, JoinType.RIGHT, additionalConstraint = { Subcategories.categoryId eq Categories.categoryId } )
        .slice(Categories.name, sumAlias)
        .selectAll()
        .groupBy(Categories.name)

    return transaction {
        categoryQuery.fold(mapOf()) { map, row ->
            map.plus(Pair(row[Categories.name], row[sumAlias] ?: 0.0))
        }
    }
}

fun getAllCategories(): List<Category> {
    return transaction {
        Categories.selectAll().map { Category.transformRow(it) }
    }
}