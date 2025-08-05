package com.seanshubin.kotlin.tryme.domain.jvmclassformat


enum class CodeArgs {
    NONE {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 0
        }
    },
    LOCAL_VARIABLE_INDEX {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    CONSTANT_POOL_INDEX {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    BYTE_VALUE {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    BRANCH_OFFSET {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    BRANCH_OFFSET_WIDE {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 4
        }
    },
    INDEX_CONST {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    CONSTANT_POOL_INDEX_THEN_TWO_ZEROES {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 4
        }
    },
    CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 4
        }
    },
    CONSTANT_POOL_INDEX_SHORT {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    LOOKUP_SWITCH {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return OpCodeEntry.LookupSwitchEntry.fromBytes(index, code).argSize()
        }
    },
    CONSTANT_POOL_INDEX_THEN_DIMENSIONS {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 3
        }
    },
    ARRAY_TYPE {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 1
        }
    },
    SHORT_VALUE {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return 2
        }
    },
    TABLE_SWITCH {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return OpCodeEntry.TableSwitchEntry.fromBytes(index, code).argSize()
        }
    },
    WIDE {
        override fun lookupArgSize(index: Int, code: List<Byte>): Int {
            return OpCodeEntry.WideEntry.fromBytes(index, code).argSize()
        }
    };


    abstract fun lookupArgSize(index: Int, code: List<Byte>): Int
}