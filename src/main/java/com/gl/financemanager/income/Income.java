package com.gl.financemanager.income;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "INCOME")
public class Income {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
