package selim.geyser.items.bukkit;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;

import net.minecraft.server.v1_12_R1.Items;

public class GeyserItem {

	private String unlocalizedName;

	public String getUnlocalizedName(GeyserItemStack stack) {
		return this.unlocalizedName;
	}

	public void setUnlocalizedName(String unlocalizedName) {
		this.unlocalizedName = unlocalizedName;
	}

	public boolean onHitEntity(GeyserItemStack stack, LivingEntity hitEntity, LivingEntity attacker) {
		return false;
	}

	public boolean onBlockDestroyed(GeyserItemStack stack, Location pos, BlockState state,
			LivingEntity entityLiving) {
		return false;
	}

	public boolean hasEffect(GeyserItemStack stack) {
//		return stack.isItemEnchanted();
		return false;
	}

}
