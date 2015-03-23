package com.surevine.gateway.auditing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import play.Logger;

import com.typesafe.config.ConfigFactory;

/**
 * AuditService implementation which records actions in XML file.
 *
 * @author jonnyheavey
 *
 */
@org.springframework.stereotype.Service
public class XMLAuditServiceImpl implements AuditService {

	/**
	 * Environment the management console is deployed to (used for auditing purposes)
	 */
	private static final String EVENT_SYSTEM_ENVIRONMENT = ConfigFactory.load().getString("xml.audit.system.environment");

	/**
	 * Directory where XML log files should be created and written to.
	 */
	private static final String XML_LOG_FILE_DIRECTORY = ConfigFactory.load().getString("xml.audit.log.file.directory");

	/**
	 * Template for new audit.xml log file to created from
	 */
	private static final String XML_LOG_FILE_TEMPLATE = ConfigFactory.load().getString("xml.audit.log.file.template");

	/**
	 * Template including tokens to represent event. Valid tokens include:
	 * %EVENT_TIME%
	 * %EVENT_SYSTEM_ENVIRONMENT%
	 * %EVENT_USER%
	 * %EVENT_ACTION%
	 */
	private static final String XML_EVENT_TEMPLATE = ConfigFactory.load().getString("xml.audit.event.template");

	private static XMLAuditServiceImpl _instance = null;
	private DocumentBuilder documentBuilder;
	private SimpleDateFormat dateFormat;
	private String auditLogFile;

	private XMLAuditServiceImpl() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new AuditServiceException("Unable to init XML audit service.", e);
		}

        createAuditFile();
	}

	public static XMLAuditServiceImpl getInstance() {
		if(_instance == null) {
			_instance = new XMLAuditServiceImpl();
		}
		return _instance;
	}

	@Override
	public void audit(AuditEvent auditEvent) {
        Document document;
        Node event;
		try {
			document = documentBuilder.parse(auditLogFile);
			event = createEventXML(auditEvent);
		} catch (SAXException | IOException e) {
			throw new AuditServiceException("Unable to load XML audit log file.", e);
		}

		Node importedEventNode = document.importNode(event, true);
		Element events = document.getDocumentElement();
		events.appendChild(importedEventNode);

        persistEvent(document);
	}

	/**
	 * Creates audit XML file to log events to (if file doesn't exist)
	 */
	private void createAuditFile() {

		Path auditFile = Paths.get(XML_LOG_FILE_DIRECTORY, "audit.xml");
		if(!Files.exists(auditFile)) {
			Logger.info("No existing XML audit file found. Creating new file.");
			Path auditFileTemplate = Paths.get(XML_LOG_FILE_TEMPLATE);
			try {
				Files.copy(auditFileTemplate, auditFile);
			} catch (IOException e) {
				throw new AuditServiceException("Could not create new XML audit log file.", e);
			}
		} else {
			Logger.info("Found existing XML audit file.");
		}

		this.auditLogFile = auditFile.toString();
	}

	/**
	 * Generates a new Event node to be added to XML audit log
	 *
	 * @param event Audited action
	 * @return XML Node representing audit event
	 * @throws AuditServiceException
	 */
	private Node createEventXML(AuditEvent event) throws AuditServiceException {
		String eventTemplate = loadEventTemplate();
		String populatedEvent = populateEventTemplate(eventTemplate, event);
		Node xmlEvent = null;
		InputStream eventInputStream = null;

		try {
			eventInputStream = new ByteArrayInputStream(populatedEvent.getBytes("UTF-8"));
			xmlEvent = documentBuilder.parse(eventInputStream).getFirstChild();
		} catch ( SAXException | IOException e) {
			throw new AuditServiceException("Error creating event XML.", e);
		} finally {
			IOUtils.closeQuietly(eventInputStream);
		}

		return xmlEvent;
	}

	/**
	 * Loads the XML template for an event from disk
	 *
	 * @return Contents of event template
	 */
	private String loadEventTemplate() {

		Path eventTemplatePath = Paths.get(XML_EVENT_TEMPLATE);
		StringBuilder parsedEventTemplate = new StringBuilder();

		try {
			List<String> lines = Files.readAllLines(eventTemplatePath, Charset.defaultCharset());
			for (String line : lines) {
				parsedEventTemplate.append(line + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			throw new AuditServiceException("Unable to load audit event template.", e);
		}

		return parsedEventTemplate.toString();
	}

	/**
	 * Writes an audit event to disk
	 *
	 * @param document
	 * @throws TransformerFactoryConfigurationError
	 */
	private void persistEvent(Document document) {
		DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(auditLogFile);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.transform(source, result);
		} catch (TransformerException e) {
			throw new AuditServiceException("Unable to write audit event to log file.", e);
		}
	}

	/**
	 * Populates an event template with audit event values
	 *
	 * @param template Template to populate
	 * @param event Audited action
	 * @return String populated event template string
	 */
	private String populateEventTemplate(String template, AuditEvent event) {

		String autheticatedUser = event.getUsername();
		if(autheticatedUser == null) {
			autheticatedUser = "Unauthticated user";
		}

		String[] tokens = new String[]{"%EVENT_USER%",
										"%EVENT_TIME%",
										"%EVENT_SYSTEM_ENVIRONMENT%",
										"%EVENT_ACTION%"};

		String[] values = new String[]{autheticatedUser,
										dateFormat.format(event.getDatetime()),
										EVENT_SYSTEM_ENVIRONMENT,
										event.getAction().serialize()};

		return StringUtils.replaceEach(template, tokens, values);
	}

}
