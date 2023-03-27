import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import models.{Order, Item, Product}

object OrderAnalyzer {
  def main(args: Array[String]): Unit = {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val startDate = LocalDateTime.parse(args(0), dateFormatter)
    val endDate = LocalDateTime.parse(args(1), dateFormatter)

    val product1 = Product(1, "Product1", "Electronics", 1.0, 100.0, LocalDateTime.parse("2022-11-01T00:00:00"))
    val product2 = Product(2, "Product2", "Books", 0.5, 50.0, LocalDateTime.parse("2022-02-01T00:00:00"))
    val product3 = Product(3, "Product3", "Toys", 0.3, 20.0, LocalDateTime.parse("2023-01-15T00:00:00"))

    val item1 = Item(1, 100.0, 10.0, 5.0, product1)
    val item2 = Item(2, 120.0, 8.0, 4.0, product2)
    val item3 = Item(3, 20.3, 8.0, 1.0, product3)

    val order1 = Order(1, "John Doe", "john@example.com", "123 Main St", 115.0, List(item1), LocalDateTime.parse("2022-02-01T12:00:00"))
    val order2 = Order(2, "Jane Smith", "jane@example.com", "456 Oak St", 212.0, List(item2, item3), LocalDateTime.parse("2023-01-05T15:30:00"))
    val order3 = Order(3, "Tom Brown", "tom@example.com", "789 Pine St", 120.0, List(item3), LocalDateTime.parse("2023-02-10T09:45:00"))

    val orders = List(order1, order2, order3)


    val customIntervals = if (args.length > 2) {
      args(2).split(",").map(range => {
        val Array(start, end) = range.split("-")
        (start.toInt, end.toInt)
      }).toList
    } else {
      List((1, 3), (4, 6), (7, 12), (13, Int.MaxValue))
    }

    val filteredOrders = filterOrdersByDate(orders, startDate, endDate)
    val groupedOrders = groupOrdersByProductAge(filteredOrders, LocalDateTime.now(), customIntervals)

    groupedOrders.foreach { case (interval, count) =>
      println(s"$interval: $count orders")
    }
  }

  /*included start and end date*/
  def filterOrdersByDate(orders: List[Order], startDate: LocalDateTime, endDate: LocalDateTime): List[Order] = {
    orders.filter(order => !order.date.isBefore(startDate) && !order.date.isAfter(endDate))
  }

  def getProductAgeInMonths(product: Product, currentDate: LocalDateTime): Int = {
    ChronoUnit.MONTHS.between(product.creationDate, currentDate).toInt
  }

  def groupOrdersByProductAge(orders: List[Order], currentDate: LocalDateTime, intervals: List[(Int, Int)]): Map[String, Int] = {
    val productAges = orders.flatMap(_.items.map(item => getProductAgeInMonths(item.product, currentDate)))

    intervals.map { case (start, end) =>
      val range = if (end == Int.MaxValue) s">12" else s"$start-$end"
      val count = productAges.count(age => age >= start && age <= end)
      (range, count)
    }.toMap
  }
}
