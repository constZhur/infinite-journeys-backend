package ru.mirea.infinitejourneysbackend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


import java.time.OffsetDateTime;
import java.util.List;

import static ru.mirea.infinitejourneysbackend.domain.model.Comment.COMMENT_TABLE_NAME;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = COMMENT_TABLE_NAME)
@Audited
@AuditTable("aud_" + COMMENT_TABLE_NAME)
public class Comment {
    public static final String COMMENT_TABLE_NAME = "ij_comment";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_id_seq")
    @SequenceGenerator(name = "comment_id_seq", sequenceName = "comment_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "tour_id")
    @NotAudited
    private Tour tour;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "author_id")
    @NotAudited
    private User author;

    @Column(name = "content", length = 1000)
    private String content;

    @NotAudited
    @Column(name = "is_anonymous", columnDefinition = "boolean default false")
    private Boolean isAnonymous = false;

    @NotAudited
    @Column(name = "content_modified", columnDefinition = "boolean default false")
    private boolean contentModified = false;

    @ManyToOne
    @NotAudited
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @NotAudited
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Comment> replies;

    @ManyToOne
    @NotAudited
    @JoinColumn(name = "answer_to_id")
    private Comment answerTo;

    @NotAudited
    @OneToMany(mappedBy = "answerTo", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Comment> repliesToThisComment;

    @NotAudited
    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @NotAudited
    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public boolean isAuthor(User user) {
        if (user == null) {
            return false;
        }
        return user.getId().equals(author.getId());
    }


}
