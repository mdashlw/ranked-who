package ru.mdashlw.rankedwho.command

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler
import ru.mdashlw.rankedwho.RankedWho

abstract class Command : ICommand {
    abstract val aliases: List<String>
    open val async: Boolean = false

    fun register() {
        ClientCommandHandler.instance.registerCommand(this)
    }

    abstract fun execute(args: List<String>)

    override fun getCommandName(): String = aliases.first()

    override fun getCommandUsage(sender: ICommandSender?): String = ""

    override fun getCommandAliases(): MutableList<String> = aliases.drop(1).toMutableList()

    @Suppress("NAME_SHADOWING")
    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
        val args = args.toList()

        if (async) {
            GlobalScope.launch(RankedWho.singlePool) {
                execute(args)
            }
        } else {
            execute(args)
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean = true

    override fun addTabCompletionOptions(
        sender: ICommandSender?,
        args: Array<out String>?,
        pos: BlockPos?
    ): MutableList<String>? = null

    override fun isUsernameIndex(args: Array<out String>?, index: Int): Boolean = false

    override fun compareTo(other: ICommand): Int = commandName.compareTo(other.commandName)
}
