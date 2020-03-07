package com.leyou.search.repository;

import com.leyou.common.utils.TransferTToKUtil;
import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.item.vo.SpuVO;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.service.SearchService;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Aspect
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Autowired
    private TransferTToKUtil<SpuVO, Spu> tToKUtil;

    @Test
    public void test() {
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }

    @Test
    public void test3() {
        Goods goods = new Goods();
        goods.setSkus("skus");
        goods.setSubTitle("subtitle");
        goods.setAll("all");
        goodsRepository.save(goods);
    }

    @Test
    public void test4() {
        Iterable<Goods> all = goodsRepository.findAll();
        Iterator<Goods> iterator = all.iterator();
        int i=0;
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
            i++;
        }
        System.out.println(i);
    }

    @Test
    public void test2() throws Exception{
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            PageResult<SpuVO> spuVOPageResult = goodsClient.querySpuByPage(page, rows, true, null);
            List<SpuVO> spuVOList = spuVOPageResult.getItems();
            if(CollectionUtils.isEmpty(spuVOList)) {
                break;
            }

            List<Goods> goodsList = new ArrayList<>(spuVOList.size());
            for (SpuVO spuVO : spuVOList) {
                Spu spu = tToKUtil.transferTToK(spuVO, Spu.class);
                Goods goods = searchService.buildEasyGoodsForSearch(spu);
                goodsList.add(goods);
            }

            goodsRepository.saveAll(goodsList);
            page ++;
            size = spuVOList.size();
        } while(size == rows);
    }


    final static String url = "http://localhost:9200/goods/docs";



    /**
     * 发送HttpPost请求
     *
     * @param strURL
     *            服务地址
     * @param params
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     * @return 成功:返回json字符串<br/>
     */
    public static String post(String strURL, String params) {
        System.out.println(strURL);
        System.out.println(params);
        BufferedReader reader = null;
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            // connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            //一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            String res = "";
            while ((line = reader.readLine()) != null) {
                res += line;
            }
            reader.close();

            //如果一定要使用如下方式接收响应数据， 则响应必须为: response.getWriter().print(StringUtils.join("{\"errCode\":\"1\",\"errMsg\":\"", message, "\"}")); 来返回
//            int length = (int) connection.getContentLength();// 获取长度
//            if (length != -1) {
//                byte[] data = new byte[length];
//                byte[] temp = new byte[512];
//                int readLen = 0;
//                int destPos = 0;
//                while ((readLen = is.read(temp)) > 0) {
//                    System.arraycopy(temp, 0, data, destPos, readLen);
//                    destPos += readLen;
//                }
//                String result = new String(data, "UTF-8"); // utf-8编码
//                System.out.println(result);
//                return result;
//            }

            return res;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "error"; // 自定义错误信息
    }

}
