package org.pucar.dristi.util;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;

import static org.pucar.dristi.config.ServiceConstants.*;

@Component
@Slf4j
public class ResponseInfoFactory {

	public ResponseInfo createResponseInfoFromRequestInfo(final RequestInfo requestInfo, final Boolean success) {

		try {
			final String apiId = requestInfo != null ? requestInfo.getApiId() : "";
			final String ver = requestInfo != null ? requestInfo.getVer() : "";
			Long ts = null;
			if (requestInfo != null)
				ts = requestInfo.getTs();
			final String resMsgId = RES_MSG_ID;
			final String msgId = requestInfo != null ? requestInfo.getMsgId() : "";
			final String responseStatus = success ? SUCCESSFUL : FAILED;

			return ResponseInfo.builder().apiId(apiId).ver(ver).ts(ts).resMsgId(resMsgId).msgId(msgId).resMsgId(resMsgId)
					.status(responseStatus).build();
		} catch (Exception e) {
			log.error("Error while preparing response info object from request info :: {}", e.toString());
			throw new CustomException(RESPONSE_INFO_FACTORY_EXCEPTION,"Error while preparing response info object from request info: "+e.getMessage());
		}
	}

}