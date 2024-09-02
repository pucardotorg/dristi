package org.egov.web.notification.mail.service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONArray;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.egov.web.notification.mail.config.ApplicationConfiguration;
import org.egov.web.notification.mail.consumer.contract.Email;
import org.egov.web.notification.mail.utils.Constants;
import org.egov.web.notification.mail.utils.LocalizationUtil;
import org.egov.web.notification.mail.utils.MdmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static org.egov.web.notification.mail.utils.Constants.*;

@Component
public class MessageConstruction {

    @Autowired
    private MdmsUtil mdmsUtil;

    @Autowired
    private ApplicationConfiguration config;

    @Autowired
    private LocalizationUtil localizationUtil;


    public String constructMessage(Email email) {
        try {
            String templateId = Constants.EMAIL_TEMPLATE_MASTER_NAME;
            String filter = "[?(@['code'] == '"+ email.getTemplateCode() + "')]";

            String moduleName = Constants.EMAIL_TEMPLATE_MODULE_NAME;
            HashMap<String, String> masterName = new HashMap<>();
            masterName.put(templateId, filter);

            RequestInfo requestInfo = new RequestInfo();

            Map<String, Map<String, JSONArray>> mdmsData = mdmsUtil.fetchMdmsData(requestInfo, config.getStateTenantId(), moduleName, masterName);

            //set message codes
            Set<String> messageCodes = new HashSet<>();
            messageCodes.add(GREETING);
            messageCodes.add(email.getTemplateCode());
            messageCodes.add(FOOTER);

            JSONArray templateArray = mdmsData.get(moduleName).get(templateId);
            if(templateArray == null || templateArray.isEmpty()){
                throw new RuntimeException("Template not found");
            }

            LinkedHashMap templateMap = (LinkedHashMap) templateArray.get(0);
            String handlerTemplate = String.valueOf(templateMap.get("value"));
            Map<String, Map<String, String>> messageResponse = localizationUtil.getLocalisedMessages(requestInfo, config.getStateTenantId(), Constants.MESSAGE_LOCALE, Constants.LOCALIZATION_MODULE, messageCodes);

            Handlebars handlebars = new Handlebars();
            Template template = null;
            try {
                template = handlebars.compileInline(handlerTemplate);
                Map<String, String> data = new HashMap<>();

                Map<String, String> messageMap = messageResponse.get(Constants.MESSAGE_LOCALE + "|" + config.getStateTenantId());
                data.put("header", messageMap.get(GREETING));
                data.put("message", messageMap.get(email.getTemplateCode()));
                data.put("footer", messageMap.get(FOOTER));
                String mainTemplate = template.apply(data);
                Template template1 = handlebars.compileInline(mainTemplate);
                data.clear();

                String emailBody = email.getBody();
                JsonObject jsonObject = JsonParser.parseString(emailBody).getAsJsonObject();

                for(String key: jsonObject.keySet()){
                    data.put(key, jsonObject.get(key).getAsString());
                }
                return template1.apply(data);
            } catch (Exception e) {
                throw new CustomException("HANDLEBARS_ERROR", "Error while compiling handlebars template");
            }
        } catch (Exception e) {
            throw new CustomException(MESSAGE_CONSTRUCTION_ERROR, e.getLocalizedMessage());
        }
    }
}
