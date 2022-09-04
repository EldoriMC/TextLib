package com.flushmc.textlib.api.enums;

import lombok.Getter;
import net.md_5.bungee.api.chat.HoverEvent;

public enum HoverAction {

    SHOW_ITEM(HoverEvent.Action.SHOW_ITEM),
    SHOW_TEXT(HoverEvent.Action.SHOW_TEXT);

    @Getter private HoverEvent.Action action;

    HoverAction(HoverEvent.Action action) {
        this.action = action;
    }

}
