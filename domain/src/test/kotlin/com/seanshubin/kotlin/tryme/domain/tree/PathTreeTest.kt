package com.seanshubin.kotlin.tryme.domain.tree

import com.seanshubin.kotlin.tryme.domain.contract.FilesContract
import com.seanshubin.kotlin.tryme.domain.contract.FilesDelegate
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.assertEquals

class PathTreeTest {
    @Test
    fun createPathTreeIntegrationTest() {
        val filesContract: FilesContract = FilesDelegate
        val basePath = Paths.get("src/test/resources/sample-data/tree-sample")
        val accept:(Path)->Boolean = { true }
        val pathTree = PathTreeFactory.create(filesContract, basePath, accept)
        val coordinateString: (Path, List<Path>, List<Int>) -> String = { value, ancestors, indices ->
            "$value, ancestors = $ancestors, indices = $indices"
        }
        val actual = mutableListOf<String>()
        val expected = listOf(
                "src/test/resources/sample-data/tree-sample, ancestors = [], indices = []",
                "src/test/resources/sample-data/tree-sample/a, ancestors = [src/test/resources/sample-data/tree-sample], indices = [0]",
                "src/test/resources/sample-data/tree-sample/a/c.txt, ancestors = [src/test/resources/sample-data/tree-sample, src/test/resources/sample-data/tree-sample/a], indices = [0, 0]",
                "src/test/resources/sample-data/tree-sample/a/b.txt, ancestors = [src/test/resources/sample-data/tree-sample, src/test/resources/sample-data/tree-sample/a], indices = [0, 1]",
                "src/test/resources/sample-data/tree-sample/f, ancestors = [src/test/resources/sample-data/tree-sample], indices = [1]",
                "src/test/resources/sample-data/tree-sample/f/g.txt, ancestors = [src/test/resources/sample-data/tree-sample, src/test/resources/sample-data/tree-sample/f], indices = [1, 0]",
                "src/test/resources/sample-data/tree-sample/e, ancestors = [src/test/resources/sample-data/tree-sample], indices = [2]"
        )
        pathTree.mapWithCoordinates(coordinateString).forEach { actual.add(it.value) }
        assertEquals(expected, actual)
    }
}
