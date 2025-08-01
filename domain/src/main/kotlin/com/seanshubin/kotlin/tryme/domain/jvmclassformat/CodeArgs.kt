package com.seanshubin.kotlin.tryme.domain.jvmclassformat

enum class CodeArgs {
    NONE{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 0
        }
    },
    LOCAL_VARIABLE_INDEX {
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    CONSTANT_POOL_INDEX {
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    BYTE_VALUE{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    BRANCH_OFFSET{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    BRANCH_OFFSET_WIDE{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 4
        }
    },
    TWO_REFERENCES{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    TWO_INTS{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    INDEX_CONST{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    CONSTANT_POOL_INDEX_THEN_TWO_ZEROES{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 4
        }
    },
    CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 4
        }
    },
    CONSTANT_POOL_INDEX_SHORT {
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    LOOKUP_SWITCH{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            throw UnsupportedOperationException("not implemented")
        }
    },
    CONSTANT_POOL_INDEX_THEN_DIMENSIONS{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 3
        }
    },
    ARRAY_TYPE{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    SHORT_VALUE{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    TABLE_SWITCH{
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            throw UnsupportedOperationException("not implemented")
        }
    },
    WIDE {
        override fun lookupSize(index: Int, code: List<Byte>): Int {
            throw UnsupportedOperationException("not implemented")
        }
    };


    abstract fun lookupSize(index:Int, code:List<Byte>): Int
}