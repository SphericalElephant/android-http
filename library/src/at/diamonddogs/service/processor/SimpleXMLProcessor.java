package at.diamonddogs.service.processor;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import at.diamonddogs.exception.ProcessorExeception;

/**
 * An XML processor that uses the SimpleXML framework
 * (http://simple.sourceforge.net/) to parse XML data.
 * 
 * @param <CLASS>
 *            the input class that needs to be provided to the constructor
 * @param <OUTPUT>
 *            the output object type, which is linked to the <CLASS> parameter
 */
public abstract class SimpleXMLProcessor<CLASS extends Class<OUTPUT>, OUTPUT> extends DataProcessor<String, OUTPUT> {
	private final Serializer serializer = new Persister();
	private CLASS clazz;

	public SimpleXMLProcessor(CLASS clazz) {
		this.clazz = clazz;
	}

	@Override
	protected String createParsedObjectFromByteArray(byte[] data) {
		return new String(data);
	}

	@Override
	protected OUTPUT parse(String inputObject) {
		try {
			return serializer.read(clazz, inputObject);
		} catch (Exception e) {
			throw new ProcessorExeception(e);
		}
	}
}
