package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Meta  implements Serializable {
    private int meta_id;
    private int group_id;
    private int version;
    private String version_display;
    private String data;
}
