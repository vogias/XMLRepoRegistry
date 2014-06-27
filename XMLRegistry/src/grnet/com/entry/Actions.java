/*******************************************************************************
 * Copyright (c) 2014 Kostas Vogias.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Kostas Vogias - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package grnet.com.entry;

import grnet.com.repo.RSSRepo;
import grnet.com.repo.Repository;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vogias
 * 
 */
public class Actions {

	Scanner reader;
	JAXBContext context;

	Marshaller marshaller;
	Unmarshaller unmarshaller;

	private static final Logger slf4jLogger = LoggerFactory
			.getLogger(Actions.class);

	public Actions(Scanner reader) {
		this.reader = reader;

	}

	public Actions() {

	}

	private void initMarshaller() throws JAXBException {
		context = JAXBContext.newInstance(Repository.class);
		marshaller = context.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	}

	private void initRSSMarshaller() throws JAXBException {
		context = JAXBContext.newInstance(RSSRepo.class);
		marshaller = context.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

	}

	private void initUnMarshaller() throws JAXBException {
		context = JAXBContext.newInstance(Repository.class);
		unmarshaller = context.createUnmarshaller();

		// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		// Boolean.TRUE);
	}

	private void printRepoInfo(Repository repo) {
		System.out.println("----------------------------------");
		System.out.println("Repository name:" + repo.getName());
		System.out.println("Repository URL:" + repo.getUrl());
		System.out.println("Repository prefix:" + repo.getPrefix());
		System.out.println("Repository oaiVersion:" + repo.getOaiVersion());
		System.out.println("Repository deleted records policy:"
				+ repo.getDelPolicy());
		System.out.println("Repository granularity:" + repo.getDelPolicy());
		System.out.println("Repository responsible person mail:"
				+ repo.getResponsible());
		System.out.println("Repository set:" + repo.getSet());
		System.out.println("XSL URL:" + repo.getXslURLstr());

	}

	public void listRepositories(File path) throws JAXBException {

		String[] extensions = { "xml" };
		FileUtils utils = new FileUtils();

		Collection<File> files = utils.listFiles(path, extensions, true);

		if (!files.isEmpty()) {
			initUnMarshaller();

			Iterator<File> iterator = files.iterator();
			while (iterator.hasNext()) {
				Repository repository = (Repository) unmarshaller
						.unmarshal(iterator.next());
				printRepoInfo(repository);

			}

		} else
			System.err.println("No repositories found...");

	}

	public void listSpecificRepoInfo(File path, String name)
			throws JAXBException {

		String[] extensions = { "xml" };
		FileUtils utils = new FileUtils();

		Collection<File> files = utils.listFiles(path, extensions, true);

		if (!files.isEmpty()) {
			initUnMarshaller();

			Iterator<File> iterator = files.iterator();
			boolean flag = false;
			while (iterator.hasNext()) {
				File next = iterator.next();

				if (next.getName().equals(name + ".xml")) {
					flag = true;
					Repository repository = (Repository) unmarshaller
							.unmarshal(next);
					printRepoInfo(repository);
				}

			}
			if (flag == false)
				System.err.println("No repository found...");

		} else
			System.err.println("No repositories found...");

	}

	private void logRepo(Repository repo, String status) {
		StringBuffer buffer = new StringBuffer();

		buffer.append(repo.getName());
		buffer.append(" " + status);
		buffer.append(" " + repo.getUrl());
		buffer.append(" " + repo.getPrefix());
		buffer.append(" " + repo.getOaiVersion());
		buffer.append(" " + repo.getDelPolicy());
		buffer.append(" " + repo.getGranularity());
		buffer.append(" " + repo.getResponsible());
		slf4jLogger.info(buffer.toString());
	}

	public void addRepository(String url, int choice, File path)
			throws JAXBException {
		if (choice == 1) {
			Repository repository = new Repository();
			repository.setUrl(url);

			System.out.println("Insert repository name:");
			repository.setName(reader.next().replace(" ", ""));

			System.out.println("Insert metadata prefix:");
			repository.setPrefix(reader.next());

			System.out.println("Insert responsible person mail:");
			repository.setResponsible(reader.next());

			System.out.println("Insert XSL file URL:");
			repository.setXslURLstr(reader.next());

			System.out.println("Insert set:");
			repository.setSet(reader.next());

			repository.setPartialInfo();

			File repoFile = new File(path, repository.getName() + ".xml");
			initMarshaller();
			marshaller.marshal(repository, repoFile);
			logRepo(repository, "NEW");

			System.out.println("Repository is saved.");

		} else if (choice == 2) {
			Repository repository = new Repository();
			repository.setUrl(url);

			System.out.println("Insert repository name:");
			repository.setName(reader.next());

			System.out.println("Insert metadata prefix:");
			repository.setPrefix(reader.next());

			System.out.println("Insert set:");
			repository.setSet(reader.next());

			repository.setFullInfo();
			File repoFile = new File(path, repository.getName() + ".xml");
			initMarshaller();
			marshaller.marshal(repository, repoFile);
			logRepo(repository, "NEW");
			System.out.println("Repository is saved.");
		} else {
			System.err.println("Wrong choice value.");
			System.exit(-1);
		}

	}

	public void addRepository(String url, File path) throws JAXBException {

		RSSRepo repository = new RSSRepo();
		repository.setUrl(url);

		System.out.println("Insert repository name:");
		repository.setName(reader.next().replace(" ", ""));

		System.out.println("Insert repository URL:" + url);
		repository.setUrl(url);

		System.out.println("Insert XSL file URL:");
		repository.setXslURLstr("Insert manually the XSL File URL.");

		File repoFile = new File(path, repository.getName() + ".xml");
		initRSSMarshaller();
		marshaller.marshal(repository, repoFile);

		System.out.println("Repository is saved.");

	}

	public void delRepository(File path, String name) throws JAXBException {
		String[] extensions = { "xml" };
		FileUtils utils = new FileUtils();

		Collection<File> files = utils.listFiles(path, extensions, true);

		Iterator<File> iterator = files.iterator();

		initUnMarshaller();

		if (name.equals("-a")) {

			while (iterator.hasNext()) {
				File next = iterator.next();
				Repository repository = (Repository) unmarshaller
						.unmarshal(next);
				logRepo(repository, "DELETE");
				next.delete();
			}
			System.out.println("All repository entries are deleted.");
		} else {
			boolean flag = false;
			while (iterator.hasNext()) {
				File next = iterator.next();
				String fileName = next.getName();

				if ((name + ".xml").equals(fileName)) {
					flag = true;
					Repository repository = (Repository) unmarshaller
							.unmarshal(next);
					logRepo(repository, "DELETE");
					next.delete();

				}

			}
			if (flag == false)
				System.err.println("No repository found...");
			else
				System.out.println("Repository:" + name + " is deleted.");
		}
	}
}
