package ru.mdashlw.rankedwho.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.ListIterator;

public class ChatUtil {
    public static void addChatMessageWithId(int id, IChatComponent component) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(component, id);
    }

    public static void editMessage(int id, IChatComponent component) {
        GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        List<ChatLine> chatLines;
        List<ChatLine> drawnChatLines;

        try {
            chatLines = ReflectionUtil.getObfuscatedField(chatGUI, "field_146252_h", "chatLines");
            drawnChatLines = ReflectionUtil.getObfuscatedField(chatGUI, "field_146253_i", "drawnChatLines");
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            exception.printStackTrace();
            return;
        }

        editChatLineList(chatLines, id, component);
        editChatLineList(drawnChatLines, id, component);
    }

    private static void editChatLineList(List<ChatLine> list, int id, IChatComponent component) {
        ListIterator<ChatLine> iterator = list.listIterator();

        while (iterator.hasNext()) {
            ChatLine line = iterator.next();

            if (line.getChatLineID() != id) {
                continue;
            }

            iterator.remove();
            iterator.add(new ChatLine(line.getUpdatedCounter(), component, id));
        }
    }
}
