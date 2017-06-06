package models

import anorm._
import play.api.Play.current
import play.api.db._

case class Contact(id: Long, name: String, emailAddress: String)

object Contact {

  def all = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM contacts")().map { row =>
        Contact(
          id = row[Long]("id"),
          name = row[String]("name"),
          emailAddress = row[String]("emailAddress")
        )
      }.toList
    }
  }

  def create(contact: Contact) {
    DB.withConnection { implicit connection =>
      SQL("INSERT INTO contacts(name, emailAddress) VALUES({name}, {emailAddress})").on(
        "name" -> contact.name,
        "emailAddress" -> contact.emailAddress
      ).execute()
    }
  }

  def get(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM contacts WHERE id = {id}").on("id" -> id)().headOption.map { row =>
        Contact(
          id = row[Long]("id"),
          name = row[String]("name"),
          emailAddress = row[String]("emailAddress")
        )
      }
    }
  }

  def update(id: Long, contact: Contact) {
    DB.withConnection { implicit connection =>
      SQL("UPDATE contacts SET name = {name}, emailAddress = {emailAddress} where id={id}").on(
        "id" -> id,
        "name" -> contact.name,
        "emailAddress" -> contact.emailAddress
      ).execute()
    }
  }

  def delete(id: Long) {
    DB.withConnection { implicit connection =>
      SQL("DELETE FROM contacts WHERE id = {id}").on("id" -> id).execute()
    }
  }

  import play.api.data.Forms._
  import play.api.data._

  val form = Form(
    mapping(
      "id" -> ignored(0L),
      "name" -> nonEmptyText,
      "emailAddress" -> email
    )(Contact.apply)(Contact.unapply)
  )
}
