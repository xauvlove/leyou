package com.leyou.com.leyou.upload.service;

import com.leyou.com.leyou.upload.config.UploadProperties;
import com.leyou.common.enums.LyMarketExceptionEnum;
import com.leyou.common.exception.LyMarketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    @Autowired
    private UploadProperties prop;

    public String uploadImage(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();
            if(!prop.getAllowTypes().contains(contentType)) {
                log.error("UploadService uploadImage");
                throw new LyMarketException(LyMarketExceptionEnum.INVALID_IMAGE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null) {
                throw new LyMarketException(LyMarketExceptionEnum.INVALID_IMAGE_CONTENT);
            }
            //1.保存文件到本地
            //准备目标路径
            String path = prop.getBaseImageStoredPath() + UUID.randomUUID().toString() + "-";
            File dest = new File(path + file.getOriginalFilename());
            //2.返回路径
            file.transferTo(dest);
            return prop.getBaseAccessUrl() + dest.getName();
        } catch (Exception e) {
            log.error("UploadService uploadImage", e);
            e.printStackTrace();
            throw new LyMarketException(LyMarketExceptionEnum.UPLOAD_IMAGE_ERROR);
        }
    }
}
