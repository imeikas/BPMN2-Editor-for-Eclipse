package org.jboss.bpmn2.editor.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.xml.sax.InputSource;


public class BPMN2ContentDescriber implements ITextContentDescriber {

	private static final String BPMN2_NAMESPACE = "http://www.omg.org/spec/BPMN/20100524/MODEL"; //$NON-NLS-1$
	private static final String ROOT_ELEMENT = "definitions"; //$NON-NLS-1$

	private RootElementParser parser;

	public BPMN2ContentDescriber() {
	}

	public int describe(Reader contents, IContentDescription description) throws IOException {
		return doDescribe(contents) == null ? INVALID : VALID;
	}

	public int describe(InputStream contents, IContentDescription description) throws IOException {
		return describe(new InputStreamReader(contents), description);
	}

	private synchronized String doDescribe(Reader contents) throws IOException {
		try {
			InputSource source = new InputSource(contents);
			parser = new RootElementParser();
			parser.parse(source);
		} catch (AcceptedException e) {
			return e.acceptedRootElement;
		} catch (RejectedException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
		finally {
			parser = null;
		}

		return null;
	}

	public QualifiedName[] getSupportedOptions() {
		return null;
	}

	private class RootElementParser extends SAXParser {
		public void startElement(QName qName, XMLAttributes attributes, Augmentations augmentations) throws XNIException {

			super.startElement(qName, attributes, augmentations);

			if (ROOT_ELEMENT.equals(qName.localpart)) {
				String namespace = fNamespaceContext.getURI(qName.prefix);
				if (BPMN2_NAMESPACE.equals(namespace))
					throw new AcceptedException(qName.localpart);
				else
					throw new RejectedException();
			} else
				throw new RejectedException();
		}
	}

	private class AcceptedException extends RuntimeException {
		public String acceptedRootElement;

		public AcceptedException(String acceptedRootElement) {
			this.acceptedRootElement = acceptedRootElement;
		}

		private static final long serialVersionUID = 1L;
	}

	private class RejectedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}
