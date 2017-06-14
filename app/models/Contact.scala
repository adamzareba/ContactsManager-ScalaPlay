package models

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

case class Contact(id: Long, name: String, age: Int, emailAddress: String)

class ContactService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import dbConfig.driver.api._

  private[models] val Contacts = TableQuery[ContactsTable]

  //  def all: DBIO[Seq[Contact]] = {
  //    Contacts.result
  //  }

  def all: Future[List[Contact]] = {
    db.run(Contacts.to[List].result)
  }

  def create(contact: Contact): Future[Long] = {
    db.run(Contacts returning Contacts.map(_.id) += contact)
  }

  def get(id: Long): Future[Contact] = {
    db.run(Contacts.filter(_.id === id).result.head)
  }

  def update(id: Long, contact: Contact): Future[Int] = {
    val computerToUpdate: Contact = contact.copy(id)
    db.run(Contacts.filter(_.id === id).update(computerToUpdate))
  }

  def delete(id: Long): Future[Int] = {
    db.run(Contacts.filter(_.id === id).delete)
  }

  import play.api.data.Forms._
  import play.api.data._

  val form = Form(
    mapping(
      "id" -> ignored(0L),
      "name" -> nonEmptyText,
      "age" -> number(min = 0, max = 150),
      "emailAddress" -> email
    )(Contact.apply)(Contact.unapply)
  )

  private[models] class ContactsTable(tag: Tag) extends Table[Contact](tag, "CONTACTS") {

    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def name = column[String]("NAME")

    def age = column[Int]("AGE")

    def emailAddress = column[String]("EMAILADDRESS")

    def * = (id, name, age, emailAddress) <> (Contact.tupled, Contact.unapply)

    def ? = (id.?, name.?, age.?, emailAddress.?).shaped.<>({ r => import r._; _1.map(_ => Contact.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}
