package net.linkle.valley.Registry.Cooking.FoodTypeBases;

import org.jetbrains.annotations.Nullable;

import net.linkle.valley.Registry.Utils.FoodStatusEffects;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class MilkBottleBase extends FoodItemBase {
    
    public MilkBottleBase(Settings settings) {
        super(settings);
    }
    
    public MilkBottleBase(Settings settings, int hunger, float saturationModifier, boolean isMeat,
            @Nullable FoodStatusEffects effects) {
        super(settings, hunger, saturationModifier, isMeat, effects);
    }

    public MilkBottleBase(Settings settings, int hunger, float saturationModifier, boolean isMeat) {
        super(settings, hunger, saturationModifier, isMeat);
    }

    public MilkBottleBase(Settings settings, int hunger, float saturationModifier, @Nullable FoodStatusEffects effects) {
        super(settings, hunger, saturationModifier, effects);
    }

    public MilkBottleBase(Settings settings, int hunger, float saturationModifier) {
        super(settings, hunger, saturationModifier);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;
        if (playerEntity instanceof ServerPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
        }

        if (!world.isClient) {
            user.clearStatusEffects();
        }

        if (playerEntity != null) {
            playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
            if (!playerEntity.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }

            if (playerEntity != null) {
                playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
