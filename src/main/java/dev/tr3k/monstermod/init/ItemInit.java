package dev.tr3k.monstermod.init;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dev.tr3k.monstermod.MonsterMod;
import net.minecraft.client.Game;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.*;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MonsterMod.MODID);
	
	final static int MONSTER_EFFECT_DURATION = 1200;
	final static int MONSTER_USE_DURATION = 20;
	
	public static class ModCreativeTab extends CreativeModeTab {
	    private ModCreativeTab(int index, String label) {
	        super(index, label);
	    }

	    @Override
	    public ItemStack makeIcon() {
	        return new ItemStack(MONSTER_ORIGINAL.get());
	    }
	    
	    public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "Monster Mod");
	}
	
	public static class DrinkableItem extends HoneyBottleItem {
		
		
		public DrinkableItem(Properties properties) {
			super(properties);
		}
		
		
		@Override
		public int getUseDuration(ItemStack i) {
			return MONSTER_USE_DURATION;
		}
		
		@Override
		public ItemStack finishUsingItem(ItemStack p_42984_, Level p_42985_, LivingEntity p_42986_) {
			// creates a consumer from player using a break event and then damages the used item in hand
			Consumer<LivingEntity> consumer = cons -> {
				p_42986_.broadcastBreakEvent(p_42986_.swingingArm);
				((Player)p_42986_).addItem(new ItemStack(ItemInit.EMPTY_CAN.get()));
				
			};
			p_42986_.getItemInHand(p_42986_.getUsedItemHand()).hurtAndBreak(10, p_42986_, consumer);
			// checks if the player has a status effect map and then applies the speed status effect when the monster is used
			if (p_42986_.getActiveEffectsMap() != null) { 
				
				List<MobEffectInstance> effectsToAdd = new ArrayList<MobEffectInstance>();
				
				for (MobEffectInstance currentEffect: p_42986_.getActiveEffects()) {
					if (currentEffect.getEffect() == MobEffects.MOVEMENT_SPEED) {
						
						effectsToAdd.add(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, currentEffect.getDuration(), currentEffect.getAmplifier() + 1));
						if (currentEffect.getAmplifier() >= 3) {
							effectsToAdd.add(new MobEffectInstance(MobEffects.NIGHT_VISION, currentEffect.getDuration(), 2));
						}
						if (currentEffect.getAmplifier() >= 5) {
							effectsToAdd.add(new MobEffectInstance(MobEffects.DARKNESS, currentEffect.getDuration(), 2));
						}
						if (currentEffect.getAmplifier() >= 8) {
							effectsToAdd.add(new MobEffectInstance(MobEffects.GLOWING, currentEffect.getDuration(), 2));
							effectsToAdd.add(new MobEffectInstance(MobEffects.INVISIBILITY, currentEffect.getDuration(), 2));
						}
					} else {
						effectsToAdd.add(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MONSTER_EFFECT_DURATION, 1));
					}
				}
				
				for (MobEffectInstance currentEffect: effectsToAdd) {
					p_42986_.addEffect(currentEffect);
				}

			}
			p_42986_.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MONSTER_EFFECT_DURATION, 1));
			return p_42984_;
		}
		
		@Override
		public UseAnim getUseAnimation(ItemStack p_42931_) {
			return UseAnim.DRINK;
		}
		
	}
	

	public static final RegistryObject<Item> MONSTER_ORIGINAL = ITEMS.register("monster_original", () -> 
		new DrinkableItem(new DrinkableItem.Properties().tab(ModCreativeTab.instance).defaultDurability(100)));
	

	public static final RegistryObject<Item> EMPTY_CAN = ITEMS.register("empty_can", () -> 
		new Item(new Item.Properties().tab(ModCreativeTab.instance)));
	

	public static final RegistryObject<Item> MONSTER_BLEND = ITEMS.register("monster_blend", () -> 
		new Item(new Item.Properties().tab(ModCreativeTab.instance)));

	//new Item.Properties().tab(ModCreativeTab.instance);
	
	

}
