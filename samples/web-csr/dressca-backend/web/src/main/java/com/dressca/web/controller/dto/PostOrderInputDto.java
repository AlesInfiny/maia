package com.dressca.web.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostOrderInputDto {
  private String fullName;
  private String postalCode;
  private String todofuken;
  private String shikuchoson;
  private String azanaAndOthers;
}
