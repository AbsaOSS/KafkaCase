package za.co.absa.KafkaCase.Writer

trait Writer[TType] extends AutoCloseable {
  def Write(key: String, value: TType): Unit
  def Flush(): Unit

  def WriteSync(key: String, value: TType): Unit = {
    Write(key, value)
    Flush()
  }
}
