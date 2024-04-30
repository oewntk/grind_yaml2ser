/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.grind.yaml2ser

import org.oewntk.grind.yaml2ser.Tracing.progress
import org.oewntk.grind.yaml2ser.Tracing.start
import org.oewntk.ser.out.ModelConsumer
import org.oewntk.yaml.`in`.Factory
import java.io.File

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
object Grind {

    /**
     * Main entry point
     *
     * @param args command-line arguments
     * ```
     * yamlDir [outputDir]
     * ```
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val iArg = 0

        // Tracing
        val startTime = start()

        // Input
        val inDir = File(args[iArg])
        Tracing.psInfo.println("[Input] " + inDir.absolutePath)

        // Input2
        val inDir2 = File(args[iArg + 1])
        Tracing.psInfo.println("[Input2] " + inDir2.absolutePath)

        // Output
        val outFile = File(args[iArg + 2])
        if (!outFile.exists()) {
            outFile.delete()
        }
        Tracing.psInfo.println("[Output] " + outFile.absolutePath)

        // Supply model
        progress("before model is supplied,", startTime)
        val model = Factory(inDir, inDir2).get()
        //Tracing.psInfo.printf("[Model] %s%n%s%n%n", Arrays.toString(model.getSources()), model.info());
        progress("after model is supplied,", startTime)

        // Consume model
        progress("before model is consumed,", startTime)
        ModelConsumer(outFile).accept(model!!)
        progress("after model is consumed,", startTime)

        // End
        progress("total,", startTime)
    }
}
