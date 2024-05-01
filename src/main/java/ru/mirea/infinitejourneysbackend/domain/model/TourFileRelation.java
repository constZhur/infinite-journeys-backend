package ru.mirea.infinitejourneysbackend.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ij_tour_file_relation")
public class TourFileRelation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tour_file_id_seq")
    @SequenceGenerator(name = "tour_file_id_seq", sequenceName = "tour_file_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private UploadedFile file;
}
