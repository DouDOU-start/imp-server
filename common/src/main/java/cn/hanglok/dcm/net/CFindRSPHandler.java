package cn.hanglok.dcm.net;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Allen
 * @version 1.0
 * @className CFindRSPHandler
 * @description TODO
 * @date 2023/9/8
 */
@Slf4j
public class CFindRSPHandler extends DimseRSPHandlerH {
    public CFindRSPHandler(int msgId, DimseType dimseType, SCU scu) {
        super(msgId, dimseType, scu);
    }

}