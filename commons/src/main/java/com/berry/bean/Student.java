package com.berry.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author fengfshao
 * @since 2023/7/24
 */

@Data
@AllArgsConstructor
public class Student extends Person {
  private String name;
  private double score;
}
