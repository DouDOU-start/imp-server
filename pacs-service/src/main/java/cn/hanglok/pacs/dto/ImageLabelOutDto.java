package cn.hanglok.pacs.dto;

import cn.hanglok.pacs.entity.ImageLabel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Allen
 * @version 1.0
 * @className ImageLabelOutDto
 * @description TODO
 * @date 2023/8/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageLabelOutDto extends ImageLabel {
    private List<Integer> organId;

    public ImageLabelOutDto(Long id, String organId) {
        this.setId(id);
        if (null != organId) {
            this.organId = Arrays.stream(organId.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else {
            this.organId = List.of();
        }
    }
}
