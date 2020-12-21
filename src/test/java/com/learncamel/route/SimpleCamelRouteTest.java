package com.learncamel.route;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
public class SimpleCamelRouteTest {

	@Autowired
	ProducerTemplate producerTamplate;
	@Autowired
	Environment environment;

	@BeforeClass
	public static void startCleanUp() throws IOException {

		FileUtils.cleanDirectory(new File("data/input"));
		FileUtils.deleteDirectory(new File("data/output"));
		FileUtils.deleteDirectory(new File("data/input/error"));

	}

	@Test
	public void testMoveFile() {

		String message = "type, sku#, itemdescription, price\r\n" + "ADD,100, Samsung TV, 500\r\n"
				+ "ADD,100, LG TV, 500";
		String fileName = "fileTest.txt";

		producerTamplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File outFile = new File("data/output/" + fileName);

		assertTrue(outFile.exists());

	}

	@Test
	public void testMoveFile_ADD() throws IOException, InterruptedException {
		String message = "type, sku#, itemdescription, price\r\n" + "ADD,100, Samsung TV, 500\r\n"
				+ "ADD,101, LG TV, 500";
		String fileName = "fileTest.txt";

		producerTamplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		Thread.sleep(15000);

		File outFile = new File("data/output/" + fileName);

		assertTrue(outFile.exists());

		String outputMessage = "Data Update Successfully";
		String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));

		assertEquals(outputMessage, output);
	};
	
	@Test
	public void testMoveFile_ADD_Exception() throws IOException, InterruptedException {
		String message = "type, sku#, itemdescription, price\r\n" + "ADD,, Samsung TV, 500\r\n"
				+ "ADD,101, LG TV, 500";
		String fileName = "fileTest.txt";

		producerTamplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		Thread.sleep(15000);

		File outFile = new File("data/output/" + fileName);
		assertTrue(outFile.exists());
		
		File errorDirectory = new File("data/input/error");
		assertTrue(errorDirectory.exists());
	};

	@Test
	public void testMoveFile_UPDATE() throws IOException, InterruptedException {
		String message = "type, sku#, itemdescription, price\r\n" + "UPDATE,101,LG TV, 600";
		String fileName = "fileUpdate.txt";

		producerTamplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		Thread.sleep(15000);

		File outFile = new File("data/output/" + fileName);

		assertTrue(outFile.exists());

		String outputMessage = "Data Update Successfully";
		String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));

		assertEquals(outputMessage, output);
	};
	
	@Test
	public void testMoveFile_DELETE() throws IOException, InterruptedException {
		String message = "type, sku#, itemdescription, price\r\n" + "DELETE,101,LG TV, 600";
		String fileName = "fileDelete.txt";

		producerTamplate.sendBodyAndHeader(environment.getProperty("fromRoute"), message, Exchange.FILE_NAME, fileName);

		Thread.sleep(15000);

		File outFile = new File("data/output/" + fileName);

		assertTrue(outFile.exists());

		String outputMessage = "Data Update Successfully";
		String output = new String(Files.readAllBytes(Paths.get("data/output/Success.txt")));

		assertEquals(outputMessage, output);
	};
	
	

}
