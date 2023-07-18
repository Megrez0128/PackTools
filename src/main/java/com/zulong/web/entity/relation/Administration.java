package com.zulong.web.entity.relation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administration implements Serializable {
    private boolean update_allowed;
    private boolean delete_allowed;
    private String user_id;  // primary
    private int group_id;  // primary

    public Administration(Administration ad) {
        this.update_allowed = ad.update_allowed;
        this.delete_allowed = ad.delete_allowed;
        this.user_id = ad.user_id;
        this.group_id = ad.group_id;
    }

    public Administration(String user_id, int group_id){
        this.update_allowed = false;
        this.delete_allowed = false;
        this.user_id = user_id;
        this.group_id = group_id;
    }
}
