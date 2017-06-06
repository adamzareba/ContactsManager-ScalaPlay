package controllers

import models.Contact
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._

object Contacts extends Controller {

  def index = Action {
    val contacts = Contact.all

    Ok(views.html.index(contacts, Contact.form))
  }

  def create = Action { implicit request =>
    Contact.form.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Contact.all, errors)),
      contact => {
        Contact.create(contact)
        Redirect(routes.Contacts.index())
      }
    )
  }

  def edit(id: Long) = Action {
    Contact.get(id).map { contact =>
      Ok(views.html.edit(id, Contact.form.fill(contact)))
    } getOrElse {
      Redirect(routes.Contacts.index())
    }
  }

  def update(id: Long) = Action { implicit request =>
    Contact.form.bindFromRequest.fold(
      errors => BadRequest(views.html.edit(id, errors)),
      contact => {
        Contact.update(id, contact)
        Redirect(routes.Contacts.index())
      }
    )
  }

  def delete(id: Long) = Action {
    Contact.delete(id)
    Redirect(routes.Contacts.index())
  }
}
