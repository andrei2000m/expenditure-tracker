package sql

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

val databaseConnection = Database.connect("jdbc:sqlite:cheltuieli.db", driver = "org.sqlite.JDBC", setupConnection = { connection ->
    connection.createStatement().executeUpdate("PRAGMA foreign_keys = ON")
})

object Categories : Table() {
    val categoryId = integer("category_id").autoIncrement()
    val name = varchar("name", 255).uniqueIndex("UI_categoryName")
    override val primaryKey = PrimaryKey(categoryId, name = "PK_categoryId")
}

@Serializable
data class Category(val categoryId: Int, val name: String) {
    companion object {
        fun transformRow(resultRow: ResultRow) = Category(
            categoryId = resultRow[Categories.categoryId],
            name = resultRow[Categories.name]
        )
    }
}

object Subcategories : Table() {
    val subcategoryId = integer("subcategory_id").autoIncrement()
    val categoryId = reference("category_id", Categories.categoryId)
    val name = varchar("name", 255)
    override val primaryKey = PrimaryKey(subcategoryId, name = "PK_subcategoryId")
    init {
        uniqueIndex("UI_subcategoryName", categoryId, name)
    }
}

@Serializable
data class Subcategory(val subcategoryId: Int, val categoryId: Int, val name: String) {
    companion object {
        fun transformRow(resultRow: ResultRow) = Subcategory(
            subcategoryId = resultRow[Subcategories.subcategoryId],
            categoryId = resultRow[Subcategories.categoryId],
            name = resultRow[Subcategories.name]
        )
    }
}

object Transactions : Table() {
    val transactionId = integer("transaction_id").autoIncrement()
    val date = date("date")
    val subcategoryId = reference("subcategory_id", Subcategories.subcategoryId)
    val value = double("value")
    val isDebitTransaction = bool("isDebitTransaction")
    override val primaryKey = PrimaryKey(transactionId, name = "PK_transactionId")
}

@Serializable
data class Transaction(val transactionId: Int, @Serializable(DateSerializer::class) val date: LocalDate, val subcategoryId: Int, val value: Double, val isDebitTransaction: Boolean) {
    companion object {
        fun transformRow(resultRow: ResultRow) = Transaction(
            transactionId = resultRow[Transactions.transactionId],
            date = resultRow[Transactions.date],
            subcategoryId = resultRow[Transactions.subcategoryId],
            value = resultRow[Transactions.value],
            isDebitTransaction = resultRow[Transactions.isDebitTransaction]
        )
    }
}

object DateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDate = LocalDate.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: LocalDate) = encoder.encodeString(value.toString())
}