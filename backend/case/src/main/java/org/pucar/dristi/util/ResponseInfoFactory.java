package org.pucar.dristi.util;

<<<<<<< HEAD
import static org.pucar.dristi.config.ServiceConstants.FAILED;
import static org.pucar.dristi.config.ServiceConstants.RES_MSG_ID;
import static org.pucar.dristi.config.ServiceConstants.SUCCESSFUL;

=======
>>>>>>> main
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.stereotype.Component;

<<<<<<< HEAD
=======
import static org.pucar.dristi.config.ServiceConstants.*;

>>>>>>> main
@Component
public class ResponseInfoFactory {

	public ResponseInfo createResponseInfoFromRequestInfo(final RequestInfo requestInfo, final Boolean success) {

		final String apiId = requestInfo != null ? requestInfo.getApiId() : "";
		final String ver = requestInfo != null ? requestInfo.getVer() : "";
		Long ts = null;
		if (requestInfo != null)
			ts = requestInfo.getTs();
<<<<<<< HEAD
		final String resMsgId = RES_MSG_ID;
=======
		final String resMsgId = RES_MSG_ID; // FIXME : Hard-coded
>>>>>>> main
		final String msgId = requestInfo != null ? requestInfo.getMsgId() : "";
		final String responseStatus = success ? SUCCESSFUL : FAILED;

		return ResponseInfo.builder().apiId(apiId).ver(ver).ts(ts).resMsgId(resMsgId).msgId(msgId).resMsgId(resMsgId)
				.status(responseStatus).build();
	}

}