package com.khoatrbl.blog.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID tagId;

    @Column(nullable = false, unique = true)
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagId, tag.tagId) && Objects.equals(tagName, tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, tagName);
    }
}
