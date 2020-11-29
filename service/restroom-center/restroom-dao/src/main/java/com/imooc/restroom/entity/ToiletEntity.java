package com.imooc.restroom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "toilet")
public class ToiletEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "clean", nullable = false)
    private boolean clean;

    @Column(name = "available", nullable = false)
    private boolean available;

    @Column(name = "reserved", nullable = false)
    private boolean reserved;
}
