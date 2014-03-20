package at.diamonddogs.service.processor;

import org.json.JSONObject;

public class JSONDispatchObjectProcessor extends JSONProcessor<JSONObject>{

	@Override
	protected JSONObject parse(JSONObject inputObject) {
		return inputObject;
	}

	@Override
	public int getProcessorID() {
		return 235928525;
	}

}
