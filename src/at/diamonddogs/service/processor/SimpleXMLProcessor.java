package at.diamonddogs.service.processor;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import at.diamonddogs.exception.ProcessorExeception;

/**
 * An XML processor that uses the SimpleXML framework
 * (http://simple.sourceforge.net/) to parse XML data.Due to the dynamic nature
 * of this processor, users will have to provide a processorId to the
 * constructor.
 * 
 * @param <CLASS>
 *            the input class that needs to be provided to the constructor
 */
public class SimpleXMLProcessor<OUTPUT> extends DataProcessor<String, OUTPUT> {
	private final Serializer serializer = new Persister();
	private Class<OUTPUT> clazz;
	protected final int processorId;

	/**
	 * Constructor
	 * 
	 * @param clazz
	 *            the class instance of the {@link Object} to be parsed
	 * @param processorId
	 *            a app unique processorId to be used by this processor
	 */
	public SimpleXMLProcessor(Class<OUTPUT> clazz, int processorId) {
		this.clazz = clazz;
		this.processorId = processorId;
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

	@Override
	public int getProcessorID() {
		return this.processorId;
	}
}
