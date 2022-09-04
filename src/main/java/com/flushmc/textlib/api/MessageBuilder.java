package com.flushmc.textlib.api;

import com.flushmc.textlib.api.enums.HoverAction;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageBuilder {

    private TextComponent finalComponent, component;

    public MessageBuilder() {
        this.finalComponent = new TextComponent("");
    }

    public static MessageBuilder i() {
        return new MessageBuilder();
    }

    public MessageBuilder space() {
        if (component != null) {
            finalComponent.addExtra(component);
        }
        finalComponent.addExtra(" ");
        component = null;
        return this;
    }

    public MessageBuilder text(String text) {
        if (component != null) {
            finalComponent.addExtra(component);
        }
        component = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
        return this;
    }

    public MessageBuilder color(ChatColor color) {
        if (component != null) {
            component.setColor(color);
        }
        return this;
    }

    public MessageBuilder colorHEX(String hexString) {
        try {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(hexString);

            while (matcher.find()) {
                String color = hexString.substring(matcher.start(), matcher.end());
                component.setColor(ChatColor.of(color));
            }
        } catch (Exception ex) {}
        return this;
    }

    public MessageBuilder hoverText(String text) {
        if (component != null) {
            component.setHoverEvent(
                    new MessageEvent(HoverAction.SHOW_TEXT, text).getHoverEvent()
            );
        }
        return this;
    }

    public MessageBuilder event(MessageEvent event) {
        if (event != null && component != null) {
            if (event.getClickEvent() != null) {
                component.setClickEvent(event.getClickEvent());
            }
            if (event.getHoverEvent() != null) {
                component.setHoverEvent(event.getHoverEvent());
            }
        }
        return this;
    }

    public TextComponent build() {
        if (component != null) {
            finalComponent.addExtra(component);
        }
        return finalComponent;
    }

    public void send(Player player) {
        if (player != null && player.isOnline()) {
            player.spigot().sendMessage(build());
        }
    }

    public void sendToAll() {
        var onlinePlayers = Bukkit.getOnlinePlayers();
        if (!onlinePlayers.isEmpty()) {
            onlinePlayers.forEach(player -> player.spigot().sendMessage(build()));
        }
    }

}
