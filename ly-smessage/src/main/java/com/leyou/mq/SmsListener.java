package com.leyou.mq;

import com.leyou.common.utils.JsonUtils;
import com.leyou.properties.SmsProperties;
import com.leyou.utils.SmsUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.Map;

@Component
@EnableConfigurationProperties(value = SmsProperties.class)
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private SmsProperties smsProperties;

    /**
     * 发送短信验证码
     * @param
     */
    @RabbitListener(bindings = @QueueBinding(
            // 队列名字自取  短信验证码队列
            value = @Queue(name = "sms.verify.code.queue", durable = "true"),
            // 交换机，是消息发送者定义的
            exchange = @Exchange(name = "ly.sms.change", type = ExchangeTypes.TOPIC),
            // 监听消息的类型  增加和更新商品
            key = {"sms.verify.code"}
    ))
    public void listenInsertOrUpdate(Map<String, String> msg) {
        if(CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.remove("phone");
        if(StringUtils.isEmpty(phone)) {
            return;
        }
        smsUtil.sendSms(phone, smsProperties.getSignName(),
                smsProperties.getVerifyCodeTemplate(), JsonUtils.serialize(msg));
    }
}
