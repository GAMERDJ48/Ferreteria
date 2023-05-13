package com.ferreteria.ferreteria.entities.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "excelsave")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExcelSave extends Base{

    private String nombre;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{NotNull.fecha}")
    private LocalDate fecha;
}
