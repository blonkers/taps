package fun.blonks.mixin;

import fun.blonks.Taps;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerEntity.class)
public abstract class VillagerDeathMixin {

	@Inject(at = @At("TAIL"), method = "onDeath")
	private void onDeath(DamageSource source, CallbackInfo ci) {
		VillagerEntity killed = (VillagerEntity)(Object)this;

		World world = killed.getEntityWorld();
		MinecraftServer server = world.getServer();

		// Client-side, do nothing
		if (world.isClient || server == null) return;

		VillagerData data = killed.getVillagerData();

		// Notify on all deaths, for pizzazz
		MutableText deathMessage = source.getDeathMessage(killed).copy();
		String job = data.getProfession() == VillagerProfession.NONE ? "jobless" : data.getProfession().toString();
		MutableText formattedJob = Text.literal(" (" + job + ")").formatted(Formatting.GOLD);
		MutableText postedMessage = deathMessage.append(formattedJob);
		server.getPlayerManager().broadcast(postedMessage, true);

		// Don't cry over spilled milk
		if (data.getLevel() <= 1) return;

		var pos = killed.getPos();
		if (pos != null) {
			world.playSound(
				null,
				pos.x,
				pos.y,
				pos.z,
				Taps.TAPS_EVENT,
				SoundCategory.AMBIENT,
				0.5f,
				1f
			);
		}
	}
}