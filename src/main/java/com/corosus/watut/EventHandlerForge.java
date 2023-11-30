package com.corosus.watut;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Watut.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandlerForge {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void guiRender(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof ChatScreen && mc.getConnection() != null) {
            ChatScreen chat = (ChatScreen) mc.screen;
            GuiGraphics guigraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
            int height = chat.height + 26;
            guigraphics.drawString(mc.font, Watut.getPlayerStatusManagerClient().getTypingPlayers(), 2, height - 50, 16777215);
            guigraphics.flush();
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onMouse(InputEvent.MouseButton.Post event) {
        Watut.getPlayerStatusManagerClient().onMouse(event);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onMouse(InputEvent.Key event) {
        Watut.getPlayerStatusManagerClient().onKey(event);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onGameTick(TickEvent.ClientTickEvent event) {
        Watut.getPlayerStatusManagerClient().tickGame(event);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.player.level().isClientSide()) {
                Watut.getPlayerStatusManagerClient().tickPlayer(event.player);
            } else {
                Watut.getPlayerStatusManagerServer().tickPlayer(event.player);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Watut.getPlayerStatusManagerServer().playerLoggedIn(event.getEntity());
    }
}
