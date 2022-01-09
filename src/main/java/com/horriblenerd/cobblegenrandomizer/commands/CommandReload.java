//package com.horriblenerd.cobblegenrandomizer.commands;
//
//import com.horriblenerd.cobblegenrandomizer.CobbleGenRandomizer;
//import com.mojang.brigadier.Command;
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.builder.ArgumentBuilder;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.minecraft.ChatFormatting;
//import net.minecraft.commands.CommandSource;
//import net.minecraft.commands.Commands;
//import net.minecraft.network.chat.TextComponent;
//
///**
// * Created by HorribleNerd on 05/09/2020
// */
//public class CommandReload implements Command<CommandSource> {
//
//    private static final CommandReload CMD = new CommandReload();
//
//    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
//        return Commands.literal("reload").executes(CMD);
//    }
//
//    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
//        boolean reload = CobbleGenRandomizer.GENERATORS.reload();
//        TextComponent msg;
//        if (reload) {
//            msg = new TextComponent("Successfully loaded generators");
//        }
//        else {
//            msg = new TextComponent("An error occured during generator loading");
//            msg.withStyle(ChatFormatting.RED);
//        }
//        context.getSource().sendSuccess(msg, true);
//        return 0;
//    }
//
//}
