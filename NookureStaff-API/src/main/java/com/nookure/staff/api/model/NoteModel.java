package com.nookure.staff.api.model;

import com.nookure.staff.api.util.Object2Text;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "nookure_staff_notes")
public class NoteModel extends BaseDomain implements Object2Text {
    @ManyToOne(optional = false)
    PlayerModel player;

    @Column(length = 1024, nullable = false)
    String note;

    @Column(nullable = false)
    Boolean active = true;

    @Column(nullable = false, name = "show_on_join")
    Boolean showOnJoin = true;

    @Column(nullable = false, name = "show_only_to_administrators")
    Boolean showOnlyToAdministrators = false;

    public PlayerModel getPlayer() {
        return player;
    }

    public NoteModel setPlayer(PlayerModel player) {
        this.player = player;
        return this;
    }

    public String getNote() {
        return note;
    }

    public NoteModel setNote(String note) {
        this.note = note;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public NoteModel setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getShowOnJoin() {
        return showOnJoin;
    }

    public NoteModel setShowOnJoin(Boolean showOnJoin) {
        this.showOnJoin = showOnJoin;
        return this;
    }

    public Boolean getShowOnlyToAdministrators() {
        return showOnlyToAdministrators;
    }

    public NoteModel setShowOnlyToAdministrators(Boolean showOnlyToAdministrators) {
        this.showOnlyToAdministrators = showOnlyToAdministrators;
        return this;
    }

    @Override
    public String replaceText(String text) {
        return text.replace("{note.active}", active.toString())
                .replace("{note.note}", note)
                .replace("{note.showOnJoin}", showOnJoin.toString())
                .replace("{note.id}", id.toString())
                .replace("{note.showOnlyToAdministrators}", showOnlyToAdministrators.toString());
    }
}
