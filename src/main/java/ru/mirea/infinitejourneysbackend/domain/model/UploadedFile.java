package ru.mirea.infinitejourneysbackend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ij_uploaded_file")
public class UploadedFile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "extension")
    private String extension;

    @Column(name = "path")
    private String path;

    @Column(name = "size")
    private Long size;

    @OneToMany(mappedBy = "file", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<TourFileRelation> countries;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
