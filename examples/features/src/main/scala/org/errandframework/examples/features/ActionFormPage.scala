package org.errandframework.examples.features

import org.errandframework.http._
import org.errandframework.components.forms._
import org.errandframework.components.pages.PageResponse
import org.errandframework.components._

import FeaturesExampleApplication._

/**
 * A trait that encapsulates the common elements used by both ActionFormPage and ComplexActionFormPage.
 */
trait ActionFormPageElements {

  // These fields are the model for this form.  There could be a separate class for them, but since this is a
  // simple example, we'll just keep them here.

  protected var name: String = ""
  protected var age: Int = 0
  protected var gender: Option[Gender] = None
  protected var agree: Boolean = false

  protected def displayGender = gender match {
    case Some(Male) => "male"
    case Some(Female) => "female"
    case _ => "unspecified"
  }

  protected def submitMessage = "Welcome, " + name + ". Your age is " + age + " and your gender is " + displayGender + "."

  // The fields bind to the model using a pair of functions.
  // Note that the fields don't have to be defined as part of the form.  They are used by the form but
  // are not children of the form or contained within the form.  Most components in Errand are independent and
  // may be combined arbitrarily at render time.
  // The fields all have IdBehavior, which generates a unique XML ID for each one.  This is because the fields
  // are used with LeftLabeledField and RightLabeledField, which require the field to have an ID to reference from
  // the <label> tag.

  protected val nameField = TextField[String]("name", name, name = _, IdBehavior())
  protected val ageField = TextField[Int]("age", age, age = _, IdBehavior())
  protected val genderField = SelectField[Option[Gender]]("gender", gender, gender = _,
    Seq(None -> "", Some(Male) -> "Male", Some(Female) -> "Female"), IdBehavior())(new OptionCodec(GenderCodec))
  protected val agreeField = CheckboxField("agree", agree, agree = _, IdBehavior())

  protected val editors = Seq(nameField, ageField, genderField, agreeField)

  // This is a validation function that checks the agree field.

  protected def validateForm = {
    var ok = true
    if (name.length == 0) {
      Message.error("You must enter your name.")
      ok = false
    }
    if (age < 13) {
      Message.error("You must be at least 13 to use this framework!")
      ok = false
    }
    if (!agree) {
      Message.error("You must check the agree box.")
      ok = false
    }
    ok
  }

  protected val formsLink = Link("Forms", rootLocation.toUrl())
}

/**
 * Demonstrates the ActionForm class.
 */
class ActionFormPage extends FeaturePage with ActionFormPageElements {

  def featureContent = {
    <div>
      {ActionFormPage.description}
      {actionForm.render}
      <p>{formsLink.render}</p>
    </div>
  }

  private val actionForm = new ActionForm {

    def content = {
      <div>{renderEditors(editors)}</div>
      <div>{saveButton.render}</div>
    }

    // The save button cannot be common because it needs to know what page to display in the event of an error.

    private val saveButton = ActionButton(ActionFormPage.this, "Save", editors, validateForm) {
      Message.info(submitMessage)
      PageResponse(ActionFormPage.this)
    }
  }

  override def title = "ActionForm"
}

object ActionFormPage {

  val description = <p>This first example is a simple form that uses the built-in renderEditors method to display the form.
    This is useful for prototyping and getting forms up and running quickly.</p>
}

class ComplexActionFormPage extends FeaturePage with ActionFormPageElements {

  def featureContent = {
    <div>
      {ComplexActionFormPage.description}
      {complexActionForm.render}
      <p>{formsLink.render}</p>
    </div>
  }

  override def headContent = StyleSheet("""
input[name=name] {
    width: 200px;
}
input[name=age] {
    width: 40px;
}
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
    margin-left: 110px;
}
.errand-RightLabeledField {
    clear: both;
    margin-left: 110px;
}
""")

  private val complexActionForm = new ActionForm {

    def content = {
      <div>
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
      </div>
    }

    private val checkAgeButton = ActionButton(ComplexActionFormPage.this, "Check Age", Seq(ageField), true) {
      if (age < 13)
        Message.warning("You must be at least 13 to use this framework!")
      else
        Message.info("Age check passed.")
      PageResponse(ComplexActionFormPage.this)
    }

    private val saveButton = ActionButton(ComplexActionFormPage.this, "Save", editors, validateForm) {
      Message.info(submitMessage)
      PageResponse(ComplexActionFormPage.this)
    }

    private val saveAndCloseButton = ActionButton(ComplexActionFormPage.this, "Save and Close", editors, validateForm) {
      Message.info(submitMessage)
      RedirectResponse(rootLocation.toUrl())
    }

    private val labeledNameField = LeftLabeledField("Name", nameField)
    private val labeledAgeField = LeftLabeledField("Age", ageField)
    private val labeledGenderField = LeftLabeledField("Gender", genderField)
    private val labeledAgreeField = RightLabeledField("I agree to everything", agreeField)
  }

  override def title = "Complex ActionForm"
}

object ComplexActionFormPage {

  val description = <p>This more complex example has labels and three different buttons.  ActionForm does not have its own
    controller. Instead, each button has its own controller with its own validation logic and its own
    callback.</p>
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