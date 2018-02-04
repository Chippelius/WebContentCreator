package model;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XMLHandler {

	private static final String ROOT_TAG = "dataStorage";
	private static final String VERSION_ATTRIBUTE = "version";
	private static final String PAGE_TAG = "page";
	private static final String FILENAME_ATTRIBUTE = "filename";
	private static final String NAME_ATTRIBUTE = "name";
	private static final String ELEMENT_TAG = "element";
	private static final String TYPE_ATTRIBUTE = "type";

	private static DocumentBuilder builder = null;
	private static Transformer transformer = null;

	public static DataStorage loadDataStorage(File source) throws Exception {
		Document document = getDocumentBuilder().parse(source);
		document.getDocumentElement().normalize();
		switch(Integer.parseInt(document.getDocumentElement().getAttribute(VERSION_ATTRIBUTE))) {
		case 1:
			return handleVersion1(document);
		default:
			return handleVersion1(document);
		}
	}

	private static DataStorage handleVersion1(Document document) {
		DataStorage dataStorage = new DataStorage();
		NodeList pages = document.getElementsByTagName(PAGE_TAG);
		for(int i=0; i<pages.getLength(); ++i) {
			if(pages.item(i).getNodeType() == Node.ELEMENT_NODE) {
				org.w3c.dom.Element currentPage = (org.w3c.dom.Element) pages.item(i);
				Page p = new Page(currentPage.getAttribute(FILENAME_ATTRIBUTE), currentPage.getAttribute(NAME_ATTRIBUTE));
				dataStorage.add(p);
				NodeList elements = currentPage.getElementsByTagName(ELEMENT_TAG);
				for(int j=0; j<elements.getLength(); ++j) {
					if(elements.item(j).getNodeType() == Node.ELEMENT_NODE) {
						org.w3c.dom.Element currentElement = (org.w3c.dom.Element) elements.item(j);
						p.add(new Element(currentElement.getAttribute(TYPE_ATTRIBUTE), currentElement.getTextContent()));
					}
				}
			}
		}
		dataStorage.setEditedSinceLastSave(false);
		return dataStorage;
	}

	private static DocumentBuilder getDocumentBuilder() throws Exception {
		if(builder==null)
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return builder;
	}

	private static Transformer getTransformer() throws Exception {
		if(transformer==null)
			transformer = TransformerFactory.newInstance().newTransformer();
		return transformer;
	}

	public static void saveDataStorage(File destination, DataStorage dataStorage) throws Exception {
		Document document = getDocumentBuilder().newDocument();
		org.w3c.dom.Element root = document.createElement(ROOT_TAG);
		root.setAttribute(VERSION_ATTRIBUTE, ""+DataStorage.VERSION);
		document.appendChild(root);
		for(Page page : dataStorage) {
			org.w3c.dom.Element pageNode = document.createElement(PAGE_TAG);
			pageNode.setAttribute(FILENAME_ATTRIBUTE, page.getFilename());
			pageNode.setAttribute(NAME_ATTRIBUTE, page.getName());
			root.appendChild(pageNode);
			for(Element element : page) {
				org.w3c.dom.Element elementNode = document.createElement(ELEMENT_TAG);
				elementNode.setAttribute(TYPE_ATTRIBUTE, element.getType());
				elementNode.setTextContent(element.getValue());
				pageNode.appendChild(elementNode);
			}
		}

		DOMSource domSource = new DOMSource(document);
		StreamResult result = new StreamResult(destination);
		getTransformer().transform(domSource, result);
	}

}
