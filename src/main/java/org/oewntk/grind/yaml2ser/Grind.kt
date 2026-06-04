/*
 * Copyright (c) 2021-2024. Bernard Bou.
 */
package org.oewntk.grind.yaml2ser

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import org.oewntk.grind.yaml2ser.Tracing.progress
import org.oewntk.grind.yaml2ser.Tracing.start
import org.oewntk.model.ModelInfo
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
        val parser = ArgParser("yaml2ser")

        // Options (start with - or --)
        // @formatter:off
        val in1 by parser.argument(    ArgType.String,                                              description = "Input dir or file")
        val in2 by parser.argument(    ArgType.String,                                              description = "Extra input dir or file")
        val out by parser.argument(    ArgType.String,                                              description = "Output dir or file")
        val out2 by parser.option(     ArgType.String,   shortName = "o2", fullName = "out2",       description = "Extra output dir or file")        .default("")
        val verbose by parser.option(  ArgType.Boolean,  shortName = "v",  fullName = "verbose",    description = "Verbose output")                  .default(false)

        val traceTime by parser.option(ArgType.Boolean,  shortName = "tt", fullName = "trace:time", description = "trace time")                      .default(false)
        val traceHeap by parser.option(ArgType.Boolean,  shortName = "th", fullName = "trace:heap", description = "trace heap")                      .default(false)
        // @formatter:on
        parser.parse(args)

        // Tracing
        Tracing.traceTime = traceTime
        Tracing.traceHeap = traceHeap

        val startTime = start()

        // Input
        val inDir = File(in1)
        Tracing.psInfo.println("[Input] " + inDir.absolutePath)

        // Input2
        val inDir2 = File(in2)
        Tracing.psInfo.println("[Input2] " + inDir2.absolutePath)

        // Output
        val outFile = File(out)
        val outDir = outFile.parentFile
        if (!outDir.exists()) {
            outDir.mkdirs()
        }
        if (outFile.exists() && !outFile.isDirectory) {
            outFile.delete()
        }
        Tracing.psInfo.println("[Output] " + outFile.absolutePath)

        // Supply model
        progress("before model is supplied,", startTime)
        val model = Factory(inDir, inDir2, verbose = verbose).get()!!
        progress("after model is supplied,", startTime)

        // Consume model
        progress("before model is consumed,", startTime)
        ModelConsumer(outFile, verbose = verbose).accept(model)
        progress("after model is consumed,", startTime)

        // End
        progress("total,", startTime)

        // info
        val modelInfo = model.info()
        val modelCounts = ModelInfo.counts(model)
        val modelInfo2 = "$modelInfo\n$modelCounts"
        Tracing.psInfo.println(modelInfo2)
        File(out2.ifEmpty { "oewn.ser.info" }).writeText(modelInfo2)
    }
}
