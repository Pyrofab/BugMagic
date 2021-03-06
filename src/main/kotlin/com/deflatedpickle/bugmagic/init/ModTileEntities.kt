package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.tileentity.TileEntityAltar
import com.deflatedpickle.bugmagic.tileentity.TileEntityBugJar
import com.deflatedpickle.bugmagic.tileentity.TileEntityCauldron
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry

object ModTileEntities {
    init {
        GameRegistry.registerTileEntity(TileEntityBugJar::class.java, ResourceLocation(Reference.MOD_ID, "tileentity_bug_jar"))
        GameRegistry.registerTileEntity(TileEntityCauldron::class.java, ResourceLocation(Reference.MOD_ID, "tileentity_cauldron"))
        GameRegistry.registerTileEntity(TileEntityAltar::class.java, ResourceLocation(Reference.MOD_ID, "tileentity_altar"))
    }
}