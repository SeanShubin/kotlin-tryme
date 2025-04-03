package com.seanshubin.kotlin.tryme.domain.dynamic

abstract class CsvLineParseState(val line:String, val index:Int, val cell:String, val cells:List<String>){
    fun navigateToEnd():CsvLineParseState {
        var current = this
        while(current !is End){
            current = current.next()
        }
        return current
    }
    fun atEnd():Boolean = index >= line.length
    val currentChar get() = line[index]
    fun currentCharIsQuote():Boolean = currentChar == '\"'
    fun currentCharIsSeparator():Boolean = currentChar == ','
    fun atCellBoundary():Boolean = atEnd() || currentCharIsSeparator()
    override fun toString():String = "LineParseState(index=$index, cell=$cell, cells=$cells)"
    abstract fun next(): CsvLineParseState

    class InQuotedCell(line:String, index:Int, cell:String, cells:List<String>):CsvLineParseState(line, index, cell, cells){
        override fun next(): CsvLineParseState {
            if(atEnd()) throw RuntimeException("open quote without close quote")
            return if(currentCharIsQuote()){
                AfterFirstInnerQuote(line, index+1, cell, cells)
            } else {
                InQuotedCell(line, index+1, cell+currentChar, cells)
            }
        }
    }

    class AfterFirstInnerQuote(line:String, index:Int, cell:String, cells:List<String>):CsvLineParseState(line, index, cell, cells){
        override fun next(): CsvLineParseState {
            return if(atEnd()) {
                val newCell = ""
                val newCells = cells + cell
                End(line, index, newCell, newCells)
            } else {
                return if(currentCharIsQuote()){
                    InQuotedCell(line, index+1, cell+"\"", cells)
                } else if(currentCharIsSeparator()) {
                    val newCell = ""
                    val newCells = cells + cell
                    AfterCellBoundary(line, index+1, newCell, newCells)
                } else{
                    throw RuntimeException("unexpected character after quote, '$currentChar'")
                }
            }
        }
    }

    class AfterCellBoundary(line:String, index:Int, cell:String, cells:List<String>):CsvLineParseState(line, index, cell, cells){
        override fun next(): CsvLineParseState {
            if(atEnd()) return End(line, index, cell, cells)
            return if(currentCharIsQuote()) {
                InQuotedCell(line, index+1, cell, cells)
            } else if(currentCharIsSeparator()) {
                AfterCellBoundary(line, index+1, cell, cells + "")
            } else {
                InUnquotedCell(line, index+1, cell+currentChar, cells)
            }
        }
    }

    class InUnquotedCell(line:String, index:Int, cell:String, cells:List<String>):CsvLineParseState(line, index, cell, cells){
        override fun next(): CsvLineParseState {
            if(atCellBoundary()){
                val newCell = ""
                val newCells = cells + cell
                return if(atEnd()) {
                    End(line, index, newCell, newCells)
                } else {
                    AfterCellBoundary(line, index+1, newCell, newCells)
                }
            } else {
                return InUnquotedCell(line, index+1, cell+currentChar, cells)
            }
        }
    }

    class End(line:String, index:Int, cell:String, cells:List<String>):CsvLineParseState(line, index, cell, cells){
        override fun next(): CsvLineParseState {
            throw UnsupportedOperationException("can't call next on End")
        }
    }

    companion object {
        fun create(line:String):CsvLineParseState = AfterCellBoundary(line, 0, "", emptyList())
    }
}
