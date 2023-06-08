package com.flushmc.textlib.api;

import com.flushmc.textlib.api.enums.ClickAction;
import com.flushmc.textlib.api.enums.HoverAction;
import com.flushmc.textlib.shared.ReflectionUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

public class MessageEvent {

    private ClickAction clickAction;
    private HoverAction hoverAction;
    private String clickText, hoverText;
    private ItemStack itemStack;

    public MessageEvent(ClickAction clickAction, String clickText) {
        this.clickAction = clickAction;
        this.clickText = clickText;
    }

    public MessageEvent(HoverAction hoverAction, ItemStack itemStack) {
        this.hoverAction = hoverAction;
        this.itemStack = itemStack;
    }

    public MessageEvent(HoverAction hoverAction, String hoverText) {
        this.hoverAction = hoverAction;
        this.hoverText = ChatColor.translateAlternateColorCodes('&', hoverText);
    }

    public ClickEvent getClickEvent() {
        if (clickAction == null || clickText == null) return null;
        return new ClickEvent(clickAction.getAction(), clickText);
    }

    public HoverEvent getHoverEvent() {
        if (hoverAction == null || (itemStack == null && hoverText == null)) return null;
        if (itemStack == null) {
            return new HoverEvent(
                    hoverAction.getAction(),
                    new ComponentBuilder(hoverText).create()
            );
        } else {
            var json = convertItemStackToString(itemStack);
            return new HoverEvent(
                    hoverAction.getAction(),
                    new ComponentBuilder(json).create()
            );
        }
    }

    private String convertItemStackToString(ItemStack itemStack) {
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            return null;
        }

        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }


}
