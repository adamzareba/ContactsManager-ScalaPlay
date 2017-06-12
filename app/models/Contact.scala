package models

import javax.inject.Inject

import anorm._
import play.api.db._

case class Contact(id: Long, name: String, age: Int, emailAddress: String)

class ContactService @Inject()(database: Database) {

  implicit val parser: RowParser[Contact] = Macro.namedParser[Contact]

  def all = {
    database.withConnection { implicit connection =>
      SQL("SELECT * FROM contacts").as(parser.*)
    }
  }

  def create(contact: Contact) {
    database.withConnection { implicit connection =>
      SQL("INSERT INTO contacts(name, age, emailAddress) VALUES({name}, {age}, {emailAddress})").on(
        "name" -> contact.name,
        "age" -> contact.age,
        "emailAddress" -> contact.emailAddress
      ).executeInsert()
    }
  }

  def get(id: Long) = {
    database.withConnection { implicit connection =>
      SQL("SELECT * FROM contacts WHERE id = {id}").on("id" -> id).as(parser.single)
    }
  }

  def update(id: Long, contact: Contact) {
    database.withConnection { implicit connection =>
      SQL("UPDATE contacts SET name = {name}, emailAddress = {emailAddress}, age = {age} where id={id}").on(
        "id" -> id,
        "name" -> contact.name,
        "age" -> contact.age,
        "emailAddress" -> contact.emailAddress
      ).executeUpdate()
    }
  }

  def delete(id: Long) {
    database.withConnection { implicit connection =>
      SQL("DELETE FROM contacts WHERE id = {id}").on("id" -> id).executeUpdate()
    }
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
}
