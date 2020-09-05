package com.horriblenerd.cobblegenrandomizer.commands;

import com.horriblenerd.cobblegenrandomizer.CobbleGenRandomizer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class CommandReload implements Command<CommandSource> {

    private static final CommandReload CMD = new CommandReload();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("reload").executes(CMD);
    }

    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        boolean reload = CobbleGenRandomizer.GENERATORS.reload();
        StringTextComponent msg;
        if (reload) {
            msg = new StringTextComponent("Successfully loaded generators");
        }
        else {
            msg = new StringTextComponent("An error occured during generator loading");
            msg.func_240699_a_(TextFormatting.RED);
        }
        context.getSource().sendFeedback(msg, true);
        return 0;
    }

}
