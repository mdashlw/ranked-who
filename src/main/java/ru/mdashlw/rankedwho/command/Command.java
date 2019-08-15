package ru.mdashlw.rankedwho.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import ru.mdashlw.rankedwho.RankedWho;

import java.util.Collections;
import java.util.List;

public abstract class Command implements ICommand {
    protected RankedWho mod;

    public Command(RankedWho mod) {
        this.mod = mod;
    }

    public abstract String getName();

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    public boolean isAsync() {
        return false;
    }

    public void register() {
        ClientCommandHandler.instance.registerCommand(this);
    }

    public abstract void execute(ICommandSender sender, String[] args);

    @Override
    public String getCommandName() {
        return getName();
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public List<String> getCommandAliases() {
        return getAliases();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (isAsync()) {
            mod.getCommandHandler().submit(() -> {
                run(sender, args);
            });
        } else {
            run(sender, args);
        }
    }

    private void run(ICommandSender sender, String[] args) {
        try {
            execute(sender, args);
        } catch (Throwable exception) {
            exception.printStackTrace();
            sender.addChatMessage(new ChatComponentText("Uncaught exception has been thrown while executing this command, contact the mod owner.")
                    .setChatStyle(new ChatStyle()
                            .setColor(EnumChatFormatting.RED)));
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return getName().compareTo(o.getCommandName());
    }
}
