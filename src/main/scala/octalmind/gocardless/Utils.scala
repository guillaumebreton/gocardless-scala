package octalmind.gocardless

object Utils {
  def getFieldName[T](implicit m: reflect.ClassTag[T]): String = {
    val className = m.runtimeClass.getSimpleName()
    val fieldName = if (className.endsWith("Request")) {
      className.substring(0, className.lastIndexOf("Request"))
    } else {
      className
    }
    camelToUnderscores(fieldName) + "s"
  }
  def camelToUnderscores(name: String) = "(?<!^)[A-Z\\d]".r.replaceAllIn(name, { m ⇒
    "_" + m.group(0).toLowerCase()
  }).toLowerCase()
}
