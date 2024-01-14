package dev.tr3k.monstermod;

import dev.tr3k.monstermod.init.ItemInit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.GameData;

@Mod(MonsterMod.MODID)

public class MonsterMod {
	public static final String MODID = "monstermod";
	
	public MonsterMod() {
		
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();		
		
	    ItemInit.ITEMS.register(modEventBus);
	    modEventBus.addListener(this::setup);
	    
	    MinecraftForge.EVENT_BUS.register(this);
	    
	    
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		// adds monster original as a brewing recipe
		BrewingRecipeRegistry.addRecipe(Ingredient.of(ItemInit.EMPTY_CAN.get()), Ingredient.of(ItemInit.MONSTER_BLEND.get()), new ItemStack(ItemInit.MONSTER_ORIGINAL.get()));
    }
	
}
