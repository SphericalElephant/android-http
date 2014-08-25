package at.diamonddogs.http.entity;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

/**
 * A simple {@link StringEntity} wrapper for XML data, please note that this
 * wrapper merely sets the content type and does no XML validation, checking,
 * etc.
 * 
 */
public class XMLHttpEntity extends StringEntity {
	public XMLHttpEntity(String s) throws UnsupportedEncodingException {
		this(s, false);
	}

	public XMLHttpEntity(String s, boolean readAbleByCasualUsers) throws UnsupportedEncodingException {
		super(s);
		if (readAbleByCasualUsers) {
			setContentType("text/xml");
		} else {
			setContentType("application/xml");
		}
	}

	public XMLHttpEntity(String s, String charset) throws UnsupportedEncodingException {
		this(s, charset, false);
	}

	public XMLHttpEntity(String s, String charset, boolean readAbleByCasualUsers) throws UnsupportedEncodingException {
		super(s, charset);
		if (readAbleByCasualUsers) {
			setContentType("text/xml");
		} else {
			setContentType("application/xml");
		}
	}
}
