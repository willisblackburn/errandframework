package org.errandframework.examples.features

import FeaturesExampleApplication._
import org.errandframework.components.{MessagePanel, Component}
import org.errandframework.components.forms.{SelectField, TextField, Form}
import org.errandframework.http.{OptionCodec, Methods, Codec}

/**
 * Demonstrates form-generation and handling features.
 */
class FormsPage extends FeaturePage {

  private var name: String = ""
  private var age: Int = 0
  private var gender: Option[Gender] = None

  def featureContent = {
    messagePanel.render ++
    plainForm.render ++
    componentForm.render
  }

  val messagePanel = new MessagePanel

  val plainForm: Component = {
    <form action={searchLocation.toUrl()} method="GET">
      <div>Query: <input name="q" type="text"/></div>
      <div>Page: <select name="page"><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select></div>
      <div><button type="submit">Search</button></div>
    </form>
  }

  val componentForm = new Form {

    def content = {
      <div>Query: <input name="q" type="text"/></div>
      <div>Page: <select name="page"><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option></select></div>
      <div><button type="submit">Search</button></div>
    }

    def url = searchLocation.toUrl()

    override def method = Methods.POST
  }

  // Note that the fields don't have to be defined as part of the form.  They are used by the form but
  // are not children of the form or contained within the form.  Most components in Errand are independent and
  // may be combined arbitrarily at render time.

  private val nameField = TextField[String]("name", name, name = _)
  private val genderField = SelectField[Option[Gender]]("gender", gender, gender = _,
    Seq(None -> "", Some(Male) -> "Male", Some(Female) -> "Female"))(new OptionCodec(GenderCodec))
  private val ageField = TextField[Int]("age", age, age = _)

  override def title = "Forms"
}

object FormsPage {



}

sealed trait Gender
case object Male extends Gender
case object Female extends Gender

object GenderCodec extends Codec[Gender] {
  def encode(value: Gender) = value match {
    case Male => "M"
    case Female => "F"
  }
  def decode(raw: String) = raw match {
    case "M" => Male
    case "F" => Female
  }
}