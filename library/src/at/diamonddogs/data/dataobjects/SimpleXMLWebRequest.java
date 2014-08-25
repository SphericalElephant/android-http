package at.diamonddogs.data.dataobjects;

import java.io.StringWriter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import at.diamonddogs.http.entity.XMLHttpEntity;

/**
 * {@link WebRequest} with XML payload (created using SimpleXML
 * http://simple.sourceforge.net/).
 * 
 * @param <T>
 *            the type of {@link Object} to write
 */
public class SimpleXMLWebRequest<T> extends WebRequest {

	private static final long serialVersionUID = 4327428207485203155L;

	private final Serializer serializer = new Persister();

	public SimpleXMLWebRequest(T data) {
		try {
			StringWriter w = new StringWriter();
			serializer.write(data, w);
			XMLHttpEntity e = new XMLHttpEntity(w.getBuffer().toString());
			setHttpEntity(e);
		} catch (Throwable tr) {
			throw new RuntimeException(tr);
		}
	}
}
