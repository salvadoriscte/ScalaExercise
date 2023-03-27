package models

import java.time.LocalDateTime

case class Product(id: Int, name: String, category: String, weight: Double, price: Double, creationDate: LocalDateTime)
