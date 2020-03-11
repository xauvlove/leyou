package com.leyou.search.mq;

import com.leyou.search.service.SearchService;
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
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            // 队列名字自取
            value = @Queue(name = "search.item.insert.queue", durable = "true"),
            // 交换机，是消息发送者定义的
            exchange = @Exchange(name = "ly.item.change", type = ExchangeTypes.TOPIC),
            // 监听消息的类型  增加和更新商品
            key = {"item.insert", "item.update"}
    ))
    public void listenInsertOrUpdate(Long spuId) {
        if(spuId == null) {
            return;
        }
        //处理消息  对索引库进行新增或修改
        searchService.createOrUpdateIndex(spuId);
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
        //处理消息  对索引库进行新增或修改
        searchService.deleteIndex(spuId);
    }
}