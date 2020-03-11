package com.leyou.page.mq;

import com.leyou.page.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListener {

    @Autowired
    private PageService pageService;

    @RabbitListener(bindings = @QueueBinding(
            // 队列名字自取
            value = @Queue(name = "page.item.insert.queue", durable = "true"),
            // 交换机，是消息发送者定义的
            exchange = @Exchange(name = "ly.item.change", type = ExchangeTypes.TOPIC),
            // 监听消息的类型  增加和更新商品
            key = {"item.insert", "item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId) {
        if(spuId == null) {
            return;
        }
        //
        pageService.createHtml(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            // 队列名字自取
            value = @Queue(name = "search.item.delete.queue", durable = "true"),
            // 交换机，是消息发送者定义的
            exchange = @Exchange(name = "ly.item.exchange", type = ExchangeTypes.TOPIC),
            // 监听消息的类型  增加和更新商品
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId) {
        if(spuId == null) {
            return;
        }
        pageService.deleteHtml(spuId);
    }
}