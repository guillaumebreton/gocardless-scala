package octalmind.gocardless

object Utils {
  def getFieldName[T](implicit m: reflect.ClassTag[T]): String = {
    val className = m.runtimeClass.getSimpleName()
    val suffixRegex = """(CreateRequest|UpdateRequest|Request)""".r
    val fieldName = suffixRegex.replaceFirstIn(className, "")
    camelToUnderscores(fieldName) + "s"
  }
  def camelToUnderscores(name: String) = "(?<!^)[A-Z\\d]".r.replaceAllIn(name, { m ⇒
    "_" + m.group(0).toLowerCase()
  }).toLowerCase()
}
