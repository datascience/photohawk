package at.ac.tuwien.RAWverna.evaluation.evaluators.javafitsevaluator;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class FitsNamespaceContext implements NamespaceContext {

    public String getNamespaceURI(String prefix) {
        if (prefix == null)
            throw new NullPointerException("Null prefix");
        else if ("fits".equals(prefix))
            return "http://hul.harvard.edu/ois/xml/ns/fits/fits_output";
        else if ("xml".equals(prefix))
            return XMLConstants.XML_NS_URI;
        return XMLConstants.NULL_NS_URI;
    }

    // This method isn't necessary for XPath processing.
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    // This method isn't necessary for XPath processing either.
    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }

}
