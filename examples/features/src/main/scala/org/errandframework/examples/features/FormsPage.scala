package org.errandframework.examples.features

import org.errandframework.http._
import org.errandframework.components.forms._

import FeaturesExampleApplication._
import org.errandframework.components.pages.{Page, PageResponse}
import org.errandframework.components._

/**
 * Demonstrates form-generation and handling features.
 */
class FormsPage extends FeaturePage {

  override def headContent = StyleSheet("""
.errand-LeftLabeledField {
    clear: both;
}
.errand-LeftLabeledField label {
    display: block;
    float: left;
    text-align: right;
    width: 100px;
}
.errand-LeftLabeledField-field {
    display: block;
    margin-left: 120px;
}
.errand-RightLabeledField {
    clear: both;
    margin-left: 120px;
}
""")

  def featureContent = {
    <div>
      <h2>Messages</h2>
      {messagePanel.render}
      <h2>Plain Form</h2>
      <p>You can use plain old HTML to create forms. The action URL can be either external or internal.</p>
      {plainForm.render}
      <h2>Form as a Component</h2>
      <p>You can also build forms as components.  The base Form class will render the form but does not offer any
        other features.</p>
      {componentForm.render}
      <h2>Action Form</h2>
      <p>In most cases you'll want ActionForm.  It supports the general use case of displaying a form, letting the
        user submit it, validating the fields, and finally either taking some action after a
        successful validation or redisplaying the form with appropriate error messages.</p>
      <p>This first example is a simple form that uses the built-in renderEditors method to display the form.
        This is useful for prototyping and getting forms up and running quickly.</p>
      {simpleActionForm.render}
      <p>This more complex example has labels and three different buttons.  ActionForm does not have its own
        controller. Instead, each button has its own controller with its own validation logic and its own
        callback.</p>
      {complexActionForm.render}
    </div>
  }

  private val messagePanel = new MessagePanel

  private val plainForm: Component = {
    <form action="http://www.google.com/search" method="GET">
      <div>Query: <input name="q" type="text"/></div>
      <div>Start: <select name="start"><option>0</option><option>10</option><option>20</option><option>30</option><option>40</option></select></div>
      <div><button type="submit">Search</button></div>
    </form>
  }

  private val componentForm = new Form {

    def content = {
      <div>Query: <input name="q" type="text"/></div>
      <div>Start: <select name="start"><option>0</option><option>10</option><option>20</option><option>30</option><option>40</option></select></div>
      <div><button type="submit">Search</button></div>
    }

    def url = "http://www.google.com/search"

    override def method = Methods.GET
  }

  private var name: String = ""
  private var age: Int = 0
  private var gender: Option[Gender] = None
  private var agree: Boolean = false

  private def displayGender = gender match {
    case Some(Male) => "male"
    case Some(Female) => "female"
    case _ => "unspecified"
  }

  private def submitMessage = "Welcome, " + name + ". Your age is " + age + " and your gender is " + displayGender + "."

  // Note that the fields don't have to be defined as part of the form.  They are used by the form but
  // are not children of the form or contained within the form.  Most components in Errand are independent and
  // may be combined arbitrarily at render time.
  // The fields all have IdBehavior, which generates a unique XML ID for each one.  This is because the fields
  // are used with LeftLabeledField and RightLabeledField, which require the field to have an ID to reference from
  // the <label> tag.

  private val nameField = TextField[String]("name", name, name = _, IdBehavior())
  private val ageField = TextField[Int]("age", age, age = _, IdBehavior())
  private val genderField = SelectField[Option[Gender]]("gender", gender, gender = _,
    Seq(None -> "", Some(Male) -> "Male", Some(Female) -> "Female"), IdBehavior())(new OptionCodec(GenderCodec))
  private val agreeField = CheckboxField("agree", agree, agree = _, IdBehavior())

  private val editors = Seq(nameField, ageField, genderField, agreeField)

  // This is a validation function that checks the agree field.

  private def validateForm = {
    var ok = true
    if (!agree) {
      Message.error("You must check the agree box.")
      ok = false
    }
    if (name.length == 0) {
      Message.error("You must enter your name.")
      ok = false
    }
    ok
  }

  private val simpleActionForm = new ActionForm {

    def content = {
      <div>{renderEditors(editors)}</div>
      <div>{saveButton.render}</div>
    }

    private val saveButton = ActionButton(FormsPage.this, "Save", editors, validateForm) {
      Message.info(submitMessage)
      PageResponse(FormsPage.this)
    }
  }

  private val complexActionForm = new ActionForm {

    def content = {
      <div>
        {labeledNameField.render}
        {labeledAgeField.render}
        {labeledGenderField.render}
        {labeledAgreeField.render}
      </div>
      <div>
        {checkAgeButton.render}
        {saveButton.render}
        {saveAndCloseButton.render}
      </div>
    }

    private val checkAgeButton = ActionButton(FormsPage.this, "Check Age", Seq(ageField), true) {
      if (age < 13)
        Message.warning("You must be at least 13 to use this framework!")
      else
        Message.info("Age check passed.")
      PageResponse(FormsPage.this)
    }

    private val saveButton = ActionButton(FormsPage.this, "Save", editors, validateForm) {
      Message.info(submitMessage)
      PageResponse(FormsPage.this)
    }

    private val saveAndCloseButton = ActionButton(FormsPage.this, "Save and Close", editors, validateForm) {
      Message.info(submitMessage)
      RedirectResponse(rootLocation.toUrl())
    }

    private val labeledNameField = LeftLabeledField("Name", nameField)
    private val labeledAgeField = LeftLabeledField("Age", ageField)
    private val labeledGenderField = LeftLabeledField("Gender", genderField)
    private val labeledAgreeField = RightLabeledField("I agree to everything", agreeField)
  }

  override def title = "Forms"
}

// Example of a custom type and associated codec.
// In the actual code we use this with OptionCodec to produce a codec that can handle three values: male, female,
// and unknown.  We could have added a third case object called Unknown, but the OptionCodec approach might work
// better with a database, in which the male and female values map to strings but the unknown value maps to null.

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