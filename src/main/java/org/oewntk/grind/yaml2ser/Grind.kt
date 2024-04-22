/*
 * Copyright (c) 2021-2021. Bernard Bou.
 */

package org.oewntk.grind.yaml2ser;

import org.oewntk.model.Model;
import org.oewntk.ser.out.ModelConsumer;
import org.oewntk.yaml.in.Factory;

import java.io.File;

/**
 * Main class that generates the WN database in the SQL format
 *
 * @author Bernard Bou
 * @see "https://sqlunet.sourceforge.net/schema.html"
 */
public class Grind
{
	/**
	 * Main entry point
	 *
	 * @param args command-line arguments yamlDir [outputDir]
	 */
	public static void main(String[] args)
	{
		int iArg = 0;

		// Tracing
		final long startTime = Tracing.start();

		// Input
		File inDir = new File(args[iArg]);
		Tracing.psInfo.println("[Input] " + inDir.getAbsolutePath());

		// Input2
		File inDir2 = new File(args[iArg + 1]);
		Tracing.psInfo.println("[Input2] " + inDir2.getAbsolutePath());

		// Output
		File outFile = new File(args[iArg + 2]);
		if (!outFile.exists())
		{
			//noinspection ResultOfMethodCallIgnored
			outFile.delete();
		}
		Tracing.psInfo.println("[Output] " + outFile.getAbsolutePath());

		// Supply model
		Tracing.progress("before model is supplied,", startTime);
		Model model = new Factory(inDir, inDir2).get();
		//Tracing.psInfo.printf("[Model] %s%n%s%n%n", Arrays.toString(model.getSources()), model.info());
		Tracing.progress("after model is supplied,", startTime);

		// Consume model
		Tracing.progress("before model is consumed,", startTime);
		new ModelConsumer(outFile).accept(model);
		Tracing.progress("after model is consumed,", startTime);

		// End
		Tracing.progress("total,", startTime);
	}
}
