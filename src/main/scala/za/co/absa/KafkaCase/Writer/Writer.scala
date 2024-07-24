package za.co.absa.KafkaCase.Writer

trait Writer[TKey, TValue] extends AutoCloseable {
  def Write(key: TKey, value: TValue): Unit
  def Flush(): Unit

  def WriteSync(key: TKey, value: TValue): Unit = {
    Write(key, value)
    Flush()
  }
}
