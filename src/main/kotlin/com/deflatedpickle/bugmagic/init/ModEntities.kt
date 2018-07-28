package com.deflatedpickle.bugmagic.init

import com.deflatedpickle.bugmagic.BugMagic
import com.deflatedpickle.bugmagic.Reference
import com.deflatedpickle.bugmagic.entity.mob.EntityFirefly
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityRegistry

object ModEntities {
    init {
        EntityRegistry.registerModEntity(ResourceLocation(Reference.MOD_ID + ":firefly"), EntityFirefly::class.java, "firefly", 0, BugMagic, 16, 1, false, 0x40362A, 0xFFEB66)
    }
}