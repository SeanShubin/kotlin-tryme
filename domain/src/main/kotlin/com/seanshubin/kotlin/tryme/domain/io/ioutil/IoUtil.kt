package com.seanshubin.kotlin.tryme.domain.io.ioutil

import java.io.*
import java.nio.charset.Charset

fun InputStream.sendTo(outputStream: OutputStream) {
  var byte = read()
  while (byte != -1) {
    outputStream.write(byte)
    byte = read()
  }
}

fun Reader.sendTo(writer: Writer) {
  var char = read()
  while (char != -1) {
    writer.write(char)
    char = read()
  }
}

fun InputStream.consumeBytes(): ByteArray {
  val outputStream = ByteArrayOutputStream()
  sendTo(outputStream)
  return outputStream.toByteArray()
}

fun InputStream.consumeLines(charset: Charset): List<String> =
    toReader(charset).consumeLines()

fun InputStream.toReader(charset: Charset): Reader =
    InputStreamReader(this, charset)

fun Reader.consumeLines(): List<String> =
    toBufferedReader().consumeLines()

fun BufferedReader.consumeLines(): List<String> {
  val lines = mutableListOf<String>()
  var line = readLine()
  while (line != null) {
    lines.add(line)
    line = readLine()
  }
  return lines
}

fun Reader.toBufferedReader(): BufferedReader = BufferedReader(this)

fun Reader.consumeString(): String {
  val writer = StringWriter()
  sendTo(writer)
  return writer.buffer.toString()
}

fun ByteArray.toInputStream(): InputStream = ByteArrayInputStream(this)

fun String.toInputStream(charset: Charset): InputStream = toByteArray(charset).toInputStream()

fun InputStream.consumeString(charset: Charset): String = String(consumeBytes(), charset)

fun String.toReader(): Reader = StringReader(this)

fun String.toIterator(): Iterator<Char> = toReader().toIterator()

fun String.sendTo(charset: Charset, outputStream: OutputStream) =
    outputStream.write(toByteArray(charset))

fun ByteArray.toString(charset: Charset): String = String(this, charset)

fun ByteArray.sendTo(outputStream: OutputStream) =
    toInputStream().sendTo(outputStream)

fun Reader.toIterator(): Iterator<Char> {
  return object : Iterator<Char> {
    var current = read()
    override fun hasNext(): Boolean {
      return current != -1
    }

    override fun next(): Char {
      val old = current
      current = read()
      return old.toChar()
    }
  }
}

fun InputStream.toBufferedReader(charset: Charset): BufferedReader =
    BufferedReader(toReader(charset))

fun InputStream.toLineIterator(charset: Charset): Iterator<String> =
    toBufferedReader(charset).toLineIterator()

fun BufferedReader.toLineIterator(): Iterator<String> {
  return object : Iterator<String> {
    var current = readLine()
    override fun hasNext(): Boolean {
      return current != null
    }

    override fun next(): String {
      val old = current
      current = readLine()
      return old
    }
  }
}

fun InputStream.toNumberedLineIterator(charset: Charset): Iterator<NumberedLine> = toBufferedReader(charset).toNumberedLineIterator()

fun BufferedReader.toNumberedLineIterator(): Iterator<NumberedLine> {
  return object : Iterator<NumberedLine> {
    var current = readLine()
    var index = 0
    override fun hasNext(): Boolean {
      return current != null
    }

    override fun next(): NumberedLine {
      val old = current
      current = readLine()
      index++
      return NumberedLine(index, old)
    }
  }
}
