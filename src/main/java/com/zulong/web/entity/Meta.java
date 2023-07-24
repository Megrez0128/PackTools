package com.zulong.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Meta  implements Serializable {
    private int meta_id;
    private int group_id;
    private int version;
    private String version_display;
    private String data;

    public static Meta getMetaFromResultSet(ResultSet resultSet) throws SQLException {
        Meta meta = new Meta();
        meta.setMeta_id(resultSet.getInt("meta_id"));
        meta.setGroup_id(resultSet.getInt("group_id"));
        meta.setVersion(resultSet.getInt("version"));
        meta.setVersion_display(resultSet.getString("version_display"));
        Blob dataBlob = resultSet.getBlob("data");
        String dataString = new String(dataBlob.getBytes(1, (int) dataBlob.length()));
        meta.setData(dataString);
        return meta;
    }
}
