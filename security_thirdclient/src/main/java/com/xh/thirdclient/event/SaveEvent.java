package com.xh.thirdclient.event;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * author  Xiao Hong
 * date  2021/7/31 9:41
 * description
 */
@Data
public class SaveEvent {

    private Long aid;

    private String name;

}
