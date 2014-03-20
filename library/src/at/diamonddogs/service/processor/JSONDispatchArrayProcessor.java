package at.diamonddogs.service.processor;

import org.json.JSONArray;

public class JSONDispatchArrayProcessor extends JSONArrayProcessor<JSONArray> {

	@Override
	protected JSONArray parse(JSONArray inputObject) {
		return inputObject;
	}

	@Override
	public int getProcessorID() {
		return 437831647;
	}

}
