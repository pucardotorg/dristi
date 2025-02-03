package digit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.TimeZone;

@Component
@Data
@Import({TracerConfiguration.class})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Configuration {


    // User Config
    @Value("${egov.user.host}")
    private String userHost;

    @Value("${egov.user.context.path}")
    private String userContextPath;

    @Value("${egov.user.create.path}")
    private String userCreateEndpoint;

    @Value("${egov.user.search.path}")
    private String userSearchEndpoint;

    @Value("${egov.user.update.path}")
    private String userUpdateEndpoint;


    //Idgen Config
    @Value("${egov.idgen.host}")
    private String idGenHost;

    @Value("${egov.idgen.path}")
    private String idGenPath;


    //Workflow Config
    @Value("${egov.workflow.host}")
    private String wfHost;

    @Value("${egov.workflow.transition.path}")
    private String wfTransitionPath;

    @Value("${egov.workflow.businessservice.search.path}")
    private String wfBusinessServiceSearchPath;

    @Value("${egov.workflow.processinstance.search.path}")
    private String wfProcessInstanceSearchPath;


    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;


    //HRMS
    @Value("${egov.hrms.host}")
    private String hrmsHost;

    @Value("${egov.hrms.search.endpoint}")
    private String hrmsEndPoint;


    //URLShortening
    @Value("${egov.url.shortner.host}")
    private String urlShortnerHost;

    @Value("${egov.url.shortner.endpoint}")
    private String urlShortnerEndpoint;


    //SMSNotification
    @Value("${egov.sms.notification.topic}")
    private String smsNotificationTopic;

    //DiaryEntry Create topic
    @Value("${diaryentry.kafka.create.topic}")
    private String diaryEntryCreateTopic;

    @Value("${diaryentry.kafka.update.topic}")
    private String diaryEntryUpdateTopic;

    @Value("${diary.kafka.update.topic}")
    private String diaryUpdateTopic;

    // Filestore Config
    @Value("${egov.filestore.host}")
    private String fileStoreHost;

    @Value("${egov.file.store.save.endpoint}")
    private String fileStoreSaveEndPoint;

    @Value("${egov.filestore.path}")
    private String fileStorePath;

    @Value("${egov.filestore.caseDiary.module}")
    private String fileStoreCaseDiaryModule;

    @Value("${caseDiary.create.topic}")
    private String caseDiaryTopic;

    //court details
    @Value("${court.id}")
    private String courtId;

    @Value("${court.name}")
    private String courtName;

    @Value("${court.enabled}")
    private Boolean courtEnabled;

    @Value("${judge.name}")
    private String judgeName;

    @Value("${judge.designation}")
    private String judgeDesignation;

    //Pdf Services
    @Value("${egov.pdf.service.host}")
    private String pdfServiceHost;

    @Value("${egov.pdf.service.create.endpoint}")
    private String pdfServiceEndpoint;

    @Value("${aDiary.pdf.template.key}")
    private String aDiaryPdfTemplateKey;

    @Value("${bDiary.pdf.template.key}")
    private String bDiaryPdfTemplateKey;

    @Value("${egov.workflow.case.diary.business.name}")
    private String caseDiaryBusinessName;

    @Value("${egov.workflow.case.diary.business.service.name}")
    private String caseDiaryBusinessServiceName;

    @Value("${dristi.case.host}")
    private String caseHost;

    @Value("${dristi.case.search.path}")
    private String caseSearchPath;

}
