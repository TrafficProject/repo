/**
  * Created by Cristina on 11/8/2016.
  */
import java.io._
import java.sql.DriverManager
import java.sql.{Connection,DriverManager}

import EchoServer._
//remove if not needed
import scala.collection.JavaConversions._

object EchoServer {

  val DEFAULT_PORT = 5555

  var connection :Connection=_

  def main(args: Array[String]) {
    var port = 0
    try {
      port = java.lang.Integer.parseInt(args(0))
    } catch {
      case t: Throwable => port = DEFAULT_PORT
    }
    val sv = new EchoServer(port)
    try {
      sv.listen()
    } catch {
      case ex: Exception => println("ERROR - Could not listen for clients!")
    }
  }
}

class EchoServer(port: Int) extends AbstractServer(port) {

  def handleMessageFromClient(msg:AnyRef, client: ConnectionToClient) {
    println("Message received: " + msg + " from " + client)

    val url = "jdbc:mysql://localhost:3306/Trafic"
    val driver = "com.mysql.jdbc.Driver"
    val username = "root"
    val password = ""
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      //print(statement)

      var viteza =0
      var nr=0

      val message: Array[String] = msg.asInstanceOf[Array[String]]
      //      if (message == null)
      //      output.writeObject("NULL!!!!!")
      // else {
      val q1: String="select strazi.id from strazi where strazi.strada= '" + message(0) + "';"
      val rs1 = statement.executeQuery(q1)
      var id1: Integer=0
      while (rs1.next) {
        id1 = rs1.getInt("id")

      }


      val q2: String="select conditii_meteo.id from conditii_meteo where conditii_meteo.descriere= '" + message(1) + "';"

      val rs2 = statement.executeQuery(q2)
      var id2: Integer=0
      while (rs2.next) {
        id2 = rs2.getInt("id")

      }

      val q3: String="select perioada_zi.id from perioada_zi where perioada_zi.descriere= '" + message(2) + "';"

      val rs3 = statement.executeQuery(q3)
      var id3: Integer=0
      while (rs3.next) {
        id3 = rs3.getInt("id")

      }
      val q4: String="select starea_drumului.id from starea_drumului where starea_drumului.descriere= '" + message(3) + "';"

      val rs4 = statement.executeQuery(q4)
      var id4: Integer=0
      while (rs4.next) {
        id4 = rs4.getInt("id")

      }

      val query: String = "select date_trafic.viteza_medie_autovehicole, date_trafic.nr_client from date_trafic where date_trafic.id_strada = "+id1+ " and  date_trafic.id_cond_meteo = " +id2+ " and date_trafic.id_perioada_zi = " +id3+ " and date_trafic.id_starea_drumului = " +id4+ ";"

      val rs = statement.executeQuery(query)


      while (rs.next) {
       viteza = rs.getInt("viteza_medie_autovehicole")
        nr =rs.getInt("nr_client")

      }
      viteza=(viteza*nr+message(4).toInt)/(nr+1)
      nr=nr+1

      //val query1 = "call ACTUALIZARE("+id1+","+id2+","+id3+","+id4+","+viteza+","+nr+");"
     statement.executeUpdate("call ACTUALIZARE("+id1+","+id2+","+id3+","+id4+","+viteza+","+nr+");")


      client.sendToClient("Mersi!")
    }

    catch {
      case e: Exception => e.printStackTrace
    }
    connection.close



  }

  override  def serverStarted() {
    println("Server listening for connections on port " + getPort)
  }

  override  def serverStopped() {
    println("Server has stopped listening for connections.")
  }
}
