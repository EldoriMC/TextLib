package com.flushmc.textlib.api.enums;

import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;

public enum ClickAction {

    COPY(ClickEvent.Action.COPY_TO_CLIPBOARD),
    OPEN_URL(ClickEvent.Action.OPEN_URL),
    RUN_COMMAND(ClickEvent.Action.RUN_COMMAND),
    SUGGEST_COMMAND(ClickEvent.Action.SUGGEST_COMMAND);

    @Getter private ClickEvent.Action action;

    ClickAction(ClickEvent.Action action) {
        this.action = action;
    }

}
