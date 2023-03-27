package models

import java.time.LocalDateTime

case class Order(id: Int, customerName: String, contact:String, shippingAddress: String, grandTotal: Double, items: List[Item], date: LocalDateTime)

