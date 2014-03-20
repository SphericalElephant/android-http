package at.diamonddogs.service.processor;

import com.google.gson.Gson;

public class GsonProcessor<CLAZZ extends Class<?>, OUTPUT> extends DataProcessor<String, OUTPUT> {
	protected final int processorId;
	protected final CLAZZ clazz;
	protected final Gson gson = new Gson();

	public GsonProcessor(CLAZZ clazz, int processorId) {
		this.clazz = clazz;
		this.processorId = processorId;
	}

	@Override
	protected String createParsedObjectFromByteArray(byte[] data) {
		return new String(data);
	}

	@Override
	protected OUTPUT parse(String inputObject) {
		return gson.fromJson(inputObject, clazz);
	}

	@Override
	public int getProcessorID() {
		return this.processorId;
	}
}
