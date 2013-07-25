/*******************************************************************************
 * Copyright 2013 Vienna University of Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package at.ac.tuwien.photohawk.taverna.evaluation.evaluators.javafitsevaluator;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import at.ac.tuwien.photohawk.taverna.model.model.scales.Scale;
import at.ac.tuwien.photohawk.taverna.model.model.values.Value;

/**
 * This is a generic helper class that takes an XPath expression and uses it to
 * search a specified XML and extract a @link {@link Value}
 * 
 * @author cb
 * 
 */
public class XmlExtractor {

    /**
     * this is (currently) for testing if we can extract that kind of thing
     * easily - checking for a certain value
     * 
     * @param xml
     * @param scale
     * @param xpath
     * @param toMatch
     * @return
     * 
     *         THIS does not seem to make sense - one should extract the text,
     *         compare, and note observations too, such as
     *         "original value is, target value should be,... " and so i hide
     *         this at the moment to avoid wrong usage. see ObjectEvaluator!
     * 
     *         public Value checkValue(InputSource xml, Scale scale, String
     *         xpath, String toMatch) { //Well-Formed and valid String text =
     *         ""; BooleanValue v = (BooleanValue) scale.createValue(); try {
     *         text = extractText(getDocument(xml), xpath);
     *         v.bool(toMatch.equals(text)); } catch (Exception e) {
     *         v.bool(false);
     *         v.setComment("An error occured in extracting the value."
     *         +e.getMessage()); } return v; }
     */
    public Value extractValue(Document xml, Scale scale, String xpath, String commentXPath) {
        try {
            Document pcdlDoc = xml;
            String text = extractTextInternal(pcdlDoc, xpath);
            Value v = scale.createValue();
            v.parse(text);
            if (commentXPath != null) {
                String comment = extractTextInternal(pcdlDoc, commentXPath);
                v.setComment(comment);
            }
            return v;

        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error(
                "Could not parse XML " + " searching for path " + xpath + ": " + e.getMessage(), e);
            return null;
        }
    }

    public String extractText(Document xml, String xpath) {
        try {
            Document pcdlDoc = xml;
            String text = extractTextInternal(pcdlDoc, xpath);
            return text;
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error(
                "Could not parse XML " + " searching for path " + xpath + ": " + e.getMessage(), e);
            return null;
        }
    }

    public Document getDocument(InputSource xml) throws ParserConfigurationException, SAXException, IOException {
        // extract value via XPath
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document pcdlDoc = builder.parse(xml);
        return pcdlDoc;
    }

    public Value extractAttributeValue() {
        return null;
    }

    /**
     * very useful: {@link http
     * ://www.ibm.com/developerworks/library/x-javaxpathapi.html}
     * 
     * @param doc
     * @param path
     * @param scale
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    private String extractTextInternal(Document doc, String path) throws ParserConfigurationException, SAXException,
        IOException, XPathExpressionException {

        XPathFactory factory = XPathFactory.newInstance();

        XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new FitsNamespaceContext());
        XPathExpression expr = xpath.compile(path);
        try {
            String result = (String) expr.evaluate(doc, XPathConstants.STRING);
            return result;
        } catch (Exception e) {
            Logger.getLogger(this.getClass())
                .error("XML extraction for path " + path + " failed: " + e.getMessage(), e);
            return "XML extraction for path " + path + " failed: " + e.getMessage();
        }
    }

    public HashMap<String, String> extractValues(Document xml, String path) {
        try {
            HashMap<String, String> resultMap = new HashMap<String, String>();

            XPathFactory factory = XPathFactory.newInstance();

            XPath xpath = factory.newXPath();
            xpath.setNamespaceContext(new FitsNamespaceContext());
            XPathExpression expr = xpath.compile(path);

            NodeList list = (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    Node n = list.item(i);
                    String content = n.getTextContent();
                    if (content != null) {
                        resultMap.put(n.getLocalName(), content);
                    }
                }
            }
            return resultMap;
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).error(
                "Could not parse XML " + " searching for path " + path + ": " + e.getMessage(), e);
            return null;
        }
    }

}
