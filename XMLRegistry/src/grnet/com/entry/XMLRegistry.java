/**
 * 
 */
package grnet.com.entry;

import grnet.constans.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

/**
 * @author vogias
 * 
 */
public class XMLRegistry {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException, JAXBException {
		// TODO Auto-generated method stub

		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("configure.properties")));
		String targetPath = properties.getProperty(Constants.path);
		File path = new File(targetPath);

		if (!path.exists()) {
			System.err.println(path.getPath() + " does not exist...");
			System.exit(-1);
		}

		if (args.length == 2) {
			Actions actions;
			if (args[0].equals("-l") && args[1].equals("-a")) {
				actions = new Actions();
				actions.listRepositories(path);
			}
			if (args[0].equals("-l") && !args[1].equals("-a")) {
				actions = new Actions();

				actions.listSpecificRepoInfo(path, args[1]);
			}

			if (args[0].equals("-d")) {
				actions = new Actions();

				actions.delRepository(path, args[1]);
			}

		} else if (args.length == 0) {
			System.out.println("Choose one of the following actions:");
			System.out
					.println("1. Add repository | 2. List repositories | 3. Delete repository");
			Scanner reader = new Scanner(System.in);

			int choice = reader.nextInt();

			Actions actions;
			if (choice == 1) {
				actions = new Actions(reader);
				System.out.println("Insert new repository oai-pmh target url:");
				String url = reader.next();
				System.out
						.println("1. Insert info manually | 2. Insert info automatically");
				int input = reader.nextInt();
				actions.addRepository(url, input, path);
			} else if (choice == 2) {
				actions = new Actions(reader);
				System.out
						.println("1. List all repositories | 2. List a specific repository info.");
				int in = reader.nextInt();

				if (in == 1)
					actions.listRepositories(path);
				else if (in == 2) {
					System.out.println("Enter repository name:");
					String name = reader.next();
					actions.listSpecificRepoInfo(path, name);
				} else {
					System.err.print("Wrong input");
					System.exit(-1);
				}
			} else if (choice == 3) {
				actions = new Actions(reader);
				System.out.println("Insert repository name to delete:");
				String name = reader.next();
				actions.delRepository(path, name);
			} else {
				System.err.println("Wrong choice.");
				System.exit(-1);
			}
		} else {
			System.err.println("Wrong number of inputs");
			System.exit(-1);
		}

	}

}
