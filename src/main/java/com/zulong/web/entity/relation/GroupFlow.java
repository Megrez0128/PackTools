package com.zulong.web.entity.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class  GroupFlow  implements Serializable {
    private int group_id;
    private int flow_id;
}
