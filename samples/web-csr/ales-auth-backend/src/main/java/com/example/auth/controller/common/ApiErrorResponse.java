package com.example.auth.controller.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * エラーレスポンスクラス
 */
@Data
@JsonIgnoreProperties(ignoreUnknown=true)
@NoArgsConstructor
public class ApiErrorResponse {

    /**
     *  HTTPステータスコード
     */
    @JsonProperty("status")
    private int status;

    /**
     *  エラーメッセージタイトル
     */
    @JsonProperty("title")
    private String title;

    /**
     *  エラーメッセージ詳細
     */
    @JsonProperty("details")
    private String details;

    /**
     * レスポンス日時
     */
    @JsonProperty("response_dt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Tokyo")
    private OffsetDateTime responseDt;

}
