package org.utn.tup.psbacknewsandnotifications.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendEmailDto {
 private    String to;
  private   String subject;
  private   String body;
}
