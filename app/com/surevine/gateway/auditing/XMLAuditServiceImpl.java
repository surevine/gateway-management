package com.surevine.gateway.auditing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
public class XMLAuditServiceImpl implements AuditService {

	private static final String EVENT_SYSTEM_NAME = "Community portal";
	private static final String EVENT_SYSTEM_ENVIRONMENT = "hosting-env-123";
	private static final String EVENT_GENERATOR = "Gateway management console";
	private static final String XML_LOG_FILE=  ConfigFactory.load().getString("xml.audit.log.file");
	private static final String XML_EVENT_TEMPLATE = ConfigFactory.load().getString("xml.audit.event.template");
	private static final String XML_EVENT_ACTION_TEMPLATES_DIR = ConfigFactory.load().getString("xml.audit.event.action.templates.dir");

	private static XMLAuditServiceImpl _instance = null;

	private DocumentBuilder documentBuilder;
	private SimpleDateFormat dateFormat;

	private XMLAuditServiceImpl() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new AuditServiceException("Unable to init XML audit service.", e);
		}
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
			document = documentBuilder.parse(XML_LOG_FILE);
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
	 * Generates a new Event node to be added to XML audit log
	 *
	 * @param event Audited action
	 * @return XML Node representing audit event
	 * @throws SAXException
	 * @throws IOException
	 */
	private Node createEventXML(AuditEvent event) throws SAXException, IOException {

		String eventTemplate = loadEventTemplate();
		String populatedEvent = populateEventTemplate(eventTemplate, event);
		InputStream eventInputStream = new ByteArrayInputStream(populatedEvent.getBytes("UTF-8"));
		Node eventNode = documentBuilder.parse(eventInputStream).getFirstChild();

		return eventNode;
	}

	/**
	 * Loads the XML template for a specific audit event action
	 *
	 * @param action action to load template for
	 * @return Contents of action template
	 */
	private String loadActionTemplate(GatewayAction action) {
		String filename = action.toString().toLowerCase() + ".xml";
		Path actionTemplatePath = Paths.get(XML_EVENT_ACTION_TEMPLATES_DIR, filename);
		StringBuffer parsedActionTemplate = new StringBuffer();

		try {
			List<String> lines = Files.readAllLines(actionTemplatePath, Charset.defaultCharset());
			for (String line : lines) {
				parsedActionTemplate.append(line + System.getProperty("line.separator"));
			}
		} catch (IOException e) {
			throw new AuditServiceException("Unable to load audit event action template.", e);
		}

		return parsedActionTemplate.toString();
	}

	/**
	 * Loads the XML template for an event from disk
	 *
	 * @return Contents of event template
	 */
	private String loadEventTemplate() {

		Path eventTemplatePath = Paths.get(XML_EVENT_TEMPLATE);
		StringBuffer parsedEventTemplate = new StringBuffer();

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
        StreamResult result = new StreamResult(XML_LOG_FILE);
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

		template = template.replace("%EVENT_TIME%", dateFormat.format(event.getDatetime()));
		template = template.replace("%EVENT_SYSTEM_NAME%", EVENT_SYSTEM_NAME);
		template = template.replace("%EVENT_SYSTEM_ENVIRONMENT%", EVENT_SYSTEM_ENVIRONMENT);
		template = template.replace("%EVENT_GENERATOR%", EVENT_GENERATOR);
		template = template.replace("%EVENT_USER%", event.getUsername());
		template = template.replace("%EVENT_MESSAGE%", event.getMessage());

		String actionTemplate = loadActionTemplate(event.getAction());
		template = template.replace("%EVENT_ACTION%", actionTemplate);

		return template;
	}

}
