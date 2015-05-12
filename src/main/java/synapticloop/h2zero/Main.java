package synapticloop.h2zero;

import synapticloop.h2zero.ant.H2ZeroTask;

/*
 * Copyright (c) 2012-2015 synapticloop.
 * 
 * All rights reserved.
 *
 * This source code and any derived binaries are covered by the terms and
 * conditions of the Licence agreement ("the Licence").  You may not use this
 * source code or any derived binaries except in compliance with the Licence.
 * A copy of the Licence is available in the file named LICENCE shipped with
 * this source code or binaries.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations
 * under the Licence.
 */

public class Main {
	private static void usage() {
		usage(null);
	}

	/**
	 * Simple usage message with optional explanatory message
	 * 
	 * @param message The message to be printed out if not null
	 */
	private static void usage(Exception exception) {
		if(exception != null) {
			System.out.println("FATAL: " + exception.getMessage() + "\n");
		}

		System.out.println("Usage:\n\tjava synapticloop.h2zero.Main <filename.h2zero> <out.dir>");
		System.out.println("Where:");
		System.out.println("\t<filename.h2zero> is the path to the json format file");
		System.out.println("\t<out.dir> is the path to where the generated artefacts will go");

		if(null != exception) {
			System.out.println("Stack trace follows:");
			exception.printStackTrace();
		}
		throw new IllegalArgumentException();
	}

	public static void main(String[] args) {
		if(null == args || args.length != 2) {
			usage();
		} else {
			String inFile = args[0];
			String outDir = args[1];

			H2ZeroTask h2ZeroTask = new H2ZeroTask();
			h2ZeroTask.setIn(inFile);
			h2ZeroTask.setOutDir(outDir);

			h2ZeroTask.execute();
		}
	}
}
