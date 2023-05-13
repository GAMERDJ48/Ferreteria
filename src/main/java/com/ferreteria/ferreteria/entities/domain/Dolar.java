package com.ferreteria.ferreteria.entities.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dolar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dolar extends Base{
    private BigDecimal valor;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime fecha;
}
