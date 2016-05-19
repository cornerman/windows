package scala.scalanative.native

object ToUnsigned {
  //TODO: how to do this properly:
  // https://github.com/scala-native/scala-native/issues/41
  implicit class LongToUnsigned(val num: Long) {
    def toULong = new ULong(num)
    def toUInt = new UInt(num.toInt)
    def toUShort = new UShort(num.toShort)
    def toUByte = new UByte(num.toByte)
  }
  implicit class IntToUnsigned(val num: Int) {
    def toULong = new ULong(num.toLong)
    def toUInt = new UInt(num)
    def toUShort = new UShort(num.toShort)
    def toUByte = new UByte(num.toByte)
  }
  implicit class ShortToUnsigned(val num: Short) {
    def toULong = new ULong(num.toLong)
    def toUInt = new UInt(num.toInt)
    def toUShort = new UShort(num)
    def toUByte = new UByte(num.toByte)
  }
  implicit class ByteToUnsigned(val num: Byte) {
    def toULong = new ULong(num.toLong)
    def toUInt = new UInt(num.toInt)
    def toUShort = new UShort(num.toShort)
    def toUByte = new UByte(num)
  }
}

object Unsigned {
  //TODO: for now, don't use unsigned
  type CULong = CLong
  type CUInt = CInt
  type CUShort = CShort
  type CUChar = CChar
}
