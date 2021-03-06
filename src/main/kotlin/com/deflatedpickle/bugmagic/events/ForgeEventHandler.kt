package com.deflatedpickle.bugmagic.events

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.items.ItemWand
import com.deflatedpickle.bugmagic.util.BugUtil
import com.deflatedpickle.bugmagic.util.SpellUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.client.event.MouseEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.commons.lang3.RandomUtils

class ForgeEventHandler {
    var tickCounter = 0
    val drainMap: MutableMap<String, MutableList<Int>> = mutableMapOf()

    @SubscribeEvent
    @SideOnly(value = Side.CLIENT)
    fun onRenderGameOverlayEvent(event: RenderGameOverlayEvent) {
        val player = Minecraft.getMinecraft().player

        if (player.heldItemMainhand.item is ItemWand) {
            Minecraft.getMinecraft().fontRenderer.drawString("%sBug Power: %d/%d".format(TextFormatting.WHITE,
                    BugUtil.getBugPower(player), BugUtil.getMaxBugPower(player)),
                    5, ScaledResolution(Minecraft.getMinecraft()).scaledHeight - 15, ScaledResolution(Minecraft.getMinecraft()).scaledWidth - 5)
        }
    }

    @SubscribeEvent
    fun onEntityJoinWorldEvent(event: EntityJoinWorldEvent) {
        if (event.entity is EntityPlayer) {
            val player = event.entity as EntityPlayer
            val playerData = player.entityData

            playerData.setTag("bugmagic.spells", NBTTagCompound())
            playerData.setTag("bugmagic.casted", NBTTagCompound())
            // playerData.setTag("bugmagic.drain", NBTTagCompound())

            if (event.entity.world.isRemote) {
                // Sets the initial Bug Power value
                playerData.setBoolean("bugmagic.initPlayer", true)
                BugUtil.setBugPower(player, 30)
                BugUtil.setMaxBugPower(player, RandomUtils.nextInt(40, 80))
                // playerData.setTag("bugmagic.spells", NBTTagCompound())
            }
        }
    }

    @SubscribeEvent
    fun onPlayerLoggedOutEvent(event: PlayerEvent.PlayerLoggedOutEvent) {
        // TODO: Save somewhere on the client side to kill on relog, as entities are killed a tick after the world has been unloaded
        SpellUtil.uncastAllSpells(event.player)
    }

    @SubscribeEvent
    fun onPlayerTickEvent(event: TickEvent.PlayerTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.side == Side.CLIENT) {
                tickCounter++

                if (tickCounter == 240) {
                    tickCounter = 0
                    BugUtil.giveCappedBugPower(event.player, 1)
                }

                // Drain the players bug power
                // TODO: Fix this? Might get laggy
                // TODO: Move this to NBT
                for (i in SpellUtil.getCastedSpells(event.player)) {
                    for (n in 0 until i.second) {
                        if (!drainMap.containsKey(i.first)) {
                            drainMap[i.first] = MutableList(i.second) { SpellUtil.spellMap[i.first]!!.drainWait }
                        }
                        else if (drainMap[i.first]!!.size != i.second) {
                            // TODO: Make this use the current wait values for casted spells instead of resetting all of them
                            drainMap[i.first] = MutableList(i.second) { SpellUtil.spellMap[i.first]!!.drainWait }
                        }
                        else {
                            drainMap[i.first]!![n] = drainMap[i.first]!![n] - 1
                        }

                        if (drainMap[i.first]!![n] <= 0) {
                            BugUtil.useCappedBugPower(event.player, SpellUtil.spellMap[i.first]!!.drain)
                            drainMap[i.first]!![n] = SpellUtil.spellMap[i.first]!!.drainWait
                        }
                    }
                }

                // Uncast all spells when power runs out
                if (BugUtil.getBugPower(event.player) <= 0) {
                    for (i in SpellUtil.getCastedSpells(event.player)) {
                        SpellUtil.spellMap[i.first]!!.uncast()
                    }
                    drainMap.clear()
                }
            }
        }
    }

    @SubscribeEvent
    fun onMouseEvent(event: MouseEvent) {
        val player = BugMagic.proxy.getPlayer()!!

        if (player.isSneaking) {
            if (player.heldItemMainhand.item is ItemWand) {
                (player.heldItemMainhand.item as ItemWand).onMouseEvent()

                event.isCanceled = true
            }
        }
    }
}