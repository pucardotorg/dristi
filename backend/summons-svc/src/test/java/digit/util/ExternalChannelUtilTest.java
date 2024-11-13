package digit.util;

import digit.channel.ChannelFactory;
import digit.channel.ExternalChannel;
import digit.web.models.SummonsDelivery;
import digit.web.models.TaskRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExternalChannelUtilTest {

    @InjectMocks
    private ExternalChannelUtil externalChannelUtil;

    @Mock
    private TaskRequest request;

    @Mock
    private SummonsDelivery summonsDelivery;

    @Mock
    private ChannelFactory channelFactory;

    @Mock
    private ExternalChannel externalChannel;

    @Test
    public void sendSummonsByDeliveryChannel() {
        // Arrange
        when(channelFactory.getDeliveryChannel(any())).thenReturn(externalChannel);
        // Act
        externalChannelUtil.sendSummonsByDeliveryChannel(request, summonsDelivery);

        // Assert
    }
}
