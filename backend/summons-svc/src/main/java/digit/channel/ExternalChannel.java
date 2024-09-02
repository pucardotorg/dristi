package digit.channel;

import digit.web.models.ChannelMessage;
import digit.web.models.TaskRequest;

public interface ExternalChannel {

    ChannelMessage sendSummons(TaskRequest request);
}
