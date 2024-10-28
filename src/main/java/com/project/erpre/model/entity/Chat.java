package com.project.erpre.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "m_chat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatNo;

    @Column(name = "chat_origin_title", length = 50, nullable = false)
    private String chatOriginTitle;

    @OneToMany(mappedBy = "chat")
    @JsonIgnore
    @ToString.Exclude
    private List<ChatParticipant> chatParticipantList;

    @OneToMany(mappedBy = "chat")
    @JsonIgnore
    @ToString.Exclude
    private List<ChatMessage> chatMessageList;
}
