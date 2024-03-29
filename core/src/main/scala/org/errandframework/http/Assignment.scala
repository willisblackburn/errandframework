/*
 * Errand Framework Copyright (c) 2002-2011 SIG Networks Corporation
 */

package org.errandframework.http

/**
 * Assignment is a parameter with a specific value.
 * Parameters can implicitly become Assignments through binding with their own current value,
 * or the application can provide another value.
 */
case class Assignment[T](parameter: RequestParameter[T], value: T) {

  def encode() = parameter.encode(value)

  def encodeAsString() = parameter.encodeAsString(value)

  final override def equals(that: Any) = that match {
    case assignment: Assignment[_] => parameter == assignment.parameter
    case _ => false
  }

  final override def hashCode() = parameter.hashCode
}

object Assignment {

  /**
   * Implicitly binds a Parameter using its current value.
   */
  implicit def fromCurrentValue[T](parameter: RequestParameter[T]) = parameter.toAssignment

  /**
   * Implicitly binds a Parameter using some other compatible parameter.
   * The input is a Tuple2[Parameter[T], Parameter[T]] so the syntax <code>parameter -> value</code> may be converted
   * implicitly into a ValueParameter.
   */
  implicit def fromParameter[T](pv: (Parameter[T], RequestParameter[T])) = pv._1.toAssignment(pv._2.get)

  /**
   * Implicitly binds a Parameter using some other compatible value.
   * The input is a Tuple2[Parameter[T], T] so the syntax <code>parameter -> value</code> may be converted
   * implicitly into a ValueParameter.
   */
  implicit def fromValue[T](pv: (RequestParameter[T], T)) = pv._1.toAssignment(pv._2)
}
