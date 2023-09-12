package cn.hanglok.pacs.service;

import cn.hanglok.pacs.entity.SeriesTree;
import cn.hanglok.pacs.service.impl.ImageSeriesServiceImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Allen
 * @version 1.0
 * @className ImageSeriesTest
 * @description TODO
 * @date 2023/7/6 16:53
 */
public class ImageSeriesTest {

    private final Logger logger = LoggerFactory.getLogger(ImageSeriesTest.class);

    @Autowired
    ImageSeriesServiceImpl imageSeriesService;

    @Test
    public void getSeriesTree() {
        SeriesTree serviceTree = imageSeriesService.getTree(388L);
        logger.info(serviceTree.toString());
    }

}
