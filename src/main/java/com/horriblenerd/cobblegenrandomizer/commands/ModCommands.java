package com.horriblenerd.cobblegenrandomizer.commands;

import com.horriblenerd.cobblegenrandomizer.CobbleGenRandomizer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

/**
 * Created by HorribleNerd on 05/09/2020
 */
public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> cmdHTweaks = dispatcher.register(
                Commands.literal(CobbleGenRandomizer.MODID)
                        .then(CommandReload.register(dispatcher))
        );

        dispatcher.register(Commands.literal("cgr").redirect(cmdHTweaks));

    }

}
