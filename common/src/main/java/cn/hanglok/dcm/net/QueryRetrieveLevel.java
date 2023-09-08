package cn.hanglok.dcm.net;

import lombok.Getter;

/**
 * @author Allen
 * @version 1.0
 * @enum QueryRetrieveLevel
 * @description Base the queryRetrieverLevel to retriever the data
 * @date 2023/9/6
 */
@Getter
public enum QueryRetrieveLevel {
    Patient,
    Study,
    Series,
    Image;
}
